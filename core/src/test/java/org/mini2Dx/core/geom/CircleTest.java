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

import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.engine.PositionChangeListener;

/**
 * Unit tests for {@link Circle}
 */
public class CircleTest {
	private Circle circle;

	@Before
	public void setup() {
		circle = new Circle(4);
	}

	@Test
	public void testContains() {
		for (float x = -5f; x <= 5f; x += 0.1f) {
			for (float y = -5f; y <= 5f; y += 0.1f) {
				Assert.assertEquals(circle.getDistanceFromCenter(x, y) <= circle.getRadius(), circle.contains(x, y));
			}
		}
	}

	@Test
	public void testIntersectsCircle() {
		Circle circle2 = new Circle(20f, 20f, 4);
		Assert.assertEquals(false, circle.intersects(circle2));
		Assert.assertEquals(false, circle2.intersects(circle));

		circle2.setCenter(5f, 0f);
		Assert.assertEquals(true, circle.intersects(circle2));
		Assert.assertEquals(true, circle2.intersects(circle));
	}

	@Test
	public void testGetDistanceToPositionable() {
		Point point = new Point(3f, 0f);
		Assert.assertEquals(0f, circle.getDistanceTo(point));

		point.set(5f, 0f);
		Assert.assertEquals(1f, circle.getDistanceTo(point));
	}

	@Test
	public void testSetCenter() {
		circle.setCenter(20f, 25f);

		Assert.assertEquals(20f, circle.getX());
		Assert.assertEquals(25f, circle.getY());
	}

	@Test
	public void testSetX() {
		circle.setX(25f);

		Assert.assertEquals(25f, circle.getX());
		Assert.assertEquals(0f, circle.getY());
	}

	@Test
	public void testSetY() {
		circle.setY(25f);

		Assert.assertEquals(0f, circle.getX());
		Assert.assertEquals(25f, circle.getY());
	}

}
