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
import org.mini2Dx.core.exception.SerializationException;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.serialization.GameDataOutputStream;
import org.mini2Dx.core.serialization.annotation.Field;

import java.io.*;

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
public abstract class PlayerData {
    private static final String LOGGING_TAG = PlayerData.class.getSimpleName();
    private static final int DEFAULT_WRITE_BUFFER_SIZE = 8 * 1024; //8kb

    /**
     * Returns a {@link FileHandle} for a file or directory within the save data directory
     * @param filepath The path broken up by directory/file
     * @return A {@link FileHandle} instance
     */
    protected abstract FileHandle resolve(String[] filepath);

    /**
     * Returns a {@link FileHandle} for a temporary file or directory within the save data directory
     * @param filepath The path broken up by directory/file
     * @return A {@link FileHandle} instance
     */
    protected abstract FileHandle resolveTmp(String[] filepath);

    /**
     * Internal method for setting up the save data directory (if required)
     */
    protected abstract void ensureDataDirectoryExists() throws IOException;

    /**
     * Returns if the player data storage is full
     * @return True if storage is full
     * @throws Exception Thrown if storage cannot be accessed
     */
    public abstract boolean isDataStorageFull() throws Exception;

    /**
     * Returns if the player data storage is connected
     * @return False if not connected
     */
    public abstract boolean isDataStorageConnected();

    /**
     * Wipes all data within the game data location
     *
     * @throws PlayerDataException
     *             Thrown if the game data location cannot be accessed or the
     *             data could not be wiped
     */
    public abstract void wipe() throws PlayerDataException, IOException;

    /**
     * Returns a {@link FileHandle} of a file or directory in the player data location
     * @param filepath The path to the file. This will be resolved as a path within the player data location.
     * @return A {@link FileHandle} instance for file or directory
     */
    public FileHandle getFileHandle(String... filepath){
        return resolve(filepath);
    };

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
    public <T> T readXml(Class<T> clazz, String... filepath)
            throws PlayerDataException {
        if (filepath.length == 0) {
            throw new PlayerDataException("No file path specified");
        }
        try {
            final InputStream inputStream = resolve(filepath).read();
            final T result = Mdx.xml.fromXml(new InputStreamReader(inputStream), clazz);
            inputStream.close();
            return result;
        } catch (SerializationException e) {
            throw new PlayerDataException(e);
        } catch (IOException e) {
            throw new PlayerDataException(e);
        }
    }

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
    public <T> void writeXml(T object, String... filepath)
            throws PlayerDataException {
        if (filepath.length == 0) {
            throw new PlayerDataException("No file path specified");
        }
        try {
            ensureDirectoryExistsForFile(filepath);
            StringWriter writer = new StringWriter();
            Mdx.xml.toXml(object, writer);

            FileHandle file = resolve(filepath);
            FileHandle tmpFile = resolveTmp(filepath);
            tmpFile.writeString(writer.toString(), false);
            writer.flush();
            writer.close();

            if(file.exists()) {
                file.delete();
            }
            tmpFile.moveTo(file);
        } catch (SerializationException e) {
            throw new PlayerDataException(e);
        } catch (IOException e) {
            throw new PlayerDataException(e);
        }
    }

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
    public <T> T readJson(Class<T> clazz, String... filepath)
            throws PlayerDataException {
        if (filepath.length == 0) {
            throw new PlayerDataException("No file path specified");
        }
        try {
            return Mdx.json.fromJson(resolve(filepath).readString(), clazz);
        } catch (SerializationException e) {
            throw new PlayerDataException(e);
        } catch (IOException e) {
            throw new PlayerDataException(e);
        }
    }

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
    public <T> void writeJson(T object, String... filepath)
            throws PlayerDataException {
        if (filepath.length == 0) {
            throw new PlayerDataException("No file path specified");
        }
        try {
            ensureDirectoryExistsForFile(filepath);
            FileHandle file = resolve(filepath);
            FileHandle tmpFile = resolveTmp(filepath);
            tmpFile.writeString(Mdx.json.toJson(object), false);
            if(file.exists()) {
                file.delete();
            }
            tmpFile.moveTo(file);
        } catch (SerializationException e) {
            throw new PlayerDataException(e);
        } catch (IOException e) {
            throw new PlayerDataException(e);
        }
    }

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
    public DataInputStream readBytes(String... filepath) throws PlayerDataException {
        if (filepath.length == 0) {
            throw new PlayerDataException("No file path specified");
        }
        try {
            FileHandle file = resolve(filepath);
            final InputStream inputStream = file.read();
            return new DataInputStream(new BufferedInputStream(inputStream));
        } catch (Exception e) {
            throw new PlayerDataException(e);
        }
    }

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
    public DataOutputStream writeBytes(String... filepath) throws PlayerDataException {
        return writeBytes(DEFAULT_WRITE_BUFFER_SIZE, filepath);
    }

    /**
     * Writes contents to a file in the player data location.
     * Note: Ensure that {@link DataOutputStream#close()} is called when finished writing.
     *
     * @param filepath
     *            The path to the file. This will be resolved as a path
     *            within the game data location.
     * @param bufferSize The write buffer size in bytes
     * @return A {@link DataOutputStream} to write to
     * @throws PlayerDataException
     *             Thrown if the game data location cannot be accessed or the
     *             data cannot be written to the file.
     */
    public DataOutputStream writeBytes(int bufferSize, String... filepath) throws PlayerDataException {
        if (filepath.length == 0) {
            throw new PlayerDataException("No file path specified");
        }
        try {
            ensureDirectoryExistsForFile(filepath);
            final FileHandle file = resolve(filepath);
            final FileHandle tmpFile = resolveTmp(filepath);
            final OutputStream outputStream = tmpFile.write(false);
            final GameDataOutputStream result = new GameDataOutputStream(new BufferedOutputStream(outputStream, bufferSize));
            result.setCloseListener(new GameDataOutputStream.CloseListener() {
                @Override
                public void onClose() {
                    try {
                        if(file.exists()) {
                            file.delete();
                        }
                        tmpFile.moveTo(file);
                    } catch (Exception e) {
                        Mdx.log.error(LOGGING_TAG, e.getMessage(), e);
                    }
                }
            });
            return result;
        } catch (Exception e) {
            throw new PlayerDataException(e);
        }
    }

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
    public String readString(String... filepath) throws PlayerDataException {
        if (filepath.length == 0) {
            throw new PlayerDataException("No file path specified");
        }
        try {
            FileHandle file = resolve(filepath);
            return file.readString();
        } catch (Exception e) {
            throw new PlayerDataException(e);
        }
    }

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
    public void writeString(String content, String... filepath)
            throws PlayerDataException {
        if (filepath.length == 0) {
            throw new PlayerDataException("No file path specified");
        }
        try {
            ensureDirectoryExistsForFile(filepath);
            FileHandle file = resolve(filepath);
            FileHandle tmpFile = resolveTmp(filepath);
            tmpFile.writeString(content, false);
            if(file.exists()) {
                file.delete();
            }
            tmpFile.moveTo(file);
        } catch (Exception e) {
            throw new PlayerDataException(e);
        }
    }

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
    public boolean delete(String... path) throws PlayerDataException {
        if (path.length == 0) {
            throw new PlayerDataException("No file path specified");
        }
        FileHandle file = resolve(path);
        try {
            if(file.isDirectory()) {
                return file.deleteDirectory();
            }
            return file.delete();
        } catch (IOException e) {
            Mdx.log.error(LOGGING_TAG, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Checks if the file exists in the game data location
     *
     * @param filepath
     *            The path to the file within the game data location
     * @return True if the file exists
     * @throws PlayerDataException
     *             Thrown if the game data location cannot be accessed
     */
    public boolean hasFile(String... filepath) throws PlayerDataException {
        if (filepath.length == 0) {
            throw new PlayerDataException("No file path specified");
        }
        FileHandle file = resolve(filepath);
        if (file.exists()) {
            return !file.isDirectory();
        }
        return false;
    }

    /**
     * Checks if the directory exists in the game data location
     *
     * @param path
     *            The path to the directory within the game data location
     * @return True if the file exists
     * @throws PlayerDataException
     *             Thrown if the game data location cannot be accessed
     */
    public boolean hasDirectory(String... path) throws PlayerDataException {
        if (path.length == 0) {
            throw new PlayerDataException("No path specified");
        }
        FileHandle directoryHandle = resolve(path);
        if (directoryHandle.exists()) {
            return directoryHandle.isDirectory();
        }
        return false;
    }

    /**
     * Creates a directory within in the game data location
     *
     * @param path
     *            The path to the directory within the game data location
     * @throws PlayerDataException
     *             Thrown if the game data location cannot be accessed or the
     *             directory could not be created
     */
    public void createDirectory(String... path) throws PlayerDataException {
        if (path.length == 0) {
            throw new PlayerDataException("No path specified");
        }
        FileHandle directory = resolve(path);
        if (directory.exists()) {
            return;
        }

        try {
            ensureDataDirectoryExists();
            directory.mkdirs();
        } catch (Exception e) {
            throw new PlayerDataException(e);
        }
    }

    protected void ensureDirectoryExistsForFile(String... filepath) throws IOException, PlayerDataException {
        ensureDataDirectoryExists();

        FileHandle file = resolve(filepath);
        if (file.exists()) {
            return;
        }
        FileHandle parent = file.parent();
        if (parent.exists()) {
            return;
        }
        parent.mkdirs();
    }
}
