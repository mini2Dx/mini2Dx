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
package org.mini2Dx.ui.animation;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.collision.CollisionBox;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.ui.effect.SlideDirection;
import org.mini2Dx.ui.effect.SlideIn;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.render.UiContainerRenderTree;

import junit.framework.Assert;

/**
 *
 */
public class SlideInTest {
	private static final float DELTA = 1f / 60f;
	private static final int CONTAINER_WIDTH = 800;
	private static final int CONTAINER_HEIGHT = 600;

	private static final float TARGET_WIDTH = 120f;
	private static final float TARGET_HEIGHT = 140f;
	private static final float TARGET_X = (CONTAINER_WIDTH / 2f) - (TARGET_WIDTH / 2f);
	private static final float TARGET_Y = (CONTAINER_HEIGHT / 2f) - (TARGET_HEIGHT / 2f);

	private final Mockery mockery = new Mockery();
	private final CollisionBox currentArea = new CollisionBox();
	private final Rectangle targetArea = new Rectangle(TARGET_X, TARGET_Y, TARGET_WIDTH, TARGET_HEIGHT);

	private UiElement uiElement;
	private UiContainerRenderTree renderTree;

	@Before
	public void setUp() {
		mockery.setImposteriser(ClassImposteriser.INSTANCE);

		uiElement = mockery.mock(UiElement.class);
		renderTree = mockery.mock(UiContainerRenderTree.class);
	}

	@After
	public void teardown() {
		mockery.assertIsSatisfied();
	}

	@Test
	public void testSlideInUpDuration() {
		for(int duration = 1; duration <= 10; duration++) {
			final float expectedSpeed = (CONTAINER_HEIGHT - TARGET_Y) / duration;
			final int totalFrames = (duration * 60);
			
			mockery.checking(new Expectations() {
				{
					oneOf(uiElement).setVisibility(with(Visibility.VISIBLE));
					oneOf(renderTree).getOuterRenderY();
					will(returnValue(0));
					oneOf(renderTree).getOuterRenderHeight();
					will(returnValue(CONTAINER_HEIGHT));
				}
			});
			
			SlideIn slideIn = new SlideIn(SlideDirection.UP, duration);
			slideIn.preBegin(uiElement);
			for(int i = 0; i < totalFrames; i++) {
				if(i < totalFrames - 1) {
					Assert.assertEquals(false, slideIn.isFinished());
				}
				slideIn.update(renderTree, currentArea, targetArea, DELTA);
			}
			Assert.assertEquals(true, slideIn.isFinished());
			Assert.assertEquals(expectedSpeed, slideIn.getSpeed(), 1f);
			Assert.assertEquals(TARGET_X, currentArea.getX(), 0.1f);
			Assert.assertEquals(TARGET_Y, currentArea.getY(), 0.1f);
		}
	}
	
	@Test
	public void testSlideInDownDuration() {
		for(int duration = 1; duration <= 10; duration++) {
			final float expectedSpeed = (TARGET_Y + TARGET_HEIGHT) / duration;
			final int totalFrames = (duration * 60);
			
			mockery.checking(new Expectations() {
				{
					oneOf(uiElement).setVisibility(with(Visibility.VISIBLE));
					oneOf(renderTree).getOuterRenderY();
					will(returnValue(0));
				}
			});
			
			SlideIn slideIn = new SlideIn(SlideDirection.DOWN, duration);
			slideIn.preBegin(uiElement);
			for(int i = 0; i < totalFrames; i++) {
				if(i < totalFrames - 1) {
					Assert.assertEquals(false, slideIn.isFinished());
				}
				slideIn.update(renderTree, currentArea, targetArea, DELTA);
			}
			Assert.assertEquals(true, slideIn.isFinished());
			Assert.assertEquals(expectedSpeed, slideIn.getSpeed(), 1f);
			Assert.assertEquals(TARGET_X, currentArea.getX(), 0.1f);
			Assert.assertEquals(TARGET_Y, currentArea.getY(), 0.1f);
		}
	}
	
	@Test
	public void testSlideInLeftDuration() {
		for(int duration = 1; duration <= 10; duration++) {
			final float expectedSpeed = (CONTAINER_WIDTH - TARGET_X) / duration;
			final int totalFrames = (duration * 60);
			
			mockery.checking(new Expectations() {
				{
					oneOf(uiElement).setVisibility(with(Visibility.VISIBLE));
					oneOf(renderTree).getOuterRenderX();
					will(returnValue(0));
					oneOf(renderTree).getOuterRenderWidth();
					will(returnValue(CONTAINER_WIDTH));
				}
			});
			
			SlideIn slideIn = new SlideIn(SlideDirection.LEFT, duration);
			slideIn.preBegin(uiElement);
			for(int i = 0; i < totalFrames; i++) {
				if(i < totalFrames - 1) {
					Assert.assertEquals(false, slideIn.isFinished());
				}
				slideIn.update(renderTree, currentArea, targetArea, DELTA);
			}
			Assert.assertEquals(true, slideIn.isFinished());
			Assert.assertEquals(expectedSpeed, slideIn.getSpeed(), 1f);
			Assert.assertEquals(TARGET_X, currentArea.getX(), 0.1f);
			Assert.assertEquals(TARGET_Y, currentArea.getY(), 0.1f);
		}
	}
	
	@Test
	public void testSlideInRightDuration() {
		for(int duration = 1; duration <= 10; duration++) {
			final float expectedSpeed = (TARGET_X + TARGET_WIDTH) / duration;
			final int totalFrames = (duration * 60);
			
			mockery.checking(new Expectations() {
				{
					oneOf(uiElement).setVisibility(with(Visibility.VISIBLE));
					oneOf(renderTree).getOuterRenderX();
					will(returnValue(0));
				}
			});
			
			SlideIn slideIn = new SlideIn(SlideDirection.RIGHT, duration);
			slideIn.preBegin(uiElement);
			for(int i = 0; i < totalFrames; i++) {
				if(i < totalFrames - 1) {
					Assert.assertEquals(false, slideIn.isFinished());
				}
				slideIn.update(renderTree, currentArea, targetArea, DELTA);
			}
			Assert.assertEquals(true, slideIn.isFinished());
			Assert.assertEquals(expectedSpeed, slideIn.getSpeed(), 1f);
			Assert.assertEquals(TARGET_X, currentArea.getX(), 0.1f);
			Assert.assertEquals(TARGET_Y, currentArea.getY(), 0.1f);
		}
	}
	
	@Test
	public void testSlideInWithTargetPositionChangeDuringAnimation() {
		final int duration = 1;
		final int totalFrames = (duration * 60);
		final int halfTotalFrames = totalFrames / 2;
		
		targetArea.set(0f, 0f, TARGET_WIDTH, TARGET_HEIGHT);
		float expectedSpeed = CONTAINER_HEIGHT / duration;
		
		mockery.checking(new Expectations() {
			{
				oneOf(uiElement).setVisibility(with(Visibility.VISIBLE));
				oneOf(renderTree).getOuterRenderY();
				will(returnValue(0));
				oneOf(renderTree).getOuterRenderHeight();
				will(returnValue(CONTAINER_HEIGHT));
			}
		});
		
		SlideIn slideIn = new SlideIn(SlideDirection.UP, duration);
		slideIn.preBegin(uiElement);
		for(int i = 0; i < halfTotalFrames; i++) {
			slideIn.update(renderTree, currentArea, targetArea, DELTA);
		}
		Assert.assertEquals(false, slideIn.isFinished());
		Assert.assertEquals(expectedSpeed, slideIn.getSpeed(), 1f);
		
		targetArea.set(TARGET_X, TARGET_Y, TARGET_WIDTH, TARGET_HEIGHT);
		expectedSpeed = (currentArea.getY() - TARGET_Y) / (duration * 0.5f);
		
		for(int i = halfTotalFrames; i < totalFrames; i++) {
			slideIn.update(renderTree, currentArea, targetArea, DELTA);
		}
		Assert.assertEquals(true, slideIn.isFinished());
		Assert.assertEquals(expectedSpeed, slideIn.getSpeed(), 1f);
	}
}
