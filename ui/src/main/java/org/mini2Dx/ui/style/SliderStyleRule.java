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
package org.mini2Dx.ui.style;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.core.serialization.annotation.Field;

/**
 *
 */
public class SliderStyleRule extends ParentStyleRule {
	
	@Field(optional=true)
	private String sliderBar;
	@Field(optional=true)
	private int sliderBarMaxHeight = 0;
	@Field
	private String normal;
	@Field
	private String active;
	@Field
	private String hover;
	@Field
	private String disabled;
	
	private BackgroundRenderer sliderBarRenderer;
	private TextureRegion normalTextureRegion, activeTextureRegion, hoverTextureRegion, disabledTextureRegion;
	
	@Override
	public void validate(UiTheme theme) {
		super.validate(theme);
		if(sliderBar == null || sliderBar.isEmpty()) {
			return;
		}
	}
	
	@Override
	public void prepareAssets(UiTheme theme, FileHandleResolver fileHandleResolver, AssetManager assetManager) {
		if (theme.isHeadless()) {
			return; 
		}
		super.prepareAssets(theme, fileHandleResolver, assetManager);
		
		if(sliderBar != null) {
			sliderBarRenderer = BackgroundRenderer.parse(sliderBar);
			sliderBarRenderer.prepareAssets(theme, fileHandleResolver, assetManager);
		}
		normalTextureRegion = Mdx.graphics.newTextureRegion(theme.getTextureAtlas().findRegion(normal));
		activeTextureRegion = Mdx.graphics.newTextureRegion(theme.getTextureAtlas().findRegion(active));
		hoverTextureRegion = Mdx.graphics.newTextureRegion(theme.getTextureAtlas().findRegion(hover));
		disabledTextureRegion = Mdx.graphics.newTextureRegion(theme.getTextureAtlas().findRegion(disabled));
	}

	public BackgroundRenderer getSliderBarRenderer() {
		return sliderBarRenderer;
	}

	public TextureRegion getNormalTextureRegion() {
		return normalTextureRegion;
	}

	public TextureRegion getActiveTextureRegion() {
		return activeTextureRegion;
	}

	public TextureRegion getHoverTextureRegion() {
		return hoverTextureRegion;
	}

	public TextureRegion getDisabledTextureRegion() {
		return disabledTextureRegion;
	}

	public int getSliderBarMaxHeight() {
		return sliderBarMaxHeight;
	}

	public void setSliderBarMaxHeight(int sliderBarMaxHeight) {
		this.sliderBarMaxHeight = sliderBarMaxHeight;
	}
	
	
}
