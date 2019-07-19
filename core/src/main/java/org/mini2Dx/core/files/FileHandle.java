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
package org.mini2Dx.core.files;

import java.io.*;

public interface FileHandle {

	/**
	 * @return the path of the file as specified on construction, e.g. Gdx.files.internal("dir/file.png") -&gt; dir/file.png.
	 * backward slashes will be replaced by forward slashes.
	 */
	public String path();

	/**
	 * Returns the path with redundant path elements removed (i.e. dir/dir2/../file.png -&gt; dir/file.png)
	 *
	 * @return
	 */
	public String normalize();

	/**
	 * Returns this {@link FileHandle} resolved with a normalised path
	 * @return
	 */
	public FileHandle normalizedHandle();

	/**
	 * @return the name of the file, without any parent paths.
	 */
	public String name();

	public String extension();

	/**
	 * @return the name of the file, without parent paths or the extension.
	 */
	public String nameWithoutExtension();

	/**
	 * @return the path and filename without the extension, e.g. dir/dir2/file.png -&gt; dir/dir2/file. backward slashes will be
	 * returned as forward slashes.
	 */
	public String pathWithoutExtension();

	public FileType type();

	/**
	 * Returns a stream for reading this file as bytes.
	 *
	 * @throws IOException if the file handle represents a directory, doesn't exist, or could not be read.
	 */
	public InputStream read() throws IOException;

	/**
	 * Returns a buffered stream for reading this file as bytes.
	 *
	 * @throws IOException if the file handle represents a directory, doesn't exist, or could not be read.
	 */
	public BufferedInputStream read(int bufferSize) throws IOException;

	/**
	 * Returns a reader for reading this file as characters.
	 *
	 * @throws IOException if the file handle represents a directory, doesn't exist, or could not be read.
	 */
	public Reader reader() throws IOException;

	/**
	 * Returns a reader for reading this file as characters.
	 *
	 * @throws IOException if the file handle represents a directory, doesn't exist, or could not be read.
	 */
	public Reader reader(String charset) throws IOException;

	/**
	 * Returns a buffered reader for reading this file as characters.
	 *
	 * @throws IOException if the file handle represents a directory, doesn't exist, or could not be read.
	 */
	public BufferedReader reader(int bufferSize) throws IOException;

	/**
	 * Returns a buffered reader for reading this file as characters.
	 *
	 * @throws IOException if the file handle represents a directory, doesn't exist, or could not be read.
	 */
	public BufferedReader reader(int bufferSize, String charset) throws IOException;

	/**
	 * Reads the entire file into a string using the platform's default charset.
	 *
	 * @throws IOException if the file handle represents a directory, doesn't exist, or could not be read.
	 */
	public String readString() throws IOException;

	/**
	 * Reads the entire file into a string using the specified charset.
	 *
	 * @param charset If null the default charset is used.
	 * @throws IOException if the file handle represents a directory, doesn't exist, or could not be read.
	 */
	public String readString(String charset) throws IOException;

	/**
	 * Reads the entire file into a byte array.
	 *
	 * @throws IOException if the file handle represents a directory, doesn't exist, or could not be read.
	 */
	public byte[] readBytes() throws IOException;

	/**
	 * Reads the entire file into the byte array. The byte array must be big enough to hold the file's data.
	 *
	 * @param bytes  the array to load the file into
	 * @param offset the offset to start writing bytes
	 * @param size   the number of bytes to read, see {@link #length()}
	 * @return the number of read bytes
	 */
	public int readBytes(byte[] bytes, int offset, int size) throws IOException;

	/**
	 * Returns a stream for writing to this file. Parent directories will be created if necessary.
	 *
	 * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
	 * @throws IOException if this file handle represents a directory, if it is a {@link FileType#INTERNAL} file, or if it could not be written.
	 */
	public OutputStream write(boolean append) throws IOException;

	/**
	 * Returns a buffered stream for writing to this file. Parent directories will be created if necessary.
	 *
	 * @param append     If false, this file will be overwritten if it exists, otherwise it will be appended.
	 * @param bufferSize The size of the buffer.
	 * @throws IOException if this file handle represents a directory, if it is a {@link FileType#INTERNAL} file, or if it could not be written.
	 */
	public OutputStream write(boolean append, int bufferSize) throws IOException;

	/**
	 * Reads the remaining bytes from the specified stream and writes them to this file. The stream is closed. Parent directories
	 * will be created if necessary.
	 *
	 * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
	 * @throws IOException if this file handle represents a directory, if it is a {@link FileType#INTERNAL} file, or if it could not be written.
	 */
	public void write(InputStream input, boolean append) throws IOException;

	/**
	 * Returns a writer for writing to this file using the default charset. Parent directories will be created if necessary.
	 *
	 * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
	 * @throws IOException if this file handle represents a directory, if it is a {@link FileType#INTERNAL} file, or if it could not be written.
	 */
	public Writer writer(boolean append) throws IOException;

	/**
	 * Returns a writer for writing to this file. Parent directories will be created if necessary.
	 *
	 * @param append  If false, this file will be overwritten if it exists, otherwise it will be appended.
	 * @param charset May be null to use the default charset.
	 * @throws IOException if this file handle represents a directory, if it is a {@link FileType#INTERNAL} file, or if it could not be written.
	 */
	public Writer writer(boolean append, String charset) throws IOException;

	/**
	 * Writes the specified string to the file using the default charset. Parent directories will be created if necessary.
	 *
	 * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
	 * @throws IOException if this file handle represents a directory, if it is a {@link FileType#INTERNAL} file, or if it could not be written.
	 */
	public void writeString(String string, boolean append) throws IOException;

	/**
	 * Writes the specified string to the file using the specified charset. Parent directories will be created if necessary.
	 *
	 * @param append  If false, this file will be overwritten if it exists, otherwise it will be appended.
	 * @param charset May be null to use the default charset.
	 * @throws IOException if this file handle represents a directory, if it is a {@link FileType#INTERNAL} file, or if it could not be written.
	 */
	public void writeString(String string, boolean append, String charset) throws IOException;

	/**
	 * Writes the specified bytes to the file. Parent directories will be created if necessary.
	 *
	 * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
	 * @throws IOException if this file handle represents a directory, if it is a {@link FileType#INTERNAL} file, or if it could not be written.
	 */
	public void writeBytes(byte[] bytes, boolean append) throws IOException;

	/**
	 * Writes the specified bytes to the file. Parent directories will be created if necessary.
	 *
	 * @param append If false, this file will be overwritten if it exists, otherwise it will be appended.
	 * @throws IOException if this file handle represents a directory, if it is a {@link FileType#INTERNAL} file, or if it could not be written.
	 */
	public void writeBytes(byte[] bytes, int offset, int length, boolean append) throws IOException;

	/**
	 * Returns the paths to the children of this directory. Returns an empty list if this file handle represents a file and not a
	 * directory. On the desktop, an {@link FileType#INTERNAL} handle to a directory on the classpath will return a zero length
	 * array.
	 */
	public FileHandle[] list() throws IOException;

	/**
	 * Returns the paths to the children of this directory that satisfy the specified filter. Returns an empty list if this file
	 * handle represents a file and not a directory. On the desktop, an {@link FileType#INTERNAL} handle to a directory on the
	 * classpath will return a zero length array.
	 *
	 * @param filter the {@link FileFilter} to filter files
	 */
	public FileHandle[] list(FileFilter filter) throws IOException;

	/**
	 * Returns the paths to the children of this directory that satisfy the specified filter. Returns an empty list if this file
	 * handle represents a file and not a directory. On the desktop, an {@link FileType#INTERNAL} handle to a directory on the
	 * classpath will return a zero length array.
	 *
	 * @param filter the {@link FilenameFilter} to filter files
	 */
	public FileHandle[] list(FilenameFilter filter) throws IOException;

	/**
	 * Returns the paths to the children of this directory with the specified suffix. Returns an empty list if this file handle
	 * represents a file and not a directory. On the desktop, an {@link FileType#INTERNAL} handle to a directory on the classpath
	 * will return a zero length array.
	 */
	public FileHandle[] list(String suffix) throws IOException;

	/**
	 * Returns true if this file is a directory. Always returns false for classpath files. On Android, an
	 * {@link FileType#INTERNAL} handle to an empty directory will return false. On the desktop, an {@link FileType#INTERNAL}
	 * handle to a directory on the classpath will return false.
	 */
	public boolean isDirectory();

	/**
	 * Returns a handle to the child with the specified name.
	 */
	public FileHandle child(String name);

	/**
	 * Returns a handle to the sibling with the specified name.
	 */
	public FileHandle sibling(String name);

	public FileHandle parent();

	/**
	 * @throws IOException if this file handle is a {@link FileType#INTERNAL} file.
	 */
	public void mkdirs() throws IOException;

	/**
	 * Returns true if the file exists. On Android, a {@link FileType#INTERNAL} handle to a
	 * directory will always return false. Note that this can be very slow for internal files on Android!
	 */
	public boolean exists();

	/**
	 * Deletes this file or empty directory and returns success. Will not delete a directory that has children.
	 *
	 * @throws IOException if this file handle is a {@link FileType#INTERNAL} file.
	 */
	public boolean delete() throws IOException;

	/**
	 * Deletes this file or directory and all children, recursively.
	 *
	 * @throws IOException if this file handle is a {@link FileType#INTERNAL} file.
	 */
	public boolean deleteDirectory() throws IOException;

	/**
	 * Deletes all children of this directory, recursively.
	 *
	 * @throws IOException if this file handle is a {@link FileType#INTERNAL} file.
	 */
	public void emptyDirectory() throws IOException;

	/**
	 * Deletes all children of this directory, recursively. Optionally preserving the folder structure.
	 *
	 * @throws IOException if this file handle is a {@link FileType#INTERNAL} file.
	 */
	public void emptyDirectory(boolean preserveTree) throws IOException;

	/**
	 * Copies this file or directory to the specified file or directory. If this handle is a file, then 1) if the destination is a
	 * file, it is overwritten, or 2) if the destination is a directory, this file is copied into it, or 3) if the destination
	 * doesn't exist, {@link #mkdirs()} is called on the destination's parent and this file is copied into it with a new name. If
	 * this handle is a directory, then 1) if the destination is a file, IOException is thrown, or 2) if the destination is
	 * a directory, this directory is copied into it recursively, overwriting existing files, or 3) if the destination doesn't
	 * exist, {@link #mkdirs()} is called on the destination and this directory is copied into it recursively.
	 *
	 * @throws IOException if the destination file handle is a {@link FileType#INTERNAL}
	 *                     file, or copying failed.
	 */
	public void copyTo(FileHandle dest) throws IOException;

	/**
	 * Moves this file to the specified file, overwriting the file if it already exists.
	 *
	 * @throws IOException if the source or destination file handle is a {@link FileType#INTERNAL} file.
	 */
	public void moveTo(FileHandle dest) throws IOException;

	/**
	 * Returns the length in bytes of this file, or 0 if this file is a directory, does not exist, or the size cannot otherwise be
	 * determined.
	 */
	public long length();

	/**
	 * Returns the last modified time in milliseconds for this file. Zero is returned if the file doesn't exist. Zero is returned
	 * for {@link FileType#INTERNAL} files.
	 */
	public long lastModified();
}
