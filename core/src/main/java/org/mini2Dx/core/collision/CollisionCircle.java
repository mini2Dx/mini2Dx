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
import org.mini2Dx.core.geom.*;
import org.mini2Dx.core.lock.ReadWriteLock;
import org.mini2Dx.core.util.InterpolationTracker;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.utils.Array;

import java.util.Objects;

/**
 * An implementation of {@link Circle} that allows for interpolation. Game
 * objects can use this class to move around the game world and retrieve the
 * appropriate rendering coordinates during the render phase.
 */
public class CollisionCircle extends Circle implements CollisionArea,
		PositionChangeListener<CollisionCircle>, SizeChangeListener<CollisionCircle> {
	private final int id;
	private final ReadWriteLock positionChangeListenerLock;
	private final ReadWriteLock sizeChangeListenerLock;

	private final Circle previousCircle;
	private final Circle renderCircle;

	private int renderX, renderY, renderRadius;
	private boolean interpolateRequired = false;

	public CollisionCircle(float radius) {
		this(CollisionIdSequence.nextId(), radius);
	}

	public CollisionCircle(int id, float radius) {
		this(id, 0f, 0f, radius);
	}

	public CollisionCircle(float centerX, float centerY, float radius) {
		this(CollisionIdSequence.nextId(), centerX, centerY, radius);
	}

	public CollisionCircle(int id, float centerX, float centerY, float radius) {
		super(centerX, centerY, radius);
		this.id = id;

		positionChangeListenerLock = Mdx.locks.newReadWriteLock();
		sizeChangeListenerLock = Mdx.locks.newReadWriteLock();
		addPostionChangeListener(this);
		addSizeChangeListener(this);

		InterpolationTracker.register(this);

		previousCircle = Mdx.geom.circle();
		previousCircle.setXY(centerX, centerY);
		previousCircle.setRadius(radius);

		renderCircle = new Circle(centerX, centerY, radius);
		renderCircle.setXY(centerX, centerY);
		renderCircle.setRadius(radius);

		storeRenderCoordinates();
	}

	private void storeRenderCoordinates() {
		renderX = MathUtils.round(renderCircle.getX());
		renderY = MathUtils.round(renderCircle.getY());
		renderRadius = MathUtils.round(renderCircle.getRadius());
	}

	@Override
	public void dispose() {
		InterpolationTracker.deregister(this);
		super.dispose();
		previousCircle.dispose();
		renderCircle.dispose();
	}

	@Override
	public void preUpdate() {
		previousCircle.set(this);
	}

	@Override
	public void interpolate(float alpha) {
		if(!interpolateRequired) {
			return;
		}
		renderCircle.set(previousCircle.lerp(this, alpha));
		storeRenderCoordinates();
		if(renderX != MathUtils.round(getX())) {
			return;
		}
		if(renderY != MathUtils.round(getY())) {
			return;
		}
		if(renderRadius != MathUtils.round(getRadius())) {
			return;
		}
		interpolateRequired = false;
	}

	@Override
	public void forceTo(float x, float y, float width, float height) {
		super.setXY(x, y);
		super.setRadius(width * 0.5f);

		previousCircle.setXY(x, y);
		previousCircle.setRadius(width * 0.5f);

		renderCircle.setXY(x, y);
		renderCircle.setRadius(width * 0.5f);

		storeRenderCoordinates();
		interpolateRequired = false;
	}

	@Override
	public void forceTo(float x, float y) {
		super.setXY(x, y);
		previousCircle.setXY(x, y);
		renderCircle.setXY(x, y);
		storeRenderCoordinates();
		interpolateRequired = false;
	}

	@Override
	public int getId() {
		return id;
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
	public int getRenderWidth() {
		return renderRadius * 2;
	}

	@Override
	public int getRenderHeight() {
		return renderRadius * 2;
	}

	public int getRenderRadius() {
		return renderRadius;
	}

	@Override
	public void positionChanged(CollisionCircle moved) {
		if(moved.getId() != id) {
			return;
		}
		interpolateRequired = true;
	}

	@Override
	public void sizeChanged(CollisionCircle changed) {
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CollisionCircle)) return false;
		if (!super.equals(o)) return false;
		CollisionCircle that = (CollisionCircle) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "CollisionCircle [id=" + id + ", x=" + getX() + ", y=" + getY() + ", radius="
				+ getRadius() + ", renderCircle=" + renderCircle + "]";
	}
}
