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
package org.mini2Dx.tiled.renderer;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.utils.IntMap;
import org.mini2Dx.tiled.TileLayer;
import org.mini2Dx.tiled.TiledMap;
import org.mini2Dx.tiled.Tileset;

import java.util.Objects;

/**
 * Renders isometric {@link TileLayer}s
 */
public class IsometricTileLayerRenderer implements TileLayerRenderer {
	private TiledMapRenderArea mapClip, tmpClip;

	private final TiledMap tiledMap;
	private final IntMap<Tileset> tileIdToTileset;
	private final float halfTileWidth, halfTileHeight;

	public IsometricTileLayerRenderer(TiledMap tiledMap, IntMap<Tileset> tileIdToTileset) {
		super();
		this.tiledMap = tiledMap;
		this.tileIdToTileset = tileIdToTileset;
		
		this.halfTileWidth = tiledMap.getTileWidth() * 0.5f;
		this.halfTileHeight = tiledMap.getTileHeight() * 0.5f;
	}

	@Override
	public void drawLayer(Graphics g, TileLayer layer, int renderX, int renderY, int startTileX, int startTileY,
						  int widthInTiles, int heightInTiles, float alpha) {
		renderLayer(g, layer, renderX, renderY, startTileX,
				startTileY, widthInTiles, heightInTiles, alpha);
	}
	
	private void renderLayer(Graphics g, TileLayer layer, int renderX,
			int renderY, int startTileX, int startTileY, int widthInTiles,
			int heightInTiles, float alpha) {
		int totalRows = widthInTiles + heightInTiles;
		
		for(int row = 0; row < totalRows - 1; row++) {
			int startX = getStartX(row, heightInTiles);
			int endY = getEndY(row, widthInTiles);
			int totalCols = getTotalCols(row, widthInTiles, heightInTiles);
			
			for(int col = 0; col < totalCols; col++) {
				int relativeTileX = col + startX;
				int relativeTileY = totalCols - col - 1 + endY;
				
				int tileId = layer.getTileId(startTileX + relativeTileX, 
						startTileY + relativeTileY);
				if (tileId < 1) {
					continue;
				}
				
				int tileRenderX = MathUtils.round(renderX + ((relativeTileX - relativeTileY) * halfTileWidth));
				int tileRenderY = MathUtils.round(renderY + ((relativeTileX + relativeTileY) * halfTileHeight));
				renderTile(g, alpha, tileId, tileRenderX, tileRenderY);
			}
		}
	}

	private boolean renderTile(Graphics g, float alpha, int tileId, int tileRenderX, int tileRenderY) {
		Tileset tileset = tileIdToTileset.get(tileId, null);
		if(tileset == null) {
			for (int i = 0; i < tiledMap.getTilesets().size; i++) {
				Tileset searchTileset = tiledMap.getTilesets().get(i);
				if (searchTileset.contains(tileId)) {
					tileset = searchTileset;
					tileIdToTileset.put(tileId, tileset);
					break;
				}
			}
			if(tileset == null) {
				return true;
			}
		}
		tileset.getTile(tileId).draw(g, tileRenderX, tileRenderY, alpha);
		return false;
	}

	private int getTotalCols(int row, int width, int height) {
		if(row < width && row < height) {
			return row + 1;
		}
		if(row >= height) {
			return width + height - row - 1;
		}
		return Math.min(width, height);
	}
	
	private int getStartX(int row, int height) {
		if(row < height) {
			return 0;
		}
		return row - height + 1;
	}
	
	private int getEndY(int row, int width) {
		if(row < width) {
			return 0;
		}
		return row - width + 1;
	}
	
	@Override
	public void dispose() {
		
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		IsometricTileLayerRenderer that = (IsometricTileLayerRenderer) o;
		return Float.compare(that.halfTileWidth, halfTileWidth) == 0 && Float.compare(that.halfTileHeight, halfTileHeight) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(halfTileWidth, halfTileHeight);
	}
}
