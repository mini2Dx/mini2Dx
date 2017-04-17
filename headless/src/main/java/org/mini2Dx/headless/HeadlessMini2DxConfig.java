/**
 * Copyright (c) 2016 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.headless;

import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;


/**
 * Extends {@link HeadlessApplicationConfiguration} to include mini2Dx options and defaults
 */
public class HeadlessMini2DxConfig extends HeadlessApplicationConfiguration {
	public final String gameIdentifier;
	/**
	 * The target framerate
	 */
	public int targetFPS = 60;
	/**
	 * The target timestep
	 */
	public float targetTimestep = (1f / targetFPS);
	
	/**
	 * The mocked window width
	 */
	public int width = 640;
	
	/**
	 * The mocked window height
	 */
	public int height = 480;
	
	/**
	 * True if the game should execute
	 */
	public boolean runGame = true;
	
	public HeadlessMini2DxConfig(String gameIdentifier) {
		this.gameIdentifier = gameIdentifier;
	}
}
