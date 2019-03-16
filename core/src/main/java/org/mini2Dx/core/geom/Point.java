/*******************************************************************************
 * Copyright 2019 See AUTHORS file
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.mini2Dx.core.geom;

import org.mini2Dx.core.Geometry;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.math.Vector2;

/**
 * Extends {@link Vector2} with additional functionality
 */
public class Point extends Vector2 {
    private static final long serialVersionUID = 3773673953486445831L;

    private final Geometry geometry;

    /**
     * Constructs a new {@link Point} belonging to the {@link Geometry} pool
     * @param geometry The {@link Geometry} pool
     */
    public Point(Geometry geometry) {
        super();
        this.geometry = geometry;
    }

    /**
     * Constructs a new {@link Point} at 0,0
     */
    public Point() {
        super();
        this.geometry = null;
    }

    /**
     * Constructs a new {@link Point} at a specific coordinate
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public Point(float x, float y) {
        super(x, y);
        this.geometry = null;
    }

    /**
     * Constructs a new {@link Point} with the same coordinates as another {@link Point}
     * @param point The {@link Point} to copy from
     */
    public Point(Point point) {
        super(point);
        this.geometry = null;
    }

    /**
     * Releases this {@link Point} back to the {@link Geometry} pool (if it was created from the pool)
     */
    public void release() {
        if(geometry == null) {
            return;
        }
        geometry.release(this);
    }

    public float getDistanceTo(Point point) {
        return this.dst(point.getX(), point.getY());
    }

    public float getDistanceTo(float x, float y) {
        return this.dst(x, y);
    }

    /**
     * Rotates this {@link Point} around another {@link Point}
     *
     * @param center
     *            The {@link Point} to rotate around
     * @param degrees
     *            The angle to rotate by in degrees
     */
    public void rotateAround(Point center, float degrees) {
        rotateAround(center.x, center.y, degrees);
    }

    /**
     * Rotates this {@link Point} around a coordinate
     *
     * @param centerX The x coordinate to rotate around
     * @param centerY The y coordinate to rotate around
     * @param degrees
     *            The angle to rotate by in degrees
     */
    public void rotateAround(float centerX, float centerY, float degrees) {
        if (degrees == 0)
            return;

        float cos = MathUtils.cos(degrees * MathUtils.degreesToRadians);
        float sin = MathUtils.sin(degrees * MathUtils.degreesToRadians);

        float newX = (cos * (x - centerX) - sin * (y - centerY) + centerX);
        float newY = (sin * (x - centerX) + cos * (y - centerY) + centerY);

        set(newX, newY);
    }

    /**
     * Returns if this {@link Point} is between a and b on a line
     *
     * @param a
     *            {@link Point} a on a line
     * @param b
     *            {@link Point} b on a line
     * @return False if this {@link Point} is not on the same line as a and b OR
     *         is not between a and b on the same line
     */
    public boolean isOnLineBetween(Point a, Point b) {
        float areaOfTriangle = (a.x * (b.y - y) + b.x * (y - a.y) + x
                * (a.y - b.y)) / 2f;
        if (areaOfTriangle == 0f) {
            if (x == a.x && y == a.y)
                return true;
            if (x == b.x && y == b.y)
                return true;
            if (x == a.x) {
                /* Same x axis */
                return (y > a.y && y < b.y) || (y > b.y && y < a.y);
            } else {
                /* Same y axis */
                return (x > a.x && x < b.x) || (x > b.x && x < a.x);
            }
        }
        return false;
    }

    /**
     * Determines if another {@link Vector2} is exactly equal to this one
     *
     * @param v
     *            The {@link Vector2} to compare to
     * @return True if both {@link Vector2}s x and y are exactly equal
     */
    public boolean equals(Vector2 v) {
        return x == v.x && y == v.y;
    }

    /**
     * Determines if this and a {@link Vector2} are nearly equal. A delta of 0.1
     * means 0.0 and 0.1 would be considered equal but 0.0 and 0.11 would not.
     *
     * @param v
     *            The {@link Vector2} to compare to
     * @param delta
     *            The amount of error to allow for.
     * @return True if the two points are equal allowing for a certain margin of error
     */
    public boolean equals(Vector2 v, float delta) {
        return equals(v.x, v.y, delta);
    }

    /**
     * Determines if a coordinate is nearly equal to this one. A delta of 0.1
     * means 0.0 and 0.1 would be considered equal but 0.0 and 0.11 would not.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @param delta
     *            The amount of error to allow for.
     * @return True if the two points are equal allowing for a certain margin of error
     */
    public boolean equals(float x, float y, float delta) {
        return Math.abs(this.x - x) <= delta && Math.abs(this.y - y) <= delta;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public void setX(float x) {
        set(x, this.y);
    }

    public void setY(float y) {
        set(this.x, y);
    }

    @Override
    public Vector2 set(Vector2 v) {
        return set(v.x, v.y);
    }

    @Override
    public Vector2 add(Vector2 v) {
        return add(v.x, v.y);
    }

    @Override
    public Vector2 sub(Vector2 v) {
        return sub(v.x, v.y);
    }

    public Point copy() {
        return new Point(x, y);
    }
}
