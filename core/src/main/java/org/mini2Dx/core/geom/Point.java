/**
 * Copyright (c) 2015, mini2Dx Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.geom;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.mini2Dx.core.engine.PositionChangeListener;
import org.mini2Dx.core.engine.Positionable;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Extends {@link Vector2} with some utility methods
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Point extends Vector2 implements Positionable {
	private static final long serialVersionUID = 3773673953486445831L;

	private List<PositionChangeListener> positionChangeListeners;
	private Lock positionChangeListenerLock;

	public Point() {
		super();
		this.positionChangeListenerLock = new ReentrantLock();
	}

	public Point(float x, float y) {
		super(x, y);
		this.positionChangeListenerLock = new ReentrantLock();
	}

	public Point(Point point) {
		super(point);
		this.positionChangeListenerLock = new ReentrantLock();
	}

	private void notifyPositionChangeListeners() {
		if (positionChangeListeners != null) {
			positionChangeListenerLock.lock();
			for (int i = positionChangeListeners.size() - 1; i >= 0; i--) {
				PositionChangeListener<Positionable> listener = positionChangeListeners
						.get(i);
				listener.positionChanged(this);
			}
			positionChangeListenerLock.unlock();
		}
	}

	/**
	 * Rotates this {@link Point} around another {@link Point}
	 * 
	 * @param center
	 *            The {@link Point} to rotate around
	 * @param degrees
	 *            The angle to rotate by in degrees
	 */
	public void rotateAround(Point center, float degrees) {
		if (degrees == 0)
			return;

		float cos = MathUtils.cos(degrees * MathUtils.degreesToRadians);
		float sin = MathUtils.sin(degrees * MathUtils.degreesToRadians);

		float newX = (cos * (x - center.x) - sin * (y - center.y) + center.x);
		float newY = (sin * (x - center.x) + cos * (y - center.y) + center.y);

		set(newX, newY);
	}

	/**
	 * Returns if this {@link Point} is between a and b on a line
	 * 
	 * @param a
	 *            {@link Point} a on a line
	 * @param b
	 *            {@link Point} b on a line
	 * @return False if this {@link Point} is not on the same line as a and b OR
	 *         is not between a and b on the same line
	 */
	public boolean isOnLineBetween(Point a, Point b) {
		float areaOfTriangle = (a.x * (b.y - y) + b.x * (y - a.y) + x
				* (a.y - b.y)) / 2f;
		if (areaOfTriangle == 0f) {
			if (x == a.x && y == a.y)
				return true;
			if (x == b.x && y == b.y)
				return true;
			if (x == a.x) {
				/* Same x axis */
				return (y > a.y && y < b.y) || (y > b.y && y < a.y);
			} else {
				/* Same y axis */
				return (x > a.x && x < b.x) || (x > b.x && x < a.x);
			}
		}
		return false;
	}

	/**
	 * Determines if another {@link Vector2} is exactly equal to this one
	 * 
	 * @param v
	 *            The {@link Vector2} to compare to
	 * @return True if both {@link Vector2}s x and y are exactly equal
	 */
	public boolean equals(Vector2 v) {
		return x == v.x && y == v.y;
	}

	/**
	 * Determines if a {@link Vector2} are nearly equal. A delta of 0.1
	 * means 0.0 and 0.1 would be considered equal but 0.0 and 0.11 would not.
	 * 
	 * @param v
	 *            The {@link Vector2} to compare to
	 * @param delta
	 *            The amount of error to allow for.
	 * @return True if the two points are equal allowing for a certain margin of error
	 */
	public boolean equals(Vector2 v, float delta) {
		return Math.abs(x - v.x) <= delta && Math.abs(y - v.y) <= delta;
	}

	@Override
	public <T extends Positionable> void addPostionChangeListener(
			PositionChangeListener<T> listener) {
		positionChangeListenerLock.lock();
		if (positionChangeListeners == null) {
			positionChangeListeners = new ArrayList<PositionChangeListener>(1);
		}
		positionChangeListeners.add(listener);
		positionChangeListenerLock.unlock();
	}

	@Override
	public <T extends Positionable> void removePositionChangeListener(
			PositionChangeListener<T> listener) {
		if (positionChangeListeners != null) {
			positionChangeListenerLock.lock();
			positionChangeListeners.remove(listener);
			positionChangeListenerLock.unlock();
		}
	}

	@Override
	public float getDistanceTo(Positionable positionable) {
		return this.dst(positionable.getX(), positionable.getY());
	}

	@Override
	public float getX() {
		return this.x;
	}

	@Override
	public float getY() {
		return this.y;
	}

	@Override
	public Vector2 set(float x, float y) {
		super.set(x, y);
		notifyPositionChangeListeners();
		return this;
	}

	@Override
	public Vector2 set(Vector2 v) {
		return set(v.x, v.y);
	}

	@Override
	public Vector2 add(float x, float y) {
		super.add(x, y);
		notifyPositionChangeListeners();
		return this;
	}

	@Override
	public Vector2 add(Vector2 v) {
		return add(v.x, v.y);
	}

	@Override
	public Vector2 sub(float x, float y) {
		super.sub(x, y);
		notifyPositionChangeListeners();
		return this;
	}

	@Override
	public Vector2 sub(Vector2 v) {
		return sub(v.x, v.y);
	}
}
