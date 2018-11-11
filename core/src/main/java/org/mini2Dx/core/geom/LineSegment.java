/**
 * Copyright (c) 2018 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice, this
 *   list of conditions and the following disclaimer in the documentation and/or
 *   other materials provided with the distribution.
 *
 * * Neither the name of mini2Dx nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.geom;

import org.mini2Dx.core.Geometry;

/**
 * Represents a segment of a line (the space between two points)
 */
public class LineSegment {
    private static final Point TMP_INTERSECTION = new Point();

    protected final Geometry geometry;
    protected Point pointA, pointB;

    /**
     * Constructs a {@link LineSegment} belonging to the {@link Geometry} pool
     * @param geometry The {@link Geometry} pool
     */
    public LineSegment(Geometry geometry) {
        super();
        this.pointA = new Point();
        this.pointB = new Point();
        this.geometry = geometry;
    }

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
        super();
        this.pointA = pA;
        this.pointB = pB;
        this.geometry = null;
    }

    /**
     * Releases this {@link LineSegment} back to the {@link Geometry} pool (if it was created from the pool)
     */
    public void release() {
        if(geometry == null) {
            return;
        }
        geometry.release(this);
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
        if (pointA.getX() == x && pointA.getY() == y)
            return true;
        if (pointB.getX() == x && pointB.getY() == y)
            return true;

        Point p3 = new Point(x, y);
        return p3.isOnLineBetween(pointA, pointB);
    }

    public boolean intersectsLineSegment(float segmentX1, float segmentY1, float segmentX2, float segmentY2) {
        return Intersector.intersectLineSegments(pointA.x, pointA.y, pointB.x, pointB.y, segmentX1, segmentY1,
                segmentX2, segmentY2);
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
        return intersectsLineSegment(lineSegment.getPointA().x, lineSegment.getPointA().y, lineSegment.getPointB().x,
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
    @Deprecated
    public Point getIntersection(LineSegment lineSegment) {
        if(getIntersection(lineSegment, TMP_INTERSECTION)) {
            return TMP_INTERSECTION;
        }
        return null;
    }

    /**
     * Returns the point at which this {@link LineSegment} intersects with another
     * @param lineSegment The {@link LineSegment} to check for intersection with
     * @param result The {@link Point} at which the lines intersect
     * @return True if the lines intersect, false otherwise
     */
    public boolean getIntersection(LineSegment lineSegment, Point result) {
        if (!Intersector.intersectLines(pointA, pointB, lineSegment.getPointA(), lineSegment.getPointB(),
                result)) {
            return false;
        }
        if (!result.isOnLineBetween(pointA, pointB)) {
            return false;
        }
        return true;
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

    public float getLength() {
        return pointA.getDistanceTo(pointB);
    }

    @Override
    public String toString() {
        return "LineSegment [pointA=" + pointA + ", pointB=" + pointB + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pointA == null) ? 0 : pointA.hashCode());
        result = prime * result + ((pointB == null) ? 0 : pointB.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LineSegment other = (LineSegment) obj;
        if (pointA == null) {
            if (other.pointA != null)
                return false;
        } else if (!pointA.equals(other.pointA))
            return false;
        if (pointB == null) {
            if (other.pointB != null)
                return false;
        } else if (!pointB.equals(other.pointB))
            return false;
        return true;
    }
}