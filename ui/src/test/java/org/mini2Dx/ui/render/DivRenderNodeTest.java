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

import org.mini2Dx.gdx.utils.Array;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.ui.InputSource;
import org.mini2Dx.ui.dummy.DummyRenderNode;
import org.mini2Dx.ui.dummy.DummyUiElement;
import org.mini2Dx.ui.element.Div;
import org.mini2Dx.ui.element.FlexRow;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.style.ParentStyleRule;
import org.mini2Dx.ui.style.UiTheme;

import junit.framework.Assert;

import java.util.List;

/**
 * Unit tests for {@link DivRenderNode}
 */
public class DivRenderNodeTest {
	private Mockery mockery;
	private UiContainerRenderTree renderTree;
	private UiTheme theme;
	
	private LayoutState layoutState;
	
	private Div div = new Div("column1");
	private DivRenderNode divRenderNode = new DivRenderNode(null, div);
	
	private FlexRow flexRow1 = new FlexRow("flexRow1");
	private FlexRow flexRow2 = new FlexRow("flexRow2");
	private DivRenderNode rowRenderNode1 = new DivRenderNode(divRenderNode, flexRow1);
	private DivRenderNode rowRenderNode2 = new DivRenderNode(divRenderNode, flexRow2);
	
	private DummyUiElement uiElement1 = new DummyUiElement("uiElement1");
	private DummyUiElement uiElement2 = new DummyUiElement("uiElement2");
	private DummyUiElement uiElement3 = new DummyUiElement("uiElement3");
	private DummyUiElement uiElement4 = new DummyUiElement("uiElement4");
	
	private DummyRenderNode renderNode1 = new DummyRenderNode(rowRenderNode1, uiElement1);
	private DummyRenderNode renderNode2 = new DummyRenderNode(rowRenderNode1, uiElement2);
	private DummyRenderNode renderNode3 = new DummyRenderNode(rowRenderNode2, uiElement3);
	private DummyRenderNode renderNode4 = new DummyRenderNode(rowRenderNode2, uiElement4);
	
	@Before
	public void setUp() {
		mockery = new Mockery();
		mockery.setImposteriser(ClassImposteriser.INSTANCE);
		
		theme = mockery.mock(UiTheme.class);
		renderTree = mockery.mock(UiContainerRenderTree.class);
		div.setFlexLayout("flex-col:xs-3c");
		
		div.setVisibility(Visibility.VISIBLE);
		flexRow1.setVisibility(Visibility.VISIBLE);
		flexRow2.setVisibility(Visibility.VISIBLE);
		
		rowRenderNode1.addChild(renderNode1);
		rowRenderNode1.addChild(renderNode2);
		rowRenderNode2.addChild(renderNode3);
		rowRenderNode2.addChild(renderNode4);

		divRenderNode.addChild(rowRenderNode1);
		divRenderNode.addChild(rowRenderNode2);

		mockery.checking(new Expectations() {
			{
				atLeast(1).of(renderTree).transferLayoutDeferred(with(any(Array.class)));
			}
		});
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
		uiElement3.setPreferredContentWidth(preferredWidth);
		uiElement3.setPreferredContentHeight(preferredHeight);
		uiElement4.setPreferredContentWidth(preferredWidth);
		uiElement4.setPreferredContentHeight(preferredHeight);
		
		renderNode1.setDirty();
		renderNode2.setDirty();
		divRenderNode.layout(layoutState);
		
		Assert.assertEquals(preferredWidth, uiElement1.getPreferredContentWidth());
		Assert.assertEquals(preferredWidth, uiElement2.getPreferredContentWidth());
		Assert.assertEquals(parentWidth / 4f, rowRenderNode1.getPreferredContentWidth());
		Assert.assertEquals(parentWidth / 4f, rowRenderNode2.getPreferredContentWidth());
		Assert.assertEquals(parentWidth / 4f, divRenderNode.getPreferredContentWidth());
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
		uiElement3.setPreferredContentWidth(preferredWidth);
		uiElement3.setPreferredContentHeight(preferredHeight);
		uiElement4.setPreferredContentWidth(preferredWidth);
		uiElement4.setPreferredContentHeight(preferredHeight);
		
		renderNode1.setDirty();
		renderNode2.setDirty();
		divRenderNode.layout(layoutState);
		
		Assert.assertEquals(preferredHeight, renderNode1.getPreferredContentHeight());
		Assert.assertEquals(preferredHeight, renderNode2.getPreferredContentHeight());
		Assert.assertEquals(preferredHeight, rowRenderNode1.getPreferredContentHeight());
		Assert.assertEquals(preferredHeight, rowRenderNode2.getPreferredContentHeight());
		Assert.assertEquals((preferredHeight * 2f), divRenderNode.getPreferredContentHeight());
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
		uiElement3.setPreferredContentWidth(preferredWidth);
		uiElement3.setPreferredContentHeight(preferredHeight);
		uiElement4.setPreferredContentWidth(preferredWidth);
		uiElement4.setPreferredContentHeight(preferredHeight);
		
		renderNode1.setDirty();
		renderNode2.setDirty();
		divRenderNode.layout(layoutState);
		
		Assert.assertEquals(preferredHeight, renderNode1.getPreferredContentHeight());
		Assert.assertEquals(preferredHeight, renderNode2.getPreferredContentHeight());
		Assert.assertEquals((preferredHeight * 2f), rowRenderNode1.getPreferredContentHeight());
		Assert.assertEquals((preferredHeight * 2f), rowRenderNode2.getPreferredContentHeight());
		Assert.assertEquals((preferredHeight * 4f), divRenderNode.getPreferredContentHeight());
	}
	
	private void configureParentWithWidth(final float parentWidth) {
		layoutState = new LayoutState(renderTree, null, theme, ScreenSize.XS, 12, parentWidth, true);
		mockery.checking(new Expectations() {
			{
				atLeast(1).of(theme).getId();
				will(returnValue("mock"));
				atLeast(1).of(renderTree).getScreenSizeScale();
				will(returnValue(1f));
				atLeast(1).of(theme).getStyleRule(with(any(Div.class)), with(ScreenSize.XS));
				will(returnValue(new ParentStyleRule()));
				atLeast(1).of(theme).getStyleRule(with(any(FlexRow.class)), with(ScreenSize.XS));
				will(returnValue(new ParentStyleRule()));
				atLeast(1).of(renderTree).getLastInputSource();
				will(returnValue(InputSource.KEYBOARD_MOUSE));
			}
		});
		divRenderNode.layout(layoutState);
	}
}
