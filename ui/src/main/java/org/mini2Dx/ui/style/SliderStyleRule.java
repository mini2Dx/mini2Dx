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

import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.graphics.NinePatch;
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.core.serialization.annotation.Field;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;

/**
 *
 */
public class SliderStyleRule extends ParentStyleRule {
	
	@Field(optional=true)
	private String sliderBar;
	@Field(optional=true)
	private int sliderBarNinePatchTop = -1;
	@Field(optional=true)
	private int sliderBarNinePatchLeft = -1;
	@Field(optional=true)
	private int sliderBarNinePatchRight = -1;
	@Field(optional=true)
	private int sliderBarNinePatchBottom = -1;
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
	
	private NinePatch sliderBarNinePatch;
	private TextureRegion normalTextureRegion, activeTextureRegion, hoverTextureRegion, disabledTextureRegion;
	
	@Override
	public void validate(UiTheme theme) {
		super.validate(theme);
		if(sliderBar == null) {
			return;
		}
		if(sliderBarNinePatchTop < 0) {
			throw new MdxException("sliderBarNinePatchTop must be greater than or equal to 0. Required by " + SliderStyleRule.class.getSimpleName());
		}
		if(sliderBarNinePatchBottom < 0) {
			throw new MdxException("sliderBarNinePatchBottom must be greater than or equal to 0. Required by " + SliderStyleRule.class.getSimpleName());
		}
		if(sliderBarNinePatchLeft < 0) {
			throw new MdxException("sliderBarNinePatchLeft must be greater than or equal to 0. Required by " + SliderStyleRule.class.getSimpleName());
		}
		if(sliderBarNinePatchRight < 0) {
			throw new MdxException("sliderBarNinePatchRight must be greater than or equal to 0. Required by " + SliderStyleRule.class.getSimpleName());
		}
	}
	
	@Override
	public void prepareAssets(UiTheme theme, FileHandleResolver fileHandleResolver, AssetManager assetManager) {
		if (theme.isHeadless()) {
			return; 
		}
		super.prepareAssets(theme, fileHandleResolver, assetManager);
		
		if(sliderBar != null) {
			sliderBarNinePatch = new NinePatch(new TextureRegion(theme.getTextureAtlas().findRegion(sliderBar)), sliderBarNinePatchLeft,
					sliderBarNinePatchRight, sliderBarNinePatchTop, sliderBarNinePatchBottom);
		}
		normalTextureRegion = new TextureRegion(theme.getTextureAtlas().findRegion(normal));
		activeTextureRegion = new TextureRegion(theme.getTextureAtlas().findRegion(active));
		hoverTextureRegion = new TextureRegion(theme.getTextureAtlas().findRegion(hover));
		disabledTextureRegion = new TextureRegion(theme.getTextureAtlas().findRegion(disabled));
	}

	public NinePatch getSliderBarNinePatch() {
		return sliderBarNinePatch;
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
