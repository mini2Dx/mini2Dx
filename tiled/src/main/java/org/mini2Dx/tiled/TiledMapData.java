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

import org.mini2Dx.core.assets.AssetDescriptor;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.graphics.TextureAtlas;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.IntSet;
import org.mini2Dx.gdx.utils.ObjectMap;
import org.mini2Dx.gdx.utils.ObjectSet;
import org.mini2Dx.tiled.exception.TiledException;
import org.mini2Dx.tiled.exception.TiledParsingException;
import org.mini2Dx.tiled.renderer.AnimatedTileRenderer;

import java.io.IOException;

/**
 * Data parsed from a TMX file created with Tiled
 */
public class TiledMapData implements TiledParserListener {
	public static long MAX_TILESET_LOAD_TIMESLICE_MILLIS = 2L;

	static final ObjectSet<String> OBJECT_TEMPLATE_TILESET_SOURCES = new ObjectSet<String>();
	protected final FileHandle fileHandle;

	protected final Array<Tileset> tilesets = new Array<Tileset>(true, 2, Tileset.class);
	protected final IntSet tilesetGids = new IntSet();
	protected final Array<Layer> layers = new Array<Layer>(true, 2, Layer.class);
	protected final ObjectMap<String, TiledObjectGroup> objectGroups = new ObjectMap<String, TiledObjectGroup>(8);

	private String orientationValue;
	private Orientation orientation;
	private StaggerAxis staggerAxis;
	private StaggerIndex staggerIndex;
	private int width, height, tileWidth, tileHeight, pixelWidth, pixelHeight, sideLength;
	private Color backgroundColor;
	private Array<Tile> animatedTiles;
	private ObjectMap<String, String> properties;

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

		tiledParser.setListener(this);
		try {
			tiledParser.parseTmx(fileHandle);
		} catch (IOException e) {
			tiledParser.setListener(null);
			throw new TiledParsingException(e);
		}
		tiledParser.setListener(null);
	}

	public Array<AssetDescriptor> getDependencies() {
		Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>();
		for (int i = 0; i < tilesets.size; i++) {
			dependencies.addAll(tilesets.get(i).getDependencies(fileHandle));
		}
		return dependencies;
	}

	/**
	 * Loads all {@link Tileset} textures for this map if they are not already loaded<br>
	 *     Note: Depending on the texture sizes, this may need to be called over several frames
	 * @return True if all tilesets + textures have been loaded, otherwise false
	 */
	public boolean loadTilesetTextures() {
		return loadTilesetTextures(true);
	}

	/**
	 * Loads all {@link Tileset} textures for this map if they are not already loaded
	 * <br>Note: Depending on the texture sizes, this may need to be called over several frames
	 * @param assetManager The {@link AssetManager} to use
	 * @return True if all tilesets + textures have been loaded, otherwise false
	 */
	public boolean loadTilesetTextures(AssetManager assetManager) {
		return loadTilesetTextures(assetManager, true);
	}

	/**
	 * Loads all {@link Tileset} textures for this map if they are not already loaded
	 * <br>Note: Depending on the texture sizes, this may need to be called over several frames
	 * @param textureAtlas The {@link TextureAtlas} to load textures from
	 * @return True if all tilesets + textures have been loaded, otherwise false
	 */
	public boolean loadTilesetTextures(TextureAtlas textureAtlas) {
		return loadTilesetTextures(textureAtlas, true);
	}

	/**
	 * Loads all {@link Tileset} textures for this map if they are not already loaded
	 * <br>Note: Depending on the texture sizes, this may need to be called over several frames
	 * @param loadObjectTemplateTilesets True if tilesets used by object templates should be loaded
	 * @return True if all tilesets + textures have been loaded, otherwise false
	 */
	public boolean loadTilesetTextures(boolean loadObjectTemplateTilesets) {
		final long startTime = System.currentTimeMillis();

		for (int i = 0; i < tilesets.size; i++) {
			final Tileset tileset = tilesets.get(i);
			if(tileset.isTextureLoaded()) {
				continue;
			}
			if(!loadObjectTemplateTilesets && OBJECT_TEMPLATE_TILESET_SOURCES.contains(tileset.getSourceInternalUuid())) {
				continue;
			}
			tileset.loadTexture(fileHandle);

			if(System.currentTimeMillis() - startTime >= MAX_TILESET_LOAD_TIMESLICE_MILLIS) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Loads all {@link Tileset} textures for this map if they are not already loaded
	 * <br>Note: Depending on the texture sizes, this may need to be called over several frames
	 * @param loadObjectTemplateTilesets True if tilesets used by object templates should be loaded
	 * @param assetManager The {@link AssetManager} to use
	 * @return True if all tilesets + textures have been loaded, otherwise false
	 */
	public boolean loadTilesetTextures(AssetManager assetManager, boolean loadObjectTemplateTilesets) {
		final long startTime = System.currentTimeMillis();

		for (int i = 0; i < tilesets.size; i++) {
			final Tileset tileset = tilesets.get(i);
			if(tileset.isTextureLoaded()) {
				continue;
			}
			if(!loadObjectTemplateTilesets && OBJECT_TEMPLATE_TILESET_SOURCES.contains(tileset.getSourceInternalUuid())) {
				continue;
			}
			tileset.loadTexture(assetManager, fileHandle);

			if(System.currentTimeMillis() - startTime >= MAX_TILESET_LOAD_TIMESLICE_MILLIS) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Loads all {@link Tileset} textures for this map if they are not already loaded
	 * <br>Note: Depending on the texture sizes, this may need to be called over several frames
	 * @param loadObjectTemplateTilesets True if tilesets used by object templates should be loaded
	 * @param textureAtlas The {@link TextureAtlas} to load textures from
	 * @return True if all tilesets + textures have been loaded, otherwise false
	 */
	public boolean loadTilesetTextures(TextureAtlas textureAtlas, boolean loadObjectTemplateTilesets) {
		final long startTime = System.currentTimeMillis();

		for (int i = 0; i < tilesets.size; i++) {
			final Tileset tileset = tilesets.get(i);
			if(tileset.isTextureLoaded()) {
				continue;
			}
			if(!loadObjectTemplateTilesets && OBJECT_TEMPLATE_TILESET_SOURCES.contains(tileset.getSourceInternalUuid())) {
				continue;
			}
			tileset.loadTexture(textureAtlas);

			if(System.currentTimeMillis() - startTime >= MAX_TILESET_LOAD_TIMESLICE_MILLIS) {
				return false;
			}
		}
		return true;
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
			properties = new ObjectMap<String, String>();
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
		if (tile.getTileRenderer() instanceof AnimatedTileRenderer) {
			if (animatedTiles == null) {
				animatedTiles = new Array<Tile>(true,1, Tile.class);
			}
			animatedTiles.add(tile);
		}
	}

	@Override
	public void onTilesetParsed(Tileset parsedTileset) {
		if(tilesetGids.add(parsedTileset.getFirstGid())) {
			tilesets.add(parsedTileset);
		}
	}

	@Override
	public void onTileLayerParsed(TileLayer parsedLayer) {
		parsedLayer.setIndex(layers.size);
		layers.add(parsedLayer);
	}

	@Override
	public void onObjectGroupParsed(TiledObjectGroup parsedObjectGroup) {
		parsedObjectGroup.setIndex(layers.size);
		layers.add(parsedObjectGroup);
		objectGroups.put(parsedObjectGroup.getName(), parsedObjectGroup);
	}

	@Override
	public void onGroupLayerParsed(GroupLayer parsedLayer) {
		parsedLayer.setIndex(layers.size);
		layers.add(parsedLayer);
	}

	@Override
	public void onObjectTemplateParsed(TiledObjectTemplate parsedObjectTemplate) {
		OBJECT_TEMPLATE_TILESET_SOURCES.add(parsedObjectTemplate.getTileset().getSourceInternalUuid());
	}

	/**
	 * Returns the {@link TileLayer} with the given name
	 * 
	 * @param name The name to search for
	 * @return Null if there is no such {@link TileLayer}
	 */
	public TileLayer getTileLayer(String name) {
		return getTileLayer(name, true);
	}

	/**
	 * Returns the {@link TileLayer} with the given name
	 *
	 * @param name The name to search for
	 * @param recursive False if only the root's immediate child layers should be searched (ignoring descendants)
	 * @return Null if there is no such {@link TileLayer}
	 */
	public TileLayer getTileLayer(String name, boolean recursive) {
		return getTileLayer(layers, name, recursive);
	}

	/**
	 * Returns the {@link TileLayer} with the given name
	 *
	 * @param layers The layers to search through
	 * @param name The name to search for
	 * @param recursive False if only the immediate layers should be searched (ignoring descendants)
	 * @return Null if there is no such {@link TileLayer}
	 */
	public static TileLayer getTileLayer(final Array<Layer> layers, String name, boolean recursive) {
		for (Layer layer : layers) {
			if (layer.getLayerType().equals(LayerType.TILE)) {
				if (layer.getName().compareTo(name) == 0) {
					return (TileLayer) layer;
				}
			} else if(recursive && layer.getLayerType().equals(LayerType.GROUP)) {
				GroupLayer groupLayer = (GroupLayer) layer;
				TileLayer result = getTileLayer(groupLayer.layers, name, recursive);
				if(result != null) {
					return result;
				}
			}
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
		if (index < 0 || index >= layers.size) {
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
		return getObjectGroup(name, true);
	}

	/**
	 * Returns the {@link TiledObjectGroup} with the given name
	 *
	 * @param name The name to search for
	 * @param recursive False if only the immediate layers should be searched (ignoring descendants)
	 * @return Null if there is no such {@link TiledObjectGroup}
	 */
	public TiledObjectGroup getObjectGroup(String name, boolean recursive) {
		return getObjectGroup(layers, objectGroups, name, recursive);
	}

	/**
	 * Returns the {@link TiledObjectGroup} with the given name
	 *
	 * @param layers The layers to search through
	 * @param objectGroups A map of layer names to object groups
	 * @param name The name to search for
	 * @param recursive False if only the immediate layers should be searched (ignoring descendants)
	 * @return Null if there is no such {@link TiledObjectGroup}
	 */
	public static TiledObjectGroup getObjectGroup(final Array<Layer> layers, final ObjectMap<String, TiledObjectGroup> objectGroups,
	                                       String name, boolean recursive) {
		TiledObjectGroup result = objectGroups.get(name, null);
		if(result != null) {
			return result;
		}
		if(!recursive) {
			return null;
		}
		for (Layer layer : layers) {
			if (!layer.getLayerType().equals(LayerType.GROUP)) {
				continue;
			}
			final GroupLayer groupLayer = (GroupLayer) layer;
			result = getObjectGroup(groupLayer.layers, groupLayer.objectGroups, name, recursive);
			if(result != null) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the {@link GroupLayer} with the given name
	 * @param name The name of the layer
	 * @return Null if the layer does not exist
	 */
	public GroupLayer getGroupLayer(String name) {
		return getGroupLayer(name, true);
	}

	/**
	 * Returns the {@link GroupLayer} with the given name
	 * @param name The name of the layer
	 * @param recursive False if only the root's immediate child layers should be searched (ignoring descendants)
	 * @return Null if the layer does not exist
	 */
	public GroupLayer getGroupLayer(String name, boolean recursive) {
		return getGroupLayer(layers, name, recursive);
	}

	/**
	 * Returns the {@link GroupLayer} with the given name
	 * @param layers The layers to search through
	 * @param name The name of the layer
	 * @param recursive False if only the immediate layers should be searched (ignoring descendants)
	 * @return Null if the layer does not exist
	 */
	public static GroupLayer getGroupLayer(final Array<Layer> layers, String name, boolean recursive) {
		for (Layer layer : layers) {
			if (!layer.getLayerType().equals(LayerType.GROUP)) {
				continue;
			}
			if (layer.getName().compareTo(name) == 0) {
				return (GroupLayer) layer;
			} else if(recursive) {
				GroupLayer result = getGroupLayer(((GroupLayer) layer).layers, name, recursive);
				if(result != null) {
					return result;
				}
			}
		}
		return null;
	}

	/**
	 * Returns the {@link GroupLayer} at the given index
	 * @param index The index of the layer
	 * @return Null if the index is out of bounds
	 */
	public GroupLayer getGroupLayer(int index) {
		if (index < 0 || index >= layers.size) {
			return null;
		}
		return (GroupLayer) layers.get(index);
	}

	/**
	 * Returns all the {@link TiledObjectGroup}s in this map
	 * 
	 * @return Null if there are no {@link TiledObjectGroup}s
	 */
	public Iterable<TiledObjectGroup> getObjectGroups() {
		if (objectGroups.size == 0) {
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
		for (int i = 0; i < layers.size; i++) {
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
		for (int i = 0; i < layers.size; i++) {
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
		for (int i = 0; i < tilesets.size; i++) {
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
	public Array<Tileset> getTilesets() {
		return tilesets;
	}

	/**
	 * Returns the {@link Layer}s of this map
	 * 
	 * @return
	 */
	public Array<Layer> getLayers() {
		return layers;
	}

	public Array<Tile> getAnimatedTiles() {
		return animatedTiles;
	}

	/**
	 * Returns the total amount of {@link TiledObjectGroup} instances
	 * 
	 * @return
	 */
	public int getTotalObjectGroups() {
		return objectGroups.size;
	}

	/**
	 * Returns the total amount of {@link Layer} instances
	 * 
	 * @return
	 */
	public int getTotalLayers() {
		return layers.size;
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
		return animatedTiles.size > 0;
	}

	/**
	 * Returns if the {@link Tileset} images have been loaded
	 *
	 * @return True if they have been loaded
	 */
	public boolean isTilesetTexturesLoaded() {
		return isTilesetTexturesLoaded(false);
	}

	/**
	 * Returns if the {@link Tileset} images have been loaded
	 * @param ignoreObjectTemplateTilesets True if tilesets referenced by object templates should be ignored
	 * @return True if they have been loaded
	 */
	public boolean isTilesetTexturesLoaded(boolean ignoreObjectTemplateTilesets) {
		for (int i = 0; i < tilesets.size; i++) {
			final Tileset tileset = tilesets.get(i);
			if (ignoreObjectTemplateTilesets && OBJECT_TEMPLATE_TILESET_SOURCES.contains(tileset.getSourceInternalUuid())) {
				continue;
			}
			if (!tileset.isTextureLoaded()) {
				return false;
			}
		}
		return true;
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
		for (int i = 0; i < tilesets.size; i++) {
			tilesets.get(i).dispose();
		}
	}
}
