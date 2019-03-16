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

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.util.Align;

public interface GameFont {

	/**
	 * Draws text to the {@link Graphics} context using this font
	 * @param g The {@link Graphics} context
	 * @param str The text to render
	 * @param x The x coordinate to draw at
	 * @param y The y coordinate to draw at
	 */
	public void draw(Graphics g, String str, float x, float y);

	/**
	 * Draws text to the {@link Graphics} context using this font
	 * @param g The {@link Graphics} context
	 * @param str The text to render
	 * @param x The x coordinate to draw at
	 * @param y The y coordinate to draw at
	 * @param targetWidth The target width to render with (Note: text will wrap if exceeding this width)
	 */
	public void draw(Graphics g, String str, float x, float y, float targetWidth);

	/**
	 * Draws text to the {@link Graphics} context using this font
	 * @param g The {@link Graphics} context
	 * @param str The text to render
	 * @param x The x coordinate to draw at
	 * @param y The y coordinate to draw at
	 * @param targetWidth The target width to render with
	 * @param horizontalAlignment The horizontal alignment within the targetWidth. See {@link Align}
	 * @param wrap True if text should wrap if exceeding targetWidth, false if it should clip
	 */
	public void draw(Graphics g, String str, float x, float y, float targetWidth, int horizontalAlignment, boolean wrap);

	/**
	 * Creates a {@link FontGlyphLayout} for this font
	 * @return A new {@link FontGlyphLayout} instance
	 */
	public FontGlyphLayout newGlyphLayout();

	/**
	 * Returns a {@link FontGlyphLayout} instance associated with this {@link GameFont} instance
	 * @return A {@link FontGlyphLayout} instance attached to this font
	 */
	public FontGlyphLayout getSharedGlyphLayout();

	/**
	 * Creates a {@link GameFontCache} for this font
	 * @return A new {@link GameFontCache} instance
	 */
	public GameFontCache newCache();

	/**
	 * Returns the {@link Color} the font will be rendered with
	 * @return Black by default
	 */
	public Color getColor();

	/**
	 * Sets the {@link Color} to render the font with
	 * @param color
	 */
	public void setColor(Color color);

	/**
	 * Returns the line height of the font
	 * @return
	 */
	public float getLineHeight();

	/**
	 * Returns the height of a capital letter above the baseline
	 * @return
	 */
	public float getCapHeight();

	/**
	 * Returns if characters use integer positions
	 * @return False if characters can be placed on half-pixels
	 */
	public boolean useIntegerPositions();

	/**
	 * Disposes of this font instance and its resources
	 */
	public void dispose();
}

