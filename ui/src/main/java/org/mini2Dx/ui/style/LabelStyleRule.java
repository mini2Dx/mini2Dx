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

import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.font.GameFont;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.core.util.ColorUtils;
import org.mini2Dx.ui.element.Label;

/**
 * Extends {@link StyleRule} for {@link Label} styling
 */
public class LabelStyleRule extends ParentStyleRule {
	@Field
	private String font;
	@Field(optional=true)
	private String textColor;
	
	private UiFont uiFont;
	private Color color;

	@Override
	public void validate(UiTheme theme) {
		super.validate(theme);

		if(!theme.getFonts().containsKey(font)) {
			throw new MdxException("No such font " + font);
		}
	}

	@Override
	public void prepareAssets(UiTheme theme, FileHandleResolver fileHandleResolver, AssetManager assetManager) {
		if (theme.isHeadless()) {
			return; 
		}
		
		super.prepareAssets(theme, fileHandleResolver, assetManager);
		if(textColor != null) {
			color = ColorUtils.rgbToColor(textColor);
		}

		uiFont = theme.getFont(font);
	}

	public Color getColor() {
		return color;
	}

	public GameFont getGameFont() {
		return uiFont.getGameFont();
	}

	public int getFontSize() {
		return uiFont.getFontSize();
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}
}
