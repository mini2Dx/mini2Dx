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
	public static int DEFAULT_POOL_SIZE = 32;

	final Queue<CollisionBox> collisionBoxes = new Queue<CollisionBox>(DEFAULT_POOL_SIZE * 2);
	final Queue<CollisionCircle> collisionCircles = new Queue<CollisionCircle>(DEFAULT_POOL_SIZE * 2);
	final Queue<CollisionPoint> collisionPoints = new Queue<CollisionPoint>(DEFAULT_POOL_SIZE * 2);
	final Queue<CollisionPolygon> collisionPolygons = new Queue<CollisionPolygon>(DEFAULT_POOL_SIZE * 2);

	final Queue<QuadTreeAwareCollisionBox> quadTreeAwareCollisionBoxes = new Queue<QuadTreeAwareCollisionBox>(DEFAULT_POOL_SIZE * 2);
	final Queue<QuadTreeAwareCollisionCircle> quadTreeAwareCollisionCircles = new Queue<QuadTreeAwareCollisionCircle>(DEFAULT_POOL_SIZE * 2);
	final Queue<QuadTreeAwareCollisionPoint> quadTreeAwareCollisionPoints = new Queue<QuadTreeAwareCollisionPoint>(DEFAULT_POOL_SIZE * 2);
	final Queue<QuadTreeAwareCollisionPolygon> quadTreeAwareCollisionPolygons = new Queue<QuadTreeAwareCollisionPolygon>(DEFAULT_POOL_SIZE * 2);

	final Queue<StaticCollisionBox> staticCollisionBoxes = new Queue<StaticCollisionBox>(DEFAULT_POOL_SIZE * 2);
	final Queue<StaticCollisionCircle> staticCollisionCircles = new Queue<StaticCollisionCircle>(DEFAULT_POOL_SIZE * 2);
	final Queue<StaticCollisionPoint> staticCollisionPoints = new Queue<StaticCollisionPoint>(DEFAULT_POOL_SIZE * 2);
	final Queue<StaticCollisionPolygon> staticCollisionPolygons = new Queue<StaticCollisionPolygon>(DEFAULT_POOL_SIZE * 2);

	final Queue<QuadTreeAwareStaticCollisionBox> quadTreeAwareStaticCollisionBoxes = new Queue<QuadTreeAwareStaticCollisionBox>(DEFAULT_POOL_SIZE * 2);
	final Queue<QuadTreeAwareStaticCollisionCircle> quadTreeAwareStaticCollisionCircles = new Queue<QuadTreeAwareStaticCollisionCircle>(DEFAULT_POOL_SIZE * 2);
	final Queue<QuadTreeAwareStaticCollisionPoint> quadTreeAwareStaticCollisionPoints = new Queue<QuadTreeAwareStaticCollisionPoint>(DEFAULT_POOL_SIZE * 2);
	final Queue<QuadTreeAwareStaticCollisionPolygon> quadTreeAwareStaticCollisionPolygons = new Queue<QuadTreeAwareStaticCollisionPolygon>(DEFAULT_POOL_SIZE * 2);

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

			quadTreeAwareCollisionBoxes.addLast(new QuadTreeAwareCollisionBox(CollisionIdSequence.nextId(),this));
			quadTreeAwareCollisionBoxes.removeLast().dispose();
			quadTreeAwareCollisionCircles.addLast(new QuadTreeAwareCollisionCircle(CollisionIdSequence.nextId(),this));
			quadTreeAwareCollisionCircles.removeLast().dispose();
			quadTreeAwareCollisionPoints.addLast(new QuadTreeAwareCollisionPoint(CollisionIdSequence.nextId(),this));
			quadTreeAwareCollisionPoints.removeLast().dispose();

			staticCollisionBoxes.addLast(new StaticCollisionBox(CollisionIdSequence.nextId(),this));
			staticCollisionBoxes.removeLast().dispose();
			staticCollisionCircles.addLast(new StaticCollisionCircle(CollisionIdSequence.nextId(),this));
			staticCollisionCircles.removeLast().dispose();
			staticCollisionPoints.addLast(new StaticCollisionPoint(CollisionIdSequence.nextId(),this));
			staticCollisionPoints.removeLast().dispose();

			quadTreeAwareStaticCollisionBoxes.addLast(new QuadTreeAwareStaticCollisionBox(CollisionIdSequence.nextId(),this));
			quadTreeAwareStaticCollisionBoxes.removeLast().dispose();
			quadTreeAwareStaticCollisionCircles.addLast(new QuadTreeAwareStaticCollisionCircle(CollisionIdSequence.nextId(),this));
			quadTreeAwareStaticCollisionCircles.removeLast().dispose();
			quadTreeAwareStaticCollisionPoints.addLast(new QuadTreeAwareStaticCollisionPoint(CollisionIdSequence.nextId(),this));
			quadTreeAwareStaticCollisionPoints.removeLast().dispose();
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

	public QuadTreeAwareCollisionBox quadTreeAwareCollisionBox() {
		return quadTreeAwareCollisionBox(CollisionIdSequence.nextId());
	}

	public QuadTreeAwareCollisionBox quadTreeAwareCollisionBox(int id) {
		return quadTreeAwareCollisionBox(id, 0f, 0f, 1f, 1f);
	}

	public QuadTreeAwareCollisionBox quadTreeAwareCollisionBox(int id, float x, float y, float width, float height) {
		init();

		final QuadTreeAwareCollisionBox result;
		synchronized (quadTreeAwareCollisionBoxes) {
			if(quadTreeAwareCollisionBoxes.size == 0) {
				result = new QuadTreeAwareCollisionBox(CollisionIdSequence.offset(id), this);
				InterpolationTracker.deregister(result);
			} else {
				result = quadTreeAwareCollisionBoxes.removeFirst();
			}
		}
		result.init(id, x, y, width, height);
		return result;
	}

	public QuadTreeAwareCollisionCircle quadTreeAwareCollisionCircle() {
		return quadTreeAwareCollisionCircle(CollisionIdSequence.nextId());
	}

	public QuadTreeAwareCollisionCircle quadTreeAwareCollisionCircle(int id) {
		return quadTreeAwareCollisionCircle(id, 0f, 0f, 1f);
	}

	public QuadTreeAwareCollisionCircle quadTreeAwareCollisionCircle(int id, float x, float y, float radius) {
		init();

		final QuadTreeAwareCollisionCircle result;
		synchronized (quadTreeAwareCollisionCircles) {
			if(quadTreeAwareCollisionCircles.size == 0) {
				result = new QuadTreeAwareCollisionCircle(CollisionIdSequence.offset(id), this);
				InterpolationTracker.deregister(result);
			} else {
				result = quadTreeAwareCollisionCircles.removeFirst();
			}
		}
		result.init(id, x, y, radius);
		return result;
	}

	public QuadTreeAwareCollisionPoint quadTreeAwareCollisionPoint() {
		return quadTreeAwareCollisionPoint(CollisionIdSequence.nextId());
	}

	public QuadTreeAwareCollisionPoint quadTreeAwareCollisionPoint(int id) {
		return quadTreeAwareCollisionPoint(id, 0f, 0f);
	}

	public QuadTreeAwareCollisionPoint quadTreeAwareCollisionPoint(int id, float x, float y) {
		init();

		final QuadTreeAwareCollisionPoint result;
		synchronized (quadTreeAwareCollisionPoints) {
			if(quadTreeAwareCollisionPoints.size == 0) {
				result = new QuadTreeAwareCollisionPoint(CollisionIdSequence.offset(id), this);
				InterpolationTracker.deregister(result);
			} else {
				result = quadTreeAwareCollisionPoints.removeFirst();
			}
		}
		result.init(id, x, y);
		return result;
	}

	public QuadTreeAwareCollisionPolygon quadTreeAwareCollisionPolygon(float [] vertices) {
		return quadTreeAwareCollisionPolygon(CollisionIdSequence.nextId(), vertices);
	}

	public QuadTreeAwareCollisionPolygon quadTreeAwareCollisionPolygon(int id, float [] vertices) {
		init();

		final QuadTreeAwareCollisionPolygon result;
		synchronized (quadTreeAwareCollisionPolygons) {
			if(quadTreeAwareCollisionPolygons.size == 0) {
				result = new QuadTreeAwareCollisionPolygon(CollisionIdSequence.offset(id), this, vertices);
				InterpolationTracker.deregister(result);
			} else {
				result = quadTreeAwareCollisionPolygons.removeFirst();
			}
		}
		result.init(id, vertices);
		return result;
	}

	public QuadTreeAwareCollisionPolygon quadTreeAwareCollisionPolygon(Vector2[] vectors) {
		return quadTreeAwareCollisionPolygon(CollisionIdSequence.nextId(), vectors);
	}

	public QuadTreeAwareCollisionPolygon quadTreeAwareCollisionPolygon(int id, Vector2[] vectors) {
		init();

		final QuadTreeAwareCollisionPolygon result;
		synchronized (quadTreeAwareCollisionPolygons) {
			if(quadTreeAwareCollisionPolygons.size == 0) {
				result = new QuadTreeAwareCollisionPolygon(CollisionIdSequence.offset(id), this, vectors);
				InterpolationTracker.deregister(result);
			} else {
				result = quadTreeAwareCollisionPolygons.removeFirst();
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

	public QuadTreeAwareStaticCollisionBox quadTreeAwareStaticCollisionBox() {
		return quadTreeAwareStaticCollisionBox(CollisionIdSequence.nextId());
	}

	public QuadTreeAwareStaticCollisionBox quadTreeAwareStaticCollisionBox(int id) {
		return quadTreeAwareStaticCollisionBox(id, 0f, 0f, 1f, 1f);
	}

	public QuadTreeAwareStaticCollisionBox quadTreeAwareStaticCollisionBox(int id, float x, float y, float width, float height) {
		init();

		final QuadTreeAwareStaticCollisionBox result;
		synchronized (quadTreeAwareStaticCollisionBoxes) {
			if(quadTreeAwareStaticCollisionBoxes.size == 0) {
				result = new QuadTreeAwareStaticCollisionBox(CollisionIdSequence.offset(id), this);
			} else {
				result = quadTreeAwareStaticCollisionBoxes.removeFirst();
			}
		}
		result.init(id, x, y, width, height);
		return result;
	}

	public QuadTreeAwareStaticCollisionCircle quadTreeAwareStaticCollisionCircle() {
		return quadTreeAwareStaticCollisionCircle(CollisionIdSequence.nextId());
	}

	public QuadTreeAwareStaticCollisionCircle quadTreeAwareStaticCollisionCircle(int id) {
		return quadTreeAwareStaticCollisionCircle(id, 0f, 0f, 1f);
	}

	public QuadTreeAwareStaticCollisionCircle quadTreeAwareStaticCollisionCircle(int id, float centerX, float centerY, float radius) {
		init();

		final QuadTreeAwareStaticCollisionCircle result;
		synchronized (quadTreeAwareStaticCollisionCircle()) {
			if(quadTreeAwareStaticCollisionCircles.size == 0) {
				result = new QuadTreeAwareStaticCollisionCircle(CollisionIdSequence.offset(id),this);
			} else {
				result = quadTreeAwareStaticCollisionCircles.removeFirst();
			}
		}
		result.init(id, centerX, centerY, radius);
		return result;
	}

	public QuadTreeAwareStaticCollisionPoint quadTreeAwareStaticCollisionPoint() {
		return quadTreeAwareStaticCollisionPoint(CollisionIdSequence.nextId());
	}

	public QuadTreeAwareStaticCollisionPoint quadTreeAwareStaticCollisionPoint(int id) {
		return quadTreeAwareStaticCollisionPoint(id, 0f, 0f);
	}

	public QuadTreeAwareStaticCollisionPoint quadTreeAwareStaticCollisionPoint(int id, float x, float y) {
		init();

		final QuadTreeAwareStaticCollisionPoint result;
		synchronized (quadTreeAwareStaticCollisionPoints) {
			if(quadTreeAwareStaticCollisionPoints.size == 0) {
				result = new QuadTreeAwareStaticCollisionPoint(CollisionIdSequence.offset(id), this);
			} else {
				result = quadTreeAwareStaticCollisionPoints.removeFirst();
			}
		}
		result.init(id, x, y);
		return result;
	}

	public QuadTreeAwareStaticCollisionPolygon quadTreeAwareStaticCollisionPolygon(float [] vertices) {
		return quadTreeAwareStaticCollisionPolygon(CollisionIdSequence.nextId(), vertices);
	}

	public QuadTreeAwareStaticCollisionPolygon quadTreeAwareStaticCollisionPolygon(int id, float [] vertices) {
		init();

		final QuadTreeAwareStaticCollisionPolygon result;
		synchronized (quadTreeAwareStaticCollisionPolygons) {
			if(quadTreeAwareStaticCollisionPolygons.size == 0) {
				result = new QuadTreeAwareStaticCollisionPolygon(CollisionIdSequence.offset(id), this, vertices);
			} else {
				result = quadTreeAwareStaticCollisionPolygons.removeFirst();
			}
		}
		result.init(id, vertices);
		return result;
	}

	public QuadTreeAwareStaticCollisionPolygon quadTreeAwareStaticCollisionPolygon(Vector2[] vectors) {
		return quadTreeAwareStaticCollisionPolygon(CollisionIdSequence.nextId(), vectors);
	}

	public QuadTreeAwareStaticCollisionPolygon quadTreeAwareStaticCollisionPolygon(int id, Vector2[] vectors) {
		init();

		final QuadTreeAwareStaticCollisionPolygon result;
		synchronized (quadTreeAwareStaticCollisionPolygons) {
			if(quadTreeAwareStaticCollisionPolygons.size == 0) {
				result = new QuadTreeAwareStaticCollisionPolygon(CollisionIdSequence.offset(id), this, vectors);
			} else {
				result = quadTreeAwareStaticCollisionPolygons.removeFirst();
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

	public void release(QuadTreeAwareCollisionBox collisionBox) {
		synchronized (quadTreeAwareCollisionBoxes) {
			quadTreeAwareCollisionBoxes.addLast(collisionBox);
		}
	}

	public void release(QuadTreeAwareCollisionCircle collisionCircle) {
		synchronized (quadTreeAwareCollisionCircles) {
			quadTreeAwareCollisionCircles.addLast(collisionCircle);
		}
	}

	public void release(QuadTreeAwareCollisionPoint collisionPoint) {
		synchronized (quadTreeAwareCollisionPoints) {
			quadTreeAwareCollisionPoints.addLast(collisionPoint);
		}
	}

	public void release(QuadTreeAwareCollisionPolygon collisionPolygon) {
		synchronized (quadTreeAwareCollisionPolygons) {
			quadTreeAwareCollisionPolygons.addLast(collisionPolygon);
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

	public void release(QuadTreeAwareStaticCollisionBox collisionBox) {
		synchronized (quadTreeAwareStaticCollisionBoxes) {
			quadTreeAwareStaticCollisionBoxes.addLast(collisionBox);
		}
	}

	public void release(QuadTreeAwareStaticCollisionCircle collisionCircle) {
		synchronized (quadTreeAwareStaticCollisionCircles) {
			quadTreeAwareStaticCollisionCircles.addLast(collisionCircle);
		}
	}

	public void release(QuadTreeAwareStaticCollisionPoint collisionPoint) {
		synchronized (quadTreeAwareStaticCollisionPoints) {
			quadTreeAwareStaticCollisionPoints.addLast(collisionPoint);
		}
	}

	public void release(QuadTreeAwareStaticCollisionPolygon collisionPolygon) {
		synchronized (quadTreeAwareStaticCollisionPolygons) {
			quadTreeAwareStaticCollisionPolygons.addLast(collisionPolygon);
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

	public int getTotalQuadTreeAwareCollisionBoxesAvailable() {
		init();

		synchronized (quadTreeAwareCollisionBoxes) {
			return quadTreeAwareCollisionBoxes.size;
		}
	}

	public int getTotalQuadTreeAwareCollisionCirclesAvailable() {
		init();

		synchronized (quadTreeAwareCollisionCircles) {
			return quadTreeAwareCollisionCircles.size;
		}
	}

	public int getTotalQuadTreeAwareCollisionPointsAvailable() {
		init();

		synchronized (quadTreeAwareCollisionPoints) {
			return quadTreeAwareCollisionPoints.size;
		}
	}

	public int getTotalQuadTreeAwareCollisionPolygonsAvailable() {
		init();

		synchronized (quadTreeAwareCollisionPolygons) {
			return quadTreeAwareCollisionPolygons.size;
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

	public int getTotalQuadTreeAwareStaticCollisionBoxesAvailable() {
		init();

		synchronized (quadTreeAwareStaticCollisionBoxes) {
			return quadTreeAwareStaticCollisionBoxes.size;
		}
	}

	public int getTotalQuadTreeAwareStaticCollisionCirclesAvailable() {
		init();

		synchronized (quadTreeAwareStaticCollisionCircles) {
			return quadTreeAwareStaticCollisionCircles.size;
		}
	}

	public int getTotalQuadTreeAwareStaticCollisionPointsAvailable() {
		init();

		synchronized (quadTreeAwareStaticCollisionPoints) {
			return quadTreeAwareStaticCollisionPoints.size;
		}
	}

	public int getTotalQuadTreeAwareStaticCollisionPolygonsAvailable() {
		init();

		synchronized (quadTreeAwareStaticCollisionPolygons) {
			return quadTreeAwareStaticCollisionPolygons.size;
		}
	}
}
