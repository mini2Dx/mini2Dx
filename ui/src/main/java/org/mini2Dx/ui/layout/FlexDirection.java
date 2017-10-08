/**
 * Copyright (c) 2017 See AUTHORS file
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

import java.util.List;

import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.RenderNode;

/**
 * Controls the order of child elements
 */
public enum FlexDirection {
	/**
	 * Elements are ordered left-to-right, top-to-bottom
	 */
	COLUMN {
		@Override
		public void layout(LayoutState layoutState, ParentRenderNode<?, ?> parentNode, List<RenderNode<?, ?>> children) {
			float startX = parentNode.getStyle().getPaddingLeft();
			float startY = parentNode.getStyle().getPaddingTop();
			
			for (int i = 0; i < children.size(); i++) {
				RenderNode<?, ?> node = children.get(i);
				node.layout(layoutState);
				if (!node.isIncludedInLayout()) {
					continue;
				}
				
				if(startX - parentNode.getStyle().getPaddingLeft() + node.getXOffset() + node.getPreferredOuterWidth() > parentNode.getPreferredContentWidth()) {
					float maxHeight = 0f;
					for (int j = i - 1; j >= 0; j--) {
						RenderNode<?, ?> previousNode = children.get(j);
						if (!previousNode.isIncludedInLayout()) {
							continue;
						}
						if (previousNode.getRelativeY() == startY + previousNode.getYOffset()) {
							float height = previousNode.getPreferredOuterHeight() + node.getYOffset();
							if (height > maxHeight) {
								maxHeight = height;
							}
						}
					}
					startY += maxHeight;
					startX = parentNode.getStyle().getPaddingLeft();
				}

				node.setRelativeX(startX + node.getXOffset());
				node.setRelativeY(startY + node.getYOffset());
				startX += node.getPreferredOuterWidth() + node.getXOffset();
			}
		}
	},
	/**
	 * Elements are ordered right-to-left, top-to-bottom
	 */
	COLUMN_REVERSE {
		@Override
		public void layout(LayoutState layoutState, ParentRenderNode<?, ?> parentNode,
				List<RenderNode<?, ?>> children) {
			float startX = parentNode.getStyle().getPaddingLeft() + parentNode.getPreferredContentWidth();
			float startY = parentNode.getStyle().getPaddingTop();
			
			for (int i = 0; i < children.size(); i++) {
				RenderNode<?, ?> node = children.get(i);
				node.layout(layoutState);
				if (!node.isIncludedInLayout()) {
					continue;
				}
				
				if(startX - parentNode.getStyle().getPaddingLeft() - node.getXOffset() - node.getPreferredOuterWidth() < 0f) {
					float maxHeight = 0f;
					for (int j = i - 1; j >= 0; j--) {
						RenderNode<?, ?> previousNode = children.get(j);
						if (!previousNode.isIncludedInLayout()) {
							continue;
						}
						if (previousNode.getRelativeY() == startY + previousNode.getYOffset()) {
							float height = previousNode.getPreferredOuterHeight() + node.getYOffset();
							if (height > maxHeight) {
								maxHeight = height;
							}
						}
					}
					startY += maxHeight;
					startX = parentNode.getStyle().getPaddingLeft() + parentNode.getPreferredContentWidth();
				}

				node.setRelativeX(startX - (node.getXOffset() + node.getPreferredOuterWidth()));
				node.setRelativeY(startY + node.getYOffset());
				startX -= node.getPreferredOuterWidth() + node.getXOffset();
			}
		}
	},
	/**
	 * Elements are ordered top-to-bottom
	 */
	ROW {
		@Override
		public void layout(LayoutState layoutState, ParentRenderNode<?, ?> parentNode,
				List<RenderNode<?, ?>> children) {
			float startX = parentNode.getStyle().getPaddingLeft();
			float startY = parentNode.getStyle().getPaddingTop();
			
			for (int i = 0; i < children.size(); i++) {
				RenderNode<?, ?> node = children.get(i);
				node.layout(layoutState);
				if (!node.isIncludedInLayout()) {
					continue;
				}

				node.setRelativeX(startX + node.getXOffset());
				node.setRelativeY(startY + node.getYOffset());
				startY += node.getPreferredOuterHeight() + node.getYOffset();
			}
		}
	},
	/**
	 * Elements are ordered bottom-to-top
	 */
	ROW_REVERSE {
		@Override
		public void layout(LayoutState layoutState, ParentRenderNode<?, ?> parentNode,
				List<RenderNode<?, ?>> children) {
			float startX = parentNode.getStyle().getPaddingLeft();
			float startY = parentNode.getStyle().getPaddingTop();
			
			for (int i = children.size() - 1; i >= 0; i--) {
				RenderNode<?, ?> node = children.get(i);
				node.layout(layoutState);
				if (!node.isIncludedInLayout()) {
					continue;
				}

				node.setRelativeX(startX + node.getXOffset());
				node.setRelativeY(startY + node.getYOffset());
				startY += node.getPreferredOuterHeight() + node.getYOffset();
			}
		}
	};
	
	/**
	 * Executes the layout of children inside a parent render node
	 * 
	 * @param layoutState The current {@link LayoutState}
	 * @param parentNode The parent render node
	 * @param children The children of the parent that require layout
	 */
	public abstract void layout(LayoutState layoutState, ParentRenderNode<?, ?> parentNode, List<RenderNode<?, ?>> children);
}
