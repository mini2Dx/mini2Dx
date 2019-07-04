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
package org.mini2Dx.libgdx;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.PlayerData;
import org.mini2Dx.core.exception.PlayerDataException;
import org.mini2Dx.core.exception.SerializationException;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.serialization.GameDataOutputStream;

import java.io.*;

public abstract class LibgdxPlayerData implements PlayerData {
	private static final String LOGGING_TAG = LibgdxPlayerData.class.getSimpleName();

	protected abstract FileHandle resolve(String[] filepath);

	protected abstract FileHandle resolveTmp(String[] filepath);

	protected abstract void ensureDataDirectoryExists();

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
	public DataInputStream readBytes(String... filepath) throws PlayerDataException {
		if (filepath.length == 0) {
			throw new PlayerDataException("No file path specified");
		}
		try {
			FileHandle file = resolve(filepath);
			final InputStream inputStream = file.read();
			return new DataInputStream(inputStream);
		} catch (Exception e) {
			throw new PlayerDataException(e);
		}
	}

	@Override
	public DataOutputStream writeBytes(String... filepath) throws PlayerDataException {
		if (filepath.length == 0) {
			throw new PlayerDataException("No file path specified");
		}
		try {
			ensureDirectoryExistsForFile(filepath);
			final FileHandle file = resolve(filepath);
			final FileHandle tmpFile = resolveTmp(filepath);
			final OutputStream outputStream = tmpFile.write(false);
			final GameDataOutputStream result = new GameDataOutputStream(outputStream);
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	protected void ensureDirectoryExistsForFile(String... filepath) throws IOException {
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
