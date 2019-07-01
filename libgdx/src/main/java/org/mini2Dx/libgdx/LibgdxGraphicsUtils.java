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
package org.mini2Dx.libgdx;

import org.mini2Dx.core.GraphicsUtils;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.graphics.*;
import org.mini2Dx.libgdx.graphics.LibgdxColor;

public class LibgdxGraphicsUtils implements GraphicsUtils {
	@Override
	public Color newColor(int rgba8888) {
		return new LibgdxColor(rgba8888);
	}

	@Override
	public Color newColor(int r, int g, int b, int a) {
		return new LibgdxColor(r, g, b, a);
	}

	@Override
	public Color newColor(float r, float g, float b, float a) {
		return new LibgdxColor(r, g, b, a);
	}

	@Override
	public Color newColor(byte r, byte g, byte b, byte a) {
		return new LibgdxColor(r, g, b, a);
	}

	@Override
	public Pixmap newPixmap(int width, int height, PixmapFormat format) {
		return null;
	}

	@Override
	public Pixmap newPixmap(FileHandle file) {
		return null;
	}

	@Override
	public Texture newTexture(FileHandle file) {
		return null;
	}

	@Override
	public Texture newTexture(FileHandle file, PixmapFormat format) {
		return null;
	}

	@Override
	public Texture newTexture(Pixmap pixmap) {
		return null;
	}

	@Override
	public Texture newTexture(Pixmap pixmap, PixmapFormat format) {
		return null;
	}

	@Override
	public Texture newTexture(byte[] fileData) {
		return null;
	}

	@Override
	public TextureRegion newTextureRegion(Texture texture) {
		return null;
	}

	@Override
	public TextureRegion newTextureRegion(Texture texture, int width, int height) {
		return null;
	}

	@Override
	public TextureRegion newTextureRegion(Texture texture, int x, int y, int width, int height) {
		return null;
	}

	@Override
	public TextureRegion newTextureRegion(TextureRegion texture) {
		return null;
	}

	@Override
	public TextureRegion newTextureRegion(TextureRegion texture, int width, int height) {
		return null;
	}

	@Override
	public TextureRegion newTextureRegion(TextureRegion texture, int x, int y, int width, int height) {
		return null;
	}

	@Override
	public Sprite newSprite(Texture texture) {
		return null;
	}

	@Override
	public Sprite newSprite(Texture texture, int width, int height) {
		return null;
	}

	@Override
	public Sprite newSprite(Texture texture, int x, int y, int width, int height) {
		return null;
	}

	@Override
	public Sprite newSprite(TextureRegion texture) {
		return null;
	}

	@Override
	public Sprite newSprite(TextureRegion texture, int width, int height) {
		return null;
	}

	@Override
	public Sprite newSprite(TextureRegion texture, int x, int y, int width, int height) {
		return null;
	}

	@Override
	public Sprite newSprite(Sprite sprite) {
		return null;
	}

	@Override
	public TextureAtlas newTextureAtlas(FileHandle packFile) {
		return null;
	}

	@Override
	public TextureAtlas newTextureAtlas(FileHandle packFile, boolean flip) {
		return null;
	}

	@Override
	public TextureAtlas newTextureAtlas(FileHandle packFile, FileHandle imagesDir) {
		return null;
	}

	@Override
	public TextureAtlas newTextureAtlas(FileHandle packFile, FileHandle imagesDir, boolean flip) {
		return null;
	}

	@Override
	public ParticleEffect newParticleEffect() {
		return null;
	}

	@Override
	public ParticleEffect newParticleEffect(FileHandle effectFile, FileHandle imagesDir) {
		return null;
	}

	@Override
	public ParticleEffect newParticleEffect(FileHandle effectFile, TextureAtlas atlas) {
		return null;
	}

	@Override
	public NinePatch newNinePatch(Texture texture, int left, int right, int top, int bottom) {
		return null;
	}

	@Override
	public NinePatch newNinePatch(TextureRegion region, int left, int right, int top, int bottom) {
		return null;
	}

	@Override
	public FrameBuffer newFrameBuffer(int width, int height) {
		return null;
	}
}
