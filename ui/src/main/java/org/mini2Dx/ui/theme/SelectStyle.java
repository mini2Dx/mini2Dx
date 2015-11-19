/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.theme;

import org.mini2Dx.core.serialization.annotation.Field;

import com.badlogic.gdx.assets.AssetManager;

/**
 *
 */
public class SelectStyle extends BaseUiElementStyle {
	@Field
	private String buttonStyle;
	@Field
	private String labelStyle;

	@Override
	public void prepareAssets(AssetManager assetManager) {
	}

	public String getButtonStyle() {
		return buttonStyle;
	}

	public void setButtonStyle(String buttonStyle) {
		this.buttonStyle = buttonStyle;
	}

	public String getLabelStyle() {
		return labelStyle;
	}

	public void setLabelStyle(String labelStyle) {
		this.labelStyle = labelStyle;
	}

}
