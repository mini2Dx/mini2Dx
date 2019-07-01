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
package org.mini2Dx.libgdx;

import com.badlogic.gdx.Gdx;
import org.mini2Dx.core.Files;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.libgdx.files.LibgdxFileHandle;

public class LibgdxFiles implements Files {
	@Override
	public FileHandle internal(String path) {
		return new LibgdxFileHandle(Gdx.files.internal(path));
	}

	@Override
	public FileHandle external(String path) {
		return new LibgdxFileHandle(Gdx.files.external(path));
	}

	@Override
	public FileHandle local(String path) {
		return new LibgdxFileHandle(Gdx.files.local(path));
	}

	@Override
	public boolean isExternalStorageAvailable() {
		return Gdx.files.isExternalStorageAvailable();
	}

	@Override
	public boolean isLocalStorageAvailable() {
		return Gdx.files.isLocalStorageAvailable();
	}
}
