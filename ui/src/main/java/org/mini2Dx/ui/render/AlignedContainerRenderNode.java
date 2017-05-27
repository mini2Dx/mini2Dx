/**
 * Copyright (c) 2016 See AUTHORS file
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

import org.mini2Dx.ui.element.AlignedContainer;
import org.mini2Dx.ui.element.Column;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.style.ContainerStyleRule;

import com.badlogic.gdx.math.MathUtils;

/**
 * {@link RenderNode} implementation for {@link AlignedContainer}
 */
public class AlignedContainerRenderNode extends ContainerRenderNode {

	public AlignedContainerRenderNode(ParentRenderNode<?, ?> parent, Column column) {
		super(parent, column);
	}

	@Override
	protected float determineXOffset(LayoutState layoutState) {
		float outerWidth = determinePreferredContentWidth(layoutState) + style.getPaddingLeft()
				+ style.getPaddingRight() + style.getMarginLeft() + style.getMarginRight();
		switch (((AlignedContainer) element).getHorizontalAlignment()) {
		case RIGHT:
			return layoutState.getUiContainerRenderTree().getOuterWidth() - outerWidth;
		case CENTER:
			return MathUtils.round((layoutState.getUiContainerRenderTree().getOuterWidth() / 2f) - (outerWidth / 2f));
		default:
			return 0f;
		}
	}

	@Override
	protected float determineYOffset(LayoutState layoutState) {
		float outerHeight = determinePreferredContentHeight(layoutState) + style.getPaddingTop()
				+ style.getPaddingBottom() + style.getMarginTop() + style.getMarginBottom();
		switch (((AlignedContainer) element).getVerticalAlignment()) {
		case BOTTOM:
			return layoutState.getUiContainerRenderTree().getOuterHeight() - outerHeight;
		case MIDDLE:
			return MathUtils.round((layoutState.getUiContainerRenderTree().getOuterHeight() / 2f) - (outerHeight / 2f));
		default:
			return 0f;
		}
	}

	@Override
	protected ContainerStyleRule determineStyleRule(LayoutState layoutState) {
		return layoutState.getTheme().getStyleRule(((AlignedContainer) element), layoutState.getScreenSize(),
				layoutState.getScreenSizeScale());
	}

}
