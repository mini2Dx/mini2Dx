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

public class Lwjgl3Mini2DxConfig extends Lwjgl3ApplicationConfiguration {
	public final String gameIdentifier;
	/**
	 * The target framerate
	 */
	public int targetFPS = 60;
	/**
	 * The target timestep. Typically 1 / targetFPS
	 */
	public float targetTimestep = (1f / targetFPS);
	/**
	 * The window listener
	 */
	public Lwjgl3Mini2DxWindowListener windowListener;

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
		targetTimestep = config.targetTimestep;
	}
}
