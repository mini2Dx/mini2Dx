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

import com.badlogic.gdx.math.Vector2;

/**
 * Represents a segement of a line (the space between two points )
 * 
 * @author Thomas Cashman
 */
public class LineSegment {
	private Point intersection;
	protected Point pointA, pointB;

	/**
	 * Constructs a line segment between point A and point B
	 * 
	 * @param x1
	 *            The x coordinate of point A
	 * @param y1
	 *            The y coordinate of point A
	 * @param x2
	 *            The x coordinate of point B
	 * @param y2
	 *            The y coordinate of point B
	 */
	public LineSegment(float x1, float y1, float x2, float y2) {
		this(new Point(x1, y1), new Point(x2, y2));
	}

	/**
	 * Constructs a line segment between point A and point B
	 * 
	 * @param pA
	 *            {@link Point} A
	 * @param pB
	 *            {@link Point} B
	 */
	public LineSegment(Point pA, Point pB) {
		this.pointA = pA;
		this.pointB = pB;
		intersection = new Point();
	}

	/**
	 * Sets the coordinates of point A and point B
	 * 
	 * @param x1
	 *            The x coordinate of point A
	 * @param y1
	 *            The y coordinate of point A
	 * @param x2
	 *            The x coordinate of point B
	 * @param y2
	 *            The y coordinate of point B
	 */
	public void set(float x1, float y1, float x2, float y2) {
		pointA.set(x1, y1);
		pointB.set(x2, y2);
	}

	/**
	 * Returns if the coordinate x,y is on the line between point A and point B
	 * 
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 * @return False if the coordinate is not on the line between point A and
	 *         point B
	 */
	public boolean contains(float x, float y) {
		Point p3 = new Point(x, y);
		return p3.isOnLineBetween(pointA, pointB);
	}

	/**
	 * Returns if this {@link LineSegment} intersects another {@link LineSegment}
	 * @param lineSegment The {@link LineSegment} to check for intersection with
	 * @return True if this line intersects with lineSegment
	 */
	public boolean intersects(LineSegment lineSegment) {
		if (Intersector.intersectLines(pointA, pointB, lineSegment.getPointA(),
				lineSegment.getPointB(), intersection)) {
			return intersection.isOnLineBetween(pointA, pointB);
		}
		return false;
	}

	/**
	 * Returns the point at which this {@link LineSegment} intersects with another
	 * @param lineSegment The {@link LineSegment} to check for intersection with
	 * @return Null if the {@link LineSegment}s don't intersect
	 */
	public Point getIntersection(LineSegment lineSegment) {
		if (!Intersector.intersectLines(pointA, pointB, lineSegment.getPointA(),
				lineSegment.getPointB(), intersection)) {
			return null;
		}
		if (!intersection.isOnLineBetween(pointA, pointB))
			return null;
		return intersection;
	}

	/**
	 * Returns if a {@link Rectangle} intersects this {@link LineSegment}
	 * @param rectangle The {@link Rectangle} to test for intersection
	 * @return False if this {@link LineSegment} does not intersect the {@link Rectangle}
	 */
	public boolean intersects(Rectangle rectangle) {
		return rectangle.intersects(this);
	}

	/**
	 * Returns the {@link Point}s at which this {@link LineSegment} intersects a {@link Rectangle}
	 * @param rectangle The {@link Rectangle} to test for intersection
	 * @return An empty {@link List} if there are no intersections
	 */
	public List<Point> getIntersections(Rectangle rectangle) {
		List<Point> result = new ArrayList<Point>();
		Point intersection = rectangle.top.getIntersection(this);
		if (intersection != null) {
			result.add(intersection);
		}
		intersection = rectangle.bottom.getIntersection(this);
		if (intersection != null) {
			result.add(intersection);
		}
		intersection = rectangle.left.getIntersection(this);
		if (intersection != null) {
			result.add(intersection);
		}
		intersection = rectangle.right.getIntersection(this);
		if (intersection != null) {
			result.add(intersection);
		}
		return result;
	}

	public Point getPointA() {
		return pointA;
	}

	public void setPointA(Point pointA) {
		this.pointA = pointA;
	}

	public Point getPointB() {
		return pointB;
	}

	public void setPointB(Point pointB) {
		this.pointB = pointB;
	}

	@Override
	public String toString() {
		return "Line [pointA=" + pointA + ", pointB=" + pointB + "]";
	}
}
