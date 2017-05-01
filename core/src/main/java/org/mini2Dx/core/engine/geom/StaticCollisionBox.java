/**
 * Copyright (c) 2017 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.engine.geom;

import java.util.ArrayList;
import java.util.List;

import org.mini2Dx.core.engine.PositionChangeListener;
import org.mini2Dx.core.engine.Positionable;
import org.mini2Dx.core.engine.SizeChangeListener;
import org.mini2Dx.core.engine.Sizeable;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.geom.Shape;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * A {@link Rectangle} implementation of {@link CollisionShape} that does not
 * need to be updated/interpolated. More lightweight than using a
 * {@link CollisionBox} for static collisions.
 */
public class StaticCollisionBox extends Rectangle implements CollisionShape {
	private final int id;
	
	private final Vector2 tmpSourceVector = new Vector2();
	private final Vector2 tmpTargetVector = new Vector2();

	private List<PositionChangeListener> positionChangeListeners;
	private List<SizeChangeListener> sizeChangeListeners;

	public StaticCollisionBox() {
		this(0f, 0f, 1f, 1f);
	}

	public StaticCollisionBox(int id) {
		this(id, 0f, 0f, 1f, 1f);
	}

	public StaticCollisionBox(float x, float y, float width, float height) {
		this(CollisionIdSequence.nextId(), x, y, width, height);
	}

	public StaticCollisionBox(int id, float x, float y, float width, float height) {
		super(x, y, width, height);
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public int getRenderX() {
		return MathUtils.round(getX());
	}

	@Override
	public int getRenderY() {
		return MathUtils.round(getY());
	}

	public int getRenderWidth() {
		return MathUtils.round(getWidth());
	}

	public int getRenderHeight() {
		return MathUtils.round(getHeight());
	}

	@Override
	public void add(float x, float y) {
		super.add(x, y);
		notifyPositionChangeListeners();
	}

	@Override
	public void subtract(float x, float y) {
		super.subtract(x, y);
		notifyPositionChangeListeners();
	}

	@Override
	public Rectangle set(float x, float y, float width, float height) {
		boolean notifyPositionListeners = x != getX() || y != getY();
		boolean notifySizeListeners = width != getWidth() || height != getHeight();

		if (notifyPositionListeners || notifySizeListeners) {
			super.set(x, y, width, height);
		}

		if (notifyPositionListeners) {
			notifyPositionChangeListeners();
		}
		if (notifySizeListeners) {
			notifySizeChangeListeners();
		}
		return this;
	}

	public void set(Rectangle rectangle) {
		boolean notifyPositionListeners = rectangle.getX() != getX() || rectangle.getY() != getY();
		boolean notifySizeListeners = rectangle.getWidth() != getWidth() || rectangle.getHeight() != getHeight();

		if (notifyPositionListeners || notifySizeListeners) {
			super.set(rectangle);
		}
		if (notifyPositionListeners) {
			notifyPositionChangeListeners();
		}
		if (notifySizeListeners) {
			notifySizeChangeListeners();
		}
	}

	@Override
	public void set(float x, float y) {
		if (x == getX() && y == getY()) {
			return;
		}
		super.set(x, y);
		notifyPositionChangeListeners();
	}

	@Override
	public void set(Vector2 position) {
		if (getX() == position.x && getY() == position.y) {
			return;
		}
		super.set(position);
		notifyPositionChangeListeners();
	}

	@Override
	public void setX(float x) {
		if (x == getX()) {
			return;
		}
		super.setX(x);
		notifyPositionChangeListeners();
	}

	@Override
	public void setY(float y) {
		if (y == getY()) {
			return;
		}
		super.setY(y);
		notifyPositionChangeListeners();
	}

	@Override
	public Rectangle setWidth(float width) {
		if (width == getWidth()) {
			return this;
		}
		super.setWidth(width);
		notifySizeChangeListeners();
		return this;
	}

	@Override
	public Rectangle setHeight(float height) {
		if (height == getHeight()) {
			return this;
		}
		super.setHeight(height);
		notifySizeChangeListeners();
		return this;
	}

	@Override
	public Rectangle setSize(float width, float height) {
		if (width == getWidth() && height == getHeight()) {
			return this;
		}
		super.setSize(width, height);
		notifySizeChangeListeners();
		return this;
	}

	@Override
	public Rectangle setSize(float sizeXY) {
		if (getWidth() == sizeXY && getHeight() == sizeXY) {
			return this;
		}
		super.setSize(sizeXY);
		notifySizeChangeListeners();
		return this;
	}

	@Override
	public void setRadius(float radius) {
		super.setRadius(radius);
		notifySizeChangeListeners();
	}

	@Override
	public void scale(float scale) {
		super.scale(scale);
		notifySizeChangeListeners();
	}

	@Override
	public float getDistanceTo(Positionable positionable) {
		return getDistanceTo(positionable.getX(), positionable.getY());
	}

	@Override
	public void moveTowards(float x, float y, float speed) {
		tmpSourceVector.set(getX(), getY());
		tmpTargetVector.set(x, y);
		Vector2 direction = tmpTargetVector.sub(tmpSourceVector).nor();
		
		float xComponent = speed * MathUtils.cosDeg(direction.angle());
		float yComponent = speed * MathUtils.sinDeg(direction.angle());
		tmpSourceVector.add(xComponent, yComponent);
		
		set(tmpSourceVector.x, tmpSourceVector.y);
	}

	@Override
	public void moveTowards(Positionable positionable, float speed) {
		moveTowards(positionable.getX(), positionable.getY(), speed);
	}

	@Override
	public void preUpdate() {
	}

	@Override
	public void update(GameContainer gc, float delta) {
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
	}

	/**
	 * @see Positionable#addPostionChangeListener(PositionChangeListener)
	 */
	@Override
	public <T extends Positionable> void addPostionChangeListener(PositionChangeListener<T> listener) {
		if (positionChangeListeners == null) {
			positionChangeListeners = new ArrayList<PositionChangeListener>(1);
		}
		positionChangeListeners.add(listener);
	}

	/**
	 * @see Positionable#removePositionChangeListener(PositionChangeListener)
	 */
	@Override
	public <T extends Positionable> void removePositionChangeListener(PositionChangeListener<T> listener) {
		if (positionChangeListeners == null) {
			return;
		}
		positionChangeListeners.remove(listener);
	}

	private void notifyPositionChangeListeners() {
		if (positionChangeListeners == null) {
			return;
		}
		for (int i = positionChangeListeners.size() - 1; i >= 0; i--) {
			if (i >= positionChangeListeners.size()) {
				i = positionChangeListeners.size() - 1;
			}
			PositionChangeListener listener = positionChangeListeners.get(i);
			listener.positionChanged(this);
		}
	}

	@Override
	public <T extends Sizeable> void addSizeChangeListener(SizeChangeListener<T> listener) {
		if (sizeChangeListeners == null) {
			sizeChangeListeners = new ArrayList<SizeChangeListener>(1);
		}
		sizeChangeListeners.add(listener);
	}

	@Override
	public <T extends Sizeable> void removeSizeChangeListener(SizeChangeListener<T> listener) {
		if (sizeChangeListeners == null) {
			return;
		}
		sizeChangeListeners.remove(listener);
	}

	private void notifySizeChangeListeners() {
		if (sizeChangeListeners == null) {
			return;
		}
		for (int i = sizeChangeListeners.size() - 1; i >= 0; i--) {
			if (i >= sizeChangeListeners.size()) {
				i = sizeChangeListeners.size() - 1;
			}
			SizeChangeListener listener = sizeChangeListeners.get(i);
			listener.sizeChanged(this);
		}
	}

	@Override
	public void forceTo(float x, float y) {
		set(x, y);
	}

	@Override
	public Shape getShape() {
		return this;
	}

}
