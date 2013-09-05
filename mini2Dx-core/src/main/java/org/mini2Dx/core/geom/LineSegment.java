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

	public boolean intersectsLineSegment(float segmentX1, float segmentY1,
			float segmentX2, float segmentY2) {
		float x1 = pointA.x, y1 = pointA.y, x2 = pointB.x, y2 = pointB.y, x3 = segmentX1, y3 = segmentY1, x4 = segmentX2, y4 = segmentY2;

		// Return false if either of the lines have zero length
		if (x1 == x2 && y1 == y2 || x3 == x4 && y3 == y4) {
			return false;
		}
		// Fastest method, based on Franklin Antonio's
		// "Faster Line Segment Intersection" topic "in Graphics Gems III" book
		// (http://www.graphicsgems.org/)
		float ax = x2 - x1;
		float ay = y2 - y1;
		float bx = x3 - x4;
		float by = y3 - y4;
		float cx = x1 - x3;
		float cy = y1 - y3;

		float alphaNumerator = by * cx - bx * cy;
		float commonDenominator = ay * bx - ax * by;
		if (commonDenominator > 0) {
			if (alphaNumerator < 0 || alphaNumerator > commonDenominator) {
				return false;
			}
		} else if (commonDenominator < 0) {
			if (alphaNumerator > 0 || alphaNumerator < commonDenominator) {
				return false;
			}
		}
		float betaNumerator = ax * cy - ay * cx;
		if (commonDenominator > 0) {
			if (betaNumerator < 0 || betaNumerator > commonDenominator) {
				return false;
			}
		} else if (commonDenominator < 0) {
			if (betaNumerator > 0 || betaNumerator < commonDenominator) {
				return false;
			}
		}
		if (commonDenominator == 0) {
			// This code wasn't in Franklin Antonio's method. It was added by
			// Keith Woodward.
			// The lines are parallel.
			// Check if they're collinear.
			float y3LessY1 = y3 - y1;
			float collinearityTestForP3 = x1 * (y2 - y3) + x2 * (y3LessY1) + x3
					* (y1 - y2); // see
									// http://mathworld.wolfram.com/Collinear.html
			// If p3 is collinear with p1 and p2 then p4 will also be collinear,
			// since p1-p2 is parallel with p3-p4
			if (collinearityTestForP3 == 0) {
				// The lines are collinear. Now check if they overlap.
				if (x1 >= x3 && x1 <= x4 || x1 <= x3 && x1 >= x4 || x2 >= x3
						&& x2 <= x4 || x2 <= x3 && x2 >= x4 || x3 >= x1
						&& x3 <= x2 || x3 <= x1 && x3 >= x2) {
					if (y1 >= y3 && y1 <= y4 || y1 <= y3 && y1 >= y4
							|| y2 >= y3 && y2 <= y4 || y2 <= y3 && y2 >= y4
							|| y3 >= y1 && y3 <= y2 || y3 <= y1 && y3 >= y2) {
						return true;
					}
				}
			}
			return false;
		}
		return true;
	}

	/**
	 * Returns if this {@link LineSegment} intersects another
	 * {@link LineSegment}
	 * 
	 * @param lineSegment
	 *            The {@link LineSegment} to check for intersection with
	 * @return True if this line intersects with lineSegment
	 */
	public boolean intersects(LineSegment lineSegment) {
		return intersectsLineSegment(lineSegment.getPointA().x,
				lineSegment.getPointA().y, lineSegment.getPointB().x,
				lineSegment.getPointB().y);
	}

	/**
	 * Returns the point at which this {@link LineSegment} intersects with
	 * another
	 * 
	 * @param lineSegment
	 *            The {@link LineSegment} to check for intersection with
	 * @return Null if the {@link LineSegment}s don't intersect
	 */
	public Point getIntersection(LineSegment lineSegment) {
		if (!Intersector.intersectLines(pointA, pointB,
				lineSegment.getPointA(), lineSegment.getPointB(), intersection)) {
			return null;
		}
		if (!intersection.isOnLineBetween(pointA, pointB))
			return null;
		return intersection;
	}

	/**
	 * Returns if a {@link Rectangle} intersects this {@link LineSegment}
	 * 
	 * @param rectangle
	 *            The {@link Rectangle} to test for intersection
	 * @return False if this {@link LineSegment} does not intersect the
	 *         {@link Rectangle}
	 */
	public boolean intersects(Rectangle rectangle) {
		return rectangle.intersects(this);
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

	public float getMinX() {
		if (pointA.getX() < pointB.getX())
			return pointA.getX();
		return pointB.getX();
	}

	public float getMinY() {
		if (pointA.getY() < pointB.getY())
			return pointA.getY();
		return pointB.getY();
	}

	public float getMaxX() {
		if (pointA.getX() > pointB.getX())
			return pointA.getX();
		return pointB.getX();
	}

	public float getMaxY() {
		if (pointA.getY() > pointB.getY())
			return pointA.getY();
		return pointB.getY();
	}

	@Override
	public String toString() {
		return "Line [pointA=" + pointA + ", pointB=" + pointB + "]";
	}
}
