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
package org.mini2Dx.tiled;

import java.util.BitSet;

/**
 * Represents a tile layer with in a {@link TiledMap}
 */
public class TileLayer extends Layer {
	private final int[][] tiles;
	private final BitSet flipHorizontally;
	private final BitSet flipVertically;
	private final BitSet flipDiagonally;

	public TileLayer(int width, int height) {
		super(LayerType.TILE);
		tiles = new int[width][height];
		flipHorizontally = new BitSet(width * height);
		flipVertically = new BitSet(width * height);
		flipDiagonally = new BitSet(width * height);
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
}
