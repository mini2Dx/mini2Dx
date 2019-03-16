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
package org.mini2Dx.core;

import org.mini2Dx.core.files.FileHandle;

public interface Files {
	/**
	 * Convenience method that returns a {@link org.mini2Dx.core.files.FileType#INTERNAL} file handle.
	 */
	public FileHandle internal(String path);

	/**
	 * Convenience method that returns a {@link org.mini2Dx.core.files.FileType#EXTERNAL} file handle.
	 */
	public FileHandle external(String path);

	/**
	 * Convenience method that returns a {@link org.mini2Dx.core.files.FileType#LOCAL} file handle.
	 */
	public FileHandle local(String path);

	/**
	 * Returns true if the external storage is ready for file IO. Eg, on Android, the SD card is not available when mounted for use
	 * with a PC.
	 */
	public boolean isExternalStorageAvailable();

	/**
	 * Returns true if the local storage is ready for file IO.
	 */
	public boolean isLocalStorageAvailable();
}
