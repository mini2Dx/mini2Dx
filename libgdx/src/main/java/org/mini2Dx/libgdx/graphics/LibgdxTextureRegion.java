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

import com.badlogic.gdx.graphics.LibgdxTextureRegionWrapper;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.graphics.Pixmap;
import org.mini2Dx.core.graphics.PixmapFormat;
import org.mini2Dx.core.graphics.Texture;
import org.mini2Dx.core.graphics.TextureRegion;

public class LibgdxTextureRegion implements TextureRegion {
	public final LibgdxTextureRegionWrapper textureRegion;

	public LibgdxTextureRegion(LibgdxTextureRegionWrapper textureRegion) {
		this.textureRegion = textureRegion;
		setFlip(false, false);
	}

	public LibgdxTextureRegion(LibgdxTextureRegion textureRegion) {
		this.textureRegion = new LibgdxTextureRegionWrapper(textureRegion.textureRegion);
	}

	public LibgdxTextureRegion(LibgdxTextureRegion textureRegion, int x, int y, int width, int height) {
		this.textureRegion = new LibgdxTextureRegionWrapper(textureRegion.textureRegion, x, y, width, height);
	}

	@Override
	public void setRegion(Texture texture) {
		final LibgdxTexture gdxTexture = (LibgdxTexture) texture;
		textureRegion.setRegion(gdxTexture);
	}

	@Override
	public void setRegion(int x, int y, int width, int height) {
		textureRegion.setRegion(x, y, width, height);
	}

	@Override
	public void setRegion(float u, float v, float u2, float v2) {
		textureRegion.setRegion(u, v, u2, v2);
	}

	@Override
	public void setRegion(TextureRegion region) {
		final LibgdxTextureRegion gdxTextureRegion = (LibgdxTextureRegion) region;
		textureRegion.setRegion(gdxTextureRegion.textureRegion);
	}

	@Override
	public void setRegion(TextureRegion region, int x, int y, int width, int height) {
		final LibgdxTextureRegion gdxTextureRegion = (LibgdxTextureRegion) region;
		textureRegion.setRegion(gdxTextureRegion.textureRegion, x, y, width, height);
	}

	@Override
	public Texture getTexture() {
		return (LibgdxTexture) textureRegion.getTexture();
	}

	@Override
	public void setTexture(Texture texture) {
		final LibgdxTexture gdxTexture = (LibgdxTexture) texture;
		textureRegion.setTexture(gdxTexture);
	}

	@Override
	public float getU() {
		return textureRegion.getU();
	}

	@Override
	public void setU(float u) {
		textureRegion.setU(u);
	}

	@Override
	public float getV() {
		return textureRegion.getV();
	}

	@Override
	public void setV(float v) {
		textureRegion.setV(v);
	}

	@Override
	public float getU2() {
		return textureRegion.getU2();
	}

	@Override
	public void setU2(float u2) {
		textureRegion.setU2(u2);
	}

	@Override
	public float getV2() {
		return textureRegion.getV2();
	}

	@Override
	public void setV2(float v2) {
		textureRegion.setV2(v2);
	}

	@Override
	public int getRegionX() {
		return textureRegion.getRegionX();
	}

	@Override
	public void setRegionX(int x) {
		textureRegion.setRegionX(x);
	}

	@Override
	public int getRegionY() {
		return textureRegion.getRegionY();
	}

	@Override
	public void setRegionY(int y) {
		textureRegion.setRegionY(y);
	}

	@Override
	public int getRegionWidth() {
		return textureRegion.getRegionWidth();
	}

	@Override
	public void setRegionWidth(int width) {
		textureRegion.setRegionWidth(width);
	}

	@Override
	public int getRegionHeight() {
		return textureRegion.getRegionHeight();
	}

	@Override
	public void setRegionHeight(int height) {
		textureRegion.setRegionHeight(height);
	}

	@Override
	public void flip(boolean x, boolean y) {
		textureRegion.flip(x, y);
	}

	@Override
	public void setFlip(boolean flipX, boolean flipY) {
		setFlipX(flipX);
		setFlipY(flipY);
	}

	@Override
	public boolean isFlipX() {
		return textureRegion.isFlipX();
	}

	@Override
	public void setFlipX(boolean flipX) {
		textureRegion.setFlipX(flipX);
	}

	@Override
	public boolean isFlipY() {
		return textureRegion.isFlipY();
	}

	@Override
	public void setFlipY(boolean flipY) {
		textureRegion.setFlipY(flipY);
	}

	@Override
	public void scroll(float xAmount, float yAmount) {
		textureRegion.scroll(xAmount, yAmount);
	}

	@Override
	public Pixmap toPixmap() {
		final LibgdxTexture texture = (LibgdxTexture) getTexture();
		if (!texture.getTextureData().isPrepared()) {
			texture.getTextureData().prepare();
		}
		final com.badlogic.gdx.graphics.Pixmap texturePixmap = texture.getTextureData().consumePixmap();
		final LibgdxPixmap result = (LibgdxPixmap) Mdx.graphics.newPixmap(getRegionWidth(), getRegionHeight(), PixmapFormat.RGBA8888);
		result.pixmap.drawPixmap(texturePixmap, 0, 0, getRegionX(), getRegionY(), getRegionWidth(), getRegionHeight());
		return result;
	}

	@Override
	public TextureRegion[][] split(int tileWidth, int tileHeight) {
		int x = getRegionX();
		int y = getRegionY();
		final int width = getRegionWidth();
		final int height = getRegionHeight();

		final int rows = height / tileHeight;
		final int columns = width / tileWidth;

		int startX = x;
		final TextureRegion[][] result = new TextureRegion[rows][columns];
		for (int row = 0; row < rows; row++, y += tileHeight) {
			x = startX;
			for (int col = 0; col < columns; col++, x += tileWidth) {
				result[row][col] = Mdx.graphics.newTextureRegion(getTexture(), x, y, tileWidth, tileHeight);
			}
		}
		return result;
	}
}
