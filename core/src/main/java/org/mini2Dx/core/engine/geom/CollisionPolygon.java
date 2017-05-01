/**
 * Copyright (c) 2016 See AUTHORS file
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
import org.mini2Dx.core.geom.Polygon;
import org.mini2Dx.core.geom.Shape;
import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 *
 */
public class CollisionPolygon extends Polygon implements CollisionShape {
	private final int id;
	private final ReentrantReadWriteLock positionChangeListenerLock;
	private final ReentrantReadWriteLock sizeChangeListenerLock;
	
	private final Vector2 tmpSourceVector = new Vector2();
	private final Vector2 tmpTargetVector = new Vector2();
	
	private List<PositionChangeListener> positionChangeListeners;
	private List<SizeChangeListener> sizeChangeListeners;

	private Polygon previousPolygon;
	private Polygon renderPolygon;
	private int renderX, renderY;
	private boolean interpolate = false;
	
	public CollisionPolygon(float [] vertices) {
		this(CollisionIdSequence.nextId(), vertices);
	}
	
	public CollisionPolygon(Vector2[] vectors) {
		this(CollisionIdSequence.nextId(), vectors);
	}

	public CollisionPolygon(int id, float[] vertices) {
		super(vertices);
		this.id = id;
		
		positionChangeListenerLock = new ReentrantReadWriteLock();
		sizeChangeListenerLock = new ReentrantReadWriteLock();
	}

	public CollisionPolygon(int id, Vector2[] vectors) {
		super(vectors);
		this.id = id;
		
		positionChangeListenerLock = new ReentrantReadWriteLock();
		sizeChangeListenerLock = new ReentrantReadWriteLock();
	}
	
	private void storeRenderCoordinates() {
		renderX = MathUtils.round(renderPolygon.getX());
		renderY = MathUtils.round(renderPolygon.getY());
	}
	
	@Override
	public void preUpdate() {
		previousPolygon.set(this);
	}
	
	@Override
	public void update(GameContainer gc, float delta) {}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
		if(!interpolate) {
			return;
		}
		renderPolygon.set(previousPolygon.lerp(this, alpha));
		storeRenderCoordinates();
		if(renderX != MathUtils.round(this.getX())) {
			return;
		}
		if(renderY != MathUtils.round(this.getY())) {
			return;
		}
		interpolate = false;
	}
	
	@Override
	public void draw(Graphics g) {
		renderPolygon.draw(g);
	}
	
	@Override
	public void fill(Graphics g) {
		renderPolygon.fill(g);
	}

	@Override
	public int getId() {
		return id;
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
			positionChangeListeners = new ArrayList<PositionChangeListener>(1);
		}
		positionChangeListeners.add(listener);
		positionChangeListenerLock.writeLock().unlock();
	}
	
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
	public void addPoint(float x, float y) {
		super.addPoint(x, y);
		notifyPositionChangeListeners();
		notifySizeChangeListeners();
		interpolate = true;
	}
	
	@Override
	public void removePoint(float x, float y) {
		super.removePoint(x, y);
		notifyPositionChangeListeners();
		notifySizeChangeListeners();
		interpolate = true;
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
	public void add(float x, float y) {
		super.add(x, y);
		interpolate = true;
		notifyPositionChangeListeners();
	}
	
	@Override
	public void subtract(float x, float y) {
		super.subtract(x, y);
		interpolate = true;
		notifyPositionChangeListeners();
	}
	
	@Override
	public void setX(float x) {
		if(x == getX()) {
			return;
		}
		super.setX(x);
		notifyPositionChangeListeners();
		interpolate = true;
	}
	
	@Override
	public void setY(float y) {
		if(y == getY()) {
			return;
		}
		super.setY(y);
		notifyPositionChangeListeners();
		interpolate = true;
	}
	
	@Override
	public void set(float x, float y) {
		if(x == getX() && y == getY()) {
			return;
		}
		super.set(x, y);
		notifyPositionChangeListeners();
		interpolate = true;
	}
	
	@Override
	public void forceTo(float x, float y) {
		boolean notifyPositionListeners = x != getX() || y != getY();
		super.set(x, y);
		previousPolygon.set(this);
		renderPolygon.set(previousPolygon);
		
		if(notifyPositionListeners) {
			notifyPositionChangeListeners();
		}
	}
	
	@Override
	public void setRotation(float degrees) {
		if(getRotation() == degrees) {
			return;
		}
		super.setRotation(degrees);
		notifyPositionChangeListeners();
		interpolate = true;
	}
	
	@Override
	public void rotate(float degrees) {
		if(degrees == 0) {
			return;
		}
		super.rotate(degrees);
		notifyPositionChangeListeners();
		interpolate = true;
	}
	
	@Override
	public void setRotationAround(float centerX, float centerY, float degrees) {
		if(getRotation() == degrees && centerX == getX() && centerY == getY()) {
			return;
		}
		super.setRotationAround(centerX, centerY, degrees);
		notifyPositionChangeListeners();
		interpolate = true;
	}
	
	@Override
	public void rotateAround(float centerX, float centerY, float degrees) {
		if(degrees == 0) {
			return;
		}
		super.rotateAround(centerX, centerY, degrees);
		notifyPositionChangeListeners();
		interpolate = true;
	}
	
	@Override
	public void setVertices(float[] vertices) {
		super.setVertices(vertices);
		notifyPositionChangeListeners();
		notifySizeChangeListeners();
		interpolate = true;
	}
	
	@Override
	public void setVertices(Vector2[] vertices) {
		super.setVertices(vertices);
		notifyPositionChangeListeners();
		notifySizeChangeListeners();
		interpolate = true;
	}
	
	@Override
	public void setRadius(float radius) {
		super.setRadius(radius);
		interpolate = true;
		notifySizeChangeListeners();
	}
	
	@Override
	public void scale(float scale) {
		super.scale(scale);
		interpolate = true;
		notifySizeChangeListeners();
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
	public float getWidth() {
		return getMaxX() - getMinX();
	}

	@Override
	public float getHeight() {
		return getMaxY() - getMinY();
	}
	
	@Override
	public Shape getShape() {
		return this;
	}
}
