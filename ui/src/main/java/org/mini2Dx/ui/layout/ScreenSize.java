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
package org.mini2Dx.ui.layout;

import org.mini2Dx.gdx.math.MathUtils;
import org.mini2Dx.gdx.utils.Array;

import java.util.Iterator;

/**
 * Represents different screen size categories
 */
public enum ScreenSize {
	/**
	 * Extra small window size: 0px - 765px
	 */
	XS(0),
	/**
	 * Small window size: 768px - 991px
	 */
	SM(768),
	/**
	 * Medium window size: 992px - 1199px 
	 */
	MD(992),
	/**
	 * Large window size: 1200px - 1599px
	 */
	LG(1200),
	/**
	 * Extra large window size: 1600px+
	 */
	XL(1600);
	
	private static final Array<ScreenSize> smallestToLargest = new Array<ScreenSize>(true, 5, ScreenSize.class) {
		{
			add(XS);
			add(SM);
			add(MD);
			add(LG);
			add(XL);
		}
	};
	private static final Array<ScreenSize> largestToSmallest = new Array<ScreenSize>(true, 5, ScreenSize.class) {
		{
			add(XL);
			add(LG);
			add(MD);
			add(SM);
			add(XS);
		}
	};
	
	private final int minSize;
	
	private ScreenSize(int minSize) {
		this.minSize = minSize;
	}

	/**
	 * Returns the minimum screen size in pixels
	 * @return
	 */
	public int getMinSize(float scale) {
		return MathUtils.round(minSize * scale);
	}
	
	/**
	 * Returns if this {@link ScreenSize} is larger than another {@link ScreenSize} instance
	 * @param otherSize The {@link ScreenSize} to check against
	 * @return True if the minimum screen size is larger
	 */
	public boolean isGreaterThan(ScreenSize otherSize) {
		return minSize > otherSize.minSize;
	}
	
	/**
	 * Returns an {@link Iterator} of smallest to largest {@link ScreenSize}s
	 * @return
	 */
	public static Iterator<ScreenSize> smallestToLargest() {
		return smallestToLargest.iterator();
	}
	
	/**
	 * Returns an {@link Iterator} of largest to smallest {@link ScreenSize}s
	 * @return
	 */
	public static Iterator<ScreenSize> largestToSmallest() {
		return largestToSmallest.iterator();
	}
	
	public static ScreenSize fromString(String value) {
		switch(value.toLowerCase()) {
		case "xs":
			return ScreenSize.XS;
		case "sm":
			return ScreenSize.SM;
		case "md":
			return ScreenSize.MD;
		case "lg":
			return ScreenSize.LG;
		case "xl":
			return ScreenSize.XL;
		}
		return ScreenSize.XS;
	}
}
