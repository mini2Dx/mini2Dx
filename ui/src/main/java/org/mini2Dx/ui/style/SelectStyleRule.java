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
