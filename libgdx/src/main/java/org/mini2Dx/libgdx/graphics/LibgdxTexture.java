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

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.TextureData;
import org.mini2Dx.core.graphics.Pixmap;
import org.mini2Dx.core.graphics.Texture;

public class LibgdxTexture extends com.badlogic.gdx.graphics.Texture implements Texture {

	public LibgdxTexture(String internalPath) {
		super(internalPath);
	}

	public LibgdxTexture(FileHandle file) {
		super(file);
	}

	public LibgdxTexture(FileHandle file, boolean useMipMaps) {
		super(file, useMipMaps);
	}

	public LibgdxTexture(FileHandle file, com.badlogic.gdx.graphics.Pixmap.Format format, boolean useMipMaps) {
		super(file, format, useMipMaps);
	}

	public LibgdxTexture(com.badlogic.gdx.graphics.Pixmap pixmap) {
		super(pixmap);
	}

	public LibgdxTexture(com.badlogic.gdx.graphics.Pixmap pixmap, boolean useMipMaps) {
		super(pixmap, useMipMaps);
	}

	public LibgdxTexture(com.badlogic.gdx.graphics.Pixmap pixmap, com.badlogic.gdx.graphics.Pixmap.Format format, boolean useMipMaps) {
		super(pixmap, format, useMipMaps);
	}

	public LibgdxTexture(int width, int height, com.badlogic.gdx.graphics.Pixmap.Format format) {
		super(width, height, format);
	}

	public LibgdxTexture(TextureData data) {
		super(data);
	}

	protected LibgdxTexture(int glTarget, int glHandle, TextureData data) {
		super(glTarget, glHandle, data);
	}

	@Override
	public void draw(Pixmap pixmap, int x, int y) {
		final LibgdxPixmap gdxPixmap = (LibgdxPixmap) pixmap;
		draw(gdxPixmap.pixmap, x, y);
	}
}
