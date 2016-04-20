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

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.FloatArray;

/**
 * Provides implementations for detecting intersections between geom package
 * objects.
 * 
 * Note: This exists because LibGDX implementations weren't accurate
 */
public class Intersector {
	private final static Vector2 ip = new Vector2();
	private final static Vector2 ep1 = new Vector2();
	private final static Vector2 ep2 = new Vector2();
	private final static Vector2 s = new Vector2();
	private final static Vector2 e = new Vector2();
	private final static FloatArray floatArray = new FloatArray();
	private final static FloatArray floatArray2 = new FloatArray();

	public static boolean intersectLines(Vector2 p1, Vector2 p2, Vector2 p3, Vector2 p4, Vector2 intersection) {
		float x1 = p1.x, y1 = p1.y, x2 = p2.x, y2 = p2.y, x3 = p3.x, y3 = p3.y, x4 = p4.x, y4 = p4.y;

		float det3 = det(x1 - x2, y1 - y2, x3 - x4, y3 - y4);
		if (det3 == 0)
			return false;

		float det1 = det(x1, y1, x2, y2);
		float det2 = det(x3, y3, x4, y4);

		float x = det(det1, x1 - x2, det2, x3 - x4) / det3;
		float y = det(det1, y1 - y2, det2, y3 - y4) / det3;

		intersection.x = x;
		intersection.y = y;

		return true;
	}

	public static boolean intersectLineSegments(float segAX1, float segAY1, float segAX2, float segAY2, float segBX1,
			float segBY1, float segBX2, float segBY2) {
		float x1 = segAX1, y1 = segAY1, x2 = segAX2, y2 = segAY2, x3 = segBX1, y3 = segBY1, x4 = segBX2, y4 = segBY2;

		// Return false if either of the lines have zero length
		if (x1 == x2 && y1 == y2 || x3 == x4 && y3 == y4) {
			return false;
		}
		// Fastest method, based on Franklin Antonio's
		// "Faster Line Segment Intersection" topic "in Graphics Gems III" book
		// (http://www.graphicsgems.org/)
		float ax = x2 - x1;
		float ay = y2 - y1;
		float bx = x3 - x4;
		float by = y3 - y4;
		float cx = x1 - x3;
		float cy = y1 - y3;

		float alphaNumerator = by * cx - bx * cy;
		float commonDenominator = ay * bx - ax * by;
		if (commonDenominator > 0) {
			if (alphaNumerator < 0 || alphaNumerator > commonDenominator) {
				return false;
			}
		} else if (commonDenominator < 0) {
			if (alphaNumerator > 0 || alphaNumerator < commonDenominator) {
				return false;
			}
		}
		float betaNumerator = ax * cy - ay * cx;
		if (commonDenominator > 0) {
			if (betaNumerator < 0 || betaNumerator > commonDenominator) {
				return false;
			}
		} else if (commonDenominator < 0) {
			if (betaNumerator > 0 || betaNumerator < commonDenominator) {
				return false;
			}
		}
		if (commonDenominator == 0) {
			// This code wasn't in Franklin Antonio's method. It was added by
			// Keith Woodward.
			// The lines are parallel.
			// Check if they're collinear.
			float y3LessY1 = y3 - y1;
			float collinearityTestForP3 = x1 * (y2 - y3) + x2 * (y3LessY1) + x3 * (y1 - y2); // see
																								// http://mathworld.wolfram.com/Collinear.html
			// If p3 is collinear with p1 and p2 then p4 will also be collinear,
			// since p1-p2 is parallel with p3-p4
			if (collinearityTestForP3 == 0) {
				// The lines are collinear. Now check if they overlap.
				if (x1 >= x3 && x1 <= x4 || x1 <= x3 && x1 >= x4 || x2 >= x3 && x2 <= x4 || x2 <= x3 && x2 >= x4
						|| x3 >= x1 && x3 <= x2 || x3 <= x1 && x3 >= x2) {
					if (y1 >= y3 && y1 <= y4 || y1 <= y3 && y1 >= y4 || y2 >= y3 && y2 <= y4 || y2 <= y3 && y2 >= y4
							|| y3 >= y1 && y3 <= y2 || y3 <= y1 && y3 >= y2) {
						return true;
					}
				}
			}
			return false;
		}
		return true;
	}

	private static float det(float a, float b, float c, float d) {
		return (a * d) - (b * c);
	}

	public static boolean intersects(Rectangle rectangle, Circle circle) {
		float closestX = circle.getX();
		float closestY = circle.getY();

		if (circle.getX() < rectangle.getMinX()) {
			closestX = rectangle.getMinX();
		} else if (circle.getX() > rectangle.getMaxX()) {
			closestX = rectangle.getMaxX();
		}

		if (circle.getY() < rectangle.getMinY()) {
			closestY = rectangle.getMinY();
		} else if (circle.getY() > rectangle.getMaxY()) {
			closestY = rectangle.getMaxY();
		}

		closestX = closestX - circle.getX();
		closestX *= closestX;
		closestY = closestY - circle.getY();
		closestY *= closestY;

		return closestX + closestY < circle.getRadius() * circle.getRadius();
	}

	public static boolean containsPolygon(Polygon p1, Polygon p2) {
		float[] polygonB = p2.getVertices();

		for (int i = 0; i < polygonB.length; i += 2) {
			if (!p1.contains(polygonB[i], polygonB[i + 1])) {
				return false;
			}
		}
		return true;
	}
}
