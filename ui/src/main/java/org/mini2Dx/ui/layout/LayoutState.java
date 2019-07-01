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

import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.input.GamePadType;
import org.mini2Dx.ui.InputSource;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.element.UiElement;
import org.mini2Dx.ui.render.UiContainerRenderTree;
import org.mini2Dx.ui.style.UiTheme;

/**
 * Represents the current flow state during layout
 */
public class LayoutState {
	private final UiContainerRenderTree uiContainerRenderTree;
	private final AssetManager assetManager;
	private final UiTheme theme;
	private final ScreenSize screenSize;
	private final float totalColumns;
	private final boolean screenSizeChanged;
	
	private float parentWidth;

	public LayoutState(UiContainerRenderTree uiContainerRenderTree, AssetManager assetManager,  UiTheme theme,
			ScreenSize screenSize, int totalColumns, float parentWidth, boolean screenSizeChanged) {
		this.uiContainerRenderTree = uiContainerRenderTree;
		this.assetManager = assetManager;
		this.theme = theme;
		this.screenSize = screenSize;
		this.totalColumns = totalColumns;
		this.screenSizeChanged = screenSizeChanged;
		setParentWidth(parentWidth);
	}

	/**
	 * Returns the width of the parent {@link UiElement}
	 * @return
	 */
	public float getParentWidth() {
		return parentWidth;
	}

	/**
	 * Sets the width of the parent {@link UiElement}
	 * @param parentWidth
	 */
	public void setParentWidth(float parentWidth) {
		this.parentWidth = parentWidth;
	}

	/**
	 * Returns the {@link AssetManager} belonging to the {@link UiContainer}
	 * @return
	 */
	public AssetManager getAssetManager() {
		return assetManager;
	}

	/**
	 * Returns the current {@link ScreenSize}
	 * @return
	 */
	public ScreenSize getScreenSize() {
		return screenSize;
	}
	
	/**
	 * Returns the scale to apply to {@link ScreenSize}
	 * @return 1f by default
	 */
	public float getScreenSizeScale() {
		return uiContainerRenderTree.getScreenSizeScale();
	}

	/**
	 * Returns the total columns the UI is split into
	 * @return
	 */
	public int getTotalColumns() {
		return (int) totalColumns;
	}

	/**
	 * Returns the pixel width for a given column amount
	 * @param columns The amount of columns
	 * @return The width in pixels
	 */
	public float getColumnWidth(int columns) {
		if(columns <= 0) {
			return 0f;
		}
		return (int) ((columns / totalColumns) * parentWidth);
	}

	/**
	 * Returns the current {@link UiTheme}
	 * @return
	 */
	public UiTheme getTheme() {
		return theme;
	}

	/**
	 * Returns the {@link UiContainerRenderTree}
	 * @return
	 */
	public UiContainerRenderTree getUiContainerRenderTree() {
		return uiContainerRenderTree;
	}
	
	public InputSource getLastInputSource() {
		return uiContainerRenderTree.getLastInputSource();
	}
	
	public GamePadType getLastGamePadType() {
		return uiContainerRenderTree.getLastGamePadType();
	}

	/**
	 * Returns if the layout was triggered by a {@link ScreenSize} change
	 * @return True if the layout was triggered by a {@link ScreenSize} change
	 */
	public boolean isScreenSizeChanged() {
		return screenSizeChanged;
	}

	@Override
	public String toString() {
		return "LayoutState [theme=" + theme.getId() + ", screenSize=" + screenSize + ", totalColumns=" + totalColumns
				+ ", parentWidth=" + parentWidth + "]";
	}
}
