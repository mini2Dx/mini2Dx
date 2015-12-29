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

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.game.GameResizeListener;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.layout.LayoutState;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.listener.ScreenSizeListener;
import org.mini2Dx.ui.style.StyleRule;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

/**
 *
 */
public class UiContainerRenderTree extends ParentRenderNode<UiContainer, StyleRule> implements GameResizeListener {
	private static final String LOGGING_TAG = UiContainerRenderTree.class.getSimpleName();
	
	private final GameContainer gc;
	private final AssetManager assetManager;
	
	private List<ScreenSizeListener> screenSizeListeners;
	private ScreenSize currentScreenSize = ScreenSize.XS;
	private boolean screenSizeChanged = false;

	public UiContainerRenderTree(UiContainer uiContainer, GameContainer gc, AssetManager assetManager) {
		super(null, uiContainer);
		this.gc = gc;
		this.assetManager = assetManager;
		
		gc.addResizeListener(this);
		onResize(gc.getWidth(), gc.getHeight());
	}
	
	public void update(float delta) {
		super.update(this, delta);
	}
	
	public void layout() {
		layout(new LayoutState(this, assetManager, element.getTheme(), currentScreenSize, 12, gc.getWidth(), screenSizeChanged));
	}
	
	@Override
	public void layout(LayoutState layoutState) {
		if(!isDirty() && !layoutState.isScreenSizeChanged()) {
			return;
		}
		if(element.isDebugEnabled()) {
			Gdx.app.log(LOGGING_TAG, "Layout triggered");
		}
		style = determineStyleRule(layoutState);
		preferredWidth = determinePreferredWidth(layoutState);
		preferredHeight = determinePreferredHeight(layoutState);
		xOffset = determineXOffset(layoutState);
		yOffset = determineYOffset(layoutState);
		currentArea.forceTo(xOffset, yOffset, preferredWidth, preferredHeight);
		
		for (int i = 0; i < children.size(); i++) {
			RenderNode<?, ?> node = children.get(i);
			node.layout(layoutState);
			
			if(!node.isIncludedInLayout()) {
				continue;
			}
			
			node.setRelativeX(node.getXOffset());
			node.setRelativeY(node.getYOffset());
		}
		
		setDirty(false);
		childDirty = false;
		screenSizeChanged = false;
	}

	@Override
	public void onResize(int width, int height) {
		ScreenSize screenSize = ScreenSize.XS;
		if(width >= ScreenSize.SM.getMinSize()) {
			screenSize = ScreenSize.SM;
		}
		if(width >= ScreenSize.MD.getMinSize()) {
			screenSize = ScreenSize.MD;
		}
		if(width >= ScreenSize.LG.getMinSize()) {
			screenSize = ScreenSize.LG;
		}
		if(width >= ScreenSize.XL.getMinSize()) {
			screenSize = ScreenSize.XL;
		}
		screenSizeChanged = true;
		this.currentScreenSize = screenSize;
		
		if(screenSizeListeners == null) {
			return;
		}
		for(int i = screenSizeListeners.size() - 1; i >= 0; i--) {
			screenSizeListeners.get(i).onScreenSizeChanged(currentScreenSize);
		}
	}
	
	public void addScreenSizeListener(ScreenSizeListener listener) {
		if(screenSizeListeners == null) {
			screenSizeListeners = new ArrayList<ScreenSizeListener>(1);
		}
		screenSizeListeners.add(listener);
	}
	
	public void removeScreenSizeListener(ScreenSizeListener listener) {
		if(screenSizeListeners == null) {
			return;
		}
		screenSizeListeners.remove(listener);
	}
	
	@Override
	protected float determinePreferredWidth(LayoutState layoutState) {
		return gc.getWidth();
	}
	
	@Override
	protected float determinePreferredHeight(LayoutState layoutState) {
		return gc.getHeight();
	}
	
	@Override
	protected float determineXOffset(LayoutState layoutState) {
		return 0f;
	}

	@Override
	protected float determineYOffset(LayoutState layoutState) {
		return 0f;
	}
	
	@Override
	protected StyleRule determineStyleRule(LayoutState layoutState) {
		return new StyleRule();
	}
	
	@Override
	public boolean isDirty() {
		return screenSizeChanged || super.isDirty();
	}
}
