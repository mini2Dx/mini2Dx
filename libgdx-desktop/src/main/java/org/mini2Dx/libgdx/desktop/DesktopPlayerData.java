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
package org.mini2Dx.libgdx.desktop;

import com.badlogic.gdx.Gdx;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.PlayerData;
import org.mini2Dx.core.exception.PlayerDataException;
import org.mini2Dx.core.files.FileHandle;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

public class DesktopPlayerData extends PlayerData {
	private final String saveDirectory;

	public DesktopPlayerData(String gameIdentifier) {
		super();
		saveDirectory = getSaveDirectoryForGame(gameIdentifier);
	}

	@Override
	protected FileHandle resolve(String[] filepath) {
		return Mdx.files.external(Paths.get(saveDirectory, filepath).toString());
	}

	@Override
	protected FileHandle resolveTmp(String[] filepath) {
		final String [] tmpFilepath = Arrays.copyOf(filepath, filepath.length);
		tmpFilepath[tmpFilepath.length - 1] = tmpFilepath[tmpFilepath.length - 1] + ".tmp";
		return Mdx.files.external(Paths.get(saveDirectory, tmpFilepath).toString());
	}

	@Override
	protected void ensureDataDirectoryExists() throws IOException {
		FileHandle directory = Mdx.files.external(saveDirectory);
		if (directory.exists()) {
			return;
		}
		directory.mkdirs();
	}

	@Override
	public void wipe() throws PlayerDataException {
		FileHandle directory = Mdx.files.external(saveDirectory);
		if (!directory.exists()) {
			return;
		}
		try {
			directory.emptyDirectory();
			directory.deleteDirectory();
		} catch (IOException e) {
			throw new PlayerDataException(e);
		}
	}

	public String getSaveDirectoryForGame(String gameIdentifier) {
		switch (Mdx.platform) {
		case WINDOWS:
			return Paths
					.get(Gdx.files.getExternalStoragePath(), "AppData",
							"Roaming", gameIdentifier).toAbsolutePath()
					.toString();
		case MAC:
			return Paths
					.get(Gdx.files.getExternalStoragePath(), "Library",
							"Application Support", gameIdentifier)
					.toAbsolutePath().toString();
		case LINUX:
			if (gameIdentifier.startsWith(".")) {
				gameIdentifier = gameIdentifier.substring(gameIdentifier
						.indexOf('.') + 1);
			}
			return Paths
					.get(Gdx.files.getExternalStoragePath(),
							"." + gameIdentifier).toAbsolutePath().toString();
		default:
			return Paths.get(Gdx.files.getLocalStoragePath()).toAbsolutePath()
					.toString();
		}
	}
}
