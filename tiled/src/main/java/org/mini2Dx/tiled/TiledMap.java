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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.tiled.exception.TiledException;
import org.mini2Dx.tiled.exception.TiledParsingException;
import org.mini2Dx.tiled.exception.UnsupportedOrientationException;
import org.mini2Dx.tiled.renderer.OrthogonalTileLayerRenderer;
import org.mini2Dx.tiled.renderer.TileLayerRenderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;

/**
 * An implementation of a parsed map from Tiled.
 * 
 * Note that this implementation will cache renderings of the {@link TileLayer}s
 * to improve render speeds
 */
public class TiledMap implements TiledParserListener {
	private String orientationValue;
	private Orientation orientation;
	private int width, height, tileWidth, tileHeight;
	private Color backgroundColor;
	protected List<Tileset> tilesets;
	protected List<TileLayer> tileLayers;
	protected List<TiledObjectGroup> objectGroups;
	private Map<String, String> properties;
	private FileHandle fileHandle;

	private TileLayerRenderer tileLayerRenderer;

	/**
	 * Constructs an empty map
	 */
	public TiledMap() {
		tilesets = new ArrayList<Tileset>();
		tileLayers = new ArrayList<TileLayer>();
		objectGroups = new ArrayList<TiledObjectGroup>();
	}

	/**
	 * Constructs a map from a TMX file
	 * 
	 * @param fileHandle
	 *            A {@link FileHandle} to a .tmx file
	 * @throws TiledException
	 *             Thrown if there were issues with the loaded map
	 */
	public TiledMap(FileHandle fileHandle) throws TiledException {
		this(fileHandle, true, false);
	}

	/**
	 * Constructs a map from a TMX file
	 * 
	 * @param fileHandle
	 *            A {@link FileHandle} to a .tmx file
	 * @param loadTilesets
	 *            True if the tileset images should be loaded and the map
	 *            pre-rendered
	 * @throws TiledException
	 *             Thrown if there were issues with the loaded map
	 */
	public TiledMap(FileHandle fileHandle, boolean loadTilesets,
			boolean cacheLayers) throws TiledException {
		this();
		this.fileHandle = fileHandle;

		try {
			TiledParser parser = new TiledParser();
			parser.addListener(this);
			parser.parse(fileHandle);
		} catch (IOException e) {
			throw new TiledParsingException(e);
		}

		if (loadTilesets) {
			loadTilesets();
		}

		switch (orientation) {
		case ISOMETRIC:
			// TODO: Add renderer for isometric maps
			break;
		case ORTHOGONAL:
			tileLayerRenderer = new OrthogonalTileLayerRenderer(this, cacheLayers);
			break;
		case STAGGERED:
			// TODO: Add renderer for hexagonal maps
			break;
		case UNKNOWN:
		default:
			throw new UnsupportedOrientationException(orientationValue);
		}
	}

	/**
	 * Returns if the {@link Tileset} images have been loaded
	 * 
	 * @return True if they have been loaded
	 */
	public boolean isTilesetsLoaded() {
		boolean result = true;
		for (int i = 0; i < tilesets.size(); i++) {
			if (!tilesets.get(i).isTextureLoaded()) {
				return false;
			}
		}
		return result;
	}

	/**
	 * Returns the total amount of {@link Tileset}s loaded
	 * 
	 * @return 0 if none are loaded
	 */
	public int getTotalTilesetsLoaded() {
		int result = 0;
		for (int i = 0; i < tilesets.size(); i++) {
			if (tilesets.get(i).isTextureLoaded()) {
				result++;
			}
		}
		return result;
	}

	/**
	 * Loads a specific {@link Tileset}
	 * 
	 * @param tilesetIndex
	 */
	public void loadTileset(int tilesetIndex) {
		if (tilesetIndex < 0 || tilesetIndex >= tilesets.size()) {
			throw new RuntimeException("No such tileset with index "
					+ tilesetIndex);
		}
		if (!tilesets.get(tilesetIndex).isTextureLoaded()) {
			tilesets.get(tilesetIndex).loadTexture(fileHandle.parent());
		}
	}

	/**
	 * Loads all {@link Tileset}s for this map if they are not already loaded
	 */
	public void loadTilesets() {
		for (int i = 0; i < tilesets.size(); i++) {
			if (!tilesets.get(i).isTextureLoaded()) {
				tilesets.get(i).loadTexture(fileHandle.parent());
			}
		}
	}

	/**
	 * Draws the entire map at the specified coordinates
	 * 
	 * @param g
	 *            The {@link Graphics} context available for rendering
	 * @param x
	 *            The x coordinate to render at
	 * @param y
	 *            The y coordinate to render at
	 */
	public void draw(Graphics g, int x, int y) {
		draw(g, x, y, 0, 0, width, height);
	}

	/**
	 * Draws a layer of the map at the specified coordinates
	 * 
	 * @param g
	 *            The {@link Graphics} context available for rendering
	 * @param x
	 *            The x coordinate to render at
	 * @param y
	 *            The y coordinate to render at
	 * @param layer
	 *            The layer index to render
	 */
	public void draw(Graphics g, int x, int y, int layer) {
		draw(g, x, y, 0, 0, width, height, layer);
	}

	/**
	 * Draws a section of the map at the specified coordinates
	 * 
	 * @param g
	 *            The {@link Graphics} context available for rendering
	 * @param x
	 *            The x coordinate to render at
	 * @param y
	 *            The y coordinate to render at
	 * @param startTileX
	 *            The x tile coordinate in the map to start from
	 * @param startTileY
	 *            The y tile coordinate in the map to start from
	 * @param widthInTiles
	 *            The amount of tiles across the x axis to render
	 * @param heightInTiles
	 *            The amount of tiles across the y axis to render
	 */
	public void draw(Graphics g, int x, int y, int startTileX, int startTileY,
			int widthInTiles, int heightInTiles) {
		for (int i = 0; i < tileLayers.size(); i++) {
			draw(g, x, y, startTileX, startTileY, widthInTiles, heightInTiles,
					i);
		}
	}

	/**
	 * Draws a section of a specified layer of the map at the specified
	 * coordinates
	 * 
	 * @param g
	 *            The {@link Graphics} context available for rendering
	 * @param x
	 *            The x coordinate to render at
	 * @param y
	 *            The y coordinate to render at
	 * @param startTileX
	 *            The x tile coordinate in the map to start from
	 * @param startTileY
	 *            The y tile coordinate in the map to start from
	 * @param widthInTiles
	 *            The amount of tiles across the x axis to render
	 * @param heightInTiles
	 *            The amount of tiles across the y axis to render
	 * @param layer
	 *            The layer index to render
	 */
	public void draw(Graphics g, int x, int y, int startTileX, int startTileY,
			int widthInTiles, int heightInTiles, int layer) {
		drawLayer(g, tileLayers.get(layer), x, y, startTileX, startTileY,
				widthInTiles, heightInTiles);
	}

	/**
	 * Developers can override this method which is called before a layer is
	 * rendered
	 * 
	 * To prevent the layer from rendering, return false from this method.
	 * 
	 * @param g
	 *            The {@link Graphics} context used for rendering
	 * @param layer
	 *            The {@link TileLayer} which was just rendered
	 * @param startTileX
	 *            The x coordinate in tiles where rendering started
	 * @param startTileY
	 *            The y coordinate in tiles where rendering started
	 * @param widthInTiles
	 *            The amount of tiles that were rendered along the X axis
	 * @param heightInTiles
	 *            The amount of tiles that were rendered along the Y axis
	 * @return True if the layer should be rendered
	 */
	protected boolean preLayerRendered(Graphics g, TileLayer layer,
			int startTileX, int startTileY, int widthInTiles, int heightInTiles) {
		return true;
	}

	/**
	 * Developers can override this method to implement sprite rendering
	 * 
	 * @param g
	 *            The {@link Graphics} context used for rendering
	 * @param layer
	 *            The {@link TileLayer} which was just rendered
	 * @param startTileX
	 *            The x coordinate in tiles where rendering started
	 * @param startTileY
	 *            The y coordinate in tiles where rendering started
	 * @param widthInTiles
	 *            The amount of tiles that were rendered along the X axis
	 * @param heightInTiles
	 *            The amount of tiles that were rendered along the Y axis
	 */
	protected void onLayerRendered(Graphics g, TileLayer layer, int startTileX,
			int startTileY, int widthInTiles, int heightInTiles) {

	}

	private void drawLayer(Graphics g, TileLayer layer, int renderX,
			int renderY, int startTileX, int startTileY, int widthInTiles,
			int heightInTiles) {
		if (!isTilesetsLoaded()) {
			Gdx.app.error(TiledMap.class.getSimpleName(),
					"Attempting to render TiledMap without its tilesets loaded");
			return;
		}

		if (!preLayerRendered(g, layer, startTileX, startTileY, widthInTiles,
				heightInTiles))
			return;

		tileLayerRenderer.drawLayer(g, layer, renderX, renderY, startTileX,
				startTileY, widthInTiles, heightInTiles);

		onLayerRendered(g, layer, startTileX, startTileY, widthInTiles,
				heightInTiles);
	}

	/**
	 * Returns if the map contains the specified property
	 * 
	 * @param propertyName
	 *            The property name to search for
	 * @return True if the map contains the property
	 */
	public boolean containsProperty(String propertyName) {
		if (properties == null)
			return false;
		return properties.containsKey(propertyName);
	}

	/**
	 * Returns the value of a specified property
	 * 
	 * @param propertyName
	 *            The property name to search for
	 * @return Null if there is no such property
	 */
	public String getProperty(String propertyName) {
		if (properties == null)
			return null;
		return properties.get(propertyName);
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
		if (properties == null)
			properties = new HashMap<String, String>();
		properties.put(propertyName, value);
	}

	@Override
	public void onBeginParsing(String orientation, Color backgroundColor,
			int width, int height, int tileWidth, int tileHeight) {
		this.orientationValue = orientation;
		try {
			this.orientation = Orientation.valueOf(orientation.toUpperCase());
		} catch (Exception e) {
			this.orientation = Orientation.UNKNOWN;
		}
		if (backgroundColor != null) {
			this.backgroundColor = backgroundColor;
		}
		this.width = width;
		this.height = height;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
	}

	@Override
	public void onMapPropertyParsed(String propertyName, String value) {
		setProperty(propertyName, value);
	}

	@Override
	public void onTilePropertiesParsed(Tile tile) {

	}

	@Override
	public void onTilesetParsed(Tileset parsedTileset) {
		tilesets.add(parsedTileset);
	}

	@Override
	public void onTileLayerParsed(TileLayer parsedLayer) {
		parsedLayer.setIndex(tileLayers.size());
		tileLayers.add(parsedLayer);
	}

	@Override
	public void onObjectGroupParsed(TiledObjectGroup parsedObjectGroup) {
		objectGroups.add(parsedObjectGroup);
	}

	/**
	 * Returns the {@link TileLayer} with the given name
	 * 
	 * @param name
	 *            The name to search for
	 * @return Null if there is no such {@link TileLayer}
	 */
	public TileLayer getTileLayer(String name) {
		for (TileLayer layer : tileLayers) {
			if (layer.getName().compareTo(name) == 0) {
				return layer;
			}
		}
		return null;
	}
	
	/**
	 * Returns the {@link TileLayer} at the given index
	 * 
	 * @param index The index of the layer
	 * @return Null if the index is out of bounds
	 */
	public TileLayer getTileLayer(int index) {
		if(index < 0 || index >= tileLayers.size()) {
			return null;
		}
		return tileLayers.get(index);
	}

	/**
	 * Returns the {@link TiledObjectGroup} with the given name
	 * 
	 * @param name
	 *            The name to search for
	 * @return Null if there is no such {@link TiledObjectGroup}
	 */
	public TiledObjectGroup getObjectGroup(String name) {
		for (TiledObjectGroup group : objectGroups) {
			if (group.getName().compareTo(name) == 0) {
				return group;
			}
		}
		return null;
	}

	/**
	 * Returns the index of the {@link TileLayer} with the given name
	 * 
	 * @param name
	 *            The name to search for
	 * @return -1 if there is no such {@link TileLayer}
	 */
	public int getLayerIndex(String name) {
		for (int i = 0; i < tileLayers.size(); i++) {
			if (tileLayers.get(i).getName().compareTo(name) == 0) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the {@link Tile} for the given tile ID
	 * 
	 * @param tileId
	 *            The tile ID to search for
	 * @return Null if there is no {@link Tile} with the given ID
	 */
	public Tile getTile(int tileId) {
		for (int i = 0; i < tilesets.size(); i++) {
			if (tilesets.get(i).contains(tileId)) {
				return tilesets.get(i).getTile(tileId);
			}
		}
		return null;
	}
	
	/**
	 * Returns the {@link Tile} at the given coordinate on a specific layer
	 * 
	 * @param x The x coordinate (in tiles)
	 * @param y The y coordinate (in tiles)
	 * @param layer The layer index
	 * @return Null if there is no {@link Tile}
	 */
	public Tile getTile(int x, int y, int layer) {
		return getTile(tileLayers.get(layer).getTileId(x, y));
	}

	/**
	 * Releases any resources used by this TiledMap
	 */
	public void dispose() {
		if (tileLayerRenderer != null) {
			tileLayerRenderer.dispose();
		}
	}

	/**
	 * Returns the {@link Orientation} of this map
	 * 
	 * @return
	 */
	public Orientation getOrientation() {
		return orientation;
	}

	/**
	 * Returns the width of the map in tiles
	 * 
	 * @return
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of the map in tiles
	 * 
	 * @return
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns the width of tiles in pixels
	 * 
	 * @return
	 */
	public int getTileWidth() {
		return tileWidth;
	}

	/**
	 * Returns the height of tiles in pixels
	 * 
	 * @return
	 */
	public int getTileHeight() {
		return tileHeight;
	}

	/**
	 * Returns the {@link Tileset}s of this map
	 * 
	 * @return An empty list if none have been loaded
	 */
	public List<Tileset> getTilesets() {
		return tilesets;
	}

	/**
	 * Returns the {@link TileLayer}s of this map
	 * 
	 * @return An empty list if none have been loaded
	 */
	public List<TileLayer> getTileLayers() {
		return tileLayers;
	}

	/**
	 * Returns the {@link TiledObjectGroup}s of this map
	 * 
	 * @return An empty list if none have been loaded
	 */
	public List<TiledObjectGroup> getObjectGroups() {
		return objectGroups;
	}

	/**
	 * Returns the background {@link Color} of the map
	 * 
	 * @return null by default
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Sets the {@link TileLayerRenderer} implementation to use for rendering
	 * 
	 * @param tileLayerRenderer The {@link TileLayerRenderer} to use
	 */
	public void setTileLayerRenderer(TileLayerRenderer tileLayerRenderer) {
		this.tileLayerRenderer = tileLayerRenderer;
	}
}
