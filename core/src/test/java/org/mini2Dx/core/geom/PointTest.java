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
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.Geometry;

/**
 * Implements unit tests for {@link Point}
 */
public class PointTest {
	private Point point1, point2, point3;
	
	@Before
	public void setup() {
		point1 = new Point();
		point2 = new Point();
		point3 = new Point();
	}
	
	@Test
	public void testEquals() {
		point1.set(0f, 0f);
		point2.set(0f, 0f);
		
		Assert.assertEquals(true, point1.equals(point2));
		
		point2.set(0.1f, 0f);
		Assert.assertEquals(false, point1.equals(point2));
		
		point2.set(0f, 0.1f);
		Assert.assertEquals(false, point1.equals(point2));
		
		point2.set(0.1f, 0.1f);
		Assert.assertEquals(false, point1.equals(point2));
		
		point2.set(1f, 1f);
		Assert.assertEquals(false, point1.equals(point2));
	}
	
	@Test
	public void testEqualsWithDelta() {
		point1.set(0f, 0f);
		point2.set(0f, 0f);
		
		Assert.assertEquals(true, point1.equals(point2, 0.1f));
		
		point2.set(0.1f, 0f);
		Assert.assertEquals(true, point1.equals(point2, 0.1f));
		
		point2.set(0.11f, 0f);
		Assert.assertEquals(false, point1.equals(point2, 0.1f));
		
		point2.set(0f, 0.1f);
		Assert.assertEquals(true, point1.equals(point2, 0.1f));
		
		point2.set(0f, 0.11f);
		Assert.assertEquals(false, point1.equals(point2, 0.1f));
		
		point2.set(0.1f, 0.1f);
		Assert.assertEquals(true, point1.equals(point2, 0.1f));
		
		point2.set(0.11f, 0.11f);
		Assert.assertEquals(false, point1.equals(point2, 0.1f));
		
		point2.set(1f, 1f);
		Assert.assertEquals(false, point1.equals(point2, 0.1f));
	}
	
	@Test
	public void testInBetween() {
		point1.set(0, 0);
		point2.set(10, 0);
		point3.set(5, 0);
		
		Assert.assertEquals(true, point3.isOnLineBetween(point1, point2));
		Assert.assertEquals(false, point1.isOnLineBetween(point2, point3));
		Assert.assertEquals(false, point2.isOnLineBetween(point3, point1));
		
		point1.set(0, 0);
		point2.set(0, 10);
		point3.set(0, 5);
		
		Assert.assertEquals(true, point3.isOnLineBetween(point1, point2));
		Assert.assertEquals(false, point1.isOnLineBetween(point2, point3));
		Assert.assertEquals(false, point2.isOnLineBetween(point3, point1));
	}
	
	@Test
	public void testRotateAround() {
		point1.set(0, 0);
		point2.set(10, 0);
		
		point2.rotateAround(point1, 90f);
		Assert.assertEquals(10f, point2.getY(), 0.001f);
		
		point1.set(10, 0);
		point2.set(20, 0);
		
		point2.rotateAround(point1, 90f);
		Assert.assertEquals(10f, point2.getY(), 0.001f);
	}

	@Test
	public void testDispose() {
		Geometry.DEFAULT_POOL_SIZE = 1;
		Geometry geometry = new Geometry();
		org.junit.Assert.assertEquals(1, geometry.getTotalPointsAvailable());
		point1 = geometry.point();
		org.junit.Assert.assertEquals(0, geometry.getTotalPointsAvailable());

		point1.dispose();
		org.junit.Assert.assertEquals(1, geometry.getTotalPointsAvailable());
		point1.dispose();
		org.junit.Assert.assertEquals(1, geometry.getTotalPointsAvailable());

		//Test re-allocate and re-dispose
		point1 = geometry.point();
		org.junit.Assert.assertEquals(0, geometry.getTotalPointsAvailable());
		point1.dispose();
		org.junit.Assert.assertEquals(1, geometry.getTotalPointsAvailable());
	}
}
