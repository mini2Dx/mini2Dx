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
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.graphics.*;

public abstract class GraphicsUtils {

	public abstract Color newColor(int rgba8888);

	public abstract Color newColor(int r, int g, int b, int a);

	public abstract Color newColor(float r, float g, float b, float a);

	public abstract Color newColor(byte r, byte g, byte b, byte a);

	public abstract Color newColor(Color color);

	public abstract Color newReadOnlyColor(int rgba8888);

	public abstract Color newReadOnlyColor(int r, int g, int b, int a);

	public abstract Color newReadOnlyColor(float r, float g, float b, float a);

	public abstract Color newReadOnlyColor(byte r, byte g, byte b, byte a);

	public abstract Color newReadOnlyColor(Color color);

	public abstract Pixmap newPixmap(int width, int height, PixmapFormat format);

	public abstract Pixmap newPixmap(FileHandle file);

	public abstract Texture newTexture(FileHandle file);

	public abstract Texture newTexture(FileHandle file, PixmapFormat format);

	public abstract Texture newTexture(Pixmap pixmap);

	public abstract Texture newTexture(Pixmap pixmap, PixmapFormat format);

	public abstract Texture newTexture(byte[] fileData);

	public abstract TextureRegion newTextureRegion(Texture texture);

	public abstract TextureRegion newTextureRegion(Texture texture, int width, int height);

	public abstract TextureRegion newTextureRegion(Texture texture, int x, int y, int width, int height);

	public abstract TextureRegion newTextureRegion(TextureRegion textureRegion);

	public abstract TextureRegion newTextureRegion(TextureRegion textureRegion, int width, int height);

	public abstract TextureRegion newTextureRegion(TextureRegion textureRegion, int x, int y, int width, int height);

	public abstract Sprite newSprite(Texture texture);

	public abstract Sprite newSprite(Texture texture, int width, int height);

	public abstract Sprite newSprite(Texture texture, int x, int y, int width, int height);

	public abstract Sprite newSprite(TextureRegion textureRegion);

	public abstract Sprite newSprite(TextureRegion textureRegion, int width, int height);

	public abstract Sprite newSprite(TextureRegion textureRegion, int x, int y, int width, int height);

	public abstract Sprite newSprite(Sprite sprite);

	/** Loads the specified pack file, using the parent directory of the pack file to find the page images. */
	public abstract TextureAtlas newTextureAtlas(FileHandle packFile);

	/** @param flip If true, all regions loaded will be flipped for use with a perspective where 0,0 is the upper left corner.*/
	public abstract TextureAtlas newTextureAtlas(FileHandle packFile, boolean flip);

	public abstract TextureAtlas newTextureAtlas(FileHandle packFile, FileHandle imagesDir);

	/** @param flip If true, all regions loaded will be flipped for use with a perspective where 0,0 is the upper left corner. */
	public abstract TextureAtlas newTextureAtlas(FileHandle packFile, FileHandle imagesDir, boolean flip);

	public abstract ParticleEffect newParticleEffect();

	public abstract ParticleEffect newParticleEffect(FileHandle effectFile, FileHandle imagesDir);

	public abstract ParticleEffect newParticleEffect(FileHandle effectFile, TextureAtlas atlas);

	public abstract NinePatch newNinePatch(Texture texture, int left, int right, int top, int bottom);

	public abstract NinePatch newNinePatch(TextureRegion region, int left, int right, int top, int bottom);

	public TilingDrawable newTilingDrawable(TextureRegion textureRegion){
		return new TilingDrawable(textureRegion);
	}

	/**
	 * Loads a shader at the specified path. Note: only provide the shader name, e.g. for the following files<br>
	 * <i>path/myshader.frag.glsl</i><br>
	 * <i>path/myshader.vert.glsl</i><br>
	 * <i>path/myshader.fx</i><br>
	 * you would pass in <i>path/myshader</i> as the path
	 * @param path The shader path
	 * @return The platform-specific implementation of {@link Shader}
	 */
	public abstract Shader newShader(String path);

	public abstract FrameBuffer newFrameBuffer(int width, int height);

	/**
	 * Creates a new custom mouse cursor. This class must be set as the {@link org.mini2Dx.gdx.InputProcessor} or added to a {@link org.mini2Dx.gdx.InputMultiplexer}.
	 * @param upPixmap The image to use in the mouse button up state
	 * @param downPixmap The image to use in the mouse button down state
	 * @param xHotspot The x location of the hotspot pixel within the cursor image (origin top-left corner)
	 * @param yHotspot The y location of the hotspot pixel within the cursor image (origin top-left corner)
	 */
	public abstract CustomCursor newCustomCursor(Pixmap upPixmap, Pixmap downPixmap, int xHotspot, int yHotspot);

	public abstract SpriteCache newSpriteCache();
}
