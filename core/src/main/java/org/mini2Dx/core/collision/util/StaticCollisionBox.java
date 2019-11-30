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
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.gdx.math.MathUtils;

/**
 * An implementation of a collision box that will not move between updates/frames, thus, does not register as an interpolating collision. Due to this,
 * memory and CPU overhead are reduced compared to using the {@link org.mini2Dx.core.collision.CollisionBox} implementation.
 */
public class StaticCollisionBox extends Rectangle implements CollisionArea {
	private final int id;

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
		this.id = id;
	}

	public StaticCollisionBox(int id, float x, float y, float width, float height) {
		super(x, y, width, height);
		this.id = id;
	}

	public StaticCollisionBox(int id, Rectangle rectangle) {
		super(rectangle);
		this.id = id;
	}

	@Override
	public void forceTo(float x, float y, float width, float height) {
		set(x, y, width, height);
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
	public int getRenderX() {
		return MathUtils.round(getX());
	}

	@Override
	public int getRenderY() {
		return MathUtils.round(getY());
	}

	@Override
	public int getRenderWidth() {
		return MathUtils.round(getWidth());
	}

	@Override
	public int getRenderHeight() {
		return MathUtils.round(getHeight());
	}
}
