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
 * Provides information about and access to a single constructor of a class
 */
public interface Constructor {
	/**
	 * The class that declares this constructor
	 * @return
	 */
	public Class getDeclaringClass();

	/**
	 * Returns an array of classes for the constructor parameters in declared order
	 * @return An empty array if no parameters
	 */
	public Class [] getParameterTypes();

	/**
	 * Returns an array of arrays representing the annotations on the parameters in declared order
	 * @return An empty array if no parameters
	 */
	public Annotation [][] getParameterAnnotations();

	/**
	 * Use this constructor to create a new object instance
	 * @param args The constructor parameters in declared order
	 * @return A new object instance
	 * @throws ReflectionException
	 */
	public Object newInstance(Object... args) throws ReflectionException;
}
