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
package org.mini2Dx.libgdx.graphics;

import com.badlogic.gdx.graphics.LibgdxNinePatchWrapper;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.graphics.Colors;
import org.mini2Dx.core.graphics.NinePatch;
import org.mini2Dx.core.graphics.Texture;

public class LibgdxNinePatch implements NinePatch {
	public final LibgdxNinePatchWrapper ninepatch;
	private final Color tmpColor = Mdx.graphics.newColor(Colors.WHITE());

	public LibgdxNinePatch(LibgdxTexture texture, int left, int right, int top, int bottom) {
		ninepatch = new LibgdxNinePatchWrapper(texture, left, right, top, bottom);
	}

	public LibgdxNinePatch(LibgdxTextureRegion textureRegion, int left, int right, int top, int bottom) {
		ninepatch = new LibgdxNinePatchWrapper(textureRegion.textureRegion, left, right, top, bottom);
	}

	public LibgdxNinePatch(LibgdxNinePatch ninepatch) {
		this.ninepatch = new LibgdxNinePatchWrapper(ninepatch.ninepatch);
	}

	@Override
	public void render(Graphics g, float x, float y, float width, float height) {
		ninepatch.render(g, x, y, width, height);
	}

	@Override
	public Color getColor() {
		tmpColor.set(ninepatch.getColor().r, ninepatch.getColor().g, ninepatch.getColor().b, ninepatch.getColor().a);
		return tmpColor;
	}

	@Override
	public void setColor(Color color) {
		ninepatch.setColor(color.rf(), color.gf(), color.bf(), color.af());
	}

	@Override
	public float getLeftWidth() {
		return ninepatch.getLeftWidth();
	}

	@Override
	public void setLeftWidth(float leftWidth) {
		ninepatch.setLeftWidth(leftWidth);
	}

	@Override
	public float getRightWidth() {
		return ninepatch.getRightWidth();
	}

	@Override
	public void setRightWidth(float rightWidth) {
		ninepatch.setRightWidth(rightWidth);
	}

	@Override
	public float getTopHeight() {
		return ninepatch.getTopHeight();
	}

	@Override
	public void setTopHeight(float topHeight) {
		ninepatch.setTopHeight(topHeight);
	}

	@Override
	public float getBottomHeight() {
		return ninepatch.getBottomHeight();
	}

	@Override
	public void setBottomHeight(float bottomHeight) {
		ninepatch.setBottomHeight(bottomHeight);
	}

	@Override
	public float getMiddleWidth() {
		return ninepatch.getMiddleWidth();
	}

	@Override
	public void setMiddleWidth(float middleWidth) {
		ninepatch.setMiddleWidth(middleWidth);
	}

	@Override
	public float getMiddleHeight() {
		return ninepatch.getMiddleHeight();
	}

	@Override
	public void setMiddleHeight(float middleHeight) {
		ninepatch.setMiddleHeight(middleHeight);
	}

	@Override
	public float getTotalWidth() {
		return ninepatch.getTotalWidth();
	}

	@Override
	public float getTotalHeight() {
		return ninepatch.getTotalHeight();
	}

	@Override
	public void setPadding(float left, float right, float top, float bottom) {
		ninepatch.setPadding(left, right, top, bottom);
	}

	@Override
	public float getPaddingLeft() {
		return ninepatch.getPaddingLeft();
	}

	@Override
	public void setPaddingLeft(float left) {
		ninepatch.setPaddingLeft(left);
	}

	@Override
	public float getPaddingRight() {
		return ninepatch.getPaddingRight();
	}

	@Override
	public void setPaddingRight(float right) {
		ninepatch.setPaddingRight(right);
	}

	@Override
	public float getPaddingTop() {
		return ninepatch.getPaddingTop();
	}

	@Override
	public void setPaddingTop(float top) {
		ninepatch.setPaddingTop(top);
	}

	@Override
	public float getPaddingBottom() {
		return ninepatch.getPaddingBottom();
	}

	@Override
	public void setPaddingBottom(float bottom) {
		ninepatch.setPaddingBottom(bottom);
	}

	@Override
	public void scale(float scaleX, float scaleY) {
		ninepatch.scale(scaleX, scaleY);
	}

	@Override
	public Texture getTexture() {
		return ninepatch.getTexture();
	}
}
