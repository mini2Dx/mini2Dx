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
package org.mini2Dx.core.engine.geom;

import org.mini2Dx.core.engine.Positionable;
import org.mini2Dx.core.engine.Sizeable;
import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Shape;

import com.badlogic.gdx.math.Vector2;

/**
 * Common interface for collision detection {@link Shape}s
 */
public interface CollisionShape extends Positionable, Sizeable {
	/**
	 * Sets the current x and y coordinate to the specified x and y and force updates the
	 * rendering position to match
	 * 
	 * @param x
	 *            The x coordinate to set
	 * @param y
	 *            The y coordinate to set
	 */
	public void forceTo(float x, float y);

	/**
	 * Returns if this {@link CollisionShape} contains the specified {@link Shape}
	 * @param shape The {@link Shape} to check
	 * @return True if this {@link Shape} is inside this {@link CollisionShape}
	 */
	public boolean contains(Shape shape);
	
	/**
	 * Returns if a {@link Vector2} is contained inside this {@link CollisionShape}
	 * 
	 * @param point
	 *            The {@link Vector2} to check
	 * @return True if this {@link CollisionShape} contains the specified {@link Vector2}
	 */
	public boolean contains(Vector2 point);
	
	/**
	 * Returns if this {@link CollisionShape} intersects a {@link Shape}
	 * @param shape The {@link Shape} to check
	 * @return True if this {@link CollisionShape} intersects the specified {@link Shape}
	 */
	public boolean intersects(Shape shape);
	
	/**
	 * Returns if this {@link CollisionShape} intersects the specified
	 * {@link LineSegment}
	 * 
	 * @param lineSegment
	 *            The {@link LineSegment} to check
	 * @return True if this {@link CollisionShape} intersects the {@link LineSegment}
	 */
	public boolean intersects(LineSegment lineSegment);
	
	/**
	 * Returns if this {@link CollisionShape} intersects a line segment
	 * 
	 * @param pointA
	 *            The first point in the line segment
	 * @param pointB
	 *            The second point in the line segment
	 * @return True if this {@link CollisionShape} intersects the line segment
	 */
	public boolean intersectsLineSegment(Vector2 pointA, Vector2 pointB);

	/**
	 * Returns if this {@link CollisionShape} intersects a line segment
	 * 
	 * @param x1 The x coordinate of the first point
	 * @param y1 The y coordinate of the first point
	 * @param x2 The x coordinate of the second point
	 * @param y2 The y coordinate of the second point
	 * @return True if this {@link CollisionShape} intersects the line segment
	 */
	public boolean intersectsLineSegment(float x1, float y1, float x2, float y2);
	
	/**
	 * Returns the underlying {@link Shape} instance
	 * @return
	 */
	public Shape getShape();
}
