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
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

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
		flip(false, true);
	}
	
	/**
	 * Constructs a region from an {@link AtlasRegion}
	 * @param atlasRegion
	 */
	public TextureRegion(AtlasRegion atlasRegion) {
		super(atlasRegion);
		flip(false, true);
	}
	
	@Override
	public boolean isFlipY() {
		return !super.isFlipY();
	}
	
	@Override
	public int getRegionY() {
		setFlipY(!isFlipY());
		int result = super.getRegionY();
		setFlipY(!isFlipY());
		return result;
	}

	/**
	 * Sets if the {@link TextureRegion} is flipped
	 * @param flipX True if the region is flipped horizontally
	 * @param flipY True if the region is flipped vertically
	 */
	public void setFlip(boolean flipX, boolean flipY) {
		setFlipX(flipX);
		setFlipY(flipY);
	}
	
	/**
	 * Sets if the {@link TextureRegion} is flipped horizontally
	 * @param flipX True if the region is flipped horizontally
	 */
	public void setFlipX(boolean flipX) {
		if(flipX == isFlipX()) {
			return;
		}
		flip(true, false);
	}
	
	/**
	 * Sets if the {@link TextureRegion} is flipped vertically
	 * @param flipY True if the region is flipped vertically
	 */
	public void setFlipY(boolean flipY) {
		if(flipY == isFlipY()) {
			return;
		}
		flip(false, true);
	}
}
