/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.render;

import org.mini2Dx.ui.element.TabButton;

/**
 *
 */
public class TabButtonRenderNode extends ContentButtonRenderNode {
	
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
