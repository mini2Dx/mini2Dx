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

import org.junit.Test;

import junit.framework.Assert;
import org.mini2Dx.gdx.math.MathUtils;

/**
 * Unit tests for {@link RegularHexagon}
 */
public class RegularHexagonTest {
	@Test
	public void testRotationalSymmetry() {
		RegularHexagon hexagon = new RegularHexagon(0f, 0f, 100f);
		
		Assert.assertEquals(0, MathUtils.round(hexagon.getX(0)));
		Assert.assertEquals(-100, MathUtils.round(hexagon.getY(0)));
		
		Assert.assertEquals(87, MathUtils.round(hexagon.getX(1)));
		Assert.assertEquals(-50, MathUtils.round(hexagon.getY(1)));
		
		Assert.assertEquals(87, MathUtils.round(hexagon.getX(2)));
		Assert.assertEquals(50, MathUtils.round(hexagon.getY(2)));
		
		Assert.assertEquals(0, MathUtils.round(hexagon.getX(3)));
		Assert.assertEquals(100, MathUtils.round(hexagon.getY(3)));
		
		Assert.assertEquals(-87, MathUtils.round(hexagon.getX(4)));
		Assert.assertEquals(50, MathUtils.round(hexagon.getY(4)));
		
		Assert.assertEquals(-87, MathUtils.round(hexagon.getX(5)));
		Assert.assertEquals(-50, MathUtils.round(hexagon.getY(5)));
	}
	
	@Test
	public void testNumberOfSides() {
		RegularHexagon hexagon = new RegularHexagon(0f, 0f, 100f);
		Assert.assertEquals(6, hexagon.getNumberOfSides());
	}
}
