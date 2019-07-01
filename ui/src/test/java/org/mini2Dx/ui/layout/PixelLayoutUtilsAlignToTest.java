/**
 * Copyright (c) 2018 See AUTHORS file
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * <p>
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.ui.layout;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.ui.dummy.DummyParentUiElement;
import org.mini2Dx.ui.dummy.DummyUiElement;

public class PixelLayoutUtilsAlignToTest {

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
	public void testAlignBelow() {
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

				oneOf(childElement).setXY(PARENT_X, PARENT_Y + PARENT_HEIGHT);
			}
		});

		PixelLayoutUtils.alignBelow(childElement, parentElement, HorizontalAlignment.LEFT);
		PixelLayoutUtils.update(1f);
	}
}
