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
package org.mini2Dx.ui.render;

import org.junit.Ignore;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.Platform;
import org.mini2Dx.core.util.InterpolationTracker;
import org.mini2Dx.gdx.utils.Array;
import junit.framework.Assert;
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
		Mdx.platform = Platform.WINDOWS;

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
	public void testChangeRenderCoordinatesBeforeLayout() {
		mockery.checking(new Expectations() {
			{
				atLeast(1).of(renderTree).transferLayoutDeferred(with(any(Array.class)));
				atLeast(1).of(renderTree).transferUpdateDeferred(with(any(Array.class)));
			}
		});

		configureParentWidth();

		InterpolationTracker.preUpdate();
		parentRenderNode.layout(layoutState);
		parentRenderNode.update(null, 0.1f);
		InterpolationTracker.interpolate(1f);

		Assert.assertEquals(false, renderNode.isDirty());
		Assert.assertEquals(false, parentRenderNode.isDirty());

		InterpolationTracker.preUpdate();
		renderNode.setDirty();
		Assert.assertEquals(true, renderNode.isDirty());
		uiElement.setXY(50f, 75f);
		Assert.assertEquals(true, renderNode.isDirty());
		Assert.assertEquals(true, parentRenderNode.isDirty());

		parentRenderNode.layout(layoutState);
		parentRenderNode.update(null, 0.1f);
		InterpolationTracker.interpolate(1f);

		Assert.assertEquals(false, renderNode.isDirty());
		Assert.assertEquals(false, parentRenderNode.isDirty());

		Assert.assertEquals(50, renderNode.getOuterRenderX());
		Assert.assertEquals(75, renderNode.getOuterRenderY());
	}
	
	@Test
	public void testRenderCoordinatesWithParentMargin() {
		mockery.checking(new Expectations() {
			{
				atLeast(1).of(renderTree).transferLayoutDeferred(with(any(Array.class)));
				atLeast(1).of(renderTree).transferUpdateDeferred(with(any(Array.class)));
			}
		});

		configureParentWidth();

		InterpolationTracker.preUpdate();
		parentRenderNode.getStyle().setMarginLeft(4);
		parentRenderNode.getStyle().setMarginTop(8);
		parentRenderNode.getStyle().setMarginRight(7);
		parentRenderNode.getStyle().setMarginBottom(3);
		
		renderNode.setDirty();
		Assert.assertEquals(true, renderNode.isDirty());
		Assert.assertEquals(true, parentRenderNode.isDirty());

		parentRenderNode.layout(layoutState);
		Assert.assertEquals(false, parentRenderNode.isDirty());

		parentRenderNode.update(null, 0.1f);
		InterpolationTracker.interpolate(1f);
		
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
		mockery.checking(new Expectations() {
			{
				atLeast(1).of(renderTree).transferLayoutDeferred(with(any(Array.class)));
				atLeast(1).of(renderTree).transferUpdateDeferred(with(any(Array.class)));
			}
		});

		configureParentWidth();

		InterpolationTracker.preUpdate();
		parentRenderNode.getStyle().setPaddingLeft(4);
		parentRenderNode.getStyle().setPaddingTop(8);
		parentRenderNode.getStyle().setPaddingRight(3);
		parentRenderNode.getStyle().setPaddingBottom(7);
		
		renderNode.setDirty();
		parentRenderNode.layout(layoutState);
		parentRenderNode.update(null, 0.1f);
		InterpolationTracker.interpolate(1f);
		
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
		mockery.checking(new Expectations() {
			{
				atLeast(1).of(renderTree).transferLayoutDeferred(with(any(Array.class)));
				atLeast(1).of(renderTree).transferUpdateDeferred(with(any(Array.class)));
			}
		});

		configureParentWidth();
		
		parentRenderNode.getStyle().setPaddingLeft(12);
		parentRenderNode.getStyle().setPaddingTop(8);
		parentRenderNode.getStyle().setPaddingRight(3);
		parentRenderNode.getStyle().setPaddingBottom(7);
		
		parentRenderNode.getStyle().setMarginLeft(4);
		parentRenderNode.getStyle().setMarginTop(-4);
		parentRenderNode.getStyle().setMarginRight(7);
		parentRenderNode.getStyle().setMarginBottom(3);
		
		renderNode.setDirty();
		parentRenderNode.layout(layoutState);
		parentRenderNode.update(null, 0.1f);
		InterpolationTracker.interpolate(1f);
		
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
		mockery.checking(new Expectations() {
			{
				atLeast(1).of(renderTree).transferLayoutDeferred(with(any(Array.class)));
				atLeast(1).of(renderTree).transferUpdateDeferred(with(any(Array.class)));
			}
		});

		configureParentWidth();

		InterpolationTracker.preUpdate();
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
		
		renderNode.setDirty();
		parentRenderNode.layout(layoutState);
		parentRenderNode.update(null, 0.1f);
		InterpolationTracker.interpolate(1f);
		
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
				atLeast(1).of(renderTree).transferLayoutDeferred(with(any(Array.class)));
			}
		});
		
		final float preferredWidth = 150f;
		final float preferredHeight = 200f;
		
		uiElement.setPreferredContentWidth(preferredWidth);
		uiElement.setPreferredContentHeight(preferredHeight);
		
		renderNode.setDirty();
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
				atLeast(1).of(renderTree).transferLayoutDeferred(with(any(Array.class)));
			}
		});
		
		final int padding = 20;
		final float preferredWidth = 150f;
		final float preferredHeight = 200f;
		
		uiElement.getStyleRule().setPadding(padding);
		uiElement.setPreferredContentWidth(preferredWidth);
		uiElement.setPreferredContentHeight(preferredHeight);
		
		renderNode.setDirty();
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
				atLeast(1).of(renderTree).transferLayoutDeferred(with(any(Array.class)));
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
		
		renderNode.setDirty();
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
				atLeast(1).of(renderTree).transferLayoutDeferred(with(any(Array.class)));
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
		
		renderNode.setDirty();
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
				atLeast(1).of(layoutState).getParentWidth();
				will(returnValue(0f + PARENT_WIDTH));
				atLeast(1).of(layoutState).setParentWidth(with(any(Float.class)));
			}
		});
		parentElement.setPreferredContentWidth(PARENT_WIDTH);
	}
}
