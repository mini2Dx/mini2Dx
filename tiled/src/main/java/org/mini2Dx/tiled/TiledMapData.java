/**
 * Copyright (c) 2017 See AUTHORS file
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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mini2Dx.tiled.exception.TiledException;
import org.mini2Dx.tiled.exception.TiledParsingException;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/**
 * Data parsed from a TMX file created with Tiled
 */
public class TiledMapData implements TiledParserListener {
	protected final FileHandle fileHandle;
	protected final List<Tileset> tilesets = new ArrayList<Tileset>();
	protected final List<Layer> layers = new ArrayList<Layer>();
	protected final Map<String, TiledObjectGroup> objectGroups = new HashMap<String, TiledObjectGroup>();

	private String orientationValue;
	private Orientation orientation;
	private StaggerAxis staggerAxis;
	private StaggerIndex staggerIndex;
	private int width, height, tileWidth, tileHeight, pixelWidth, pixelHeight, sideLength;
	private Color backgroundColor;
	private List<Tile> animatedTiles;
	private Map<String, String> properties;

	/**
	 * 
	 * @param fileHandle
	 * @throws TiledException
	 */
	public TiledMapData(FileHandle fileHandle) {
		this(new TiledParser(), fileHandle);
	}

	/**
	 * 
	 * @param tiledParser
	 * @param fileHandle
	 * @throws TiledException
	 */
	public TiledMapData(TiledParser tiledParser, FileHandle fileHandle) {
		super();
		this.fileHandle = fileHandle;

		tiledParser.addListener(this);
		try {
			tiledParser.parseTmx(fileHandle);
		} catch (IOException e) {
			tiledParser.removeListener(this);
			throw new TiledParsingException(e);
		}
		tiledParser.removeListener(this);
	}

	public Array<AssetDescriptor> getDependencies() {
		Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>();
		for (int i = 0; i < tilesets.size(); i++) {
			dependencies.addAll(tilesets.get(i).getDependencies(fileHandle));
		}
		return dependencies;
	}

	public void loadTilesetTextures() {
		for (int i = 0; i < tilesets.size(); i++) {
			if (!tilesets.get(i).isTextureLoaded()) {
				tilesets.get(i).loadTexture(fileHandle);
			}
		}
	}

	public void loadTilesetTextures(AssetManager assetManager) {
		for (int i = 0; i < tilesets.size(); i++) {
			if (!tilesets.get(i).isTextureLoaded()) {
				tilesets.get(i).loadTexture(assetManager, fileHandle);
			}
		}
	}

	@Override
	public void onBeginParsing(String orientation, String staggerAxis, String staggerIndex, Color backgroundColor,
			int width, int height, int tileWidth, int tileHeight, int sideLength) {
		this.orientationValue = orientation;
		try {
			this.orientation = Orientation.valueOf(orientation.toUpperCase());
		} catch (Exception e) {
			this.orientation = Orientation.UNKNOWN;
		}
		if (backgroundColor != null) {
			this.backgroundColor = backgroundColor;
		}
		if (staggerAxis != null) {
			this.staggerAxis = StaggerAxis.valueOf(staggerAxis.toUpperCase());
			if (sideLength < 0) {
				switch (this.staggerAxis) {
				case X:
					sideLength = tileWidth / 2;
					break;
				case Y:
				default:
					sideLength = tileHeight / 2;
					break;
				}
			}
		}
		if (staggerIndex != null) {
			this.staggerIndex = StaggerIndex.valueOf(staggerIndex.toUpperCase());
		}
		this.width = width;
		this.height = height;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.sideLength = sideLength;

		switch (this.orientation) {
		case HEXAGONAL:
			switch (this.staggerAxis) {
			case X:
				this.pixelWidth = MathUtils.round(((tileWidth * 0.75f) * width) + (tileWidth * 0.25f));
				this.pixelHeight = MathUtils.round((tileHeight * height) + (tileHeight * 0.5f));
				break;
			case Y:
			default:
				this.pixelWidth = MathUtils.round((tileWidth * width) + (tileWidth * 0.5f));
				this.pixelHeight = MathUtils.round(((tileHeight * 0.75f) * height) + (tileHeight * 0.25f));
				break;
			}
			break;
		case ISOMETRIC_STAGGERED:
			break;
		case ISOMETRIC:
		case ORTHOGONAL:
		case UNKNOWN:
		default:
			this.pixelWidth = width * tileWidth;
			this.pixelHeight = height * tileHeight;
			break;
		}
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
	public void onMapPropertyParsed(String propertyName, String value) {
		setProperty(propertyName, value);
	}

	@Override
	public void onTilePropertiesParsed(Tile tile) {
		if (tile.getTileRenderer() == null) {
			return;
		}
		if (animatedTiles == null) {
			animatedTiles = new ArrayList<Tile>(1);
		}
		animatedTiles.add(tile);
	}

	@Override
	public void onTilesetParsed(Tileset parsedTileset) {
		tilesets.add(parsedTileset);
	}

	@Override
	public void onTileLayerParsed(TileLayer parsedLayer) {
		parsedLayer.setIndex(layers.size());
		layers.add(parsedLayer);
	}

	@Override
	public void onObjectGroupParsed(TiledObjectGroup parsedObjectGroup) {
		parsedObjectGroup.setIndex(layers.size());
		layers.add(parsedObjectGroup);
		objectGroups.put(parsedObjectGroup.getName(), parsedObjectGroup);
	}

	/**
	 * Returns the {@link TileLayer} with the given name
	 * 
	 * @param name
	 *            The name to search for
	 * @return Null if there is no such {@link TileLayer}
	 */
	public TileLayer getTileLayer(String name) {
		for (Layer layer : layers) {
			if (layer.getName().compareTo(name) != 0) {
				continue;
			}
			if (!layer.getLayerType().equals(LayerType.TILE)) {
				continue;
			}
			return (TileLayer) layer;
		}
		return null;
	}

	/**
	 * Returns the {@link TileLayer} at the given index
	 * 
	 * @param index
	 *            The index of the layer
	 * @return Null if the index is out of bounds
	 */
	public TileLayer getTileLayer(int index) {
		if (index < 0 || index >= layers.size()) {
			return null;
		}
		return (TileLayer) layers.get(index);
	}

	/**
	 * Returns the {@link TiledObjectGroup} with the given name
	 * 
	 * @param name
	 *            The name to search for
	 * @return Null if there is no such {@link TiledObjectGroup}
	 */
	public TiledObjectGroup getObjectGroup(String name) {
		return objectGroups.get(name);
	}

	/**
	 * Returns all the {@link TiledObjectGroup}s in this map
	 * 
	 * @return Null if there are no {@link TiledObjectGroup}s
	 */
	public Collection<TiledObjectGroup> getObjectGroups() {
		if (objectGroups.isEmpty()) {
			return null;
		}
		return objectGroups.values();
	}

	/**
	 * Returns the index of the {@link TileLayer} or {@link TiledObjectGroup}
	 * with the given name
	 * 
	 * @param name
	 *            The name to search for
	 * @return -1 if there is no such {@link TileLayer} or
	 *         {@link TiledObjectGroup}
	 */
	public int getLayerIndex(String name) {
		for (int i = 0; i < layers.size(); i++) {
			Layer layer = layers.get(i);
			if (layer.getName().compareTo(name) == 0) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the index of the {@link TileLayer} or {@link TiledObjectGroup}
	 * with the given name ignoring upper/lowercase differences
	 * 
	 * @param name
	 *            The name to search for
	 * @return -1 if there is no such {@link TileLayer} or
	 *         {@link TiledObjectGroup}
	 */
	public int getLayerIndexIgnoreCase(String name) {
		for (int i = 0; i < layers.size(); i++) {
			Layer layer = layers.get(i);
			if (layer.getName().compareToIgnoreCase(name) == 0) {
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
	 * @param x
	 *            The x coordinate (in tiles)
	 * @param y
	 *            The y coordinate (in tiles)
	 * @param layer
	 *            The layer index
	 * @return Null if there is no {@link Tile}
	 */
	public Tile getTile(int x, int y, int layer) {
		Layer tiledLayer = layers.get(layer);
		if (!tiledLayer.getLayerType().equals(LayerType.TILE)) {
			return null;
		}
		return getTile(((TileLayer) tiledLayer).getTileId(x, y));
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
	 * Returns the {@link StaggerAxis} of this map
	 * 
	 * @return Null if there is no value
	 */
	public StaggerAxis getStaggerAxis() {
		return staggerAxis;
	}

	/**
	 * Returns the {@link StaggerIndex} of this map
	 * 
	 * @return Null if there is no value
	 */
	public StaggerIndex getStaggerIndex() {
		return staggerIndex;
	}

	/**
	 * Returns the stagger side length of this map
	 * 
	 * @return -1 if there is no value
	 */
	public int getSideLength() {
		return sideLength;
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
	 * Returns the width of the map in pixels
	 * 
	 * @return
	 */
	public int getPixelWidth() {
		return pixelWidth;
	}

	/**
	 * Return the height of the map in pixels
	 * 
	 * @return
	 */
	public int getPixelHeight() {
		return pixelHeight;
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
	 * Returns the {@link Layer}s of this map
	 * 
	 * @return
	 */
	public List<Layer> getLayers() {
		return layers;
	}

	public List<Tile> getAnimatedTiles() {
		return animatedTiles;
	}

	/**
	 * Returns the total amount of {@link TiledObjectGroup} instances
	 * 
	 * @return
	 */
	public int getTotalObjectGroups() {
		return objectGroups.size();
	}

	/**
	 * Returns the total amount of {@link Layer} instances
	 * 
	 * @return
	 */
	public int getTotalLayers() {
		return layers.size();
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
	 * Returns if this map contains animated tiles
	 * 
	 * @return True if there are animated tiles
	 */
	public boolean containsAnimatedTiles() {
		if (animatedTiles == null) {
			return false;
		}
		return !animatedTiles.isEmpty();
	}

	/**
	 * Returns the {@link FileHandle} for this data
	 * 
	 * @return
	 */
	public FileHandle getFileHandle() {
		return fileHandle;
	}

	/**
	 * Releases any resources used by this TiledMap including tilesets
	 */
	public void dispose() {
		dispose(true);
	}

	/**
	 * Releases any resources used by this TiledMap
	 * 
	 * @param disposeTilesets
	 *            True if tilesets should also be disposed
	 */
	public void dispose(boolean disposeTilesets) {
		if (!disposeTilesets) {
			return;
		}
		for (int i = 0; i < tilesets.size(); i++) {
			tilesets.get(i).dispose();
		}
	}
}
