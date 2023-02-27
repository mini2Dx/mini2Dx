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
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.graphics.TextureAtlas;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.IntMap;
import org.mini2Dx.tiled.exception.TiledException;
import org.mini2Dx.tiled.exception.UnsupportedOrientationException;
import org.mini2Dx.tiled.renderer.*;

/**
 * A Tiled map instance
 */
public class TiledMap {
	private static int INITIAL_TILE_ID_TO_TILESET_MAP_SIZE = 512;

	/**
	 * Set to true to tell the renderer to not render layers marked as hidden in Tiled
	 */
	public static boolean STRICT_LAYER_VISIBILITY = false;
	/**
	 * Set to true to optimise CPU time (at cost of memory usage) for rendering tile layers that are mostly empty
	 */
	public static boolean FAST_RENDER_EMPTY_LAYERS = false;
	/**
	 * Threshold for when {@link #FAST_RENDER_EMPTY_LAYERS} is applied to a layer. Defaults to 0.8 (80% of layer is empty).
	 */
	public static float FAST_RENDER_EMPTY_LAYERS_THRESHOLD = 0.8f;
	/**
	 * Set to true if mini2Dx should check the viewport bounds while rendering a tile and avoid rendering it if it is outside of the bounds
	 */
	public static boolean CLIP_TILES_OUTSIDE_GRAPHICS_VIEWPORT = false;
	/**
	 * Set to true if all maps share the same set of tilesets
	 */
	public static boolean SHARED_TILE_ID_MAP = false;

	private static final IntMap<Tileset> GLOBAL_TILE_ID_TO_TILESET = new IntMap<>(INITIAL_TILE_ID_TO_TILESET_MAP_SIZE);

	private final TiledMapData tiledMapData;
	private final IntMap<Tileset> tileIdToTileset;

	private TileLayerRenderer tileLayerRenderer;
	private TiledObjectGroupRenderer tiledObjectGroupRenderer;

	/**
	 * Constructs a map from a TMX file
	 * 
	 * @param fileHandle
	 *            A {@link FileHandle} to a .tmx file
	 * @throws TiledException
	 *             Thrown if there were issues with the loaded map
	 */
	public TiledMap(FileHandle fileHandle) {
		this(fileHandle, true);
	}

	/**
	 * Constructs a map from a TMX file
	 * 
	 * @param fileHandle
	 *            A {@link FileHandle} to a .tmx file
	 * @param loadTilesetTextures
	 *            True if the tileset images should be loaded
	 * @throws TiledException
	 *             Thrown if there were issues with the loaded map
	 */
	public TiledMap(FileHandle fileHandle, boolean loadTilesetTextures) {
		this(new TiledParser(), fileHandle, loadTilesetTextures);
	}

	/**
	 * Constructs a map from a TMX file
	 * 
	 * @param parser
	 *            An existing {@link TiledParser} instance
	 * @param fileHandle
	 *            A {@link FileHandle} to a .tmx file
	 * @param loadTilesetTextures
	 *            True if the tileset images should be loaded
	 * @throws TiledException
	 *             Thrown if there were issues with the loaded map
	 */
	public TiledMap(TiledParser parser, FileHandle fileHandle, boolean loadTilesetTextures) {
		this(new TiledMapData(parser, fileHandle), loadTilesetTextures);
	}

	public TiledMap(TiledMapData tiledMapData, boolean loadTilesetTextures) {
		super();
		this.tiledMapData = tiledMapData;

		tileIdToTileset = SHARED_TILE_ID_MAP ? GLOBAL_TILE_ID_TO_TILESET : new IntMap<>(INITIAL_TILE_ID_TO_TILESET_MAP_SIZE);

		if (loadTilesetTextures) {
			loadTilesetTextures();
		}

		switch (tiledMapData.getOrientation()) {
		case ORTHOGONAL:
			tileLayerRenderer = new OrthogonalTileLayerRenderer(this, tileIdToTileset);
			break;
		case ISOMETRIC:
			tileLayerRenderer = new IsometricTileLayerRenderer(this, tileIdToTileset);
			break;
		case ISOMETRIC_STAGGERED:
			// TODO: Add renderer for isometric maps
			break;
		case HEXAGONAL:
			tileLayerRenderer = new HexagonalTileLayerRenderer(this, tileIdToTileset);
			break;
		case UNKNOWN:
		default:
			throw new UnsupportedOrientationException(tiledMapData.getOrientation().name());
		}
	}

	/**
	 * Returns if the {@link Tileset} images have been loaded
	 * 
	 * @return True if all tilesets + textures have been loaded, otherwise false
	 */
	public boolean isTilesetTexturesLoaded() {
		return tiledMapData.isTilesetTexturesLoaded();
	}

	/**
	 * Returns if the {@link Tileset} images have been loaded
	 * @param ignoreObjectTemplateTilesets True if tilesets referenced by object templates should be ignored
	 * @return True if all tilesets + textures have been loaded, otherwise false
	 */
	public boolean isTilesetTexturesLoaded(boolean ignoreObjectTemplateTilesets) {
		return tiledMapData.isTilesetTexturesLoaded(ignoreObjectTemplateTilesets);
	}

	/**
	 * Loads all {@link Tileset} textures for this map if they are not already loaded<br>
	 *     Note: Depending on the texture sizes, this may need to be called over several frames
	 * @return True if all tilesets + textures have been loaded, otherwise false
	 */
	public boolean loadTilesetTextures() {
		return tiledMapData.loadTilesetTextures();
	}
	
	/**
	 * Loads all {@link Tileset} textures for this map if they are not already loaded
	 * <br>Note: Depending on the texture sizes, this may need to be called over several frames
	 * @param assetManager The {@link AssetManager} to use
	 * @return True if all tilesets + textures have been loaded, otherwise false
	 */
	public boolean loadTilesetTextures(AssetManager assetManager) {
		return tiledMapData.loadTilesetTextures(assetManager);
	}

	/**
	 * Loads all {@link Tileset} textures for this map if they are not already loaded
	 * <br>Note: Depending on the texture sizes, this may need to be called over several frames
	 * @param textureAtlas The {@link TextureAtlas} to load textures from
	 * @return True if all tilesets + textures have been loaded, otherwise false
	 */
	public boolean loadTilesetTextures(TextureAtlas textureAtlas) {
		return tiledMapData.loadTilesetTextures(textureAtlas);
	}

	/**
	 * Loads all {@link Tileset} textures for this map if they are not already loaded
	 * <br>Note: Depending on the texture sizes, this may need to be called over several frames
	 * @param loadObjectTemplateTilesets True if tilesets used by object templates should be loaded
	 * @return True if all tilesets + textures have been loaded, otherwise false
	 */
	public boolean loadTilesetTextures(boolean loadObjectTemplateTilesets) {
		return tiledMapData.loadTilesetTextures(loadObjectTemplateTilesets);
	}

	/**
	 * Loads all {@link Tileset} textures for this map if they are not already loaded
	 * <br>Note: Depending on the texture sizes, this may need to be called over several frames
	 * @param loadObjectTemplateTilesets True if tilesets used by object templates should be loaded
	 * @param assetManager The {@link AssetManager} to use
	 * @return True if all tilesets + textures have been loaded, otherwise false
	 */
	public boolean loadTilesetTextures(AssetManager assetManager, boolean loadObjectTemplateTilesets) {
		return tiledMapData.loadTilesetTextures(assetManager, loadObjectTemplateTilesets);
	}

	/**
	 * Loads all {@link Tileset} textures for this map if they are not already loaded
	 * <br>Note: Depending on the texture sizes, this may need to be called over several frames
	 * @param loadObjectTemplateTilesets True if tilesets used by object templates should be loaded
	 * @param textureAtlas The {@link TextureAtlas} to load textures from
	 * @return True if all tilesets + textures have been loaded, otherwise false
	 */
	public boolean loadTilesetTextures(TextureAtlas textureAtlas, boolean loadObjectTemplateTilesets) {
		return tiledMapData.loadTilesetTextures(textureAtlas, loadObjectTemplateTilesets);
	}

	/**
	 * Updates map elements such as animated tiles
	 * 
	 * @param delta
	 *            The time since the last frame (in seconds)
	 */
	public void update(float delta) {
		for (int i = 0; i < tiledMapData.getTilesets().size; i++) {
			final Tileset tileset = tiledMapData.getTilesets().get(i);
			if(tileset.getTilesetSource() == null) {
				return;
			}
			tileset.getTilesetSource().updateAnimatedTiles(delta);
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
		draw(g, x, y, 0, 0, tiledMapData.getWidth(), tiledMapData.getHeight());
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
	 *            The layer index to render (Note: The indicies only represent the root layers)
	 */
	public void draw(Graphics g, int x, int y, int layer) {
		draw(g, x, y, 0, 0, tiledMapData.getWidth(), tiledMapData.getHeight(), layer);
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
	public void draw(Graphics g, int x, int y, int startTileX, int startTileY, int widthInTiles, int heightInTiles) {
		for (int i = 0; i < tiledMapData.getTotalLayers(); i++) {
			draw(g, x, y, startTileX, startTileY, widthInTiles, heightInTiles, i);
		}
	}

	/**
	 * Draws a section of a specified layer of the map at the specified coordinates
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
	 *            The layer index to render (Note: The indicies only represent the root layers)
	 */
	public void draw(Graphics g, int x, int y, int startTileX, int startTileY, int widthInTiles, int heightInTiles,
			int layer) {
		draw(g, x, y, startTileX, startTileY, widthInTiles, heightInTiles, layer, 1.0f);
	}

	/**
	 * Draws a section of a specified layer of the map at the specified coordinates
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
	 *            The layer index to render (Note: The indicies only represent the root layers)
	 * @param alpha
	 *            The alpha blending value to render with
	 */
	public void draw(Graphics g, int x, int y, int startTileX, int startTileY, int widthInTiles, int heightInTiles,
	                 int layer, float alpha) {
		Layer tiledLayer = tiledMapData.getLayers().get(layer);
		drawLayer(g, tiledLayer, x, y, startTileX, startTileY, widthInTiles, heightInTiles, alpha);
	}

	private void drawLayer(Graphics g, Layer tiledLayer, int x, int y, int startTileX, int startTileY, int widthInTiles, int heightInTiles, float alpha) {
		switch (tiledLayer.getLayerType()) {
		case IMAGE:
			break;
		case OBJECT:
			drawTiledObjectGroup(g, (TiledObjectGroup) tiledLayer, x, y, startTileX, startTileY, widthInTiles,
					heightInTiles, alpha);
			break;
		case TILE:
			drawTileLayer(g, (TileLayer) tiledLayer, x, y, startTileX, startTileY, widthInTiles, heightInTiles, alpha);
			break;
		case GROUP:
			drawGroupLayer(g, (GroupLayer) tiledLayer, x, y, startTileX, startTileY, widthInTiles, heightInTiles, alpha);
			break;
		default:
			break;
		}
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
	protected boolean preTileLayerRendered(Graphics g, TileLayer layer, int startTileX, int startTileY, int widthInTiles,
	                                       int heightInTiles) {
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
	protected void onTileLayerRendered(Graphics g, TileLayer layer, int startTileX, int startTileY, int widthInTiles,
	                                   int heightInTiles) {
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
	 *            The {@link GroupLayer} which was just rendered
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
	protected boolean preGroupLayerRendered(Graphics g, GroupLayer layer, int startTileX, int startTileY, int widthInTiles,
	                                       int heightInTiles) {
		return true;
	}

	/**
	 * Developers can override this method to implement sprite rendering
	 *
	 * @param g
	 *            The {@link Graphics} context used for rendering
	 * @param layer
	 *            The {@link GroupLayer} which was just rendered
	 * @param startTileX
	 *            The x coordinate in tiles where rendering started
	 * @param startTileY
	 *            The y coordinate in tiles where rendering started
	 * @param widthInTiles
	 *            The amount of tiles that were rendered along the X axis
	 * @param heightInTiles
	 *            The amount of tiles that were rendered along the Y axis
	 */
	protected void onGroupLayerRendered(Graphics g, GroupLayer layer, int startTileX, int startTileY, int widthInTiles,
	                                    int heightInTiles) {
	}

	public void drawTileLayer(Graphics g, TileLayer layer, int renderX, int renderY, int startTileX, int startTileY,
			int widthInTiles, int heightInTiles, float alpha) {
		if (!isTilesetTexturesLoaded(true)) {
			Mdx.log.error(TiledMap.class.getSimpleName(), "Attempting to render TiledMap without its tilesets loaded");
			return;
		}

		if(STRICT_LAYER_VISIBILITY) {
			if(!layer.isVisible()) {
				return;
			}
		}

		if (!preTileLayerRendered(g, layer, startTileX, startTileY, widthInTiles, heightInTiles))
			return;

		tileLayerRenderer.drawLayer(g, layer, renderX, renderY, startTileX, startTileY, widthInTiles, heightInTiles, alpha);

		onTileLayerRendered(g, layer, startTileX, startTileY, widthInTiles, heightInTiles);
	}

	private void drawTiledObjectGroup(Graphics g, TiledObjectGroup objectGroup, int renderX, int renderY,
			int startTileX, int startTileY, int widthInTiles, int heightInTiles, float alpha) {
		if (tiledObjectGroupRenderer == null) {
			return;
		}
		tiledObjectGroupRenderer.drawObjectGroup(g, objectGroup, renderX, renderY, startTileX, startTileY, widthInTiles,
				heightInTiles, alpha);
	}

	public void drawGroupLayer(Graphics g, GroupLayer layer, int renderX, int renderY, int startTileX, int startTileY,
	                          int widthInTiles, int heightInTiles, float alpha) {
		if (!isTilesetTexturesLoaded(true)) {
			Mdx.log.error(TiledMap.class.getSimpleName(), "Attempting to render TiledMap without its tilesets loaded");
			return;
		}

		if(STRICT_LAYER_VISIBILITY) {
			if(!layer.isVisible()) {
				return;
			}
		}

		if (!preGroupLayerRendered(g, layer, startTileX, startTileY, widthInTiles, heightInTiles))
			return;

		for(Layer childLayer : layer.layers) {
			drawLayer(g, childLayer, renderX, renderY, startTileX, startTileY, widthInTiles, heightInTiles, alpha);
		}

		onGroupLayerRendered(g, layer, startTileX, startTileY, widthInTiles, heightInTiles);
	}

	/**
	 * Returns if the map contains the specified property
	 * 
	 * @param propertyName
	 *            The property name to search for
	 * @return True if the map contains the property
	 */
	public boolean containsProperty(String propertyName) {
		return tiledMapData.containsProperty(propertyName);
	}

	/**
	 * Returns the value of a specified property
	 * 
	 * @param propertyName
	 *            The property name to search for
	 * @return Null if there is no such property
	 */
	public String getProperty(String propertyName) {
		return tiledMapData.getProperty(propertyName);
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
		tiledMapData.setProperty(propertyName, value);
	}

	/**
	 * Returns the {@link TileLayer} with the given name
	 * 
	 * @param name
	 *            The name to search for
	 * @return Null if there is no such {@link TileLayer}
	 */
	public TileLayer getTileLayer(String name) {
		return tiledMapData.getTileLayer(name);
	}

	/**
	 * Returns the {@link TileLayer} with the given name
	 *
	 * @param name The name to search for
	 * @param recursive False if only the root's immediate child layers should be searched (ignoring descendants)
	 * @return Null if there is no such {@link TileLayer}
	 */
	public TileLayer getTileLayer(String name, boolean recursive) {
		return tiledMapData.getTileLayer(name, recursive);
	}

	/**
	 * Returns the {@link TileLayer} at the given index
	 * 
	 * @param index
	 *            The index of the layer
	 * @return Null if the index is out of bounds
	 */
	public TileLayer getTileLayer(int index) {
		return tiledMapData.getTileLayer(index);
	}

	/**
	 * Returns the {@link GroupLayer} with the given name
	 * @param name The name of the layer
	 * @return Null if the layer does not exist
	 */
	public GroupLayer getGroupLayer(String name) {
		return tiledMapData.getGroupLayer(name);
	}

	/**
	 * Returns the {@link GroupLayer} with the given name
	 * @param name The name of the layer
	 * @param recursive False if only the root's immediate child layers should be searched (ignoring descendants)
	 * @return Null if the layer does not exist
	 */
	public GroupLayer getGroupLayer(String name, boolean recursive) {
		return tiledMapData.getGroupLayer(name, recursive);
	}

	/**
	 * Returns the {@link GroupLayer} at the given index
	 * @param index The index of the layer
	 * @return Null if the index is out of bounds
	 */
	public GroupLayer getGroupLayer(int index) {
		return tiledMapData.getGroupLayer(index);
	}

	/**
	 * Returns the {@link TiledObjectGroup} with the given name
	 * 
	 * @param name
	 *            The name to search for
	 * @return Null if there is no such {@link TiledObjectGroup}
	 */
	public TiledObjectGroup getObjectGroup(String name) {
		return tiledMapData.getObjectGroup(name);
	}

	/**
	 * Returns the {@link TiledObjectGroup} with the given name
	 *
	 * @param name The name to search for
	 * @param recursive False if only the root's immediate layers should be searched (ignoring descendants)
	 * @return Null if there is no such {@link TiledObjectGroup}
	 */
	public TiledObjectGroup getObjectGroup(String name, boolean recursive) {
		return tiledMapData.getObjectGroup(name, recursive);
	}

	/**
	 * Returns all the {@link TiledObjectGroup}s in this map
	 * 
	 * @return Null if there are no {@link TiledObjectGroup}s
	 */
	public Iterable<TiledObjectGroup> getObjectGroups() {
		return tiledMapData.getObjectGroups();
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
		return tiledMapData.getLayerIndex(name);
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
		return tiledMapData.getLayerIndexIgnoreCase(name);
	}

	/**
	 * Returns the {@link Tile} for the given tile ID
	 * 
	 * @param tileId
	 *            The tile ID to search for
	 * @return Null if there is no {@link Tile} with the given ID
	 */
	public Tile getTile(int tileId) {
		return tiledMapData.getTile(tileId);
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
		return tiledMapData.getTile(x, y, layer);
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
		if (tileLayerRenderer != null) {
			tileLayerRenderer.dispose();
		}
		tiledMapData.dispose(disposeTilesets);
	}

	/**
	 * Returns the {@link Orientation} of this map
	 * 
	 * @return
	 */
	public Orientation getOrientation() {
		return tiledMapData.getOrientation();
	}

	/**
	 * Returns the {@link StaggerAxis} of this map
	 * 
	 * @return Null if there is no value
	 */
	public StaggerAxis getStaggerAxis() {
		return tiledMapData.getStaggerAxis();
	}

	/**
	 * Returns the {@link StaggerIndex} of this map
	 * 
	 * @return Null if there is no value
	 */
	public StaggerIndex getStaggerIndex() {
		return tiledMapData.getStaggerIndex();
	}

	/**
	 * Returns the stagger side length of this map
	 * 
	 * @return -1 if there is no value
	 */
	public int getSideLength() {
		return tiledMapData.getSideLength();
	}

	/**
	 * Returns the width of the map in tiles
	 * 
	 * @return
	 */
	public int getWidth() {
		return tiledMapData.getWidth();
	}

	/**
	 * Returns the height of the map in tiles
	 * 
	 * @return
	 */
	public int getHeight() {
		return tiledMapData.getHeight();
	}

	/**
	 * Returns the width of tiles in pixels
	 * 
	 * @return
	 */
	public int getTileWidth() {
		return tiledMapData.getTileWidth();
	}

	/**
	 * Returns the height of tiles in pixels
	 * 
	 * @return
	 */
	public int getTileHeight() {
		return tiledMapData.getTileHeight();
	}

	/**
	 * Returns the width of the map in pixels
	 * 
	 * @return
	 */
	public int getPixelWidth() {
		return tiledMapData.getPixelWidth();
	}

	/**
	 * Return the height of the map in pixels
	 * 
	 * @return
	 */
	public int getPixelHeight() {
		return tiledMapData.getPixelHeight();
	}

	/**
	 * Returns the {@link Tileset}s of this map
	 * 
	 * @return An empty list if none have been loaded
	 */
	public Array<Tileset> getTilesets() {
		return tiledMapData.getTilesets();
	}

	/**
	 * Returns the {@link Layer}s of this map
	 * 
	 * @return
	 */
	public Array<Layer> getLayers() {
		return tiledMapData.getLayers();
	}

	/**
	 * Returns the total amount of {@link TiledObjectGroup} instances
	 * 
	 * @return
	 */
	public int getTotalObjectGroups() {
		return tiledMapData.getTotalObjectGroups();
	}

	/**
	 * Returns the background {@link Color} of the map
	 * 
	 * @return null by default
	 */
	public Color getBackgroundColor() {
		return tiledMapData.getBackgroundColor();
	}

	/**
	 * Sets the {@link TileLayerRenderer} implementation to use for rendering
	 * 
	 * @param tileLayerRenderer
	 *            The {@link TileLayerRenderer} to use
	 */
	public void setTileLayerRenderer(TileLayerRenderer tileLayerRenderer) {
		this.tileLayerRenderer = tileLayerRenderer;
	}

	/**
	 * Sets the {@link TiledObjectGroupRenderer} implementation to use for
	 * rendering
	 * 
	 * @param tiledObjectGroupRenderer
	 *            The {@link TiledObjectGroupRenderer} to use
	 */
	public void setTiledObjectGroupRenderer(TiledObjectGroupRenderer tiledObjectGroupRenderer) {
		this.tiledObjectGroupRenderer = tiledObjectGroupRenderer;
	}

	/**
	 * Returns if this map contains animated tiles
	 * 
	 * @return True if there are animated tiles
	 */
	public boolean containsAnimatedTiles() {
		return tiledMapData.containsAnimatedTiles();
	}

	/**
	 * Returns the underlying data object
	 * @return
	 */
	public TiledMapData getTiledMapData() {
		return tiledMapData;
	}
}
