/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.ui.theme;

import org.mini2Dx.ui.element.Row;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;

/**
 *
 */
public class RowStyle implements UiElementStyle {
	private final Row row;
	
	public RowStyle(Row row) {
		this.row = row;
	}

	@Override
	public void prepareAssets(AssetManager assetManager) {
	}

	@Override
	public int getMinHeight() {
		return 0;
	}

	@Override
	public int getPaddingTop() {
		return 0;
	}

	@Override
	public int getPaddingBottom() {
		return 0;
	}

	@Override
	public int getPaddingLeft() {
		return 0;
	}

	@Override
	public int getPaddingRight() {
		return 0;
	}

	@Override
	public int getMarginTop() {
		return MathUtils.round(row.getRowYOffset());
	}
	
	@Override
	public int getMarginBottom() {
		return 0;
	}

	@Override
	public int getMarginLeft() {
		return 0;
	}

	@Override
	public int getMarginRight() {
		return 0;
	}

}
