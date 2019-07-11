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
