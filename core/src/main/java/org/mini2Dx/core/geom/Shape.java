/*******************************************************************************
 * Copyright 2019 See AUTHORS file
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
package org.mini2Dx.core.geom;


import org.mini2Dx.core.Geometry;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.lock.ReadWriteLock;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.math.Vector2;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.Disposable;

/**
 * Base class for shapes
 */
public abstract class Shape implements Sizeable, Disposable {
	private static final Vector2 TMP_VECTOR = new Vector2();

	private static final Vector2 TMP_SOURCE_VECTOR = new Vector2();
	private static final Vector2 TMP_TARGET_VECTOR = new Vector2();

	protected final Geometry geometry;
	protected boolean disposed = false;

	protected Array<PositionChangeListener> positionChangeListeners;
	protected Array<SizeChangeListener> sizeChangeListeners;

	/**
	 * Constructor for shapes not belonging to the {@link Geometry} pool
	 */
	public Shape() {
		super();
		this.geometry = null;
	}

	/**
	 * Constructor for shapes belonging to the {@link Geometry} pool
	 * @param geometry The {@link Geometry} pool
	 */
	public Shape(Geometry geometry) {
		super();
		this.geometry = geometry;
	}

	/**
	 * INTERNAL USE ONLY
	 * @param disposed
	 */
	public void setDisposed(boolean disposed) {
		this.disposed = disposed;
	}

	/**
	 * Releases this {@link Shape} back to the {@link Geometry} pool (if it was created from the pool)
	 */
	public abstract void dispose();

	/**
	 * Returns an exact copy of this {@link Shape}
	 * 
	 * @return A copy (new) instance of this {@link Shape}
	 */
	public abstract Shape copy();

	/**
	 * Returns if this {@link Shape} intersects the specified
	 * {@link LineSegment}
	 * 
	 * @param lineSegment
	 *            The {@link LineSegment} to check
	 * @return True if this {@link Shape} intersects the {@link LineSegment}
	 */
	public boolean intersects(LineSegment lineSegment) {
		return intersectsLineSegment(lineSegment.getPointA(), lineSegment.getPointB());
	}

	/**
	 * Returns the distance from this {@link Shape} to a {@link Positionable}
	 *
	 * @param positionable The {@link Positionable} to get the distance to
	 * @return The distance
	 */
	public float getDistanceTo(Positionable positionable) {
		return getDistanceTo(positionable.getX(), positionable.getY());
	}
	
	/**
	 * Adds components to the position of this {@link Shape}
	 * @param x The x component
	 * @param y The y component
	 */
	public void add(float x, float y) {
		TMP_VECTOR.set(getX(), getY());
		TMP_VECTOR.add(x, y);
		setXY(TMP_VECTOR.x, TMP_VECTOR.y);
	}
	
	/**
	 * Subtracts components from the position of this {@link Shape}
	 * @param x The x component
	 * @param y The y component
	 */
	public void subtract(float x, float y) {
		TMP_VECTOR.set(getX(), getY());
		TMP_VECTOR.sub(x, y);
		setXY(TMP_VECTOR.x, TMP_VECTOR.y);
	}

	/**
	 * Returns the rotation of this {@link Shape}
	 * 
	 * @return The rotation in degrees
	 */
	public abstract float getRotation();

	/**
	 * Sets the rotation of this {@link Shape} with its first point as the
	 * origin
	 * 
	 * @param degrees
	 *            The rotation in degrees
	 */
	public abstract void setRotation(float degrees);

	/**
	 * Sets the rotation of this {@link Shape} with a specified point as the origin
	 * @param center The origin {@link Point}
	 * @param degrees The rotation in degrees
	 */
	public void setRotationAround(Point center, float degrees) {
		setRotationAround(center.x, center.y, degrees);
	}

	/**
	 * Sets the rotation of this {@link Shape} with a specified point as the
	 * origin
	 * 
	 * @param originX
	 *            The origin/center x coordinate
	 * @param originY
	 *            The origin/center y coordinate
	 * @param degrees
	 *            The rotation in degrees
	 */
	public abstract void setRotationAround(float originX, float originY, float degrees);

	/**
	 * Rotates this {@link Shape} by a specified amount of degrees with its
	 * first point as the origin
	 * 
	 * @param degrees
	 *            The rotation in degrees
	 */
	public abstract void rotate(float degrees);

	/**
	 * Rotates this {@link Shape} by a specified amount of degrees around a
	 * specified point
	 * 
	 * @param originX
	 *            The origin/center x coordinate
	 * @param originY
	 *            The origin/center y coordinate
	 * @param degrees
	 *            The rotation in degrees
	 */
	public abstract void rotateAround(float originX, float originY, float degrees);

	/**
	 * Draws this shape using a {@link Graphics} instance
	 * 
	 * @param g
	 *            The {@link Graphics} context to render with
	 */
	public abstract void draw(Graphics g);

	/**
	 * Fills this shape using a {@link Graphics} instance
	 * 
	 * @param g
	 *            The {@link Graphics} context to render with
	 */
	public abstract void fill(Graphics g);

	/**
	 * Translates the x and y coordinate of this object
	 * 
	 * @param translateX
	 *            The x translation amount
	 * @param translateY
	 *            The y translation amount
	 */
	public abstract void translate(float translateX, float translateY);

	/**
	 * Returns the number of edges of this object
	 * 
	 * @return The number of sides/edges
	 */
	public abstract int getNumberOfSides();

	/**
	 * Returns an {@link EdgeIterator} for looping over the edges of this
	 * {@link Shape}
	 * 
	 * @return The {@link EdgeIterator}
	 */
	public abstract EdgeIterator edgeIterator();

	/**
	 * Returns if this {@link Shape} is a {@link Circle}
	 * 
	 * @return True if this {@link Shape} is a {@link Circle}
	 */
	public abstract boolean isCircle();

	/**
	 * Returns the {@link Polygon} representing this {@link Shape}
	 * 
	 * @return Null if this {@link Shape} is a {@link Circle}
	 */
	public abstract Polygon getPolygon();

	@Override
	public void moveTowards(float x, float y, float speed) {
		TMP_SOURCE_VECTOR.set(getX(), getY());
		TMP_TARGET_VECTOR.set(x, y);
		Vector2 direction = TMP_TARGET_VECTOR.sub(TMP_SOURCE_VECTOR).nor();

		float xComponent = speed * MathUtils.cosDeg(direction.angle());
		float yComponent = speed * MathUtils.sinDeg(direction.angle());
		TMP_SOURCE_VECTOR.add(xComponent, yComponent);

		setXY(TMP_SOURCE_VECTOR.x, TMP_SOURCE_VECTOR.y);
	}

	@Override
	public void moveTowards(Positionable positionable, float speed) {
		moveTowards(positionable.getX(), positionable.getY(), speed);
	}

	/**
	 * @see Positionable#addPostionChangeListener(PositionChangeListener)
	 */
	@Override
	public <T extends Positionable> void addPostionChangeListener(
			PositionChangeListener<T> listener) {
		if (positionChangeListeners == null) {
			positionChangeListeners = new Array<PositionChangeListener>(true,1);
		}
		positionChangeListeners.add(listener);
	}

	/**
	 * @see Positionable#removePositionChangeListener(PositionChangeListener)
	 */
	@Override
	public <T extends Positionable> void removePositionChangeListener(
			PositionChangeListener<T> listener) {
		removePositionListener(positionChangeListeners, listener);
	}

	@Override
	public <T extends Sizeable> void addSizeChangeListener(SizeChangeListener<T> listener) {
		if (sizeChangeListeners == null) {
			sizeChangeListeners = new Array<SizeChangeListener>(true,1);
		}
		sizeChangeListeners.add(listener);
	}

	@Override
	public <T extends Sizeable> void removeSizeChangeListener(SizeChangeListener<T> listener) {
		removeSizeListener(sizeChangeListeners, listener);
	}

	protected void notifyPositionChangeListeners() {
		notifyPositionListeners(positionChangeListeners, this);
	}

	protected void clearPositionChangeListeners() {
		clearPositionListeners(positionChangeListeners);
	}

	protected void notifySizeChangeListeners() {
		notifySizeListeners(sizeChangeListeners, this);
	}

	protected void clearSizeChangeListeners() {
		clearSizeListeners(sizeChangeListeners);
	}

	public static <T extends Positionable> void removePositionListener(Array<PositionChangeListener> positionChangeListeners, PositionChangeListener listener) {
		if (positionChangeListeners == null) {
			return;
		}
		positionChangeListeners.removeValue(listener, false);
	}

	public static <T extends Positionable> void removePositionListener(ReadWriteLock lock, Array<PositionChangeListener> positionChangeListeners, PositionChangeListener listener) {
		lock.lockRead();
		if (positionChangeListeners == null) {
			lock.unlockRead();
			return;
		}
		lock.unlockRead();

		lock.lockWrite();
		positionChangeListeners.removeValue(listener, false);
		lock.unlockWrite();
	}

	public static <T extends Positionable> void notifyPositionListeners(Array<PositionChangeListener> positionChangeListeners, T notifier) {
		if (positionChangeListeners == null) {
			return;
		}
		for (int i = positionChangeListeners.size - 1; i >= 0; i--) {
			if(i >= positionChangeListeners.size) {
				i = positionChangeListeners.size - 1;
			}
			PositionChangeListener<T> listener = positionChangeListeners.get(i);
			listener.positionChanged(notifier);
		}
	}

	public static <T extends Positionable> void notifyPositionListeners(ReadWriteLock lock, Array<PositionChangeListener> positionChangeListeners, T notifier) {
		lock.lockRead();
		if (positionChangeListeners == null) {
			lock.unlockRead();
			return;
		}
		for (int i = positionChangeListeners.size - 1; i >= 0; i--) {
			if(i >= positionChangeListeners.size) {
				i = positionChangeListeners.size - 1;
			}
			PositionChangeListener listener = positionChangeListeners.get(i);
			lock.unlockRead();
			listener.positionChanged(notifier);
			lock.lockRead();
		}
		lock.unlockRead();
	}

	public static <T extends Positionable> void clearPositionListeners(Array<PositionChangeListener> positionChangeListeners) {
		if (positionChangeListeners == null) {
			return;
		}
		positionChangeListeners.clear();
	}

	public static <T extends Positionable> void clearPositionListeners(ReadWriteLock lock, Array<PositionChangeListener> positionChangeListeners) {
		lock.lockRead();
		if (positionChangeListeners == null) {
			lock.unlockRead();
			return;
		}
		lock.unlockRead();

		lock.lockWrite();
		positionChangeListeners.clear();
		lock.unlockWrite();
	}

	public static <T extends Sizeable> void removeSizeListener(Array<SizeChangeListener> sizeChangeListeners, SizeChangeListener listener) {
		if (sizeChangeListeners == null) {
			return;
		}
		sizeChangeListeners.removeValue(listener, false);
	}

	public static <T extends Sizeable> void removeSizeListener(ReadWriteLock lock, Array<SizeChangeListener> sizeChangeListeners, SizeChangeListener listener) {
		lock.lockRead();
		if (sizeChangeListeners == null) {
			lock.unlockRead();
			return;
		}
		lock.unlockRead();

		lock.lockWrite();
		sizeChangeListeners.removeValue(listener, false);
		lock.unlockWrite();
	}

	public static <T extends Sizeable> void notifySizeListeners(Array<SizeChangeListener> sizeChangeListeners, T notifier) {
		if (sizeChangeListeners == null) {
			return;
		}
		for (int i = sizeChangeListeners.size - 1; i >= 0; i--) {
			if(i >= sizeChangeListeners.size) {
				i = sizeChangeListeners.size - 1;
			}
			SizeChangeListener<T> listener = sizeChangeListeners.get(i);
			listener.sizeChanged(notifier);
		}
	}

	public static <T extends Sizeable> void notifySizeListeners(ReadWriteLock lock, Array<SizeChangeListener> sizeChangeListeners, T notifier) {
		lock.lockRead();
		if (sizeChangeListeners == null) {
			lock.unlockRead();
			return;
		}
		for (int i = sizeChangeListeners.size - 1; i >= 0; i--) {
			if(i >= sizeChangeListeners.size) {
				i = sizeChangeListeners.size - 1;
			}
			SizeChangeListener listener = sizeChangeListeners.get(i);
			lock.unlockRead();
			listener.sizeChanged(notifier);
			lock.lockRead();
		}
		lock.unlockRead();
	}

	public static <T extends Sizeable> void clearSizeListeners(Array<SizeChangeListener> sizeChangeListeners) {
		if (sizeChangeListeners == null) {
			return;
		}
		sizeChangeListeners.clear();
	}

	public static <T extends Sizeable> void clearSizeListeners(ReadWriteLock lock, Array<SizeChangeListener> sizeChangeListeners) {
		lock.lockRead();
		if (sizeChangeListeners == null) {
			lock.unlockRead();
			return;
		}
		lock.unlockRead();

		lock.lockWrite();
		sizeChangeListeners.clear();
		lock.unlockWrite();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(getNumberOfSides());
		result = prime * result + Float.floatToIntBits(getX());
		result = prime * result + Float.floatToIntBits(getY());
		result = prime * result + Float.floatToIntBits(getMinX());
		result = prime * result + Float.floatToIntBits(getMinY());
		result = prime * result + Float.floatToIntBits(getMaxX());
		result = prime * result + Float.floatToIntBits(getMaxY());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Shape other = (Shape) obj;
		if (Float.floatToIntBits(getNumberOfSides()) != Float.floatToIntBits(other.getNumberOfSides()))
			return false;
		if (Float.floatToIntBits(getX()) != Float.floatToIntBits(other.getX()))
			return false;
		if (Float.floatToIntBits(getY()) != Float.floatToIntBits(other.getY()))
			return false;
		if (Float.floatToIntBits(getMinX()) != Float.floatToIntBits(other.getMinX()))
			return false;
		if (Float.floatToIntBits(getMinY()) != Float.floatToIntBits(other.getMinY()))
			return false;
		if (Float.floatToIntBits(getMaxX()) != Float.floatToIntBits(other.getMaxX()))
			return false;
		if (Float.floatToIntBits(getMaxY()) != Float.floatToIntBits(other.getMaxY()))
			return false;
		return true;
	}
}
