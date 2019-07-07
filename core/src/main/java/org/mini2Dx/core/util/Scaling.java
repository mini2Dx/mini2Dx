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
package org.mini2Dx.core.util;

import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.math.Vector2;

public enum Scaling {
	NONE,
	FIT,
	FILL,
	FILL_X,
	FILL_Y,
	STRETCH;

	public void apply(Vector2 sizeResult, Vector2 scaleResult, boolean powerOfTwo, float sourceWidth, float sourceHeight,
	                  float targetWidth, float targetHeight) {
		switch(this) {
		case FIT: {
			float targetRatio = targetHeight / targetWidth;
			float sourceRatio = sourceHeight / sourceWidth;
			float scale = targetRatio > sourceRatio ? targetWidth / sourceWidth : targetHeight / sourceHeight;
			if(powerOfTwo) {
				scale = PowerOfTwo.previousPowerOfTwo(MathUtils.floor(scale));
			}
			sizeResult.set(sourceWidth * scale, sourceHeight * scale);
			scaleResult.set(scale, scale);
			break;
		}
		case FILL: {
			float targetRatio = targetHeight / targetWidth;
			float sourceRatio = sourceHeight / sourceWidth;
			float scale = targetRatio < sourceRatio ? targetWidth / sourceWidth : targetHeight / sourceHeight;
			if(powerOfTwo) {
				scale = PowerOfTwo.previousPowerOfTwo(MathUtils.ceil(scale));
			}
			sizeResult.set(sourceWidth * scale, sourceHeight * scale);
			scaleResult.set(scale, scale);
			break;
		}
		case FILL_X: {
			float scale = targetWidth / sourceWidth;
			if(powerOfTwo) {
				scale = PowerOfTwo.previousPowerOfTwo(MathUtils.ceil(scale));
			}
			sizeResult.set(sourceWidth * scale, sourceHeight * scale);
			scaleResult.set(scale, scale);
			break;
		}
		case FILL_Y:
			float scale = targetHeight / sourceHeight;
			if(powerOfTwo) {
				scale = PowerOfTwo.previousPowerOfTwo(MathUtils.ceil(scale));
			}
			sizeResult.set(sourceWidth * scale, sourceHeight * scale);
			scaleResult.set(scale, scale);
			break;
		case STRETCH:
			float scaleX = targetWidth / sourceWidth;
			float scaleY = targetHeight / sourceHeight;
			if(powerOfTwo) {
				scaleX = PowerOfTwo.previousPowerOfTwo(MathUtils.ceil(scaleX));
				scaleY = PowerOfTwo.previousPowerOfTwo(MathUtils.ceil(scaleY));
			}
			sizeResult.set(targetWidth, targetHeight);
			scaleResult.set(scaleX, scaleY);
			break;
		default:
		case NONE:
			sizeResult.set(sourceWidth, sourceHeight);
			scaleResult.set(1f, 1f);
			break;
		}
	}

	private boolean isPowerOfTwo(int value) {
		return (value != 0) && ((value & (value - 1)) == 0);
	}
}
