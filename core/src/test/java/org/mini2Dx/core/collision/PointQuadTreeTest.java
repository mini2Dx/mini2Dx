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
import org.mini2Dx.core.collision.util.QuadTreeAwareCollisionPoint;
import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.util.InterpolationTracker;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.lockprovider.jvm.JvmLocks;

import java.util.Random;

/**
 * Unit tests for {@link PointQuadTree}
 */
public class PointQuadTreeTest {

	private PointQuadTree<CollisionPoint> rootQuad;
	private CollisionPoint point1, point2, point3, point4;
	private QuadTreeAwareCollisionPoint qAPoint1, qAPoint2, qAPoint3, qAPoint4;

	@Before
	public void setup() {
		InterpolationTracker.deregisterAll();
		Mdx.locks = new JvmLocks();

		Mdx.graphics = null;

		rootQuad = new PointQuadTree<CollisionPoint>(2, 0, 0, 128, 128);
		point1 = new CollisionPoint(0, 0);
		point2 = new CollisionPoint(127, 0);
		point3 = new CollisionPoint(0, 127);
		point4 = new CollisionPoint(127, 127);
		qAPoint1 = new QuadTreeAwareCollisionPoint(point1);
		qAPoint2 = new QuadTreeAwareCollisionPoint(point2);
		qAPoint3 = new QuadTreeAwareCollisionPoint(point3);
		qAPoint4 = new QuadTreeAwareCollisionPoint(point4);
	}
	
	@Test
	public void testAdd() {
		Random random = new Random();
		for(int i = 0; i < 100; i++) {
			rootQuad.add(new CollisionPoint(random.nextInt(128), random.nextInt(128)));
			Assert.assertEquals(i + 1, rootQuad.getElements().size);
		}
	}
	
	@Test
	public void testAddAll() {
		Random random = new Random();
		Array<CollisionPoint> points = new Array<CollisionPoint>();
		for(int i = 0; i < 100; i++) {
			points.add(new CollisionPoint(random.nextInt(128), random.nextInt(128)));
		}
		rootQuad.addAll(points);
		Assert.assertEquals(points.size, rootQuad.getTotalElements());
	}
	
	@Test
	public void testRemove() {
		Random random = new Random();
		Array<CollisionPoint> collisionPoints = new Array<CollisionPoint>();
		for(int i = 0; i < 1000; i++) {
			collisionPoints.add(new CollisionPoint(random.nextInt(128), random.nextInt(128)));
		}
		
		for(int i = 0; i < collisionPoints.size; i++) {
			Assert.assertEquals(true, rootQuad.add(collisionPoints.get(i)));
			Assert.assertEquals(i + 1, rootQuad.getElements().size);
		}
		
		for(int i = collisionPoints.size - 1; i >= 0 ; i--) {
			Assert.assertEquals(i + 1, rootQuad.getElements().size);
			rootQuad.remove(collisionPoints.get(i));
			Assert.assertEquals(i, rootQuad.getElements().size);
		}
	}
	
	@Test
	public void testRemoveAll() {
		Random random = new Random();
		Array<CollisionPoint> points = new Array<CollisionPoint>();
		for(int i = 0; i < 100; i++) {
			points.add(new CollisionPoint(random.nextInt(128), random.nextInt(128)));
		}
		rootQuad.addAll(points);
		Assert.assertEquals(points.size, rootQuad.getTotalElements());
		rootQuad.removeAll(points);
		Assert.assertEquals(0, rootQuad.getTotalElements());
	}

	@Test
	public void testClear() {
		Random random = new Random();
		Array<CollisionPoint> points = new Array<CollisionPoint>();
		for(int i = 0; i < 100; i++) {
			points.add(new CollisionPoint(random.nextInt(128), random.nextInt(128)));
		}
		rootQuad.addAll(points);
		Assert.assertEquals(points.size, rootQuad.getTotalElements());
		rootQuad.clear();
		Assert.assertEquals(0, rootQuad.getTotalElements());
	}
	
	@Test
	public void testSubdivide() {
		rootQuad.add(point1);
		Assert.assertEquals(1, rootQuad.getElements().size);
		Assert.assertEquals(1, rootQuad.getTotalQuads());
		rootQuad.add(point2);
		Assert.assertEquals(2, rootQuad.getElements().size);
		Assert.assertEquals(1, rootQuad.getTotalQuads());
		rootQuad.add(point3);
		Assert.assertEquals(3, rootQuad.getElements().size);
		Assert.assertEquals(4, rootQuad.getTotalQuads());
		rootQuad.add(point4);
		Assert.assertEquals(4, rootQuad.getElements().size);
		Assert.assertEquals(4, rootQuad.getTotalQuads());
		rootQuad.add(new CollisionPoint(32, 32));
		Assert.assertEquals(5, rootQuad.getElements().size);
		Assert.assertEquals(4, rootQuad.getTotalQuads());
		rootQuad.add(new CollisionPoint(48, 48));
		Assert.assertEquals(6, rootQuad.getElements().size);
		Assert.assertEquals(7, rootQuad.getTotalQuads());
	}
	
	@Test
	public void testMerge() {
		rootQuad = new PointQuadTree<CollisionPoint>(4, 3, 0, 0, 128, 128);
		rootQuad.add(point1);
		Assert.assertEquals(1, rootQuad.getTotalQuads());
		rootQuad.add(point2);
		rootQuad.add(point3);
		rootQuad.add(point4);
		rootQuad.add(new CollisionPoint(32, 32));
		Assert.assertEquals(4, rootQuad.getTotalQuads());
		rootQuad.remove(point4);
		rootQuad.remove(point3);
		rootQuad.remove(point2);
		Assert.assertEquals(1, rootQuad.getTotalQuads());
		Assert.assertEquals(2, rootQuad.getTotalElements());
		Assert.assertEquals(true, rootQuad.getElements().contains(point1, false));
	}
	
	@Test
	public void testGetTotalElements() {
		rootQuad.add(point1);
		Assert.assertEquals(1, rootQuad.getTotalElements());
		rootQuad.add(point2);
		Assert.assertEquals(2, rootQuad.getTotalElements());
		rootQuad.add(point3);
		Assert.assertEquals(3, rootQuad.getTotalElements());
		rootQuad.remove(point2);
		Assert.assertEquals(2, rootQuad.getTotalElements());
		rootQuad.add(point4);
		Assert.assertEquals(3, rootQuad.getTotalElements());
		rootQuad.add(point2);
		Assert.assertEquals(4, rootQuad.getTotalElements());
		rootQuad.add(new CollisionPoint(32, 32));
		Assert.assertEquals(5, rootQuad.getTotalElements());
		rootQuad.add(new CollisionPoint(48, 48));
		Assert.assertEquals(6, rootQuad.getTotalElements());
	}

	@Test
	public void testGetElementsWithinRegion() {
		rootQuad.add(point1);
		rootQuad.add(point2);
		rootQuad.add(point3);
		rootQuad.add(point4);

		Array<CollisionPoint> collisionPoints = rootQuad.getElementsWithinArea(new Rectangle(0, 0, 64, 64));
		Assert.assertEquals(1, collisionPoints.size);
		Assert.assertEquals(point1, collisionPoints.get(0));

		collisionPoints = rootQuad.getElementsWithinArea(new Rectangle(64, 0, 64, 64));
		Assert.assertEquals(1, collisionPoints.size);
		Assert.assertEquals(point2, collisionPoints.get(0));

		collisionPoints = rootQuad.getElementsWithinArea(new Rectangle(0, 64, 64, 64));
		Assert.assertEquals(1, collisionPoints.size);
		Assert.assertEquals(point3, collisionPoints.get(0));

		collisionPoints = rootQuad.getElementsWithinArea(new Rectangle(64, 64, 64, 64));
		Assert.assertEquals(1, collisionPoints.size);
		Assert.assertEquals(point4, collisionPoints.get(0));

		CollisionPoint collisionPoint5 = new CollisionPoint(32, 32);
		CollisionPoint collisionPoint6 = new CollisionPoint(48, 48);
		rootQuad.add(collisionPoint5);
		rootQuad.add(collisionPoint6);

		collisionPoints = rootQuad.getElementsWithinArea(new Rectangle(0, 0, 64, 64));
		Assert.assertEquals(3, collisionPoints.size);
		Assert.assertEquals(true, collisionPoints.contains(point1, false));
		Assert.assertEquals(true, collisionPoints.contains(collisionPoint5, false));
		Assert.assertEquals(true, collisionPoints.contains(collisionPoint6, false));
	}

	@Test
	public void testGetElementsWithinRegionIgnoringEdges() {
		rootQuad.add(point1);
		rootQuad.add(point2);
		rootQuad.add(point3);
		rootQuad.add(point4);

		Array<CollisionPoint> collisionPoints = rootQuad.getElementsWithinAreaIgnoringEdges(new Rectangle(0, 0, 64, 64));
		Assert.assertEquals(1, collisionPoints.size);
		Assert.assertEquals(point1, collisionPoints.get(0));

		collisionPoints = rootQuad.getElementsWithinAreaIgnoringEdges(new Rectangle(64, 0, 64, 64));
		Assert.assertEquals(1, collisionPoints.size);
		Assert.assertEquals(point2, collisionPoints.get(0));

		collisionPoints = rootQuad.getElementsWithinAreaIgnoringEdges(new Rectangle(0, 64, 64, 64));
		Assert.assertEquals(1, collisionPoints.size);
		Assert.assertEquals(point3, collisionPoints.get(0));

		collisionPoints = rootQuad.getElementsWithinAreaIgnoringEdges(new Rectangle(64, 64, 64, 64));
		Assert.assertEquals(1, collisionPoints.size);
		Assert.assertEquals(point4, collisionPoints.get(0));

		CollisionPoint collisionPoint5 = new CollisionPoint(32, 32);
		CollisionPoint collisionPoint6 = new CollisionPoint(48, 48);
		rootQuad.add(collisionPoint5);
		rootQuad.add(collisionPoint6);

		collisionPoints = rootQuad.getElementsWithinAreaIgnoringEdges(new Rectangle(0, 0, 64, 64));
		Assert.assertEquals(3, collisionPoints.size);
		Assert.assertEquals(true, collisionPoints.contains(point1, false));
		Assert.assertEquals(true, collisionPoints.contains(collisionPoint5, false));
		Assert.assertEquals(true, collisionPoints.contains(collisionPoint6, false));
	}

	@Test
	public void testGetElementsWithinRegionUpwards() {
		rootQuad.add(qAPoint1);
		rootQuad.add(qAPoint2);
		rootQuad.add(qAPoint3);
		rootQuad.add(qAPoint4);

		Array<CollisionPoint> collisionPoints;

		collisionPoints = qAPoint1.getQuad().getElementsWithinArea(new Rectangle(0, 0, 64, 64));
		Assert.assertEquals(1, collisionPoints.size);
		Assert.assertEquals(qAPoint1, collisionPoints.get(0));

		collisionPoints = qAPoint2.getQuad().getElementsWithinArea(new Rectangle(64, 0, 64, 64));
		Assert.assertEquals(1, collisionPoints.size);
		Assert.assertEquals(qAPoint2, collisionPoints.get(0));

		collisionPoints = qAPoint3.getQuad().getElementsWithinArea(new Rectangle(0, 64, 64, 64));
		Assert.assertEquals(1, collisionPoints.size);
		Assert.assertEquals(qAPoint3, collisionPoints.get(0));

		collisionPoints = qAPoint4.getQuad().getElementsWithinArea(new Rectangle(64, 64, 64, 64));
		Assert.assertEquals(1, collisionPoints.size);
		Assert.assertEquals(qAPoint4, collisionPoints.get(0));

		QuadTreeAwareCollisionPoint collisionPoint5 = new QuadTreeAwareCollisionPoint(32, 32);
		QuadTreeAwareCollisionPoint collisionPoint6 = new QuadTreeAwareCollisionPoint(48, 48);
		rootQuad.add(collisionPoint5);
		rootQuad.add(collisionPoint6);

		collisionPoints = qAPoint1.getQuad().getElementsWithinArea(new Rectangle(0, 0, 64, 64), QuadTreeSearchDirection.UPWARDS);
		Assert.assertEquals(3, collisionPoints.size);
		Assert.assertEquals(true, collisionPoints.contains(qAPoint1, false));
		Assert.assertEquals(true, collisionPoints.contains(collisionPoint5, false));
		Assert.assertEquals(true, collisionPoints.contains(collisionPoint6, false));
	}

	@Test
	public void testGetElementsWithinRegionIgnoringEdgesUpwards() {
		rootQuad.add(qAPoint1);
		rootQuad.add(qAPoint2);
		rootQuad.add(qAPoint3);
		rootQuad.add(qAPoint4);

		Array<CollisionPoint> collisionPoints;

		collisionPoints = qAPoint1.getQuad().getElementsWithinAreaIgnoringEdges(new Rectangle(0, 0, 64, 64));
		Assert.assertEquals(1, collisionPoints.size);
		Assert.assertEquals(qAPoint1, collisionPoints.get(0));

		collisionPoints = qAPoint2.getQuad().getElementsWithinAreaIgnoringEdges(new Rectangle(64, 0, 64, 64));
		Assert.assertEquals(1, collisionPoints.size);
		Assert.assertEquals(qAPoint2, collisionPoints.get(0));

		collisionPoints = qAPoint3.getQuad().getElementsWithinAreaIgnoringEdges(new Rectangle(0, 64, 64, 64));
		Assert.assertEquals(1, collisionPoints.size);
		Assert.assertEquals(qAPoint3, collisionPoints.get(0));

		collisionPoints = qAPoint4.getQuad().getElementsWithinAreaIgnoringEdges(new Rectangle(64, 64, 64, 64));
		Assert.assertEquals(1, collisionPoints.size);
		Assert.assertEquals(qAPoint4, collisionPoints.get(0));

		QuadTreeAwareCollisionPoint collisionPoint5 = new QuadTreeAwareCollisionPoint(32, 32);
		QuadTreeAwareCollisionPoint collisionPoint6 = new QuadTreeAwareCollisionPoint(48, 48);
		rootQuad.add(collisionPoint5);
		rootQuad.add(collisionPoint6);

		collisionPoints = qAPoint1.getQuad().getElementsWithinAreaIgnoringEdges(new Rectangle(0, 0, 64, 64), QuadTreeSearchDirection.UPWARDS);
		Assert.assertEquals(3, collisionPoints.size);
		Assert.assertEquals(true, collisionPoints.contains(qAPoint1, false));
		Assert.assertEquals(true, collisionPoints.contains(collisionPoint5, false));
		Assert.assertEquals(true, collisionPoints.contains(collisionPoint6, false));
	}

	@Test
	public void testGetElementsIntersectingLineSegment() {
		rootQuad.add(point1);
		rootQuad.add(point2);
		rootQuad.add(point3);
		rootQuad.add(point4);

		Array<CollisionPoint> CollisionPoints = rootQuad.getElementsIntersectingLineSegment(new LineSegment(0, 0, 128, 128));
		Assert.assertEquals(true, CollisionPoints.contains(point1, false));
		Assert.assertEquals(false, CollisionPoints.contains(point2, false));
		Assert.assertEquals(false, CollisionPoints.contains(point3, false));
		Assert.assertEquals(true, CollisionPoints.contains(point4, false));
	}

	@Test
	public void testGetElementsIntersectingLineSegmentUpwards() {
		rootQuad.add(qAPoint1);
		rootQuad.add(qAPoint2);
		rootQuad.add(qAPoint3);
		rootQuad.add(qAPoint4);

		Array<CollisionPoint> CollisionPoints = qAPoint1.getQuad().getElementsIntersectingLineSegment(new LineSegment(0, 0, 128, 128), QuadTreeSearchDirection.UPWARDS);
		Assert.assertEquals(true, CollisionPoints.contains(qAPoint1, false));
		Assert.assertEquals(false, CollisionPoints.contains(qAPoint2, false));
		Assert.assertEquals(false, CollisionPoints.contains(qAPoint3, false));
		Assert.assertEquals(true, CollisionPoints.contains(qAPoint4, false));
	}

	@Test
	public void testWarmupWithDepth() {
		rootQuad.warmupWithDepth(1);
		Assert.assertEquals(4, rootQuad.getTotalQuads());

		rootQuad.warmupWithDepth(2);
		Assert.assertEquals(16, rootQuad.getTotalQuads());
	}

	@Test
	public void testGetElementsWithinRegionUpwardsWithPooling() {
		rootQuad.warmupPool(32);
		testGetElementsWithinRegionUpwards();
	}

	@Test
	public void testGetElementsIntersectingLineSegmentWithPooling() {
		rootQuad.warmupPool(32);
		testGetElementsIntersectingLineSegment();
	}

	@Test
	public void testGetElementsWithinRegionIgnoringEdgesWithPooling() {
		rootQuad.warmupPool(32);
		testGetElementsWithinRegionIgnoringEdges();
	}
}
