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
package org.mini2Dx.ui.element;

import org.mini2Dx.ui.render.AbsoluteContainerRenderNode;
import org.mini2Dx.ui.render.ParentRenderNode;

/**
 *
 */
public class AbsoluteContainer extends Container {
	private float x, y;
	
	public AbsoluteContainer() {
		this(null);
	}
	
	public AbsoluteContainer(String id) {
		super(id);
	}
	
	@Override
	public void attach(ParentRenderNode<?, ?> parentRenderNode) {
		if(renderNode != null) {
			return;
		}
		renderNode = new AbsoluteContainerRenderNode(parentRenderNode, this);
		for(int i = 0; i < children.size(); i++) {
			children.get(i).attach(renderNode);
		}
		parentRenderNode.addChild(renderNode);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		if(this.x == x) {
			return;
		}
		this.x = x;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		if(this.y == y) {
			return;
		}
		this.y = y;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}
	
	public void set(float x, float y) {
		if(this.x == x && this.y == y) {
			return;
		}
		this.x = x;
		this.y = y;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}
	
	public float getWidth() {
		if(renderNode == null) {
			return 0f;
		}
		return renderNode.getWidth();
	}
	
	public float getHeight() {
		if(renderNode == null) {
			return 0f;
		}
		return renderNode.getHeight();
	}
}
