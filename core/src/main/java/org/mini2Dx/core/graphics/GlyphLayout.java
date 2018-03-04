/**
 * Copyright (c) 2018 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.Glyph;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;

/**
 * Utility class for calculating render coordinates for text
 */
public class GlyphLayout extends com.badlogic.gdx.graphics.g2d.GlyphLayout {
	private static final GlyphLayout TMP_LAYOUT = new GlyphLayout();

	public GlyphLayout() {
		super();
	}

	public GlyphLayout(BitmapFont font, CharSequence str) {
		super(font, str);
	}

	public GlyphLayout(BitmapFont font, CharSequence str, Color color, float targetWidth, int halign, boolean wrap) {
		super(font, str, color, targetWidth, halign, wrap);
	}

	public GlyphLayout(BitmapFont font, CharSequence str, int start, int end, Color color, float targetWidth,
			int halign, boolean wrap, String truncate) {
		super(font, str, start, end, color, targetWidth, halign, wrap, truncate);
	}
	
	/**
	 * Returns the first render position of a word within a given sentence (case-sensitive)
	 * @param font The {@link BitmapFont} used to render the sentence
	 * @param sentence The sentence to search
	 * @param word The word to search for
	 * @param targetWidth The target render width
	 * @param halign The {@link Align} value
	 * @param wrap True if the text should wrap when exceeding the targetWidth
	 * @return Null if the word is not found in the sentence
	 */
	public static GlyphPosition calculateFirstRenderPositionOfWord(BitmapFont font, String sentence, String word, float targetWidth, int halign, boolean wrap) {
		TMP_LAYOUT.setText(font, sentence, Color.WHITE, targetWidth, halign, wrap);
		
		final GlyphPosition result = TMP_LAYOUT.new GlyphPosition();
		
		for(int runIndex = 0; runIndex < TMP_LAYOUT.runs.size; runIndex++) {
			GlyphRun run = TMP_LAYOUT.runs.get(runIndex);
			
			boolean match = false;
			int glyphIndex = 0;
			int charIndex = 0;
			
			for(; glyphIndex < run.glyphs.size; glyphIndex++) {
				Glyph glyph = run.glyphs.get(glyphIndex);
				if(glyph.id == word.charAt(charIndex)) {
					charIndex++;
					
					if(charIndex >= word.length()) {
						match = true;
						break;
					}
					continue;
				}
				charIndex = 0;
			}
			
			if(!match) {
				continue;
			}
			int glyphStartIndex = glyphIndex > 0 ? glyphIndex - (word.length() - 1) : 0;
			
			float xAdvancesSum = run.xAdvances.get(0);
			for(int i = 0; i < run.glyphs.size && i < glyphStartIndex; i++) {
				xAdvancesSum += run.xAdvances.get(i + 1);
			}
			
			result.x = run.x + xAdvancesSum;
			result.y = run.y;
			return result;
		}
		return null;
	}
	
	public class GlyphPosition {
		public float x;
		public float y;
	}
}
