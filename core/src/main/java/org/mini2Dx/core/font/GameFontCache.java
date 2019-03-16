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

/**
 * Caches glyph geometry for fast rendering of static text
 */
public interface GameFontCache {

	/**
	 * Adds text to the cache
	 * @param str The text to be added
	 * @param x The x coordinate to add the text to
	 * @param y The y coordinate to add the text to
	 */
	public void addText(java.lang.CharSequence str, float x, float y);

	/**
	 * Adds text to the cache
	 * @param str The text to be added
	 * @param x The x coordinate to add the text to
	 * @param y The y coordinate to add the text to
	 * @param targetWidth The target width of the text (text will wrap if exceeded)
	 * @param halign The horizontal alignment based on {@link org.mini2Dx.core.util.Align}
	 */
	public void addText(java.lang.CharSequence str, float x, float y, float targetWidth, int halign, boolean wrap);

	/**
	 * Clears all text from the cache
	 */
	public void clear();

	/**
	 * Draws the cache to the {@link Graphics} context
	 * @param g The {@link Graphics} context to draw to
	 */
	public void draw(Graphics g);

	/**
	 * Returns the {@link Color} used for subsequent text operations
	 * @return The {@link Color} to draw with
	 */
	public Color getColor();

	/**
	 * Sets the {@link Color} for subsequent text operations
	 * @param color The {@link Color} to draw with
	 */
	public void setColor(Color color);

	/**
	 * Sets the {@link Color} for previous text operations
	 * @param color The {@link Color} to draw with
	 */
	public void setAllColors(Color color);

	/**
	 * Sets the {@link Color} alpha for previous text operations
	 * @param alpha The alpha to draw with
	 */
	public void setAllAlphas(float alpha);

	/**
	 * Clears all text from the cache and adds a new text sequence
	 * @param str The text to be added
	 * @param x The x coordinate to add the text to
	 * @param y The y coordinate to add the text to
	 */
	public void setText(java.lang.CharSequence str, float x, float y);

	/**
	 * Clears all text from the cache and adds a new text sequence
	 * @param str The text to be added
	 * @param x The x coordinate to add the text to
	 * @param y The y coordinate to add the text to
	 * @param targetWidth The target width of the text (text will wrap if exceeded)
	 * @param halign The horizontal alignment of the text based on {@link org.mini2Dx.core.util.Align}
	 * @param wrap True if the text should wrap when exceeding the targetWidth, false if the text should clip
	 */
	public void setText(java.lang.CharSequence str, float x, float y, float targetWidth, int halign, boolean wrap);

	/**
	 * Sets the position of the text relative to its current position
	 * @param x The amount to move by on the x axis
	 * @param y The amount to move by on the y axis
	 */
	public void translate(float x, float y);

	/**
	 * Sets the position of the cache
	 * @param x The amount to move by on the x axis
	 * @param y The amount to move by on the y axis
	 */
	public void setPosition(float x, float y);

	/**
	 * Returns the underlying {@link GameFont} for this cache
	 * @return The {@link GameFont} this cache was created for
	 */
	public GameFont getFont();
}
