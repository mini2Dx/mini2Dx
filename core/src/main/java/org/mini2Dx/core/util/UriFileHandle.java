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
package org.mini2Dx.core.util;

import org.mini2Dx.core.exception.MdxException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Wraps Gdx.files with a URI scheme
 */
public class UriFileHandle {

	/**
	 * Returns a {@link FileHandle} for a URI
	 * e.g. internal://textures/sprite.png
	 * 
	 * @param uri The file path with a URI. Valid URIs are: internal://, external://, absolute://, classpath://, local://
	 * @return A {@link FileHandle} for the file based on the path and URI
	 */
	public static FileHandle get(String uri) {
		if(uri == null) {
			throw new NullPointerException("Cannot pass null to " + UriFileHandle.class.getSimpleName() + ".get(String uri)");
		}
		String [] components = uri.split(":\\/\\/");
		if(components.length < 2) {
			throw new MdxException("No URI specified. Options are internal://, external://, absolute://, classpath://, local://");
		}
		switch(components[0]) {
		case "internal":
			return Gdx.files.internal(components[1]);
		case "external":
			return Gdx.files.external(components[1]);
		case "absolute":
			return Gdx.files.absolute(components[1]);
		case "classpath":
			return Gdx.files.classpath(components[1]);
		case "local":
			return Gdx.files.local(components[1]);
		default:
			throw new MdxException("Invalid URI specified. Options are internal://, external://, absolute://, classpath://, local://");
		}
	}
}
