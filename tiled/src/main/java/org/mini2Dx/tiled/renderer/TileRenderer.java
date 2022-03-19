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
package org.mini2Dx.tiled.renderer;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.graphics.Sprite;
import org.mini2Dx.core.serialization.GameDataSerializable;
import org.mini2Dx.gdx.utils.Disposable;
import org.mini2Dx.tiled.Tile;

/**
 * Common interface for tile renderers
 */
public interface TileRenderer extends GameDataSerializable, Disposable {

	/**
	 * Updates the {@link Tile} frame
	 *
	 * @param tile
	 * 			  The {@link Tile} to update
	 * @param delta
	 *            The time since the last update in seconds
	 */
	public void update(Tile tile, float delta);

	/**
	 * Renders the {@link Tile} at the given coordinates
	 * 
	 * @param g
	 *            The {@link Graphics} context to use
	 * @param tile
	 * 			  The {@link Tile} to render
	 * @param renderX
	 *            The x coordinate to render at
	 * @param renderY
	 *            The y coordinate to render at
	 * @param alpha
	 * 	          The alpha value to render with
	 */
	public void draw(Graphics g, Tile tile, int renderX, int renderY, float alpha);

	/**
	 * Renders the {@link Tile} at the given coordinates
	 * 
	 * @param g
	 *            The {@link Graphics} context to use
	 * @param tile
	 * 			  The {@link Tile} to render
	 * @param renderX
	 *            The x coordinate to render at
	 * @param renderY
	 *            The y coordinate to render at
	 * @param alpha
	 *            The alpha value to render with
	 * @param flipH
	 *            True if the tile is flipped horizontally
	 * @param flipV
	 *            True if the tile is flipped vertically
	 * @param flipD
	 *            True if the tile is flipped (anti) diagonally - rotation
	 */
	public void draw(Graphics g, Tile tile, int renderX, int renderY, float alpha, boolean flipH, boolean flipV, boolean flipD);

	/**
	 * Returns the current {@link Tile} image
	 * @param tile
	 * 			  The {@link Tile} to get the image for
	 *
	 * @return The current image
	 */
	public Sprite getCurrentTileImage(Tile tile);


	public int getRendererType();
}
