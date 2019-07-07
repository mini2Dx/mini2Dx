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
