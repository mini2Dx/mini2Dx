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

/**
 * Implementation of {@link Annotation} where JVM-based reflection is supported
 */
public class JvmAnnotation implements Annotation {
	public final java.lang.annotation.Annotation annotation;

	public JvmAnnotation(java.lang.annotation.Annotation annotation) {
		this.annotation = annotation;
	}

	@Override
	public <T extends java.lang.annotation.Annotation> T getAnnotation(Class<T> annotationType) {
		if (annotation.annotationType().equals(annotationType)) {
			return (T) annotation;
		}
		return null;
	}

	@Override
	public Class<? extends java.lang.annotation.Annotation> getAnnotationType() {
		return annotation.annotationType();
	}
}
