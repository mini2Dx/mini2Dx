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
import org.mini2Dx.core.reflect.Annotation;
import org.mini2Dx.core.reflect.Constructor;
import org.mini2Dx.core.reflect.Field;
import org.mini2Dx.core.reflect.Method;
import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.core.serialization.annotation.NonConcrete;
import org.mini2Dx.core.serialization.annotation.PostDeserialize;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.ObjectMap;

import java.io.PrintWriter;
import java.util.Objects;
import java.util.Scanner;

/**
 * Utility class for storing class field information ahead of time
 */
public class AotSerializedClassData {
	private final String qualifiedClassName, postDeserializeMethodName;
	private final boolean nonConcrete;
	private final Class clazz;
	private final Array<AotSerializedConstructorData> constructorData = new Array<AotSerializedConstructorData>();
	private final Array<AotSerializedFieldData> fieldData = new Array<AotSerializedFieldData>();
	private final ObjectMap<String, AotSerializedFieldData> fieldDataByFieldName = new ObjectMap<>();

	private Field [] fieldDataAsFieldArray;

	public AotSerializedClassData(Class clazz) {
		this.clazz = clazz;
		this.qualifiedClassName = clazz.getName();

		if(Mdx.reflect.isAnnotationPresent(clazz, NonConcrete.class)) {
			nonConcrete = true;
		} else {
			nonConcrete = false;
		}

		String postDeserializeMethod = null;
		for(Method method : Mdx.reflect.getDeclaredMethods(clazz)) {
			if(method.isAnnotationPresent(PostDeserialize.class)) {
				postDeserializeMethod = method.getName();
				break;
			}
		}
		this.postDeserializeMethodName = postDeserializeMethod;

		Constructor[] constructors = Mdx.reflect.getConstructors(clazz);
		final Array<ConstructorArg> constructorArgs = new Array<ConstructorArg>();

		for(int i = 0; i < constructors.length; i++) {
			constructorArgs.clear();

			for (int j = 0; j < constructors[i].getParameterAnnotations().length; j++) {
				ConstructorArg constructorArg = null;
				final Annotation[] annotations = constructors[i].getParameterAnnotations()[j];
				for (int k = 0; k < annotations.length; k++) {
					if (!annotations[k].getAnnotationType().isAssignableFrom(ConstructorArg.class)) {
						continue;
					}
					Annotation annotation = annotations[k];
					if(annotation == null) {
						continue;
					}
					constructorArg = (ConstructorArg) annotation.getAnnotation(ConstructorArg.class);
					break;
				}
				if(constructorArg == null) {
					constructorArgs.clear();
					break;
				}
				constructorArgs.add(constructorArg);
			}

			if(constructorArgs.size == 0) {
				continue;
			}
			if(constructorArgs.size != constructors[i].getParameterTypes().length) {
				continue;
			}

			constructorData.add(new AotSerializedConstructorData(clazz, constructors[i], constructorArgs));
		}

		Field[] fields = Mdx.reflect.getDeclaredFields(clazz);
		for(int i = 0; i < fields.length; i++) {
			if(!fields[i].isAnnotationPresent(org.mini2Dx.core.serialization.annotation.Field.class)) {
				continue;
			}
			fieldData.add(new AotSerializedFieldData(clazz, fields[i]));
		}
	}

	public AotSerializedClassData(Scanner scanner) throws ClassNotFoundException {
		this.qualifiedClassName = scanner.nextLine();
		this.nonConcrete = Boolean.parseBoolean(scanner.nextLine());

		final String postDeserializeMethod = scanner.nextLine();
		if(postDeserializeMethod.equals("null")) {
			postDeserializeMethodName = null;
		} else {
			postDeserializeMethodName = postDeserializeMethod;
		}

		this.clazz = Class.forName(qualifiedClassName);

		int totalConstructors = Integer.parseInt(scanner.nextLine().trim());
		for(int i = 0; i < totalConstructors; i++) {
			constructorData.add(new AotSerializedConstructorData(scanner));
		}

		int totalFields = Integer.parseInt(scanner.nextLine().trim());
		for(int i = 0; i < totalFields; i++) {
			fieldData.add(new AotSerializedFieldData(clazz, scanner));
		}
	}

	public void saveTo(PrintWriter writer) {
		writer.println(qualifiedClassName);
		writer.println(nonConcrete);
		writer.println(postDeserializeMethodName);
		writer.println(constructorData.size);
		for(int i = 0; i < constructorData.size; i++) {
			constructorData.get(i).saveTo(writer);
		}

		writer.println(fieldData.size);
		for(int i = 0; i < fieldData.size; i++) {
			fieldData.get(i).saveTo(writer);
		}
	}

	public String getQualifiedClassName() {
		return qualifiedClassName;
	}

	public String getPostDeserializeMethodName() {
		return postDeserializeMethodName;
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

	public Field getFieldDataAsField(int fieldIndex) {
		return fieldData.get(fieldIndex).getField();
	}

	public synchronized Field[] getFieldDataAsFieldArray() {
		if(fieldDataAsFieldArray == null) {
			fieldDataAsFieldArray = new Field[getTotalFields()];
			for(int i = 0; i < fieldDataAsFieldArray.length; i++) {
				fieldDataAsFieldArray[i] = getFieldData(i).getField();
			}
		}
		return fieldDataAsFieldArray;
	}

	public synchronized AotSerializedFieldData getFieldData(String fieldName) {
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

	public int getTotalConstructors() {
		return constructorData.size;
	}

	public AotSerializedConstructorData getConstructorData(int index) {
		return constructorData.get(index);
	}

	public boolean isNonConcrete() {
		return nonConcrete;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AotSerializedClassData)) return false;
		AotSerializedClassData classData = (AotSerializedClassData) o;
		return nonConcrete == classData.nonConcrete &&
				Objects.equals(qualifiedClassName, classData.qualifiedClassName) &&
				Objects.equals(postDeserializeMethodName, classData.postDeserializeMethodName) &&
				Objects.equals(constructorData, classData.constructorData) &&
				Objects.equals(fieldData, classData.fieldData);
	}

	@Override
	public int hashCode() {
		return Objects.hash(qualifiedClassName, postDeserializeMethodName, nonConcrete, constructorData, fieldData);
	}
}
