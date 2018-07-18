/**
 * Copyright (c) 2018 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice, this
 *   list of conditions and the following disclaimer in the documentation and/or
 *   other materials provided with the distribution.
 *
 * * Neither the name of mini2Dx nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.geom;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.gdx.math.Vector2;

/**
 * Unit tests for {@link Circle}
 */
public class CircleTest {
	private Circle circle1, circle2;

	@Before
	public void setup() {
		circle1 = new Circle(4);
	}
	
	@Test
	public void testXY() {
		circle1 = new Circle(100f, 50f, 25f);
		Assert.assertEquals(100f, circle1.getX());
		Assert.assertEquals(50f, circle1.getY());
		Assert.assertEquals(25f, circle1.getRadius());
	}

	@Test
	public void testContains() {
		for (float x = -5f; x <= 5f; x += 0.1f) {
			for (float y = -5f; y <= 5f; y += 0.1f) {
				Assert.assertEquals(circle1.getDistanceFromCenter(x, y) <= circle1.getRadius(), circle1.contains(x, y));
			}
		}
	}

	@Test
	public void testIntersectsCircle() {
		Circle circle2 = new Circle(20f, 20f, 4);
		Assert.assertEquals(false, circle1.intersects(circle2));
		Assert.assertEquals(false, circle2.intersects(circle1));

		circle2.set(5f, 0f);
		Assert.assertEquals(true, circle1.intersects(circle2));
		Assert.assertEquals(true, circle2.intersects(circle1));
	}

	@Test
	public void testGetDistanceToPositionable() {
		Point point = new Point(3f, 0f);
		Assert.assertEquals(0f, circle1.getDistanceTo(point));

		point.set(5f, 0f);
		Assert.assertEquals(1f, circle1.getDistanceTo(point));
	}

	@Test
	public void testSetCenter() {
		circle1.set(20f, 25f);

		Assert.assertEquals(20f, circle1.getX());
		Assert.assertEquals(25f, circle1.getY());
	}

	@Test
	public void testSetX() {
		circle1.setX(25f);

		Assert.assertEquals(25f, circle1.getX());
		Assert.assertEquals(0f, circle1.getY());
	}

	@Test
	public void testSetY() {
		circle1.setY(25f);

		Assert.assertEquals(0f, circle1.getX());
		Assert.assertEquals(25f, circle1.getY());
	}

	@Test
	public void testContainsShape() {
		Shape shape = new Circle(2f);
		Assert.assertEquals(true, circle1.contains(shape));
		
		shape = new Rectangle(0f,0f,1f,1f);
		Assert.assertEquals(true, circle1.contains(shape));

		shape = new Rectangle(0f,0f,3f,3f);
		Assert.assertEquals(false, circle1.contains(shape));
		
		shape = new Triangle(new Vector2(0f, 4f), new Vector2(-4f,0f), new Vector2(4f,0f));
		Assert.assertEquals(true,circle1.contains(shape));

		shape = new Triangle(new Vector2(0f, 5f), new Vector2(-4f,0f), new Vector2(4f,0f));
		Assert.assertEquals(false,circle1.contains(shape));
	}

	@Test
	public void testContainsRectangle() {
		Rectangle rectangle = new Rectangle(0f,0f,1f,1f);
		Assert.assertEquals(true, circle1.contains(rectangle));

		rectangle = new Rectangle(0f,0f,3f,3f);
		Assert.assertEquals(false, circle1.contains(rectangle));
	}

	@Test
	public void testIntersectLineSegmentsXY() {
		Assert.assertEquals(true, circle1.intersectsLineSegment(0f,0f,5f,5f));
		Assert.assertEquals(false, circle1.intersectsLineSegment(-5f,5f,5f,5f));
	}

	@Test
	public void testIntersectLineSegmentsVectors() {
		Vector2 point1 = new Vector2(0f, 0f);
		Vector2 point2 = new Vector2(5f, 5f);
		Assert.assertEquals(true, circle1.intersectsLineSegment(point1, point2));

		point1 = new Vector2(-5f,5f);
		point2 = new Vector2(5f, 5f);
		Assert.assertEquals(false, circle1.intersectsLineSegment(point1, point2));

	}

	@Test
	public void testGetBoundingBox() {
		Rectangle boundingBox = circle1.getBoundingBox();
		Assert.assertEquals(-4f, boundingBox.getX());
		Assert.assertEquals(-4f, boundingBox.getY());
		Assert.assertEquals(8f, boundingBox.getWidth());
		Assert.assertEquals(8f, boundingBox.getHeight());
	}

	@Test
	public void testTranslate() {
		circle1.translate(1f,-1f);
		Assert.assertEquals(1f,circle1.getCenterX());
		Assert.assertEquals(-1f,circle1.getCenterY());
	}
	
	@Test
	public void testEquals() {
		circle1 = new Circle(4);
		circle2 = new Circle(4);
		
		Assert.assertEquals(true, circle1.equals(circle2));
		
		circle1 = new Circle(1, 1, 4);
		circle2 = new Circle(1, 1, 4);
		
		Assert.assertEquals(true, circle1.equals(circle2));
		
		circle1 = new Circle(4);
		circle2 = new Circle(4.00001f);
		
		Assert.assertEquals(false, circle1.equals(circle2));
	}
}
