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

import org.mini2Dx.core.exception.SerializationException;
import org.mini2Dx.core.files.FileHandle;

/**
 * Serializes objects to/from JSON based on
 * {@link org.mini2Dx.core.serialization.annotation.Field} annotations
 */
public interface JsonSerializer {
	/**
	 * Reads a JSON document and converts it into an object of the specified
	 * type
	 *
	 * @param fileHandle
	 *            The {@link FileHandle} for the JSON document
	 * @param clazz
	 *            The {@link Class} to convert the document to
	 * @return The object deserialized from JSON
	 * @throws SerializationException
	 *             Thrown when the data is invalid
	 */
	public <T> T fromJson(FileHandle fileHandle, Class<T> clazz) throws SerializationException;

	/**
	 * Reads a JSON document and converts it into an object of the specified
	 * type
	 *
	 * @param json
	 *            The JSON document
	 * @param clazz
	 *            The {@link Class} to convert the document to
	 * @return The object deserialized from JSON
	 * @throws SerializationException
	 *             Thrown when the data is invalid
	 */
	public <T> T fromJson(String json, Class<T> clazz) throws SerializationException;

	/**
	 * Writes a JSON document by searching the object for
	 * {@link org.mini2Dx.core.serialization.annotation.Field} annotations
	 *
	 * @param fileHandle
	 *            The {@link FileHandle} to write to
	 * @param object
	 *            The object to convert to JSON
	 * @throws SerializationException
	 *             Thrown when the object is invalid
	 */
	public <T> void toJson(FileHandle fileHandle, T object) throws SerializationException;

	/**
	 * Writes a JSON document by searching the object for
	 * {@link org.mini2Dx.core.serialization.annotation.Field} annotations
	 *
	 * @param fileHandle
	 *            The {@link FileHandle} to write to
	 * @param object
	 *            The object to convert to JSON
	 * @param prettyPrint
	 *            Set to true if the JSON should be prettified
	 * @throws SerializationException
	 *             Thrown when the object is invalid
	 */
	public <T> void toJson(FileHandle fileHandle, T object, boolean prettyPrint) throws SerializationException;

	/**
	 * Writes a JSON document by searching the object for
	 * {@link org.mini2Dx.core.serialization.annotation.Field} annotations
	 *
	 * @param object
	 *            The object to convert to JSON
	 * @return The object serialized as JSON
	 * @throws SerializationException
	 *             Thrown when the object is invalid
	 */
	public <T> String toJson(T object) throws SerializationException;

	/**
	 * Writes a JSON document by searching the object for
	 * {@link org.mini2Dx.core.serialization.annotation.Field} annotations
	 *
	 * @param object
	 *            The object to convert to JSON
	 * @param prettyPrint
	 *            Set to true if the JSON should be prettified
	 * @return The object serialized as JSON
	 * @throws SerializationException
	 *             Thrown when the object is invalid
	 */
	public <T> String toJson(T object, boolean prettyPrint) throws SerializationException;
}
