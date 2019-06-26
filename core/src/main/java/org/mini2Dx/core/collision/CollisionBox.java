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
package org.mini2Dx.core.collision;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.geom.PositionChangeListener;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.geom.SizeChangeListener;
import org.mini2Dx.core.util.InterpolationTracker;
import org.mini2Dx.gdx.math.MathUtils;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * An implementation of {@link Rectangle} that allows for interpolation. Game
 * objects can use this class to move around the game world and retrieve the
 * appropriate rendering coordinates during the render phase.
 */
public class CollisionBox extends Rectangle implements CollisionArea,
		PositionChangeListener<CollisionBox>, SizeChangeListener<CollisionBox> {
	private final int id;
	private final ReentrantReadWriteLock positionChangeListenerLock;
	private final ReentrantReadWriteLock sizeChangeListenerLock;

	private final Rectangle previousRectangle;
	private final Rectangle renderRectangle;

	private int renderX, renderY, renderWidth, renderHeight;
	private boolean interpolateRequired = false;

	public CollisionBox() {
		this(0f, 0f, 1f, 1f);
	}

	public CollisionBox(int id) {
		this(id, 0f, 0f, 1f, 1f);
	}

	public CollisionBox(float x, float y, float width, float height) {
		this(CollisionIdSequence.nextId(), x, y, width, height);
	}

	public CollisionBox(int id, float x, float y, float width, float height) {
		super(x, y, width, height);
		this.id = id;
		addPostionChangeListener(this);
		addSizeChangeListener(this);

		InterpolationTracker.register(this);

		positionChangeListenerLock = new ReentrantReadWriteLock();
		sizeChangeListenerLock = new ReentrantReadWriteLock();
		previousRectangle = Mdx.geom.rectangle();
		previousRectangle.set(x, y, width, height);

		renderRectangle = new Rectangle();
		renderRectangle.set(x, y, width, height);

		storeRenderCoordinates();
	}

	private void storeRenderCoordinates() {
		renderX = MathUtils.round(renderRectangle.getX());
		renderY = MathUtils.round(renderRectangle.getY());
		renderWidth = MathUtils.round(renderRectangle.getWidth());
		renderHeight = MathUtils.round(renderRectangle.getHeight());
	}

	@Override
	public void dispose() {
		InterpolationTracker.deregister(this);
		super.dispose();
		previousRectangle.dispose();
		renderRectangle.dispose();
	}

	@Override
	public void forceTo(float x, float y) {
		forceTo(x, y, getWidth(), getHeight());
	}

	@Override
	public void forceTo(float x, float y, float width, float height) {
		super.set(x, y, width, height);
		previousRectangle.set(x, y, width, height);
		renderRectangle.set(previousRectangle);
		interpolateRequired = false;
	}

	@Override
	public int getRenderWidth() {
		return renderWidth;
	}

	@Override
	public int getRenderHeight() {
		return renderHeight;
	}

	@Override
	public void preUpdate() {
		previousRectangle.set(this);
	}

	@Override
	public void interpolate(float alpha) {
		if(!interpolateRequired) {
			return;
		}
		renderRectangle.set(previousRectangle.lerp(this, alpha));
		storeRenderCoordinates();
		if(renderX != MathUtils.round(this.getX())) {
			return;
		}
		if(renderY != MathUtils.round(this.getY())) {
			return;
		}
		if(renderWidth != MathUtils.round(this.getWidth())) {
			return;
		}
		if(renderHeight != MathUtils.round(this.getHeight())) {
			return;
		}
		interpolateRequired = false;
	}

	@Override
	public int getRenderX() {
		return renderX;
	}

	@Override
	public int getRenderY() {
		return renderY;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void positionChanged(CollisionBox moved) {
		if(moved.getId() != id) {
			return;
		}
		interpolateRequired = true;
	}

	@Override
	public void sizeChanged(CollisionBox changed) {
		if(changed.getId() != id) {
			return;
		}
		interpolateRequired = true;
	}
}
