/**
 * Copyright (c) 2015 See AUTHORS file
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

import com.badlogic.gdx.math.Vector2;

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
