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

import org.mini2Dx.core.reflect.Annotation;
import org.mini2Dx.core.reflect.Constructor;

import java.lang.reflect.InvocationTargetException;

/**
 * Implementation of {@link Constructor} where JVM-based reflection is supported
 */
public class JvmConstructor implements Constructor {
	public final java.lang.reflect.Constructor constructor;

	public JvmConstructor(java.lang.reflect.Constructor constructor) {
		try {
			constructor.setAccessible(true);
		} catch (Exception e) {}
		this.constructor = constructor;
	}

	@Override
	public Class getDeclaringClass() {
		return constructor.getDeclaringClass();
	}

	@Override
	public Class[] getParameterTypes() {
		return constructor.getParameterTypes();
	}

	@Override
	public Annotation[][] getParameterAnnotations() {
		final java.lang.annotation.Annotation [][] annotations = constructor.getParameterAnnotations();
		final Annotation [][] result = new Annotation[annotations.length][];
		for(int i = 0; i < annotations.length; i++) {
			result[i] = new Annotation[annotations[i].length];
			for(int j = 0; j < annotations[i].length; j++) {
				result[i][j] = new JvmAnnotation(annotations[i][j]);
			}
		}
		return result;
	}

	@Override
	public Object newInstance(Object... args) throws org.mini2Dx.core.exception.ReflectionException {
		try {
			return constructor.newInstance(args);
		} catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
			throw new org.mini2Dx.core.exception.ReflectionException(e.getMessage(), e);
		}
	}
}
