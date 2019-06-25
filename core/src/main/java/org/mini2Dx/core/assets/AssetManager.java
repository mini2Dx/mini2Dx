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
package org.mini2Dx.core.assets;

import org.mini2Dx.gdx.utils.Disposable;

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

	public <T> T get(String filePath, Class<T> clazz) {
		return null;
	}

	@Override
	public void dispose() {

	}
}
