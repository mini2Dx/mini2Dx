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
package org.mini2Dx.core.font;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.util.Align;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.utils.Array;

/**
 *
 */
public class MonospaceFontGlyphLayout implements FontGlyphLayout {
	private final Array<MonospaceGlyph> glyphs = new Array<MonospaceGlyph>();
	private final MonospaceGameFont monospaceFont;
	private final MonospaceGameFont.FontParameters fontParameters;
	private final Color black = Mdx.graphics.newColor(0f, 0f, 0f, 1f);

	private float maxX, maxY;

	public MonospaceFontGlyphLayout(MonospaceGameFont monospaceFont) {
		super();
		this.monospaceFont = monospaceFont;
		fontParameters = monospaceFont.getFontParameters();
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
				final MonospaceGlyph glyph = glyphs.removeIndex(i);
				glyph.release();
			}
		}

		for(int i = 0; i < glyphs.size; i++) {
			final MonospaceGlyph glyph = getGlyph(i);
			if(glyph.glyphChar == '\n' || glyph.glyphChar == '\r') {
				continue;
			}
			maxX = Math.max(maxX, glyph.x + fontParameters.characterWidth);
			maxY = Math.max(maxY, glyph.y + fontParameters.lineHeight);
		}

		if(halign == Align.CENTER && targetWidth >= 0f) {
			maxX = targetWidth;
		}
	}

	private void setTextLeftAlign(CharSequence str, Color color, float targetWidth, boolean wrap) {
		final int estimateMaxCharsPerLine;
		if(targetWidth < 0f) {
			targetWidth = Float.MAX_VALUE;
			estimateMaxCharsPerLine = Integer.MAX_VALUE;
		} else {
			estimateMaxCharsPerLine = MathUtils.round(targetWidth / (fontParameters.characterWidth + fontParameters.spacing));
		}

		float yOffset = 0f;
		float xOffset = 0f;

		for(int i = 0; i < str.length();) {
			final char startChar = str.charAt(i);
			if(startChar == '\n' || startChar == '\r' || Character.isWhitespace(startChar)) {
				final MonospaceGlyph glyph = getGlyph(i);
				glyph.color.set(color);
				glyph.glyphChar = startChar;
				glyph.x = -1f;
				glyph.y = -1f;
				glyph.textureRegion = null;
				i++;
				continue;
			}

			final int totalChars = Math.max(1, calculateMaxCharactersBeforeWrap(str, i, estimateMaxCharsPerLine, targetWidth));

			for(int j = i; j < i + totalChars && j < str.length(); j++) {
				final char c = str.charAt(j);

				final MonospaceGlyph glyph = getGlyph(j);
				glyph.color.set(color);
				glyph.glyphChar = c;
				glyph.x = xOffset;
				glyph.y = yOffset;
				glyph.textureRegion = monospaceFont.getTextureRegion(c);

				xOffset += fontParameters.characterWidth + fontParameters.spacing;
			}

			if(!wrap) {
				return;
			}
			xOffset = 0f;
			yOffset += fontParameters.lineHeight;
			i += totalChars;
		}
	}

	private void setTextRightAlign(CharSequence str, Color color, float targetWidth, boolean wrap) {
		final int charactersPerLine;

		if(targetWidth < 0f) {
			int maxCharsPerLine = 0;

			for(int i = 0; i < str.length();) {
				if(str.charAt(i) == '\n' || str.charAt(i) == '\r' || Character.isWhitespace(str.charAt(i))) {
					i++;
					continue;
				}
				final int totalChars = calculateMaxCharactersBeforeWrap(str, i, Integer.MAX_VALUE, Float.MAX_VALUE);
				maxCharsPerLine = Math.max(totalChars, maxCharsPerLine);
				i += totalChars;
			}

			if(maxCharsPerLine == 0) {
				return;
			} else {
				charactersPerLine = maxCharsPerLine;
			}

			targetWidth = (charactersPerLine * fontParameters.characterWidth) + (charactersPerLine * fontParameters.spacing) - fontParameters.spacing;
		} else {
			charactersPerLine = MathUtils.round(targetWidth / (fontParameters.characterWidth + fontParameters.spacing));
		}

		float yOffset = 0f;
		float xOffset = targetWidth - fontParameters.characterWidth;

		for(int i = 0; i < str.length();) {
			final char startChar = str.charAt(i);
			if(startChar == '\n' || startChar == '\r' || Character.isWhitespace(startChar)) {
				final MonospaceGlyph glyph = getGlyph(i);
				glyph.color.set(color);
				glyph.glyphChar = startChar;
				glyph.x = -1f;
				glyph.y = -1f;
				glyph.textureRegion = null;
				i++;
				continue;
			}

			final int totalChars = Math.max(1, calculateMaxCharactersBeforeWrap(str, i, charactersPerLine, targetWidth));

			for(int j = i + totalChars - 1; j >= i; j--) {
				final char c = str.charAt(j);
				final MonospaceGlyph glyph = getGlyph(j);
				glyph.x = xOffset;
				glyph.y = yOffset;
				glyph.glyphChar = c;
				glyph.color.set(color);
				glyph.textureRegion = monospaceFont.getTextureRegion(c);

				xOffset -= fontParameters.characterWidth + fontParameters.spacing;
			}
			if(!wrap) {
				return;
			}
			xOffset = targetWidth - fontParameters.characterWidth;
			yOffset += fontParameters.lineHeight;
			i += totalChars;
		}
	}

	private void setTextCenterAlign(CharSequence str, Color color, float targetWidth, boolean wrap) {
		final int charactersPerLine;

		if(targetWidth < 0f) {
			int maxCharsPerLine = 0;

			for(int i = 0; i < str.length();) {
				if(str.charAt(i) == '\n' || str.charAt(i) == '\r' || Character.isWhitespace(str.charAt(i))) {
					i++;
					continue;
				}
				final int totalChars = calculateMaxCharactersBeforeWrap(str, i, Integer.MAX_VALUE, Float.MAX_VALUE);
				maxCharsPerLine = Math.max(totalChars, maxCharsPerLine);
				i += totalChars;
			}

			if(maxCharsPerLine == 0) {
				return;
			} else {
				charactersPerLine = maxCharsPerLine;
			}

			targetWidth = (charactersPerLine * fontParameters.characterWidth) + (charactersPerLine * fontParameters.spacing) - fontParameters.spacing;
		} else {
			charactersPerLine = MathUtils.round(targetWidth / (fontParameters.characterWidth + fontParameters.spacing));
		}

		float yOffset = 0f;

		for(int i = 0; i < str.length();) {
			final char startChar = str.charAt(i);
			if(startChar == '\n' || startChar == '\r' || Character.isWhitespace(startChar)) {
				final MonospaceGlyph glyph = getGlyph(i);
				glyph.color.set(color);
				glyph.glyphChar = startChar;
				glyph.x = -1f;
				glyph.y = -1f;
				glyph.textureRegion = null;
				i++;
				continue;
			}

			final int totalChars = Math.max(1, calculateMaxCharactersBeforeWrap(str, i, charactersPerLine, targetWidth));
			final float lineWidth = (totalChars * fontParameters.characterWidth) + (totalChars * fontParameters.spacing) - fontParameters.spacing;

			float xOffset = MathUtils.round((targetWidth * 0.5f) - (lineWidth * 0.5f));

			for(int j = i; j < i + totalChars; j++) {
				final char c = str.charAt(j);
				final MonospaceGlyph glyph = getGlyph(j);
				glyph.x = xOffset;
				glyph.y = yOffset;
				glyph.glyphChar = c;
				glyph.color.set(color);
				glyph.textureRegion = monospaceFont.getTextureRegion(c);

				xOffset += fontParameters.characterWidth + fontParameters.spacing;
			}

			if(!wrap) {
				return;
			}
			yOffset += fontParameters.lineHeight;
			i += totalChars;
		}
	}

	/**
	 *
	 * @param str
	 * @param from
	 * @return The amount of characters to render before the wrap
	 */
	public int calculateMaxCharactersBeforeWrap(CharSequence str, int from, int estimate, float targetWidth) {
		float x = 0f;

		for(int i = from; i < str.length(); i++) {
			final char c = str.charAt(i);
			switch(c) {
			case '\r':
			case '\n':
				return i - from;
			}

			if(x + fontParameters.characterWidth > targetWidth) {
				if(Character.isWhitespace(c)) {
					return i - from;
				}
				if(c == '\n' || c == '\r') {
					return i - from;
				}

				//Scan backwards for first space
				for(int j = i - 1; j >= from; j--) {
					final char previousChar = str.charAt(j);
					if(Character.isWhitespace(previousChar)) {
						return j - from;
					}
				}
				return i - from;
			}

			x += fontParameters.characterWidth + fontParameters.spacing;
		}
		return Math.min(estimate, str.length() - from);
	}

	@Override
	public void reset() {
		while(glyphs.size > 0) {
			final MonospaceGlyph run = glyphs.removeIndex(0);
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
		return monospaceFont;
	}

	public void transferGlyphsTo(Array<MonospaceGlyph> result, float x, float y) {
		while(glyphs.size > 0) {
			final MonospaceGlyph glyph = glyphs.removeIndex(0);
			glyph.x += x;
			glyph.y += y;
			result.add(glyph);
		}
	}

	public Array<MonospaceGlyph> getGlyphs() {
		return glyphs;
	}

	private MonospaceGlyph getGlyph(int index) {
		while(index >= glyphs.size) {
			final MonospaceGlyph result = MonospaceGlyph.allocate();
			glyphs.add(result);
		}
		return glyphs.get(index);
	}
}