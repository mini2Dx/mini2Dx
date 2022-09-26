/**
 * Copyright 2022 Viridian Software Ltd.
 */
package org.mini2Dx.core.collision;

import org.mini2Dx.core.geom.Intersector;
import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.gdx.math.Vector2;
import org.mini2Dx.gdx.utils.Queue;

public class QuadElementBounds {
	private static final Queue<QuadElementBounds> POOL = new Queue<>();
	private static float [] VERTICES = new float[8];

	static {
		for(int i = 0; i < Quad.INITIAL_POOL_SIZE; i++) {
			POOL.addLast(new QuadElementBounds());
		}
	}

	public float x, y, maxX, maxY;

	public boolean contains(float x, float y) {
		if(x < this.x) {
			return false;
		}
		if(y < this.y) {
			return false;
		}
		if(x > this.maxX) {
			return false;
		}
		if(y > this.maxY) {
			return false;
		}
		return true;
	}

	public boolean intersects(LineSegment lineSegment) {
		final Vector2 pointA = lineSegment.pointA;
		final Vector2 pointB = lineSegment.pointB;

		if(contains(pointA.x, pointA.y)) {
			return true;
		}
		if(contains(pointB.x, pointB.y)) {
			return true;
		}

		final float minX = Math.min(pointA.x, pointB.x);
		if(minX > this.maxX) {
			return false;
		}

		final float minY = Math.min(pointA.y, pointB.y);
		if(minY > this.maxY) {
			return false;
		}

		final float maxX = Math.max(pointA.x, pointB.x);
		if(maxX < this.x) {
			return false;
		}

		final float maxY = Math.max(pointA.y, pointB.y);
		if(maxY < this.y) {
			return false;
		}

		VERTICES[0] = x;
		VERTICES[1] = y;
		VERTICES[2] = maxX;
		VERTICES[3] = y;
		VERTICES[4] = maxX;
		VERTICES[5] = maxY;
		VERTICES[6] = x;
		VERTICES[7] = maxY;
		return Intersector.intersectSegmentPolygon(pointA, pointB, VERTICES);
	}

	public boolean containedBy(Rectangle rectangle) {
		final float rectMinX = rectangle.getMinX();
		if(x < rectMinX) {
			return false;
		}

		final float rectMinY = rectangle.getMinY();
		if(y < rectMinY) {
			return false;
		}

		final float rectMaxX = rectangle.getMaxX();
		if(maxX > rectMaxX) {
			return false;
		}

		final float rectMaxY = rectangle.getMaxY();
		if(maxY > rectMaxY) {
			return false;
		}
		return true;
	}

	public boolean containedBy(float rectMinX, float rectMinY, float rectMaxX, float rectMaxY) {
		if(x < rectMinX) {
			return false;
		}
		if(y < rectMinY) {
			return false;
		}
		if(maxX > rectMaxX) {
			return false;
		}
		if(maxY > rectMaxY) {
			return false;
		}
		return true;
	}

	private QuadElementBounds() {
	}

	public static QuadElementBounds allocate() {
		synchronized(POOL) {
			if(POOL.size == 0) {
				return new QuadElementBounds();
			}
			return POOL.removeFirst();
		}
	}

	public void dispose() {
		synchronized(POOL) {
			POOL.addLast(this);
		}
	}
}
