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
	public boolean loadOnGameThread(AssetManager assetManager, AssetDescriptor<TiledMap> assetDescriptor, AsyncLoadingCache asyncLoadingCache, AssetLoaderResult<TiledMap> resultHolder) {
		final TiledMap result = asyncLoadingCache.getCache(CACHE_TILED_MAP, TiledMap.class);
		final TiledAssetProperties tiledAssetProperties = (TiledAssetProperties) assetDescriptor.getParameters();
		if(tiledAssetProperties != null) {
			if(tiledAssetProperties.loadTilesets) {
				if(result.loadTilesetTextures(assetManager)) {
					resultHolder.setResult(result);
					return true;
				}
				return false;
			}
		} else {
			if(result.loadTilesetTextures(assetManager)) {
				resultHolder.setResult(result);
				return true;
			}
			return false;
		}
		resultHolder.setResult(result);
		return true;
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

	public static class TiledAssetProperties implements AssetProperties<TiledMap> {
		public boolean loadTilesets = true;
	}
}
