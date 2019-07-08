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
package org.mini2Dx.libgdx.graphics;

import com.badlogic.gdx.graphics.LibgdxTextureRegionWrapper;
import com.badlogic.gdx.graphics.g2d.LibgdxTextureAtlasWrapper;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import org.mini2Dx.core.graphics.TextureAtlasRegion;

public class LibgdxTextureAtlasRegion extends LibgdxTextureRegion implements TextureAtlasRegion {
	public final LibgdxTextureAtlasWrapper.AtlasRegion atlasRegion;

	public LibgdxTextureAtlasRegion(LibgdxTextureAtlasWrapper.AtlasRegion atlasRegion) {
		super(new LibgdxTextureRegionWrapper(atlasRegion));
		this.atlasRegion = atlasRegion;
	}

	@Override
	public String getName() {
		return atlasRegion.name;
	}

	@Override
	public int getIndex() {
		return atlasRegion.index;
	}

	@Override
	public float getPackedWidth() {
		return atlasRegion.packedWidth;
	}

	@Override
	public float getPackedHeight() {
		return atlasRegion.packedHeight;
	}

	@Override
	public float getOriginalWidth() {
		return atlasRegion.originalWidth;
	}

	@Override
	public float getOriginalHeight() {
		return atlasRegion.originalHeight;
	}

	@Override
	public float getOffsetX() {
		return atlasRegion.offsetX;
	}

	@Override
	public float getOffsetY() {
		return atlasRegion.offsetY;
	}

	@Override
	public float getRotatedPackedWidth() {
		return atlasRegion.getRotatedPackedWidth();
	}

	@Override
	public float getRotatedPackedHeight() {
		return atlasRegion.getRotatedPackedHeight();
	}
}
