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
package org.mini2Dx.libgdx.graphics;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.gdx.utils.ObjectMap;

/**
 * Implements a cache of textures for shapes
 */
public class ShapeTextureCache {
	private ObjectMap<Integer, Texture> filledRectangleTextures;

	/**
	 * Constructor
	 */
	public ShapeTextureCache() {
		filledRectangleTextures = new ObjectMap<Integer, Texture>();
	}

	/**
	 * Returns a filled rectangular texture for the provided {@link LibgdxColor}
	 *
	 * @param color
	 *            The {@link LibgdxColor} to fetch a texture of
	 * @return A new {@link Texture} if this is first time it has been
	 *         requested, otherwise it will return a cached instance of the
	 *         {@link Texture} for the given {@link LibgdxColor}
	 */
	public Texture getFilledRectangleTexture(LibgdxColor color) {
		int bits = color.color.toIntBits();
		if (!filledRectangleTextures.containsKey(bits)) {
			Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
			pixmap.setColor(color.color);
			pixmap.fillRectangle(0, 0, 1, 1);
			filledRectangleTextures.put(bits, new Texture(pixmap));
			pixmap.dispose();
		}
		return filledRectangleTextures.get(bits);
	}
}
