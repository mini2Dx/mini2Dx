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
package org.mini2Dx.ui.layout;

import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.RenderNode;

/**
 * The size and offset ruleset of a {@link UiElement} for different
 * {@link ScreenSize}s
 */
public abstract class LayoutRuleset {

	public abstract void layout(LayoutState layoutState, ParentRenderNode<?, ?> parentNode, Array<RenderNode<?, ?>> children);

	public abstract float getPreferredElementRelativeX(LayoutState layoutState);

	public abstract float getPreferredElementRelativeY(LayoutState layoutState);

	public abstract float getPreferredElementWidth(LayoutState layoutState);

	public abstract float getPreferredElementHeight(LayoutState layoutState);

	public abstract boolean isHiddenByInputSource(LayoutState layoutState);

	public abstract SizeRule getCurrentWidthRule();

	public abstract SizeRule getCurrentHeightRule();

	public abstract OffsetRule getCurrentOffsetXRule();

	public abstract OffsetRule getCurrentOffsetYRule();

	public abstract boolean isFlexLayout();

	public abstract boolean equals(String rules);

	public static void setElementSize(final ParentRenderNode<?, ?> parentNode, final RenderNode<?, ?> node) {
		if(!node.isInitialLayoutOccurred()) {
			node.getElement().deferUntilLayout(new Runnable() {
				@Override
				public void run() {
					setElementSize(parentNode, node);
				}
			}, true);
			return;
		}
		if(parentNode.getElement().isFlexLayout()) {
			if(node.getElement().isFlexLayout()) {
				node.getElement().set(node.getRelativeX() - parentNode.getStyle().getPaddingLeft(), node.getRelativeY() - parentNode.getStyle().getPaddingRight(), node.getPreferredOuterWidth(), node.getPreferredOuterHeight());
			} else {
				node.getElement().setXY(node.getRelativeX() - parentNode.getStyle().getPaddingLeft(), node.getRelativeY() - parentNode.getStyle().getPaddingRight());
			}
		} else {
			if(node.getElement().isFlexLayout()) {
				node.getElement().set(node.getRelativeX() - parentNode.getStyle().getPaddingLeft(), node.getRelativeY() - parentNode.getStyle().getPaddingRight(), node.getPreferredOuterWidth(), node.getPreferredOuterHeight());
			}
		}
	}
}
