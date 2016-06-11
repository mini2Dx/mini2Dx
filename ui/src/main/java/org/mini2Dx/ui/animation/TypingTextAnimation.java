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

import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.serialization.annotation.ConstructorArg;

/**
 *
 */
public class TypingTextAnimation implements TextAnimation {
	private final float charactersPerSecond;
	private final float speed;
	
	private boolean finished = false;
	private float timer = 0f;
	private int characterIndex = 0;

	public TypingTextAnimation() {
		this(24f);
	}
	
	public TypingTextAnimation(@ConstructorArg(clazz=Float.class, name="charactersPerSecond") float charactersPerSecond) {
		this.charactersPerSecond = charactersPerSecond;
		speed = 1f / charactersPerSecond;
	}
	
	@Override
	public void update(String text, float delta) {
		if(characterIndex >= text.length() - 1) {
			finished = true;
			return;
		}
		
		timer += delta;
		if(timer >= speed) {
			timer -= speed;
			characterIndex++;
		}
	}
	
	@Override
	public void interpolate(String text, float alpha) {}

	@Override
	public void render(String text, Graphics g, float x, float y, float width, int hAlign) {
		if(finished) {
			g.drawString(text, x, y, width, hAlign);
		} else {
			g.drawString(text.substring(0, characterIndex), x, y, width, hAlign);
		}
	}

	@Override
	public void skip() {
		finished = true;
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

	@ConstructorArg(clazz=Float.class, name="charactersPerSecond")
	public float getCharactersPerSecond() {
		return charactersPerSecond;
	}

}
