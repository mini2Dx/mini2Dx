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
package org.mini2Dx.core.reflect.jvm;

import org.mini2Dx.core.Reflection;
import org.mini2Dx.core.collections.concurrent.ConcurrentObjectMap;
import org.mini2Dx.core.exception.ReflectionException;
import org.mini2Dx.core.reflect.Annotation;
import org.mini2Dx.core.reflect.Constructor;
import org.mini2Dx.core.reflect.Field;
import org.mini2Dx.core.reflect.Method;
import org.mini2Dx.gdx.utils.ObjectMap;

import java.lang.reflect.Modifier;

/**
 * Implementation of {@link Reflection} where JVM-based reflection is supported
 */
public class JvmReflection implements Reflection {
	private final ObjectMap<Class, Constructor[]> constructorsCache = new ConcurrentObjectMap<>();
	private final ObjectMap<Class, Constructor[]> declaredConstructorsCache = new ConcurrentObjectMap<>();
	private final ObjectMap<Class, Method[]> methodsCache = new ConcurrentObjectMap<>();
	private final ObjectMap<Class, Method[]> declaredMethodsCache = new ConcurrentObjectMap<>();
	private final ObjectMap<Class, Field[]> fieldsCache = new ConcurrentObjectMap<>();
	private final ObjectMap<Class, Field[]> declaredFieldsCache = new ConcurrentObjectMap<>();
	private final ObjectMap<String, Field> fieldCache = new ConcurrentObjectMap<>();
	private final ObjectMap<String, Field> declaredFieldCache = new ConcurrentObjectMap<>();

	@Override
	public Class forName(String qualifiedName) throws ReflectionException {
		try {
			return Class.forName(qualifiedName);
		} catch (ClassNotFoundException e) {
			throw new ReflectionException(e.getMessage(), e);
		}
	}

	@Override
	public Object newInstance(Class clazz) throws ReflectionException {
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ReflectionException(e.getMessage(), e);
		}
	}

	@Override
	public boolean isInstanceOf(Class clazz, Object obj) {
		return clazz.isInstance(obj);
	}

	@Override
	public boolean isAssignableFrom(Class class1, Class class2) {
		return class1.isAssignableFrom(class2);
	}

	@Override
	public boolean isAnnotationPresent(Class clazz, Class<? extends java.lang.annotation.Annotation> annotationType) {
		return clazz.isAnnotationPresent(annotationType);
	}

	@Override
	public org.mini2Dx.core.reflect.Annotation[] getAnnotations(Class clazz) {
		return convert(clazz.getAnnotations());
	}

	@Override
	public Annotation getAnnotation(Class clazz, Class<? extends java.lang.annotation.Annotation> annotationType) {
		return new JvmAnnotation(clazz.getAnnotation(annotationType));
	}

	@Override
	public Annotation[] getDeclaredAnnotations(Class clazz) {
		return convert(clazz.getDeclaredAnnotations());
	}

	@Override
	public Annotation getDeclaredAnnotation(Class clazz, Class<? extends java.lang.annotation.Annotation> annotationType) {
		return new JvmAnnotation(clazz.getDeclaredAnnotation(annotationType));
	}

	@Override
	public Constructor[] getConstructors(Class clazz) {
		if(!constructorsCache.containsKey(clazz)) {
			constructorsCache.put(clazz, convert(clazz.getConstructors()));
		}
		return constructorsCache.get(clazz);
	}

	@Override
	public Constructor[] getDeclaredConstructors(Class clazz) {
		if(!declaredConstructorsCache.containsKey(clazz)) {
			declaredConstructorsCache.put(clazz, convert(clazz.getDeclaredConstructors()));
		}
		return declaredConstructorsCache.get(clazz);
	}

	@Override
	public Method[] getMethods(Class clazz) {
		if(!methodsCache.containsKey(clazz)) {
			methodsCache.put(clazz, convert(clazz.getMethods()));
		}
		return methodsCache.get(clazz);
	}

	@Override
	public Method[] getDeclaredMethods(Class clazz) {
		if(!declaredMethodsCache.containsKey(clazz)) {
			declaredMethodsCache.put(clazz, convert(clazz.getDeclaredMethods()));
		}
		return declaredMethodsCache.get(clazz);
	}

	@Override
	public Field[] getFields(Class clazz) {
		if(!fieldsCache.containsKey(clazz)) {
			fieldsCache.put(clazz, convert(clazz.getFields()));
		}
		return fieldsCache.get(clazz);
	}

	@Override
	public Field[] getDeclaredFields(Class clazz) {
		if(!declaredFieldsCache.containsKey(clazz)) {
			declaredFieldsCache.put(clazz, convert(clazz.getDeclaredFields()));
		}
		return declaredFieldsCache.get(clazz);
	}

	@Override
	public Field getField(Class clazz, String fieldName) throws ReflectionException {
		final String key = clazz.getName() + "->" + fieldName;
		if(!fieldCache.containsKey(key)) {
			try {
				fieldCache.put(key, new JvmField(clazz.getField(fieldName)));
			} catch (NoSuchFieldException e) {
				throw new ReflectionException(e.getMessage(), e);
			}
		}
		return fieldCache.get(key);
	}

	@Override
	public Field getDeclaredField(Class clazz, String fieldName) throws ReflectionException {
		final String key = clazz.getName() + "->" + fieldName;
		if(!declaredFieldCache.containsKey(key)) {
			try {
				declaredFieldCache.put(key, new JvmField(clazz.getDeclaredField(fieldName)));
			} catch (NoSuchFieldException e) {
				throw new ReflectionException(e.getMessage(), e);
			}
		}
		return declaredFieldCache.get(key);
	}

	@Override
	public boolean isAbstract(Class clazz) {
		return Modifier.isAbstract(clazz.getModifiers());
	}

	@Override
	public boolean isInterface(Class clazz) {
		return clazz.isInterface();
	}

	@Override
	public boolean isEnum(Class clazz) {
		return clazz.isEnum();
	}

	@Override
	public boolean isPrimitive(Class clazz) {
		return clazz.isPrimitive();
	}

	@Override
	public boolean isAnnotation(Class clazz) {
		return clazz.isAnnotation();
	}

	@Override
	public boolean isArray(Class clazz) {
		return clazz.isArray();
	}

	@Override
	public Class arrayComponentType(Class clazz) {
		return clazz.getComponentType();
	}

	@Override
	public Object arrayGet(Object array, int index) {
		return java.lang.reflect.Array.get(array, index);
	}

	@Override
	public void arraySet(Object array, int index, Object value) {
		java.lang.reflect.Array.set(array, index, value);
	}

	@Override
	public int arrayLength(Object array) {
		return java.lang.reflect.Array.getLength(array);
	}

	@Override
	public Object newArray(Class clazz, int size) {
		return java.lang.reflect.Array.newInstance(clazz, size);
	}

	private Constructor [] convert(java.lang.reflect.Constructor [] constructors) {
		final Constructor[] result = new Constructor[constructors.length];
		for (int i = 0; i < constructors.length; i++) {
			result[i] = new JvmConstructor(constructors[i]);
		}
		return result;
	}

	private Method [] convert(java.lang.reflect.Method [] methods) {
		final Method[] result = new Method[methods.length];
		for (int i = 0; i < methods.length; i++) {
			result[i] = new JvmMethod(methods[i]);
		}
		return result;
	}

	private Field [] convert(java.lang.reflect.Field [] fields) {
		final Field[] result = new Field[fields.length];
		for (int i = 0; i < fields.length; i++) {
			result[i] = new JvmField(fields[i]);
		}
		return result;
	}

	private Annotation [] convert(java.lang.annotation.Annotation [] annotations) {
		final Annotation[] result = new Annotation[annotations.length];
		for (int i = 0; i < annotations.length; i++) {
			result[i] = new JvmAnnotation(annotations[i]);
		}
		return result;
	}
}
