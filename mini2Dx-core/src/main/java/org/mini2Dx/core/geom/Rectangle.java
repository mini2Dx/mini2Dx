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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.mini2Dx.core.engine.Parallelogram;
import org.mini2Dx.core.engine.PositionChangeListener;
import org.mini2Dx.core.engine.Positionable;
import org.mini2Dx.core.engine.Shape;
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
	Point topLeft, topRight, bottomLeft, bottomRight, center, rotationalCenter;
	private float minX, minY, maxX, maxY;
	private List<PositionChangeListener> positionChangeListeners;
	private Lock positionChangeListenerLock;

	/**
	 * Default constructor. Creates a {@link Rectangle} at 0,0 with a width and
	 * height of 1
	 */
	public Rectangle() {
		this(0, 0, 1, 1);
	}

	/**
	 * Constructor
	 * 
	 * @param x
	 *            The x coordinate of the {@link Rectangle}
	 * @param y
	 *            The y coordinate of the {@link Rectangle}
	 * @param width
	 *            The width of the {@link Rectangle}
	 * @param height
	 *            The height of the {@link Rectangle}
	 */
	public Rectangle(float x, float y, float width, float height) {
		super(x, y, width, height);
		positionChangeListenerLock = new ReentrantLock();
		topLeft = new Point(x, y);
		topRight = new Point(x + width, y);
		bottomLeft = new Point(x, y + height);
		bottomRight = new Point(x + width, y + height);
		center = new Point(x + (width / 2f), y + (height / 2f));
		rotationalCenter = topLeft;
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

	/**
	 * @see Positionable#getDistanceTo(Positionable)
	 */
	@Override
	public float getDistanceTo(Positionable positionable) {
		return 0;
	}

	/**
	 * @see Positionable#addPostionChangeListener(PositionChangeListener)
	 */
	@Override
	public <T extends Positionable> void addPostionChangeListener(
			PositionChangeListener<T> listener) {
		positionChangeListenerLock.lock();
		if (positionChangeListeners == null) {
			positionChangeListeners = new ArrayList<PositionChangeListener>(1);
		}
		positionChangeListeners.add(listener);
		positionChangeListenerLock.unlock();
	}

	/**
	 * @see Positionable#removePositionChangeListener(PositionChangeListener)
	 */
	@Override
	public <T extends Positionable> void removePositionChangeListener(
			PositionChangeListener<T> listener) {
		if (positionChangeListeners != null) {
			positionChangeListenerLock.lock();
			positionChangeListeners.remove(listener);
			positionChangeListenerLock.unlock();
		}
	}

	private void notifyPositionChangeListeners() {
		if (positionChangeListeners != null) {
			positionChangeListenerLock.lock();
			for (int i = positionChangeListeners.size() - 1; i >= 0; i--) {
				PositionChangeListener listener = positionChangeListeners
						.get(i);
				listener.positionChanged(this);
			}
			positionChangeListenerLock.unlock();
		}
	}

	/**
	 * @see Parallelogram#getRotation()
	 */
	@Override
	public float getRotation() {
		return rotation;
	}

	/**
	 * @see Parallelogram#setRotation(float)
	 */
	@Override
	public void setRotation(float degrees) {
		setRotationAround(topLeft, degrees);
	}

	/**
	 * @see Parallelogram#setRotationAround(Point, float)
	 */
	@Override
	public void setRotationAround(Point center, float degrees) {
		degrees = degrees % 360;
		performRotation(-rotation);
		rotation = degrees;
		rotationalCenter = center;
		performRotation(rotation);
		recalculateMinMax();
		notifyPositionChangeListeners();
	}

	/**
	 * @see Parallelogram#rotate(float)
	 */
	@Override
	public void rotate(float degrees) {
		rotateAround(topLeft, degrees);
	}

	/**
	 * @see Parallelogram#rotateAround(Point, float)
	 */
	@Override
	public void rotateAround(Point center, float degrees) {
		rotationalCenter = center;
		performRotation(degrees);
		rotation += (degrees % 360);
		recalculateMinMax();
		notifyPositionChangeListeners();
	}

	private void performRotation(float degrees) {
		if (degrees == 0)
			return;

		topRight.rotateAround(rotationalCenter, degrees);
		bottomLeft.rotateAround(rotationalCenter, degrees);
		bottomRight.rotateAround(rotationalCenter, degrees);
		this.center.rotateAround(rotationalCenter, degrees);

		if (!rotationalCenter.equals(topLeft)) {
			topLeft.rotateAround(rotationalCenter, degrees);
		}
		super.setX(topLeft.x);
		super.setY(topLeft.y);
	}

	/**
	 * @see Parallelogram#intersects(LineSegment)
	 */
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

	/**
	 * Returns if the specified {@link Rectangle} intersects this one
	 * 
	 * @param rectangle
	 *            The {@link Rectangle} to test for intersection
	 * @return True if the {@link Rectangle}s intersect
	 */
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

	/**
	 * @see Parallelogram#intersects(Parallelogram)
	 */
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

	/**
	 * @see Parallelogram#intersects(float, float, float, float)
	 */
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

	/**
	 * @see Parallelogram#contains(Parallelogram)
	 */
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

	/**
	 * @see Parallelogram#contains(Parallelogram)
	 */
	public boolean contains(Rectangle rectangle) {
		return contains(rectangle.topLeft) && contains(rectangle.topRight)
				&& contains(rectangle.bottomLeft)
				&& contains(rectangle.bottomRight);
	}

	/**
	 * @see Parallelogram#contains(Positionable)
	 */
	@Override
	public boolean contains(Positionable positionable) {
		performRotation(-rotation);

		Point point = new Point(positionable.getX(), positionable.getY());
		point.rotateAround(topLeft, -rotation);

		float thisX = getX();
		float thisY = getY();

		performRotation(rotation);

		if (point.x < thisX) {
			return false;
		}
		if (point.y < thisY) {
			return false;
		}
		if (point.x > thisX + getWidth()) {
			return false;
		}
		if (point.y > thisY + getHeight()) {
			return false;
		}
		return true;
	}

	/**
	 * @see Shape#getNumberOfSides()
	 */
	@Override
	public int getNumberOfSides() {
		return 4;
	}

	/**
	 * @see Shape#draw(Graphics)
	 */
	@Override
	public void draw(Graphics g) {
		if (rotation == 0f) {
			g.drawRect(topLeft.x, topLeft.y, width, height);
			return;
		}
		g.drawLineSegment(topLeft.x, topLeft.y, topRight.x, topRight.y);
		g.drawLineSegment(topRight.x, topRight.y, bottomRight.x, bottomRight.y);
		g.drawLineSegment(bottomLeft.x, bottomLeft.y, bottomRight.x,
				bottomRight.y);
		g.drawLineSegment(topLeft.x, topLeft.y, bottomLeft.x, bottomLeft.y);
	}

	@Override
	public void set(float x, float y, float width, float height) {
		performRotation(-rotation);
		super.set(x, y, width, height);
		recalculateCoordinates();
		performRotation(rotation);
		recalculateMinMax();
		notifyPositionChangeListeners();
	}

	public void set(Rectangle rectangle) {
		performRotation(-rotation);
		super.set(rectangle);
		recalculateCoordinates();
		performRotation(rotation);
		recalculateMinMax();
		notifyPositionChangeListeners();
	}

	@Override
	public void set(com.badlogic.gdx.math.Rectangle rectangle) {
		performRotation(-rotation);
		super.set(rectangle);
		recalculateCoordinates();
		performRotation(rotation);
		recalculateMinMax();
		notifyPositionChangeListeners();
	}

	@Override
	public void setX(float x) {
		performRotation(-rotation);
		super.setX(x);
		recalculateCoordinates();
		performRotation(rotation);
		recalculateMinMax();
		notifyPositionChangeListeners();
	}

	@Override
	public void setY(float y) {
		performRotation(-rotation);
		super.setY(y);
		recalculateCoordinates();
		performRotation(rotation);
		recalculateMinMax();
		notifyPositionChangeListeners();
	}

	@Override
	public void setWidth(float width) {
		performRotation(-rotation);
		super.setWidth(width);
		recalculateCoordinates();
		performRotation(rotation);
		recalculateMinMax();
		notifyPositionChangeListeners();
	}

	@Override
	public void setHeight(float height) {
		performRotation(-rotation);
		super.setHeight(height);
		recalculateCoordinates();
		performRotation(rotation);
		recalculateMinMax();
		notifyPositionChangeListeners();
	}

	/**
	 * Returns the x coordinate of the center of this {@link Rectangle}
	 * 
	 * @return
	 */
	public float getCenterX() {
		return center.x;
	}

	/**
	 * Returns the y coordinate of the center of this {@link Rectangle}
	 * 
	 * @return
	 */
	public float getCenterY() {
		return center.y;
	}

	/**
	 * Returns the least x coordinate this {@link Rectangle}
	 * 
	 * @return
	 */
	public float getMinX() {
		return minX;
	}

	/**
	 * Returns the least y coordinate this {@link Rectangle}
	 * 
	 * @return
	 */
	public float getMinY() {
		return minY;
	}

	/**
	 * Returns the greatest x coordinate this {@link Rectangle}
	 * 
	 * @return
	 */
	public float getMaxX() {
		return maxX;
	}

	/**
	 * Returns the greatest y coordinate this {@link Rectangle}
	 * 
	 * @return
	 */
	public float getMaxY() {
		return maxY;
	}
}
