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
