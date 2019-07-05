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

import org.mini2Dx.gdx.utils.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Utility class used during JSON/XML serialization
 */
public abstract class SerializedCollection<T> {
	protected final T collection;

	public SerializedCollection(T collection) {
		super();
		this.collection = collection;
	}

	public abstract Object get(int index);

	public abstract int getLength();

	public abstract void dispose();

	public static SerializedCollection getImplementation(Class<?> clazz, Object collection) throws NoSuchFieldException {
		if(List.class.isAssignableFrom(clazz)) {
			return new ListSerializedCollection((List) collection);
		} else if(Set.class.isAssignableFrom(clazz)) {
			return new CollectionSerializedCollection((Set) collection);
		} else if(Collection.class.isAssignableFrom(clazz)) {
			return new CollectionSerializedCollection((Collection) collection);
		}  else if(Array.class.isAssignableFrom(clazz)) {
			return new GdxSerializedCollection<Array>(Array.class, (Array) collection);
		} else if(BooleanArray.class.isAssignableFrom(clazz)) {
			return new GdxSerializedCollection<BooleanArray>(BooleanArray.class, (BooleanArray) collection);
		} else if(ByteArray.class.isAssignableFrom(clazz)) {
			return new GdxSerializedCollection<ByteArray>(ByteArray.class, (ByteArray) collection);
		} else if(CharArray.class.isAssignableFrom(clazz)) {
			return new GdxSerializedCollection<CharArray>(CharArray.class, (CharArray) collection);
		} else if(FloatArray.class.isAssignableFrom(clazz)) {
			return new GdxSerializedCollection<FloatArray>(FloatArray.class, (FloatArray) collection);
		} else if(IntArray.class.isAssignableFrom(clazz)) {
			return new GdxSerializedCollection<IntArray>(IntArray.class, (IntArray) collection);
		} else if(IntSet.class.isAssignableFrom(clazz)) {
			return new IntSetSerializedCollection((IntSet) collection);
		} else if(OrderedSet.class.isAssignableFrom(clazz)) {
			return new IterableGdxSerializedCollection<OrderedSet>(OrderedSet.class, (OrderedSet) collection);
		} else if(ObjectSet.class.isAssignableFrom(clazz)) {
			return new IterableGdxSerializedCollection<ObjectSet>(ObjectSet.class, (ObjectSet) collection);
		} else if(LongArray.class.isAssignableFrom(clazz)) {
			return new GdxSerializedCollection<LongArray>(LongArray.class, (LongArray) collection);
		} else if(ShortArray.class.isAssignableFrom(clazz)) {
			return new GdxSerializedCollection<ShortArray>(ShortArray.class, (ShortArray) collection);
		} else if(SortedIntList.class.isAssignableFrom(clazz)) {
			return new GdxSerializedCollection<SortedIntList>(SortedIntList.class, (SortedIntList) collection);
		}
		return null;
	}
}
