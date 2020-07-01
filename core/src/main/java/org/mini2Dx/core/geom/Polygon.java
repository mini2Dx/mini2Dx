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
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.util.Lerper;
import org.mini2Dx.gdx.math.EarClippingTriangulator;
import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.math.Vector2;
import org.mini2Dx.gdx.utils.ShortArray;

import java.util.Arrays;

/**
 * Implements a rotatable polygon. Adds extra functionality to the default
 * polygon implementation in LibGDX
 */
public class Polygon extends Shape {
	private static final String LOGGING_TAG = Polygon.class.getSimpleName();
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

	private static final ThreadLocal<EarClippingTriangulator> TRIANGULATOR = new ThreadLocal<EarClippingTriangulator>(){
		@Override
		protected EarClippingTriangulator initialValue() {
			return new EarClippingTriangulator();
		}
	};

	private final PolygonEdgeIterator edgeIterator = new PolygonEdgeIterator();
	private final PolygonEdgeIterator internalEdgeIterator = new PolygonEdgeIterator(new LineSegment(0f, 0f, 1f, 1f));

	private final Vector2 centroid = new Vector2();
	private float[] vertices;
	private float rotation = 0f;
	private int totalSidesCache = -1;
	private float minX, minY, maxX, maxY;
	private ShortArray triangles;
	private boolean isRectangle, isEquilateral;
	private boolean minMaxDirty = true;
	private boolean trianglesDirty = true;
	private boolean centroidDirty = true;

	/**
	 * Constructs a {@link Polygon} belonging to the {@link Geometry} pool
	 * @param geometry The {@link Geometry} pool
	 * @param vertices The vertices
	 */
	public Polygon(Geometry geometry, float[] vertices) {
		super(geometry);
		this.vertices = vertices;

		getNumberOfSides();
	}

	/**
	 * Constructor. Note that vertices must be in a clockwise order for
	 * performance and accuracy.
	 * 
	 * @param vertices
	 *            All points in x,y pairs. E.g. x1,y1,x2,y2,etc.
	 */
	public Polygon(float[] vertices) {
		super();
		this.vertices = vertices;

		getNumberOfSides();
	}

	/**
	 * Constructor with vectors. Note that vectors must be in a clockwise order
	 * for performance and accuracy.
	 * 
	 * @param points
	 *            All points in the polygon
	 */
	public Polygon(Vector2[] points) {
		this(toVertices(points));
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
			vertices = null;
			triangles = null;
			return;
		}
		geometry.release(this);
	}

	/**
	 * Returns if this {@link Polygon} is the same as another
	 * 
	 * @param polygon
	 *            The {@link Polygon} to compare to
	 * @return True if the vertices match
	 */
	public boolean isSameAs(Polygon polygon) {
		for (int i = 0; i < vertices.length; i++) {
			if (vertices[i] != polygon.vertices[i]) {
				return false;
			}
		}
		return true;
	}

	public Polygon lerp(Polygon target, float alpha) {
		lerp(this, target, alpha);
		return this;
	}

	public void lerp(Polygon result, Polygon target, float alpha) {
		lerp(result, this, target, alpha);
	}

	public static void lerp(Polygon result, Polygon from, Polygon target, float alpha) {
		float[] currentVertices = from.vertices;
		float[] targetVertices = target.vertices;
		float[] resultVertices = result.vertices;

		if (currentVertices.length != targetVertices.length) {
			throw new MdxException("Cannot lerp polygons with different vertice amounts");
		}
		if (from.getRotation() != target.getRotation()) {
			float newRotation = Lerper.lerp(from.rotation, target.getRotation(), alpha);
			if (from.getX() == target.getX() && from.getY() == target.getY()) {
				// Same origin, only rotational change
				result.setRotation(newRotation);
			} else {
				result.setRotationAround(Lerper.lerp(currentVertices[0], targetVertices[0], alpha),
						Lerper.lerp(currentVertices[1], targetVertices[1], alpha), newRotation);
			}
		} else if (!from.isSameAs(target)) {
			for (int i = 0; i < currentVertices.length; i += 2) {
				resultVertices[i] = Lerper.lerp(currentVertices[i], targetVertices[i], alpha);
				resultVertices[i + 1] =  Lerper.lerp(currentVertices[i + 1], targetVertices[i + 1], alpha);
			}
			result.vertices = resultVertices;
			result.setDirty();
			result.notifyPositionChangeListeners();
		}
	}

	@Override
	public Shape copy() {
		Polygon result = new Polygon(Arrays.copyOf(vertices, vertices.length));
		result.rotation = rotation;
		return result;
	}

	private void clearTotalSidesCache() {
		totalSidesCache = -1;
	}

	protected boolean triangleContains(float x, float y, float p1x, float p1y, float p2x, float p2y, float p3x,
			float p3y) {
		boolean b1, b2, b3;
		if (MathUtils.isEqual(p1x, p2x) && MathUtils.isEqual(p2x, p3x) && MathUtils.isEqual(p1y, p2y) && MathUtils.isEqual(p2y, p3y)){
		    return MathUtils.isEqual(x, p1x) && MathUtils.isEqual(y, p1y);
        }
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
			return triangleContains(x, y, vertices[0], vertices[1], vertices[2], vertices[3], vertices[6], vertices[7])
					|| triangleContains(x, y, vertices[6], vertices[7], vertices[2], vertices[3], vertices[4],
							vertices[5]);
		}

		int intersects = 0;

		for (int i = 0; i < vertices.length; i += 2) {
			float x1 = vertices[i];
			float y1 = vertices[i + 1];
			float x2 = vertices[(i + 2) % vertices.length];
			float y2 = vertices[(i + 3) % vertices.length];
			if (((y1 <= y && y < y2) || (y2 <= y && y < y1)) && x < ((x2 - x1) / (y2 - y1) * (y - y1) + x1))
				intersects++;
		}
		return (intersects & 1) == 1;
	}

	@Override
	public boolean contains(Vector2 vector2) {
		return contains(vector2.x, vector2.y);
	}

	@Override
	public boolean contains(Sizeable shape) {
		if (shape.isCircle()) {
			Rectangle circleBox = ((Circle) shape).getBoundingBox();
			return contains(circleBox.getPolygon());
		}
		return contains(shape.getPolygon());
	}

	public boolean contains(Polygon polygon) {
		return Intersector.containsPolygon(this, polygon);
	}

	@Override
	public boolean intersects(Sizeable shape) {
		if (shape.isCircle()) {
			return intersects((Circle) shape);
		}
		return intersects(shape.getPolygon());
	}

	/**
	 * Returns if this {@link Polygon} intersects another
	 * 
	 * @param polygon
	 *            The other {@link Polygon}
	 * @return True if the two {@link Polygon}s intersect
	 */
	public boolean intersects(Polygon polygon) {
		minMaxDirtyCheck();
		polygon.minMaxDirtyCheck();
		if (isRectangle && polygon.isRectangle &&
				MathUtils.round(rotation % 90f) == 0f &&
				MathUtils.round(polygon.rotation % 90f) == 0f) {
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
		if (isRectangle) {
			minMaxDirtyCheck();
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
		return Intersector.intersectSegmentPolygon(pointA, pointB, vertices);
	}

	@Override
	public boolean intersectsLineSegment(float x1, float y1, float x2, float y2) {
		final Vector2 tmpVector1 = TMP_VECTOR1.get();
		final Vector2 tmpVector2 = TMP_VECTOR2.get();
		tmpVector1.set(x1, y1);
		tmpVector2.set(x2, y2);
		return Intersector.intersectSegmentPolygon(tmpVector1, tmpVector2, vertices);
	}

	@Override
	public float getWidth() {
		minMaxDirtyCheck();
		return maxX - minX;
	}

	@Override
	public float getHeight() {
		minMaxDirtyCheck();
		return maxY - minY;
	}

	@Override
	public float getDistanceTo(float x, float y) {
		float result = org.mini2Dx.gdx.math.Intersector.distanceSegmentPoint(vertices[vertices.length - 2],
				vertices[vertices.length - 1], vertices[0], vertices[1], x, y);
		for (int i = 0; i < vertices.length - 2; i += 2) {
			float distance = org.mini2Dx.gdx.math.Intersector.distanceSegmentPoint(vertices[i], vertices[i + 1],
					vertices[i + 2], vertices[i + 3], x, y);
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
		float[] existingVertices = vertices;
		float[] newVertices = new float[existingVertices.length + 2];

		if (existingVertices.length > 0) {
			System.arraycopy(existingVertices, 0, newVertices, 0, existingVertices.length);
		}
		newVertices[existingVertices.length] = x;
		newVertices[existingVertices.length + 1] = y;

		this.vertices = newVertices;

		clearTotalSidesCache();
		setDirty();
		notifyPositionChangeListeners();
		notifySizeChangeListeners();
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
		float[] existingVertices = vertices;
		float[] newVertices = new float[existingVertices.length - 2];
		if (i > 0) {
			System.arraycopy(existingVertices, 0, newVertices, 0, i);
		}
		if (i < existingVertices.length - 2) {
			System.arraycopy(existingVertices, i + 2, newVertices, i, existingVertices.length - i - 2);
		}
		this.vertices = newVertices;

		setDirty();
		clearTotalSidesCache();
		notifyPositionChangeListeners();
		notifySizeChangeListeners();
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
		float[] existingVertices = vertices;
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
	
	private void checkSidesCache() {
		if(totalSidesCache >= 0) {
			return;
		}
		totalSidesCache = vertices.length / 2;
		isRectangle = totalSidesCache == 4;
		isEquilateral = isEquilateral(MathUtils.FLOAT_ROUNDING_ERROR);
	}

	@Override
	public int getNumberOfSides() {
		checkSidesCache();
		return totalSidesCache;
	}

	@Override
	public void draw(Graphics g) {
		g.drawPolygon(vertices);
	}

	@Override
	public void fill(Graphics g) {
		g.fillPolygon(vertices, getTriangles().items);
	}

	public float[] getVertices() {
		return vertices;
	}

	public void setVertices(float[] vertices) {
		boolean changed = false;
		if(this.vertices.length == vertices.length) {
			for(int i = 0; i < vertices.length; i++) {
				changed |= !MathUtils.isEqual(this.vertices[i], vertices[i]);
			}
		} else {
			changed = true;
		}
		
		rotation = 0f;
		
		if(!changed) {
			return;
		}
		final float previousX = getX();
		final float previousY = getY();
		final float previousWidth = getWidth();
		final float previousHeight = getHeight();

		this.vertices = vertices;

		clearTotalSidesCache();
		setDirty();

		if(!MathUtils.isEqual(previousX, getX()) || !MathUtils.isEqual(previousY, getY())) {
			notifyPositionChangeListeners();
		}
		if(!MathUtils.isEqual(previousWidth, getWidth()) || !MathUtils.isEqual(previousHeight, getHeight())) {
			notifySizeChangeListeners();
		}
	}

	public void setVertices(Vector2[] vertices) {
		if(this.vertices.length != vertices.length * 2) {
			setVertices(toVertices(vertices));
			return;
		}

		final float previousX = getX();
		final float previousY = getY();
		final float previousWidth = getWidth();
		final float previousHeight = getHeight();

		boolean changed = false;
		for(int i = 0; i < vertices.length; i++) {
			int index = i * 2;
			changed |= !MathUtils.isEqual(this.vertices[index], vertices[i].x);
			changed |= !MathUtils.isEqual(this.vertices[index + 1], vertices[i].y);
			this.vertices[index] = vertices[i].x;
			this.vertices[index + 1] = vertices[i].y;
		}
		rotation = 0f;
		
		if(!changed) {
			return;
		}
		clearTotalSidesCache();
		setDirty();

		if(!MathUtils.isEqual(previousX, getX()) || !MathUtils.isEqual(previousY, getY())) {
			notifyPositionChangeListeners();
		}
		if(!MathUtils.isEqual(previousWidth, getWidth()) || !MathUtils.isEqual(previousHeight, getHeight())) {
			notifySizeChangeListeners();
		}
	}

	@Override
	public float getRotation() {
		return rotation;
	}

	@Override
	public void setRotation(float degrees) {
		setRotationAround(vertices[0], vertices[1], degrees);
	}

	@Override
	public void rotate(float degrees) {
		rotateAround(vertices[0], vertices[1], degrees);
	}

	@Override
	public void setRotationAround(float centerX, float centerY, float degrees) {
		if (rotation == degrees && centerX == getX() && centerY == getY()) {
			return;
		}
		rotateAround(centerX, centerY, degrees - rotation);
	}

	@Override
	public void rotateAround(float centerX, float centerY, float degrees) {
		if (degrees == 0f) {
			return;
		}
		rotation += degrees;

		final float cos = MathUtils.cos(degrees * MathUtils.degreesToRadians);
		final float sin = MathUtils.sin(degrees * MathUtils.degreesToRadians);

		for (int i = 0; i < vertices.length; i += 2) {
			float x = vertices[i];
			float y = vertices[i + 1];

			vertices[i] = (cos * (x - centerX) - sin * (y - centerY) + centerX);
			vertices[i + 1] = (sin * (x - centerX) + cos * (y - centerY) + centerY);
		}
		setDirty();
		notifyPositionChangeListeners();
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
		return vertices[index * 2];
	}

	/**
	 * Returns the y coordinate of the corner at the specified index
	 * 
	 * @param index
	 *            The point index
	 * @return The y coordinate of the corner
	 */
	public float getY(int index) {
		return vertices[(index * 2) + 1];
	}
	
	@Override
	public float getCenterX() {
		if(centroidDirty) {
			setCentroid();
		}
		return centroid.x;
	}

	@Override
	public float getCenterY() {
		if(centroidDirty) {
			setCentroid();
		}
		return centroid.y;
	}

	private void setCentroid() {
		switch(getNumberOfSides()) {
		case 3:
			centroid.x = (vertices[0] + vertices[2] + vertices[4]) / 3f;
			centroid.y = (vertices[1] + vertices[3] + vertices[5]) / 3f;
			break;
		case 4:
			if(MathUtils.isZero(rotation)) {
				centroid.x = getX() + (getWidth() * 0.5f);
				centroid.y = getY() + (getHeight() * 0.5f);
			} else {
				float avgX1 = (vertices[0] + vertices[2] + vertices[4]) / 3f;
				float avgY1 = (vertices[1] + vertices[3] + vertices[5]) / 3f;
				float avgX2 = (vertices[0] + vertices[6] + vertices[4]) / 3f;
				float avgY2 = (vertices[1] + vertices[7] + vertices[5]) / 3f;
				centroid.x = avgX1 - (avgX1 - avgX2) * 0.5f;
				centroid.y = avgY1 - (avgY1 - avgY2) * 0.5f;
			}
			break;
		default:
			float area = 0, x = 0, y = 0;
			int last = vertices.length - 2;
			float x1 = vertices[last], y1 = vertices[last + 1];
			for (int i = 0; i <= last; i += 2) {
				float x2 = vertices[i], y2 = vertices[i + 1];
				float a = x1 * y2 - x2 * y1;
				area += a;
				x += (x1 + x2) * a;
				y += (y1 + y2) * a;
				x1 = x2;
				y1 = y2;
			}
			if (area == 0) {
				centroid.x = 0f;
				centroid.y = 0f;
			} else {
				area *= 0.5f;
				centroid.x = x / (6f * area);
				centroid.y = y / (6f * area);
			}
			break;
		}
		centroidDirty = false;
	}
	
	@Override
	public void setCenter(float x, float y) {
		float centerX = getCenterX();
		float centerY = getCenterY();
		
		if(x == centerX && y == centerY) {
			return;
		}
		translate(x - centerX, y - centerY);
	}

	@Override
	public void setCenterX(float x) {
		float centerX = getCenterX();
		if(x == centerX) {
			return;
		}
		translate(x - centerX, 0f);
	}

	@Override
	public void setCenterY(float y) {
		float centerY = getCenterY();
		if(y == centerY) {
			return;
		}
		translate(0f, y - centerY);
	}

	/**
	 * Returns min X coordinate of this {@link Polygon}
	 * 
	 * @return The left-most x coordinate
	 */
	public float getMinX() {
		minMaxDirtyCheck();
		return minX;
	}

	/**
	 * Returns min Y coordinate of this {@link Polygon}
	 * 
	 * @return The up-most y coordinate
	 */
	public float getMinY() {
		minMaxDirtyCheck();
		return minY;
	}

	/**
	 * Returns max X coordinate of this {@link Polygon}
	 * 
	 * @return The right-most x coordinate
	 */
	public float getMaxX() {
		minMaxDirtyCheck();
		return maxX;
	}

	/**
	 * Returns max Y coordinate of this {@link Polygon}
	 * 
	 * @return The bottom-most y coordinate
	 */
	public float getMaxY() {
		minMaxDirtyCheck();
		return maxY;
	}

	/**
	 * Returns an array of vertex indices that the define the triangles which
	 * make up this {@link Polygon}
	 * 
	 * @return Array of triangle indices
	 */
	public ShortArray getTriangles() {
		trianglesDirtyCheck();
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
		if (MathUtils.isEqual(x, getX())) {
			return;
		}

		float xDiff = x - getX();

		for (int i = 0; i < vertices.length; i += 2) {
			vertices[i] += xDiff;
		}
		setDirty();
		notifyPositionChangeListeners();
	}

	@Override
	public void setY(float y) {
		if (MathUtils.isEqual(y, getY())) {
			return;
		}

		float yDiff = y - getY();

		for (int i = 1; i < vertices.length; i += 2) {
			vertices[i] += yDiff;
		}
		setDirty();
		notifyPositionChangeListeners();
	}

	@Override
	public void setXY(float x, float y) {
		if (MathUtils.isEqual(x,getX()) && MathUtils.isEqual(y, getY())) {
			return;
		}

		float xDiff = x - getX();
		float yDiff = y - getY();

		for (int i = 0; i < vertices.length; i += 2) {
			vertices[i] += xDiff;
			vertices[i + 1] += yDiff;
		}
		setDirty();
		notifyPositionChangeListeners();
	}
	
	@Override
	public void setRadius(float radius) {
		final Vector2 tmpVector1 = TMP_VECTOR1.get();
		tmpVector1.set(vertices[0], vertices[1]);
		scale(radius / tmpVector1.dst(getCenterX(), getCenterY()));
	}
	
	@Override
	public void scale(float scale) {
		if(!isEquilateral()) {
			Mdx.log.error(LOGGING_TAG, "Cannot set radius on non-equilateral Polygon");
			return;
		}
		
		for(int i = 0; i < vertices.length; i += 2) {
			final Vector2 tmpVector1 = TMP_VECTOR1.get();
			tmpVector1.set(vertices[i], vertices[i + 1]);
			tmpVector1.sub(getCenterX(), getCenterY());
			tmpVector1.scl(scale);
			tmpVector1.add(vertices[i], vertices[i + 1]);
			vertices[i] = tmpVector1.x;
			vertices[i + 1] = tmpVector1.y;
		}
		
		setDirty();
		notifySizeChangeListeners();
	}

	public void set(Polygon polygon) {
		this.vertices = polygon.vertices;
		this.rotation = polygon.rotation;
		clearTotalSidesCache();
		setDirty();
		notifyPositionChangeListeners();
		notifySizeChangeListeners();
	}

	@Override
	public void translate(float translateX, float translateY) {
		if(MathUtils.isZero(translateX) && MathUtils.isZero(translateY)) {
			return;
		}
		for (int i = 0; i < vertices.length; i += 2) {
			vertices[i] += translateX;
			vertices[i + 1] += translateY;
		}
		setDirty();
		notifyPositionChangeListeners();
	}

	@Override
	public EdgeIterator edgeIterator() {
		return edgeIterator;
	}
	
	public boolean isEquilateral() {
		checkSidesCache();
		return isEquilateral;
	}
	
	public boolean isEquilateral(float tolerance) {
		if(isRectangle()) {
			return MathUtils.isEqual(getMaxX() - getX(), getMaxY() - getY(), tolerance);
		}
		
		PolygonEdgeIterator edgeIterator = this.new PolygonEdgeIterator();
		edgeIterator.begin();
		edgeIterator.next();
		float length = edgeIterator.getEdgeLineSegment().getLength();
		while(edgeIterator.hasNext()) {
			edgeIterator.next();
			float nextLength = edgeIterator.getEdgeLineSegment().getLength();
			if(!MathUtils.isEqual(length, nextLength, tolerance)) {
				edgeIterator.end();
				return false;
			}
		}
		edgeIterator.end();
		return true;
	}

	public boolean isRectangle() {
		checkSidesCache();
		return isRectangle;
	}

	@Override
	public boolean isCircle() {
		return false;
	}

	@Override
	public Polygon getPolygon() {
		return this;
	}

	boolean isDirty() {
		return minMaxDirty || trianglesDirty || centroidDirty;
	}

	private void setDirty() {
		minMaxDirty = true;
		trianglesDirty = true;
		centroidDirty = true;
	}

	private void minMaxDirtyCheck() {
		if (!minMaxDirty) {
			return;
		}
		calculateMinMaxXY(vertices);
		minMaxDirty = false;
	}

	private void trianglesDirtyCheck() {
		if (!trianglesDirty) {
			return;
		}
		computeTriangles(vertices);
		trianglesDirty = false;
	}

	private void computeTriangles(float[] vertices) {
		triangles = TRIANGULATOR.get().computeTriangles(vertices);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(vertices);
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
		Polygon other = (Polygon) obj;
		if (!Arrays.equals(vertices, other.vertices))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < vertices.length; i += 2) {
			result.append("[");
			result.append(vertices[i]);
			result.append(",");
			result.append(vertices[i + 1]);
			result.append("]");
		}
		return result.toString();
	}

	private class PolygonEdgeIterator extends EdgeIterator {
		private int edge = 0;
		private final LineSegment edgeLineSegment;

		public PolygonEdgeIterator() {
			this(new LineSegment(0f, 0f, 1f, 1f));
		}

		public PolygonEdgeIterator(LineSegment edgeLineSegment) {
			this.edgeLineSegment = edgeLineSegment;
		}

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
			return vertices[edge * 2];
		}

		@Override
		public float getPointAY() {
			if (edge < 0) {
				throw new MdxException("Make sure to call next() after beginning iteration");
			}
			return vertices[(edge * 2) + 1];
		}

		@Override
		public float getPointBX() {
			if (edge < 0) {
				throw new MdxException("Make sure to call next() after beginning iteration");
			}
			if (edge == getNumberOfSides() - 1) {
				return vertices[0];
			}
			return vertices[(edge + 1) * 2];
		}

		@Override
		public float getPointBY() {
			if (edge < 0) {
				throw new MdxException("Make sure to call next() after beginning iteration");
			}
			if (edge == getNumberOfSides() - 1) {
				return vertices[1];
			}
			return vertices[((edge + 1) * 2) + 1];
		}

		@Override
		public LineSegment getEdgeLineSegment() {
			return edgeLineSegment;
		}

	}
}
