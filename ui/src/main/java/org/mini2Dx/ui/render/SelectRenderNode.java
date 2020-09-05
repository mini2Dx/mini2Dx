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
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.collision.CollisionBox;
import org.mini2Dx.core.collision.util.StaticCollisionBox;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.font.FontGlyphLayout;
import org.mini2Dx.core.font.GameFont;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.util.Align;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.ui.element.Select;
import org.mini2Dx.ui.element.SelectOption;
import org.mini2Dx.ui.event.EventTrigger;
import org.mini2Dx.ui.event.params.EventTriggerParams;
import org.mini2Dx.ui.event.params.EventTriggerParamsPool;
import org.mini2Dx.ui.event.params.MouseEventTriggerParams;
import org.mini2Dx.ui.layout.*;
import org.mini2Dx.ui.style.ButtonStyleRule;
import org.mini2Dx.ui.style.LabelStyleRule;
import org.mini2Dx.ui.style.SelectStyleRule;
import org.mini2Dx.ui.style.UiTheme;

/**
 * {@link RenderNode} implementation for {@link Select}
 */
public class SelectRenderNode extends RenderNode<Select<?>, SelectStyleRule> implements ActionableRenderNode {
	protected LayoutRuleset layoutRuleset;
	private final StaticCollisionBox leftButton = new StaticCollisionBox();
	private final StaticCollisionBox rightButton = new StaticCollisionBox();
	private final Color white = Mdx.graphics.newColor(1f,1f, 1f, 1f);

	private NodeState leftButtonState = NodeState.NORMAL;
	private NodeState rightButtonState = NodeState.NORMAL;

	private ButtonStyleRule leftButtonStyleRule, rightButtonStyleRule;
	private LabelStyleRule enabledStyleRule, disabledStyleRule, leftButtonLabelStyleRule, rightButtonLabelStyleRule;
	private GameFont fallbackFont = Mdx.fonts.defaultFont();
	private FontGlyphLayout glyphLayout = fallbackFont.newGlyphLayout();
	private float labelHeight = 0f;

	public SelectRenderNode(ParentRenderNode<?, ?> parent, Select<?> element) {
		super(parent, element);
		initLayoutRuleset();
	}

	protected void initLayoutRuleset() {
		if(element.getFlexLayout() != null) {
			layoutRuleset = FlexLayoutRuleset.parse(element.getFlexLayout());
		} else {
			layoutRuleset = new ImmediateLayoutRuleset(element);
		}
	}

	@Override
	public void layout(LayoutState layoutState) {
		if (!layoutRuleset.equals(element.getFlexLayout())) {
			initLayoutRuleset();
		}
		super.layout(layoutState);
	}

	@Override
	public void update(UiContainerRenderTree uiContainer, float delta) {
		super.update(uiContainer, delta);
		leftButton.setXY(getContentRenderX(), getContentRenderY());
		rightButton.setXY(getContentRenderX() + getContentRenderWidth() - rightButton.getWidth(), getContentRenderY());
	}

	@Override
	protected void renderElement(Graphics g) {
		SelectOption<?> selectedOption = element.getSelectedOption();
		if (selectedOption == null) {
			return;
		}

		if (style.getNormalBackgroundRenderer() != null) {
			style.getNormalBackgroundRenderer().render(g, getInnerRenderX(), getInnerRenderY(), getInnerRenderWidth(),
					getInnerRenderHeight());
		}

		Color tmpColor = g.getColor();
		GameFont tmpFont = g.getFont();

		final String text = element.getTotalOptions() > 0 ? element.getSelectedLabel() : " ";

		if (element.isEnabled()) {
			if (element.getEnabledTextColor() != null) {
				g.setColor(element.getEnabledTextColor());
			} else if (enabledStyleRule.getColor() != null) {
				g.setColor(enabledStyleRule.getColor());
			} else {
				throw new MdxException("Could not determine Color for Select element " + element.getId()
						+ ". Please use Select#setEnabledTextColor or apply a Color to the enabled label style.");
			}

			if (enabledStyleRule.getGameFont() != null) {
				g.setFont(enabledStyleRule.getGameFont());
			} else {
				g.setFont(fallbackFont);
			}

			g.drawString(text, leftButton.getRenderX() + leftButton.getRenderWidth() + enabledStyleRule.getPaddingLeft(),
					leftButton.getRenderY() + MathUtils.round((leftButton.getRenderHeight() * 0.5f) - (labelHeight * 0.5f))
							+ enabledStyleRule.getPaddingTop(),
					getContentRenderWidth() - leftButton.getRenderWidth() - rightButton.getRenderWidth(),
					HorizontalAlignment.CENTER.getAlignValue());

			switch (leftButtonState) {
			case ACTION:
				leftButtonStyleRule.getActionBackgroundRenderer().render(g, leftButton.getRenderX(),
						leftButton.getRenderY(), leftButton.getRenderWidth(), leftButton.getRenderHeight());
				break;
			case HOVER:
				leftButtonStyleRule.getHoverBackgroundRenderer().render(g, leftButton.getRenderX(),
						leftButton.getRenderY(), leftButton.getRenderWidth(), leftButton.getRenderHeight());
				break;
			case NORMAL:
			default:
				leftButtonStyleRule.getNormalBackgroundRenderer().render(g, leftButton.getRenderX(),
						leftButton.getRenderY(), leftButton.getRenderWidth(), leftButton.getRenderHeight());
				break;
			}

			switch (rightButtonState) {
			case ACTION:
				rightButtonStyleRule.getActionBackgroundRenderer().render(g, rightButton.getRenderX(),
						rightButton.getRenderY(), rightButton.getRenderWidth(), rightButton.getRenderHeight());
				break;
			case HOVER:
				rightButtonStyleRule.getHoverBackgroundRenderer().render(g, rightButton.getRenderX(),
						rightButton.getRenderY(), rightButton.getRenderWidth(), rightButton.getRenderHeight());
				break;
			case NORMAL:
			default:
				rightButtonStyleRule.getNormalBackgroundRenderer().render(g, rightButton.getRenderX(),
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

			if (disabledStyleRule.getGameFont() != null) {
				g.setFont(disabledStyleRule.getGameFont());
			} else {
				g.setFont(fallbackFont);
			}

			g.drawString(text, leftButton.getRenderX() + leftButton.getRenderWidth() + enabledStyleRule.getPaddingLeft(),
					leftButton.getRenderY() + MathUtils.round((leftButton.getRenderHeight() * 0.5f) - (labelHeight * 0.5f))
					+ disabledStyleRule.getPaddingLeft(),
					getContentRenderWidth() - leftButton.getRenderWidth() - rightButton.getRenderWidth(),
					HorizontalAlignment.CENTER.getAlignValue());

			leftButtonStyleRule.getDisabledBackgroundRenderer().render(g, leftButton.getRenderX(),
					leftButton.getRenderY(), leftButton.getRenderWidth(), leftButton.getRenderHeight());
			rightButtonStyleRule.getDisabledBackgroundRenderer().render(g, rightButton.getRenderX(),
					rightButton.getRenderY(), rightButton.getRenderWidth(), rightButton.getRenderHeight());
		}

		if (element.getLeftButtonText() != null) {
			g.setColor(leftButtonLabelStyleRule.getColor());
			g.setFont(leftButtonLabelStyleRule.getGameFont());
			glyphLayout.setText(element.getLeftButtonText());

			int textRenderX = MathUtils
					.round(leftButton.getRenderX() + (leftButton.getRenderWidth() / 2) - (glyphLayout.getWidth() / 2f));
			int textRenderY = MathUtils
					.round(leftButton.getRenderY() + (leftButton.getRenderHeight() / 2) - (glyphLayout.getHeight() / 2f));
			g.drawString(element.getLeftButtonText(), textRenderX, textRenderY, glyphLayout.getWidth(), Align.CENTER);
		}
		if (element.getRightButtonText() != null) {
			g.setColor(rightButtonLabelStyleRule.getColor());
			g.setFont(rightButtonLabelStyleRule.getGameFont());
			glyphLayout.setText(element.getRightButtonText());

			int textRenderX = MathUtils
					.round(rightButton.getRenderX() + (rightButton.getRenderWidth() / 2) - (glyphLayout.getWidth() / 2f));
			int textRenderY = MathUtils
					.round(rightButton.getRenderY() + (rightButton.getRenderHeight() / 2) - (glyphLayout.getHeight() / 2f));
			g.drawString(element.getRightButtonText(), textRenderX, textRenderY, glyphLayout.getWidth(), Align.CENTER);
		}

		g.setColor(tmpColor);
		g.setFont(tmpFont);
	}

	@Override
	public void setState(NodeState state) {
		switch (state) {
		case HOVER:
			if (!element.isHoverEnabled())
				break;
			if (leftButtonState != NodeState.ACTION) {
				leftButtonState = NodeState.HOVER;
			}
			if (rightButtonState != NodeState.ACTION) {
				rightButtonState = NodeState.HOVER;
			}
			break;
		case NORMAL:
			if (leftButtonState != NodeState.ACTION) {
				leftButtonState = NodeState.NORMAL;
			}
			if (rightButtonState != NodeState.ACTION) {
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
		if (innerArea.contains(screenX, screenY)) {
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

		if (layoutRuleset.isHiddenByInputSource(layoutState)) {
			return 0f;
		}
		float layoutRuleResult = layoutRuleset.getPreferredElementWidth(layoutState);
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
		GameFont labelStyleFont = null;
		float labelMarginTop = 0f, labelMarginBottom = 0f;
		if (element.isEnabled()) {
			labelStyleFont = enabledStyleRule.getGameFont();
			labelMarginTop = enabledStyleRule.getMarginTop();
			labelMarginBottom = enabledStyleRule.getMarginBottom();
		} else {
			labelStyleFont = disabledStyleRule.getGameFont();
			labelMarginTop = disabledStyleRule.getMarginTop();
			labelMarginBottom = disabledStyleRule.getMarginBottom();
		}

		final String text = element.getTotalOptions() > 0 ? element.getSelectedLabel() : " ";
		if (labelStyleFont == null) {
			glyphLayout.setText(text, white, preferredContentWidth,
					HorizontalAlignment.CENTER.getAlignValue(), true);
		} else {
			if(!labelStyleFont.equals(glyphLayout.getFont())) {
				glyphLayout = labelStyleFont.newGlyphLayout();
			}
			glyphLayout.setText(text, white, preferredContentWidth,
					HorizontalAlignment.CENTER.getAlignValue(), true);
		}
		labelHeight = glyphLayout.getHeight() + labelMarginTop + labelMarginBottom;

		float result = labelHeight;
		if (style.getMinHeight() > 0 && result + style.getPaddingTop() + style.getPaddingBottom() + style.getMarginTop()
				+ style.getMarginBottom() < style.getMinHeight()) {
			result = style.getMinHeight() - style.getPaddingTop() - style.getPaddingBottom() - style.getMarginTop()
					- style.getMarginBottom();
		}
		float sizeRuleHeight = layoutRuleset.getPreferredElementHeight(layoutState) - style.getPaddingTop()
				- style.getPaddingBottom() - style.getMarginTop() - style.getMarginBottom();
		if (!layoutRuleset.getCurrentHeightRule().isAutoSize()) {
			result = Math.max(result, sizeRuleHeight);
		}
		leftButton.setHeight(result);
		rightButton.setHeight(result);
		return result;
	}

	@Override
	protected float determineXOffset(LayoutState layoutState) {
		return layoutRuleset.getPreferredElementRelativeX(layoutState);
	}

	@Override
	protected float determineYOffset(LayoutState layoutState) {
		return layoutRuleset.getPreferredElementRelativeY(layoutState);
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
		if (selectStyleRule.getLeftButtonLabelStyle() != null) {
			leftButtonLabelStyleRule = layoutState.getTheme().getLabelStyleRule(selectStyleRule.getLeftButtonLabelStyle(),
					layoutState.getScreenSize());
		} else {
			leftButtonLabelStyleRule = layoutState.getTheme().getLabelStyleRule(UiTheme.DEFAULT_STYLE_ID,
					layoutState.getScreenSize());
		}
		if (selectStyleRule.getRightButtonLabelStyle() != null) {
			rightButtonLabelStyleRule = layoutState.getTheme().getLabelStyleRule(selectStyleRule.getRightButtonLabelStyle(),
					layoutState.getScreenSize());
		} else {
			rightButtonLabelStyleRule = layoutState.getTheme().getLabelStyleRule(UiTheme.DEFAULT_STYLE_ID,
					layoutState.getScreenSize());
		}
		return selectStyleRule;
	}

	@Override
	public boolean isEnabled() {
		return element.isEnabled();
	}
}
