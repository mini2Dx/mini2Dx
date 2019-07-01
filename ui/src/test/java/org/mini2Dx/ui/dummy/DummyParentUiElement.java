/**
 * Copyright (c) 2017 See AUTHORS file
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

import org.mini2Dx.ui.element.ParentUiElement;
import org.mini2Dx.ui.element.Visibility;
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.style.ParentStyleRule;

/**
 *
 */
public class DummyParentUiElement extends ParentUiElement {
	private float preferredContentWidth, preferredContentHeight;
	private ParentStyleRule styleRule = new ParentStyleRule();
	
	private DummyRenderNode renderNode;
	
	public DummyParentUiElement() {
		super();
		setVisibility(Visibility.VISIBLE);
	}
	
	public DummyParentUiElement(String id) {
		super(id);
		setVisibility(Visibility.VISIBLE);
	}

	@Override
	protected ParentRenderNode<?, ?> createRenderNode(ParentRenderNode<?, ?> parent) {
		return new DummyParentRenderNode(parent, this);
	}

	public ParentStyleRule getStyleRule() {
		return styleRule;
	}

	public void setStyleRule(ParentStyleRule styleRule) {
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
}
