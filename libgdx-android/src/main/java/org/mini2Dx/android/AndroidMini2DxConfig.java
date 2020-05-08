/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.android;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

/**
 * Extends {@link AndroidApplicationConfiguration} to include mini2Dx options and defaults
 */
public class AndroidMini2DxConfig extends AndroidApplicationConfiguration {
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
	
	public AndroidMini2DxConfig(String gameIdentifier) {
		this.gameIdentifier = gameIdentifier;
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
