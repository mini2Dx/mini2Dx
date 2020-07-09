/*******************************************************************************
 * Copyright 2020 See AUTHORS file
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

import org.mini2Dx.gdx.math.MathUtils;

/**
 * Handles lerp operations with configurable levels of accuracy (at cost of CPU time)
 */
public class Lerper {

	public static Mode MODE = Mode.MONOTONIC;

	public static float lerp(float from, float to, float alpha) {
		switch(MODE) {
		case PRECISE:
			return lerp3(from, to, alpha);
		case BOUNDLESS:
			return lerp0(from, to, alpha);
		case MONOTONIC:
			return lerp2(from, to, alpha);
		default:
		case CLAMP:
			return lerp1(from, to, alpha);
		}
	}

	static float lerp0(float from, float to, float alpha) {
		// Exact, monotonic, bounded, determinate, and (for a=b=0) consistent
		final float inverseAlpha = 1.0f - alpha;
		return (from * inverseAlpha) + (to * alpha);
	}

	static float lerp1(float from, float to, float alpha) {
		final float inverseAlpha = 1.0f - alpha;
		return MathUtils.clamp((from * inverseAlpha) + (to * alpha), from, to);
	}

	static float lerp2(float from, float to, float alpha) {
		return from + alpha * (to - from);
	}

	static float lerp3(float from, float to, float alpha) {
		// Exact, monotonic, bounded, determinate, and (for a=b=0) consistent:
		if (from <= 0 && to >= 0 || from >= 0 && to <= 0) {
			return alpha * to + (1 - alpha) * from;
		}

		if (alpha == 1) {
			return to; // exact
		}
		// Exact at t=0, monotonic except near t=1,
		// bounded, determinate, and consistent:
		float x = from + alpha * (to - from);
		return alpha > 1 == to > from ? Math.max(to, x) : Math.min(to, x);  // monotonic near t=1
	}

	public static enum Mode {
		/**
		 * Regular lerp algorithm without bound clamping, i.e. alpha values close to 0f or 1f can overflow outside of from/to bounds due to float precision.
		 */
		BOUNDLESS,
		/**
		 * {@link #BOUNDLESS} mode with clamp applied.
		 *
		 * ~1.5x slower than {@link #BOUNDLESS}
		 */
		CLAMP,
		/**
		 * Accurate except near alpha values of 1f.
		 */
		MONOTONIC,
		/**
		 * Mathematically accurate precision. See <a href="http://www.open-std.org/jtc1/sc22/wg21/docs/papers/2018/p0811r2.html">here</a>.
		 *
		 * ~8x slower than {@link #BOUNDLESS}
		 */
		PRECISE
	}
}
