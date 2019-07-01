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

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.util.IntTreeMap;
import org.mini2Dx.gdx.utils.IntMap;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.element.ParentUiElement;
import org.mini2Dx.ui.layout.FlexLayoutRuleset;
import org.mini2Dx.ui.layout.ImmediateLayoutRuleset;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.layout.LayoutRuleset;
import org.mini2Dx.ui.style.ParentStyleRule;

/**
 * Base class for {@link RenderNode} implementations that contains child nodes
 */
public abstract class ParentRenderNode<T extends ParentUiElement, S extends ParentStyleRule> extends RenderNode<T, S> {
	private static final String LOGGING_TAG = ParentRenderNode.class.getSimpleName();

	protected final IntTreeMap<RenderLayer> layers = new IntTreeMap<RenderLayer>();

	protected LayoutRuleset layoutRuleset;

	private boolean cachedDirty;
	protected boolean cachedDirtyUpdateRequired;

	private Rectangle cachedClip;

	public ParentRenderNode(ParentRenderNode<?, ?> parent, T element) {
		super(parent, element);
		initLayoutRuleset();
	}

	protected void initLayoutRuleset() {
		if(element.getFlexLayout() != null) {
			layoutRuleset = FlexLayoutRuleset.parse(element.getFlexLayout());
		} else {
			layoutRuleset = new ImmediateLayoutRuleset(element);
		}
	}

	@Override
	public void update(UiContainerRenderTree uiContainer, float delta) {
		super.update(uiContainer, delta);
		final IntMap.Keys keys = layers.ascendingKeys();
		keys.reset();
		while(keys.hasNext) {
			final int layerIndex = keys.next();
			final RenderLayer layer = layers.get(layerIndex);
			layer.update(uiContainer, delta);
		}
	}

	@Override
	public void interpolate(float alpha) {
		super.interpolate(alpha);
		final IntMap.Keys keys = layers.ascendingKeys();
		keys.reset();
		while(keys.hasNext) {
			final int layerIndex = keys.next();
			final RenderLayer layer = layers.get(layerIndex);
			layer.interpolate(alpha);
		}
	}

	protected void renderBackground(Graphics g) {
		switch(getState()) {
		case NORMAL:
			if (style.getNormalBackgroundRenderer() != null) {
				style.getNormalBackgroundRenderer().render(g, getInnerRenderX(), getInnerRenderY(), getInnerRenderWidth(),
						getInnerRenderHeight());
			}
			break;
		case HOVER:
			if(style.getHoverBackgroundRenderer() != null) {
				style.getHoverBackgroundRenderer().render(g, getInnerRenderX(), getInnerRenderY(), getInnerRenderWidth(),
						getInnerRenderHeight());
			}
			break;
		case ACTION:
			if(style.getHoverBackgroundRenderer() != null) {
				style.getHoverBackgroundRenderer().render(g, getInnerRenderX(), getInnerRenderY(), getInnerRenderWidth(),
						getInnerRenderHeight());
			}
			break;
		}
	}

	@Override
	protected void renderElement(Graphics g) {
		boolean overflowClipped = element.isOverflowClipped();
		if (overflowClipped) {
			if(cachedClip == null) {
				cachedClip = new Rectangle();
			}
			g.peekClip(cachedClip);
			if(effects.size == 0) {
				g.setClip(outerArea);
			} else {
				g.setClip(outerArea.getRenderX(), outerArea.getRenderY(), outerArea.getRenderWidth(), outerArea.getRenderHeight());
			}
		}

		renderBackground(g);

		final IntMap.Keys keys = layers.ascendingKeys();
		keys.reset();
		while(keys.hasNext) {
			final int layerIndex = keys.next();
			final RenderLayer layer = layers.get(layerIndex);
			layer.render(g);
		}
		if (overflowClipped) {
			g.setClip(cachedClip);
		}
	}

	@Override
	public void layout(LayoutState layoutState) {
		if (!isDirty() && !layoutState.isScreenSizeChanged()) {
			if (element.isDebugEnabled()) {
				Mdx.log.debug(LOGGING_TAG, "Layout not triggered - " + isImmediateDirty() + " " + isChildDirty() + " " + cachedDirty);
			}
			return;
		}
		if (element.isDebugEnabled()) {
			Mdx.log.debug(LOGGING_TAG, "Layout triggered");
		}
		if (!layoutRuleset.equals(element.getFlexLayout())) {
			initLayoutRuleset();
		}

		float parentWidth = layoutState.getParentWidth();
		rootNode = layoutState.getUiContainerRenderTree();
		style = determineStyleRule(layoutState);

		if (this.zIndex != element.getZIndex()) {
			parent.removeChild(this);
			zIndex = element.getZIndex();
			parent.addChild(this);
		}

		xOffset = determineXOffset(layoutState);
		preferredContentWidth = determinePreferredContentWidth(layoutState);
		layoutState.setParentWidth(getPreferredContentWidth());

		layoutRuleset.getPreferredElementHeight(layoutState);
		if (!layoutRuleset.getCurrentHeightRule().isAutoSize()) {
			preferredContentHeight = determinePreferredContentHeight(layoutState);
		}

		final IntMap.Keys keys = layers.ascendingKeys();
		keys.reset();
		while(keys.hasNext) {
			final int layerIndex = keys.next();
			final RenderLayer layer = layers.get(layerIndex);
			layer.layout(layoutState, layoutRuleset);
		}

		layoutState.setParentWidth(parentWidth);

		yOffset = determineYOffset(layoutState);
		if (layoutRuleset.getCurrentHeightRule().isAutoSize()) {
			preferredContentHeight = determinePreferredContentHeight(layoutState);
		}
		clearDirty();
		cachedDirtyUpdateRequired = true;
		initialLayoutOccurred = true;

		element.syncWithLayout(rootNode);
	}

	@Override
	protected float determinePreferredContentHeight(LayoutState layoutState) {
		if (preferredContentWidth <= 0f) {
			return 0f;
		}
		float maxHeight = 0f;
		float sizeRuleHeight = layoutRuleset.getPreferredElementHeight(layoutState);

		if (layoutRuleset.getCurrentHeightRule().isAutoSize()) {
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
		return style.getRounding().calculateRounding(result);
	}

	@Override
	protected float determineXOffset(LayoutState layoutState) {
		return layoutRuleset.getPreferredElementRelativeX(layoutState);
	}

	@Override
	protected float determineYOffset(LayoutState layoutState) {
		return layoutRuleset.getPreferredElementRelativeY(layoutState);
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if (innerArea.contains(screenX, screenY)) {
			setState(NodeState.HOVER);
			boolean result = false;

			final IntMap.Keys keys = layers.descendingKeys();
			keys.reset();
			while(keys.hasNext) {
				final int layerIndex = keys.next();
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
		if (innerArea.contains(screenX, screenY)) {
			boolean result = false;

			final IntMap.Keys keys = layers.descendingKeys();
			keys.reset();
			while(keys.hasNext) {
				final int layerIndex = keys.next();
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
		final IntMap.Keys keys = layers.descendingKeys();
		keys.reset();
		while(keys.hasNext) {
			final int layerIndex = keys.next();
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
		setDirty();
	}

	public void removeChild(RenderNode<?, ?> child) {
		if (!layers.containsKey(child.getZIndex())) {
			return;
		}
		layers.get(child.getZIndex()).remove(child);
		setDirty();
	}

	public void clearChildren() {
		layers.clear();
		setDirty();
	}

	@Override
	public boolean isDirty() {
		if(cachedDirtyUpdateRequired) {
			cachedDirty = isChildDirty() || super.isDirty();
			cachedDirtyUpdateRequired = false;
		}
		return cachedDirty;
	}

	@Override
	public boolean setDirty() {
		if (layers == null || layers.size == 0) {
			cachedDirtyUpdateRequired |= super.setDirty();
		} else {
			for (RenderLayer layer : layers.values()) {
				cachedDirtyUpdateRequired |= layer.setDirty();
			}
		}
		return cachedDirtyUpdateRequired;
	}

	protected boolean isImmediateDirty() {
		return super.isDirty();
	}

	boolean isChildDirty() {
		if (layers == null || layers.size == 0) {
			return false;
		} else {
			for (RenderLayer layer : layers.values()) {
				if(layer.isDirty()) {
					return true;
				}
			}
		}
		return false;
	}

	public void setChildDirty() {
		cachedDirtyUpdateRequired = true;
		if(parent == null) {
			return;
		}
		parent.setChildDirty();
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

	public LayoutRuleset getLayoutRuleset() {
		return layoutRuleset;
	}
}
