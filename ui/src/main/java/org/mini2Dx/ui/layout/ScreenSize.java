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
package org.mini2Dx.ui.layout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.math.MathUtils;

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
	
	private static final List<ScreenSize> smallestToLargest = new ArrayList<ScreenSize>() {
		{
			add(XS);
			add(SM);
			add(MD);
			add(LG);
			add(XL);
		}
	};
	private static final List<ScreenSize> largestToSmallest = new ArrayList<ScreenSize>() {
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
