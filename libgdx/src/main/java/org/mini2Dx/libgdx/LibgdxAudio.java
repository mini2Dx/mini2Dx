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
import org.mini2Dx.core.Audio;
import org.mini2Dx.core.audio.*;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.libgdx.audio.LibgdxAsyncSoundResult;
import org.mini2Dx.libgdx.audio.LibgdxMusic;
import org.mini2Dx.libgdx.audio.LibgdxSound;
import org.mini2Dx.libgdx.files.LibgdxFileHandle;

import java.io.IOException;

public class LibgdxAudio implements Audio {
	private final Array<MusicCompletionListener> musicCompletionListeners = new Array<MusicCompletionListener>();
	private final Array<SoundCompletionListener> soundCompletionListeners = new Array<SoundCompletionListener>();

	@Override
	public Sound newSound(FileHandle fileHandle) throws IOException {
		final LibgdxFileHandle gdxFileHandle = (LibgdxFileHandle) fileHandle;
		return new LibgdxSound(Gdx.audio.newSound(gdxFileHandle.fileHandle));
	}

	@Override
	public AsyncSoundResult newAsyncSound(FileHandle fileHandle) {
		return new LibgdxAsyncSoundResult(fileHandle);
	}

	@Override
	public Music newMusic(FileHandle fileHandle) throws IOException {
		final LibgdxFileHandle gdxFileHandle = (LibgdxFileHandle) fileHandle;
		return new LibgdxMusic(this, Gdx.audio.newMusic(gdxFileHandle.fileHandle));
	}

	@Override
	public void addMusicCompletionListener(MusicCompletionListener completionListener) {
		musicCompletionListeners.add(completionListener);
	}

	@Override
	public void removeMusicCompletionListener(MusicCompletionListener completionListener) {
		musicCompletionListeners.removeValue(completionListener, false);
	}

	@Override
	public void addSoundCompletionListener(SoundCompletionListener completionListener) {
		soundCompletionListeners.add(completionListener);
	}

	@Override
	public void removeSoundCompletionListener(SoundCompletionListener completionListener) {
		soundCompletionListeners.removeValue(completionListener, false);
	}

	public void notifyMusicCompletionListeners(Music music) {
		for(int i = musicCompletionListeners.size - 1; i >= 0; i--) {
			musicCompletionListeners.get(i).onMusicCompleted(music);
		}
	}

	public void notifySoundCompletionListeners(long soundId) {
		for(int i = soundCompletionListeners.size - 1; i >= 0; i--) {
			soundCompletionListeners.get(i).onSoundCompleted(soundId);
		}
	}
}
