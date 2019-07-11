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
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.gdx.Input;
import org.mini2Dx.ui.element.Checkbox;
import org.mini2Dx.ui.event.EventTrigger;
import org.mini2Dx.ui.event.params.EventTriggerParams;
import org.mini2Dx.ui.event.params.EventTriggerParamsPool;
import org.mini2Dx.ui.event.params.MouseEventTriggerParams;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.style.BackgroundRenderer;
import org.mini2Dx.ui.style.CheckboxStyleRule;

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
		if (button != Input.Buttons.LEFT) {
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

	protected void renderBackground(Graphics g) {
		switch(getState()) {
		case NORMAL:
			if (style.getNormalBackgroundRenderer() != null) {
				style.getNormalBackgroundRenderer().render(g, getInnerRenderX(), getInnerRenderY(), getInnerRenderWidth(),
						getInnerRenderHeight());
			}
			break;
		case HOVER:
			if(style.getHoverBackgroundRenderer() != null) {
				style.getHoverBackgroundRenderer().render(g, getInnerRenderX(), getInnerRenderY(), getInnerRenderWidth(),
						getInnerRenderHeight());
			}
			break;
		case ACTION:
			if(style.getHoverBackgroundRenderer() != null) {
				style.getHoverBackgroundRenderer().render(g, getInnerRenderX(), getInnerRenderY(), getInnerRenderWidth(),
						getInnerRenderHeight());
			}
			break;
		}
	}

	@Override
	protected void renderElement(Graphics g) {
		renderBackground(g);

		BackgroundRenderer boxBackgroundRenderer = null;
		TextureRegion checkTextureRegion = null;

		if (element.isEnabled()) {
			boxBackgroundRenderer = style.getEnabledBackgroundRenderer();

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
			boxBackgroundRenderer = style.getDisabledBackgroundRenderer();

			if (element.isChecked()) {
				checkTextureRegion = style.getDisabledCheckTextureRegion();
			} else {
				checkTextureRegion = style.getDisabledUncheckTextureRegion();
			}
		}

		boxBackgroundRenderer.render(g, getInnerRenderX(), getInnerRenderY(), getInnerRenderWidth(),
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
		if(parent.getLayoutRuleset().isFlexLayout()) {
			return 0f;
		} else {
			return element.getX();
		}
	}

	@Override
	protected float determineYOffset(LayoutState layoutState) {
		if(parent.getLayoutRuleset().isFlexLayout()) {
			return 0f;
		} else {
			return element.getY();
		}
	}

	@Override
	public boolean isEnabled() {
		return element.isEnabled();
	}

}
