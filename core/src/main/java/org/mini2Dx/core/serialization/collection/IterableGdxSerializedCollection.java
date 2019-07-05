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
import org.mini2Dx.gdx.utils.Array;

/**
 * Utility class used during JSON/XML serialization
 */
public class IterableGdxSerializedCollection <T extends Iterable> extends SerializedCollection<T> {
	private static final String LOGGING_TAG = IterableGdxSerializedCollection.class.getSimpleName();

	private final Array elements = new Array();
	private Field sizeField;

	public IterableGdxSerializedCollection(Class<T> clazz, T collection) throws NoSuchFieldException {
		super(collection);

		sizeField = Mdx.reflect.getField(clazz, "size");
		for(Object obj : collection) {
			elements.add(obj);
		}
	}

	@Override
	public Object get(int index) {
		return elements.get(index);
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
		elements.clear();
	}
}
