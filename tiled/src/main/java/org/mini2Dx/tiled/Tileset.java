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
package org.mini2Dx.tiled;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.assets.AssetDescriptor;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.graphics.TextureAtlas;
import org.mini2Dx.core.serialization.GameDataSerializable;
import org.mini2Dx.core.serialization.GameDataSerializableUtils;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.Disposable;
import org.mini2Dx.gdx.utils.ObjectMap;
import org.mini2Dx.tiled.tileset.ImageTilesetSource;
import org.mini2Dx.tiled.tileset.TilesetSource;
import org.mini2Dx.tiled.tileset.TsxTilesetSource;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * A tileset loaded with a {@link TiledMap}
 */
public class Tileset implements GameDataSerializable, Disposable {
	private TilesetSource tilesetSource;

	private int firstGid;
	private int lastGid = Integer.MAX_VALUE;

	public Tileset(int firstGid, TilesetSource tilesetSource) {
		super();
		this.tilesetSource = tilesetSource;
		this.firstGid = firstGid;
		calculateLastGid();
	}

	private Tileset() {}

	public static Tileset fromInputStream(TiledMapData mapData, DataInputStream inputStream) throws IOException {
		final Tileset result = new Tileset();
		result.readData(inputStream);

		final int tilesetSourceType = inputStream.readInt();
		switch (tilesetSourceType) {
		case TsxTilesetSource.TILESET_TYPE:
			result.tilesetSource = TsxTilesetSource.fromInputStream(mapData, inputStream);
			break;
		default:
		case ImageTilesetSource.TILESET_TYPE:
			result.tilesetSource = ImageTilesetSource.fromInputStream(inputStream);
			break;
		}
		result.calculateLastGid();

		for(int x = 0; x < result.getWidthInTiles(); x++) {
			for(int y = 0; y < result.getHeightInTiles(); y++) {
				mapData.onTilePropertiesParsed(result.getTile(x, y));
			}
		}
		return result;
	}

	@Override
	public void writeData(DataOutputStream outputStream) throws IOException {
		outputStream.writeInt(firstGid);
		outputStream.writeInt(tilesetSource.getTilesetSourceType());
		tilesetSource.writeData(outputStream);
	}

	@Override
	public void readData(DataInputStream inputStream) throws IOException {
		firstGid = inputStream.readInt();
	}

	/**
	 * Returns if the tileset contains the specified property
	 * 
	 * @param propertyName
	 *            The property name to search for
	 * @return True if the tileset contains the property
	 */
	public boolean containsProperty(String propertyName) {
		return tilesetSource.containsProperty(propertyName);
	}

	/**
	 * Returns the value of a specified property
	 * 
	 * @param propertyName
	 *            The property name to search for
	 * @return Null if there is no such property
	 */
	public String getProperty(String propertyName) {
		return tilesetSource.getProperty(propertyName);
	}

	/**
	 * Sets the value of a specified property
	 * 
	 * @param propertyName
	 *            The property name to set the value for
	 * @param value
	 *            The value of the property to set
	 */
	public void setProperty(String propertyName, String value) {
		tilesetSource.setProperty(propertyName, value);
	}

	/**
	 * Returns the properties {@link ObjectMap} of this {@link Tileset}
	 * 
	 * @return Null if there are no properties
	 */
	public ObjectMap<String, String> getProperties() {
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
		tilesetSource.drawTile(g, tileId, firstGid, renderX, renderY, 1.0f);
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
	 * @param alpha
	 *            The alpha blend value
	 */
	public void drawTile(Graphics g, int tileId, int renderX, int renderY, float alpha) {
		tilesetSource.drawTile(g, tileId, firstGid, renderX, renderY, alpha);
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
		tilesetSource.drawTileset(g, renderX, renderY, 1.0f);
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
	 * @param alpha
	 *            The alpha blend value
	 */
	public void drawTileset(Graphics g, int renderX, int renderY, float alpha) {
		tilesetSource.drawTileset(g, renderX, renderY, alpha);
	}

	/**
	 * Returns the {@link Tile} for a given tile id
	 * 
	 * @param tileId
	 *            The tile id to look up
	 * @return The {@link Tile}
	 */
	public Tile getTile(int tileId) {
		return tilesetSource.getTile(tileId, firstGid);
	}

	/**
	 * Returns the {@link Tile} for a given tile coordinate on the tileset
	 * 
	 * @param x
	 *            The x coordinate in tiles
	 * @param y
	 *            The y coordinate in tiles
	 * @return The {@link Tile}
	 */
	public Tile getTile(int x, int y) {
		return getTile(tilesetSource.getTileId(x, y, firstGid));
	}
	
	/**
	 * Returns the {@link AssetManager} dependencies for this tileset
	 * @param tmxPath The path of the TMX file
	 * @return null if there are no dependencies
	 */
	public Array<AssetDescriptor> getDependencies(FileHandle tmxPath) {
		return tilesetSource.getDependencies(tmxPath);
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
	 * @param tmxPath
	 *            The path of the TMX file for the {@link TiledMap} that has
	 *            loaded this tileset
	 */
	public void loadTexture(FileHandle tmxPath) {
		tilesetSource.loadTexture(tmxPath);
	}

	/**
	 * Loads the tileset image
	 * 
	 * @param assetManager The {@link AssetManager} to use
	 * @param tmxPath The path of the TMX file for the {@link TiledMap} that has
	 *            loaded this tileset
	 */
	public void loadTexture(AssetManager assetManager, FileHandle tmxPath) {
		tilesetSource.loadTexture(tmxPath);
	}

	/**
	 * Loads the tileset image
	 * @param textureAtlas The {@link TextureAtlas} to load the image from
	 */
	public void loadTexture(TextureAtlas textureAtlas) {
		tilesetSource.loadTexture(textureAtlas);
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
	 * 
	 * @param x
	 *            The x coordinate of the tile on the tileset
	 * @param y
	 *            The y coordinate of the tile on the tileset
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
	 * <a href="https://github.com/bjorn/tiled/wiki/TMX-Map-Format">TMX Map
	 * Format</a>
	 * 
	 * @return
	 */
	public int getFirstGid() {
		return firstGid;
	}

	/**
	 * Returns the tileset source (image/TSX)
	 * @return
	 */
	public TilesetSource getTilesetSource() {
		return tilesetSource;
	}

	/**
	 * Returns the {@link TilesetSource} UUID generated by mini2Dx
	 * @return A non-null String
	 */
	public String getSourceInternalUuid() {
		return tilesetSource.getInternalUuid();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Tileset tileset = (Tileset) o;
		return firstGid == tileset.firstGid && Objects.equals(tilesetSource, tileset.tilesetSource);
	}

	@Override
	public int hashCode() {
		return Objects.hash(tilesetSource, firstGid);
	}

	@Override
	public String toString() {
		return "Tileset{" +
				"tilesetSource=" + tilesetSource +
				", firstGid=" + firstGid +
				'}';
	}
}
