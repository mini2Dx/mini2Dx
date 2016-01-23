/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.render;

import org.mini2Dx.ui.element.AlignedModal;
import org.mini2Dx.ui.element.Column;
import org.mini2Dx.ui.layout.LayoutState;

/**
 *
 */
public class AlignedModalRenderNode extends ModalRenderNode {

	public AlignedModalRenderNode(ParentRenderNode<?, ?> parent, Column column) {
		super(parent, column);
	}
	
	@Override
	protected float determineXOffset(LayoutState layoutState) {
		switch(((AlignedModal) element).getHorizontalAlignment()) {
		case RIGHT:
			return layoutState.getUiContainer().getWidth() - determinePreferredWidth(layoutState);
		case CENTER:
			return (layoutState.getUiContainer().getWidth() / 2f) - (determinePreferredWidth(layoutState) / 2f);
		default:
			return 0f;
		}
	}

	@Override
	protected float determineYOffset(LayoutState layoutState) {
		switch(((AlignedModal) element).getVerticalAlignment()) {
		case BOTTOM:
			return layoutState.getUiContainer().getHeight() - determinePreferredHeight(layoutState);
		case MIDDLE:
			return (layoutState.getUiContainer().getHeight() / 2f) - (determinePreferredHeight(layoutState) / 2f);
		default:
			return 0f;
		}
	}
}
