/*******************************************************************************
 * Copyright 2021 See AUTHORS file
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
package org.mini2Dx.core.font;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.util.Align;
import org.mini2Dx.gdx.utils.Array;

public class FontBuilderGameFontCache implements GameFontCache {
	private final Array<FontBuilderGlyph> glyphs = new Array<FontBuilderGlyph>();
	private final FontBuilderGameFont font;
	private final FontBuilderGlyphLayout glyphLayout;

	private final Color color = Mdx.graphics.newColor(0f, 0f, 0f, 1f);
	private float x, y;

	public FontBuilderGameFontCache(FontBuilderGameFont font) {
		super();
		this.font = font;
		glyphLayout = new FontBuilderGlyphLayout(font);
	}

	@Override
	public void addText(CharSequence str, float x, float y) {
		glyphLayout.setText(str, color, -1f, Align.LEFT, true);
		glyphLayout.transferGlyphsTo(glyphs, x, y);
	}

	@Override
	public void addText(CharSequence str, float x, float y, float targetWidth, int halign, boolean wrap) {
		glyphLayout.setText(str, color, targetWidth, halign, wrap);
		glyphLayout.transferGlyphsTo(glyphs, x, y);
	}

	@Override
	public void clear() {
		while(glyphs.size > 0) {
			final FontBuilderGlyph glyph = glyphs.removeIndex(0);
			glyph.release();
		}
	}

	@Override
	public void draw(Graphics g) {
		font.draw(g, glyphs, x, y, null);
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void setColor(Color color) {
		this.color.set(color);
	}

	@Override
	public void setAllColors(Color color) {
		for(int i = 0; i < glyphs.size; i++) {
			glyphs.get(i).color.set(color);
		}
	}

	@Override
	public void setAllAlphas(float alpha) {
		for(int i = 0; i < glyphs.size; i++) {
			glyphs.get(i).color.setA(alpha);
		}
	}

	@Override
	public void setText(CharSequence str, float x, float y) {
		clear();
		glyphLayout.setText(str, color, -1f, Align.LEFT, true);
		glyphLayout.transferGlyphsTo(glyphs, x, y);
	}

	@Override
	public void setText(CharSequence str, float x, float y, float targetWidth, int halign, boolean wrap) {
		clear();
		glyphLayout.setText(str, color, targetWidth, halign, wrap);
		glyphLayout.transferGlyphsTo(glyphs, x, y);
	}

	@Override
	public void translate(float x, float y) {
		this.x += x;
		this.y += y;
	}

	@Override
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public GameFont getFont() {
		return font;
	}
}