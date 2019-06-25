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

import org.junit.BeforeClass;
import org.junit.Test;
import org.mini2Dx.core.geom.Polygon;
import org.mini2Dx.tiled.exception.TiledException;

import com.badlogic.gdx.files.FileHandle;

/**
 * Unit tests for {@link TiledMap}s
 */
public class TiledMapTest {
	private static TiledMap tiledMap;

	@BeforeClass
	public static void loadMap() throws TiledException {
		FileHandle file = new FileHandle(Thread.currentThread()
				.getContextClassLoader().getResource("orthogonal.tmx").getFile());
		tiledMap = new TiledMap(file, false, false);
	}
	
	@Test
	public void testGetBackgroundColor() {
		Assert.assertNotNull(tiledMap.getBackgroundColor());
	}

	@Test
	public void testGetMapProperty() {
		Assert.assertEquals("SUCCESS", tiledMap.getProperty("testMapProperty"));
	}
	
	@Test
	public void testGetLayerProperty() {
		Assert.assertEquals("SUCCESS", tiledMap.getTileLayer("Collisions").getProperty("testLayerProperty"));
	}
	
	@Test
	public void testGetTilesetProperty() {
		Assert.assertEquals("SUCCESS", tiledMap.getTilesets().get(0).getProperty("testTilesetProperty"));
	}
	
	@Test
	public void testGetTileProperty() {
		Assert.assertEquals("SUCCESS", tiledMap.getTilesets().get(0).getTile(0, 0).getProperty("testTileProperty"));
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

		for (int i = 0; i < tiledMap.getTilesets().size; i++) {
			Assert.assertEquals(32, tiledMap.getTilesets().get(i)
					.getTileWidth());
		}
	}

	@Test
	public void testGetTileHeight() {
		Assert.assertEquals(32, tiledMap.getTileHeight());

		for (int i = 0; i < tiledMap.getTilesets().size; i++) {
			Assert.assertEquals(32, tiledMap.getTilesets().get(i)
					.getTileHeight());
		}
	}

	@Test
	public void testGetTilesets() {
		Assert.assertEquals(1, tiledMap.getTilesets().size);
	}

	@Test
	public void testGetTileLayers() {
		Assert.assertEquals(true, tiledMap.getTileLayer("Ground") != null);
		Assert.assertEquals(0, tiledMap.getTileLayer("Ground").getIndex());
		
		Assert.assertEquals(true, tiledMap.getTileLayer("Collisions") != null);
		Assert.assertEquals(3, tiledMap.getTileLayer("Collisions").getIndex());
		
		Assert.assertEquals(true, tiledMap.getTileLayer("Higher") != null);
		Assert.assertEquals(6, tiledMap.getTileLayer("Higher").getIndex());
	}

	@Test
	public void testShapes() {
		Assert.assertEquals(2, tiledMap.getTotalObjectGroups());

		TiledObjectGroup group = tiledMap.getObjectGroup("Shapes");

		final TiledObject point = group.getObjectByName("point");
		Assert.assertEquals(TiledObjectShape.POINT, point.getObjectShape());
		Assert.assertEquals(64f, point.toPoint().x, 0.1f);
		Assert.assertEquals(160f, point.toPoint().y, 0.1f);

		final TiledObject rectangle = group.getObjectByName("rectangle");
		Assert.assertEquals(TiledObjectShape.RECTANGLE, rectangle.getObjectShape());
		Assert.assertEquals(32f, rectangle.toRectangle().getX(), 0.1f);
		Assert.assertEquals(192f, rectangle.toRectangle().getY(), 0.1f);
		Assert.assertEquals(64f, rectangle.toRectangle().getWidth(), 0.1f);
		Assert.assertEquals(32f, rectangle.toRectangle().getHeight(), 0.1f);

		final TiledObject circle = group.getObjectByName("circle");
		Assert.assertEquals(TiledObjectShape.ELLIPSE, circle.getObjectShape());
		Assert.assertEquals(112f, circle.toCircle().getX(), 0.1f);
		Assert.assertEquals(144f, circle.toCircle().getY(), 0.1f);
		Assert.assertEquals(16f, circle.toCircle().getRadius(), 0.1f);

		final TiledObject polygon = group.getObjectByName("polygon");
		Assert.assertEquals(TiledObjectShape.POLYGON, polygon.getObjectShape());
		final Polygon polygon1 = polygon.toPolygon();
		Assert.assertEquals(128f, polygon1.getX(0), 0.1f);
		Assert.assertEquals(224f, polygon1.getY(0), 0.1f);
		Assert.assertEquals(160f, polygon1.getX(1), 0.1f);
		Assert.assertEquals(160f, polygon1.getY(1), 0.1f);
		Assert.assertEquals(192f, polygon1.getX(2), 0.1f);
		Assert.assertEquals(192f, polygon1.getY(2), 0.1f);

		final TiledObject text = group.getObjectByName("text");
		Assert.assertEquals(TiledObjectShape.TEXT, text.getObjectShape());
		Assert.assertEquals("Hello World", text.getText());
		Assert.assertEquals(true, text.isWrapText());
	}

	@Test
	public void testGetObjectGroups() {
		Assert.assertEquals(2, tiledMap.getTotalObjectGroups());

		TiledObjectGroup group = tiledMap.getObjectGroup("Objects");
		Assert.assertEquals("Objects", group.getName());
		Assert.assertEquals(3, group.getObjects().size);

		for(int i = 0; i < group.getObjects().size; i++) {
			TiledObject obj = group.getObjects().get(i);
			switch(obj.getName()) {
			case "test1":
				Assert.assertEquals(true, obj.containsProperty("testProperty"));
				Assert.assertEquals("SUCCESS", obj.getProperty("testProperty"));
				Assert.assertEquals(32f, obj.getX());
				Assert.assertEquals(64f, obj.getY());
				Assert.assertEquals(16f, obj.getWidth());
				Assert.assertEquals(24f, obj.getHeight());
				break;
			case "test2":
				Assert.assertEquals(true, obj.containsProperty("testProperty"));
				Assert.assertEquals("SUCCESS2", obj.getProperty("testProperty"));
				Assert.assertEquals(260f, obj.getX());
				Assert.assertEquals(192f, obj.getY());
				Assert.assertEquals(24f, obj.getWidth());
				Assert.assertEquals(16f, obj.getHeight());
				break;
			case "flipped_tile_object":
				Assert.assertEquals(288f, obj.getX());
				Assert.assertEquals(96f, obj.getY());
				Assert.assertEquals(32f, obj.getWidth());
				Assert.assertEquals(32f, obj.getHeight());
				Assert.assertEquals(156, obj.getGid());
				Assert.assertEquals(false, obj.isGidFlipDiagonally());
				Assert.assertEquals(true, obj.isGidFlipHorizontally());
				Assert.assertEquals(true, obj.isGidFlipVertically());
				break;
			}
		}
	}

	@Test
	public void testParsedIndices() {
		GroupLayer groupLayer = tiledMap.getGroupLayer("Group 1");
		Assert.assertEquals(2, groupLayer.getIndex());

		TiledObjectGroup group = tiledMap.getObjectGroup("Objects");
		Assert.assertEquals(4, group.getIndex());
		Assert.assertEquals(group.getIndex(), tiledMap.getLayerIndex(group.getName()));
		
		TileLayer tileLayer = tiledMap.getTileLayer("Higher");
		Assert.assertEquals(6, tileLayer.getIndex());
	}
}
