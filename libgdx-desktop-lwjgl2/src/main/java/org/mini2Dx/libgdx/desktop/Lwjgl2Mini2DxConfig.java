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
	public int targetFPS = 60;
	/**
	 * The target timestep
	 */
	public float targetTimestep = (1f / targetFPS);

	public Lwjgl2Mini2DxConfig(String gameIdentifier) {
		this.gameIdentifier = gameIdentifier;
		this.foregroundFPS = 0;
		this.backgroundFPS = 0;
	}
}
