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

import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.event.ActionEvent;
import org.mini2Dx.ui.event.ActionEventPool;
import org.mini2Dx.ui.event.EventTrigger;
import org.mini2Dx.ui.event.params.EventTriggerParams;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.listener.ActionListener;
import org.mini2Dx.ui.render.CustomUiElementRenderNode;
import org.mini2Dx.ui.render.ImageRenderNode;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.UiContainerRenderTree;
import org.mini2Dx.ui.style.StyleRule;

/**
 * Base class for implementing custom {@link UiElement}s
 */
public abstract class CustomUiElement extends UiElement implements Actionable {
	private Array<ActionListener> actionListeners;
	private CustomUiElementRenderNode renderNode;

	@Field(optional=true)
	private boolean enabled = true;

	/**
	 * Constructor. Generates a unique ID for this {@link Checkbox}
	 */
	public CustomUiElement() {
		this(null);
	}

	/**
	 * Constructor
	 *
	 * @param id
	 *            The unique ID for this {@link Checkbox}
	 */
	public CustomUiElement(@ConstructorArg(clazz = String.class, name = "id") String id) {
		this(id, 0f, 0f, 20f, 20f);
	}

	/**
	 * Constructor
	 * @param x The x coordinate of this element relative to its parent
	 * @param y The y coordinate of this element relative to its parent
	 * @param width The width of this element
	 * @param height The height of this element
	 */
	public CustomUiElement(@ConstructorArg(clazz = Float.class, name = "x") float x,
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
	public CustomUiElement(@ConstructorArg(clazz = String.class, name = "id") String id,
					@ConstructorArg(clazz = Float.class, name = "x") float x,
					@ConstructorArg(clazz = Float.class, name = "y") float y,
					@ConstructorArg(clazz = Float.class, name = "width") float width,
					@ConstructorArg(clazz = Float.class, name = "height") float height) {
		super(id, x, y, width, height);
	}

	/**
	 * Update the UI state
	 * @param uiContainer The root {@link UiElement}
	 * @param delta The time since the last update
	 */
	public abstract void update(UiContainerRenderTree uiContainer, float delta);

	/**
	 * Interpoalte the UI state
	 * @param alpha
	 */
	public abstract void interpolate(float alpha);

	/**
	 * Render the {@link UiElement}
	 * @param g The {@link Graphics} context
	 * @param renderNode The {@link org.mini2Dx.ui.render.RenderNode} detailing where to render the element
	 */
	public abstract void render(Graphics g, CustomUiElementRenderNode renderNode);

	/**
	 * An overridable method that is called when the mouse moves
	 * @param mouseX The mouse screen X coordinate
	 * @param mouseY The mouse screen Y coordinate
	 */
	public void onMouseMoved(int mouseX, int mouseY) {}

	/**
	 * An overridable method that is called when the mouse/touch is pressed down on this element
	 * @param mouseX The mouse/touch screen X coordinate
	 * @param mouseY The mouse/touch screen Y coordinate
	 * @param pointer The pointer ID
	 * @param button The button ID (see Input.buttons)
	 */
	public void onMouseDown(int mouseX, int mouseY, int pointer, int button) {}

	/**
	 * An overridable method that is called when the mouse/touch is released after being pressed down on this element
	 * @param mouseX The mouse/touch screen X coordinate
	 * @param mouseY The mouse/touch screen Y coordinate
	 * @param pointer The pointer ID
	 * @param button The button ID (see Input.buttons)
	 */
	public void onMouseUp(int mouseX, int mouseY, int pointer, int button) {}

	@Override
	public void attach(ParentRenderNode<?, ?> parentRenderNode) {
		if (renderNode != null) {
			return;
		}
		renderNode = new CustomUiElementRenderNode(parentRenderNode, this);
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
		if (visibility == null) {
			return;
		}
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
		return UiContainer.getTheme().getColumnStyleRule(styleId, ScreenSize.XS);
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
