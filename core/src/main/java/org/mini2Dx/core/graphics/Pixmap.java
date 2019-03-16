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
package org.mini2Dx.core.graphics;

import org.mini2Dx.gdx.utils.Disposable;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface Pixmap extends Disposable {

	/**
	 * Fills the complete bitmap with the currently set color.
	 */
	public void fill();

	/**
	 * Draws a line between the given coordinates using the currently set color.
	 *
	 * @param x  The x-coodinate of the first point
	 * @param y  The y-coordinate of the first point
	 * @param x2 The x-coordinate of the first point
	 * @param y2 The y-coordinate of the first point
	 */
	public void drawLine(int x, int y, int x2, int y2);

	/**
	 * Draws a rectangle outline starting at x, y extending by width to the right and by height downwards (y-axis points downwards)
	 * using the current color.
	 *
	 * @param x      The x coordinate
	 * @param y      The y coordinate
	 * @param width  The width in pixels
	 * @param height The height in pixels
	 */
	public void drawRectangle(int x, int y, int width, int height);

	/**
	 * Draws an area from another Pixmap to this Pixmap.
	 *
	 * @param pixmap The other Pixmap
	 * @param x      The target x-coordinate (top left corner)
	 * @param y      The target y-coordinate (top left corner)
	 */
	public void drawPixmap(Pixmap pixmap, int x, int y);

	/**
	 * Draws an area from another Pixmap to this Pixmap.
	 *
	 * @param pixmap    The other Pixmap
	 * @param x         The target x-coordinate (top left corner)
	 * @param y         The target y-coordinate (top left corner)
	 * @param srcx      The source x-coordinate (top left corner)
	 * @param srcy      The source y-coordinate (top left corner);
	 * @param srcWidth  The width of the area from the other Pixmap in pixels
	 * @param srcHeight The height of the area from the other Pixmap in pixels
	 */
	public void drawPixmap(Pixmap pixmap, int x, int y, int srcx, int srcy, int srcWidth, int srcHeight);

	/**
	 * Draws an area from another Pixmap to this Pixmap. This will automatically scale and stretch the source image to the
	 * specified target rectangle. Use {@link Pixmap#setFilter(PixmapFilter)} to specify the type of filtering to be used (nearest
	 * neighbour or bilinear).
	 *
	 * @param pixmap    The other Pixmap
	 * @param srcx      The source x-coordinate (top left corner)
	 * @param srcy      The source y-coordinate (top left corner);
	 * @param srcWidth  The width of the area from the other Pixmap in pixels
	 * @param srcHeight The height of the area from the other Pixmap in pixels
	 * @param dstx      The target x-coordinate (top left corner)
	 * @param dsty      The target y-coordinate (top left corner)
	 * @param dstWidth  The target width
	 * @param dstHeight the target height
	 */
	public void drawPixmap(Pixmap pixmap, int srcx, int srcy, int srcWidth, int srcHeight, int dstx, int dsty, int dstWidth,
						   int dstHeight);

	/**
	 * Fills a rectangle starting at x, y extending by width to the right and by height downwards (y-axis points downwards) using
	 * the current color.
	 *
	 * @param x      The x coordinate
	 * @param y      The y coordinate
	 * @param width  The width in pixels
	 * @param height The height in pixels
	 */
	public void fillRectangle(int x, int y, int width, int height);

	/**
	 * Draws a circle outline with the center at x,y and a radius using the current color and stroke width.
	 *
	 * @param x      The x-coordinate of the center
	 * @param y      The y-coordinate of the center
	 * @param radius The radius in pixels
	 */
	public void drawCircle(int x, int y, int radius);

	/**
	 * Fills a circle with the center at x,y and a radius using the current color.
	 *
	 * @param x      The x-coordinate of the center
	 * @param y      The y-coordinate of the center
	 * @param radius The radius in pixels
	 */
	public void fillCircle(int x, int y, int radius);

	/**
	 * Fills a triangle with vertices at x1,y1 and x2,y2 and x3,y3 using the current color.
	 *
	 * @param x1 The x-coordinate of vertex 1
	 * @param y1 The y-coordinate of vertex 1
	 * @param x2 The x-coordinate of vertex 2
	 * @param y2 The y-coordinate of vertex 2
	 * @param x3 The x-coordinate of vertex 3
	 * @param y3 The y-coordinate of vertex 3
	 */
	public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3);

	/**
	 * Draws a pixel at the given location with the current color.
	 *
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 */
	public void drawPixel(int x, int y);

	/**
	 * Draws a pixel at the given location with the given color.
	 *
	 * @param x     the x-coordinate
	 * @param y     the y-coordinate
	 * @param color the color.
	 */
	public void drawPixel(int x, int y, Color color);

	/**
	 * Returns the 32-bit RGBA8888 value of the pixel at x, y. For Alpha formats the RGB components will be one.
	 *
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 * @return The pixel color in RGBA8888 format.
	 */
	public int getPixel(int x, int y);

	/**
	 * Returns the direct ByteBuffer holding the pixel data. For the format Alpha each value is encoded as a byte. For the format
	 * LuminanceAlpha the luminance is the first byte and the alpha is the second byte of the pixel. For the formats RGB888 and
	 * RGBA8888 the color components are stored in a single byte each in the order red, green, blue (alpha). For the formats RGB565
	 * and RGBA4444 the pixel colors are stored in shorts in machine dependent order.
	 *
	 * @return the direct {@link ByteBuffer} holding the pixel data.
	 */
	public ByteBuffer getPixels();

	/**
	 * @return The width of the Pixmap in pixels.
	 */
	public int getWidth();

	/**
	 * @return The height of the Pixmap in pixels.
	 */
	public int getHeight();

	/**
	 * @return the {@link PixmapFormat} of this Pixmap.
	 */
	public PixmapFormat getFormat();

	/**
	 * @return the currently set {@link PixmapBlending}
	 */
	public PixmapBlending getBlending();

	public void setBlending(PixmapBlending blending);

	/**
	 * @return the currently set {@link PixmapFilter}
	 */
	public PixmapFilter getFilter();

	/**
	 * Sets the type of interpolation {@link PixmapFilter} to be used in conjunction with
	 * {@link Pixmap#drawPixmap(Pixmap, int, int, int, int, int, int, int, int)}.
	 *
	 * @param filter the filter.
	 */
	public void setFilter(PixmapFilter filter);

	/**
	 * Sets the color for the following drawing operations
	 *
	 * @param color the color
	 */
	public void setColor(Color color);
}
