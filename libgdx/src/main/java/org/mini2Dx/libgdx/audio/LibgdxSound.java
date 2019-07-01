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

import org.mini2Dx.core.audio.Sound;

public class LibgdxSound implements Sound {
	public final com.badlogic.gdx.audio.Sound sound;

	public LibgdxSound(com.badlogic.gdx.audio.Sound sound) {
		this.sound = sound;
	}

	@Override
	public long play() {
		return sound.play();
	}

	@Override
	public long play(float volume) {
		return sound.play(volume);
	}

	@Override
	public long play(float volume, float pitch, float pan) {
		return sound.play(volume, pitch, pan);
	}

	@Override
	public long loop() {
		return sound.loop();
	}

	@Override
	public long loop(float volume) {
		return sound.loop(volume);
	}

	@Override
	public long loop(float volume, float pitch, float pan) {
		return sound.loop(volume, pitch, pan);
	}

	@Override
	public void stop() {
		sound.stop();
	}

	@Override
	public void pause() {
		sound.pause();
	}

	@Override
	public void resume() {
		sound.resume();
	}

	@Override
	public void stop(long soundId) {
		sound.stop(soundId);
	}

	@Override
	public void pause(long soundId) {
		sound.pause(soundId);
	}

	@Override
	public void resume(long soundId) {
		sound.resume(soundId);
	}

	@Override
	public void setLooping(long soundId, boolean looping) {
		sound.setLooping(soundId, looping);
	}

	@Override
	public void setPitch(long soundId, float pitch) {
		sound.setPitch(soundId, pitch);
	}

	@Override
	public void setVolume(long soundId, float volume) {
		sound.setVolume(soundId, volume);
	}

	@Override
	public void setPan(long soundId, float pan, float volume) {
		sound.setPan(soundId, pan, volume);
	}

	@Override
	public void dispose() {
		sound.dispose();
	}
}
