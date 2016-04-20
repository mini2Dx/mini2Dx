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

import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.exception.NotYetImplementedException;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.util.EdgeIterator;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

/**
 * Implements a rectangle.
 */
public class Rectangle extends Shape implements
		Parallelogram {
	private static final long serialVersionUID = 4016090439885217620L;
	
	final Polygon polygon;
	private float width, height;
	private Rectangle tmp = null;
	
	/**
	 * Default constructor. Creates a {@link Rectangle} at 0,0 with a width and
	 * height of 1
	 */
	public Rectangle() {
		this(0, 0, 1, 1);
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
		this.width = width;
		this.height = height;
		polygon = new Polygon(determineVertices(x, y, width, height));
	}
	
	private Vector2 [] determineVertices(float x, float y, float width, float height) {
		Vector2 topLeft = new Vector2(x, y);
		Vector2 topRight = new Vector2(x + width, y);
		Vector2 bottomLeft = new Vector2(x, y + height);
		Vector2 bottomRight = new Vector2(x + width, y + height);
		return new Vector2[] {
				topLeft,
				topRight,
				bottomRight,
				bottomLeft
			};
	}

	/**
	 * Renders this {@link Rectangle} and the {@link LineSegment}s between each
	 * of its point and its rotational center
	 * 
	 * @param g
	 *            The {@link Graphics} context to render to
	 */
	public void debug(Graphics g) {
		this.draw(g);
//		g.drawLineSegment(topLeft.x, topLeft.y, rotationalCenter.x,
//				rotationalCenter.y);
//		g.drawLineSegment(topRight.x, topRight.y, rotationalCenter.x,
//				rotationalCenter.y);
//		g.drawLineSegment(bottomLeft.x, bottomLeft.y, rotationalCenter.x,
//				rotationalCenter.y);
//		g.drawLineSegment(bottomRight.x, bottomRight.y, rotationalCenter.x,
//				rotationalCenter.y);
	}
	
	public Rectangle lerp(Rectangle target, float alpha) {
		final float inverseAlpha = 1.0f - alpha;
		float x = (getX() * inverseAlpha) + (target.getX() * alpha);
		float y = (getY() * inverseAlpha) + (target.getY() * alpha);
		float width = this.width;
		float height = this.height;
		float rotation = polygon.getRotation();
		
		if(getWidth() != target.getWidth()) {
			width = (getWidth() * inverseAlpha) + (target.getWidth() * alpha);
		}
		
		if(getHeight() != target.getHeight()) {
			height = (getHeight() * inverseAlpha) + (target.getHeight() * alpha);
		}
		
		if(getRotation() != target.getRotation()) {
			rotation = (getRotation() * inverseAlpha) + (target.getRotation() * alpha);
		}
		set(x, y, width, height);
		setRotation(rotation);
		return this;
	}
	
	public float getDistanceTo(Point point) {
	    return getDistanceTo(point.getX(), point.getY());
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
	public void setRotationAround(Point center, float degrees) {
		polygon.setRotationAround(center.x, center.y, degrees);
	}

	@Override
	public void setRotationAround(float centerX, float centerY, float degrees) {
		polygon.setRotationAround(centerX, centerY, degrees);
	}
	
	@Override
	public boolean intersectsLineSegment(LineSegment lineSegment) {
		return polygon.intersects(lineSegment);
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
		boolean xAxisOverlaps = true;
		boolean yAxisOverlaps = true;

		if (polygon.getMaxX() < rectangle.getMinX())
			xAxisOverlaps = false;
		if (rectangle.getMaxX() < polygon.getMinX())
			xAxisOverlaps = false;
		if (polygon.getMaxY() < rectangle.getMinY())
			yAxisOverlaps = false;
		if (rectangle.getMaxY() < polygon.getMinY())
			yAxisOverlaps = false;

		return xAxisOverlaps && yAxisOverlaps;
	}

	/**
	 * @see Parallelogram#intersects(Parallelogram)
	 */
	@Override
	public boolean intersects(Parallelogram parallelogram) {
		if(tmp == null) {
			tmp = new Rectangle(parallelogram.getX(),
					parallelogram.getY(), parallelogram.getWidth(),
					parallelogram.getHeight());
		} else {
			tmp.set(parallelogram.getX(),
					parallelogram.getY(), parallelogram.getWidth(),
					parallelogram.getHeight());
		}
		return intersects(tmp);
	}

	/**
	 * @see Parallelogram#intersects(float, float, float, float)
	 */
	@Override
	public boolean intersects(float x, float y, float width, float height) {
		if(tmp == null) {
			tmp = new Rectangle(x, y, width, height);
		} else {
			tmp.set(x, y, width, height);
		}
		return intersects(tmp);
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
	 * @see Parallelogram#contains(Parallelogram)
	 */
	@Override
	public boolean contains(Parallelogram parallelogram) {
		if (parallelogram instanceof Rectangle) {
			return contains((Rectangle) parallelogram);
		} else {
			Rectangle rect = new Rectangle(parallelogram.getX(),
					parallelogram.getY(), parallelogram.getWidth(),
					parallelogram.getHeight());
			rect.rotate(parallelogram.getRotation());
			return contains(rect);
		}
	}

	/**
	 * @see Parallelogram#contains(Parallelogram)
	 */
	public boolean contains(Rectangle rectangle) {
		return this.polygon.contains(rectangle.polygon);
	}

	@Override
	public boolean contains(float x, float y) {
		return polygon.contains(x, y);
	}

	@Override
	public boolean contains(Vector2 point) {
		return polygon.contains(point);
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

	public Rectangle set(float x, float y, float width, float height) {
		float rotation = polygon.getRotation();
		polygon.setRotation(-rotation);
		polygon.setVertices(determineVertices(x, y, width, height));
		polygon.setRotation(rotation);
		
		this.width = width;
		this.height = height;
		return this;
	}

	public void set(Rectangle rectangle) {
		set(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
	}
	
	public void set(float x, float y) {
		polygon.set(x, y);
	}
	
	public void set(Vector2 position) {
		polygon.set(position.x, position.y);
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
		return 0f;
	}

	/**
	 * Returns the y coordinate of the center of this {@link Rectangle}
	 * 
	 * @return
	 */
	public float getCenterY() {
		return 0f;
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
}
