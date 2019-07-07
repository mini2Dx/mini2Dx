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
import org.mini2Dx.core.font.GameFont;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.core.graphics.Colors;
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
			color = Colors.rgbToColor(textColor);
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
