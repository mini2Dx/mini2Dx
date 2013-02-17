/**
 * Copyright (c) 2013, mini2Dx Project
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

import java.io.IOException;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.files.FileHandle;

/**
 * Unit tests for {@link TiledMap}s
 * 
 * @author Thomas Cashman
 */
public class TiledMapTest {
	private static TiledMap tiledMap;

	@BeforeClass
	public static void loadMap() throws IOException {
		FileHandle file = new FileHandle(Thread.currentThread()
				.getContextClassLoader().getResource("simple.tmx").getFile());
		tiledMap = new TiledMap(file, false);
	}

	@Test
	public void testGetProperty() {
		Assert.assertEquals("SUCCESS", tiledMap.getProperty("testProperty"));
	}

	@Test
	public void testGetOrientation() {
		Assert.assertEquals(Orientation.ORTHOGONAL, tiledMap.getOrientation());
	}

	@Test
	public void testGetWidth() {
		Assert.assertEquals(10, tiledMap.getWidth());
	}

	@Test
	public void testGetHeight() {
		Assert.assertEquals(8, tiledMap.getHeight());
	}

	@Test
	public void testGetTileWidth() {
		Assert.assertEquals(32, tiledMap.getTileWidth());

		for (int i = 0; i < tiledMap.getTilesets().size(); i++) {
			Assert.assertEquals(32, tiledMap.getTilesets().get(i)
					.getTileWidth());
		}
	}

	@Test
	public void testGetTileHeight() {
		Assert.assertEquals(32, tiledMap.getTileHeight());

		for (int i = 0; i < tiledMap.getTilesets().size(); i++) {
			Assert.assertEquals(32, tiledMap.getTilesets().get(i)
					.getTileHeight());
		}
	}

	@Test
	public void testGetTilesets() {
		Assert.assertEquals(1, tiledMap.getTilesets().size());
	}

	@Test
	public void testGetTileLayers() {
		Assert.assertEquals(3, tiledMap.getTileLayers().size());
	}

	@Test
	public void testGetObjectGroups() {
		Assert.assertEquals(1, tiledMap.getObjectGroups().size());

		TiledObjectGroup group = tiledMap.getObjectGroups().get(0);
		Assert.assertEquals("Objects", group.getName());
		Assert.assertEquals(1, group.getObjects().size());

		TiledObject obj = group.getObjects().get(0);
		Assert.assertEquals(true, obj.containsProperty("testProperty"));
		Assert.assertEquals("SUCCESS", obj.getProperty("testProperty"));
		Assert.assertEquals(32, obj.getX());
		Assert.assertEquals(64, obj.getY());
		Assert.assertEquals(16, obj.getWidth());
		Assert.assertEquals(24, obj.getHeight());
	}

}
