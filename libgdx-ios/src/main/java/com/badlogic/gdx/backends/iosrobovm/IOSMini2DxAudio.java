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

import com.badlogic.gdx.backends.iosrobovm.objectal.ALChannelSource;
import com.badlogic.gdx.backends.iosrobovm.objectal.ALSource;
import com.badlogic.gdx.backends.iosrobovm.objectal.OALSimpleAudio;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.LongArray;
import org.mini2Dx.core.Audio;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.audio.Music;
import org.mini2Dx.core.audio.MusicCompletionListener;
import org.mini2Dx.core.audio.Sound;
import org.mini2Dx.core.audio.SoundCompletionListener;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.libgdx.LibgdxAudio;
import org.robovm.apple.foundation.NSArray;

import java.io.IOException;

/**
 *
 */
public class IOSMini2DxAudio extends IOSAudio {
	private final LongArray recentSoundIds = new LongArray();
	
	private final ALChannelSource channel;
	private final NSArray<ALSource> sourcePool;
	
	public IOSMini2DxAudio(IOSApplicationConfiguration config) {
		super(config);
		channel = OALSimpleAudio.sharedInstance().getChannelSource();
		sourcePool = channel.getSourcePool().getSources();
	}

	public void update() {
		for (int i = recentSoundIds.size - 1; i >= 0; i--) {
			long soundId = recentSoundIds.items[i];
			
			ALSource source = getSoundSource(soundId);
			if (source == null) {
				continue;
			}
			//TODO: Check if sound has finished playing
			recentSoundIds.removeIndex(i);
			((LibgdxAudio) Mdx.audio).notifySoundCompletionListeners(soundId);
		}
	}
	
	void appendRecentSoundId(long soundId) {
		recentSoundIds.add(soundId);
	}
	
	private ALSource getSoundSource (long soundId) {	
		for (ALSource source : sourcePool) {
			if (source.getSourceId() == soundId) return source;			
		}
		return null;
	}
}
