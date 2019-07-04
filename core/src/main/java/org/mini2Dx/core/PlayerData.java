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
package org.mini2Dx.core;

import org.mini2Dx.core.exception.PlayerDataException;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.serialization.annotation.Field;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Common interface for reading/writing player data, e.g. game saves, preferences,
 * etc.
 *
 * The location to save data in is automatically determined by the game
 * identifier and the current platform. All files/directories specified by the
 * developer will be automatically placed inside the pre-determined data
 * location.
 *
 * Note: Objects must use {@link Field} annotations to be
 * serialized/deserialized properly
 */
public interface PlayerData {
    /**
     * Returns a {@link FileHandle} of a file in the player data location
     * @param filepath The path to the file. This will be resolved as a path within the player data location.
     * @return
     */
    public FileHandle getFileHandle(String... filepath);

    /**
     * Reads the contents of a file in the player data location via a {@link DataInputStream}.
     * Note: Ensure that {@link DataInputStream#close()} is called when finished reading.
     *
     * @param filepath
     *            The path to the file. This will be resolved as a path
     *            within the game data location.
     * @return A {@link DataInputStream} to read from
     * @throws PlayerDataException
     *             Thrown if the file does not exist
     */
    public DataInputStream readBytes(String... filepath) throws PlayerDataException;

    /**
     * Writes contents to a file in the player data location.
     * Note: Ensure that {@link DataOutputStream#close()} is called when finished writing.
     *
     * @param filepath
     *            The path to the file. This will be resolved as a path
     *            within the game data location.
     * @return A {@link DataOutputStream} to write to
     * @throws PlayerDataException
     *             Thrown if the game data location cannot be accessed or the
     *             data cannot be written to the file.
     */
    public DataOutputStream writeBytes(String... filepath) throws PlayerDataException;

    /**
     * Reads the contents of a file in the player data location into a {@link String}
     *
     * @param filepath
     *            The path to the file. This will be resolved as a path
     *            within the game data location.
     * @return A string containing the contents of the file
     * @throws PlayerDataException
     *             Thrown if the file does not exist
     */
    public String readString(String... filepath) throws PlayerDataException;

    /**
     * Writes a {@link String} to a file in the player data location
     *
     * @param content
     *            The {@link String} to be written to the file
     * @param filepath
     *            The path to the file. This will be resolved as a path
     *            within the game data location.
     * @throws PlayerDataException
     *             Thrown if the game data location cannot be accessed or the
     *             data cannot be written to the file.
     */
    public void writeString(String content, String... filepath) throws PlayerDataException;

    /**
     * Converts XML from a file into an object. Note the object must use the
     * mini2Dx data annotations.
     *
     * @param <T> The type of {@link Class} to read
     * @param clazz
     *            The object {@link Class} to convert the XML into
     * @param filepath
     *            The path to the XML file. This will be resolved as a path
     *            within the game data location.
     * @return The resulting object
     * @throws PlayerDataException
     *             Thrown if the XML is invalid, the file does not exist or the
     *             game data location cannot be accessed.
     */
    public <T> T readXml(Class<T> clazz, String... filepath) throws PlayerDataException;

    /**
     * Writes an object as XML to a file. Note the object must use the mini2Dx
     * data annotations.
     *
     * @param <T> The type of {@link Class} to write
     * @param object
     *            The object to be written to the file
     * @param filepath
     *            The path to the XML file. This will be resolved as a path
     *            within the game data location.
     * @throws PlayerDataException
     *             Thrown if the game data location cannot be accessed or the
     *             data cannot be written to the file.
     */
    public <T> void writeXml(T object, String... filepath) throws PlayerDataException;

    /**
     * Converts JSON from a file into an object. Note the object must use the
     * mini2Dx data annotations.
     *
     * @param <T> The type of {@link Class} to read
     * @param clazz
     *            The object {@link Class} to convert the JSON into
     * @param filepath
     *            The path to the JSON file. This will be resolved as a path
     *            within the game data location.
     * @return The resulting object
     * @throws PlayerDataException
     *             Thrown if the XML is invalid, the file does not exist or the
     *             game data location cannot be accessed.
     */
    public <T> T readJson(Class<T> clazz, String... filepath) throws PlayerDataException;

    /**
     * Writes an object as JSON to a file. Note the object must use the mini2Dx
     * data annotations.
     *
     * @param <T> The type of {@link Class} to write
     * @param object
     *            The object to be written to the file
     * @param filepath
     *            The path to the JSON file. This will be resolved as a path
     *            within the game data location.
     * @throws PlayerDataException
     *             Thrown if the game data location cannot be accessed or the
     *             data cannot be written to the file.
     */
    public <T> void writeJson(T object, String... filepath) throws PlayerDataException;

    /**
     * Checks if the file exists in the game data location
     *
     * @param filepath
     *            The path to the file within the game data location
     * @return True if the file exists
     * @throws PlayerDataException
     *             Thrown if the game data location cannot be accessed
     */
    public boolean hasFile(String... filepath) throws PlayerDataException;

    /**
     * Checks if the directory exists in the game data location
     *
     * @param path
     *            The path to the directory within the game data location
     * @return True if the file exists
     * @throws PlayerDataException
     *             Thrown if the game data location cannot be accessed
     */
    public boolean hasDirectory(String... path) throws PlayerDataException;

    /**
     * Creates a directory within in the game data location
     *
     * @param path
     *            The path to the directory within the game data location
     * @throws PlayerDataException
     *             Thrown if the game data location cannot be accessed or the
     *             directory could not be created
     */
    public void createDirectory(String... path) throws PlayerDataException;

    /**
     * Deletes a file or directory within in the game data location
     *
     * @param path
     *            The path to the file or directory within the game data location
     * @throws PlayerDataException
     *             Thrown if the game data location cannot be accessed or the
     *             file/directory could not be deleted
     * @return True if the file or directory was deleted successfully
     */
    public boolean delete(String... path) throws PlayerDataException;

    /**
     * Wipes all data within the game data location
     *
     * @throws PlayerDataException
     *             Thrown if the game data location cannot be accessed or the
     *             data could not be wiped
     */
    public void wipe() throws PlayerDataException;
}
