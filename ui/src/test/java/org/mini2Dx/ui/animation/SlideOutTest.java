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
import org.mini2Dx.ui.effect.SlideOut;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.render.UiContainerRenderTree;

import org.mini2Dx.gdx.math.MathUtils;

import junit.framework.Assert;

/**
 *
 */
public class SlideOutTest {
	private static final float DELTA = 1f / 60f;
	private static final int CONTAINER_WIDTH = 800;
	private static final int CONTAINER_HEIGHT = 600;

	private static final float CURRENT_WIDTH = 120f;
	private static final float CURRENT_HEIGHT = 140f;
	private static final float CURRENT_X = (CONTAINER_WIDTH / 2f) - (CURRENT_WIDTH / 2f);
	private static final float CURRENT_Y = (CONTAINER_HEIGHT / 2f) - (CURRENT_HEIGHT / 2f);

	private final Mockery mockery = new Mockery();
	private final CollisionBox currentArea = new CollisionBox(CURRENT_X, CURRENT_Y, CURRENT_WIDTH, CURRENT_HEIGHT);
	private final Rectangle targetArea = new Rectangle();

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
	public void testSlideOutUpDuration() {
		for (int duration = 1; duration <= 10; duration++) {
			currentArea.forceTo(CURRENT_X, CURRENT_Y, CURRENT_WIDTH, CURRENT_HEIGHT);

			final float expectedSpeed = (CURRENT_Y + CURRENT_HEIGHT) / duration;
			final int totalFrames = (duration * 60);

			mockery.checking(new Expectations() {
				{
					oneOf(uiElement).setVisibility(Visibility.HIDDEN);
				}
			});

			SlideOut slideOut = new SlideOut(SlideDirection.UP, duration);
			slideOut.preBegin(uiElement);
			for (int i = 0; i < totalFrames; i++) {
				if (i < totalFrames - 1) {
					Assert.assertEquals(false, slideOut.isFinished());
				}
				slideOut.update(renderTree, currentArea, targetArea, DELTA);
			}
			Assert.assertEquals(true, slideOut.isFinished());
			Assert.assertEquals(expectedSpeed, slideOut.getSpeed(), 1f);
			Assert.assertEquals(true, currentArea.getY() < 0f);
		}
	}

	@Test
	public void testSlideOutDownDuration() {
		mockery.checking(new Expectations() {
			{
				atLeast(1).of(renderTree).getOuterRenderY();
				will(returnValue(0));
				atLeast(1).of(renderTree).getOuterRenderHeight();
				will(returnValue(CONTAINER_HEIGHT));
			}
		});
		
		for (int duration = 1; duration <= 10; duration++) {
			currentArea.forceTo(CURRENT_X, CURRENT_Y, CURRENT_WIDTH, CURRENT_HEIGHT);
			
			final float expectedSpeed = (CONTAINER_HEIGHT - CURRENT_Y) / duration;
			final int totalFrames = (duration * 60);

			mockery.checking(new Expectations() {
				{
					oneOf(uiElement).setVisibility(Visibility.HIDDEN);
				}
			});

			SlideOut slideOut = new SlideOut(SlideDirection.DOWN, duration);
			slideOut.preBegin(uiElement);
			for (int i = 0; i < totalFrames; i++) {
				if (i < totalFrames - 1) {
					Assert.assertEquals(false, slideOut.isFinished());
				}
				slideOut.update(renderTree, currentArea, targetArea, DELTA);
			}
			Assert.assertEquals(true, slideOut.isFinished());
			Assert.assertEquals(expectedSpeed, slideOut.getSpeed(), 1f);
			Assert.assertEquals(true, MathUtils.isEqual(currentArea.getY(), CONTAINER_HEIGHT, 0.1f)
					|| currentArea.getY() >= CONTAINER_HEIGHT);
		}
	}

	@Test
	public void testSlideOutLeftDuration() {
		for (int duration = 1; duration <= 10; duration++) {
			currentArea.forceTo(CURRENT_X, CURRENT_Y, CURRENT_WIDTH, CURRENT_HEIGHT);
			
			final float expectedSpeed = (CURRENT_X + CURRENT_WIDTH) / duration;
			final int totalFrames = (duration * 60);

			mockery.checking(new Expectations() {
				{
					oneOf(uiElement).setVisibility(Visibility.HIDDEN);
				}
			});

			SlideOut slideOut = new SlideOut(SlideDirection.LEFT, duration);
			slideOut.preBegin(uiElement);
			for (int i = 0; i < totalFrames; i++) {
				if (i < totalFrames - 1) {
					Assert.assertEquals(false, slideOut.isFinished());
				}
				slideOut.update(renderTree, currentArea, targetArea, DELTA);
			}
			Assert.assertEquals(true, slideOut.isFinished());
			Assert.assertEquals(expectedSpeed, slideOut.getSpeed(), 1f);
			Assert.assertEquals(true, currentArea.getX() < 0f);
		}
	}

	@Test
	public void testSlideOutRightDuration() {
		mockery.checking(new Expectations() {
			{
				atLeast(1).of(renderTree).getOuterRenderX();
				will(returnValue(0));
				atLeast(1).of(renderTree).getOuterRenderWidth();
				will(returnValue(CONTAINER_WIDTH));
			}
		});
		
		for (int duration = 1; duration <= 10; duration++) {
			currentArea.forceTo(CURRENT_X, CURRENT_Y, CURRENT_WIDTH, CURRENT_HEIGHT);
			
			final float expectedSpeed = (CONTAINER_WIDTH - CURRENT_X) / duration;
			final int totalFrames = (duration * 60);

			mockery.checking(new Expectations() {
				{
					oneOf(uiElement).setVisibility(Visibility.HIDDEN);
				}
			});

			SlideOut slideOut = new SlideOut(SlideDirection.RIGHT, duration);
			slideOut.preBegin(uiElement);
			for (int i = 0; i < totalFrames; i++) {
				if (i < totalFrames - 1) {
					Assert.assertEquals(false, slideOut.isFinished());
				}
				slideOut.update(renderTree, currentArea, targetArea, DELTA);
			}
			Assert.assertEquals(true, slideOut.isFinished());
			Assert.assertEquals(expectedSpeed, slideOut.getSpeed(), 1f);
			Assert.assertEquals(true, MathUtils.isEqual(currentArea.getX(), CONTAINER_WIDTH, 0.1f)
					|| currentArea.getX() >= CONTAINER_WIDTH);
		}
	}
}
