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

import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.Queue;
import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.event.ActionEvent;
import org.mini2Dx.ui.event.ActionEventPool;
import org.mini2Dx.ui.event.EventTrigger;
import org.mini2Dx.ui.event.params.EventTriggerParams;
import org.mini2Dx.ui.layout.*;
import org.mini2Dx.ui.listener.ActionListener;
import org.mini2Dx.ui.listener.TextInputListener;
import org.mini2Dx.ui.render.NodeState;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.TextBoxRenderNode;
import org.mini2Dx.ui.render.UiContainerRenderTree;
import org.mini2Dx.ui.style.StyleRule;

/**
 * A text box {@link UiElement}. Can optionally function as a password field.
 */
public class TextBox extends UiElement implements Actionable, FlexUiElement {
	private final Queue<Runnable> deferredQueue = new Queue<Runnable>();
	
	private Array<ActionListener> actionListeners;
	private Array<TextInputListener> textInputListeners;
	private String value = "";

	@Field(optional=true)
	private String flexLayout = null;
	@Field(optional = true)
	private float x;
	@Field(optional = true)
	private float y;
	@Field(optional = true)
	private float width;
	@Field(optional = true)
	private float height;
	@Field(optional = true)
	private boolean enabled = true;
	@Field(optional = true)
	private boolean passwordField = false;
	
	protected TextBoxRenderNode renderNode;

	/**
	 * Constructor. Generates a unique ID for this {@link TextBox}
	 */
	public TextBox() {
		this(null);
	}

	/**
	 * Constructor
	 * @param id The unique ID for this {@link TextBox}
	 */
	public TextBox(@ConstructorArg(clazz = String.class, name = "id") String id) {
		this(id, 0f, 0f, 40f, 20f);
	}

	/**
	 * Constructor
	 * @param x The x coordinate of this element relative to its parent
	 * @param y The y coordinate of this element relative to its parent
	 * @param width The width of this element
	 * @param height The height of this element
	 */
	public TextBox(@ConstructorArg(clazz = Float.class, name = "x") float x,
				   @ConstructorArg(clazz = Float.class, name = "y") float y,
				   @ConstructorArg(clazz = Float.class, name = "width") float width,
				   @ConstructorArg(clazz = Float.class, name = "height") float height) {
		super(null, x, y, width, height);
	}

	/**
	 * Constructor
	 * @param id The unique ID for this element (if null an ID will be generated)
	 * @param x The x coordinate of this element relative to its parent
	 * @param y The y coordinate of this element relative to its parent
	 * @param width The width of this element
	 * @param height The height of this element
	 */
	public TextBox(@ConstructorArg(clazz = String.class, name = "id") String id,
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
		renderNode = new TextBoxRenderNode(parentRenderNode, this);
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
	 * Returns the text value entered into this {@link TextBox}
	 * @return An empty {@link String} by default
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the text value entered into this {@link TextBox}
	 * @param value A non-null {@link String}
	 */
	public void setValue(String value) {
		if (value == null) {
			return;
		}
		if (value.equals(this.value)) {
			return;
		}
		this.value = value;
		
		if (renderNode == null) {
			return;
		}
		renderNode.updateBitmapFontCache();
	}

	/**
	 * Returns if this {@link TextBox} is a password field
	 * @return True if characters are hidden by * characters
	 */
	public boolean isPasswordField() {
		return passwordField;
	}

	/**
	 * Sets if this {@link TextBox} is a password field
	 * @param passwordField True if characters should be hidden by * characters
	 */
	public void setPasswordField(boolean passwordField) {
		this.passwordField = passwordField;
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
			actionListeners = new Array<ActionListener>(true, 1, ActionListener.class);
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

	public void addTextInputListener(TextInputListener listener) {
		if(textInputListeners == null) {
			textInputListeners = new Array<TextInputListener>(true, 1, TextInputListener.class);
		}
		textInputListeners.add(listener);
	}

	public void removeTextInputListener(TextInputListener listener) {
		if(textInputListeners == null) {
			return;
		}
		textInputListeners.removeValue(listener, false);
	}

	public boolean notifyTextInputListeners(char c) {
		if(textInputListeners == null) {
			return true;
		}
		for(int i = 0; i < textInputListeners.size; i++) {
			if(!textInputListeners.get(i).textReceived(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Grabs the input as if the player clicked/selected this element
	 * @param uiContainer The {@link UiContainer} the {@link TextBox} belongs to
	 * @return True if the input was successfully obtained
	 */
	public boolean grabInput(UiContainer uiContainer) {
		if(renderNode == null) {
			return false;
		}
		uiContainer.setActiveAction(renderNode);
		renderNode.setState(NodeState.ACTION);
		return true;
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
		return UiContainer.getTheme().getTextBoxStyleRule(styleId, ScreenSize.XS);
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
}
