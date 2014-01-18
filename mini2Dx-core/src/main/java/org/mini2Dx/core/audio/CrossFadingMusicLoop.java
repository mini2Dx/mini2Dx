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

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;

/**
 * Implements a loopable music track and crossfades into itself
 * 
 * @author Thomas Cashman
 */
public class CrossFadingMusicLoop implements Runnable {
	private Music currentTrack, nextTrack;
	private long crossfadeTime, crossfadeDuration;
	private ScheduledExecutorService scheduledExecutorService;
	private ScheduledFuture<?> scheduledFuture;
	private float targetVolume = 1f;

	/**
	 * Constructor
	 * 
	 * @param musicFile
	 *            The {@link FileHandle} for the music to be looped
	 * @param crossfadeTime
	 *            The time at which the crossfade begins at the end of the track
	 * @param timeUnit
	 *            The {@link TimeUnit} for crossfadeTime
	 */
	public CrossFadingMusicLoop(FileHandle musicFile, long crossfadeTime,
			long crossfadeDuration, TimeUnit timeUnit) {
		this.currentTrack = Gdx.audio.newMusic(musicFile);
		this.nextTrack = Gdx.audio.newMusic(musicFile);
		this.crossfadeTime = timeUnit.toMillis(crossfadeTime);
		this.crossfadeDuration = timeUnit.toMillis(crossfadeDuration);
		this.scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
	}

	@Override
	public void run() {
		nextTrack.play();
		nextTrack.setVolume(0f);
		Music tempTrack = currentTrack;
		currentTrack = nextTrack;
		nextTrack = tempTrack;
		scheduleFadeIn();
		scheduleFadeOut();
	}

	private void scheduleFadeIn() {
		for (int i = 0; i < crossfadeDuration; i += 50) {
			float volume = MathUtils.clamp((i / crossfadeDuration), 0f, targetVolume) ;
			scheduledExecutorService.schedule(new ScheduleFadeIn(volume), i,
					TimeUnit.MILLISECONDS);
		}
	}

	private void scheduleFadeOut() {
		for (int i = 0; i < crossfadeDuration; i += 50) {
			float volume = MathUtils.clamp(1f - (i / crossfadeDuration), 0f, targetVolume) ;
			scheduledExecutorService.schedule(new ScheduleFadeOut(volume), i,
					TimeUnit.MILLISECONDS);
		}
	}

	/**
	 * Starts playing the loop
	 */
	public void play() {
		currentTrack.setVolume(targetVolume);
		currentTrack.play();
		long time = (long) crossfadeTime;
		scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(this,
				time, time, TimeUnit.MILLISECONDS);
	}

	/**
	 * Stops playing the loop
	 */
	public void stop() {
		if(scheduledFuture != null) {
			scheduledFuture.cancel(false);
			while (!scheduledFuture.isDone()) {
				try {
					Thread.sleep(1);
				} catch (Exception e) {
				}
			}
		}
		currentTrack.stop();
		nextTrack.stop();
		scheduledFuture = null;
	}

	/**
	 * Returns if the loop is playing
	 * 
	 * @return True if playing
	 */
	public boolean isPlaying() {
		return scheduledFuture != null;
	}

	/**
	 * Cleans up resources. To be called when this instance is no longer needed.
	 */
	public void dispose() {
		if (isPlaying()) {
			throw new RuntimeException(
					"Cannot dispose of a music instance that is currently playing");
		}
		currentTrack.dispose();
		nextTrack.dispose();
	}
	
	public void setVolume(float volume) {
		targetVolume = volume;
		if(currentTrack.isPlaying()) {
			currentTrack.setVolume(targetVolume);
		}
	}

	private class ScheduleFadeOut implements Runnable {
		private float volume;

		public ScheduleFadeOut(float volume) {
			this.volume = volume;
		}

		public void run() {
			nextTrack.setVolume(volume);
		}
	}

	private class ScheduleFadeIn implements Runnable {
		private float volume;

		public ScheduleFadeIn(float volume) {
			this.volume = volume;
		}

		public void run() {
			currentTrack.setVolume(volume);
		}
	}
}
