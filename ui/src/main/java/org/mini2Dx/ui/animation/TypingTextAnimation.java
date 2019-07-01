/**
 * Copyright (c) 2016 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.ui.animation;

import org.mini2Dx.core.font.GameFontCache;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.serialization.annotation.ConstructorArg;

/**
 * A {@link TextAnimation} that reveals the text as if it were being typed
 */
public class TypingTextAnimation extends BaseTextAnimation {
	public static final float DEFAULT_CHARACTERS_PER_SECOND = 24f;
	
	private final float charactersPerSecond;
	private final float speed;
	
	private float timer = 0f;
	private boolean skip = false;
	private int characterIndex = 0;

	/**
	 * Constructor. Defaults to {@link #DEFAULT_CHARACTERS_PER_SECOND} characters revealed per second.
	 */
	public TypingTextAnimation() {
		this(DEFAULT_CHARACTERS_PER_SECOND);
	}
	
	/**
	 * Constructor
	 * @param charactersPerSecond The amount of characters to reveal per second
	 */
	public TypingTextAnimation(@ConstructorArg(clazz=Float.class, name="charactersPerSecond") float charactersPerSecond) {
		super();
		this.charactersPerSecond = charactersPerSecond;
		speed = 1f / charactersPerSecond;
	}
	
	@Override
	public void update(GameFontCache cache, String text, float renderWidth, int hAlign, float delta) {
		if(skip || characterIndex >= text.length() - 1) {
			if(!isFinished()) {
				cache.clear();
				cache.addText(text, 0f, 0f, renderWidth, hAlign, true);
				setFinished(true);
			}
			return;
		}
		
		timer += delta;
		if(timer >= speed) {
			timer -= speed;
			characterIndex++;
			
			cache.clear();
			cache.addText(text.substring(0, characterIndex), 0f, 0f, renderWidth, hAlign, true);
		}
	}
	
	@Override
	public void interpolate(GameFontCache cache, String text, float alpha) {}

	@Override
	public void render(GameFontCache cache, Graphics g, int renderX, int renderY) {
		cache.setPosition(renderX, renderY);
		g.drawFontCache(cache);
	}

	@Override
	public void skip() {
		skip = true;
	}

	@Override
	public void onResize(GameFontCache cache, String text, float renderWidth, int hAlign) {
		cache.clear();
		if(characterIndex == 0) {
			return;
		}
		if(characterIndex >= text.length() - 1) {
			cache.addText(text, 0f, 0f, renderWidth, hAlign, true);
		} else {
			cache.addText(text.substring(0, characterIndex), 0f, 0f, renderWidth, hAlign, true);
		}
	}

	@ConstructorArg(clazz=Float.class, name="charactersPerSecond")
	public float getCharactersPerSecond() {
		return charactersPerSecond;
	}

	@Override
	protected void resetState() {
		characterIndex = 0;
		timer = 0f;
		skip = false;
	}

	/**
	 * Returns the current index of the animation
	 * @return The index of the most recently displayed character
	 */
	public int getCurrentCharacterIndex() {
		return characterIndex;
	}
}
