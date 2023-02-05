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
package org.mini2Dx.ui;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.*;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.exception.SerializationException;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.core.graphics.Texture;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.ui.style.UiTheme;

/**
 * A {@link AsyncAssetLoader} for loading {@link UiTheme}s
 */
public class UiThemeLoader implements AsyncAssetLoader<UiTheme> {
	private static final String LOGGING_TAG = UiThemeLoader.class.getSimpleName();

	protected static final String CACHE_THEME_KEY = "theme";

	protected final FileHandleResolver fileHandleResolver;
	protected final boolean headless;
	
	/**
	 * Constructor
	 * @param resolver The {@link FileHandleResolver} to use
	 */
	public UiThemeLoader(FileHandleResolver resolver) {
		this(resolver, false);
	}
	
	/**
	 * Constructor
	 * @param resolver The {@link FileHandleResolver} to use
	 * @param headless True if the game is using the headless runtime
	 */
	public UiThemeLoader(FileHandleResolver resolver, boolean headless) {
		super();
		this.fileHandleResolver = resolver;
		this.headless = headless;
	}

	@Override
	public void loadOnAsyncThread(AssetDescriptor assetDescriptor, AsyncLoadingCache asyncLoadingCache) {
		final UiTheme theme = asyncLoadingCache.getCache(CACHE_THEME_KEY, UiTheme.class);
		theme.validate();
	}

	@Override
	public boolean loadOnGameThread(AssetManager assetManager, AssetDescriptor<UiTheme> assetDescriptor,
	                                AsyncLoadingCache asyncLoadingCache, AssetLoaderResult<UiTheme> resultHolder) {
		final UiTheme theme = asyncLoadingCache.getCache(CACHE_THEME_KEY, UiTheme.class);
		theme.prepareAssets(fileHandleResolver, assetManager);
		resultHolder.setResult(theme);
		return true;
	}

	@Override
	public Array<AssetDescriptor> getDependencies(AssetDescriptor assetDescriptor, AsyncLoadingCache asyncLoadingCache) {
		Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>();

		if(!asyncLoadingCache.containsCache(CACHE_THEME_KEY)) {
			try {
				asyncLoadingCache.setCache(CACHE_THEME_KEY, Mdx.json.fromJson(assetDescriptor.getResolvedFileHandle(), UiTheme.class));
			} catch (SerializationException e) {
				throw new MdxException(e.getMessage(), e);
			}
		}
		final UiTheme theme = asyncLoadingCache.getCache(CACHE_THEME_KEY, UiTheme.class);
		theme.loadDependencies(dependencies, fileHandleResolver, headless);
		return dependencies;
	}
}
