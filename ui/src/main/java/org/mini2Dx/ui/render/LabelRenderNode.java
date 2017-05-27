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

import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.ui.animation.NullTextAnimation;
import org.mini2Dx.ui.element.Label;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.style.LabelStyleRule;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * {@link RenderNode} implementation for {@link Label}
 */
public class LabelRenderNode extends RenderNode<Label, LabelStyleRule> {
	protected static final GlyphLayout GLYPH_LAYOUT = new GlyphLayout();
	protected static final BitmapFont DEFAULT_FONT = new BitmapFont(true);

	protected final NullTextAnimation nullAnimation = new NullTextAnimation();
	protected BitmapFontCache bitmapFontCache = DEFAULT_FONT.newFontCache();

	public LabelRenderNode(ParentRenderNode<?, ?> parent, Label element) {
		super(parent, element);
	}

	@Override
	public void update(UiContainerRenderTree uiContainer, float delta) {
		super.update(uiContainer, delta);
		if (element.getTextAnimation() == null) {
			nullAnimation.update(bitmapFontCache, element.getText(), preferredContentWidth,
					element.getHorizontalAlignment().getAlignValue(), delta);
		} else {
			element.getTextAnimation().update(bitmapFontCache, element.getText(), preferredContentWidth,
					element.getHorizontalAlignment().getAlignValue(), delta);
		}
	}

	@Override
	public void interpolate(float alpha) {
		super.interpolate(alpha);
		if (element.getTextAnimation() == null) {
			nullAnimation.interpolate(bitmapFontCache, element.getText(), alpha);
		} else {
			element.getTextAnimation().interpolate(bitmapFontCache, element.getText(), alpha);
		}
	}

	@Override
	protected void renderElement(Graphics g) {
		if (style.getBackgroundNinePatch() != null) {
			g.drawNinePatch(style.getBackgroundNinePatch(), getInnerRenderX(), getInnerRenderY(), getInnerRenderWidth(),
					getInnerRenderHeight());
		}

		if (element.getTextAnimation() == null) {
			nullAnimation.render(bitmapFontCache, g, getContentRenderX(), getContentRenderY());
		} else {
			element.getTextAnimation().render(bitmapFontCache, g, getContentRenderX(), getContentRenderY());
		}
	}

	@Override
	protected float determinePreferredContentWidth(LayoutState layoutState) {
		float availableWidth = layoutState.getParentWidth() - style.getPaddingLeft() - style.getPaddingRight()
				- style.getMarginLeft() - style.getMarginRight();
		if (element.isResponsive()) {
			return availableWidth;
		} else {
			GLYPH_LAYOUT.setText(bitmapFontCache.getFont(), element.getText());

			if (GLYPH_LAYOUT.width > availableWidth) {
				return availableWidth;
			}
			return GLYPH_LAYOUT.width;
		}
	}

	@Override
	protected float determinePreferredContentHeight(LayoutState layoutState) {
		GLYPH_LAYOUT.setText(bitmapFontCache.getFont(), element.getText(), Color.WHITE, preferredContentWidth,
				element.getHorizontalAlignment().getAlignValue(), true);
		if (GLYPH_LAYOUT.height < style.getMinHeight()) {
			return style.getMinHeight();
		}
		return GLYPH_LAYOUT.height;
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
	protected LabelStyleRule determineStyleRule(LayoutState layoutState) {
		if (bitmapFontCache != null) {
			bitmapFontCache.clear();
			bitmapFontCache = null;
			nullAnimation.reset();

			if (element.getTextAnimation() != null) {
				element.getTextAnimation().reset();
			}
		}

		LabelStyleRule result = layoutState.getTheme().getStyleRule(element, layoutState.getScreenSize(),
				layoutState.getScreenSizeScale());
		if (result.getBitmapFont() == null) {
			bitmapFontCache = DEFAULT_FONT.newFontCache();
		} else {
			bitmapFontCache = result.getBitmapFont().newFontCache();
		}

		if (element.getColor() != null) {
			bitmapFontCache.setColor(element.getColor());
		} else if (result.getColor() != null) {
			bitmapFontCache.setColor(result.getColor());
		} else {
			throw new MdxException("Could not determine color for Label " + element.getId()
					+ ". Please use Label#setColor or set a Color on the label style rule");
		}
		return result;
	}

	public void updateBitmapFontCache() {
		if (style == null) {
			return;
		}
		bitmapFontCache.clear();
		nullAnimation.reset();

		GLYPH_LAYOUT.setText(bitmapFontCache.getFont(), element.getText(), Color.WHITE, preferredContentWidth,
				element.getHorizontalAlignment().getAlignValue(), true);
		if (GLYPH_LAYOUT.height == getPreferredContentHeight()) {
			return;
		}
		setDirty(true);
	}
}
