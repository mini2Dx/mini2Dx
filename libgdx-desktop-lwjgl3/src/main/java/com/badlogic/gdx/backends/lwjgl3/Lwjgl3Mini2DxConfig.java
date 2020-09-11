/*******************************************************************************
 * Copyright 2020 See AUTHORS file
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
package com.badlogic.gdx.backends.lwjgl3;

import org.mini2Dx.core.TimestepMode;

public class Lwjgl3Mini2DxConfig extends Lwjgl3ApplicationConfiguration {
	public final String gameIdentifier;

	/**
	 * The {@link TimestepMode} to use
	 */
	public TimestepMode timestepMode = TimestepMode.DEFAULT;
	/**
	 * The target framerate
	 */
	public int targetFPS = 30;
	/**
	 * True if there should be no more updates than the target FPS (i.e. avoids fluctuations between 60 and 61 FPS)
	 */
	public boolean capUpdatesPerSecond = false;
	/**
	 * True if an error should be logged when frames a dropped
	 */
	public boolean errorOnFrameDrop = false;
	/**
	 * Sets the target framerate for the application. The CPU sleeps as needed. Must be positive.
	 * Use 0 to never sleep. Default is 0.
	 */
	public int foregroundFPS = 0;
	/**
	 * The window listener
	 */
	public Lwjgl3Mini2DxWindowListener windowListener;

	private long targetTimestepNanos = -1L;
	private float targetTimestepSeconds;

	public Lwjgl3Mini2DxConfig(String gameIdentifier) {
		this.gameIdentifier = gameIdentifier;
	}

	static Lwjgl3Mini2DxConfig copy(Lwjgl3Mini2DxConfig config) {
		Lwjgl3Mini2DxConfig copy = new Lwjgl3Mini2DxConfig(config.gameIdentifier);
		copy.set(config);
		return copy;
	}

	void set(Lwjgl3Mini2DxConfig config) {
		super.set(config);
		targetFPS = config.targetFPS;
		capUpdatesPerSecond = config.capUpdatesPerSecond;
		errorOnFrameDrop = config.errorOnFrameDrop;
		windowListener = config.windowListener;
		foregroundFPS = config.foregroundFPS;
	}

	private void setTargetTimestep() {
		if(targetTimestepNanos > -1L) {
			return;
		}
		targetTimestepSeconds = 1f / targetFPS;
		targetTimestepNanos = 1000000000L / targetFPS;
	}

	public long targetTimestepNanos() {
		setTargetTimestep();
		return targetTimestepNanos;
	}

	public float targetTimestepSeconds() {
		setTargetTimestep();
		return targetTimestepSeconds;
	}

	public long maximumTimestepNanos() {
		return targetTimestepNanos() * 2L;
	}

	public float maximumTimestepSeconds() {
		return targetTimestepSeconds() * 2f;
	}
}
