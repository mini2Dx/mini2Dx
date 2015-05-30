/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.core.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Wraps {@link com.badlogic.gdx.graphics.g2d.Sprite} to auto-flip
 * {@link Texture}s due to mini2Dx placing its origin in the top-left
 * corner rather than the bottom-left corner like LibGDX
 */
public class Sprite extends com.badlogic.gdx.graphics.g2d.Sprite {
	/**
	 * Creates an uninitialized sprite. The sprite will need a texture region
	 * and bounds set before it can be drawn.
	 */
	public Sprite() {
		super();
		flip(false, true);
	}

	/**
	 * Creates a sprite with width, height, and texture region equal to the size
	 * of the texture.
	 */
	public Sprite(Texture texture) {
		super(texture);
		flip(false, true);
	}

	/**
	 * Creates a sprite with width, height, and texture region equal to the
	 * specified size. The texture region's upper left corner will be 0,0.
	 * 
	 * @param srcWidth
	 *            The width of the texture region. May be negative to flip the
	 *            sprite when drawn.
	 * @param srcHeight
	 *            The height of the texture region. May be negative to flip the
	 *            sprite when drawn.
	 */
	public Sprite(Texture texture, int srcWidth, int srcHeight) {
		super(texture, srcWidth, srcHeight);
		flip(false, true);
	}

	/**
	 * Creates a sprite with width, height, and texture region equal to the
	 * specified size.
	 * 
	 * @param srcX The x coordinate of the region to draw
	 * @param srcY The y coordinate of the region to draw
	 * @param srcWidth
	 *            The width of the texture region. May be negative to flip the
	 *            sprite when drawn.
	 * @param srcHeight
	 *            The height of the texture region. May be negative to flip the
	 *            sprite when drawn.
	 */
	public Sprite(Texture texture, int srcX, int srcY, int srcWidth,
			int srcHeight) {
		super(texture, srcX, srcY, srcWidth, srcHeight);
		flip(false, true);
	}

	/**
	 * Creates a sprite based on a specific TextureRegion, the new sprite's
	 * region is a copy of the parameter region - altering one does not affect
	 * the other
	 */
	public Sprite(TextureRegion region) {
		super(region);
		flip(false, true);
	}

	/**
	 * Creates a sprite with width, height, and texture region equal to the
	 * specified size, relative to specified sprite's texture region.
	 * 
	 * @param srcX The x coordinate of the region to draw
	 * @param srcY The y coordinate of the region to draw
	 * @param srcWidth
	 *            The width of the texture region. May be negative to flip the
	 *            sprite when drawn.
	 * @param srcHeight
	 *            The height of the texture region. May be negative to flip the
	 *            sprite when drawn.
	 */
	public Sprite(TextureRegion region, int srcX, int srcY, int srcWidth,
			int srcHeight) {
		super(region, srcX, srcY, srcWidth, srcHeight);
		flip(false, true);
	}

	/** Creates a sprite that is a copy in every way of the specified sprite. */
	public Sprite(Sprite sprite) {
		super(sprite);
	}
}
