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
package org.mini2Dx.core.serialization.map.serialize;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.gdx.utils.*;

import java.util.Map;

/**
 * Utility class used during JSON/XML serialization
 */
public abstract class SerializedMap<T> {
	protected final T map;

	public SerializedMap(T map) {
		super();
		this.map = map;
	}

	public abstract Object get(Object key);

	public abstract int getSize();

	public abstract Iterable keys();

	public static SerializedMap getImplementation(Class<?> clazz, Object map) throws NoSuchFieldException {
		if(Mdx.reflect.isAssignableFrom(ArrayMap.class, clazz)) {
			return new ArrayMapSerializedMap((ArrayMap) map);
		} else if(Mdx.reflect.isAssignableFrom(IntFloatMap.class, clazz)) {
			return new IntFloatMapSerializedMap((IntFloatMap) map);
		} else if(Mdx.reflect.isAssignableFrom(IntIntMap.class, clazz)) {
			return new IntIntMapSerializedMap((IntIntMap) map);
		} else if(Mdx.reflect.isAssignableFrom(IntMap.class, clazz)) {
			return new IntMapSerializedMap((IntMap) map);
		} else if(Mdx.reflect.isAssignableFrom(LongMap.class, clazz)) {
			return new LongMapSerializedMap((LongMap) map);
		} else if(Mdx.reflect.isAssignableFrom(ObjectFloatMap.class, clazz)) {
			return new ObjectFloatMapSerializedMap((ObjectFloatMap) map);
		} else if(Mdx.reflect.isAssignableFrom(ObjectIntMap.class, clazz)) {
			return new ObjectIntMapSerializedMap((ObjectIntMap) map);
		} else if(Mdx.reflect.isAssignableFrom(ObjectMap.class, clazz)) {
			return new ObjectMapSerializedMap((ObjectMap) map);
		} else if(Mdx.reflect.isAssignableFrom(OrderedMap.class, clazz)) {
			return new OrderedMapSerializedMap((OrderedMap) map);
		} else if(Mdx.reflect.isAssignableFrom(Map.class, clazz)) {
			return new MapSerializedMap((Map) map);
		}
		return null;
	}
}
