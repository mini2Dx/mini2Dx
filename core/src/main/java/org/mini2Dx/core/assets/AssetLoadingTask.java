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

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.executor.AsyncFuture;
import org.mini2Dx.core.executor.FrameSpreadTask;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.Disposable;

import java.util.concurrent.atomic.AtomicBoolean;

public class AssetLoadingTask<T> implements Runnable, Comparable<AssetLoadingTask>, Disposable {
	private static final String LOGGING_TAG = AssetLoadingTask.class.getSimpleName();

	private final AssetLoader<T> assetLoader;
	private final AssetDescriptor<T> assetDescriptor;

	private Array<AssetDescriptor> dependencies;
	private boolean dependenciesRetrieved = false;
	private boolean dependenciesQueued = false;
	private boolean dependenciesLoaded = false;

	private boolean asyncCompleted;
	private AsyncFuture asyncFuture;
	private AsyncLoadingCache asyncLoadingCache;

	public AssetLoadingTask(AssetLoader<T> assetLoader, AssetDescriptor<T> assetDescriptor) {
		this.assetLoader = assetLoader;
		this.assetDescriptor = assetDescriptor;

		if(assetLoader instanceof AsyncAssetLoader) {
			asyncLoadingCache = new AsyncLoadingCache();
		} else {
			asyncCompleted = true;
		}
	}

	public boolean update(AssetManager assetManager) {
		if(!dependenciesRetrieved) {
			dependencies = assetLoader.getDependencies(assetDescriptor, asyncLoadingCache);
			dependenciesRetrieved = true;

			dependenciesQueued = dependencies == null;
			dependenciesLoaded = dependenciesQueued;
			return false;
		}

		if(!dependenciesQueued) {
			for(int i = 0; i < dependencies.size; i++) {
				assetManager.load(dependencies.get(i));
			}
			dependenciesQueued = true;
			return false;
		}

		if(!dependenciesLoaded) {
			for(int i = 0; i < dependencies.size; i++) {
				if(!assetManager.isLoaded(dependencies.get(i).getFilePath())) {
					return false;
				}
			}
			dependenciesLoaded = true;
		}

		if(!asyncCompleted) {
			if(asyncFuture == null) {
				asyncFuture = Mdx.executor.submit((Runnable) this);
			}
			asyncCompleted = asyncFuture.isFinished();
			return false;
		}

		final T result = assetLoader.loadOnGameThread(assetManager, assetDescriptor, asyncLoadingCache);
		assetManager.getAssets().put(assetDescriptor.getFilePath(), new ReferenceCountedObject(result));
		return true;
	}

	@Override
	public void run() {
		try {
			AsyncAssetLoader<T> asyncAssetLoader = (AsyncAssetLoader) assetLoader;
			asyncAssetLoader.loadOnAsyncThread(assetDescriptor, asyncLoadingCache);
		} catch (Exception e) {
			Mdx.log.error(LOGGING_TAG, e.getMessage(), e);
		}
	}

	@Override
	public int compareTo(AssetLoadingTask o) {
		return Integer.compare(o.getTotalDependencies(), getTotalDependencies());
	}

	public int getTotalDependencies() {
		if(!dependenciesRetrieved) {
			return -1;
		}
		if(dependencies == null) {
			return 0;
		}
		return dependencies.size;
	}

	@Override
	public void dispose() {
		if(asyncLoadingCache != null) {
			asyncLoadingCache.clearCache();
			asyncLoadingCache = null;
		}
		if(dependencies != null) {
			dependencies.clear();
			dependencies = null;
		}
		asyncFuture = null;

	}
}
