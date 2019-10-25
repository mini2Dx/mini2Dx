/*******************************************************************************
 * Copyright 2019 See AUTHORS file
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.mini2Dx.core.geom;

import org.junit.Assert;
import org.junit.Test;
import org.mini2Dx.core.Geometry;

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
		
		Assert.assertEquals(15f, segment1.getPointA().x, 0f);
		Assert.assertEquals(17f, segment1.getPointA().y, 0f);
		Assert.assertEquals(25f, segment1.getPointB().x, 0f);
		Assert.assertEquals(27f, segment1.getPointB().y, 0f);
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

	@Test
	public void testDispose() {
		Geometry.DEFAULT_POOL_SIZE = 1;
		Geometry geometry = new Geometry();
		org.junit.Assert.assertEquals(1, geometry.getTotalLineSegmentsAvailable());
		segment1 = geometry.lineSegment();
		org.junit.Assert.assertEquals(0, geometry.getTotalLineSegmentsAvailable());

		segment1.dispose();
		org.junit.Assert.assertEquals(1, geometry.getTotalLineSegmentsAvailable());
		segment1.dispose();
		org.junit.Assert.assertEquals(1, geometry.getTotalLineSegmentsAvailable());

		//Test re-allocate and re-dispose
		segment1 = geometry.lineSegment();
		org.junit.Assert.assertEquals(0, geometry.getTotalLineSegmentsAvailable());
		segment1.dispose();
		org.junit.Assert.assertEquals(1, geometry.getTotalLineSegmentsAvailable());
	}
}
