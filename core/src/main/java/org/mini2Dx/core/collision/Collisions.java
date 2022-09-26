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

import org.mini2Dx.core.collision.util.*;
import org.mini2Dx.core.util.InterpolationTracker;
import org.mini2Dx.gdx.math.Vector2;
import org.mini2Dx.gdx.utils.Queue;

/**
 * Provides pooled collision classes
 */
public class Collisions {
	/**
	 * Default pool size. Modify this value before launching the game to increase the default pool sizes.
	 */
	public static int DEFAULT_POOL_SIZE = 256;

	final Queue<CollisionBox> collisionBoxes = new Queue<CollisionBox>(DEFAULT_POOL_SIZE * 2);
	final Queue<CollisionCircle> collisionCircles = new Queue<CollisionCircle>(DEFAULT_POOL_SIZE * 2);
	final Queue<CollisionPoint> collisionPoints = new Queue<CollisionPoint>(DEFAULT_POOL_SIZE * 2);
	final Queue<CollisionPolygon> collisionPolygons = new Queue<CollisionPolygon>(DEFAULT_POOL_SIZE * 2);

	final Queue<StaticCollisionBox> staticCollisionBoxes = new Queue<StaticCollisionBox>(DEFAULT_POOL_SIZE * 2);
	final Queue<StaticCollisionCircle> staticCollisionCircles = new Queue<StaticCollisionCircle>(DEFAULT_POOL_SIZE * 2);
	final Queue<StaticCollisionPoint> staticCollisionPoints = new Queue<StaticCollisionPoint>(DEFAULT_POOL_SIZE * 2);
	final Queue<StaticCollisionPolygon> staticCollisionPolygons = new Queue<StaticCollisionPolygon>(DEFAULT_POOL_SIZE * 2);

	private boolean initialised = false;

	public Collisions() {
		super();
	}

	/**
	 * Initialises the pool
	 */
	public synchronized void init() {
		if(initialised) {
			return;
		}
		for(int i = 0; i < DEFAULT_POOL_SIZE; i++) {
			collisionBoxes.addLast(new CollisionBox(CollisionIdSequence.nextId(), this));
			collisionBoxes.removeLast().dispose();
			collisionCircles.addLast(new CollisionCircle(CollisionIdSequence.nextId(),this));
			collisionCircles.removeLast().dispose();
			collisionPoints.addLast(new CollisionPoint(CollisionIdSequence.nextId(),this));
			collisionPoints.removeLast().dispose();

			staticCollisionBoxes.addLast(new StaticCollisionBox(CollisionIdSequence.nextId(),this));
			staticCollisionBoxes.removeLast().dispose();
			staticCollisionCircles.addLast(new StaticCollisionCircle(CollisionIdSequence.nextId(),this));
			staticCollisionCircles.removeLast().dispose();
			staticCollisionPoints.addLast(new StaticCollisionPoint(CollisionIdSequence.nextId(),this));
			staticCollisionPoints.removeLast().dispose();
		}
		initialised = true;
	}

	public CollisionBox collisionBox() {
		return collisionBox(CollisionIdSequence.nextId());
	}

	public CollisionBox collisionBox(int id) {
		return collisionBox(id, 0f, 0f, 1f, 1f);
	}

	public CollisionBox collisionBox(int id, float x, float y, float width, float height) {
		init();

		final CollisionBox result;
		synchronized (collisionBoxes) {
			if(collisionBoxes.size == 0) {
				result = new CollisionBox(CollisionIdSequence.offset(id), this);
				InterpolationTracker.deregister(result);
			} else {
				result = collisionBoxes.removeFirst();
			}
		}
		result.init(id, x, y, width, height);
		return result;
	}

	public CollisionCircle collisionCircle() {
		return collisionCircle(CollisionIdSequence.nextId());
	}

	public CollisionCircle collisionCircle(int id) {
		return collisionCircle(id, 0f, 0f, 1f);
	}

	public CollisionCircle collisionCircle(int id, float x, float y, float radius) {
		init();

		final CollisionCircle result;
		synchronized (collisionCircles) {
			if(collisionCircles.size == 0) {
				result = new CollisionCircle(CollisionIdSequence.offset(id),this);
				InterpolationTracker.deregister(result);
			} else {
				result = collisionCircles.removeFirst();
			}
		}
		result.init(id, x, y, radius);
		return result;
	}

	public CollisionPoint collisionPoint() {
		return collisionPoint(CollisionIdSequence.nextId());
	}

	public CollisionPoint collisionPoint(int id) {
		return collisionPoint(id, 0f, 0f);
	}

	public CollisionPoint collisionPoint(int id, float x, float y) {
		init();

		final CollisionPoint result;
		synchronized (collisionPoints) {
			if(collisionPoints.size == 0) {
				result = new CollisionPoint(CollisionIdSequence.offset(id),this);
				InterpolationTracker.deregister(result);
			} else {
				result = collisionPoints.removeFirst();
			}
		}
		result.init(id, x, y);
		return result;
	}

	public CollisionPolygon collisionPolygon(float [] vertices) {
		return collisionPolygon(CollisionIdSequence.nextId(), vertices);
	}

	public CollisionPolygon collisionPolygon(int id, float [] vertices) {
		init();

		final CollisionPolygon result;
		synchronized (collisionPolygons) {
			if(collisionPolygons.size == 0) {
				result = new CollisionPolygon(CollisionIdSequence.offset(id), this, vertices);
				InterpolationTracker.deregister(result);
			} else {
				result = collisionPolygons.removeFirst();
			}
		}
		result.init(id, vertices);
		return result;
	}

	public CollisionPolygon collisionPolygon(Vector2[] vectors) {
		return collisionPolygon(CollisionIdSequence.nextId(), vectors);
	}

	public CollisionPolygon collisionPolygon(int id, Vector2[] vectors) {
		init();

		final CollisionPolygon result;
		synchronized (collisionPolygons) {
			if(collisionPolygons.size == 0) {
				result = new CollisionPolygon(CollisionIdSequence.offset(id), this, vectors);
				InterpolationTracker.deregister(result);
			} else {
				result = collisionPolygons.removeFirst();
			}
		}
		result.init(id, vectors);
		return result;
	}

	public StaticCollisionBox staticCollisionBox() {
		return staticCollisionBox(CollisionIdSequence.nextId());
	}

	public StaticCollisionBox staticCollisionBox(int id) {
		return staticCollisionBox(id, 0f, 0f, 1f, 1f);
	}

	public StaticCollisionBox staticCollisionBox(int id, float x, float y, float width, float height) {
		init();

		final StaticCollisionBox result;
		synchronized (staticCollisionBoxes) {
			if(staticCollisionBoxes.size == 0) {
				result = new StaticCollisionBox(CollisionIdSequence.offset(id), this);
			} else {
				result = staticCollisionBoxes.removeFirst();
			}
		}
		result.init(id, x, y, width, height);
		return result;
	}

	public StaticCollisionCircle staticCollisionCircle() {
		return staticCollisionCircle(CollisionIdSequence.nextId());
	}

	public StaticCollisionCircle staticCollisionCircle(int id) {
		return staticCollisionCircle(id, 0f, 0f, 1f);
	}

	public StaticCollisionCircle staticCollisionCircle(int id, float centerX, float centerY, float radius) {
		init();

		final StaticCollisionCircle result;
		synchronized (staticCollisionCircles) {
			if(staticCollisionCircles.size == 0) {
				result = new StaticCollisionCircle(CollisionIdSequence.offset(id),this);
			} else {
				result = staticCollisionCircles.removeFirst();
			}
		}
		result.init(id, centerX, centerY, radius);
		return result;
	}

	public StaticCollisionPoint staticCollisionPoint() {
		return staticCollisionPoint(CollisionIdSequence.nextId());
	}

	public StaticCollisionPoint staticCollisionPoint(int id) {
		return staticCollisionPoint(id, 0f, 0f);
	}

	public StaticCollisionPoint staticCollisionPoint(int id, float x, float y) {
		init();

		final StaticCollisionPoint result;
		synchronized (staticCollisionPoints) {
			if(staticCollisionPoints.size == 0) {
				result = new StaticCollisionPoint(CollisionIdSequence.offset(id), this);
			} else {
				result = staticCollisionPoints.removeFirst();
			}
		}
		result.init(id, x, y);
		return result;
	}

	public StaticCollisionPolygon staticCollisionPolygon(float [] vertices) {
		return staticCollisionPolygon(CollisionIdSequence.nextId(), vertices);
	}

	public StaticCollisionPolygon staticCollisionPolygon(int id, float [] vertices) {
		init();

		final StaticCollisionPolygon result;
		synchronized (staticCollisionPolygons) {
			if(staticCollisionPolygons.size == 0) {
				result = new StaticCollisionPolygon(CollisionIdSequence.offset(id), this, vertices);
			} else {
				result = staticCollisionPolygons.removeFirst();
			}
		}
		result.init(id, vertices);
		return result;
	}

	public StaticCollisionPolygon staticCollisionPolygon(Vector2[] vectors) {
		return staticCollisionPolygon(CollisionIdSequence.nextId(), vectors);
	}

	public StaticCollisionPolygon staticCollisionPolygon(int id, Vector2[] vectors) {
		init();

		final StaticCollisionPolygon result;
		synchronized (staticCollisionPolygons) {
			if(staticCollisionPolygons.size == 0) {
				result = new StaticCollisionPolygon(CollisionIdSequence.offset(id), this, vectors);
			} else {
				result = staticCollisionPolygons.removeFirst();
			}
		}
		result.init(id, vectors);
		return result;
	}

	public void release(CollisionBox collisionBox) {
		synchronized (collisionBoxes) {
			collisionBoxes.addLast(collisionBox);
		}
	}

	public void release(CollisionCircle collisionCircle) {
		synchronized (collisionCircles) {
			collisionCircles.addLast(collisionCircle);
		}
	}

	public void release(CollisionPoint collisionPoint) {
		synchronized (collisionPoints) {
			collisionPoints.addLast(collisionPoint);
		}
	}

	public void release(CollisionPolygon collisionPolygon) {
		synchronized (collisionPolygons) {
			collisionPolygons.addLast(collisionPolygon);
		}
	}

	public void release(StaticCollisionBox collisionBox) {
		synchronized (staticCollisionBoxes) {
			staticCollisionBoxes.addLast(collisionBox);
		}
	}

	public void release(StaticCollisionCircle collisionCircle) {
		synchronized (staticCollisionCircles) {
			staticCollisionCircles.addLast(collisionCircle);
		}
	}

	public void release(StaticCollisionPoint collisionPoint) {
		synchronized (staticCollisionPoints) {
			staticCollisionPoints.addLast(collisionPoint);
		}
	}

	public void release(StaticCollisionPolygon collisionPolygon) {
		synchronized (staticCollisionPolygons) {
			staticCollisionPolygons.addLast(collisionPolygon);
		}
	}

	public int getTotalCollisionBoxesAvailable() {
		init();

		synchronized (collisionBoxes) {
			return collisionBoxes.size;
		}
	}

	public int getTotalCollisionCirclesAvailable() {
		init();

		synchronized (collisionCircles) {
			return collisionCircles.size;
		}
	}

	public int getTotalCollisionPointsAvailable() {
		init();

		synchronized (collisionPoints) {
			return collisionPoints.size;
		}
	}

	public int getTotalCollisionPolygonsAvailable() {
		init();

		synchronized (collisionPolygons) {
			return collisionPolygons.size;
		}
	}

	public int getTotalStaticCollisionBoxesAvailable() {
		init();

		synchronized (staticCollisionBoxes) {
			return staticCollisionBoxes.size;
		}
	}

	public int getTotalStaticCollisionCirclesAvailable() {
		init();

		synchronized (staticCollisionCircles) {
			return staticCollisionCircles.size;
		}
	}

	public int getTotalStaticCollisionPointsAvailable() {
		init();

		synchronized (staticCollisionPoints) {
			return staticCollisionPoints.size;
		}
	}

	public int getTotalStaticCollisionPolygonsAvailable() {
		init();

		synchronized (staticCollisionPolygons) {
			return staticCollisionPolygons.size;
		}
	}

	/**
	 * Warms up CollisionBox pool to specified size
	 * @param poolSize The amount of CollisionBox instances in the pool
	 */
	public void warmupCollisionBoxes(int poolSize) {
		for(int i = 0; i < poolSize; i++) {
			synchronized(collisionBoxes) {
				collisionBoxes.addLast(new CollisionBox(CollisionIdSequence.nextId(), this));
				collisionBoxes.removeLast().dispose();
			}
		}
	}

	/**
	 * Warms up CollisionCircle pool to specified size
	 * @param poolSize The amount of CollisionCircle instances in the pool
	 */
	public void warmupCollisionCircles(int poolSize) {
		for(int i = 0; i < poolSize; i++) {
			synchronized(collisionCircles) {
				collisionCircles.addLast(new CollisionCircle(CollisionIdSequence.nextId(),this));
				collisionCircles.removeLast().dispose();
			}
		}
	}

	/**
	 * Warms up CollisionPoint pool to specified size
	 * @param poolSize The amount of CollisionPoint instances in the pool
	 */
	public void warmupCollisionPoints(int poolSize) {
		for(int i = 0; i < poolSize; i++) {
			synchronized(collisionPoints) {
				collisionPoints.addLast(new CollisionPoint(CollisionIdSequence.nextId(),this));
				collisionPoints.removeLast().dispose();
			}
		}
	}

	/**
	 * Warms up StaticCollisionBox pool to specified size
	 * @param poolSize The amount of StaticCollisionBox instances in the pool
	 */
	public void warmupStaticCollisionBoxes(int poolSize) {
		for(int i = 0; i < poolSize; i++) {
			synchronized(staticCollisionBoxes) {
				staticCollisionBoxes.addLast(new StaticCollisionBox(CollisionIdSequence.nextId(),this));
				staticCollisionBoxes.removeLast().dispose();
			}
		}
	}

	/**
	 * Warms up StaticCollisionCircle pool to specified size
	 * @param poolSize The amount of StaticCollisionCircle instances in the pool
	 */
	public void warmupStaticCollisionCircles(int poolSize) {
		for(int i = 0; i < poolSize; i++) {
			synchronized(staticCollisionCircles) {
				staticCollisionCircles.addLast(new StaticCollisionCircle(CollisionIdSequence.nextId(),this));
				staticCollisionCircles.removeLast().dispose();
			}
		}
	}

	/**
	 * Warms up StaticCollisionPoint pool to specified size
	 * @param poolSize The amount of StaticCollisionPoint instances in the pool
	 */
	public void warmupStaticCollisionPoints(int poolSize) {
		for(int i = 0; i < poolSize; i++) {
			synchronized(staticCollisionPoints) {
				staticCollisionPoints.addLast(new StaticCollisionPoint(CollisionIdSequence.nextId(),this));
				staticCollisionPoints.removeLast().dispose();
			}
		}
	}
}
