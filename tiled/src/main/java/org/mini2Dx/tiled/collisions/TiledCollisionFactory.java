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
package org.mini2Dx.tiled.collisions;

import org.mini2Dx.core.engine.Positionable;
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
