/**
 * Copyright (c) 2013, mini2Dx Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.audio;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;

/**
 * Implements a loopable music track and crossfades into itself.
 * 
 * Note: It is required to call {@link #update()} each frame when using this
 * object
 * 
 * @author Thomas Cashman
 */
public class CrossFadingMusicLoop {
    private final float crossfadeTime, crossfadeDuration;

    private Music currentTrack, nextTrack;
    private volatile boolean playing;
    private volatile float targetVolume = 1f;
    private float cursor;
    private long previousTimestamp;

    private ScheduledExecutorService scheduledExecutorService;

    /**
     * Constructor
     * 
     * @param musicFile
     *            The {@link FileHandle} for the music to be looped
     * @param crossfadeTime
     *            The time (in seconds) at which the crossfade begins at the end
     *            of the track
     * @param crossfadeDuration
     *            The duration of the crossfade in seconds
     */
    public CrossFadingMusicLoop(FileHandle musicFile, float crossfadeTime, float crossfadeDuration) {
        this.currentTrack = Gdx.audio.newMusic(musicFile);
        this.nextTrack = Gdx.audio.newMusic(musicFile);
        this.crossfadeTime = crossfadeTime;
        this.crossfadeDuration = crossfadeDuration;

        scheduledExecutorService = Executors.newScheduledThreadPool(1);
    }

    /**
     * Updates the crossfading of the track
     */
    public void update() {
        if (playing && !currentTrack.isPlaying()) {
            currentTrack.setVolume(targetVolume);
            currentTrack.play();
            previousTimestamp = System.currentTimeMillis();
        }
        if (!playing) {
            if(currentTrack.isPlaying()) {
                currentTrack.stop();
                nextTrack.stop();
            }
            return;
        }
        long timestamp = System.currentTimeMillis();
        cursor += (timestamp - previousTimestamp) / 1000f;
        previousTimestamp = timestamp;

        if (cursor < crossfadeTime) {
            currentTrack.setVolume(targetVolume);
        } else {
            fadeInNextTrack();
            fadeOutCurrentTrack();
        }

        if (cursor >= crossfadeTime + crossfadeDuration) {
            cursor -= crossfadeTime + crossfadeDuration;
            Music tmpTrack = currentTrack;
            currentTrack = nextTrack;
            nextTrack = tmpTrack;
        }
    }

    private void fadeInNextTrack() {
        if(!playing) {
            return;
        }
        float trackTargetVolume = MathUtils.clamp((Float.valueOf(cursor - crossfadeTime) / Float.valueOf(crossfadeDuration)), 0f,
                targetVolume);
        nextTrack.setVolume(trackTargetVolume);
        if (!nextTrack.isPlaying()) {
            nextTrack.play();
        }
    }

    private void fadeOutCurrentTrack() {
        float trackTargetVolume = MathUtils.clamp(1f - (Float.valueOf(cursor - crossfadeTime) / Float.valueOf(crossfadeDuration)), 0f,
                targetVolume);
        currentTrack.setVolume(trackTargetVolume);
    }

    /**
     * Starts playing the loop
     */
    public void play() {
        playing = true;
    }

    /**
     * Stops playing the loop
     */
    public void stop() {
        playing = false;
    }

    /**
     * Returns if the loop is playing
     * 
     * @return True if playing
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * Cleans up resources. To be called when this instance is no longer needed.
     */
    public void dispose() {
        if (isPlaying()) {
            throw new RuntimeException("Cannot dispose of a music instance that is currently playing");
        }
        if(currentTrack.isPlaying()) {
            currentTrack.stop();
        }
        if(nextTrack.isPlaying()) {
            nextTrack.stop();
        }
        currentTrack.dispose();
        nextTrack.dispose();
        scheduledExecutorService.shutdown();
    }

    public void setVolume(float volume) {
        targetVolume = volume;
    }

    public void fadeOut(long duration) {
        for (long i = 0; i < duration; i += 50) {
            float volume = MathUtils.clamp(1f - (Float.valueOf(i) / Float.valueOf(duration)), 0f, 1f);
            scheduledExecutorService.schedule(new ScheduleFadeOut(volume), i, TimeUnit.MILLISECONDS);
        }
    }

    public void fadeOutAndStop(long duration) {
        fadeOut(duration - 50);
        scheduledExecutorService.schedule(new ScheduleStop(), duration, TimeUnit.MILLISECONDS);
    }

    public class ScheduleStop implements Runnable {

        @Override
        public void run() {
            stop();
        }

    }

    private class ScheduleFadeOut implements Runnable {
        private float volume;

        public ScheduleFadeOut(float volume) {
            this.volume = volume;
        }

        @Override
        public void run() {
            targetVolume = volume;
        }
    }
}
