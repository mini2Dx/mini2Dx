/**
 * Copyright (c) 2013, mini2Dx Project
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

import org.junit.Before;
import org.junit.Test;

/**
 * Implements unit tests for {@link Point}
 * @author Thomas Cashman
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
	public void testInBetween() {
		point1.set(0, 0);
		point2.set(10, 0);
		point3.set(5, 0);
		
		Assert.assertEquals(true, point3.isBetween(point1, point2));
		Assert.assertEquals(false, point1.isBetween(point2, point3));
		Assert.assertEquals(false, point2.isBetween(point3, point1));
		
		point1.set(0, 0);
		point2.set(0, 10);
		point3.set(0, 5);
		
		Assert.assertEquals(true, point3.isBetween(point1, point2));
		Assert.assertEquals(false, point1.isBetween(point2, point3));
		Assert.assertEquals(false, point2.isBetween(point3, point1));
	}
}
