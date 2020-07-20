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

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.loader.*;
import org.mini2Dx.core.audio.Music;
import org.mini2Dx.core.audio.Sound;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.graphics.Pixmap;
import org.mini2Dx.core.graphics.Shader;
import org.mini2Dx.core.graphics.Texture;
import org.mini2Dx.core.graphics.TextureAtlas;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.Disposable;
import org.mini2Dx.gdx.utils.ObjectMap;

import java.util.concurrent.TimeUnit;

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
	private static final String LOGGING_TAG = AssetManager.class.getSimpleName();
	/**
	 * The time limit for loading operations per frame. Defaults to 4ms (one eight of a frame @ 30FPS)
	 */
	public static long UPDATE_TIMEBOX_MILLIS = 4;

	private final FileHandleResolver fileHandleResolver;

	private final ObjectMap<Class, AssetLoader> assetLoaders = new ObjectMap<Class, AssetLoader>();
	private final ObjectMap<String, ReferenceCountedObject> assets = new ObjectMap<String, ReferenceCountedObject>();
	private final ObjectMap<String, AssetDescriptor> assetDescriptors = new ObjectMap<String, AssetDescriptor>();

	private final Array<AssetDescriptor> loadingQueue = new Array<AssetDescriptor>(false, 32);
	private final Array<AssetLoadingTask> loadingTasks = new Array<AssetLoadingTask>(false, 32);

	private float queuedAssets = 0f;
	private float completedTasks = 0f;
	private boolean loadingTasksDirty = false;

	public AssetManager(FileHandleResolver fileHandleResolver) {
		this(fileHandleResolver, true);
	}

	public AssetManager(FileHandleResolver fileHandleResolver, boolean initDefaultLoaders) {
		this.fileHandleResolver = fileHandleResolver;

		if(initDefaultLoaders) {
			assetLoaders.put(Music.class, new MusicLoader());
			assetLoaders.put(Pixmap.class, new PixmapLoader());
			assetLoaders.put(Shader.class, new ShaderLoader());
			assetLoaders.put(Sound.class, new SoundLoader());
			assetLoaders.put(Texture.class, new TextureLoader());
			assetLoaders.put(TextureAtlas.class, new TextureAtlasLoader());
		}
	}

	public <T> T get(String filePath, Class<T> clazz) {
		if(!assets.containsKey(filePath)) {
			throw new MdxException(filePath + " not yet loaded");
		}
 		return assets.get(filePath).getObject(clazz);
	}

	public <T> ObjectMap<String, T> getAll(Class<T> clazz) {
		final ObjectMap<String, T> assetsOfType = new ObjectMap<>();

		assetDescriptors.entries().forEach(assetDescriptorEntry -> {
			if(assetDescriptorEntry.value.getClazz().equals(clazz)) {
				assetsOfType.put(assetDescriptorEntry.key, assets.get(assetDescriptorEntry.key).getObject(clazz));
			}
		});

		return assetsOfType;
	}

	public boolean isLoaded(String filePath) {
		return assets.containsKey(filePath);
	}

	public <T> void load(String filePath, Class<T> clazz) {
		load(new AssetDescriptor(filePath, clazz));
	}

	public <T> void load(String filePath, Class<T> clazz, AssetProperties assetProperties) {
		load(new AssetDescriptor(filePath, clazz, assetProperties));
	}

	public void load(AssetDescriptor assetDescriptor) {
		if(assets.containsKey(assetDescriptor.getFilePath())) {
			return;
		}
		if(!assetLoaders.containsKey(assetDescriptor.getClazz())) {
			throw new MdxException("No asset loader configured for " + assetDescriptor.getClazz().getName());
		}

		for(int i = 0; i < loadingQueue.size; i++) {
			final AssetDescriptor queuedDescriptor = loadingQueue.get(i);
			if(!queuedDescriptor.getFilePath().equals(assetDescriptor.getFilePath())) {
				continue;
			}
			if(!assetDescriptor.getClazz().equals(queuedDescriptor.getClazz())) {
				throw new MdxException(assetDescriptor.getFilePath() + " already queued but with a different class type (queued: " +
						queuedDescriptor.getClazz().getName() + ", attempting: " + assetDescriptor.getClazz().getName() + ")");
			}
			Mdx.log.debug(LOGGING_TAG, assetDescriptor.getFilePath() + " is already queued for loading");
			return;
		}

		loadingQueue.add(assetDescriptor);
		queuedAssets++;
	}

	public void unload(String filePath) {
		assets.remove(filePath);
		assetDescriptors.remove(filePath);
	}

	public boolean update() {
		final long startTime = System.nanoTime();

		while(loadingQueue.size > 0) {
			final AssetDescriptor assetDescriptor = loadingQueue.removeIndex(0);
			assetDescriptor.setResolvedFileHandle(fileHandleResolver.resolve(assetDescriptor.getFilePath()));
			final AssetLoader assetLoader = assetLoaders.get(assetDescriptor.getClazz());
			loadingTasks.add(new AssetLoadingTask(assetLoader, assetDescriptor));
			loadingTasksDirty = true;
		}
		if(System.nanoTime() - startTime >= TimeUnit.MILLISECONDS.toNanos(UPDATE_TIMEBOX_MILLIS)) {
			return false;
		}

		//Sort loading tasks so that tasks with less dependencies (more likely to be depended on) are processed first
		if(loadingTasksDirty) {
			loadingTasks.sort();
			loadingTasksDirty = false;
		}

		if(System.nanoTime() - startTime >= TimeUnit.MILLISECONDS.toNanos(UPDATE_TIMEBOX_MILLIS)) {
			return false;
		}

		for(int i = loadingTasks.size - 1; i >= 0; i--) {
			if(loadingTasks.get(i).update(this)) {
				loadingTasks.removeIndex(i);
				completedTasks++;
				loadingTasksDirty = true;
			}
			if(System.nanoTime() - startTime >= TimeUnit.MILLISECONDS.toNanos(UPDATE_TIMEBOX_MILLIS)) {
				return false;
			}
		}
		return loadingTasks.size == 0 && loadingQueue.size == 0;
	}

	public void finishLoading() {
		while(!update()) {

		}
	}

	/**
	 * Sets the {@link AssetLoader} to use for a specific class
	 * @param clazz The class to use the loader for
	 * @param assetLoader The {@link AssetLoader}
	 * @param <T> The class type
	 */
	public <T> void setAssetLoader(Class<T> clazz, AssetLoader<T> assetLoader) {
		assetLoaders.put(clazz, assetLoader);
	}

	/**
	 * Clears all {@link AssetLoader}s so that new ones can be set
	 */
	public void clearAssetLoaders() {
		assetLoaders.clear();
	}

	@Override
	public void dispose() {
		loadingQueue.clear();
	}

	/**
	 * Returns the loading progress
	 * @return A value between 0.0 and 1.0
	 */
	public float getProgress() {
		return completedTasks / queuedAssets;
	}

	/**
	 * Returns the number of all assets (both loaded and not loaded) ever queued
	 * @return A value more than or equal zero
	 */
	public float getQueuedAssets() {
		return queuedAssets;
	}

	/**
	 * Returns the number of already loaded assets
	 * @return A value more than or equal zero
	 */
	public float getCompletedTasks() {
		return completedTasks;
	}

	/**
	 * Returns the number of currently queued assets
	 * @return A value more than or equal zero
	 */
	public float getUnfinishedTasks() {
		return queuedAssets - completedTasks;
	}

	public FileHandleResolver getFileHandleResolver() {
		return fileHandleResolver;
	}

	ObjectMap<String, ReferenceCountedObject> getAssets() {
		return assets;
	}

	ObjectMap<String, AssetDescriptor> getAssetDescriptors() {
		return assetDescriptors;
	}
}
