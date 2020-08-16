/*******************************************************************************
 * Copyright 2019 See AUTHORS file
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.mini2Dx.ui.element;

import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.event.ActionEvent;
import org.mini2Dx.ui.event.ActionEventPool;
import org.mini2Dx.ui.event.EventTrigger;
import org.mini2Dx.ui.event.params.EventTriggerParams;
import org.mini2Dx.ui.layout.FlexLayoutRuleset;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.listener.ActionListener;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.SelectRenderNode;
import org.mini2Dx.ui.render.UiContainerRenderTree;
import org.mini2Dx.ui.style.StyleRule;

/**
 * A {@link UiElement} with preset options. Uses left/right buttons to change
 * the selection.
 */
public class Select<V> extends UiElement implements Actionable, FlexUiElement {
	private final Array<SelectOption<V>> options = new Array<SelectOption<V>>(true,1, SelectOption.class);
	private Array<ActionListener> actionListeners;

	private int selectedIndex = 0;
	private boolean wrapSelectedIndex = true;
	private Color enabledTextColor = null;
	private Color disabledTextColor = null;

	@Field(optional = true)
	private boolean enabled = true;
	@Field(optional = true)
	private String flexLayout = null;
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
		this(id, 0f, 0f, 300f, 64f);
	}

	/**
	 * Constructor
	 * @param x The x coordinate of this element relative to its parent
	 * @param y The y coordinate of this element relative to its parent
	 * @param width The width of this element
	 * @param height The height of this element
	 */
	public Select(@ConstructorArg(clazz = Float.class, name = "x") float x,
						   @ConstructorArg(clazz = Float.class, name = "y") float y,
						   @ConstructorArg(clazz = Float.class, name = "width") float width,
						   @ConstructorArg(clazz = Float.class, name = "height") float height) {
		this(null, x, y, width, height);
	}

	/**
	 * Constructor
	 * @param id The unique ID for this element (if null an ID will be generated)
	 * @param x The x coordinate of this element relative to its parent
	 * @param y The y coordinate of this element relative to its parent
	 * @param width The width of this element
	 * @param height The height of this element
	 */
	public Select(@ConstructorArg(clazz = String.class, name = "id") String id,
					   @ConstructorArg(clazz = Float.class, name = "x") float x,
					   @ConstructorArg(clazz = Float.class, name = "y") float y,
					   @ConstructorArg(clazz = Float.class, name = "width") float width,
					   @ConstructorArg(clazz = Float.class, name = "height") float height) {
		super(id, x, y, width, height);
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
		renderNode.dispose();
		renderNode = null;
	}

	@Override
	public void invokeBeginHover() {
		if(renderNode == null) {
			return;
		}
		renderNode.beginFakeHover();
	}

	@Override
	public void invokeEndHover() {
		if(renderNode == null) {
			return;
		}
		renderNode.endFakeHover();
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
		renderNode.setDirty();
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
		renderNode.setDirty();
	}
	
	@Override
	public void syncWithUpdate(UiContainerRenderTree rootNode) {
		while (effects.size > 0) {
			renderNode.applyEffect(effects.removeFirst());
		}
		super.syncWithUpdate(rootNode);
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
		options.removeValue(option, false);
	}

	/**
	 * Clears all options
	 */
	public void clearOptions() {
		options.clear();
		selectedIndex = 0;
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
		for (int i = 0; i < options.size; i++) {
			if (options.get(i).getLabel().equals(label)) {
				options.removeIndex(i);
				selectedIndex = MathUtils.clamp(selectedIndex, 0, options.size -1);
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
		for (int i = 0; i < options.size; i++) {
			if (options.get(i).getValue().equals(value)) {
				options.removeIndex(i);
				selectedIndex = MathUtils.clamp(selectedIndex, 0, options.size -1);
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the option at the specified index
	 * @param index The index to get
	 * @return The {@link SelectOption} instance
	 */
	public SelectOption<V> getOption(int index) {
		return options.get(index);
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
		return options.size;
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
	 * current selection is the last option then this method
	 * does nothing if {@link #wrapSelectedIndex} is false.
	 */
	public void nextOption() {
		if (selectedIndex >= options.size - 1) {
			if(wrapSelectedIndex) {
				selectedIndex = 0;
			}
			return;
		}
		selectedIndex++;
	}

	/**
	 * Changes the current selection to the option before the currently selected
	 * option. If this current selection is the first option then this method
	 * does nothing if {@link #wrapSelectedIndex} is false.
	 */
	public void previousOption() {
		if (selectedIndex <= 0) {
			if(wrapSelectedIndex) {
				selectedIndex = options.size - 1;
			}
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
		for (int i = actionListeners.size - 1; i >= 0; i--) {
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
		for (int i = actionListeners.size - 1; i >= 0; i--) {
			actionListeners.get(i).onActionEnd(event);
		}
		ActionEventPool.release(event);
	}

	@Override
	public void addActionListener(ActionListener listener) {
		if (actionListeners == null) {
			actionListeners = new Array<ActionListener>(true,1, ActionListener.class);
		}
		actionListeners.add(listener);
	}

	@Override
	public void removeActionListener(ActionListener listener) {
		if (actionListeners == null) {
			return;
		}
		actionListeners.removeValue(listener, false);
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
		renderNode.setDirty();
	}

	@Override
	public void setZIndex(int zIndex) {
		this.zIndex = zIndex;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	@Override
	public StyleRule getStyleRule() {
		if(!UiContainer.isThemeApplied()) {
			return null;
		}
		return UiContainer.getTheme().getSelectStyleRule(styleId, ScreenSize.XS);
	}

	public String getFlexLayout() {
		return flexLayout;
	}

	public void setFlexLayout(String flexLayout) {
		if(flexLayout == null) {
			return;
		}
		if(this.flexLayout != null && this.flexLayout.equals(flexLayout)) {
			return;
		}
		this.flexLayout = flexLayout;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
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

	@Override
	public boolean isRenderNodeDirty() {
		if (renderNode == null) {
			return true;
		}
		return renderNode.isDirty();
	}

	@Override
	public void setRenderNodeDirty() {
		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	@Override
	public boolean isInitialLayoutOccurred() {
		if (renderNode == null) {
			return false;
		}
		return renderNode.isInitialLayoutOccurred();
	}

	@Override
	public boolean isInitialUpdateOccurred() {
		if(renderNode == null) {
			return false;
		}
		return renderNode.isInitialUpdateOccurred();
	}

	@Override
	public int getRenderX() {
		if(renderNode == null) {
			return Integer.MIN_VALUE;
		}
		return renderNode.getOuterRenderX();
	}

	@Override
	public int getRenderY() {
		if(renderNode == null) {
			return Integer.MIN_VALUE;
		}
		return renderNode.getOuterRenderY();
	}

	@Override
	public int getRenderWidth() {
		if(renderNode == null) {
			return -1;
		}
		return renderNode.getOuterRenderWidth();
	}

	@Override
	public int getRenderHeight() {
		if(renderNode == null) {
			return -1;
		}
		return renderNode.getOuterRenderHeight();
	}

	@Override
	public boolean isFlexLayout() {
		return flexLayout != null;
	}

	/**
	 * Equivalent to {@link #set(float, float, float, float)} for usage with flex layout
	 * @param x The x coordinate (in pixels) relative to its parent
	 * @param y The y coordinate (in pixels) relative to its parent
	 * @param width The width in pixels
	 * @param height The height in pixels
	 */
	public void setFlex(float x, float y, float width, float height) {
		flexLayout = FlexLayoutRuleset.set(flexLayout, x, y, width, height);
	}

	/**
	 * Equivalent to {@link #setXY(float, float)} for usage with flex layout
	 * @param x The x coordinate (in pixels) relative to its parent
	 * @param y The y coordinate (in pixels) relative to its parent
	 */
	public void setXYFlex(float x, float y) {
		flexLayout = FlexLayoutRuleset.setXY(flexLayout, x, y);
	}

	/**
	 * Equivalent to {@link #setX(float)} for usage with flex layout
	 * @param x The x coordinate (in pixels) relative to its parent
	 */
	public void setXFlex(float x) {
		flexLayout = FlexLayoutRuleset.setX(flexLayout, x);
	}

	/**
	 * Equivalent to {@link #setY(float)} for usage with flex layout
	 * @param y The y coordinate (in pixels) relative to its parent
	 */
	public void setYFlex(float y) {
		flexLayout = FlexLayoutRuleset.setY(flexLayout, y);
	}

	/**
	 * Equivalent to {@link #setWidth(float)} for usage with flex layout
	 * @param width The width in pixels
	 */
	public void setWidthFlex(float width) {
		flexLayout = FlexLayoutRuleset.setWidth(flexLayout, width);
	}

	/**
	 * Equivalent to {@link #setHeight(float)} for usage with flex layout
	 * @param height The height in pixels
	 */
	public void setHeightFlex(float height) {
		flexLayout = FlexLayoutRuleset.setHeight(flexLayout, height);
	}

	/**
	 * True if the index will wrap around if the player keeps pressing one of the buttons
	 * @return
	 */
	public boolean isWrapSelectedIndex() {
		return wrapSelectedIndex;
	}

	/**
	 * Sets if the index will wrap around if the player keeps pressing one of the buttons
	 * @param wrapSelectedIndex
	 */
	public void setWrapSelectedIndex(boolean wrapSelectedIndex) {
		this.wrapSelectedIndex = wrapSelectedIndex;
	}
}
