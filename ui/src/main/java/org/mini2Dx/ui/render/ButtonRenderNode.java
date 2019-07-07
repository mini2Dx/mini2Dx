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
import org.mini2Dx.gdx.Input.Buttons;
import org.mini2Dx.ui.element.Button;
import org.mini2Dx.ui.event.EventTrigger;
import org.mini2Dx.ui.event.params.EventTriggerParams;
import org.mini2Dx.ui.event.params.EventTriggerParamsPool;
import org.mini2Dx.ui.event.params.MouseEventTriggerParams;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.style.BackgroundRenderer;
import org.mini2Dx.ui.style.ButtonStyleRule;

/**
 * {@link RenderNode} implementation for {@link Button}
 */
public class ButtonRenderNode extends ParentRenderNode<Button, ButtonStyleRule> implements ActionableRenderNode {
	
	public ButtonRenderNode(ParentRenderNode<?, ?> parent, Button element) {
		super(parent, element);
	}

	@Override
	public ActionableRenderNode mouseDown(int screenX, int screenY, int pointer, int button) {
		if (!isIncludedInRender()) {
			return null;
		}
		if (button != Buttons.LEFT && button != Buttons.RIGHT && button != Buttons.MIDDLE) {
			return null;
		}
		if (!element.isEnabled()) {
			return null;
		}
		if (innerArea.contains(screenX, screenY)) {
			setState(NodeState.ACTION);
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
		return super.mouseMoved(screenX, screenY);
	}

	@Override
	protected void renderBackground(Graphics g) {
		BackgroundRenderer backgroundRenderer = style.getNormalBackgroundRenderer();
		if (element.isEnabled()) {
			switch (getState()) {
			case ACTION:
				backgroundRenderer = style.getActionBackgroundRenderer();
				break;
			case HOVER:
				backgroundRenderer = style.getHoverBackgroundRenderer();
				break;
			default:
				break;
			}
		} else {
			backgroundRenderer = style.getDisabledBackgroundRenderer();
		}

		if (backgroundRenderer != null) {
			backgroundRenderer.render(g, getInnerRenderX(), getInnerRenderY(), getInnerRenderWidth(),
					getInnerRenderHeight());
		}
	}

	@Override
	protected ButtonStyleRule determineStyleRule(LayoutState layoutState) {
		return layoutState.getTheme().getStyleRule(element, layoutState.getScreenSize());
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
	public boolean isEnabled() {
		return element.isEnabled();
	}
}
