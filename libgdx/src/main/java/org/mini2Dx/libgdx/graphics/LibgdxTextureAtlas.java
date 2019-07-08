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

import com.badlogic.gdx.graphics.g2d.LibgdxTextureAtlasWrapper;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.graphics.TextureAtlas;
import org.mini2Dx.core.graphics.TextureAtlasRegion;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.libgdx.files.LibgdxFileHandle;

public class LibgdxTextureAtlas implements TextureAtlas {
	public LibgdxTextureAtlasWrapper textureAtlas;
	public Array<TextureAtlasRegion> regions;

	public LibgdxTextureAtlas(FileHandle packFile, FileHandle imagesDir, boolean flip) {
		final LibgdxFileHandle gdxPackFile = (LibgdxFileHandle) packFile;
		final LibgdxFileHandle gdxImagesDir = (LibgdxFileHandle) imagesDir;
		this.textureAtlas = new LibgdxTextureAtlasWrapper(gdxPackFile.fileHandle, gdxImagesDir.fileHandle, flip);
	}

	@Override
	public Array<TextureAtlasRegion> getRegions() {
		if(regions == null) {
			final com.badlogic.gdx.utils.Array<LibgdxTextureAtlasWrapper.AtlasRegion> atlasRegions = textureAtlas.getRegions();
			regions = new Array<TextureAtlasRegion>(true, atlasRegions.size + 1);
			for(int i = 0; i < atlasRegions.size; i++) {
				regions.add(new LibgdxTextureAtlasRegion(atlasRegions.get(i)));
			}
		}
		return regions;
	}

	@Override
	public TextureAtlasRegion findRegion(String name) {
		if(regions == null) {
			getRegions();
		}
		for(int i = 0; i < regions.size; i++) {
			if(regions.get(i).getName().equals(name)) {
				return regions.get(i);
			}
		}
		return null;
	}

	@Override
	public TextureAtlasRegion findRegion(String name, int index) {
		if(regions == null) {
			getRegions();
		}
		for(int i = 0; i < regions.size; i++) {
			final TextureAtlasRegion textureAtlasRegion = regions.get(i);
			if(!textureAtlasRegion.getName().equals(name)) {
				continue;
			}
			if(textureAtlasRegion.getIndex() != index) {
				continue;
			}
			return textureAtlasRegion;
		}
		return null;
	}

	@Override
	public Array<TextureAtlasRegion> findRegions(String name) {
		if(regions == null) {
			getRegions();
		}

		final Array<TextureAtlasRegion> result = new Array<TextureAtlasRegion>();
		for(int i = 0; i < regions.size; i++) {
			if(regions.get(i).getName().equals(name)) {
				result.add(regions.get(i));
			}
		}
		return result;
	}

	@Override
	public void dispose() {
		if(regions != null) {
			regions.clear();
			regions = null;
		}
		textureAtlas.dispose();
	}
}
