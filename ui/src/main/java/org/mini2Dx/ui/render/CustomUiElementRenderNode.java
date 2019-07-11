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
package org.mini2Dx.ui.render;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.gdx.Input;
import org.mini2Dx.ui.element.CustomUiElement;
import org.mini2Dx.ui.event.EventTrigger;
import org.mini2Dx.ui.event.params.EventTriggerParams;
import org.mini2Dx.ui.event.params.EventTriggerParamsPool;
import org.mini2Dx.ui.event.params.MouseEventTriggerParams;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.style.StyleRule;

/**
 * {@link RenderNode} implementation for {@link CustomUiElement}
 */
public class CustomUiElementRenderNode extends RenderNode<CustomUiElement, StyleRule> implements ActionableRenderNode {

	public CustomUiElementRenderNode(ParentRenderNode<?, ?> parent, CustomUiElement element) {
		super(parent, element);
	}

	@Override
	public void update(UiContainerRenderTree uiContainer, float delta) {
		super.update(uiContainer, delta);
		element.update(uiContainer, delta);
	}

	@Override
	protected void renderElement(Graphics g) {
		element.render(g, this);
	}

	@Override
	protected StyleRule determineStyleRule(LayoutState layoutState) {
		return layoutState.getTheme().getColumnStyleRule(element.getStyleId(), layoutState.getScreenSize());
	}

	@Override
	protected float determinePreferredContentWidth(LayoutState layoutState) {
		return element.getContentWidth();
	}

	@Override
	protected float determinePreferredContentHeight(LayoutState layoutState) {
		return element.getContentHeight();
	}

	@Override
	protected float determineXOffset(LayoutState layoutState) {
		return element.getX();
	}

	@Override
	protected float determineYOffset(LayoutState layoutState) {
		return element.getY();
	}

	@Override
	public void beginAction(EventTrigger eventTrigger, EventTriggerParams eventTriggerParams) {
		element.notifyActionListenersOfBeginEvent(eventTrigger, eventTriggerParams);
	}

	@Override
	public void endAction(EventTrigger eventTrigger, EventTriggerParams eventTriggerParams) {
		element.notifyActionListenersOfEndEvent(eventTrigger, eventTriggerParams);
	}

	@Override
	public ActionableRenderNode mouseDown(int screenX, int screenY, int pointer, int button) {
		if (!isIncludedInRender()) {
			return null;
		}
		if (button != Input.Buttons.LEFT && button != Input.Buttons.RIGHT && button != Input.Buttons.MIDDLE) {
			return null;
		}
		if (!element.isEnabled()) {
			return null;
		}
		if (innerArea.contains(screenX, screenY)) {
			setState(NodeState.ACTION);
			element.onMouseDown(screenX, screenY, pointer, button);
			return this;
		}
		return null;
	}

	@Override
	public void mouseUp(int screenX, int screenY, int pointer, int button) {
		if (getState() != NodeState.ACTION) {
			return;
		}
		if (innerArea.contains(screenX, screenY)) {
			setState(NodeState.HOVER);
		} else {
			setState(NodeState.NORMAL);
		}

		element.onMouseUp(screenX, screenY, pointer, button);

		MouseEventTriggerParams params = EventTriggerParamsPool.allocateMouseParams();
		params.setMouseX(screenX);
		params.setMouseY(screenY);
		endAction(EventTrigger.getTriggerForMouseClick(button), params);
		EventTriggerParamsPool.release(params);
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if (getState() == NodeState.ACTION) {
			return true;
		}
		final boolean result = super.mouseMoved(screenX, screenY);
		element.onMouseMoved(screenX, screenY);
		return result;
	}

	@Override
	public boolean isEnabled() {
		return element.isEnabled();
	}
}
