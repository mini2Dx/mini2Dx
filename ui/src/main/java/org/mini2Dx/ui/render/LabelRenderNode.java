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
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * {@link RenderNode} implementation for {@link Label}
 */
public class LabelRenderNode extends RenderNode<Label, LabelStyleRule> {
	protected static final GlyphLayout glyphLayout = new GlyphLayout();
	protected static final NullTextAnimation NULL_TEXT_ANIMATION = new NullTextAnimation();

	protected BitmapFont font = new BitmapFont(true);

	public LabelRenderNode(ParentRenderNode<?, ?> parent, Label element) {
		super(parent, element);
	}

	@Override
	public void update(UiContainerRenderTree uiContainer, float delta) {
		super.update(uiContainer, delta);
		if (element.getTextAnimation() != null) {
			element.getTextAnimation().update(element.getText(), delta);
		}
	}

	@Override
	public void interpolate(float alpha) {
		super.interpolate(alpha);
		if (element.getTextAnimation() != null) {
			element.getTextAnimation().interpolate(element.getText(), alpha);
		}
	}

	@Override
	protected void renderElement(Graphics g) {
		BitmapFont tmpFont = g.getFont();
		Color tmpColor = g.getColor();

		if (style.getBitmapFont() == null) {
			g.setFont(font);
		} else {
			g.setFont(style.getBitmapFont());
		}

		if(element.getColor() != null) {
			g.setColor(element.getColor());
		} else if(style.getColor() != null) {
			g.setColor(style.getColor());
		} else {
			throw new MdxException("Could not determine color for Label " + element.getId() + ". Please use Label#setColor or set a Color on the label style rule");
		}
		
		if (element.getTextAnimation() == null) {
			NULL_TEXT_ANIMATION.render(element.getText(), g, getContentRenderX(), getContentRenderY(),
					getContentRenderWidth(), element.getHorizontalAlignment().getAlignValue());
		} else {
			element.getTextAnimation().render(element.getText(), g, getContentRenderX(), getContentRenderY(),
					getContentRenderWidth(), element.getHorizontalAlignment().getAlignValue());
		}
		g.setColor(tmpColor);
		g.setFont(tmpFont);
	}

	@Override
	protected float determinePreferredContentWidth(LayoutState layoutState) {
		float availableWidth = layoutState.getParentWidth() - style.getPaddingLeft() - style.getPaddingRight()
				- style.getMarginLeft() - style.getMarginRight();
		if (element.isResponsive()) {
			return availableWidth;
		} else {
			if (style.getBitmapFont() == null) {
				glyphLayout.setText(font, element.getText());
			} else {
				glyphLayout.setText(style.getBitmapFont(), element.getText());
			}
			if (glyphLayout.width > availableWidth) {
				return availableWidth;
			}
			return glyphLayout.width;
		}
	}

	@Override
	protected float determinePreferredContentHeight(LayoutState layoutState) {
		if (style.getBitmapFont() == null) {
			glyphLayout.setText(font, element.getText(), Color.WHITE, preferredContentWidth,
					element.getHorizontalAlignment().getAlignValue(), true);
		} else {
			glyphLayout.setText(style.getBitmapFont(), element.getText(), Color.WHITE, preferredContentWidth,
					element.getHorizontalAlignment().getAlignValue(), true);
		}
		if(glyphLayout.height < style.getMinHeight()) {
			return style.getMinHeight();
		}
		return glyphLayout.height;
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
		return layoutState.getTheme().getStyleRule(element, layoutState.getScreenSize());
	}
}
