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

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.graphics.Color;

/**
 * Static utility methods for {@link Color}s
 * The colors returned by this class are readonly, if you want to change their values you should call {@link Color#copy()} on them, otherwise you'll get UnsupportedOperationException
 */
public class Colors {

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

	public static Color CLEAR() {
		if(COLOR_CLEAR == null) {
			COLOR_CLEAR = Mdx.graphics.newReadOnlyColor(0f, 0f, 0f, 0f);
		}
		return COLOR_CLEAR;
	}

	public static Color WHITE() {
		if(COLOR_WHITE == null) {
			COLOR_WHITE = Mdx.graphics.newReadOnlyColor(1f, 1f, 1f, 1f);
		}
		return COLOR_WHITE;
	}

	public static Color WHITE_M1() {
		if(COLOR_WHITE_M1 == null) {
			COLOR_WHITE_M1 = Mdx.graphics.newReadOnlyColor(254,254,254, 255);
		}
		return COLOR_WHITE_M1;
	}

	public static Color LIGHT_GRAY() {
		if(COLOR_LIGHT_GRAY == null) {
			COLOR_LIGHT_GRAY = Mdx.graphics.newReadOnlyColor(211,211,211, 255);
		}
		return COLOR_LIGHT_GRAY;
	}

	public static Color GRAY() {
		if(COLOR_GRAY == null) {
			COLOR_GRAY = Mdx.graphics.newReadOnlyColor(128, 128, 128, 255);
		}
		return COLOR_GRAY;
	}

	public static Color DARK_GRAY() {
		if(COLOR_DARK_GRAY == null) {
			COLOR_DARK_GRAY = Mdx.graphics.newReadOnlyColor(105,105,105, 255);
		}
		return COLOR_DARK_GRAY;
	}

	public static Color BLACK_P1() {
		if(COLOR_BLACK_P1 == null) {
			COLOR_BLACK_P1 = Mdx.graphics.newReadOnlyColor(1,1,1, 255);
		}
		return COLOR_BLACK_P1;
	}

	public static Color BLACK() {
		if(COLOR_BLACK == null) {
			COLOR_BLACK = Mdx.graphics.newReadOnlyColor(0f, 0f, 0f, 1f);
		}
		return COLOR_BLACK;
	}

	public static Color RED() {
		if(COLOR_RED == null) {
			COLOR_RED = Mdx.graphics.newReadOnlyColor(1f, 0f, 0f, 1f);
		}
		return COLOR_RED;
	}

	public static Color GREEN() {
		if(COLOR_GREEN == null) {
			COLOR_GREEN = Mdx.graphics.newReadOnlyColor(0f, 1f, 0f, 1f);
		}
		return COLOR_GREEN;
	}

	public static Color BLUE() {
		if(COLOR_BLUE == null) {
			COLOR_BLUE = Mdx.graphics.newReadOnlyColor(0f, 0f, 1f, 1f);
		}
		return COLOR_BLUE;
	}

	public static Color MAROON() {
		if(COLOR_MAROON == null) {
			COLOR_MAROON = Mdx.graphics.newReadOnlyColor(128,0,0, 255);
		}
		return COLOR_MAROON;
	}

	public static Color CORAL() {
		if(COLOR_CORAL == null) {
			COLOR_CORAL = Mdx.graphics.newReadOnlyColor(255,127,80, 255);
		}
		return COLOR_CORAL;
	}

	public static Color SALMON() {
		if(COLOR_SALMON == null) {
			COLOR_SALMON = Mdx.graphics.newReadOnlyColor(250,128,114, 255);
		}
		return COLOR_SALMON;
	}

	public static Color PINK() {
		if(COLOR_PINK == null) {
			COLOR_PINK = Mdx.graphics.newReadOnlyColor(255,192,203, 255);
		}
		return COLOR_PINK;
	}

	public static Color LIME() {
		if(COLOR_LIME == null) {
			COLOR_LIME = Mdx.graphics.newReadOnlyColor(0,255,0, 255);
		}
		return COLOR_LIME;
	}

	public static Color FOREST() {
		if(COLOR_FOREST == null) {
			COLOR_FOREST = Mdx.graphics.newReadOnlyColor(34,139,34, 255);
		}
		return COLOR_FOREST;
	}

	public static Color OLIVE() {
		if(COLOR_OLIVE == null) {
			COLOR_OLIVE = Mdx.graphics.newReadOnlyColor(128,128,0, 255);
		}
		return COLOR_OLIVE;
	}

	public static Color NAVY() {
		if(COLOR_NAVY == null) {
			COLOR_NAVY = Mdx.graphics.newReadOnlyColor(0,0,128, 255);
		}
		return COLOR_NAVY;
	}

	public static Color ROYAL() {
		if(COLOR_ROYAL == null) {
			COLOR_ROYAL = Mdx.graphics.newReadOnlyColor(65,105,225, 255);
		}
		return COLOR_ROYAL;
	}

	public static Color SKY() {
		if(COLOR_SKY == null) {
			COLOR_SKY = Mdx.graphics.newReadOnlyColor(135,206,235, 255);
		}
		return COLOR_SKY;
	}

	public static Color CYAN() {
		if(COLOR_CYAN == null) {
			COLOR_CYAN = Mdx.graphics.newReadOnlyColor(0,255,255, 255);
		}
		return COLOR_CYAN;
	}

	public static Color TEAL() {
		if(COLOR_TEAL == null) {
			COLOR_TEAL = Mdx.graphics.newReadOnlyColor(0,128,128, 255);
		}
		return COLOR_TEAL;
	}

	public static Color YELLOW() {
		if(COLOR_YELLOW == null) {
			COLOR_YELLOW = Mdx.graphics.newReadOnlyColor(255,255,0, 255);
		}
		return COLOR_YELLOW;
	}

	public static Color GOLD() {
		if(COLOR_GOLD == null) {
			COLOR_GOLD = Mdx.graphics.newReadOnlyColor(255,215,0, 255);
		}
		return COLOR_GOLD;
	}

	public static Color GOLDENROD() {
		if(COLOR_GOLDENROD == null) {
			COLOR_GOLDENROD = Mdx.graphics.newReadOnlyColor(218,165,32, 255);
		}
		return COLOR_GOLDENROD;
	}

	public static Color ORANGE() {
		if(COLOR_ORANGE == null) {
			COLOR_ORANGE = Mdx.graphics.newReadOnlyColor(255,165,0, 255);
		}
		return COLOR_ORANGE;
	}

	public static Color BROWN() {
		if(COLOR_BROWN == null) {
			COLOR_BROWN = Mdx.graphics.newReadOnlyColor(165,42,42, 255);
		}
		return COLOR_BROWN;
	}

	public static Color TAN() {
		if(COLOR_TAN == null) {
			COLOR_TAN = Mdx.graphics.newReadOnlyColor(210,180,140, 255);
		}
		return COLOR_TAN;
	}

	public static Color FIREBRICK() {
		if(COLOR_FIREBRICK == null) {
			COLOR_FIREBRICK = Mdx.graphics.newReadOnlyColor(178,34,34, 255);
		}
		return COLOR_FIREBRICK;
	}

	public static Color PURPLE() {
		if(COLOR_PURPLE == null) {
			COLOR_PURPLE = Mdx.graphics.newReadOnlyColor(128,0,128, 255);
		}
		return COLOR_PURPLE;
	}

	public static Color VIOLET() {
		if(COLOR_VIOLET == null) {
			COLOR_VIOLET = Mdx.graphics.newReadOnlyColor(238,130,238, 255);
		}
		return COLOR_VIOLET;
	}

	public static Color MAGENTA() {
		if(COLOR_MAGENTA == null) {
			COLOR_MAGENTA = Mdx.graphics.newReadOnlyColor(255,0,255, 255);
		}
		return COLOR_MAGENTA;
	}

	private static Color COLOR_CLEAR = null;

	private static Color COLOR_WHITE = null;
	private static Color COLOR_WHITE_M1 = null;
	private static Color COLOR_LIGHT_GRAY = null;
	private static Color COLOR_GRAY = null;
	private static Color COLOR_DARK_GRAY = null;
	private static Color COLOR_BLACK_P1 = null;
	private static Color COLOR_BLACK = null;

	private static Color COLOR_RED = null;
	private static Color COLOR_MAROON = null;
	private static Color COLOR_CORAL = null;
	private static Color COLOR_SALMON = null;
	private static Color COLOR_PINK = null;

	private static Color COLOR_GREEN = null;
	private static Color COLOR_LIME = null;
	private static Color COLOR_FOREST = null;
	private static Color COLOR_OLIVE = null;

	private static Color COLOR_BLUE = null;
	private static Color COLOR_NAVY = null;
	private static Color COLOR_ROYAL = null;
	private static Color COLOR_SKY = null;
	private static Color COLOR_CYAN = null;
	private static Color COLOR_TEAL = null;

	private static Color COLOR_YELLOW = null;
	private static Color COLOR_GOLD = null;
	private static Color COLOR_GOLDENROD = null;
	private static Color COLOR_ORANGE = null;
	private static Color COLOR_BROWN = null;
	private static Color COLOR_TAN = null;
	private static Color COLOR_FIREBRICK = null;

	private static Color COLOR_PURPLE = null;
	private static Color COLOR_VIOLET = null;
	private static Color COLOR_MAGENTA = null;

}
