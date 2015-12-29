/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.render;

import org.mini2Dx.ui.element.Column;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.style.StyleRule;

/**
 *
 */
public class ColumnRenderNode extends AbstractColumnRenderNode<StyleRule> {

	public ColumnRenderNode(ParentRenderNode<?, ?> parent, Column column) {
		super(parent, column);
	}

	@Override
	protected StyleRule determineStyleRule(LayoutState layoutState) {
		return layoutState.getTheme().getStyleRule(element, layoutState.getScreenSize());
	}
}
