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

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.gdx.Input;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.ui.element.Slider;
import org.mini2Dx.ui.event.EventTrigger;
import org.mini2Dx.ui.event.params.*;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.style.SliderStyleRule;

/**
 *
 */
public class SliderRenderNode extends RenderNode<Slider, SliderStyleRule> implements ActionableRenderNode {
	private final Rectangle sliderPosition;
	private float valueDeltaPerFrame;

	private boolean dragging = false;

	public SliderRenderNode(ParentRenderNode<?, ?> parent, Slider element) {
		super(parent, element);
		sliderPosition = new Rectangle(0f, 0f, 1f, 1f);
	}

	@Override
	public void update(UiContainerRenderTree uiContainer, float delta) {
		super.update(uiContainer, delta);

		if (valueDeltaPerFrame != 0f) {
			element.setValue(element.getValue() + valueDeltaPerFrame);
		}

		if (dragging) {
			float relativeX = Math.max(0, Mdx.input.getX() - getContentRenderX());
			element.setValue(relativeX / getContentRenderWidth());
			determineSliderPosiitonByElementValue(getContentRenderWidth());
		}
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if (dragging) {
			return true;
		}
		return super.mouseMoved(screenX, screenY);
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

			if (sliderPosition.contains(screenX - getContentRenderX(), screenY - getContentRenderY())) {
				dragging = true;
			} else {
				dragging = false;
			}
			return this;
		}
		return null;
	}

	@Override
	public void mouseUp(int screenX, int screenY, int pointer, int button) {
		if (!dragging && getState() != NodeState.ACTION) {
			return;
		}
		if (innerArea.contains(screenX, screenY)) {
			setState(NodeState.HOVER);
		} else {
			setState(NodeState.NORMAL);
		}
		dragging = false;

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

		if (style.getSliderBarRenderer() != null) {
			int sliderBarRenderY = getContentRenderY();
			int slideBarHeight = getContentRenderHeight();

			if (style.getSliderBarMaxHeight() > 0) {
				slideBarHeight = style.getSliderBarMaxHeight();
				sliderBarRenderY = getContentRenderY() + (getContentRenderHeight() / 2) - (slideBarHeight / 2);
			}

			style.getSliderBarRenderer().render(g, getContentRenderX(), sliderBarRenderY,
					getContentRenderWidth(), slideBarHeight);
		}

		TextureRegion textureRegion = null;

		if (element.isEnabled()) {
			switch (getState()) {
			case ACTION:
				textureRegion = style.getActiveTextureRegion();
				break;
			case HOVER:
				textureRegion = style.getHoverTextureRegion();
				break;
			case NORMAL:
			default:
				textureRegion = style.getNormalTextureRegion();
				break;
			}
		} else {
			textureRegion = style.getDisabledTextureRegion();
		}
		int sliderRenderX = MathUtils.round(getContentRenderX() + sliderPosition.getX());
		g.drawTextureRegion(textureRegion, sliderRenderX, getContentRenderY());
	}

	@Override
	public void beginAction(EventTrigger eventTrigger, EventTriggerParams eventTriggerParams) {
		switch (eventTrigger) {
		case CONTROLLER:
			if (element.isChangedOnBeginEvent()) {
				GamePadEventTriggerParams controllerParams = (GamePadEventTriggerParams) eventTriggerParams;
//				switch (controllerParams.getGamePadButton().getInternalName()) {
//				case Xbox360Button.LEFT:
//				case "xboxOne-left":
//					valueDeltaPerFrame = -element.getValueStep();
//					break;
//				case "xbox360-right":
//				case "xboxOne-right":
//					valueDeltaPerFrame = element.getValueStep();
//					break;
//				}
			}
			break;
		case KEYBOARD:
			if (element.isChangedOnBeginEvent()) {
				GamePadEventTriggerParams controllerParams = (GamePadEventTriggerParams) eventTriggerParams;
//				switch (controllerParams.getGamePadButton().getInternalName()) {
//				case "xbox360-left":
//				case "xboxOne-left":
//					valueDeltaPerFrame = -element.getValueStep();
//					break;
//				case "xbox360-right":
//				case "xboxOne-right":
//					valueDeltaPerFrame = element.getValueStep();
//					break;
//				}
			}
			break;
		default:

			break;
		}
		element.notifyActionListenersOfBeginEvent(eventTrigger, eventTriggerParams);
	}

	@Override
	public void endAction(EventTrigger eventTrigger, EventTriggerParams eventTriggerParams) {
		switch (eventTrigger) {
		case CONTROLLER:
			if (!element.isChangedOnBeginEvent()) {
				GamePadEventTriggerParams controllerParams = (GamePadEventTriggerParams) eventTriggerParams;
//				switch (controllerParams.getGamePadButton().getInternalName()) {
//				case "xbox360-left":
//				case "xboxOne-left":
//					element.setValue(element.getValue() - element.getValueStep());
//					break;
//				case "xbox360-right":
//				case "xboxOne-right":
//					element.setValue(element.getValue() + element.getValueStep());
//					break;
//				}
			} else {
				valueDeltaPerFrame = 0f;
			}
			break;
		case KEYBOARD:
			if (!element.isChangedOnBeginEvent()) {
				KeyboardEventTriggerParams keyboardParams = (KeyboardEventTriggerParams) eventTriggerParams;
				switch (keyboardParams.getKey()) {
				case Input.Keys.LEFT:
				case Input.Keys.A:
					element.setValue(element.getValue() - element.getValueStep());
					break;
				case Input.Keys.RIGHT:
				case Input.Keys.D:
					element.setValue(element.getValue() + element.getValueStep());
					break;
				}
			} else {
				valueDeltaPerFrame = 0f;
			}
			break;
		default:
			if (!dragging) {
				MouseEventTriggerParams mouseParams = (MouseEventTriggerParams) eventTriggerParams;
				float relativeX = mouseParams.getMouseX() - getContentRenderX();
				element.setValue(relativeX / getContentRenderWidth());
				determineSliderPosiitonByElementValue(getContentRenderWidth());
			}
			break;
		}
		element.notifyActionListenersOfEndEvent(eventTrigger, eventTriggerParams);
	}

	@Override
	protected float determinePreferredContentWidth(LayoutState layoutState) {
		float availableWidth = layoutState.getParentWidth() - style.getPaddingLeft() - style.getPaddingRight()
				- style.getMarginLeft() - style.getMarginRight();
		determineSliderPosiitonByElementValue(availableWidth);
		return style.getRounding().calculateRounding(availableWidth);
	}

	private void determineSliderPosiitonByElementValue(float sliderWidth) {
		sliderPosition.set(
				MathUtils.round(sliderWidth * element.getValue())
						- (style.getActiveTextureRegion().getRegionWidth() / 2),
				0, style.getActiveTextureRegion().getRegionWidth(), style.getActiveTextureRegion().getRegionHeight());
	}

	@Override
	protected float determinePreferredContentHeight(LayoutState layoutState) {
		float result = style.getActiveTextureRegion().getRegionHeight();
		if (style.getMinHeight() > 0 && result + style.getPaddingTop() + style.getPaddingBottom() + style.getMarginTop()
				+ style.getMarginBottom() < style.getMinHeight()) {
			result = style.getMinHeight() - style.getPaddingTop() - style.getPaddingBottom() - style.getMarginTop()
					- style.getMarginBottom();
		}
		return result;
	}

	@Override
	protected SliderStyleRule determineStyleRule(LayoutState layoutState) {
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
