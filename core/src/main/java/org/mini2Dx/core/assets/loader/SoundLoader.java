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
import org.mini2Dx.core.audio.Sound;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.gdx.utils.Array;

import java.io.IOException;

public class SoundLoader implements AsyncAssetLoader<Sound> {
	private static final String LOGGING_TAG = SoundLoader.class.getSimpleName();
	private static final String CACHE_SOUND_KEY = "sound";

	@Override
	public Sound loadOnGameThread(AssetManager assetManager, AssetDescriptor assetDescriptor, AsyncLoadingCache asyncLoadingCache) {
		return asyncLoadingCache.getCache(CACHE_SOUND_KEY, Sound.class);
	}

	@Override
	public void loadOnAsyncThread(AssetDescriptor assetDescriptor, AsyncLoadingCache asyncLoadingCache) {
		try {
			asyncLoadingCache.setCache(CACHE_SOUND_KEY, Mdx.audio.newSound(assetDescriptor.getResolvedFileHandle()));
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
