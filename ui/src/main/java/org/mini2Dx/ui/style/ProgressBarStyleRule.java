/**
 * Copyright (c) 2016 See AUTHORS file
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
