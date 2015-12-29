/**
 * Copyright (c) 2015 See AUTHORS file
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

import org.mini2Dx.core.exception.MdxException;

import com.badlogic.gdx.graphics.Color;

/**
 * Static utility methods for {@link Color}s
 */
public class ColorUtils {

	/**
	 * Converts a RGB value to an instance of {@link Color}
	 * @param value A comma-separated RGB value - e.g. 255,255,255
	 * @return The {@link Color} instance corresponding to the value
	 */
	public static Color rgbToColor(String value) {
		String[] values = value.split(",");
		if(values.length != 3) {
			throw new MdxException("Invalid RGB value: " + value);
		}
		return new Color(Float.parseFloat(values[0]) / 255f, Float.parseFloat(values[1]) / 255f,
				Float.parseFloat(values[2]) / 255f, 1f);
	}

	/**
	 * Converts a RGBA value to an instance of {@link Color}
	 * @param value A comma-separated RGBA value - e.g. 255,255,255,0.3
	 * @return The {@link Color} instance corresponding to the value
	 */
	public static Color rgbaToColor(String value) {
		String[] values = value.split(",");
		if(values.length != 4) {
			throw new MdxException("Invalid RGBA value: " + value);
		}
		return new Color(Float.parseFloat(values[0]) / 255f, Float.parseFloat(values[1]) / 255f,
				Float.parseFloat(values[2]) / 255f, Float.parseFloat(values[3]));
	}
}
