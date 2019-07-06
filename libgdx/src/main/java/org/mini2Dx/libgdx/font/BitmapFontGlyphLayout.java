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
package org.mini2Dx.libgdx.font;

import org.mini2Dx.core.font.FontGlyphLayout;
import org.mini2Dx.core.font.GameFont;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.libgdx.graphics.LibgdxColor;

public class BitmapFontGlyphLayout extends com.badlogic.gdx.graphics.g2d.GlyphLayout implements FontGlyphLayout {
	private final LibgdxBitmapFont font;

	public BitmapFontGlyphLayout(LibgdxBitmapFont font) {
		super();
		this.font = font;
	}

	@Override
	public void setText(CharSequence str) {
		setText(font.bitmapFont, str);
	}

	@Override
	public void setText(CharSequence str, Color color, float targetWidth, int halign, boolean wrap) {
		final LibgdxColor gdxColor = (LibgdxColor) color;
		setText(font.bitmapFont, str, gdxColor.color, targetWidth, halign, wrap);
	}

	@Override
	public void dispose() {
		reset();
	}

	@Override
	public float getWidth() {
		return super.width;
	}

	@Override
	public float getHeight() {
		return super.height;
	}

	@Override
	public GameFont getFont() {
		return font;
	}
}