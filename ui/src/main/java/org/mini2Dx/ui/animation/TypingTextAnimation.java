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
