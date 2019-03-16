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

import org.mini2Dx.core.audio.Music;
import org.mini2Dx.core.audio.Sound;
import org.mini2Dx.core.files.FileHandle;

import java.io.IOException;

public interface Audio {

	/** <p>
	 * Creates a new {@link Sound} which is used to play back audio effects such as gun shots or explosions. The Sound's audio data
	 * is retrieved from the file specified via the {@link FileHandle}. Note that the complete audio data is loaded into RAM. You
	 * should therefore not load big audio files with this methods. The current upper limit for decoded audio is 1 MB.
	 * </p>
	 *
	 * <p>
	 * Currently supported formats are WAV, MP3 and OGG.
	 * </p>
	 *
	 * <p>
	 * The Sound has to be disposed if it is no longer used via the {@link Sound#dispose()} method.
	 * </p>
	 *
	 * @return the new Sound
	 * @throws IOException in case the sound could not be loaded */
	public Sound newSound (FileHandle fileHandle) throws IOException;

	/** Creates a new {@link Music} instance which is used to play back a music stream from a file. Currently supported formats are
	 * WAV, MP3 and OGG. The Music instance has to be disposed if it is no longer used via the {@link Music#dispose()} method.
	 * Music instances are automatically paused when the application is paused and resumed when the application is resumed.
	 *
	 * @param file the FileHandle
	 * @return the new Music or null if the Music could not be loaded
	 * @throws IOException in case the music could not be loaded */
	public Music newMusic (FileHandle file) throws IOException;
}
