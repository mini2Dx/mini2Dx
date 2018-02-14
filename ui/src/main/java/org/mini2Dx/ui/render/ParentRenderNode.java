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
package org.mini2Dx.ui.render;

import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.TreeMap;

import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.ui.element.ParentUiElement;
import org.mini2Dx.ui.layout.FlexDirection;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.layout.LayoutRuleset;
import org.mini2Dx.ui.style.ParentStyleRule;

import com.badlogic.gdx.math.MathUtils;

/**
 * Base class for {@link RenderNode} implementations that contains child nodes
 */
public abstract class ParentRenderNode<T extends ParentUiElement, S extends ParentStyleRule> extends RenderNode<T, S> {
	protected final NavigableMap<Integer, RenderLayer> layers = new TreeMap<Integer, RenderLayer>();
	protected final Rectangle cachedClip = new Rectangle();
	
	protected boolean childDirty = false;
	protected FlexDirection flexDirection = FlexDirection.COLUMN;
	protected LayoutRuleset horizontalLayoutRuleset, verticalLayoutRuleset;

	public ParentRenderNode(ParentRenderNode<?, ?> parent, T element) {
		super(parent, element);
		horizontalLayoutRuleset = new LayoutRuleset(true, element.getHorizontalLayout());
		verticalLayoutRuleset = new LayoutRuleset(false, element.getVerticalLayout());
	}

	@Override
	public void update(UiContainerRenderTree uiContainer, float delta) {
		super.update(uiContainer, delta);
		for (RenderLayer layer : layers.values()) {
			layer.update(uiContainer, delta);
		}
	}

	@Override
	public void interpolate(float alpha) {
		super.interpolate(alpha);
		for (RenderLayer layer : layers.values()) {
			layer.interpolate(alpha);
		}
	}

	@Override
	protected void renderElement(Graphics g) {
		boolean overflowClipped = element.isOverflowClipped();
		g.peekClip(cachedClip);
		if (overflowClipped) {
			g.setClip(outerArea);
		}
		if (style.getBackgroundNinePatch() != null) {
			g.drawNinePatch(style.getBackgroundNinePatch(), getInnerRenderX(), getInnerRenderY(), getInnerRenderWidth(),
					getInnerRenderHeight());
		}
		for (RenderLayer layer : layers.values()) {
			layer.render(g);
		}
		if (overflowClipped) {
			g.setClip(cachedClip);
		}
	}

	@Override
	public void layout(LayoutState layoutState) {
		if (!isDirty() && !layoutState.isScreenSizeChanged()) {
			return;
		}
		if (!horizontalLayoutRuleset.equals(element.getHorizontalLayout())) {
			horizontalLayoutRuleset = new LayoutRuleset(true, element.getHorizontalLayout());
		}
		if (!verticalLayoutRuleset.equals(element.getVerticalLayout())) {
			verticalLayoutRuleset = new LayoutRuleset(false, element.getVerticalLayout());
		}

		float parentWidth = layoutState.getParentWidth();
		rootNode = layoutState.getUiContainerRenderTree();
		style = determineStyleRule(layoutState);

		if (this.zIndex != element.getZIndex()) {
			parent.removeChild(this);
			zIndex = element.getZIndex();
			parent.addChild(this);
		}

		flexDirection = element.getFlexDirection();
		xOffset = determineXOffset(layoutState);
		preferredContentWidth = determinePreferredContentWidth(layoutState);
		layoutState.setParentWidth(getPreferredContentWidth());

		verticalLayoutRuleset.getPreferredSize(layoutState);
		if (!verticalLayoutRuleset.getCurrentSizeRule().isAutoSize()) {
			preferredContentHeight = determinePreferredContentHeight(layoutState);
		}

		for (RenderLayer layer : layers.values()) {
			layer.layout(layoutState);
		}

		layoutState.setParentWidth(parentWidth);

		yOffset = determineYOffset(layoutState);
		if (verticalLayoutRuleset.getCurrentSizeRule().isAutoSize()) {
			preferredContentHeight = determinePreferredContentHeight(layoutState);
		}
		setImmediateDirty(false);
		setDirty(false);
		childDirty = false;
		initialLayoutOccurred = true;
	}

	@Override
	protected float determinePreferredContentHeight(LayoutState layoutState) {
		if (preferredContentWidth <= 0f) {
			return 0f;
		}
		float maxHeight = 0f;
		float sizeRuleHeight = verticalLayoutRuleset.getPreferredSize(layoutState);

		if (verticalLayoutRuleset.getCurrentSizeRule().isAutoSize()) {
			for (RenderLayer layer : layers.values()) {
				float height = layer.determinePreferredContentHeight(layoutState);
				if (height > maxHeight) {
					maxHeight = height;
				}
			}
		} else {
			maxHeight = sizeRuleHeight - style.getPaddingTop() - style.getPaddingBottom() - style.getMarginTop()
					- style.getMarginBottom();
		}
		if (style.getMinHeight() > 0 && maxHeight + style.getPaddingTop() + style.getPaddingBottom() + style.getMarginTop()
				+ style.getMarginBottom() < style.getMinHeight()) {
			return style.getMinHeight() - style.getPaddingTop() - style.getPaddingBottom() - style.getMarginTop()
					- style.getMarginBottom();
		}
		return maxHeight;
	}

	@Override
	protected float determinePreferredContentWidth(LayoutState layoutState) {
		if (horizontalLayoutRuleset.isHiddenByInputSource(layoutState)) {
			return 0f;
		}
		float layoutRuleResult = horizontalLayoutRuleset.getPreferredSize(layoutState);
		if (layoutRuleResult <= 0f) {
			hiddenByLayoutRule = true;
			return 0f;
		} else {
			hiddenByLayoutRule = false;
		}
		float result = layoutRuleResult - style.getPaddingLeft() - style.getPaddingRight() - style.getMarginLeft()
				- style.getMarginRight();
		return style.getRounding().calculateRounding(result);
	}

	@Override
	protected float determineXOffset(LayoutState layoutState) {
		return horizontalLayoutRuleset.getOffset(layoutState);
	}

	@Override
	protected float determineYOffset(LayoutState layoutState) {
		return verticalLayoutRuleset.getOffset(layoutState);
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if (outerArea.contains(screenX, screenY)) {
			setState(NodeState.HOVER);
			boolean result = false;
			NavigableSet<Integer> descendingLayerKeys = layers.descendingKeySet();
			for (Integer layerIndex : descendingLayerKeys) {
				if (layers.get(layerIndex).mouseMoved(screenX, screenY)) {
					result = true;
				}
			}
			return result;
		} else if (getState() != NodeState.NORMAL) {
			setState(NodeState.NORMAL);
		}
		return false;
	}

	@Override
	public boolean mouseScrolled(int screenX, int screenY, float amount) {
		if (outerArea.contains(screenX, screenY)) {
			boolean result = false;
			NavigableSet<Integer> descendingLayerKeys = layers.descendingKeySet();
			for (Integer layerIndex : descendingLayerKeys) {
				if (layers.get(layerIndex).mouseScrolled(screenX, screenY, amount)) {
					result = true;
				}
			}
			return result;
		}
		return false;
	}

	@Override
	public ActionableRenderNode mouseDown(int screenX, int screenY, int pointer, int button) {
		if (!isIncludedInRender()) {
			return null;
		}
		NavigableSet<Integer> descendingLayerKeys = layers.descendingKeySet();
		for (Integer layerIndex : descendingLayerKeys) {
			ActionableRenderNode result = layers.get(layerIndex).mouseDown(screenX, screenY, pointer, button);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	public void addChild(RenderNode<?, ?> child) {
		int zIndex = child.getZIndex();
		if (!layers.containsKey(zIndex)) {
			layers.put(zIndex, new RenderLayer(this, zIndex));
		}
		layers.get(zIndex).add(child);
		setDirty(true);
	}

	public void removeChild(RenderNode<?, ?> child) {
		if (!layers.containsKey(child.getZIndex())) {
			return;
		}
		layers.get(child.getZIndex()).remove(child);
		setDirty(true);
	}

	public void clearChildren() {
		layers.clear();
		setDirty(true);
	}

	@Override
	public boolean isDirty() {
		return childDirty || super.isDirty();
	}

	@Override
	public void setDirty(boolean dirty) {
		if (layers == null || layers.size() == 0) {
			super.setDirty(dirty);
		} else {
			for (RenderLayer layer : layers.values()) {
				layer.setDirty(dirty);
			}
		}
	}

	protected void setImmediateDirty(boolean dirty) {
		super.setDirty(dirty);
	}

	boolean isChildDirty() {
		return childDirty;
	}

	public void setChildDirty(boolean childDirty) {
		if (!childDirty) {
			return;
		}
		if (this.childDirty) {
			// Prevent repeated bubbling
			return;
		}
		this.childDirty = childDirty;

		if (parent == null) {
			return;
		}
		parent.setChildDirty(true);
	}

	@Override
	public void setState(NodeState state) {
		super.setState(state);
		if (state != NodeState.NORMAL) {
			return;
		}
		for (RenderLayer layer : layers.values()) {
			layer.setState(state);
		}
	}

	public RenderNode<?, ?> getElementById(String id) {
		if (element.getId().equals(id)) {
			return this;
		}
		for (RenderLayer layer : layers.values()) {
			RenderNode<?, ?> result = layer.getElementById(id);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	public RenderNode<?, ?> searchTreeForElementById(String id) {
		if (rootNode == null) {
			return getElementById(id);
		}
		return rootNode.getElementById(id);
	}

	public LayoutRuleset getHorizontalLayoutRuleset() {
		return horizontalLayoutRuleset;
	}

	public LayoutRuleset getVerticalLayoutRuleset() {
		return verticalLayoutRuleset;
	}

	public FlexDirection getFlexDirection() {
		if (flexDirection == null) {
			flexDirection = FlexDirection.COLUMN;
		}
		return flexDirection;
	}
}
