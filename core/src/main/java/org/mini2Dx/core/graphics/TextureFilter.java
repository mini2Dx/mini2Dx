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
package org.mini2Dx.core.graphics;

public enum TextureFilter {
	/**
	 * Fetch four nearest texels that best maps to the pixel on screen
	 */
	LINEAR,
	/**
	 * Fetch the nearest texel that best maps to the pixel on screen
	 */
	PIXEL,

	/**
	 * Same as LINEAR, but samples the nearest mip map.
	 */
	LINEAR_MIP_POINT,

	/**
	 * Same as PIXEL, but samples linearly between mip maps.
	 */
	PIXEL_MIP_LINEAR

}
