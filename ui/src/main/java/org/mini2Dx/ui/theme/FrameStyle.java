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
package org.mini2Dx.ui.theme;

import org.mini2Dx.core.serialization.annotation.Field;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;

/**
 *
 */
public class FrameStyle extends BaseUiElementStyle {
	@Field
	private String backgroundImage;
	@Field
	private String scrollBarImage;
	@Field
	private int verticalScrollBarWidth;
	@Field
	private int horizontalScrollBarHeight;
	@Field
	private int scrollBarPaddingTop;
	@Field
	private int scrollBarPaddingBottom;
	@Field
	private int scrollBarPaddingLeft;
	@Field
	private int scrollBarPaddingRight;
	
	private NinePatch backgroundNinePatch, scrollBarNinePatch;
	
	@Override
	public void prepareAssets(AssetManager assetManager) {
		backgroundNinePatch = new NinePatch(assetManager.get(backgroundImage, Texture.class), getPaddingLeft(),
				getPaddingRight(), getPaddingTop(), getPaddingBottom());
		scrollBarNinePatch = new NinePatch(assetManager.get(scrollBarImage, Texture.class), getScrollBarPaddingLeft(),
				getScrollBarPaddingRight(), getScrollBarPaddingTop(), getScrollBarPaddingBottom());
	}

	public String getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public String getScrollBarImage() {
		return scrollBarImage;
	}

	public void setScrollBarImage(String scrollBarImage) {
		this.scrollBarImage = scrollBarImage;
	}

	public int getVerticalScrollBarWidth() {
		return verticalScrollBarWidth;
	}

	public void setVerticalScrollBarWidth(int verticalScrollBarWidth) {
		this.verticalScrollBarWidth = verticalScrollBarWidth;
	}

	public int getHorizontalScrollBarHeight() {
		return horizontalScrollBarHeight;
	}

	public void setHorizontalScrollBarHeight(int horizontalScrollBarHeight) {
		this.horizontalScrollBarHeight = horizontalScrollBarHeight;
	}

	public int getScrollBarPaddingTop() {
		return scrollBarPaddingTop;
	}

	public void setScrollBarPaddingTop(int scrollBarPaddingTop) {
		this.scrollBarPaddingTop = scrollBarPaddingTop;
	}

	public int getScrollBarPaddingBottom() {
		return scrollBarPaddingBottom;
	}

	public void setScrollBarPaddingBottom(int scrollBarPaddingBottom) {
		this.scrollBarPaddingBottom = scrollBarPaddingBottom;
	}

	public int getScrollBarPaddingLeft() {
		return scrollBarPaddingLeft;
	}

	public void setScrollBarPaddingLeft(int scrollBarPaddingLeft) {
		this.scrollBarPaddingLeft = scrollBarPaddingLeft;
	}

	public int getScrollBarPaddingRight() {
		return scrollBarPaddingRight;
	}

	public void setScrollBarPaddingRight(int scrollBarPaddingRight) {
		this.scrollBarPaddingRight = scrollBarPaddingRight;
	}

	public NinePatch getBackgroundNinePatch() {
		return backgroundNinePatch;
	}

	public NinePatch getScrollBarNinePatch() {
		return scrollBarNinePatch;
	}
}