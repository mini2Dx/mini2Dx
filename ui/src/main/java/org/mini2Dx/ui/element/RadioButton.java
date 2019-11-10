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

import java.util.Iterator;

import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.event.ActionEvent;
import org.mini2Dx.ui.event.ActionEventPool;
import org.mini2Dx.ui.event.EventTrigger;
import org.mini2Dx.ui.event.params.EventTriggerParams;
import org.mini2Dx.ui.layout.FlexDirection;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.listener.ActionListener;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.RadioButtonRenderNode;
import org.mini2Dx.ui.render.UiContainerRenderTree;
import org.mini2Dx.ui.style.StyleRule;

/**
 * A responsive radio buttons UI element
 */
public class RadioButton extends UiElement implements Actionable {
	@Field(optional=true)
	private final Array<String> options = new Array<String>(true,2, String.class);
	@Field(optional = true)
	private String defaultSelectedOption = null;
	@Field(optional = true)
	private boolean enabled = true;
	@Field(optional = true)
	private boolean responsive = false;
	@Field(optional=true)
	private FlexDirection flexDirection = FlexDirection.ROW;
	
	private Array<ActionListener> actionListeners;
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
		this(id, 0f, 0f, 300f, 64f);
	}

	/**
	 * Constructor
	 * @param x The x coordinate of this element relative to its parent
	 * @param y The y coordinate of this element relative to its parent
	 * @param width The width of this element
	 * @param height The height of this element
	 */
	public RadioButton(@ConstructorArg(clazz = Float.class, name = "x") float x,
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
	public RadioButton(@ConstructorArg(clazz = String.class, name = "id") String id,
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
		renderNode = new RadioButtonRenderNode(parentRenderNode, this);
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
	public void notifyActionListenersOfBeginEvent(EventTrigger eventTrigger, EventTriggerParams eventTriggerParams) {
		if(actionListeners == null) {
			return;
		}
		ActionEvent event = ActionEventPool.allocate();
		event.set(this, eventTrigger, eventTriggerParams);
		for(int i = actionListeners.size - 1; i >= 0; i--) {
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
		for(int i = actionListeners.size - 1; i >= 0; i--) {
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

	@Override
	public void setZIndex(int zIndex) {
		this.zIndex = zIndex;

		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
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
			actionListeners = new Array<ActionListener>(true, 1, ActionListener.class);
		}
		actionListeners.add(listener);
	}

	@Override
	public void removeActionListener(ActionListener listener) {
		if(actionListeners == null) {
			return;
		}
		actionListeners.removeValue(listener, false);
	}

	@Override
	public StyleRule getStyleRule() {
		if(!UiContainer.isThemeApplied()) {
			return null;
		}
		return UiContainer.getTheme().getRadioButtonStyleRule(styleId, ScreenSize.XS);
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
		if(flexDirection.equals(FlexDirection.CENTER)) {
			throw new MdxException(FlexDirection.CENTER + " is not supported for RadioButton");
		}
		this.flexDirection = flexDirection;
		
		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
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
		renderNode.setDirty();
	}

	public void addOption(String option) {
		options.add(option);
		
		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}
	
	public void removeOption(String option) {
		options.removeValue(option, false);
		
		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}
	
	public String getOption(int index) {
		return options.get(index);
	}
	
	public Iterator<String> getOptions() {
		return options.iterator();
	}
	
	public int getTotalOptions() {
		return options.size;
	}

	public int getSelectedOptionIndex() {
		if(selectedOptionIndex < 0) {
			if(defaultSelectedOption != null && options.contains(defaultSelectedOption, false)) {
				selectedOptionIndex = options.indexOf(defaultSelectedOption, false);
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
		if(selectedOptionIndex >= options.size) {
			return;
		}
		this.selectedOptionIndex = selectedOptionIndex;
	}

	public String getSelectedOption() {
		return options.get(getSelectedOptionIndex());
	}

	public void setSelectedOption(String selectedOption) {
		int index = options.indexOf(selectedOption, false);
		if(index < 0) {
			if(defaultSelectedOption != null && options.contains(defaultSelectedOption, false)) {
				selectedOptionIndex = options.indexOf(defaultSelectedOption, false);
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
		selectedOptionIndex = selectedOption % options.size;
	}
	
	public void selectPreviousOption() {
		int selectedOption = getSelectedOptionIndex();
		selectedOption--;
		if(selectedOption < 0) {
			selectedOptionIndex = options.size - 1;
		} else {
			selectedOptionIndex = selectedOption;
		}
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
}
