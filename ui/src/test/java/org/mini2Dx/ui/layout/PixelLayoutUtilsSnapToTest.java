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
package org.mini2Dx.ui.layout;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.ui.dummy.DummyParentUiElement;
import org.mini2Dx.ui.dummy.DummyUiElement;
import org.mini2Dx.ui.element.UiElement;

public class PixelLayoutUtilsSnapToTest {
	private final Mockery mockery = new Mockery();

	private static final float PARENT_X = 100f, PARENT_Y = 200f;
	private static final float PARENT_WIDTH = 100f, PARENT_HEIGHT = 100f;

	private static final float CHILD_WIDTH = 50f, CHILD_HEIGHT = 50f;

	private DummyParentUiElement parentElement, childElement;

	@Before
	public void setUp() {
		mockery.setImposteriser(ClassImposteriser.INSTANCE);

		parentElement = mockery.mock(DummyParentUiElement.class, "parentElement");
		childElement = mockery.mock(DummyParentUiElement.class, "childElement");

		mockery.checking(new Expectations() {
			{
				allowing(parentElement).getId();
				will(returnValue("0"));
				allowing(parentElement).getX();
				will(returnValue(PARENT_X));
				allowing(parentElement).getY();
				will(returnValue(PARENT_Y));
				allowing(parentElement).getWidth();
				will(returnValue(PARENT_WIDTH));
				allowing(parentElement).getHeight();
				will(returnValue(PARENT_HEIGHT));
				allowing(parentElement).getTotalChildren();
				will(returnValue(1));
				allowing(parentElement).getChild(0);
				will(returnValue(childElement));

				allowing(childElement).getId();
				will(returnValue("1"));
				allowing(childElement).getWidth();
				will(returnValue(CHILD_WIDTH));
				allowing(childElement).getHeight();
				will(returnValue(CHILD_HEIGHT));
				allowing(childElement).getTotalChildren();
				will(returnValue(0));
				allowing(childElement).isFlexLayout();
				will(returnValue(false));
			}
		});
	}

	@After
	public void teardown() {
		mockery.assertIsSatisfied();
	}

	@Test
	public void testSnapToTopLeft() {
		mockery.checking(new Expectations() {
			{
				allowing(parentElement).isInitialised();
				will(returnValue(true));
				allowing(parentElement).isRenderNodeDirty();
				will(returnValue(false));
				allowing(childElement).isInitialised();
				will(returnValue(true));
				allowing(childElement).isRenderNodeDirty();
				will(returnValue(false));

				oneOf(childElement).setXY(PARENT_X, PARENT_Y);
			}
		});

		PixelLayoutUtils.snapTo(childElement, parentElement, HorizontalAlignment.LEFT, VerticalAlignment.TOP);
		PixelLayoutUtils.update(1f);
	}

	@Test
	public void testSnapToTopRight() {
		mockery.checking(new Expectations() {
			{
				allowing(parentElement).isInitialised();
				will(returnValue(true));
				allowing(parentElement).isRenderNodeDirty();
				will(returnValue(false));
				allowing(childElement).isInitialised();
				will(returnValue(true));
				allowing(childElement).isRenderNodeDirty();
				will(returnValue(false));

				oneOf(childElement).setXY(PARENT_X + PARENT_WIDTH - CHILD_WIDTH, PARENT_Y);
			}
		});

		PixelLayoutUtils.snapTo(childElement, parentElement, HorizontalAlignment.RIGHT, VerticalAlignment.TOP);
		PixelLayoutUtils.update(1f);
	}

	@Test
	public void testSnapToBottomLeft() {
		mockery.checking(new Expectations() {
			{
				allowing(parentElement).isInitialised();
				will(returnValue(true));
				allowing(parentElement).isRenderNodeDirty();
				will(returnValue(false));
				allowing(childElement).isInitialised();
				will(returnValue(true));
				allowing(childElement).isRenderNodeDirty();
				will(returnValue(false));

				oneOf(childElement).setXY(PARENT_X, PARENT_Y + PARENT_HEIGHT - CHILD_HEIGHT);
			}
		});

		PixelLayoutUtils.snapTo(childElement, parentElement, HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM);
		PixelLayoutUtils.update(1f);
	}

	@Test
	public void testSnapToBottomRight() {
		mockery.checking(new Expectations() {
			{
				allowing(parentElement).isInitialised();
				will(returnValue(true));
				allowing(parentElement).isRenderNodeDirty();
				will(returnValue(false));
				allowing(childElement).isInitialised();
				will(returnValue(true));
				allowing(childElement).isRenderNodeDirty();
				will(returnValue(false));

				oneOf(childElement).setXY(PARENT_X + PARENT_WIDTH - CHILD_WIDTH, PARENT_Y + PARENT_HEIGHT - CHILD_HEIGHT);
			}
		});

		PixelLayoutUtils.snapTo(childElement, parentElement, HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
		PixelLayoutUtils.update(1f);
	}
}
