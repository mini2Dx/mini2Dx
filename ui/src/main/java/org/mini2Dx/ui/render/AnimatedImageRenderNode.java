/**
 * Copyright (c) 2018 See AUTHORS file
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

import org.mini2Dx.core.graphics.Graphics;
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
		return 0f;
	}

	@Override
	protected float determineYOffset(LayoutState layoutState) {
		return 0f;
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