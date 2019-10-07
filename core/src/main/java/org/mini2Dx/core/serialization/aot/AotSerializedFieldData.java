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
import org.mini2Dx.core.serialization.AotSerializationData;
import org.mini2Dx.gdx.utils.Array;

import java.io.PrintWriter;
import java.util.Objects;
import java.util.Scanner;

/**
 * Utility class for storing field (and associated generics) information ahead of time
 */
public class AotSerializedFieldData {
	private final String fieldName;
	private final Field field;
	private final Array<Class> elementTypes = new Array<Class>();

	public AotSerializedFieldData(Class ownerClass, Field field) {
		this.fieldName = field.getName();
		this.field = field;

		if(!field.getType().equals(ownerClass)) {
			AotSerializationData.registerClass(field.getType());
		}

		for(int i = 0; i < field.getTotalElementTypes(); i++) {
			Class elementClass = field.getElementType(i);
			elementTypes.add(elementClass);

			if(!elementClass.equals(ownerClass)) {
				AotSerializationData.registerClass(elementClass);
			}
		}
	}

	public AotSerializedFieldData(Class clazz, Scanner scanner) throws ClassNotFoundException {
		final String [] fieldInfo = scanner.nextLine().split(",");
		this.fieldName = fieldInfo[0];
		this.field = Mdx.reflect.getDeclaredField(clazz, fieldName);

		final int totalElementTypes = Integer.parseInt(fieldInfo[1].trim());
		for(int i = 0; i < totalElementTypes; i++) {
			elementTypes.add(Class.forName(fieldInfo[2 + i]));
		}
	}

	public void saveTo(PrintWriter writer) {
		final StringBuilder result = new StringBuilder();
		result.append(fieldName);
		result.append(',');
		result.append(field.getTotalElementTypes());

		for(int i = 0; i < field.getTotalElementTypes(); i++) {
			result.append(',');
			result.append(field.getElementType(i).getName());
		}
		writer.println(result);
	}

	public String getFieldName() {
		return fieldName;
	}

	public Field getField() {
		return field;
	}

	public int getTotalElementTypes() {
		return elementTypes.size;
	}

	public Class getElementType(int index) {
		return elementTypes.get(index);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AotSerializedFieldData fieldData = (AotSerializedFieldData) o;
		return Objects.equals(fieldName, fieldData.fieldName) &&
				Objects.equals(elementTypes, fieldData.elementTypes);
	}

	@Override
	public int hashCode() {
		return Objects.hash(fieldName, elementTypes);
	}
}
