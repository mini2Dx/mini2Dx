/*******************************************************************************
 * Copyright 2019 Viridian Software Limited
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

import org.mini2Dx.gdx.math.Vector2;

/**
 * A common interface for objects that can be sized with an x and y dimension
 */
public interface Sizeable extends Positionable {

	/**
	 * Returns if this shape contains the specified shape
	 * @param shape The {@link Sizeable} to check
	 * @return True if this {@link Sizeable} is inside this shape
	 */
	public boolean contains(Sizeable shape);

	/**
	 * Returns if a {@link Vector2} is contained inside this shape
	 *
	 * @param point
	 *            The {@link Vector2} to check
	 * @return True if this shape contains the specified {@link Vector2}
	 */
	public boolean contains(Vector2 point);

	/**
	 * Returns if a set of coordinates are contained inside this shape
	 *
	 * @param x
	 *            The x coordinate to check
	 * @param y
	 *            The y coordinate to check
	 * @return True if this shape contains the specified coordinates
	 */
	public boolean contains(float x, float y);

	/**
	 * Returns if this shape intersects a shape
	 * @param shape The {@link Sizeable} to check
	 * @return True if this shape intersects the specified {@link Sizeable}
	 */
	public boolean intersects(Sizeable shape);

	/**
	 * Returns if this shape intersects the specified
	 * {@link LineSegment}
	 *
	 * @param lineSegment
	 *            The {@link LineSegment} to check
	 * @return True if this shape intersects the {@link LineSegment}
	 */
	public boolean intersects(LineSegment lineSegment);

	/**
	 * Returns if this shape intersects a line segment
	 *
	 * @param pointA
	 *            The first point in the line segment
	 * @param pointB
	 *            The second point in the line segment
	 * @return True if this shape intersects the line segment
	 */
	public boolean intersectsLineSegment(Vector2 pointA, Vector2 pointB);

	/**
	 * Returns if this shape intersects a line segment
	 *
	 * @param x1 The x coordinate of the first point
	 * @param y1 The y coordinate of the first point
	 * @param x2 The x coordinate of the second point
	 * @param y2 The y coordinate of the second point
	 * @return True if this shape intersects the line segment
	 */
	public boolean intersectsLineSegment(float x1, float y1, float x2, float y2);

	/**
	 * Returns the width of this object
	 *
	 * @return 0 by default
	 */
	public float getWidth();

	/**
	 * Returns the height of this object
	 *
	 * @return 0 by default
	 */
	public float getHeight();

	/**
	 * Sets the radius of this shape. For {@link Polygon} shapes, this will
	 * stretch in/out the shape from its center. Note that {@link Polygon}s must
	 * be equilateral.
	 *
	 * @param radius
	 *            The radius in pixels
	 */
	public void setRadius(float radius);

	/**
	 * Scales the radius of this shape. For {@link Polygon} shapes, this will
	 * stretch in/out the shape from its center. Note that {@link Polygon}s must
	 * be equilateral.
	 *
	 * @param scale The amount to scale by (e.g. 2.0 = double the size)
	 */
	public void scale(float scale);

	/**
	 * Returns min X coordinate of this object
	 *
	 * @return The left-most x coordinate
	 */
	public float getMinX();

	/**
	 * Returns min Y coordinate of this object
	 *
	 * @return The up-most y coordinate
	 */
	public float getMinY();

	/**
	 * Returns max X coordinate of this object
	 *
	 * @return The right-most x coordinate
	 */
	public float getMaxX();

	/**
	 * Returns max Y coordinate of this object
	 *
	 * @return The bottom-most y coordinate
	 */
	public float getMaxY();

	/**
	 * Returns the center x coordinate of this object. Note for {@link Circle}
	 * this is the same as x.
	 *
	 * @return 0 by default
	 */
	public float getCenterX();

	/**
	 * Returns the center y coordinate of this object. Note for {@link Circle}
	 * this is the same as y.
	 *
	 * @return 0 by default
	 */
	public float getCenterY();

	/**
	 * Sets the center x,y coordinate of this object
	 * @param x The x coordinate of the shape's center
	 * @param y The y coordinate of the shape's center
	 */
	public void setCenter(float x, float y);

	/**
	 * Sets the center x coordinate
	 * @param x The x coordinate of the shape's center
	 */
	public void setCenterX(float x);

	/**
	 * Sets the center y coordianate
	 * @param y The y coordinate of the shape's center
	 */
	public void setCenterY(float y);

	/**
	 * Adds a {@link SizeChangeListener} to be notified of size changes
	 *
	 * @param listener
	 *            The {@link SizeChangeListener} to add
	 */
	public <T extends Sizeable> void addSizeChangeListener(SizeChangeListener<T> listener);

	/**
	 * Removes a {@link SizeChangeListener} to stop it being notified of size
	 * changes
	 *
	 * @param listener
	 *            The {@link SizeChangeListener} to remove
	 */
	public <T extends Sizeable> void removeSizeChangeListener(SizeChangeListener<T> listener);

	/**
	 * Returns if this is a {@link Circle} instance
	 * @return False unless a {@link Circle} instance
	 */
	public boolean isCircle();

	/**
	 * Returns the underlying {@link Polygon} for this shape
	 * @return The {@link Polygon} for this shape
	 */
	public Polygon getPolygon();

	/**
	 * Returns the number of sides of this shape
	 * @return 0 if a {@link Circle}
	 */
	public int getNumberOfSides();
}
