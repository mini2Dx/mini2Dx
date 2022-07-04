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
package org.mini2Dx.tiled;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import org.junit.Assert;
import org.junit.Test;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.libgdx.LibgdxFiles;
import org.mini2Dx.libgdx.LibgdxGraphicsUtils;
import org.mini2Dx.libgdx.LibgdxPlatformUtils;
import org.mini2Dx.tiled.exception.TiledException;

/**
 * Unit tests for {@link TiledMap} instances sharing the same
 * {@link TiledParser}
 */
public class SharedParserTiledMapTest {

	@Test
	public void testSharedParser() throws TiledException {
		Gdx.files = new LwjglFiles();
		Mdx.files = new LibgdxFiles();
		Mdx.graphics = new LibgdxGraphicsUtils();
		Mdx.platformUtils = new LibgdxPlatformUtils() {
			@Override
			public boolean isGameThread() {
				return false;
			}

			@Override
			public void enablePerformanceMode() {
			}

			@Override
			public void cancelPerformanceMode() {
			}
		};

		TiledParser parser = new TiledParser();

		FileHandle orthogonalFile = Mdx.files.internal(Thread.currentThread().getContextClassLoader()
				.getResource("orthogonal.tmx").getFile().replaceAll("%20", " "));
		FileHandle orthogonalTsxFile = Mdx.files.internal(Thread.currentThread().getContextClassLoader()
				.getResource("orthogonal_tsx.tmx").getFile().replaceAll("%20", " "));
		FileHandle isometricFile = Mdx.files.internal(Thread.currentThread().getContextClassLoader()
				.getResource("isometric.tmx").getFile().replaceAll("%20", " "));

		TiledMap orthogonalTiledMap = new TiledMap(parser, orthogonalFile, false);
		TiledMap orthogonalTsxTiledMap = new TiledMap(parser, orthogonalTsxFile, false);
		TiledMap isometricTiledMap = new TiledMap(parser, isometricFile, false);

		Assert.assertEquals(Orientation.ORTHOGONAL, orthogonalTiledMap.getOrientation());
		Assert.assertEquals(Orientation.ISOMETRIC, isometricTiledMap.getOrientation());

		Assert.assertEquals(Orientation.ORTHOGONAL, orthogonalTsxTiledMap.getOrientation());
		Assert.assertEquals(1, orthogonalTsxTiledMap.getTilesets().size);
	}
}
