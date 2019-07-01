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
package org.mini2Dx.ui.dummy;

import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.UiContainerRenderTree;
import org.mini2Dx.ui.style.StyleRule;

/**
 * A dummy {@link UiElement} for directly manipulating a {@link DummyRenderNode}
 */
public class DummyUiElement extends UiElement {
	private float xOffset, yOffset;
	private float preferredContentWidth, preferredContentHeight;
	private StyleRule styleRule = new StyleRule();
	
	private DummyRenderNode renderNode;
	
	public DummyUiElement() {
		super();
		setVisibility(Visibility.VISIBLE);
	}
	
	public DummyUiElement(String id) {
		super(id);
		setVisibility(Visibility.VISIBLE);
	}

	@Override
	public void attach(ParentRenderNode<?, ?> parentRenderNode) {
		if(renderNode != null) {
			return;
		}
		renderNode = new DummyRenderNode(parentRenderNode, this);
		parentRenderNode.addChild(renderNode);
	}

	@Override
	public void detach(ParentRenderNode<?, ?> parentRenderNode) {
		if(renderNode == null) {
			return;
		}
		parentRenderNode.removeChild(renderNode);
		renderNode = null;
	}

	@Override
	public void invokeBeginHover() {
		if(renderNode == null) {
			return;
		}
		renderNode.beginFakeHover();
	}

	@Override
	public void invokeEndHover() {
		if(renderNode == null) {
			return;
		}
		renderNode.endFakeHover();
	}

	@Override
	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	@Override
	public void setStyleId(String styleId) {
		this.styleId = styleId;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	@Override
	public void setZIndex(int zIndex) {
		this.zIndex = zIndex;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	public StyleRule getStyleRule() {
		return styleRule;
	}

	public void setStyleRule(StyleRule styleRule) {
		this.styleRule = styleRule;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	public float getPreferredContentWidth() {
		return preferredContentWidth;
	}

	public void setPreferredContentWidth(float preferredContentWidth) {
		this.preferredContentWidth = preferredContentWidth;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}
	
	public float getPreferredContentHeight() {
		return preferredContentHeight;
	}

	public void setPreferredContentHeight(float preferredContentHeight) {
		this.preferredContentHeight = preferredContentHeight;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	@Override
	public boolean isRenderNodeDirty() {
		if (renderNode == null) {
			return true;
		}
		return renderNode.isDirty();
	}

	@Override
	public void setRenderNodeDirty() {
		if (renderNode == null) {
			return;
		}
		renderNode.setDirty();
	}

	@Override
	public boolean isInitialLayoutOccurred() {
		if (renderNode == null) {
			return false;
		}
		return renderNode.isInitialLayoutOccurred();
	}

	@Override
	public boolean isInitialUpdateOccurred() {
		if(renderNode == null) {
			return false;
		}
		return renderNode.isInitialUpdateOccurred();
	}

	@Override
	public int getRenderX() {
		if(renderNode == null) {
			return Integer.MIN_VALUE;
		}
		return renderNode.getOuterRenderX();
	}

	@Override
	public int getRenderY() {
		if(renderNode == null) {
			return Integer.MIN_VALUE;
		}
		return renderNode.getOuterRenderY();
	}

	@Override
	public int getRenderWidth() {
		if(renderNode == null) {
			return -1;
		}
		return renderNode.getOuterRenderWidth();
	}

	@Override
	public int getRenderHeight() {
		if(renderNode == null) {
			return -1;
		}
		return renderNode.getOuterRenderHeight();
	}
}
