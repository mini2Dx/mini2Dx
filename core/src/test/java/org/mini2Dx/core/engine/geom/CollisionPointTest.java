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
package org.mini2Dx.core.engine.geom;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link CollisionCollisionPoint}
 * @author Thomas Cashman
 */
public class CollisionPointTest {
	private CollisionPoint point1, point2, point3;
	
	@Before
	public void setup() {
		point1 = new CollisionPoint();
		point2 = new CollisionPoint();
		point3 = new CollisionPoint();
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
		Assert.assertEquals(10f, point2.getY());
		
		point1.set(10, 0);
		point2.set(20, 0);
		
		point2.rotateAround(point1, 90f);
		Assert.assertEquals(10f, point2.getY());
	}
}
