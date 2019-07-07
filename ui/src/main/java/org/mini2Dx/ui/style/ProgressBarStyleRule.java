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
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.element.ProgressBar;

/**
 * Extends {@link StyleRule} for {@link ProgressBar} styling
 */
public class ProgressBarStyleRule extends StyleRule {
	@Field(optional = true)
	private String background;
	@Field
	private String fill;

	private BackgroundRenderer backgroundRenderer, fillRenderer;

	@Override
	public void validate(UiTheme theme) {
		super.validate(theme);
		if (getMinHeight() <= 0) {
			throw new MdxException(
					"minHeight value must be greater than 0 for " + ProgressBarStyleRule.class.getSimpleName());
		}
	}

	@Override
	public void prepareAssets(UiTheme theme, FileHandleResolver fileHandleResolver, AssetManager assetManager) {
		if (theme.isHeadless()) {
			return; 
		}
		if (background != null) {
			backgroundRenderer = BackgroundRenderer.parse(background);
			backgroundRenderer.prepareAssets(theme, fileHandleResolver, assetManager);
		}
		if(fill != null) {
			fillRenderer = BackgroundRenderer.parse(fill);
			fillRenderer.prepareAssets(theme, fileHandleResolver, assetManager);
		}
	}

	public BackgroundRenderer getBackgroundRenderer() {
		return backgroundRenderer;
	}

	public BackgroundRenderer getFillRenderer() {
		return fillRenderer;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public String getFill() {
		return fill;
	}

	public void setFill(String fill) {
		this.fill = fill;
	}
}
