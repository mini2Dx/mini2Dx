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

import com.badlogic.gdx.graphics.Texture;

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
	}

	/**
	 * Creates a sprite with width, height, and texture region equal to the size
	 * of the texture.
	 */
	public Sprite(Texture texture) {
		this(texture, texture.getWidth(), texture.getHeight());
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
		this(texture, 0, 0, srcWidth, srcHeight);
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
		setFlip(false, true);
	}

	/**
	 * Creates a sprite based on a specific TextureRegion, the new sprite's
	 * region is a copy of the parameter region - altering one does not affect
	 * the other
	 */
	public Sprite(TextureRegion region) {
		super(region);
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
	}

	/** Creates a sprite that is a copy in every way of the specified sprite. */
	public Sprite(Sprite sprite) {
		super(sprite);
	}
}
