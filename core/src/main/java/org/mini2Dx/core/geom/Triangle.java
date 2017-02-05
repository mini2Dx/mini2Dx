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
		polygon = new Polygon(new float[] { x1, y1, x2, y2, x3, y3 });
	}
	
	/**
	 * Constructs a {@link Triangle} as a copy of another
	 * @param triangle The {@link Triangle} to copy
	 */
	public Triangle(Triangle triangle) {
		polygon = (Polygon) triangle.polygon.copy();
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
	public boolean contains(Shape shape) {
		return polygon.contains(shape);
	}

	@Override
	public boolean intersects(Shape shape) {
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
		polygon.set(x, polygon.getY());
	}
	
	@Override
	public float getY() {
		return polygon.getY();
	}

	@Override
	public void setY(float y) {
		polygon.set(polygon.getX(), y);
	}

	@Override
	public void set(float x, float y) {
		polygon.set(x, y);
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
}
