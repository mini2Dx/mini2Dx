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
import org.mini2Dx.core.engine.Shape;
import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.math.Vector2;

/**
 * Implements a circle
 * @author Thomas Cashman
 */
public class Circle implements Shape {
	private Vector2 center;
	private int radius;
	private List<PositionChangeListener> positionChangleListeners;
	
	public Circle(int radius) {
		center = new Vector2();
		this.radius = radius;
	}
	
	public Circle(float centerX, float centerY, int radius) {
		center = new Vector2(centerX, centerY);
		this.radius = radius;
	}

	public boolean intersects(Circle circle) {
		return center.dst(circle.center) <= radius + circle.radius;
	}

	@Override
	public float getDistanceTo(Positionable positionable) {
		float result = center.dst(positionable.getX(), positionable.getY());
		if(result <= radius) {
			return 0f;
		}
		return result - radius;
	}
	
	@Override
	public int getNumberOfSides() {
		return 0;
	}

	@Override
	public void draw(Graphics g) {
		g.drawCircle(center.x, center.y, radius);
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
	
	private void notifyPositionChanged() {
		if(positionChangleListeners != null) {
			for(PositionChangeListener<Circle> listener : positionChangleListeners) {
				listener.positionChanged(this);
			}
		}
	}

	@Override
	public float getX() {
		return center.x;
	}

	@Override
	public float getY() {
		return center.y;
	}
	
	public void setX(float x) {
		center.set(x, center.y);
		notifyPositionChanged();
	}
	
	public void setY(float y) {
		center.set(center.x, y);
		notifyPositionChanged();
	}
	
	public void setCenter(float x, float y) {
		center.set(x, y);
		notifyPositionChanged();
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}
}
