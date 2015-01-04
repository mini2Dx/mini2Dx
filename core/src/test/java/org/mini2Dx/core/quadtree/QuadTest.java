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
import org.mini2Dx.core.geom.Point;
import org.mini2Dx.core.geom.Rectangle;

/**
 * Unit tests for {@link Quad}
 */
public class QuadTest {
	private Quad<Point> rootQuad;
	private Point point1, point2, point3, point4;

	@Before
	public void setup() {
		rootQuad = new Quad<Point>(2, 0, 0, 128, 128);
		point1 = new Point(0, 0);
		point2 = new Point(128, 0);
		point3 = new Point(0, 128);
		point4 = new Point(128, 128);
	}
	
	@Test
	public void testAdd() {
		Random random = new Random();
		for(int i = 0; i < 100; i++) {
			rootQuad.add(new Point(random.nextInt(128), random.nextInt(128)));
			Assert.assertEquals(i + 1, rootQuad.getElements().size());
		}
	}
	
	@Test
	public void testRemove() {
		Random random = new Random();
		List<Point> points = new ArrayList<Point>();
		for(int i = 0; i < 1000; i++) {
			points.add(new Point(random.nextInt(128), random.nextInt(128)));
		}
		
		for(int i = 0; i < points.size(); i++) {
			Assert.assertEquals(true, rootQuad.add(points.get(i)));
			Assert.assertEquals(i + 1, rootQuad.getElements().size());
		}
		
		for(int i = points.size() - 1; i >= 0 ; i--) {
			Assert.assertEquals(i + 1, rootQuad.getElements().size());
			rootQuad.remove(points.get(i));
			Assert.assertEquals(i, rootQuad.getElements().size());
		}
	}
	
	@Test
	public void testSubdivide() {
		rootQuad.add(point1);
		Assert.assertEquals(1, rootQuad.getElements().size());
		Assert.assertEquals(1, rootQuad.getTotalQuads());
		rootQuad.add(point2);
		Assert.assertEquals(2, rootQuad.getElements().size());
		Assert.assertEquals(1, rootQuad.getTotalQuads());
		rootQuad.add(point3);
		Assert.assertEquals(3, rootQuad.getElements().size());
		Assert.assertEquals(4, rootQuad.getTotalQuads());
		rootQuad.add(point4);
		Assert.assertEquals(4, rootQuad.getElements().size());
		Assert.assertEquals(4, rootQuad.getTotalQuads());
		rootQuad.add(new Point(32, 32));
		Assert.assertEquals(5, rootQuad.getElements().size());
		Assert.assertEquals(4, rootQuad.getTotalQuads());
		rootQuad.add(new Point(48, 48));
		Assert.assertEquals(6, rootQuad.getElements().size());
		Assert.assertEquals(7, rootQuad.getTotalQuads());
	}
	
	@Test
	public void testGetElementsWithinRegion() {
		rootQuad.add(point1);
		rootQuad.add(point2);
		rootQuad.add(point3);
		rootQuad.add(point4);
		
		List<Point> points = rootQuad.getElementsWithinRegion(new Rectangle(0, 0, 64, 64));
		Assert.assertEquals(1, points.size());
		Assert.assertEquals(point1, points.get(0));
		
		points = rootQuad.getElementsWithinRegion(new Rectangle(64, 0, 64, 64));
		Assert.assertEquals(1, points.size());
		Assert.assertEquals(point2, points.get(0));
		
		points = rootQuad.getElementsWithinRegion(new Rectangle(0, 64, 64, 64));
		Assert.assertEquals(1, points.size());
		Assert.assertEquals(point3, points.get(0));
		
		points = rootQuad.getElementsWithinRegion(new Rectangle(64, 64, 64, 64));
		Assert.assertEquals(1, points.size());
		Assert.assertEquals(point4, points.get(0));
		
		Point point5 = new Point(32, 32);
		Point point6 = new Point(48, 48);
		rootQuad.add(point5);
		rootQuad.add(point6);
		
		points = rootQuad.getElementsWithinRegion(new Rectangle(0, 0, 64, 64));
		Assert.assertEquals(3, points.size());
		Assert.assertEquals(true, points.contains(point1));
		Assert.assertEquals(true, points.contains(point5));
		Assert.assertEquals(true, points.contains(point6));
	}
	
	@Test
	public void testGetElementsIntersectingLineSegment() {
		rootQuad.add(point1);
		rootQuad.add(point2);
		rootQuad.add(point3);
		rootQuad.add(point4);
		
		List<Point> points = rootQuad.getElementsIntersectingLineSegment(new LineSegment(0, 0, 128, 128));
		Assert.assertEquals(true, points.contains(point1));
		Assert.assertEquals(false, points.contains(point2));
		Assert.assertEquals(false, points.contains(point3));
		Assert.assertEquals(true, points.contains(point4));
	}
}
