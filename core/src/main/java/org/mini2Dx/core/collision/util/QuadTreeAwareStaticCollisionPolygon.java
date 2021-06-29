/**
 * Copyright 2021 Viridian Software Ltd.
 */
package org.mini2Dx.core.collision.util;

import org.mini2Dx.core.collision.Collisions;
import org.mini2Dx.core.collision.QuadTree;
import org.mini2Dx.core.collision.QuadTreeAware;
import org.mini2Dx.gdx.math.Vector2;

public class QuadTreeAwareStaticCollisionPolygon extends StaticCollisionPolygon implements QuadTreeAware {
	private QuadTree quadTree;

	public QuadTreeAwareStaticCollisionPolygon(float[] vertices) {
		super(vertices);
	}

	public QuadTreeAwareStaticCollisionPolygon(Vector2[] points) {
		super(points);
	}

	public QuadTreeAwareStaticCollisionPolygon(int id, float[] vertices) {
		super(id, vertices);
	}

	public QuadTreeAwareStaticCollisionPolygon(int id, Vector2[] points) {
		super(id, points);
	}

	public QuadTreeAwareStaticCollisionPolygon(int id, Collisions collisions, float[] vertices) {
		super(id, collisions, vertices);
	}

	public QuadTreeAwareStaticCollisionPolygon(int id, Collisions collisions, Vector2[] points) {
		super(id, collisions, points);
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
