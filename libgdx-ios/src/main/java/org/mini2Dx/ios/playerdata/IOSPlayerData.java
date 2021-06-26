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
package org.mini2Dx.ios.playerdata;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.PlayerData;
import org.mini2Dx.core.exception.PlayerDataException;
import org.mini2Dx.core.exception.SerializationException;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.serialization.GameDataSerializable;

import java.io.*;
import java.util.Arrays;

/**
 * iOS implementation of {@link PlayerData}
 */
public class IOSPlayerData extends PlayerData {

	@Override
	public void wipe() throws PlayerDataException {
		FileHandle directory = Mdx.files.local("./");
		if (!directory.exists()) {
			return;
		}
		try {
			directory.emptyDirectory();
		} catch (IOException e) {
			throw new PlayerDataException(e);
		}
	}

	@Override
	public FileHandle resolve(String[] filepath) {
		String path = "";
		for (int i = 0; i < filepath.length; i++) {
			path += filepath[i];
			if (path.length() < filepath.length - 1) {
				path += "/";
			}
		}
		return Mdx.files.local(path);
	}

	@Override
	public FileHandle resolveTmp(String[] filepath) {
		final String [] tmpFilepath = Arrays.copyOf(filepath, filepath.length);
		tmpFilepath[tmpFilepath.length - 1] = tmpFilepath[tmpFilepath.length - 1] + ".tmp";
		return resolve(tmpFilepath);
	}

	@Override
	protected void ensureDataDirectoryExists() throws IOException {
		//Exists by default
	}

	@Override
	public boolean isDataStorageFull() throws Exception {
		//TODO: Implement partition querying
		return false;
	}

	@Override
	public boolean isDataStorageConnected() {
		return true;
	}
}
