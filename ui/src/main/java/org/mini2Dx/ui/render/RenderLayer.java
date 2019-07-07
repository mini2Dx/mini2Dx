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
