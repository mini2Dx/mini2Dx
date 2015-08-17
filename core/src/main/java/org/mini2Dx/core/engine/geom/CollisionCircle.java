/**
 * Copyright (c) 2015 See AUTHORS file
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
import org.mini2Dx.core.geom.Circle;

/**
 * An implementation of {@link Circle} that allows for interpolation. Game
 * objects can use this class to move around the game world and retrieve the
 * appropriate rendering coordinates after interpolating between the previous
 * and current position.
 */
public class CollisionCircle extends Circle implements Positionable {
	
	private ReadWriteLock positionChangeListenerLock;
	private List<PositionChangeListener> positionChangeListeners;
	
	private Circle previousCircle;
	private Circle renderCircle;
	
	public CollisionCircle(int radius) {
		super(radius);
		positionChangeListenerLock = new ReentrantReadWriteLock();
		previousCircle = new Circle(radius);
		renderCircle = new Circle(radius);
	}
	
	public CollisionCircle(float centerX, float centerY, int radius) {
		super(centerX, centerY, radius);
		positionChangeListenerLock = new ReentrantReadWriteLock();
		previousCircle = new Circle(centerX, centerY, radius);
		renderCircle = new Circle(centerX, centerY, radius);
	}
	
	/**
	 * This method needs to be called at the start of each frame
	 * before any changes are made to this object
	 */
	public void preUpdate() {
		previousCircle.set(this);
	}
	
	@Override
	public void update(GameContainer gc, float delta) {
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
		renderCircle.set(previousCircle.lerp(this, alpha));
	}
	
	@Override
	public float getDistanceTo(Positionable positionable) {
		return getDistanceTo(positionable.getX(), positionable.getY());
	}

	@Override
	public <T extends Positionable> void addPostionChangeListener(
			PositionChangeListener<T> listener) {
		positionChangeListenerLock.writeLock().lock();
		if (positionChangeListeners == null) {
			positionChangeListeners = new ArrayList<PositionChangeListener>();
		}
		positionChangeListeners.add(listener);
		positionChangeListenerLock.writeLock().unlock();
	}

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
			if(i >= positionChangeListeners.size()) {
				i = positionChangeListeners.size() - 1;
			}
			PositionChangeListener listener = positionChangeListeners.get(i);
			positionChangeListenerLock.readLock().unlock();
			listener.positionChanged(this);
			positionChangeListenerLock.readLock().lock();
		}
		positionChangeListenerLock.readLock().unlock();
	}
	
	/**
	 * Sets the current x and y coordinate to the specified x and y and force updates the
	 * rendering position to match
	 * 
	 * @param x
	 *            The x coordinate to set
	 * @param y
	 *            The y coordinate to set
	 */
	public void forceTo(float x, float y) {
		boolean notifyPositionListeners = x != getX() || y != getY();
		
		super.setCenter(x, y);
		previousCircle.setCenter(x, y);
		renderCircle.setCenter(x, y);
		
		if(!notifyPositionListeners) {
			return;
		}
		notifyPositionChangeListeners();
	}

	public void setX(float x) {
		super.setX(x);
		notifyPositionChangeListeners();
	}
	
	public void setY(float y) {
		super.setY(y);
		notifyPositionChangeListeners();
	}
	
	public void setCenter(float x, float y) {
		super.setCenter(x, y);
		notifyPositionChangeListeners();
	}
	
	public float getRenderX() {
		return renderCircle.getX();
	}
	
	public float getRenderY() {
		return renderCircle.getY();
	}
	
	public float getRenderRadius() {
		return renderCircle.getRadius();
	}
	
	public float getPreviousX() {
		return previousCircle.getX();
	}
	
	public float getPreviousY() {
		return previousCircle.getY();
	}

	public float getPreviousRadius() {
		return previousCircle.getRadius();
	}
}
