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
package org.mini2Dx.ui.render;

import org.mini2Dx.gdx.math.MathUtils;

/**
 * Different modes for rounding element sizes
 */
public enum SizeRounding {
	/**
	 * Default mode - no-op
	 */
	NONE,
	/**
	 * If element size is uneven, adds 1 pixel
	 */
	UP,
	/**
	 * If element size is uneven, subtracts 1 pixel
	 */
	DOWN;
	
	public float calculateRounding(float value) {
		switch(this) {
		case DOWN:
			if(MathUtils.round(value) % 2 == 1) {
				return value - 1f;
			}
			return value;
		case UP:
			if(MathUtils.round(value) % 2 == 1) {
				return value + 1f;
			}
			return value;
		case NONE:
		default:
			return value;
		}
	}
}
