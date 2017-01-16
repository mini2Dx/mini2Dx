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
import org.mini2Dx.ui.InputSource;
import org.mini2Dx.ui.dummy.DummyRenderNode;
import org.mini2Dx.ui.dummy.DummyUiElement;
import org.mini2Dx.ui.element.Column;
import org.mini2Dx.ui.element.Row;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.layout.LayoutRuleset;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.style.ParentStyleRule;
import org.mini2Dx.ui.style.UiTheme;

import junit.framework.Assert;

/**
 * Unit tests for {@link ColumnRenderNode}
 */
public class ColumnRenderNodeTest {
	private Mockery mockery;
	private UiContainerRenderTree uiContainer;
	private UiTheme theme;
	
	private LayoutState layoutState;
	
	private Column column = new Column("column1");
	private ColumnRenderNode columnRenderNode = new ColumnRenderNode(null, column);
	
	private Row row1 = new Row("row1");
	private Row row2 = new Row("row2");
	private RowRenderNode rowRenderNode1 = new RowRenderNode(columnRenderNode, row1);
	private RowRenderNode rowRenderNode2 = new RowRenderNode(columnRenderNode, row2);
	
	private DummyUiElement uiElement1 = new DummyUiElement("uiElement1");
	private DummyUiElement uiElement2 = new DummyUiElement("uiElement1");
	
	private DummyRenderNode renderNode1 = new DummyRenderNode(rowRenderNode1, uiElement1);
	private DummyRenderNode renderNode2 = new DummyRenderNode(rowRenderNode1, uiElement2);
	
	@Before
	public void setUp() {
		mockery = new Mockery();
		mockery.setImposteriser(ClassImposteriser.INSTANCE);
		
		theme = mockery.mock(UiTheme.class);
		uiContainer = mockery.mock(UiContainerRenderTree.class);
		column.setLayout("xs-3c");
		
		column.setVisibility(Visibility.VISIBLE);
		row1.setVisibility(Visibility.VISIBLE);
		row2.setVisibility(Visibility.VISIBLE);
		
		rowRenderNode1.addChild(renderNode1);
		rowRenderNode1.addChild(renderNode2);
		rowRenderNode2.addChild(renderNode1);
		rowRenderNode2.addChild(renderNode2);
		
		columnRenderNode.addChild(rowRenderNode1);
		columnRenderNode.addChild(rowRenderNode2);
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
		columnRenderNode.layout(layoutState);
		
		Assert.assertEquals(preferredWidth, uiElement1.getPreferredContentWidth());
		Assert.assertEquals(preferredWidth, uiElement2.getPreferredContentWidth());
		Assert.assertEquals(parentWidth / 4f, rowRenderNode1.getPreferredContentWidth());
		Assert.assertEquals(parentWidth / 4f, rowRenderNode2.getPreferredContentWidth());
		Assert.assertEquals(parentWidth / 4f, columnRenderNode.getPreferredContentWidth());
	}

	@Test
	public void testPreferredHeightNonWrapped() {
		final float parentWidth = 2000f;
		configureParentWithWidth(parentWidth);
		
		final float preferredWidth = 150f;
		final float preferredHeight = 200f;
		
		uiElement1.setPreferredContentWidth(preferredWidth);
		uiElement1.setPreferredContentHeight(preferredHeight);
		uiElement2.setPreferredContentWidth(preferredWidth);
		uiElement2.setPreferredContentHeight(preferredHeight);
		
		renderNode1.setDirty(true);
		renderNode2.setDirty(true);
		columnRenderNode.layout(layoutState);
		
		Assert.assertEquals(preferredHeight, renderNode1.getPreferredContentHeight());
		Assert.assertEquals(preferredHeight, renderNode2.getPreferredContentHeight());
		Assert.assertEquals(preferredHeight, rowRenderNode1.getPreferredContentHeight());
		Assert.assertEquals(preferredHeight, rowRenderNode2.getPreferredContentHeight());
		Assert.assertEquals((preferredHeight * 2f), columnRenderNode.getPreferredContentHeight());
	}
	
	@Test
	public void testPreferredHeightWrapped() {
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
		columnRenderNode.layout(layoutState);
		
		Assert.assertEquals(preferredHeight, renderNode1.getPreferredContentHeight());
		Assert.assertEquals(preferredHeight, renderNode2.getPreferredContentHeight());
		Assert.assertEquals((preferredHeight * 2f), rowRenderNode1.getPreferredContentHeight());
		Assert.assertEquals((preferredHeight * 2f), rowRenderNode2.getPreferredContentHeight());
		Assert.assertEquals((preferredHeight * 4f), columnRenderNode.getPreferredContentHeight());
	}
	
	private void configureParentWithWidth(final float parentWidth) {
		layoutState = new LayoutState(uiContainer, null, theme, ScreenSize.XS, 12, parentWidth, true);
		mockery.checking(new Expectations() {
			{
				atLeast(1).of(theme).getId();
				will(returnValue("mock"));
				atLeast(1).of(theme).getStyleRule(with(any(Column.class)), with(ScreenSize.XS));
				will(returnValue(new ParentStyleRule()));
				atLeast(1).of(theme).getStyleRule(with(any(Row.class)), with(ScreenSize.XS));
				will(returnValue(new ParentStyleRule()));
				atLeast(1).of(uiContainer).getLastInputSource();
				will(returnValue(InputSource.KEYBOARD_MOUSE));
			}
		});
		columnRenderNode.layout(layoutState);
	}
}
