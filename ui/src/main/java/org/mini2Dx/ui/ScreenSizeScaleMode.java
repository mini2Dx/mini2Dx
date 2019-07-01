/**
 * Copyright 2017 Thomas Cashman
 */
package org.mini2Dx.ui;

import org.mini2Dx.ui.layout.ScreenSize;

/**
 *
 */
public enum ScreenSizeScaleMode {
	/**
	 * No scaling is applied to {@link ScreenSize} values - this is the default
	 * mode. For example, if the {@link UiContainer} is
	 * scaled 2x, {@link ScreenSize} values remain at 1x scale.
	 */
	NO_SCALING,
	/**
	 * Applies the same scale set on the {@link UiContainer} to
	 * {@link ScreenSize} values. For example, if the {@link UiContainer} is
	 * scaled 2x, {@link ScreenSize} values are also scaled 2x
	 */
	LINEAR,
	/**
	 * Applies inverse scaling to {@link ScreenSize} values. For example, if the
	 * {@link UiContainer} is scaled x2, then {@link ScreenSize} values are
	 * halved.
	 */
	INVERSE
}
