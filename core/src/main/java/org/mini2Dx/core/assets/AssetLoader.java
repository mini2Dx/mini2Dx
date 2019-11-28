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

import org.mini2Dx.gdx.utils.Array;

/**
 * Interface for implementing asset loading via {@link AssetManager}
 * @param <T> The resulting type that this loader will load
 */
public interface AssetLoader<T> {
	/**
	 * Loads an asset on the main game thread
	 * @param assetManager The {@link AssetManager} executing the loading
	 * @param assetDescriptor A descriptor of the asset to be loaded
	 * @param asyncLoadingCache Stores values loaded on other threads
	 * @param resultHolder Stores the resulting object
	 * @return True if loading is complete
	 */
	public boolean loadOnGameThread(AssetManager assetManager, AssetDescriptor<T> assetDescriptor, AsyncLoadingCache asyncLoadingCache, AssetLoaderResult<T> resultHolder);

	/**
	 * Returns an {@link Array} of assets that the requested asset depends on
	 * @param assetDescriptor A descriptor of the asset to be loaded
	 * @param asyncLoadingCache Stores values loaded on other threads
	 * @return An empty instance of {@link Array} or null if there's no dependencies
	 */
	public Array<AssetDescriptor> getDependencies(AssetDescriptor<T> assetDescriptor, AsyncLoadingCache asyncLoadingCache);
}
