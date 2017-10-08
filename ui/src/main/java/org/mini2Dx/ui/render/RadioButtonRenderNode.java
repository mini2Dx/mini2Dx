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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.ui.element.RadioButton;
import org.mini2Dx.ui.event.EventTrigger;
import org.mini2Dx.ui.event.params.EventTriggerParams;
import org.mini2Dx.ui.event.params.EventTriggerParamsPool;
import org.mini2Dx.ui.event.params.MouseEventTriggerParams;
import org.mini2Dx.ui.layout.HorizontalAlignment;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.style.RadioButtonStyleRule;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.MathUtils;

/**
 *
 */
public class RadioButtonRenderNode extends RenderNode<RadioButton, RadioButtonStyleRule>
		implements ActionableRenderNode {
	protected static final GlyphLayout GLYPH_LAYOUT = new GlyphLayout();
	protected static final BitmapFont DEFAULT_FONT = new BitmapFont(true);

	protected final List<Rectangle> buttonRenderPositions = new ArrayList<Rectangle>();

	protected BitmapFontCache bitmapFontCache = DEFAULT_FONT.newFontCache();
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
		for (int i = 0; i < buttonRenderPositions.size(); i++) {
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
		if (element.getTotalOptions() != buttonRenderPositions.size()) {
			return;
		}
		if (style.getBackgroundNinePatch() != null) {
			g.drawNinePatch(style.getBackgroundNinePatch(), getInnerRenderX(), getInnerRenderY(), getInnerRenderWidth(),
					getInnerRenderHeight());
		}

		bitmapFontCache.setPosition(getContentRenderX(), getContentRenderY());

		for (int i = 0; i < element.getTotalOptions(); i++) {
			Rectangle buttonRenderPosition = buttonRenderPositions.get(i);
			renderButton(g, i, buttonRenderPosition.getX() + getContentRenderX(),
					buttonRenderPosition.getY() + getContentRenderY(), i == hoveredIndex);
		}
		g.drawBitmapFontCache(bitmapFontCache);
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

			for (int i = 0; i < buttonRenderPositions.size(); i++) {
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
				Math.max(style.getBitmapFont().getLineHeight(), style.getActiveTextureRegion().getRegionHeight()));

		int buttonX = 0;
		int buttonY = MathUtils.round((lineHeight / 2f) - (style.getActiveTextureRegion().getRegionHeight() / 2));
		int textX = style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent();
		int textY = MathUtils.round((lineHeight / 2f) - (style.getBitmapFont().getCapHeight() / 2f));
		float minX = availableWidth;
		float maxX = 0;

		switch (element.getFlexDirection()) {
		case COLUMN: {
			int i = 0;
			while (options.hasNext()) {
				String nextOption = options.next();
				GLYPH_LAYOUT.setText(style.getBitmapFont(), nextOption);
				if (textX + GLYPH_LAYOUT.width >= availableWidth) {
					textX = style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent();
					buttonX = 0;
					textY += lineHeight + style.getOptionsSpacing();
					buttonY += lineHeight + style.getOptionsSpacing();
				}
				if (buttonX + style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent()
						+ GLYPH_LAYOUT.width > maxX) {
					maxX = buttonX + style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent()
							+ GLYPH_LAYOUT.width;
				}

				pushButtonRenderPosition(i, buttonX, buttonY, MathUtils.round(
						style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent() + GLYPH_LAYOUT.width),
						lineHeight);
				bitmapFontCache.addText(nextOption, textX, textY, availableWidth,
						HorizontalAlignment.LEFT.getAlignValue(), true);
				buttonX += style.getLabelIndent() + GLYPH_LAYOUT.width + style.getActiveTextureRegion().getRegionWidth()
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
					GLYPH_LAYOUT.setText(style.getBitmapFont(), nextOption);
					textX -= GLYPH_LAYOUT.width;
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
									+ GLYPH_LAYOUT.width),
							lineHeight);
					bitmapFontCache.addText(nextOption, textX, textY, availableWidth,
							HorizontalAlignment.LEFT.getAlignValue(), true);
					textX = buttonX - style.getOptionsSpacing();
					i++;
				}
			} else {
				for (int i = element.getTotalOptions() - 1; i >= 0; i--) {
					String nextOption = element.getOption(i);
					GLYPH_LAYOUT.setText(style.getBitmapFont(), nextOption);
					if (textX + GLYPH_LAYOUT.width >= availableWidth) {
						textX = style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent();
						buttonX = 0;
						textY += lineHeight + style.getOptionsSpacing();
						buttonY += lineHeight + style.getOptionsSpacing();
					}
					if (buttonX + style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent()
							+ GLYPH_LAYOUT.width > maxX) {
						maxX = buttonX + style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent()
								+ GLYPH_LAYOUT.width;
					}

					pushButtonRenderPosition(i, buttonX, buttonY,
							MathUtils.round(style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent()
									+ GLYPH_LAYOUT.width),
							lineHeight);
					bitmapFontCache.addText(nextOption, textX, textY, availableWidth,
							HorizontalAlignment.LEFT.getAlignValue(), true);
					buttonX += style.getLabelIndent() + GLYPH_LAYOUT.width
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
				GLYPH_LAYOUT.setText(style.getBitmapFont(), nextOption);
				if (buttonX + style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent()
						+ GLYPH_LAYOUT.width > maxX) {
					maxX = buttonX + style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent()
							+ GLYPH_LAYOUT.width;
				}
				pushButtonRenderPosition(i, buttonX, maxY, MathUtils.round(availableWidth), lineHeight);
				bitmapFontCache.addText(nextOption, textX, maxY + buttonTextYDiff, availableWidth,
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
				GLYPH_LAYOUT.setText(style.getBitmapFont(), nextOption);
				if (buttonX + style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent()
						+ GLYPH_LAYOUT.width > maxX) {
					maxX = buttonX + style.getActiveTextureRegion().getRegionWidth() + style.getLabelIndent()
							+ GLYPH_LAYOUT.width;
				}
				pushButtonRenderPosition(i, buttonX, buttonY, MathUtils.round(availableWidth), lineHeight);
				bitmapFontCache.addText(nextOption, textX, textY, availableWidth,
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
			return availableWidth;
		} else {
			return maxX;
		}
	}

	private void pushButtonRenderPosition(int index, int x, int y, int width, int height) {
		if (index < buttonRenderPositions.size()) {
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

		if (bitmapFontCache != null) {
			bitmapFontCache.clear();
		}
		if (previousFont == null || !previousFont.equals(result.getFont())) {
			bitmapFontCache = null;
			bitmapFontCache = result.getBitmapFont().newFontCache();
			previousFont = result.getFont();
		}
		bitmapFontCache.setColor(result.getColor());
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
}
