/**
 * Copyright 2021 Viridian Software Ltd.
 */
package org.mini2Dx.core.collision.util;

import org.mini2Dx.core.collision.Collisions;
import org.mini2Dx.core.collision.QuadTree;
import org.mini2Dx.core.collision.QuadTreeAware;
import org.mini2Dx.core.geom.Circle;

public class QuadTreeAwareStaticCollisionCircle extends StaticCollisionCircle implements QuadTreeAware {
	private QuadTree quadTree;

	public QuadTreeAwareStaticCollisionCircle(float radius) {
		super(radius);
	}

	public QuadTreeAwareStaticCollisionCircle(float centerX, float centerY, float radius) {
		super(centerX, centerY, radius);
	}

	public QuadTreeAwareStaticCollisionCircle(Circle circle) {
		super(circle);
	}

	public QuadTreeAwareStaticCollisionCircle(int id, float radius) {
		super(id, radius);
	}

	public QuadTreeAwareStaticCollisionCircle(int id, Circle circle) {
		super(id, circle);
	}

	public QuadTreeAwareStaticCollisionCircle(int id, Collisions collisions) {
		super(id, collisions);
	}

	public QuadTreeAwareStaticCollisionCircle(int id, float centerX, float centerY, float radius) {
		super(id, centerX, centerY, radius);
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
