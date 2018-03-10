/*******************************************************************************
 * Copyright 2011 See LIBGDX_AUTHORS file.
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
package com.badlogic.gdx.backends.iosrobovm;

import com.badlogic.gdx.files.FileHandle;

/**
 *
 */
public class IOSMini2DxSound extends IOSSound {
	private final IOSMini2DxAudio audio;

	public IOSMini2DxSound(IOSMini2DxAudio audio, FileHandle filePath) {
		super(filePath);
		this.audio = audio;
	}

	@Override
	public long play (float volume, float pitch, float pan, boolean loop) {
		long soundId = super.play(volume, pitch, pan, loop);
		if(soundId < 0L) {
			return soundId;
		}
		audio.appendRecentSoundId(soundId);
		return soundId;
	}
}
