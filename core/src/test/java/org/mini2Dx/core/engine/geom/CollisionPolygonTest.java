/**
 * Copyright (c) 2016 See AUTHORS file
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

import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.engine.PositionChangeListener;
import org.mini2Dx.core.engine.SizeChangeListener;

import com.badlogic.gdx.math.Vector2;

import junit.framework.Assert;

/**
 * Unit tests for {@link CollisionPolygon}
 */
public class CollisionPolygonTest implements PositionChangeListener<CollisionPolygon>, SizeChangeListener<CollisionPolygon> {
	private CollisionPolygon polygon1, polygon2;
	private int positionNotificationReceived, sizeNotificationReceived;
	
	@Before
	public void setup() {
		positionNotificationReceived = 0;
		sizeNotificationReceived = 0;
		
		polygon1 = new CollisionPolygon(new float [] { 0f, 0f,
				10f, 0f,
				10f, 10f,
				0f, 10f
				});
	}
	
	@Test
	public void testSet() {
		polygon1.addPostionChangeListener(this);
		polygon1.set(10f, 10f);
		Assert.assertEquals(1, positionNotificationReceived);
	}
	
	@Test
	public void testAddPoint() {
		polygon1.addPostionChangeListener(this);
		polygon1.addPoint(new Vector2(-5f, 5f));
		Assert.assertEquals(1, positionNotificationReceived);
	}

	@Override
	public void positionChanged(CollisionPolygon moved) {
		positionNotificationReceived++;
	}
	
	@Override
	public void sizeChanged(CollisionPolygon changed) {
		sizeNotificationReceived++;
	}
}