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

import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.serialization.aot.AotSerializedClassData;
import org.mini2Dx.gdx.utils.ObjectMap;

import java.io.*;
import java.util.Scanner;

/**
 * Utility class for reading/writing required reflection data at compile time
 */
public class AotSerializationData {
	private static final ObjectMap<String, AotSerializedClassData> AOT_DATA = new ObjectMap<String, AotSerializedClassData>();

	public static void registerClass(Class clazz) {
		if(clazz.isPrimitive()) {
			return;
		}
		if(clazz.getName().equals("java.lang.String")) {
			return;
		}
		if(AOT_DATA.containsKey(clazz.getName())) {
			return;
		}
		final AotSerializedClassData classData = new AotSerializedClassData(clazz);
		if(classData.getTotalFields() == 0) {
			return;
		}
		AOT_DATA.put(clazz.getName(), classData);
	}

	public static AotSerializedClassData getClassData(Class clazz) {
		return AOT_DATA.get(clazz.getName(), null);
	}

	public static void saveTo(FileHandle fileHandle) throws IOException {
		saveTo(fileHandle.writer(false));
	}

	public static void saveTo(Writer output) throws IOException {
		final PrintWriter writer = new PrintWriter(output);
		for(AotSerializedClassData data : AOT_DATA.values()) {
			data.saveTo(writer);
		}
		writer.flush();
		writer.close();
	}

	public static void restoreFrom(FileHandle fileHandle) throws IOException, ClassNotFoundException {
		restoreFrom(new InputStreamReader(fileHandle.read()));
	}

	public static void restoreFrom(Reader reader) throws IOException, ClassNotFoundException {
		final Scanner scanner = new Scanner(reader);
		while(scanner.hasNext()) {
			AotSerializedClassData classData = new AotSerializedClassData(scanner);
			AOT_DATA.put(classData.getQualifiedClassName(), classData);
		}
		scanner.close();
	}

	public static void clear() {
		AOT_DATA.clear();
	}
}
