/**
 * Copyright (c) 2019 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.tiled.renderer;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.IntMap;
import org.mini2Dx.gdx.utils.Queue;
import org.mini2Dx.tiled.Tile;
import org.mini2Dx.tiled.TileLayer;
import org.mini2Dx.tiled.TiledMap;
import org.mini2Dx.tiled.Tileset;

import java.util.BitSet;

/**
 * Optimises CPU time for rendering orthogonal {@link TileLayer}s
 * where the layer consists mostly of empty tiles
 */
public class OrthogonalEmptyTileLayerRenderer implements TileLayerRenderer {
	private final IntMap<Tileset> tileIdToTileset;
	private final TiledMap tiledMap;
	private final TileLayer layer;
	private final BitSet tiles;

	public OrthogonalEmptyTileLayerRenderer(TiledMap tiledMap, TileLayer layer, IntMap<Tileset> tileIdToTileset) {
		super();
		this.tileIdToTileset = tileIdToTileset;
		this.tiledMap = tiledMap;
		this.layer = layer;

		tiles = new BitSet(layer.getWidth() * layer.getHeight());

		for (int y = 0; y < layer.getHeight(); y++) {
			for (int x = 0; x < layer.getWidth(); x++) {
				int tileId = layer.getTileId(x, y);

				if (tileId < 1) {
					continue;
				}
				tiles.set((y * layer.getWidth()) + x);
			}
		}
	}

	@Override
	public void drawLayer(Graphics g, TileLayer layer, int renderX, int renderY,
						  int startTileX, int startTileY, int widthInTiles, int heightInTiles, float alpha) {
		int startTileRenderX = (startTileX * tiledMap.getTileWidth());
		int startTileRenderY = (startTileY * tiledMap.getTileHeight());

		renderX = MathUtils.round(renderX - startTileRenderX);
		renderY = MathUtils.round(renderY - startTileRenderY);

		for (int index = tiles.nextSetBit(0); index >= 0; index = tiles.nextSetBit(index + 1)) {
			int tileY = index / layer.getWidth();
			int tileX = index - (tileY * layer.getWidth());

			if(tileX < startTileX) {
				continue;
			}
			if(tileY < startTileY) {
				continue;
			}
			if(tileX >= startTileX + widthInTiles) {
				continue;
			}
			if(tileY >= startTileY + heightInTiles) {
				continue;
			}

			int tileId = layer.getTileId(tileX, tileY);

			if (tileId < 1) {
				continue;
			}

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
					return;
				}
			}

			Tile tile = tileset.getTile(tileId);

			boolean flipHorizontally = layer.isFlippedHorizontally(tileX, tileY);
			boolean flipVertically = layer.isFlippedVertically(tileX, tileY);
			boolean flipDiagonally = layer.isFlippedDiagonally(tileX, tileY);

			int tileRenderX = renderX + (tileX * tiledMap.getTileWidth());
			int tileRenderY = renderY + (tileY * tiledMap.getTileHeight());

			tile.draw(g, tileRenderX, tileRenderY, alpha, flipHorizontally, flipVertically, flipDiagonally);
		}
	}

	@Override
	public void dispose() {
		tiles.clear();
	}

	public TiledMap getTiledMap() {
		return tiledMap;
	}

	public TileLayer getLayer() {
		return layer;
	}

	private static class TileRenderRef {
		public int x, y;
		public int tileId;
		public Tile tile;
	}
}
