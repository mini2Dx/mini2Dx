/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.ui.element;

import java.util.ArrayList;
import java.util.List;

import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.event.ActionEvent;
import org.mini2Dx.ui.event.ActionEventPool;
import org.mini2Dx.ui.event.EventTrigger;
import org.mini2Dx.ui.event.params.EventTriggerParams;
import org.mini2Dx.ui.layout.LayoutRuleset;
import org.mini2Dx.ui.listener.ActionListener;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.SelectRenderNode;

import com.badlogic.gdx.graphics.Color;

/**
 * A {@link UiElement} with preset options. Uses left/right buttons to change
 * the selection.
 */
public class Select<V> extends UiElement implements Actionable {
	private final List<SelectOption<V>> options = new ArrayList<SelectOption<V>>(1);
	private List<ActionListener> actionListeners;

	private LayoutRuleset layout = LayoutRuleset.DEFAULT_RULESET;
	private boolean enabled = true;
	private int selectedIndex = 0;
	private Color enabledTextColor = null;
	private Color disabledTextColor = null;
	
	@Field(optional=true)
	private String leftButtonText;
	@Field(optional=true)
	private String rightButtonText;

	protected SelectRenderNode renderNode;
	
	/**
	 * Constructor. Generates a unique ID for this {@link Select}
	 */
	public Select() {
		this(null);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            The unique ID for this {@link Select}
	 */
	public Select(@ConstructorArg(clazz = String.class, name = "id") String id) {
		super(id);
	}

	@Override
	public void attach(ParentRenderNode<?, ?> parentRenderNode) {
		if (renderNode != null) {
			return;
		}
		renderNode = new SelectRenderNode(parentRenderNode, this);
		parentRenderNode.addChild(renderNode);
	}

	@Override
	public void detach(ParentRenderNode<?, ?> parentRenderNode) {
		if (renderNode == null) {
			return;
		}
		parentRenderNode.removeChild(renderNode);
		renderNode = null;
	}

	@Override
	public void setVisibility(Visibility visibility) {
		if (this.visibility == visibility) {
			return;
		}
		this.visibility = visibility;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}

	@Override
	public void setStyleId(String styleId) {
		if (styleId == null) {
			return;
		}
		if (this.styleId.equals(styleId)) {
			return;
		}
		this.styleId = styleId;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}
	
	@Override
	public void syncWithRenderNode() {
		while (!effects.isEmpty()) {
			renderNode.applyEffect(effects.poll());
		}
		processDeferred();
	}

	/**
	 * Adds a selectable option to this {@link Select}
	 * 
	 * @param label
	 *            The visible text of the option
	 * @param value
	 *            The value of the option
	 * @return The {@link SelectOption} that was generated
	 */
	public SelectOption<V> addOption(String label, V value) {
		SelectOption<V> option = new SelectOption<V>(label, value);
		options.add(option);
		return option;
	}

	/**
	 * Adds a selectable option to this {@link Select}
	 * 
	 * @param option
	 *            The {@link SelectOption} to add
	 */
	public void addOption(SelectOption<V> option) {
		options.add(option);
	}

	/**
	 * Removes a selectable option from this {@link Select}
	 * 
	 * @param option
	 *            The {@link SelectOption} to remove
	 */
	public void removeOption(SelectOption<V> option) {
		options.remove(option);
	}

	/**
	 * Removes a selectable option from this {@link Select} based on the
	 * option's label
	 * 
	 * @param label
	 *            The label to search for
	 * @return True if the option was found
	 */
	public boolean removeOptionByLabel(String label) {
		for (int i = 0; i < options.size(); i++) {
			if (options.get(i).getLabel().equals(label)) {
				options.remove(i);
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes a selectable option from this {@link Select} based on the
	 * option's value
	 * 
	 * @param value
	 *            The value to search for
	 * @return True if the option was found
	 */
	public boolean removeOptionByValue(V value) {
		for (int i = 0; i < options.size(); i++) {
			if (options.get(i).getValue().equals(value)) {
				options.remove(i);
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the currently selected option
	 * 
	 * @return The selected {@link SelectOption}
	 */
	public SelectOption<V> getSelectedOption() {
		return options.get(selectedIndex);
	}

	/**
	 * Returns the label of the currently selected option
	 * 
	 * @return The text that the player sees
	 */
	public String getSelectedLabel() {
		return options.get(selectedIndex).getLabel();
	}

	/**
	 * Returns the value of the currently selected option
	 * 
	 * @return The selected value
	 */
	public V getSelectedValue() {
		return options.get(selectedIndex).getValue();
	}

	/**
	 * Returns the total amount of options
	 * 
	 * @return 0 if no options have been added
	 */
	public int getTotalOptions() {
		return options.size();
	}

	/**
	 * Returns the index of the currently selected option
	 * 
	 * @return 0 by default
	 */
	public int getSelectedIndex() {
		return selectedIndex;
	}

	/**
	 * Sets the index of the currently selected option
	 * 
	 * @param index
	 *            The index to set as selected
	 */
	public void setSelectedIndex(int index) {
		this.selectedIndex = index;
	}

	/**
	 * Changes the current selection to the next available option. If the
	 * current selection is the last option then this method does nothing.
	 */
	public void nextOption() {
		if (selectedIndex >= options.size() - 1) {
			return;
		}
		selectedIndex++;
	}

	/**
	 * Changes the current selection to the option before the currently selected
	 * option. If this current selection is the first option then this method
	 * does nothing.
	 */
	public void previousOption() {
		if (selectedIndex <= 0) {
			return;
		}
		selectedIndex--;
	}

	@Override
	public void notifyActionListenersOfBeginEvent(EventTrigger eventTrigger, EventTriggerParams eventTriggerParams) {
		if (actionListeners == null) {
			return;
		}
		ActionEvent event = ActionEventPool.allocate();
		event.set(this, eventTrigger, eventTriggerParams);
		for (int i = actionListeners.size() - 1; i >= 0; i--) {
			actionListeners.get(i).onActionBegin(event);
		}
		ActionEventPool.release(event);
	}

	@Override
	public void notifyActionListenersOfEndEvent(EventTrigger eventTrigger, EventTriggerParams eventTriggerParams) {
		if (actionListeners == null) {
			return;
		}
		ActionEvent event = ActionEventPool.allocate();
		event.set(this, eventTrigger, eventTriggerParams);
		for (int i = actionListeners.size() - 1; i >= 0; i--) {
			actionListeners.get(i).onActionEnd(event);
		}
		ActionEventPool.release(event);
	}

	@Override
	public void addActionListener(ActionListener listener) {
		if (actionListeners == null) {
			actionListeners = new ArrayList<ActionListener>(1);
		}
		actionListeners.add(listener);
	}

	@Override
	public void removeActionListener(ActionListener listener) {
		if (actionListeners == null) {
			return;
		}
		actionListeners.remove(listener);
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}

	@Override
	public void setZIndex(int zIndex) {
		this.zIndex = zIndex;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}

	public LayoutRuleset getLayout() {
		return layout;
	}
	
	public void setLayout(String layoutRuleset) {
		setLayout(new LayoutRuleset(layoutRuleset));
	}

	public void setLayout(LayoutRuleset layoutRuleset) {
		if (layoutRuleset == null) {
			return;
		}
		this.layout = layoutRuleset;
		if (renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}

	public Color getEnabledTextColor() {
		return enabledTextColor;
	}

	public void setEnabledTextColor(Color enabledTextColor) {
		this.enabledTextColor = enabledTextColor;
	}

	public Color getDisabledTextColor() {
		return disabledTextColor;
	}

	public void setDisabledTextColor(Color disabledTextColor) {
		this.disabledTextColor = disabledTextColor;
	}

	public String getLeftButtonText() {
		return leftButtonText;
	}

	public void setLeftButtonText(String leftButtonText) {
		this.leftButtonText = leftButtonText;
	}

	public String getRightButtonText() {
		return rightButtonText;
	}

	public void setRightButtonText(String rightButtonText) {
		this.rightButtonText = rightButtonText;
	}
}
