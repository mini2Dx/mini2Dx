/**
 * Copyright (c) 2017 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.tiled;

import org.mini2Dx.core.assets.*;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.gdx.utils.Array;

/**
 * An {@link AssetLoader} implementation for loading {@link TiledMap} instances
 */
public class TiledMapLoader implements AsyncAssetLoader<TiledMap> {
	private static final String LOGGING_TAG = TiledMapLoader.class.getSimpleName();

	private static final String CACHE_TILED_MAP_DATA = "tiledMapData";
	private static final String CACHE_TILED_MAP = "tiledMap";

	private final TiledParser tiledParser = new TiledParser();


	@Override
	public TiledMap loadOnGameThread(AssetManager assetManager, AssetDescriptor assetDescriptor, AsyncLoadingCache asyncLoadingCache) {
		final TiledMap result = asyncLoadingCache.getCache(CACHE_TILED_MAP, TiledMap.class);
		final TiledAssetProperties tiledAssetProperties = (TiledAssetProperties) assetDescriptor.getParameters();
		if(tiledAssetProperties != null && tiledAssetProperties.loadTilesets) {
			result.loadTilesetTextures(assetManager);
		}
		return result;
	}

	@Override
	public Array<AssetDescriptor> getDependencies(AssetDescriptor assetDescriptor, AsyncLoadingCache asyncLoadingCache) {
		final TiledMapData tiledMapData;
		if(!asyncLoadingCache.containsCache(CACHE_TILED_MAP_DATA)) {
			tiledMapData = new TiledMapData(tiledParser, assetDescriptor.getResolvedFileHandle());
			asyncLoadingCache.setCache(CACHE_TILED_MAP_DATA, tiledMapData);
		} else {
			tiledMapData = asyncLoadingCache.getCache(CACHE_TILED_MAP_DATA, TiledMapData.class);
		}
		return tiledMapData.getDependencies();
	}

	@Override
	public void loadOnAsyncThread(AssetDescriptor assetDescriptor, AsyncLoadingCache asyncLoadingCache) {
		if(asyncLoadingCache.containsCache(CACHE_TILED_MAP)) {
			return;
		}
		asyncLoadingCache.setCache(CACHE_TILED_MAP, new TiledMap(
				asyncLoadingCache.getCache(CACHE_TILED_MAP_DATA, TiledMapData.class), false));
	}

	static class TiledAssetProperties implements AssetProperties<TiledMap> {
		boolean loadTilesets = true;
	}
}
