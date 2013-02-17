/**
 * Copyright (c) 2013, mini2Dx Project
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
 * Implements a cache of textures filled with a solid color
 * 
 * @author Thomas Cashman
 */
public class ColorTextureCache {
	private Map<Integer, Texture> rectangularTextures;
	private Map<String, Texture> circularTextures;

	/**
	 * Constructor
	 */
	public ColorTextureCache() {
		rectangularTextures = new HashMap<Integer, Texture>();
		circularTextures = new HashMap<String, Texture>();
	}

	/**
	 * Returns a rectangular texture for the provided {@link Color}
	 * 
	 * @param color
	 *            The {@link Color} to fetch a texture of
	 * @return A new {@link Texture} if this is first time it has been
	 *         requested, otherwise it will return a cached instance of the
	 *         {@link Texture} for the given {@link Color}
	 */
	public Texture getRectangleTextureForColor(Color color) {
		int bits = color.toIntBits();
		if (!rectangularTextures.containsKey(bits)) {
			Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
			pixmap.setColor(color);
			pixmap.fillRectangle(0, 0, 1, 1);
			rectangularTextures.put(bits, new Texture(pixmap));
			pixmap.dispose();
		}
		return rectangularTextures.get(bits);
	}

	/**
	 * Returns a circular texture for the provided {@link Color}
	 * 
	 * @param color
	 *            The {@link Color} to fetch a texture of
	 * @param radius
	 *            The radius of the circle
	 * @return A new {@link Texture} if this is first time it has been
	 *         requested, otherwise it will return a cached instance of the
	 *         {@link Texture} for the given {@link Color}
	 */
	public Texture getCircularTextureForColor(Color color, int radius) {
		int bits = color.toIntBits();
		String key = "" + bits + radius;
		if (!circularTextures.containsKey(key)) {
			Pixmap pixmap = new Pixmap((radius * 2) + 1, (radius * 2) + 1,
					Pixmap.Format.RGBA8888);
			pixmap.setColor(color);
			pixmap.fillCircle(radius, radius, radius);
			circularTextures.put(key, new Texture(pixmap));
			pixmap.dispose();
		}
		return circularTextures.get(key);
	}
}
