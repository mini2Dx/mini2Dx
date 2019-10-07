/*******************************************************************************
 * Copyright 2019 See AUTHORS file
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

import org.mini2Dx.core.Mdx;

import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class JvmZlibStream implements ZlibStream {
	private static final String LOGGING_TAG = JvmZlibStream.class.getSimpleName();

	private final Inflater inflater;

	public JvmZlibStream(byte [] compressedData) {
		inflater = new Inflater();
		inflater.setInput(compressedData, 0, compressedData.length);
	}

	@Override
	public void read(byte[] buffer) {
		try {
			inflater.inflate(buffer, 0, buffer.length);
		} catch (DataFormatException e) {
			Mdx.log.error(LOGGING_TAG, e.getMessage(), e);
		}
	}

	@Override
	public void dispose() {
		inflater.end();
	}
}
