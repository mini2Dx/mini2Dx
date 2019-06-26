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
import org.mini2Dx.core.Graphics;
import org.mini2Dx.gdx.math.Vector2;

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

	protected RegularPolygon(Geometry geometry, int totalSides, float rotationSymmetry) {
		super(geometry);
		this.center = new Point(0f, 0f);
		this.rotationSymmetry = rotationSymmetry;
		this.totalSides = totalSides;
		setRadius(1f);
		initProxyListeners();
	}

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
		super();
		this.center = new Point(centerX, centerY);
		this.rotationSymmetry = rotationSymmetry;
		this.totalSides = totalSides;
		setRadius(radius);
		initProxyListeners();
	}
	
	/**
	 * Constructs a new {@link RegularPolygon} as a copy of another
	 * @param regularPolygon The {@link RegularPolygon} to copy
	 */
	public RegularPolygon(RegularPolygon regularPolygon) {
		super();
		this.center = regularPolygon.center.copy();
		this.rotationSymmetry = regularPolygon.rotationSymmetry;
		this.totalSides = regularPolygon.totalSides;
		setRadius(regularPolygon.getRadius());
		initProxyListeners();
	}

	private void initProxyListeners() {
		polygon.addPostionChangeListener(new PositionChangeListener<Positionable>() {
			@Override
			public void positionChanged(Positionable moved) {
				RegularPolygon.this.notifyPositionChangeListeners();
			}
		});
		polygon.addSizeChangeListener(new SizeChangeListener<Sizeable>() {
			@Override
			public void sizeChanged(Sizeable changed) {
				RegularPolygon.this.notifySizeChangeListeners();
			}
		});
	}

	@Override
	public void dispose() {
		clearPositionChangeListeners();
		clearSizeChangeListeners();
	}
	
	@Override
	public Shape copy() {
		return new RegularPolygon(this);
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
	public boolean contains(Sizeable shape) {
		return polygon.contains(shape);
	}

	@Override
	public boolean intersects(Sizeable shape) {
		return polygon.intersects(shape);
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
	public float getWidth() {
		return radius * 2f;
	}

	@Override
	public float getHeight() {
		return radius * 2f;
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
	public void setXY(float centerX, float centerY) {
		float diffX = centerX - this.center.getX();
		float diffY = centerY - this.center.getY();
		polygon.translate(diffX, diffY);
		center.set(centerX, centerY);
	}
	
	
	@Override
	public void translate(float translateX, float translateY) {
		setXY(getX() + translateX, getY() + translateY);
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
	@Override
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
		if(polygon == null) {
			polygon = new Polygon(points);
		} else {
			polygon.setVertices(points);
		}
	}
	
	@Override
	public void scale(float scale) {
		setRadius(radius * scale);
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

	@Override
	public boolean isCircle() {
		return false;
	}

	@Override
	public Polygon getPolygon() {
		return polygon;
	}

	@Override
	public float getMinX() {
		return polygon.getMinX();
	}

	@Override
	public float getMinY() {
		return polygon.getMinY();
	}

	@Override
	public float getMaxX() {
		return polygon.getMaxX();
	}

	@Override
	public float getMaxY() {
		return polygon.getMaxY();
	}

	@Override
	public float getCenterX() {
		return polygon.getCenterX();
	}

	@Override
	public float getCenterY() {
		return polygon.getCenterY();
	}
	
	@Override
	public void setCenter(float x, float y) {
		polygon.setCenter(x, y);
	}

	@Override
	public void setCenterX(float x) {
		polygon.setCenterX(x);
	}

	@Override
	public void setCenterY(float y) {
		polygon.setCenterY(y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((polygon == null) ? 0 : polygon.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegularPolygon other = (RegularPolygon) obj;
		if (polygon == null) {
			if (other.polygon != null)
				return false;
		} else if (!polygon.equals(other.polygon))
			return false;
		return true;
	}
}
