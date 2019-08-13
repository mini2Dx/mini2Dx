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
package com.badlogic.gdx.graphics;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class LibgdxTextureRegionWrapper extends TextureRegion {

	/** Constructs a region with no texture and no coordinates defined. */
	public LibgdxTextureRegionWrapper () {
	}

	/** Constructs a region the size of the specified texture. */
	public LibgdxTextureRegionWrapper (Texture texture) {
		super(texture);
		flip(false, true);
	}

	/** @param width The width of the texture region. May be negative to flip the sprite when drawn.
	 * @param height The height of the texture region. May be negative to flip the sprite when drawn. */
	public LibgdxTextureRegionWrapper (Texture texture, int width, int height) {
		super(texture, width, height);
		flip(false, true);
	}

	/** @param width The width of the texture region. May be negative to flip the sprite when drawn.
	 * @param height The height of the texture region. May be negative to flip the sprite when drawn. */
	public LibgdxTextureRegionWrapper (Texture texture, int x, int y, int width, int height) {
		super(texture, x, y, width, height);
		flip(false, true);
	}

	public LibgdxTextureRegionWrapper (Texture texture, float u, float v, float u2, float v2) {
		super(texture, u, v, u2, v2);
		flip(false, true);
	}

	/** Constructs a region with the same texture and coordinates of the specified region. */
	public LibgdxTextureRegionWrapper (TextureRegion region) {
		super(region);
	}

	/** Constructs a region with the same texture as the specified region and sets the coordinates relative to the specified region.
	 * @param width The width of the texture region. May be negative to flip the sprite when drawn.
	 * @param height The height of the texture region. May be negative to flip the sprite when drawn. */
	public LibgdxTextureRegionWrapper (TextureRegion region, int x, int y, int width, int height) {
		super(region, x, y, width, height);
		flip(false, true);
	}

	/**
	 * Constructs a region from an {@link TextureAtlas.AtlasRegion}
	 * @param atlasRegion
	 */
	public LibgdxTextureRegionWrapper(TextureAtlas.AtlasRegion atlasRegion) {
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

	public void setRegionHeight (int height) {
		if (!isFlipY()) {
			setV(getV2() + height / (float)getTexture().getHeight());
		} else {
			setV2(getV() + height / (float)getTexture().getHeight());
		}
	}
}
