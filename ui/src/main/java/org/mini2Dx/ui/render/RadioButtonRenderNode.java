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
import org.mini2Dx.core.font.GameFont;
import org.mini2Dx.core.font.GameFontCache;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.gdx.Input;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.ui.element.RadioButton;
import org.mini2Dx.ui.event.EventTrigger;
import org.mini2Dx.ui.event.params.EventTriggerParams;
import org.mini2Dx.ui.event.params.EventTriggerParamsPool;
import org.mini2Dx.ui.event.params.MouseEventTriggerParams;
import org.mini2Dx.ui.layout.HorizontalAlignment;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.style.RadioButtonStyleRule;

import java.util.Iterator;

/**
 *
 */
public class RadioButtonRenderNode extends RenderNode<RadioButton, RadioButtonStyleRule>
		implements ActionableRenderNode {
	protected final Array<Rectangle> buttonRenderPositions = new Array<Rectangle>(true, 1, Rectangle.class);

	protected GameFont font;
	protected GameFontCache fontCache = Mdx.fonts.defaultFont().newCache();
	protected String previousFont;
	protected int lineHeight;
	protected float calculatedHeight;
	protected int hoveredIndex;

	public RadioButtonRenderNode(ParentRenderNode<?, ?> parent, RadioButton element) {
		super(parent, element);
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		boolean result = super.mouseMoved(screenX, screenY);
		hoveredIndex = -1;
		for (int i = 0; i < buttonRenderPositions.size; i++) {
			if (buttonRenderPositions.get(i).contains(screenX - getContentRenderX(), screenY - getContentRenderY())) {
				hoveredIndex = i;
				break;
			}
		}
		return result;
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
		if (element.getTotalOptions() != buttonRenderPositions.size) {
			return;
		}
		renderBackground(g);

		fontCache.setPosition(getContentRenderX(), getContentRenderY());

		for (int i = 0; i < element.getTotalOptions(); i++) {
			Rectangle buttonRenderPosition = buttonRenderPositions.get(i);
			renderButton(g, i, buttonRenderPosition.getX() + getContentRenderX(),
					buttonRenderPosition.getY() + getContentRenderY(), i == hoveredIndex);
		}
		g.drawFontCache(fontCache);
	}

	private void renderButton(Graphics g, int index, float renderX, float renderY, boolean hovered) {
		TextureRegion textureRegion = null;
		if (element.isEnabled()) {
			if (index == element.getSelectedOptionIndex()) {
				if (hovered) {
					textureRegion = style.getActiveHoverTextureRegion();
				} else {
					textureRegion = style.getActiveTextureRegion();
				}
			} else {
				if (hovered) {
					textureRegion = style.getInactiveHoverTextureRegion();
				} else {
					textureRegion = style.getInactiveTextureRegion();
				}
			}
		} else {
			if (index == element.getSelectedOptionIndex()) {
				if (hovered) {
					textureRegion = style.getDisabledActiveHoverTextureRegion();
				} else {
					textureRegion = style.getDisabledActiveTextureRegion();
				}
			} else {
				if (hovered) {
					textureRegion = style.getDisabledInactiveHoverTextureRegion();
				} else {
					textureRegion = style.getDisabledInactiveTextureRegion();
				}
			}
		}
		g.drawTextureRegion(textureRegion, renderX, renderY);
	}

	@Override
	public void beginAction(EventTrigger eventTrigger, EventTriggerParams eventTriggerParams) {
		element.notifyActionListenersOfBeginEvent(eventTrigger, eventTriggerParams);
	}

	@Override
	public void endAction(EventTrigger eventTrigger, EventTriggerParams eventTriggerParams) {
		switch (eventTrigger) {
		case CONTROLLER:
		case KEYBOARD:
			element.selectNextOption();
			break;
		default:
			MouseEventTriggerParams mouseEventTriggerParams = (MouseEventTriggerParams) eventTriggerParams;
			int clickX = mouseEventTriggerParams.getMouseX() - getContentRenderX();
			int clickY = mouseEventTriggerParams.getMouseY() - getContentRenderY();

			for (int i = 0; i < buttonRenderPositions.size; i++) {
				if (buttonRenderPositions.get(i).contains(clickX, clickY)) {
					element.setSelectedOptionIndex(i);
					break;
				}
			}
			break;
		}
		element.notifyActionListenersOfEndEvent(eventTrigger, eventTriggerParams);
	}

	@Override
	protected float determinePreferredContentWidth(LayoutState layoutState) {
		float availableWidth = layoutState.getParentWidth() - style.getPaddingLeft() - style.getPaddingRight()
				- style.getMarginLeft() - style.getMarginRight();

		Iterator<String> options = element.getOptions();
		lineHeight = MathUtils.round(
				Math.max(style.getGameFont().getLineHeight(), style.getActiveTextureRegion().getRegionHeight()));

		int buttonX = 0;
		int buttonY = MathUtils.round((lineHeight / 2f) - (style.getActiveTextureRegion().getRegionHeight() / 2));
		int textX = style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent();
		int textY = MathUtils.round((lineHeight / 2f) - (style.getGameFont().getCapHeight() / 2f));
		float minX = availableWidth;
		float maxX = 0;

		switch (element.getFlexDirection()) {
		case COLUMN: {
			int i = 0;
			while (options.hasNext()) {
				String nextOption = options.next();
				font.getSharedGlyphLayout().setText(nextOption);
				if (textX + font.getSharedGlyphLayout().getWidth() >= availableWidth) {
					textX = style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent();
					buttonX = 0;
					textY += lineHeight + style.getOptionsSpacing();
					buttonY += lineHeight + style.getOptionsSpacing();
				}
				if (buttonX + style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent()
						+ font.getSharedGlyphLayout().getWidth() > maxX) {
					maxX = buttonX + style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent()
							+ font.getSharedGlyphLayout().getWidth();
				}

				pushButtonRenderPosition(i, buttonX, buttonY, MathUtils.round(
						style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent() + font.getSharedGlyphLayout().getWidth()),
						lineHeight);
				fontCache.addText(nextOption, textX, textY, availableWidth,
						HorizontalAlignment.LEFT.getAlignValue(), true);
				buttonX += style.getLabelIndent() + font.getSharedGlyphLayout().getWidth() + style.getActiveTextureRegion().getRegionWidth()
						+ style.getOptionsSpacing();
				textX = buttonX + style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent();
				i++;
			}
			break;
		}
		case COLUMN_REVERSE: {
			if (element.isResponsive()) {
				textX = MathUtils.round(availableWidth);
				int i = 0;
				while (options.hasNext()) {
					String nextOption = options.next();
					font.getSharedGlyphLayout().setText(nextOption);
					textX -= font.getSharedGlyphLayout().getWidth();
					buttonX = textX - style.getLabelIndent() - style.getActiveTextureRegion().getRegionWidth();
					if (buttonX <= 0) {
						textX = MathUtils.round(availableWidth);
						buttonX = textX - style.getLabelIndent() - style.getActiveTextureRegion().getRegionWidth();
						textY += lineHeight + style.getOptionsSpacing();
						buttonY += lineHeight + style.getOptionsSpacing();
					}
					if (buttonX < minX) {
						minX = buttonX;
					}
					pushButtonRenderPosition(i, buttonX, buttonY,
							MathUtils.round(style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent()
									+ font.getSharedGlyphLayout().getWidth()),
							lineHeight);
					fontCache.addText(nextOption, textX, textY, availableWidth,
							HorizontalAlignment.LEFT.getAlignValue(), true);
					textX = buttonX - style.getOptionsSpacing();
					i++;
				}
			} else {
				for (int i = element.getTotalOptions() - 1; i >= 0; i--) {
					String nextOption = element.getOption(i);
					font.getSharedGlyphLayout().setText(nextOption);
					if (textX + font.getSharedGlyphLayout().getWidth() >= availableWidth) {
						textX = style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent();
						buttonX = 0;
						textY += lineHeight + style.getOptionsSpacing();
						buttonY += lineHeight + style.getOptionsSpacing();
					}
					if (buttonX + style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent()
							+ font.getSharedGlyphLayout().getWidth() > maxX) {
						maxX = buttonX + style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent()
								+ font.getSharedGlyphLayout().getWidth();
					}

					pushButtonRenderPosition(i, buttonX, buttonY,
							MathUtils.round(style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent()
									+ font.getSharedGlyphLayout().getWidth()),
							lineHeight);
					fontCache.addText(nextOption, textX, textY, availableWidth,
							HorizontalAlignment.LEFT.getAlignValue(), true);
					buttonX += style.getLabelIndent() + font.getSharedGlyphLayout().getWidth()
							+ style.getActiveTextureRegion().getRegionWidth() + style.getOptionsSpacing();
					textX = buttonX + style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent();
				}
			}
			break;
		}
		case ROW_REVERSE: {
			int maxY = buttonY;
			while (options.hasNext()) {
				options.next();
				maxY += lineHeight + style.getOptionsSpacing();
			}
			int buttonTextYDiff = textY - buttonY;
			textY = maxY + buttonTextYDiff;
			maxY -= lineHeight + style.getOptionsSpacing();
			options = element.getOptions();

			int i = 0;
			while (options.hasNext()) {
				String nextOption = options.next();
				font.getSharedGlyphLayout().setText(nextOption);
				if (buttonX + style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent()
						+ font.getSharedGlyphLayout().getWidth() > maxX) {
					maxX = buttonX + style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent()
							+ font.getSharedGlyphLayout().getWidth();
				}
				pushButtonRenderPosition(i, buttonX, maxY, MathUtils.round(availableWidth), lineHeight);
				fontCache.addText(nextOption, textX, maxY + buttonTextYDiff, availableWidth,
						HorizontalAlignment.LEFT.getAlignValue(), true);
				maxY -= lineHeight + style.getOptionsSpacing();
				buttonY += lineHeight + style.getOptionsSpacing();
				i++;
			}
			
			break;
		}
		case ROW:
		default: {
			int i = 0;
			while (options.hasNext()) {
				String nextOption = options.next();
				font.getSharedGlyphLayout().setText(nextOption);
				if (buttonX + style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent()
						+ font.getSharedGlyphLayout().getWidth() > maxX) {
					maxX = buttonX + style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent()
							+ font.getSharedGlyphLayout().getWidth();
				}
				pushButtonRenderPosition(i, buttonX, buttonY, MathUtils.round(availableWidth), lineHeight);
				fontCache.addText(nextOption, textX, textY, availableWidth,
						HorizontalAlignment.LEFT.getAlignValue(), true);
				textY += lineHeight + style.getOptionsSpacing();
				buttonY += lineHeight + style.getOptionsSpacing();
				i++;
			}
			break;
		}
		}

		calculatedHeight = textY + lineHeight;

		if (element.isResponsive()) {
			return style.getRounding().calculateRounding(availableWidth);
		} else {
			return style.getRounding().calculateRounding(maxX);
		}
	}

	private void pushButtonRenderPosition(int index, int x, int y, int width, int height) {
		if (index < buttonRenderPositions.size) {
			buttonRenderPositions.get(index).set(x, y, width, height);
		} else {
			buttonRenderPositions.add(new Rectangle(x, y, width, height));
		}
	}

	@Override
	protected float determinePreferredContentHeight(LayoutState layoutState) {
		float result = calculatedHeight;
		if (style.getMinHeight() > 0 && result + style.getPaddingTop() + style.getPaddingBottom() + style.getMarginTop()
				+ style.getMarginBottom() < style.getMinHeight()) {
			result = style.getMinHeight() - style.getPaddingTop() - style.getPaddingBottom() - style.getMarginTop()
					- style.getMarginBottom();
		}
		return result;
	}

	@Override
	protected RadioButtonStyleRule determineStyleRule(LayoutState layoutState) {
		RadioButtonStyleRule result = layoutState.getTheme().getStyleRule(element, layoutState.getScreenSize());

		if (fontCache != null) {
			fontCache.clear();
		}
		if (previousFont == null || !previousFont.equals(result.getFont())) {
			fontCache = null;
			font = result.getGameFont();
			fontCache = result.getGameFont().newCache();
			previousFont = result.getFont();
		}
		fontCache.setColor(result.getColor());
		return result;
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
