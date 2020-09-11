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
import org.mini2Dx.core.geom.Circle;

import java.util.Objects;

/**
 * An implementation of a collision circle that does not register as an interpolating collision. Due to this,
 * memory and CPU overhead are reduced compared to using the {@link org.mini2Dx.core.collision.CollisionCircle} implementation.
 */
public class StaticCollisionCircle extends Circle implements CollisionArea {
	private int id;
	private Collisions collisions = null;

	private RenderCoordMode renderCoordMode = RenderCoordMode.GLOBAL_DEFAULT;

	public StaticCollisionCircle(float radius) {
		this(CollisionIdSequence.nextId(), radius);
	}

	public StaticCollisionCircle(float centerX, float centerY, float radius) {
		this(CollisionIdSequence.nextId(), centerX, centerY, radius);
	}

	public StaticCollisionCircle(Circle circle) {
		this(CollisionIdSequence.nextId(), circle);
	}

	public StaticCollisionCircle(int id, float radius) {
		this(id, 0f, 0f, radius);
	}

	public StaticCollisionCircle(int id, Circle circle) {
		this(id, circle.getX(), circle.getY(), circle.getRadius());
	}

	public StaticCollisionCircle(int id, Collisions collisions) {
		this(id, 0f, 0f, 1f);
		this.collisions = collisions;
	}

	public StaticCollisionCircle(int id, float centerX, float centerY, float radius) {
		super(centerX, centerY, radius);
		init(id, centerX, centerY, radius);
	}

	public void init(int id, float centerX, float centerY, float radius) {
		this.id = id;
		disposed = false;
		setXY(centerX, centerY);
		setRadius(radius);
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
		setXY(x, y);
		setRadius(width * 0.5f);
		return this;
	}

	@Override
	public void forceTo(float x, float y, float width, float height) {
		setXY(x, y);
		setRadius(width * 0.5f);
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
	public void forceToWidth(float width) {
		setRadius(width * 0.5f);
	}

	@Override
	public void forceToHeight(float height) {
		setRadius(height * 0.5f);
	}

	@Override
	public int getRenderX() {
		return renderCoordMode.apply(getX());
	}

	@Override
	public int getRenderY() {
		return renderCoordMode.apply(getY());
	}

	public int getRenderRadius() {
		return renderCoordMode.apply(getRadius());
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
		StaticCollisionCircle that = (StaticCollisionCircle) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
