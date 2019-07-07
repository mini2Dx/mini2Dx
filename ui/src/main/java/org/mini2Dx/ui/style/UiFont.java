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
import org.mini2Dx.core.assets.AssetDescriptor;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.font.GameFont;
import org.mini2Dx.core.font.MonospaceGameFont;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.graphics.Texture;
import org.mini2Dx.core.graphics.TextureAtlas;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.gdx.utils.Array;

/**
 * A font for user interfaces
 */
public class UiFont {
	private static final String LOGGING_TAG = UiFont.class.getSimpleName();

	@Field
	private String path;
	@Field(optional=true)
	private String borderColor;
	@Field(optional=true)
	private int borderWidth;
	@Field(optional=true)
	private String shadowColor;
	@Field(optional=true)
	private int shadowOffsetX;
	@Field(optional=true)
	private int shadowOffsetY;
	@Field(optional=true)
	private int spaceX;
	@Field(optional=true)
	private int spaceY;
	@Field(optional=true)
	private boolean flip;
	@Field(optional=true)
	private boolean kerning = true;
	@Field(optional=true)
	private int fontSize = 12;
	
	private Color fontBorderColor, fontShadowColor;
	private GameFont gameFont;
	
	public void prepareAssets(UiTheme theme, FileHandleResolver fileHandleResolver, AssetManager assetManager) {
		if(theme.isHeadless()) {
			return;
		}

		if(path.endsWith(".ttf")) {
			gameFont = Mdx.fonts.newPlatformFont(fileHandleResolver.resolve(path));
		} else if(path.endsWith(".fnt")) {
			gameFont = Mdx.fonts.newBitmapFont(fileHandleResolver.resolve(path));
		} else if(path.endsWith(".xml")) {
			try {
				final MonospaceGameFont.FontParameters fontParameters = Mdx.xml.fromXml(fileHandleResolver.resolve(path).reader(), MonospaceGameFont.FontParameters.class);
				gameFont = Mdx.fonts.newMonospaceFont(fontParameters);
			} catch (Exception e) {
				Mdx.log.error(LOGGING_TAG, e.getMessage(), e);
			}
		}
		if(gameFont != null) {
			gameFont.load(assetManager);
		}
	}

	public void loadDependencies(UiTheme theme, FileHandleResolver fileHandleResolver, Array<AssetDescriptor> dependencies) {
		if(theme.isHeadless()) {
			return;
		}
		if(path.endsWith(".xml")) {
			try {
				final MonospaceGameFont.FontParameters fontParameters = Mdx.xml.fromXml(fileHandleResolver.resolve(path).reader(), MonospaceGameFont.FontParameters.class);
				if(fontParameters.textureAtlasPath != null) {
					dependencies.add(new AssetDescriptor(fontParameters.textureAtlasPath, TextureAtlas.class));
				} else if(fontParameters.texturePath != null) {
					dependencies.add(new AssetDescriptor(fontParameters.texturePath, Texture.class));
				}
			} catch (Exception e) {
				Mdx.log.error(LOGGING_TAG, e.getMessage(), e);
			}
		}
	}

	public void dispose() {
		gameFont.dispose();
	}

	public GameFont getGameFont() {
		return gameFont;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
	}

	public Color getFontBorderColor() {
		return fontBorderColor;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	public String getShadowColor() {
		return shadowColor;
	}

	public void setShadowColor(String shadowColor) {
		this.shadowColor = shadowColor;
	}

	public int getShadowOffsetX() {
		return shadowOffsetX;
	}

	public void setShadowOffsetX(int shadowOffsetX) {
		this.shadowOffsetX = shadowOffsetX;
	}

	public int getShadowOffsetY() {
		return shadowOffsetY;
	}

	public void setShadowOffsetY(int shadowOffsetY) {
		this.shadowOffsetY = shadowOffsetY;
	}

	public int getSpaceX() {
		return spaceX;
	}

	public void setSpaceX(int spaceX) {
		this.spaceX = spaceX;
	}

	public int getSpaceY() {
		return spaceY;
	}

	public void setSpaceY(int spaceY) {
		this.spaceY = spaceY;
	}

	public boolean isFlip() {
		return flip;
	}

	public void setFlip(boolean flip) {
		this.flip = flip;
	}

	public boolean isKerning() {
		return kerning;
	}

	public void setKerning(boolean kerning) {
		this.kerning = kerning;
	}
}
