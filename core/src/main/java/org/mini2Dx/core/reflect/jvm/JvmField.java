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

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.exception.ReflectionException;
import org.mini2Dx.core.reflect.Annotation;
import org.mini2Dx.core.reflect.Field;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Implementation of {@link Field} where JVM-based reflection is supported
 */
public class JvmField implements Field {
	public final java.lang.reflect.Field field;

	public JvmField(java.lang.reflect.Field field) {
		try {
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
		} catch (Exception e) {
		}
		this.field = field;
	}

	@Override
	public String getName() {
		return field.getName();
	}

	@Override
	public Object get(Object instance) throws ReflectionException {
		try {
			return field.get(instance);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e.getMessage(), e);
		}
	}

	@Override
	public void set(Object instance, Object value) throws ReflectionException {
		try {
			field.set(instance, value);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e.getMessage(), e);
		}
	}

	@Override
	public boolean isAnnotationPresent(Class<? extends java.lang.annotation.Annotation> annotation) {
		return field.isAnnotationPresent(annotation);
	}

	@Override
	public Annotation[] getDeclaredAnnotations() {
		final java.lang.annotation.Annotation[] annotations = field.getDeclaredAnnotations();
		final Annotation[] result = new Annotation[annotations.length];
		if(annotations != null) {
			for (int i = 0; i < annotations.length; i++) {
				result[i] = new JvmAnnotation(annotations[i]);
			}
		}
		return result;
	}

	@Override
	public Annotation getDeclaredAnnotation(Class<? extends java.lang.annotation.Annotation> annotationType) {
		final java.lang.annotation.Annotation[] annotations = field.getDeclaredAnnotations();
		if (annotations == null) {
			return null;
		}
		for (java.lang.annotation.Annotation annotation : annotations) {
			if (annotation.annotationType().equals(annotationType)) {
				return new JvmAnnotation(annotation);
			}
		}
		return null;
	}

	@Override
	public Class getType() {
		return field.getType();
	}

	@Override
	public Class getElementType(int index) {
		Type genericType = field.getGenericType();
		if (genericType instanceof ParameterizedType) {
			Type[] actualTypes = ((ParameterizedType) genericType).getActualTypeArguments();
			if (actualTypes.length - 1 >= index) {
				Type actualType = actualTypes[index];
				if (actualType instanceof Class) {
					return (Class) actualType;
				} else if (actualType instanceof ParameterizedType) {
					return (Class) ((ParameterizedType) actualType).getRawType();
				} else if (actualType instanceof GenericArrayType) {
					Type componentType = ((GenericArrayType) actualType).getGenericComponentType();
					if (componentType instanceof Class) {
						return Mdx.reflect.newArray((Class) componentType, 0).getClass();
					}
				}
			}
		}
		return null;
	}

	@Override
	public int getTotalElementTypes() {
		Type genericType = field.getGenericType();
		if (genericType instanceof ParameterizedType) {
			Type[] actualTypes = ((ParameterizedType) genericType).getActualTypeArguments();
			return actualTypes.length;
		}
		return 0;
	}

	@Override
	public boolean isDefaultAccess () {
		return !isPrivate() && !isProtected() && !isPublic();
	}

	@Override
	public boolean isFinal () {
		return Modifier.isFinal(field.getModifiers());
	}

	@Override
	public boolean isPrivate () {
		return Modifier.isPrivate(field.getModifiers());
	}

	@Override
	public boolean isProtected () {
		return Modifier.isProtected(field.getModifiers());
	}

	@Override
	public boolean isPublic () {
		return Modifier.isPublic(field.getModifiers());
	}

	@Override
	public boolean isStatic () {
		return Modifier.isStatic(field.getModifiers());
	}

	@Override
	public boolean isTransient () {
		return Modifier.isTransient(field.getModifiers());
	}

	@Override
	public boolean isVolatile () {
		return Modifier.isVolatile(field.getModifiers());
	}

	@Override
	public boolean isSynthetic () {
		return field.isSynthetic();
	}
}
