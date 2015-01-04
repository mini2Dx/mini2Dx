/**
 * Copyright (c) 2015, mini2Dx Project
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Rectangle;

/**
 * Unit tests for {@link RegionQuad}
 */
public class RegionQuadTest {
	private RegionQuad<Rectangle> rootQuad;
	private Rectangle rectangle1, rectangle2, rectangle3, rectangle4;
	
	@Before
	public void setup() {
		rootQuad = new RegionQuad<Rectangle>(2, 0, 0, 128, 128);
		
		rectangle1 = new Rectangle(0, 0, 32, 32);
		rectangle2 = new Rectangle(96, 0, 32, 32);
		rectangle3 = new Rectangle(0, 96, 32, 32);
		rectangle4 = new Rectangle(96, 96, 32, 32);
	}
	
	@Test
	public void testAdd() {
		Random random = new Random();
		for(int i = 0; i < 100; i++) {
			Rectangle rect = new Rectangle(random.nextInt(96), random.nextInt(96), 32f, 32f);
			Assert.assertEquals(true, rootQuad.add(rect));
			Assert.assertEquals(i + 1, rootQuad.getElements().size());
		}
	}
	
	@Test
	public void testRemove() {
		Random random = new Random();
		List<Rectangle> rectangles = new ArrayList<Rectangle>();
		for(int i = 0; i < 1000; i++) {
			rectangles.add(new Rectangle(random.nextInt(96), random.nextInt(96), random.nextInt(32), random.nextInt(32)));
		}
		
		for(int i = 0; i < rectangles.size(); i++) {
			rootQuad.add(rectangles.get(i));
			Assert.assertEquals(i + 1, rootQuad.getElements().size());
		}
		
		for(int i = rectangles.size() - 1; i >= 0 ; i--) {
			Assert.assertEquals(i + 1, rootQuad.getElements().size());
			rootQuad.remove(rectangles.get(i));
			Assert.assertEquals(i, rootQuad.getElements().size());
		}
	}
	
	@Test
	public void testSubdivide() {
		rootQuad.add(rectangle1);
		Assert.assertEquals(1, rootQuad.getElements().size());
		Assert.assertEquals(1, rootQuad.getTotalQuads());
		
		rootQuad.add(rectangle2);
		Assert.assertEquals(2, rootQuad.getElements().size());
		Assert.assertEquals(1, rootQuad.getTotalQuads());
		
		rootQuad.add(rectangle3);
		Assert.assertEquals(3, rootQuad.getElements().size());
		Assert.assertEquals(4, rootQuad.getTotalQuads());
		
		rootQuad.add(rectangle4);
		Assert.assertEquals(4, rootQuad.getElements().size());
		Assert.assertEquals(4, rootQuad.getTotalQuads());
		
		rootQuad.add(new Rectangle(24, 24, 2, 2));
		Assert.assertEquals(5, rootQuad.getElements().size());
		Assert.assertEquals(4, rootQuad.getTotalQuads());
		
		rootQuad.add(new Rectangle(48, 48, 32, 32));
		Assert.assertEquals(6, rootQuad.getElements().size());
		Assert.assertEquals(4, rootQuad.getTotalQuads());
		
		rootQuad.add(new Rectangle(12, 48, 8, 8));
		Assert.assertEquals(7, rootQuad.getElements().size());
		Assert.assertEquals(7, rootQuad.getTotalQuads());
	}
	
	@Test
	public void testGetElementsWithinRegion() {
		rootQuad.add(rectangle1);
		rootQuad.add(rectangle2);
		rootQuad.add(rectangle3);
		rootQuad.add(rectangle4);
		
		List<Rectangle> rectangles = rootQuad.getElementsWithinRegion(new Rectangle(48, 48, 32, 32));
		Assert.assertEquals(0, rectangles.size());
		
		Rectangle rectangle5 = new Rectangle(24, 24, 2, 2);
		Rectangle rectangle6 = new Rectangle(48, 48, 32, 32);
		Rectangle rectangle7 = new Rectangle(12, 48, 8, 8);
		
		rootQuad.add(rectangle5);
		rootQuad.add(rectangle6);
		rootQuad.add(rectangle7);
		
		rectangles = rootQuad.getElementsWithinRegion(new Rectangle(0, 0, 128, 128));
		Assert.assertEquals(rootQuad.getElements().size(), rectangles.size());
		
		rectangles = rootQuad.getElementsWithinRegion(new Rectangle(33, 33, 32, 32));
		Assert.assertEquals(1, rectangles.size());
		Assert.assertEquals(rectangle6, rectangles.get(0));
		
		rectangles = rootQuad.getElementsWithinRegion(new Rectangle(0, 0, 64, 64));
		Assert.assertEquals(4, rectangles.size());
		Assert.assertEquals(true, rectangles.contains(rectangle1));
		Assert.assertEquals(true, rectangles.contains(rectangle5));
		Assert.assertEquals(true, rectangles.contains(rectangle6));
		Assert.assertEquals(true, rectangles.contains(rectangle7));
		
		rectangles = rootQuad.getElementsWithinRegion(new Rectangle(16, 16, 24, 24));
		Assert.assertEquals(2, rectangles.size());
		Assert.assertEquals(true, rectangles.contains(rectangle1));
		Assert.assertEquals(true, rectangles.contains(rectangle5));
		
		rectangles = rootQuad.getElementsWithinRegion(new Rectangle(12, 40, 48, 8));
		Assert.assertEquals(2, rectangles.size());
		Assert.assertEquals(true, rectangles.contains(rectangle6));
		Assert.assertEquals(true, rectangles.contains(rectangle7));
	}
	
	@Test
	public void testGetElementsIntersectingLineSegment() {
		rootQuad.add(rectangle1);
		rootQuad.add(rectangle2);
		rootQuad.add(rectangle3);
		rootQuad.add(rectangle4);
		
		List<Rectangle> rectangles = rootQuad.getElementsIntersectingLineSegment(new LineSegment(0,  0, 128, 128));
		Assert.assertEquals(2, rectangles.size());
		Assert.assertEquals(true, rectangles.contains(rectangle1));
		Assert.assertEquals(true, rectangles.contains(rectangle4));
		
		Rectangle rectangle5 = new Rectangle(24, 24, 2, 2);
		Rectangle rectangle6 = new Rectangle(48, 48, 32, 32);
		Rectangle rectangle7 = new Rectangle(12, 48, 8, 8);
		
		rootQuad.add(rectangle5);
		rootQuad.add(rectangle6);
		rootQuad.add(rectangle7);
		
		rectangles = rootQuad.getElementsIntersectingLineSegment(new LineSegment(0,  0, 128, 128));
		Assert.assertEquals(4, rectangles.size());
		Assert.assertEquals(true, rectangles.contains(rectangle1));
		Assert.assertEquals(true, rectangles.contains(rectangle4));
		Assert.assertEquals(true, rectangles.contains(rectangle5));
		Assert.assertEquals(true, rectangles.contains(rectangle6));
	}
}
