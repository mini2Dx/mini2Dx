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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.geom.Polygon;
import org.mini2Dx.libgdx.LibgdxFiles;
import org.mini2Dx.libgdx.LibgdxGraphicsUtils;
import org.mini2Dx.libgdx.LibgdxPlatformUtils;
import org.mini2Dx.tiled.exception.TiledException;

/**
 * Unit tests for {@link TiledMap}s
 */
public class TiledMapTest {
	private static TiledMap tiledMap;

	@BeforeClass
	public static void loadMap() throws TiledException {
		Gdx.files = new LwjglFiles();
		Mdx.files = new LibgdxFiles();
		Mdx.graphics = new LibgdxGraphicsUtils();
		Mdx.platformUtils = new LibgdxPlatformUtils() {
			@Override
			public boolean isGameThread() {
				return false;
			}
		};

		FileHandle file = Mdx.files.internal(Thread.currentThread().getContextClassLoader()
				.getResource("orthogonal.tmx").getFile().replaceAll("%20", " "));
		tiledMap = new TiledMap(file, false);
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
			Assert.assertEquals(32, tiledMap.getTilesets().get(i).getTileWidth());
		}
	}

	@Test
	public void testGetTileHeight() {
		Assert.assertEquals(32, tiledMap.getTileHeight());

		for (int i = 0; i < tiledMap.getTilesets().size; i++) {
			Assert.assertEquals(32, tiledMap.getTilesets().get(i).getTileHeight());
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
		Assert.assertEquals(1, tiledMap.getTileLayer("Collisions").getIndex());

		Assert.assertEquals(true, tiledMap.getTileLayer("Higher") != null);
		Assert.assertEquals(4, tiledMap.getTileLayer("Higher").getIndex());
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

		for (int i = 0; i < group.getObjects().size; i++) {
			TiledObject obj = group.getObjects().get(i);
			switch (obj.getName()) {
			case "test1":
				Assert.assertEquals(true, obj.containsProperty("testProperty"));
				Assert.assertEquals("SUCCESS", obj.getProperty("testProperty"));
				Assert.assertEquals(32f, obj.getX(), 0f);
				Assert.assertEquals(64f, obj.getY(), 0f);
				Assert.assertEquals(16f, obj.getWidth(), 0f);
				Assert.assertEquals(24f, obj.getHeight(), 0f);
				break;
			case "test2":
				Assert.assertEquals(true, obj.containsProperty("testProperty"));
				Assert.assertEquals("SUCCESS2", obj.getProperty("testProperty"));
				Assert.assertEquals(260f, obj.getX(), 0f);
				Assert.assertEquals(192f, obj.getY(), 0f);
				Assert.assertEquals(24f, obj.getWidth(), 0f);
				Assert.assertEquals(16f, obj.getHeight(), 0f);
				break;
			case "flipped_tile_object":
				Assert.assertEquals(288f, obj.getX(), 0f);
				Assert.assertEquals(96f, obj.getY(), 0f);
				Assert.assertEquals(32f, obj.getWidth(), 0f);
				Assert.assertEquals(32f, obj.getHeight(), 0f);
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
		Assert.assertEquals(0, groupLayer.getIndex());

		TiledObjectGroup group = tiledMap.getObjectGroup("Objects");
		Assert.assertEquals(2, group.getIndex());
		Assert.assertEquals(group.getIndex(), tiledMap.getLayerIndex(group.getName()));

		TileLayer tileLayer = tiledMap.getTileLayer("Higher");
		Assert.assertEquals(4, tileLayer.getIndex());
	}

	@Test
	public void testGroupLayers() {
		GroupLayer groupLayer = tiledMap.getGroupLayer("Group 1");
		Assert.assertEquals(0, groupLayer.getIndex());
		Assert.assertEquals(2, groupLayer.getLayers().size);

		TileLayer animatedLayer = tiledMap.getTileLayer("Animated");
		Assert.assertEquals(1, animatedLayer.getIndex());

		TileLayer tileLayer = tiledMap.getTileLayer("Ground");
		Assert.assertEquals(0, tileLayer.getIndex());
	}
}
