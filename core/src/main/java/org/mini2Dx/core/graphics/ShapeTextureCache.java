/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.graphics;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

/**
 * Implements a cache of textures for shapes
 */
public class ShapeTextureCache {
	private Map<Integer, Texture> filledRectangleTextures;
	private Map<String, Texture> rectangleTextures;
	private Map<String, Texture> circleTextures;
	private Map<String, Texture> filledCircleTextures;

	/**
	 * Constructor
	 */
	public ShapeTextureCache() {
		rectangleTextures = new HashMap<String, Texture>();
		filledRectangleTextures = new HashMap<Integer, Texture>();
		circleTextures = new HashMap<String, Texture>();
		filledCircleTextures = new HashMap<String, Texture>();
	}

	/**
	 * Returns a filled rectangular texture for the provided {@link Color}
	 * 
	 * @param color
	 *            The {@link Color} to fetch a texture of
	 * @return A new {@link Texture} if this is first time it has been
	 *         requested, otherwise it will return a cached instance of the
	 *         {@link Texture} for the given {@link Color}
	 */
	public Texture getFilledRectangleTexture(Color color) {
		int bits = color.toIntBits();
		if (!filledRectangleTextures.containsKey(bits)) {
			Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
			pixmap.setColor(color);
			pixmap.fillRectangle(0, 0, 1, 1);
			filledRectangleTextures.put(bits, new Texture(pixmap));
			pixmap.dispose();
		}
		return filledRectangleTextures.get(bits);
	}

	/**
	 * Returns a rectangular texture for the provided {@link Color}
	 * 
	 * @param color
	 *            The {@link Color} to fetch a texture of
	 * @param width
	 *            The width of the rectangle
	 * @param height
	 *            The height of the rectangle
	 * @param lineHeight
	 *            The line height of the rectangle
	 * @return A new {@link Texture} if this is first time it has been
	 *         requested, otherwise it will return a cached instance of the
	 *         {@link Texture} for the given {@link Color}
	 */
	public Texture getRectangleTexture(Color color, int width, int height,
			int lineHeight) {
		int bits = color.toIntBits();
		String key = "" + bits + "," + width + "," + height + "," + lineHeight;
		if (!rectangleTextures.containsKey(key)) {
			Pixmap pixmap = new Pixmap(width + 1, height + 1,
					Pixmap.Format.RGBA8888);
			pixmap.setColor(color);
			for (int i = 0; i < lineHeight; i++) {
				pixmap.drawRectangle(i, i, width - (i * 2), height - (i * 2));
			}
			rectangleTextures.put(key, new Texture(pixmap));
			pixmap.dispose();
		}
		return rectangleTextures.get(key);
	}

	/**
	 * Returns a circle texture for the provided {@link Color}
	 * 
	 * @param color
	 *            The {@link Color} to fetch a texture of
	 * @param radius
	 *            The radius of the circle
	 * @param lineHeight
	 *            The line height of the circle
	 * @return A new {@link Texture} if this is first time it has been
	 *         requested, otherwise it will return a cached instance of the
	 *         {@link Texture} for the given {@link Color}
	 */
	public Texture getCircleTexture(Color color, int radius, int lineHeight) {
		int bits = color.toIntBits();
		String key = "" + bits + "," + radius + "," + lineHeight;
		if (!circleTextures.containsKey(key)) {
			Pixmap pixmap = new Pixmap((radius * 2) + 1, (radius * 2) + 1,
					Pixmap.Format.RGBA8888);
			pixmap.setColor(color);
			for (int i = 0; i < lineHeight; i++) {
				pixmap.drawCircle(radius, radius, radius - i);
			}
			circleTextures.put(key, new Texture(pixmap));
			pixmap.dispose();
		}
		return circleTextures.get(key);
	}

	/**
	 * Returns a filled circular texture for the provided {@link Color}
	 * 
	 * @param color
	 *            The {@link Color} to fetch a texture of
	 * @param radius
	 *            The radius of the circle
	 * @return A new {@link Texture} if this is first time it has been
	 *         requested, otherwise it will return a cached instance of the
	 *         {@link Texture} for the given {@link Color}
	 */
	public Texture getFilledCircleTexture(Color color, int radius) {
		int bits = color.toIntBits();
		String key = "" + bits + "," + radius;
		if (!filledCircleTextures.containsKey(key)) {
			Pixmap pixmap = new Pixmap((radius * 2) + 1, (radius * 2) + 1,
					Pixmap.Format.RGBA8888);
			pixmap.setColor(color);
			pixmap.fillCircle(radius, radius, radius);
			filledCircleTextures.put(key, new Texture(pixmap));
			pixmap.dispose();
		}
		return filledCircleTextures.get(key);
	}
}
