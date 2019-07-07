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
package org.mini2Dx.tiled.collisions.merger;

import org.mini2Dx.tiled.Tile;
import org.mini2Dx.tiled.TileLayer;
import org.mini2Dx.tiled.TiledMap;
import org.mini2Dx.tiled.collisions.TiledCollisionMapper;
import org.mini2Dx.tiled.collisions.TiledCollisionMerger;

/**
 * An implementation of {@link TiledCollisionMapper} that merges tiles if
 * they have the same tile ID
 */
public class TileIdCollisionMerger implements TiledCollisionMerger {

	@Override
	public boolean isMergable(TiledMap tiledMap, TileLayer layer, Tile tile1, int tile1X, int tile1Y, int tile2X, int tile2Y) {
		final Tile tile2 = tiledMap.getTile(layer.getTileId(tile2X, tile2Y));
		return tile1.getTileId() == tile2.getTileId();
	}

}
