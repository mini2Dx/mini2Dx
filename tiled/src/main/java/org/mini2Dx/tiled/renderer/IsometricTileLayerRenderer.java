/**
 * Copyright (c) 2015 See AUTHORS file
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

import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.tiled.TileLayer;
import org.mini2Dx.tiled.TiledMap;
import org.mini2Dx.tiled.Tileset;

import com.badlogic.gdx.math.MathUtils;

/**
 * Renders isometric {@link TileLayer}s
 */
public class IsometricTileLayerRenderer implements TileLayerRenderer {
	private TiledMapRenderArea mapClip, tmpClip;
	
	private final boolean cacheLayers;
	private final TiledMap tiledMap;
	private final float halfTileWidth, halfTileHeight;

	public IsometricTileLayerRenderer(TiledMap tiledMap, boolean cacheLayers) {
		super();
		this.cacheLayers = cacheLayers;
		this.tiledMap = tiledMap;
		
		this.halfTileWidth = tiledMap.getTileWidth() / 2f;
		this.halfTileHeight = tiledMap.getTileHeight() / 2f;
	}

	@Override
	public void drawLayer(Graphics g, TileLayer layer, int renderX, int renderY, int startTileX, int startTileY,
			int widthInTiles, int heightInTiles) {
		
		//TODO: Support cached layers through SpriteCache
		renderLayer(g, layer, renderX, renderY, startTileX,
				startTileY, widthInTiles, heightInTiles);
	}
	
	private void renderLayer(Graphics g, TileLayer layer, int renderX,
			int renderY, int startTileX, int startTileY, int widthInTiles,
			int heightInTiles) {
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
				
				for (int i = 0; i < tiledMap.getTilesets().size(); i++) {
					Tileset tileset = tiledMap.getTilesets().get(i);
					if (tileset.contains(tileId)) {
						tileset.getTile(tileId).draw(g, tileRenderX, tileRenderY);
						break;
					}
				}
			}
		}
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

}
