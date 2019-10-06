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
package org.mini2Dx.core.serialization.map.deserialize;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.exception.ReflectionException;
import org.mini2Dx.core.reflect.Field;
import org.mini2Dx.gdx.utils.*;

import java.util.Map;

/**
 * Utility class used during JSON/XML deserialization
 */
public abstract  class DeserializedMap<T> {
	protected final Class<?> ownerClass;
	protected final Field field;
	protected final Class<?> fieldClass;

	protected final T map;

	public DeserializedMap(Class<?> ownerClass, Field field, Class<?> fieldClass, Object object) throws ReflectionException {
		super();
		this.ownerClass = ownerClass;
		this.field = field;
		this.fieldClass = fieldClass;

		if(field.isFinal()) {
			map = (T) field.get(object);
		} else {
			map = (T) (Mdx.reflect.isInterface(fieldClass) ? Mdx.reflect.newInstance(getFallbackImplementation())
					: Mdx.reflect.newInstance(fieldClass));
			field.set(object, map);
		}
	}

	public abstract Class<? extends T> getFallbackImplementation();

	public abstract Class<?> getKeyClass();

	public abstract Class<?> getValueClass();

	public abstract void put(Object key, Object value);

	public static DeserializedMap getImplementation(Class<?> ownerClass, Field field, Class<?> fieldClass, Object object) throws ReflectionException {
		if(Mdx.reflect.isAssignableFrom(Map.class, fieldClass)) {
			return new MapDeserializedMap(ownerClass, field, fieldClass, object);
		} else if(Mdx.reflect.isAssignableFrom(ArrayMap.class, fieldClass)) {
			return new GdxDeserializedMap<ArrayMap>(ownerClass, field, ArrayMap.class, field.getElementType(0), field.getElementType(1), object);
		} else if(Mdx.reflect.isAssignableFrom(IntMap.class, fieldClass)) {
			return new GdxDeserializedMap<IntMap>(ownerClass, field, IntMap.class, Integer.class, field.getElementType(0), object);
		} else if(Mdx.reflect.isAssignableFrom(IntFloatMap.class, fieldClass)) {
			return new GdxDeserializedMap<IntFloatMap>(ownerClass, field, IntFloatMap.class, Integer.class, Float.class, object);
		} else if(Mdx.reflect.isAssignableFrom(IntIntMap.class, fieldClass)) {
			return new GdxDeserializedMap<IntIntMap>(ownerClass, field, IntIntMap.class, Integer.class, Integer.class, object);
		} else if(Mdx.reflect.isAssignableFrom(LongMap.class, fieldClass)) {
			return new GdxDeserializedMap<LongMap>(ownerClass, field, LongMap.class, Long.class, field.getElementType(0), object);
		} else if(Mdx.reflect.isAssignableFrom(ObjectFloatMap.class, fieldClass)) {
			return new GdxDeserializedMap<ObjectFloatMap>(ownerClass, field, ObjectFloatMap.class, field.getElementType(0), Float.class, object);
		} else if(Mdx.reflect.isAssignableFrom(ObjectIntMap.class, fieldClass)) {
			return new GdxDeserializedMap<ObjectIntMap>(ownerClass, field, ObjectIntMap.class, field.getElementType(0), Integer.class, object);
		} else if(Mdx.reflect.isAssignableFrom(ObjectMap.class, fieldClass)) {
			return new GdxDeserializedMap<ObjectMap>(ownerClass, field, ObjectMap.class, field.getElementType(0), field.getElementType(1), object);
		} else if(Mdx.reflect.isAssignableFrom(OrderedMap.class, fieldClass)) {
			return new GdxDeserializedMap<OrderedMap>(ownerClass, field, OrderedMap.class, field.getElementType(0), field.getElementType(1), object);
		}
		return null;
	}
}
