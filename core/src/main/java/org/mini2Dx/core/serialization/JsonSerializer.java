/**
 * Copyright (c) 2015, mini2Dx Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.serialization;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.badlogic.gdx.utils.reflect.Annotation;
import com.badlogic.gdx.utils.reflect.ArrayReflection;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;

/**
 * Serializes objects to/from JSON based on mini2Dx annotations
 */
public class JsonSerializer {

	public <T> T fromJson(String json, Class<T> clazz)
			throws SerializationException {
		return deserialize(new JsonReader().parse(json), clazz);
	}

	public <T> String toJson(T object) throws SerializationException {
		return toJson(object, false);
	}

	public <T> String toJson(T object, boolean prettyPrint)
			throws SerializationException {
		StringWriter writer = new StringWriter();
		Json json = new Json();
		json.setOutputType(OutputType.json);
		json.setWriter(writer);

		writeObject(object, null, json);

		String result = writer.toString();
		try {
			writer.close();
		} catch (IOException e) {
			throw new SerializationException(e);
		}
		if (prettyPrint) {
			return json.prettyPrint(result);
		}
		return result;
	}

	private <T> void writePrimitive(String fieldName, Object value, Json json) {
		if (fieldName != null) {
			json.writeValue(fieldName, value);
		} else {
			json.writeValue(value);
		}
	}

	private <T> void writeArray(String fieldName, Object array, Json json)
			throws SerializationException {
		if (fieldName != null) {
			json.writeArrayStart(fieldName);
		} else {
			json.writeArrayStart();
		}
		int arrayLength = Array.getLength(array);
		for (int i = 0; i < arrayLength; i++) {
			writeObject(Array.get(array, i), null, json);
		}
		json.writeArrayEnd();
	}

	private <T> void writeMap(String fieldName, Map map, Json json)
			throws SerializationException {
		if (fieldName != null) {
			json.writeObjectStart(fieldName);
		} else {
			json.writeObjectStart();
		}
		for (Object key : map.keySet()) {
			writeObject(map.get(key), key.toString(), json);
		}
		json.writeObjectEnd();
	}

	private <T> void writeObject(T object, String fieldName, Json json)
			throws SerializationException {
		try {
			if (object == null) {
				writePrimitive(fieldName, null, json);
				return;
			}

			Class<?> clazz = object.getClass();

			if (isPrimitive(clazz) || clazz.equals(String.class)) {
				writePrimitive(fieldName, object, json);
				return;
			}
			if (clazz.isArray()) {
				writeArray(fieldName, object, json);
				return;
			}
			if (Collection.class.isAssignableFrom(clazz)) {
				Collection collection = (Collection) object;
				writeArray(fieldName, collection.toArray(), json);
				return;
			}
			if (Map.class.isAssignableFrom(clazz)) {
				writeMap(fieldName, (Map) object, json);
				return;
			}

			if (fieldName == null) {
				json.writeObjectStart();
			} else {
				json.writeObjectStart(fieldName);
			}

			for (Field field : ClassReflection.getDeclaredFields(clazz)) {
				field.setAccessible(true);
				Annotation annotation = field
						.getDeclaredAnnotation(org.mini2Dx.core.serialization.annotation.Field.class);

				if (annotation == null) {
					continue;
				}
				org.mini2Dx.core.serialization.annotation.Field fieldAnnotation = annotation
						.getAnnotation(org.mini2Dx.core.serialization.annotation.Field.class);

				if (!fieldAnnotation.optional() && field.get(object) == null) {
					throw new RequiredFieldException(clazz, field.getName());
				}
				writeObject(field.get(object), field.getName(), json);
			}

			json.writeObjectEnd();
		} catch (SerializationException e) {
			throw e;
		} catch (Exception e) {
			throw new SerializationException(e);
		}
	}

	private <T> T deserialize(JsonValue objectRoot, Class<T> clazz)
			throws SerializationException {
		try {
			if (objectRoot.isNull()) {
				return null;
			}
			if (objectRoot.isObject()) {
				T result = ClassReflection.newInstance(clazz);
				for (Field field : ClassReflection.getDeclaredFields(clazz)) {
					field.setAccessible(true);
					Annotation annotation = field
							.getDeclaredAnnotation(org.mini2Dx.core.serialization.annotation.Field.class);

					if (annotation == null) {
						continue;
					}
					org.mini2Dx.core.serialization.annotation.Field fieldAnnotation = annotation
							.getAnnotation(org.mini2Dx.core.serialization.annotation.Field.class);

					JsonValue value = objectRoot.get(field.getName());
					if (value == null || value.isNull()) {
						if (!fieldAnnotation.optional()) {
							throw new RequiredFieldException(clazz,
									field.getName());
						}
						continue;
					}
					setField(result, field, value);
				}
				return result;
			}
			if (objectRoot.isArray()) {
				Class<?> arrayType = clazz.getComponentType();
				Object array = ArrayReflection.newInstance(arrayType,
						objectRoot.size);
				for (int i = 0; i < objectRoot.size; i++) {
					Array.set(array, i, objectRoot.get(i));
				}
				return (T) array;
			}

			if (clazz.equals(Boolean.TYPE) || clazz.equals(Boolean.class)) {
				return (T) ((Boolean) objectRoot.asBoolean());
			} else if (clazz.equals(Byte.TYPE) || clazz.equals(Byte.class)) {
				return (T) ((Byte) objectRoot.asByte());
			} else if (clazz.equals(Character.TYPE)
					|| clazz.equals(Character.class)) {
				return (T) ((Character) objectRoot.asChar());
			} else if (clazz.equals(Double.TYPE) || clazz.equals(Double.class)) {
				return (T) ((Double) objectRoot.asDouble());
			} else if (clazz.equals(Float.TYPE) || clazz.equals(Float.class)) {
				return (T) ((Float) objectRoot.asFloat());
			} else if (clazz.equals(Integer.TYPE)
					|| clazz.equals(Integer.class)) {
				return (T) ((Integer) objectRoot.asInt());
			} else if (clazz.equals(Long.TYPE) || clazz.equals(Long.class)) {
				return (T) ((Long) objectRoot.asLong());
			} else if (clazz.equals(Short.TYPE) || clazz.equals(Short.class)) {
				return (T) ((Short) objectRoot.asShort());
			} else {
				return (T) objectRoot.asString();
			}
		} catch (SerializationException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SerializationException(e);
		}
	}

	private <T> void setField(T targetObject, Field field, JsonValue value)
			throws SerializationException {
		try {
			Class<?> clazz = field.getType();
			if (clazz.isArray()) {
				setArrayField(targetObject, field, clazz, value);
				return;
			}
			if (clazz.isEnum()) {
				field.set(
						targetObject,
						Enum.valueOf((Class<? extends Enum>) clazz,
								value.asString()));
				return;
			}
			if (!clazz.isPrimitive()) {
				if (clazz.equals(String.class)) {
					field.set(targetObject, value.asString());
				} else if (Map.class.isAssignableFrom(clazz)) {
					setMapField(targetObject, field, clazz, value);
				} else if (Collection.class.isAssignableFrom(clazz)) {
					setCollectionField(targetObject, field, clazz, value);
				} else {
					field.set(targetObject, deserialize(value, clazz));
				}
				return;
			}
			field.set(targetObject, deserialize(value, clazz));
		} catch (SerializationException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SerializationException(e);
		}
	}

	private <T> void setArrayField(T targetObject, Field field, Class<?> clazz,
			JsonValue value) throws SerializationException {
		try {
			if (clazz.equals(boolean[].class)) {
				field.set(targetObject, value.asBooleanArray());
			} else if (clazz.equals(byte[].class)) {
				field.set(targetObject, value.asByteArray());
			} else if (clazz.equals(char[].class)) {
				field.set(targetObject, value.asCharArray());
			} else if (clazz.equals(double[].class)) {
				field.set(targetObject, value.asDoubleArray());
			} else if (clazz.equals(float[].class)) {
				field.set(targetObject, value.asFloatArray());
			} else if (clazz.equals(int[].class)) {
				field.set(targetObject, value.asIntArray());
			} else if (clazz.equals(long[].class)) {
				field.set(targetObject, value.asLongArray());
			} else if (clazz.equals(short[].class)) {
				field.set(targetObject, value.asShortArray());
			} else if (clazz.equals(String[].class)) {
				field.set(targetObject, value.asStringArray());
			} else {
				field.set(targetObject, deserialize(value, clazz));
			}
		} catch (SerializationException e) {
			throw e;
		} catch (Exception e) {
			throw new SerializationException(e);
		}
	}

	private <T> void setCollectionField(T targetObject, Field field,
			Class<?> clazz, JsonValue value) throws SerializationException {
		try {
			Class<?> valueClass = field.getElementType(0);

			Collection collection = (Collection) (clazz.isInterface() ? new ArrayList()
					: ClassReflection.newInstance(clazz));
			for (int i = 0; i < value.size; i++) {
				collection.add(deserialize(value.get(i), valueClass));
			}
			field.set(targetObject, collection);
		} catch (SerializationException e) {
			throw e;
		} catch (Exception e) {
			throw new SerializationException(e);
		}
	}

	private <T> void setMapField(T targetObject, Field field, Class<?> clazz,
			JsonValue value) throws SerializationException {
		try {
			Map map = (Map) (clazz.isInterface() ? new HashMap()
					: ClassReflection.newInstance(clazz));
			Class<?> valueClass = field.getElementType(1);

			for (int i = 0; i < value.size; i++) {
				map.put(value.get(i).name,
						deserialize(value.get(i), valueClass));
			}
			field.set(targetObject, map);
		} catch (SerializationException e) {
			throw e;
		} catch (Exception e) {
			throw new SerializationException(e);
		}
	}

	private boolean isPrimitive(Class<?> clazz) {
		if (clazz.isPrimitive()) {
			return true;
		}
		if (clazz.equals(Boolean.class)) {
			return true;
		}
		if (clazz.equals(Byte.class)) {
			return true;
		}
		if (clazz.equals(Character.class)) {
			return true;
		}
		if (clazz.equals(Double.class)) {
			return true;
		}
		if (clazz.equals(Float.class)) {
			return true;
		}
		if (clazz.equals(Integer.class)) {
			return true;
		}
		if (clazz.equals(Long.class)) {
			return true;
		}
		if (clazz.equals(Short.class)) {
			return true;
		}
		return false;
	}
}
