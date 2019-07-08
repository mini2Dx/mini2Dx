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
package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import org.mini2Dx.core.graphics.TextureFilter;
import org.mini2Dx.libgdx.graphics.LibgdxTexture;

public class LibgdxSpriteBatchWrapper extends SpriteBatch {
	private TextureFilter minFilter = TextureFilter.PIXEL;
	private TextureFilter magFilter = TextureFilter.PIXEL;

	public LibgdxSpriteBatchWrapper() {
	}

	public LibgdxSpriteBatchWrapper(int size) {
		super(size);
	}

	public LibgdxSpriteBatchWrapper(int size, ShaderProgram defaultShader) {
		super(size, defaultShader);
	}

	@Override
	protected void switchTexture(Texture texture) {
		if(texture instanceof LibgdxTexture) {
			LibgdxTexture gdxTexture = (LibgdxTexture) texture;
			setTextureFilters(gdxTexture);
		}
		super.switchTexture(texture);
	}

	private void setTextureFilters(LibgdxTexture texture) {
		if(!texture.getMagTextureFilter().equals(magFilter)) {
			texture.setTextureFilter(minFilter, magFilter);
			return;
		}
		if(!texture.getMinTextureFilter().equals(minFilter)) {
			texture.setTextureFilter(minFilter, magFilter);
			return;
		}
	}

	public TextureFilter getMinFilter() {
		return minFilter;
	}

	public void setMinFilter(TextureFilter minFilter) {
		this.minFilter = minFilter;
	}

	public TextureFilter getMagFilter() {
		return magFilter;
	}

	public void setMagFilter(TextureFilter magFilter) {
		this.magFilter = magFilter;
	}
}
