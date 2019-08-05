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
package org.mini2Dx.libgdx.files;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.files.FileType;

import java.io.*;

public class LibgdxFileHandle implements FileHandle {
	public final com.badlogic.gdx.files.FileHandle fileHandle;

	public LibgdxFileHandle(com.badlogic.gdx.files.FileHandle fileHandle) {
		this.fileHandle = fileHandle;
	}

	@Override
	public String path() {
		return fileHandle.path();
	}

	@Override
	public String normalize() {
		String path = path();
		while(path.contains("..")) {
			path = path.replaceAll("[^\\/]+\\/\\.\\.\\/", "");
		}
		while(path.contains("./")) {
			path = path.replaceAll("\\.\\/", "");
		}
		return path;
	}

	@Override
	public FileHandle normalizedHandle() {
		switch(type())
		{
		default:
		case INTERNAL:
			return Mdx.files.internal(normalize());
		case EXTERNAL:
			return Mdx.files.external(normalize());
		case LOCAL:
			return Mdx.files.local(normalize());
		}
	}

	@Override
	public String name() {
		return fileHandle.name();
	}

	@Override
	public String extension() {
		return fileHandle.extension();
	}

	@Override
	public String nameWithoutExtension() {
		return fileHandle.nameWithoutExtension();
	}

	@Override
	public String pathWithoutExtension() {
		return fileHandle.pathWithoutExtension();
	}

	@Override
	public FileType type() {
		switch(fileHandle.type()) {
		default:
		case Classpath:
		case Internal:
			return FileType.INTERNAL;
		case External:
		case Absolute:
			return FileType.EXTERNAL;
		case Local:
			return FileType.LOCAL;
		}
	}

	@Override
	public InputStream read() throws IOException {
		return fileHandle.read();
	}

	@Override
	public BufferedInputStream read(int bufferSize) throws IOException {
		return fileHandle.read(bufferSize);
	}

	@Override
	public Reader reader() throws IOException {
		return fileHandle.reader();
	}

	@Override
	public Reader reader(String charset) throws IOException {
		return fileHandle.reader(charset);
	}

	@Override
	public BufferedReader reader(int bufferSize) throws IOException {
		return fileHandle.reader(bufferSize);
	}

	@Override
	public BufferedReader reader(int bufferSize, String charset) throws IOException {
		return fileHandle.reader(bufferSize, charset);
	}

	@Override
	public String readString() throws IOException {
		return fileHandle.readString();
	}

	@Override
	public String readString(String charset) throws IOException {
		return fileHandle.readString(charset);
	}

	@Override
	public byte[] readBytes() throws IOException {
		return fileHandle.readBytes();
	}

	@Override
	public int readBytes(byte[] bytes, int offset, int size) throws IOException {
		return fileHandle.readBytes(bytes, offset, size);
	}

	@Override
	public OutputStream write(boolean append) throws IOException {
		return fileHandle.write(append);
	}

	@Override
	public OutputStream write(boolean append, int bufferSize) throws IOException {
		return fileHandle.write(append, bufferSize);
	}

	@Override
	public void write(InputStream input, boolean append) throws IOException {
		fileHandle.write(input, append);
	}

	@Override
	public Writer writer(boolean append) throws IOException {
		return fileHandle.writer(append);
	}

	@Override
	public Writer writer(boolean append, String charset) throws IOException {
		return fileHandle.writer(append, charset);
	}

	@Override
	public void writeString(String string, boolean append) throws IOException {
		fileHandle.writeString(string, append);
	}

	@Override
	public void writeString(String string, boolean append, String charset) throws IOException {
		fileHandle.writeString(string, append, charset);
	}

	@Override
	public void writeBytes(byte[] bytes, boolean append) throws IOException {
		fileHandle.writeBytes(bytes, append);
	}

	@Override
	public void writeBytes(byte[] bytes, int offset, int length, boolean append) throws IOException {
		fileHandle.writeBytes(bytes, offset, length, append);
	}

    private static FileHandle[] gdxFileArrayToMdxHandles(com.badlogic.gdx.files.FileHandle[] gdxList) {
        FileHandle[] list = new FileHandle[gdxList.length];
        for (int i = 0; i < list.length; i++) {
            list[i] = new LibgdxFileHandle(gdxList[i]);
        }
        return list;
    }

	@Override
	public FileHandle[] list() throws IOException {
        com.badlogic.gdx.files.FileHandle[] gdxList = fileHandle.list();
        return gdxFileArrayToMdxHandles(gdxList);
	}

	@Override
	public FileHandle[] list(FileFilter filter) throws IOException {
        com.badlogic.gdx.files.FileHandle[] gdxList = fileHandle.list(filter);
        return gdxFileArrayToMdxHandles(gdxList);
	}

	@Override
	public FileHandle[] list(FilenameFilter filter) throws IOException {
        com.badlogic.gdx.files.FileHandle[] gdxList = fileHandle.list(filter);
        return gdxFileArrayToMdxHandles(gdxList);
	}

	@Override
	public FileHandle[] list(String suffix) throws IOException {
        com.badlogic.gdx.files.FileHandle[] gdxList = fileHandle.list(suffix);
        return gdxFileArrayToMdxHandles(gdxList);
	}

	@Override
	public boolean isDirectory() {
		return fileHandle.isDirectory();
	}

	@Override
	public FileHandle child(String name) {
		return new LibgdxFileHandle(fileHandle.child(name));
	}

	@Override
	public FileHandle sibling(String name) {
		return new LibgdxFileHandle(fileHandle.sibling(name));
	}

	@Override
	public FileHandle parent() {
		return new LibgdxFileHandle(fileHandle.parent());
	}

	@Override
	public void mkdirs() throws IOException {
		fileHandle.mkdirs();
	}

	@Override
	public boolean exists() {
		return fileHandle.exists();
	}

	@Override
	public boolean delete() throws IOException {
		return fileHandle.delete();
	}

	@Override
	public boolean deleteDirectory() throws IOException {
		return fileHandle.deleteDirectory();
	}

	@Override
	public void emptyDirectory() throws IOException {
		fileHandle.emptyDirectory();
	}

	@Override
	public void emptyDirectory(boolean preserveTree) throws IOException {
		fileHandle.emptyDirectory(preserveTree);
	}

	@Override
	public void copyTo(FileHandle dest) throws IOException {
		final LibgdxFileHandle gdxDest = (LibgdxFileHandle) dest;
		fileHandle.copyTo(gdxDest.fileHandle);
	}

	@Override
	public void moveTo(FileHandle dest) throws IOException {
		final LibgdxFileHandle gdxDest = (LibgdxFileHandle) dest;
		fileHandle.moveTo(gdxDest.fileHandle);
	}

	@Override
	public long length() {
		return fileHandle.length();
	}

	@Override
	public long lastModified() {
		return fileHandle.lastModified();
	}
}
