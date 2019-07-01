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

import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.ui.layout.LayoutRuleset;
import org.mini2Dx.ui.layout.LayoutState;

/**
 * Represents a layer of {@link RenderNode}s on the z axis of a {@link ParentRenderNode}
 */
public class RenderLayer implements Comparable<RenderLayer> {
	protected final Array<RenderNode<?, ?>> children = new Array<RenderNode<?, ?>>(1);
	protected final ParentRenderNode<?, ?> owner;
	protected final int zIndex;
	
	public RenderLayer(ParentRenderNode<?, ?> owner, int zIndex) {
		this.owner = owner;
		this.zIndex = zIndex;
	}
	
	public void add(RenderNode<?, ?> child) {
		children.add(child);
	}
	
	public void remove(RenderNode<?, ?> child) {
		children.removeValue(child, false);
	}
	
	public void update(UiContainerRenderTree uiContainer, float delta) {
		for (int i = 0; i < children.size; i++) {
			children.get(i).update(uiContainer, delta);
		}
	}
	
	public void interpolate(float alpha) {
		for (int i = 0; i < children.size; i++) {
			children.get(i).interpolate(alpha);
		}
	}
	
	public void render(Graphics g) {
		for (int i = 0; i < children.size; i++) {
			children.get(i).render(g);
		}
	}
	
	public void layout(LayoutState layoutState, LayoutRuleset layoutRuleset) {
		layoutRuleset.layout(layoutState, owner, children);
	}
	
	public boolean mouseScrolled(int screenX, int screenY, float amount) {
		boolean result = false;
		for(int i = children.size - 1; i >= 0; i--) {
			if(children.get(i).mouseScrolled(screenX, screenY, amount)) {
				result = true;
			}
		}
		return result;
	}
	
	public boolean mouseMoved(int screenX, int screenY) {
		boolean result = false;
		for(int i = children.size - 1; i >= 0; i--) {
			if(children.get(i).mouseMoved(screenX, screenY)) {
				result = true;
			}
		}
		return result;
	}
	
	public ActionableRenderNode mouseDown(int screenX, int screenY, int pointer, int button) {
		for (int i = children.size - 1; i >= 0; i--) {
			if(!children.get(i).isIncludedInRender()) {
				continue;
			}
			ActionableRenderNode result = children.get(i).mouseDown(screenX, screenY, pointer, button);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
	
	public float determinePreferredContentHeight(LayoutState layoutState) {
		float maxHeight = 0f;

		for (int i = 0; i < children.size; i++) {
			if(!children.get(i).isIncludedInLayout()) {
				continue;
			}
			float height = children.get(i).getRelativeY() + children.get(i).getPreferredOuterHeight();
			if (height > maxHeight) {
				maxHeight = height;
			}
		}
		return maxHeight - owner.getStyle().getPaddingTop();
	}
	
	public boolean isDirty() {
		for (int i = children.size - 1; i >= 0; i--) {
			if(children.get(i).isDirty()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean setDirty() {
		boolean result = false;
		for (int i = children.size - 1; i >= 0; i--) {
			result |= children.get(i).setDirty();
		}
		return result;
	}
	
	public void setState(NodeState state) {
		for (int i = children.size - 1; i >= 0; i--) {
			children.get(i).setState(NodeState.NORMAL);
		}
	}
	
	public RenderNode<?, ?> getElementById(String id) {
		for (RenderNode<?, ?> child : children) {
			RenderNode<?, ?> result = child.getElementById(id);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
	
	public RenderNode<?, ?> getFirstChild() {
		if(children.size == 0) {
			return null;
		}
		return children.get(0);
	}
	
	public RenderNode<?, ?> getLastChild() {
		if(children.size == 0) {
			return null;
		}
		return children.get(children.size - 1);
	}
	
	public RenderNode<?, ?> getChild(int index) {
		return children.get(index);
	}
	
	public int getTotalChildren() {
		return children.size;
	}

	@Override
	public int compareTo(RenderLayer o) {
		return Integer.compare(zIndex, o.zIndex);
	}
}
