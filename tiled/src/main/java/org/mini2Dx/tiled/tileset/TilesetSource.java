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
package org.mini2Dx.tiled.tileset;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.assets.AssetDescriptor;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.graphics.Sprite;
import org.mini2Dx.core.graphics.TextureAtlas;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.Disposable;
import org.mini2Dx.gdx.utils.ObjectMap;
import org.mini2Dx.tiled.Tile;
import org.mini2Dx.tiled.Tileset;

/**
 * Base class for tileset sources
 */
public abstract class TilesetSource implements Disposable {

	/**
	 * Returns the tile ID for a given tile within a tileset
	 * 
	 * @param x
	 *            The x coordinate of the tile on a tileset
	 * @param y
	 *            The y coordinate of the tile on a tileset
	 * @param firstGid
	 *            The first gid of the tileset
	 * @return The tile ID
	 */
	public int getTileId(int x, int y, int firstGid) {
		return firstGid + (y * getWidthInTiles()) + x;
	}

	/**
	 * Returns the x coordinate of a tile within a tileset
	 * 
	 * @param tileId
	 *            The tile id to get the x coordinate for
	 * @param firstGid
	 *            The first gid of the tileset
	 * @return
	 */
	public int getTileX(int tileId, int firstGid) {
		return (tileId - firstGid) % getWidthInTiles();
	}

	/**
	 * Returns the y coordinate of a tile within a tileset
	 * 
	 * @param tileId
	 *            The tile id to get the y coordinate for
	 * @param firstGid
	 *            The first gid of the tileset
	 * @return
	 */
	public int getTileY(int tileId, int firstGid) {
		return (tileId - firstGid) / getWidthInTiles();
	}

	/**
	 * Returns true if a tileset contains the tile with the given id
	 * 
	 * @param tileId
	 *            The tile id to search for
	 * @param firstGid
	 *            The first gid of the tileset
	 * @param lastGid
	 *            The last gid of the tileset
	 * @return False if the tileset does not contain the tile
	 */
	public boolean contains(int tileId, int firstGid, int lastGid) {
		return tileId >= firstGid && tileId <= lastGid;
	}

	/**
	 * Returns the dependencies of this source
	 * 
	 * @return null if there are no dependencies
	 * 
	 * @param tmxPath
	 *            The path of the TMX file
	 */
	public abstract Array<AssetDescriptor> getDependencies(FileHandle tmxPath);

	/**
	 * Loads the tileset's texture image
	 * 
	 * @param tmxPath
	 *            The path of the TMX file
	 */
	public abstract void loadTexture(FileHandle tmxPath);

	/**
	 * Loads the tileset's texture image
	 * 
	 * @param assetManager
	 *            The {@link AssetManager} to use
	 * @param tmxPath
	 *            The path of the TMX file
	 */
	public abstract void loadTexture(AssetManager assetManager, FileHandle tmxPath);

	/**
	 * Loads the texture from a {@link TextureAtlas}
	 * @param textureAtlas The {@link TextureAtlas} to use
	 */
	public abstract void loadTexture(TextureAtlas textureAtlas);

	/**
	 * Returns if the tileset texture image is loaded
	 * 
	 * @return True if loaded
	 */
	public abstract boolean isTextureLoaded();

	/**
	 * Returns the {@link Sprite} for a given tile
	 * 
	 * @param tileId
	 *            The tile id to retrieve
	 * @return The {@link Sprite} or null if that textures haven't been loaded
	 */
	public abstract Sprite getTileImage(int tileId);

	/**
	 * Returns the {@link Sprite} for a given tile
	 * 
	 * @param tileId
	 *            The tile id to retrieve
	 * @param firstGid
	 *            The first gid of the tileset
	 * @return The {@link Sprite} or null if that textures haven't been loaded
	 */
	public Sprite getTileImage(int tileId, int firstGid) {
		return getTileImage(tileId - firstGid);
	}

	/**
	 * Draws a tile to the {@link Graphics} context
	 * 
	 * @param g
	 *            The {@link Graphics} context available for rendering
	 * @param tileId
	 *            The tile id to render
	 * @param firstGid
	 *            The first gid of the parent tileset
	 * @param renderX
	 *            The X coordinate to render at
	 * @param renderY
	 *            The Y coordinate to render at
	 */
	public abstract void drawTile(Graphics g, int tileId, int firstGid, int renderX, int renderY);

	/**
	 * Draws the whole tileset to the {@link Graphics} context
	 * 
	 * @param g
	 *            The {@link Graphics} context available for rendering
	 * @param renderX
	 *            The X coordinate to render at
	 * @param renderY
	 *            The Y coordinate to render at
	 */
	public abstract void drawTileset(Graphics g, int renderX, int renderY);

	/**
	 * Returns the {@link Tile} for a given tile id
	 * 
	 * @param tileId
	 *            The tile id to look up
	 * @param firstGid
	 *            The first gid for the tileset
	 * @return The {@link Tile}
	 */
	public abstract Tile getTile(int tileId, int firstGid);

	/**
	 * Returns the {@link Tile} for a given x/y coordinate with the tileset
	 * source
	 * 
	 * @param x
	 *            The x coordinate of the tile within the tileset (in tiles)
	 * @param y
	 *            The y coordinate of the tile within the tileset (in tiles)
	 * @return The {@link Tile}
	 */
	public abstract Tile getTileByPosition(int x, int y);

	/**
	 * Returns the width of this tileset in pixels
	 * 
	 * @return
	 */
	public abstract int getWidth();

	/**
	 * Returns the height of this tileset in pixels
	 * 
	 * @return
	 */
	public abstract int getHeight();

	/**
	 * Returns the width of each tile in pixels
	 * 
	 * @return
	 */
	public abstract int getTileWidth();

	/**
	 * Returns the height of each tile in pixels
	 * 
	 * @return
	 */
	public abstract int getTileHeight();

	/**
	 * Returns the width of this source in tiles
	 * 
	 * @return
	 */
	public abstract int getWidthInTiles();

	/**
	 * Returns the height of this source in tiles
	 * 
	 * @return
	 */
	public abstract int getHeightInTiles();

	/**
	 * Returns the internal spacing between each tile
	 * 
	 * @return The spacing in pixels
	 */
	public abstract int getSpacing();

	/**
	 * Returns the margin at the borders of the tileset
	 * 
	 * @return The margin in pixels
	 */
	public abstract int getMargin();

	/**
	 * Returns if the tileset contains the specified property
	 * 
	 * @param propertyName
	 *            The property name to search for
	 * @return True if the tileset contains the property
	 */
	public abstract boolean containsProperty(String propertyName);

	/**
	 * Returns the value of a specified property
	 * 
	 * @param propertyName
	 *            The property name to search for
	 * @return Null if there is no such property
	 */
	public abstract String getProperty(String propertyName);

	/**
	 * Sets the value of a specified property
	 * 
	 * @param propertyName
	 *            The property name to set the value for
	 * @param value
	 *            The value of the property to set
	 */
	public abstract void setProperty(String propertyName, String value);

	/**
	 * Returns the properties {@link ObjectMap} of this {@link Tileset}
	 * 
	 * @return Null if there are no properties
	 */
	public abstract ObjectMap<String, String> getProperties();

	/**
	 * Returns a UUID generated by mini2Dx
	 * @return A non-null String representing this source
	 */
	public abstract String getInternalUuid();

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof TilesetSource) {
			final TilesetSource otherSource = (TilesetSource) obj;
			return getInternalUuid().equals(otherSource.getInternalUuid());
		}
		return super.equals(obj);
	}
}
