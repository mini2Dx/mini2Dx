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

import org.mini2Dx.core.exception.ReflectionException;
import org.mini2Dx.core.reflect.Annotation;
import org.mini2Dx.core.reflect.Method;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 * Implementation of {@link Method} where JVM-based reflection is supported
 */
public class JvmMethod implements Method {
	private final java.lang.reflect.Method method;

	public JvmMethod(java.lang.reflect.Method method) {
		try {
			method.setAccessible(true);
		} catch (Exception e) {}
		this.method = method;
	}

	@Override
	public String getName() {
		return method.getName();
	}

	@Override
	public Object invoke(Object obj, Object... args) throws ReflectionException {
		try {
			return method.invoke(obj, args);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new ReflectionException(e.getMessage(), e);
		}
	}

	@Override
	public boolean isAnnotationPresent(Class<? extends java.lang.annotation.Annotation> annotation) {
		return method.isAnnotationPresent(annotation);
	}

	@Override
	public Annotation[] getDeclaredAnnotations() {
		final java.lang.annotation.Annotation[] annotations = method.getDeclaredAnnotations();
		final Annotation[] result = new Annotation[annotations.length];
		if (annotations != null) {
			for (int i = 0; i < annotations.length; i++) {
				result[i] = new JvmAnnotation(annotations[i]);
			}
		}
		return result;
	}

	@Override
	public Annotation getDeclaredAnnotation(Class<? extends java.lang.annotation.Annotation> annotationType) {
		final java.lang.annotation.Annotation[] annotations = method.getDeclaredAnnotations();
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
	public Class getReturnType() {
		return method.getReturnType();
	}

	@Override
	public Class[] getParameterTypes() {
		return method.getParameterTypes();
	}

	@Override
	public Class getDeclaringClass() {
		return method.getDeclaringClass();
	}

	@Override
	public boolean isAbstract() {
		return Modifier.isAbstract(method.getModifiers());
	}

	@Override
	public boolean isDefaultAccess() {
		if(isPrivate()) {
			return false;
		}
		if(isProtected()) {
			return false;
		}
		if(isPublic()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isFinal() {
		return Modifier.isFinal(method.getModifiers());
	}

	@Override
	public boolean isPrivate() {
		return Modifier.isPrivate(method.getModifiers());
	}

	@Override
	public boolean isProtected() {
		return Modifier.isProtected(method.getModifiers());
	}

	@Override
	public boolean isPublic() {
		return Modifier.isPublic(method.getModifiers());
	}

	@Override
	public boolean isNative() {
		return Modifier.isNative(method.getModifiers());
	}

	@Override
	public boolean isStatic() {
		return Modifier.isStatic(method.getModifiers());
	}

	@Override
	public boolean isVarArgs() {
		return method.isVarArgs();
	}
}
