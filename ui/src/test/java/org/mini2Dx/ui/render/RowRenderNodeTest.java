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
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.ui.element.Row;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.mock.DummyRenderNode;
import org.mini2Dx.ui.mock.DummyUiElement;
import org.mini2Dx.ui.style.StyleRule;
import org.mini2Dx.ui.style.UiTheme;

import junit.framework.Assert;

/**
 * Unit tests for {@link RowRenderNode}
 */
public class RowRenderNodeTest {
	private Mockery mockery;
	private UiTheme theme;
	private LayoutState layoutState;
	
	private Row row = new Row();
	private RowRenderNode rowRenderNode = new RowRenderNode(null, row);
	
	private DummyUiElement uiElement1 = new DummyUiElement();
	private DummyUiElement uiElement2 = new DummyUiElement();
	
	private DummyRenderNode renderNode1 = new DummyRenderNode(rowRenderNode, uiElement1);
	private DummyRenderNode renderNode2 = new DummyRenderNode(rowRenderNode, uiElement2);
	
	@Before
	public void setUp() {
		mockery = new Mockery();
		mockery.setImposteriser(ClassImposteriser.INSTANCE);
		
		layoutState = mockery.mock(LayoutState.class);
		theme = mockery.mock(UiTheme.class);
		
		row.setVisibility(Visibility.VISIBLE);
		rowRenderNode.addChild(renderNode1);
		rowRenderNode.addChild(renderNode2);
	}
	
	@Test
	public void testPreferredWidth() {
		final float parentWidth = 1000f;
		configureParentWithWidth(parentWidth);
		
		final float preferredWidth = 150f;
		final float preferredHeight = 200f;
		
		uiElement1.setPreferredContentWidth(preferredWidth);
		uiElement1.setPreferredContentHeight(preferredHeight);
		uiElement2.setPreferredContentWidth(preferredWidth);
		uiElement2.setPreferredContentHeight(preferredHeight);
		
		renderNode1.setDirty(true);
		renderNode2.setDirty(true);
		rowRenderNode.layout(layoutState);
		
		Assert.assertEquals(preferredWidth, renderNode1.getPreferredContentWidth());
		Assert.assertEquals(preferredWidth, renderNode2.getPreferredContentWidth());
		Assert.assertEquals(parentWidth, rowRenderNode.getPreferredContentWidth());
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
		
		renderNode1.setDirty(true);
		renderNode2.setDirty(true);
		rowRenderNode.layout(layoutState);
		
		Assert.assertEquals(preferredHeight, rowRenderNode.getPreferredContentHeight());
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
		
		renderNode1.setDirty(true);
		renderNode2.setDirty(true);
		rowRenderNode.layout(layoutState);
		
		Assert.assertEquals(preferredHeight * 2f, rowRenderNode.getPreferredContentHeight());
	}
	
	@Test
	public void testPreferredHeightNonWrappedWithYOffset() {
		configureParentWithWidth(500f);
		
		final float preferredWidth = 150f;
		final float preferredHeight = 200f;
		final float yOffset = 24f;
		
		uiElement1.setYOffset(yOffset);
		uiElement2.setYOffset(yOffset);
		uiElement1.setPreferredContentWidth(preferredWidth);
		uiElement1.setPreferredContentHeight(preferredHeight);
		uiElement2.setPreferredContentWidth(preferredWidth);
		uiElement2.setPreferredContentHeight(preferredHeight);
		
		renderNode1.setDirty(true);
		renderNode2.setDirty(true);
		rowRenderNode.layout(layoutState);
		
		Assert.assertEquals(preferredHeight + yOffset, rowRenderNode.getPreferredContentHeight());
	}
	
	@Test
	public void testPreferredHeightWrappedWithYOffset() {
		configureParentWithWidth(150f);
		
		final float preferredWidth = 150f;
		final float preferredHeight = 200f;
		final float yOffset = 20f;
		
		uiElement1.setYOffset(yOffset);
		uiElement2.setYOffset(yOffset);
		uiElement1.setPreferredContentWidth(preferredWidth);
		uiElement1.setPreferredContentHeight(preferredHeight);
		uiElement2.setPreferredContentWidth(preferredWidth);
		uiElement2.setPreferredContentHeight(preferredHeight);
		
		renderNode1.setDirty(true);
		renderNode2.setDirty(true);
		rowRenderNode.layout(layoutState);
		
		Assert.assertEquals((preferredHeight * 2f) + (yOffset * 2f), rowRenderNode.getPreferredContentHeight());
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
		
		renderNode1.setDirty(true);
		renderNode2.setDirty(true);
		rowRenderNode.layout(layoutState);
		
		Assert.assertEquals(preferredHeight + (padding * 2f), rowRenderNode.getPreferredContentHeight());
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
		
		renderNode1.setDirty(true);
		renderNode2.setDirty(true);
		rowRenderNode.layout(layoutState);
		
		Assert.assertEquals((preferredHeight * 2f) + (padding * 4f), rowRenderNode.getPreferredContentHeight());
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
		
		renderNode1.setDirty(true);
		renderNode2.setDirty(true);
		rowRenderNode.layout(layoutState);
		
		Assert.assertEquals(preferredHeight + (padding * 2f) + (margin * 2f), rowRenderNode.getPreferredContentHeight());
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
		
		renderNode1.setDirty(true);
		renderNode2.setDirty(true);
		rowRenderNode.layout(layoutState);
		
		Assert.assertEquals((preferredHeight * 2f) + (padding * 4f) + (margin * 4f), rowRenderNode.getPreferredContentHeight());
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
		
		renderNode1.setDirty(true);
		renderNode2.setDirty(true);
		rowRenderNode.layout(layoutState);
		
		Assert.assertEquals(preferredHeight + (padding * 2f) + (margin * 2f), rowRenderNode.getPreferredContentHeight());
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
		
		renderNode1.setDirty(true);
		renderNode2.setDirty(true);
		rowRenderNode.layout(layoutState);
		
		Assert.assertEquals((preferredHeight * 2f) + (padding * 4f) + (margin * 4f), rowRenderNode.getPreferredContentHeight());
	}
	
	private void configureParentWithWidth(final float parentWidth) {
		mockery.checking(new Expectations() {
			{
				atLeast(1).of(layoutState).getTheme();
				will(returnValue(theme));
				atLeast(1).of(layoutState).getScreenSize();
				will(returnValue(ScreenSize.XS));
				atLeast(1).of(theme).getStyleRule(with(row), with(ScreenSize.XS));
				will(returnValue(new StyleRule()));
				atLeast(1).of(layoutState).getParentWidth();
				will(returnValue(parentWidth));
				atLeast(1).of(layoutState).getColumnWidth();
				will(returnValue(parentWidth / 12f));
				atLeast(1).of(layoutState).setParentWidth(with(any(Float.class)));
			}
		});
		rowRenderNode.layout(layoutState);
	}
}
