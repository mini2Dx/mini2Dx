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
package org.mini2Dx.core.collision;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.util.InterpolationTracker;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.math.RandomXS128;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.lockprovider.jvm.JvmLocks;

import java.util.Random;

/**
 * Unit tests for {@link PointQuadTree}
 */
public class PointQuadTreeTest {

	private PointQuadTree<CollisionPoint> quadTree;
	private CollisionPoint point1, point2, point3, point4;

	@Before
	public void setup() {
		InterpolationTracker.deregisterAll();
		Mdx.locks = new JvmLocks();

		Mdx.graphics = null;

		quadTree = new PointQuadTree<CollisionPoint>(2, 0, 0, 128, 128);
		point1 = new CollisionPoint(0, 0);
		point2 = new CollisionPoint(127, 0);
		point3 = new CollisionPoint(0, 127);
		point4 = new CollisionPoint(127, 127);
	}
	
	@Test
	public void testAdd() {
		RandomXS128 random = new RandomXS128();
		random.setState(7111708194453062212L, -495964951339506455L);

		float minX = 1000f;
		float minY = 1000f;
		float maxX = -1000f;
		float maxY = -1000f;

		for(int i = 0; i < 100; i++) {
			final CollisionPoint point = new CollisionPoint(random.nextInt(128), random.nextInt(128));
			minX = Math.min(point.getX(), minX);
			minY = Math.min(point.getY(), minY);
			maxX = Math.max(point.getX(), maxX);
			maxY = Math.max(point.getY(), maxY);
			Assert.assertTrue(quadTree.add(point));
			Assert.assertEquals(i + 1, quadTree.getElements().size);

			Assert.assertEquals(minX, quadTree.rootQuad.elementBounds.x, 1f);
			Assert.assertEquals(minY, quadTree.rootQuad.elementBounds.y, 1f);
			Assert.assertEquals(maxX, quadTree.rootQuad.elementBounds.maxX, 1f);
			Assert.assertEquals(maxY, quadTree.rootQuad.elementBounds.maxY, 1f);
		}
	}
	
	@Test
	public void testAddAll() {
		RandomXS128 random = new RandomXS128();
		random.setState(7111708194453062212L, -495964951339506455L);

		Array<CollisionPoint> points = new Array<CollisionPoint>();
		float minX = 1000f;
		float minY = 1000f;
		float maxX = -1000f;
		float maxY = -1000f;
		for(int i = 0; i < 100; i++) {
			final CollisionPoint point = new CollisionPoint(random.nextInt(128), random.nextInt(128));
			minX = Math.min(point.getX(), minX);
			minY = Math.min(point.getY(), minY);
			maxX = Math.max(point.getX(), maxX);
			maxY = Math.max(point.getY(), maxY);
			points.add(point);
		}
		quadTree.addAll(points);
		Assert.assertEquals(points.size, quadTree.getTotalElements());
		Assert.assertEquals(minX, quadTree.rootQuad.elementBounds.x, MathUtils.FLOAT_ROUNDING_ERROR);
		Assert.assertEquals(minY, quadTree.rootQuad.elementBounds.y, MathUtils.FLOAT_ROUNDING_ERROR);
		Assert.assertEquals(maxX, quadTree.rootQuad.elementBounds.maxX, MathUtils.FLOAT_ROUNDING_ERROR);
		Assert.assertEquals(maxY, quadTree.rootQuad.elementBounds.maxY, MathUtils.FLOAT_ROUNDING_ERROR);
	}
	
	@Test
	public void testRemove() {
		Random random = new Random();
		Array<CollisionPoint> collisionPoints = new Array<CollisionPoint>();
		for(int i = 0; i < 1000; i++) {
			collisionPoints.add(new CollisionPoint(random.nextInt(128), random.nextInt(128)));
		}
		
		for(int i = 0; i < collisionPoints.size; i++) {
			Assert.assertEquals(true, quadTree.add(collisionPoints.get(i)));
			Assert.assertEquals(i + 1, quadTree.getElements().size);
		}
		
		for(int i = collisionPoints.size - 1; i >= 0 ; i--) {
			Assert.assertEquals(i + 1, quadTree.getElements().size);
			quadTree.remove(collisionPoints.get(i));
			Assert.assertEquals(i, quadTree.getElements().size);
		}
	}
	
	@Test
	public void testRemoveAll() {
		Random random = new Random();
		Array<CollisionPoint> points = new Array<CollisionPoint>();
		for(int i = 0; i < 100; i++) {
			points.add(new CollisionPoint(random.nextInt(128), random.nextInt(128)));
		}
		quadTree.addAll(points);
		Assert.assertEquals(points.size, quadTree.getTotalElements());
		quadTree.removeAll(points);
		Assert.assertEquals(0, quadTree.getTotalElements());
	}

	@Test
	public void testClear() {
		Random random = new Random();
		Array<CollisionPoint> points = new Array<CollisionPoint>();
		for(int i = 0; i < 100; i++) {
			points.add(new CollisionPoint(random.nextInt(128), random.nextInt(128)));
		}
		quadTree.addAll(points);
		Assert.assertEquals(points.size, quadTree.getTotalElements());
		quadTree.clear();
		Assert.assertEquals(0, quadTree.getTotalElements());
	}
	
	@Test
	public void testSubdivide() {
		quadTree.add(point1);
		Assert.assertEquals(1, quadTree.getElements().size);
		Assert.assertEquals(1, quadTree.getTotalQuads());
		quadTree.add(point2);
		Assert.assertEquals(2, quadTree.getElements().size);
		Assert.assertEquals(1, quadTree.getTotalQuads());

		//Subdivide now
		quadTree.add(point3);
		for(int i = 0; i < quadTree.quads.totalItems; i++) {
			final Quad quad = quadTree.quads.get(i);
			Assert.assertNotEquals(-1, quad.index);
		}
		Assert.assertEquals(3, quadTree.getElements().size);
		Assert.assertEquals(5, quadTree.getTotalQuads());

		quadTree.add(point4);
		Assert.assertEquals(4, quadTree.getElements().size);
		Assert.assertEquals(5, quadTree.getTotalQuads());

		quadTree.add(new CollisionPoint(32, 32));
		Assert.assertEquals(5, quadTree.getElements().size);
		Assert.assertEquals(5, quadTree.getTotalQuads());

		//Subdivide now
		quadTree.add(new CollisionPoint(48, 48));
		for(int i = 0; i < quadTree.quads.totalItems; i++) {
			final Quad quad = quadTree.quads.get(i);
			Assert.assertNotEquals(-1, quad.index);
		}
		Assert.assertEquals(6, quadTree.getElements().size);
		Assert.assertEquals(9, quadTree.getTotalQuads());

		quadTree.add(new CollisionPoint(96, 96));
		Assert.assertEquals(7, quadTree.getElements().size);
		Assert.assertEquals(9, quadTree.getTotalQuads());

		//Subdivide now
		quadTree.add(new CollisionPoint(97, 97));
		Assert.assertEquals(8, quadTree.getElements().size);
		Assert.assertEquals(13, quadTree.getTotalQuads());
	}
	
	@Test
	public void testGetTotalElements() {
		Assert.assertTrue(quadTree.add(point1));
		Assert.assertEquals(1, quadTree.getTotalElements());
		Assert.assertTrue(quadTree.add(point2));
		Assert.assertEquals(2, quadTree.getTotalElements());
		Assert.assertTrue(quadTree.add(point3));
		Assert.assertEquals(3, quadTree.getTotalElements());
		Assert.assertTrue(quadTree.remove(point2));
		Assert.assertEquals(2, quadTree.getTotalElements());
		Assert.assertTrue(quadTree.add(point4));
		Assert.assertEquals(3, quadTree.getTotalElements());
		Assert.assertTrue(quadTree.add(point2));
		Assert.assertEquals(4, quadTree.getTotalElements());
		Assert.assertTrue(quadTree.add(new CollisionPoint(32, 32)));
		Assert.assertEquals(5, quadTree.getTotalElements());
		Assert.assertTrue(quadTree.add(new CollisionPoint(48, 48)));
		Assert.assertEquals(6, quadTree.getTotalElements());
	}

	@Test
	public void testGetElementsOverlappingArea() {
		quadTree.add(point1);
		quadTree.add(point2);
		quadTree.add(point3);
		quadTree.add(point4);

		Array<CollisionPoint> collisionPoints = quadTree.getElementsOverlappingArea(new Rectangle(0, 0, 64, 64));
		Assert.assertEquals(1, collisionPoints.size);
		Assert.assertEquals(point1, collisionPoints.get(0));

		collisionPoints = quadTree.getElementsOverlappingArea(new Rectangle(64, 0, 64, 64));
		Assert.assertEquals(1, collisionPoints.size);
		Assert.assertEquals(point2, collisionPoints.get(0));

		collisionPoints = quadTree.getElementsOverlappingArea(new Rectangle(0, 64, 64, 64));
		Assert.assertEquals(1, collisionPoints.size);
		Assert.assertEquals(point3, collisionPoints.get(0));

		collisionPoints = quadTree.getElementsOverlappingArea(new Rectangle(64, 64, 64, 64));
		Assert.assertEquals(1, collisionPoints.size);
		Assert.assertEquals(point4, collisionPoints.get(0));

		CollisionPoint collisionPoint5 = new CollisionPoint(32, 32);
		CollisionPoint collisionPoint6 = new CollisionPoint(48, 48);
		quadTree.add(collisionPoint5);
		quadTree.add(collisionPoint6);

		collisionPoints = quadTree.getElementsOverlappingArea(new Rectangle(0, 0, 64, 64));
		Assert.assertEquals(3, collisionPoints.size);
		Assert.assertEquals(true, collisionPoints.contains(point1, false));
		Assert.assertEquals(true, collisionPoints.contains(collisionPoint5, false));
		Assert.assertEquals(true, collisionPoints.contains(collisionPoint6, false));
	}

	@Test
	public void testGetElementsOverlappingAreaIgnoringEdges() {
		quadTree.add(point1);
		quadTree.add(point2);
		quadTree.add(point3);
		quadTree.add(point4);

		Array<CollisionPoint> collisionPoints = quadTree.getElementsOverlappingAreaIgnoringEdges(new Rectangle(0, 0, 64, 64));
		Assert.assertEquals(1, collisionPoints.size);
		Assert.assertEquals(point1, collisionPoints.get(0));

		collisionPoints = quadTree.getElementsOverlappingAreaIgnoringEdges(new Rectangle(64, 0, 64, 64));
		Assert.assertEquals(1, collisionPoints.size);
		Assert.assertEquals(point2, collisionPoints.get(0));

		collisionPoints = quadTree.getElementsOverlappingAreaIgnoringEdges(new Rectangle(0, 64, 64, 64));
		Assert.assertEquals(1, collisionPoints.size);
		Assert.assertEquals(point3, collisionPoints.get(0));

		collisionPoints = quadTree.getElementsOverlappingAreaIgnoringEdges(new Rectangle(64, 64, 64, 64));
		Assert.assertEquals(1, collisionPoints.size);
		Assert.assertEquals(point4, collisionPoints.get(0));

		CollisionPoint collisionPoint5 = new CollisionPoint(32, 32);
		CollisionPoint collisionPoint6 = new CollisionPoint(48, 48);
		quadTree.add(collisionPoint5);
		quadTree.add(collisionPoint6);

		collisionPoints = quadTree.getElementsOverlappingAreaIgnoringEdges(new Rectangle(0, 0, 64, 64));
		Assert.assertEquals(3, collisionPoints.size);
		Assert.assertEquals(true, collisionPoints.contains(point1, false));
		Assert.assertEquals(true, collisionPoints.contains(collisionPoint5, false));
		Assert.assertEquals(true, collisionPoints.contains(collisionPoint6, false));
	}

	@Test
	public void testGetElementsIntersectingLineSegment() {
		quadTree.add(point1);
		quadTree.add(point2);
		quadTree.add(point3);
		quadTree.add(point4);

		Array<CollisionPoint> CollisionPoints = quadTree.getElementsIntersectingLineSegment(new LineSegment(0, 0, 128, 128));
		Assert.assertEquals(true, CollisionPoints.contains(point1, false));
		Assert.assertEquals(false, CollisionPoints.contains(point2, false));
		Assert.assertEquals(false, CollisionPoints.contains(point3, false));
		Assert.assertEquals(true, CollisionPoints.contains(point4, false));
	}

	@Test
	public void testMove() {
		quadTree.add(point1);
		quadTree.add(point2);
		quadTree.add(point3);
		quadTree.add(point4);

		final CollisionPoint point = new CollisionPoint(126, 126);
		quadTree.add(point);

		final int notExpected = quadTree.getQuad(point).index;
		point.forceTo(0, 126);
		Assert.assertNotEquals(notExpected, quadTree.getQuad(point).index);
		Assert.assertNotEquals(-1, quadTree.getQuad(point).index);
	}

	@Test
	public void testMove2() {
		quadTree.add(point1);
		quadTree.add(point2);
		quadTree.add(point3);
		quadTree.add(point4);

		for(int i = 0; i < 4; i++) {
			final CollisionPoint point1 = new CollisionPoint(126 - i, 126 - i);
			quadTree.add(point1);
		}

		final CollisionPoint point = new CollisionPoint(100, 100);
		quadTree.add(point);

		final int notExpected = quadTree.getQuad(point).index;
		Assert.assertNotEquals(-1, notExpected);

		point.forceTo(96, 126);
		final int result1 = quadTree.getQuad(point).index;
		Assert.assertNotEquals(notExpected, result1);
		Assert.assertNotEquals(-1, result1);

		point.forceTo(0, 126);
		final int result2 = quadTree.getQuad(point).index;
		Assert.assertNotEquals(notExpected, result1);
		Assert.assertNotEquals(result1, result2);
		Assert.assertNotEquals(-1, result2);
	}
}
