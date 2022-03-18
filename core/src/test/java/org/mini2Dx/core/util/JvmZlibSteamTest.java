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

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Random;
import java.util.zip.DeflaterOutputStream;

public class JvmZlibSteamTest {
	private static final int TOTAL_BYTES = 1048576;

	@Test
	public void testDecompress() throws IOException {
		final byte [] expected = new byte[TOTAL_BYTES];
		final Random random = new Random();

		for(int i = 0; i < expected.length; i++) {
			expected[i] = (byte) random.nextInt();
		}

		final File tmpFile = File.createTempFile("test", "test");
		writeByteArray(expected, tmpFile);

		final byte [] result = readByteArray(tmpFile, false);
		Assert.assertTrue(Arrays.equals(expected, result));
	}

	@Test
	public void testDecompressInputStream() throws IOException {
		final byte [] expected = new byte[TOTAL_BYTES];
		final Random random = new Random();

		for(int i = 0; i < expected.length; i++) {
			expected[i] = (byte) random.nextInt();
		}

		final File tmpFile = File.createTempFile("test", "test");
		writeByteArray(expected, tmpFile);

		final byte [] result = readByteArray(tmpFile, true);
		Assert.assertTrue(Arrays.equals(expected, result));
	}

	private byte [] readByteArray(File tmpFile, boolean useZlibInputStream) throws IOException {
		final byte [] bytes = Files.readAllBytes(tmpFile.toPath());
		final JvmZlibStream zlibStream = new JvmZlibStream(bytes);

		final byte [] result = new byte[TOTAL_BYTES];
		if(useZlibInputStream) {
			final ZlibInputStream inputStream = new ZlibInputStream(zlibStream);
			inputStream.read(result);
		} else {
			zlibStream.read(result);
		}
		return result;
	}

	private void writeByteArray(byte [] data, File tmpFile) throws IOException {
		final DataOutputStream outputStream = new DataOutputStream(
				new DeflaterOutputStream(new BufferedOutputStream(
						new FileOutputStream(tmpFile))));
		outputStream.write(data);
		outputStream.flush();
		outputStream.close();
	}
}
