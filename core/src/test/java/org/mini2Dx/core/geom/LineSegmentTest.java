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
package org.mini2Dx.core.geom;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Unit tests for {@link LineSegment}
 */
public class LineSegmentTest {
	private LineSegment segment1, segment2;
	
	@Test
	public void testConstructor() {
		segment1 = new LineSegment(0, 0, 10, 10);
	}

	@Test
	public void testSet() {
		segment1 = new LineSegment(0, 0, 10, 10);
		
		segment1.set(15, 17, 25, 27);
		
		Assert.assertEquals(15f, segment1.getPointA().x);
		Assert.assertEquals(17f, segment1.getPointA().y);
		Assert.assertEquals(25f, segment1.getPointB().x);
		Assert.assertEquals(27f, segment1.getPointB().y);
	}

	@Test
	public void testContains() {
		segment1  = new LineSegment(0, 0, 10, 0);
		
		Assert.assertEquals(true, segment1.contains(0, 0));
		Assert.assertEquals(true, segment1.contains(10, 0));
		Assert.assertEquals(true, segment1.contains(5, 0));
		
		Assert.assertEquals(false, segment1.contains(5, -5));
		Assert.assertEquals(false, segment1.contains(5, 5));
		Assert.assertEquals(false, segment1.contains(-5, -5));
		Assert.assertEquals(false, segment1.contains(15, -5));
		Assert.assertEquals(false, segment1.contains(-5, 5));
		Assert.assertEquals(false, segment1.contains(15, 5));
	}

	@Test
	public void testIntersectsLineSegment() {
		segment1  = new LineSegment(0, 0, 10, 0);
		segment2  = new LineSegment(5, -5, 5, 5);
		
		Assert.assertEquals(true, segment1.intersects(segment2));
		Assert.assertEquals(true, segment2.intersects(segment1));
		
		segment2  = new LineSegment(15, -5, 15, 5);
		
		Assert.assertEquals(false, segment1.intersects(segment2));
		Assert.assertEquals(false, segment2.intersects(segment1));
	}
}
