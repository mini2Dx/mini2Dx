/*******************************************************************************
 * Copyright 2021 Viridian Software Limited
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
import org.mini2Dx.core.geom.Point;
import org.mini2Dx.core.util.InterpolationTracker;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.lockprovider.jvm.JvmLocks;

import java.util.Random;

public class CellGridTest {
	private CellGrid<CollisionBox> grid;
	private CollisionBox box1, box2, box3, box4;

	@Before
	public void setup() {
		InterpolationTracker.deregisterAll();
		Mdx.locks = new JvmLocks();

		Mdx.graphics = null;

		grid = new CellGrid<CollisionBox>(0, 0, 128, 128, 32, 32);

		box1 = new CollisionBox(1, 1, 32, 32);
		box2 = new CollisionBox(95, 1, 32, 32);
		box3 = new CollisionBox(1, 95, 32, 32);
		box4 = new CollisionBox(95, 95, 32, 32);
	}

	@Test
	public void testAdd() {
		int totalElements = 100;
		Random random = new Random();
		long startTime = System.nanoTime();
		for(int i = 0; i < totalElements; i++) {
			CollisionBox rect = new CollisionBox(random.nextInt(96), random.nextInt(96), 32f, 32f);
			Assert.assertEquals(true, grid.add(rect));
			Assert.assertEquals(i + 1, grid.getTotalElements());
		}
		long duration = System.nanoTime() - startTime;
		System.out.println("Took " + duration + "ns to add " + totalElements + " elements individually to " + RegionQuadTree.class.getSimpleName());
	}

	@Test
	public void testAddAll() {
		int totalElements = 100;
		Random random = new Random();
		Array<CollisionBox> rects = new Array<CollisionBox>();
		long startTime = System.nanoTime();
		for(int i = 0; i < totalElements; i++) {
			rects.add(new CollisionBox(random.nextInt(96), random.nextInt(96), 32f, 32f));
		}
		rects.add(new CollisionBox(-4f, -4f, 32f, 32f));

		grid.addAll(rects);
		long duration = System.nanoTime() - startTime;
		System.out.println("Took " + duration + "ns to add " + totalElements + " elements in bulk to " + RegionQuadTree.class.getSimpleName());
		Assert.assertEquals(rects.size, grid.getTotalElements());
	}

	@Test
	public void testRemove() {
		Random random = new Random();
		Array<CollisionBox> collisionBoxs = new Array<CollisionBox>();
		for(int i = 0; i < 1000; i++) {
			collisionBoxs.add(new CollisionBox(random.nextInt(96), random.nextInt(96), random.nextInt(32), random.nextInt(32)));
		}

		for(int i = 0; i < collisionBoxs.size; i++) {
			grid.add(collisionBoxs.get(i));
			Assert.assertEquals(i + 1, grid.getElements().size);
		}

		for(int i = collisionBoxs.size - 1; i >= 0 ; i--) {
			Assert.assertEquals(i + 1, grid.getElements().size);
			grid.remove(collisionBoxs.get(i));
			Assert.assertEquals(i, grid.getElements().size);
		}
	}

	@Test
	public void testRemoveAll() {
		Random random = new Random();
		Array<CollisionBox> rects = new Array<CollisionBox>();
		for(int i = 0; i < 100; i++) {
			rects.add(new CollisionBox(random.nextInt(96), random.nextInt(96), random.nextInt(32), random.nextInt(32)));
		}
		grid.addAll(rects);
		Assert.assertEquals(rects.size, grid.getTotalElements());
		grid.removeAll(rects);
		Assert.assertEquals(0, grid.getTotalElements());
	}

	@Test
	public void testClear() {
		Random random = new Random();
		Array<CollisionBox> rects = new Array<CollisionBox>();
		for(int i = 0; i < 100; i++) {
			rects.add(new CollisionBox(random.nextInt(96), random.nextInt(96), random.nextInt(32), random.nextInt(32)));
		}
		grid.addAll(rects);
		Assert.assertEquals(rects.size, grid.getTotalElements());
		grid.clear();
		Assert.assertEquals(0, grid.getTotalElements());
	}

	@Test
	public void testGetTotalElements() {
		grid.add(box1);
		Assert.assertEquals(1, grid.getTotalElements());
		grid.add(box2);
		Assert.assertEquals(2, grid.getTotalElements());
		grid.add(box3);
		Assert.assertEquals(3, grid.getTotalElements());
		grid.remove(box2);
		Assert.assertEquals(2, grid.getTotalElements());
		grid.add(box4);
		Assert.assertEquals(3, grid.getTotalElements());
		grid.add(box2);
		Assert.assertEquals(4, grid.getTotalElements());
		grid.add(new CollisionBox(48, 48, 32, 32));
		Assert.assertEquals(5, grid.getTotalElements());
		grid.add(new CollisionBox(12, 48, 8, 8));
		Assert.assertEquals(6, grid.getTotalElements());
	}

	@Test
	public void testGetElementsOverlappingArea() {
		grid.add(box1);
		grid.add(box2);
		grid.add(box3);
		grid.add(box4);

		Array<CollisionBox> collisionBoxs = grid.getElementsOverlappingArea(new CollisionBox(48, 48, 32, 32));
		Assert.assertEquals(0, collisionBoxs.size);

		CollisionBox collisionBox5 = new CollisionBox(24, 24, 2, 2);
		CollisionBox collisionBox6 = new CollisionBox(48, 48, 32, 32);
		CollisionBox collisionBox7 = new CollisionBox(12, 48, 8, 8);

		grid.add(collisionBox5);
		grid.add(collisionBox6);
		grid.add(collisionBox7);

		collisionBoxs = grid.getElementsOverlappingArea(new CollisionBox(0, 0, 128, 128));
		Assert.assertEquals(grid.getElements().size, collisionBoxs.size);

		collisionBoxs = grid.getElementsOverlappingArea(new CollisionBox(36, 36, 32, 32));
		Assert.assertEquals(1, collisionBoxs.size);
		Assert.assertEquals(collisionBox6, collisionBoxs.get(0));

		collisionBoxs = grid.getElementsOverlappingArea(new CollisionBox(0, 0, 64, 64));
		Assert.assertEquals(4, collisionBoxs.size);
		Assert.assertEquals(true, collisionBoxs.contains(box1, false));
		Assert.assertEquals(true, collisionBoxs.contains(collisionBox5, false));
		Assert.assertEquals(true, collisionBoxs.contains(collisionBox6, false));
		Assert.assertEquals(true, collisionBoxs.contains(collisionBox7, false));

		collisionBoxs = grid.getElementsOverlappingArea(new CollisionBox(16, 16, 24, 24));
		Assert.assertEquals(2, collisionBoxs.size);
		Assert.assertEquals(true, collisionBoxs.contains(box1, false));
		Assert.assertEquals(true, collisionBoxs.contains(collisionBox5, false));

		collisionBoxs = grid.getElementsOverlappingArea(new CollisionBox(12, 40, 48, 8));
		Assert.assertEquals(2, collisionBoxs.size);
		Assert.assertEquals(true, collisionBoxs.contains(collisionBox6, false));
		Assert.assertEquals(true, collisionBoxs.contains(collisionBox7, false));
	}

	@Test
	public void testGetElementsOverlappingAreaIgnoringEdges() {
		grid.add(box1);
		grid.add(box2);
		grid.add(box3);
		grid.add(box4);

		Array<CollisionBox> collisionBoxs = grid.getElementsOverlappingAreaIgnoringEdges(new CollisionBox(48, 48, 32, 32));
		Assert.assertEquals(0, collisionBoxs.size);

		collisionBoxs = grid.getElementsOverlappingAreaIgnoringEdges(new CollisionBox(0, 0, 128, 128));
		Assert.assertEquals(grid.getElements().size, collisionBoxs.size);

		collisionBoxs = grid.getElementsOverlappingAreaIgnoringEdges(new CollisionBox(32, 32, 8, 8));
		Assert.assertEquals(1, collisionBoxs.size);
		Assert.assertEquals(box1, collisionBoxs.get(0));

		collisionBoxs = grid.getElementsOverlappingAreaIgnoringEdges(new CollisionBox(33, 0, 8, 8));
		Assert.assertEquals(0, collisionBoxs.size);

		collisionBoxs = grid.getElementsOverlappingAreaIgnoringEdges(new CollisionBox(0, 33, 8, 8));
		Assert.assertEquals(0, collisionBoxs.size);
	}

	@Test
	public void testGetElementsIntersectingLineSegment() {
		grid.add(box1);
		grid.add(box2);
		grid.add(box3);
		grid.add(box4);

		Array<CollisionBox> collisionBoxs = grid.getElementsIntersectingLineSegment(new LineSegment(0,  0, 128, 128));
		Assert.assertEquals(2, collisionBoxs.size);
		Assert.assertEquals(true, collisionBoxs.contains(box1, false));
		Assert.assertEquals(true, collisionBoxs.contains(box4, false));

		CollisionBox collisionBox5 = new CollisionBox(24, 24, 2, 2);
		CollisionBox collisionBox6 = new CollisionBox(48, 48, 32, 32);
		CollisionBox collisionBox7 = new CollisionBox(12, 48, 8, 8);

		grid.add(collisionBox5);
		grid.add(collisionBox6);
		grid.add(collisionBox7);

		collisionBoxs = grid.getElementsIntersectingLineSegment(new LineSegment(0,  0, 128, 128));
		Assert.assertEquals(4, collisionBoxs.size);
		Assert.assertEquals(true, collisionBoxs.contains(box1, false));
		Assert.assertEquals(true, collisionBoxs.contains(box4, false));
		Assert.assertEquals(true, collisionBoxs.contains(collisionBox5, false));
		Assert.assertEquals(true, collisionBoxs.contains(collisionBox6, false));
	}

	@Test
	public void testGetElementsContainingPoint() {
		grid.add(box1);
		grid.add(box2);
		grid.add(box3);
		grid.add(box4);

		Array<CollisionBox> collisionBoxs = grid.getElementsContainingPoint(new Point(16, 16));
		Assert.assertEquals(1, collisionBoxs.size);
		Assert.assertEquals(true, collisionBoxs.contains(box1, false));

		collisionBoxs = grid.getElementsContainingPoint(new Point(112, 16));
		Assert.assertEquals(1, collisionBoxs.size);
		Assert.assertEquals(true, collisionBoxs.contains(box2, false));

		collisionBoxs = grid.getElementsContainingPoint(new Point(16, 112));
		Assert.assertEquals(1, collisionBoxs.size);
		Assert.assertEquals(true, collisionBoxs.contains(box3, false));

		collisionBoxs = grid.getElementsContainingPoint(new Point(112, 112));
		Assert.assertEquals(1, collisionBoxs.size);
		Assert.assertEquals(true, collisionBoxs.contains(box4, false));
	}

	@Test
	public void testGetElementsContainingArea() {
		grid.add(box1);

		//Collision box is inside box1
		Array<CollisionBox> collisionBoxs = grid.getElementsContainingArea(new CollisionBox(2, 2, 10, 10));
		Assert.assertEquals(1, collisionBoxs.size);
		Assert.assertEquals(true, collisionBoxs.contains(box1, false));

		//Collision box is outside box1
		collisionBoxs = grid.getElementsContainingArea(new CollisionBox(40, 40, 10, 10));
		Assert.assertEquals(0, collisionBoxs.size);
		Assert.assertEquals(false, collisionBoxs.contains(box1, false));

		//Collision box is partially inside box1
		collisionBoxs = grid.getElementsContainingArea(new CollisionBox(25, 25, 10, 10));
		Assert.assertEquals(0, collisionBoxs.size);
		Assert.assertEquals(false, collisionBoxs.contains(box1, false));

		//Collision box is larger than, and contains box 1
		collisionBoxs = grid.getElementsContainingArea(new CollisionBox(0.5f, 0.5f, 64, 64));
		Assert.assertEquals(0, collisionBoxs.size);
		Assert.assertEquals(false, collisionBoxs.contains(box1, false));
	}
}
