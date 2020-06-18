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
package org.mini2Dx.ui;

import org.mini2Dx.core.assets.*;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.files.FileHandleResolver;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.ui.xml.UiXmlLoader;

/**
 * A {@link AsyncAssetLoader} for loading {@link UiModel} instances
 */
public class UiModelLoader implements AsyncAssetLoader<UiModel> {
    private static final String CACHE_MODEL_DATA_KEY = "modelData";
    private final FileHandleResolver fileHandleResolver;

    /**
     * Constructor
     * @param fileHandleResolver The {@link FileHandleResolver} to use
     */
    public UiModelLoader(final FileHandleResolver fileHandleResolver) {
        this.fileHandleResolver = fileHandleResolver;
    }

    @Override
    public boolean loadOnGameThread(final AssetManager assetManager, final AssetDescriptor<UiModel> assetDescriptor,
                                    final AsyncLoadingCache asyncLoadingCache, final AssetLoaderResult<UiModel> resultHolder) {
        final String filename = asyncLoadingCache.getCache(CACHE_MODEL_DATA_KEY, String.class);
        final UiModelAssetProperties uiModelAssetProperties = (UiModelAssetProperties) assetDescriptor.getParameters();
        resultHolder.setResult(new UiXmlLoader(fileHandleResolver).load(filename, uiModelAssetProperties.getModel()));
        return true;
    }

    @Override
    public void loadOnAsyncThread(final AssetDescriptor assetDescriptor, final AsyncLoadingCache asyncLoadingCache) {
        try {
            asyncLoadingCache.setCache(CACHE_MODEL_DATA_KEY, assetDescriptor.getFilePath());
        } catch (final Exception e) {
            throw new MdxException(e.getMessage(), e);
        }
    }

    @Override
    public Array<AssetDescriptor> getDependencies(final AssetDescriptor assetDescriptor, final AsyncLoadingCache asyncLoadingCache) {
        return null;
    }

    /**
     * the UiModelAssetProperty holds the UiModel instance to be populated
     */
    public static class UiModelAssetProperties implements AssetProperties<UiModel> {
        private final UiModel model;

        public UiModelAssetProperties(final UiModel model) {
            this.model = model;
        }

        public UiModel getModel() {
            return model;
        }
    }
}
