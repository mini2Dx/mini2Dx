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

import android.content.Context;
import org.mini2Dx.android.di.AndroidDependencyInjection;
import org.mini2Dx.android.playerdata.AndroidPlayerData;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.libgdx.game.GameWrapper;

/**
 * Android implementation of {@link GameWrapper}
 */
public class AndroidGameWrapper extends GameWrapper {
	private final Context applicationContext;

	public AndroidGameWrapper(Context applicationContext, GameContainer gc, String gameIdentifier) {
		super(gc, gameIdentifier);
		this.applicationContext = applicationContext;
	}

	@Override
	public void initialise(String gameIdentifier) {
		Mdx.di = new AndroidDependencyInjection(applicationContext);
		Mdx.playerData = new AndroidPlayerData();
		Mdx.platformUtils = new AndroidPlatformUtils();
	}
	
	@Override
	public boolean isGameWindowReady() {
		return true;
	}
}
