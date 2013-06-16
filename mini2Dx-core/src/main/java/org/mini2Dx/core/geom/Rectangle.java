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
import org.mini2Dx.core.engine.Parallelogram;

import com.badlogic.gdx.math.Vector2;

/**
 * Adds extra functionality to the default rectangle implementation in LibGDX
 * 
 * @author Thomas Cashman
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class Rectangle extends com.badlogic.gdx.math.Rectangle implements
		Parallelogram {
	private static final long serialVersionUID = 6405432580555614156L;

	private Vector2 xy, centerXY, maxXY;
	private LineSegment topSide, bottomSide, leftSide, rightSide;
	private List<PositionChangeListener> positionChangleListeners;

	/**
	 * Constructor. Creates a {@link Rectangle} at 0,0 with a width and height
	 * of 1
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

		xy = new Vector2();
		centerXY = new Vector2();
		maxXY = new Vector2();

		topSide = new LineSegment();
		bottomSide = new LineSegment();
		leftSide = new LineSegment();
		rightSide = new LineSegment();

		calculateCoordinates();
		calculateSides();
	}

	private void calculateCoordinates() {
		xy.set(getX(), getY());
		centerXY.set(getX() + (getWidth() / 2f), getY() + (getHeight() / 2f));
		maxXY.set(this.getX() + this.getWidth(), this.getY() + this.getHeight());
	}

	private void calculateSides() {
		topSide.set(x, y, maxXY.x, y);
		bottomSide.set(x, maxXY.y, maxXY.x, maxXY.y);
		leftSide.set(x, y, x, maxXY.y);
		rightSide.set(maxXY.x, y, maxXY.x, maxXY.y);
		
		if (positionChangleListeners != null) {
			for (PositionChangeListener<Parallelogram> listener : positionChangleListeners) {
				listener.positionChanged(this);
			}
		}
	}
	
	public float getDistanceTo(Positionable positionable) {
		if(positionable.getX() < getX()) {
			/* Left side */
			return com.badlogic.gdx.math.Intersector.distanceLinePoint(leftSide.getP1(), leftSide.getP2(), new Vector2(positionable.getX(), positionable.getY()));
		} else if(positionable.getX() > getMaxX()) {
			/* Right side */
			return com.badlogic.gdx.math.Intersector.distanceLinePoint(rightSide.getP1(), rightSide.getP2(), new Vector2(positionable.getX(), positionable.getY()));
		} else if(positionable.getY() < getY()) {
			/* Above */
			return com.badlogic.gdx.math.Intersector.distanceLinePoint(topSide.getP1(), topSide.getP2(), new Vector2(positionable.getX(), positionable.getY()));
		} else if(positionable.getY() > getMaxY()) {
			/* Below */
			return com.badlogic.gdx.math.Intersector.distanceLinePoint(bottomSide.getP1(), bottomSide.getP2(), new Vector2(positionable.getX(), positionable.getY()));
		}
		/* Inside */
		return 0;
	}

	/**
	 * Moves this {@link Rectangle} by a distance
	 * 
	 * @param x
	 *            The distance to move along the x axis
	 * @param y
	 *            The distance to move along the y axis
	 */
	public void move(float x, float y) {
		setX(getX() + x);
		setY(getY() + y);
	}

	/**
	 * Moves this {@link Rectangle} by a distance
	 * 
	 * @param xy
	 *            The distance to move expressed as a {@link Vector2}
	 */
	public void move(Vector2 xy) {
		move(xy.x, xy.y);
	}
	
	public boolean intersects(LineSegment line) {
		return line.intersects(this);
	}

	@Override
	public boolean overlaps(float x, float y, float width, float height) {
		if ((getX() > (x + width)) || ((getMaxX()) < x)) {
			return false;
		}
		if ((getY() > (y + height)) || ((getMaxY()) < y)) {
			return false;
		}
		return true;
	}

	@Override
	public boolean intersects(Parallelogram spatial) {
		if(spatial instanceof Rectangle) {
			return intersects((Rectangle) spatial);
		}
		return false;
	}
	
	public boolean intersects(Rectangle rectangle) {
		if ((x > rectangle.getMaxX()) || ((getMaxX()) < rectangle.getX())) {
			return false;
		}
		if ((y > (rectangle.getMaxY())) || ((getMaxY()) < rectangle.getY())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean contains(Parallelogram spatial) {
		if(spatial instanceof Rectangle) {
			return contains((Rectangle) spatial);
		}
		return false;
	}
	
	public boolean contains(Rectangle rectangle) {
		return rectangle.getX() >= getX() && rectangle.getMaxX() <= getMaxX()
				&& rectangle.getY() >= getY() && rectangle.getMaxY() <= getMaxY();
	}

	public Rectangle intersection(Rectangle rect) {
		float newX = Math.max(getX(), rect.getX());
		float newY = Math.max(getY(), rect.getY());
		float newWidth = Math.min(getMaxX(), rect.getMaxX()) - newX;
		float newHeight = Math.min(getMaxY(), rect.getMaxY()) - newY;
		return new Rectangle(newX, newY, newWidth, newHeight);
	}

	@Override
	public void setX(float x) {
		super.setX(x);
		calculateCoordinates();
		calculateSides();
	}

	@Override
	public void setY(float y) {
		super.setY(y);
		calculateCoordinates();
		calculateSides();
	}

	@Override
	public void setWidth(float width) {
		if (width > 0f) {
			super.setWidth(width);
			calculateCoordinates();
			calculateSides();
		} else {
			throw new RuntimeException("Cannot set width to a value less than 1");
		}
	}

	@Override
	public void setHeight(float height) {
		if (height > 0f) {
			super.setHeight(height);
			calculateCoordinates();
			calculateSides();
		} else {
			throw new RuntimeException("Cannot set height to a value less than 1");
		}
	}

	@Override
	public void set(com.badlogic.gdx.math.Rectangle rectangle) {
		super.set(rectangle);
		calculateCoordinates();
		calculateSides();
	}

	@Override
	public void set(float x, float y, float width, float height) {
		if (width > 0f && height > 0f) {
			super.set(x, y, width, height);
			calculateCoordinates();
			calculateSides();
		}
	}

	/**
	 * Returns the coordinates of this {@link Rectangle} expressed as a
	 * {@link Vector2}
	 * 
	 * Note: Modifying this {@link Vector2} will not move the {@link Rectangle}.
	 * This object should only be used in a read-only manner.
	 * 
	 * @return
	 */
	protected Vector2 getCoordinatesAsVector2() {
		return xy;
	}

	/**
	 * Returns the center x coordinate of the {@link Rectangle}
	 * 
	 * @return
	 */
	public float getCenterX() {
		return centerXY.x;
	}

	/**
	 * Returns the center y coordinate of the {@link Rectangle}
	 * 
	 * @return
	 */
	public float getCenterY() {
		return centerXY.y;
	}

	/**
	 * Sets the center coordinates of the {@link Rectangle} and recalculates the
	 * max x and y coordinates based on the width and height of
	 * {@link Rectangle}
	 * 
	 * @param x
	 *            The center x coordinate
	 * @param y
	 *            The center y coordinate
	 */
	public void setCenter(float x, float y) {
		centerXY.set(x, y);
		super.setX(centerXY.x - (getWidth() / 2f));
		super.setY(centerXY.y - (getHeight() / 2f));
		calculateCoordinates();
		calculateSides();
	}

	/**
	 * Returns the x coordinate of the right side of this {@link Rectangle}
	 * 
	 * @return
	 */
	public float getMaxX() {
		return maxXY.x;
	}

	/**
	 * Returns the y coordinate of the bottom side of this {@link Rectangle}
	 * 
	 * @return
	 */
	public float getMaxY() {
		return maxXY.y;
	}
	
	@Override
	public boolean contains(Positionable positionable) {
		return this.contains(positionable.getX(), positionable.getY());
	}
	
	@Override
	public <T extends Positionable> void addPostionChangeListener(
			PositionChangeListener<T> listener) {
		if(positionChangleListeners == null) {
			positionChangleListeners = new CopyOnWriteArrayList<PositionChangeListener>();
		}
		positionChangleListeners.add(listener);
	}

	@Override
	public <T extends Positionable> void removePositionChangeListener(
			PositionChangeListener<T> listener) {
		if(positionChangleListeners != null) {
			positionChangleListeners.remove(listener);
		}
	}

	public LineSegment getTopSide() {
		return topSide;
	}

	public LineSegment getBottomSide() {
		return bottomSide;
	}

	public LineSegment getLeftSide() {
		return leftSide;
	}

	public LineSegment getRightSide() {
		return rightSide;
	}

	@Override
	public String toString() {
		return "Rectangle [xy=" + xy + ", centerXY=" + centerXY + ", maxXY="
				+ maxXY + ", topSide=" + topSide + ", bottomSide=" + bottomSide
				+ ", leftSide=" + leftSide + ", rightSide=" + rightSide + "]";
	}

	@Override
	public float getRotation() {
		return 0;
	}
}
