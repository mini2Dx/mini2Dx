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
package org.mini2Dx.tiled.renderer;

import org.mini2Dx.core.serialization.GameDataSerializable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 */
public class TileFrame implements GameDataSerializable {
	float duration;
	int tileId;
	
	public TileFrame(float duration, int tileId) {
		super();
		this.duration = duration;
		this.tileId = tileId;
	}

	private TileFrame() {}

	public static TileFrame fromInputStream(DataInputStream inputStream) throws IOException {
		final TileFrame result = new TileFrame();
		result.readData(inputStream);
		return result;
	}

	public float getDuration() {
		return duration;
	}

	public int getTileId() {
		return tileId;
	}

	@Override
	public void writeData(DataOutputStream outputStream) throws IOException {
		outputStream.writeFloat(duration);
		outputStream.writeInt(tileId);
	}

	@Override
	public void readData(DataInputStream inputStream) throws IOException {
		duration = inputStream.readFloat();
		tileId = inputStream.readInt();
	}
}
