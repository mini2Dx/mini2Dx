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
package org.mini2Dx.tiled;

import org.mini2Dx.core.serialization.GameDataSerializableUtils;
import org.mini2Dx.gdx.math.MathUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.BitSet;

/**
 * Represents a tile layer with in a {@link TiledMap}
 */
public class TileLayer extends Layer {
	private final int[][] tiles;
	private BitSet flipHorizontally;
	private BitSet flipVertically;
	private BitSet flipDiagonally;

	public TileLayer(int width, int height) {
		super(LayerType.TILE);

		tiles = new int[width][height];
		flipHorizontally = new BitSet(width * height);
		flipVertically = new BitSet(width * height);
		flipDiagonally = new BitSet(width * height);
	}

	public static TileLayer fromInputStream(DataInputStream inputStream) throws IOException {
		final int width = inputStream.readInt();
		final int height = inputStream.readInt();
		final TileLayer result = new TileLayer(width, height);
		result.readData(inputStream);
		return result;
	}

	@Override
	public void writeData(DataOutputStream outputStream) throws IOException {
		outputStream.writeInt(getWidth());
		outputStream.writeInt(getHeight());
		for(int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				outputStream.writeInt(tiles[x][y]);
			}
		}

		GameDataSerializableUtils.writeArray(flipHorizontally.toLongArray(), outputStream);
		GameDataSerializableUtils.writeArray(flipVertically.toLongArray(), outputStream);
		GameDataSerializableUtils.writeArray(flipDiagonally.toLongArray(), outputStream);
	}

	@Override
	public void readData(DataInputStream inputStream) throws IOException {
		for(int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				tiles[x][y] = inputStream.readInt();
			}
		}

		flipHorizontally = BitSet.valueOf(GameDataSerializableUtils.readArray(inputStream));
		flipVertically = BitSet.valueOf(GameDataSerializableUtils.readArray(inputStream));
		flipDiagonally = BitSet.valueOf(GameDataSerializableUtils.readArray(inputStream));
	}

	/**
	 * Returns the tile id at a given coordinate on the layer
	 * 
	 * @param x
	 *            The x coordinate in tiles
	 * @param y
	 *            The y coordinate in tiles
	 * @return 0 if there is no tile
	 */
	public int getTileId(int x, int y) {
		return tiles[x][y];
	}

	/**
	 * Sets the tile id at a given coordinate on the layer
	 * 
	 * @param x
	 *            The x coordinate in tiles
	 * @param y
	 *            The y coordinate in tiles
	 * @param id
	 *            0 if there is no tile
	 */
	public void setTileId(int x, int y, int id) {
		tiles[x][y] = id;
	}
	
	/**
	 * Sets the tile id at a given coordinate on the layer
	 * 
	 * @param x
	 *            The x coordinate in tiles
	 * @param y
	 *            The y coordinate in tiles
	 * @param id
	 *            0 if there is no tile
	 * @param flipH True if the tile is flipped horizontally
	 * @param flipV True if the tile is flipped vertically
	 * @param flipD True if the tile is flipped (anti) diagonally - rotation
	 */
	public void setTileId(int x, int y, int id, boolean flipH, boolean flipV, boolean flipD) {
		tiles[x][y] = id;
		setFlippedHorizontally(x, y, flipH);
		setFlippedVertically(x, y, flipV);
		setFlippedDiagonally(x, y, flipD);
	}
	
	/**
	 * Returns if the tile at the given coordinate is flipped horizontally
	 * @param x The tile x coordinate
	 * @param y The tile y coordinate
	 * @return True if flipped horizontally
	 */
	public boolean isFlippedHorizontally(int x, int y) {
		return flipHorizontally.get(getBitIndex(x, y));
	}
	
	/**
	 * Sets if the tile at the given coordinate is flipped horizontally
	 * @param x The tile x coordinate
	 * @param y The tile y coordinate
	 * @param flip True if flipped horizontally
	 */
	public void setFlippedHorizontally(int x, int y, boolean flip) {
		flipHorizontally.set(getBitIndex(x, y), flip);
	}
	
	/**
	 * Returns if the tile at the given coordinate is flipped vertically
	 * @param x The tile x coordinate
	 * @param y The tile y coordinate
	 * @return True if flipped vertically
	 */
	public boolean isFlippedVertically(int x, int y) {
		return flipVertically.get(getBitIndex(x, y));
	}
	
	/**
	 * Sets if the tile at the given coordinate is flipped vertically
	 * @param x The tile x coordinate
	 * @param y The tile y coordinate
	 * @param flip True if flipped vertically
	 */
	public void setFlippedVertically(int x, int y, boolean flip) {
		flipVertically.set(getBitIndex(x, y), flip);
	}
	
	/**
	 * Returns if the tile at the given coordinate is flipped diagonally
	 * @param x The tile x coordinate
	 * @param y The tile y coordinate
	 * @return True if flipped diagonally
	 */
	public boolean isFlippedDiagonally(int x, int y) {
		return flipDiagonally.get(getBitIndex(x, y));
	}
	
	/**
	 * Sets if the tile at the given coordinate is flipped diagonally
	 * @param x The tile x coordinate
	 * @param y The tile y coordinate
	 * @param flip True if flipped diagonally
	 */
	public void setFlippedDiagonally(int x, int y, boolean flip) {
		flipDiagonally.set(getBitIndex(x, y), flip);
	}

	/**
	 * Returns the width of the layer
	 * @return The width in tiles
	 */
	public int getWidth() {
		return tiles.length;
	}

	/**
	 * Returns the height of the layer
	 * @return The height in tiles
	 */
	public int getHeight() {
		return tiles[0].length;
	}
	
	private int getBitIndex(int x, int y) {
		return (y * tiles.length) + x;
	}

	/**
	 * Returns if the majority of the {@link TileLayer} consists of empty tiles
	 * @return False if &gt;{@link TiledMap#FAST_RENDER_EMPTY_LAYERS_THRESHOLD} of the layer contains tiles
	 */
	public boolean isMostlyEmptyTiles() {
		return (getTotalEmptyTiles() * 1f) / (getWidth() * getHeight() * 1f) >= TiledMap.FAST_RENDER_EMPTY_LAYERS_THRESHOLD;
	}

	/**
	 * Returns the total number of empty tiles on this layer
	 * @return
	 */
	public int getTotalEmptyTiles() {
		int result = 0;

		for(int x = 0; x < getWidth(); x++) {
			for(int y = 0; y < getHeight(); y++) {
				if(getTileId(x, y) >= 1) {
					continue;
				}
				result++;
			}
		}
		return result;
	}

	/**
	 * Returns the total number of tiles that have content on this layer
	 * @return
	 */
	public int getTotalFilledTiles() {
		int result = 0;

		for(int x = 0; x < getWidth(); x++) {
			for(int y = 0; y < getHeight(); y++) {
				if(getTileId(x, y) < 1) {
					continue;
				}
				result++;
			}
		}
		return result;
	}
}
