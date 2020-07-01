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
import org.mini2Dx.core.util.Lerper;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.math.Vector2;

/**
 * Implements a circle
 */
public class Circle extends Shape {
	private static final long serialVersionUID = 7900371446650127192L;

	private static final ThreadLocal<Vector2> TMP_VECTOR1 = new ThreadLocal<Vector2>() {
		@Override
		protected Vector2 initialValue() {
			return new Vector2();
		}
	};
	private static final ThreadLocal<Vector2> TMP_VECTOR2 = new ThreadLocal<Vector2>() {
		@Override
		protected Vector2 initialValue() {
			return new Vector2();
		}
	};
	private static final ThreadLocal<Vector2> CENTER_TMP = new ThreadLocal<Vector2>() {
		@Override
		protected Vector2 initialValue() {
			return new Vector2();
		}
	};

	private final Geometry geometry;
	private final Rectangle boundingBox = new Rectangle();
	
	private final CircleEdgeIterator edgeIterator = new CircleEdgeIterator();
	final org.mini2Dx.gdx.math.Circle circle;
	
	private boolean dirty = true;

	/**
	 * Constructs a {@link Circle} belonging to the {@link Geometry} pool
	 * @param geometry The {@link Geometry} pool
	 */
	public Circle(Geometry geometry) {
		this.circle = new org.mini2Dx.gdx.math.Circle(0f, 0f, 1f);
		this.geometry = geometry;
	}

	/**
	 * Constructs a {@link Circle} at 0,0 with a radius
	 * @param radius The {@link Circle} radius
	 */
	public Circle(float radius) {
		this(0f, 0f, radius);
	}
	
	/**
	 * Constructs a {@link Circle} with a center and radius
	 * @param centerX The center x coordinate
	 * @param centerY The center y coordinate
	 * @param radius The radius
	 */
	public Circle(float centerX, float centerY, float radius) {
		this.circle = new org.mini2Dx.gdx.math.Circle(centerX, centerY, radius);
		this.geometry = null;
	}
	
	/**
	 * Constructs a {@link Circle} as a copy of another
	 * @param circle The {@link Circle} to copy
	 */
	public Circle(Circle circle) {
		this.circle = new org.mini2Dx.gdx.math.Circle(circle.circle);
		this.geometry = null;
	}

	@Override
	public void dispose() {
		if(disposed) {
			return;
		}
		disposed = true;

		clearPositionChangeListeners();
		clearSizeChangeListeners();

		if(geometry == null) {
			return;
		}
		geometry.release(this);
	}

	@Override
	public Shape copy() {
		return new Circle(this);
	}
	
	public Circle lerp(Circle target, float alpha) {
		lerp(this, target, alpha);
		return this;
	}

	public void lerp(Circle result, Circle target, float alpha) {
		lerp(result, this, target, alpha);
	}

	public static void lerp(Circle result, Circle from, Circle target, float alpha) {
		result.circle.x = Lerper.lerp(from.circle.x, target.circle.x, alpha);
		result.circle.y = Lerper.lerp(from.circle.y, target.circle.y, alpha);
		result.circle.radius = Lerper.lerp(from.circle.radius, target.circle.radius, alpha);
		result.notifyPositionChangeListeners();
		result.notifyPositionChangeListeners();
		result.notifySizeChangeListeners();
		result.setDirty();
	}

	@Override
	public boolean contains(Vector2 point) {
		return contains(point.x, point.y);
	}
	
	@Override
	public boolean contains(float x, float y) {
		float dx = Math.abs(x - circle.x);
		if(dx > circle.radius) {
			return false;
		}
		
		float dy = Math.abs(y - circle.y);
		if(dy > circle.radius) {
			return false;
		}
		
		if(dx + dy <= circle.radius) {
			return true;
		}
		if((dx * dx) + (dy * dy) <= (circle.radius * circle.radius)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean contains(Sizeable shape) {
		if(shape.isCircle()) {
			return contains((Circle) shape);
		}
		if(shape.getNumberOfSides() == 4) {
			float distanceX = Math.max(circle.x - shape.getMinX(), shape.getMaxX() - circle.x);
			float distanceY = Math.max(circle.y - shape.getMinY(), shape.getMaxY() - circle.y);
			return circle.radius * circle.radius >= (distanceX * distanceX) + (distanceY * distanceY);
		}
		return contains(shape.getPolygon());
	}
	
	/**
	 * Returns if another {@link Circle} is contained within this one
	 * @param circle The {@link Circle} to check
	 * @return True if the other {@link Circle} is contained within this one
	 */
	public boolean contains(Circle circle) {
		return this.circle.contains(circle.circle);
	}
	
	/**
	 * Returns if this {@link Circle} contains a {@link Rectangle}
	 * @param rectangle The {@link Rectangle} to check
	 * @return True if this {@link Circle} contains the {@link Rectangle}
	 */
	public boolean contains(Rectangle rectangle) {
		float distanceX = Math.max(circle.x - rectangle.getMinX(), rectangle.getMaxX() - circle.x);
		float distanceY = Math.max(circle.y - rectangle.getMinY(), rectangle.getMaxY() - circle.y);
		return circle.radius * circle.radius >= (distanceX * distanceX) + (distanceY * distanceY);
	}
	
	/**
	 * Returns if this {@link Circle} contains a {@link Polygon}
	 * @param polygon The {@link Polygon} to check
	 * @return True if this {@link Circle} contains the {@link Polygon}
	 */
	public boolean contains(Polygon polygon) {
		float [] vertices = polygon.getVertices();
		for(int i = 0; i < vertices.length; i += 2) {
			if(!contains(vertices[i], vertices[i + 1])) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean intersects(Sizeable shape) {
		if(shape.isCircle()) {
			return intersects((Circle) shape);
		}
		return shape.getPolygon().intersects(this);
	}
	
	@Override
	public boolean intersectsLineSegment(Vector2 pointA, Vector2 pointB) {
		final Vector2 centerTmp = CENTER_TMP.get();
		centerTmp.set(circle.x, circle.y);
		return org.mini2Dx.gdx.math.Intersector.intersectSegmentCircle(pointA, pointB, centerTmp, circle.radius * circle.radius);
	}

	@Override
	public boolean intersectsLineSegment(float x1, float y1, float x2, float y2) {
		final Vector2 tmpVector1 = TMP_VECTOR1.get();
		final Vector2 tmpVector2 = TMP_VECTOR2.get();
		final Vector2 centerTmp = CENTER_TMP.get();
		tmpVector1.set(x1, y1);
		tmpVector2.set(x2, y2);
		centerTmp.set(circle.x, circle.y);
		return org.mini2Dx.gdx.math.Intersector.intersectSegmentCircle(tmpVector1, tmpVector2, centerTmp, circle.radius * circle.radius);
	}

	@Override
	public float getWidth() {
		return getRadius() * 2f;
	}

	@Override
	public float getHeight() {
		return getRadius() * 2f;
	}

	/**
	 * Returns if the specified {@link Rectangle} intersects this {@link Circle}
	 * 
	 * @param rectangle The {@link Rectangle} to test for intersection
	 * @return True if intersection occurs
	 */
	public boolean intersects(Rectangle rectangle) {
		return org.mini2Dx.core.geom.Intersector.intersects(rectangle, this);
	}

	/**
	 * Returns if the specified {@link Circle} intersects this one
	 * 
	 * @param circle The {@link Circle} to test for intersection
	 * @return True if intersection occurs
	 */
	public boolean intersects(Circle circle) {
		return Vector2.dst(this.circle.x, this.circle.y, circle.getX(), circle.getY()) <= this.circle.radius + circle.getRadius();
	}
	
	/**
	 * Returns the distance from the edge of this {@link Circle} to a point
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return The distance to the point
	 */
	public float getDistanceTo(float x, float y) {
		float result = Vector2.dst(circle.x, circle.y, x, y);
		if(result <= circle.radius) {
			return 0f;
		}
		return result - circle.radius;
	}
	
	/**
	 * Returns the distance from the center of this {@link Circle} to a point
	 * @param point The point
	 * @return The distane to the point
	 */
	public float getDistanceFromCenter(Vector2 point) {
		return Vector2.dst(circle.x, circle.y, point.x, point.y);
	}
	
	/**
	 * Returns the distance from the center of this {@link Circle} to a point
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return The distane to the point
	 */
	public float getDistanceFromCenter(float x, float y) {
		return Vector2.dst(circle.x, circle.y, x, y);
	}
	
	@Override
	public int getNumberOfSides() {
		return 0;
	}

	@Override
	public void draw(Graphics g) {
		g.drawCircle(circle.x, circle.y, MathUtils.round(circle.radius));
	}
	
	@Override
	public void fill(Graphics g) {
		g.fillCircle(circle.x, circle.y, MathUtils.round(circle.radius));
	}
	
	public void set(Circle circle) {
		setXY(circle.getX(), circle.getY());
		setRadius(circle.getRadius());
	}

	@Override
	public float getX() {
		return circle.x;
	}

	@Override
	public float getY() {
		return circle.y;
	}
	
	@Override
	public void setX(float x) {
		setCenterX(x);
	}
	
	@Override
	public void setY(float y) {
		setCenterY(y);
	}

	@Override
	public float getCenterX() {
		return circle.x;
	}

	@Override
	public float getCenterY() {
		return circle.y;
	}
	
	@Override
	public void setCenter(float x, float y) {
		setXY(x, y);
	}

	@Override
	public void setCenterX(float x) {
		if(MathUtils.isEqual(circle.x, x)) {
			return;
		}
		circle.x = x;
		setDirty();
		notifyPositionChangeListeners();
	}

	@Override
	public void setCenterY(float y) {
		if(MathUtils.isEqual(circle.y, y)) {
			return;
		}
		circle.y = y;
		setDirty();
		notifyPositionChangeListeners();
	}
	
	@Override
	public void setXY(float x, float y) {
		if(MathUtils.isEqual(circle.x, x) && MathUtils.isEqual(circle.y, y)) {
			return;
		}

		circle.x = x;
		circle.y = y;
		setDirty();
		notifyPositionChangeListeners();
	}
	
	@Override
	public float getMinX() {
		return circle.x - circle.radius;
	}

	@Override
	public float getMinY() {
		return circle.y - circle.radius;
	}

	@Override
	public float getMaxX() {
		return circle.x + circle.radius;
	}

	@Override
	public float getMaxY() {
		return circle.y + circle.radius;
	}

	public float getRadius() {
		return circle.radius;
	}

	public void setRadius(float radius) {
		if(MathUtils.isEqual(circle.radius, radius)) {
			return;
		}
		circle.radius = radius;
		setDirty();
		notifySizeChangeListeners();
	}
	
	@Override
	public void scale(float scale) {
		setRadius(circle.radius * scale);
	}

	@Override
	public void translate(float translateX, float translateY) {
		if(MathUtils.isZero(translateX) && MathUtils.isZero(translateY)) {
			return;
		}
		circle.x += translateX;
		circle.y += translateY;
		setDirty();
		notifyPositionChangeListeners();
	}
	
	private void setDirty() {
		dirty = true;
	}
	
	private void computeBoundingBox() {
		if(!dirty) {
			return;
		}
		float diameter = circle.radius * 2;
		boundingBox.set(getMinX(), getMinY(), diameter, diameter);
		dirty = false;
	}

	@Override
	public EdgeIterator edgeIterator() {
		return edgeIterator;
	}
	
	/**
	 * Returns the bounding box of this {@link Circle}
	 * @return A {@link Rectangle} representing the area of this {@link Circle}
	 */
	public Rectangle getBoundingBox() {
		computeBoundingBox();
		return boundingBox;
	}
	
	private class CircleEdgeIterator extends EdgeIterator {

		@Override
		protected void beginIteration() {}

		@Override
		protected void endIteration() {}

		@Override
		protected void nextEdge() {}

		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public float getPointAX() {
			return circle.x;
		}

		@Override
		public float getPointAY() {
			return circle.y;
		}

		@Override
		public float getPointBX() {
			return circle.x;
		}

		@Override
		public float getPointBY() {
			return circle.y;
		}

		@Override
		public LineSegment getEdgeLineSegment() {
			return null;
		}
	}

	@Override
	public float getRotation() {
		return 0f;
	}

	@Override
	public void setRotation(float degrees) {}

	@Override
	public void setRotationAround(float centerX, float centerY, float degrees) {}

	@Override
	public void rotate(float degrees) {}

	@Override
	public void rotateAround(float centerX, float centerY, float degrees) {}
	
	@Override
	public boolean isCircle() {
		return true;
	}

	@Override
	public Polygon getPolygon() {
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((circle == null) ? 0 : circle.hashCode());
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
		Circle other = (Circle) obj;
		if (Float.floatToIntBits(getX()) != Float.floatToIntBits(other.getX()))
			return false;
		if (Float.floatToIntBits(getY()) != Float.floatToIntBits(other.getY()))
			return false;
		if (Float.floatToIntBits(getRadius()) != Float.floatToIntBits(other.getRadius()))
			return false;
		return true;
	}
}
