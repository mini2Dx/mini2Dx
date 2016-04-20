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
package org.mini2Dx.core.geom;

import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.util.EdgeIterator;

import com.badlogic.gdx.math.Vector2;

/**
 * A polygon that is both equilateral and equiangular.
 * 
 * See <a href="https://en.wikipedia.org/wiki/Regular_polygon">Regular Polygon</a>
 */
public class RegularPolygon extends Shape {
	private final float rotationSymmetry;
	private final int totalSides;
	private final Point center;

	private Polygon polygon;
	private float radius;

	/**
	 * Constructor
	 * 
	 * @param centerX
	 *            The center X coordinate
	 * @param centerY
	 *            The center Y coordinate
	 * @param radius
	 *            The distance from the center to the corner points
	 * @param totalSides
	 *            The total sides of this shape
	 * @param rotationSymmetry
	 *            The rotational symmetry of the corner points from the center
	 */
	public RegularPolygon(float centerX, float centerY, float radius, int totalSides, float rotationSymmetry) {
		this.center = new Point(centerX, centerY);
		this.rotationSymmetry = rotationSymmetry;
		this.totalSides = totalSides;
		setRadius(radius);
	}
	
	@Override
	public void draw(Graphics g) {
		polygon.draw(g);
	}

	@Override
	public void fill(Graphics g) {
		polygon.fill(g);
	}
	
	/**
	 * Returns if this {@link RegularPolygon} intersects a {@link Polygon}
	 * 
	 * @param polygon
	 *            The {@link Polygon} to check
	 * @return True if this {@link RegularPolygon} and {@link Polygon} intersect
	 */
	public boolean intersects(Polygon polygon) {
		return polygon.intersects(polygon);
	}

	/**
	 * Returns if the specified {@link Rectangle} intersects this
	 * {@link RegularPolygon}
	 * 
	 * @param rectangle
	 *            The {@link Rectangle} to check
	 * @return True if this {@link RegularPolygon} and {@link Rectangle} intersect
	 */
	public boolean intersects(Rectangle rectangle) {
		return rectangle.intersects(polygon);
	}

	@Override
	public boolean contains(float x, float y) {
		return polygon.contains(x, y);
	}

	@Override
	public boolean contains(Vector2 vector2) {
		return polygon.contains(vector2);
	}

	@Override
	public boolean intersects(LineSegment lineSegment) {
		return intersectsLineSegment(lineSegment.getPointA(), lineSegment.getPointB());
	}
	
	@Override
	public boolean intersectsLineSegment(Vector2 pointA, Vector2 pointB) {
		return polygon.intersectsLineSegment(pointA, pointB);
	}
	
	@Override
	public boolean intersectsLineSegment(float x1, float y1, float x2, float y2) {
		return polygon.intersectsLineSegment(x1, y1, x2, y2);
	}

	@Override
	public float getDistanceTo(float x, float y) {
		return polygon.getDistanceTo(x, y);
	}

	@Override
	public float getRotation() {
		return polygon.getRotation();
	}

	@Override
	public void setRotation(float degrees) {
		polygon.setRotation(degrees);
	}

	@Override
	public void setRotationAround(float centerX, float centerY, float degrees) {
		polygon.setRotationAround(centerX, centerY, degrees);
	}

	@Override
	public void rotate(float degrees) {
		polygon.rotate(degrees);
	}

	@Override
	public void rotateAround(float centerX, float centerY, float degrees) {
		polygon.rotateAround(centerX, centerY, degrees);
	}

	/**
	 * Returns the center X coordinate of this {@link RegularPolygon}
	 * 
	 * @return The center X coordinate
	 */
	@Override
	public float getX() {
		return center.getX();
	}

	/**
	 * Sets the X coordinate of this {@link RegularPolygon}
	 * 
	 * @param centerX
	 *            The center X coordinate
	 */
	@Override
	public void setX(float centerX) {
		if (this.center.getX() == centerX) {
			return;
		}
		float diff = centerX - center.getX();
		polygon.translate(diff, 0f);
		center.set(centerX, center.y);
	}

	/**
	 * Returns the center Y coordinate of this {@link RegularPolygon}
	 * 
	 * @return The center Y coordinate
	 */
	@Override
	public float getY() {
		return center.getY();
	}

	/**
	 * Sets the Y coordinate of this {@link RegularPolygon}
	 * 
	 * @param centerY
	 *            The center Y coordinate
	 */
	@Override
	public void setY(float centerY) {
		if (this.center.getY() == centerY) {
			return;
		}
		float diff = centerY - this.center.getY();
		polygon.translate(0f, diff);
		center.set(center.x, centerY);
	}

	/**
	 * Sets the center X and Y coordinates. Faster than calling
	 * {@link RegularPolygon#setX(float)} and {@link RegularPolygon#setY(float)}
	 * separately.
	 * 
	 * @param centerX
	 *            The center X coordinate
	 * @param centerY
	 *            The center Y coordinate
	 */
	@Override
	public void set(float centerX, float centerY) {
		float diffX = centerX - this.center.getX();
		float diffY = centerY - this.center.getY();
		polygon.translate(diffX, diffY);
		center.set(centerX, centerY);
	}
	
	
	@Override
	public void translate(float translateX, float translateY) {
		set(getX() + translateX, getY() + translateY);
	}

	/**
	 * Returns the radius of this {@link RegularPolygon}
	 * 
	 * @return The distance between the center and its corners
	 */
	public float getRadius() {
		return radius;
	}

	/**
	 * Sets the radius of this {@link RegularPolygon}
	 * 
	 * Note: This operation is expensive
	 * 
	 * @param radius
	 *            The distance between the center and its corners
	 */
	public void setRadius(float radius) {
		if (this.radius == radius) {
			return;
		}
		this.radius = radius;

		Point[] points = new Point[totalSides];
		points[0] = new Point(center.getX(), center.getY() - radius);
		for (int i = 1; i < points.length; i++) {
			points[i] = new Point(points[0]);
			points[i].rotateAround(center, rotationSymmetry * i);
		}
		polygon = new Polygon(points);
	}

	/**
	 * Returns the x coordinate of the corner at the specified index
	 * 
	 * @param index
	 *            The index where 0 = the top corner and increments in a
	 *            clockwise method
	 * @return The x coordinate of the corner
	 */
	public float getX(int index) {
		return polygon.getX(index);
	}
	
	/**
	 * Returns the y coordinate of the corner at the specified index
	 * 
	 * @param index
	 *            The index where 0 = the top corner and increments in a
	 *            clockwise method
	 * @return The y coordinate of the corner
	 */
	public float getY(int index) {
		return polygon.getY(index);
	}

	@Override
	public int getNumberOfSides() {
		return totalSides;
	}

	@Override
	public EdgeIterator edgeIterator() {
		return polygon.edgeIterator();
	}
}
