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
package org.mini2Dx.ui.render;

import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.NinePatch;
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.ui.element.Checkbox;
import org.mini2Dx.ui.event.EventTrigger;
import org.mini2Dx.ui.event.params.EventTriggerParams;
import org.mini2Dx.ui.event.params.EventTriggerParamsPool;
import org.mini2Dx.ui.event.params.MouseEventTriggerParams;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.style.CheckboxStyleRule;

import com.badlogic.gdx.Input.Buttons;

/**
 *
 */
public class CheckboxRenderNode extends RenderNode<Checkbox, CheckboxStyleRule> implements ActionableRenderNode {

	public CheckboxRenderNode(ParentRenderNode<?, ?> parent, Checkbox element) {
		super(parent, element);
	}

	@Override
	public ActionableRenderNode mouseDown(int screenX, int screenY, int pointer, int button) {
		if (!isIncludedInRender()) {
			return null;
		}
		if (button != Buttons.LEFT) {
			return null;
		}
		if (!element.isEnabled()) {
			return null;
		}
		if (outerArea.contains(screenX, screenY)) {
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
		if (outerArea.contains(screenX, screenY)) {
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
	protected void renderElement(Graphics g) {
		if (style.getBackgroundNinePatch() != null) {
			g.drawNinePatch(style.getBackgroundNinePatch(), getInnerRenderX(), getInnerRenderY(), getInnerRenderWidth(),
					getInnerRenderHeight());
		}

		NinePatch boxNinePatch = null;
		TextureRegion checkTextureRegion = null;

		if (element.isEnabled()) {
			boxNinePatch = style.getEnabledNinePatch();

			switch (getState()) {
			case HOVER:
				if (element.isChecked()) {
					checkTextureRegion = style.getEnabledCheckTextureRegion();
				} else {
					checkTextureRegion = style.getEnabledUncheckTextureRegion();
				}
				break;
			case ACTION:
			case NORMAL:
			default:
				if (element.isChecked()) {
					checkTextureRegion = style.getEnabledCheckTextureRegion();
				} else {
					checkTextureRegion = style.getEnabledUncheckTextureRegion();
				}
				break;
			}
		} else {
			boxNinePatch = style.getDisabledNinePatch();

			if (element.isChecked()) {
				checkTextureRegion = style.getDisabledCheckTextureRegion();
			} else {
				checkTextureRegion = style.getDisabledUncheckTextureRegion();
			}
		}

		g.drawNinePatch(boxNinePatch, getInnerRenderX(), getInnerRenderY(), getInnerRenderWidth(),
				getInnerRenderHeight());
		if (checkTextureRegion == null) {
			return;
		}

		if (element.isResponsive()) {
			g.drawTextureRegion(checkTextureRegion,
					getContentRenderX() + (getContentRenderWidth() / 2) - (checkTextureRegion.getRegionWidth() / 2),
					getContentRenderY());
		} else {
			g.drawTextureRegion(checkTextureRegion, getContentRenderX(), getContentRenderY());
		}
	}

	@Override
	public void beginAction(EventTrigger eventTrigger, EventTriggerParams eventTriggerParams) {
		element.notifyActionListenersOfBeginEvent(eventTrigger, eventTriggerParams);
	}

	@Override
	public void endAction(EventTrigger eventTrigger, EventTriggerParams eventTriggerParams) {
		element.setChecked(!element.isChecked());
		element.notifyActionListenersOfEndEvent(eventTrigger, eventTriggerParams);
	}

	@Override
	protected float determinePreferredContentWidth(LayoutState layoutState) {
		float availableWidth = layoutState.getParentWidth() - style.getPaddingLeft() - style.getPaddingRight()
				- style.getMarginLeft() - style.getMarginRight();

		if (element.isResponsive()) {
			return style.getRounding().calculateRounding(availableWidth);
		} else {
			return style.getEnabledCheckTextureRegion().getRegionWidth();
		}
	}

	@Override
	protected float determinePreferredContentHeight(LayoutState layoutState) {
		if (element.isResponsive()) {
			return style.getEnabledCheckTextureRegion().getRegionHeight() * (preferredContentWidth / style.getEnabledCheckTextureRegion().getRegionWidth());
		} else {
			return style.getEnabledCheckTextureRegion().getRegionHeight();
		}
	}

	@Override
	protected CheckboxStyleRule determineStyleRule(LayoutState layoutState) {
		return layoutState.getTheme().getStyleRule(element, layoutState.getScreenSize());
	}

	@Override
	protected float determineXOffset(LayoutState layoutState) {
		return 0f;
	}

	@Override
	protected float determineYOffset(LayoutState layoutState) {
		return 0f;
	}

}
