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
package org.mini2Dx.tiled.tileset;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetDescriptor;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.graphics.Sprite;
import org.mini2Dx.core.graphics.TextureAtlas;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.ObjectMap;
import org.mini2Dx.tiled.Tile;
import org.mini2Dx.tiled.TiledMap;
import org.mini2Dx.tiled.TiledParser;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A {@link TilesetSource} loaded from a TSX file. Allows for sharing data
 * across multiple {@link TiledMap} instances.
 */
public class TsxTilesetSource extends TilesetSource {
	private static final String LOGGING_TAG = TsxTilesetSource.class.getSimpleName();

	private static final TiledParser TSX_PARSER = new TiledParser();
	private static final ObjectMap<String, ImageTilesetSource> TILESETS = new ObjectMap<String, ImageTilesetSource>();
	private static final ObjectMap<String, AtomicInteger> TILESET_REFS = new ObjectMap<String, AtomicInteger>();

	private final String tsxPath;
	private final ImageTilesetSource tileset;

	public TsxTilesetSource(FileHandle tmxPath, String tsxPath) {
		super();
		final FileHandle tsxFileHandle = tmxPath.sibling(tsxPath);
		this.tsxPath = tsxFileHandle.normalize();

		if (!TILESETS.containsKey(this.tsxPath)) {
			try {
				TILESETS.put(this.tsxPath, TSX_PARSER.parseTsx(tsxFileHandle));
			} catch (IOException e) {
				Mdx.log.error(LOGGING_TAG, "Could not parse " + tsxPath + ". " + e.getMessage(), e);
				TILESETS.put(this.tsxPath, null);
			}
			TILESET_REFS.put(this.tsxPath, new AtomicInteger(0));
		}
		tileset = TILESETS.get(this.tsxPath);
		TILESET_REFS.get(this.tsxPath).incrementAndGet();
	}
	
	@Override
	public Array<AssetDescriptor> getDependencies(FileHandle tmxPath) {
		return tileset.getDependencies(tmxPath);
	}

	@Override
	public void loadTexture(FileHandle tmxPath) {
		tileset.loadTexture(tmxPath);
	}
	
	@Override
	public void loadTexture(AssetManager assetManager, FileHandle tmxPath) {
		tileset.loadTexture(assetManager, tmxPath);
	}

	@Override
	public void loadTexture(TextureAtlas textureAtlas) {
		tileset.loadTexture(textureAtlas);
	}

	@Override
	public boolean isTextureLoaded() {
		return tileset.isTextureLoaded();
	}
	
	@Override
	public Sprite getTileImage(int tileId) {
		return tileset.getTileImage(tileId);
	}

	@Override
	public int getWidthInTiles() {
		return tileset.getWidthInTiles();
	}

	@Override
	public int getHeightInTiles() {
		return tileset.getHeightInTiles();
	}

	@Override
	public void drawTile(Graphics g, int tileId, int firstGid, int renderX, int renderY) {
		tileset.drawTile(g, tileId, firstGid, renderX, renderY);
	}

	@Override
	public void drawTileset(Graphics g, int renderX, int renderY) {
		tileset.drawTileset(g, renderX, renderY);
	}

	@Override
	public int getWidth() {
		return tileset.getWidth();
	}

	@Override
	public int getHeight() {
		return tileset.getHeight();
	}

	@Override
	public int getTileWidth() {
		return tileset.getTileWidth();
	}

	@Override
	public int getTileHeight() {
		return tileset.getTileHeight();
	}

	@Override
	public int getSpacing() {
		return tileset.getSpacing();
	}

	@Override
	public int getMargin() {
		return tileset.getMargin();
	}
	
	@Override
	public Tile getTileByPosition(int x, int y) {
		return tileset.getTileByPosition(x, y);
	}

	@Override
	public Tile getTile(int tileId, int firstGid) {
		return tileset.getTile(tileId, firstGid);
	}

	@Override
	public boolean containsProperty(String propertyName) {
		return tileset.containsProperty(propertyName);
	}

	@Override
	public String getProperty(String propertyName) {
		return tileset.getProperty(propertyName);
	}

	@Override
	public void setProperty(String propertyName, String value) {
		tileset.setProperty(propertyName, value);
	}

	@Override
	public ObjectMap<String, String> getProperties() {
		return tileset.getProperties();
	}

	@Override
	public String getInternalUuid() {
		return getTsxPath();
	}

	public String getTsxPath() {
		return tsxPath;
	}

	@Override
	public void dispose() {
		int remainingRefs = TILESET_REFS.get(tsxPath).decrementAndGet();
		if (remainingRefs > 0) {
			return;
		}
		ImageTilesetSource tilesetSource = TILESETS.remove(tsxPath);
		if (tilesetSource == null) {
			return;
		}
		tilesetSource.dispose();
	}
}
