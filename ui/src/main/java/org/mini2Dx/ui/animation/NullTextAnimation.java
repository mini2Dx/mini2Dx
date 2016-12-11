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

import com.badlogic.gdx.graphics.g2d.BitmapFontCache;

/**
 * A {@link TextAnimation} that just renders the text directly
 */
public class NullTextAnimation extends BaseTextAnimation {
	
	@Override
	public void skip() {}

	@Override
	public void update(BitmapFontCache cache, String text, float renderWidth, int hAlign, float delta) {
		if(!isFinished()) {
			cache.addText(text, 0f, 0f, renderWidth, hAlign, true);
		}
		setFinished(true);
	}

	@Override
	public void interpolate(BitmapFontCache cache, String text, float alpha) {}

	@Override
	public void render(BitmapFontCache cache, Graphics g, int renderX, int renderY) {
		cache.setPosition(renderX, renderY);
		g.drawBitmapFontCache(cache);
	}

	@Override
	protected void resetState() {}
}
