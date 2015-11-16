/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.ui.theme;

import org.mini2Dx.core.serialization.annotation.Field;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;

/**
 *
 */
public class CheckBoxStyle extends BaseUiElementStyle {
	@Field
	private String normalImage;
	@Field
	private String hoverImage;
	@Field
	private String disabledImage;
	@Field
	private String normalCheckIcon;
	@Field
	private String disabledCheckIcon;
	
	private Texture normalCheckIconTexture, disabledCheckIconTexture;
	private NinePatch normalNinePatch, hoverNinePatch, disabledNinePatch;
	
	@Override
	public void prepareAssets(AssetManager assetManager) {
		normalCheckIconTexture = assetManager.get(normalCheckIcon, Texture.class);
		disabledCheckIconTexture = assetManager.get(disabledCheckIcon, Texture.class);
		normalNinePatch = new NinePatch(assetManager.get(normalImage, Texture.class), getPaddingLeft(),
				getPaddingRight(), getPaddingTop(), getPaddingBottom());
		hoverNinePatch = new NinePatch(assetManager.get(hoverImage, Texture.class), getPaddingLeft(),
				getPaddingRight(), getPaddingTop(), getPaddingBottom());
		disabledNinePatch = new NinePatch(assetManager.get(disabledImage, Texture.class), getPaddingLeft(),
				getPaddingRight(), getPaddingTop(), getPaddingBottom());
	}
	
	public String getNormalImage() {
		return normalImage;
	}
	
	public void setNormalImage(String uncheckedImage) {
		this.normalImage = uncheckedImage;
	}
	
	public String getNormalCheckIcon() {
		return normalCheckIcon;
	}
	
	public void setNormalCheckIcon(String checkedImage) {
		this.normalCheckIcon = checkedImage;
	}

	public String getDisabledCheckIcon() {
		return disabledCheckIcon;
	}

	public void setDisabledCheckIcon(String disabledCheckIcon) {
		this.disabledCheckIcon = disabledCheckIcon;
	}

	public NinePatch getNormalNinePatch() {
		return normalNinePatch;
	}

	public NinePatch getDisabledNinePatch() {
		return disabledNinePatch;
	}

	public String getDisabledImage() {
		return disabledImage;
	}

	public void setDisabledImage(String disabledImage) {
		this.disabledImage = disabledImage;
	}

	public String getHoverImage() {
		return hoverImage;
	}

	public void setHoverImage(String hoverImage) {
		this.hoverImage = hoverImage;
	}

	public Texture getNormalCheckIconTexture() {
		return normalCheckIconTexture;
	}

	public Texture getDisabledCheckIconTexture() {
		return disabledCheckIconTexture;
	}

	public NinePatch getHoverNinePatch() {
		return hoverNinePatch;
	}
}
