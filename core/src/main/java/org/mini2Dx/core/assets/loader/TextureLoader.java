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
package org.mini2Dx.core.assets.loader;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.*;
import org.mini2Dx.core.graphics.Texture;
import org.mini2Dx.gdx.utils.Array;

import java.io.IOException;

public class TextureLoader implements AsyncAssetLoader<Texture> {
	private static final String LOGGING_TAG = TextureLoader.class.getSimpleName();
	private static final String CACHE_TEXTURE_DATA_KEY = "textureData";

	@Override
	public boolean loadOnGameThread(AssetManager assetManager, AssetDescriptor<Texture> assetDescriptor,
	                                AsyncLoadingCache asyncLoadingCache, AssetLoaderResult<Texture> resultHolder) {
		resultHolder.setResult(Mdx.graphics.newTexture(asyncLoadingCache.getCache(CACHE_TEXTURE_DATA_KEY, byte[].class)));
		return true;
	}

	@Override
	public void loadOnAsyncThread(AssetDescriptor assetDescriptor, AsyncLoadingCache asyncLoadingCache) {
		try {
			asyncLoadingCache.setCache(CACHE_TEXTURE_DATA_KEY, assetDescriptor.getResolvedFileHandle().readBytes());
		} catch (IOException e) {
			Mdx.log.error(LOGGING_TAG, e.getMessage(), e);
		} catch (Exception e) {
			Mdx.log.error(LOGGING_TAG, e.getMessage(), e);
		}
	}

	@Override
	public Array<AssetDescriptor> getDependencies(AssetDescriptor assetDescriptor, AsyncLoadingCache asyncLoadingCache) {
		return null;
	}
}
