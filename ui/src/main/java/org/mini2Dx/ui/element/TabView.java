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
import org.mini2Dx.ui.render.ParentRenderNode;
import org.mini2Dx.ui.render.TabViewRenderNode;

/**
 *
 */
public class TabView extends UiElement {
	private final List<Tab> tabs = new ArrayList<Tab>();
	
	private LayoutRuleset layout = LayoutRuleset.DEFAULT_RULESET;
	private TabViewRenderNode renderNode;
	private int currentTabIndex = 0;
	
	public TabView() {
		this(null);
	}
	
	public TabView(String id) {
		super(id);
	}
	
	public void add(Tab tab) {
		if(tab == null) {
			throw new MdxException("Cannot add null element to TabView");
		}
		tabs.add(tab);
		if(renderNode == null) {
			return;
		}
		tab.attach(renderNode);
	}
	
	public void add(int index, Tab tab) {
		if(tab == null) {
			throw new MdxException("Cannot add null element to TabView");
		}
		tabs.add(index, tab);
		if(renderNode == null) {
			return;
		}
		tab.attach(renderNode);
	}
	
	public boolean remove(Tab tab) {
		if(renderNode != null) {
			tab.detach(renderNode);
		}
		return tabs.remove(tab);
	}
	
	public boolean remove(int index) {
		return remove(tabs.get(index));
	}
	
	@Override
	public void syncWithRenderNode() {
		while(!effects.isEmpty()) {
			renderNode.applyEffect(effects.poll());
		}
	}

	@Override
	public void attach(ParentRenderNode<?, ?> parentRenderNode) {
		if(renderNode != null) {
			return;
		}
		renderNode = new TabViewRenderNode(parentRenderNode, this);
		for(int i = 0; i < tabs.size(); i++) {
			tabs.get(i).attach(renderNode);
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

	@Override
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

	@Override
	public void setZIndex(int zIndex) {
		this.zIndex = zIndex;
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}
	
	public LayoutRuleset getLayout() {
		return layout;
	}

	public void setLayout(LayoutRuleset layoutRuleset) {
		if(layoutRuleset == null) {
			return;
		}
		this.layout = layoutRuleset;
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}
	
	public Tab getCurrentTab() {
		if(currentTabIndex >= tabs.size()) {
			return null;
		}
		return tabs.get(currentTabIndex);
	}
	
	public void setCurrentTab(Tab tab) {
		int tabIndex = tabs.indexOf(tab);
		if(tabIndex < 0) {
			throw new MdxException(tab + " cannot be set to current tab as it was not added to " + TabView.class.getSimpleName() + ":" + getId());
		}
		setCurrentTabIndex(tabIndex);
	}

	public int getCurrentTabIndex() {
		return currentTabIndex;
	}

	public void setCurrentTabIndex(int currentTabIndex) {
		if(currentTabIndex < 0) {
			return;
		}
		if(currentTabIndex >= tabs.size()) {
			return;
		}
		if(this.currentTabIndex == currentTabIndex) {
			return;
		}
		
		tabs.get(this.currentTabIndex).deactivateTab();
		this.currentTabIndex = currentTabIndex;
		tabs.get(this.currentTabIndex).activateTab();
		
		if(renderNode == null) {
			return;
		}
		renderNode.setDirty(true);
	}
	
	public void nextTab() {
		if(currentTabIndex >= tabs.size() - 1) {
			setCurrentTabIndex(0);
		} else {
			setCurrentTabIndex(currentTabIndex + 1);
		}
	}
	
	public void previousTab() {
		if(currentTabIndex <= 0) {
			setCurrentTabIndex(tabs.size() - 1);
		} else {
			setCurrentTabIndex(currentTabIndex - 1);
		}
	}
}
