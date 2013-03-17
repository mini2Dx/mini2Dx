/**
 * Copyright (c) 2013, mini2Dx Project
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

import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.math.MathUtils;

/**
 * An implementation of a parsed map from Tiled
 * 
 * @author Thomas Cashman
 */
public class TiledMap implements TiledParserListener {
	private Orientation orientation;
	private int width, height, tileWidth, tileHeight;
	protected List<Tileset> tilesets;
	protected List<TileLayer> tileLayers;
	protected List<TiledObjectGroup> objectGroups;
	private Map<String, String> properties;
	private boolean loadMaps = true;
	private FileHandle fileHandle;

	private SpriteCache layerCache;
	private Map<TileLayer, Integer> layerCacheIds;

	/**
	 * Constructs an empty map
	 */
	public TiledMap() {
		tilesets = new ArrayList<Tileset>();
		tileLayers = new ArrayList<TileLayer>();
		objectGroups = new ArrayList<TiledObjectGroup>();
		layerCacheIds = new HashMap<TileLayer, Integer>();
		this.loadMaps = false;
	}

	/**
	 * Constructs a map from a TMX file
	 * 
	 * @param fileHandle
	 *            A {@link FileHandle} to a .tmx file
	 * @throws IOException
	 *             Thrown if the map file could not be parsed
	 */
	public TiledMap(FileHandle fileHandle) throws IOException {
		this(fileHandle, true);
	}

	/**
	 * Constructs a map from a TMX file
	 * 
	 * @param fileHandle
	 *            A {@link FileHandle} to a .tmx file
	 * @param loadMaps
	 *            True if the tileset images should be loaded
	 * @throws IOException
	 *             Thrown if the map file could not be parsed
	 */
	public TiledMap(FileHandle fileHandle, boolean loadMaps) throws IOException {
		this();
		this.loadMaps = loadMaps;
		this.fileHandle = fileHandle;

		TiledParser parser = new TiledParser();
		parser.addListener(this);
		parser.parse(fileHandle);

		if (loadMaps) {
			layerCache = new SpriteCache();
			for (int layer = 0; layer < tileLayers.size(); layer++) {
				layerCache.beginCache();
				for (int y = 0; y < getHeight(); y++) {
					for (int x = 0; x < getWidth(); x++) {
						int tileId = tileLayers.get(layer).getTileId(x, y);

						if (tileId > 0) {
							int tileRenderX = x * getTileWidth();
							int tileRenderY = y * getTileHeight();

							for (int i = 0; i < tilesets.size(); i++) {
								Tileset tileset = tilesets.get(i);
								if (tileset.contains(tileId)) {
									layerCache.add(
											tileset.getTileImage(tileId),
											tileRenderX, tileRenderY);
									break;
								}
							}
						}
					}
				}
				int layerCacheId = layerCache.endCache();
				layerCacheIds.put(tileLayers.get(layer), layerCacheId);
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
	 * @param width
	 *            The amount of tiles across the x axis to render
	 * @param height
	 *            The amount of tiles across the y axis to render
	 */
	public void draw(Graphics g, int x, int y, int startTileX, int startTileY,
			int width, int height) {
		for (int i = 0; i < tileLayers.size(); i++) {
			draw(g, x, y, startTileX, startTileY, width, height, i);
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
	 * @param width
	 *            The amount of tiles across the x axis to render
	 * @param height
	 *            The amount of tiles across the y axis to render
	 * @param layer
	 *            The layer index to render
	 */
	public void draw(Graphics g, int x, int y, int startTileX, int startTileY,
			int width, int height, int layer) {
		drawLayer(g, tileLayers.get(layer), x, y, startTileX, startTileY,
				width, height);
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
	 * @param width
	 *            The amount of tiles that were rendered along the X axis
	 * @param height
	 *            The amount of tiles that were rendered along the Y axis
	 */
	protected void onLayerRendered(Graphics g, TileLayer layer, int startTileX,
			int startTileY, int width, int height) {

	}

	private void drawLayer(Graphics g, TileLayer layer, int renderX,
			int renderY, int startTileX, int startTileY, int width, int height) {
		int startTileRenderX = startTileX * getTileWidth();
		int tileRenderX = MathUtils.round(startTileRenderX * g.getScaleX())
				- renderX;

		int startTileRenderY = startTileY * getTileHeight();
		int tileRenderY = MathUtils.round(startTileRenderY * g.getScaleY())
				- renderY;

		Rectangle existingClip = g.removeClip();
		Rectangle newClip = new Rectangle(startTileRenderX, startTileRenderY, width
					* getTileWidth(), height * getTileHeight());

		g.translate(-tileRenderX, -tileRenderY);

		if (existingClip != null) {
			if(existingClip.intersects(newClip)) {
				g.setClip(existingClip.intersection(newClip));
			} else {
				g.setClip(existingClip);
			}
		} else {
			g.setClip(newClip);
		}

		int layerCacheId = layerCacheIds.get(layer);
		g.drawSpriteCache(layerCache, layerCacheId);

		g.removeClip();
		g.translate(tileRenderX, tileRenderY);

		if (existingClip != null)
			g.setClip(existingClip.getX(), existingClip.getY(),
					existingClip.getWidth(), existingClip.getHeight());

		onLayerRendered(g, layer, startTileX, startTileY, width, height);
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
	public void onBeginParsing(String orientation, int width, int height,
			int tileWidth, int tileHeight) {
		try {
			this.orientation = Orientation.valueOf(orientation.toUpperCase());
		} catch (Exception e) {
			this.orientation = Orientation.UNKNOWN;
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
	public void onTilePropertyParsed(int tileId, String propertyName,
			String value) {

	}

	@Override
	public void onTilesetParsed(Tileset parsedTileset) {
		if (loadMaps && !parsedTileset.isTextureLoaded()) {
			parsedTileset.loadTexture(fileHandle.parent());
		}
		this.tilesets.add(parsedTileset);
	}

	@Override
	public void onTileLayerParsed(TileLayer parsedLayer) {
		this.tileLayers.add(parsedLayer);
	}

	@Override
	public void onObjectGroupParsed(TiledObjectGroup parsedObjectGroup) {
		this.objectGroups.add(parsedObjectGroup);
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
}
