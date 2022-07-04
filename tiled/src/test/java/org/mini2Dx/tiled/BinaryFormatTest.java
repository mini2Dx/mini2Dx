/**
 * Copyright 2022 Viridian Software Ltd.
 */
package org.mini2Dx.tiled;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.serialization.GameDataOutputStream;
import org.mini2Dx.libgdx.LibgdxFiles;
import org.mini2Dx.libgdx.LibgdxGraphicsUtils;
import org.mini2Dx.libgdx.LibgdxPlatformUtils;
import org.mini2Dx.tiled.exception.TiledException;

import java.io.*;

public class BinaryFormatTest {

	@BeforeClass
	public static void loadMap() throws TiledException {
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
	}

	@Test
	public void testOrthogonalMap() throws IOException {
		FileHandle fileHandle = Mdx.files.internal(Thread.currentThread().getContextClassLoader()
				.getResource("orthogonal.tmx").getFile().replaceAll("%20", " "));

		final TiledMapData expected = new TiledMapData(fileHandle);
		final File tmpFile = File.createTempFile("orth", ".tmx");
		writeMap(expected, tmpFile);

		final TiledMapData result = readMap(tmpFile);
		if(expected.equals(result)) {
			Assert.assertEquals(expected, result);
			return;
		}

		Assert.assertEquals(expected.layers.size, result.layers.size);
		for(int i = 0; i < expected.layers.size; i++) {
			Assert.assertEquals(expected.layers.get(i), result.layers.get(i));
		}

		Assert.assertEquals(expected.tilesets.size, result.tilesets.size);
		for(int i = 0; i < expected.tilesets.size; i++) {
			Assert.assertEquals(expected.tilesets.get(i), result.tilesets.get(i));
		}
	}

	private TiledMapData readMap(File tmpFile) throws IOException {
		final DataInputStream inputStream = new DataInputStream(new FileInputStream(tmpFile));
		final TiledMapData result = TiledMapData.fromInputStream(inputStream);
		inputStream.close();
		return result;
	}

	private void writeMap(TiledMapData mapData, File tmpFile) throws IOException {
		final DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(tmpFile));
		mapData.writeData(outputStream);
		outputStream.flush();
		outputStream.close();
	}
}
