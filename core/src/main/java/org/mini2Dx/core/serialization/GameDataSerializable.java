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
package org.mini2Dx.core.serialization;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Interface for implementing serializable classes which read/write in binary formats
 */
public interface GameDataSerializable {

	/**
	 * Serializes this object to a {@link DataOutputStream}
	 * @param outputStream The {@link DataOutputStream} to write to
	 * @throws IOException Thrown if an error occurs during I/O
	 */
	public void writeData(DataOutputStream outputStream) throws IOException;

	/**
	 * Deserializes this object from a {@link DataInputStream}
	 * @param inputStream The {@link DataInputStream} to read from
	 * @throws IOException Thrown if an error occurs during I/O
	 */
	public void readData(DataInputStream inputStream) throws IOException;
}