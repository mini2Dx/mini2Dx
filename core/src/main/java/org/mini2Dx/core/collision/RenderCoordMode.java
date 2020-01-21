/*******************************************************************************
 * Copyright 2020 Viridian Software Limited
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
package org.mini2Dx.core.collision;

import org.mini2Dx.gdx.math.MathUtils;

/**
 * Represents how render coordinates of {@link CollisionObject}s should be determined.
 */
public enum RenderCoordMode {
	/**
	 * Coordinates will use {@link MathUtils#floor(float)}
	 */
	FLOOR,
	/**
	 * Coordinates will use {@link MathUtils#ceil(float)}
	 */
	CEIL,
	/**
	 * Coordinates will use {@link MathUtils#round(float)}
	 */
	ROUND;

	/**
	 * The default for new {@link CollisionObject} instances. Defaults to {@link #ROUND}
	 */
	public static RenderCoordMode GLOBAL_DEFAULT = ROUND;

	public int apply(float value) {
		return apply(this, value);
	}

	public static int apply(RenderCoordMode mode, float value) {
		switch(mode) {
		case FLOOR:
			return MathUtils.floor(value);
		case CEIL:
			return MathUtils.ceil(value);
		default:
		case ROUND:
			return MathUtils.round(value);
		}
	}
}
