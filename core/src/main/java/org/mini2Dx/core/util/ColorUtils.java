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

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.graphics.Color;

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
		return Mdx.graphics.newColor(Float.parseFloat(values[0]) / 255f, Float.parseFloat(values[1]) / 255f,
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
		return Mdx.graphics.newColor(Float.parseFloat(values[0]) / 255f, Float.parseFloat(values[1]) / 255f,
				Float.parseFloat(values[2]) / 255f, Float.parseFloat(values[3]));
	}
}
