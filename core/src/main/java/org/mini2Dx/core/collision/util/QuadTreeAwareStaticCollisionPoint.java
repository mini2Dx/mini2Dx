/**
 * Copyright 2021 Viridian Software Ltd.
 */
package org.mini2Dx.core.collision.util;

import org.mini2Dx.core.collision.Collisions;
import org.mini2Dx.core.collision.QuadTree;
import org.mini2Dx.core.collision.QuadTreeAware;
import org.mini2Dx.core.geom.Point;

public class QuadTreeAwareStaticCollisionPoint extends StaticCollisionPoint implements QuadTreeAware {
	private QuadTree quadTree;

	public QuadTreeAwareStaticCollisionPoint() {
	}

	public QuadTreeAwareStaticCollisionPoint(float x, float y) {
		super(x, y);
	}

	public QuadTreeAwareStaticCollisionPoint(Point point) {
		super(point);
	}

	public QuadTreeAwareStaticCollisionPoint(int id) {
		super(id);
	}

	public QuadTreeAwareStaticCollisionPoint(int id, Point point) {
		super(id, point);
	}

	public QuadTreeAwareStaticCollisionPoint(int id, float x, float y) {
		super(id, x, y);
	}

	public QuadTreeAwareStaticCollisionPoint(int id, Collisions collisions) {
		super(id, collisions);
	}

	@Override
	protected void release() {
		collisions.release(this);
	}

	@Override
	public void setQuad(QuadTree quadTree) {
		this.quadTree = quadTree;
	}

	@Override
	public QuadTree getQuad() {
		return quadTree;
	}
}
