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

import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.element.ParentUiElement;
import org.mini2Dx.ui.render.SizeRounding;

/**
 * Extends {@link StyleRule} for {@link ParentUiElement} styling
 */
public class ParentStyleRule extends StyleRule {
	private static final String BG_STRETCH_MODE =  "stretch";
	private static final String BG_REPEAT_MODE =  "repeat";
	
	@Field(optional=true)
	private String background;
	@Field(optional=true)
	private String hoverBackground;
	@Field(optional=true)
	private String sizeRounding;
	
	private SizeRounding rounding = SizeRounding.NONE;
	private BackgroundRenderer normalBackgroundRenderer, hoverBackgroundRenderer;
	
	@Override
	public void prepareAssets(UiTheme theme, FileHandleResolver fileHandleResolver, AssetManager assetManager) {
		if (theme.isHeadless()) {
			return; 
		}
		
		if(background != null) {
			normalBackgroundRenderer = BackgroundRenderer.parse(background);
			normalBackgroundRenderer.prepareAssets(theme, fileHandleResolver, assetManager);
		}
		if(hoverBackground != null) {
			hoverBackgroundRenderer = BackgroundRenderer.parse(hoverBackground);
			hoverBackgroundRenderer.prepareAssets(theme, fileHandleResolver, assetManager);
		} else {
			hoverBackgroundRenderer = normalBackgroundRenderer;
		}
		if(sizeRounding != null) {
			rounding = SizeRounding.valueOf(sizeRounding.toUpperCase());
		}
	}

	public BackgroundRenderer getNormalBackgroundRenderer() {
		return normalBackgroundRenderer;
	}

	public BackgroundRenderer getHoverBackgroundRenderer() {
		return hoverBackgroundRenderer;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}
	
	public SizeRounding getRounding() {
		return rounding;
	}

	public void setRounding(SizeRounding rounding) {
		if(rounding == null) {
			return;
		}
		this.rounding = rounding;
	}

	public String getHoverBackground() {
		return hoverBackground;
	}

	public void setHoverBackground(String hoverBackground) {
		this.hoverBackground = hoverBackground;
	}
}
