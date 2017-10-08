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
package org.mini2Dx.ui.render;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.ui.dummy.DummyParentRenderNode;
import org.mini2Dx.ui.dummy.DummyParentUiElement;
import org.mini2Dx.ui.dummy.DummyRenderNode;
import org.mini2Dx.ui.dummy.DummyUiElement;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.layout.ScreenSize;

import junit.framework.Assert;

/**
 * Unit tests for {@link RenderNode}
 */
public class RenderNodeTest {
	private static final int PARENT_WIDTH = 400;
	private static final int PARENT_HEIGHT = 300;
	private static final int ELEMENT_WIDTH = 200;
	private static final int ELEMENT_HEIGHT = 150;
	
	private Mockery mockery;
	private LayoutState layoutState;
	private UiContainerRenderTree renderTree;
	
	private DummyParentUiElement parentElement = new DummyParentUiElement();
	private DummyUiElement uiElement = new DummyUiElement();
	
	private DummyParentRenderNode parentRenderNode = new DummyParentRenderNode(null, parentElement);
	private DummyRenderNode renderNode = new DummyRenderNode(parentRenderNode, uiElement);
	
	@Before
	public void setUp() {
		parentElement.setVisibility(Visibility.VISIBLE);
		parentElement.setPreferredContentWidth(PARENT_WIDTH);
		parentElement.setPreferredContentHeight(PARENT_HEIGHT);
		
		uiElement.setVisibility(Visibility.VISIBLE);
		uiElement.setPreferredContentWidth(ELEMENT_WIDTH);
		uiElement.setPreferredContentHeight(ELEMENT_HEIGHT);
		parentRenderNode.addChild(renderNode);
		
		mockery = new Mockery();
		mockery.setImposteriser(ClassImposteriser.INSTANCE);
		
		layoutState = mockery.mock(LayoutState.class);
		renderTree = mockery.mock(UiContainerRenderTree.class);
	}
	
	@After
	public void teardown() {
		mockery.assertIsSatisfied();
	}
	
	@Test
	public void testRenderCoordinatesWithParentMargin() {
		configureParentWidth();
		
		parentRenderNode.getStyle().setMarginLeft(4);
		parentRenderNode.getStyle().setMarginTop(8);
		parentRenderNode.getStyle().setMarginRight(7);
		parentRenderNode.getStyle().setMarginBottom(3);
		
		renderNode.setDirty(true);
		parentRenderNode.layout(layoutState);
		parentRenderNode.update(null, 0.1f);
		parentRenderNode.interpolate(1f);
		
		Assert.assertEquals(0, parentRenderNode.getOuterRenderX());
		Assert.assertEquals(0, parentRenderNode.getOuterRenderY());
		Assert.assertEquals(4, parentRenderNode.getInnerRenderX());
		Assert.assertEquals(8, parentRenderNode.getInnerRenderY());
		Assert.assertEquals(PARENT_WIDTH + 11, parentRenderNode.getOuterRenderWidth());
		Assert.assertEquals(PARENT_WIDTH, parentRenderNode.getInnerRenderWidth());
		
		Assert.assertEquals(4, renderNode.getOuterRenderX());
		Assert.assertEquals(8, renderNode.getOuterRenderY());
		Assert.assertEquals(ELEMENT_WIDTH, renderNode.getOuterRenderWidth());
		Assert.assertEquals(ELEMENT_HEIGHT, renderNode.getOuterRenderHeight());
	}
	
	@Test
	public void testRenderCoordinatesWithParentPadding() {
		configureParentWidth();
		
		parentRenderNode.getStyle().setPaddingLeft(4);
		parentRenderNode.getStyle().setPaddingTop(8);
		parentRenderNode.getStyle().setPaddingRight(3);
		parentRenderNode.getStyle().setPaddingBottom(7);
		
		renderNode.setDirty(true);
		parentRenderNode.layout(layoutState);
		parentRenderNode.update(null, 0.1f);
		parentRenderNode.interpolate(1f);
		
		Assert.assertEquals(0, parentRenderNode.getOuterRenderX());
		Assert.assertEquals(0, parentRenderNode.getOuterRenderY());
		Assert.assertEquals(0, parentRenderNode.getInnerRenderX());
		Assert.assertEquals(0, parentRenderNode.getInnerRenderY());
		Assert.assertEquals(4, parentRenderNode.getContentRenderX());
		Assert.assertEquals(8, parentRenderNode.getContentRenderY());
		
		Assert.assertEquals(4, renderNode.getOuterRenderX());
		Assert.assertEquals(8, renderNode.getOuterRenderY());
		Assert.assertEquals(4, renderNode.getInnerRenderX());
		Assert.assertEquals(8, renderNode.getInnerRenderY());
		Assert.assertEquals(ELEMENT_WIDTH, renderNode.getOuterRenderWidth());
		Assert.assertEquals(ELEMENT_HEIGHT, renderNode.getOuterRenderHeight());
	}
	
	@Test
	public void testRenderCoordinatesWithParentMarginAndPadding() {
		configureParentWidth();
		
		parentRenderNode.getStyle().setPaddingLeft(12);
		parentRenderNode.getStyle().setPaddingTop(8);
		parentRenderNode.getStyle().setPaddingRight(3);
		parentRenderNode.getStyle().setPaddingBottom(7);
		
		parentRenderNode.getStyle().setMarginLeft(4);
		parentRenderNode.getStyle().setMarginTop(-4);
		parentRenderNode.getStyle().setMarginRight(7);
		parentRenderNode.getStyle().setMarginBottom(3);
		
		renderNode.setDirty(true);
		parentRenderNode.layout(layoutState);
		parentRenderNode.update(null, 0.1f);
		parentRenderNode.interpolate(1f);
		
		Assert.assertEquals(0, parentRenderNode.getOuterRenderX());
		Assert.assertEquals(0, parentRenderNode.getOuterRenderY());
		Assert.assertEquals(4, parentRenderNode.getInnerRenderX());
		Assert.assertEquals(-4, parentRenderNode.getInnerRenderY());
		Assert.assertEquals(16, parentRenderNode.getContentRenderX());
		Assert.assertEquals(4, parentRenderNode.getContentRenderY());
		
		Assert.assertEquals(16, renderNode.getOuterRenderX());
		Assert.assertEquals(4, renderNode.getOuterRenderY());
		Assert.assertEquals(ELEMENT_WIDTH, renderNode.getOuterRenderWidth());
		Assert.assertEquals(16, renderNode.getInnerRenderX());
		Assert.assertEquals(4, renderNode.getInnerRenderY());
	}
	
	@Test
	public void testRenderCoordinatesWithChildAndParentMarginAndPadding() {
		configureParentWidth();
		
		parentRenderNode.getStyle().setPadding(8);
		parentRenderNode.getStyle().setMargin(4);
		
		renderNode.getStyle().setMarginTop(-3);
		renderNode.getStyle().setMarginBottom(9);
		renderNode.getStyle().setMarginLeft(5);
		renderNode.getStyle().setMarginRight(7);
		
		renderNode.getStyle().setPaddingTop(1);
		renderNode.getStyle().setPaddingBottom(2);
		renderNode.getStyle().setPaddingLeft(3);
		renderNode.getStyle().setPaddingRight(4);
		
		renderNode.setDirty(true);
		parentRenderNode.layout(layoutState);
		parentRenderNode.update(null, 0.1f);
		parentRenderNode.interpolate(1f);
		
		Assert.assertEquals(12, renderNode.getOuterRenderX());
		Assert.assertEquals(12, renderNode.getOuterRenderY());
		Assert.assertEquals(ELEMENT_WIDTH + 19, renderNode.getOuterRenderWidth());
		Assert.assertEquals(17, renderNode.getInnerRenderX());
		Assert.assertEquals(9, renderNode.getInnerRenderY());
		Assert.assertEquals(ELEMENT_WIDTH + 7, renderNode.getInnerRenderWidth());
		Assert.assertEquals(20, renderNode.getContentRenderX());
		Assert.assertEquals(10, renderNode.getContentRenderY());
		Assert.assertEquals(ELEMENT_WIDTH, renderNode.getContentRenderWidth());
	}

	@Test
	public void testPreferredSize() {
		mockery.checking(new Expectations() {
			{
				atLeast(1).of(layoutState).getUiContainerRenderTree();
				will(returnValue(renderTree));
			}
		});
		
		final float preferredWidth = 150f;
		final float preferredHeight = 200f;
		
		uiElement.setPreferredContentWidth(preferredWidth);
		uiElement.setPreferredContentHeight(preferredHeight);
		
		renderNode.setDirty(true);
		renderNode.layout(layoutState);
		Assert.assertEquals(preferredWidth, renderNode.getPreferredContentWidth());
		Assert.assertEquals(preferredHeight, renderNode.getPreferredContentHeight());
		Assert.assertEquals(preferredWidth, renderNode.getPreferredInnerWidth());
		Assert.assertEquals(preferredHeight, renderNode.getPreferredInnerHeight());
		Assert.assertEquals(preferredWidth, renderNode.getPreferredOuterWidth());
		Assert.assertEquals(preferredHeight, renderNode.getPreferredOuterHeight());
	}
	
	@Test
	public void testPreferredSizeWithPadding() {
		mockery.checking(new Expectations() {
			{
				atLeast(1).of(layoutState).getUiContainerRenderTree();
				will(returnValue(renderTree));
			}
		});
		
		final int padding = 20;
		final float preferredWidth = 150f;
		final float preferredHeight = 200f;
		
		uiElement.getStyleRule().setPadding(padding);
		uiElement.setPreferredContentWidth(preferredWidth);
		uiElement.setPreferredContentHeight(preferredHeight);
		
		renderNode.setDirty(true);
		renderNode.layout(layoutState);
		Assert.assertEquals(preferredWidth, renderNode.getPreferredContentWidth());
		Assert.assertEquals(preferredHeight, renderNode.getPreferredContentHeight());
		Assert.assertEquals(preferredWidth + (padding * 2), renderNode.getPreferredInnerWidth());
		Assert.assertEquals(preferredHeight + (padding * 2), renderNode.getPreferredInnerHeight());
		Assert.assertEquals(preferredWidth + (padding * 2), renderNode.getPreferredOuterWidth());
		Assert.assertEquals(preferredHeight + (padding * 2), renderNode.getPreferredOuterHeight());
	}
	
	@Test
	public void testPreferredSizeWithMargin() {
		mockery.checking(new Expectations() {
			{
				atLeast(1).of(layoutState).getUiContainerRenderTree();
				will(returnValue(renderTree));
			}
		});
		
		final int padding = 20;
		final int margin = 35;
		final float preferredWidth = 150f;
		final float preferredHeight = 200f;
		
		uiElement.getStyleRule().setMargin(margin);
		uiElement.getStyleRule().setPadding(padding);
		uiElement.setPreferredContentWidth(preferredWidth);
		uiElement.setPreferredContentHeight(preferredHeight);
		
		renderNode.setDirty(true);
		renderNode.layout(layoutState);
		Assert.assertEquals(preferredWidth, renderNode.getPreferredContentWidth());
		Assert.assertEquals(preferredHeight, renderNode.getPreferredContentHeight());
		Assert.assertEquals(preferredWidth + (padding * 2), renderNode.getPreferredInnerWidth());
		Assert.assertEquals(preferredHeight + (padding * 2), renderNode.getPreferredInnerHeight());
		Assert.assertEquals(preferredWidth + (padding * 2) + (margin * 2), renderNode.getPreferredOuterWidth());
		Assert.assertEquals(preferredHeight + (padding * 2) + (margin * 2), renderNode.getPreferredOuterHeight());
	}
	
	@Test
	public void testPreferredSizeWithNegativeMargin() {
		mockery.checking(new Expectations() {
			{
				atLeast(1).of(layoutState).getUiContainerRenderTree();
				will(returnValue(renderTree));
			}
		});
		
		final int padding = 20;
		final int margin = -35;
		final float preferredWidth = 150f;
		final float preferredHeight = 200f;
		
		uiElement.getStyleRule().setMargin(margin);
		uiElement.getStyleRule().setPadding(padding);
		uiElement.setPreferredContentWidth(preferredWidth);
		uiElement.setPreferredContentHeight(preferredHeight);
		
		renderNode.setDirty(true);
		renderNode.layout(layoutState);
		Assert.assertEquals(preferredWidth, renderNode.getPreferredContentWidth());
		Assert.assertEquals(preferredHeight, renderNode.getPreferredContentHeight());
		Assert.assertEquals(preferredWidth + (padding * 2), renderNode.getPreferredInnerWidth());
		Assert.assertEquals(preferredHeight + (padding * 2), renderNode.getPreferredInnerHeight());
		Assert.assertEquals(preferredWidth + (padding * 2) + (margin * 2), renderNode.getPreferredOuterWidth());
		Assert.assertEquals(preferredHeight + (padding * 2) + (margin * 2), renderNode.getPreferredOuterHeight());
	}
	
	private void configureParentWidth() {
		mockery.checking(new Expectations() {
			{
				atLeast(1).of(layoutState).getUiContainerRenderTree();
				will(returnValue(renderTree));
				atLeast(1).of(layoutState).getScreenSize();
				will(returnValue(ScreenSize.XS));
				atLeast(1).of(layoutState).getParentWidth();
				will(returnValue(0f + PARENT_WIDTH));
				atLeast(1).of(layoutState).setParentWidth(with(any(Float.class)));
			}
		});
		parentElement.setPreferredContentWidth(PARENT_WIDTH);
	}
}
