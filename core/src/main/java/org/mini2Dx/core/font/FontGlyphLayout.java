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

import org.mini2Dx.core.graphics.Color;

/**
 * Computes glyph geometry
 */
public interface FontGlyphLayout {
	/**
	 * Sets the text to compute the geometry for
	 * @param str The text to compute the geometry for
	 */
	public void setText(CharSequence str);

	/**
	 * Sets the text to compute the geometry for
	 * @param str The text to compute the geometry for
	 * @param color The {@link Color} of the text
	 * @param targetWidth
	 * @param halign The alignment based on {@link org.mini2Dx.core.util.Align}
	 * @param wrap
	 */
	public void setText(CharSequence str, Color color, float targetWidth, int halign, boolean wrap);

	/**
	 * Resets this instance
	 */
	public void reset();

	/**
	 * Disposes of this instance
	 */
	public void dispose();

	/**
	 * Returns the total width of the text
	 * @return -1f if no text has been computed yet
	 */
	public float getWidth();

	/**
	 * Returns the total height of the text
	 * @return -1f if no text has been computed yet
	 */
	public float getHeight();

	/**
	 * Returns the {@link GameFont} that this {@link FontGlyphLayout} was created from
	 * @return
	 */
	public GameFont getFont();
}

