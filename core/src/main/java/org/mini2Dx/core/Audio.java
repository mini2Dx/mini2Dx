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

import org.mini2Dx.core.audio.*;
import org.mini2Dx.core.files.FileHandle;

import java.io.IOException;

/**
 * Provides audio functionality
 */
public interface Audio {
	/**
	 * <p>
	 * Creates a new {@link Sound} instance used to play back audio sound effects (e.g. explosions, gunshots, etc.).
	 * Note that the complete audio data is loaded into RAM and you should avoid loading large files.
	 * The current upper limit for decoded audio is 1 MB.
	 * </p>
	 *
	 * <p>Currently supported formats are WAV, MP3 and OGG.</p>
	 *
	 * <p>When the {@link Sound} instance is no longer needed, call the {@link Sound#dispose()} method</p>
	 *
	 * @param fileHandle The {@link FileHandle} to load the audio from
	 * @return The new {@link Sound} instance
	 * @throws IOException Thrown when the sound could not be loaded
	 */
    public Sound newSound(FileHandle fileHandle) throws IOException;

	/**
	 * See {@link #newSound(FileHandle)}.
	 * Loads a new {@link Sound} instance asynchronously.
	 * On platforms not supporting async sound loading a warning will be logged and the sound would be loaded on game thread
	 * when {@link AsyncSoundResult#getResult()} is called.
	 * Supported platforms: Libgdx Desktop LWJGL2/3, Monogame.
	 *
	 * @param fileHandle The {@link FileHandle} to load the audio from
	 * @return The {@link AsyncSoundResult} instance that's loading the sound
	 */
	public AsyncSoundResult newAsyncSound(FileHandle fileHandle);

	/**
	 * <p>
	 * Creates a new {@link Music} instance used to play back an audio stream from a file.
	 * Music instances are automatically paused when the application is paused and resumed when the application is resumed.
	 * </p>
	 *
	 * <p>Currently supported formats are WAV, MP3 and OGG.</p>
	 *
	 * <p>When the {@link Music} instance is no longer needed, call the {@link Music#dispose()} method</p>
	 *
	 * @param fileHandle The {@link FileHandle} to stream the audio from
	 * @return The new {@link Music} instance
	 * @throws IOException Thrown when the music could not be loaded
	 */
	public Music newMusic(FileHandle fileHandle) throws IOException;

	/**
	 * Adds a {@link MusicCompletionListener}
	 * @param completionListener The {@link MusicCompletionListener} to be notified of {@link Music} completion events.
	 */
	public void addMusicCompletionListener(MusicCompletionListener completionListener);

	/**
	 * Removes a {@link MusicCompletionListener}
	 * @param completionListener The {@link MusicCompletionListener} to be removed
	 */
	public void removeMusicCompletionListener(MusicCompletionListener completionListener);

	/**
	 * Adds a {@link SoundCompletionListener}
	 * @param completionListener The {@link SoundCompletionListener} to be notified of {@link Sound} completion events.
	 */
	public void addSoundCompletionListener(SoundCompletionListener completionListener);

	/**
	 * Removes a {@link SoundCompletionListener}
	 * @param completionListener The {@link SoundCompletionListener} to be removed
	 */
	public void removeSoundCompletionListener(SoundCompletionListener completionListener);
}
