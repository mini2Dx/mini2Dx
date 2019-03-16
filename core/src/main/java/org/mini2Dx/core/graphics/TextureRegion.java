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

public interface TextureRegion {

	public void setRegion(Texture texture);

	/**
	 * @param width  The width of the texture region. May be negative to flip the sprite when drawn.
	 * @param height The height of the texture region. May be negative to flip the sprite when drawn.
	 */
	public void setRegion(int x, int y, int width, int height);

	public void setRegion(float u, float v, float u2, float v2);

	/**
	 * Sets the texture and coordinates to the specified region.
	 */
	public void setRegion(TextureRegion region);

	/**
	 * Sets the texture to that of the specified region and sets the coordinates relative to the specified region.
	 */
	public void setRegion(TextureRegion region, int x, int y, int width, int height);

	public Texture getTexture();

	public void setTexture(Texture texture);

	public float getU();

	public void setU(float u);

	public float getV();

	public void setV(float v);

	public float getU2();

	public void setU2(float u2);

	public float getV2();

	public void setV2(float v2);

	public int getRegionX();

	public void setRegionX(int x);

	public int getRegionY();

	public void setRegionY(int y);

	/**
	 * Returns the region's width.
	 */
	public int getRegionWidth();

	public void setRegionWidth(int width);

	/**
	 * Returns the region's height.
	 */
	public int getRegionHeight();

	public void setRegionHeight(int height);

	public void flip(boolean x, boolean y);

	public void setFlip(boolean flipX, boolean flipY);

	public boolean isFlipX();

	public void setFlipX(boolean flipX);

	public boolean isFlipY();

	public void setFlipY(boolean flipY);

	/**
	 * Offsets the region relative to the current region. Generally the region's size should be the entire size of the texture in
	 * the direction(s) it is scrolled.
	 *
	 * @param xAmount The percentage to offset horizontally.
	 * @param yAmount The percentage to offset vertically. This is done in texture space, so up is negative.
	 */
	public void scroll(float xAmount, float yAmount);

	/**
	 * Converts this {@link TextureRegion} to a {@link Pixmap}
	 *
	 * @return A new {@link Pixmap} instance containing the pixel data
	 */
	public Pixmap toPixmap();

	/**
	 * Helper function to create tiles out of this TextureRegion starting from the top left corner going to the right and ending at
	 * the bottom right corner. Only complete tiles will be returned so if the region's width or height are not a multiple of the
	 * tile width and height not all of the region will be used. This will not work on texture regions returned form a TextureAtlas
	 * that either have whitespace removed or where flipped before the region is split.
	 *
	 * @param tileWidth  a tile's width in pixels
	 * @param tileHeight a tile's height in pixels
	 * @return a 2D array of TextureRegions indexed by [row][column].
	 */
	public TextureRegion[][] split(int tileWidth, int tileHeight);
}
