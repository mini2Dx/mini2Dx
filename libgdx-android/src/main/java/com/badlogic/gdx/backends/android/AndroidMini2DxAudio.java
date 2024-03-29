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
package com.badlogic.gdx.backends.android;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.*;
import android.os.Build;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.LongArray;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.libgdx.LibgdxAudio;
import org.mini2Dx.libgdx.audio.LibgdxExtAudio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Modified version of AndroidAudio to support sound completion events
 */
public class AndroidMini2DxAudio implements com.badlogic.gdx.backends.android.AndroidAudio, LibgdxExtAudio {
    private final SoundPool soundPool;
    private final AudioManager manager;
    private final LongArray recentSoundIds = new LongArray();
    protected final List<AndroidMini2DxMusic> musics = new ArrayList<AndroidMini2DxMusic>();

    public AndroidMini2DxAudio(Context context, AndroidApplicationConfiguration config) {
        if (!config.disableAudio) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AudioAttributes audioAttrib = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build();
                soundPool = new SoundPool.Builder().setAudioAttributes(audioAttrib).setMaxStreams(config.maxSimultaneousSounds).build();
            } else {
                soundPool = new SoundPool(config.maxSimultaneousSounds, AudioManager.STREAM_MUSIC, 0);// srcQuality: the sample-rate converter quality. Currently has no effect. Use 0 for the default.
            }
            manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (context instanceof Activity) {
                ((Activity) context).setVolumeControlStream(AudioManager.STREAM_MUSIC);
            }
        } else {
            soundPool = null;
            manager = null;
        }
    }

    public void update() {
        for (int i = recentSoundIds.size - 1; i >= 0; i--) {
            long soundId = recentSoundIds.items[i];
            if (isSoundPlaying(soundId)) {
                continue;
            }
            recentSoundIds.removeIndex(i);
            ((LibgdxAudio) Mdx.audio).notifySoundCompletionListeners(soundId);
        }
    }

    private boolean isSoundPlaying(long streamId) {
        //TODO: Implement another audio system to detect this
        return false;
    }

    public void appendRecentSoundId(long streamId) {
        recentSoundIds.add(streamId);
    }

    public void pause() {
        if (soundPool == null) {
            return;
        }
        synchronized (musics) {
            for (AndroidMini2DxMusic music : musics) {
                if (music.isPlaying()) {
                    music.pause();
                    music.wasPlaying = true;
                } else
                    music.wasPlaying = false;
            }
        }
        this.soundPool.autoPause();
    }

    public void resume() {
        if (soundPool == null) {
            return;
        }
        synchronized (musics) {
            for (int i = 0; i < musics.size(); i++) {
                if (musics.get(i).wasPlaying) musics.get(i).play();
            }
        }
        this.soundPool.autoResume();
    }

    @Override
    public void notifyMusicDisposed(AndroidMusic music) {
        synchronized (musics) {
            musics.remove(this);
        }
    }

    /**
     * Kills the soundpool and all other resources
     */
    public void dispose() {
        if (soundPool == null) {
            return;
        }
        synchronized (musics) {
            // gah i hate myself.... music.dispose() removes the music from the list...
            ArrayList<AndroidMini2DxMusic> musicsCopy = new ArrayList<AndroidMini2DxMusic>(musics);
            for (AndroidMini2DxMusic music : musicsCopy) {
                music.dispose();
            }
        }
        soundPool.release();
    }

    @Override
    public AudioDevice newAudioDevice(int samplingRate, boolean isMono) {
        if (soundPool == null) {
            throw new GdxRuntimeException("Android audio is not enabled by the application config.");
        }
        return new AndroidAudioDevice(samplingRate, isMono);
    }

    @Override
    public AudioRecorder newAudioRecorder(int samplingRate, boolean isMono) {
        if (soundPool == null) {
            throw new GdxRuntimeException("Android audio is not enabled by the application config.");
        }
        return new AndroidAudioRecorder(samplingRate, isMono);
    }

    @Override
    public Sound newSound(FileHandle fileHandle) {
        if (soundPool == null) {
            throw new GdxRuntimeException("Android audio is not enabled by the application config.");
        }
        AndroidFileHandle aHandle = (AndroidFileHandle) fileHandle;
        if (aHandle.type() == FileType.Internal) {
            try {
                AssetFileDescriptor descriptor = aHandle.getAssetFileDescriptor();
                AndroidMini2DxSound sound = new AndroidMini2DxSound(this, soundPool, manager, soundPool.load(descriptor, 1));
                descriptor.close();
                return sound;
            } catch (IOException ex) {
                throw new GdxRuntimeException("Error loading audio file: " + fileHandle
                        + "\nNote: Internal audio files must be placed in the assets directory.", ex);
            }
        } else {
            try {
                return new AndroidMini2DxSound(this, soundPool, manager, soundPool.load(aHandle.file().getPath(), 1));
            } catch (Exception ex) {
                throw new GdxRuntimeException("Error loading audio file: " + fileHandle, ex);
            }
        }
    }

    @Override
    public Music newMusic(FileHandle file) {
        if (soundPool == null) {
            throw new GdxRuntimeException("Android audio is not enabled by the application config.");
        }
        AndroidFileHandle aHandle = (AndroidFileHandle) file;

        MediaPlayer mediaPlayer = new MediaPlayer();

        if (aHandle.type() == FileType.Internal) {
            try {
                AssetFileDescriptor descriptor = aHandle.getAssetFileDescriptor();
                mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();
                mediaPlayer.prepare();
                AndroidMini2DxMusic music = new AndroidMini2DxMusic(this, mediaPlayer);
                synchronized (musics) {
                    musics.add(music);
                }
                return music;
            } catch (Exception ex) {
                throw new GdxRuntimeException("Error loading audio file: " + file
                        + "\nNote: Internal audio files must be placed in the assets directory.", ex);
            }
        } else {
            try {
                mediaPlayer.setDataSource(aHandle.file().getPath());
                mediaPlayer.prepare();
                AndroidMini2DxMusic music = new AndroidMini2DxMusic(this, mediaPlayer);
                synchronized (musics) {
                    musics.add(music);
                }
                return music;
            } catch (Exception ex) {
                throw new GdxRuntimeException("Error loading audio file: " + file, ex);
            }
        }
    }

    @Override
    public Music newMusic(FileHandle file, boolean loadIntoMemory) {
        if(!loadIntoMemory) {
            return newMusic(file);
        }
        return newMusic(file.readBytes(), file.extension());
    }

    @Override
    public Music newMusic(byte[] bytes, String format) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(new ByteArrayMediaSource(bytes));
            mediaPlayer.prepare();
            AndroidMini2DxMusic music = new AndroidMini2DxMusic(this, mediaPlayer);
            synchronized (musics) {
                musics.add(music);
            }
            return music;
        } catch (Exception ex) {
            throw new GdxRuntimeException("Error loading audio byte[]: " + format, ex);
        }
    }
}