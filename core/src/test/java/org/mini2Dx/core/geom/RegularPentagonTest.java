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
import org.mini2Dx.gdx.math.MathUtils;

/**
 * Unit tests for {@link RegularPentagon}
 */
public class RegularPentagonTest {
	@Test
	public void testRotationalSymmetry() {
		RegularPentagon pentagon = new RegularPentagon(0f, 0f, 100f);
		
		Assert.assertEquals(0, MathUtils.round(pentagon.getX(0)));
		Assert.assertEquals(-100, MathUtils.round(pentagon.getY(0)));
		
		Assert.assertEquals(95, MathUtils.round(pentagon.getX(1)));
		Assert.assertEquals(-31, MathUtils.round(pentagon.getY(1)));
		
		Assert.assertEquals(59, MathUtils.round(pentagon.getX(2)));
		Assert.assertEquals(81, MathUtils.round(pentagon.getY(2)));
		
		Assert.assertEquals(-59, MathUtils.round(pentagon.getX(3)));
		Assert.assertEquals(81, MathUtils.round(pentagon.getY(3)));
		
		Assert.assertEquals(-95, MathUtils.round(pentagon.getX(4)));
		Assert.assertEquals(-31, MathUtils.round(pentagon.getY(4)));
	}
	
	@Test
	public void testNumberOfSides() {
		RegularPentagon pentagon = new RegularPentagon(0f, 0f, 100f);
		Assert.assertEquals(5, pentagon.getNumberOfSides());
	}

	@Test
	public void testDispose() {
		Geometry.DEFAULT_POOL_SIZE = 1;
		Geometry geometry = new Geometry();
		org.junit.Assert.assertEquals(1, geometry.getTotalRegularPentagonsAvailable());
		RegularPentagon pentagon = geometry.regularPentagon();
		org.junit.Assert.assertEquals(0, geometry.getTotalRegularPentagonsAvailable());

		pentagon.dispose();
		org.junit.Assert.assertEquals(1, geometry.getTotalRegularPentagonsAvailable());
		pentagon.dispose();
		org.junit.Assert.assertEquals(1, geometry.getTotalRegularPentagonsAvailable());

		//Test re-allocate and re-dispose
		pentagon = geometry.regularPentagon();
		org.junit.Assert.assertEquals(0, geometry.getTotalRegularPentagonsAvailable());
		pentagon.dispose();
		org.junit.Assert.assertEquals(1, geometry.getTotalRegularPentagonsAvailable());
	}
}
