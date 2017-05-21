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
package org.mini2Dx.core.collisions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.engine.geom.CollisionBox;
import org.mini2Dx.core.engine.geom.CollisionPoint;
import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Point;
import org.mini2Dx.core.geom.Rectangle;

import com.badlogic.gdx.math.MathUtils;

import junit.framework.Assert;

/**
 * Unit tests for {@link ConcurrentRegionQuadTree}
 */
public class ConcurrentRegionQuadTreeTest implements Runnable {
	private static final long CONCURRENCY_TEST_DURATION = 5000L;
	private static final int CONCURRENCY_TEST_WATERMARK = 4;
	private static final float TREE_WIDTH = 128f;
	private static final float TREE_HEIGHT = 128f;

	private ConcurrentRegionQuadTree<CollisionBox> rootQuad;
	private CollisionBox box1, box2, box3, box4;

	private AtomicLong timer = new AtomicLong(0L);
	private AtomicBoolean concurrencyExceptionOccurred = new AtomicBoolean(false);
	private AtomicInteger coordinateCursor = new AtomicInteger(0);
	private AtomicInteger collisionsFound = new AtomicInteger(0);
	private Queue<CollisionBox> threadCollisions = new ConcurrentLinkedQueue<CollisionBox>();

	@Before
	public void setup() {
		rootQuad = new ConcurrentRegionQuadTree<CollisionBox>(2, 0, 0, 128, 128);

		box1 = new CollisionBox(0, 0, 32, 32);
		box2 = new CollisionBox(96, 0, 32, 32);
		box3 = new CollisionBox(0, 96, 32, 32);
		box4 = new CollisionBox(96, 96, 32, 32);
	}

	@Test
	public void testAdd() {
		int totalElements = 100;
		Random random = new Random();
		long startTime = System.nanoTime();
		for (int i = 0; i < totalElements; i++) {
			CollisionBox rect = new CollisionBox(random.nextInt(96), random.nextInt(96), 32f, 32f);
			Assert.assertEquals(true, rootQuad.add(rect));
			Assert.assertEquals(i + 1, rootQuad.getElements().size());
		}
		long duration = System.nanoTime() - startTime;
		System.out.println("Took " + duration + "ns to add " + totalElements + " elements individually to "
				+ ConcurrentRegionQuadTree.class.getSimpleName());
	}

	@Test
	public void testAddAll() {
		int totalElements = 100;
		Random random = new Random();
		List<CollisionBox> rects = new ArrayList<CollisionBox>();
		long startTime = System.nanoTime();
		for (int i = 0; i < totalElements; i++) {
			rects.add(new CollisionBox(random.nextInt(96), random.nextInt(96), 32f, 32f));
		}
		rects.add(new CollisionBox(-4f, -4f, 32f, 32f));

		rootQuad.addAll(rects);
		long duration = System.nanoTime() - startTime;
		System.out.println("Took " + duration + "ns to add " + totalElements + " elements in bulk to "
				+ ConcurrentRegionQuadTree.class.getSimpleName());
		Assert.assertEquals(rects.size(), rootQuad.getTotalElements());
	}

	@Test
	public void testRemove() {
		Random random = new Random();
		List<CollisionBox> CollisionBoxs = new ArrayList<CollisionBox>();
		for (int i = 0; i < 1000; i++) {
			CollisionBoxs.add(
					new CollisionBox(random.nextInt(96), random.nextInt(96), random.nextInt(32), random.nextInt(32)));
		}

		for (int i = 0; i < CollisionBoxs.size(); i++) {
			rootQuad.add(CollisionBoxs.get(i));
			Assert.assertEquals(i + 1, rootQuad.getElements().size());
		}

		for (int i = CollisionBoxs.size() - 1; i >= 0; i--) {
			Assert.assertEquals(i + 1, rootQuad.getElements().size());
			rootQuad.remove(CollisionBoxs.get(i));
			Assert.assertEquals(i, rootQuad.getElements().size());
		}
	}

	@Test
	public void testRemoveAll() {
		Random random = new Random();
		List<CollisionBox> rects = new ArrayList<CollisionBox>();
		for (int i = 0; i < 100; i++) {
			rects.add(new CollisionBox(random.nextInt(96), random.nextInt(96), random.nextInt(32), random.nextInt(32)));
		}
		rootQuad.addAll(rects);
		Assert.assertEquals(rects.size(), rootQuad.getTotalElements());
		rootQuad.removeAll(rects);
		Assert.assertEquals(0, rootQuad.getTotalElements());
	}

	@Test
	public void testSubdivide() {
		rootQuad.add(box1);
		Assert.assertEquals(1, rootQuad.getElements().size());
		Assert.assertEquals(1, rootQuad.getTotalQuads());

		rootQuad.add(box2);
		Assert.assertEquals(2, rootQuad.getElements().size());
		Assert.assertEquals(1, rootQuad.getTotalQuads());

		rootQuad.add(box3);
		Assert.assertEquals(3, rootQuad.getElements().size());
		Assert.assertEquals(4, rootQuad.getTotalQuads());

		rootQuad.add(box4);
		Assert.assertEquals(4, rootQuad.getElements().size());
		Assert.assertEquals(4, rootQuad.getTotalQuads());

		rootQuad.add(new CollisionBox(24, 24, 2, 2));
		Assert.assertEquals(5, rootQuad.getElements().size());
		Assert.assertEquals(4, rootQuad.getTotalQuads());

		rootQuad.add(new CollisionBox(48, 48, 32, 32));
		Assert.assertEquals(6, rootQuad.getElements().size());
		Assert.assertEquals(4, rootQuad.getTotalQuads());

		rootQuad.add(new CollisionBox(12, 48, 8, 8));
		Assert.assertEquals(7, rootQuad.getElements().size());
		Assert.assertEquals(7, rootQuad.getTotalQuads());
	}

	@Test
	public void testMerge() {
		rootQuad = new ConcurrentRegionQuadTree<CollisionBox>(4, 3, 0, 0, 128, 128);
		rootQuad.add(box1);
		Assert.assertEquals(1, rootQuad.getTotalQuads());
		rootQuad.add(box2);
		rootQuad.add(box3);
		rootQuad.add(box4);
		rootQuad.add(new CollisionBox(24, 24, 2, 2));
		Assert.assertEquals(4, rootQuad.getTotalQuads());
		rootQuad.remove(box4);
		rootQuad.remove(box3);
		rootQuad.remove(box2);
		Assert.assertEquals(1, rootQuad.getTotalQuads());
		Assert.assertEquals(2, rootQuad.getTotalElements());
		Assert.assertEquals(true, rootQuad.getElements().contains(box1));
	}

	@Test
	public void testGetTotalElements() {
		rootQuad.add(box1);
		Assert.assertEquals(1, rootQuad.getTotalElements());
		rootQuad.add(box2);
		Assert.assertEquals(2, rootQuad.getTotalElements());
		rootQuad.add(box3);
		Assert.assertEquals(3, rootQuad.getTotalElements());
		rootQuad.remove(box2);
		Assert.assertEquals(2, rootQuad.getTotalElements());
		rootQuad.add(box4);
		Assert.assertEquals(3, rootQuad.getTotalElements());
		rootQuad.add(box2);
		Assert.assertEquals(4, rootQuad.getTotalElements());
		rootQuad.add(new CollisionBox(48, 48, 32, 32));
		Assert.assertEquals(5, rootQuad.getTotalElements());
		rootQuad.add(new CollisionBox(12, 48, 8, 8));
		Assert.assertEquals(6, rootQuad.getTotalElements());
	}

	@Test
	public void testGetElementsWithinRegion() {
		rootQuad.add(box1);
		rootQuad.add(box2);
		rootQuad.add(box3);
		rootQuad.add(box4);

		List<CollisionBox> CollisionBoxs = rootQuad.getElementsWithinArea(new CollisionBox(48, 48, 32, 32));
		Assert.assertEquals(0, CollisionBoxs.size());

		CollisionBox CollisionBox5 = new CollisionBox(24, 24, 2, 2);
		CollisionBox CollisionBox6 = new CollisionBox(48, 48, 32, 32);
		CollisionBox CollisionBox7 = new CollisionBox(12, 48, 8, 8);

		rootQuad.add(CollisionBox5);
		rootQuad.add(CollisionBox6);
		rootQuad.add(CollisionBox7);

		CollisionBoxs = rootQuad.getElementsWithinArea(new CollisionBox(0, 0, 128, 128));
		Assert.assertEquals(rootQuad.getElements().size(), CollisionBoxs.size());

		CollisionBoxs = rootQuad.getElementsWithinArea(new CollisionBox(33, 33, 32, 32));
		Assert.assertEquals(1, CollisionBoxs.size());
		Assert.assertEquals(CollisionBox6, CollisionBoxs.get(0));

		CollisionBoxs = rootQuad.getElementsWithinArea(new CollisionBox(0, 0, 64, 64));
		Assert.assertEquals(4, CollisionBoxs.size());
		Assert.assertEquals(true, CollisionBoxs.contains(box1));
		Assert.assertEquals(true, CollisionBoxs.contains(CollisionBox5));
		Assert.assertEquals(true, CollisionBoxs.contains(CollisionBox6));
		Assert.assertEquals(true, CollisionBoxs.contains(CollisionBox7));

		CollisionBoxs = rootQuad.getElementsWithinArea(new CollisionBox(16, 16, 24, 24));
		Assert.assertEquals(2, CollisionBoxs.size());
		Assert.assertEquals(true, CollisionBoxs.contains(box1));
		Assert.assertEquals(true, CollisionBoxs.contains(CollisionBox5));

		CollisionBoxs = rootQuad.getElementsWithinArea(new CollisionBox(12, 40, 48, 8));
		Assert.assertEquals(2, CollisionBoxs.size());
		Assert.assertEquals(true, CollisionBoxs.contains(CollisionBox6));
		Assert.assertEquals(true, CollisionBoxs.contains(CollisionBox7));
	}

	@Test
	public void testGetElementsIntersectingLineSegment() {
		rootQuad.add(box1);
		rootQuad.add(box2);
		rootQuad.add(box3);
		rootQuad.add(box4);

		List<CollisionBox> collisionBoxs = rootQuad.getElementsIntersectingLineSegment(new LineSegment(0, 0, 128, 128));
		Assert.assertEquals(2, collisionBoxs.size());
		Assert.assertEquals(true, collisionBoxs.contains(box1));
		Assert.assertEquals(true, collisionBoxs.contains(box4));

		CollisionBox collisionBox5 = new CollisionBox(24, 24, 2, 2);
		CollisionBox collisionBox6 = new CollisionBox(48, 48, 32, 32);
		CollisionBox collisionBox7 = new CollisionBox(12, 48, 8, 8);

		rootQuad.add(collisionBox5);
		rootQuad.add(collisionBox6);
		rootQuad.add(collisionBox7);

		collisionBoxs = rootQuad.getElementsIntersectingLineSegment(new LineSegment(0, 0, 128, 128));
		Assert.assertEquals(4, collisionBoxs.size());
		Assert.assertEquals(true, collisionBoxs.contains(box1));
		Assert.assertEquals(true, collisionBoxs.contains(box4));
		Assert.assertEquals(true, collisionBoxs.contains(collisionBox5));
		Assert.assertEquals(true, collisionBoxs.contains(collisionBox6));

		collisionBoxs = rootQuad.getElementsIntersectingLineSegment(new LineSegment(0, 0, 1, 1));
		Assert.assertEquals(1, collisionBoxs.size());
		Assert.assertEquals(true, collisionBoxs.contains(box1));

		collisionBoxs = rootQuad.getElementsIntersectingLineSegment(new LineSegment(-1, -1, 0, 0));
		Assert.assertEquals(1, collisionBoxs.size());
		Assert.assertEquals(true, collisionBoxs.contains(box1));

		collisionBoxs = rootQuad.getElementsIntersectingLineSegment(new LineSegment(31f, 31f, 32f, 32f));
		Assert.assertEquals(1, collisionBoxs.size());
		Assert.assertEquals(true, collisionBoxs.contains(box1));

		collisionBoxs = rootQuad.getElementsIntersectingLineSegment(new LineSegment(33f, 33f, 32f, 32f));
		Assert.assertEquals(1, collisionBoxs.size());
		Assert.assertEquals(true, collisionBoxs.contains(box1));
	}

	@Test
	public void testGetElementsIntersectingLineSegmentWithNegativeBox() {
		rootQuad = new ConcurrentRegionQuadTree<CollisionBox>(2, -128f, -128f, 256f, 256f);
		rootQuad.add(new CollisionBox(-80f, -80f, 32f, 32f));

		List<CollisionBox> collisionBoxs = rootQuad
				.getElementsIntersectingLineSegment(new LineSegment(-83f, -84f, -83f, -85f));
		Assert.assertEquals(0, collisionBoxs.size());
	}

	@Test
	public void testGetElementsContainingPoint() {
		rootQuad.add(box1);
		rootQuad.add(box2);
		rootQuad.add(box3);
		rootQuad.add(box4);

		List<CollisionBox> collisionBoxs = rootQuad.getElementsContainingPoint(new Point(16, 16));
		Assert.assertEquals(1, collisionBoxs.size());
		Assert.assertEquals(true, collisionBoxs.contains(box1));

		collisionBoxs = rootQuad.getElementsContainingPoint(new Point(112, 16));
		Assert.assertEquals(1, collisionBoxs.size());
		Assert.assertEquals(true, collisionBoxs.contains(box2));

		collisionBoxs = rootQuad.getElementsContainingPoint(new Point(16, 112));
		Assert.assertEquals(1, collisionBoxs.size());
		Assert.assertEquals(true, collisionBoxs.contains(box3));

		collisionBoxs = rootQuad.getElementsContainingPoint(new Point(112, 112));
		Assert.assertEquals(1, collisionBoxs.size());
		Assert.assertEquals(true, collisionBoxs.contains(box4));
	}

	@Test
	public void testMergingConcurrency() {
		rootQuad = new ConcurrentRegionQuadTree<CollisionBox>(CONCURRENCY_TEST_WATERMARK * 2,
				CONCURRENCY_TEST_WATERMARK, 0, 0, TREE_WIDTH, TREE_HEIGHT);

		for (int i = 0; i < MathUtils.round(TREE_WIDTH * TREE_HEIGHT); i++) {
			createNextCollision();
		}

		int totalThreads = Runtime.getRuntime().availableProcessors() * 2;
		if (totalThreads % 2 == 1) {
			totalThreads++;
		}
		for (int i = 0; i < totalThreads; i++) {
			new Thread(this).start();
		}

		while (timer.get() < CONCURRENCY_TEST_DURATION) {
			long startTime = System.currentTimeMillis();
			try {
				Thread.sleep(100L);
			} catch (Exception e) {
			}
			timer.addAndGet(System.currentTimeMillis() - startTime);
		}

		System.out.println(rootQuad.getTotalMergeOperations() + " total merge operations, "
				+ collisionsFound.getAndIncrement() + " collisions found concurrently");
		Assert.assertEquals(true, rootQuad.getTotalMergeOperations() > 0);
		Assert.assertEquals(true, collisionsFound.get() > 0);
		Assert.assertEquals(false, concurrencyExceptionOccurred.get());
	}

	@Override
	public void run() {
		boolean readerThread = Thread.currentThread().getId() % 2L == 0L;
		List<CollisionBox> collisions = new ArrayList<CollisionBox>();

		while (timer.get() < CONCURRENCY_TEST_DURATION) {
			try {
				if (threadCollisions.isEmpty()) {
					createNextCollision();
				} else if (readerThread) {
					rootQuad.getElementsWithinArea(collisions,
							new Rectangle(MathUtils.random(TREE_WIDTH / 2f), MathUtils.random(TREE_HEIGHT / 2f),
									MathUtils.random(TREE_HEIGHT / 3f), MathUtils.random(TREE_HEIGHT / 3f)));
					collisionsFound.addAndGet(collisions.size());
					collisions.clear();
				} else {
					rootQuad.remove(threadCollisions.poll());
				}

				if (readerThread && rootQuad.getTotalMergeOperations() == 0) {
					Thread.sleep(10L);
				}
			} catch (Exception e) {
				e.printStackTrace();
				concurrencyExceptionOccurred.set(true);
			}
		}
	}

	private void createNextCollision() {
		int size = MathUtils.round(TREE_WIDTH / (CONCURRENCY_TEST_WATERMARK * 8));
		int cursor = coordinateCursor.addAndGet(size);
		float x = cursor % TREE_WIDTH;
		float y = (cursor / TREE_WIDTH) % TREE_HEIGHT;

		CollisionBox nextCollision = new CollisionBox(x, y, 2f, 2f);
		threadCollisions.offer(nextCollision);
		rootQuad.add(nextCollision);
	}
}
