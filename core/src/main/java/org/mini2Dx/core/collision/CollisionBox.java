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
import org.mini2Dx.core.TimestepMode;
import org.mini2Dx.core.geom.*;
import org.mini2Dx.core.lock.ReadWriteLock;
import org.mini2Dx.core.util.InterpolationTracker;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.utils.Array;

import java.util.Objects;

/**
 * An implementation of {@link Rectangle} that allows for interpolation. Game
 * objects can use this class to move around the game world and retrieve the
 * appropriate rendering coordinates during the render phase.
 */
public class CollisionBox extends Rectangle implements CollisionArea,
		PositionChangeListener<CollisionBox>, SizeChangeListener<CollisionBox> {
	private final ReadWriteLock positionChangeListenerLock;
	private final ReadWriteLock sizeChangeListenerLock;

	private final Rectangle previousRectangle;
	private final Rectangle renderRectangle;

	protected Collisions collisions = null;
	private int id;

	private RenderCoordMode renderCoordMode = RenderCoordMode.GLOBAL_DEFAULT;
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

		positionChangeListenerLock = Mdx.locks.newReadWriteLock();
		sizeChangeListenerLock = Mdx.locks.newReadWriteLock();
		addPostionChangeListener(this);
		addSizeChangeListener(this);

		previousRectangle = Mdx.geom.rectangle();
		renderRectangle = Mdx.geom.rectangle();

		init(id, x, y, width, height);
	}

	public CollisionBox(int id, Collisions collisions) {
		this(id);
		this.collisions = collisions;
	}

	protected void init(int id, float x, float y, float width, float height) {
		this.id = id;

		disposed = false;

		InterpolationTracker.register(this);
		forceTo(x, y, width, height);
	}

	private void storeRenderCoordinates() {
		renderX = renderCoordMode.apply(renderRectangle.getX());
		renderY = renderCoordMode.apply(renderRectangle.getY());
		renderWidth = renderCoordMode.apply(renderRectangle.getWidth());
		renderHeight = renderCoordMode.apply(renderRectangle.getHeight());
	}

	protected void release() {
		collisions.release(this);
	}

	@Override
	public void dispose() {
		if(disposed) {
			return;
		}

		InterpolationTracker.deregister(this);

		if(collisions != null) {
			clearPositionChangeListeners();
			clearSizeChangeListeners();

			addPostionChangeListener(this);
			addSizeChangeListener(this);

			disposed = true;
			release();
			return;
		}

		super.dispose();
		previousRectangle.dispose();
		renderRectangle.dispose();
	}

	@Override
	public void forceTo(float x, float y) {
		forceTo(x, y, getWidth(), getHeight());
	}

	@Override
	public CollisionArea setTo(float x, float y, float width, float height) {
		set(x, y, width, height);
		return this;
	}

	@Override
	public void forceTo(float x, float y, float width, float height) {
		super.set(x, y, width, height);
		previousRectangle.set(x, y, width, height);
		renderRectangle.set(previousRectangle);
		storeRenderCoordinates();
		interpolateRequired = false;
	}

	/**
	 * Sets the current width to the specified width and force updates the
	 * rendering bounds to match
	 *
	 * @param width
	 *            The width to set
	 */
	public void forceToWidth(float width) {
		super.setWidth(width);
		previousRectangle.set(this);
		renderRectangle.set(this);
		storeRenderCoordinates();
		notifySizeChangeListeners();
	}

	/**
	 * Sets the current height to the specified height and force updates the
	 * rendering bounds to match
	 *
	 * @param height
	 *            The height to set
	 */
	public void forceToHeight(float height) {
		super.setHeight(height);
		previousRectangle.set(this);
		renderRectangle.set(this);
		storeRenderCoordinates();
		notifySizeChangeListeners();
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
	public float getRawRenderWidth() {
		return renderRectangle.getWidth();
	}

	@Override
	public float getRawRenderHeight() {
		return renderRectangle.getHeight();
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
		if(Mdx.timestepMode.equals(TimestepMode.DEFAULT)) {
			renderRectangle.set(this);
			storeRenderCoordinates();
			interpolateRequired = false;
			return;
		}
		previousRectangle.lerp(renderRectangle, this, alpha);
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
	public float getRawRenderX() {
		return renderRectangle.getX();
	}

	@Override
	public float getRawRenderY() {
		return renderRectangle.getY();
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

	@Override
	public <T extends Positionable> void addPostionChangeListener(
			PositionChangeListener<T> listener) {
		positionChangeListenerLock.lockWrite();
		if (positionChangeListeners == null) {
			positionChangeListeners = new Array<PositionChangeListener>(true,1);
		}
		positionChangeListeners.add(listener);
		positionChangeListenerLock.unlockWrite();
	}

	@Override
	public <T extends Positionable> void removePositionChangeListener(
			PositionChangeListener<T> listener) {
		removePositionListener(positionChangeListenerLock, positionChangeListeners, listener);
	}

	@Override
	public <T extends Sizeable> void addSizeChangeListener(SizeChangeListener<T> listener) {
		sizeChangeListenerLock.lockWrite();
		if (sizeChangeListeners == null) {
			sizeChangeListeners = new Array<SizeChangeListener>(true,1);
		}
		sizeChangeListeners.add(listener);
		sizeChangeListenerLock.unlockWrite();
	}

	@Override
	public <T extends Sizeable> void removeSizeChangeListener(SizeChangeListener<T> listener) {
		removeSizeListener(sizeChangeListenerLock, sizeChangeListeners, listener);
	}

	@Override
	protected void notifyPositionChangeListeners() {
		notifyPositionListeners(positionChangeListenerLock, positionChangeListeners, this);
	}

	@Override
	protected void clearPositionChangeListeners() {
		clearPositionListeners(positionChangeListenerLock, positionChangeListeners);
	}

	@Override
	protected void notifySizeChangeListeners() {
		notifySizeListeners(sizeChangeListenerLock, sizeChangeListeners, this);
	}

	@Override
	protected void clearSizeChangeListeners() {
		clearSizeListeners(sizeChangeListenerLock, sizeChangeListeners);
	}

	public boolean isInterpolateRequired() {
		return interpolateRequired;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		CollisionBox that = (CollisionBox) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "CollisionBox [id=" + id + ", x=" + getX() + ", y=" + getY() + ", width="
				+ getWidth() + ", height=" + getHeight() + ", getRotation()=" + getRotation() + ", renderRectangle=" + renderRectangle + "]";
	}
}
