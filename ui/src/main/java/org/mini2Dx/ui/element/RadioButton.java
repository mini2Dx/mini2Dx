/**
 * Copyright (c) 2017 See AUTHORS file
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.event.ActionEvent;
import org.mini2Dx.ui.event.ActionEventPool;
import org.mini2Dx.ui.event.EventTrigger;
import org.mini2Dx.ui.event.params.EventTriggerParams;
import org.mini2Dx.ui.layout.FlexDirection;
import org.mini2Dx.ui.listener.ActionListener;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.RadioButtonRenderNode;

/**
 * A responsive radio buttons UI element
 */
public class RadioButton extends UiElement implements Actionable {
	@Field
	private final List<String> options = new ArrayList<String>(2);
	@Field(optional = true)
	private String defaultSelectedOption = null;
	@Field(optional = true)
	private boolean enabled = true;
	@Field(optional = true)
	private boolean responsive = false;
	@Field(optional=true)
	private FlexDirection flexDirection = FlexDirection.ROW;
	
	private List<ActionListener> actionListeners;
	private RadioButtonRenderNode renderNode;
	private int selectedOptionIndex = -1;
	
	/**
	 * Constructor. Generates a unique ID for this {@link RadioButton}
	 */
	public RadioButton() {
		this(null);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            The unique ID for this {@link RadioButton}
	 */
	public RadioButton(@ConstructorArg(clazz = String.class, name = "id") String id) {
		super(id);
	}

	@Override
	public void attach(ParentRenderNode<?, ?> parentRenderNode) {
		if (renderNode != null) {
			return;
		}
		renderNode = new RadioButtonRenderNode(parentRenderNode, this);
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
	public void notifyActionListenersOfBeginEvent(EventTrigger eventTrigger, EventTriggerParams eventTriggerParams) {
		if(actionListeners == null) {
			return;
		}
		ActionEvent event = ActionEventPool.allocate();
		event.set(this, eventTrigger, eventTriggerParams);
		for(int i = actionListeners.size() - 1; i >= 0; i--) {
			actionListeners.get(i).onActionBegin(event);
		}
		ActionEventPool.release(event);
	}
	
	@Override
	public void notifyActionListenersOfEndEvent(EventTrigger eventTrigger, EventTriggerParams eventTriggerParams) {
		if(actionListeners == null) {
			return;
		}
		ActionEvent event = ActionEventPool.allocate();
		event.set(this, eventTrigger, eventTriggerParams);
		for(int i = actionListeners.size() - 1; i >= 0; i--) {
			actionListeners.get(i).onActionEnd(event);
		}
		ActionEventPool.release(event);
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
	public void defer(Runnable runnable) {
		deferredQueue.offer(runnable);
	}
	
	@Override
	public void syncWithRenderNode() {
		while (!effects.isEmpty()) {
			renderNode.applyEffect(effects.poll());
		}
		while (!deferredQueue.isEmpty()) {
			deferredQueue.poll().run();
		}
	}

	@Override
	public void setZIndex(int zIndex) {
		this.zIndex = zIndex;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public void addActionListener(ActionListener listener) {
		if(actionListeners == null) {
			actionListeners = new ArrayList<ActionListener>(1);
		}
		actionListeners.add(listener);
	}

	@Override
	public void removeActionListener(ActionListener listener) {
		if(actionListeners == null) {
			return;
		}
		actionListeners.remove(listener);
	}

	/**
	 * Returns the {@link FlexDirection} for rendering the buttons
	 * @return {@link FlexDirection#ROW} by default
	 */
	public FlexDirection getFlexDirection() {
		return flexDirection;
	}

	/**
	 * Sets the {@link FlexDirection} for the buttons rendering
	 * @param flexDirection The {@link FlexDirection} to set
	 */
	public void setFlexDirection(FlexDirection flexDirection) {
		if(flexDirection == null) {
			return;
		}
		if(this.flexDirection.equals(flexDirection)) {
			return;
		}
		this.flexDirection = flexDirection;
		
		if (renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}
	
	public boolean isResponsive() {
		return responsive;
	}

	public void setResponsive(boolean responsive) {
		if(this.responsive == responsive) {
			return;
		}
		this.responsive = responsive;
		
		if (renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}

	public void addOption(String option) {
		options.add(option);
		
		if (renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}
	
	public void removeOption(String option) {
		options.remove(option);
		
		if (renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}
	
	public String getOption(int index) {
		return options.get(index);
	}
	
	public Iterator<String> getOptions() {
		return options.iterator();
	}
	
	public int getTotalOptions() {
		return options.size();
	}

	public int getSelectedOptionIndex() {
		if(selectedOptionIndex < 0) {
			if(defaultSelectedOption != null && options.contains(defaultSelectedOption)) {
				selectedOptionIndex = options.indexOf(defaultSelectedOption);
			} else {
				selectedOptionIndex = 0;
			}
		}
		return selectedOptionIndex;
	}

	public void setSelectedOptionIndex(int selectedOptionIndex) {
		if(selectedOptionIndex < 0) {
			return;
		}
		if(selectedOptionIndex >= options.size()) {
			return;
		}
		this.selectedOptionIndex = selectedOptionIndex;
	}

	public String getSelectedOption() {
		return options.get(getSelectedOptionIndex());
	}

	public void setSelectedOption(String selectedOption) {
		int index = options.indexOf(selectedOption);
		if(index < 0) {
			if(defaultSelectedOption != null && options.contains(defaultSelectedOption)) {
				selectedOptionIndex = options.indexOf(defaultSelectedOption);
			} else {
				selectedOptionIndex = 0;
			}
		} else {
			selectedOptionIndex = index;
		}
	}
	
	public void selectNextOption() {
		int selectedOption = getSelectedOptionIndex();
		selectedOption++;
		selectedOptionIndex = selectedOption % options.size();
	}
	
	public void selectPreviousOption() {
		int selectedOption = getSelectedOptionIndex();
		selectedOption--;
		if(selectedOption < 0) {
			selectedOptionIndex = options.size() - 1;
		} else {
			selectedOptionIndex = selectedOption;
		}
	}
}
