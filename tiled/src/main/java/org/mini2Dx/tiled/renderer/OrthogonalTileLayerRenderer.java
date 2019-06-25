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

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.utils.IntMap;
import org.mini2Dx.tiled.*;

/**
 * Renders orthogonal {@link TileLayer}s
 */
public class OrthogonalTileLayerRenderer implements TileLayerRenderer {
	private IntMap<OrthogonalEmptyTileLayerRenderer> emptyTileLayerRenderers;

	private final TiledMap tiledMap;
	private final Rectangle graphicsClip = new Rectangle();

	public OrthogonalTileLayerRenderer(TiledMap tiledMap) {
		super();
		this.tiledMap = tiledMap;

		if(TiledMap.FAST_RENDER_EMPTY_LAYERS) {
			emptyTileLayerRenderers = new IntMap<OrthogonalEmptyTileLayerRenderer>();
			for(Layer layer : tiledMap.getLayers()) {
				if(!layer.getLayerType().equals(LayerType.TILE)) {
					continue;
				}
				final TileLayer tileLayer = tiledMap.getTileLayer(layer.getIndex());
				if(!tileLayer.isMostlyEmptyTiles()) {
					continue;
				}
				emptyTileLayerRenderers.put(tileLayer.getIndex(), new OrthogonalEmptyTileLayerRenderer(tiledMap, tileLayer));
			}
		}
	}

	@Override
	public void drawLayer(Graphics g, TileLayer layer, int renderX, int renderY, int startTileX, int startTileY,
						  int widthInTiles, int heightInTiles) {
		if(TiledMap.FAST_RENDER_EMPTY_LAYERS && emptyTileLayerRenderers != null) {
			final OrthogonalEmptyTileLayerRenderer renderer = emptyTileLayerRenderers.get(layer.getIndex(), null);
			if(renderer != null) {
				renderer.drawLayer(g, layer, renderX, renderY, startTileX, startTileY, widthInTiles, heightInTiles);
				return;
			}
		}
		renderWithoutClipAndTranslate(g, layer, renderX, renderY, startTileX, startTileY, widthInTiles, heightInTiles);
	}

	private void renderWithoutClipAndTranslate(Graphics g, TileLayer layer, int renderX, int renderY, int startTileX, int startTileY,
	                                        int widthInTiles, int heightInTiles) {
		int startTileRenderX = (startTileX * tiledMap.getTileWidth());
		int startTileRenderY = (startTileY * tiledMap.getTileHeight());

		int tileRenderX = MathUtils.round(renderX - startTileRenderX);
		int tileRenderY = MathUtils.round(renderY - startTileRenderY);

		renderLayer(g, layer, tileRenderX, tileRenderY, startTileX, startTileY, widthInTiles, heightInTiles);
	}

	private void renderLayer(Graphics g, TileLayer layer, int renderX, int renderY, int startTileX, int startTileY,
			int widthInTiles, int heightInTiles) {
		for (int y = startTileY; y < startTileY + heightInTiles && y < layer.getHeight(); y++) {
			for (int x = startTileX; x < startTileX + widthInTiles && x < layer.getWidth(); x++) {
				int tileId = layer.getTileId(x, y);

				if (tileId < 1) {
					continue;
				}
				boolean flipHorizontally = layer.isFlippedHorizontally(x, y);
				boolean flipVertically = layer.isFlippedVertically(x, y);
				boolean flipDiagonally = layer.isFlippedDiagonally(x, y);

				int tileRenderX = renderX + (x * tiledMap.getTileWidth());
				int tileRenderY = renderY + (y * tiledMap.getTileHeight());

				if (tileRenderX + tiledMap.getTileWidth() < g.getTranslationX()) {
					continue;
				}
				if (tileRenderY + tiledMap.getTileHeight() < g.getTranslationY()) {
					continue;
				}
				if (tileRenderX > g.getTranslationX() + g.getViewportWidth()) {
					continue;
				}
				if (tileRenderY > g.getTranslationY() + g.getViewportHeight()) {
					continue;
				}

				for (int i = 0; i < tiledMap.getTilesets().size; i++) {
					Tileset tileset = tiledMap.getTilesets().get(i);
					if (tileset.contains(tileId)) {
						tileset.getTile(tileId).draw(g, tileRenderX, tileRenderY, flipHorizontally, flipVertically, flipDiagonally);
						break;
					}
				}
			}
		}
	}

	@Override
	public void dispose() {
		if (emptyTileLayerRenderers != null) {
			for(OrthogonalEmptyTileLayerRenderer renderer : emptyTileLayerRenderers.values()) {
				renderer.dispose();
			}
			emptyTileLayerRenderers.clear();
			emptyTileLayerRenderers = null;
		}
	}

}
