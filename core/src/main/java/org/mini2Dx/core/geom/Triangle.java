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
 * Implements a triangle. Backed by a {@link Polygon}.
 */
public class Triangle extends Shape {
	private static final int TOTAL_SIDES = 3;

	final Polygon polygon;

	/**
	 * Constructor
	 * @param v1 The first point in the {@link Triangle}
	 * @param v2 The second point in the {@link Triangle}
	 * @param v3 The third point in the {@link Triangle}
	 */
	public Triangle(Vector2 v1, Vector2 v2, Vector2 v3) {
		this(v1.x, v1.y, v2.x, v2.y, v3.x, v3.y);
	}

	/**
	 * Constructor
	 * @param x1 The first x coordinate in the {@link Triangle}
	 * @param y1 The first y coordinate in the {@link Triangle}
	 * @param x2 The second x coordinate in the {@link Triangle}
	 * @param y2 The second y coordinate in the {@link Triangle}
	 * @param x3 The third x coordinate in the {@link Triangle}
	 * @param y3 The third y coordinate in the {@link Triangle}
	 */
	public Triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
		super();
		polygon = new Polygon(new float[] { x1, y1, x2, y2, x3, y3 });
		initProxyListeners();
	}
	
	/**
	 * Constructs a {@link Triangle} as a copy of another
	 * @param triangle The {@link Triangle} to copy
	 */
	public Triangle(Triangle triangle) {
		super();
		polygon = (Polygon) triangle.polygon.copy();
		initProxyListeners();
	}

	/**
	 * Constructs a {@link Triangle} belonging to the {@link Geometry} pool
	 * @param geometry The {@link Geometry} pool
	 * @param x1 The first x coordinate in the {@link Triangle}
	 * @param y1 The first y coordinate in the {@link Triangle}
	 * @param x2 The second x coordinate in the {@link Triangle}
	 * @param y2 The second y coordinate in the {@link Triangle}
	 * @param x3 The third x coordinate in the {@link Triangle}
	 * @param y3 The third y coordinate in the {@link Triangle}
	 */
	public Triangle(Geometry geometry, float x1, float y1, float x2, float y2, float x3, float y3) {
		super(geometry);
		polygon = new Polygon(new float[] { x1, y1, x2, y2, x3, y3 });
		initProxyListeners();
	}

	private void initProxyListeners() {
		polygon.addPostionChangeListener(new PositionChangeListener<Positionable>() {
			@Override
			public void positionChanged(Positionable moved) {
				Triangle.this.notifyPositionChangeListeners();
			}
		});
		polygon.addSizeChangeListener(new SizeChangeListener<Sizeable>() {
			@Override
			public void sizeChanged(Sizeable changed) {
				Triangle.this.notifySizeChangeListeners();
			}
		});
	}

	@Override
	public void dispose() {
		clearPositionChangeListeners();
		clearSizeChangeListeners();

		if(geometry == null) {
			return;
		}
		geometry.release(this);
	}
	
	@Override
	public Shape copy() {
		return new Triangle(this);
	}
	
	@Override
	public boolean contains(float x, float y) {
		return polygon.contains(x, y);
	}

	@Override
	public boolean contains(Vector2 vector2) {
		return polygon.contains(vector2.x, vector2.y);
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
		return polygon.getWidth();
	}

	@Override
	public float getHeight() {
		return polygon.getHeight();
	}

	/**
	 * Returns if this {@link Triangle} intersects a {@link Polygon}
	 * 
	 * @param polygon
	 *            The {@link Polygon} to check
	 * @return True if this {@link Triangle} and {@link Polygon} intersect
	 */
	public boolean intersects(Polygon polygon) {
		return polygon.intersects(polygon);
	}
	
	/**
	 * Returns if this {@link Triangle} intersects another {@link Triangle}
	 * 
	 * @param triangle
	 *            The {@link Triangle} to check
	 * @return True if both {@link Triangle}s intersect
	 */
	public boolean intersects(Triangle triangle) {
		return polygon.intersects(triangle.polygon);
	}

	/**
	 * Returns if the specified {@link Rectangle} intersects this
	 * {@link Triangle}
	 * 
	 * @param rectangle
	 *            The {@link Rectangle} to check
	 * @return True if this {@link Triangle} and {@link Rectangle} intersect
	 */
	public boolean intersects(Rectangle rectangle) {
		return polygon.intersects(rectangle);
	}

	public void translate(float translateX, float translateY) {
		polygon.translate(translateX, translateY);
	}

	public void setRotation(float degrees) {
		polygon.setRotation(degrees);
	}

	public void rotate(float degrees) {
		polygon.rotate(degrees);
	}

	@Override
	public int getNumberOfSides() {
		return TOTAL_SIDES;
	}

	@Override
	public void draw(Graphics g) {
		polygon.draw(g);
	}

	@Override
	public void fill(Graphics g) {
		polygon.fill(g);
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
	public void setRotationAround(float centerX, float centerY, float degrees) {
		polygon.setRotationAround(centerX, centerY, degrees);
	}

	@Override
	public void rotateAround(float centerX, float centerY, float degrees) {
		polygon.rotateAround(centerX, centerY, degrees);
	}
	
	@Override
	public float getX() {
		return polygon.getX();
	}
	
	@Override
	public void setX(float x) {
		polygon.setXY(x, polygon.getY());
	}
	
	@Override
	public float getY() {
		return polygon.getY();
	}

	@Override
	public void setY(float y) {
		polygon.setXY(polygon.getX(), y);
	}

	@Override
	public void setXY(float x, float y) {
		polygon.setXY(x, y);
	}

	public void setPosition(float x1, float y1, float x2, float y2, float x3, float y3) {
		polygon.setVertices(new float[] { x1, y1, x2, y2, x3, y3 });
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
	public void setRadius(float radius) {
		polygon.setRadius(radius);
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
	public void scale(float scale) {
		polygon.scale(scale);
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
		Triangle other = (Triangle) obj;
		if (polygon == null) {
			if (other.polygon != null)
				return false;
		} else if (!polygon.equals(other.polygon))
			return false;
		return true;
	}
}
