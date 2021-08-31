/**
 * Copyright 2021 Viridian Software Ltd.
 */
package org.mini2Dx.core.geom;

import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.gdx.utils.Queue;

public class PolygonEdgeIterator extends EdgeIterator {
	private static final Queue<PolygonEdgeIterator> POOL = new Queue<>();

	private int edge = 0;
	private final LineSegment edgeLineSegment;

	private Polygon polygon;

	private PolygonEdgeIterator() {
		this(new LineSegment(0f, 0f, 1f, 1f));
	}

	private PolygonEdgeIterator(LineSegment edgeLineSegment) {
		this.edgeLineSegment = edgeLineSegment;
	}

	public static PolygonEdgeIterator allocate(Polygon polygon) {
		final PolygonEdgeIterator iterator;
		synchronized(POOL) {
			if(POOL.size == 0) {
				iterator = new PolygonEdgeIterator();
			} else {
				iterator = POOL.removeFirst();
			}
		}
		iterator.polygon = polygon;
		return iterator;
	}

	@Override
	protected void beginIteration() {
		edge = -1;
	}

	@Override
	protected void endIteration() {
		synchronized(POOL) {
			polygon = null;
			POOL.addLast(this);
		}
	}

	@Override
	protected void nextEdge() {
		if (edge >= polygon.getNumberOfSides()) {
			throw new MdxException("No more edges remaining. Make sure to call end()");
		}
		edge++;
		if (!hasNext()) {
			return;
		}
		edgeLineSegment.set(getPointAX(), getPointAY(), getPointBX(), getPointBY());
	}

	@Override
	public boolean hasNext() {
		return edge < polygon.getNumberOfSides() - 1;
	}

	@Override
	public float getPointAX() {
		if (edge < 0) {
			throw new MdxException("Make sure to call next() after beginning iteration");
		}
		return polygon.vertices[edge * 2];
	}

	@Override
	public float getPointAY() {
		if (edge < 0) {
			throw new MdxException("Make sure to call next() after beginning iteration");
		}
		return polygon.vertices[(edge * 2) + 1];
	}

	@Override
	public float getPointBX() {
		if (edge < 0) {
			throw new MdxException("Make sure to call next() after beginning iteration");
		}
		if (edge == polygon.getNumberOfSides() - 1) {
			return polygon.vertices[0];
		}
		return polygon.vertices[(edge + 1) * 2];
	}

	@Override
	public float getPointBY() {
		if (edge < 0) {
			throw new MdxException("Make sure to call next() after beginning iteration");
		}
		if (edge == polygon.getNumberOfSides() - 1) {
			return polygon.vertices[1];
		}
		return polygon.vertices[((edge + 1) * 2) + 1];
	}

	@Override
	public LineSegment getEdgeLineSegment() {
		return edgeLineSegment;
	}
}
