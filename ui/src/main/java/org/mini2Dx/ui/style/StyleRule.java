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
package org.mini2Dx.ui.style;

import org.mini2Dx.core.assets.AssetDescriptor;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.ui.element.UiElement;

/**
 * Applies styling (e.g. padding, margin, etc.) to a {@link UiElement}s
 */
public class StyleRule {
	public static final StyleRule NOOP = new StyleRule();

	@Field(optional=true)
	private int paddingTop, paddingBottom, paddingLeft, paddingRight;
	@Field(optional=true)
	private int marginTop, marginBottom, marginLeft, marginRight;
	@Field(optional=true)
	private int minHeight;
	
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
	
	public void setPadding(int padding) {
		this.paddingTop = padding;
		this.paddingBottom = padding;
		this.paddingLeft = padding;
		this.paddingRight = padding;
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
	
	public void setMargin(int margin) {
		this.marginTop = margin;
		this.marginBottom = margin;
		this.marginLeft = margin;
		this.marginRight = margin;
	}

	public int getMinHeight() {
		return minHeight;
	}

	public void setMinHeight(int minHeight) {
		this.minHeight = minHeight;
	}

	@Override
	public String toString() {
		return "StyleRule [paddingTop=" + paddingTop + ", paddingBottom=" + paddingBottom + ", paddingLeft="
				+ paddingLeft + ", paddingRight=" + paddingRight + ", marginTop=" + marginTop + ", marginBottom="
				+ marginBottom + ", marginLeft=" + marginLeft + ", marginRight=" + marginRight + ", minHeight="
				+ minHeight + "]";
	}
}
