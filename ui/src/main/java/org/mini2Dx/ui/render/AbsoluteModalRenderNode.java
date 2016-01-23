/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.render;

import org.mini2Dx.ui.element.AbsoluteContainer;
import org.mini2Dx.ui.element.AbsoluteModal;
import org.mini2Dx.ui.element.Column;
import org.mini2Dx.ui.layout.LayoutState;

/**
 *
 */
public class AbsoluteModalRenderNode extends ModalRenderNode {

	public AbsoluteModalRenderNode(ParentRenderNode<?, ?> parent, Column column) {
		super(parent, column);
	}

	@Override
	protected float determineXOffset(LayoutState layoutState) {
		return ((AbsoluteModal) element).getX();
	}

	@Override
	protected float determineYOffset(LayoutState layoutState) {
		return ((AbsoluteModal) element).getY();
	}
}
