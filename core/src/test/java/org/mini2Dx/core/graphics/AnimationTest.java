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
package org.mini2Dx.core.graphics;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link Animation}
 */
public class AnimationTest {
	private Animation<Sprite> animation;
	private Mockery mockery;

	private Color color;
	private Sprite sprite;

	@Before
	public void setup() {
		mockery = new Mockery();
		mockery.setImposteriser(ClassImposteriser.INSTANCE);
		sprite = mockery.mock(Sprite.class);
		color = mockery.mock(Color.class);

		mockery.checking(new Expectations(){
			{
				exactly(5).of(sprite).getOriginX();
				will(returnValue(1f));
				exactly(5).of(sprite).getOriginY();
				will(returnValue(1f));
				exactly(1).of(sprite).getTint();
				will(returnValue(color));
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
