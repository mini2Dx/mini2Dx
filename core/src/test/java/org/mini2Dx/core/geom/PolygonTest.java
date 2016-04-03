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

import org.junit.Test;

import junit.framework.Assert;

/**
 * Unit tests for {@link Polygon}
 */
public class PolygonTest {
	@Test
	public void testAddPoint() {
		Polygon polygon = new Polygon(new Point [] { new Point(0f, 0f),
				new Point(5f, 5f), new Point(2.5f, 10f) });
		polygon.addPoint(new Point(-2.5f, 10f));
		polygon.addPoint(new Point(-5f, 5f));
		Assert.assertEquals(0f, polygon.getX());
		Assert.assertEquals(0f, polygon.getY());
		Assert.assertEquals(5f, polygon.getMaxX());
		Assert.assertEquals(10f, polygon.getMaxY());
	}

	@Test
	public void testRemovePoint() {
		Point maxX = new Point(5f, 5f);
		Point maxY1 = new Point(2.5f, 10f);
		Point maxY2 = new Point(-2.5f, 10f);
		Polygon polygon = new Polygon(new Point[] { new Point(0f, 0f), maxX, maxY1, 
				maxY2, new Point(-5f, 5f) });
		Assert.assertEquals(5f, polygon.getMaxX());
		Assert.assertEquals(10f, polygon.getMaxY());

		polygon.removePoint(maxX);
		Assert.assertEquals(2.5f, polygon.getMaxX());
		Assert.assertEquals(10f, polygon.getMaxY());

		polygon.addPoint(maxX);
		polygon.removePoint(maxY1);
		polygon.removePoint(maxY2);
		Assert.assertEquals(5f, polygon.getMaxX());
		Assert.assertEquals(5f, polygon.getMaxY());
	}
	
	@Test
	public void testTranslate() {
		Polygon polygon = new Polygon(new Point [] {
			new Point(0f, 0f),
			new Point(10f, 0f),
			new Point(10f, 10f),
			new Point(0f, 10f)
		});
		polygon.translate(10f, 0f);
		Assert.assertEquals(10f, polygon.getX(0));
		Assert.assertEquals(0f, polygon.getY(0));
		Assert.assertEquals(20f, polygon.getX(1));
		Assert.assertEquals(0f, polygon.getY(1));
		Assert.assertEquals(20f, polygon.getX(2));
		Assert.assertEquals(10f, polygon.getY(2));
		Assert.assertEquals(10f, polygon.getX(3));
		Assert.assertEquals(10f, polygon.getY(3));
	}
	
	@Test
	public void testTranslateAndAddPoint() {
		Point [] initialPoints = new Point [] {
				new Point(0f, 0f),
				new Point(10f, 0f),
				new Point(10f, 10f),
				new Point(5f, 10f)
		};
		
		Polygon polygon = new Polygon(initialPoints);
		polygon.translate(10f, 0f);
		polygon.addPoint(new Point(10f, 10f));
		Assert.assertEquals(10f, polygon.getX(0));
		Assert.assertEquals(0f, polygon.getY(0));
		Assert.assertEquals(20f, polygon.getX(1));
		Assert.assertEquals(0f, polygon.getY(1));
		Assert.assertEquals(20f, polygon.getX(2));
		Assert.assertEquals(10f, polygon.getY(2));
		Assert.assertEquals(15f, polygon.getX(3));
		Assert.assertEquals(10f, polygon.getY(3));
		Assert.assertEquals(10f, polygon.getX(4));
		Assert.assertEquals(10f, polygon.getY(4));
	}
	
	@Test
	public void testTranslateAndRemovePoint() {
		Point [] initialPoints = new Point [] {
				new Point(0f, 0f),
				new Point(10f, 0f),
				new Point(10f, 10f),
				new Point(5f, 10f),
				new Point(0f, 10f)
		};
		
		Polygon polygon = new Polygon(initialPoints);
		polygon.translate(10f, 0f);
		polygon.removePoint(15f, 10f);
		Assert.assertEquals(10f, polygon.getX(0));
		Assert.assertEquals(0f, polygon.getY(0));
		Assert.assertEquals(20f, polygon.getX(1));
		Assert.assertEquals(0f, polygon.getY(1));
		Assert.assertEquals(20f, polygon.getX(2));
		Assert.assertEquals(10f, polygon.getY(2));
		Assert.assertEquals(10f, polygon.getX(3));
		Assert.assertEquals(10f, polygon.getY(3));
	}
}
