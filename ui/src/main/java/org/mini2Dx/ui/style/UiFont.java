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
