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

import org.mini2Dx.core.engine.Parallelogram;
import org.mini2Dx.core.engine.PositionChangeListener;
import org.mini2Dx.core.engine.Positionable;

import com.badlogic.gdx.math.MathUtils;

/**
 * Implements a rotatable rectangle. Adds extra functionality to the default
 * rectangle implementation in LibGDX
 * 
 * @author Thomas Cashman
 */
public class Rectangle extends com.badlogic.gdx.math.Rectangle implements
		Parallelogram {
	private static final long serialVersionUID = 4016090439885217620L;
	private float rotation;
	Point topLeft, topRight, bottomLeft, bottomRight, center;
	LineSegment top, bottom, left, right;
	private List<PositionChangeListener> positionChangleListeners;

	public Rectangle() {
		this(0, 0, 1, 1);
	}

	public Rectangle(float x, float y, float width, float height) {
		super(x, y, width, height);
		topLeft = new Point(x, y);
		topRight = new Point(x + width, y);
		bottomLeft = new Point(x, y + height);
		bottomRight = new Point(x + width, y + height);
		center = new Point(x + (width / 2f), y + (height / 2f));

		top = new LineSegment(topLeft, topRight);
		bottom = new LineSegment(bottomLeft, bottomRight);
		left = new LineSegment(topLeft, bottomLeft);
		right = new LineSegment(topRight, bottomRight);
	}

	private void recalculateCoordinates() {
		topLeft.set(x, y);
		topRight.set(x + width, y);
		bottomLeft.set(x, y + height);
		bottomRight.set(x + width, y + height);
		center.set(x + (width / 2f), y + (height / 2f));
		rotate(rotation);
	}

	@Override
	public float getDistanceTo(Positionable positionable) {
		return 0;
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

	private void notifyPositionChangeListeners() {
		if (positionChangleListeners != null) {
			for (PositionChangeListener<Positionable> listener : positionChangleListeners) {
				listener.positionChanged(this);
			}
		}
	}

	@Override
	public float getRotation() {
		return rotation;
	}

	private void setRotation(float degrees) {
		rotation = degrees % 360;
	}

	@Override
	public void rotate(float degrees) {
		rotateAround(topLeft, degrees);
	}

	@Override
	public void rotateAround(Point center, float degrees) {
		performRotation(center, degrees);
		setRotation(rotation + degrees);
		notifyPositionChangeListeners();
	}

	private void performRotation(Point center, float degrees) {
		topLeft.rotateAround(center, degrees);
		topRight.rotateAround(center, degrees);
		bottomLeft.rotateAround(center, degrees);
		bottomRight.rotateAround(center, degrees);
		this.center.rotateAround(center, degrees);
		super.setX(topLeft.x);
		super.setY(topLeft.y);
	}

	@Override
	public boolean intersects(LineSegment lineSegment) {
		return top.intersects(lineSegment) || bottom.intersects(lineSegment)
				|| right.intersects(lineSegment)
				|| left.intersects(lineSegment);
	}

	public boolean intersects(Rectangle rectangle) {
		if (top.intersects(rectangle.top))
			return true;
		if (top.intersects(rectangle.left))
			return true;
		if (top.intersects(rectangle.right))
			return true;
		if (top.intersects(rectangle.bottom))
			return true;

		if (left.intersects(rectangle.top))
			return true;
		if (left.intersects(rectangle.left))
			return true;
		if (left.intersects(rectangle.right))
			return true;
		if (left.intersects(rectangle.bottom))
			return true;

		if (right.intersects(rectangle.top))
			return true;
		if (right.intersects(rectangle.left))
			return true;
		if (right.intersects(rectangle.right))
			return true;
		if (right.intersects(rectangle.bottom))
			return true;

		if (bottom.intersects(rectangle.top))
			return true;
		if (bottom.intersects(rectangle.left))
			return true;
		if (bottom.intersects(rectangle.right))
			return true;
		if (bottom.intersects(rectangle.bottom))
			return true;
		return false;
	}

	@Override
	public boolean intersects(Parallelogram parallelogram) {
		if (parallelogram instanceof Rectangle) {
			return intersects((Rectangle) parallelogram);
		} else {
			Rectangle rect = new Rectangle(parallelogram.getX(),
					parallelogram.getY(), parallelogram.getWidth(),
					parallelogram.getHeight());
			rect.rotate(parallelogram.getRotation());
			return intersects(rect);
		}
	}

	@Override
	public boolean intersects(float x, float y, float width, float height) {
		Rectangle rect = new Rectangle(x, y, width, height);
		return intersects(rect);
	}

	public Rectangle intersection(Rectangle rect) {
		if(rotation != 0 || rect.getRotation() != 0)
			throw new UnsupportedOperationException("Rectangle.intersection is not implemented to handle rotated rectangles");
		
		float newX = Math.max(getX(), rect.getX());
		float newY = Math.max(getY(), rect.getY());
		float newWidth = Math.min(bottomRight.x, rect.bottomRight.x) - newX;
		float newHeight = Math.min(bottomRight.y, rect.bottomRight.y) - newY;
		return new Rectangle(newX, newY, newWidth, newHeight);
	}

	@Override
	public boolean contains(Parallelogram parallelogram) {
		if (parallelogram instanceof Rectangle) {
			return contains((Rectangle) parallelogram);
		} else {
			Rectangle rect = new Rectangle(parallelogram.getX(),
					parallelogram.getY(), parallelogram.getWidth(),
					parallelogram.getHeight());
			rect.rotate(parallelogram.getRotation());
			return contains(rect);
		}
	}

	public boolean contains(Rectangle rectangle) {
		return contains(rectangle.topLeft) && contains(rectangle.topRight)
				&& contains(rectangle.bottomLeft)
				&& contains(rectangle.bottomRight);
	}

	@Override
	public boolean contains(Positionable positionable) {
		performRotation(topLeft, -rotation);
		boolean result = true;

		float px = positionable.getX();
		float py = positionable.getY();

		if (px < getX())
			result = false;
		if (py < getY())
			result = false;
		if (px > getX() + getWidth())
			result = false;
		if (py > getY() + getHeight())
			result = false;
		performRotation(topLeft, rotation);
		return result;
	}

	@Override
	public LineSegment projectOnTo(Line line) {
		float minX = 0;
		float minY = 0;
		float maxX = 0;
		float maxY = 0;

		return null;
	}

	@Override
	public int getNumberOfSides() {
		return 4;
	}

	@Override
	public void set(float x, float y, float width, float height) {
		super.set(x, y, width, height);
		recalculateCoordinates();
	}

	public void set(Rectangle rectangle) {
		super.set(rectangle);
		recalculateCoordinates();
	}

	@Override
	public void set(com.badlogic.gdx.math.Rectangle rectangle) {
		super.set(rectangle);
		recalculateCoordinates();
	}

	@Override
	public void setX(float x) {
		super.setX(x);
		recalculateCoordinates();
	}

	@Override
	public void setY(float y) {
		super.setY(y);
		recalculateCoordinates();
	}

	@Override
	public void setWidth(float width) {
		super.setWidth(width);
		recalculateCoordinates();
	}

	@Override
	public void setHeight(float height) {
		super.setHeight(height);
		recalculateCoordinates();
	}
	
	public float getCenterX() {
		return center.x;
	}
	
	public float getCenterY() {
		return center.y;
	}
}
