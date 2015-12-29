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
package org.mini2Dx.ui.layout;

import org.mini2Dx.ui.render.UiContainerRenderTree;
import org.mini2Dx.ui.style.UiTheme;

import com.badlogic.gdx.assets.AssetManager;

/**
 *
 */
public class LayoutState {
	private final UiContainerRenderTree uiContainer;
	private final AssetManager assetManager;
	private final UiTheme theme;
	private final ScreenSize screenSize;
	private final int totalColumns;
	private final boolean screenSizeChanged;
	
	private float parentWidth;
	private float columnWidth;

	public LayoutState(UiContainerRenderTree uiContainer, AssetManager assetManager, UiTheme theme,
			ScreenSize screenSize, int totalColumns, float parentWidth, boolean screenSizeChanged) {
		this.uiContainer = uiContainer;
		this.assetManager = assetManager;
		this.theme = theme;
		this.screenSize = screenSize;
		this.totalColumns = totalColumns;
		this.screenSizeChanged = screenSizeChanged;
		setParentWidth(parentWidth);
	}

	public float getParentWidth() {
		return parentWidth;
	}

	public void setParentWidth(float parentWidth) {
		this.parentWidth = parentWidth;
		this.columnWidth = parentWidth / totalColumns;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public ScreenSize getScreenSize() {
		return screenSize;
	}

	public int getTotalColumns() {
		return totalColumns;
	}

	public float getColumnWidth() {
		return columnWidth;
	}

	public UiTheme getTheme() {
		return theme;
	}

	public UiContainerRenderTree getUiContainer() {
		return uiContainer;
	}

	public boolean isScreenSizeChanged() {
		return screenSizeChanged;
	}

	@Override
	public String toString() {
		return "LayoutState [theme=" + theme.getId() + ", screenSize=" + screenSize + ", totalColumns=" + totalColumns
				+ ", parentWidth=" + parentWidth + ", columnWidth=" + columnWidth + "]";
	}
}
