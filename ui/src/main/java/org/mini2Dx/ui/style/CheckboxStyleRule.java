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
public class CheckboxStyleRule extends ParentStyleRule {
	@Field
	private String enabledBox;
	@Field
	private String disabledBox;
	@Field
	private String disabledCheck;
	@Field(optional = true)
	private String disabledUncheck;
	@Field(optional = true)
	private String hoveredCheck;
	@Field(optional = true)
	private String hoveredUncheck;
	@Field
	private String enabledCheck;
	@Field(optional = true)
	private String enabledUncheck;

	private BackgroundRenderer enabledBackgroundRenderer, disabledBackgroundRenderer;
	private TextureRegion disabledCheckTextureRegion, disabledUncheckTextureRegion, enabledCheckTextureRegion,
			enabledUncheckTextureRegion, hoveredCheckTextureRegion, hoveredUncheckTextureRegion;
	
	@Override
	public void prepareAssets(UiTheme theme, FileHandleResolver fileHandleResolver, AssetManager assetManager) {
		if (theme.isHeadless()) {
			return; 
		}
		super.prepareAssets(theme, fileHandleResolver, assetManager);

		enabledBackgroundRenderer = BackgroundRenderer.parse(enabledBox);
		enabledBackgroundRenderer.prepareAssets(theme, fileHandleResolver, assetManager);

		disabledBackgroundRenderer = BackgroundRenderer.parse(disabledBox);
		disabledBackgroundRenderer.prepareAssets(theme, fileHandleResolver, assetManager);

		enabledCheckTextureRegion = Mdx.graphics.newTextureRegion(theme.getTextureAtlas().findRegion(enabledCheck));
		disabledCheckTextureRegion = Mdx.graphics.newTextureRegion(theme.getTextureAtlas().findRegion(disabledCheck));
		
		if(disabledUncheck != null) {
			disabledUncheckTextureRegion = Mdx.graphics.newTextureRegion(theme.getTextureAtlas().findRegion(disabledUncheck));
		}
		if(enabledUncheck != null) {
			enabledUncheckTextureRegion = Mdx.graphics.newTextureRegion(theme.getTextureAtlas().findRegion(enabledUncheck));
		}
		if(hoveredCheck == null) {
			hoveredCheckTextureRegion = enabledCheckTextureRegion;
		} else {
			hoveredCheckTextureRegion = Mdx.graphics.newTextureRegion(theme.getTextureAtlas().findRegion(hoveredCheck));
		}
		if (hoveredUncheck != null) {
			hoveredUncheckTextureRegion = Mdx.graphics.newTextureRegion(theme.getTextureAtlas().findRegion(hoveredUncheck));
		}
	}

	public BackgroundRenderer getEnabledBackgroundRenderer() {
		return enabledBackgroundRenderer;
	}

	public BackgroundRenderer getDisabledBackgroundRenderer() {
		return disabledBackgroundRenderer;
	}

	public TextureRegion getDisabledCheckTextureRegion() {
		return disabledCheckTextureRegion;
	}

	public TextureRegion getDisabledUncheckTextureRegion() {
		return disabledUncheckTextureRegion;
	}

	public TextureRegion getEnabledCheckTextureRegion() {
		return enabledCheckTextureRegion;
	}

	public TextureRegion getEnabledUncheckTextureRegion() {
		return enabledUncheckTextureRegion;
	}

	public TextureRegion getHoveredCheckTextureRegion() {
		return hoveredCheckTextureRegion;
	}

	public TextureRegion getHoveredUncheckTextureRegion() {
		return hoveredUncheckTextureRegion;
	}
}
