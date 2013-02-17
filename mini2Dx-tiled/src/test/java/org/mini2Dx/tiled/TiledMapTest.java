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
