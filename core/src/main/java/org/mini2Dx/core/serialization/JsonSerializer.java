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
package org.mini2Dx.core.serialization;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.collections.concurrent.ConcurrentObjectMap;
import org.mini2Dx.core.exception.ReflectionException;
import org.mini2Dx.core.exception.RequiredFieldException;
import org.mini2Dx.core.exception.SerializationException;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.reflect.Annotation;
import org.mini2Dx.core.reflect.Constructor;
import org.mini2Dx.core.reflect.Field;
import org.mini2Dx.core.reflect.Method;
import org.mini2Dx.core.serialization.annotation.ConstructorArg;
import org.mini2Dx.core.serialization.annotation.NonConcrete;
import org.mini2Dx.core.serialization.annotation.PostDeserialize;
import org.mini2Dx.core.serialization.aot.AotSerializedClassData;
import org.mini2Dx.core.serialization.aot.AotSerializedConstructorData;
import org.mini2Dx.core.serialization.collection.DeserializedCollection;
import org.mini2Dx.core.serialization.collection.SerializedCollection;
import org.mini2Dx.core.serialization.map.deserialize.DeserializedMap;
import org.mini2Dx.core.serialization.map.serialize.SerializedMap;
import org.mini2Dx.gdx.json.JsonReader;
import org.mini2Dx.gdx.json.JsonValue;
import org.mini2Dx.gdx.json.JsonWriter;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.ObjectMap;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Map;

/**
 * Serializes objects to/from JSON based on
 * {@link org.mini2Dx.core.serialization.annotation.Field} annotations
 */
public class JsonSerializer {
	private static final String LOGGING_TAG = JsonSerializer.class.getSimpleName();

	private final ObjectMap<String, Method[]> methodCache = new ConcurrentObjectMap<>();
	private final ObjectMap<String, Field[]> fieldCache = new ConcurrentObjectMap<>();

	/**
	 * Reads a JSON document and converts it into an object of the specified
	 * type
	 *
	 * @param fileHandle
	 *            The {@link FileHandle} for the JSON document
	 * @param clazz
	 *            The {@link Class} to convert the document to
	 * @return The object deserialized from JSON
	 * @throws SerializationException
	 *             Thrown when the data is invalid
	 */
	public <T> T fromJson(FileHandle fileHandle, Class<T> clazz) throws SerializationException {
		try {
			return deserialize(new JsonReader().parse(fileHandle.reader()), clazz);
		} catch (IOException e) {
			throw new SerializationException(e.getMessage(), e);
		}
	}

	/**
	 * Reads a JSON document and converts it into an object of the specified
	 * type
	 *
	 * @param json
	 *            The JSON document
	 * @param clazz
	 *            The {@link Class} to convert the document to
	 * @return The object deserialized from JSON
	 * @throws SerializationException
	 *             Thrown when the data is invalid
	 */
	public <T> T fromJson(String json, Class<T> clazz) throws SerializationException {
		return deserialize(new JsonReader().parse(json), clazz);
	}

	/**
	 * Writes a JSON document by searching the object for
	 * {@link org.mini2Dx.core.serialization.annotation.Field} annotations
	 *
	 * @param fileHandle
	 *            The {@link FileHandle} to write to
	 * @param object
	 *            The object to convert to JSON
	 * @throws SerializationException
	 *             Thrown when the object is invalid
	 */
	public <T> void toJson(FileHandle fileHandle, T object) throws SerializationException {
		toJson(fileHandle, object, false);
	}

	/**
	 * Writes a JSON document by searching the object for
	 * {@link org.mini2Dx.core.serialization.annotation.Field} annotations
	 *
	 * @param fileHandle
	 *            The {@link FileHandle} to write to
	 * @param object
	 *            The object to convert to JSON
	 * @param prettyPrint
	 *            Set to true if the JSON should be prettified
	 * @throws SerializationException
	 *             Thrown when the object is invalid
	 */
	public <T> void toJson(FileHandle fileHandle, T object, boolean prettyPrint) throws SerializationException {
		final String json = toJson(object, prettyPrint);
		try {
			fileHandle.writeString(json, false);
		} catch (IOException e) {
			throw new SerializationException(e.getMessage(), e);
		}
	}

	/**
	 * Writes a JSON document by searching the object for
	 * {@link org.mini2Dx.core.serialization.annotation.Field} annotations
	 *
	 * @param object
	 *            The object to convert to JSON
	 * @return The object serialized as JSON
	 * @throws SerializationException
	 *             Thrown when the object is invalid
	 */
	public <T> String toJson(T object) throws SerializationException {
		return toJson(object, false);
	}

	/**
	 * Writes a JSON document by searching the object for
	 * {@link org.mini2Dx.core.serialization.annotation.Field} annotations
	 *
	 * @param object
	 *            The object to convert to JSON
	 * @param prettyPrint
	 *            Set to true if the JSON should be prettified
	 * @return The object serialized as JSON
	 * @throws SerializationException
	 *             Thrown when the object is invalid
	 */
	public <T> String toJson(T object, boolean prettyPrint) throws SerializationException {
		StringWriter writer = new StringWriter();
		JsonWriter jsonWriter = new JsonWriter(writer);
		jsonWriter.setOutputType(JsonWriter.OutputType.json);

		writeObject(null, object, null, jsonWriter);

		try {
			jsonWriter.flush();
			final String result = writer.toString();
			writer.flush();
			writer.close();

			if(prettyPrint) {
				return new JsonReader().parse(result).prettyPrint(JsonWriter.OutputType.json, 0);
			}

			return result;
		} catch (IOException e) {
			throw new SerializationException(e);
		}
	}

	private <T> void writeObject(Field fieldDefinition, T object, String fieldName, JsonWriter json) throws SerializationException {
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
			if (clazz.isEnum() || clazz.getSuperclass().isEnum()) {
				writePrimitive(fieldName, object.toString(), json);
				return;
			}
			if (clazz.isArray()) {
				writeArray(fieldDefinition, object, json);
				return;
			}
			SerializedMap serializedMap = SerializedMap.getImplementation(clazz, object);
			if(serializedMap != null) {
				writeSerializedMap(fieldDefinition, serializedMap, json);
				return;
			}

			SerializedCollection serializedCollection = SerializedCollection.getImplementation(clazz, object);
			if(serializedCollection != null) {
				writeSerializedCollection(fieldDefinition, serializedCollection, json);
				return;
			}

			if (fieldName == null) {
				json.object();
			} else {
				json.object(fieldName);
			}
			writeClassFieldIfRequired(fieldDefinition, object, fieldName, json);

			Class<?> currentClass = clazz;
			while (currentClass != null && !currentClass.equals(Object.class)) {
				final String className = currentClass.getName();
				if(!fieldCache.containsKey(className)) {
					fieldCache.put(className, Mdx.reflect.getDeclaredFields(currentClass));
				}
				if(!methodCache.containsKey(className)) {
					methodCache.put(className, Mdx.reflect.getDeclaredMethods(currentClass));
				}

				final Method [] methods = methodCache.get(className);
				final Field [] fields = fieldCache.get(className);

				for (Field field : fields) {
					Annotation annotation = field
							.getDeclaredAnnotation(org.mini2Dx.core.serialization.annotation.Field.class);

					if (annotation == null) {
						continue;
					}
					org.mini2Dx.core.serialization.annotation.Field fieldAnnotation = annotation
							.getAnnotation(org.mini2Dx.core.serialization.annotation.Field.class);

					if (!fieldAnnotation.optional() && field.get(object) == null) {
						throw new RequiredFieldException(currentClass, field.getName());
					}
					writeObject(field, field.get(object), field.getName(), json);
				}
				for(Method method : methods) {
					if(method.getParameterTypes().length > 0) {
						continue;
					}
					Annotation annotation = method.getDeclaredAnnotation(ConstructorArg.class);
					if(annotation == null) {
						continue;
					}
					ConstructorArg constructorArg = annotation.getAnnotation(ConstructorArg.class);
					writeObject(null, method.invoke(object), constructorArg.name(), json); ;
				}
				currentClass = currentClass.getSuperclass();
			}

			//Check for @ConstructorArg annotations in interface methods
			Class<?> [] interfaces = clazz.getInterfaces();
			for(int i = 0; i < interfaces.length; i++) {
				final String className = interfaces[i].getName();
				if(!methodCache.containsKey(className)) {
					methodCache.put(className, Mdx.reflect.getDeclaredMethods(interfaces[i]));
				}
				final Method [] methods = methodCache.get(className);

				for(Method method : methods) {
					if(method.getParameterTypes().length > 0) {
						continue;
					}
					Annotation annotation = method.getDeclaredAnnotation(ConstructorArg.class);
					if(annotation == null) {
						continue;
					}
					ConstructorArg constructorArg = annotation.getAnnotation(ConstructorArg.class);
					writeObject(null, method.invoke(object), constructorArg.name(), json); ;
				}
			}

			json.pop();
		} catch (SerializationException e) {
			throw e;
		} catch (Exception e) {
			throw new SerializationException(e);
		}
	}

	private <T> void writeClassFieldIfRequired(Field fieldDefinition, T object, String fieldName, JsonWriter json) throws SerializationException, IOException {
		if (fieldDefinition == null) {
			return;
		}
		Class<?> clazz = object.getClass();
		Class<?> fieldDefinitionClass = fieldDefinition.getType();

		if(Mdx.reflect.isArray(fieldDefinitionClass)) {
			Class<?> arrayComponentType = Mdx.reflect.arrayComponentType(fieldDefinitionClass);
			if(arrayComponentType.isInterface() && arrayComponentType.getAnnotation(NonConcrete.class) == null) {
				throw new SerializationException("Cannot serialize interface unless it has a @" + NonConcrete.class.getSimpleName() + " annotation");
			}
			writePrimitive("class", clazz.getName(), json);
			return;
		}
		if(Mdx.reflect.isAssignableFrom(Collection.class, fieldDefinitionClass)) {
			Class<?> valueClass = fieldDefinition.getElementType(0);
			if(valueClass.isInterface() && valueClass.getAnnotation(NonConcrete.class) == null) {
				throw new SerializationException("Cannot serialize interface unless it has a @" + NonConcrete.class.getSimpleName() + " annotation");
			}
			writePrimitive("class", clazz.getName(), json);
			return;
		}
		if(Mdx.reflect.isAssignableFrom(Map.class, fieldDefinitionClass)) {
			Class<?> valueClass = fieldDefinition.getElementType(1);
			if(valueClass.isInterface() && valueClass.getAnnotation(NonConcrete.class) == null) {
				throw new SerializationException("Cannot serialize interface unless it has a @" + NonConcrete.class.getSimpleName() + " annotation");
			}
			writePrimitive("class", clazz.getName(), json);
			return;
		}
		if(Mdx.reflect.isInterface(fieldDefinitionClass)) {
			if(fieldDefinitionClass.getAnnotation(NonConcrete.class) == null) {
				throw new SerializationException("Cannot serialize interface unless it has a @" + NonConcrete.class.getSimpleName() + " annotation");
			}
			writePrimitive("class", clazz.getName(), json);
			return;
		}
		if(Mdx.reflect.isAbstract(fieldDefinitionClass)) {
			if(fieldDefinitionClass.getAnnotation(NonConcrete.class) == null) {
				throw new SerializationException("Cannot serialize abstract class unless it has a @" + NonConcrete.class.getSimpleName() + " annotation");
			}
			writePrimitive("class", clazz.getName(), json);
			return;
		}
	}

	private <T> void writeArray(Field field, Object array, JsonWriter json) throws IOException, SerializationException {
		if (field != null) {
			json.array(field.getName());
		} else {
			json.array();
		}

		int arrayLength = Mdx.reflect.arrayLength(array);
		for (int i = 0; i < arrayLength; i++) {
			writeObject(field, Mdx.reflect.arrayGet(array, i), null, json);
		}
		json.pop();
	}

	private <T> void writeSerializedCollection(Field field, SerializedCollection collection, JsonWriter json) throws IOException, SerializationException {
		if (field != null) {
			json.array(field.getName());
		} else {
			json.array();
		}

		int arrayLength = collection.getLength();
		for (int i = 0; i < arrayLength; i++) {
			writeObject(field, collection.get(i), null, json);
		}
		json.pop();

		collection.dispose();
	}

	private <T> void writeSerializedMap(Field field, SerializedMap map, JsonWriter json) throws IOException, SerializationException {
		if (field != null) {
			json.object(field.getName());
		} else {
			json.object();
		}

		for (Object key : map.keys()) {
			writeObject(field, map.get(key), key.toString(), json);
		}
		json.pop();
	}

	private <T> void writePrimitive(String fieldName, Object value, JsonWriter json) throws IOException {
		if (fieldName != null) {
			json.name(fieldName);
		}
		json.value(value);
	}

	private <T> T construct(JsonValue objectRoot, Class<?> clazz) throws SerializationException, IllegalArgumentException {
		Constructor[] constructors = Mdx.reflect.getConstructors(clazz);
		// Single constructor with no args
		if (constructors.length == 1 && constructors[0].getParameterAnnotations().length == 0) {
			return (T) constructors[0].newInstance();
		}

		final AotSerializedClassData classData = AotSerializationData.getClassData(clazz);
		if(classData != null) {
			AotSerializedConstructorData bestMatchedConstructor = null;
			for (int i = 0; i < classData.getTotalConstructors(); i++) {
				final AotSerializedConstructorData constructorData = classData.getConstructorData(i);

				boolean allMatched = true;
				for(int j = 0; j < constructorData.getTotalArgs(); j++) {
					final String argName = constructorData.getConstructorArgName(j);

					if (objectRoot.get(argName) != null) {
						continue;
					}
					allMatched = false;
					break;
				}

				if(!allMatched) {
					continue;
				}

				if(bestMatchedConstructor == null) {
					bestMatchedConstructor = constructorData;
				} else if(constructorData.getTotalArgs() > bestMatchedConstructor.getTotalArgs()) {
					bestMatchedConstructor = constructorData;
				}
			}

			if(bestMatchedConstructor == null) {
				return (T) Mdx.reflect.newInstance(clazz);
			}

			final Object[] constructorParameters = new Object[bestMatchedConstructor.getTotalArgs()];
			for (int i = 0; i < bestMatchedConstructor.getTotalArgs(); i++) {
				constructorParameters[i] = deserialize(objectRoot.get(bestMatchedConstructor.getConstructorArgName(i)), bestMatchedConstructor.getConstructorArgType(i));
				objectRoot.remove(bestMatchedConstructor.getConstructorArgName(i));
			}

			try {
				return (T) clazz.getConstructor(bestMatchedConstructor.getConstructorArgTypes()).newInstance(constructorParameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return (T) Mdx.reflect.newInstance(clazz);
		} else {
			Constructor bestMatchedConstructor = null;
			final Array<ConstructorArg> detectedAnnotations = new Array<ConstructorArg>(1);

			for (int i = 0; i < constructors.length; i++) {
				detectedAnnotations.clear();
				boolean allAnnotated = true;

				for (int j = 0; j < constructors[i].getParameterAnnotations().length; j++) {
					Annotation[] annotations = constructors[i].getParameterAnnotations()[j];
					if (annotations.length == 0) {
						allAnnotated = false;
						break;
					}

					boolean hasConstructorArgAnnotation = false;
					for (int k = 0; k < annotations.length; k++) {
						if (!annotations[k].getAnnotationType().isAssignableFrom(ConstructorArg.class)) {
							continue;
						}
						Annotation annotation = annotations[k];
						if(annotation == null) {
							continue;
						}
						ConstructorArg constructorArg = (ConstructorArg) annotation.getAnnotation(ConstructorArg.class);
						if (objectRoot.get(constructorArg.name()) == null) {
							continue;
						}
						detectedAnnotations.add(constructorArg);
						hasConstructorArgAnnotation = true;
						break;
					}
					if (!hasConstructorArgAnnotation) {
						allAnnotated = false;
					}
				}
				if (!allAnnotated) {
					continue;
				}
				if (bestMatchedConstructor == null) {
					bestMatchedConstructor = constructors[i];
				} else if (detectedAnnotations.size > bestMatchedConstructor.getParameterAnnotations().length) {
					bestMatchedConstructor = constructors[i];
				}
			}
			if (bestMatchedConstructor == null || detectedAnnotations.size == 0) {
				if(detectedAnnotations.size > 0) {
					Mdx.log.error(LOGGING_TAG, "Could not find suitable constructor for " + clazz.getSimpleName() + ". Falling back to default constructor.");
				}
				return (T) Mdx.reflect.newInstance(clazz);
			}

			final Object[] constructorParameters = new Object[detectedAnnotations.size];
			for (int i = 0; i < detectedAnnotations.size; i++) {
				ConstructorArg constructorArg = detectedAnnotations.get(i);
				constructorParameters[i] = deserialize(objectRoot.get(constructorArg.name()), constructorArg.clazz());
				objectRoot.remove(constructorArg.name());
			}
			return (T) bestMatchedConstructor.newInstance(constructorParameters);
		}
	}

	private Class<?> determineImplementation(JsonValue objectRoot, Class<?> clazz) throws SerializationException, ClassNotFoundException {
		if(Mdx.reflect.isInterface(clazz) || Mdx.reflect.isAbstract(clazz)) {
			JsonValue classField = objectRoot.get("class");
			if(classField == null) {
				throw new SerializationException("No class field found for deserializing " + clazz.getName());
			}
			clazz = Mdx.reflect.forName(classField.asString());
		}
		return clazz;
	}

	private <T> void callPostDeserializeMethods(T object, Class<?> clazz) throws SerializationException {
		Class<?> currentClass = clazz;
		while (currentClass != null && !currentClass.equals(Object.class)) {
			final String className = currentClass.getName();
			if(!methodCache.containsKey(className)) {
				methodCache.put(className, Mdx.reflect.getDeclaredMethods(currentClass));
			}
			final Method [] methods = methodCache.get(className);

			final AotSerializedClassData classData = AotSerializationData.getClassData(currentClass);
			if(classData != null) {
				if(classData.getPostDeserializeMethodName() != null) {
					for(Method method : methods) {
						if(method.getName().equals(classData.getPostDeserializeMethodName())) {
							try {
								method.invoke(object);
							} catch (ReflectionException e) {
								throw new SerializationException(e);
							}
						}
					}
				}
			} else {
				for(Method method : methods) {
					if(method.isAnnotationPresent(PostDeserialize.class)) {
						try {
							method.invoke(object);
						} catch (ReflectionException e) {
							throw new SerializationException(e);
						}
					}
				}
			}

			currentClass = currentClass.getSuperclass();
		}
	}

	private <T> T deserialize(JsonValue objectRoot, Class<T> fieldClass) throws SerializationException {
		try {
			if (objectRoot.isNull()) {
				return null;
			}
			if (objectRoot.isObject()) {
				Class<?> clazz = determineImplementation(objectRoot, fieldClass);
				T result = construct(objectRoot, clazz);
				Class<?> currentClass = clazz;
				while (currentClass != null && !currentClass.equals(Object.class)) {
					AotSerializedClassData classData = AotSerializationData.getClassData(currentClass);
					final Field [] fields;

					if(classData != null) {
						fields = classData.getFieldDataAsFieldArray();
					} else {
						fields = Mdx.reflect.getDeclaredFields(currentClass);
					}

					for (Field field : fields) {
						Annotation annotation = field
								.getDeclaredAnnotation(org.mini2Dx.core.serialization.annotation.Field.class);
						org.mini2Dx.core.serialization.annotation.Field fieldAnnotation = null;
						if(classData == null) {
							if (annotation == null) {
								continue;
							}
							fieldAnnotation = annotation
									.getAnnotation(org.mini2Dx.core.serialization.annotation.Field.class);
							if(fieldAnnotation == null) {
								continue;
							}
						}
						JsonValue value = objectRoot.get(field.getName());
						if (value == null || value.isNull()) {
							if (classData == null && fieldAnnotation != null && !fieldAnnotation.optional()) {
								throw new RequiredFieldException(currentClass, field.getName());
							}
							continue;
						}
						setField(result, currentClass, field, value);
					}
					currentClass = currentClass.getSuperclass();
				}
				callPostDeserializeMethods(result, clazz);
				return result;
			}
			if (objectRoot.isArray()) {
				Class<?> arrayType = Mdx.reflect.arrayComponentType(fieldClass);
				Object array = Mdx.reflect.newArray(arrayType, objectRoot.size);
				for (int i = 0; i < objectRoot.size; i++) {
					Mdx.reflect.arraySet(array, i, deserialize(objectRoot.get(i), arrayType));
				}
				return (T) array;
			}
			if (Mdx.reflect.isEnum(fieldClass)) {
				return (T) Enum.valueOf((Class<Enum>) fieldClass, objectRoot.asString());
			}

			if (fieldClass.equals(Boolean.TYPE) || fieldClass.equals(Boolean.class)) {
				return (T) ((Boolean) objectRoot.asBoolean());
			} else if (fieldClass.equals(Byte.TYPE) || fieldClass.equals(Byte.class)) {
				return (T) ((Byte) objectRoot.asByte());
			} else if (fieldClass.equals(Character.TYPE) || fieldClass.equals(Character.class)) {
				return (T) ((Character) objectRoot.asChar());
			} else if (fieldClass.equals(Double.TYPE) || fieldClass.equals(Double.class)) {
				return (T) ((Double) objectRoot.asDouble());
			} else if (fieldClass.equals(Float.TYPE) || fieldClass.equals(Float.class)) {
				return (T) ((Float) objectRoot.asFloat());
			} else if (fieldClass.equals(Integer.TYPE) || fieldClass.equals(Integer.class)) {
				return (T) ((Integer) objectRoot.asInt());
			} else if (fieldClass.equals(Long.TYPE) || fieldClass.equals(Long.class)) {
				return (T) ((Long) objectRoot.asLong());
			} else if (fieldClass.equals(Short.TYPE) || fieldClass.equals(Short.class)) {
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

	private <T> void setField(T targetObject, Class<?> ownerClass, Field field, JsonValue value) throws SerializationException {
		try {
			Class<?> clazz = field.getType();
			if (Mdx.reflect.isArray(clazz)) {
				setArrayField(targetObject, field, clazz, value);
				return;
			}
			if (Mdx.reflect.isEnum(clazz)) {
				if(field.isFinal()) {
					throw new SerializationException("Cannot use @Field on final enum fields. Use the @ConstructorArg method instead.");
				}
				field.set(targetObject, Enum.valueOf((Class<? extends Enum>) clazz, value.asString()));
				return;
			}
			if (!Mdx.reflect.isPrimitive(clazz)) {
				if (clazz.equals(String.class)) {
					if(field.isFinal()) {
						throw new SerializationException("Cannot use @Field on final String fields. Use the @ConstructorArg method instead.");
					}
					field.set(targetObject, value.asString());
				} else {
					DeserializedMap deserializedMap = DeserializedMap.getImplementation(ownerClass, field, clazz, targetObject);
					if(deserializedMap != null) {
						setSerializedMapField(deserializedMap, targetObject, field, clazz, value);
					} else {
						DeserializedCollection deserializedCollection = DeserializedCollection.getImplementation(ownerClass, field, clazz, targetObject);
						if(deserializedCollection != null) {
							setSerializedCollectionField(deserializedCollection, targetObject, field, clazz, value);
						} else if(field.isFinal()) {
							throw new SerializationException("Cannot use @Field on final " + clazz.getName() +" fields.");
						} else {
							field.set(targetObject, deserialize(value, clazz));
						}
					}
				}
				return;
			}
			if(field.isFinal()) {
				throw new SerializationException("Cannot use @Field on final " + clazz.getName() +" fields. Use the @ConstructorArg method instead.");
			}
			field.set(targetObject, deserialize(value, clazz));
		} catch (SerializationException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SerializationException(e);
		}
	}

	private <T> void setSerializedCollectionField(DeserializedCollection deserializedCollection, T targetObject, Field field, Class<?> clazz, JsonValue value)
			throws SerializationException {
		try {
			Class<?> valueClass = deserializedCollection.getValueClass();

			for(int i = 0; i < value.size; i++) {
				deserializedCollection.add(deserialize(value.get(i), valueClass));
			}
		} catch (SerializationException e) {
			throw e;
		} catch (Exception e) {
			e.getMessage();
			throw new SerializationException(e);
		}
	}

	private <T> void setArrayField(T targetObject, Field field, Class<?> clazz, JsonValue value)
			throws SerializationException {
		try {
			if (clazz.equals(boolean[].class)) {
				boolean [] result = value.asBooleanArray();
				if(field.isFinal()) {
					Object targetArray = field.get(targetObject);
					for(int i = 0; i < result.length; i++) {
						Mdx.reflect.arraySet(targetArray, i, result[i]);
					}
				} else {
					field.set(targetObject, result);
				}
			} else if (clazz.equals(byte[].class)) {
				byte [] result = value.asByteArray();
				if(field.isFinal()) {
					Object targetArray = field.get(targetObject);
					for(int i = 0; i < result.length; i++) {
						Mdx.reflect.arraySet(targetArray, i, result[i]);
					}
				} else {
					field.set(targetObject, result);
				}
			} else if (clazz.equals(char[].class)) {
				char [] result = value.asCharArray();
				if(field.isFinal()) {
					Object targetArray = field.get(targetObject);
					for(int i = 0; i < result.length; i++) {
						Mdx.reflect.arraySet(targetArray, i, result[i]);
					}
				} else {
					field.set(targetObject, result);
				}
			} else if (clazz.equals(double[].class)) {
				double [] result = value.asDoubleArray();
				if(field.isFinal()) {
					Object targetArray = field.get(targetObject);
					for(int i = 0; i < result.length; i++) {
						Mdx.reflect.arraySet(targetArray, i, result[i]);
					}
				} else {
					field.set(targetObject, result);
				}
			} else if (clazz.equals(float[].class)) {
				float [] result = value.asFloatArray();
				if(field.isFinal()) {
					Object targetArray = field.get(targetObject);
					for(int i = 0; i < result.length; i++) {
						Mdx.reflect.arraySet(targetArray, i, result[i]);
					}
				} else {
					field.set(targetObject, result);
				}
			} else if (clazz.equals(int[].class)) {
				int [] result = value.asIntArray();
				if(field.isFinal()) {
					Object targetArray = field.get(targetObject);
					for(int i = 0; i < result.length; i++) {
						Mdx.reflect.arraySet(targetArray, i, result[i]);
					}
				} else {
					field.set(targetObject, result);
				}
			} else if (clazz.equals(long[].class)) {
				long [] result = value.asLongArray();
				if(field.isFinal()) {
					Object targetArray = field.get(targetObject);
					for(int i = 0; i < result.length; i++) {
						Mdx.reflect.arraySet(targetArray, i, result[i]);
					}
				} else {
					field.set(targetObject, result);
				}
			} else if (clazz.equals(short[].class)) {
				short [] result = value.asShortArray();
				if(field.isFinal()) {
					Object targetArray = field.get(targetObject);
					for(int i = 0; i < result.length; i++) {
						Mdx.reflect.arraySet(targetArray, i, result[i]);
					}
				} else {
					field.set(targetObject, result);
				}
			} else if (clazz.equals(String[].class)) {
				String [] result = value.asStringArray();
				if(field.isFinal()) {
					Object targetArray = field.get(targetObject);
					for(int i = 0; i < result.length; i++) {
						Mdx.reflect.arraySet(targetArray, i, result[i]);
					}
				} else {
					field.set(targetObject, result);
				}
			} else {
				Object result = deserialize(value, clazz);
				if(field.isFinal()) {
					Object targetArray = field.get(targetObject);
					int length = Mdx.reflect.arrayLength(result);
					for(int i = 0; i < length; i++) {
						Mdx.reflect.arraySet(targetArray, i, Mdx.reflect.arrayGet(result, i));
					}
				} else {
					field.set(targetObject, result);
				}
			}
		} catch (SerializationException e) {
			throw e;
		} catch (Exception e) {
			throw new SerializationException(e);
		}
	}

	private <T> void setSerializedMapField(DeserializedMap deserializedMap, T targetObject, Field field, Class<?> clazz, JsonValue value)
			throws SerializationException {
		try {
			Class<?> keyClass = deserializedMap.getKeyClass();
			Class<?> valueClass = deserializedMap.getValueClass();

			for (int i = 0; i < value.size; i++) {
				deserializedMap.put(parseMapKey(value.get(i).name, keyClass), deserialize(value.get(i), valueClass));
			}
		} catch (SerializationException e) {
			throw e;
		} catch (Exception e) {
			throw new SerializationException(e);
		}
	}

	private boolean isPrimitive(Class<?> clazz) {
		if (Mdx.reflect.isPrimitive(clazz)) {
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

	private <T> T parseMapKey(String value, Class<T> clazz) {
		if (clazz.equals(Boolean.TYPE) || clazz.equals(Boolean.class)) {
			return (T) new Boolean(value);
		}
		if (clazz.equals(Byte.TYPE) || clazz.equals(Byte.class)) {
			return (T) new Byte(value);
		}
		if (clazz.equals(Character.TYPE) || clazz.equals(Character.class)) {
			return (T) new Character(value.charAt(0));
		}
		if (clazz.equals(Double.TYPE) || clazz.equals(Double.class)) {
			return (T) new Double(value);
		}
		if (clazz.equals(Float.TYPE) || clazz.equals(Float.class)) {
			return (T) new Float(value);
		}
		if (clazz.equals(Integer.TYPE) || clazz.equals(Integer.class)) {
			return (T) new Integer(value);
		}
		if (clazz.equals(Long.TYPE) || clazz.equals(Long.class)) {
			return (T) new Long(value);
		}
		if (clazz.equals(Short.TYPE) || clazz.equals(Short.class)) {
			return (T) new Short(value);
		}
		if (Mdx.reflect.isEnum(clazz)) {
			return (T) Enum.valueOf((Class<Enum>) clazz, value);
		}
		return (T) value;
	}
}
