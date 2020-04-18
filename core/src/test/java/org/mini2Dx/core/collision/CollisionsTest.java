/*******************************************************************************
 * Copyright 2020 See AUTHORS file
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
package org.mini2Dx.core.collision;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mini2Dx.core.JvmLocks;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.collision.util.*;
import org.mini2Dx.gdx.math.Vector2;

public class CollisionsTest {
	private static final int DEFAULT_POOL_SIZE = 1;

	private final Collisions collisions = new Collisions();

	@BeforeClass
	public static void beforeClass() {
		Mdx.locks = new JvmLocks();
		Collisions.DEFAULT_POOL_SIZE = DEFAULT_POOL_SIZE;
	}

	@Test
	public void testCollisionBox() {
		Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalCollisionBoxesAvailable());

		for(int i = 0; i < 10; i++) {
			final int id = 7 + i;
			final float x = 9.0f * i;
			final float y = 11.0f * i;
			final float width = 123f * i;
			final float height = 132f * i;

			final CollisionBox collisionBox = collisions.collisionBox(id, x, y, width, height);
			Assert.assertEquals(0, collisions.getTotalCollisionBoxesAvailable());

			Assert.assertEquals(id, collisionBox.getId());
			Assert.assertEquals(x, collisionBox.getX(), 0f);
			Assert.assertEquals(y, collisionBox.getY(), 0f);
			Assert.assertEquals(width, collisionBox.getWidth(), 0f);
			Assert.assertEquals(height, collisionBox.getHeight(), 0f);
			Assert.assertEquals(x, collisionBox.getRenderX(), 0f);
			Assert.assertEquals(y, collisionBox.getRenderY(), 0f);
			Assert.assertEquals(width, collisionBox.getRenderWidth(), 0f);
			Assert.assertEquals(height, collisionBox.getRenderHeight(), 0f);

			collisionBox.dispose();
			Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalCollisionBoxesAvailable());
			collisionBox.dispose();
			Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalCollisionBoxesAvailable());
		}
	}

	@Test
	public void testCollisionCircle() {
		Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalCollisionCirclesAvailable());

		for(int i = 0; i < 10; i++) {
			final int id = 45 + i;
			final float x = 53f * i;
			final float y = 11f * i;
			final float radius = 46f * i;

			final CollisionCircle collisionCircle = collisions.collisionCircle(id, x, y, radius);
			Assert.assertEquals(0, collisions.getTotalCollisionCirclesAvailable());

			Assert.assertEquals(id, collisionCircle.getId());
			Assert.assertEquals(x, collisionCircle.getX(), 0f);
			Assert.assertEquals(y, collisionCircle.getY(), 0f);
			Assert.assertEquals(radius, collisionCircle.getRadius(), 0f);
			Assert.assertEquals(x, collisionCircle.getRenderX(), 0f);
			Assert.assertEquals(y, collisionCircle.getRenderY(), 0f);
			Assert.assertEquals(radius, collisionCircle.getRenderRadius(), 0f);

			collisionCircle.dispose();
			Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalCollisionCirclesAvailable());
			collisionCircle.dispose();
			Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalCollisionCirclesAvailable());
		}
	}

	@Test
	public void testCollisionPoint() {
		Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalCollisionPointsAvailable());

		for(int i = 0; i < 10; i++) {
			final int id = 103 + i;
			final float x = 77f * i;
			final float y = 3f * i;

			final CollisionPoint collisionPoint = collisions.collisionPoint(id, x, y);
			Assert.assertEquals(0, collisions.getTotalCollisionPointsAvailable());

			Assert.assertEquals(id, collisionPoint.getId());
			Assert.assertEquals(x, collisionPoint.getX(), 0f);
			Assert.assertEquals(y, collisionPoint.getY(), 0f);

			collisionPoint.dispose();
			Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalCollisionPointsAvailable());
			collisionPoint.dispose();
			Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalCollisionPointsAvailable());
		}
	}

	@Test
	public void testCollisionPolygon() {
		for(int i = 0; i < 10; i++) {
			final int id = 97 + i;
			final Vector2 [] vectors = new Vector2[] {
				new Vector2(10f * i, 10f * i), new Vector2(20f * i, 20f * i),
				new Vector2(i, 20f * i)
			};

			final CollisionPolygon collisionPolygon = collisions.collisionPolygon(id, vectors);
			Assert.assertEquals(0, collisions.getTotalCollisionPolygonsAvailable());

			Assert.assertEquals(id, collisionPolygon.getId());
			Assert.assertEquals(vectors[0].x, collisionPolygon.getX(), 0f);
			Assert.assertEquals(vectors[0].y, collisionPolygon.getY(), 0f);
			Assert.assertEquals(vectors[1].x, collisionPolygon.getX(1), 0f);
			Assert.assertEquals(vectors[1].y, collisionPolygon.getY(1), 0f);
			Assert.assertEquals(vectors[2].x, collisionPolygon.getX(2), 0f);
			Assert.assertEquals(vectors[2].y, collisionPolygon.getY(2), 0f);

			collisionPolygon.dispose();
			Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalCollisionPolygonsAvailable());
			collisionPolygon.dispose();
			Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalCollisionPolygonsAvailable());
		}
	}

	@Test
	public void testQuadTreeAwareCollisionBox() {
		Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalQuadTreeAwareCollisionBoxesAvailable());

		for(int i = 0; i < 10; i++) {
			final int id = 7 + i;
			final float x = 9.0f * i;
			final float y = 11.0f * i;
			final float width = 123f * i;
			final float height = 132f * i;

			final QuadTreeAwareCollisionBox collisionBox = collisions.quadTreeAwareCollisionBox(id, x, y, width, height);
			Assert.assertEquals(0, collisions.getTotalQuadTreeAwareCollisionBoxesAvailable());

			Assert.assertEquals(id, collisionBox.getId());
			Assert.assertEquals(x, collisionBox.getX(), 0f);
			Assert.assertEquals(y, collisionBox.getY(), 0f);
			Assert.assertEquals(width, collisionBox.getWidth(), 0f);
			Assert.assertEquals(height, collisionBox.getHeight(), 0f);
			Assert.assertEquals(x, collisionBox.getRenderX(), 0f);
			Assert.assertEquals(y, collisionBox.getRenderY(), 0f);
			Assert.assertEquals(width, collisionBox.getRenderWidth(), 0f);
			Assert.assertEquals(height, collisionBox.getRenderHeight(), 0f);

			collisionBox.dispose();
			Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalQuadTreeAwareCollisionBoxesAvailable());
			collisionBox.dispose();
			Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalQuadTreeAwareCollisionBoxesAvailable());
		}
	}

	@Test
	public void testQuadTreeAwareCollisionCircle() {
		Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalQuadTreeAwareCollisionCirclesAvailable());

		for(int i = 0; i < 10; i++) {
			final int id = 45 + i;
			final float x = 53f * i;
			final float y = 11f * i;
			final float radius = 46f * i;

			final QuadTreeAwareCollisionCircle collisionCircle = collisions.quadTreeAwareCollisionCircle(id, x, y, radius);
			Assert.assertEquals(0, collisions.getTotalQuadTreeAwareCollisionCirclesAvailable());

			Assert.assertEquals(id, collisionCircle.getId());
			Assert.assertEquals(x, collisionCircle.getX(), 0f);
			Assert.assertEquals(y, collisionCircle.getY(), 0f);
			Assert.assertEquals(radius, collisionCircle.getRadius(), 0f);
			Assert.assertEquals(x, collisionCircle.getRenderX(), 0f);
			Assert.assertEquals(y, collisionCircle.getRenderY(), 0f);
			Assert.assertEquals(radius, collisionCircle.getRenderRadius(), 0f);

			collisionCircle.dispose();
			Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalQuadTreeAwareCollisionCirclesAvailable());
			collisionCircle.dispose();
			Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalQuadTreeAwareCollisionCirclesAvailable());
		}
	}

	@Test
	public void testQuadTreeAwareCollisionPoint() {
		Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalQuadTreeAwareCollisionPointsAvailable());

		for(int i = 0; i < 10; i++) {
			final int id = 103 + i;
			final float x = 77f * i;
			final float y = 3f * i;

			final CollisionPoint collisionPoint = collisions.quadTreeAwareCollisionPoint(id, x, y);
			Assert.assertEquals(0, collisions.getTotalQuadTreeAwareCollisionPointsAvailable());

			Assert.assertEquals(id, collisionPoint.getId());
			Assert.assertEquals(x, collisionPoint.getX(), 0f);
			Assert.assertEquals(y, collisionPoint.getY(), 0f);

			collisionPoint.dispose();
			Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalQuadTreeAwareCollisionPointsAvailable());
			collisionPoint.dispose();
			Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalQuadTreeAwareCollisionPointsAvailable());
		}
	}

	@Test
	public void testQuadTreeAwareCollisionPolygon() {
		for(int i = 0; i < 10; i++) {
			final int id = 97 + i;
			final Vector2 [] vectors = new Vector2[] {
					new Vector2(10f * i, 10f * i), new Vector2(20f * i, 20f * i),
					new Vector2(i, 20f * i)
			};

			final CollisionPolygon collisionPolygon = collisions.quadTreeAwareCollisionPolygon(id, vectors);
			Assert.assertEquals(0, collisions.getTotalQuadTreeAwareCollisionPolygonsAvailable());

			Assert.assertEquals(id, collisionPolygon.getId());
			Assert.assertEquals(vectors[0].x, collisionPolygon.getX(), 0f);
			Assert.assertEquals(vectors[0].y, collisionPolygon.getY(), 0f);
			Assert.assertEquals(vectors[1].x, collisionPolygon.getX(1), 0f);
			Assert.assertEquals(vectors[1].y, collisionPolygon.getY(1), 0f);
			Assert.assertEquals(vectors[2].x, collisionPolygon.getX(2), 0f);
			Assert.assertEquals(vectors[2].y, collisionPolygon.getY(2), 0f);

			collisionPolygon.dispose();
			Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalQuadTreeAwareCollisionPolygonsAvailable());
			collisionPolygon.dispose();
			Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalQuadTreeAwareCollisionPolygonsAvailable());
		}
	}

	@Test
	public void testStaticCollisionBox() {
		Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalStaticCollisionBoxesAvailable());

		for(int i = 0; i < 10; i++) {
			final int id = 7 + i;
			final float x = 9.0f * i;
			final float y = 11.0f * i;
			final float width = 123f * i;
			final float height = 132f * i;

			final StaticCollisionBox collisionBox = collisions.staticCollisionBox(id, x, y, width, height);
			Assert.assertEquals(0, collisions.getTotalStaticCollisionBoxesAvailable());

			Assert.assertEquals(id, collisionBox.getId());
			Assert.assertEquals(x, collisionBox.getX(), 0f);
			Assert.assertEquals(y, collisionBox.getY(), 0f);
			Assert.assertEquals(width, collisionBox.getWidth(), 0f);
			Assert.assertEquals(height, collisionBox.getHeight(), 0f);
			Assert.assertEquals(x, collisionBox.getRenderX(), 0f);
			Assert.assertEquals(y, collisionBox.getRenderY(), 0f);
			Assert.assertEquals(width, collisionBox.getRenderWidth(), 0f);
			Assert.assertEquals(height, collisionBox.getRenderHeight(), 0f);

			collisionBox.dispose();
			Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalStaticCollisionBoxesAvailable());
			collisionBox.dispose();
			Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalStaticCollisionBoxesAvailable());
		}
	}

	@Test
	public void testStaticCollisionCircle() {
		Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalStaticCollisionCirclesAvailable());

		for(int i = 0; i < 10; i++) {
			final int id = 45 + i;
			final float x = 53f * i;
			final float y = 11f * i;
			final float radius = 46f * i;

			final StaticCollisionCircle collisionCircle = collisions.staticCollisionCircle(id, x, y, radius);
			Assert.assertEquals(0, collisions.getTotalStaticCollisionCirclesAvailable());

			Assert.assertEquals(id, collisionCircle.getId());
			Assert.assertEquals(x, collisionCircle.getX(), 0f);
			Assert.assertEquals(y, collisionCircle.getY(), 0f);
			Assert.assertEquals(radius, collisionCircle.getRadius(), 0f);
			Assert.assertEquals(x, collisionCircle.getRenderX(), 0f);
			Assert.assertEquals(y, collisionCircle.getRenderY(), 0f);
			Assert.assertEquals(radius, collisionCircle.getRenderRadius(), 0f);

			collisionCircle.dispose();
			Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalStaticCollisionCirclesAvailable());
			collisionCircle.dispose();
			Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalStaticCollisionCirclesAvailable());
		}
	}

	@Test
	public void testStaticCollisionPoint() {
		Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalStaticCollisionPointsAvailable());

		for(int i = 0; i < 10; i++) {
			final int id = 103 + i;
			final float x = 77f * i;
			final float y = 3f * i;

			final StaticCollisionPoint collisionPoint = collisions.staticCollisionPoint(id, x, y);
			Assert.assertEquals(0, collisions.getTotalStaticCollisionPointsAvailable());

			Assert.assertEquals(id, collisionPoint.getId());
			Assert.assertEquals(x, collisionPoint.getX(), 0f);
			Assert.assertEquals(y, collisionPoint.getY(), 0f);

			collisionPoint.dispose();
			Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalStaticCollisionPointsAvailable());
			collisionPoint.dispose();
			Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalStaticCollisionPointsAvailable());
		}
	}

	@Test
	public void testStaticCollisionPolygon() {
		for(int i = 0; i < 10; i++) {
			final int id = 97 + i;
			final Vector2 [] vectors = new Vector2[] {
					new Vector2(10f * i, 10f * i), new Vector2(20f * i, 20f * i),
					new Vector2(i, 20f * i)
			};

			final StaticCollisionPolygon collisionPolygon = collisions.staticCollisionPolygon(id, vectors);
			Assert.assertEquals(0, collisions.getTotalStaticCollisionPolygonsAvailable());

			Assert.assertEquals(id, collisionPolygon.getId());
			Assert.assertEquals(vectors[0].x, collisionPolygon.getX(), 0f);
			Assert.assertEquals(vectors[0].y, collisionPolygon.getY(), 0f);
			Assert.assertEquals(vectors[1].x, collisionPolygon.getX(1), 0f);
			Assert.assertEquals(vectors[1].y, collisionPolygon.getY(1), 0f);
			Assert.assertEquals(vectors[2].x, collisionPolygon.getX(2), 0f);
			Assert.assertEquals(vectors[2].y, collisionPolygon.getY(2), 0f);

			collisionPolygon.dispose();
			Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalStaticCollisionPolygonsAvailable());
			collisionPolygon.dispose();
			Assert.assertEquals(DEFAULT_POOL_SIZE, collisions.getTotalStaticCollisionPolygonsAvailable());
		}
	}
}
