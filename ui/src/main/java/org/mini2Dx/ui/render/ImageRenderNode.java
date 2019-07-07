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
import org.mini2Dx.ui.element.Image;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.style.StyleRule;

/**
 * {@link RenderNode} implementation for {@link Image}
 */
public class ImageRenderNode extends RenderNode<Image, StyleRule> {
	protected TextureRegion textureRegion;

	public ImageRenderNode(ParentRenderNode<?, ?> parent, Image element) {
		super(parent, element);
	}

	@Override
	protected void renderElement(Graphics g) {
		if (textureRegion == null) {
			return;
		}
		textureRegion.setFlip(element.isFlipX(), element.isFlipY());
		g.drawTextureRegion(textureRegion, getContentRenderX(), getContentRenderY(), getContentRenderWidth(),
				getContentRenderHeight());
	}

	@Override
	public void layout(LayoutState layoutState) {
		textureRegion = element.getTextureRegion(layoutState.getAssetManager());
		super.layout(layoutState);
	}

	@Override
	protected float determinePreferredContentWidth(LayoutState layoutState) {
		if (textureRegion == null) {
			return 0f;
		}
		if(parent.getElement().isFlexLayout()) {
			if (element.isResponsive()) {
				return layoutState.getParentWidth() - style.getPaddingLeft() - style.getPaddingRight()
						- style.getMarginLeft() - style.getMarginRight();
			}
			return textureRegion.getRegionWidth();
		} else {
			if (element.isResponsive()) {
				return element.getWidth() - style.getPaddingLeft() - style.getPaddingRight()
						- style.getMarginLeft() - style.getMarginRight();
			}
			return textureRegion.getRegionWidth();
		}
	}

	@Override
	protected float determinePreferredContentHeight(LayoutState layoutState) {
		if (textureRegion == null) {
			return 0f;
		}
		float result = 0f;
		if(parent.getElement().isFlexLayout()) {
			if (element.isResponsive()) {
				result = textureRegion.getRegionHeight() * (preferredContentWidth / textureRegion.getRegionWidth());
			} else {
				result = textureRegion.getRegionHeight();
			}
		} else {
			if (element.isResponsive()) {
				result = element.getHeight() - style.getPaddingTop() - style.getPaddingBottom() - style.getMarginTop()
						- style.getMarginBottom();
			} else {
				result = textureRegion.getRegionHeight();
			}
		}

		if (style.getMinHeight() > 0 && result + style.getPaddingTop() + style.getPaddingBottom() + style.getMarginTop()
				+ style.getMarginBottom() < style.getMinHeight()) {
			return style.getMinHeight() - style.getPaddingTop() - style.getPaddingBottom() - style.getMarginTop()
					- style.getMarginBottom();
		}
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
	protected StyleRule determineStyleRule(LayoutState layoutState) {
		return layoutState.getTheme().getStyleRule(element, layoutState.getScreenSize());
	}
}
