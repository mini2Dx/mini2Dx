/**
 * Copyright 2021 Viridian Software Ltd.
 */
package org.mini2Dx.core.util;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.exception.PlayerDataException;
import org.mini2Dx.core.geom.Circle;
import org.mini2Dx.core.geom.CirclePerformanceTest;
import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Rectangle;
import org.openjdk.jmh.annotations.*;

import java.io.*;
import java.util.Random;

public class PlayerDataPerformanceTest {
	public static final int _1MB = 1048576;
	public static final int _10MB = _1MB * 10;

	@State(Scope.Thread)
	public static class ReadState {
		public File _1mbFile, _10mbFile;

		@Setup(Level.Trial)
		public void setUp() throws IOException {
			_1mbFile = File.createTempFile("readState", ".dat");
			_10mbFile = File.createTempFile("readState", ".dat");

			writeFile(_1mbFile, _1MB);
			writeFile(_10mbFile, _10MB);
		}

		private void writeFile(File file, int totalBytes) throws IOException {
			byte [] bytes = new byte[1024];
			final BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
			for(int i = 0; i < totalBytes; i += bytes.length) {
				outputStream.write(bytes);
			}
			outputStream.flush();
			outputStream.close();
		}
	}

	@State(Scope.Thread)
	public static class WriteState {
		public File file;

		@Setup(Level.Iteration)
		public void setUp() throws IOException {
			file = File.createTempFile("writeState", ".dat");
		}
	}

	@Benchmark
	@BenchmarkMode(value= Mode.AverageTime)
	@Group("PlayerData")
	public void testWrite1MB(PlayerDataPerformanceTest.WriteState state) throws IOException {
		final DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(state.file)));
		for(int i = 0; i < _1MB; i++) {
			dataOutputStream.writeByte(0);
		}
		dataOutputStream.flush();
		dataOutputStream.close();
	}

	@Benchmark
	@BenchmarkMode(value= Mode.AverageTime)
	@Group("PlayerData")
	public void testWrite10MB(PlayerDataPerformanceTest.WriteState state) throws IOException {
		final DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(state.file)));
		for(int i = 0; i < _10MB; i++) {
			dataOutputStream.writeByte(0);
		}
		dataOutputStream.flush();
		dataOutputStream.close();
	}

	@Benchmark
	@BenchmarkMode(value= Mode.AverageTime)
	@Group("PlayerData")
	public void testRead1MB(PlayerDataPerformanceTest.ReadState state) throws IOException {
		final DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(state._1mbFile)));
		for(int i = 0; i < _1MB; i++) {
			dataInputStream.readByte();
		}
		dataInputStream.close();
	}

	@Benchmark
	@BenchmarkMode(value= Mode.AverageTime)
	@Group("PlayerData")
	public void testRead10MB(PlayerDataPerformanceTest.ReadState state) throws IOException {
		final DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(state._10mbFile)));
		for(int i = 0; i < _10MB; i++) {
			dataInputStream.readByte();
		}
		dataInputStream.close();
	}
}
