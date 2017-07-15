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
package org.mini2Dx.tiled;

import java.util.Map;

import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.tiled.tileset.TilesetSource;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;

/**
 * A tileset loaded with a {@link TiledMap}
 */
public class Tileset implements Disposable {
	private final TilesetSource tilesetSource;
	
	private int firstGid;
	private int lastGid = Integer.MAX_VALUE;
	
	public Tileset(int firstGid, TilesetSource tilesetSource) {
		super();
		this.tilesetSource = tilesetSource;
		this.firstGid = firstGid;
		calculateLastGid();
	}
	
	/**
	 * Returns if the tileset contains the specified property
	 * @param propertyName The property name to search for
	 * @return True if the tileset contains the property
	 */
	public boolean containsProperty(String propertyName) {
		return tilesetSource.containsProperty(propertyName);
	}

	/**
	 * Returns the value of a specified property
	 * @param propertyName The property name to search for
	 * @return Null if there is no such property
	 */
	public String getProperty(String propertyName) {
		return tilesetSource.getProperty(propertyName);
	}
	
	/**
	 * Sets the value of a specified property
	 * @param propertyName The property name to set the value for
	 * @param value The value of the property to set
	 */
	public void setProperty(String propertyName, String value) {
		tilesetSource.setProperty(propertyName, value);
	}
	
	/**
	 * Returns the properties {@link Map} of this {@link Tileset}
	 * @return Null if there are no properties
	 */
	public Map<String, String> getProperties() {
		return tilesetSource.getProperties();
	}

	/**
	 * Draws a tile to the {@link Graphics} context
	 * 
	 * @param g
	 *            The {@link Graphics} context available for rendering
	 * @param tileId
	 *            The tile id to render
	 * @param renderX
	 *            The X coordinate to render at
	 * @param renderY
	 *            The Y coordinate to render at
	 */
	public void drawTile(Graphics g, int tileId, int renderX, int renderY) {
		tilesetSource.drawTile(g, tileId, firstGid, renderX, renderY);
	}

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
	public void drawTileset(Graphics g, int renderX, int renderY) {
		tilesetSource.drawTileset(g, renderX, renderY);
	}
	
	/**
	 * Returns the {@link Tile} for a given tile id
	 * @param tileId The tile id to look up
	 * @return The {@link Tile}
	 */
	public Tile getTile(int tileId) {
		return tilesetSource.getTile(tileId, firstGid);
	}
	
	/**
	 * Returns the {@link Tile} for a given tile coordinate on the tileset
	 * @param x The x coordinate in tiles
	 * @param y The y coordinate in tiles
	 * @return The {@link Tile}
	 */
	public Tile getTile(int x, int y) {
		return getTile(tilesetSource.getTileId(x, y, firstGid));
	}

	/**
	 * Returns if the tileset image has been loaded
	 * 
	 * @return True if loaded
	 */
	public boolean isTextureLoaded() {
		return tilesetSource.isTextureLoaded();
	}

	/**
	 * Loads the tileset image
	 * 
	 * @param tmxDirectory
	 *            The directory containing the TMX files for the
	 *            {@link TiledMap} that has loaded this tileset
	 */
	public void loadTexture(FileHandle tmxDirectory) {
		tilesetSource.loadTexture(tmxDirectory);
	}
	
	@Override
	public void dispose() {
		tilesetSource.dispose();
	}
	
	private void calculateLastGid() {
		lastGid = (getWidthInTiles() * getHeightInTiles()) + firstGid - 1;
	}

	/**
	 * Returns true if this tileset contains the tile with the given id
	 * 
	 * @param tileId
	 *            The tile id to search for
	 * @return False if the tileset does not contain the tile
	 */
	public boolean contains(int tileId) {
		return tilesetSource.contains(tileId, firstGid, lastGid);
	}
	
	/**
	 * Returns the tile ID for a given tile within this tileset
	 * @param x The x coordinate of the tile on the tileset
	 * @param y The y coordinate of the tile on the tileset
	 * @return The tile ID
	 */
	public int getTileId(int x, int y) {
		return tilesetSource.getTileId(x, y, firstGid);
	}

	/**
	 * Returns the x coordinate of a tile within this tileset
	 * 
	 * @param tileId
	 *            The tile id to get the x coordinate for
	 * @return
	 */
	public int getTileX(int tileId) {
		return tilesetSource.getTileX(tileId, firstGid);
	}

	/**
	 * Returns the y coordinate of a tile within this tileset
	 * 
	 * @param tileId
	 *            The tile id to get the y coordinate for
	 * @return
	 */
	public int getTileY(int tileId) {
		return tilesetSource.getTileY(tileId, firstGid);
	}

	/**
	 * Returns the width of this tileset in tiles
	 * 
	 * @return
	 */
	public int getWidthInTiles() {
		return tilesetSource.getWidthInTiles();
	}

	/**
	 * Returns the height of this tileset in tiles
	 * 
	 * @return
	 */
	public int getHeightInTiles() {
		return tilesetSource.getHeightInTiles();
	}

	/**
	 * Returns the width of this tileset in pixels
	 * 
	 * @return
	 */
	public int getWidth() {
		return tilesetSource.getWidth();
	}

	/**
	 * Returns the height of this tileset in pixels
	 * 
	 * @return
	 */
	public int getHeight() {
		return tilesetSource.getHeight();
	}

	/**
	 * Returns the width of each tile in pixels
	 * 
	 * @return
	 */
	public int getTileWidth() {
		return tilesetSource.getTileWidth();
	}

	/**
	 * Returns the height of each tile in pixels
	 * 
	 * @return
	 */
	public int getTileHeight() {
		return tilesetSource.getTileHeight();
	}

	/**
	 * Returns the internal spacing between each tile
	 * 
	 * @return The spacing in pixels
	 */
	public int getSpacing() {
		return tilesetSource.getSpacing();
	}

	/**
	 * Returns the margin at the borders of the tileset
	 * 
	 * @return The margin in pixels
	 */
	public int getMargin() {
		return tilesetSource.getMargin();
	}
	
	/**
	 * Returns the first GID. See:
	 * <a href="https://github.com/bjorn/tiled/wiki/TMX-Map-Format">TMX Map Format</a>
	 * 
	 * @return
	 */
	public int getFirstGid() {
		return firstGid;
	}
}
