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

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.font.GameFont;
import org.mini2Dx.core.font.GameFontCache;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.libgdx.LibgdxGraphics;
import org.mini2Dx.libgdx.graphics.LibgdxColor;

public class LibgdxBitmapFontCache implements GameFontCache {
	private final LibgdxBitmapFont bitmapFont;
	private final com.badlogic.gdx.graphics.g2d.BitmapFontCache bitmapFontCache;
	private final LibgdxColor tmpColor;

	public LibgdxBitmapFontCache(LibgdxBitmapFont font) {
		this(font, font.useIntegerPositions());
	}

	public LibgdxBitmapFontCache(LibgdxBitmapFont font, boolean integer) {
		super();
		bitmapFont = font;
		bitmapFontCache = new com.badlogic.gdx.graphics.g2d.BitmapFontCache(font.bitmapFont, integer);
		tmpColor = new LibgdxColor(bitmapFontCache.getColor());
	}

	@Override
	public void addText(CharSequence str, float x, float y) {
		bitmapFontCache.addText(str, x , y);
	}

	@Override
	public void addText(CharSequence str, float x, float y, float targetWidth, int halign, boolean wrap) {
		bitmapFontCache.addText(str, x , y,  targetWidth, halign, wrap);
	}

	@Override
	public void clear() {
		bitmapFontCache.clear();
	}

	@Override
	public void draw(Graphics g) {
		final LibgdxGraphics gdxGraphics = (LibgdxGraphics) g;
		bitmapFontCache.draw(gdxGraphics.spriteBatch);
	}

	@Override
	public Color getColor() {
		return tmpColor;
	}

	@Override
	public void setColor(Color color) {
		final LibgdxColor gdxColor = (LibgdxColor) color;
		bitmapFontCache.setColor(gdxColor.color);
		tmpColor.set(color);
	}

	@Override
	public void setAllColors(Color color) {
		final LibgdxColor gdxColor = (LibgdxColor) color;
		bitmapFontCache.setColors(gdxColor.color);
		tmpColor.set(color);
	}

	@Override
	public void setAllAlphas(float alpha) {
		bitmapFontCache.setAlphas(alpha);
	}

	@Override
	public void setText(CharSequence str, float x, float y) {
		bitmapFontCache.setText(str, x, y);
	}

	@Override
	public void setText(CharSequence str, float x, float y, float targetWidth, int halign, boolean wrap) {
		bitmapFontCache.setText(str, x, y, targetWidth, halign, wrap);
	}

	@Override
	public void translate(float x, float y) {
		bitmapFontCache.translate(x, y);
	}

	@Override
	public void setPosition(float x, float y) {
		bitmapFontCache.setPosition(x, y);
	}

	@Override
	public GameFont getFont() {
		return bitmapFont;
	}
}