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

import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.style.StyleRule;

/**
 *
 */
public abstract class ParentRenderNode<T extends UiElement, S extends StyleRule> extends RenderNode<T, S> {
	protected final NavigableMap<Integer, RenderLayer> layers = new TreeMap<Integer, RenderLayer>();

	protected boolean childDirty = false;

	public ParentRenderNode(ParentRenderNode<?, ?> parent, T element) {
		super(parent, element);
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
		for (RenderLayer layer : layers.values()) {
			layer.render(g);
		}
	}
	
	@Override
	public void layout(LayoutState layoutState) {
		if(!isDirty() && !layoutState.isScreenSizeChanged()) {
			return;
		}
		
		float parentWidth = layoutState.getParentWidth();
		style = determineStyleRule(layoutState);
		
		if(this.zIndex != element.getZIndex()) {
			parent.removeChild(this);
			zIndex = element.getZIndex();
			parent.addChild(this);
		}
		
		xOffset = determineXOffset(layoutState);
		preferredContentWidth = determinePreferredContentWidth(layoutState);
		layoutState.setParentWidth(getPreferredContentWidth());

		for(RenderLayer layer : layers.values()) {
			layer.layout(layoutState);
		}

		layoutState.setParentWidth(parentWidth);

		yOffset = determineYOffset(layoutState);
		preferredContentHeight = determinePreferredContentHeight(layoutState);
		setDirty(false);
		childDirty = false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if (currentArea.contains(screenX, screenY)) {
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
	public ActionableRenderNode mouseDown(int screenX, int screenY, int pointer, int button) {
		if (!isIncludedInRender()) {
			return null;
		}
		NavigableSet<Integer> descendingLayerKeys = layers.descendingKeySet();
		for (Integer layerIndex : descendingLayerKeys) {
			ActionableRenderNode result = layers.get(layerIndex).mouseDown(screenX, screenY, pointer, button);
			if(result != null) {
				return result;
			}
		}
		return null;
	}

	public void addChild(RenderNode<?, ?> child) {
		int zIndex = child.getZIndex();
		if(!layers.containsKey(zIndex)) {
			layers.put(zIndex, new RenderLayer(this, zIndex));
		}
		layers.get(zIndex).add(child);
		setDirty(true);
	}

	public void removeChild(RenderNode<?, ?> child) {
		if(!layers.containsKey(child.getZIndex())) {
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

	public void setChildDirty(boolean childDirty) {
		if (!childDirty) {
			return;
		}
		this.childDirty = childDirty;
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
}
