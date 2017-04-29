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

import org.mini2Dx.core.engine.geom.CollisionBox;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.ui.element.Select;
import org.mini2Dx.ui.element.SelectOption;
import org.mini2Dx.ui.event.EventTrigger;
import org.mini2Dx.ui.event.params.EventTriggerParams;
import org.mini2Dx.ui.event.params.EventTriggerParamsPool;
import org.mini2Dx.ui.event.params.MouseEventTriggerParams;
import org.mini2Dx.ui.layout.HorizontalAlignment;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.style.ButtonStyleRule;
import org.mini2Dx.ui.style.LabelStyleRule;
import org.mini2Dx.ui.style.SelectStyleRule;
import org.mini2Dx.ui.style.UiTheme;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * {@link RenderNode} implementation for {@link Select}
 */
public class SelectRenderNode extends RenderNode<Select<?>, SelectStyleRule>implements ActionableRenderNode {
	private static final GlyphLayout glyphLayout = new GlyphLayout();

	private final CollisionBox leftButton = new CollisionBox();
	private final CollisionBox rightButton = new CollisionBox();

	private NodeState leftButtonState = NodeState.NORMAL;
	private NodeState rightButtonState = NodeState.NORMAL;

	private ButtonStyleRule leftButtonStyleRule, rightButtonStyleRule;
	private LabelStyleRule enabledStyleRule, disabledStyleRule;
	private BitmapFont font = new BitmapFont(true);
	private float labelHeight = 0f;

	public SelectRenderNode(ParentRenderNode<?, ?> parent, Select<?> element) {
		super(parent, element);
	}

	@Override
	public void update(UiContainerRenderTree uiContainer, float delta) {
		leftButton.preUpdate();
		rightButton.preUpdate();
		super.update(uiContainer, delta);
		leftButton.set(getContentRenderX(), getContentRenderY());
		rightButton.set(getContentRenderX() + getContentRenderWidth() - rightButton.getWidth(), getContentRenderY());
	}

	@Override
	public void interpolate(float alpha) {
		super.interpolate(alpha);
		leftButton.interpolate(null, alpha);
		rightButton.interpolate(null, alpha);
	}

	@Override
	protected void renderElement(Graphics g) {
		SelectOption<?> selectedOption = element.getSelectedOption();
		if (selectedOption == null) {
			return;
		}

		if (style.getBackgroundNinePatch() != null) {
			g.drawNinePatch(style.getBackgroundNinePatch(), getInnerRenderX(), getInnerRenderY(), getInnerRenderWidth(),
					getInnerRenderHeight());
		}

		Color tmpColor = g.getColor();
		BitmapFont tmpFont = g.getFont();

		if (element.isEnabled()) {
			if (element.getEnabledTextColor() != null) {
				g.setColor(element.getEnabledTextColor());
			} else if (enabledStyleRule.getColor() != null) {
				g.setColor(enabledStyleRule.getColor());
			} else {
				throw new MdxException("Could not determine Color for Select element " + element.getId()
						+ ". Please use Select#setEnabledTextColor or apply a Color to the enabled label style.");
			}

			if (enabledStyleRule.getBitmapFont() != null) {
				g.setFont(enabledStyleRule.getBitmapFont());
			} else {
				g.setFont(font);
			}

			g.drawString(element.getSelectedLabel(), leftButton.getRenderX() + leftButton.getRenderWidth(),
					leftButton.getRenderY() + (leftButton.getRenderHeight() / 2) - (labelHeight / 2f),
					getContentRenderWidth() - leftButton.getRenderWidth() - rightButton.getRenderWidth(),
					HorizontalAlignment.CENTER.getAlignValue());

			switch (leftButtonState) {
			case ACTION:
				g.drawNinePatch(leftButtonStyleRule.getActionNinePatch(), leftButton.getRenderX(),
						leftButton.getRenderY(), leftButton.getRenderWidth(), leftButton.getRenderHeight());
				break;
			case HOVER:
				g.drawNinePatch(leftButtonStyleRule.getHoverNinePatch(), leftButton.getRenderX(),
						leftButton.getRenderY(), leftButton.getRenderWidth(), leftButton.getRenderHeight());
				break;
			case NORMAL:
			default:
				g.drawNinePatch(leftButtonStyleRule.getNormalNinePatch(), leftButton.getRenderX(),
						leftButton.getRenderY(), leftButton.getRenderWidth(), leftButton.getRenderHeight());
				break;
			}
			
			switch (rightButtonState) {
			case ACTION:
				g.drawNinePatch(rightButtonStyleRule.getActionNinePatch(), rightButton.getRenderX(),
						rightButton.getRenderY(), rightButton.getRenderWidth(), rightButton.getRenderHeight());
				break;
			case HOVER:
				g.drawNinePatch(rightButtonStyleRule.getHoverNinePatch(), rightButton.getRenderX(),
						rightButton.getRenderY(), rightButton.getRenderWidth(), rightButton.getRenderHeight());
				break;
			case NORMAL:
			default:
				g.drawNinePatch(rightButtonStyleRule.getNormalNinePatch(), rightButton.getRenderX(),
						rightButton.getRenderY(), rightButton.getRenderWidth(), rightButton.getRenderHeight());
				break;
			}
		} else {
			if (element.getDisabledTextColor() != null) {
				g.setColor(element.getDisabledTextColor());
			} else if (disabledStyleRule.getColor() != null) {
				g.setColor(disabledStyleRule.getColor());
			} else {
				throw new MdxException("Could not determine Color for Select element " + element.getId()
						+ ". Please use Select#setDisabledTextColor or apply a Color to the disabled label style.");
			}

			if (disabledStyleRule.getBitmapFont() != null) {
				g.setFont(disabledStyleRule.getBitmapFont());
			} else {
				g.setFont(font);
			}

			g.drawString(element.getSelectedLabel(), leftButton.getRenderX() + leftButton.getRenderWidth(),
					leftButton.getRenderY() + (leftButton.getRenderHeight() / 2) - (labelHeight / 2f),
					getContentRenderWidth() - leftButton.getRenderWidth() - rightButton.getRenderWidth(),
					HorizontalAlignment.CENTER.getAlignValue());

			g.drawNinePatch(leftButtonStyleRule.getDisabledNinePatch(), leftButton.getRenderX(),
					leftButton.getRenderY(), leftButton.getRenderWidth(), leftButton.getRenderHeight());
			g.drawNinePatch(rightButtonStyleRule.getDisabledNinePatch(), rightButton.getRenderX(),
					rightButton.getRenderY(), rightButton.getRenderWidth(), rightButton.getRenderHeight());
		}

		g.setColor(tmpColor);
		g.setFont(tmpFont);
	}
	
	@Override
	public void setState(NodeState state) {
		switch(state) {
		case HOVER:
			if(leftButtonState != NodeState.ACTION) {
				leftButtonState = NodeState.HOVER;
			}
			if(rightButtonState != NodeState.ACTION) {
				rightButtonState = NodeState.HOVER;
			}
			break;
		case NORMAL:
			if(leftButtonState != NodeState.ACTION) {
				leftButtonState = NodeState.NORMAL;
			}
			if(rightButtonState != NodeState.ACTION) {
				rightButtonState = NodeState.NORMAL;
			}
			break;
		case ACTION:
		default:
			break;
		}
		super.setState(state);
	}

	@Override
	public ActionableRenderNode mouseDown(int screenX, int screenY, int pointer, int button) {
		if (!isIncludedInRender()) {
			return null;
		}
		if (!element.isEnabled()) {
			return null;
		}
		if (leftButton.contains(screenX, screenY)) {
			setState(NodeState.ACTION);
			leftButtonState = NodeState.ACTION;
			return this;
		} else if (rightButton.contains(screenX, screenY)) {
			setState(NodeState.ACTION);
			rightButtonState = NodeState.ACTION;
			return this;
		}
		return null;
	}

	@Override
	public void mouseUp(int screenX, int screenY, int pointer, int button) {
		if (leftButtonState == NodeState.ACTION) {
			element.previousOption();
			leftButtonState = NodeState.NORMAL;
		} else if (rightButtonState == NodeState.ACTION) {
			element.nextOption();
			rightButtonState = NodeState.NORMAL;
		}
		
		MouseEventTriggerParams params = EventTriggerParamsPool.allocateMouseParams();
		params.setMouseX(screenX);
		params.setMouseY(screenY);
		endAction(EventTrigger.getTriggerForMouseClick(button), params);
		EventTriggerParamsPool.release(params);
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if (outerArea.contains(screenX, screenY)) {
			setState(NodeState.HOVER);

			if (leftButton.contains(screenX, screenY)) {
				if (leftButtonState != NodeState.ACTION) {
					leftButtonState = NodeState.HOVER;
				}
				if (rightButtonState != NodeState.ACTION) {
					rightButtonState = NodeState.NORMAL;
				}
			} else if (rightButton.contains(screenX, screenY)) {
				if (rightButtonState != NodeState.ACTION) {
					rightButtonState = NodeState.HOVER;
				}
				if (leftButtonState != NodeState.ACTION) {
					leftButtonState = NodeState.NORMAL;
				}
			} else {
				if (rightButtonState != NodeState.ACTION) {
					rightButtonState = NodeState.NORMAL;
				}
				if (leftButtonState != NodeState.ACTION) {
					leftButtonState = NodeState.NORMAL;
				}
			}
			return true;
		} else {
			setState(NodeState.NORMAL);
		}
		return false;
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
	protected float determinePreferredContentWidth(LayoutState layoutState) {
		leftButton.setWidth(style.getButtonWidth());
		rightButton.setWidth(style.getButtonWidth());
		
		if (element.getLayout().isHiddenByInputSource(layoutState)) {
			return 0f;
		}
		float layoutRuleResult = element.getLayout().getPreferredWidth(layoutState);
		if (layoutRuleResult <= 0f) {
			hiddenByLayoutRule = true;
			return 0f;
		} else {
			hiddenByLayoutRule = false;
		}
		return layoutRuleResult - style.getPaddingLeft() - style.getPaddingRight() - style.getMarginLeft()
				- style.getMarginRight();
	}

	@Override
	protected float determinePreferredContentHeight(LayoutState layoutState) {
		BitmapFont labelStyleFont = null;
		if (element.isEnabled()) {
			labelStyleFont = enabledStyleRule.getBitmapFont();
		} else {
			labelStyleFont = disabledStyleRule.getBitmapFont();
		}
		if (labelStyleFont == null) {
			glyphLayout.setText(font, element.getSelectedLabel(), Color.WHITE, preferredContentWidth,
					HorizontalAlignment.CENTER.getAlignValue(), true);
		} else {
			glyphLayout.setText(labelStyleFont, element.getSelectedLabel(), Color.WHITE, preferredContentWidth,
					HorizontalAlignment.CENTER.getAlignValue(), true);
		}
		labelHeight = glyphLayout.height;
		
		float result = glyphLayout.height;
		if(style.getMinHeight() > 0 && result < style.getMinHeight()) {
			result = style.getMinHeight();
		}
		leftButton.setHeight(result);
		rightButton.setHeight(result);
		return result;
	}

	@Override
	protected float determineXOffset(LayoutState layoutState) {
		return 0f;
	}

	@Override
	protected float determineYOffset(LayoutState layoutState) {
		return 0f;
	}

	@Override
	protected SelectStyleRule determineStyleRule(LayoutState layoutState) {
		SelectStyleRule selectStyleRule = layoutState.getTheme().getStyleRule(element, layoutState.getScreenSize());
		if (selectStyleRule.getLeftButtonStyle() != null) {
			leftButtonStyleRule = layoutState.getTheme().getButtonStyleRule(selectStyleRule.getLeftButtonStyle(),
					layoutState.getScreenSize());
		} else {
			leftButtonStyleRule = layoutState.getTheme().getButtonStyleRule(UiTheme.DEFAULT_STYLE_ID,
					layoutState.getScreenSize());
		}
		if (selectStyleRule.getRightButtonStyle() != null) {
			rightButtonStyleRule = layoutState.getTheme().getButtonStyleRule(selectStyleRule.getRightButtonStyle(),
					layoutState.getScreenSize());
		} else {
			rightButtonStyleRule = layoutState.getTheme().getButtonStyleRule(UiTheme.DEFAULT_STYLE_ID,
					layoutState.getScreenSize());
		}
		if (selectStyleRule.getEnabledLabelStyle() != null) {
			enabledStyleRule = layoutState.getTheme().getLabelStyleRule(selectStyleRule.getEnabledLabelStyle(),
					layoutState.getScreenSize());
		} else {
			enabledStyleRule = layoutState.getTheme().getLabelStyleRule(UiTheme.DEFAULT_STYLE_ID,
					layoutState.getScreenSize());
		}
		if (selectStyleRule.getDisabledLabelStyle() != null) {
			disabledStyleRule = layoutState.getTheme().getLabelStyleRule(selectStyleRule.getDisabledLabelStyle(),
					layoutState.getScreenSize());
		} else {
			disabledStyleRule = layoutState.getTheme().getLabelStyleRule(UiTheme.DEFAULT_STYLE_ID,
					layoutState.getScreenSize());
		}
		return selectStyleRule;
	}
}
