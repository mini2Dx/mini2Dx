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
import org.mini2Dx.core.geom.Rectangle;

import java.util.Objects;

/**
 * An implementation of a collision box that does not register as an interpolating collision. Due to this,
 * memory and CPU overhead are reduced compared to using the {@link org.mini2Dx.core.collision.CollisionBox} implementation.
 */
public class StaticCollisionBox extends Rectangle implements CollisionArea {
	private Collisions collisions = null;
	private int id;

	private RenderCoordMode renderCoordMode = RenderCoordMode.GLOBAL_DEFAULT;

	public StaticCollisionBox() {
		this(CollisionIdSequence.nextId());
	}

	public StaticCollisionBox(float x, float y, float width, float height) {
		this(CollisionIdSequence.nextId(), x, y, width, height);
	}

	public StaticCollisionBox(Rectangle rectangle) {
		this(CollisionIdSequence.nextId(), rectangle);
	}

	public StaticCollisionBox(int id) {
		this(id, 0f, 0f, 1f, 1f);
	}

	public StaticCollisionBox(int id, Rectangle rectangle) {
		this(id, rectangle.getX(), rectangle.getY(),
				rectangle.getWidth(), rectangle.getHeight());
	}

	public StaticCollisionBox(int id, float x, float y, float width, float height) {
		super(x, y, width, height);
		this.id = id;
		init(id, x, y, width, height);
	}

	public StaticCollisionBox(int id, Collisions collisions) {
		this(id);
		this.collisions = collisions;
	}

	public void init(int id, float x, float y, float width, float height) {
		this.id = id;
		disposed = false;
		forceTo(x, y, width, height);
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
	public void forceTo(float x, float y, float width, float height) {
		set(x, y, width, height);
	}

	@Override
	public void forceToWidth(float width) {
		setWidth(width);
	}

	@Override
	public void forceToHeight(float height) {
		setHeight(height);
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
		set(x, y, getWidth(), getHeight());
	}

	@Override
	public CollisionArea setTo(float x, float y, float width, float height) {
		set(x, y, width, height);
		return this;
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
		StaticCollisionBox that = (StaticCollisionBox) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
