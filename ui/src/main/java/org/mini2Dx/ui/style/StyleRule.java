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
package org.mini2Dx.ui.style;

import org.mini2Dx.core.serialization.annotation.Field;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.utils.Array;

/**
 *
 */
public class StyleRule {
	@Field
	private int paddingTop, paddingBottom, paddingLeft, paddingRight;
	@Field
	private int marginTop, marginBottom, marginLeft, marginRight;
	
	public void validate(UiTheme theme) {
		
	}
	
	public void loadDependencies(UiTheme theme, Array<AssetDescriptor> dependencies) {
		
	}
	
	public void prepareAssets(UiTheme theme, FileHandleResolver fileHandleResolver, AssetManager assetManager) {
		
	}
	
	public int getPaddingTop() {
		return paddingTop;
	}
	
	public void setPaddingTop(int paddingTop) {
		this.paddingTop = paddingTop;
	}
	
	public int getPaddingBottom() {
		return paddingBottom;
	}
	
	public void setPaddingBottom(int paddingBottom) {
		this.paddingBottom = paddingBottom;
	}
	
	public int getPaddingLeft() {
		return paddingLeft;
	}
	
	public void setPaddingLeft(int paddingLeft) {
		this.paddingLeft = paddingLeft;
	}
	
	public int getPaddingRight() {
		return paddingRight;
	}
	
	public void setPaddingRight(int paddingRight) {
		this.paddingRight = paddingRight;
	}
	
	public int getMarginTop() {
		return marginTop;
	}
	
	public void setMarginTop(int marginTop) {
		this.marginTop = marginTop;
	}
	
	public int getMarginBottom() {
		return marginBottom;
	}
	
	public void setMarginBottom(int marginBottom) {
		this.marginBottom = marginBottom;
	}
	
	public int getMarginLeft() {
		return marginLeft;
	}
	
	public void setMarginLeft(int marginLeft) {
		this.marginLeft = marginLeft;
	}
	
	public int getMarginRight() {
		return marginRight;
	}
	
	public void setMarginRight(int marginRight) {
		this.marginRight = marginRight;
	}
}
