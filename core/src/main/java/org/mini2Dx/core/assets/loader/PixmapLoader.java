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
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.graphics.Pixmap;
import org.mini2Dx.gdx.utils.Array;

public class PixmapLoader implements AssetLoader<Pixmap> {

	@Override
	public Pixmap loadOnGameThread(AssetManager assetManager, AssetDescriptor assetDescriptor, AsyncLoadingCache asyncLoadingCache) {
		return Mdx.graphics.newPixmap(assetDescriptor.getResolvedFileHandle());
	}

	@Override
	public Array<AssetDescriptor> getDependencies(AssetDescriptor assetDescriptor, AsyncLoadingCache asyncLoadingCache) {
		return null;
	}
}
