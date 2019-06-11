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
package org.mini2Dx.core;

import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.graphics.*;

public interface GraphicsUtils {

	public Color newColor(int rgba8888);

	public Color newColor(int r, int g, int b, int a);

	public Color newColor(float r, float g, float b, float a);

	public Color newColor(byte r, byte g, byte b, byte a);

	public Pixmap newPixmap(int width, int height, PixmapFormat format);

	public Pixmap newPixmap(byte[] encodedData, int offset, int len);

	public Pixmap newPixmap(FileHandle file);

	public Texture newTexture(FileHandle file);

	public Texture newTexture(FileHandle file, PixmapFormat format);

	public Texture newTexture(Pixmap pixmap);

	public Texture newTexture(Pixmap pixmap, PixmapFormat format);

	public TextureRegion newTextureRegion(Texture texture);

	public TextureRegion newTextureRegion(Texture texture, int width, int height);

	public TextureRegion newTextureRegion(Texture texture, int x, int y, int width, int height);

	public TextureRegion newTextureRegion(TextureRegion texture);

	public TextureRegion newTextureRegion(TextureRegion texture, int width, int height);

	public TextureRegion newTextureRegion(TextureRegion texture, int x, int y, int width, int height);

	public Sprite newSprite(Texture texture);

	public Sprite newSprite(Texture texture, int width, int height);

	public Sprite newSprite(Texture texture, int x, int y, int width, int height);

	public Sprite newSprite(TextureRegion texture);

	public Sprite newSprite(TextureRegion texture, int width, int height);

	public Sprite newSprite(TextureRegion texture, int x, int y, int width, int height);

	public Sprite newSprite(Sprite sprite);

	/** Loads the specified pack file, using the parent directory of the pack file to find the page images. */
	public TextureAtlas newTextureAtlas (FileHandle packFile);

	/** @param flip If true, all regions loaded will be flipped for use with a perspective where 0,0 is the upper left corner.*/
	public TextureAtlas newTextureAtlas(FileHandle packFile, boolean flip);

	public TextureAtlas newTextureAtlas(FileHandle packFile, FileHandle imagesDir);

	/** @param flip If true, all regions loaded will be flipped for use with a perspective where 0,0 is the upper left corner. */
	public TextureAtlas newTextureAtlas(FileHandle packFile, FileHandle imagesDir, boolean flip);

	public ParticleEffect newParticleEffect();

	public ParticleEffect newParticleEffect(FileHandle effectFile, FileHandle imagesDir);

	public ParticleEffect newParticleEffect(FileHandle effectFile, TextureAtlas atlas);

	public NinePatch newNinePatch(Texture texture, int left, int right, int top, int bottom);

	public NinePatch newNinePatch(TextureRegion region, int left, int right, int top, int bottom);

	public FrameBuffer newFrameBuffer(int width, int height);
}
