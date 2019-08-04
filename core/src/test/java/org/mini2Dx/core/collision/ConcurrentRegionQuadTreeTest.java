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

import junit.framework.Assert;
import net.jodah.concurrentunit.Waiter;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Point;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.util.InterpolationTracker;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.core.Mdx;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Unit tests for {@link ConcurrentRegionQuadTree}
 */
public class ConcurrentRegionQuadTreeTest implements Runnable {
	private static final long CONCURRENCY_TEST_TIMEOUT = 20000L;
	private static final int CONCURRENCY_TEST_ELEMENT_LIMIT = 10;
	private static final int CONCURRENCY_TEST_WATERMARK = 5;
	private static final float CONCURRENCY_TREE_WIDTH = 128000f;
	private static final float CONCURRENCY_TREE_HEIGHT = 76800f;

	private ConcurrentRegionQuadTree<CollisionBox> rootQuad;
	private CollisionBox box1, box2, box3, box4;

	private final Waiter waiter = new Waiter();
	private final AtomicInteger totalThreads = new AtomicInteger();
	private final AtomicBoolean concurrencyExceptionOccurred = new AtomicBoolean(false);
	private final AtomicInteger coordinateCursor = new AtomicInteger(0);
	private final AtomicInteger collisionsFound = new AtomicInteger(0);
	private final AtomicInteger collisionsMoved = new AtomicInteger(0);
	private final Queue<CollisionBox> threadCollisions = new ConcurrentLinkedQueue<CollisionBox>();

	private final CountDownLatch concurrentTestStartLatch = new CountDownLatch(1);

	@Before
	public void setup() {
		InterpolationTracker.deregisterAll();

		Mdx.graphics = null;

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
			Assert.assertEquals(i + 1, rootQuad.getElements().size);
		}
		long duration = System.nanoTime() - startTime;
		System.out.println("Took " + duration + "ns to add " + totalElements + " elements individually to "
				+ ConcurrentRegionQuadTree.class.getSimpleName());
	}

	@Test
	public void testConcurrentAdd() throws TimeoutException {
		final int totalThreads = 4;
		final int elementsPerThread = 1000;
		final int expectedTotalElements = totalThreads * elementsPerThread;

		for(int i = 0; i < totalThreads; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						concurrentTestStartLatch.await();
					} catch (Exception e) {}

					Random random = new Random();
					for (int i = 0; i < elementsPerThread; i++) {
						CollisionBox rect = new CollisionBox(random.nextInt(96), random.nextInt(96), 32f, 32f);
						Assert.assertEquals(true, rootQuad.add(rect));
					}
					waiter.resume();
				}
			}).start();
		}

		final long startTime = System.nanoTime();
		concurrentTestStartLatch.countDown();
		waiter.await(CONCURRENCY_TEST_TIMEOUT, totalThreads);

		Assert.assertEquals(expectedTotalElements, rootQuad.getElements().size);

		long duration = System.nanoTime() - startTime;
		System.out.println("Took " + duration + "ns to add " + expectedTotalElements + " elements individually to "
				+ ConcurrentRegionQuadTree.class.getSimpleName());
	}

	@Test
	public void testAddAll() {
		int totalElements = 100;
		Random random = new Random();
		Array<CollisionBox> rects = new Array<CollisionBox>();
		long startTime = System.nanoTime();
		for (int i = 0; i < totalElements; i++) {
			rects.add(new CollisionBox(random.nextInt(96), random.nextInt(96), 32f, 32f));
		}
		rects.add(new CollisionBox(-4f, -4f, 32f, 32f));

		rootQuad.addAll(rects);
		long duration = System.nanoTime() - startTime;
		System.out.println("Took " + duration + "ns to add " + totalElements + " elements in bulk to "
				+ ConcurrentRegionQuadTree.class.getSimpleName());
		Assert.assertEquals(rects.size, rootQuad.getTotalElements());
	}

	@Test
	public void testRemove() {
		Random random = new Random();
		Array<CollisionBox> collisionBoxs = new Array<CollisionBox>();
		for (int i = 0; i < 1000; i++) {
			collisionBoxs.add(
					new CollisionBox(random.nextInt(96), random.nextInt(96), random.nextInt(32), random.nextInt(32)));
		}

		for (int i = 0; i < collisionBoxs.size; i++) {
			rootQuad.add(collisionBoxs.get(i));
			Assert.assertEquals(i + 1, rootQuad.getElements().size);
		}

		for (int i = collisionBoxs.size - 1; i >= 0; i--) {
			Assert.assertEquals(i + 1, rootQuad.getElements().size);
			rootQuad.remove(collisionBoxs.get(i));
			Assert.assertEquals(i, rootQuad.getElements().size);
		}
	}

	@Test
	public void testRemoveAll() {
		Random random = new Random();
		Array<CollisionBox> rects = new Array<CollisionBox>();
		for (int i = 0; i < 100; i++) {
			rects.add(new CollisionBox(random.nextInt(96), random.nextInt(96), random.nextInt(32), random.nextInt(32)));
		}
		rootQuad.addAll(rects);
		Assert.assertEquals(rects.size, rootQuad.getTotalElements());
		rootQuad.removeAll(rects);
		Assert.assertEquals(0, rootQuad.getTotalElements());
	}

	@Test
	public void testSubdivide() {
		rootQuad.add(box1);
		Assert.assertEquals(1, rootQuad.getElements().size);
		Assert.assertEquals(1, rootQuad.getTotalQuads());

		rootQuad.add(box2);
		Assert.assertEquals(2, rootQuad.getElements().size);
		Assert.assertEquals(1, rootQuad.getTotalQuads());

		rootQuad.add(box3);
		Assert.assertEquals(3, rootQuad.getElements().size);
		Assert.assertEquals(4, rootQuad.getTotalQuads());

		rootQuad.add(box4);
		Assert.assertEquals(4, rootQuad.getElements().size);
		Assert.assertEquals(4, rootQuad.getTotalQuads());

		rootQuad.add(new CollisionBox(24, 24, 2, 2));
		Assert.assertEquals(5, rootQuad.getElements().size);
		Assert.assertEquals(4, rootQuad.getTotalQuads());

		rootQuad.add(new CollisionBox(48, 48, 32, 32));
		Assert.assertEquals(6, rootQuad.getElements().size);
		Assert.assertEquals(4, rootQuad.getTotalQuads());

		rootQuad.add(new CollisionBox(12, 48, 8, 8));
		Assert.assertEquals(7, rootQuad.getElements().size);
		Assert.assertEquals(7, rootQuad.getTotalQuads());
	}

	@Test
	public void testMergeRemoveSingleElements() {
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
		Assert.assertEquals(true, rootQuad.getElements().contains(box1, false));
	}
	
	@Test
	public void testMergeRemoveAllElements() {
		rootQuad = new ConcurrentRegionQuadTree<CollisionBox>(4, 3, 0, 0, 128, 128);
		rootQuad.add(box1);
		Assert.assertEquals(1, rootQuad.getTotalQuads());
		rootQuad.add(box2);
		rootQuad.add(box3);
		rootQuad.add(box4);
		rootQuad.add(new CollisionBox(24, 24, 2, 2));
		Assert.assertEquals(4, rootQuad.getTotalQuads());

		Array<CollisionBox> boxes = new Array<CollisionBox>();
		boxes.add(box4);
		boxes.add(box3);
		boxes.add(box2);
		
		rootQuad.removeAll(boxes);
		Assert.assertEquals(1, rootQuad.getTotalQuads());
		Assert.assertEquals(2, rootQuad.getTotalElements());
		Assert.assertEquals(true, rootQuad.getElements().contains(box1, false));
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

		Array<CollisionBox> collisionBoxs = rootQuad.getElementsWithinArea(new CollisionBox(48, 48, 32, 32));
		Assert.assertEquals(0, collisionBoxs.size);

		CollisionBox collisionBox5 = new CollisionBox(24, 24, 2, 2);
		CollisionBox collisionBox6 = new CollisionBox(48, 48, 32, 32);
		CollisionBox collisionBox7 = new CollisionBox(12, 48, 8, 8);

		rootQuad.add(collisionBox5);
		rootQuad.add(collisionBox6);
		rootQuad.add(collisionBox7);

		collisionBoxs = rootQuad.getElementsWithinArea(new CollisionBox(0, 0, 128, 128));
		Assert.assertEquals(rootQuad.getElements().size, collisionBoxs.size);

		collisionBoxs = rootQuad.getElementsWithinArea(new CollisionBox(33, 33, 32, 32));
		Assert.assertEquals(1, collisionBoxs.size);
		Assert.assertEquals(collisionBox6, collisionBoxs.get(0));

		collisionBoxs = rootQuad.getElementsWithinArea(new CollisionBox(0, 0, 64, 64));
		Assert.assertEquals(4, collisionBoxs.size);
		Assert.assertEquals(true, collisionBoxs.contains(box1, false));
		Assert.assertEquals(true, collisionBoxs.contains(collisionBox5, false));
		Assert.assertEquals(true, collisionBoxs.contains(collisionBox6, false));
		Assert.assertEquals(true, collisionBoxs.contains(collisionBox7, false));

		collisionBoxs = rootQuad.getElementsWithinArea(new CollisionBox(16, 16, 24, 24));
		Assert.assertEquals(2, collisionBoxs.size);
		Assert.assertEquals(true, collisionBoxs.contains(box1, false));
		Assert.assertEquals(true, collisionBoxs.contains(collisionBox5, false));

		collisionBoxs = rootQuad.getElementsWithinArea(new CollisionBox(12, 40, 48, 8));
		Assert.assertEquals(2, collisionBoxs.size);
		Assert.assertEquals(true, collisionBoxs.contains(collisionBox6, false));
		Assert.assertEquals(true, collisionBoxs.contains(collisionBox7, false));
	}

	@Test
	public void testGetElementsIntersectingLineSegment() {
		rootQuad.add(box1);
		rootQuad.add(box2);
		rootQuad.add(box3);
		rootQuad.add(box4);

		Array<CollisionBox> collisionBoxs = rootQuad.getElementsIntersectingLineSegment(new LineSegment(0, 0, 128, 128));
		Assert.assertEquals(2, collisionBoxs.size);
		Assert.assertEquals(true, collisionBoxs.contains(box1, false));
		Assert.assertEquals(true, collisionBoxs.contains(box4, false));

		CollisionBox collisionBox5 = new CollisionBox(24, 24, 2, 2);
		CollisionBox collisionBox6 = new CollisionBox(48, 48, 32, 32);
		CollisionBox collisionBox7 = new CollisionBox(12, 48, 8, 8);

		rootQuad.add(collisionBox5);
		rootQuad.add(collisionBox6);
		rootQuad.add(collisionBox7);

		collisionBoxs = rootQuad.getElementsIntersectingLineSegment(new LineSegment(0, 0, 128, 128));
		Assert.assertEquals(4, collisionBoxs.size);
		Assert.assertEquals(true, collisionBoxs.contains(box1, false));
		Assert.assertEquals(true, collisionBoxs.contains(box4, false));
		Assert.assertEquals(true, collisionBoxs.contains(collisionBox5, false));
		Assert.assertEquals(true, collisionBoxs.contains(collisionBox6, false));

		collisionBoxs = rootQuad.getElementsIntersectingLineSegment(new LineSegment(0, 0, 1, 1));
		Assert.assertEquals(1, collisionBoxs.size);
		Assert.assertEquals(true, collisionBoxs.contains(box1, false));

		collisionBoxs = rootQuad.getElementsIntersectingLineSegment(new LineSegment(-1, -1, 0, 0));
		Assert.assertEquals(1, collisionBoxs.size);
		Assert.assertEquals(true, collisionBoxs.contains(box1, false));

		collisionBoxs = rootQuad.getElementsIntersectingLineSegment(new LineSegment(31f, 31f, 32f, 32f));
		Assert.assertEquals(1, collisionBoxs.size);
		Assert.assertEquals(true, collisionBoxs.contains(box1, false));

		collisionBoxs = rootQuad.getElementsIntersectingLineSegment(new LineSegment(33f, 33f, 32f, 32f));
		Assert.assertEquals(1, collisionBoxs.size);
		Assert.assertEquals(true, collisionBoxs.contains(box1, false));
	}

	@Test
	public void testGetElementsIntersectingLineSegmentWithNegativeBox() {
		rootQuad = new ConcurrentRegionQuadTree<CollisionBox>(2, -128f, -128f, 256f, 256f);
		rootQuad.add(new CollisionBox(-80f, -80f, 32f, 32f));

		Array<CollisionBox> collisionBoxs = rootQuad
				.getElementsIntersectingLineSegment(new LineSegment(-83f, -84f, -83f, -85f));
		Assert.assertEquals(0, collisionBoxs.size);
	}

	@Test
	public void testGetElementsContainingPoint() {
		rootQuad.add(box1);
		rootQuad.add(box2);
		rootQuad.add(box3);
		rootQuad.add(box4);

		Array<CollisionBox> collisionBoxs = rootQuad.getElementsContainingPoint(new Point(16, 16));
		Assert.assertEquals(1, collisionBoxs.size);
		Assert.assertEquals(true, collisionBoxs.contains(box1, false));

		collisionBoxs = rootQuad.getElementsContainingPoint(new Point(112, 16));
		Assert.assertEquals(1, collisionBoxs.size);
		Assert.assertEquals(true, collisionBoxs.contains(box2, false));

		collisionBoxs = rootQuad.getElementsContainingPoint(new Point(16, 112));
		Assert.assertEquals(1, collisionBoxs.size);
		Assert.assertEquals(true, collisionBoxs.contains(box3, false));

		collisionBoxs = rootQuad.getElementsContainingPoint(new Point(112, 112));
		Assert.assertEquals(1, collisionBoxs.size);
		Assert.assertEquals(true, collisionBoxs.contains(box4, false));
	}

	@Test
	public void testMergingConcurrency() throws TimeoutException {
		rootQuad = new ConcurrentRegionQuadTree<CollisionBox>(CONCURRENCY_TEST_ELEMENT_LIMIT,
				CONCURRENCY_TEST_WATERMARK, 0, 0, CONCURRENCY_TREE_WIDTH, CONCURRENCY_TREE_HEIGHT);

		List<CollisionBox> initialCollisions = new ArrayList<CollisionBox>();
		for (int i = 0; i < MathUtils.round(CONCURRENCY_TREE_WIDTH / CONCURRENCY_TEST_ELEMENT_LIMIT); i++) {
			createNextConcurrencyCollision(initialCollisions);
			createRandomConcurrencyCollision(initialCollisions);
		}
		Collections.shuffle(initialCollisions);
		threadCollisions.addAll(initialCollisions);

		int totalThreads = Runtime.getRuntime().availableProcessors();
		while (totalThreads % 3 != 0) {
			totalThreads++;
		}
		for (int i = 0; i < totalThreads; i++) {
			new Thread(this).start();
		}

		concurrentTestStartLatch.countDown();
		waiter.await(CONCURRENCY_TEST_TIMEOUT);

		System.out.println(rootQuad.getTotalMergeOperations() + " total merge operations.");
		System.out.println(collisionsFound.get() + " collisions found concurrently.");
		System.out.println(collisionsMoved.get() + " collisions moved concurrently.");
		Assert.assertEquals(true, rootQuad.getTotalMergeOperations() > 0);
		Assert.assertEquals(true, collisionsFound.get() > 0);
		Assert.assertEquals(true, collisionsMoved.get() > 0);
		Assert.assertEquals(false, concurrencyExceptionOccurred.get());
	}

	@Override
	public void run() {
		ConcurrencyThreadType threadType = ConcurrencyThreadType.READER;
		int totalThreads = this.totalThreads.incrementAndGet();
		switch(totalThreads % 3) {
		case 0:
			threadType = ConcurrencyThreadType.READER;
			break;
		case 1:
			threadType = ConcurrencyThreadType.REMOVER;
			break;
		case 2:
			threadType = ConcurrencyThreadType.MOVER;
			break;
		}
		Array<CollisionBox> collisions = new Array<CollisionBox>();

		while (rootQuad.getTotalMergeOperations() < 20 || collisionsFound.get() == 0) {
			try {
				concurrentTestStartLatch.await();

				if (threadCollisions.isEmpty()) {
					int totalCollisions = MathUtils.random(1, MathUtils.round(CONCURRENCY_TREE_WIDTH));
					
					List<CollisionBox> additionalCollisions = new ArrayList<CollisionBox>();
					for(int i = 0; i < totalCollisions; i++) {
						createRandomConcurrencyCollision(additionalCollisions);
					}
					threadCollisions.addAll(additionalCollisions);
					continue;
				}
				
				switch(threadType) {
				case MOVER:
					CollisionBox collisionBox = threadCollisions.poll();
					float originalX = collisionBox.getX();
					float moveDistance = (CONCURRENCY_TREE_WIDTH - originalX) / 100f;
					
					while(collisionBox.getX() + collisionBox.getWidth() < CONCURRENCY_TREE_WIDTH) {
						collisionBox.add(moveDistance, moveDistance);
					}
					while(collisionBox.getX() > originalX) {
						collisionBox.add(-moveDistance, -moveDistance);
					}
					threadCollisions.offer(collisionBox);
					collisionsMoved.incrementAndGet();
					break;
				case REMOVER:
					rootQuad.remove(threadCollisions.poll());
					break;
				case READER:
				default:
					rootQuad.getElementsWithinArea(collisions,
							new Rectangle(MathUtils.random(CONCURRENCY_TREE_WIDTH / 2f), MathUtils.random(CONCURRENCY_TREE_HEIGHT / 2f),
									MathUtils.random(CONCURRENCY_TREE_WIDTH / 3f), MathUtils.random(CONCURRENCY_TREE_HEIGHT / 3f)));
					collisionsFound.addAndGet(collisions.size);
					collisions.clear();
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				concurrencyExceptionOccurred.set(true);
			}
		}
		waiter.resume();
	}
	
	private void createRandomConcurrencyCollision(List<CollisionBox> threadCollisions) {
		int size = MathUtils.round(MathUtils.random(CONCURRENCY_TREE_WIDTH / CONCURRENCY_TEST_ELEMENT_LIMIT));
		float x = MathUtils.random(0f, CONCURRENCY_TREE_WIDTH - size);
		float y = MathUtils.random(0, CONCURRENCY_TREE_HEIGHT - size);
		CollisionBox nextCollision = new CollisionBox(x, y, size, size);
		threadCollisions.add(nextCollision);
		rootQuad.add(nextCollision);
	}

	private void createNextConcurrencyCollision(List<CollisionBox> threadCollisions) {
		int size = MathUtils.round(CONCURRENCY_TREE_WIDTH / CONCURRENCY_TEST_ELEMENT_LIMIT);
		int cursor = coordinateCursor.addAndGet(size / 2);
		float x = cursor % (CONCURRENCY_TREE_WIDTH - size);
		float y = (cursor / (CONCURRENCY_TREE_WIDTH - size)) % (CONCURRENCY_TREE_HEIGHT - size);

		CollisionBox nextCollision = new CollisionBox(x, y, size, size);
		threadCollisions.add(nextCollision);
		rootQuad.add(nextCollision);
	}
	
	private enum ConcurrencyThreadType {
		READER,
		REMOVER,
		MOVER
	}
}
