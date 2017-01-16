/**
 * Copyright 2017 Thomas Cashman
 */
package org.mini2Dx.ui.layout;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mini2Dx.ui.dummy.DummyParentRenderNode;
import org.mini2Dx.ui.dummy.DummyParentUiElement;
import org.mini2Dx.ui.dummy.DummyRenderNode;
import org.mini2Dx.ui.dummy.DummyUiElement;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.render.RenderNode;

import junit.framework.Assert;

/**
 * Unit tests for {@link FlexDirection}
 */
public class FlexDirectionTest {
	private static final int TOTAL_CHILDREN = 4;
	
	private LayoutState layoutState;
	
	private final DummyParentUiElement parentElement = new DummyParentUiElement();
	private final List<DummyUiElement> children = new ArrayList<DummyUiElement>(1);
	private final List<RenderNode<?, ?>> childrenRenderNodes = new ArrayList<RenderNode<?, ?>>(1);
	
	private DummyParentRenderNode parentRenderNode;
	
	@Test
	public void testColumn() {
		setUpPreferredSizes(300f, 100f);
		
		FlexDirection.COLUMN.layout(layoutState, parentRenderNode, childrenRenderNodes);
		
		float relativeX = childrenRenderNodes.get(0).getRelativeX();
		float relativeY = childrenRenderNodes.get(0).getRelativeY();
		for(int i = 1; i < childrenRenderNodes.size(); i++) {
			RenderNode<?, ?> nextNode = childrenRenderNodes.get(i);
			if(nextNode.getRelativeY() == relativeY) {
				Assert.assertEquals(true, nextNode.getRelativeX() > relativeX);
				relativeX = nextNode.getRelativeX();
			} else {
				Assert.assertEquals(0f, nextNode.getRelativeX());
				Assert.assertEquals(true, nextNode.getRelativeY() > relativeY);
				relativeX = nextNode.getRelativeX();
				relativeY = nextNode.getRelativeY();
			}
		}
	}
	
	@Test
	public void testColumnReverse() {
		setUpPreferredSizes(300f, 100f);
		
		FlexDirection.COLUMN_REVERSE.layout(layoutState, parentRenderNode, childrenRenderNodes);
		
		float relativeX = childrenRenderNodes.get(0).getRelativeX();
		float relativeY = childrenRenderNodes.get(0).getRelativeY();
		
		for(int i = 1; i < childrenRenderNodes.size(); i++) {
			RenderNode<?, ?> nextNode = childrenRenderNodes.get(i);
			if(nextNode.getRelativeY() == relativeY) {
				Assert.assertEquals(true, relativeX > nextNode.getRelativeX());
				relativeX = nextNode.getRelativeX();
			} else {
				Assert.assertEquals(parentElement.getPreferredContentWidth() - nextNode.getPreferredContentWidth(), nextNode.getRelativeX());
				Assert.assertEquals(true, nextNode.getRelativeY() > relativeY);
				relativeX = nextNode.getRelativeX();
				relativeY = nextNode.getRelativeY();
			}
		}
	}
	
	private void setUpPreferredSizes(float parentWidth, float childSize) {
		parentElement.setPreferredContentWidth(parentWidth);
		parentElement.setVisibility(Visibility.VISIBLE);
		
		for(int i = 0; i < TOTAL_CHILDREN; i++) {
			DummyUiElement element = new DummyUiElement();
			element.setPreferredContentWidth(childSize);
			element.setPreferredContentHeight(childSize);
			element.setVisibility(Visibility.VISIBLE);
			parentElement.add(element);
			children.add(element);
		}
		parentRenderNode = new DummyParentRenderNode(null, parentElement);
		for(int i = 0; i < TOTAL_CHILDREN; i++) {
			DummyRenderNode renderNode = new DummyRenderNode(parentRenderNode, children.get(0));
			childrenRenderNodes.add(renderNode);
		}
	}
}
