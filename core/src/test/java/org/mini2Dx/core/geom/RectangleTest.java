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

import java.util.Random;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.engine.PositionChangeListener;

import com.badlogic.gdx.math.MathUtils;

/**
 * Unit tests for {@link Rectangle}
 */
public class RectangleTest {
	private Rectangle rectangle1, rectangle2;

	@Test
	public void testRectangleDefaultConstructor() {
		rectangle1 = new Rectangle();
		Assert.assertEquals(0f, rectangle1.getX());
		Assert.assertEquals(0f, rectangle1.getY());
		Assert.assertEquals(1f, rectangle1.getWidth());
		Assert.assertEquals(1f, rectangle1.getHeight());
		Assert.assertEquals(1f, rectangle1.getMaxX());
		Assert.assertEquals(1f, rectangle1.getMaxY());
	}

	@Test
	public void testRectangle() {
		rectangle1 = new Rectangle(100f, 100f, 50f, 50f);
		Assert.assertEquals(100f, rectangle1.getX());
		Assert.assertEquals(100f, rectangle1.getY());
		Assert.assertEquals(50f, rectangle1.getWidth());
		Assert.assertEquals(50f, rectangle1.getHeight());
		Assert.assertEquals(150f, rectangle1.getMaxX());
		Assert.assertEquals(150f, rectangle1.getMaxY());
	}
	
	@Test
	public void testManyRectangles() {
		Random random = new Random();
		for(int i = 0; i < 1000; i++) {
			float x = random.nextInt();
			float y = random.nextInt();
			rectangle1 = new Rectangle(x, y, 50f, 50f);
			Assert.assertEquals(x, rectangle1.getX());
			Assert.assertEquals(y, rectangle1.getY());
			Assert.assertEquals(50f, rectangle1.getWidth());
			Assert.assertEquals(50f, rectangle1.getHeight());
		}
	}

	@Test
	public void testSetX() {
		rectangle1 = new Rectangle(100f, 100f, 50f, 50f);
		rectangle1.setX(200f);
		Assert.assertEquals(200f, rectangle1.getX());
		Assert.assertEquals(100f, rectangle1.getY());
		Assert.assertEquals(50f, rectangle1.getWidth());
		Assert.assertEquals(50f, rectangle1.getHeight());
	}

	@Test
	public void testSetY() {
		rectangle1 = new Rectangle(100f, 100f, 50f, 50f);
		rectangle1.setY(200f);
		Assert.assertEquals(100f, rectangle1.getX());
		Assert.assertEquals(200f, rectangle1.getY());
		Assert.assertEquals(50f, rectangle1.getWidth());
		Assert.assertEquals(50f, rectangle1.getHeight());
	}

	@Test
	public void testSetWidth() {
		rectangle1 = new Rectangle(100f, 100f, 50f, 50f);
		rectangle1.setWidth(100f);
		Assert.assertEquals(100f, rectangle1.getX());
		Assert.assertEquals(100f, rectangle1.getY());
		Assert.assertEquals(100f, rectangle1.getWidth());
		Assert.assertEquals(50f, rectangle1.getHeight());
	}

	@Test
	public void testSetHeight() {
		rectangle1 = new Rectangle(100f, 100f, 50f, 50f);
		rectangle1.setHeight(100f);
		Assert.assertEquals(100f, rectangle1.getX());
		Assert.assertEquals(100f, rectangle1.getY());
		Assert.assertEquals(50f, rectangle1.getWidth());
		Assert.assertEquals(100f, rectangle1.getHeight());
	}

	@Test
	public void testSetFloatFloatFloatFloat() {
		rectangle1 = new Rectangle(100f, 100f, 50f, 50f);
		rectangle1.set(0f, 0f, 50f, 50f);
		Assert.assertEquals(0f, rectangle1.getX());
		Assert.assertEquals(0f, rectangle1.getY());
		Assert.assertEquals(50f, rectangle1.getWidth());
		Assert.assertEquals(50f, rectangle1.getHeight());
	}
	
	@Test
	public void testSetRectangle() {
		rectangle1 = new Rectangle(100f, 100f, 50f, 50f);
		rectangle2 = new Rectangle(200f, 200f, 100f, 100f);
		rectangle1.set(rectangle2);
		Assert.assertEquals(200f, rectangle1.getX());
		Assert.assertEquals(200f, rectangle1.getY());
		Assert.assertEquals(100f, rectangle1.getWidth());
		Assert.assertEquals(100f, rectangle1.getHeight());
	}
	
	@Test
	public void testRotateAround() {
		rectangle1 = new Rectangle(0f, 0f, 10f, 10f);
		rectangle1.rotateAround(0f, 0f, 90f);
		
		Assert.assertEquals(0f, rectangle1.getVertices()[0]);
		Assert.assertEquals(0f, rectangle1.getVertices()[1]);
		
		Assert.assertEquals(0, MathUtils.round(rectangle1.getVertices()[2]));
		Assert.assertEquals(10, MathUtils.round(rectangle1.getVertices()[3]));
		
		Assert.assertEquals(-10, MathUtils.round(rectangle1.getVertices()[4]));
		Assert.assertEquals(10, MathUtils.round(rectangle1.getVertices()[5]));
		
		Assert.assertEquals(-10, MathUtils.round(rectangle1.getVertices()[6]));
		Assert.assertEquals(0, MathUtils.round(rectangle1.getVertices()[7]));
	}
	
	@Test
	public void testContainsPoint() {
		rectangle1 = new Rectangle(0, 0, 50, 50);
		Point point = new Point(5, 1);
		
		Assert.assertEquals(true, rectangle1.contains(point));
		
		point.set(5, -1);
		Assert.assertEquals(false, rectangle1.contains(point));
		
		point.set(51, 1);
		Assert.assertEquals(false, rectangle1.contains(point));
		
		point.set(5, 51);
		Assert.assertEquals(false, rectangle1.contains(point));
		
		point.set(-5, 1);
		Assert.assertEquals(false, rectangle1.contains(point));
		
		point.set(5, 1);
		rectangle1.rotate(45f);
		Assert.assertEquals(false, rectangle1.contains(point));
		
		point.set(-5, 1);
		Assert.assertEquals(false, rectangle1.contains(point));
	}
	
	@Test
	public void testContainsParallelogram() {
		rectangle1 = new Rectangle(0, 0, 50, 50);
		rectangle2 = new Rectangle(50, 50, 50, 50);
		
		Assert.assertEquals(false, rectangle1.contains(rectangle2));
		Assert.assertEquals(false, rectangle2.contains(rectangle1));
		
		rectangle2 = new Rectangle(25, 25, 50, 50);
		Assert.assertEquals(false, rectangle1.contains(rectangle2));
		Assert.assertEquals(false, rectangle2.contains(rectangle1));
		
		rectangle2 = new Rectangle(0, 0, 25, 25);
		Assert.assertEquals(true, rectangle1.contains(rectangle2));
		Assert.assertEquals(false, rectangle2.contains(rectangle1));
		
		rectangle2 = new Rectangle(15, 15, 25, 25);
		Assert.assertEquals(true, rectangle1.contains(rectangle2));
		Assert.assertEquals(false, rectangle2.contains(rectangle1));
		
		rectangle2 = new Rectangle(48, 48, 25, 25);
		Assert.assertEquals(false, rectangle1.contains(rectangle2));
		Assert.assertEquals(false, rectangle2.contains(rectangle1));
		
		rectangle1 = new Rectangle(0, 0, 128, 128);
		rectangle2 = new Rectangle(42, 72, 32, 32);
		Assert.assertEquals(true, rectangle1.contains(rectangle2));
		Assert.assertEquals(false, rectangle2.contains(rectangle1));
	}
	
	@Test
	public void testIntersectsLineSegement() {
		rectangle1 = new Rectangle(2, 2, 4, 4);
		LineSegment segment = new LineSegment(0, 0, 10, 10);
		
		Assert.assertEquals(true, rectangle1.intersects(segment));
		
		segment.getPointA().set(10, 2);
		
		Assert.assertEquals(false, rectangle1.intersects(segment));
		
		rectangle1 = new Rectangle(96, 0, 32, 32);
		segment = new LineSegment(0, 0, 128, 128);
		Assert.assertEquals(false, rectangle1.intersects(segment));
		
		rectangle1 = new Rectangle(0, 0, 32, 32);
		Assert.assertEquals(true, rectangle1.intersects(new LineSegment(0f, 0f, -1f, -1f)));
		Assert.assertEquals(true, rectangle1.intersects(new LineSegment(-1f, -1f, 0f, 0f)));
		Assert.assertEquals(true, rectangle1.intersects(new LineSegment(0f, 0f, 1f, 1f)));
		Assert.assertEquals(true, rectangle1.intersects(new LineSegment(1f, 1f, 0f, 0f)));
		
		Assert.assertEquals(true, rectangle1.intersects(new LineSegment(32f, 32f, 31f, 31f)));
		Assert.assertEquals(true, rectangle1.intersects(new LineSegment(31f, 31f, 32f, 32f)));
		Assert.assertEquals(true, rectangle1.intersects(new LineSegment(33f, 33f, 32f, 32f)));
		Assert.assertEquals(true, rectangle1.intersects(new LineSegment(32f, 32f, 33f, 33f)));
	}
	
	@Test
	public void testIntersectsCircle() {
		rectangle1 = new Rectangle(0f, 0f, 50f, 50f);
		
		Circle intersectingCircle = new Circle(75f, 75f, 50f);
		Circle nonIntersectingCircle = new Circle(500f, 500f, 50f);
		
		Assert.assertEquals(true, rectangle1.intersects(intersectingCircle));
		Assert.assertEquals(false, rectangle1.intersects(nonIntersectingCircle));
	}

	@Test
	public void testIntersectsRectangle() {
		rectangle1 = new Rectangle(100f, 100f, 75f, 75f);
		rectangle2 = new Rectangle(50f, 50f, 100f, 100f);
		
		Assert.assertEquals(true, rectangle1.intersects(rectangle2));
		Assert.assertEquals(true, rectangle2.intersects(rectangle1));

		rectangle2 = new Rectangle(0f, 0f, 50f, 50f);

		Assert.assertEquals(false, rectangle1.intersects(rectangle2));
		Assert.assertEquals(false, rectangle2.intersects(rectangle1));
	}
	
	@Test
	public void testIntersectsSameRectangle() {
		rectangle1 = new Rectangle(0f, 0f, 32f, 32f);
		rectangle2 = new Rectangle(0f, 0f, 32f, 32f);

		Assert.assertEquals(true, rectangle1.intersects(rectangle2));
		Assert.assertEquals(true, rectangle2.intersects(rectangle1));
	}
	
	@Test
	public void testIntersectsRotatedRectangle() {
		rectangle1 = new Rectangle(100f, 100f, 50f, 50f);
		rectangle2 = new Rectangle(100f, 50f, 75f, 40f);
		
		Assert.assertEquals(false, rectangle1.intersects(rectangle2));
		Assert.assertEquals(false, rectangle2.intersects(rectangle1));

		rectangle2.rotate(45);
		
		Assert.assertEquals(true, rectangle1.intersects(rectangle2));
		Assert.assertEquals(true, rectangle2.intersects(rectangle1));
	}

	@Test
	public void testIntersectsLineWhenNotRotated() {
		rectangle1 = new Rectangle(100f, 100f, 50f, 50f);
		LineSegment line = new LineSegment(0, 100, 0, 200);

		for (float x = 0; x < rectangle1.getX(); x++) {
			line.setPointA(new Point(x, 100));
			line.setPointB(new Point(x, 200));
			Assert.assertEquals(false, rectangle1.intersects(line));
		}

		for (float y = 0; y < rectangle1.getY(); y++) {
			line.setPointA(new Point(100, y));
			line.setPointB(new Point(200, y));
			Assert.assertEquals(false, rectangle1.intersects(line));
		}

		for (float x = rectangle1.getX(); x <= rectangle1.getX()
				+ rectangle1.getWidth(); x++) {
			line.setPointA(new Point(x, 100));
			line.setPointB(new Point(x, 200));
			Assert.assertEquals(true, rectangle1.intersects(line));
		}

		for (float y = rectangle1.getY(); y <= rectangle1.getY()
				+ rectangle1.getHeight(); y++) {
			line.setPointA(new Point(100, y));
			line.setPointB(new Point(200, y));
			Assert.assertEquals(true, rectangle1.intersects(line));
		}

		for (float x = rectangle1.getX() + rectangle1.getWidth() + 1; x < (rectangle1
				.getX() + rectangle1.getWidth()) * 2; x++) {
			line.setPointA(new Point(x, 100));
			line.setPointB(new Point(x, 200));
			Assert.assertEquals(false, rectangle1.intersects(line));
		}

		for (float y = rectangle1.getY() + rectangle1.getHeight() + 1; y < (rectangle1
				.getY() + rectangle1.getHeight()) * 2; y++) {
			line.setPointA(new Point(100, y));
			line.setPointB(new Point(200, y));
			Assert.assertEquals(false, rectangle1.intersects(line));
		}
	}
}
