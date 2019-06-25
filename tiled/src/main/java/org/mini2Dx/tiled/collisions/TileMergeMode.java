/**
 * Copyright (c) 2018 See AUTHORS file
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
import org.mini2Dx.tiled.TileLayer;
import org.mini2Dx.tiled.TiledMap;

/**
 * Search method for determining mergable tiles
 */
public enum TileMergeMode {
	/**
	 * Finds max number of mergable tiles on Y axis then expands across X axis
	 */
	Y_THEN_X {
		@Override
		public <T extends Positionable> T merge(TiledCollisionFactory<T> collisionFactory,
												TiledCollisionMerger collisionMerger, int startX, int startY, int maxColumns, int maxRows,
												byte[][] collisions, TileLayer layer, TiledMap tiledMap) {
			Tile startTile = tiledMap.getTile(layer.getTileId(startX, startY));

			int maxXTiles = 0;
			int maxYTiles = 0;

			if (maxRows > 1) {
				for (int y = 1; y < layer.getHeight() - startY; y++) {
					if (collisions[startX][startY + y] == 0) {
						break;
					}

					if (!collisionMerger.isMergable(tiledMap, layer, startTile, startX, startY, startX, startY + y)) {
						break;
					}
					maxYTiles = y;
					if (maxYTiles >= maxRows - 1) {
						break;
					}
				}
			}

			if (maxColumns > 1) {
				for (int x = 1; x < layer.getWidth() - startX; x++) {
					boolean mergeable = true;
					for (int y = 0; y <= maxYTiles; y++) {
						if (collisions[startX + x][startY + y] == 0) {
							mergeable = false;
							break;
						}

						if (!collisionMerger.isMergable(tiledMap, layer, startTile, startX, startY, startX + x,
								startY + y)) {
							mergeable = false;
							break;
						}
					}
					if (!mergeable) {
						break;
					}
					maxXTiles = x;
					if (maxXTiles >= maxColumns - 1) {
						break;
					}
				}
			}

			// Clear the collision data for merged tiles
			for (int x = 0; x <= maxXTiles; x++) {
				for (int y = 0; y <= maxYTiles; y++) {
					collisions[startX + x][startY + y] = 0;
				}
			}

			return collisionFactory.createCollision(tiledMap, startTile, startX * tiledMap.getTileWidth(),
					startY * tiledMap.getTileHeight(), tiledMap.getTileWidth() + (maxXTiles * tiledMap.getTileWidth()),
					tiledMap.getTileHeight() + (maxYTiles * tiledMap.getTileHeight()));
		}
	},
	/**
	 * Finds max number of mergable tiles on X axis then expands across Y axis
	 */
	X_THEN_Y {
		@Override
		public <T extends Positionable> T merge(TiledCollisionFactory<T> collisionFactory,
												TiledCollisionMerger collisionMerger, int startX, int startY, int maxColumns, int maxRows,
												byte[][] collisions, TileLayer layer, TiledMap tiledMap) {
			Tile startTile = tiledMap.getTile(layer.getTileId(startX, startY));

			int maxXTiles = 0;
			int maxYTiles = 0;

			if (maxColumns > 1) {
				for (int x = 1; x < layer.getWidth() - startX; x++) {
					if (collisions[startX + x][startY] == 0) {
						break;
					}

					if (!collisionMerger.isMergable(tiledMap, layer, startTile, startX, startY, startX + x, startY)) {
						break;
					}
					maxXTiles = x;
					if (maxXTiles >= maxColumns - 1) {
						break;
					}
				}
			}

			if (maxRows > 1) {
				for (int y = 1; y < layer.getHeight() - startY; y++) {
					boolean mergeable = true;
					for (int x = 0; x <= maxXTiles; x++) {
						if (collisions[startX + x][startY + y] == 0) {
							mergeable = false;
							break;
						}

						if (!collisionMerger.isMergable(tiledMap, layer, startTile, startX, startY, startX + x,
								startY + y)) {
							mergeable = false;
							break;
						}
					}
					if (!mergeable) {
						break;
					}
					maxYTiles = y;
					if (maxYTiles >= maxRows - 1) {
						break;
					}
				}
			}

			// Clear the collision data for merged tiles
			for (int x = 0; x <= maxXTiles; x++) {
				for (int y = 0; y <= maxYTiles; y++) {
					collisions[startX + x][startY + y] = 0;
				}
			}

			return collisionFactory.createCollision(tiledMap, startTile, startX * tiledMap.getTileWidth(),
					startY * tiledMap.getTileHeight(), tiledMap.getTileWidth() + (maxXTiles * tiledMap.getTileWidth()),
					tiledMap.getTileHeight() + (maxYTiles * tiledMap.getTileHeight()));
		}
	},
	/**
	 * Expands on X axis by 1 tile, then Y axis by 1 tile and repeats until the maximum area is found
	 */
	X_THEN_Y_ALTERNATING {
		@Override
		public <T extends Positionable> T merge(TiledCollisionFactory<T> collisionFactory,
												TiledCollisionMerger collisionMerger, int startX, int startY, int maxColumns, int maxRows,
												byte[][] collisions, TileLayer layer, TiledMap tiledMap) {
			Tile startTile = tiledMap.getTile(layer.getTileId(startX, startY));

			int maxXTiles = 0;
			int maxYTiles = 0;

			if (maxColumns > 1 && maxRows > 1) {
				int x = 1;
				int y = 0;

				while(true) {
					if (x >= layer.getWidth() - startX) {
						break;
					}
					if (y >= layer.getHeight() - startY) {
						break;
					}
					boolean mismatch = false;
					for(int tx = 0; tx <= x; tx++) {
						for(int ty = 0; ty <= y; ty++) {
							if (collisions[startX + tx][startY + ty] == 0) {
								mismatch = true;
								break;
							}
							if (!collisionMerger.isMergable(tiledMap, layer, startTile, startX, startY, startX + tx, startY + ty)) {
								mismatch = true;
								break;
							}
						}
						if(mismatch) {
							break;
						}
					}
					if(mismatch) {
						break;
					}

					maxXTiles = x;
					maxYTiles = y;

					if(x > y) {
						y++;
					} else {
						x++;
					}

					if (maxXTiles >= maxColumns - 1) {
						break;
					}
					if (maxYTiles >= maxRows - 1) {
						break;
					}
				}
			}

			// Clear the collision data for merged tiles
			for (int x = 0; x <= maxXTiles; x++) {
				for (int y = 0; y <= maxYTiles; y++) {
					collisions[startX + x][startY + y] = 0;
				}
			}

			return collisionFactory.createCollision(tiledMap, startTile, startX * tiledMap.getTileWidth(),
					startY * tiledMap.getTileHeight(), tiledMap.getTileWidth() + (maxXTiles * tiledMap.getTileWidth()),
					tiledMap.getTileHeight() + (maxYTiles * tiledMap.getTileHeight()));
		}
	},
	/**
	 * Expands on Y axis by 1 tile, then X axis by 1 tile and repeats until the maximum area is found
	 */
	Y_THEN_X_ALTERNATING {
		@Override
		public <T extends Positionable> T merge(TiledCollisionFactory<T> collisionFactory,
												TiledCollisionMerger collisionMerger, int startX, int startY, int maxColumns, int maxRows,
												byte[][] collisions, TileLayer layer, TiledMap tiledMap) {
			Tile startTile = tiledMap.getTile(layer.getTileId(startX, startY));

			int maxXTiles = 0;
			int maxYTiles = 0;

			if (maxColumns > 1 && maxRows > 1) {
				int x = 0;
				int y = 1;

				while(true) {
					if (x >= layer.getWidth() - startX) {
						break;
					}
					if (y >= layer.getHeight() - startY) {
						break;
					}
					boolean mismatch = false;
					for(int tx = 0; tx <= x; tx++) {
						for(int ty = 0; ty <= y; ty++) {
							if (collisions[startX + tx][startY + ty] == 0) {
								mismatch = true;
								break;
							}
							if (!collisionMerger.isMergable(tiledMap, layer, startTile, startX, startY, startX + tx, startY + ty)) {
								mismatch = true;
								break;
							}
						}
						if(mismatch) {
							break;
						}
					}
					if(mismatch) {
						break;
					}

					maxXTiles = x;
					maxYTiles = y;

					if(y > x) {
						x++;
					} else {
						y++;
					}

					if (maxXTiles >= maxColumns - 1) {
						break;
					}
					if (maxYTiles >= maxRows - 1) {
						break;
					}
				}
			}

			// Clear the collision data for merged tiles
			for (int x = 0; x <= maxXTiles; x++) {
				for (int y = 0; y <= maxYTiles; y++) {
					collisions[startX + x][startY + y] = 0;
				}
			}

			return collisionFactory.createCollision(tiledMap, startTile, startX * tiledMap.getTileWidth(),
					startY * tiledMap.getTileHeight(), tiledMap.getTileWidth() + (maxXTiles * tiledMap.getTileWidth()),
					tiledMap.getTileHeight() + (maxYTiles * tiledMap.getTileHeight()));
		}
	},
	/**
	 * Finds max mergable tiles by expanding a square area
	 */
	SQUARE {
		@Override
		public <T extends Positionable> T merge(TiledCollisionFactory<T> collisionFactory,
												TiledCollisionMerger collisionMerger, int startX, int startY, int maxColumns, int maxRows,
												byte[][] collisions, TileLayer layer, TiledMap tiledMap) {
			Tile startTile = tiledMap.getTile(layer.getTileId(startX, startY));

			int maxXTiles = 0;
			int maxYTiles = 0;

			if (maxColumns > 1 && maxRows > 1) {
				for (int xy = 1; xy < layer.getWidth() - startX && xy < layer.getHeight() - startY; xy++) {
					boolean mergable = true;

					for(int x = 0; x <= xy; x++) {
						if (collisions[startX + x][startY + xy] == 0) {
							mergable = false;
							break;
						}
						if (!collisionMerger.isMergable(tiledMap, layer, startTile, startX, startY, startX + x, startY + xy)) {
							mergable = false;
							break;
						}
					}
					if(mergable) {
						for(int y = 0; y <= xy; y++) {
							if (collisions[startX + xy][startY + y] == 0) {
								mergable = false;
								break;
							}
							if (!collisionMerger.isMergable(tiledMap, layer, startTile, startX, startY, startX + xy, startY + y)) {
								mergable = false;
								break;
							}
						}
					}

					if(!mergable) {
						break;
					}

					maxXTiles = xy;
					maxYTiles = xy;

					if (maxXTiles >= maxColumns - 1) {
						break;
					}
					if (maxYTiles >= maxRows - 1) {
						break;
					}
				}
			}

			// Clear the collision data for merged tiles
			for (int x = 0; x <= maxXTiles; x++) {
				for (int y = 0; y <= maxYTiles; y++) {
					collisions[startX + x][startY + y] = 0;
				}
			}

			return collisionFactory.createCollision(tiledMap, startTile, startX * tiledMap.getTileWidth(),
					startY * tiledMap.getTileHeight(), tiledMap.getTileWidth() + (maxXTiles * tiledMap.getTileWidth()),
					tiledMap.getTileHeight() + (maxYTiles * tiledMap.getTileHeight()));
		}
	};

	public abstract <T extends Positionable> T merge(TiledCollisionFactory<T> collisionFactory,
													 TiledCollisionMerger collisionMerger, final int startX, final int startY, final int maxColumns,
													 final int maxRows, byte[][] collisions, TileLayer layer, TiledMap tiledMap);
}