/*******************************************************************************
 * Copyright 2019 Viridian Software Limited
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
package org.mini2Dx.core.assets;

import org.mini2Dx.core.assets.loader.*;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.gdx.utils.Disposable;
import org.mini2Dx.gdx.utils.ObjectMap;
import org.mini2Dx.gdx.utils.Queue;

/**
 * <p>
 * Loads and stores game assets such as textures, sounds, etc.
 * </p>
 *
 * <p>
 * The benefit of using the AssetManager vs. fetching assets manually is dependency management, reference counting and caching.
 * </p>
 *
 * <p>
 * Some assets will be wrapped in a wrapper class to track references to the asset. Calling dispose() on such assets decreases the reference count.
 * Once references to an asset are at zero, it will be disposed of by the AssetManager.
 * </p>
 */
public class AssetManager implements Disposable {
	private final FileHandleResolver fileHandleResolver;

	private final ObjectMap<String, AssetLoader> assetLoadersByFileSuffix = new ObjectMap<String, AssetLoader>();
	private final ObjectMap<String, AssetLoader> assetLoadersByDirectoryPattern = new ObjectMap<String, AssetLoader>();

	private final Queue<AssetDescriptor> loadingQueue = new Queue<AssetDescriptor>();

	public AssetManager(FileHandleResolver fileHandleResolver) {
		this(fileHandleResolver, true);
	}

	public AssetManager(FileHandleResolver fileHandleResolver, boolean initDefaultLoaders) {
		this.fileHandleResolver = fileHandleResolver;

		if(initDefaultLoaders) {
			final BitmapGameFontLoader bitmapGameFontLoader = new BitmapGameFontLoader();
			final MonospaceGameFontLoader monospaceGameFontLoader = new MonospaceGameFontLoader();
			final MusicLoader musicLoader = new MusicLoader();
			final ShaderLoader shaderLoader = new ShaderLoader();
			final SoundLoader soundLoader = new SoundLoader();
			final TextureLoader textureLoader = new TextureLoader();
			final TextureAtlasLoader textureAtlasLoader = new TextureAtlasLoader();

			assetLoadersByFileSuffix.put(".jpg", textureLoader);
			assetLoadersByFileSuffix.put(".png", textureLoader);
			assetLoadersByFileSuffix.put(".atlas", textureAtlasLoader);

			assetLoadersByDirectoryPattern.put("mfonts/", monospaceGameFontLoader);
			assetLoadersByDirectoryPattern.put("mfont/", monospaceGameFontLoader);

			assetLoadersByDirectoryPattern.put("bfonts/", bitmapGameFontLoader);
			assetLoadersByDirectoryPattern.put("bfont/", bitmapGameFontLoader);

			assetLoadersByDirectoryPattern.put("music/", musicLoader);
			assetLoadersByDirectoryPattern.put("musics/", musicLoader);
			assetLoadersByDirectoryPattern.put("tracks/", musicLoader);

			assetLoadersByDirectoryPattern.put("shaders/", shaderLoader);
			assetLoadersByDirectoryPattern.put("shader/", shaderLoader);

			assetLoadersByDirectoryPattern.put("sfx/", soundLoader);
			assetLoadersByDirectoryPattern.put("sound/", soundLoader);
			assetLoadersByDirectoryPattern.put("sounds/", soundLoader);
		}
	}

	public <T> T get(String filePath, Class<T> clazz) {
		return null;
	}

	public void load(String filePath) {
		load(new AssetDescriptor(filePath));
	}

	public void load(String filePath, AssetParameters assetParameters) {
		load(new AssetDescriptor(filePath, assetParameters));
	}

	public void load(AssetDescriptor assetDescriptor) {

	}

	public void unload(String filePath) {

	}

	public boolean update() {
		return false;
	}

	@Override
	public void dispose() {

	}
}
