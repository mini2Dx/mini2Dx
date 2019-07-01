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
package org.mini2Dx.libgdx.audio;

import org.mini2Dx.core.audio.Music;
import org.mini2Dx.libgdx.LibgdxAudio;

import java.util.concurrent.atomic.AtomicLong;

public class LibgdxMusic implements Music, com.badlogic.gdx.audio.Music.OnCompletionListener {
	private static final AtomicLong ID_COUNTER = new AtomicLong();

	public final long id;
	public final com.badlogic.gdx.audio.Music music;
	public final LibgdxAudio audio;

	public LibgdxMusic(LibgdxAudio audio, com.badlogic.gdx.audio.Music music) {
		this.id = ID_COUNTER.incrementAndGet();
		this.audio = audio;
		this.music = music;
		this.music.setOnCompletionListener(this);
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void play() {
		music.play();
	}

	@Override
	public void pause() {
		music.pause();
	}

	@Override
	public void stop() {
		music.stop();
	}

	@Override
	public boolean isPlaying() {
		return music.isPlaying();
	}

	@Override
	public void setLooping(boolean isLooping) {
		music.setLooping(true);
	}

	@Override
	public boolean isLooping() {
		return music.isLooping();
	}

	@Override
	public void setVolume(float volume) {
		music.setVolume(volume);
	}

	@Override
	public float getVolume() {
		return music.getVolume();
	}

	@Override
	public float getPosition() {
		return music.getPosition();
	}

	@Override
	public void dispose() {
		music.dispose();
	}

	@Override
	public void onCompletion(com.badlogic.gdx.audio.Music music) {
		audio.notifyMusicCompletionListeners(this);
	}
}
