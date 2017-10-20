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
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.engine.PositionChangeListener;

/**
 * Unit tests for {@link Circle}
 */
public class CircleTest {
	private Circle circle;

	@Before
	public void setup() {
		circle = new Circle(4);
	}
	
	@Test
	public void testXY() {
		circle = new Circle(100f, 50f, 25f);
		Assert.assertEquals(100f, circle.getX());
		Assert.assertEquals(50f, circle.getY());
		Assert.assertEquals(25f, circle.getRadius());
	}

	@Test
	public void testContains() {
		for (float x = -5f; x <= 5f; x += 0.1f) {
			for (float y = -5f; y <= 5f; y += 0.1f) {
				Assert.assertEquals(circle.getDistanceFromCenter(x, y) <= circle.getRadius(), circle.contains(x, y));
			}
		}
	}

	@Test
	public void testIntersectsCircle() {
		Circle circle2 = new Circle(20f, 20f, 4);
		Assert.assertEquals(false, circle.intersects(circle2));
		Assert.assertEquals(false, circle2.intersects(circle));

		circle2.set(5f, 0f);
		Assert.assertEquals(true, circle.intersects(circle2));
		Assert.assertEquals(true, circle2.intersects(circle));
	}

	@Test
	public void testGetDistanceToPositionable() {
		Point point = new Point(3f, 0f);
		Assert.assertEquals(0f, circle.getDistanceTo(point));

		point.set(5f, 0f);
		Assert.assertEquals(1f, circle.getDistanceTo(point));
	}

	@Test
	public void testSetCenter() {
		circle.set(20f, 25f);

		Assert.assertEquals(20f, circle.getX());
		Assert.assertEquals(25f, circle.getY());
	}

	@Test
	public void testSetX() {
		circle.setX(25f);

		Assert.assertEquals(25f, circle.getX());
		Assert.assertEquals(0f, circle.getY());
	}

	@Test
	public void testSetY() {
		circle.setY(25f);

		Assert.assertEquals(0f, circle.getX());
		Assert.assertEquals(25f, circle.getY());
	}

	@Test
	public void testContainsShape() {
		Shape shape = new Circle(2f);
		Assert.assertEquals(true, circle.contains(shape));
		
		shape = new Rectangle(0f,0f,1f,1f);
		Assert.assertEquals(true, circle.contains(shape));

		shape = new Rectangle(0f,0f,3f,3f);
		Assert.assertEquals(false, circle.contains(shape));
		
		shape = new Triangle(new Vector2(0f, 4f), new Vector2(-4f,0f), new Vector2(4f,0f));
		Assert.assertEquals(true,circle.contains(shape));

		shape = new Triangle(new Vector2(0f, 5f), new Vector2(-4f,0f), new Vector2(4f,0f));
		Assert.assertEquals(false,circle.contains(shape));
	}

	@Test
	public void testContainsRectangle() {
		Rectangle rectangle = new Rectangle(0f,0f,1f,1f);
		Assert.assertEquals(true, circle.contains(rectangle));

		rectangle = new Rectangle(0f,0f,3f,3f);
		Assert.assertEquals(false, circle.contains(rectangle));
	}

	@Test
	public void testIntersectLineSegmentsXY() {
		Assert.assertEquals(true, circle.intersectsLineSegment(0f,0f,5f,5f));
		Assert.assertEquals(false, circle.intersectsLineSegment(-5f,5f,5f,5f));
	}

	@Test
	public void testIntersectLineSegmentsVectors() {
		Vector2 point1 = new Vector2(0f, 0f);
		Vector2 point2 = new Vector2(5f, 5f);
		Assert.assertEquals(true, circle.intersectsLineSegment(point1, point2));

		point1 = new Vector2(-5f,5f);
		point2 = new Vector2(5f, 5f);
		Assert.assertEquals(false, circle.intersectsLineSegment(point1, point2));

	}

	@Test
	public void testGetBoundingBox() {
		Rectangle boundingBox = circle.getBoundingBox();
		Assert.assertEquals(-4f, boundingBox.getX());
		Assert.assertEquals(-4f, boundingBox.getY());
		Assert.assertEquals(8f, boundingBox.getWidth());
		Assert.assertEquals(8f, boundingBox.getHeight());
	}

	@Test
	public void testTranslate() {
		circle.translate(1f,-1f);
		Assert.assertEquals(1f,circle.getCenterX());
		Assert.assertEquals(-1f,circle.getCenterY());
	}
}
