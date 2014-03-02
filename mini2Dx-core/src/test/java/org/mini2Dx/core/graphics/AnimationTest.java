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
package org.mini2Dx.core.graphics;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Unit tests for {@link Animation}
 * 
 * @author Thomas Cashman
 */
public class AnimationTest {
	private Animation<Sprite> animation;
	private Mockery mockery;
	
	private Sprite sprite;

	@Before
	public void setup() {
		mockery = new Mockery();
		mockery.setImposteriser(ClassImposteriser.INSTANCE);
		sprite = mockery.mock(Sprite.class);
		mockery.checking(new Expectations(){
			{
				exactly(5).of(sprite).getOriginX();
				will(returnValue(1f));
				exactly(5).of(sprite).getOriginY();
				will(returnValue(1f));
			}
		});
		
		animation = new Animation<Sprite>();

		for (int i = 0; i < 5; i++) {
			animation.addFrame(sprite, 0.5f);
		}
		
		Assert.assertEquals(0, animation.getCurrentFrameIndex());
		Assert.assertEquals(5, animation.getNumberOfFrames());
	}
	
	@After
	public void teardown() {
		mockery.assertIsSatisfied();
	}

	@Test
	public void testUpdateByLessThanOneFrame() {		
		Assert.assertEquals(0, animation.getCurrentFrameIndex());
		animation.update(0.1f);
		Assert.assertEquals(0, animation.getCurrentFrameIndex());
	}

	@Test
	public void testUpdateByOneFrame() {
		Assert.assertEquals(0, animation.getCurrentFrameIndex());
		animation.update(0.5f);
		Assert.assertEquals(1, animation.getCurrentFrameIndex());
	}
	
	@Test
	public void testUpdateByMultipleFramesNoLooping() {
		animation.setLooping(false);
		
		Assert.assertEquals(0, animation.getCurrentFrameIndex());
		animation.update(1f);
		Assert.assertEquals(2, animation.getCurrentFrameIndex());
		animation.update(1f);
		Assert.assertEquals(4, animation.getCurrentFrameIndex());
		animation.update(1f);
		Assert.assertEquals(4, animation.getCurrentFrameIndex());
	}
	
	@Test
	public void testUpdateByMultipleFramesLooping() {
		animation.setLooping(true);
		
		Assert.assertEquals(0, animation.getCurrentFrameIndex());
		animation.update(1f);
		Assert.assertEquals(2, animation.getCurrentFrameIndex());
		animation.update(1f);
		Assert.assertEquals(4, animation.getCurrentFrameIndex());
		animation.update(1f);
		Assert.assertEquals(1, animation.getCurrentFrameIndex());
	}
}
