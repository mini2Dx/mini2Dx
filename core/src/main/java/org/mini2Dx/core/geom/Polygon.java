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

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

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
	private final PolygonEdgeIterator internalEdgeIterator = new PolygonEdgeIterator();
	private final Vector2 tmp1 = new Vector2();
	private final Vector2 tmp2 = new Vector2();

	final com.badlogic.gdx.math.Polygon polygon;

	private int totalSidesCache = -1;
	private float minX, minY, maxX, maxY;
	private ShortArray triangles;
	private float trackedRotation = 0f;
	private boolean isRectangle;

	/**
	 * Constructor
	 * 
	 * @param vertices
	 *            All points in x,y pairs. E.g. x1,y1,x2,y2,etc.
	 */
	public Polygon(float[] vertices) {
		polygon = new com.badlogic.gdx.math.Polygon(vertices);
		polygon.setOrigin(vertices[0], vertices[1]);
		triangulator = new EarClippingTriangulator();
		computeTriangles(polygon.getTransformedVertices());
		calculateMinMaxXY(polygon.getTransformedVertices());
		getNumberOfSides();
	}

	/**
	 * Constructor with vectors
	 * 
	 * @param points
	 *            All points in the polygon
	 */
	public Polygon(Vector2[] points) {
		this(toVertices(points));
	}

	private void clearTotalSidesCache() {
		totalSidesCache = -1;
	}

	private void computeTriangles(float[] vertices) {
		triangles = triangulator.computeTriangles(vertices);
	}

	private void calculateMinMaxXY(float[] vertices) {
		int minXIndex = 0;
		int minYIndex = 1;
		int maxXIndex = 0;
		int maxYIndex = 1;
		for (int i = 2; i < vertices.length; i += 2) {
			if (vertices[i] < vertices[minXIndex]) {
				minXIndex = i;
			}
			if (vertices[i + 1] < vertices[minYIndex]) {
				minYIndex = i + 1;
			}
			if (vertices[i] > vertices[maxXIndex]) {
				maxXIndex = i;
			}
			if (vertices[i + 1] > vertices[maxYIndex]) {
				maxYIndex = i + 1;
			}

		}
		this.minX = vertices[minXIndex];
		this.minY = vertices[minYIndex];
		this.maxX = vertices[maxXIndex];
		this.maxY = vertices[maxYIndex];
	}

	protected boolean triangleContains(float x, float y, float p1x, float p1y, float p2x, float p2y, float p3x,
			float p3y) {
		boolean b1, b2, b3;

		b1 = sign(x, y, p1x, p1y, p2x, p2y) < 0.0f;
		b2 = sign(x, y, p2x, p2y, p3x, p3y) < 0.0f;
		b3 = sign(x, y, p3x, p3y, p1x, p1y) < 0.0f;

		return ((b1 == b2) && (b2 == b3));
	}

	protected float sign(float x, float y, float p1x, float p1y, float p2x, float p2y) {
		return (x - p2x) * (p1y - p2y) - (p1x - p2x) * (y - p2y);
	}

	@Override
	public boolean contains(float x, float y) {
		if (isRectangle) {
			return triangleContains(x, y, polygon.getTransformedVertices()[0], polygon.getTransformedVertices()[1],
					polygon.getTransformedVertices()[2], polygon.getTransformedVertices()[3],
					polygon.getTransformedVertices()[6], polygon.getTransformedVertices()[7])
					|| triangleContains(x, y, polygon.getTransformedVertices()[6], polygon.getTransformedVertices()[7],
							polygon.getTransformedVertices()[2], polygon.getTransformedVertices()[3],
							polygon.getTransformedVertices()[4], polygon.getTransformedVertices()[5]);
		}
		return polygon.contains(x, y);
	}

	@Override
	public boolean contains(Vector2 vector2) {
		return contains(vector2.x, vector2.y);
	}

	public boolean contains(Polygon polygon) {
		return org.mini2Dx.core.geom.Intersector.containsPolygon(this, polygon);
	}

	/**
	 * Returns if this {@link Polygon} intersects another
	 * 
	 * @param polygon
	 *            The other {@link Polygon}
	 * @return True if the two {@link Polygon}s intersect
	 */
	public boolean intersects(Polygon polygon) {
		if (isRectangle && polygon.isRectangle) {
			boolean xAxisOverlaps = true;
			boolean yAxisOverlaps = true;

			if (maxX < polygon.minX)
				xAxisOverlaps = false;
			if (polygon.maxX < minX)
				xAxisOverlaps = false;
			if (maxY < polygon.minY)
				yAxisOverlaps = false;
			if (polygon.maxY < minY)
				yAxisOverlaps = false;

			return xAxisOverlaps && yAxisOverlaps;
		}
		
		if (polygon.minX > maxX) {
			return false;
		}
		if (polygon.maxX < minX) {
			return false;
		}
		if (polygon.minY > maxY) {
			return false;
		}
		if (polygon.maxY < minY) {
			return false;
		}
		boolean result = false;

		internalEdgeIterator.begin();
		while (internalEdgeIterator.hasNext()) {
			internalEdgeIterator.next();
			if (polygon.intersects(internalEdgeIterator.getEdgeLineSegment())) {
				result = true;
				break;
			}
		}
		internalEdgeIterator.end();
		return result;
	}

	/**
	 * Returns if this {@link Polygon} intersects a {@link Triangle}
	 * 
	 * @param triangle
	 *            The {@link Triangle} to check
	 * @return True if this {@link Polygon} and {@link Triangle} intersect
	 */
	public boolean intersects(Triangle triangle) {
		return intersects(triangle.polygon);
	}

	/**
	 * Returns if the specified {@link Rectangle} intersects this
	 * {@link Polygon}
	 * 
	 * @param rectangle
	 *            The {@link Rectangle} to check
	 * @return True if this {@link Polygon} and {@link Rectangle} intersect
	 */
	public boolean intersects(Rectangle rectangle) {
		return intersects(rectangle.polygon);
	}

	public boolean intersects(Circle circle) {
		if(isRectangle) {
			float closestX = circle.getX();
			float closestY = circle.getY();

			if (circle.getX() < minX) {
				closestX = minX;
			} else if (circle.getX() > maxX) {
				closestX = maxX;
			}

			if (circle.getY() < minY) {
				closestY = minY;
			} else if (circle.getY() > maxY) {
				closestY = maxY;
			}

			closestX = closestX - circle.getX();
			closestX *= closestX;
			closestY = closestY - circle.getY();
			closestY *= closestY;

			return closestX + closestY < circle.getRadius() * circle.getRadius();
		}
		
		boolean result = false;

		internalEdgeIterator.begin();
		while (internalEdgeIterator.hasNext()) {
			internalEdgeIterator.next();
			if (circle.intersectsLineSegment(internalEdgeIterator.getPointAX(), internalEdgeIterator.getPointAY(),
					internalEdgeIterator.getPointBX(), internalEdgeIterator.getPointBY())) {
				result = true;
				break;
			}
		}
		internalEdgeIterator.end();
		return result;
	}

	@Override
	public boolean intersects(LineSegment lineSegment) {
		return intersectsLineSegment(lineSegment.getPointA(), lineSegment.getPointB());
	}

	@Override
	public boolean intersectsLineSegment(Vector2 pointA, Vector2 pointB) {
		return Intersector.intersectSegmentPolygon(pointA, pointB, polygon);
	}

	@Override
	public boolean intersectsLineSegment(float x1, float y1, float x2, float y2) {
		tmp1.set(x1, y1);
		tmp2.set(x2, y2);
		return Intersector.intersectSegmentPolygon(tmp1, tmp2, polygon);
	}

	@Override
	public float getDistanceTo(float x, float y) {
		float[] vertices = polygon.getTransformedVertices();
		float result = Intersector.distanceSegmentPoint(vertices[vertices.length - 2], vertices[vertices.length - 1],
				vertices[0], vertices[1], x, y);
		for (int i = 0; i < vertices.length - 2; i += 2) {
			float distance = Intersector.distanceSegmentPoint(vertices[i], vertices[i + 1], vertices[i + 2],
					vertices[i + 3], x, y);
			if (distance < result) {
				result = distance;
			}
		}
		return result;
	}

	/**
	 * Adds an additional point to this {@link Polygon}
	 * 
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 */
	public void addPoint(float x, float y) {
		float[] existingVertices = polygon.getTransformedVertices();
		float[] newVertices = new float[existingVertices.length + 2];

		if (existingVertices.length > 0) {
			System.arraycopy(existingVertices, 0, newVertices, 0, existingVertices.length);
		}
		newVertices[existingVertices.length] = x;
		newVertices[existingVertices.length + 1] = y;

		polygon.translate(-polygon.getX(), -polygon.getY());
		polygon.setVertices(newVertices);

		computeTriangles(newVertices);
		clearTotalSidesCache();

		if (x > maxX) {
			maxX = x;
		} else if (x < minX) {
			minX = x;
		}
		if (y > maxY) {
			maxY = y;
		} else if (y < minY) {
			minY = y;
		}
	}

	/**
	 * Adds an additional point to this {@link Polygon}
	 * 
	 * @param point
	 *            The point to add as a {@link Vector2}
	 */
	public void addPoint(Vector2 point) {
		addPoint(point.x, point.y);
	}

	private void removePoint(int i) {
		float[] existingVertices = polygon.getTransformedVertices();
		float[] newVertices = new float[existingVertices.length - 2];
		if (i > 0) {
			System.arraycopy(existingVertices, 0, newVertices, 0, i);
		}
		if (i < existingVertices.length - 2) {
			System.arraycopy(existingVertices, i + 2, newVertices, i, existingVertices.length - i - 2);
		}
		polygon.translate(-polygon.getX(), -polygon.getY());
		polygon.setVertices(newVertices);
		computeTriangles(newVertices);
		calculateMinMaxXY(newVertices);
		clearTotalSidesCache();
	}

	/**
	 * Removes a point from this {@link Polygon}
	 * 
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 */
	public void removePoint(float x, float y) {
		float[] existingVertices = polygon.getTransformedVertices();
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
	 * 
	 * @param point
	 *            The point to remove as a {@link Vector2}
	 */
	public void removePoint(Vector2 point) {
		removePoint(point.x, point.y);
	}

	@Override
	public int getNumberOfSides() {
		if (totalSidesCache < 0) {
			totalSidesCache = polygon.getTransformedVertices().length / 2;
			isRectangle = totalSidesCache == 4;
		}
		return totalSidesCache;
	}

	@Override
	public void draw(Graphics g) {
		g.drawPolygon(polygon.getTransformedVertices());
	}

	@Override
	public void fill(Graphics g) {
		g.fillPolygon(polygon.getTransformedVertices(), triangles.items);
	}

	public float[] getVertices() {
		return polygon.getTransformedVertices();
	}

	public void setVertices(float[] vertices) {
		polygon.setVertices(vertices);
		calculateMinMaxXY(vertices);
		computeTriangles(vertices);
		clearTotalSidesCache();
	}

	public void setVertices(Vector2[] vertices) {
		setVertices(toVertices(vertices));
	}

	@Override
	public float getRotation() {
		return trackedRotation;
	}

	@Override
	public void setRotation(float degrees) {
		setRotationAround(polygon.getTransformedVertices()[0], polygon.getTransformedVertices()[1], degrees);
	}

	@Override
	public void rotate(float degrees) {
		rotateAround(polygon.getTransformedVertices()[0], polygon.getTransformedVertices()[1], degrees);
	}

	@Override
	public void setRotationAround(float centerX, float centerY, float degrees) {
		polygon.setVertices(polygon.getTransformedVertices());
		polygon.setOrigin(centerX, centerY);
		polygon.setRotation(degrees - trackedRotation);
		trackedRotation = degrees;

		calculateMinMaxXY(polygon.getTransformedVertices());
		computeTriangles(polygon.getTransformedVertices());
	}

	@Override
	public void rotateAround(float centerX, float centerY, float degrees) {
		trackedRotation += degrees;
		float[] vertices = polygon.getTransformedVertices();
		polygon.setRotation(0);
		polygon.setOrigin(centerX, centerY);
		polygon.setVertices(vertices);
		polygon.rotate(degrees);

		calculateMinMaxXY(polygon.getTransformedVertices());
		computeTriangles(polygon.getTransformedVertices());
	}

	/**
	 * Returns the x coordinate
	 * 
	 * @return The x coordinate of the first point in this {@link Polygon}
	 */
	@Override
	public float getX() {
		return getX(0);
	}

	/**
	 * Returns the y coordinate
	 * 
	 * @return The y coordinate of the first point in this {@link Polygon}
	 */
	@Override
	public float getY() {
		return getY(0);
	}

	/**
	 * Returns the x coordinate of the corner at the specified index
	 * 
	 * @param index
	 *            The point index
	 * @return The x coordinate of the corner
	 */
	public float getX(int index) {
		return polygon.getTransformedVertices()[index * 2];
	}

	/**
	 * Returns the y coordinate of the corner at the specified index
	 * 
	 * @param index
	 *            The point index
	 * @return The y coordinate of the corner
	 */
	public float getY(int index) {
		return polygon.getTransformedVertices()[(index * 2) + 1];
	}

	/**
	 * Returns min X coordinate of this {@link Polygon}
	 * 
	 * @return The left-most x coordinate
	 */
	public float getMinX() {
		return minX;
	}

	/**
	 * Returns min Y coordinate of this {@link Polygon}
	 * 
	 * @return The up-most y coordinate
	 */
	public float getMinY() {
		return minY;
	}

	/**
	 * Returns max X coordinate of this {@link Polygon}
	 * 
	 * @return The right-most x coordinate
	 */
	public float getMaxX() {
		return maxX;
	}

	/**
	 * Returns max Y coordinate of this {@link Polygon}
	 * 
	 * @return The bottom-most y coordinate
	 */
	public float getMaxY() {
		return maxY;
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

	private static float[] toVertices(Vector2[] points) {
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
	public void setX(float x) {
		float[] vertices = polygon.getTransformedVertices();
		float xDiff = x - getX();

		for (int i = 0; i < vertices.length; i += 2) {
			vertices[i] += xDiff;
		}
		polygon.setOrigin(x, getY());
		polygon.setVertices(vertices);

		calculateMinMaxXY(vertices);
		computeTriangles(vertices);
	}

	@Override
	public void setY(float y) {
		float[] vertices = polygon.getTransformedVertices();
		float yDiff = y - getY();

		for (int i = 1; i < vertices.length; i += 2) {
			vertices[i] += yDiff;
		}
		polygon.setOrigin(getX(), y);
		polygon.setVertices(vertices);

		calculateMinMaxXY(vertices);
		computeTriangles(vertices);
	}

	@Override
	public void set(float x, float y) {
		float[] vertices = polygon.getTransformedVertices();
		float xDiff = x - getX();
		float yDiff = y - getY();

		for (int i = 0; i < vertices.length; i += 2) {
			vertices[i] += xDiff;
			vertices[i + 1] += yDiff;
		}
		polygon.setOrigin(x, y);
		polygon.setVertices(vertices);

		calculateMinMaxXY(vertices);
		computeTriangles(vertices);
	}

	@Override
	public void translate(float translateX, float translateY) {
		float[] vertices = polygon.getTransformedVertices();

		for (int i = 0; i < vertices.length; i += 2) {
			vertices[i] += translateX;
			vertices[i + 1] += translateY;
		}
		polygon.setOrigin(vertices[0], vertices[1]);
		polygon.setVertices(vertices);

		calculateMinMaxXY(vertices);
		computeTriangles(vertices);
	}

	@Override
	public EdgeIterator edgeIterator() {
		return edgeIterator;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < polygon.getTransformedVertices().length; i += 2) {
			result.append("[");
			result.append(polygon.getTransformedVertices()[i]);
			result.append(",");
			result.append(polygon.getTransformedVertices()[i + 1]);
			result.append("]");
		}
		return result.toString();
	}

	private class PolygonEdgeIterator extends EdgeIterator {
		private int edge = 0;
		private LineSegment edgeLineSegment = new LineSegment(0f, 0f, 1f, 1f);

		@Override
		protected void beginIteration() {
			edge = -1;
		}

		@Override
		protected void endIteration() {
		}

		@Override
		protected void nextEdge() {
			if (edge >= getNumberOfSides()) {
				throw new MdxException("No more edges remaining. Make sure to call end()");
			}
			edge++;
			if (!hasNext()) {
				return;
			}
			edgeLineSegment.set(getPointAX(), getPointAY(), getPointBX(), getPointBY());
		}

		@Override
		public boolean hasNext() {
			return edge < getNumberOfSides() - 1;
		}

		@Override
		public float getPointAX() {
			if (edge < 0) {
				throw new MdxException("Make sure to call next() after beginning iteration");
			}
			return polygon.getTransformedVertices()[edge * 2];
		}

		@Override
		public float getPointAY() {
			if (edge < 0) {
				throw new MdxException("Make sure to call next() after beginning iteration");
			}
			return polygon.getTransformedVertices()[(edge * 2) + 1];
		}

		@Override
		public float getPointBX() {
			if (edge < 0) {
				throw new MdxException("Make sure to call next() after beginning iteration");
			}
			if (edge == getNumberOfSides() - 1) {
				return polygon.getTransformedVertices()[0];
			}
			return polygon.getTransformedVertices()[(edge + 1) * 2];
		}

		@Override
		public float getPointBY() {
			if (edge < 0) {
				throw new MdxException("Make sure to call next() after beginning iteration");
			}
			if (edge == getNumberOfSides() - 1) {
				return polygon.getTransformedVertices()[1];
			}
			return polygon.getTransformedVertices()[((edge + 1) * 2) + 1];
		}

		@Override
		public LineSegment getEdgeLineSegment() {
			return edgeLineSegment;
		}

	}
}
