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
package org.mini2Dx.tiled.renderer;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.tiled.TileLayer;
import org.mini2Dx.tiled.TiledMap;
import org.mini2Dx.tiled.Tileset;

/**
 * Renders hexagonal {@link TileLayer}s
 */
public class HexagonalTileLayerRenderer implements TileLayerRenderer {
	private final TiledMap tiledMap;

	private final int hexWidth, hexHeight;
	private final int sideOffsetX, sideOffsetY;
	private final int quarterHexWidth, quarterHexHeight, halfHexWidth, halfHexHeight, threeQuarterHexWidth,
			threeQuarterHexHeight;

	public HexagonalTileLayerRenderer(TiledMap tiledMap) {
		super();
		this.tiledMap = tiledMap;

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
						  int widthInTiles, int heightInTiles) {
		// TODO: Support caching
		switch (tiledMap.getStaggerAxis()) {
		case X:
			drawStaggeredXLayer(g, layer, renderX, renderY, startTileX, startTileY, widthInTiles, heightInTiles);
			break;
		case Y:
		default:
			drawStaggeredYLayer(g, layer, renderX, renderY, startTileX, startTileY, widthInTiles, heightInTiles);
			break;
		}
	}

	private void drawStaggeredXLayer(Graphics g, TileLayer layer, int renderX, int renderY, int startTileX,
			int startTileY, int widthInTiles, int heightInTiles) {
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
					renderTile(g, tileId, tileRenderX, tileRenderY);
				}
				for (int x = row2Offset; x < widthInTiles; x += 2) {
					int tileId = layer.getTileId(x + startTileX, y + startTileY);

					if (tileId < 1) {
						continue;
					}
					int tileRenderX = renderX + (x * threeQuarterHexWidth);
					int tileRenderY = renderY + (y * hexHeight) + halfHexHeight;
					renderTile(g, tileId, tileRenderX, tileRenderY);
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
					renderTile(g, tileId, tileRenderX, tileRenderY);
				}
				for (int x = row2Offset; x < widthInTiles; x += 2) {
					int tileId = layer.getTileId(x + startTileX, y + startTileY);

					if (tileId < 1) {
						continue;
					}
					int tileRenderX = renderX + (x * threeQuarterHexWidth);
					int tileRenderY = renderY + (y * hexHeight) + halfHexHeight;
					renderTile(g, tileId, tileRenderX, tileRenderY);
				}
				break;
			}
			}
		}
	}

	private void drawStaggeredYLayer(Graphics g, TileLayer layer, int renderX, int renderY, int startTileX,
			int startTileY, int widthInTiles, int heightInTiles) {
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

				renderTile(g, tileId, tileRenderX, tileRenderY);
			}
		}
	}

	private void renderTile(Graphics g, int tileId, int tileRenderX, int tileRenderY) {
		for (int i = 0; i < tiledMap.getTilesets().size; i++) {
			Tileset tileset = tiledMap.getTilesets().get(i);
			if (tileset.contains(tileId)) {
				tileset.getTile(tileId).draw(g, tileRenderX, tileRenderY);
				break;
			}
		}
	}

	@Override
	public void dispose() {

	}

}
