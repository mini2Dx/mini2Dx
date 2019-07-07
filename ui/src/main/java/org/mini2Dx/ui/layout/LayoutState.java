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
