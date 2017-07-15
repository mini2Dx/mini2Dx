/**
 * Copyright 2017 Thomas Cashman
 */
package org.mini2Dx.tiled.tileset;

import java.util.HashMap;
import java.util.Map;

import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.tiled.Tile;
import org.mini2Dx.tiled.Tileset;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;

/**
 *
 */
public abstract class TilesetSource implements Disposable {

	/**
	 * Returns the tile ID for a given tile within a tileset
	 * 
	 * @param x
	 *            The x coordinate of the tile on a tileset
	 * @param y
	 *            The y coordinate of the tile on a tileset
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

	public abstract void loadTexture(FileHandle tmxDirectory);

	public abstract boolean isTextureLoaded();

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
	 * @param tileId The tile id to look up
	 * @param firstGid The first gid for the tileset
	 * @return The {@link Tile}
	 */
	public abstract Tile getTile(int tileId, int firstGid);

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
	 * @param propertyName The property name to search for
	 * @return True if the tileset contains the property
	 */
	public abstract boolean containsProperty(String propertyName);

	/**
	 * Returns the value of a specified property
	 * @param propertyName The property name to search for
	 * @return Null if there is no such property
	 */
	public abstract String getProperty(String propertyName);
	
	/**
	 * Sets the value of a specified property
	 * @param propertyName The property name to set the value for
	 * @param value The value of the property to set
	 */
	public abstract void setProperty(String propertyName, String value);
	
	/**
	 * Returns the properties {@link Map} of this {@link Tileset}
	 * @return Null if there are no properties
	 */
	public abstract Map<String, String> getProperties();
}
