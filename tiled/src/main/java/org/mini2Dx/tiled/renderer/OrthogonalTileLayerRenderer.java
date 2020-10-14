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
						  int widthInTiles, int heightInTiles, float alpha) {
		if(TiledMap.FAST_RENDER_EMPTY_LAYERS && emptyTileLayerRenderers != null) {
			final OrthogonalEmptyTileLayerRenderer renderer = emptyTileLayerRenderers.get(layer.getIndex(), null);
			if(renderer != null) {
				renderer.drawLayer(g, layer, renderX, renderY, startTileX, startTileY, widthInTiles, heightInTiles, alpha);
				return;
			}
		}
		renderWithoutClipAndTranslate(g, layer, renderX, renderY, startTileX, startTileY, widthInTiles, heightInTiles, alpha);
	}

	private void renderWithoutClipAndTranslate(Graphics g, TileLayer layer, int renderX, int renderY, int startTileX, int startTileY,
	                                        int widthInTiles, int heightInTiles, float alpha) {
		int startTileRenderX = (startTileX * tiledMap.getTileWidth());
		int startTileRenderY = (startTileY * tiledMap.getTileHeight());

		int tileRenderX = MathUtils.round(renderX - startTileRenderX);
		int tileRenderY = MathUtils.round(renderY - startTileRenderY);

		renderLayer(g, layer, tileRenderX, tileRenderY, startTileX, startTileY, widthInTiles, heightInTiles, alpha);
	}

	private void renderLayer(Graphics g, TileLayer layer, int renderX, int renderY, int startTileX, int startTileY,
			int widthInTiles, int heightInTiles, float alpha) {
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

				if(TiledMap.CLIP_TILES_OUTSIDE_GRAPHICS_VIEWPORT) {
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
				}

				for (int i = 0; i < tiledMap.getTilesets().size; i++) {
					Tileset tileset = tiledMap.getTilesets().get(i);
					if (tileset.contains(tileId)) {
						tileset.getTile(tileId).draw(g, tileRenderX, tileRenderY, alpha, flipHorizontally, flipVertically, flipDiagonally);
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
