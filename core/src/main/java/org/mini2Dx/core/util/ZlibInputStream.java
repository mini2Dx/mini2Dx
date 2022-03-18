/*******************************************************************************
 * Copyright 2022 See AUTHORS file
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
package org.mini2Dx.core.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * Wraps a {@link ZlibStream} as an {@link InputStream}
 */
public class ZlibInputStream extends InputStream {
	private final ZlibStream zlibStream;

	private final byte [] buffer = new byte[2048];
	private int bufferLength = buffer.length;
	private int bufferIndex = buffer.length;

	public ZlibInputStream(ZlibStream zlibStream) {
		this.zlibStream = zlibStream;
	}

	@Override
	public int read() throws IOException {
		if(bufferLength == 0) {
			return -1;
		}
		if(!fillBuffer()) {
			return -1;
		}
		return buffer[bufferIndex++] & 0xff;
	}

	private boolean fillBuffer() {
		if(bufferIndex < bufferLength) {
			return true;
		}
		bufferLength = zlibStream.read(buffer);
		if(bufferLength == 0) {
			return false;
		}
		bufferIndex = 0;
		return true;
	}
}
