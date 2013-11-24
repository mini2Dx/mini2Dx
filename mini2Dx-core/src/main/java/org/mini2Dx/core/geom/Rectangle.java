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
import org.mini2Dx.core.graphics.Graphics;

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
	private float minX, minY, maxX, maxY;
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
		recalculateMinMax();
	}

	private void recalculateCoordinates() {
		topLeft.set(x, y);
		topRight.set(x + width, y);
		bottomLeft.set(x, y + height);
		bottomRight.set(x + width, y + height);
		center.set(x + (width / 2f), y + (height / 2f));
	}

	private void recalculateMinMax() {
		minX = topLeft.getX();
		minY = topLeft.getY();
		maxX = bottomRight.getX();
		maxY = bottomRight.getY();

		checkAgainstMinMax(topLeft);
		checkAgainstMinMax(topRight);
		checkAgainstMinMax(bottomLeft);
		checkAgainstMinMax(bottomRight);
	}

	private void checkAgainstMinMax(Point p) {
		if (p.getX() < minX)
			minX = p.getX();
		if (p.getX() > maxX)
			maxX = p.getX();
		if (p.getY() < minY)
			minY = p.getY();
		if (p.getY() > maxY)
			maxY = p.getY();
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

	@Override
	public void setRotation(float degrees) {
		setRotationAround(topLeft, degrees);
	}
	
	@Override
	public void setRotationAround(Point center, float degrees) {
		degrees = degrees % 360;
		performRotation(center, -rotation);
		rotation = degrees;
		performRotation(center, rotation);
		recalculateMinMax();
		notifyPositionChangeListeners();
	}

	@Override
	public void rotate(float degrees) {
		rotateAround(topLeft, degrees);
	}

	@Override
	public void rotateAround(Point center, float degrees) {
		performRotation(center, degrees);
		rotation += (degrees % 360);
		recalculateMinMax();
		notifyPositionChangeListeners();
	}

	private void performRotation(Point center, float degrees) {
		topRight.rotateAround(center, degrees);
		bottomLeft.rotateAround(center, degrees);
		bottomRight.rotateAround(center, degrees);
		this.center.rotateAround(center, degrees);

		if (!center.equals(topLeft)) {
			topLeft.rotateAround(center, degrees);
		}
		super.setX(topLeft.x);
		super.setY(topLeft.y);
	}

	@Override
	public boolean intersects(LineSegment lineSegment) {
		if (lineSegment.intersectsLineSegment(topLeft.x, topLeft.y,
				bottomLeft.x, bottomLeft.y)) {
			return true;
		}
		if (lineSegment.intersectsLineSegment(bottomLeft.x, bottomLeft.y,
				bottomRight.x, bottomRight.y)) {
			return true;
		}
		if (lineSegment.intersectsLineSegment(bottomRight.x, bottomRight.y,
				topRight.x, topRight.y)) {
			return true;
		}
		if (lineSegment.intersectsLineSegment(topRight.x, topRight.y,
				topLeft.x, topLeft.y)) {
			return true;
		}
		return false;
	}

	public boolean intersects(Rectangle rectangle) {
		boolean xAxisOverlaps = true;
		boolean yAxisOverlaps = true;

		if (maxX < rectangle.getMinX())
			xAxisOverlaps = false;
		if (rectangle.getMaxX() < minX)
			xAxisOverlaps = false;
		if (maxY < rectangle.getMinY())
			yAxisOverlaps = false;
		if (rectangle.getMaxY() < minY)
			yAxisOverlaps = false;

		return xAxisOverlaps && yAxisOverlaps;
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
		if (rotation != 0 || rect.getRotation() != 0)
			throw new UnsupportedOperationException(
					"Rectangle.intersection is not implemented to handle rotated rectangles");

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
	public int getNumberOfSides() {
		return 4;
	}

	@Override
	public void draw(Graphics g) {
		if (rotation == 0f) {
			g.drawRect(topLeft.x, topLeft.y, width, height);
			return;
		}

		// TODO: Draw rotated rectangle
	}

	@Override
	public void set(float x, float y, float width, float height) {
		performRotation(topLeft, -rotation);
		super.set(x, y, width, height);
		recalculateCoordinates();
		performRotation(topLeft, rotation);
		recalculateMinMax();
		notifyPositionChangeListeners();
	}

	public void set(Rectangle rectangle) {
		performRotation(topLeft, -rotation);
		super.set(rectangle);
		recalculateCoordinates();
		performRotation(topLeft, rotation);
		recalculateMinMax();
		notifyPositionChangeListeners();
	}

	@Override
	public void set(com.badlogic.gdx.math.Rectangle rectangle) {
		performRotation(topLeft, -rotation);
		super.set(rectangle);
		recalculateCoordinates();
		performRotation(topLeft, rotation);
		recalculateMinMax();
		notifyPositionChangeListeners();
	}

	@Override
	public void setX(float x) {
		performRotation(topLeft, -rotation);
		super.setX(x);
		recalculateCoordinates();
		performRotation(topLeft, rotation);
		recalculateMinMax();
		notifyPositionChangeListeners();
	}

	@Override
	public void setY(float y) {
		performRotation(topLeft, -rotation);
		super.setY(y);
		recalculateCoordinates();
		performRotation(topLeft, rotation);
		recalculateMinMax();
		notifyPositionChangeListeners();
	}

	@Override
	public void setWidth(float width) {
		performRotation(topLeft, -rotation);
		super.setWidth(width);
		recalculateCoordinates();
		performRotation(topLeft, rotation);
		recalculateMinMax();
		notifyPositionChangeListeners();
	}

	@Override
	public void setHeight(float height) {
		performRotation(topLeft, -rotation);
		super.setHeight(height);
		recalculateCoordinates();
		performRotation(topLeft, rotation);
		recalculateMinMax();
		notifyPositionChangeListeners();
	}

	public float getCenterX() {
		return center.x;
	}

	public float getCenterY() {
		return center.y;
	}

	public float getMinX() {
		return minX;
	}

	public float getMinY() {
		return minY;
	}

	public float getMaxX() {
		return maxX;
	}

	public float getMaxY() {
		return maxY;
	}
}
