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
