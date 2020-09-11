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
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.geom.*;
import org.mini2Dx.core.lock.ReadWriteLock;
import org.mini2Dx.core.util.InterpolationTracker;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.math.Vector2;
import org.mini2Dx.gdx.utils.Array;

import java.util.Objects;

/**
 * An implementation of {@link Polygon} that allows for interpolation. Game
 * objects can use this class to move around the game world and retrieve the
 * appropriate rendering coordinates during the render phase.
 */
public class CollisionPolygon extends Polygon implements CollisionArea,
		PositionChangeListener<CollisionPolygon>, SizeChangeListener<CollisionPolygon> {

	private final ReadWriteLock positionChangeListenerLock;
	private final ReadWriteLock sizeChangeListenerLock;

	private final Polygon previousPolygon;
	private final Polygon renderPolygon;

	protected Collisions collisions = null;
	private int id;

	private RenderCoordMode renderCoordMode = RenderCoordMode.GLOBAL_DEFAULT;
	private int renderX, renderY, renderWidth, renderHeight;
	private boolean interpolateRequired = false;

	public CollisionPolygon(float [] vertices) {
		this(CollisionIdSequence.nextId(), vertices);
	}

	public CollisionPolygon(Vector2[] vectors) {
		this(CollisionIdSequence.nextId(), vectors);
	}

	public CollisionPolygon(int id, float[] vertices) {
		super(vertices);

		positionChangeListenerLock = Mdx.locks.newReadWriteLock();
		sizeChangeListenerLock = Mdx.locks.newReadWriteLock();
		addPostionChangeListener(this);
		addSizeChangeListener(this);

		previousPolygon = Mdx.geom.polygon(vertices);
		renderPolygon = Mdx.geom.polygon(vertices);

		init(id, vertices);
	}

	public CollisionPolygon(int id, Vector2[] vectors) {
		super(vectors);

		positionChangeListenerLock = Mdx.locks.newReadWriteLock();
		sizeChangeListenerLock = Mdx.locks.newReadWriteLock();
		addPostionChangeListener(this);
		addSizeChangeListener(this);

		previousPolygon = Mdx.geom.polygon();
		renderPolygon = Mdx.geom.polygon();

		init(id, vectors);
	}

	public CollisionPolygon(int id, Collisions collisions, float [] vertices) {
		this(id, vertices);
		this.collisions = collisions;
	}

	public CollisionPolygon(int id, Collisions collisions, Vector2[] vectors) {
		this(id, vectors);
		this.collisions = collisions;
	}

	protected void init(int id, float[] vertices) {
		this.id = id;

		disposed = false;

		InterpolationTracker.register(this);
		forceTo(vertices);
	}

	protected void init(int id, Vector2[] vectors) {
		this.id = id;

		disposed = false;
		InterpolationTracker.register(this);

		setVertices(vectors);
		previousPolygon.setVertices(vectors);
		renderPolygon.setVertices(vectors);
	}

	private void storeRenderCoordinates() {
		renderX = renderCoordMode.apply(renderPolygon.getX());
		renderY = renderCoordMode.apply(renderPolygon.getY());
		renderWidth = renderCoordMode.apply(renderPolygon.getWidth());
		renderHeight = renderCoordMode.apply(renderPolygon.getHeight());
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
		previousPolygon.dispose();
		renderPolygon.dispose();
	}

	@Override
	public void preUpdate() {
		previousPolygon.set(this);
	}

	@Override
	public void interpolate(float alpha) {
		if(!interpolateRequired) {
			return;
		}
		if(Mdx.timestepMode.equals(TimestepMode.DEFAULT)) {
			renderPolygon.set(this);
			storeRenderCoordinates();
			interpolateRequired = false;
			return;
		}
		previousPolygon.lerp(renderPolygon,this, alpha);
		storeRenderCoordinates();
		if(renderX != MathUtils.round(this.getX())) {
			return;
		}
		if(renderY != MathUtils.round(this.getY())) {
			return;
		}
		interpolateRequired = false;
	}

	@Override
	public void forceTo(float x, float y) {
		super.setXY(x, y);
		previousPolygon.set(this);
		renderPolygon.set(previousPolygon);
		storeRenderCoordinates();
		interpolateRequired = false;
	}

	@Override
	public CollisionArea setTo(float x, float y, float width, float height) {
		throw new MdxException("#setTo(x, y, width, height) not supported on CollisionPolygon");
	}

	@Override
	public void forceTo(float x, float y, float width, float height) {
		throw new MdxException("#forceTo(x, y, width, height) not supported on CollisionPolygon");
	}

	@Override
	public void forceToWidth(float width) {
		throw new MdxException("#forceToWidth(width) not supported on CollisionPolygon");
	}

	@Override
	public void forceToHeight(float height) {
		throw new MdxException("#forceToHeight(float height) not supported on CollisionPolygon");
	}

	public void forceTo(float [] vertices) {
		setVertices(vertices);
		previousPolygon.set(this);
		renderPolygon.set(previousPolygon);
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
	public float getRawRenderX() {
		return renderPolygon.getX();
	}

	@Override
	public float getRawRenderY() {
		return renderPolygon.getY();
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
		return renderPolygon.getWidth();
	}

	@Override
	public float getRawRenderHeight() {
		return renderPolygon.getHeight();
	}

	public int getRenderX(int index) {
		return MathUtils.round(renderPolygon.getX(index));
	}

	public int getRenderY(int index) {
		return MathUtils.round(renderPolygon.getY(index));
	}

	@Override
	public void positionChanged(CollisionPolygon moved) {
		if(moved.getId() != id) {
			return;
		}
		interpolateRequired = true;
	}

	@Override
	public void sizeChanged(CollisionPolygon changed) {
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
			positionChangeListeners = new Array<PositionChangeListener>(true, 1);
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
		if (!(o instanceof CollisionPolygon)) return false;
		if (!super.equals(o)) return false;
		CollisionPolygon that = (CollisionPolygon) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "CollisionPolygon{" +
				"id=" + id +
				'}';
	}
}
