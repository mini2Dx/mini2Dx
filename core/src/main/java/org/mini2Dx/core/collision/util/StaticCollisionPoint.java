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
import org.mini2Dx.core.collision.RenderCoordMode;
import org.mini2Dx.core.geom.Point;
import org.mini2Dx.gdx.math.MathUtils;

/**
 * An implementation of a collision point that will not move between updates/frames, thus, does not register as an interpolating collision. Due to this,
 * memory and CPU overhead are reduced compared to using the {@link org.mini2Dx.core.collision.CollisionPoint} implementation.
 */
public class StaticCollisionPoint extends Point implements CollisionObject {
	private final int id;

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
		this.id = id;
	}

	public StaticCollisionPoint(int id, float x, float y) {
		super(x, y);
		this.id = id;
	}

	public StaticCollisionPoint(int id, Point point) {
		super(point);
		this.id = id;
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
}
