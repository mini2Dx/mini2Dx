/*******************************************************************************
 * Copyright 2019 Viridian Software Limited
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
package org.mini2Dx.core.util;

/**
 * Common interface for objects that need to be interpolated between frames
 */
public interface Interpolatable {
	/**
	 * Called before each update() phase to store existing state
	 */
	public void preUpdate();

	/**
	 * Called each interpolate() phase to interpolate the render state
	 * @param alpha The value (between 0.0 and 1.0) representing the inbetween progress between the previous and current update state
	 */
	public void interpolate(float alpha);
}
