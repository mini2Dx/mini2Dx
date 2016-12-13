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

import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.graphics.NinePatch;
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.element.ProgressBar;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;

/**
 * Extends {@link StyleRule} for {@link ProgressBar} styling
 */
public class ProgressBarStyleRule extends StyleRule {
	@Field(optional = true)
	private String background;
	@Field(optional = true)
	private int backgroundNinePatchTop, backgroundNinePatchBottom, backgroundNinePatchLeft, backgroundNinePatchRight;
	@Field
	private String fill;
	@Field
	private int fillNinePatchTop, fillNinePatchBottom, fillNinePatchLeft, fillNinePatchRight;

	private NinePatch backgroundNinePatch, fillNinePatch;

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
			backgroundNinePatch = new NinePatch(new TextureRegion(theme.getTextureAtlas().findRegion(background)),
					getBackgroundNinePatchLeft(), getBackgroundNinePatchRight(), getBackgroundNinePatchTop(),
					getBackgroundNinePatchBottom());
		}
		fillNinePatch = new NinePatch(new TextureRegion(theme.getTextureAtlas().findRegion(fill)), fillNinePatchLeft,
				fillNinePatchRight, fillNinePatchTop, fillNinePatchBottom);
	}

	public NinePatch getBackgroundNinePatch() {
		return backgroundNinePatch;
	}
	
	public NinePatch getFillNinePatch() {
		return fillNinePatch;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public int getBackgroundNinePatchTop() {
		if (backgroundNinePatchTop <= 0) {
			return getPaddingTop();
		}
		return backgroundNinePatchTop;
	}

	public int getBackgroundNinePatchBottom() {
		if (backgroundNinePatchBottom <= 0) {
			return getPaddingBottom();
		}
		return backgroundNinePatchBottom;
	}

	public int getBackgroundNinePatchLeft() {
		if (backgroundNinePatchLeft <= 0) {
			return getPaddingLeft();
		}
		return backgroundNinePatchLeft;
	}

	public int getBackgroundNinePatchRight() {
		if (backgroundNinePatchRight <= 0) {
			return getPaddingRight();
		}
		return backgroundNinePatchRight;
	}

	public String getFill() {
		return fill;
	}

	public void setFill(String fill) {
		this.fill = fill;
	}

	public int getFillNinePatchTop() {
		return fillNinePatchTop;
	}

	public void setFillNinePatchTop(int fillNinePatchTop) {
		this.fillNinePatchTop = fillNinePatchTop;
	}

	public int getFillNinePatchBottom() {
		return fillNinePatchBottom;
	}

	public void setFillNinePatchBottom(int fillNinePatchBottom) {
		this.fillNinePatchBottom = fillNinePatchBottom;
	}

	public int getFillNinePatchLeft() {
		return fillNinePatchLeft;
	}

	public void setFillNinePatchLeft(int fillNinePatchLeft) {
		this.fillNinePatchLeft = fillNinePatchLeft;
	}

	public int getFillNinePatchRight() {
		return fillNinePatchRight;
	}

	public void setFillNinePatchRight(int fillNinePatchRight) {
		this.fillNinePatchRight = fillNinePatchRight;
	}
}
