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

import java.util.ArrayList;
import java.util.List;

import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.ui.layout.LayoutState;

/**
 *
 */
public class RenderLayer implements Comparable<RenderLayer> {
	protected final List<RenderNode<?, ?>> children = new ArrayList<RenderNode<?, ?>>(1);
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
		children.remove(child);
	}
	
	public void update(UiContainerRenderTree uiContainer, float delta) {
		for (int i = 0; i < children.size(); i++) {
			children.get(i).update(uiContainer, delta);
		}
	}
	
	public void interpolate(float alpha) {
		for (int i = 0; i < children.size(); i++) {
			children.get(i).interpolate(alpha);
		}
	}
	
	protected void render(Graphics g) {
		for (int i = 0; i < children.size(); i++) {
			children.get(i).render(g);
		}
	}
	
	public void layout(LayoutState layoutState) {
		float startX = owner.getStyle().getPaddingLeft();
		float startY = owner.getStyle().getPaddingTop();
		
		for (int i = 0; i < children.size(); i++) {
			RenderNode<?, ?> node = children.get(i);
			node.layout(layoutState);
			if (!node.isIncludedInLayout()) {
				continue;
			}
			
			if(startX - owner.getStyle().getPaddingLeft() + node.getXOffset() + node.getPreferredOuterWidth() > owner.getPreferredContentWidth()) {
				float maxHeight = 0f;
				for (int j = i; j >= 0; j--) {
					RenderNode<?, ?> previousNode = children.get(j);
					if (previousNode.getRelativeY() == startY) {
						float height = previousNode.getPreferredOuterHeight() + node.getYOffset();
						if (height > maxHeight) {
							maxHeight = height;
						}
					}
				}
				startY += maxHeight;
				startX = owner.getStyle().getPaddingLeft();
			}

			node.setRelativeX(startX + node.getXOffset());
			node.setRelativeY(startY + node.getYOffset());
			startX += node.getPreferredOuterWidth() + node.getXOffset();
		}
	}
	
	public boolean mouseMoved(int screenX, int screenY) {
		boolean result = false;
		for(int i = children.size() - 1; i >= 0; i--) {
			if(children.get(i).mouseMoved(screenX, screenY)) {
				result = true;
			}
		}
		return result;
	}
	
	public ActionableRenderNode mouseDown(int screenX, int screenY, int pointer, int button) {
		for (int i = children.size() - 1; i >= 0; i--) {
			ActionableRenderNode result = children.get(i).mouseDown(screenX, screenY, pointer, button);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
	
	public float determinePreferredContentHeight(LayoutState layoutState) {
		float maxHeight = 0f;

		for (int i = 0; i < children.size(); i++) {
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
	
	public void setDirty(boolean dirty) {
		for (int i = children.size() - 1; i >= 0; i--) {
			children.get(i).setDirty(dirty);
		}
	}
	
	public void setState(NodeState state) {
		for (int i = children.size() - 1; i >= 0; i--) {
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

	@Override
	public int compareTo(RenderLayer o) {
		return Integer.compare(zIndex, o.zIndex);
	}
}
