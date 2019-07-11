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
