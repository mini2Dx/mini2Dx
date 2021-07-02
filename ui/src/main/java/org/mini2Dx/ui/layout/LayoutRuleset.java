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

import org.mini2Dx.gdx.math.MathUtils;
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
				node.getElement().set(MathUtils.round(node.getRelativeX() - parentNode.getStyle().getPaddingLeft()),
						MathUtils.round(node.getRelativeY() - parentNode.getStyle().getPaddingRight()),
						node.getPreferredOuterWidth(), node.getPreferredOuterHeight());
			} else {
				node.getElement().setXY(MathUtils.round(node.getRelativeX() - parentNode.getStyle().getPaddingLeft()),
						MathUtils.round(node.getRelativeY() - parentNode.getStyle().getPaddingRight()));
			}
		} else {
			if(node.getElement().isFlexLayout()) {
				node.getElement().set(MathUtils.round(node.getRelativeX() - parentNode.getStyle().getPaddingLeft()),
						MathUtils.round(node.getRelativeY() - parentNode.getStyle().getPaddingRight()),
						node.getPreferredOuterWidth(), node.getPreferredOuterHeight());
			}
		}
	}
}
