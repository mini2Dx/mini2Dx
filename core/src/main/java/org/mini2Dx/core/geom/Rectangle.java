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
package org.mini2Dx.core.geom;

import org.mini2Dx.core.exception.NotYetImplementedException;
import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

/**
 * Implements a rectangle.
 */
public class Rectangle extends Shape implements
		Parallelogram {
	private static final long serialVersionUID = 4016090439885217620L;
	
	final com.badlogic.gdx.math.Rectangle rectangle;
	
	private float rotation;
	Point topLeft, topRight, bottomLeft, bottomRight, center, rotationalCenter;
	private float minX, minY, maxX, maxY;

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
		rectangle = new com.badlogic.gdx.math.Rectangle(x, y, width, height);
		topLeft = new Point(x, y);
		topRight = new Point(x + width, y);
		bottomLeft = new Point(x, y + height);
		bottomRight = new Point(x + width, y + height);
		center = new Point(x + (width / 2f), y + (height / 2f));
		rotationalCenter = topLeft;
		recalculateMinMax();
	}

	/**
	 * Renders this {@link Rectangle} and the {@link LineSegment}s between each
	 * of its point and its rotational center
	 * 
	 * @param g
	 *            The {@link Graphics} context to render to
	 */
	public void debug(Graphics g) {
		this.draw(g);
		g.drawLineSegment(topLeft.x, topLeft.y, rotationalCenter.x,
				rotationalCenter.y);
		g.drawLineSegment(topRight.x, topRight.y, rotationalCenter.x,
				rotationalCenter.y);
		g.drawLineSegment(bottomLeft.x, bottomLeft.y, rotationalCenter.x,
				rotationalCenter.y);
		g.drawLineSegment(bottomRight.x, bottomRight.y, rotationalCenter.x,
				rotationalCenter.y);
	}
	
	public Rectangle lerp(Rectangle target, float alpha) {
		final float inverseAlpha = 1.0f - alpha;
		internalSetX((getX() * inverseAlpha) + (target.getX() * alpha));
		internalSetY((getY() * inverseAlpha) + (target.getY() * alpha));
		
		if(getWidth() != target.getWidth()) {
			internalSetWidth((getWidth() * inverseAlpha) + (target.getWidth() * alpha));
		}
		
		if(getHeight() != target.getHeight()) {
			internalSetHeight((getHeight() * inverseAlpha) + (target.getHeight() * alpha));
		}
		
		if(getRotation() != target.getRotation()) {
			internalSetRotationAround(rotationalCenter, (getRotation() * inverseAlpha) 
					+ (target.getRotation() * alpha));
		}
		return this;
	}

	private void recalculateCoordinates() {
		topLeft.set(rectangle.x, rectangle.y);
		topRight.set(rectangle.x + rectangle.width, rectangle.y);
		bottomLeft.set(rectangle.x, rectangle.y + rectangle.height);
		bottomRight.set(rectangle.x + rectangle.width, rectangle.y + rectangle.height);
		center.set(rectangle.x + (rectangle.width / 2f), rectangle.y + (rectangle.height / 2f));
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

	public float getDistanceTo(Point point) {
	    return getDistanceTo(point.getX(), point.getY());
	}
	
	public float getDistanceTo(float x, float y) {
	    float topLeftDist = topLeft.getDistanceTo(x, y);
	    float bottomLeftDist = bottomLeft.getDistanceTo(x, y);
	    float topRightDist = topRight.getDistanceTo(x, y);
	    float bottomRightDist = bottomRight.getDistanceTo(x, y);
	    
	    float result = topLeftDist;
	    result = Math.min(result, topRightDist);
	    result = Math.min(result, bottomLeftDist);
	    result = Math.min(result, bottomRightDist);
		return result;
	}

	@Override
	public float getRotation() {
		return rotation;
	}

	@Override
	public void setRotation(float degrees) {
		setRotationAround(topLeft, degrees);
	}
	
	private void internalSetRotationAround(Point center, float degrees) {
		//TODO: This operation is very expensive, can this be optimised somehow?
		degrees = degrees % 360;
		performRotation(-rotation);
		rotation = degrees;
		rotationalCenter = center;
		performRotation(rotation);
		recalculateMinMax();
	}

	/**
	 * @see Parallelogram#setRotationAround(Point, float)
	 */
	@Override
	public void setRotationAround(Point center, float degrees) {
		internalSetRotationAround(center, degrees);
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
	}

	private void performRotation(float degrees) {
		if (degrees == 0f)
			return;

		topRight.rotateAround(rotationalCenter, degrees);
		bottomLeft.rotateAround(rotationalCenter, degrees);
		bottomRight.rotateAround(rotationalCenter, degrees);
		this.center.rotateAround(rotationalCenter, degrees);

		if (!rotationalCenter.equals(topLeft)) {
			topLeft.rotateAround(rotationalCenter, degrees);
		}
		rectangle.setX(topLeft.x);
		rectangle.setY(topLeft.y);
	}
	
	@Override
	public boolean intersectsLineSegment(LineSegment lineSegment) {
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
	
	@Override
	public boolean intersectsLineSegment(Vector2 pointA, Vector2 pointB) {
		if (org.mini2Dx.core.geom.Intersector.intersectLineSegments(topLeft.x, topLeft.y,
				bottomLeft.x, bottomLeft.y, pointA.x, pointA.y, pointB.x, pointB.y)) {
			return true;
		}
		if (org.mini2Dx.core.geom.Intersector.intersectLineSegments(bottomLeft.x, bottomLeft.y,
				bottomRight.x, bottomRight.y, pointA.x, pointA.y, pointB.x, pointB.y)) {
			return true;
		}
		if (org.mini2Dx.core.geom.Intersector.intersectLineSegments(bottomRight.x, bottomRight.y,
				topRight.x, topRight.y, pointA.x, pointA.y, pointB.x, pointB.y)) {
			return true;
		}
		if (org.mini2Dx.core.geom.Intersector.intersectLineSegments(topRight.x, topRight.y,
				topLeft.x, topLeft.y, pointA.x, pointA.y, pointB.x, pointB.y)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns if the specified {@link Circle} intersects this {@link Rectangle}
	 * 
	 * @param circle The {@link Circle} to test for intersection
	 * @return True if the {@link Circle} intersects
	 */
	public boolean intersects(Circle circle) {
		return Intersector.overlaps(circle.circle, rectangle);
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
	
	/**
	 * Returns if the specified {@link Polygon} intersects this {@link Rectangle}
	 * @param polygon The {@link Polygon} to check
	 * @return True if this {@link Rectangle} and the {@link Polygon} intersect
	 */
	public boolean intersects(Polygon polygon) {
		if(polygon.intersectsLineSegment(topLeft, topRight)) {
			return true;
		}
		if(polygon.intersectsLineSegment(topLeft, bottomLeft)) {
			return true;
		}
		if(polygon.intersectsLineSegment(bottomLeft, bottomRight)) {
			return true;
		}
		if(polygon.intersectsLineSegment(bottomRight, topRight)) {
			return true;
		}
		return false;
	}

	public Rectangle intersection(Rectangle rect) {
		if (rotation != 0f || rect.getRotation() != 0f)
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
		return contains(rectangle.topLeft)
				&& contains(rectangle.topRight)
				&& contains(rectangle.bottomLeft)
				&& contains(rectangle.bottomRight);
	}

	@Override
	public boolean contains(float x, float y) {
		return triangleContains(x, y, topLeft, topRight, bottomLeft)
				|| triangleContains(x, y, bottomLeft, topRight, bottomRight);
	}

	@Override
	public boolean contains(Vector2 point) {
		return contains(point.x, point.y);
	}

	private boolean triangleContains(float x, float y, Point p1, Point p2,
			Point p3) {
		boolean b1, b2, b3;

		b1 = sign(x, y, p1, p2) < 0.0f;
		b2 = sign(x, y, p2, p3) < 0.0f;
		b3 = sign(x, y, p3, p1) < 0.0f;

		return ((b1 == b2) && (b2 == b3));
	}

	private float sign(float x, float y, Point p1, Point p2) {
		return (x - p2.x) * (p1.y - p2.y) - (p1.x - p2.x) * (y - p2.y);
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
			g.drawRect(topLeft.x, topLeft.y, rectangle.width, rectangle.height);
			return;
		}
		g.drawLineSegment(topLeft.x, topLeft.y, topRight.x, topRight.y);
		g.drawLineSegment(topRight.x, topRight.y, bottomRight.x, bottomRight.y);
		g.drawLineSegment(bottomLeft.x, bottomLeft.y, bottomRight.x,
				bottomRight.y);
		g.drawLineSegment(topLeft.x, topLeft.y, bottomLeft.x, bottomLeft.y);
	}
	
	@Override
	public void fill(Graphics g) {
		if (rotation == 0f) {
			g.fillRect(topLeft.x, topLeft.y, rectangle.width, rectangle.height);
			return;
		}
		throw new NotYetImplementedException("g.fillRect for rotated Rectangles has not been implemented yet. " 
				+ "If you would like to contribute an implementation, "
                + "please send a pull request to the mini2Dx repository at https://github.com/mini2Dx/mini2Dx");
	}

	public Rectangle set(float x, float y, float width, float height) {
		performRotation(-rotation);
		rectangle.set(x, y, width, height);
		recalculateCoordinates();
		performRotation(rotation);
		recalculateMinMax();
		return this;
	}

	public void set(Rectangle rectangle) {
		performRotation(-rotation);
		rectangle.set(rectangle);
		recalculateCoordinates();
		performRotation(rotation);
		recalculateMinMax();
	}
	
	public void set(float x, float y) {
		performRotation(-rotation);
		rectangle.setPosition(x, y);
		recalculateCoordinates();
		performRotation(rotation);
		recalculateMinMax();
	}
	
	public void set(Vector2 position) {
		performRotation(-rotation);
		rectangle.setPosition(position);
		recalculateCoordinates();
		performRotation(rotation);
		recalculateMinMax();
	}
	
	@Override
	public float getX() {
		return rectangle.x;
	}
	
	private void internalSetX(float x) {
		performRotation(-rotation);
		rectangle.setX(x);
		recalculateCoordinates();
		performRotation(rotation);
		recalculateMinMax();
	}
	
	public void setX(float x) {
		internalSetX(x);
	}
	
	private void internalSetY(float y) {
		performRotation(-rotation);
		rectangle.setY(y);
		recalculateCoordinates();
		performRotation(rotation);
		recalculateMinMax();
	}
	
	@Override
	public float getY() {
		return rectangle.y;
	}
	
	public void setY(float y) {
		internalSetY(y);
	}
	
	private void internalSetWidth(float width) {
		performRotation(-rotation);
		rectangle.setWidth(width);
		recalculateCoordinates();
		performRotation(rotation);
		recalculateMinMax();
	}
	
	public float getWidth() {
		return rectangle.width;
	}

	public Rectangle setWidth(float width) {
		internalSetWidth(width);
		return this;
	}
	
	private void internalSetHeight(float height) {
		performRotation(-rotation);
		rectangle.setHeight(height);
		recalculateCoordinates();
		performRotation(rotation);
		recalculateMinMax();
	}
	
	public float getHeight() {
		return rectangle.getHeight();
	}

	public Rectangle setHeight(float height) {
		internalSetHeight(height);
		return this;
	}

	public Rectangle setSize(float width, float height) {
		performRotation(-rotation);
		rectangle.setSize(width, height);
		recalculateCoordinates();
		performRotation(rotation);
		recalculateMinMax();
		return this;
	}

	public Rectangle setSize(float sizeXY) {
		performRotation(-rotation);
		rectangle.setSize(sizeXY);
		recalculateCoordinates();
		performRotation(rotation);
		recalculateMinMax();
		return this;
	}
	
	@Override
	public void translate(float translateX, float translateY) {
		set(getX() + translateX, getY() + translateY);
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

	@Override
	public String toString() {
		return "Rectangle [rotation=" + rotation + ", x=" + rectangle.x + ", y=" + rectangle.y
				+ ", width=" + rectangle.width + ", height=" + rectangle.height + "]";
	}
}
