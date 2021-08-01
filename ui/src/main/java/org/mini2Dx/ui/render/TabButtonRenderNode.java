/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.render;

import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.ui.element.TabButton;
import org.mini2Dx.ui.layout.LayoutState;

/**
 * {@link RenderNode} implementation for {@link TabButton}
 */
public class TabButtonRenderNode extends ButtonRenderNode {
	private final LayoutState deferredLayoutState = new LayoutState();
	
	public TabButtonRenderNode(ParentRenderNode<?, ?> parent, TabButton element) {
		super(parent, element);
	}

	@Override
	public void layout(LayoutState layoutState) {
		deferredLayoutState.reset(layoutState.getUiContainerRenderTree(), layoutState.getAssetManager(),
				layoutState.getTheme(), layoutState.getScreenSize(), layoutState.getTotalColumns(),
				layoutState.getParentWidth(), layoutState.isScreenSizeChanged());
		super.layout(layoutState);
	}

	@Override
	public NodeState getState() {
		if(((TabButton) element).isCurrentTab()) {
			return NodeState.ACTION;
		}
		return super.getState();
	}

	public int getPreferredTabWidth() {
		if(deferredLayoutState == null) {
			return 0;
		}
		if(style == null) {
			return 0;
		}
		float result = determinePreferredContentWidth(deferredLayoutState);
		return MathUtils.round(result + style.getPaddingLeft() + style.getPaddingRight() + style.getMarginLeft()
				+ style.getMarginRight());
	}
}
