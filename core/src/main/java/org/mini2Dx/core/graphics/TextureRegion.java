/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.core.graphics;

import com.badlogic.gdx.graphics.Texture;

/**
 *
 */
public class TextureRegion extends com.badlogic.gdx.graphics.g2d.TextureRegion {
	/** Constructs a region with no texture and no coordinates defined. */
	public TextureRegion () {
	}

	/** Constructs a region the size of the specified texture. */
	public TextureRegion (Texture texture) {
		super(texture);
		flip(false, true);
	}

	/** @param width The width of the texture region. May be negative to flip the sprite when drawn.
	 * @param height The height of the texture region. May be negative to flip the sprite when drawn. */
	public TextureRegion (Texture texture, int width, int height) {
		super(texture, width, height);
		flip(false, true);
	}

	/** @param width The width of the texture region. May be negative to flip the sprite when drawn.
	 * @param height The height of the texture region. May be negative to flip the sprite when drawn. */
	public TextureRegion (Texture texture, int x, int y, int width, int height) {
		super(texture, x, y, width, height);
		flip(false, true);
	}

	public TextureRegion (Texture texture, float u, float v, float u2, float v2) {
		super(texture, u, v, u2, v2);
		flip(false, true);
	}

	/** Constructs a region with the same texture and coordinates of the specified region. */
	public TextureRegion (TextureRegion region) {
		super(region);
	}

	/** Constructs a region with the same texture as the specified region and sets the coordinates relative to the specified region.
	 * @param width The width of the texture region. May be negative to flip the sprite when drawn.
	 * @param height The height of the texture region. May be negative to flip the sprite when drawn. */
	public TextureRegion (TextureRegion region, int x, int y, int width, int height) {
		super(region, x, y, width, height);
	}
}
