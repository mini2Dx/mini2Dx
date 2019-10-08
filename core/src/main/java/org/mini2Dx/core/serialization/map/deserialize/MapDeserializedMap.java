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

import org.mini2Dx.core.exception.ReflectionException;
import org.mini2Dx.core.reflect.Field;
import org.mini2Dx.core.serialization.AotSerializationData;
import org.mini2Dx.core.serialization.aot.AotSerializedClassData;
import org.mini2Dx.core.serialization.aot.AotSerializedFieldData;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class used during JSON/XML deserialization
 */
public class MapDeserializedMap extends DeserializedMap<Map> {

	public MapDeserializedMap(Class<?> ownerClass, Field field, Class<?> fieldClass, Object object) throws ReflectionException {
		super(ownerClass, field, fieldClass, object);
	}

	@Override
	public Class<? extends Map> getFallbackImplementation() {
		return HashMap.class;
	}

	@Override
	public Class<?> getKeyClass() {
		final AotSerializedFieldData aotFieldData = AotSerializationData.getFieldData(ownerClass, field.getName());
		if(aotFieldData != null) {
			return aotFieldData.getElementType(0);
		}
		return field.getElementType(0);
	}

	@Override
	public Class<?> getValueClass() {
		final AotSerializedFieldData aotFieldData = AotSerializationData.getFieldData(ownerClass, field.getName());
		if(aotFieldData != null) {
			return aotFieldData.getElementType(1);
		}
		return field.getElementType(1);
	}

	@Override
	public void put(Object key, Object value) {
		map.put(key, value);
	}
}
