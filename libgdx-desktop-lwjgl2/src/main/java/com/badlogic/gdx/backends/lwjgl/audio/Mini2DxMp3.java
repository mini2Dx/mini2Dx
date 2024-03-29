/*******************************************************************************
 * Copyright 2011 See LIBGDX_AUTHORS file.
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
package com.badlogic.gdx.backends.lwjgl.audio;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import javazoom.jl.decoder.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Modified version of {@link Mp3} to support sound completion events
 */
public class Mini2DxMp3 extends Mp3 {
	static public class Music extends Mini2DxOpenALMusic {
		// Note: This uses a slightly modified version of JLayer.

		private Bitstream bitstream;
		private OutputBuffer outputBuffer;
		private MP3Decoder decoder;

		public Music (Mini2DxOpenALAudio audio, FileHandle file) {
			super(audio, file);
			if (audio.noDevice) return;
			bitstream = new Bitstream(file.read());
			decoder = new MP3Decoder();
			bufferOverhead = 4096;
			try {
				Header header = bitstream.readFrame();
				if (header == null) throw new GdxRuntimeException("Empty MP3");
				int channels = header.mode() == Header.SINGLE_CHANNEL ? 1 : 2;
				outputBuffer = new OutputBuffer(channels, false);
				decoder.setOutputBuffer(outputBuffer);
				setup(channels, header.getSampleRate());
			} catch (BitstreamException e) {
				throw new GdxRuntimeException("error while preloading mp3", e);
			}
		}

		public Music (Mini2DxOpenALAudio audio, byte [] bytes) {
			super(audio, bytes);
			if (audio.noDevice) return;
			bitstream = new Bitstream(new ByteArrayInputStream(bytes));
			decoder = new MP3Decoder();
			bufferOverhead = 4096;
			try {
				Header header = bitstream.readFrame();
				if (header == null) throw new GdxRuntimeException("Empty MP3");
				int channels = header.mode() == Header.SINGLE_CHANNEL ? 1 : 2;
				outputBuffer = new OutputBuffer(channels, false);
				decoder.setOutputBuffer(outputBuffer);
				setup(channels, header.getSampleRate());
			} catch (BitstreamException e) {
				throw new GdxRuntimeException("error while preloading mp3", e);
			}
		}

		public int read (byte[] buffer) {
			try {
				boolean setup = bitstream == null;
				if (setup) {
					if(file != null) {
						bitstream = new Bitstream(file.read());
					} else {
						bitstream = new Bitstream(new ByteArrayInputStream(bytes));
					}
					decoder = new MP3Decoder();
				}

				int totalLength = 0;
				int minRequiredLength = buffer.length - OutputBuffer.BUFFERSIZE * 2;
				while (totalLength <= minRequiredLength) {
					Header header = bitstream.readFrame();
					if (header == null) break;
					if (setup) {
						int channels = header.mode() == Header.SINGLE_CHANNEL ? 1 : 2;
						outputBuffer = new OutputBuffer(channels, false);
						decoder.setOutputBuffer(outputBuffer);
						setup(channels, header.getSampleRate());
						setup = false;
					}
					try {
						decoder.decodeFrame(header, bitstream);
					} catch (Exception ignored) {
						// JLayer's decoder throws ArrayIndexOutOfBoundsException sometimes!?
					}
					bitstream.closeFrame();

					int length = outputBuffer.reset();
					System.arraycopy(outputBuffer.getBuffer(), 0, buffer, totalLength, length);
					totalLength += length;
				}
				return totalLength;
			} catch (Throwable ex) {
				reset();
				throw new GdxRuntimeException("Error reading audio data.", ex);
			}
		}

		public void reset () {
			if (bitstream == null) return;
			try {
				bitstream.close();
			} catch (BitstreamException ignored) {
			}
			bitstream = null;
		}
	}
	
	static public class Sound extends Mini2DxOpenALSound {
		// Note: This uses a slightly modified version of JLayer.

		public Sound (Mini2DxOpenALAudio audio, FileHandle file) {
			this(audio, file.read(), file.path());
		}

		public Sound (Mini2DxOpenALAudio audio, InputStream stream, String fileName) {
			super(audio);
			if (audio.noDevice) return;
			ByteArrayOutputStream output = new ByteArrayOutputStream(4096);

			Bitstream bitstream = new Bitstream(stream);
			MP3Decoder decoder = new MP3Decoder();

			try {
				OutputBuffer outputBuffer = null;
				int sampleRate = -1, channels = -1;
				while (true) {
					Header header = bitstream.readFrame();
					if (header == null) break;
					if (outputBuffer == null) {
						channels = header.mode() == Header.SINGLE_CHANNEL ? 1 : 2;
						outputBuffer = new OutputBuffer(channels, false);
						decoder.setOutputBuffer(outputBuffer);
						sampleRate = header.getSampleRate();
					}
					try {
						decoder.decodeFrame(header, bitstream);
					} catch (Exception ignored) {
						// JLayer's decoder throws ArrayIndexOutOfBoundsException sometimes!?
					}
					bitstream.closeFrame();
					output.write(outputBuffer.getBuffer(), 0, outputBuffer.reset());
				}
				bitstream.close();
				setup(output.toByteArray(), channels, sampleRate);
			} catch (Throwable ex) {
				throw new GdxRuntimeException("Error reading audio data.", ex);
			}
		}
	}
}
