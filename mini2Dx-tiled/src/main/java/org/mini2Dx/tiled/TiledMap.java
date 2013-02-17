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

import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.files.FileHandle;

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

	/**
	 * Constructs an empty map
	 */
	public TiledMap() {
		tilesets = new ArrayList<Tileset>();
		tileLayers = new ArrayList<TileLayer>();
		objectGroups = new ArrayList<TiledObjectGroup>();
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
	public TiledMap(FileHandle fileHandle)
			throws IOException {
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
	}

	public void draw(Graphics g, int x, int y) {
		draw(g, x, y, 0, 0, width, height);
	}

	public void draw(Graphics g, int x, int y, int startTileX, int startTileY,
			int width, int height) {
		for (int i = 0; i < tileLayers.size(); i++) {
			draw(g, x, y, startTileX, startTileY, width, height, i);
		}
	}

	public void draw(Graphics g, int x, int y, int startTileX, int startTileY,
			int width, int height, int layer) {
		drawLayer(g, tileLayers.get(layer), x, y, startTileX, startTileY,
				width, height);
	}

	/**
	 * Developers can override this method to implement sprite rendering
	 * 
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
	protected void onLayerRendered(TileLayer layer, int startTileX,
			int startTileY, int width, int height) {

	}

	private void drawLayer(Graphics g, TileLayer layer, int renderX,
			int renderY, int startTileX, int startTileY, int width, int height) {
		for (int y = startTileY; y < height && y < getHeight(); y++) {
			for (int x = startTileX; x < width && x < getWidth(); x++) {
				int tileId = layer.getTileId(x, y);

				if (tileId > 0) {
					int tileRenderX = renderX + (x * getTileWidth());
					int tileRenderY = renderY + (y * getTileHeight());

					for (int i = 0; i < tilesets.size(); i++) {
						Tileset tileset = tilesets.get(i);
						if (tileset.contains(tileId)) {
							tileset.drawTile(g, tileId, tileRenderX,
									tileRenderY);
							break;
						}
					}
				}
			}
		}
		onLayerRendered(layer, startTileX, startTileY, width, height);
	}

	public boolean containsProperty(String propertyName) {
		if (properties == null)
			return false;
		return properties.containsKey(propertyName);
	}

	public String getProperty(String propertyName) {
		if (properties == null)
			return null;
		return properties.get(propertyName);
	}

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

	public Orientation getOrientation() {
		return orientation;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public List<Tileset> getTilesets() {
		return tilesets;
	}

	public List<TileLayer> getTileLayers() {
		return tileLayers;
	}

	public List<TiledObjectGroup> getObjectGroups() {
		return objectGroups;
	}
}
