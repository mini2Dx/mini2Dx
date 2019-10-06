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
import org.mini2Dx.gdx.utils.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Utility class used during JSON/XML deserialization
 */
public abstract class DeserializedCollection<T> {
	protected final Class<?> ownerClass;
	protected final Field field;
	protected final Class<?> fieldClass;

	protected final T collection;

	public DeserializedCollection(Class<?> ownerClass, Field field, Class<?> fieldClass, Object object) throws ReflectionException {
		super();
		this.ownerClass = ownerClass;
		this.field = field;
		this.fieldClass = fieldClass;

		if(field.isFinal()) {
			collection = (T) field.get(object);
		} else {
			collection = (T) (fieldClass.isInterface() ? Mdx.reflect.newInstance(getFallbackImplementation())
					: Mdx.reflect.newInstance(fieldClass));
			field.set(object, collection);
		}
	}

	public abstract Class<? extends T> getFallbackImplementation();

	public abstract Class<?> getValueClass();

	public abstract void add(Object element);

	public static DeserializedCollection getImplementation(Class<?> ownerClass, Field field, Class<?> fieldClass, Object object) throws ReflectionException, NoSuchMethodException {
		if(Mdx.reflect.isAssignableFrom(List.class, fieldClass)) {
			return new ListDeserializedCollection(ownerClass, field, fieldClass, object);
		} else if(Mdx.reflect.isAssignableFrom(Set.class, fieldClass)) {
			return new SetDeserializedCollection(ownerClass, field, fieldClass, object);
		} else if(Mdx.reflect.isAssignableFrom(Array.class, fieldClass)) {
			return new GdxDeserializedCollection<Array, Object>(ownerClass, Array.class, field.getElementType(0), field, fieldClass, object);
		} else if(Mdx.reflect.isAssignableFrom(BooleanArray.class, fieldClass)) {
			return new GdxDeserializedCollection<BooleanArray, Boolean>(ownerClass, BooleanArray.class, Boolean.class, field, fieldClass, object);
		} else if(Mdx.reflect.isAssignableFrom(ByteArray.class, fieldClass)) {
			return new GdxDeserializedCollection<ByteArray, Byte>(ownerClass, ByteArray.class, Byte.class, field, fieldClass, object);
		} else if(Mdx.reflect.isAssignableFrom(CharArray.class, fieldClass)) {
			return new GdxDeserializedCollection<CharArray, Character>(ownerClass, CharArray.class, Character.class, field, fieldClass, object);
		} else if(Mdx.reflect.isAssignableFrom(FloatArray.class, fieldClass)) {
			return new GdxDeserializedCollection<FloatArray, Float>(ownerClass, FloatArray.class, Float.class, field, fieldClass, object);
		} else if(Mdx.reflect.isAssignableFrom(IntArray.class, fieldClass)) {
			return new GdxDeserializedCollection<IntArray, Integer>(ownerClass, IntArray.class, Integer.class, field, fieldClass, object);
		} else if(Mdx.reflect.isAssignableFrom(IntSet.class, fieldClass)) {
			return new GdxDeserializedCollection<IntSet, Integer>(ownerClass, IntSet.class, Integer.class, field, fieldClass, object);
		} else if(Mdx.reflect.isAssignableFrom(LongArray.class, fieldClass)) {
			return new GdxDeserializedCollection<LongArray, Long>(ownerClass, LongArray.class, Long.class, field, fieldClass, object);
		} else if(Mdx.reflect.isAssignableFrom(OrderedSet.class, fieldClass)) {
			return new GdxDeserializedCollection<OrderedSet, Object>(ownerClass, OrderedSet.class, field.getElementType(0), field, fieldClass, object);
		} else if(Mdx.reflect.isAssignableFrom(ObjectSet.class, fieldClass)) {
			return new GdxDeserializedCollection<ObjectSet, Object>(ownerClass, ObjectSet.class, field.getElementType(0), field, fieldClass, object);
		} else if(Mdx.reflect.isAssignableFrom(ShortArray.class, fieldClass)) {
			return new GdxDeserializedCollection<ShortArray, Short>(ownerClass, ShortArray.class, Short.class, field, fieldClass, object);
		} else if(Mdx.reflect.isAssignableFrom(SortedIntList.class, fieldClass)) {
			return new GdxDeserializedCollection<SortedIntList, Integer>(ownerClass, SortedIntList.class, Integer.class, field, fieldClass, object);
		} else if(Mdx.reflect.isAssignableFrom(Collection.class, fieldClass)) {
			return new ListDeserializedCollection(ownerClass, field, fieldClass, object);
		} else {
			return null;
		}
	}
}
