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
package com.badlogic.gdx.backends.lwjgl3.audio;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.audio.Sound;
import org.mini2Dx.libgdx.LibgdxAudio;
import org.mini2Dx.libgdx.audio.LibgdxExtAudio;
import org.mini2Dx.libgdx.audio.LibgdxSound;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

/**
 * Modified version of OpenALAudio to support sound completion events
 */
public class Mini2DxOpenALAudio implements LibgdxExtAudio {
	private final int deviceBufferSize;
	private final int deviceBufferCount;
	private IntArray idleSources, allSources;
	private LongMap<Integer> soundIdToSource;
	private IntMap<Long> sourceToSoundId;
	private long nextSoundId = 0;
	private final ObjectMap<String, Class<? extends Mini2DxOpenALSound>> extensionToSoundClass = new ObjectMap();
	private final ObjectMap<String, Class<? extends Mini2DxOpenALMusic>> extensionToMusicClass = new ObjectMap();
	private LongArray recentSoundIds;
	private Mini2DxOpenALSound[] recentSounds;
	private int mostRecetSound = -1;

	Array<Mini2DxOpenALMusic> music = new Array(false, 1, Mini2DxOpenALMusic.class);
	long device;
	long context;
	boolean noDevice = false;

	public Mini2DxOpenALAudio () {
		this(16, 9, 512);
	}

	public Mini2DxOpenALAudio (int simultaneousSources, int deviceBufferCount, int deviceBufferSize) {
		this.deviceBufferSize = deviceBufferSize;
		this.deviceBufferCount = deviceBufferCount;

		registerSound("ogg", Mini2DxOgg.Sound.class);
		registerMusic("ogg", Mini2DxOgg.Music.class);
		registerSound("wav", Mini2DxWav.Sound.class);
		registerMusic("wav", Mini2DxWav.Music.class);
		registerSound("mp3", Mini2DxMp3.Sound.class);
		registerMusic("mp3", Mini2DxMp3.Music.class);

		device = alcOpenDevice((ByteBuffer)null);
		if (device == 0L) {
			noDevice = true;
			return;
		}
		ALCCapabilities deviceCapabilities = ALC.createCapabilities(device);
		context = alcCreateContext(device, (IntBuffer)null);
		if (context == 0L) {
			alcCloseDevice(device);
			noDevice = true;
			return;
		}
		if (!alcMakeContextCurrent(context)) {
			noDevice = true;
			return;
		}
		AL.createCapabilities(deviceCapabilities);

		alGetError();
		allSources = new IntArray(false, simultaneousSources);
		for (int i = 0; i < simultaneousSources; i++) {
			int sourceID = alGenSources();
			if (alGetError() != AL_NO_ERROR) break;
			allSources.add(sourceID);
		}
		idleSources = new IntArray(allSources);
		soundIdToSource = new LongMap<Integer>();
		sourceToSoundId = new IntMap<Long>();

		FloatBuffer orientation = (FloatBuffer)BufferUtils.createFloatBuffer(6)
				.put(new float[] {0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f}).flip();
		alListenerfv(AL_ORIENTATION, orientation);
		FloatBuffer velocity = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] {0.0f, 0.0f, 0.0f}).flip();
		alListenerfv(AL_VELOCITY, velocity);
		FloatBuffer position = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] {0.0f, 0.0f, 0.0f}).flip();
		alListenerfv(AL_POSITION, position);

		recentSounds = new Mini2DxOpenALSound[simultaneousSources];
		recentSoundIds = new LongArray(simultaneousSources);
	}

	public void registerSound (String extension, Class<? extends Mini2DxOpenALSound> soundClass) {
		if (extension == null) throw new IllegalArgumentException("extension cannot be null.");
		if (soundClass == null) throw new IllegalArgumentException("soundClass cannot be null.");
		extensionToSoundClass.put(extension, soundClass);
	}

	public void registerMusic (String extension, Class<? extends Mini2DxOpenALMusic> musicClass) {
		if (extension == null) throw new IllegalArgumentException("extension cannot be null.");
		if (musicClass == null) throw new IllegalArgumentException("musicClass cannot be null.");
		extensionToMusicClass.put(extension, musicClass);
	}

	public Mini2DxOpenALSound newSound (FileHandle file) {
		if (file == null) throw new IllegalArgumentException("file cannot be null.");
		Class<? extends Mini2DxOpenALSound> soundClass = extensionToSoundClass.get(file.extension().toLowerCase());
		if (soundClass == null) throw new GdxRuntimeException("Unknown file extension for sound: " + file);
		try {
			return soundClass.getConstructor(new Class[] {Mini2DxOpenALAudio.class, FileHandle.class}).newInstance(this, file);
		} catch (Exception ex) {
			throw new GdxRuntimeException("Error creating sound " + soundClass.getName() + " for file: " + file, ex);
		}
	}

	//used for async sound loading, invoked via reflection
	@SuppressWarnings("unused")
	public Sound newSound(InputStream stream, String fileName) {
		if (stream == null)
			throw new IllegalArgumentException("file cannot be null.");
		Class<? extends Mini2DxOpenALSound> soundClass = extensionToSoundClass.get(fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase());
		if (soundClass == null)
			throw new GdxRuntimeException("Unknown file extension for sound: " + fileName);
		try {
			return new LibgdxSound(soundClass.getConstructor(new Class[] { Mini2DxOpenALAudio.class, InputStream.class, String.class }).newInstance(this,
					stream, fileName));
		} catch (Exception ex) {
			throw new GdxRuntimeException("Error creating sound " + soundClass.getName() + " for file: " + fileName, ex);
		}
	}

	@Override
	public Music newMusic(FileHandle file) {
		return newMusic(file, false);
	}

	public Mini2DxOpenALMusic newMusic (FileHandle file, boolean loadIntoMemory) {
		if (file == null) throw new IllegalArgumentException("file cannot be null.");
		Class<? extends Mini2DxOpenALMusic> musicClass = extensionToMusicClass.get(file.extension().toLowerCase());
		if (musicClass == null) throw new GdxRuntimeException("Unknown file extension for music: " + file);
		try {
			if(loadIntoMemory) {
				return musicClass.getConstructor(new Class[] {Mini2DxOpenALAudio.class, byte[].class}).newInstance(this, file.readBytes());
			} else {
				return musicClass.getConstructor(new Class[] {Mini2DxOpenALAudio.class, FileHandle.class}).newInstance(this, file);
			}
		} catch (Exception ex) {
			throw new GdxRuntimeException("Error creating music " + musicClass.getName() + " for file: " + file, ex);
		}
	}

	@Override
	public Music newMusic(byte[] bytes, String format) {
		if (bytes == null) throw new IllegalArgumentException("bytes cannot be null.");
		Class<? extends Mini2DxOpenALMusic> musicClass = extensionToMusicClass.get(format.toLowerCase());
		if (musicClass == null) throw new GdxRuntimeException("Unknown file extension for byte[]: " + format);
		try {
			return musicClass.getConstructor(new Class[] {Mini2DxOpenALAudio.class, byte[].class}).newInstance(this, bytes);
		} catch (Exception ex) {
			throw new GdxRuntimeException("Error creating music " + musicClass.getName() + " for byte[]: " + format, ex);
		}
	}

	int obtainSource (boolean isMusic) {
		if (noDevice) return 0;
		for (int i = 0, n = idleSources.size; i < n; i++) {
			int sourceId = idleSources.get(i);
			int state = alGetSourcei(sourceId, AL_SOURCE_STATE);
			if (state != AL_PLAYING && state != AL_PAUSED) {
				if (sourceToSoundId.containsKey(sourceId)) {
					long soundId = sourceToSoundId.get(sourceId);
					sourceToSoundId.remove(sourceId);
					soundIdToSource.remove(soundId);
				}

				if (isMusic) {
					idleSources.removeIndex(i);
				} else {
					long soundId = nextSoundId++;
					sourceToSoundId.put(sourceId, soundId);
					soundIdToSource.put(soundId, sourceId);
				}
				alSourceStop(sourceId);
				alSourcei(sourceId, AL_BUFFER, 0);
				AL10.alSourcef(sourceId, AL10.AL_GAIN, 1);
				AL10.alSourcef(sourceId, AL10.AL_PITCH, 1);
				AL10.alSource3f(sourceId, AL10.AL_POSITION, 0, 0, 1f);
				return sourceId;
			}
		}
		return -1;
	}

	void freeSource (int sourceID) {
		if (noDevice) return;
		alGetError();
		alSourceStop(sourceID);
		int e = alGetError();
		if (e != AL_NO_ERROR) throw new GdxRuntimeException("AL Error: " + e);
		alSourcei(sourceID, AL_BUFFER, 0);
		e = alGetError();
		if (e != AL_NO_ERROR) throw new GdxRuntimeException("AL Error: " + e);
		Long soundId = sourceToSoundId.remove(sourceID);
		if (soundId != null) soundIdToSource.remove(soundId);
		idleSources.add(sourceID);
	}

	void freeBuffer (int bufferID) {
		if (noDevice) return;
		for (int i = 0, n = idleSources.size; i < n; i++) {
			int sourceID = idleSources.get(i);
			if (alGetSourcei(sourceID, AL_BUFFER) == bufferID) {
				Long soundId = sourceToSoundId.remove(sourceID);
				if (soundId != null) soundIdToSource.remove(soundId);
				alSourceStop(sourceID);
				alSourcei(sourceID, AL_BUFFER, 0);
			}
		}
	}

	void stopSourcesWithBuffer (int bufferID) {
		if (noDevice) return;
		for (int i = 0, n = idleSources.size; i < n; i++) {
			int sourceID = idleSources.get(i);
			if (alGetSourcei(sourceID, AL_BUFFER) == bufferID) {
				Long soundId = sourceToSoundId.remove(sourceID);
				if (soundId != null) soundIdToSource.remove(soundId);
				alSourceStop(sourceID);
			}
		}
	}

	void pauseSourcesWithBuffer (int bufferID) {
		if (noDevice) return;
		for (int i = 0, n = idleSources.size; i < n; i++) {
			int sourceID = idleSources.get(i);
			if (alGetSourcei(sourceID, AL_BUFFER) == bufferID) alSourcePause(sourceID);
		}
	}

	void resumeSourcesWithBuffer (int bufferID) {
		if (noDevice) return;
		for (int i = 0, n = idleSources.size; i < n; i++) {
			int sourceID = idleSources.get(i);
			if (alGetSourcei(sourceID, AL_BUFFER) == bufferID) {
				if (alGetSourcei(sourceID, AL_SOURCE_STATE) == AL_PAUSED) alSourcePlay(sourceID);
			}
		}
	}

	public void update () {
		if (noDevice) {
			return;
		}
		final LibgdxAudio gdxAudio = (LibgdxAudio) Mdx.audio;

		for (int i = 0; i < music.size; i++) {
			music.items[i].update();
		}

		for (int i = recentSoundIds.size - 1; i >= 0; i--) {
			long soundId = recentSoundIds.items[i];
			if (isSoundPlaying(soundId)) {
				continue;
			}
			recentSoundIds.removeIndex(i);
			gdxAudio.notifySoundCompletionListeners(soundId);
		}
	}

	public void appendRecentSoundId(long soundId) {
		recentSoundIds.add(soundId);
	}

	public boolean isSoundPlaying(long soundId) {
		if (!soundIdToSource.containsKey(soundId)) {
			return false;
		}
		int sourceId = soundIdToSource.get(soundId);
		int soundState = AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE);
		alGetError();

		switch (soundState) {
		case AL_INITIAL:
		case AL_PAUSED:
		case AL_PLAYING:
			return true;
		default:
			return false;
		}
	}

	public long getSoundId (int sourceId) {
		Long soundId = sourceToSoundId.get(sourceId);
		return soundId != null ? soundId : -1;
	}

	public int getSourceId (long soundId) {
		Integer sourceId = soundIdToSource.get(soundId);
		return sourceId != null ? sourceId : -1;
	}

	public void stopSound (long soundId) {
		Integer sourceId = soundIdToSource.get(soundId);
		if (sourceId != null) alSourceStop(sourceId);
	}

	public void pauseSound (long soundId) {
		Integer sourceId = soundIdToSource.get(soundId);
		if (sourceId != null) alSourcePause(sourceId);
	}

	public void resumeSound (long soundId) {
		int sourceId = soundIdToSource.get(soundId, -1);
		if (sourceId != -1 && alGetSourcei(sourceId, AL_SOURCE_STATE) == AL_PAUSED) alSourcePlay(sourceId);
	}

	public void setSoundGain (long soundId, float volume) {
		Integer sourceId = soundIdToSource.get(soundId);
		if (sourceId != null) AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
	}

	public void setSoundLooping (long soundId, boolean looping) {
		Integer sourceId = soundIdToSource.get(soundId);
		if (sourceId != null) alSourcei(sourceId, AL10.AL_LOOPING, looping ? AL10.AL_TRUE : AL10.AL_FALSE);
	}

	public void setSoundPitch (long soundId, float pitch) {
		Integer sourceId = soundIdToSource.get(soundId);
		if (sourceId != null) AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
	}

	public void setSoundPan (long soundId, float pan, float volume) {
		int sourceId = soundIdToSource.get(soundId, -1);
		if (sourceId != -1) {
			AL10.alSource3f(sourceId, AL10.AL_POSITION, MathUtils.cos((pan - 1) * MathUtils.PI / 2), 0,
					MathUtils.sin((pan + 1) * MathUtils.PI / 2));
			AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
		}
	}

	public void dispose () {
		if (noDevice) return;
		for (int i = 0, n = allSources.size; i < n; i++) {
			int sourceID = allSources.get(i);
			int state = alGetSourcei(sourceID, AL_SOURCE_STATE);
			if (state != AL_STOPPED) alSourceStop(sourceID);
			alDeleteSources(sourceID);
		}

		sourceToSoundId.clear();
		soundIdToSource.clear();

		alcDestroyContext(context);
		alcCloseDevice(device);
	}

	public AudioDevice newAudioDevice (int sampleRate, final boolean isMono) {
		if (noDevice) return new AudioDevice() {
			@Override
			public void writeSamples (float[] samples, int offset, int numSamples) {
			}

			@Override
			public void writeSamples (short[] samples, int offset, int numSamples) {
			}

			@Override
			public void setVolume (float volume) {
			}

			@Override
			public boolean isMono () {
				return isMono;
			}

			@Override
			public int getLatency () {
				return 0;
			}

			@Override
			public void dispose () {
			}
		};
		return new Mini2DxOpenALAudioDevice(this, sampleRate, isMono, deviceBufferSize, deviceBufferCount);
	}

	public AudioRecorder newAudioRecorder (int samplingRate, boolean isMono) {
		if (noDevice) return new AudioRecorder() {
			@Override
			public void read (short[] samples, int offset, int numSamples) {
			}

			@Override
			public void dispose () {
			}
		};
		return new JavaSoundAudioRecorder(samplingRate, isMono);
	}

	/** Retains a list of the most recently played sounds and stops the sound played least recently if necessary for a new sound to
	 * play */
	protected void retain (Mini2DxOpenALSound sound, boolean stop) {
		// Move the pointer ahead and wrap
		mostRecetSound++;
		mostRecetSound %= recentSounds.length;

		if (stop) {
			// Stop the least recent sound (the one we are about to bump off the buffer)
			if (recentSounds[mostRecetSound] != null) recentSounds[mostRecetSound].stop();
		}

		recentSounds[mostRecetSound] = sound;
	}

	/** Removes the disposed sound from the least recently played list */
	public void forget (Mini2DxOpenALSound sound) {
		for (int i = 0; i < recentSounds.length; i++) {
			if (recentSounds[i] == sound) recentSounds[i] = null;
		}
	}
}