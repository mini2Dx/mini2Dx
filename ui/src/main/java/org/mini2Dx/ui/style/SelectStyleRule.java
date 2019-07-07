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
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.ui.element.Select;

/**
 * Extends {@link StyleRule} for {@link Select} styling
 */
public class SelectStyleRule extends StyleRule {
	@Field
	private int buttonWidth;
	@Field(optional=true)
	private String background;
	@Field(optional=true)
	private String enabledLabelStyle;
	@Field(optional=true)
	private String disabledLabelStyle;
	@Field(optional=true)
	private String leftButtonStyle;
	@Field(optional=true)
	private String rightButtonStyle;
	@Field(optional=true)
	private String leftButtonLabelStyle;
	@Field(optional=true)
	private String rightButtonLabelStyle;

	private BackgroundRenderer normalBackgroundRenderer;
	
	@Override
	public void validate(UiTheme theme) {
		super.validate(theme);
		
		if(enabledLabelStyle != null && !theme.containsLabelStyleRuleset(enabledLabelStyle)) {
			throw new MdxException("No style with id '" + enabledLabelStyle + "' for labels. Required by " + SelectStyleRule.class.getSimpleName());
		}
		if(disabledLabelStyle != null && !theme.containsLabelStyleRuleset(disabledLabelStyle)) {
			throw new MdxException("No style with id '" + disabledLabelStyle + "' for labels. Required by " + SelectStyleRule.class.getSimpleName());
		}
		if(leftButtonStyle != null && !theme.containsButtonStyleRuleset(leftButtonStyle)) {
			throw new MdxException("No style with id '" + leftButtonStyle + "' for buttons. Required by " + SelectStyleRule.class.getSimpleName());
		}
		if(leftButtonLabelStyle != null && !theme.containsLabelStyleRuleset(leftButtonLabelStyle)) {
			throw new MdxException("No style with id '" + leftButtonLabelStyle + "' for labels. Required by " + SelectStyleRule.class.getSimpleName());
		}
		if(rightButtonLabelStyle != null && !theme.containsLabelStyleRuleset(rightButtonLabelStyle)) {
			throw new MdxException("No style with id '" + rightButtonLabelStyle + "' for labels. Required by " + SelectStyleRule.class.getSimpleName());
		}
	}
	
	@Override
	public void prepareAssets(UiTheme theme, FileHandleResolver fileHandleResolver, AssetManager assetManager) {
		if (theme.isHeadless()) {
			return; 
		}
		super.prepareAssets(theme, fileHandleResolver, assetManager);

		if(background != null) {
			normalBackgroundRenderer = BackgroundRenderer.parse(background);
			normalBackgroundRenderer.prepareAssets(theme, fileHandleResolver, assetManager);
		}
		if(leftButtonLabelStyle == null) {
			leftButtonLabelStyle = enabledLabelStyle;
		}
		if(rightButtonLabelStyle == null) {
			rightButtonLabelStyle = enabledLabelStyle;
		}
	}

	public BackgroundRenderer getNormalBackgroundRenderer() {
		return normalBackgroundRenderer;
	}

	public int getButtonWidth() {
		return buttonWidth;
	}

	public void setButtonWidth(int buttonWidth) {
		this.buttonWidth = buttonWidth;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public String getEnabledLabelStyle() {
		return enabledLabelStyle;
	}

	public void setEnabledLabelStyle(String enabledLabelStyle) {
		this.enabledLabelStyle = enabledLabelStyle;
	}

	public String getDisabledLabelStyle() {
		return disabledLabelStyle;
	}

	public void setDisabledLabelStyle(String disabledLabelStyle) {
		this.disabledLabelStyle = disabledLabelStyle;
	}

	public String getLeftButtonStyle() {
		return leftButtonStyle;
	}

	public void setLeftButtonStyle(String leftButtonStyle) {
		this.leftButtonStyle = leftButtonStyle;
	}

	public String getRightButtonStyle() {
		return rightButtonStyle;
	}

	public void setRightButtonStyle(String rightButtonStyle) {
		this.rightButtonStyle = rightButtonStyle;
	}

	public String getLeftButtonLabelStyle() {
		return leftButtonLabelStyle;
	}

	public void setLeftButtonLabelStyle(String leftButtonLabelStyle) {
		this.leftButtonLabelStyle = leftButtonLabelStyle;
	}

	public String getRightButtonLabelStyle() {
		return rightButtonLabelStyle;
	}

	public void setRightButtonLabelStyle(String rightButtonLabelStyle) {
		this.rightButtonLabelStyle = rightButtonLabelStyle;
	}
}
