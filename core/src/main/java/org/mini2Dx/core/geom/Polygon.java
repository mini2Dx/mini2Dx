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

import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.utils.ShortArray;

/**
 * Implements a rotatable polygon. Adds extra functionality to the default
 * polygon implementation in LibGDX
 */
public class Polygon extends com.badlogic.gdx.math.Polygon implements Shape {
	private EarClippingTriangulator triangulator;

	private int maxXIndex, maxYIndex;
	private ShortArray triangles;

	public Polygon(float[] vertices) {
		super(vertices);
		triangulator = new EarClippingTriangulator();
		computeTriangles(vertices);
		calculateMaxXY(vertices);
	}

	public Polygon(Point[] points) {
		this(toVertices(points));
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

	public void addPoint(float x, float y) {
		float[] existingVertices = getVertices();
		float[] newVertices = new float[existingVertices.length + 2];

		if (existingVertices.length > 0) {
			System.arraycopy(existingVertices, 0, newVertices, 0, existingVertices.length);
		}
		newVertices[existingVertices.length] = x;
		newVertices[existingVertices.length + 1] = y;
		super.setVertices(newVertices);
		computeTriangles(newVertices);

		if (x > newVertices[maxXIndex]) {
			maxXIndex = existingVertices.length;
		}
		if (y > newVertices[maxYIndex]) {
			maxYIndex = existingVertices.length + 1;
		}
	}

	public void addPoint(Point point) {
		addPoint(point.x, point.y);
	}

	private void removePoint(int i) {
		float[] existingVertices = getVertices();
		float[] newVertices = new float[existingVertices.length - 2];
		if (i > 0) {
			System.arraycopy(existingVertices, 0, newVertices, 0, i);
		}
		if (i < existingVertices.length - 2) {
			System.arraycopy(existingVertices, i + 2, newVertices, i, existingVertices.length - i - 2);
		}
		super.setVertices(newVertices);
		computeTriangles(newVertices);
		
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

	public void removePoint(float x, float y) {
		float[] existingVertices = getVertices();
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

	public void removePoint(Point point) {
		removePoint(point.x, point.y);
	}

	@Override
	public int getNumberOfSides() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void draw(Graphics g) {
		g.drawPolygon(getVertices());
	}

	@Override
	public void fill(Graphics g) {
		g.fillPolygon(getVertices(), triangles.items);
	}
	
	@Override
	public void setVertices(float[] vertices) {
		super.setVertices(vertices);
		calculateMaxXY(vertices);
		computeTriangles(vertices);
	}

	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		calculateMaxXY(getVertices());
		computeTriangles(getVertices());
	}

	@Override
	public void setRotation(float degrees) {
		super.setRotation(degrees);
		calculateMaxXY(getVertices());
		computeTriangles(getVertices());
	}
	
	@Override
	public void rotate(float degrees) {
		super.rotate(degrees);
		calculateMaxXY(getVertices());
		computeTriangles(getVertices());
	}

	/**
	 * Returns the x coordinate
	 * 
	 * @return The x coordinate of the first point in this {@link Polygon}
	 */
	@Override
	public float getX() {
		return super.getX();
	}

	/**
	 * Returns the y coordinate
	 * 
	 * @return The y coordinate of the first point in this {@link Polygon}
	 */
	@Override
	public float getY() {
		return super.getY();
	}

	/**
	 * Returns max X coordinate of this {@link Polygon}
	 * 
	 * @return The right-most x coordinate
	 */
	public float getMaxX() {
		return getVertices()[maxXIndex];
	}

	/**
	 * Returns max Y coordinate of this {@link Polygon}
	 * 
	 * @return The bottom-most y coordinate
	 */
	public float getMaxY() {
		return getVertices()[maxYIndex];
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

	private static float[] toVertices(Point[] points) {
		if (points == null) {
			throw new MdxException(Point.class.getSimpleName() + " array cannot be null");
		}
		if (points.length < 3) {
			throw new MdxException(Point.class.getSimpleName() + " must have at least 3 points");
		}
		float[] result = new float[points.length * 2];
		for (int i = 0; i < points.length; i++) {
			int index = i * 2;
			result[index] = points[i].getX();
			result[index + 1] = points[i].getY();
		}
		return result;
	}
}
