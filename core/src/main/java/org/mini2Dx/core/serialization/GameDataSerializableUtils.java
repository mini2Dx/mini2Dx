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
package org.mini2Dx.core.serialization;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class GameDataSerializableUtils {

	public static void writeArray(long [] array, DataOutputStream outputStream) throws IOException {
		outputStream.writeInt(array.length);
		for(int i = 0; i < array.length; i++) {
			outputStream.writeLong(array[i]);
		}
	}

	public static long [] readArray(DataInputStream inputStream) throws IOException {
		final int length = inputStream.readInt();
		final long [] result = new long[length];
		for(int i = 0; i < result.length; i++) {
			result[i] = inputStream.readLong();
		}
		return result;
	}

	public static void writeString(String str, DataOutputStream outputStream) throws IOException {
		outputStream.writeBoolean(str != null);
		if(str != null) {
			outputStream.writeUTF(str);
		}
	}

	public static String readString(DataInputStream inputStream) throws IOException {
		final boolean exists = inputStream.readBoolean();
		if(!exists) {
			return null;
		}
		return inputStream.readUTF();
	}
}
