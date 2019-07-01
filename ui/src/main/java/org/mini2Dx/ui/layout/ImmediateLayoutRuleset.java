package org.mini2Dx.ui.layout;

import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.ui.element.ScrollBox;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.RenderNode;

public class ImmediateLayoutRuleset extends LayoutRuleset {
	private final UiElement element;
	private final OffsetRule xRule, yRule;
	private final SizeRule widthRule, heightRule;

	public ImmediateLayoutRuleset(UiElement element) {
		super();
		this.element = element;
		xRule = new ImmediateXRule();
		yRule = new ImmediateYRule();
		widthRule = new ImmediateWidthRule();
		heightRule = new ImmediateHeightRule();
	}

	@Override
	public void layout(LayoutState layoutState, final ParentRenderNode<?, ?> parentNode, Array<RenderNode<?, ?>> children) {
		float startX = parentNode.getStyle().getPaddingLeft();
		float startY = parentNode.getStyle().getPaddingTop();

		for (int i = 0; i < children.size; i++) {
			final RenderNode<?, ?> node = children.get(i);
			node.layout(layoutState);
			if (!node.isIncludedInLayout()) {
				continue;
			}

			node.setRelativeX(startX + node.getXOffset());
			node.setRelativeY(startY + node.getYOffset());

			setElementSize(parentNode, node);
		}
	}

	@Override
	public float getPreferredElementRelativeX(LayoutState layoutState) {
		return xRule.getOffset(layoutState);
	}

	@Override
	public float getPreferredElementRelativeY(LayoutState layoutState) {
		return yRule.getOffset(layoutState);
	}

	@Override
	public float getPreferredElementWidth(LayoutState layoutState) {
		return widthRule.getSize(layoutState);
	}

	@Override
	public float getPreferredElementHeight(LayoutState layoutState) {
		return heightRule.getSize(layoutState);
	}

	@Override
	public boolean isHiddenByInputSource(LayoutState layoutState) {
		return false;
	}

	@Override
	public SizeRule getCurrentWidthRule() {
		return widthRule;
	}

	@Override
	public SizeRule getCurrentHeightRule() {
		return heightRule;
	}

	@Override
	public OffsetRule getCurrentOffsetXRule() {
		return xRule;
	}

	@Override
	public OffsetRule getCurrentOffsetYRule() {
		return yRule;
	}

	@Override
	public boolean isFlexLayout() {
		return false;
	}

	@Override
	public boolean equals(String rules) {
		if(rules == null) {
			return true;
		}
		if(rules.isEmpty()) {
			return true;
		}
		return false;
	}

	private class ImmediateXRule implements OffsetRule {

		@Override
		public float getOffset(LayoutState layoutState) {
			return element.getX();
		}
	}

	private class ImmediateYRule implements OffsetRule {

		@Override
		public float getOffset(LayoutState layoutState) {
			return element.getY();
		}
	}

	private class ImmediateWidthRule implements SizeRule {

		@Override
		public float getSize(LayoutState layoutState) {
			return element.getWidth();
		}

		@Override
		public boolean isAutoSize() {
			return false;
		}
	}

	private class ImmediateHeightRule implements SizeRule {

		@Override
		public float getSize(LayoutState layoutState) {
			if(element instanceof ScrollBox) {
				return ((ScrollBox) element).getScrollContentHeight();
			}
			return element.getHeight();
		}

		@Override
		public boolean isAutoSize() {
			return false;
		}
	}
}
