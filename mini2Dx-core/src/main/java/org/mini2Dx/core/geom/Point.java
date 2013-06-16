/**
 * Copyright (c) 2013, mini2Dx Project
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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.mini2Dx.core.engine.PositionChangeListener;
import org.mini2Dx.core.engine.Positionable;

import com.badlogic.gdx.math.Vector2;

/**
 * Extends {@link Vector2} with some utility methods
 * 
 * @author Thomas Cashman
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Point extends Vector2 implements Positionable {
	private static final long serialVersionUID = 3773673953486445831L;

	private List<PositionChangeListener> positionChangleListeners;

	public Point() {
		super();
	}

	public Point(float x, float y) {
		super(x, y);
	}

	public Point(Point point) {
		super(point);
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

	@Override
	public <T extends Positionable> void addPostionChangeListener(
			PositionChangeListener<T> listener) {
		if (positionChangleListeners == null) {
			positionChangleListeners = new CopyOnWriteArrayList<PositionChangeListener>();
		}
		positionChangleListeners.add(listener);
	}

	@Override
	public <T extends Positionable> void removePositionChangeListener(
			PositionChangeListener<T> listener) {
		if (positionChangleListeners != null) {
			positionChangleListeners.remove(listener);
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
		if (positionChangleListeners != null) {
			for (PositionChangeListener<Positionable> listener : positionChangleListeners) {
				listener.positionChanged(this);
			}
		}
		return this;
	}
	
	@Override
	public Vector2 set(Vector2 v) {
		return set(v.x, v.y);
	}

	@Override
	public Vector2 add(float x, float y) {
		super.add(x, y);
		if (positionChangleListeners != null) {
			for (PositionChangeListener<Positionable> listener : positionChangleListeners) {
				listener.positionChanged(this);
			}
		}
		return this;
	}
	
	@Override
	public Vector2 add(Vector2 v) {
		return add(v.x, v.y);
	}

	@Override
	public Vector2 sub(float x, float y) {
		super.sub(x, y);
		if (positionChangleListeners != null) {
			for (PositionChangeListener<Positionable> listener : positionChangleListeners) {
				listener.positionChanged(this);
			}
		}
		return this;
	}
	
	@Override
	public Vector2 sub(Vector2 v) {
		return sub(v.x, v.y);
	}
	
	@Override
	public Vector2 mul(float x, float y) {
		super.mul(x, y);
		if (positionChangleListeners != null) {
			for (PositionChangeListener<Positionable> listener : positionChangleListeners) {
				listener.positionChanged(this);
			}
		}
		return this;
	}
	
	@Override
	public Vector2 mul(float scalar) {
		return mul(scalar, scalar);
	}
	
	@Override
	public Vector2 div(float x, float y) {
		super.div(x, y);
		if (positionChangleListeners != null) {
			for (PositionChangeListener<Positionable> listener : positionChangleListeners) {
				listener.positionChanged(this);
			}
		}
		return this;
	}
	
	@Override
	public Vector2 div(Vector2 v) {
		return div(v.x, v.y);
	}
}
