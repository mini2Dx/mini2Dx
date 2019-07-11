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
public class RadioButtonStyleRule extends LabelStyleRule {
	@Field
	private String active;
	@Field
	private String inactive;
	@Field(optional=true)
	private String activeHover;
	@Field(optional=true)
	private String inactiveHover;
	@Field
	private String disabledActive;
	@Field
	private String disabledInactive;
	@Field(optional=true)
	private String disabledActiveHover;
	@Field(optional=true)
	private String disabledInactiveHover;
	@Field
	private int optionsSpacing;
	@Field
	private int labelIndent;
	
	private TextureRegion activeTextureRegion, inactiveTextureRegion, activeHoverTextureRegion, inactiveHoverTextureRegion,
		disabledActiveTextureRegion, disabledInactiveTextureRegion, disabledActiveHoverTextureRegion, disabledInactiveHoverTextureRegion;

	@Override
	public void prepareAssets(UiTheme theme, FileHandleResolver fileHandleResolver, AssetManager assetManager) {
		if (theme.isHeadless()) {
			return; 
		}
		
		super.prepareAssets(theme, fileHandleResolver, assetManager);
		activeTextureRegion = Mdx.graphics.newTextureRegion(theme.getTextureAtlas().findRegion(active));
		inactiveTextureRegion = Mdx.graphics.newTextureRegion(theme.getTextureAtlas().findRegion(inactive));
		disabledActiveTextureRegion = Mdx.graphics.newTextureRegion(theme.getTextureAtlas().findRegion(disabledActive));
		disabledInactiveTextureRegion = Mdx.graphics.newTextureRegion(theme.getTextureAtlas().findRegion(disabledInactive));
	
		if(activeHover != null) {
			activeHoverTextureRegion = Mdx.graphics.newTextureRegion(theme.getTextureAtlas().findRegion(activeHover));
		} else {
			activeHoverTextureRegion = activeTextureRegion;
		}
		if(inactiveHover != null) {
			inactiveHoverTextureRegion = Mdx.graphics.newTextureRegion(theme.getTextureAtlas().findRegion(inactiveHover));
		} else {
			inactiveHoverTextureRegion = inactiveTextureRegion;
		}
		if(disabledActiveHover != null) {
			disabledActiveHoverTextureRegion = Mdx.graphics.newTextureRegion(theme.getTextureAtlas().findRegion(disabledActiveHover));
		} else {
			disabledActiveHoverTextureRegion = disabledActiveTextureRegion;
		}
		if(disabledInactiveHover != null) {
			disabledInactiveHoverTextureRegion = Mdx.graphics.newTextureRegion(theme.getTextureAtlas().findRegion(disabledInactiveHover));
		} else {
			disabledInactiveHoverTextureRegion = disabledInactiveTextureRegion;
		}
	}
	
	public TextureRegion getActiveTextureRegion() {
		return activeTextureRegion;
	}

	public TextureRegion getInactiveTextureRegion() {
		return inactiveTextureRegion;
	}

	public TextureRegion getDisabledActiveTextureRegion() {
		return disabledActiveTextureRegion;
	}

	public TextureRegion getDisabledInactiveTextureRegion() {
		return disabledInactiveTextureRegion;
	}

	public TextureRegion getActiveHoverTextureRegion() {
		return activeHoverTextureRegion;
	}
	
	public TextureRegion getInactiveHoverTextureRegion() {
		return inactiveHoverTextureRegion;
	}

	public TextureRegion getDisabledActiveHoverTextureRegion() {
		return disabledActiveHoverTextureRegion;
	}

	public TextureRegion getDisabledInactiveHoverTextureRegion() {
		return disabledInactiveHoverTextureRegion;
	}

	public int getOptionsSpacing() {
		return optionsSpacing;
	}

	public void setOptionsSpacing(int optionsSpacing) {
		this.optionsSpacing = optionsSpacing;
	}

	public int getLabelIndent() {
		return labelIndent;
	}

	public void setLabelIndent(int labelIndent) {
		this.labelIndent = labelIndent;
	}
}
