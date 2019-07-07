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
package org.mini2Dx.ui.style;

import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.element.Button;

/**
 * Extends {@link StyleRule} for {@link Button} styling
 */
public class ButtonStyleRule extends ParentStyleRule {
	@Field(optional=true)
	private String actionBackground;
	@Field(optional=true)
	private String disabledBackground;

	private BackgroundRenderer actionBackgroundRenderer, disabledBackgroundRenderer;
	
	@Override
	public void prepareAssets(UiTheme theme, FileHandleResolver fileHandleResolver, AssetManager assetManager) {
		if (theme.isHeadless()) {
			return; 
		}
		super.prepareAssets(theme, fileHandleResolver, assetManager);

		if(actionBackground != null) {
			actionBackgroundRenderer = BackgroundRenderer.parse(actionBackground);
			actionBackgroundRenderer.prepareAssets(theme, fileHandleResolver, assetManager);
		} else {
			actionBackgroundRenderer = getHoverBackgroundRenderer();
		}
		if(disabledBackground != null) {
			disabledBackgroundRenderer = BackgroundRenderer.parse(disabledBackground);
			disabledBackgroundRenderer.prepareAssets(theme, fileHandleResolver, assetManager);
		} else {
			disabledBackgroundRenderer = getNormalBackgroundRenderer();
		}
	}

	public BackgroundRenderer getActionBackgroundRenderer() {
		return actionBackgroundRenderer;
	}

	public BackgroundRenderer getDisabledBackgroundRenderer() {
		return disabledBackgroundRenderer;
	}

	public String getActionBackground() {
		return actionBackground;
	}

	public void setActionBackground(String actionBackground) {
		this.actionBackground = actionBackground;
	}

	public String getDisabledBackground() {
		return disabledBackground;
	}

	public void setDisabledBackground(String disabledBackground) {
		this.disabledBackground = disabledBackground;
	}
}
