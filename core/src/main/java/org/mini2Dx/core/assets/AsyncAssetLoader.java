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

/**
 * Interface for implement {@link AssetLoader}s that can load (or partially load) asset data via non-main threads
 * @param <T> The resulting type that this loader will load
 */
public interface AsyncAssetLoader<T> extends AssetLoader<T> {

	/**
	 * Loads an asset (or partial data) via a non-main thread
	 * @param assetDescriptor A descriptor of the asset to be loaded
	 * @param asyncLoadingCache Stores data in this cache to pass to the game thread loading
	 */
	public void loadOnAsyncThread(AssetDescriptor assetDescriptor, AsyncLoadingCache asyncLoadingCache);
}
