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


/**
 * A common interface for parallelogram implementations
 */
public interface Parallelogram {
	/**
	 * Returns the x coordinate of the shape
	 * 
	 * @return
	 */
	public float getX();

	/**
	 * Returns the y coordinate of the shape
	 * 
	 * @return
	 */
	public float getY();

	/**
	 * Returns the width of the shape
	 * 
	 * @return
	 */
	public float getWidth();

	/**
	 * Returns the height of the shape
	 * 
	 * @return
	 */
	public float getHeight();
	
	/**
	 * Returns the greatest x coordinate of this shape
	 * @return
	 */
	public float getMaxX();
	
	/**
	 * Returns the greatest y coordinate of this shape
	 * @return
	 */
	public float getMaxY();

	/**
	 * Returns the current rotation of the shape in degrees
	 * 
	 * @return
	 */
	public float getRotation();

	/**
	 * Sets the current rotation of the shape around its top-left corner
	 * 
	 * Note: Rotates around the top-left corner
	 * 
	 * @param degrees
	 *            The rotation angle in degrees
	 */
	public void setRotation(float degrees);

	/**
	 * Sets the current rotation of the shape around a center point
	 * 
	 * @param center
	 *            The center point to rotate around
	 * @param degrees
	 *            The rotation angle in degrees
	 */
	public void setRotationAround(Point center, float degrees);

	/**
	 * Rotates the shape around its top-left corner by the specified degrees
	 * adding to its existing rotation
	 * 
	 * @param degrees
	 *            The rotation in degrees
	 */
	public void rotate(float degrees);

	/**
	 * Rotates the shape around a center point by the specified degrees adding
	 * to its existing rotation
	 * 
	 * @param centerX
	 * @param centerY
	 */
	public void rotateAround(float centerX, float centerY, float degrees);

	/**
	 * Returns if this shape intersects a specified {@link LineSegment}
	 * 
	 * @param lineSegment
	 *            The {@link LineSegment} to test for intersection
	 * @return True if an intersection occurs
	 */
	public boolean intersectsLineSegment(LineSegment lineSegment);

	/**
	 * Returns if this shape intersects a specified {@link Parallelogram}
	 * 
	 * @param parallelogram
	 *            The {@link Parallelogram} to test for intersection
	 * @return True if an intersection occurs
	 */
	public boolean intersects(Parallelogram parallelogram);

	/**
	 * Returns if this shape intersects a specified rectangle dimensions
	 * 
	 * @param x
	 *            The x coordinate of the rectangle
	 * @param y
	 *            The y coordinate of the rectangle
	 * @param width
	 *            The width of the rectangle
	 * @param height
	 *            The height of the rectangle
	 * @return True if an intersection occurs
	 */
	public boolean intersects(float x, float y, float width, float height);

	/**
	 * Returns if the specified {@link Parallelogram} is contained within this
	 * one
	 * 
	 * @param parallelogram The {@link Parallelogram} to test
	 * @return True if it is within this shape
	 */
	public boolean contains(Parallelogram parallelogram);
	
	
	/**
	 * Returns if the specified coordinates are contained within this {@link Parallelogram}
	 * 
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return  True if the coordinate is within this shape
	 */
	public boolean contains(float x, float y);
}
