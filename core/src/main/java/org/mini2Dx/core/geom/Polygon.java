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

import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.util.EdgeIterator;

import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ShortArray;

/**
 * Implements a rotatable polygon. Adds extra functionality to the default
 * polygon implementation in LibGDX
 */
public class Polygon extends Shape {
	private final EarClippingTriangulator triangulator;
	private final PolygonEdgeIterator edgeIterator = new PolygonEdgeIterator();
	
	final com.badlogic.gdx.math.Polygon polygon;
	
	private int totalSidesCache = -1;
	private int maxXIndex, maxYIndex;
	private ShortArray triangles;

	/**
	 * Constructor
	 * @param vertices All points in x,y pairs. E.g. x1,y1,x2,y2,etc.
	 */
	public Polygon(float[] vertices) {
		polygon = new com.badlogic.gdx.math.Polygon(vertices);
		triangulator = new EarClippingTriangulator();
		computeTriangles(vertices);
		calculateMaxXY(vertices);
	}

	/**
	 * Constructor with vectors
	 * @param points All points in the polygon
	 */
	public Polygon(Vector2 [] points) {
		this(toVertices(points));
	}
	
	private void clearTotalSidesCache() {
		totalSidesCache = -1;
	}

	private void computeTriangles(float[] vertices) {
		triangles = triangulator.computeTriangles(vertices);
	}

	private void calculateMaxXY(float[] vertices) {
		int maxXIndex = 0;
		int maxYIndex = 1;
		for (int i = 2; i < vertices.length; i += 2) {
			if (vertices[i] > vertices[maxXIndex]) {
				maxXIndex = i;
			}
			if (vertices[i + 1] > vertices[maxYIndex]) {
				maxYIndex = i + 1;
			}
		}
		this.maxXIndex = maxXIndex;
		this.maxYIndex = maxYIndex;
	}
	
	/**
	 * Returns if this {@link Polygon} intersects another
	 * @param polygon The other {@link Polygon}
	 * @return True if the two {@link Polygon}s intersect
	 */
	public boolean intersects(Polygon polygon) {
		return Intersector.intersectPolygons(this.polygon, polygon.polygon, null);
	}
	
	/**
	 * Returns if the specified {@link Rectangle} intersects this {@link Polygon}
	 * @param rectangle The {@link Rectangle} to check
	 * @return True if this {@link Polygon} and {@link Rectangle} intersect
	 */
	public boolean intersects(Rectangle rectangle) {
		return rectangle.intersects(this);
	}
	
	@Override
	public boolean intersects(LineSegment lineSegment) {
		return intersectsLineSegment(lineSegment.getPointA(), lineSegment.getPointB());
	}
	
	@Override
	public boolean intersectsLineSegment(Vector2 pointA, Vector2 pointB) {
		return Intersector.intersectLinePolygon(pointA, pointB, polygon);
	}

	/**
	 * Adds an additional point to this {@link Polygon}
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public void addPoint(float x, float y) {
		float[] existingVertices = polygon.getVertices();
		float[] newVertices = new float[existingVertices.length + 2];

		if (existingVertices.length > 0) {
			System.arraycopy(existingVertices, 0, newVertices, 0, existingVertices.length);
		}
		newVertices[existingVertices.length] = x;
		newVertices[existingVertices.length + 1] = y;
		polygon.setVertices(newVertices);
		computeTriangles(newVertices);
		clearTotalSidesCache();

		if (x > newVertices[maxXIndex]) {
			maxXIndex = existingVertices.length;
		}
		if (y > newVertices[maxYIndex]) {
			maxYIndex = existingVertices.length + 1;
		}
	}

	/**
	 * Adds an additional point to this {@link Polygon}
	 * @param point The point to add as a {@link Vector2}
	 */
	public void addPoint(Vector2 point) {
		addPoint(point.x, point.y);
	}

	private void removePoint(int i) {
		float[] existingVertices = polygon.getVertices();
		float[] newVertices = new float[existingVertices.length - 2];
		if (i > 0) {
			System.arraycopy(existingVertices, 0, newVertices, 0, i);
		}
		if (i < existingVertices.length - 2) {
			System.arraycopy(existingVertices, i + 2, newVertices, i, existingVertices.length - i - 2);
		}
		polygon.setVertices(newVertices);
		computeTriangles(newVertices);
		clearTotalSidesCache();
		
		if (i == maxXIndex) {
			calculateMaxXY(newVertices);
			return;
		}
		if (i == maxYIndex) {
			calculateMaxXY(newVertices);
			return;
		}
		if (maxXIndex >= existingVertices.length) {
			calculateMaxXY(newVertices);
			return;
		}
		if (maxYIndex >= existingVertices.length) {
			calculateMaxXY(newVertices);
			return;
		}
	}

	/**
	 * Removes a point from this {@link Polygon}
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public void removePoint(float x, float y) {
		float[] existingVertices = polygon.getVertices();
		for (int i = 0; i < existingVertices.length; i += 2) {
			if (existingVertices[i] != x) {
				continue;
			}
			if (existingVertices[i + 1] != y) {
				continue;
			}
			removePoint(i);
			return;
		}
	}

	/**
	 * Removes a point from this {@link Polygon}
	 * @param point The point to remove as a {@link Vector2}
	 */
	public void removePoint(Vector2 point) {
		removePoint(point.x, point.y);
	}

	@Override
	public int getNumberOfSides() {
		if(totalSidesCache < 0) {
			totalSidesCache = polygon.getVertices().length / 2;
		}
		return totalSidesCache;
	}

	@Override
	public void draw(Graphics g) {
		g.drawPolygon(polygon.getVertices());
	}

	@Override
	public void fill(Graphics g) {
		g.fillPolygon(polygon.getVertices(), triangles.items);
	}
	
	public float [] getVertices() {
		return polygon.getVertices();
	}
	
	public void setVertices(float[] vertices) {
		polygon.setVertices(vertices);
		calculateMaxXY(vertices);
		computeTriangles(vertices);
		clearTotalSidesCache();
	}
	
	public void setPosition(float x, float y) {
		polygon.setPosition(x, y);
		calculateMaxXY(polygon.getVertices());
		computeTriangles(polygon.getVertices());
	}
	
	public float getRotation() {
		return polygon.getRotation();
	}
	
	public void setRotation(float degrees) {
		polygon.setRotation(degrees);
		calculateMaxXY(polygon.getVertices());
		computeTriangles(polygon.getVertices());
	}
	
	public void rotate(float degrees) {
		polygon.rotate(degrees);
		calculateMaxXY(polygon.getVertices());
		computeTriangles(polygon.getVertices());
	}

	/**
	 * Returns the x coordinate
	 * 
	 * @return The x coordinate of the first point in this {@link Polygon}
	 */
	@Override
	public float getX() {
		return polygon.getX();
	}

	/**
	 * Returns the y coordinate
	 * 
	 * @return The y coordinate of the first point in this {@link Polygon}
	 */
	@Override
	public float getY() {
		return polygon.getY();
	}
	
	/**
	 * Returns the x coordinate of the corner at the specified index
	 * 
	 * @param index The point index
	 * @return The x coordinate of the corner
	 */
	public float getX(int index) {
		return polygon.getVertices()[index * 2];
	}
	
	/**
	 * Returns the y coordinate of the corner at the specified index
	 * 
	 * @param index The point index
	 * @return The y coordinate of the corner
	 */
	public float getY(int index) {
		return polygon.getVertices()[(index * 2) + 1];
	}

	/**
	 * Returns max X coordinate of this {@link Polygon}
	 * 
	 * @return The right-most x coordinate
	 */
	public float getMaxX() {
		return polygon.getVertices()[maxXIndex];
	}

	/**
	 * Returns max Y coordinate of this {@link Polygon}
	 * 
	 * @return The bottom-most y coordinate
	 */
	public float getMaxY() {
		return polygon.getVertices()[maxYIndex];
	}

	/**
	 * Returns an array of vertex indices that the define the triangles which
	 * make up this {@link Polygon}
	 * 
	 * @return Array of triangle indices
	 */
	public ShortArray getTriangles() {
		return triangles;
	}

	private static float[] toVertices(Vector2 [] points) {
		if (points == null) {
			throw new MdxException(Point.class.getSimpleName() + " array cannot be null");
		}
		if (points.length < 3) {
			throw new MdxException(Point.class.getSimpleName() + " must have at least 3 points");
		}
		float[] result = new float[points.length * 2];
		for (int i = 0; i < points.length; i++) {
			int index = i * 2;
			result[index] = points[i].x;
			result[index + 1] = points[i].y;
		}
		return result;
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
	public void setX(float x) {
		polygon.setPosition(x, polygon.getY());
	}

	@Override
	public void setY(float y) {
		polygon.setPosition(polygon.getX(), y);
	}

	@Override
	public void set(float x, float y) {
		polygon.setPosition(x, y);
	}

	@Override
	public void translate(float translateX, float translateY) {
		polygon.translate(translateX, translateY);
	}

	@Override
	public EdgeIterator edgeIterator() {
		return edgeIterator;
	}
	
	private class PolygonEdgeIterator extends EdgeIterator {
		private int edge = 0;

		@Override
		protected void beginIteration() {
			edge = 0;
		}

		@Override
		protected void endIteration() {}

		@Override
		protected void nextEdge() {
			if(edge >=  getNumberOfSides()) {
				throw new MdxException("No more edges remaining. Make sure to call end()");
			}
			edge++;
		}

		@Override
		public boolean hasNext() {
			return edge < getNumberOfSides();
		}

		@Override
		public float getPointAX() {
			return polygon.getVertices()[edge * 2];
		}

		@Override
		public float getPointAY() {
			return polygon.getVertices()[(edge * 2) + 1];
		}

		@Override
		public float getPointBX() {
			if(edge == getNumberOfSides() - 1) {
				return polygon.getVertices()[0];
			}
			return polygon.getVertices()[(edge + 1) * 2];
		}

		@Override
		public float getPointBY() {
			if(edge == getNumberOfSides() - 1) {
				return polygon.getVertices()[1];
			}
			return polygon.getVertices()[((edge + 1) * 2) + 1];
		}
		
	}
}
