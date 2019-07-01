/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.ui;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetDescriptor;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.assets.AsyncAssetLoader;
import org.mini2Dx.core.assets.AsyncLoadingCache;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.exception.SerializationException;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.ui.style.UiTheme;

/**
 * A {@link AsyncAssetLoader} for loading {@link UiTheme}s
 */
public class UiThemeLoader implements AsyncAssetLoader<UiTheme> {
	private static final String LOGGING_TAG = UiThemeLoader.class.getSimpleName();

	private static final String CACHE_THEME_KEY = "theme";

	private final FileHandleResolver fileHandleResolver;
	private final boolean headless;
	
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
	public UiTheme loadOnGameThread(AssetManager assetManager, AssetDescriptor assetDescriptor, AsyncLoadingCache asyncLoadingCache) {
		final UiTheme theme = asyncLoadingCache.getCache(CACHE_THEME_KEY, UiTheme.class);
		theme.prepareAssets(fileHandleResolver, assetManager);
		return theme;
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
