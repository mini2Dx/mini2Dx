/*******************************************************************************
 * Copyright 2019 See AUTHORS file
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.mini2Dx.ui.layout;

import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.RenderNode;

import org.mini2Dx.gdx.math.MathUtils;

/**
 * Controls the order of child elements
 */
public enum FlexDirection {
	/**
	 * Child elements are ordered left-to-right, top-to-bottom
	 */
	COLUMN {
		@Override
		public void layout(LayoutState layoutState, ParentRenderNode<?, ?> parentNode, Array<RenderNode<?, ?>> children) {
			float startX = parentNode.getStyle().getPaddingLeft();
			float startY = parentNode.getStyle().getPaddingTop();
			
			for (int i = 0; i < children.size; i++) {
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
				LayoutRuleset.setElementSize(parentNode, node);

				startX += node.getPreferredOuterWidth() + node.getXOffset();
			}
		}
	},
	/**
	 * Child elements are ordered right-to-left, top-to-bottom
	 */
	COLUMN_REVERSE {
		@Override
		public void layout(LayoutState layoutState, ParentRenderNode<?, ?> parentNode,
						   Array<RenderNode<?, ?>> children) {
			float startX = parentNode.getStyle().getPaddingLeft() + parentNode.getPreferredContentWidth();
			float startY = parentNode.getStyle().getPaddingTop();
			
			for (int i = 0; i < children.size; i++) {
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

				LayoutRuleset.setElementSize(parentNode, node);
				startX -= node.getPreferredOuterWidth() + node.getXOffset();
			}
		}
	},
	/**
	 * Child elements are ordered top-to-bottom
	 */
	ROW {
		@Override
		public void layout(LayoutState layoutState, ParentRenderNode<?, ?> parentNode,
						   Array<RenderNode<?, ?>> children) {
			float startX = parentNode.getStyle().getPaddingLeft();
			float startY = parentNode.getStyle().getPaddingTop();
			
			for (int i = 0; i < children.size; i++) {
				RenderNode<?, ?> node = children.get(i);
				node.layout(layoutState);
				if (!node.isIncludedInLayout()) {
					continue;
				}

				node.setRelativeX(startX + node.getXOffset());
				node.setRelativeY(startY + node.getYOffset());
				LayoutRuleset.setElementSize(parentNode, node);
				startY += node.getPreferredOuterHeight() + node.getYOffset();
			}
		}
	},
	/**
	 * Child elements are ordered bottom-to-top
	 */
	ROW_REVERSE {
		@Override
		public void layout(LayoutState layoutState, ParentRenderNode<?, ?> parentNode,
						   Array<RenderNode<?, ?>> children) {
			final float startX = parentNode.getStyle().getPaddingLeft();
			if(parentNode.getLayoutRuleset().getCurrentHeightRule().isAutoSize()) {
				float startY = parentNode.getStyle().getPaddingTop();
				
				for (int i = children.size - 1; i >= 0; i--) {
					RenderNode<?, ?> node = children.get(i);
					node.layout(layoutState);
					if (!node.isIncludedInLayout()) {
						continue;
					}

					node.setRelativeX(startX + node.getXOffset());
					node.setRelativeY(startY + node.getYOffset());
					LayoutRuleset.setElementSize(parentNode, node);
					startY += node.getPreferredOuterHeight() + node.getYOffset();
				}
			} else {
				float startY = parentNode.getPreferredContentHeight();
				
				for (int i = 0; i < children.size; i++) {
					RenderNode<?, ?> node = children.get(i);
					node.layout(layoutState);
					if (!node.isIncludedInLayout()) {
						continue;
					}
					startY -= node.getPreferredOuterHeight() + node.getYOffset();

					node.setRelativeX(startX + node.getXOffset());
					node.setRelativeY(startY + node.getYOffset());
					LayoutRuleset.setElementSize(parentNode, node);
				}
			}
		}
	},
	/**
	 * Child elements are placed on top of each other in the center of the parent
	 */
	CENTER {
		@Override
		public void layout(LayoutState layoutState, final ParentRenderNode<?, ?> parentNode,
						   Array<RenderNode<?, ?>> children) {
			final float paddingLeft = parentNode.getStyle().getPaddingLeft();
			final float paddingRight = parentNode.getStyle().getPaddingRight();
			final float paddingTop = parentNode.getStyle().getPaddingTop();
			final float paddingBottom = parentNode.getStyle().getPaddingBottom();
			
			int maxHeight = 0;
			if(parentNode.getLayoutRuleset().getCurrentHeightRule().isAutoSize()) {
				for (int i = 0; i < children.size; i++) {
					RenderNode<?, ?> node = children.get(i);
					node.layout(layoutState);
					if (!node.isIncludedInLayout()) {
						continue;
					}
					if(node.getOuterRenderHeight() > maxHeight) {
						maxHeight = node.getOuterRenderHeight();
					}
				}
			} else {
				for (int i = 0; i < children.size; i++) {
					RenderNode<?, ?> node = children.get(i);
					node.layout(layoutState);
				}
				maxHeight = MathUtils.round(parentNode.getPreferredContentHeight());
			}
			
			int centerX = MathUtils.round((paddingLeft + paddingRight + parentNode.getPreferredContentWidth()) / 2f);
			int centerY = MathUtils.round((paddingTop + paddingBottom + maxHeight) / 2);
			for (int i = 0; i < children.size; i++) {
				final RenderNode<?, ?> node = children.get(i);
				if (!node.isIncludedInLayout()) {
					continue;
				}
				node.setRelativeX(centerX - (node.getXOffset() / 2f) - (node.getPreferredOuterWidth() / 2f));
				node.setRelativeY(centerY - (node.getYOffset() / 2f) - (node.getPreferredOuterHeight() / 2f));
				LayoutRuleset.setElementSize(parentNode, node);
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
	public abstract void layout(LayoutState layoutState, ParentRenderNode<?, ?> parentNode, Array<RenderNode<?, ?>> children);
}
