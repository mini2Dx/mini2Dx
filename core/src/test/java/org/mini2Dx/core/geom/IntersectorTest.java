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

import junit.framework.Assert;

import org.junit.Test;
import org.mini2Dx.gdx.math.Vector2;

/**
 * Implements unit tests for {@link Intersector}
 */
public class IntersectorTest {

	@Test
	public void testIntersectLinesWithNonIntersectingLines() {
		Vector2 line1Start = new Vector2(0, 0);
		Vector2 line1End = new Vector2(0, 32);
		
		Vector2 line2Start = new Vector2(32, 0);
		Vector2 line2End = new Vector2(32, 32);
		
		Assert.assertEquals(false, Intersector.intersectLines(line1Start, line1End, line2Start, line2End, new Vector2()));
	}
	
	@Test
	public void testIntersectLinesWithIntersectingLines() {
		Vector2 line1Start = new Vector2(0, 0);
		Vector2 line1End = new Vector2(0, 32);
		
		Vector2 line2Start = new Vector2(-32, 16);
		Vector2 line2End = new Vector2(32, 16);
		
		Assert.assertEquals(true, Intersector.intersectLines(line1Start, line1End, line2Start, line2End, new Vector2()));
		
		
		line1Start = new Vector2(100, 100);
		line1End = new Vector2(150, 100);
		
		line2Start = new Vector2(50, 100);
		line2End = new Vector2(50, 200);
		
		Assert.assertEquals(true, Intersector.intersectLines(line1Start, line1End, line2Start, line2End, new Vector2()));
	}
}
