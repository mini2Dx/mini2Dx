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
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.font.GameFont;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.ui.element.TextBox;

/**
 * Extends {@link StyleRule} for {@link TextBox} styling
 */
public class TextBoxStyleRule extends StyleRule {
	@Field(optional = true)
	private String background;
	@Field(optional = true)
	private String hoverBackground;
	@Field(optional = true)
	private String actionBackground;
	@Field(optional = true)
	private String disabledBackground;
	@Field
	private String font;
	@Field
	private String textColor;
	
	private BackgroundRenderer normalBackgroundRenderer, hoverBackgroundRenderer, actionBackgroundRenderer, disabledBackgroundRenderer;
	private UiFont uiFont;
	private Color color = null;
	
	@Override
	public void prepareAssets(UiTheme theme, FileHandleResolver fileHandleResolver, AssetManager assetManager) {
		if (theme.isHeadless()) {
			return; 
		}

		normalBackgroundRenderer = BackgroundRenderer.parse(background);
		normalBackgroundRenderer.prepareAssets(theme, fileHandleResolver, assetManager);

		hoverBackgroundRenderer = BackgroundRenderer.parse(hoverBackground);
		hoverBackgroundRenderer.prepareAssets(theme, fileHandleResolver, assetManager);

		actionBackgroundRenderer = BackgroundRenderer.parse(actionBackground);
		actionBackgroundRenderer.prepareAssets(theme, fileHandleResolver, assetManager);

		disabledBackgroundRenderer = BackgroundRenderer.parse(disabledBackground);
		disabledBackgroundRenderer.prepareAssets(theme, fileHandleResolver, assetManager);

		color = Colors.rgbToColor(textColor);

		uiFont = theme.getFont(font);
	}

	public BackgroundRenderer getNormalBackgroundRenderer() {
		return normalBackgroundRenderer;
	}

	public BackgroundRenderer getHoverBackgroundRenderer() {
		return hoverBackgroundRenderer;
	}

	public BackgroundRenderer getActionBackgroundRenderer() {
		return actionBackgroundRenderer;
	}

	public BackgroundRenderer getDisabledBackgroundRenderer() {
		return disabledBackgroundRenderer;
	}

	public GameFont getGameFont() {
		return uiFont.getGameFont();
	}

	public int getFontSize() {
		return uiFont.getFontSize();
	}

	public Color getColor() {
		return color;
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
