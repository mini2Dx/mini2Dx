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

import junit.framework.Assert;
import org.junit.Test;
import org.mini2Dx.tiled.tileset.ImageTilesetSource;

/**
 * Unit tests for {@link Tileset}
 */
public class TilesetTest {
	private Tileset tileset;

	@Test
	public void testGetTileId() {
		tileset = new Tileset(1, new ImageTilesetSource(128, 128, 32, 32, 0, 0));
		Assert.assertEquals(1, tileset.getTileId(0, 0));
		Assert.assertEquals(5, tileset.getTileId(0, 1));

		tileset = new Tileset(1, new ImageTilesetSource(1936, 1052, 32, 32, 2, 0));
		Assert.assertEquals(1, tileset.getTileId(0, 0));
		Assert.assertEquals(343, tileset.getTileId(0, 6));
		Assert.assertEquals(345, tileset.getTileId(2, 6));
	}

	@Test
	public void testLastGidWithoutSpacing() {
		tileset = new Tileset(1, new ImageTilesetSource(128, 128, 32, 32, 0, 0));
		Assert.assertEquals(true, tileset.contains(16));
		Assert.assertEquals(false, tileset.contains(17));
	}

	@Test
	public void testLastGidWithSpacing() {
		tileset = new Tileset(1, new ImageTilesetSource(128, 128, 32, 32, 4, 0));
		Assert.assertEquals(true, tileset.contains(9));
		Assert.assertEquals(false, tileset.contains(10));
	}

	@Test
	public void testGetTileX() {
		tileset = new Tileset(1, new ImageTilesetSource(128, 128, 32, 32, 0, 0));
		Assert.assertEquals(0, tileset.getTileX(1));
		Assert.assertEquals(0, tileset.getTileX(5));
		tileset = new Tileset(28, new ImageTilesetSource(128, 128, 32, 32, 0, 0));
		Assert.assertEquals(0, tileset.getTileX(28));
		Assert.assertEquals(0, tileset.getTileX(32));
	}

	@Test
	public void testGetTileXWithSpacing() {
		tileset = new Tileset(1, new ImageTilesetSource(128, 128, 32, 32, 4, 0));

		Assert.assertEquals(0, tileset.getTileX(1));
		Assert.assertEquals(0, tileset.getTileX(4));
		Assert.assertEquals(1, tileset.getTileX(5));

		tileset = new Tileset(28, new ImageTilesetSource(128, 128, 32, 32, 4, 0));
		Assert.assertEquals(0, tileset.getTileX(28));
		Assert.assertEquals(0, tileset.getTileX(31));
		Assert.assertEquals(1, tileset.getTileX(32));

		tileset = new Tileset(1, new ImageTilesetSource(1936, 1052, 32, 32, 2, 0));
		Assert.assertEquals(0, tileset.getTileX(343));
		Assert.assertEquals(2, tileset.getTileX(345));
		Assert.assertEquals(5, tileset.getTileX(234));
	}

	@Test
	public void testGetTileXWithMargin() {
		tileset = new Tileset(1, new ImageTilesetSource(128, 128, 32, 32, 0, 4));

		Assert.assertEquals(0, tileset.getTileX(1));
		Assert.assertEquals(0, tileset.getTileX(4));
		Assert.assertEquals(1, tileset.getTileX(5));

		tileset = new Tileset(28, new ImageTilesetSource(128, 128, 32, 32, 0, 4));
		Assert.assertEquals(0, tileset.getTileX(28));
		Assert.assertEquals(0, tileset.getTileX(31));
		Assert.assertEquals(1, tileset.getTileX(32));
	}

	@Test
	public void testGetTileY() {
		tileset = new Tileset(1, new ImageTilesetSource(128, 128, 32, 32, 0, 0));

		Assert.assertEquals(0, tileset.getTileY(1));
		Assert.assertEquals(1, tileset.getTileY(5));

		tileset = new Tileset(28, new ImageTilesetSource(128, 128, 32, 32, 0, 0));
		Assert.assertEquals(0, tileset.getTileY(28));
		Assert.assertEquals(1, tileset.getTileY(32));
	}

	@Test
	public void testGetTileYWithSpacing() {
		tileset = new Tileset(1, new ImageTilesetSource(128, 128, 32, 32, 4, 0));

		Assert.assertEquals(0, tileset.getTileY(1));
		Assert.assertEquals(1, tileset.getTileY(5));

		tileset = new Tileset(28, new ImageTilesetSource(128, 128, 32, 32, 4, 0));
		Assert.assertEquals(0, tileset.getTileY(28));
		Assert.assertEquals(1, tileset.getTileY(32));

		tileset = new Tileset(1, new ImageTilesetSource(1936, 1052, 32, 32, 2, 0));
		Assert.assertEquals(6, tileset.getTileY(345));
		Assert.assertEquals(4, tileset.getTileY(234));
	}

	@Test
	public void testGetTileYWithMargin() {
		tileset = new Tileset(1, new ImageTilesetSource(128, 128, 32, 32, 0, 4));

		Assert.assertEquals(0, tileset.getTileY(1));
		Assert.assertEquals(1, tileset.getTileY(5));

		tileset = new Tileset(28, new ImageTilesetSource(128, 128, 32, 32, 0, 4));
		Assert.assertEquals(0, tileset.getTileY(28));
		Assert.assertEquals(1, tileset.getTileY(32));
	}

	@Test
	public void testGetWidthInTiles() {
		tileset = new Tileset(1, new ImageTilesetSource(128, 128, 32, 32, 0, 0));
		Assert.assertEquals(4, tileset.getWidthInTiles());
		tileset = new Tileset(1, new ImageTilesetSource(96, 128, 32, 32, 0, 0));
		Assert.assertEquals(3, tileset.getWidthInTiles());
	}

	@Test
	public void testGetWidthInTilesWithSpacing() {
		tileset = new Tileset(1, new ImageTilesetSource(128, 128, 32, 32, 4, 0));
		Assert.assertEquals(3, tileset.getWidthInTiles());
		tileset = new Tileset(1, new ImageTilesetSource(96, 128, 32, 32, 4, 0));
		Assert.assertEquals(2, tileset.getWidthInTiles());
		tileset = new Tileset(1, new ImageTilesetSource(1936, 1052, 32, 32, 2, 0));
		Assert.assertEquals(57, tileset.getWidthInTiles());
	}

	@Test
	public void testGetWidthInTilesWithMargin() {
		tileset = new Tileset(1, new ImageTilesetSource(128, 128, 32, 32, 0, 4));

		Assert.assertEquals(3, tileset.getWidthInTiles());
		tileset = new Tileset(1, new ImageTilesetSource(96, 128, 32, 32, 0, 4));
		Assert.assertEquals(2, tileset.getWidthInTiles());
	}

	@Test
	public void testGetHeightInTiles() {
		tileset = new Tileset(1, new ImageTilesetSource(128, 128, 32, 32, 0, 0));
		Assert.assertEquals(4, tileset.getHeightInTiles());
		tileset = new Tileset(1, new ImageTilesetSource(128, 96, 32, 32, 0, 0));
		Assert.assertEquals(3, tileset.getHeightInTiles());
	}

	@Test
	public void testGetHeightInTilesWithSpacing() {
		tileset = new Tileset(1, new ImageTilesetSource(128, 128, 32, 32, 4, 0));
		Assert.assertEquals(3, tileset.getHeightInTiles());
		tileset = new Tileset(1, new ImageTilesetSource(128, 96, 32, 32, 4, 0));
		Assert.assertEquals(2, tileset.getHeightInTiles());
		tileset = new Tileset(1, new ImageTilesetSource(1936, 1052, 32, 32, 2, 0));
		Assert.assertEquals(31, tileset.getHeightInTiles());
	}

	@Test
	public void testGetHeightInTilesWithMargin() {
		tileset = new Tileset(1, new ImageTilesetSource(128, 128, 32, 32, 0, 4));
		Assert.assertEquals(3, tileset.getHeightInTiles());
		tileset = new Tileset(1, new ImageTilesetSource(128, 96, 32, 32, 0, 4));
		Assert.assertEquals(2, tileset.getHeightInTiles());
	}
}
