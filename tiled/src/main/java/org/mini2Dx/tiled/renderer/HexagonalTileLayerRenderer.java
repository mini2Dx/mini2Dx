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

/**
 * Renders hexagonal {@link TileLayer}s
 */
public class HexagonalTileLayerRenderer implements TileLayerRenderer {
	private final TiledMap tiledMap;
	private final IntMap<Tileset> tileIdToTileset;

	private final int hexWidth, hexHeight;
	private final int sideOffsetX, sideOffsetY;
	private final int quarterHexWidth, quarterHexHeight, halfHexWidth, halfHexHeight, threeQuarterHexWidth,
			threeQuarterHexHeight;

	public HexagonalTileLayerRenderer(TiledMap tiledMap, IntMap<Tileset> tileIdToTileset) {
		super();
		this.tiledMap = tiledMap;
		this.tileIdToTileset = tileIdToTileset;

		sideOffsetX = (tiledMap.getTileWidth() - (tiledMap.getSideLength() * 2)) / 2;
		sideOffsetY = (tiledMap.getTileHeight() - (tiledMap.getSideLength() * 2)) / 2;

		switch (tiledMap.getStaggerAxis()) {
		case X:
			hexWidth = (tiledMap.getSideLength() * 2) + sideOffsetX;
			hexHeight = tiledMap.getTileHeight();
			break;
		case Y:
		default:
			hexWidth = tiledMap.getTileWidth();
			hexHeight = (tiledMap.getSideLength() * 2) + sideOffsetY;
			break;
		}

		quarterHexWidth = MathUtils.round(hexWidth * 0.25f);
		quarterHexHeight = MathUtils.round(hexHeight * 0.25f);
		halfHexWidth = MathUtils.round(hexWidth * 0.5f);
		halfHexHeight = MathUtils.round(hexHeight * 0.5f);
		threeQuarterHexWidth = MathUtils.round(hexWidth * 0.75f);
		threeQuarterHexHeight = MathUtils.round(hexHeight * 0.75f);
	}

	@Override
	public void drawLayer(Graphics g, TileLayer layer, int renderX, int renderY, int startTileX, int startTileY,
						  int widthInTiles, int heightInTiles, float alpha) {
		// TODO: Support caching
		switch (tiledMap.getStaggerAxis()) {
		case X:
			drawStaggeredXLayer(g, layer, renderX, renderY, startTileX, startTileY, widthInTiles, heightInTiles, alpha);
			break;
		case Y:
		default:
			drawStaggeredYLayer(g, layer, renderX, renderY, startTileX, startTileY, widthInTiles, heightInTiles, alpha);
			break;
		}
	}

	private void drawStaggeredXLayer(Graphics g, TileLayer layer, int renderX, int renderY, int startTileX,
			int startTileY, int widthInTiles, int heightInTiles, float alpha) {
		for (int y = 0; y < heightInTiles; y++) {
			switch (tiledMap.getStaggerIndex()) {
			case EVEN: {
				int row1Offset = startTileX % 2 == 0 ? 1 : 0;
				int row2Offset = startTileX % 2 == 0 ? 0 : 1;
				for (int x = row1Offset; x < widthInTiles; x += 2) {
					int tileId = layer.getTileId(x + startTileX, y + startTileY);

					if (tileId < 1) {
						continue;
					}
					int tileRenderX = renderX + (x * threeQuarterHexWidth);
					int tileRenderY = renderY + (y * hexHeight);
					renderTile(g, tileId, tileRenderX, tileRenderY, alpha);
				}
				for (int x = row2Offset; x < widthInTiles; x += 2) {
					int tileId = layer.getTileId(x + startTileX, y + startTileY);

					if (tileId < 1) {
						continue;
					}
					int tileRenderX = renderX + (x * threeQuarterHexWidth);
					int tileRenderY = renderY + (y * hexHeight) + halfHexHeight;
					renderTile(g, tileId, tileRenderX, tileRenderY, alpha);
				}
				break;
			}
			case ODD:
			default: {
				int row1Offset = startTileX % 2 == 0 ? 0 : 1;
				int row2Offset = startTileX % 2 == 0 ? 1 : 0;
				for (int x = row1Offset; x <  + widthInTiles; x += 2) {
					int tileId = layer.getTileId(x + startTileX, y + startTileY);

					if (tileId < 1) {
						continue;
					}
					int tileRenderX = renderX + (x * threeQuarterHexWidth);
					int tileRenderY = renderY + (y * hexHeight);
					renderTile(g, tileId, tileRenderX, tileRenderY, alpha);
				}
				for (int x = row2Offset; x < widthInTiles; x += 2) {
					int tileId = layer.getTileId(x + startTileX, y + startTileY);

					if (tileId < 1) {
						continue;
					}
					int tileRenderX = renderX + (x * threeQuarterHexWidth);
					int tileRenderY = renderY + (y * hexHeight) + halfHexHeight;
					renderTile(g, tileId, tileRenderX, tileRenderY, alpha);
				}
				break;
			}
			}
		}
	}

	private void drawStaggeredYLayer(Graphics g, TileLayer layer, int renderX, int renderY, int startTileX,
			int startTileY, int widthInTiles, int heightInTiles, float alpha) {
		for (int y = 0; y < heightInTiles; y++) {
			int tileRenderY = renderY + (y * threeQuarterHexHeight);

			for (int x = 0; x < widthInTiles; x++) {
				int tileId = layer.getTileId(x + startTileX, y + startTileY);

				if (tileId < 1) {
					continue;
				}

				int tileRenderX = renderX + (x * hexWidth);

				switch (tiledMap.getStaggerIndex()) {
				case EVEN: {
					if (y % 2 == 0) {
						tileRenderX += halfHexWidth;
					}
					break;
				}
				default:
				case ODD: {
					if (y % 2 == 1) {
						tileRenderX += halfHexWidth;
					}
					break;
				}
				}

				renderTile(g, tileId, tileRenderX, tileRenderY, alpha);
			}
		}
	}

	private void renderTile(Graphics g, int tileId, int tileRenderX, int tileRenderY, float alpha) {
		Tileset tileset = tileIdToTileset.get(tileId, null);
		if(tileset == null) {
			for (int i = 0; i < tiledMap.getTilesets().size; i++) {
				Tileset searchTileset = tiledMap.getTilesets().get(i);
				if (searchTileset.contains(tileId)) {
					tileset = searchTileset;
					break;
				}
			}
			if(tileset == null) {
				return;
			}
		}
		tileset.getTile(tileId).draw(g, tileRenderX, tileRenderY, alpha);
	}

	@Override
	public void dispose() {

	}

}
