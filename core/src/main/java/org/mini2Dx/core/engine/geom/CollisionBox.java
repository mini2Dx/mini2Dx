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
package org.mini2Dx.core.engine.geom;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.mini2Dx.core.engine.PositionChangeListener;
import org.mini2Dx.core.engine.Positionable;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.geom.Point;
import org.mini2Dx.core.geom.Rectangle;

import com.badlogic.gdx.math.Vector2;

/**
 * An implementation of {@link Rectangle} that allows for interpolation. Game
 * objects can use this class to move around the game world and retrieve the
 * appropriate rendering coordinates after interpolating between the previous
 * and current position.
 */
public class CollisionBox extends Rectangle implements Positionable {
	private static final long serialVersionUID = -8217730724587578266L;

	private List<PositionChangeListener> positionChangeListeners;
	private ReadWriteLock positionChangeListenerLock;

	private Rectangle previousRectangle;
	private Rectangle renderRectangle;

	public CollisionBox() {
		super();
		positionChangeListenerLock = new ReentrantReadWriteLock();
		previousRectangle = new Rectangle();
		renderRectangle = new Rectangle();
	}

	public CollisionBox(float x, float y, float width, float height) {
		super(x, y, width, height);
		positionChangeListenerLock = new ReentrantReadWriteLock();
		previousRectangle = new Rectangle(x, y, width, height);
		renderRectangle = new Rectangle(x, y, width, height);
	}
	
	/**
	 * This method needs to be called at the start of each frame
	 * before any changes are made to this object
	 */
	public void preUpdate() {
		previousRectangle.set(this);
	}

	@Override
	public void update(GameContainer gc, float delta) {
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
		renderRectangle.set(previousRectangle.lerp(this, alpha));
	}

	/**
	 * @see Positionable#addPostionChangeListener(PositionChangeListener)
	 */
	@Override
	public <T extends Positionable> void addPostionChangeListener(
			PositionChangeListener<T> listener) {
		positionChangeListenerLock.writeLock().lock();
		if (positionChangeListeners == null) {
			positionChangeListeners = new ArrayList<PositionChangeListener>(1);
		}
		positionChangeListeners.add(listener);
		positionChangeListenerLock.writeLock().unlock();
	}

	/**
	 * @see Positionable#removePositionChangeListener(PositionChangeListener)
	 */
	@Override
	public <T extends Positionable> void removePositionChangeListener(
			PositionChangeListener<T> listener) {
		if (positionChangeListeners == null) {
			return;
		}
		positionChangeListenerLock.writeLock().lock();
		positionChangeListeners.remove(listener);
		positionChangeListenerLock.writeLock().unlock();
	}

	private void notifyPositionChangeListeners() {
		if (positionChangeListeners == null) {
			return;
		}
		positionChangeListenerLock.readLock().lock();
		for (int i = positionChangeListeners.size() - 1; i >= 0; i--) {
			PositionChangeListener listener = positionChangeListeners.get(i);
			listener.positionChanged(this);
		}
		positionChangeListenerLock.readLock().unlock();
	}

	@Override
	public void setRotationAround(Point center, float degrees) {
		super.setRotationAround(center, degrees);
		notifyPositionChangeListeners();
	}

	@Override
	public void rotateAround(Point center, float degrees) {
		super.rotateAround(center, degrees);
		notifyPositionChangeListeners();
	}

	@Override
	public float getDistanceTo(Positionable positionable) {
		return getDistanceTo(positionable.getX(), positionable.getY());
	}

	/**
	 * Sets the current x and y coordinate to the specified x and y and force updates the
	 * rendering bounds to match
	 * 
	 * @param x
	 *            The x coordinate to set
	 * @param y
	 *            The y coordinate to set
	 */
	public void forceTo(float x, float y) {
		forceTo(x, y, getWidth(), getHeight());
	}

	/**
	 * Sets the current bounds to the specified bounds and force updates the
	 * rendering bounds to match
	 * 
	 * @param x
	 *            The x coordinate to set
	 * @param y
	 *            The y coordinate to set
	 * @param width
	 *            The width to set
	 * @param height
	 *            The height to set
	 */
	public void forceTo(float x, float y, float width, float height) {
		boolean notifyPositionListeners = x != getX() || y != getY();
		
		super.set(x, y, width, height);
		previousRectangle.set(x, y, width, height);
		renderRectangle.set(previousRectangle);
		
		if(!notifyPositionListeners) {
			return;
		}
		notifyPositionChangeListeners();
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
	}

	@Override
	public Rectangle set(float x, float y, float width, float height) {
		super.set(x, y, width, height);
		notifyPositionChangeListeners();
		return this;
	}

	public void set(Rectangle rectangle) {
		super.set(rectangle);
		notifyPositionChangeListeners();
	}

	@Override
	public com.badlogic.gdx.math.Rectangle set(
			com.badlogic.gdx.math.Rectangle rectangle) {
		super.set(rectangle);
		notifyPositionChangeListeners();
		return this;
	}

	@Override
	public Rectangle setPosition(float x, float y) {
		super.setPosition(x, y);
		notifyPositionChangeListeners();
		return this;
	}

	@Override
	public Rectangle setPosition(Vector2 position) {
		super.setPosition(position);
		notifyPositionChangeListeners();
		return this;
	}

	@Override
	public Rectangle setX(float x) {
		super.setX(x);
		notifyPositionChangeListeners();
		return this;
	}

	@Override
	public Rectangle setY(float y) {
		super.setY(y);
		notifyPositionChangeListeners();
		return this;
	}

	@Override
	public Rectangle setWidth(float width) {
		super.setWidth(width);
		notifyPositionChangeListeners();
		return this;
	}

	@Override
	public Rectangle setHeight(float height) {
		super.setHeight(height);
		notifyPositionChangeListeners();
		return this;
	}

	@Override
	public Rectangle setSize(float width, float height) {
		super.setSize(width, height);
		notifyPositionChangeListeners();
		return this;
	}

	@Override
	public Rectangle setSize(float sizeXY) {
		super.setSize(sizeXY);
		notifyPositionChangeListeners();
		return this;
	}

	public float getRenderX() {
		return renderRectangle.getX();
	}

	public float getRenderY() {
		return renderRectangle.getY();
	}

	public float getRenderWidth() {
		return renderRectangle.getWidth();
	}

	public float getRenderHeight() {
		return renderRectangle.getHeight();
	}

	public float getRenderRotation() {
		return renderRectangle.getRotation();
	}
}
