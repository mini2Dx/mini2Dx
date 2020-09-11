/*******************************************************************************
 * Copyright 2019 Viridian Software Limited
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
package org.mini2Dx.core.collision.util;

import org.mini2Dx.core.collision.CollisionArea;
import org.mini2Dx.core.collision.CollisionIdSequence;
import org.mini2Dx.core.collision.Collisions;
import org.mini2Dx.core.collision.RenderCoordMode;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.geom.Polygon;
import org.mini2Dx.gdx.math.Vector2;

import java.util.Objects;

/**
 * An implementation of a collision polygon that does not register as an interpolating collision. Due to this,
 * memory and CPU overhead are reduced compared to using the {@link org.mini2Dx.core.collision.CollisionPolygon} implementation.
 */
public class StaticCollisionPolygon extends Polygon implements CollisionArea {
	private int id;
	private Collisions collisions = null;

	private RenderCoordMode renderCoordMode = RenderCoordMode.GLOBAL_DEFAULT;

	public StaticCollisionPolygon(float[] vertices) {
		this(CollisionIdSequence.nextId(), vertices);
	}

	public StaticCollisionPolygon(Vector2[] points) {
		this(CollisionIdSequence.nextId(), points);
	}

	public StaticCollisionPolygon(int id, float[] vertices) {
		super(vertices);
		init(id, vertices);
	}

	public StaticCollisionPolygon(int id, Vector2[] points) {
		super(points);
		init(id, points);
	}

	public StaticCollisionPolygon(int id, Collisions collisions, float [] vertices) {
		this(id, vertices);
		this.collisions = collisions;
	}

	public StaticCollisionPolygon(int id, Collisions collisions, Vector2[] points) {
		this(id, points);
		this.collisions = collisions;
	}

	public void init(int id, float [] vertices) {
		this.id = id;

		disposed = false;
		setVertices(vertices);
	}

	public void init(int id, Vector2[] points) {
		this.id = id;

		disposed = false;
		setVertices(points);
	}

	@Override
	public void dispose() {
		if(disposed) {
			return;
		}

		if(collisions != null) {
			clearPositionChangeListeners();
			clearSizeChangeListeners();

			disposed = true;
			collisions.release(this);
			return;
		}
		super.dispose();
	}

	@Override
	public CollisionArea setTo(float x, float y, float width, float height) {
		throw new MdxException("#setTo(x, y, width, height) not supported on StaticCollisionPolygon");
	}

	@Override
	public void forceTo(float x, float y, float width, float height) {
		throw new MdxException("#forceTo(x, y, width, height) not supported on StaticCollisionPolygon");
	}

	@Override
	public void forceToWidth(float width) {
		throw new MdxException("#forceToWidth(width) not supported on StaticCollisionPolygon");
	}

	@Override
	public void forceToHeight(float height) {
		throw new MdxException("#forceToHeight(float height) not supported on StaticCollisionPolygon");
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void preUpdate() {
	}

	@Override
	public void interpolate(float alpha) {
	}

	@Override
	public void forceTo(float x, float y) {
		setXY(x, y);
	}

	@Override
	public int getRenderX() {
		return renderCoordMode.apply(getX());
	}

	@Override
	public int getRenderY() {
		return renderCoordMode.apply(getY());
	}

	@Override
	public float getRawRenderX() {
		return getX();
	}

	@Override
	public float getRawRenderY() {
		return getY();
	}

	@Override
	public int getRenderWidth() {
		return renderCoordMode.apply(getWidth());
	}

	@Override
	public int getRenderHeight() {
		return renderCoordMode.apply(getHeight());
	}

	@Override
	public float getRawRenderWidth() {
		return getWidth();
	}

	@Override
	public float getRawRenderHeight() {
		return getHeight();
	}

	@Override
	public RenderCoordMode getRenderCoordMode() {
		return renderCoordMode;
	}

	@Override
	public void setRenderCoordMode(RenderCoordMode mode) {
		if(mode == null) {
			return;
		}
		this.renderCoordMode = mode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		StaticCollisionPolygon that = (StaticCollisionPolygon) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
