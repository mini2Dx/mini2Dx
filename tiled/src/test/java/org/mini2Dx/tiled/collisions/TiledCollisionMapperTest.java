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
package org.mini2Dx.tiled.collisions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.collision.CollisionBox;
import org.mini2Dx.core.collision.PointQuadTree;
import org.mini2Dx.core.collision.QuadTree;
import org.mini2Dx.core.collision.RegionQuadTree;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.libgdx.LibgdxFiles;
import org.mini2Dx.libgdx.LibgdxGraphicsUtils;
import org.mini2Dx.libgdx.LibgdxPlatformUtils;
import org.mini2Dx.lockprovider.jvm.JvmLocks;
import org.mini2Dx.tiled.TiledMap;
import org.mini2Dx.tiled.collisions.merger.AllCollisionMerger;
import org.mini2Dx.tiled.exception.TiledException;

/**
 * Unit tests for {@link TiledCollisionMapper}
 */
public class TiledCollisionMapperTest {
	private static TiledMap tiledMap;

	private TiledCollisionMapper<CollisionBox> collisionBoxMapper;

	@BeforeClass
	public static void loadMap() throws TiledException {
		Gdx.files = new LwjglFiles();
		Mdx.files = new LibgdxFiles();
		Mdx.graphics = new LibgdxGraphicsUtils();
		Mdx.locks = new JvmLocks();
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

	@Before
	public void setUp() {
		collisionBoxMapper = new TiledCollisionMapper<>(new TiledCollisionBoxFactory());
	}

	@Test
	public void testMapCollisionsByLayer() {
		int collisionLayerIndex = tiledMap.getLayerIndex("Collisions");
		QuadTree<CollisionBox> quadTree = new PointQuadTree<CollisionBox>(8, 0f, 0f,
				tiledMap.getWidth() * tiledMap.getTileWidth(), tiledMap.getHeight() * tiledMap.getTileHeight());
		Rectangle area = new Rectangle(0f, 0f, tiledMap.getWidth() * tiledMap.getTileWidth(),
				tiledMap.getHeight() * tiledMap.getTileHeight());

		collisionBoxMapper.mapCollisionsByLayer(quadTree, tiledMap, collisionLayerIndex);

		Array<CollisionBox> collisions = quadTree.getElementsWithinArea(area);
		Assert.assertEquals(7, collisions.size);

		assertCollisionAt(6, 3, quadTree, tiledMap);
		assertCollisionAt(5, 4, quadTree, tiledMap);
		assertCollisionAt(6, 4, quadTree, tiledMap);
		assertCollisionAt(1, 5, quadTree, tiledMap);
		assertCollisionAt(2, 5, quadTree, tiledMap);
		assertCollisionAt(1, 6, quadTree, tiledMap);
		assertCollisionAt(2, 6, quadTree, tiledMap);
	}

	@Test
	public void testMapCollisionsByNonExistingLayer() {
		Array<CollisionBox> result = new Array<CollisionBox>();
		collisionBoxMapper.mapCollisionsByLayer(result, tiledMap, "NonExistingLayer");
		Assert.assertEquals(0, result.size);
	}

	@Test
	public void testMapCollisionsByNonExistantObjectGroup() {
		Array<CollisionBox> result = new Array<CollisionBox>();
		collisionBoxMapper.mapCollisionsByObjectGroup(result, tiledMap, "NonExistingObjectGroup");
		Assert.assertEquals(0, result.size);
	}

	@Test
	public void testMapCollisionsByObjectGroup() {
		QuadTree<CollisionBox> quadTree = new PointQuadTree<CollisionBox>(8, 0f, 0f,
				tiledMap.getWidth() * tiledMap.getTileWidth(), tiledMap.getHeight() * tiledMap.getTileHeight());
		collisionBoxMapper.mapCollisionsByObjectGroup(quadTree, tiledMap, "Objects");
		Rectangle area = new Rectangle(0f, 0f, tiledMap.getPixelWidth(), tiledMap.getPixelHeight());

		Array<CollisionBox> collisions = quadTree.getElementsWithinArea(area);
		Assert.assertEquals(3, collisions.size);
		Assert.assertEquals(288f, collisions.get(0).getX());
		Assert.assertEquals(96f, collisions.get(0).getY());
		Assert.assertEquals(32f, collisions.get(0).getWidth());
		Assert.assertEquals(32f, collisions.get(0).getHeight());
	}

	@Test
	public void testMapCollisionsByObjectGroupAndType() {
		QuadTree<CollisionBox> quadTree = new PointQuadTree<CollisionBox>(8, 0f, 0f,
				tiledMap.getWidth() * tiledMap.getTileWidth(), tiledMap.getHeight() * tiledMap.getTileHeight());
		collisionBoxMapper.mapCollisionsByObjectGroup(quadTree, tiledMap, "Objects", "expected");
		Rectangle area = new Rectangle(0f, 0f, tiledMap.getWidth() * tiledMap.getTileWidth(),
				tiledMap.getHeight() * tiledMap.getTileHeight());

		Array<CollisionBox> collisions = quadTree.getElementsWithinArea(area);
		Assert.assertEquals(1, collisions.size);
		Assert.assertEquals(32f, collisions.get(0).getX());
		Assert.assertEquals(64f, collisions.get(0).getY());
		Assert.assertEquals(16f, collisions.get(0).getWidth());
		Assert.assertEquals(24f, collisions.get(0).getHeight());
	}

	@Test
	public void testMapAndMergeCollisionsByLayer() {
		int collisionLayerIndex = tiledMap.getLayerIndex("Collisions");
		RegionQuadTree<CollisionBox> quadTree = new RegionQuadTree<CollisionBox>(8, 0f, 0f,
				tiledMap.getWidth() * tiledMap.getTileWidth(), tiledMap.getHeight() * tiledMap.getTileHeight());

		collisionBoxMapper.mapAndMergeCollisionsByLayer(quadTree, tiledMap, collisionLayerIndex);

		Array<CollisionBox> collisions = quadTree.getElementsWithinArea(quadTree);
		Assert.assertEquals(4, collisions.size);

		CollisionBox collision = getCollisionAt(1, 5, quadTree, tiledMap);
		Assert.assertEquals(tiledMap.getTileWidth() * 2f, collision.getWidth());
		Assert.assertEquals(tiledMap.getTileHeight() * 2f, collision.getHeight());

		collision = getCollisionAt(5, 4, quadTree, tiledMap);
		Assert.assertEquals(tiledMap.getTileWidth() * 1f, collision.getWidth());
		Assert.assertEquals(tiledMap.getTileHeight() * 1f, collision.getHeight());

		collision = getCollisionAt(6, 3, quadTree, tiledMap);
		Assert.assertEquals(tiledMap.getTileWidth() * 1f, collision.getWidth());
		Assert.assertEquals(tiledMap.getTileHeight() * 1f, collision.getHeight());
	}

	@Test
	public void testMapCollisionsByLayerIndexToByteArray() {
		int collisionLayerIndex = tiledMap.getLayerIndex("Collisions");
		byte[][] result = TiledCollisionMapper.mapCollisionsByLayer(tiledMap, collisionLayerIndex);

		byte[][] expected = new byte[][] { new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				new byte[] { 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 }, new byte[] { 0, 0, 0, 0, 0, 1, 1, 0, 0, 0 },
				new byte[] { 0, 1, 1, 0, 0, 0, 0, 0, 0, 0 }, new byte[] { 0, 1, 1, 0, 0, 0, 0, 0, 0, 0 },
				new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

		for (int x = 0; x < tiledMap.getWidth(); x++) {
			for (int y = 0; y < tiledMap.getHeight(); y++) {
				if (expected[y][x] != result[x][y]) {
					Assert.fail(x + "," + y + " expected: " + expected[x][y] + ", actual: " + result[x][y]);
				}
			}
		}
	}

	@Test
	public void testMapCollisionsByLayerNameToByteArray() {
		int collisionLayerIndex = tiledMap.getLayerIndex("Collisions");
		byte[][] result = TiledCollisionMapper.mapCollisionsByLayer(tiledMap, collisionLayerIndex);

		byte[][] expected = new byte[][] { new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				new byte[] { 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 }, new byte[] { 0, 0, 0, 0, 0, 1, 1, 0, 0, 0 },
				new byte[] { 0, 1, 1, 0, 0, 0, 0, 0, 0, 0 }, new byte[] { 0, 1, 1, 0, 0, 0, 0, 0, 0, 0 },
				new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

		for (int x = 0; x < tiledMap.getWidth(); x++) {
			for (int y = 0; y < tiledMap.getHeight(); y++) {
				if (expected[y][x] != result[x][y]) {
					Assert.fail(x + "," + y + " expected: " + expected[x][y] + ", actual: " + result[x][y]);
				}
			}
		}
	}

	@Test
	public void testMapEmptySpacesByLayerIndexToByteArray() {
		int collisionLayerIndex = tiledMap.getLayerIndex("Collisions");
		byte[][] result = TiledCollisionMapper.mapEmptySpacesByLayer(tiledMap, collisionLayerIndex);

		byte[][] expected = new byte[][] { new byte[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				new byte[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, new byte[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				new byte[] { 1, 1, 1, 1, 1, 1, 0, 1, 1, 1 }, new byte[] { 1, 1, 1, 1, 1, 0, 0, 1, 1, 1 },
				new byte[] { 1, 0, 0, 1, 1, 1, 1, 1, 1, 1 }, new byte[] { 1, 0, 0, 1, 1, 1, 1, 1, 1, 1 },
				new byte[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };

		for (int x = 0; x < tiledMap.getWidth(); x++) {
			for (int y = 0; y < tiledMap.getHeight(); y++) {
				if (expected[y][x] != result[x][y]) {
					Assert.fail(x + "," + y + " expected: " + expected[x][y] + ", actual: " + result[x][y]);
				}
			}
		}
	}

	@Test
	public void testMapAndMergeEmptySpacesByLayer() {
		int collisionLayerIndex = tiledMap.getLayerIndex("Collisions");
		RegionQuadTree<CollisionBox> quadTree = new RegionQuadTree<CollisionBox>(8, 0f, 0f,
				tiledMap.getWidth() * tiledMap.getTileWidth(), tiledMap.getHeight() * tiledMap.getTileHeight());

		collisionBoxMapper = new TiledCollisionMapper<>(new TiledCollisionBoxFactory(), new AllCollisionMerger());
		collisionBoxMapper.mapAndMergeEmptySpacesByLayer(quadTree, tiledMap, collisionLayerIndex);

		Array<CollisionBox> collisions = quadTree.getElementsWithinArea(quadTree);
		Assert.assertEquals(19, collisions.size);
	}

	private void assertCollisionAt(int tileX, int tileY, QuadTree<?> quadTree, TiledMap tiledMap) {
		Array<?> collisions = quadTree.getElementsWithinArea(new Rectangle(tileX * tiledMap.getTileWidth(),
				tileY * tiledMap.getTileHeight(), tiledMap.getTileWidth() - 1f, tiledMap.getTileHeight() - 1f));
		Assert.assertEquals(1, collisions.size);
	}

	private CollisionBox getCollisionAt(int tileX, int tileY, RegionQuadTree<CollisionBox> quadTree,
			TiledMap tiledMap) {
		Array<CollisionBox> collisions = quadTree.getElementsWithinArea(new Rectangle(tileX * tiledMap.getTileWidth(),
				tileY * tiledMap.getTileHeight(), tiledMap.getTileWidth() - 1f, tiledMap.getTileHeight() - 1f));
		Assert.assertEquals(1, collisions.size);
		return collisions.get(0);
	}
}
