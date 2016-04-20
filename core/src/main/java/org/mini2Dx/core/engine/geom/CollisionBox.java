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
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.mini2Dx.core.engine.PositionChangeListener;
import org.mini2Dx.core.engine.Positionable;
import org.mini2Dx.core.engine.SizeChangeListener;
import org.mini2Dx.core.engine.Sizeable;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.geom.Point;
import org.mini2Dx.core.geom.Rectangle;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * An implementation of {@link Rectangle} that allows for interpolation. Game
 * objects can use this class to move around the game world and retrieve the
 * appropriate rendering coordinates after interpolating between the previous
 * and current position.
 */
public class CollisionBox extends Rectangle implements Positionable, Sizeable {
	private static final long serialVersionUID = -8217730724587578266L;

	private final int id;
	private final ReentrantReadWriteLock positionChangeListenerLock;
	private final ReentrantReadWriteLock sizeChangeListenerLock;
	
	private List<PositionChangeListener> positionChangeListeners;
	private List<SizeChangeListener> sizeChangeListeners;

	private Rectangle previousRectangle;
	private Rectangle renderRectangle;
	private int renderX, renderY, renderWidth, renderHeight;
	private boolean interpolate = false;

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
		this.id = id;
		
		positionChangeListenerLock = new ReentrantReadWriteLock();
		sizeChangeListenerLock = new ReentrantReadWriteLock();
		previousRectangle = new Rectangle(x, y, width, height);
		renderRectangle = new Rectangle(x, y, width, height);
		storeRenderCoordinates();
	}
	
	private void storeRenderCoordinates() {
		renderX = MathUtils.round(renderRectangle.getX());
		renderY = MathUtils.round(renderRectangle.getY());
		renderWidth = MathUtils.round(renderRectangle.getWidth());
		renderHeight = MathUtils.round(renderRectangle.getHeight());
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
		if(!interpolate) {
			return;
		}
		renderRectangle.set(previousRectangle.lerp(this, alpha));
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
		interpolate = false;
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
		positionChangeListenerLock.readLock().lock();
		if (positionChangeListeners == null) {
			positionChangeListenerLock.readLock().unlock();
			return;
		}
		positionChangeListenerLock.readLock().unlock();
		
		positionChangeListenerLock.writeLock().lock();
		positionChangeListeners.remove(listener);
		positionChangeListenerLock.writeLock().unlock();
	}

	private void notifyPositionChangeListeners() {
		positionChangeListenerLock.readLock().lock();
		if (positionChangeListeners == null) {
			positionChangeListenerLock.readLock().unlock();
			return;
		}
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
	public <T extends Sizeable> void addSizeChangeListener(SizeChangeListener<T> listener) {
		sizeChangeListenerLock.writeLock().lock();
		if (sizeChangeListeners == null) {
			sizeChangeListeners = new ArrayList<SizeChangeListener>(1);
		}
		sizeChangeListeners.add(listener);
		sizeChangeListenerLock.writeLock().unlock();
	}

	@Override
	public <T extends Sizeable> void removeSizeChangeListener(SizeChangeListener<T> listener) {
		sizeChangeListenerLock.readLock().lock();
		if (sizeChangeListeners == null) {
			sizeChangeListenerLock.readLock().unlock();
			return;
		}
		sizeChangeListenerLock.readLock().unlock();
		
		sizeChangeListenerLock.writeLock().lock();
		sizeChangeListeners.remove(listener);
		sizeChangeListenerLock.writeLock().unlock();
	}
	
	private void notifySizeChangeListeners() {
		sizeChangeListenerLock.readLock().lock();
		if (sizeChangeListeners == null) {
			sizeChangeListenerLock.readLock().unlock();
			return;
		}
		for (int i = sizeChangeListeners.size() - 1; i >= 0; i--) {
			if(i >= sizeChangeListeners.size()) {
				i = sizeChangeListeners.size() - 1;
			}
			SizeChangeListener listener = sizeChangeListeners.get(i);
			sizeChangeListenerLock.readLock().unlock();
			listener.sizeChanged(this);
			sizeChangeListenerLock.readLock().lock();
		}
		sizeChangeListenerLock.readLock().unlock();
	}

	@Override
	public void setRotationAround(Point center, float degrees) {
		super.setRotationAround(center, degrees);
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
		boolean notifySizeListeners = width != getWidth() || height != getHeight();
		
		super.set(x, y, width, height);
		previousRectangle.set(x, y, width, height);
		renderRectangle.set(previousRectangle);
		storeRenderCoordinates();
		
		if(notifyPositionListeners) {
			notifyPositionChangeListeners();
		}
		if(notifySizeListeners) {
			notifySizeChangeListeners();
		}
	}
	
	public void forceTo(Rectangle rectangle) {
		forceTo(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
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
	public Rectangle set(float x, float y, float width, float height) {
		boolean notifyPositionListeners = x != getX() || y != getY();
		boolean notifySizeListeners = width != getWidth() || height != getHeight();
		
		super.set(x, y, width, height);
		
		interpolate = true;
		if(notifyPositionListeners) {
			notifyPositionChangeListeners();
		}
		if(notifySizeListeners) {
			notifySizeChangeListeners();
		}
		return this;
	}

	public void set(Rectangle rectangle) {
		super.set(rectangle);
		interpolate = true;
		notifyPositionChangeListeners();
		notifySizeChangeListeners();
	}

	@Override
	public void set(float x, float y) {
		super.set(x, y);
		interpolate = true;
		notifyPositionChangeListeners();
	}

	@Override
	public void set(Vector2 position) {
		super.set(position);
		interpolate = true;
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
	public Rectangle setWidth(float width) {
		super.setWidth(width);
		interpolate = true;
		notifySizeChangeListeners();
		return this;
	}

	@Override
	public Rectangle setHeight(float height) {
		super.setHeight(height);
		interpolate = true;
		notifySizeChangeListeners();
		return this;
	}

	@Override
	public Rectangle setSize(float width, float height) {
		super.setSize(width, height);
		interpolate = true;
		notifySizeChangeListeners();
		return this;
	}

	@Override
	public Rectangle setSize(float sizeXY) {
		super.setSize(sizeXY);
		interpolate = true;
		notifySizeChangeListeners();
		return this;
	}

	public int getRenderX() {
		return renderX;
	}

	public int getRenderY() {
		return renderY;
	}

	public int getRenderWidth() {
		return renderWidth;
	}

	public int getRenderHeight() {
		return renderHeight;
	}

	public float getRenderRotation() {
		return renderRectangle.getRotation();
	}

	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "CollisionBox [id=" + id + ", x=" + getX() + ", y=" + getY() + ", width="
				+ getWidth() + ", height=" + getHeight() + ", getRotation()=" + getRotation() + ", renderRectangle=" + renderRectangle + "]";
	}
}
