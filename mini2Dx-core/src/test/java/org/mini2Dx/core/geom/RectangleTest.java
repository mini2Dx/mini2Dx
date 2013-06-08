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

import junit.framework.Assert;

import org.junit.Test;

/**
 * Unit tests for {@link Rectangle}
 * 
 * @author Thomas Cashman
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
		Assert.assertEquals(0.5f, rectangle1.getCenterX());
		Assert.assertEquals(0.5f, rectangle1.getCenterY());
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
		Assert.assertEquals(125f, rectangle1.getCenterX());
		Assert.assertEquals(125f, rectangle1.getCenterY());
	}

	@Test
	public void testSetX() {
		rectangle1 = new Rectangle(100f, 100f, 50f, 50f);
		rectangle1.setX(200f);
		Assert.assertEquals(200f, rectangle1.getX());
		Assert.assertEquals(100f, rectangle1.getY());
		Assert.assertEquals(50f, rectangle1.getWidth());
		Assert.assertEquals(50f, rectangle1.getHeight());
		Assert.assertEquals(250f, rectangle1.getMaxX());
		Assert.assertEquals(150f, rectangle1.getMaxY());
		Assert.assertEquals(225f, rectangle1.getCenterX());
		Assert.assertEquals(125f, rectangle1.getCenterY());
	}

	@Test
	public void testSetY() {
		rectangle1 = new Rectangle(100f, 100f, 50f, 50f);
		rectangle1.setY(200f);
		Assert.assertEquals(100f, rectangle1.getX());
		Assert.assertEquals(200f, rectangle1.getY());
		Assert.assertEquals(50f, rectangle1.getWidth());
		Assert.assertEquals(50f, rectangle1.getHeight());
		Assert.assertEquals(150f, rectangle1.getMaxX());
		Assert.assertEquals(250f, rectangle1.getMaxY());
		Assert.assertEquals(125f, rectangle1.getCenterX());
		Assert.assertEquals(225f, rectangle1.getCenterY());
	}

	@Test
	public void testSetWidth() {
		rectangle1 = new Rectangle(100f, 100f, 50f, 50f);
		rectangle1.setWidth(100f);
		Assert.assertEquals(100f, rectangle1.getX());
		Assert.assertEquals(100f, rectangle1.getY());
		Assert.assertEquals(100f, rectangle1.getWidth());
		Assert.assertEquals(50f, rectangle1.getHeight());
		Assert.assertEquals(200f, rectangle1.getMaxX());
		Assert.assertEquals(150f, rectangle1.getMaxY());
		Assert.assertEquals(150f, rectangle1.getCenterX());
		Assert.assertEquals(125f, rectangle1.getCenterY());
	}

	@Test
	public void testSetHeight() {
		rectangle1 = new Rectangle(100f, 100f, 50f, 50f);
		rectangle1.setHeight(100f);
		Assert.assertEquals(100f, rectangle1.getX());
		Assert.assertEquals(100f, rectangle1.getY());
		Assert.assertEquals(50f, rectangle1.getWidth());
		Assert.assertEquals(100f, rectangle1.getHeight());
		Assert.assertEquals(150f, rectangle1.getMaxX());
		Assert.assertEquals(200f, rectangle1.getMaxY());
		Assert.assertEquals(125f, rectangle1.getCenterX());
		Assert.assertEquals(150f, rectangle1.getCenterY());
	}

	@Test
	public void testSetFloatFloatFloatFloat() {
		rectangle1 = new Rectangle(100f, 100f, 50f, 50f);
		rectangle1.set(0f, 0f, 50f, 50f);
		Assert.assertEquals(0f, rectangle1.getX());
		Assert.assertEquals(0f, rectangle1.getY());
		Assert.assertEquals(50f, rectangle1.getWidth());
		Assert.assertEquals(50f, rectangle1.getHeight());
		Assert.assertEquals(50f, rectangle1.getMaxX());
		Assert.assertEquals(50f, rectangle1.getMaxY());
		Assert.assertEquals(25f, rectangle1.getCenterX());
		Assert.assertEquals(25f, rectangle1.getCenterY());
	}

	@Test
	public void testIntersectsRectangle() {
		rectangle1 = new Rectangle(100f, 100f, 50f, 50f);
		rectangle2 = new Rectangle(50f, 50f, 100f, 100f);

		Assert.assertEquals(true, rectangle1.intersects(rectangle2));
		Assert.assertEquals(true, rectangle2.intersects(rectangle1));

		rectangle2 = new Rectangle(0f, 0f, 50f, 50f);

		Assert.assertEquals(false, rectangle1.intersects(rectangle2));
		Assert.assertEquals(false, rectangle2.intersects(rectangle1));
	}
	
	@Test
	public void testIntersectsLine() {
		rectangle1 = new Rectangle(100f, 100f, 50f, 50f);
		LineSegment line = new LineSegment(0, 100, 0, 200);
		
		for(float x = 0; x < rectangle1.getX(); x++) {
			line.setP1(new Point(x, 100));
			line.setP2(new Point(x, 200));
			Assert.assertEquals(false, rectangle1.intersects(line));
		}
		
		for(float y = 0; y < rectangle1.getY(); y++) {
			line.setP1(new Point(100, y));
			line.setP2(new Point(200, y));
			Assert.assertEquals(false, rectangle1.intersects(line));
		}
		
		for(float x = rectangle1.getX(); x <= rectangle1.getMaxX(); x++) {
			line.setP1(new Point(x, 100));
			line.setP2(new Point(x, 200));
			Assert.assertEquals(true, rectangle1.intersects(line));
		}
		
		for(float y = rectangle1.getY(); y <= rectangle1.getMaxY(); y++) {
			line.setP1(new Point(100, y));
			line.setP2(new Point(200, y));
			Assert.assertEquals(true, rectangle1.intersects(line));
		}
		
		for(float x = rectangle1.getMaxX() + 1; x < rectangle1.getMaxX() * 2; x++) {
			line.setP1(new Point(x, 100));
			line.setP2(new Point(x, 200));
			Assert.assertEquals(false, rectangle1.intersects(line));
		}
		
		for(float y = rectangle1.getMaxY() + 1; y < rectangle1.getMaxY() * 2; y++) {
			line.setP1(new Point(100, y));
			line.setP2(new Point(200, y));
			Assert.assertEquals(false, rectangle1.intersects(line));
		}
	}

	@Test
	public void testSetCenter() {
		rectangle1 = new Rectangle(0f, 0f, 50f, 50f);
		rectangle1.setCenter(50f, 50f);
		
		Assert.assertEquals(25f, rectangle1.getX());
		Assert.assertEquals(25f, rectangle1.getY());
		Assert.assertEquals(50f, rectangle1.getWidth());
		Assert.assertEquals(50f, rectangle1.getHeight());
		Assert.assertEquals(75f, rectangle1.getMaxX());
		Assert.assertEquals(75f, rectangle1.getMaxY());
		Assert.assertEquals(50f, rectangle1.getCenterX());
		Assert.assertEquals(50f, rectangle1.getCenterY());
	}
}
