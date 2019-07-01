/**
 * Copyright 2017 Thomas Cashman
 */
package org.mini2Dx.ui.layout;

/**
 *
 */
public class AutoSizeRule implements SizeRule {

	@Override
	public float getSize(LayoutState layoutState) {
		return 0f;
	}

	@Override
	public boolean isAutoSize() {
		return true;
	}
}
