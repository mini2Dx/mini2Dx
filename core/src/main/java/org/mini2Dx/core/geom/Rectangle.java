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
import org.w3c.dom.css.Rect;

/**
 * Implements a rectangle.
 */
public class Rectangle extends Shape {
	private static final long serialVersionUID = 4016090439885217620L;

	private static final ThreadLocal<Vector2[]> TMP_VERTICES = new ThreadLocal<Vector2[]>() {
		@Override
		protected Vector2[] initialValue() {
			return new Vector2[] {
					new Vector2(), new Vector2(), new Vector2(), new Vector2()
			};
		}
	};
	private static final ThreadLocal<Rectangle> TMP_RECTANGLE = new ThreadLocal<Rectangle>() {
		@Override
		protected Rectangle initialValue() {
			return new Rectangle();
		}
	};

	final Polygon polygon;
	private float width, height;
	
	/**
	 * Default constructor. Creates a {@link Rectangle} at 0,0 with a width and
	 * height of 1
	 */
	public Rectangle() {
		this(0, 0, 1, 1);
	}

	/**
	 * Constructs a {@link Rectangle} belonging to the {@link Geometry} pool
	 * @param geometry The {@link Geometry} pool
	 */
	public Rectangle(Geometry geometry) {
		super(geometry);
		this.width = 1f;
		this.height = 1f;
		polygon = new Polygon(determineVertices(0f, 0f, width, height));
		initProxyListeners();
	}

	/**
	 * Constructor
	 * 
	 * @param x
	 *            The x coordinate of the {@link Rectangle}
	 * @param y
	 *            The y coordinate of the {@link Rectangle}
	 * @param width
	 *            The width of the {@link Rectangle}
	 * @param height
	 *            The height of the {@link Rectangle}
	 */
	public Rectangle(float x, float y, float width, float height) {
		super();
		this.width = width;
		this.height = height;
		polygon = new Polygon(determineVertices(x, y, width, height));
		initProxyListeners();
	}
	
	/**
	 * Constructs a new {@link Rectangle} as a copy of another
	 * @param rectangle The {@link Rectangle} to copy
	 */
	public Rectangle(Rectangle rectangle) {
		super();
		this.width = rectangle.getWidth();
		this.height = rectangle.getHeight();
		this.polygon = (Polygon) rectangle.polygon.copy();
		initProxyListeners();
	}

	private void initProxyListeners() {
		polygon.addPostionChangeListener(new PositionChangeListener<Positionable>() {
			@Override
			public void positionChanged(Positionable moved) {
				Rectangle.this.notifyPositionChangeListeners();
			}
		});
		polygon.addSizeChangeListener(new SizeChangeListener<Sizeable>() {
			@Override
			public void sizeChanged(Sizeable changed) {
				Rectangle.this.notifySizeChangeListeners();
			}
		});
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
		return new Rectangle(this);
	}
	
	private Vector2 [] determineVertices(float x, float y, float width, float height) {
		final Vector2 [] result = TMP_VERTICES.get();
		result[0].set(x, y);
		result[1].set(x + width, y);
		result[2].set(x + width, y + height);
		result[3].set(x, y + height);
		return result;
	}
	
	@Override
	public boolean contains(float x, float y) {
		return polygon.contains(x, y);
	}

	@Override
	public boolean contains(Vector2 point) {
		return polygon.contains(point);
	}
	
	@Override
	public boolean contains(Sizeable shape) {
		return polygon.contains(shape);
	}
	
	public boolean contains(Rectangle rectangle) {
		return this.polygon.contains(rectangle.polygon);
	}
	
	public boolean contains(Circle circle) {
		return this.polygon.contains(circle.getBoundingBox());
	}

	@Override
	public boolean intersects(Sizeable shape) {
		return polygon.intersects(shape);
	}

	/**
	 * Returns if the specified {@link Circle} intersects this {@link Rectangle}
	 * 
	 * @param circle The {@link Circle} to test for intersection
	 * @return True if the {@link Circle} intersects
	 */
	public boolean intersects(Circle circle) {
		return polygon.intersects(circle);
	}

	/**
	 * Returns if the specified {@link Rectangle} intersects this one
	 * 
	 * @param rectangle
	 *            The {@link Rectangle} to test for intersection
	 * @return True if the {@link Rectangle}s intersect
	 */
	public boolean intersects(Rectangle rectangle) {
		return polygon.intersects(rectangle);
	}

	public boolean intersects(float x, float y, float width, float height) {
		final Rectangle tmpRectangle = TMP_RECTANGLE.get();
		tmpRectangle.set(x, y, width, height);
		tmpRectangle.setRotation(0f);
		return intersects(tmpRectangle);
	}
	
	/**
	 * Returns if the specified {@link Triangle} intersects this {@link Rectangle}
	 * @param triangle The {@link Triangle} to check
	 * @return True if this {@link Rectangle} and the {@link Triangle} intersect
	 */
	public boolean intersects(Triangle triangle) {
		return polygon.intersects(triangle);
	}
	
	/**
	 * Returns if the specified {@link Polygon} intersects this {@link Rectangle}
	 * @param polygon The {@link Polygon} to check
	 * @return True if this {@link Rectangle} and the {@link Polygon} intersect
	 */
	public boolean intersects(Polygon polygon) {
		return this.polygon.intersects(polygon);
	}

	@Override
	public boolean intersectsLineSegment(Vector2 pointA, Vector2 pointB) {
		return polygon.intersectsLineSegment(pointA, pointB);
	}

	@Override
	public boolean intersectsLineSegment(float x1, float y1, float x2, float y2) {
		return polygon.intersectsLineSegment(x1, y1, x2, y2);
	}

	public Rectangle intersection(Rectangle rect) {
		if (polygon.getRotation() != 0f || rect.getRotation() != 0f)
			throw new UnsupportedOperationException(
					"Rectangle.intersection is not implemented to handle rotated rectangles");

		float newX = Math.max(getX(), rect.getX());
		float newY = Math.max(getY(), rect.getY());
		float newWidth = Math.min(getMaxX(), rect.getMaxX()) - newX;
		float newHeight = Math.min(getMaxY(), rect.getMaxY()) - newY;
		return new Rectangle(newX, newY, newWidth, newHeight);
	}

	/**
	 * @see Shape#getNumberOfSides()
	 */
	@Override
	public int getNumberOfSides() {
		return 4;
	}

	/**
	 * @see Shape#draw(Graphics)
	 */
	@Override
	public void draw(Graphics g) {
		polygon.draw(g);
	}
	
	@Override
	public void fill(Graphics g) {
		polygon.fill(g);
	}
	
	public Rectangle lerp(Rectangle target, float alpha) {
		lerp(this, this, target, alpha);
		return this;
	}

	public Rectangle lerp(Rectangle result, Rectangle target, float alpha) {
		lerp(result, this, target, alpha);
		return this;
	}

	public static void lerp(Rectangle result, Rectangle from, Rectangle target, float alpha) {
		float x = Lerper.lerp(from.getX(), target.getX(), alpha);
		float y = Lerper.lerp(from.getY(), target.getY(), alpha);
		float width = from.width;
		float height = from.height;

		if(from.getWidth() != target.getWidth()) {
			width = Lerper.lerp(from.getWidth(), target.getWidth(), alpha);
		}

		if(from.getHeight() != target.getHeight()) {
			height = Lerper.lerp(from.getHeight(), target.getHeight(), alpha);
		}

		if(from.getRotation() != target.getRotation()) {
			float rotation = Lerper.lerp(from.getRotation(), target.getRotation(), alpha);
			result.setRotation(rotation);
		}
		result.set(x, y, width, height);
	}
	
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
	public void rotate(float degrees) {
		polygon.rotate(degrees);
	}
	
	@Override
	public void rotateAround(float centerX, float centerY, float degrees) {
		polygon.rotateAround(centerX, centerY, degrees);
	}

	@Override
	public void setRotationAround(float centerX, float centerY, float degrees) {
		polygon.setRotationAround(centerX, centerY, degrees);
	}

	public Rectangle set(float x, float y, float width, float height) {
		float rotation = polygon.getRotation();
		if(rotation != 0f) {
			polygon.setRotation(-rotation);
		}
		polygon.setVertices(determineVertices(x, y, width, height));
		if(rotation != 0f) {
			polygon.setRotation(rotation);
		}
		
		this.width = width;
		this.height = height;
		return this;
	}

	public void set(Rectangle rectangle) {
		if(this.equals(rectangle)) {
			return;
		}
		set(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
		setRotation(rectangle.getRotation());
	}
	
	public void setXY(float x, float y) {
		polygon.setXY(x, y);
	}
	
	public void setXY(Vector2 position) {
		polygon.setXY(position.x, position.y);
	}
	
	@Override
	public float getX() {
		return polygon.getX();
	}
	
	public void setX(float x) {
		polygon.setX(x);
	}
	
	@Override
	public float getY() {
		return polygon.getY();
	}
	
	public void setY(float y) {
		polygon.setY(y);
	}
	
	public float getWidth() {
		return width;
	}

	public Rectangle setWidth(float width) {
		float rotation = polygon.getRotation();
		polygon.setRotation(-rotation);
		polygon.setVertices(determineVertices(getX(), getY(), width, getHeight()));
		polygon.setRotation(rotation);
		this.width = width;
		return this;
	}
	
	public float getHeight() {
		return height;
	}

	public Rectangle setHeight(float height) {
		float rotation = polygon.getRotation();
		polygon.setRotation(-rotation);
		polygon.setVertices(determineVertices(getX(), getY(), getWidth(), height));
		polygon.setRotation(rotation);
		this.height = height;
		return this;
	}

	public Rectangle setSize(float width, float height) {
		float rotation = polygon.getRotation();
		polygon.setRotation(-rotation);
		polygon.setVertices(determineVertices(getX(), getY(), getWidth(), height));
		polygon.setRotation(rotation);
		
		this.width = width;
		this.height = height;
		return this;
	}

	public Rectangle setSize(float sizeXY) {
		float rotation = polygon.getRotation();
		polygon.setRotation(-rotation);
		polygon.setVertices(determineVertices(getX(), getY(), sizeXY, sizeXY));
		polygon.setRotation(rotation);
		
		this.width = sizeXY;
		this.height = sizeXY;
		return this;
	}
	
	@Override
	public void setRadius(float radius) {
		polygon.setRadius(radius);
		width = polygon.getMaxX() - polygon.getX();
		height = polygon.getMaxY() - polygon.getY();
	}
	
	@Override
	public void scale(float scale) {
		polygon.scale(scale);
		width = polygon.getMaxX() - polygon.getX();
		height = polygon.getMaxY() - polygon.getY();
	}
	
	@Override
	public void translate(float translateX, float translateY) {
		polygon.translate(translateX, translateY);
	}
	
	@Override
	public EdgeIterator edgeIterator() {
		return polygon.edgeIterator();
	}

	/**
	 * Returns the x coordinate of the center of this {@link Rectangle}
	 * 
	 * @return
	 */
	public float getCenterX() {
		return polygon.getCenterX();
	}

	/**
	 * Returns the y coordinate of the center of this {@link Rectangle}
	 * 
	 * @return
	 */
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

	/**
	 * Returns the least x coordinate this {@link Rectangle}
	 * 
	 * @return
	 */
	public float getMinX() {
		return polygon.getMinX();
	}

	/**
	 * Returns the least y coordinate this {@link Rectangle}
	 * 
	 * @return
	 */
	public float getMinY() {
		return polygon.getMinY();
	}

	/**
	 * Returns the greatest x coordinate this {@link Rectangle}
	 * 
	 * @return
	 */
	public float getMaxX() {
		return polygon.getMaxX();
	}

	/**
	 * Returns the greatest y coordinate this {@link Rectangle}
	 * 
	 * @return
	 */
	public float getMaxY() {
		return polygon.getMaxY();
	}
	
	/**
	 * Returns the vertices that make up this {@link Rectangle}
	 * @return
	 */
	public float [] getVertices() {
		return polygon.getVertices();
	}

	@Override
	public String toString() {
		return "Rectangle [rotation=" + polygon.getRotation() + ", x=" + getX() + ", y=" + getY()
				+ ", width=" + getWidth() + ", height=" + getHeight() + "]";
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(getX());
		result = prime * result + Float.floatToIntBits(getY());
		result = prime * result + Float.floatToIntBits(height);
		result = prime * result + Float.floatToIntBits(width);
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
		Rectangle other = (Rectangle) obj;
		if (Float.floatToIntBits(getX()) != Float.floatToIntBits(other.getX()))
			return false;
		if (Float.floatToIntBits(getY()) != Float.floatToIntBits(other.getY()))
			return false;
		if (Float.floatToIntBits(height) != Float.floatToIntBits(other.height))
			return false;
		if (Float.floatToIntBits(width) != Float.floatToIntBits(other.width))
			return false;
		if (Float.floatToIntBits(getRotation()) != Float.floatToIntBits(other.getRotation()))
			return false;
		return true;
	}
}
