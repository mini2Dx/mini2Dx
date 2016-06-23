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

import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.util.EdgeIterator;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Implements a circle
 */
public class Circle extends Shape {
	private static final long serialVersionUID = 7900371446650127192L;
	
	private final Vector2 centerTmp = new Vector2();
	private final Vector2 tmp1 = new Vector2();
	private final Vector2 tmp2 = new Vector2();
	private final Rectangle boundingBox = new Rectangle();
	
	private final CircleEdgeIterator edgeIterator = new CircleEdgeIterator();
	final com.badlogic.gdx.math.Circle circle;
	
	private boolean dirty = true;

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
		this.circle = new com.badlogic.gdx.math.Circle(centerX, centerY, radius);
	}
	
	/**
	 * Constructs a {@link Circle} as a copy of another
	 * @param circle The {@link Circle} to copy
	 */
	public Circle(Circle circle) {
		this.circle = new com.badlogic.gdx.math.Circle(circle.circle);
	}
	
	@Override
	public Shape copy() {
		return new Circle(this);
	}
	
	public Circle lerp(Circle target, float alpha) {
		final float inverseAlpha = 1.0f - alpha;
		circle.x = (circle.x * inverseAlpha) + (target.getX() * alpha);
		circle.y = (circle.y * inverseAlpha) + (target.getY() * alpha);
		circle.radius = (circle.radius * inverseAlpha) + (target.getRadius() * alpha);
		setDirty();
		return this;
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
	public boolean contains(Shape shape) {
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
	public boolean intersects(Shape shape) {
		if(shape.isCircle()) {
			return intersects((Circle) shape);
		}
		return shape.getPolygon().intersects(this);
	}
	
	@Override
	public boolean intersectsLineSegment(Vector2 pointA, Vector2 pointB) {
		centerTmp.set(circle.x, circle.y);
		return Intersector.intersectSegmentCircle(pointA, pointB, centerTmp, circle.radius * circle.radius);
	}

	@Override
	public boolean intersectsLineSegment(float x1, float y1, float x2, float y2) {
		tmp1.set(x1, y1);
		tmp2.set(x2, y2);
		centerTmp.set(circle.x, circle.y);
		return Intersector.intersectSegmentCircle(tmp1, tmp2, centerTmp, circle.radius * circle.radius);
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
	 * @param point The point
	 * @return The distance to the point
	 */
	public float getDistanceTo(Vector2 point) {
		return getDistanceTo(point.x, point.y);
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
		set(circle.getX(), circle.getY());
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
		circle.x = x;
		setDirty();
	}
	
	@Override
	public void setY(float y) {
		circle.y = y;
		setDirty();
	}
	
	@Override
	public void set(float x, float y) {
		circle.x = x;
		circle.y = y;
		setDirty();
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
		circle.radius = radius;
		setDirty();
	}

	@Override
	public void translate(float translateX, float translateY) {
		circle.x += translateX;
		circle.y += translateY;
		setDirty();
	}
	
	private void setDirty() {
		dirty = true;
	}
	
	private void computeBoundingBox() {
		if(!dirty) {
			return;
		}
		float diameter = circle.radius * circle.radius;
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
	public float getOriginX() {
		return 0f;
	}
	
	@Override
	public float getOriginY() {
		return 0f;
	}
	
	@Override
	public boolean isCircle() {
		return true;
	}

	@Override
	public Polygon getPolygon() {
		return null;
	}
}
