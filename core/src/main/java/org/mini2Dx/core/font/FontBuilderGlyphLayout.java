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

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.util.Align;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.utils.Array;

public class FontBuilderGlyphLayout implements FontGlyphLayout {
	private final Array<FontBuilderGlyph> glyphs = new Array<FontBuilderGlyph>();
	private final FontBuilderGameFont font;
	private final Color black = Mdx.graphics.newColor(0f, 0f, 0f, 1f);

	private float maxX, maxY;

	public FontBuilderGlyphLayout(FontBuilderGameFont font) {
		this.font = font;
	}

	@Override
	public void setText(CharSequence str) {
		setText(str, black, -1f, Align.LEFT, true);
	}

	@Override
	public void setText(CharSequence str, Color color, float targetWidth, int halign, boolean wrap) {
		maxX = -1f;
		maxY = -1f;

		switch(halign) {
		default:
		case Align.LEFT:
			setTextLeftAlign(str, color, targetWidth, wrap);
			break;
		case Align.RIGHT:
			setTextRightAlign(str, color, targetWidth, wrap);
			break;
		case Align.CENTER:
			setTextCenterAlign(str, color, targetWidth, wrap);
			break;
		}

		if(glyphs.size > str.length()) {
			for(int i = glyphs.size - 1; i >= str.length(); i--) {
				final FontBuilderGlyph glyph = glyphs.removeIndex(i);
				glyph.release();
			}
		}

		for(int i = 0; i < glyphs.size; i++) {
			final FontBuilderGlyph glyph = getGlyph(i);
			if(glyph.glyphChar == null) {
				continue;
			}
			if(glyph.glyphChar.code == '\n' || glyph.glyphChar.code == '\r') {
				continue;
			}
			maxX = Math.max(maxX, glyph.x + glyph.glyphChar.width);
			maxY = Math.max(maxY, glyph.y + font.fontProperties.height);
		}

		if(halign == Align.CENTER && targetWidth >= 0f) {
			maxX = targetWidth;
		}
	}

	private void setTextLeftAlign(CharSequence str, Color color, float targetWidth, boolean wrap) {
		if(targetWidth < 0f) {
			targetWidth = Float.MAX_VALUE;
		}

		float yOffset = 0f;
		float xOffset = 0f;

		for(int i = 0; i < str.length();) {
			final char startChar = resolveChar(str.charAt(i));
			if(startChar == '\n' || startChar == '\r' || Character.isWhitespace(startChar)) {
				final FontBuilderGlyph glyph = getGlyph(i);
				glyph.color.set(color);
				glyph.glyphChar = null;
				glyph.x = -1f;
				glyph.y = -1f;
				i++;
				continue;
			}

			float lineWidth = calculateMaxWidthBeforeWrap(str, i, targetWidth);
			if(MathUtils.isZero(lineWidth)) {
				i++;
				continue;
			}

			for(; i < str.length() && xOffset < lineWidth; i++) {
				final char c = resolveChar(str.charAt(i));
				final float previousXOffset = xOffset;

				if(!font.charMap.containsKey(c)) {
					continue;
				}

				final FontBuilderGlyph glyph = getGlyph(i);
				glyph.color.set(color);

				final FontBuilderGameFont.FontBuilderChar fontChar = font.charMap.get(c);
				glyph.x = xOffset;
				glyph.y = yOffset;
				glyph.glyphChar = fontChar;
				xOffset = previousXOffset + fontChar.width;
			}

			if(!wrap) {
				return;
			}
			xOffset = 0f;
			yOffset += font.fontProperties.height;
		}
	}

	private void setTextRightAlign(CharSequence str, Color color, float targetWidth, boolean wrap) {
		if(targetWidth < 0f) {
			targetWidth = calculateMaxWidthBeforeWrap(str, 0, Float.MAX_VALUE);
			if(MathUtils.isZero(targetWidth)) {
				return;
			}
		}

		float yOffset = 0f;
		float xOffset = Float.MAX_VALUE;

		for(int i = 0; i < str.length();) {
			final char startChar = resolveChar(str.charAt(i));
			if(startChar == '\n' || startChar == '\r' || Character.isWhitespace(startChar)) {
				final FontBuilderGlyph glyph = getGlyph(i);
				glyph.color.set(color);
				glyph.glyphChar = null;
				glyph.x = -1f;
				glyph.y = -1f;
				i++;
				continue;
			}

			float lineWidth = calculateMaxWidthBeforeWrap(str, i, targetWidth);
			if(MathUtils.isZero(lineWidth)) {
				i++;
				continue;
			}
			if(lineWidth < targetWidth) {
				xOffset = targetWidth - lineWidth;
			} else {
				xOffset = 0f;
			}

			for(; i < str.length() && xOffset < targetWidth; i++) {
				final char c = resolveChar(str.charAt(i));
				final float previousXOffset = xOffset;

				if(!font.charMap.containsKey(c)) {
					continue;
				}

				final FontBuilderGlyph glyph = getGlyph(i);
				glyph.color.set(color);

				final FontBuilderGameFont.FontBuilderChar fontChar = font.charMap.get(c);
				glyph.x = xOffset;
				glyph.y = yOffset;
				glyph.glyphChar = fontChar;
				xOffset = previousXOffset + fontChar.width;
			}
			if(!wrap) {
				return;
			}
			yOffset += font.fontProperties.height;
		}
	}

	private void setTextCenterAlign(CharSequence str, Color color, float targetWidth, boolean wrap) {
		if(targetWidth < 0f) {
			targetWidth = calculateMaxWidthBeforeWrap(str, 0, Float.MAX_VALUE);
			if(MathUtils.isZero(targetWidth)) {
				return;
			}
		}

		float yOffset = 0f;

		for(int i = 0; i < str.length();) {
			final char startChar = resolveChar(str.charAt(i));
			if(startChar == '\n' || startChar == '\r' || Character.isWhitespace(startChar)) {
				final FontBuilderGlyph glyph = getGlyph(i);
				glyph.color.set(color);
				glyph.glyphChar = null;
				glyph.x = -1f;
				glyph.y = -1f;
				i++;
				continue;
			}

			float lineWidth = calculateMaxWidthBeforeWrap(str, i, targetWidth);
			if(MathUtils.isZero(lineWidth)) {
				i++;
				continue;
			}
			float xOffset = MathUtils.round((targetWidth * 0.5f) - (lineWidth * 0.5f));

			for(; i < str.length() && xOffset < targetWidth; i++) {
				final char c = resolveChar(str.charAt(i));
				final float previousXOffset = xOffset;

				if(!font.charMap.containsKey(c)) {
					continue;
				}

				final FontBuilderGlyph glyph = getGlyph(i);
				glyph.color.set(color);

				final FontBuilderGameFont.FontBuilderChar fontChar = font.charMap.get(c);
				glyph.x = xOffset;
				glyph.y = yOffset;
				glyph.glyphChar = fontChar;
				xOffset = previousXOffset + fontChar.width;
			}
			if(!wrap) {
				return;
			}
			yOffset += font.fontProperties.height;
		}
	}

	public float calculateMaxWidthBeforeWrap(CharSequence str, int from, float targetWidth) {
		float x = 0f;

		for(int i = from; i < str.length(); i++) {
			final char c = resolveChar(str.charAt(i));
			switch(c) {
			case '\r':
			case '\n':
				return x;
			}

			if(!font.charMap.containsKey(c)) {
				continue;
			}
			final FontBuilderGameFont.FontBuilderChar fontChar = font.charMap.get(c);

			if(x + fontChar.width > targetWidth) {
				if(Character.isWhitespace(c)) {
					return x;
				}
				if(c == '\n' || c == '\r') {
					return x;
				}

				//Scan backwards for first space
				float spaceX = x;
				for(int j = i - 1; j >= from; j--) {
					final char previousChar = str.charAt(j);
					if(!font.charMap.containsKey(previousChar)) {
						continue;
					}
					final FontBuilderGameFont.FontBuilderChar previousFontChar = font.charMap.get(previousChar);
					spaceX -= previousFontChar.width;
					if(Character.isWhitespace(previousChar)) {
						return spaceX;
					}
					if(Character.isIdeographic(previousChar)) {
						return spaceX;
					}
				}
				return x;
			}
			x += fontChar.width;
		}
		return x;
	}

	@Override
	public void reset() {
		while(glyphs.size > 0) {
			final FontBuilderGlyph run = glyphs.removeIndex(0);
			run.release();
		}

		maxX = -1f;
		maxY = -1f;
	}

	@Override
	public void dispose() {
		reset();
	}

	@Override
	public float getWidth() {
		return maxX;
	}

	@Override
	public float getHeight() {
		return maxY;
	}

	@Override
	public GameFont getFont() {
		return font;
	}

	private char resolveChar(char c) {
		if(font.charMap.containsKey(c)) {
			return c;
		}
		//Full-width fallback
		if(c == '。') {
			return '.';
		}
		if(c == '，') {
			return ',';
		}
		if(c == '？') {
			return '?';
		}
		if(c == '！') {
			return '!';
		}
		if(c == '…') {
			return '.';
		}
		return c;
	}

	public void transferGlyphsTo(Array<FontBuilderGlyph> result, float x, float y) {
		while(glyphs.size > 0) {
			final FontBuilderGlyph glyph = glyphs.removeIndex(0);
			glyph.x += x;
			glyph.y += y;
			result.add(glyph);
		}
	}

	public Array<FontBuilderGlyph> getGlyphs() {
		return glyphs;
	}

	private FontBuilderGlyph getGlyph(int index) {
		while(index >= glyphs.size) {
			final FontBuilderGlyph result = FontBuilderGlyph.allocate();
			glyphs.add(result);
		}
		return glyphs.get(index);
	}
}
