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
package org.mini2Dx.libgdx.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Lwjgl2Mini2DxConfig extends LwjglApplicationConfiguration {
	public final String gameIdentifier;
	/**
	 * The target framerate
	 */
	public int targetFPS = 30;
	/**
	 * True if an error should be logged when frames a dropped
	 */
	public boolean errorOnFrameDrop = false;

	private long targetTimestepNanos = -1L;
	private float targetTimestepSeconds;

	public Lwjgl2Mini2DxConfig(String gameIdentifier) {
		this.gameIdentifier = gameIdentifier;
		this.foregroundFPS = 0;
		this.backgroundFPS = 0;
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
