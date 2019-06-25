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

import com.badlogic.gdx.utils.Array;
import org.mini2Dx.core.collisions.QuadTree;
import org.mini2Dx.core.engine.Positionable;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.tiled.TileLayer;
import org.mini2Dx.tiled.TiledMap;
import org.mini2Dx.tiled.TiledObject;
import org.mini2Dx.tiled.TiledObjectGroup;
import org.mini2Dx.tiled.collisions.merger.TileIdCollisionMerger;

/**
 * Utility class for converting {@link TiledMap} data into collision data.
 */
public class TiledCollisionMapper<T extends Positionable> {
	private final TileMergeMode mergeMode;
	private final TiledCollisionFactory<T> collisionFactory;
	private final TiledCollisionMerger collisionMerger;

	/**
	 * Creates a {@link TiledCollisionMapper} with a {@link TileIdCollisionMerger}
	 * instance for merge operations
	 * 
	 * @param collisionFactory
	 *            An implementation of {@link TiledCollisionFactory} for creating
	 *            collision instances
	 */
	public TiledCollisionMapper(TiledCollisionFactory<T> collisionFactory) {
		this(collisionFactory, new TileIdCollisionMerger());
	}

	/**
	 * Creates a {@link TiledCollisionMapper} with a specifc
	 * {@link TiledCollisionMerger} implementation for merge operations
	 * 
	 * @param collisionFactory
	 *            An implementation of {@link TiledCollisionFactory} for creating
	 *            collision instances
	 * @param collisionMerger
	 *            An implementation of {@link TiledCollisionMerger} for determining
	 *            if two tiles can be merged into a single collision
	 */
	public TiledCollisionMapper(TiledCollisionFactory<T> collisionFactory, TiledCollisionMerger collisionMerger) {
		this(collisionFactory, collisionMerger, TileMergeMode.SQUARE);
	}

	/**
	 * Creates a {@link TiledCollisionMapper} with a specifc
	 * {@link TiledCollisionMerger} implementation for merge operations
	 * 
	 * @param collisionFactory
	 *            An implementation of {@link TiledCollisionFactory} for creating
	 *            collision instances
	 * @param collisionMerger
	 *            An implementation of {@link TiledCollisionMerger} for determining
	 *            if two tiles can be merged into a single collision
	 * @param mergeMode
	 *            The {@link TileMergeMode} to use for searching mergable tiles
	 */
	public TiledCollisionMapper(TiledCollisionFactory<T> collisionFactory, TiledCollisionMerger collisionMerger,
			TileMergeMode mergeMode) {
		super();
		this.collisionFactory = collisionFactory;
		this.collisionMerger = collisionMerger;
		this.mergeMode = mergeMode;
	}

	/**
	 * Creates a 2D byte array representing the collisions in a {@link TiledMap}
	 * layer
	 * 
	 * @param tiledMap
	 *            The {@link TiledMap} to extract collisions from
	 * @param layerName
	 *            The name of the layer to extract collisions from. Each tile drawn
	 *            in the layer is treated as a collision.
	 * @return A 2D byte array with the same width and height as the layer. [x][y]
	 *         will be set to 1 if there is a collision.
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
	 *            The index of the layer to extract collisions from. Each tile drawn
	 *            in the layer is treated as a collision.
	 * @return A 2D byte array with the same width and height as the layer. [x][y]
	 *         will be set to 1 if there is a collision.
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
	 * Creates a 2D byte array representing the empty spaces (non-collisions) in a
	 * {@link TiledMap} layer
	 * 
	 * @param tiledMap
	 *            The {@link TiledMap} to extract empty spaces from
	 * @param layerName
	 *            The name of the layer to extract empty spaces from. Each tile
	 *            drawn in the layer is treated as a collision.
	 * @return A 2D byte array with the same width and height as the layer. [x][y]
	 *         will be set to 1 if there is no collision.
	 */
	public static byte[][] mapEmptySpacesByLayer(TiledMap tiledMap, String layerName) {
		return mapEmptySpacesByLayer(tiledMap, tiledMap.getLayerIndex(layerName));
	}

	/**
	 * Creates a 2D byte array representing the empty spaces (non-collisions) in a
	 * {@link TiledMap} layer
	 * 
	 * @param tiledMap
	 *            The {@link TiledMap} to extract empty spaces from
	 * @param layerIndex
	 *            The index of the layer to extract empty spaces from. Each tile
	 *            drawn in the layer is treated as a collision.
	 * @return A 2D byte array with the same width and height as the layer. [x][y]
	 *         will be set to 1 if there is no collision.
	 */
	public static byte[][] mapEmptySpacesByLayer(TiledMap tiledMap, int layerIndex) {
		return mapEmptySpacesByLayer(tiledMap, tiledMap.getTileLayer(layerIndex));
	}

	private static byte[][] mapEmptySpacesByLayer(TiledMap tiledMap, TileLayer layer) {
		byte[][] result = new byte[layer.getWidth()][layer.getHeight()];
		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				if (layer.getTileId(x, y) == 0) {
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
	 *            The index of the layer to extract collisions from. Each tile drawn
	 *            in the layer is treated as a collision.
	 */
	public void mapCollisionsByLayer(QuadTree<T> quadTree, TiledMap tiledMap, int layerIndex) {
		if (layerIndex < 0) {
			return;
		}
		TileLayer layer = tiledMap.getTileLayer(layerIndex);
		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				if (layer.getTileId(x, y) > 0) {
					T collision = collisionFactory.createCollision(tiledMap, tiledMap.getTile(layer.getTileId(x, y)),
							x * tiledMap.getTileWidth(), y * tiledMap.getTileHeight(), tiledMap.getTileWidth(),
							tiledMap.getTileHeight());
					if (collision == null) {
						continue;
					}
					quadTree.add(collision);
				}
			}
		}
	}

	/**
	 * Extracts empty spaces in a {@link TiledMap} layer and adds them to a
	 * {@link QuadTree} instance
	 * 
	 * @param quadTree
	 *            The {@link QuadTree} instance to add empty spaces to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract empty spaces from
	 * @param layerIndex
	 *            The index of the layer to extract empty spaces from. Each tile
	 *            drawn in the layer is treated as a collision.
	 */
	public void mapEmptySpacesByLayer(QuadTree<T> quadTree, TiledMap tiledMap, int layerIndex) {
		if (layerIndex < 0) {
			return;
		}
		TileLayer layer = tiledMap.getTileLayer(layerIndex);
		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				if (layer.getTileId(x, y) == 0) {
					T collision = collisionFactory.createCollision(tiledMap, tiledMap.getTile(layer.getTileId(x, y)),
							x * tiledMap.getTileWidth(), y * tiledMap.getTileHeight(), tiledMap.getTileWidth(),
							tiledMap.getTileHeight());
					if (collision == null) {
						continue;
					}
					quadTree.add(collision);
				}
			}
		}
	}

	/**
	 * Extracts collisions in a {@link TiledMap} layer and adds them to a
	 * {@link Array} instance
	 * 
	 * @param results
	 *            The {@link Array} instance to add collisions to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract collisions from
	 * @param layerIndex
	 *            The index of the layer to extract collisions from. Each tile drawn
	 *            in the layer is treated as a collision.
	 */
	public void mapCollisionsByLayer(Array<T> results, TiledMap tiledMap, int layerIndex) {
		if (layerIndex < 0) {
			return;
		}
		TileLayer layer = tiledMap.getTileLayer(layerIndex);
		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				if (layer.getTileId(x, y) > 0) {
					T collision = collisionFactory.createCollision(tiledMap, tiledMap.getTile(layer.getTileId(x, y)),
							x * tiledMap.getTileWidth(), y * tiledMap.getTileHeight(), tiledMap.getTileWidth(),
							tiledMap.getTileHeight());
					if (collision == null) {
						continue;
					}
					results.add(collision);
				}
			}
		}
	}

	/**
	 * Extracts empty spaces in a {@link TiledMap} layer and adds them to a
	 * {@link Array} instance
	 * 
	 * @param results
	 *            The {@link Array} instance to add empty spaces to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract empty spaces from
	 * @param layerIndex
	 *            The index of the layer to extract empty spaces from. Each tile
	 *            drawn in the layer is treated as a collision.
	 */
	public void mapEmptySpacesByLayer(Array<T> results, TiledMap tiledMap, int layerIndex) {
		if (layerIndex < 0) {
			return;
		}
		TileLayer layer = tiledMap.getTileLayer(layerIndex);
		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				if (layer.getTileId(x, y) == 0) {
					T collision = collisionFactory.createCollision(tiledMap, tiledMap.getTile(layer.getTileId(x, y)),
							x * tiledMap.getTileWidth(), y * tiledMap.getTileHeight(), tiledMap.getTileWidth(),
							tiledMap.getTileHeight());
					if (collision == null) {
						continue;
					}
					results.add(collision);
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
	 *            The name of the layer to extract collisions from. Each tile drawn
	 *            in the layer is treated as a collision.
	 */
	public void mapCollisionsByLayer(QuadTree<T> quadTree, TiledMap tiledMap, String layerName) {
		mapCollisionsByLayer(quadTree, tiledMap, tiledMap.getLayerIndex(layerName));
	}

	/**
	 * Extracts collisions in a {@link TiledMap} layer and adds them to a
	 * {@link Array} instance
	 * 
	 * @param results
	 *            The {@link Array} instance to add collisions to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract collisions from
	 * @param layerName
	 *            The name of the layer to extract collisions from. Each tile drawn
	 *            in the layer is treated as a collision.
	 */
	public void mapCollisionsByLayer(Array<T> results, TiledMap tiledMap, String layerName) {
		mapCollisionsByLayer(results, tiledMap, tiledMap.getLayerIndex(layerName));
	}

	/**
	 * Extracts empty spaces in a {@link TiledMap} layer and adds them to a
	 * {@link QuadTree} instance
	 * 
	 * @param quadTree
	 *            The {@link QuadTree} instance to add empty spaces to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract empty spaces from
	 * @param layerName
	 *            The name of the layer to extract empty spaces from. Each tile
	 *            drawn in the layer is treated as a collision.
	 */
	public void mapEmptySpacesByLayer(QuadTree<T> quadTree, TiledMap tiledMap, String layerName) {
		mapEmptySpacesByLayer(quadTree, tiledMap, tiledMap.getLayerIndex(layerName));
	}

	/**
	 * Extracts empty spaces in a {@link TiledMap} layer and adds them to a
	 * {@link Array} instance
	 * 
	 * @param results
	 *            The {@link Array} instance to add empty spaces to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract empty spaces from
	 * @param layerName
	 *            The name of the layer to extract empty spaces from. Each tile
	 *            drawn in the layer is treated as a collision.
	 */
	public void mapEmptySpacesByLayer(Array<T> results, TiledMap tiledMap, String layerName) {
		mapEmptySpacesByLayer(results, tiledMap, tiledMap.getLayerIndex(layerName));
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
		if (objectGroup == null) {
			return;
		}

		for (TiledObject tiledObject : objectGroup.getObjects()) {
			T collision = collisionFactory.createCollision(tiledMap, tiledObject);
			if (collision == null) {
				continue;
			}
			quadTree.add(collision);
		}
	}

	/**
	 * Extracts collisions in a {@link TiledMap} object group that have a specific
	 * value in their type field and adds them to a {@link QuadTree} instance
	 * 
	 * @param quadTree
	 *            The {@link QuadTree} instance to add collisions to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract collisions from
	 * @param groupName
	 *            The name of the object group to extract collisions from. Each
	 *            object is treated as a collision.
	 * @param objectType
	 *            The object type to extract
	 */
	public void mapCollisionsByObjectGroup(QuadTree<T> quadTree, TiledMap tiledMap, String groupName,
			String objectType) {
		TiledObjectGroup objectGroup = tiledMap.getObjectGroup(groupName);
		if (objectGroup == null) {
			return;
		}

		for (TiledObject tiledObject : objectGroup.getObjects()) {
			if (tiledObject.getType() == null) {
				continue;
			}
			if (!tiledObject.getType().equalsIgnoreCase(objectType)) {
				continue;
			}
			T collision = collisionFactory.createCollision(tiledMap, tiledObject);
			if (collision == null) {
				continue;
			}
			quadTree.add(collision);
		}
	}

	/**
	 * Extracts collisions in a {@link TiledMap} object group and adds them to a
	 * {@link Array} instance
	 * 
	 * @param results
	 *            The {@link Array} instance to add collisions to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract collisions from
	 * @param groupName
	 *            The name of the object group to extract collisions from. Each
	 *            object is treated as a collision.
	 */
	public void mapCollisionsByObjectGroup(Array<T> results, TiledMap tiledMap, String groupName) {
		TiledObjectGroup objectGroup = tiledMap.getObjectGroup(groupName);
		if (objectGroup == null) {
			return;
		}

		for (TiledObject tiledObject : objectGroup.getObjects()) {
			T collision = collisionFactory.createCollision(tiledMap, tiledObject);
			if (collision == null) {
				continue;
			}
			results.add(collision);
		}
	}

	/**
	 * Extracts collisions in a {@link TiledMap} object group that have a specific
	 * value in their type field and adds them to a {@link Array} instance
	 * 
	 * @param results
	 *            The {@link Array} instance to add collisions to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract collisions from
	 * @param groupName
	 *            The name of the object group to extract collisions from. Each
	 *            object is treated as a collision.
	 * @param objectType
	 *            The object type to extract
	 */
	public void mapCollisionsByObjectGroup(Array<T> results, TiledMap tiledMap, String groupName, String objectType) {
		TiledObjectGroup objectGroup = tiledMap.getObjectGroup(groupName);
		if (objectGroup == null) {
			return;
		}

		for (TiledObject tiledObject : objectGroup.getObjects()) {
			if (tiledObject.getType() == null) {
				continue;
			}
			if (!tiledObject.getType().equalsIgnoreCase(objectType)) {
				continue;
			}
			T collision = collisionFactory.createCollision(tiledMap, tiledObject);
			if (collision == null) {
				continue;
			}
			results.add(collision);
		}
	}

	/**
	 * Extracts and merges collisions in a {@link TiledMap} layer and adds them to a
	 * {@link QuadTree} instance. Tiles are determined as mergeable by the
	 * {@link TiledCollisionMerger} instance associated with this
	 * {@link TiledCollisionMapper}.
	 * 
	 * @param quadTree
	 *            The {@link QuadTree} instance to add collisions to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract collisions from
	 * @param layerName
	 *            The name of the layer to extract collisions from. Each tile drawn
	 *            in the layer is treated as a collision.
	 */
	public void mapAndMergeCollisionsByLayer(QuadTree<T> quadTree, TiledMap tiledMap, String layerName) {
		mapAndMergeCollisionsByLayer(quadTree, tiledMap, tiledMap.getLayerIndex(layerName));
	}

	/**
	 * Extracts and merges collisions in a {@link TiledMap} layer and adds them to a
	 * {@link QuadTree} instance. Tiles are determined as mergeable by the
	 * {@link TiledCollisionMerger} instance associated with this
	 * {@link TiledCollisionMapper}.
	 * 
	 * @param quadTree
	 *            The {@link QuadTree} instance to add collisions to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract collisions from
	 * @param layerIndex
	 *            The index of the layer to extract collisions from. Each tile drawn
	 *            in the layer is treated as a collision.
	 */
	public void mapAndMergeCollisionsByLayer(QuadTree<T> quadTree, TiledMap tiledMap, int layerIndex) {
		mapAndMergeCollisionsByLayer(quadTree, tiledMap, layerIndex, Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	/**
	 * Extracts and merges collisions in a {@link TiledMap} layer and adds them to a
	 * {@link QuadTree} instance. Tiles are determined as mergeable by the
	 * {@link TiledCollisionMerger} instance associated with this
	 * {@link TiledCollisionMapper}.
	 * 
	 * @param quadTree
	 *            The {@link QuadTree} instance to add collisions to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract collisions from
	 * @param layerIndex
	 *            The index of the layer to extract collisions from. Each tile drawn
	 *            in the layer is treated as a collision.
	 * @param maxColumns
	 *            The maximum number of columns to merge
	 * @param maxRows
	 *            The maximum number of rows to merge
	 */
	public void mapAndMergeCollisionsByLayer(QuadTree<T> quadTree, TiledMap tiledMap, final int layerIndex,
			final int maxColumns, final int maxRows) {
		if (layerIndex < 0) {
			return;
		}
		if (maxColumns < 0) {
			throw new MdxException("maxColumns cannot be less than 1");
		}
		if (maxRows < 0) {
			throw new MdxException("maxRows cannot be less than 1");
		}

		TileLayer layer = tiledMap.getTileLayer(layerIndex);
		byte[][] collisions = mapCollisionsByLayer(tiledMap, layer);

		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				if (collisions[x][y] == 0) {
					continue;
				}
				T collision = mergeCollisions(x, y, maxColumns, maxRows, collisions, layer, tiledMap);
				if (collision == null) {
					continue;
				}
				quadTree.add(collision);
			}
		}
	}

	/**
	 * Extracts and merges empty spaces in a {@link TiledMap} layer and adds them to
	 * a {@link QuadTree} instance. Tiles are determined as mergeable by the
	 * {@link TiledCollisionMerger} instance associated with this
	 * {@link TiledCollisionMapper}.
	 * 
	 * @param quadTree
	 *            The {@link QuadTree} instance to add empty spaces to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract empty spaces from
	 * @param layerName
	 *            The name of the layer to extract empty spaces from. Each tile
	 *            drawn in the layer is treated as a collision.
	 */
	public void mapAndMergeEmptySpacesByLayer(QuadTree<T> quadTree, TiledMap tiledMap, String layerName) {
		mapAndMergeEmptySpacesByLayer(quadTree, tiledMap, tiledMap.getLayerIndex(layerName));
	}

	/**
	 * Extracts and merges empty spaces in a {@link TiledMap} layer and adds them to
	 * a {@link QuadTree} instance. Tiles are determined as mergeable by the
	 * {@link TiledCollisionMerger} instance associated with this
	 * {@link TiledCollisionMapper}.
	 * 
	 * @param quadTree
	 *            The {@link QuadTree} instance to add empty spaces to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract empty spaces from
	 * @param layerIndex
	 *            The index of the layer to extract empty spaces from. Each tile
	 *            drawn in the layer is treated as a collision.
	 */
	public void mapAndMergeEmptySpacesByLayer(QuadTree<T> quadTree, TiledMap tiledMap, int layerIndex) {
		mapAndMergeEmptySpacesByLayer(quadTree, tiledMap, layerIndex, Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	/**
	 * Extracts and merges empty spaces in a {@link TiledMap} layer and adds them to
	 * a {@link QuadTree} instance. Tiles are determined as mergeable by the
	 * {@link TiledCollisionMerger} instance associated with this
	 * {@link TiledCollisionMapper}.
	 * 
	 * @param quadTree
	 *            The {@link QuadTree} instance to add empty spaces to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract empty spaces from
	 * @param layerIndex
	 *            The index of the layer to extract empty spaces from. Each tile
	 *            drawn in the layer is treated as a collision.
	 * @param maxColumns
	 *            The maximum number of columns to merge
	 * @param maxRows
	 *            The maximum number of rows to merge
	 */
	public void mapAndMergeEmptySpacesByLayer(QuadTree<T> quadTree, TiledMap tiledMap, final int layerIndex,
			final int maxColumns, final int maxRows) {
		if (layerIndex < 0) {
			return;
		}
		if (maxColumns < 0) {
			throw new MdxException("maxColumns cannot be less than 1");
		}
		if (maxRows < 0) {
			throw new MdxException("maxRows cannot be less than 1");
		}

		TileLayer layer = tiledMap.getTileLayer(layerIndex);
		byte[][] emptySpaces = mapEmptySpacesByLayer(tiledMap, layer);

		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				if (emptySpaces[x][y] == 0) {
					continue;
				}
				T collision = mergeCollisions(x, y, maxColumns, maxRows, emptySpaces, layer, tiledMap);
				if (collision == null) {
					continue;
				}
				quadTree.add(collision);
			}
		}
	}

	/**
	 * Extracts and merges collisions in a {@link TiledMap} layer and adds them to a
	 * {@link Array} instance. Tiles are determined as mergeable by the
	 * {@link TiledCollisionMerger} instance associated with this
	 * {@link TiledCollisionMapper}.
	 * 
	 * @param results
	 *            The {@link Array} instance to add collisions to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract collisions from
	 * @param layerName
	 *            The name of the layer to extract collisions from. Each tile drawn
	 *            in the layer is treated as a collision.
	 */
	public void mapAndMergeCollisionsByLayer(Array<T> results, TiledMap tiledMap, String layerName) {
		mapAndMergeCollisionsByLayer(results, tiledMap, tiledMap.getLayerIndex(layerName));
	}

	/**
	 * Extracts and merges collisions in a {@link TiledMap} layer and adds them to a
	 * {@link Array} instance. Tiles are determined as mergeable by the
	 * {@link TiledCollisionMerger} instance associated with this
	 * {@link TiledCollisionMapper}.
	 * 
	 * @param results
	 *            The {@link Array} instance to add collisions to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract collisions from
	 * @param layerIndex
	 *            The index of the layer to extract collisions from. Each tile drawn
	 *            in the layer is treated as a collision.
	 */
	public void mapAndMergeCollisionsByLayer(Array<T> results, TiledMap tiledMap, int layerIndex) {
		mapAndMergeCollisionsByLayer(results, tiledMap, layerIndex, Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	/**
	 * Extracts and merges collisions in a {@link TiledMap} layer and adds them to a
	 * {@link Array} instance. Tiles are determined as mergeable by the
	 * {@link TiledCollisionMerger} instance associated with this
	 * {@link TiledCollisionMapper}.
	 * 
	 * @param results
	 *            The {@link Array} instance to add collisions to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract collisions from
	 * @param layerIndex
	 *            The index of the layer to extract collisions from. Each tile drawn
	 *            in the layer is treated as a collision.
	 * @param maxColumns
	 *            The maximum number of columns to merge
	 * @param maxRows
	 *            The maximum number of rows to merge
	 */
	public void mapAndMergeCollisionsByLayer(Array<T> results, TiledMap tiledMap, final int layerIndex,
			final int maxColumns, final int maxRows) {
		if (layerIndex < 0) {
			return;
		}
		if (maxColumns < 0) {
			throw new MdxException("maxColumns cannot be less than 1");
		}
		if (maxRows < 0) {
			throw new MdxException("maxRows cannot be less than 1");
		}

		TileLayer layer = tiledMap.getTileLayer(layerIndex);
		byte[][] collisions = mapCollisionsByLayer(tiledMap, layer);

		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				if (collisions[x][y] == 0) {
					continue;
				}
				T collision = mergeCollisions(x, y, maxColumns, maxRows, collisions, layer, tiledMap);
				if (collision == null) {
					continue;
				}
				results.add(collision);
			}
		}
	}

	/**
	 * Extracts and merges empty spaces in a {@link TiledMap} layer and adds them to
	 * a {@link Array} instance. Tiles are determined as mergeable by the
	 * {@link TiledCollisionMerger} instance associated with this
	 * {@link TiledCollisionMapper}.
	 * 
	 * @param results
	 *            The {@link Array} instance to add empty spaces to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract empty spaces from
	 * @param layerName
	 *            The name of the layer to extract empty spaces from. Each tile
	 *            drawn in the layer is treated as a collision.
	 */
	public void mapAndMergeEmptySpacesByLayer(Array<T> results, TiledMap tiledMap, String layerName) {
		mapAndMergeEmptySpacesByLayer(results, tiledMap, tiledMap.getLayerIndex(layerName));
	}

	/**
	 * Extracts and merges empty spaces in a {@link TiledMap} layer and adds them to
	 * a {@link Array} instance. Tiles are determined as mergeable by the
	 * {@link TiledCollisionMerger} instance associated with this
	 * {@link TiledCollisionMapper}.
	 * 
	 * @param results
	 *            The {@link Array} instance to add empty spaces to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract empty spaces from
	 * @param layerIndex
	 *            The index of the layer to extract empty spaces from. Each tile
	 *            drawn in the layer is treated as a collision.
	 */
	public void mapAndMergeEmptySpacesByLayer(Array<T> results, TiledMap tiledMap, int layerIndex) {
		mapAndMergeEmptySpacesByLayer(results, tiledMap, layerIndex, Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	/**
	 * Extracts and merges empty spaces in a {@link TiledMap} layer and adds them to
	 * a {@link Array} instance. Tiles are determined as mergeable by the
	 * {@link TiledCollisionMerger} instance associated with this
	 * {@link TiledCollisionMapper}.
	 * 
	 * @param results
	 *            The {@link Array} instance to add empty spaces to
	 * @param tiledMap
	 *            The {@link TiledMap} to extract empty spaces from
	 * @param layerIndex
	 *            The index of the layer to extract empty spaces from. Each tile
	 *            drawn in the layer is treated as a collision.
	 * @param maxColumns
	 *            The maximum number of columns to merge
	 * @param maxRows
	 *            The maximum number of rows to merge
	 */
	public void mapAndMergeEmptySpacesByLayer(Array<T> results, TiledMap tiledMap, final int layerIndex,
			final int maxColumns, final int maxRows) {
		if (layerIndex < 0) {
			return;
		}
		if (maxColumns < 0) {
			throw new MdxException("maxColumns cannot be less than 1");
		}
		if (maxRows < 0) {
			throw new MdxException("maxRows cannot be less than 1");
		}

		TileLayer layer = tiledMap.getTileLayer(layerIndex);
		byte[][] emptySpaces = mapEmptySpacesByLayer(tiledMap, layer);

		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				if (emptySpaces[x][y] == 0) {
					continue;
				}
				T collision = mergeCollisions(x, y, maxColumns, maxRows, emptySpaces, layer, tiledMap);
				if (collision == null) {
					continue;
				}
				results.add(collision);
			}
		}
	}

	private T mergeCollisions(final int startX, final int startY, final int maxColumns, final int maxRows,
			byte[][] collisions, TileLayer layer, TiledMap tiledMap) {
		return mergeMode.merge(collisionFactory, collisionMerger, startX, startY, maxColumns, maxRows, collisions,
				layer, tiledMap);
	}
}
