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
import org.mini2Dx.ui.element.AnimatedImage;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.style.StyleRule;

/**
 *
 */
public class AnimatedImageRenderNode extends RenderNode<AnimatedImage, StyleRule> {
	private TextureRegion[] textureRegions;
	private int frame;
	private float timer;

	public AnimatedImageRenderNode(ParentRenderNode<?, ?> parent, AnimatedImage element) {
		super(parent, element);
	}

	@Override
	public void update(UiContainerRenderTree uiContainer, float delta) {
		super.update(uiContainer, delta);

		float frameDuration = getCurrentFrameDuration();
		timer += delta;
		if(timer >= frameDuration) {
			timer -= frameDuration;
			incrementFrame();
		}
	}

	@Override
	protected void renderElement(Graphics g) {
		if (textureRegions == null) {
			return;
		}
		if (textureRegions.length == 0) {
			return;
		}
		TextureRegion textureRegion = getCurrentTextureRegion();
		textureRegion.setFlip(element.isFlipX(), element.isFlipY());
		g.drawTextureRegion(textureRegion, getContentRenderX(), getContentRenderY(), getContentRenderWidth(),
				getContentRenderHeight());
	}

	@Override
	public void layout(LayoutState layoutState) {
		textureRegions = element.getTextureRegions(layoutState.getAssetManager());
		super.layout(layoutState);
	}

	@Override
	protected float determinePreferredContentWidth(LayoutState layoutState) {
		if (textureRegions == null || textureRegions.length == 0) {
			return 0f;
		}
		if (element.isResponsive()) {
			return layoutState.getParentWidth() - style.getPaddingLeft() - style.getPaddingRight()
					- style.getMarginLeft() - style.getMarginRight();
		} else {
			return textureRegions[0].getRegionWidth();
		}
	}

	@Override
	protected float determinePreferredContentHeight(LayoutState layoutState) {
		if (textureRegions == null || textureRegions.length == 0) {
			return 0f;
		}
		float result = 0f;
		if (element.isResponsive()) {
			result = textureRegions[0].getRegionHeight() * (preferredContentWidth / textureRegions[0].getRegionWidth());
		} else {
			result = textureRegions[0].getRegionHeight();
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

	private float getCurrentFrameDuration() {
		float [] frameDurations = element.getFrameDurations();
		return frameDurations[frame % frameDurations.length];
	}
	
	private TextureRegion getCurrentTextureRegion() {
		return textureRegions[frame % textureRegions.length];
	}
	
	private void incrementFrame() {
		float [] frameDurations = element.getFrameDurations();
		frame++;
		if(frame >= frameDurations.length) {
			frame = 0;
		}
	}
}