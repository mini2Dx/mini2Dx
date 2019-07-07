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
import org.mini2Dx.tiled.TileLayer;

/**
 * A common interface for {@link TileLayer} rendering implementations
 */
public interface TileLayerRenderer {
	/**
	 * Renders a {@link TileLayer}
	 * @param g The {@link Graphics} context
	 * @param layer The {@link TileLayer} to render
	 * @param renderX The screen x coordinate to render at
	 * @param renderY The screen y coordinate to render at
	 * @param startTileX The tile x coordinate to start at
	 * @param startTileY The tile y coordinate to start at
	 * @param widthInTiles The width to render in tiles
	 * @param heightInTiles The height to render in tiles
	 */
	public void drawLayer(Graphics g, TileLayer layer, int renderX,
						  int renderY, int startTileX, int startTileY, int widthInTiles, int heightInTiles);
	
	public void dispose();
}
