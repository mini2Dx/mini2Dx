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

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Implements a circle
 */
public class Circle extends com.badlogic.gdx.math.Circle implements Shape {
	private static final long serialVersionUID = 7900371446650127192L;

	public Circle(float radius) {
		this(0f, 0f, radius);
	}
	
	public Circle(float centerX, float centerY, float radius) {
		super(centerX, centerY, radius);
	}
	
	public Circle(Circle circle) {
		super(circle);
	}
	
	public Circle lerp(Circle target, float alpha) {
		final float inverseAlpha = 1.0f - alpha;
		x = (x * inverseAlpha) + (target.getX() * alpha);
		y = (y * inverseAlpha) + (target.getY() * alpha);
		radius = (radius * inverseAlpha) + (target.getRadius() * alpha);
		return this;
	}
	
	public boolean contains(Vector2 point) {
		return contains(point.x, point.y);
	}
	
	public boolean contains(float x, float y) {
		float dx = Math.abs(x - this.x);
		if(dx > radius) {
			return false;
		}
		
		float dy = Math.abs(y - this.y);
		if(dy > radius) {
			return false;
		}
		
		if(dx + dy <= radius) {
			return true;
		}
		if((dx * dx) + (dy * dy) <= (radius * radius)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns if the specified {@link Rectangle} intersects this {@link Circle}
	 * 
	 * @param rectangle The {@link Rectangle} to test for intersection
	 * @return True if intersection occurs
	 */
	public boolean interects(Rectangle rectangle) {
		return com.badlogic.gdx.math.Intersector.overlaps(this, rectangle);
	}

	/**
	 * Returns if the specified {@link Circle} intersects this one
	 * 
	 * @param circle The {@link Circle} to test for intersection
	 * @return True if intersection occurs
	 */
	public boolean intersects(Circle circle) {
		return Vector2.dst(x, y, circle.x, circle.y) <= radius + circle.radius;
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
		float result = Vector2.dst(this.x, this.y, x, y);
		if(result <= radius) {
			return 0f;
		}
		return result - radius;
	}
	
	/**
	 * Returns the distance from the center of this {@link Circle} to a point
	 * @param point The point
	 * @return The distane to the point
	 */
	public float getDistanceFromCenter(Vector2 point) {
		return Vector2.dst(x, y, point.x, point.y);
	}
	
	/**
	 * Returns the distance from the center of this {@link Circle} to a point
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return The distane to the point
	 */
	public float getDistanceFromCenter(float x, float y) {
		return Vector2.dst(this.x, this.y, x, y);
	}
	
	@Override
	public int getNumberOfSides() {
		return 0;
	}

	@Override
	public void draw(Graphics g) {
		g.drawCircle(x, y, MathUtils.round(radius));
	}
	
	@Override
	public void fill(Graphics g) {
		g.fillCircle(x, y, MathUtils.round(radius));
	}
	
	public void set(Circle circle) {
		setCenter(circle.getX(), circle.getY());
		setRadius(circle.getRadius());
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public void setCenter(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
}
