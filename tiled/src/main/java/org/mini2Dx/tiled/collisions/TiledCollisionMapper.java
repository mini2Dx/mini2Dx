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

import java.util.List;

import org.mini2Dx.core.collisions.QuadTree;
import org.mini2Dx.core.engine.Positionable;
import org.mini2Dx.tiled.Tile;
import org.mini2Dx.tiled.TileLayer;
import org.mini2Dx.tiled.TiledMap;
import org.mini2Dx.tiled.TiledObject;
import org.mini2Dx.tiled.TiledObjectGroup;

/**
 * Utility class for converting {@link TiledMap} data into collision data.
 */
public class TiledCollisionMapper<T extends Positionable> {
	private final TiledCollisionFactory<T> collisionFactory;
	private final TiledCollisionMerger collisionMerger;

	/**
	 * Creates a {@link TiledCollisionMapper} with a
	 * {@link DefaultTiledCollisionMerger} instance for merge operations
	 * 
	 * @param collisionFactory
	 *            An implementation of {@link TiledCollisionFactory} for
	 *            creating collision instances
	 */
	public TiledCollisionMapper(TiledCollisionFactory<T> collisionFactory) {
		this(collisionFactory, new DefaultTiledCollisionMerger());
	}

	/**
	 * Creates a {@link TiledCollisionMapper} with a specifc
	 * {@link TiledCollisionMerger} implementation for merge operations
	 * 
	 * @param collisionFactory
	 *            An implementation of {@link TiledCollisionFactory} for
	 *            creating collision instances
	 * @param collisionMerger
	 *            An implementation of {@link TiledCollisionMerger} for
	 *            determining if two tiles can be merged into a single collision
	 */
	public TiledCollisionMapper(TiledCollisionFactory<T> collisionFactory, TiledCollisionMerger collisionMerger) {
		this.collisionFactory = collisionFactory;
		this.collisionMerger = collisionMerger;
	}

	/**
	 * Creates a 2D byte array representing the collisions in a {@link TiledMap}
	 * layer
	 * 
	 * @param tiledMap
	 *            The {@link TiledMap} to extract collisions from
	 * @param layerName
	 *            The name of the layer to extract collisions from. Each tile
	 *            drawn in the layer is treated as a collision.
	 * @return A 2D byte array with the same width and height as the layer.
	 *         [x][y] will be set to 1 if there is a collision.
	 */
	public static byte[][] mapCollisionsByLayer(TiledMap tiledMap, String layerName) {
		return mapCollisionsByLayer(tiledMap, tiledMap.getLayerIndex(layerName));
	}

	/**
	 * Creates a 2D byte array representing the collisions in a {@link TiledMap}
	 * layer
	 * 
	 * @param tiledMap
	 *            The {@link TiledMap} to extract collisions from
	 * @param layerIndex
	 *            The index of the layer to extract collisions from. Each tile
	 *            drawn in the layer is treated as a collision.
	 * @return A 2D byte array with the same width and height as the layer.
	 *         [x][y] will be set to 1 if there is a collision.
	 */
	public static byte[][] mapCollisionsByLayer(TiledMap tiledMap, int layerIndex) {
		return mapCollisionsByLayer(tiledMap, tiledMap.getTileLayer(layerIndex));
	}

	private static byte[][] mapCollisionsByLayer(TiledMap tiledMap, TileLayer layer) {
		byte[][] result = new byte[layer.getWidth()][layer.getHeight()];
		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				if (layer.getTileId(x, y) > 0) {
					result[x][y] = 1;
				}
			}
		}
		return result;
	}

	/**
	 * Extracts collisions in a {@link TiledMap} layer and adds them to a
	 * {@link QuadTree} instance
	 * 
	 * @param quadTree
	 *            The {@link QuadTree} instance to add collisions to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract collisions from
	 * @param layerIndex
	 *            The index of the layer to extract collisions from. Each tile
	 *            drawn in the layer is treated as a collision.
	 */
	public void mapCollisionsByLayer(QuadTree<T> quadTree, TiledMap tiledMap, int layerIndex) {
		TileLayer layer = tiledMap.getTileLayer(layerIndex);
		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				if (layer.getTileId(x, y) > 0) {
					quadTree.add(collisionFactory.createCollision(tiledMap.getTile(layer.getTileId(x, y)),
							x * tiledMap.getTileWidth(), y * tiledMap.getTileHeight(), tiledMap.getTileWidth(),
							tiledMap.getTileHeight()));
				}
			}
		}
	}
	
	/**
	 * Extracts collisions in a {@link TiledMap} layer and adds them to a
	 * {@link List} instance
	 * 
	 * @param results
	 *            The {@link List} instance to add collisions to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract collisions from
	 * @param layerIndex
	 *            The index of the layer to extract collisions from. Each tile
	 *            drawn in the layer is treated as a collision.
	 */
	public void mapCollisionsByLayer(List<T> results, TiledMap tiledMap, int layerIndex) {
		TileLayer layer = tiledMap.getTileLayer(layerIndex);
		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				if (layer.getTileId(x, y) > 0) {
					results.add(collisionFactory.createCollision(tiledMap.getTile(layer.getTileId(x, y)),
							x * tiledMap.getTileWidth(), y * tiledMap.getTileHeight(), tiledMap.getTileWidth(),
							tiledMap.getTileHeight()));
				}
			}
		}
	}

	/**
	 * Extracts collisions in a {@link TiledMap} layer and adds them to a
	 * {@link QuadTree} instance
	 * 
	 * @param quadTree
	 *            The {@link QuadTree} instance to add collisions to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract collisions from
	 * @param layerName
	 *            The name of the layer to extract collisions from. Each tile
	 *            drawn in the layer is treated as a collision.
	 */
	public void mapCollisionsByLayer(QuadTree<T> quadTree, TiledMap tiledMap, String layerName) {
		mapCollisionsByLayer(quadTree, tiledMap, tiledMap.getLayerIndex(layerName));
	}
	
	/**
	 * Extracts collisions in a {@link TiledMap} layer and adds them to a
	 * {@link List} instance
	 * 
	 * @param results
	 *            The {@link List} instance to add collisions to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract collisions from
	 * @param layerName
	 *            The name of the layer to extract collisions from. Each tile
	 *            drawn in the layer is treated as a collision.
	 */
	public void mapCollisionsByLayer(List<T> results, TiledMap tiledMap, String layerName) {
		mapCollisionsByLayer(results, tiledMap, tiledMap.getLayerIndex(layerName));
	}

	/**
	 * Extracts collisions in a {@link TiledMap} object group and adds them to a
	 * {@link QuadTree} instance
	 * 
	 * @param quadTree
	 *            The {@link QuadTree} instance to add collisions to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract collisions from
	 * @param groupName
	 *            The name of the object group to extract collisions from. Each
	 *            object is treated as a collision.
	 */
	public void mapCollisionsByObjectGroup(QuadTree<T> quadTree, TiledMap tiledMap, String groupName) {
		TiledObjectGroup objectGroup = tiledMap.getObjectGroup(groupName);

		for (TiledObject tiledObject : objectGroup.getObjects()) {
			quadTree.add(collisionFactory.createCollision(tiledObject));
		}
	}
	
	/**
	 * Extracts collisions in a {@link TiledMap} object group and adds them to a
	 * {@link List} instance
	 * 
	 * @param results
	 *            The {@link List} instance to add collisions to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract collisions from
	 * @param groupName
	 *            The name of the object group to extract collisions from. Each
	 *            object is treated as a collision.
	 */
	public void mapCollisionsByObjectGroup(List<T> results, TiledMap tiledMap, String groupName) {
		TiledObjectGroup objectGroup = tiledMap.getObjectGroup(groupName);

		for (TiledObject tiledObject : objectGroup.getObjects()) {
			results.add(collisionFactory.createCollision(tiledObject));
		}
	}

	/**
	 * Extracts and merges collisions in a {@link TiledMap} layer and adds them
	 * to a {@link QuadTree} instance. Tiles are determined as mergeable by the
	 * {@link TiledCollisionMerger} instance associated with this {@link TiledCollisionMapper}.
	 * 
	 * @param quadTree
	 *            The {@link QuadTree} instance to add collisions to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract collisions from
	 * @param layerName
	 *            The name of the layer to extract collisions from. Each tile
	 *            drawn in the layer is treated as a collision.
	 */
	public void mapAndMergeCollisionsByLayer(QuadTree<T> quadTree, TiledMap tiledMap, String layerName) {
		mapAndMergeCollisionsByLayer(quadTree, tiledMap, tiledMap.getLayerIndex(layerName));
	}

	/**
	 * Extracts and merges collisions in a {@link TiledMap} layer and adds them
	 * to a {@link QuadTree} instance. Tiles are determined as mergeable by the
	 * {@link TiledCollisionMerger} instance associated with this {@link TiledCollisionMapper}.
	 * 
	 * @param quadTree
	 *            The {@link QuadTree} instance to add collisions to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract collisions from
	 * @param layerIndex
	 *            The index of the layer to extract collisions from. Each tile
	 *            drawn in the layer is treated as a collision.
	 */
	public void mapAndMergeCollisionsByLayer(QuadTree<T> quadTree, TiledMap tiledMap, int layerIndex) {
		TileLayer layer = tiledMap.getTileLayer(layerIndex);
		byte[][] collisions = mapCollisionsByLayer(tiledMap, layer);

		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				if (collisions[x][y] == 0) {
					continue;
				}
				quadTree.add(mergeCollisions(x, y, collisions, layer, tiledMap));
			}
		}
	}
	
	/**
	 * Extracts and merges collisions in a {@link TiledMap} layer and adds them
	 * to a {@link List} instance. Tiles are determined as mergeable by the
	 * {@link TiledCollisionMerger} instance associated with this {@link TiledCollisionMapper}.
	 * 
	 * @param results
	 *            The {@link List} instance to add collisions to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract collisions from
	 * @param layerName
	 *            The name of the layer to extract collisions from. Each tile
	 *            drawn in the layer is treated as a collision.
	 */
	public void mapAndMergeCollisionsByLayer(List<T> results, TiledMap tiledMap, String layerName) {
		mapAndMergeCollisionsByLayer(results, tiledMap, tiledMap.getLayerIndex(layerName));
	}
	
	/**
	 * Extracts and merges collisions in a {@link TiledMap} layer and adds them
	 * to a {@link List} instance. Tiles are determined as mergeable by the
	 * {@link TiledCollisionMerger} instance associated with this {@link TiledCollisionMapper}.
	 * 
	 * @param results
	 *            The {@link List} instance to add collisions to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract collisions from
	 * @param layerIndex
	 *            The index of the layer to extract collisions from. Each tile
	 *            drawn in the layer is treated as a collision.
	 */
	public void mapAndMergeCollisionsByLayer(List<T> results, TiledMap tiledMap, int layerIndex) {
		TileLayer layer = tiledMap.getTileLayer(layerIndex);
		byte[][] collisions = mapCollisionsByLayer(tiledMap, layer);

		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				if (collisions[x][y] == 0) {
					continue;
				}
				results.add(mergeCollisions(x, y, collisions, layer, tiledMap));
			}
		}
	}

	private T mergeCollisions(int startX, int startY, byte[][] collisions, TileLayer layer, TiledMap tiledMap) {
		Tile startTile = tiledMap.getTile(layer.getTileId(startX, startY));
		Tile nextTile = null;

		int maxXTiles = 0;
		int maxYTiles = 0;

		for (int y = 1; y < layer.getHeight() - startY; y++) {
			if (collisions[startX][startY + y] == 0) {
				break;
			}

			nextTile = tiledMap.getTile(layer.getTileId(startX, startY + y));
			if (!collisionMerger.isMergable(startTile, nextTile)) {
				break;
			}
			maxYTiles = y;
		}

		for (int x = 1; x < layer.getWidth() - startX; x++) {
			boolean mergeable = true;
			for (int y = 0; y <= maxYTiles; y++) {
				if (collisions[startX + x][startY + y] == 0) {
					mergeable = false;
					break;
				}

				nextTile = tiledMap.getTile(layer.getTileId(startX + x, startY + y));
				if (!collisionMerger.isMergable(startTile, nextTile)) {
					mergeable = false;
					break;
				}
			}
			if (!mergeable) {
				break;
			}
			maxXTiles = x;
		}

		// Clear the collision data for merged tiles
		for (int x = 0; x <= maxXTiles; x++) {
			for (int y = 0; y <= maxYTiles; y++) {
				collisions[startX + x][startY + y] = 0;
			}
		}

		return collisionFactory.createCollision(startTile, startX * tiledMap.getTileWidth(),
				startY * tiledMap.getTileHeight(), tiledMap.getTileWidth() + (maxXTiles * tiledMap.getTileWidth()),
				tiledMap.getTileHeight() + (maxYTiles * tiledMap.getTileHeight()));
	}
}
