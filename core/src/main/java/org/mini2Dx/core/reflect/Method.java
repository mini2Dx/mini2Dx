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
package org.mini2Dx.core.reflect;

import org.mini2Dx.core.exception.ReflectionException;

/**
 * Provides information about and access to a single method of a class
 */
public interface Method {
	/**
	 * Returns the name of the method
	 *
	 * @return
	 */
	public String getName();

	/**
	 * Invokes the method for a given class instance
	 *
	 * @param obj  The class instance
	 * @param args The method arguments
	 * @return The method result (or null if a void method)
	 */
	public Object invoke(Object obj, Object... args) throws ReflectionException;

	/**
	 * Returns true if the method if annotated with the given annotation class
	 *
	 * @param annotation The annotation class
	 * @return False if the annotation is not present
	 */
	public boolean isAnnotationPresent(Class<? extends java.lang.annotation.Annotation> annotation);

	/**
	 * Returns the declared annotations
	 * @return An empty array if no annotations declared
	 */
	public Annotation [] getDeclaredAnnotations();

	/**
	 * Returns a annotation on this method
	 * @param annotationType The annotation type to search for
	 * @return Null if the method is not annotated with the annotation
	 */
	public Annotation getDeclaredAnnotation (Class<? extends java.lang.annotation.Annotation> annotationType);

	/**
	 * Returns the return type of the method
	 * @return
	 */
	public Class getReturnType();

	/**
	 * Returns the type of the parameters in declared order
	 * @return An empty array if the method has no parameters
	 */
	public Class[] getParameterTypes();

	/**
	 * Returns the Class object representing the class or interface that declares the method.
	 */
	public Class getDeclaringClass();

	/**
	 * Returns true if the method is declared as abstract
	 * @return
	 */
	public boolean isAbstract();

	/**
	 * Returns true if the method is not public, private or protected
	 * @return
	 */
	public boolean isDefaultAccess();

	/**
	 * Returns true if the method is declared as final
	 * @return
	 */
	public boolean isFinal();

	/**
	 * Returns true if the method is declared as private
	 * @return
	 */
	public boolean isPrivate();

	/**
	 * Returns true if the method is declared as protected
	 * @return
	 */
	public boolean isProtected();

	/**
	 * Returns true if the method is declared as public
	 * @return
	 */
	public boolean isPublic();

	/**
	 * Returns true if the method is declared as native
	 * @return
	 */
	public boolean isNative();

	/**
	 * Returns true if the method is declared as static
	 * @return
	 */
	public boolean isStatic();

	/**
	 * Returns true if the method takes a variable number of parameters (...)
	 * @return
	 */
	public boolean isVarArgs();
}
