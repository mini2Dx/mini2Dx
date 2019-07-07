/**
 * Copyright (c) 2019 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
