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
package org.mini2Dx.tiled.collisions;

import org.mini2Dx.core.geom.Positionable;
import org.mini2Dx.tiled.Tile;
import org.mini2Dx.tiled.TiledMap;
import org.mini2Dx.tiled.TiledObject;

/**
 * Creates objects to represent collision data
 */
public interface TiledCollisionFactory<T extends Positionable> {

	/**
	 * Create a new collision from a {@link Tile}
	 * 
	 * @param map
	 *            The source {@link TiledMap}
	 * @param tile
	 *            The source {@link Tile}
	 * @param x
	 *            The x coordinate of the {@link Tile}
	 * @param y
	 *            The y coordinate of the {@link Tile}
	 * @param width
	 *            The width of the {@link Tile} (Note: if wider than
	 *            {@link Tile} this means there are multiple of the same
	 *            {@link Tile} - this will only happen when using {@link TiledCollisionMerger})
	 * @param height
	 *            The height of the {@link Tile} (Note: if higher than
	 *            {@link Tile} this means there are multiple of the same
	 *            {@link Tile} - this will only happen when using {@link TiledCollisionMerger})
	 * @return Null if there is no collision
	 */
	public T createCollision(TiledMap map, Tile tile, float x, float y, float width, float height);

	/**
	 * Create a new collision from a {@link TiledObject}
	 * 
	 * @param map
	 *            The source {@link TiledMap}
	 * @param tiledObject
	 *            The {@link TiledObject}
	 * @return Null if there is no collision
	 */
	public T createCollision(TiledMap map, TiledObject tiledObject);
}
