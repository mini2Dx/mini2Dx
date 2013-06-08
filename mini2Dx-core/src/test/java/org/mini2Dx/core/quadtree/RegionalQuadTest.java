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
package org.mini2Dx.core.quadtree;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Rectangle;

/**
 * Unit tests for {@link RegionalQuad}
 * 
 * @author Thomas Cashman
 */
public class RegionalQuadTest implements Quad<Rectangle> {
	private static final int ELEMENT_LIMIT = 2;
	private RegionalQuad<Rectangle> quad;

	private Rectangle rectangle1, rectangle2, rectangle3;

	@Before
	public void setup() {
		quad = new RegionalQuad<Rectangle>(this, 0, 0, 32, 32);

		rectangle1 = new Rectangle(0, 0, 1, 1);
		rectangle2 = new Rectangle(0, 30, 1, 1);
		rectangle3 = new Rectangle(30, 0, 1, 1);
	}

	@Test
	public void testAddValue() {
		quad.add(rectangle1);
		Assert.assertEquals(1, quad.getNumberOfElements());

		quad.add(rectangle2);
		Assert.assertEquals(2, quad.getNumberOfElements());

		quad.add(rectangle3);
		Assert.assertEquals(3, quad.getNumberOfElements());

		List<Quad<Rectangle>> rect1Quads = quad.getQuadsFor(rectangle1);
		List<Quad<Rectangle>> rect2Quads = quad.getQuadsFor(rectangle2);
		List<Quad<Rectangle>> rect3Quads = quad.getQuadsFor(rectangle3);

		Assert.assertEquals(1, rect1Quads.size());
		Assert.assertEquals(1, rect2Quads.size());
		Assert.assertEquals(1, rect3Quads.size());
		assertNotSame(rect1Quads, rect2Quads);
		assertNotSame(rect2Quads, rect3Quads);
		assertNotSame(rect3Quads, rect1Quads);
	}

	@Test
	public void testRemoveValue() {
		testAddValue();

		List<Quad<Rectangle>> rect1QuadsBefore = quad.getQuadsFor(rectangle1);

		quad.remove(rectangle3);
		Assert.assertEquals(2, quad.getNumberOfElements());

		quad.remove(rectangle2);
		Assert.assertEquals(1, quad.getNumberOfElements());

		List<Quad<Rectangle>> rect1Quads = quad.getQuadsFor(rectangle1);
		assertNotSame(rect1Quads, rect1QuadsBefore);

		quad.add(rectangle2);
		Assert.assertEquals(2, quad.getNumberOfElements());

		List<Quad<Rectangle>> rect2Quads = quad.getQuadsFor(rectangle2);
		Assert.assertEquals(rect1Quads, rect2Quads);
	}

	@Test
	public void testGetIntersectionsForVerticalLines() {
		testAddValue();

		for (int x = 0; x < 32; x++) {
			LineSegment line = new LineSegment(x, -2, x, 40);
			List<Rectangle> result = quad.getIntersectionsFor(line);

			int expectedCount = 0;
			if (rectangle1.intersects(line)) {
				expectedCount++;
			}
			if (rectangle2.intersects(line)) {
				expectedCount++;
			}
			if (rectangle3.intersects(line)) {
				expectedCount++;
			}
			Assert.assertEquals(expectedCount, result.size());
		}
	}

	@Test
	public void testGetIntersectionsForHorizontalLines() {
		testAddValue();

		for (int y = 0; y < 32; y++) {
			LineSegment line = new LineSegment(-2, y, 40, y);
			List<Rectangle> result = quad.getIntersectionsFor(line);

			int expectedCount = 0;
			if (rectangle1.intersects(line)) {
				expectedCount++;
			}
			if (rectangle2.intersects(line)) {
				expectedCount++;
			}
			if (rectangle3.intersects(line)) {
				expectedCount++;
			}
			Assert.assertEquals(expectedCount, result.size());
		}
	}

	@Test
	public void testRectangleMovesQuadOnPositionChange() {
		testAddValue();

		List<Quad<Rectangle>> initalRect1Quads = quad.getQuadsFor(rectangle1);
		List<Quad<Rectangle>> initalRect2Quads = quad.getQuadsFor(rectangle2);
		
		/* Move beside rectangle2 */
		rectangle1.setY(30);
		
		List<Quad<Rectangle>> rect1Quads = quad.getQuadsFor(rectangle1);
		List<Quad<Rectangle>> rect2Quads = quad.getQuadsFor(rectangle2);
		
		assertNotSame(initalRect1Quads, rect1Quads);
		assertNotSame(initalRect2Quads, rect2Quads);
		assertSame(rect1Quads, rect2Quads);
	}

	@Test
	public void testGetParent() {
		Assert.assertEquals(this, quad.getParent());
	}

	@Test
	public void testGetElementLimit() {
		Assert.assertEquals(ELEMENT_LIMIT, quad.getElementLimit());
	}

	@Override
	public int getElementLimit() {
		return ELEMENT_LIMIT;
	}

	@Override
	public int getNumberOfElements() {
		return quad.getNumberOfElements();
	}

	@Override
	public Quad<Rectangle> getParent() {
		return null;
	}

	@Override
	public void add(Rectangle object) {
		quad.add(object);
	}

	@Override
	public void remove(Rectangle object) {
		quad.remove(object);
	}

	@Override
	public List<Rectangle> getValues() {
		return quad.getValues();
	}

	@Override
	public void positionChanged(Rectangle moved) {

	}

	private void assertNotSame(List<Quad<Rectangle>> list1,
			List<Quad<Rectangle>> list2) {
		if (list1.size() == list2.size()) {
			for (int i = 0; i < list1.size(); i++) {
				Assert.assertNotSame(list1.get(i).getValues(), list2.get(i)
						.getValues());
			}
		}
	}
	
	private void assertSame(List<Quad<Rectangle>> list1,
			List<Quad<Rectangle>> list2) {
		Assert.assertEquals(list1.size(), list2.size());
		for (int i = 0; i < list1.size(); i++) {
			Assert.assertEquals(list1.get(i).getValues(), list2.get(i)
					.getValues());
		}
	}
}
