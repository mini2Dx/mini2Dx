/**
 * Copyright 2021 Viridian Software Ltd.
 */
package org.mini2Dx.core.collision.util;

import org.mini2Dx.core.collision.Collisions;
import org.mini2Dx.core.collision.QuadTree;
import org.mini2Dx.core.collision.QuadTreeAware;
import org.mini2Dx.core.geom.Rectangle;

public class QuadTreeAwareStaticCollisionBox extends StaticCollisionBox implements QuadTreeAware {
	private QuadTree quadTree;

	public QuadTreeAwareStaticCollisionBox() {
	}

	public QuadTreeAwareStaticCollisionBox(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	public QuadTreeAwareStaticCollisionBox(Rectangle rectangle) {
		super(rectangle);
	}

	public QuadTreeAwareStaticCollisionBox(int id) {
		super(id);
	}

	public QuadTreeAwareStaticCollisionBox(int id, Rectangle rectangle) {
		super(id, rectangle);
	}

	public QuadTreeAwareStaticCollisionBox(int id, float x, float y, float width, float height) {
		super(id, x, y, width, height);
	}

	public QuadTreeAwareStaticCollisionBox(int id, Collisions collisions) {
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
