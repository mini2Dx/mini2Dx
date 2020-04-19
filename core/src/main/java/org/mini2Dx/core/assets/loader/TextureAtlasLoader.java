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

import org.mini2Dx.core.assets.*;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.graphics.Texture;
import org.mini2Dx.core.graphics.TextureAtlas;
import org.mini2Dx.core.graphics.TextureAtlasConfig;
import org.mini2Dx.gdx.utils.Array;

public class TextureAtlasLoader implements AsyncAssetLoader<TextureAtlas> {


	@Override
	public boolean loadOnGameThread(AssetManager assetManager, AssetDescriptor<TextureAtlas> assetDescriptor,
	                                AsyncLoadingCache asyncLoadingCache, AssetLoaderResult<TextureAtlas> resultHolder) {
		if(resultHolder.getResult() == null) {
			FileHandle resolvedFileHandle = assetDescriptor.getResolvedFileHandle();
			TextureAtlasConfig atlasConfig = asyncLoadingCache.getCache(resolvedFileHandle.path(), TextureAtlasConfig.class);
			for (String path : atlasConfig.textures.keySet()) {
				atlasConfig.textures.replace(path, assetManager.get(path, Texture.class));
			}
			resultHolder.setResult(new TextureAtlas(atlasConfig));
		}
		return resultHolder.getResult().loadAtlasRegionTextures();
	}

	@Override
	public Array<AssetDescriptor> getDependencies(AssetDescriptor assetDescriptor, AsyncLoadingCache asyncLoadingCache) {
		FileHandle resolvedFileHandle = assetDescriptor.getResolvedFileHandle();
		TextureAtlasConfig atlasConfig = new TextureAtlasConfig(resolvedFileHandle);
		asyncLoadingCache.setCache(resolvedFileHandle.path(), atlasConfig);
		String[] dependenciesPaths = atlasConfig.getDependencies();
		Array<AssetDescriptor> descriptors = new Array<>(dependenciesPaths.length);
		for (String dependencyPath : dependenciesPaths) {
			descriptors.add(new AssetDescriptor<>(dependencyPath, Texture.class));
		}
		return descriptors;
	}

	@Override
	public void loadOnAsyncThread(AssetDescriptor assetDescriptor, AsyncLoadingCache asyncLoadingCache) {

	}
}
