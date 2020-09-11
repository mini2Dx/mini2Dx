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

import org.mini2Dx.core.collision.CollisionIdSequence;
import org.mini2Dx.core.collision.CollisionObject;
import org.mini2Dx.core.collision.Collisions;
import org.mini2Dx.core.collision.RenderCoordMode;
import org.mini2Dx.core.geom.Point;

import java.util.Objects;

/**
 * An implementation of a collision point that does not register as an interpolating collision. Due to this,
 * memory and CPU overhead are reduced compared to using the {@link org.mini2Dx.core.collision.CollisionPoint} implementation.
 */
public class StaticCollisionPoint extends Point implements CollisionObject {
	private int id;
	private Collisions collisions = null;

	private RenderCoordMode renderCoordMode = RenderCoordMode.GLOBAL_DEFAULT;

	public StaticCollisionPoint() {
		this(CollisionIdSequence.nextId());
	}

	public StaticCollisionPoint(float x, float y) {
		this(CollisionIdSequence.nextId(), x, y);
	}

	public StaticCollisionPoint(Point point) {
		this(CollisionIdSequence.nextId(), point);
	}

	public StaticCollisionPoint(int id) {
		this(id, 0f, 0f);
	}

	public StaticCollisionPoint(int id, Point point) {
		this(id, point.getX(), point.getY());
	}

	public StaticCollisionPoint(int id, float x, float y) {
		super(x, y);
		init(id, x, y);
	}

	public StaticCollisionPoint(int id, Collisions collisions) {
		this(id);
		this.collisions = collisions;
	}

	public void init(int id, float x, float y) {
		this.id = id;

		disposed = false;
		forceTo(x, y);
	}

	@Override
	public void dispose() {
		if(disposed) {
			return;
		}

		if(collisions != null) {
			clearPositionChangeListeners();

			disposed = true;
			collisions.release(this);
			return;
		}
		super.dispose();
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
		set(x, y);
	}

	@Override
	public int getRenderX() {
		return renderCoordMode.apply(x);
	}

	@Override
	public int getRenderY() {
		return renderCoordMode.apply(y);
	}

	@Override
	public float getRawRenderX() {
		return x;
	}

	@Override
	public float getRawRenderY() {
		return y;
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
		StaticCollisionPoint that = (StaticCollisionPoint) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
