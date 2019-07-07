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
package org.mini2Dx.ui.render;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.ui.element.ProgressBar;
import org.mini2Dx.ui.layout.FlexLayoutRuleset;
import org.mini2Dx.ui.layout.ImmediateLayoutRuleset;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.layout.LayoutRuleset;
import org.mini2Dx.ui.style.ProgressBarStyleRule;

import org.mini2Dx.gdx.math.MathUtils;

/**
 * {@link RenderNode} implementation for {@link ProgressBar}
 */
public class ProgressBarRenderNode extends RenderNode<ProgressBar, ProgressBarStyleRule> {
	protected LayoutRuleset layoutRuleset;
	private float multiplier;
	private float fillWidth;

	public ProgressBarRenderNode(ParentRenderNode<?, ?> parent, ProgressBar element) {
		super(parent, element);
		initLayoutRuleset();
		multiplier = element.getValue() / element.getMax();
	}

	protected void initLayoutRuleset() {
		if(element.getFlexLayout() != null) {
			layoutRuleset = FlexLayoutRuleset.parse(element.getFlexLayout());
		} else {
			layoutRuleset = new ImmediateLayoutRuleset(element);
		}
	}

	@Override
	public void layout(LayoutState layoutState) {
		if (!layoutRuleset.equals(element.getFlexLayout())) {
			initLayoutRuleset();
		}
		super.layout(layoutState);
	}

	@Override
	protected void renderElement(Graphics g) {
		if (style.getBackground() != null) {
			style.getBackgroundRenderer().render(g, getInnerRenderX(), getInnerRenderY(), getInnerRenderWidth(),
					getInnerRenderHeight());
		}
		style.getFillRenderer().render(g, getContentRenderX(), getContentRenderY(), fillWidth,
				getContentRenderHeight());
	}

	@Override
	protected ProgressBarStyleRule determineStyleRule(LayoutState layoutState) {
		return layoutState.getTheme().getStyleRule(element, layoutState.getScreenSize());
	}

	@Override
	protected float determinePreferredContentWidth(LayoutState layoutState) {
		if (layoutRuleset.isHiddenByInputSource(layoutState)) {
			return 0f;
		}
		float layoutRuleResult = layoutRuleset.getPreferredElementWidth(layoutState);
		if (layoutRuleResult <= 0f) {
			hiddenByLayoutRule = true;
			return 0f;
		} else {
			hiddenByLayoutRule = false;
		}
		float result = layoutRuleResult - style.getPaddingLeft() - style.getPaddingRight() - style.getMarginLeft()
				- style.getMarginRight();
		fillWidth = MathUtils.round(result * multiplier);
		return result;
	}

	@Override
	protected float determinePreferredContentHeight(LayoutState layoutState) {
		if (preferredContentWidth <= 0f) {
			return 0f;
		}
		float preferredHeight = style.getMinHeight() - style.getPaddingTop()
				- style.getPaddingBottom() - style.getMarginTop() - style.getMarginBottom();

		float sizeRuleHeight = layoutRuleset.getPreferredElementHeight(layoutState) - style.getPaddingTop()
				- style.getPaddingBottom() - style.getMarginTop() - style.getMarginBottom();
		if (!layoutRuleset.getCurrentHeightRule().isAutoSize()) {
			preferredHeight = Math.max(preferredHeight, sizeRuleHeight);
		}
		return preferredHeight;
	}

	@Override
	protected float determineXOffset(LayoutState layoutState) {
		return layoutRuleset.getPreferredElementRelativeX(layoutState);
	}

	@Override
	protected float determineYOffset(LayoutState layoutState) {
		return layoutRuleset.getPreferredElementRelativeY(layoutState);
	}

	public void updateFillWidth() {
		multiplier = element.getValue() / element.getMax();
		fillWidth = MathUtils.round(getContentRenderWidth() * multiplier);
	}
}
