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
import org.mini2Dx.core.geom.Point;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * An implementation of {@link Point} that allows for interpolation. Game
 * objects can use this class to move around the game world and retrieve the
 * appropriate rendering coordinates after interpolating between the previous
 * and current position.
 */
public class CollisionPoint extends Point implements Positionable {
	private static final long serialVersionUID = -7752697723641315393L;
	
	private final int id;
	private final ReadWriteLock positionChangeListenerLock;
	
	private List<PositionChangeListener> positionChangeListeners;
	
	private Point previousPosition;
	private Point renderPosition;
	private int renderX, renderY;
	private boolean interpolate = false;
	
	public CollisionPoint() {
		this(0f, 0f);
	}
	
	public CollisionPoint(int id) {
		this(id, 0f, 0f);
	}

	public CollisionPoint(float x, float y) {
		this(CollisionIdSequence.nextId(), x, y);
	}
	
	public CollisionPoint(int id, float x, float y) {
		super(x, y);
		this.id = id;
		
		positionChangeListenerLock = new ReentrantReadWriteLock();
		previousPosition = new Point(x, y);
		renderPosition = new Point(x, y);
		storeRenderCoordinates();
	}

	public CollisionPoint(Point point) {
		this(CollisionIdSequence.nextId(), point);
	}
	
	public CollisionPoint(int id, Point point) {
		super(point);
		this.id = id;
		
		positionChangeListenerLock = new ReentrantReadWriteLock();
		previousPosition = new Point(point);
		renderPosition = new Point(point);
		storeRenderCoordinates();
	}
	
	private void storeRenderCoordinates() {
		renderX = MathUtils.round(renderPosition.getX());
		renderY = MathUtils.round(renderPosition.getY());
	}
	
	/**
	 * This method needs to be called at the start of each frame
	 * before any changes are made to this object
	 */
	public void preUpdate() {
		previousPosition.set(this);
	}
	
	@Override
	public void update(GameContainer gc, float delta) {
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
		if(!interpolate) {
			return;
		}
		renderPosition.set(previousPosition.lerp(this, alpha));
		storeRenderCoordinates();
		if(renderX != MathUtils.round(x)) {
			return;
		}
		if(renderY != MathUtils.round(y)) {
			return;
		}
		interpolate = false;
	}

	@Override
	public float getDistanceTo(Positionable positionable) {
		return this.dst(positionable.getX(), positionable.getY());
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
		
		super.set(x, y);
		previousPosition.set(x, y);
		renderPosition.set(x, y);
		storeRenderCoordinates();
		
		if(!notifyPositionListeners) {
			return;
		}
		notifyPositionChangeListeners();
	}
	
	@Override
	public void setX(float x) {
		super.setX(x);
		interpolate = true;
		notifyPositionChangeListeners();
	}

	@Override
	public void setY(float y) {
		super.setY(y);
		interpolate = true;
		notifyPositionChangeListeners();
	}
	
	@Override
	public Vector2 set(float x, float y) {
		super.set(x, y);
		interpolate = true;
		notifyPositionChangeListeners();
		return this;
	}

	@Override
	public Vector2 add(float x, float y) {
		super.add(x, y);
		interpolate = true;
		notifyPositionChangeListeners();
		return this;
	}

	@Override
	public Vector2 sub(float x, float y) {
		super.sub(x, y);
		interpolate = true;
		notifyPositionChangeListeners();
		return this;
	}
	
	public int getRenderX() {
		return renderX;
	}
	
	public int getRenderY() {
		return renderY;
	}

	public int getId() {
		return id;
	}
}
