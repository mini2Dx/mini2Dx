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
import org.mini2Dx.core.serialization.annotation.NonConcrete;
import org.mini2Dx.ui.listener.TextAnimationListener;

/**
 * Common interface for text animation implementations
 */
@NonConcrete
public interface TextAnimation {

	/**
	 * Update the animation
	 * @param cache The {@link GameFontCache} to use to render the text
	 * @param text The target text to display
	 * @param renderWidth The text render width
	 * @param hAlign The horizontal alignment of the text
	 * @param delta The time since the last frame (in seconds)
	 */
	public void update(GameFontCache cache, String text, float renderWidth, int hAlign, float delta);
	
	/**
	 * Interpolate the animation
	 * @param cache The {@link GameFontCache} to use to render the text
	 * @param text The target text to display
	 * @param alpha The interpolation alpha value
	 */
	public void interpolate(GameFontCache cache, String text, float alpha);
	
	/**
	 * Renders the animation
	 * @param cache The {@link GameFontCache} to use to render the text
	 * @param g The {@link Graphics} context
	 * @param renderX The render X coordinate
	 * @param renderY The render Y coordinate
	 */
	public void render(GameFontCache cache, Graphics g, int renderX, int renderY);
	
	/**
	 * Sets the {@link TextAnimation} to be skipped
	 */
	public void skip();
	
	/**
	 * Returns if the {@link TextAnimation} has finished
	 * @return True if the animation has finished
	 */
	public boolean isFinished();
	
	/**
	 * Resets the animation
	 */
	public void reset();

	/**
	 * Handles label resizing
	 */
	public void onResize(GameFontCache cache, String text, float renderWidth, int hAlign);
	
	/**
	 * Adds a {@link TextAnimationListener} to this {@link TextAnimation}
	 * @param listener The {@link TextAnimationListener} to be added
	 */
	public void addTextAnimationListener(TextAnimationListener listener);
	
	/**
	 * Removes a {@link TextAnimationListener} from this {@link TextAnimation}
	 * @param listener The {@link TextAnimationListener} to be removed
	 */
	public void removeTextAnimationListener(TextAnimationListener listener);
}
