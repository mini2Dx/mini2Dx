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

/**
 * Utility class used during JSON/XML serialization
 */
public class GdxSerializedCollection<T> extends SerializedCollection<T> {
	private static final String LOGGING_TAG = GdxSerializedCollection.class.getSimpleName();

	private Method getMethod;
	private Field sizeField;

	public GdxSerializedCollection(Class<T> clazz, T collection) throws NoSuchFieldException {
		super(collection);

		for(Method method : Mdx.reflect.getMethods(clazz)) {
			if(method.getName().equals("get")) {
				getMethod = method;
				break;
			}
		}
		sizeField = Mdx.reflect.getField(clazz, "size");
	}

	@Override
	public Object get(int index) {
		try {
			return getMethod.invoke(collection, index);
		} catch (ReflectionException e) {
			Mdx.log.error(LOGGING_TAG, e.getMessage(), e);
		}
		return null;
	}

	@Override
	public int getLength() {
		try {
			return (int) sizeField.get(collection);
		} catch (ReflectionException e) {
			Mdx.log.error(LOGGING_TAG, e.getMessage(), e);
		}
		return -1;
	}

	@Override
	public void dispose() {
	}
}
