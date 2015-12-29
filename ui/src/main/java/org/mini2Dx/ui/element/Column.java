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

import java.util.ArrayList;
import java.util.List;

import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.ui.layout.LayoutRuleset;
import org.mini2Dx.ui.render.AbstractColumnRenderNode;
import org.mini2Dx.ui.render.ColumnRenderNode;
import org.mini2Dx.ui.render.ParentRenderNode;

/**
 *
 */
public class Column extends UiElement {
	protected final List<UiElement> children = new ArrayList<UiElement>(1);
	protected AbstractColumnRenderNode<?> renderNode;
	private LayoutRuleset layout = LayoutRuleset.DEFAULT_RULESET;
	
	public Column() {
		this(null);
	}
	
	public Column(String id) {
		super(id);
	}
	
	public void add(UiElement element) {
		if(element == null) {
			throw new MdxException("Cannot add null element to Column");
		}
		children.add(element);
		if(renderNode == null) {
			return;
		}
		element.attach(renderNode);
	}
	
	public boolean remove(UiElement element) {
		if(renderNode != null) {
			element.detach(renderNode);
		}
		return children.remove(element);
	}

	@Override
	public void attach(ParentRenderNode<?, ?> parentRenderNode) {
		if(renderNode != null) {
			return;
		}
		renderNode = new ColumnRenderNode(parentRenderNode, this);
		for(int i = 0; i < children.size(); i++) {
			children.get(i).attach(renderNode);
		}
		parentRenderNode.addChild(renderNode);
	}
	
	@Override
	public void detach(ParentRenderNode<?, ?> parentRenderNode) {
		if(renderNode == null) {
			return;
		}
		parentRenderNode.removeChild(renderNode);
	}

	public LayoutRuleset getLayout() {
		return layout;
	}

	public void setLayout(LayoutRuleset layoutRuleset) {
		if(layoutRuleset == null) {
			return;
		}
		this.layout = layoutRuleset;
	}
	
	public void setVisibility(Visibility visibility) {
		if(this.visibility == visibility) {
			return;
		}
		this.visibility = visibility;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}

	@Override
	public void pushEffectsToRenderNode() {
		while(!effects.isEmpty()) {
			renderNode.applyEffect(effects.poll());
		}
	}
	
	@Override
	public void setStyleId(String styleId) {
		if(styleId == null) {
			return;
		}
		this.styleId = styleId;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}
	
	public static Column withElements(UiElement ...elements) {
		return withElements(null, elements);
	}
	
	public static Column withElements(String columnId, UiElement ...elements) {
		return withElements(columnId, "xs-12", elements);
	}
	
	public static Column withElements(String columnId, String layoutRuleset, UiElement ...elements) {
		Column result = new Column(columnId);
		result.setLayout(new LayoutRuleset(layoutRuleset));
		for(int i = 0; i < elements.length; i++) {
			result.add(elements[i]);
		}
		result.setVisibility(Visibility.VISIBLE);
		return result;
	}
}
