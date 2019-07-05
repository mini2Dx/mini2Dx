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
package org.mini2Dx.core;

import org.mini2Dx.core.exception.ReflectionException;
import org.mini2Dx.core.reflect.Annotation;
import org.mini2Dx.core.reflect.Constructor;
import org.mini2Dx.core.reflect.Field;
import org.mini2Dx.core.reflect.Method;

/**
 * Cross-platform reflection API
 */
public interface Reflection {

	/**
	 * Returns the class/interface/annotation/etc. associated with the given name
	 *
	 * @param qualifiedName The fully qualified name (e.g. java.lang.String)
	 * @return The {@link Class}
	 * @throws ReflectionException Thrown when the class cannot be found
	 */
	public Class forName(String qualifiedName) throws ReflectionException;

	/**
	 * Creates a new instance of the provided class using the default constructor
	 *
	 * @param clazz The class to create an instance of
	 * @return A new object instance
	 * @throws ReflectionException Thrown if the instance could not be created
	 */
	public Object newInstance(Class clazz) throws ReflectionException;

	/**
	 * Checks if the provided object is an instance of the provided class
	 * @param clazz The class to check
	 * @param obj The object to check
	 * @return True if obj is assignable to the provided class
	 */
	public boolean isInstanceOf(Class clazz, Object obj);

	/**
	 * Checks if the class1 is the same class, superclass or superinterface of class2
	 * @param class1 See above
	 * @param class2 See above
	 * @return True if assignable
	 */
	public boolean isAssignableFrom (Class class1, Class class2);

	/**
	 * Returns if the class is annotated with the provided annotation
	 *
	 * @param clazz          The class to check
	 * @param annotationType The annotation to search for
	 * @return True if the class is annotated with the provided annotation
	 */
	public boolean isAnnotationPresent(Class clazz, Class<? extends java.lang.annotation.Annotation> annotationType);

	/**
	 * Returns declared and generated annotations that this class is annotated with
	 *
	 * @param clazz The class to check
	 * @return An empty array if no annotations
	 */
	public Annotation[] getAnnotations(Class clazz);

	/**
	 * Returns the specific annotation
	 * @param clazz The class to check
	 * @param annotationType
	 * @return
	 */
	public Annotation getAnnotation(Class clazz, Class<? extends java.lang.annotation.Annotation> annotationType);

	/**
	 * Returns declared annotations that this class is annotated with
	 *
	 * @param clazz The class to check
	 * @return An empty array if no annotations
	 */
	public Annotation[] getDeclaredAnnotations(Class clazz);

	/**
	 * Returns
	 * @param clazz The class to check
	 * @param annotationType
	 * @return
	 */
	public Annotation getDeclaredAnnotation(Class clazz, Class<? extends java.lang.annotation.Annotation> annotationType);

	/**
	 * Returns an array of all (declared + compiler generated) constructors for a given class
	 *
	 * @param clazz The clazz to get constructors from
	 * @return A non-zero sized array of constructors
	 */
	public Constructor[] getConstructors(Class clazz);

	/**
	 * Returns an array of declared constructors for a given class
	 *
	 * @param clazz The clazz to get constructors from
	 * @return An empty array if there are no declared constructors
	 */
	public Constructor[] getDeclaredConstructors(Class clazz);

	/**
	 * Returns an array of all (declared + compiler generated) methods for a given class
	 *
	 * @param clazz The clazz to get methods from
	 * @return An empty array if no methods
	 */
	public Method[] getMethods(Class clazz);

	/**
	 * Returns an array of declared methods for a given class
	 *
	 * @param clazz The clazz to get methods from
	 * @return An empty array if no declared methods
	 */
	public Method[] getDeclaredMethods(Class clazz);

	/**
	 * Returns an array of all (declared + compiler generated) fields for a given class
	 *
	 * @param clazz The clazz to get fields from
	 * @return An empty array if no fields
	 */
	public Field[] getFields(Class clazz);

	/**
	 * Returns an array of declared fields for a given class
	 *
	 * @param clazz The clazz to get fields from
	 * @return An empty array if no declared fields
	 */
	public Field[] getDeclaredFields(Class clazz);

	/**
	 * Returns a declared or compiler generated field with a given name for a given class
	 *
	 * @param clazz     The clazz to get the field from
	 * @param fieldName The field name to retrieve
	 * @return The {@link Field} instance
	 * @throws ReflectionException Thrown if the field does not exist
	 */
	public Field getField(Class clazz, String fieldName) throws ReflectionException;

	/**
	 * Returns a declared field with a given name for a given class
	 *
	 * @param clazz     The clazz to get the field from
	 * @param fieldName The field name to retrieve
	 * @return The {@link Field} instance
	 * @throws ReflectionException Thrown if the field does not exist
	 */
	public Field getDeclaredField(Class clazz, String fieldName) throws ReflectionException;

	/**
	 * Returns if the provided class is an abstract class
	 *
	 * @param clazz The class to check
	 * @return True if abstract
	 */
	public boolean isAbstract(Class clazz);

	/**
	 * Returns if the provided class is an interface
	 *
	 * @param clazz The class to check
	 * @return True if interface
	 */
	public boolean isInterface(Class clazz);

	/**
	 * Returns if the provided class is an enum
	 *
	 * @param clazz The class to check
	 * @return True if interface
	 */
	public boolean isEnum(Class clazz);

	/**
	 * Returns if the provided class is a primitive
	 *
	 * @param clazz The class to check
	 * @return True if primitive
	 */
	public boolean isPrimitive(Class clazz);

	/**
	 * Returns if the provided class is an annotation
	 *
	 * @param clazz The class to check
	 * @return True if annotation
	 */
	public boolean isAnnotation(Class clazz);

	/**
	 * Returns if the provided class is an array
	 *
	 * @param clazz The class to check
	 * @return True if array
	 */
	public boolean isArray(Class clazz);

	/**
	 * Returns the class type of the elements of an array class
	 *
	 * @param clazz An array class
	 * @return Null if a non-array class is provided
	 */
	public Class arrayComponentType(Class clazz);

	/**
	 * Returns the value of an index in an array
	 *
	 * @param array The array instance
	 * @param index The index
	 * @return The object at the index or null if no object
	 */
	public Object arrayGet(Object array, int index);

	/**
	 * Sets the value of an index in an array
	 *
	 * @param array The array instance
	 * @param index The index
	 * @param value The value
	 */
	public void arraySet(Object array, int index, Object value);

	/**
	 * Returns the length of an array
	 *
	 * @param array The array instance
	 * @return The array length
	 */
	public int arrayLength(Object array);

	/**
	 * Creates a new array of elements with the type of the given class
	 *
	 * @param clazz The array element type
	 * @param size  The size of the array
	 * @return A new array instance
	 */
	public Object newArray(Class clazz, int size);
}
