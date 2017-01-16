/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.render;

import org.mini2Dx.ui.element.TabButton;

/**
 * {@link RenderNode} implementation for {@link TabButton}
 */
public class TabButtonRenderNode extends ButtonRenderNode {
	
	public TabButtonRenderNode(ParentRenderNode<?, ?> parent, TabButton element) {
		super(parent, element);
	}

	@Override
	public NodeState getState() {
		if(((TabButton) element).isCurrentTab()) {
			return NodeState.ACTION;
		}
		return super.getState();
	}
}
