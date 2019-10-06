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
package org.mini2Dx.core.serialization.aot;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.reflect.Field;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.ObjectMap;

import java.io.PrintWriter;
import java.util.Objects;
import java.util.Scanner;

/**
 * Utility class for storing class field information ahead of time
 */
public class AotSerializedClassData {
	private final String qualifiedClassName;
	private final Class clazz;
	private final Array<AotSerializedFieldData> fieldData = new Array<AotSerializedFieldData>();
	private final ObjectMap<String, AotSerializedFieldData> fieldDataByFieldName = new ObjectMap<>();

	public AotSerializedClassData(Class clazz) {
		this.clazz = clazz;
		this.qualifiedClassName = clazz.getName();

		Field[] fields = Mdx.reflect.getDeclaredFields(clazz);
		for(int i = 0; i < fields.length; i++) {
			fieldData.add(new AotSerializedFieldData(fields[i]));
		}
	}

	public AotSerializedClassData(Scanner scanner) throws ClassNotFoundException {
		this.qualifiedClassName = scanner.nextLine();
		this.clazz = Class.forName(qualifiedClassName);

		int totalFields = Integer.parseInt(scanner.nextLine().trim());
		for(int i = 0; i < totalFields; i++) {
			fieldData.add(new AotSerializedFieldData(clazz, scanner));
		}
	}

	public void saveTo(PrintWriter writer) {
		writer.println(qualifiedClassName);
		writer.println(fieldData.size);

		for(int i = 0; i < fieldData.size; i++) {
			fieldData.get(i).saveTo(writer);
		}
	}

	public String getQualifiedClassName() {
		return qualifiedClassName;
	}

	public Class getClazz() {
		return clazz;
	}

	public int getTotalFields() {
		return fieldData.size;
	}

	public AotSerializedFieldData getFieldData(int fieldIndex) {
		return fieldData.get(fieldIndex);
	}

	public AotSerializedFieldData getFieldData(String fieldName) {
		if(fieldDataByFieldName.containsKey(fieldName)) {
			return fieldDataByFieldName.get(fieldName);
		}
		for(int i = 0; i < fieldData.size; i++) {
			AotSerializedFieldData fieldData = this.fieldData.get(i);
			if(!fieldData.getFieldName().equals(fieldName)) {
				continue;
			}
			fieldDataByFieldName.put(fieldName, fieldData);
			return fieldData;
		}
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AotSerializedClassData classData = (AotSerializedClassData) o;
		return Objects.equals(qualifiedClassName, classData.qualifiedClassName) &&
				Objects.equals(fieldData, classData.fieldData);
	}

	@Override
	public int hashCode() {
		return Objects.hash(qualifiedClassName, fieldData);
	}
}
