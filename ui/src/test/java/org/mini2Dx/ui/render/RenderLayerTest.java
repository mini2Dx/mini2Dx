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
import org.mini2Dx.ui.layout.FlexLayoutRuleset;
import org.mini2Dx.ui.layout.LayoutRuleset;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.layout.ScreenSize;

import java.util.List;

/**
 * Unit tests for {@link RenderLayer}
 */
public class RenderLayerTest {
	private Mockery mockery;
	private LayoutState layoutState;
	private UiContainerRenderTree renderTree;
	
	private DummyParentUiElement parentElement = new DummyParentUiElement();
	private DummyUiElement uiElement1 = new DummyUiElement();
	private DummyUiElement uiElement2 = new DummyUiElement();
	
	private DummyParentRenderNode parentRenderNode = new DummyParentRenderNode(null, parentElement);
	private DummyRenderNode renderNode1 = new DummyRenderNode(parentRenderNode, uiElement1);
	private DummyRenderNode renderNode2 = new DummyRenderNode(parentRenderNode, uiElement2);
	
	private RenderLayer renderLayer = new RenderLayer(parentRenderNode, 0);
	
	@Before
	public void setUp() {
		parentElement.setVisibility(Visibility.VISIBLE);
		uiElement1.setVisibility(Visibility.VISIBLE);
		uiElement2.setVisibility(Visibility.VISIBLE);
		
		mockery = new Mockery();
		mockery.setImposteriser(ClassImposteriser.INSTANCE);
		
		layoutState = mockery.mock(LayoutState.class);
		renderTree = mockery.mock(UiContainerRenderTree.class);
		
		renderLayer.add(renderNode1);
		renderLayer.add(renderNode2);

		mockery.checking(new Expectations() {
			{
				atLeast(1).of(renderTree).transferLayoutDeferred(with(any(Array.class)));
			}
		});
	}
	
	@After
	public void teardown() {
		mockery.assertIsSatisfied();
	}
	
	@Test
	public void testSetChildRelativePositionWithParentPadding() {
		configureParentWithWidth(500f);
		final float preferredWidth = 150f;
		final float preferredHeight = 200f;
		
		parentRenderNode.getStyle().setPaddingTop(4);
		parentRenderNode.getStyle().setPaddingLeft(8);
		uiElement1.setPreferredContentWidth(preferredWidth);
		uiElement1.setPreferredContentHeight(preferredHeight);
		uiElement2.setPreferredContentWidth(preferredWidth);
		uiElement2.setPreferredContentHeight(preferredHeight);
		
		renderNode1.setDirty();
		renderNode2.setDirty();
		final LayoutRuleset layoutRuleset = FlexLayoutRuleset.parse("flex-column:xs-12c");
		renderLayer.layout(layoutState, layoutRuleset);
		
		Assert.assertEquals(8f, renderNode1.getRelativeX());
		Assert.assertEquals(4f, renderNode1.getRelativeY());
	}
	
	@Test
	public void testSetChildRelativePositionWithParentMargin() {
		configureParentWithWidth(500f);
		final float preferredWidth = 150f;
		final float preferredHeight = 200f;
		
		parentRenderNode.getStyle().setMarginTop(4);
		parentRenderNode.getStyle().setMarginLeft(8);
		uiElement1.setPreferredContentWidth(preferredWidth);
		uiElement1.setPreferredContentHeight(preferredHeight);
		uiElement2.setPreferredContentWidth(preferredWidth);
		uiElement2.setPreferredContentHeight(preferredHeight);
		
		renderNode1.setDirty();
		renderNode2.setDirty();
		final LayoutRuleset layoutRuleset = FlexLayoutRuleset.parse("flex-column:xs-12c");
		renderLayer.layout(layoutState, layoutRuleset);
		
		Assert.assertEquals(0f, renderNode1.getRelativeX());
		Assert.assertEquals(0f, renderNode1.getRelativeY());
	}

	@Test
	public void testPreferredHeightNonWrapped() {
		configureParentWithWidth(500f);
		
		final float preferredWidth = 150f;
		final float preferredHeight = 200f;
		
		uiElement1.setPreferredContentWidth(preferredWidth);
		uiElement1.setPreferredContentHeight(preferredHeight);
		uiElement2.setPreferredContentWidth(preferredWidth);
		uiElement2.setPreferredContentHeight(preferredHeight);
		
		renderNode1.setDirty();
		renderNode2.setDirty();
		final LayoutRuleset layoutRuleset = FlexLayoutRuleset.parse("flex-column:xs-12c");
		renderLayer.layout(layoutState, layoutRuleset);
		
		Assert.assertEquals(preferredHeight, renderLayer.determinePreferredContentHeight(layoutState));
	}
	
	@Test
	public void testPreferredHeightWrapped() {
		configureParentWithWidth(150f);
		
		final float preferredWidth = 150f;
		final float preferredHeight = 200f;
		
		uiElement1.setPreferredContentWidth(preferredWidth);
		uiElement1.setPreferredContentHeight(preferredHeight);
		uiElement2.setPreferredContentWidth(preferredWidth);
		uiElement2.setPreferredContentHeight(preferredHeight);
		
		renderNode1.setDirty();
		renderNode2.setDirty();
		final LayoutRuleset layoutRuleset = FlexLayoutRuleset.parse("flex-column:xs-12c");
		renderLayer.layout(layoutState, layoutRuleset);
		
		Assert.assertEquals(preferredHeight * 2f, renderLayer.determinePreferredContentHeight(layoutState));
	}
	
	@Test
	public void testPreferredHeightNonWrappedWithYOffset() {
		configureParentWithWidth(500f);
		
		final float preferredWidth = 150f;
		final float preferredHeight = 200f;
		final float yOffset = 24f;
		
		uiElement1.setY(yOffset);
		uiElement2.setY(yOffset);
		uiElement1.setPreferredContentWidth(preferredWidth);
		uiElement1.setPreferredContentHeight(preferredHeight);
		uiElement2.setPreferredContentWidth(preferredWidth);
		uiElement2.setPreferredContentHeight(preferredHeight);
		
		renderNode1.setDirty();
		renderNode2.setDirty();
		final LayoutRuleset layoutRuleset = FlexLayoutRuleset.parse("flex-column:xs-12c");
		renderLayer.layout(layoutState, layoutRuleset);
		
		Assert.assertEquals(preferredHeight + yOffset, renderLayer.determinePreferredContentHeight(layoutState));
	}
	
	@Test
	public void testPreferredHeightWrappedWithYOffset() {
		configureParentWithWidth(150f);
		
		final float preferredWidth = 150f;
		final float preferredHeight = 200f;
		final float yOffset = 20f;
		
		uiElement1.setY(yOffset);
		uiElement2.setY(yOffset);
		uiElement1.setPreferredContentWidth(preferredWidth);
		uiElement1.setPreferredContentHeight(preferredHeight);
		uiElement2.setPreferredContentWidth(preferredWidth);
		uiElement2.setPreferredContentHeight(preferredHeight);
		
		renderNode1.setDirty();
		renderNode2.setDirty();
		final LayoutRuleset layoutRuleset = FlexLayoutRuleset.parse("flex-column:xs-12c");
		renderLayer.layout(layoutState, layoutRuleset);
		
		Assert.assertEquals((preferredHeight * 2f) + (yOffset * 2f), renderLayer.determinePreferredContentHeight(layoutState));
	}
	
	@Test
	public void testPreferredHeightNonWrappedWithPadding() {
		configureParentWithWidth(500f);
		
		final int padding = 20;
		final float preferredWidth = 150f;
		final float preferredHeight = 200f;
		
		uiElement1.getStyleRule().setPadding(padding);
		uiElement2.getStyleRule().setPadding(padding);
		
		uiElement1.setPreferredContentWidth(preferredWidth);
		uiElement1.setPreferredContentHeight(preferredHeight);
		uiElement2.setPreferredContentWidth(preferredWidth);
		uiElement2.setPreferredContentHeight(preferredHeight);
		
		renderNode1.setDirty();
		renderNode2.setDirty();
		final LayoutRuleset layoutRuleset = FlexLayoutRuleset.parse("flex-column:xs-12c");
		renderLayer.layout(layoutState, layoutRuleset);
		
		Assert.assertEquals(preferredHeight + (padding * 2f), renderLayer.determinePreferredContentHeight(layoutState));
	}
	
	@Test
	public void testPreferredHeightWrappedWithPadding() {
		configureParentWithWidth(200f);
		
		final int padding = 20;
		final float preferredWidth = 150f;
		final float preferredHeight = 200f;
		
		uiElement1.getStyleRule().setPadding(padding);
		uiElement2.getStyleRule().setPadding(padding);
		
		uiElement1.setPreferredContentWidth(preferredWidth);
		uiElement1.setPreferredContentHeight(preferredHeight);
		uiElement2.setPreferredContentWidth(preferredWidth);
		uiElement2.setPreferredContentHeight(preferredHeight);
		
		renderNode1.setDirty();
		renderNode2.setDirty();
		final LayoutRuleset layoutRuleset = FlexLayoutRuleset.parse("flex-column:xs-12c");
		renderLayer.layout(layoutState, layoutRuleset);
		
		Assert.assertEquals((preferredHeight * 2f) + (padding * 4f), renderLayer.determinePreferredContentHeight(layoutState));
	}
	
	@Test
	public void testPreferredHeightNonWrappedWithMarginAndPadding() {
		configureParentWithWidth(1000f);
		
		final int padding = 20;
		final int margin = 35;
		final float preferredWidth = 150f;
		final float preferredHeight = 200f;
		
		uiElement1.getStyleRule().setMargin(margin);
		uiElement2.getStyleRule().setMargin(margin);
		uiElement1.getStyleRule().setPadding(padding);
		uiElement2.getStyleRule().setPadding(padding);
		
		uiElement1.setPreferredContentWidth(preferredWidth);
		uiElement1.setPreferredContentHeight(preferredHeight);
		uiElement2.setPreferredContentWidth(preferredWidth);
		uiElement2.setPreferredContentHeight(preferredHeight);
		
		renderNode1.setDirty();
		renderNode2.setDirty();
		final LayoutRuleset layoutRuleset = FlexLayoutRuleset.parse("flex-column:xs-12c");
		renderLayer.layout(layoutState, layoutRuleset);
		
		Assert.assertEquals(preferredHeight + (padding * 2f) + (margin * 2f), renderLayer.determinePreferredContentHeight(layoutState));
	}
	
	@Test
	public void testPreferredHeightWrappedWithMarginAndPadding() {
		configureParentWithWidth(300f);
		
		final int padding = 20;
		final int margin = 35;
		final float preferredWidth = 150f;
		final float preferredHeight = 200f;
		
		uiElement1.getStyleRule().setMargin(margin);
		uiElement2.getStyleRule().setMargin(margin);
		uiElement1.getStyleRule().setPadding(padding);
		uiElement2.getStyleRule().setPadding(padding);
		
		uiElement1.setPreferredContentWidth(preferredWidth);
		uiElement1.setPreferredContentHeight(preferredHeight);
		uiElement2.setPreferredContentWidth(preferredWidth);
		uiElement2.setPreferredContentHeight(preferredHeight);
		
		renderNode1.setDirty();
		renderNode2.setDirty();
		final LayoutRuleset layoutRuleset = FlexLayoutRuleset.parse("flex-column:xs-12c");
		renderLayer.layout(layoutState, layoutRuleset);
		
		Assert.assertEquals((preferredHeight * 2f) + (padding * 4f) + (margin * 4f), renderLayer.determinePreferredContentHeight(layoutState));
	}
	
	@Test
	public void testPreferredHeightNonWrappedWithPaddingAndNegativeMargin() {
		configureParentWithWidth(1000f);
		
		final int padding = 20;
		final int margin = -35;
		final float preferredWidth = 150f;
		final float preferredHeight = 200f;
		
		uiElement1.getStyleRule().setMargin(margin);
		uiElement2.getStyleRule().setMargin(margin);
		uiElement1.getStyleRule().setPadding(padding);
		uiElement2.getStyleRule().setPadding(padding);
		
		uiElement1.setPreferredContentWidth(preferredWidth);
		uiElement1.setPreferredContentHeight(preferredHeight);
		uiElement2.setPreferredContentWidth(preferredWidth);
		uiElement2.setPreferredContentHeight(preferredHeight);
		
		renderNode1.setDirty();
		renderNode2.setDirty();
		final LayoutRuleset layoutRuleset = FlexLayoutRuleset.parse("flex-column:xs-12c");
		renderLayer.layout(layoutState, layoutRuleset);
		
		Assert.assertEquals(preferredHeight + (padding * 2f) + (margin * 2f), renderLayer.determinePreferredContentHeight(layoutState));
	}
	
	@Test
	public void testPreferredHeightWrappedWithPaddingAndNegativeMargin() {
		configureParentWithWidth(200f);
		
		final int padding = 20;
		final int margin = -35;
		final float preferredWidth = 150f;
		final float preferredHeight = 200f;
		
		uiElement1.getStyleRule().setMargin(margin);
		uiElement2.getStyleRule().setMargin(margin);
		uiElement1.getStyleRule().setPadding(padding);
		uiElement2.getStyleRule().setPadding(padding);
		
		uiElement1.setPreferredContentWidth(preferredWidth);
		uiElement1.setPreferredContentHeight(preferredHeight);
		uiElement2.setPreferredContentWidth(preferredWidth);
		uiElement2.setPreferredContentHeight(preferredHeight);
		
		renderNode1.setDirty();
		renderNode2.setDirty();
		final LayoutRuleset layoutRuleset = FlexLayoutRuleset.parse("flex-column:xs-12c");
		renderLayer.layout(layoutState, layoutRuleset);
		
		Assert.assertEquals((preferredHeight * 2f) + (padding * 4f) + (margin * 4f), renderLayer.determinePreferredContentHeight(layoutState));
	}
	
	private void configureParentWithWidth(final float parentWidth) {
		mockery.checking(new Expectations() {
			{
				atLeast(1).of(layoutState).getUiContainerRenderTree();
				will(returnValue(renderTree));
				atLeast(1).of(layoutState).getParentWidth();
				will(returnValue(parentWidth));
				atLeast(1).of(layoutState).setParentWidth(with(any(Float.class)));
			}
		});
		parentElement.setPreferredContentWidth(parentWidth);
		parentRenderNode.layout(layoutState);
	}
}
