/**
 * Copyright (c) 2013, mini2Dx Project
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
 * Adds extra functionality to the default rectangle implementation in LibGDX
 * 
 * @author Thomas Cashman
 */
public class Rectangle extends com.badlogic.gdx.math.Rectangle {
	private static final long serialVersionUID = 6405432580555614156L;

	private float centerX, centerY;
	private float maxX, maxY;

	/**
	 * Constructor. Creates a {@link Rectangle} at 0,0 with a width and height
	 * of 1
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
		super(x, y, width, height);
		calculateCenterCoordinates();
		calculateMaxCoordinates();
	}

	private void calculateMaxCoordinates() {
		if (this.getWidth() > 0f) {
			maxX = this.getX() + this.getWidth();
		} else {
			maxX = getX();
		}
		if (this.getHeight() > 0f) {
			maxY = this.getY() + this.getHeight();
		} else {
			maxY = getY();
		}
	}

	private void calculateCenterCoordinates() {
		centerX = getX() + (getWidth() / 2f);
		centerY = getY() + (getHeight() / 2f);
	}

	/**
	 * Returns if another {@link Rectangle} intersects this one
	 * 
	 * @param rect
	 *            The {@link Rectangle} to check if it intersects this one
	 * @return True if there is an intersection
	 */
	public boolean intersects(Rectangle rect) {
		if ((x > rect.getMaxX()) || ((getMaxX()) < rect.getX())) {
			return false;
		}
		if ((y > (rect.getMaxY())) || ((getMaxY()) < rect.getY())) {
			return false;
		}
		return true;
	}

	@Override
	public void setX(float x) {
		super.setX(x);
		calculateCenterCoordinates();
		calculateMaxCoordinates();
	}

	@Override
	public void setY(float y) {
		super.setY(y);
		calculateCenterCoordinates();
		calculateMaxCoordinates();
	}

	@Override
	public void setWidth(float width) {
		if (width > 0f) {
			super.setWidth(width);
			calculateCenterCoordinates();
			calculateMaxCoordinates();
		} else {
			
		}
	}

	@Override
	public void setHeight(float height) {
		if(height > 0f) {
			super.setHeight(height);
			calculateCenterCoordinates();
			calculateMaxCoordinates();
		} else {
			
		}
	}

	@Override
	public void set(com.badlogic.gdx.math.Rectangle rectangle) {
		super.set(rectangle);
		calculateCenterCoordinates();
		calculateMaxCoordinates();
	}

	@Override
	public void set(float x, float y, float width, float height) {
		if(width > 0f && height > 0f) {
			super.set(x, y, width, height);
			calculateCenterCoordinates();
			calculateMaxCoordinates();
		}
	}

	/**
	 * Returns the center x coordinate of the {@link Rectangle}
	 * 
	 * @return
	 */
	public float getCenterX() {
		return centerX;
	}

	/**
	 * Sets the center x coordinate of the {@link Rectangle} and recalculates
	 * the x and maxX coordinates based on the width of {@link Rectangle}
	 * 
	 * @param x
	 */
	public void setCenterX(float x) {
		centerX = x;
		super.setX(centerX - (getWidth() / 2f));
		calculateMaxCoordinates();
	}

	/**
	 * Returns the center y coordiante of the {@link Rectangle}
	 * 
	 * @return
	 */
	public float getCenterY() {
		return centerY;
	}

	/**
	 * Sets the center y coordinate of the {@link Rectangle} and recalculates
	 * the y and maxY coordinates based on the height of the {@link Rectangle}
	 * 
	 * @param y
	 */
	public void setCenterY(float y) {
		centerY = y;
		super.setY(centerY - (getHeight() / 2f));
		calculateMaxCoordinates();
	}

	/**
	 * Returns the x coordinate of the right side of this {@link Rectangle}
	 * 
	 * @return
	 */
	public float getMaxX() {
		return maxX;
	}

	/**
	 * Returns the y coordinate of the bottom side of this {@link Rectangle}
	 * 
	 * @return
	 */
	public float getMaxY() {
		return maxY;
	}
}
