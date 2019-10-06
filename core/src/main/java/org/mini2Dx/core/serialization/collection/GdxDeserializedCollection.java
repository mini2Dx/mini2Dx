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
package org.mini2Dx.core.serialization.collection;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.exception.ReflectionException;
import org.mini2Dx.core.reflect.Field;
import org.mini2Dx.core.reflect.Method;
import org.mini2Dx.core.serialization.AotSerializationData;
import org.mini2Dx.core.serialization.aot.AotSerializedClassData;
import org.mini2Dx.core.serialization.aot.AotSerializedFieldData;

/**
 * Utility class used during JSON/XML deserialization
 */
public class GdxDeserializedCollection<T, N> extends DeserializedCollection<T> {
	private static final String LOGGING_TAG = GdxDeserializedCollection.class.getSimpleName();

	private final Class<T> fallbackClass;
	private Class<N> valueClass;
	private Method addMethod;

	public GdxDeserializedCollection(Class<?> ownerClass, Class<T> fallbackClass, Class<N> valueClass, Field field, Class<?> fieldClass, Object object)
			throws ReflectionException, NoSuchMethodException {
		super(ownerClass, field, fieldClass, object);
		this.fallbackClass = fallbackClass;
		this.valueClass = valueClass;

		for(Method method : Mdx.reflect.getMethods(fieldClass)) {
			if(method.getName().equals("add") && method.getParameterTypes().length == 1) {
				addMethod = method;
				break;
			}
		}

		if(addMethod == null) {
			throw new NoSuchMethodException("No such method add on " + fieldClass.getName());
		}
	}

	@Override
	public Class<? extends T> getFallbackImplementation() {
		return fallbackClass;
	}

	@Override
	public Class<?> getValueClass() {
		if(valueClass == null) {
			AotSerializedClassData aotClassData = AotSerializationData.getClassData(ownerClass);
			if(aotClassData != null) {
				AotSerializedFieldData aotFieldData = aotClassData.getFieldData(field.getName());
				if(aotFieldData != null) {
					valueClass = aotFieldData.getElementType(0);
				}
			}
		}
		return valueClass;
	}

	@Override
	public void add(Object element) {
		try {
			addMethod.invoke(collection, (N) element);
		} catch (Exception e) {
			Mdx.log.error(LOGGING_TAG, e.getMessage(), e);
		}
	}
}
