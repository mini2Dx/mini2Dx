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
import org.mini2Dx.core.collision.RenderCoordMode;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.geom.Polygon;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.math.Vector2;

/**
 * An implementation of a collision polygon that will not move between updates/frames, thus, does not register as an interpolating collision. Due to this,
 * memory and CPU overhead are reduced compared to using the {@link org.mini2Dx.core.collision.CollisionPolygon} implementation.
 */
public class StaticCollisionPolygon extends Polygon implements CollisionArea {
	private final int id;

	private RenderCoordMode renderCoordMode = RenderCoordMode.GLOBAL_DEFAULT;

	public StaticCollisionPolygon(float[] vertices) {
		this(CollisionIdSequence.nextId(), vertices);
	}

	public StaticCollisionPolygon(Vector2[] points) {
		this(CollisionIdSequence.nextId(), points);
	}

	public StaticCollisionPolygon(int id, float[] vertices) {
		super(vertices);
		this.id = id;
	}

	public StaticCollisionPolygon(int id, Vector2[] points) {
		super(points);
		this.id = id;
	}

	@Override
	public void forceTo(float x, float y, float width, float height) {
		throw new MdxException("#forceTo(x, y, width, height) not supported on CollisionPolygon");
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
	public int getRenderWidth() {
		return renderCoordMode.apply(getWidth());
	}

	@Override
	public int getRenderHeight() {
		return renderCoordMode.apply(getHeight());
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
