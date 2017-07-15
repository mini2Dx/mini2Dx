/**
 * Copyright 2017 Thomas Cashman
 */
package org.mini2Dx.tiled.tileset;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.tiled.Tile;
import org.mini2Dx.tiled.TiledParser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 *
 */
public class TsxTilesetSource extends TilesetSource {
	private static final String LOGGING_TAG = TsxTilesetSource.class.getSimpleName();
	
	private static final TiledParser TSX_PARSER = new TiledParser();
	private static final Map<String, ImageTilesetSource> TILESETS = new ConcurrentHashMap<String, ImageTilesetSource>();
	private static final Map<String, AtomicInteger> TILESET_REFS = new HashMap<String, AtomicInteger>();
	
	private final String tsxPath;
	private final ImageTilesetSource tileset;

	public TsxTilesetSource(FileHandle tmxDirectory, String tsxPath) {
		super();
		this.tsxPath = tsxPath;
		
		if(!TILESETS.containsKey(tsxPath)) {
			try {
				TILESETS.put(tsxPath, TSX_PARSER.parseTsx(tmxDirectory.child(tsxPath)));
			} catch (IOException e) {
				Gdx.app.error(LOGGING_TAG, "Could not parse " + tsxPath + ". " + e.getMessage(), e);
				TILESETS.put(tsxPath, null);
			}
			TILESET_REFS.put(tsxPath, new AtomicInteger(0));
		}
		tileset = TILESETS.get(tsxPath);
		TILESET_REFS.get(tsxPath).incrementAndGet();
	}

	@Override
	public void loadTexture(FileHandle tmxDirectory) {
		tileset.loadTexture(tmxDirectory);
	}

	@Override
	public boolean isTextureLoaded() {
		return tileset.isTextureLoaded();
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
	public Map<String, String> getProperties() {
		return tileset.getProperties();
	}

	public String getTsxPath() {
		return tsxPath;
	}

	@Override
	public void dispose() {
		int remainingRefs = TILESET_REFS.get(tsxPath).decrementAndGet();
		if(remainingRefs > 0) {
			return;
		}
		ImageTilesetSource tilesetSource = TILESETS.remove(tsxPath);
		if(tilesetSource == null) {
			return;
		}
		tilesetSource.dispose();
	}

}
