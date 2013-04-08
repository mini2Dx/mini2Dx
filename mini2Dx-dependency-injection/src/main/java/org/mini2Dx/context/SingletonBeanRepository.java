/**
 * Copyright (c) 2013, mini2Dx Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.context;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.mini2Dx.injection.Autowired;
import org.mini2Dx.injection.exceptions.BeanException;
import org.mini2Dx.injection.exceptions.BeanInitialisationException;
import org.mini2Dx.injection.exceptions.NoSuchBeanException;
import org.mini2Dx.injection.exceptions.RepositoryAccessException;

/**
 * A repository for storing and autowiring singletons
 * 
 * To perform any write operations to the repository the open() method must be
 * called first. Once writing is finished, the close() method must called.
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Singleton_pattern">Singleton
 *      Pattern</a>
 * @see <a href="http://en.wikipedia.org/wiki/Dependency_injection">Dependency
 *      Injection</a>
 * 
 * @author Thomas Cashman
 */
public class SingletonBeanRepository {
	private static Map<String, Object> singletons = new HashMap<String, Object>();
	private static boolean isOpen = false;

	/**
	 * Opens the repository for accepting beans
	 */
	public static void open() {
		isOpen = true;
	}

	/**
	 * Clears the repository
	 */
	public static void clear() {
		if (!isOpen) {
			throw new RepositoryAccessException(
					"Cannot clear repository when it is closed.");
		}
		singletons.clear();
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz) {
		if (isOpen) {
			throw new RepositoryAccessException(
					"Cannot retrieve bean while repository is open.");
		}

		String key = getBeanKey(clazz);
		if (singletons.containsKey(key)) {
			return (T) singletons.get(key);
		}
		return null;
	}

	public static <T> void registerBean(Class<T> clazz) throws BeanInitialisationException {
		if (!isOpen) {
			throw new RepositoryAccessException(
					"Cannot register bean while repository is closed.");
		}
		try {
			String key = getBeanKey(clazz);
			singletons.put(key, clazz.newInstance());
		} catch (InstantiationException e) {
			throw new BeanInitialisationException(clazz.getSimpleName());
		} catch (IllegalAccessException e) {
			throw new BeanInitialisationException(clazz.getSimpleName());
		}
	}

	public static void registerBean(Object object) {
		if (!isOpen) {
			throw new RepositoryAccessException(
					"Cannot register bean while repository is closed.");
		}
		String key = getBeanKey(object.getClass());
		singletons.put(key, object);
	}

	/**
	 * Closes the repository and executes autowiring on all registered beans
	 */
	public static void close() throws BeanException {
		if (!isOpen) {
			throw new RepositoryAccessException(
					"Cannot closed a repository that is not opened.");
		}

		try {
			for (String key : singletons.keySet()) {
				Object object = singletons.get(key);
				for (Field field : object.getClass().getDeclaredFields()) {
					field.setAccessible(true);
					Autowired autowireAnnotaiton = field
							.getAnnotation(Autowired.class);
					if (autowireAnnotaiton != null) {
						Class<?> clazz = field.getType();

						/* Injecting a class */
						String clazzKey = getBeanKey(clazz);
						if (singletons.containsKey(clazzKey)) {
							Object dependency = singletons.get(clazzKey);
							field.set(object, dependency);
						} else if (clazz.isInterface()) {
							boolean found = false;
							/*
							 * Injecting a dependency implementation for an
							 * interface
							 */
							for (String singletonKey : singletons.keySet()) {
								if (singletonKey.compareTo(key) != 0) {
									Object singleton = singletons
											.get(singletonKey);

									for (Class<?> interfaceImpl : singleton
											.getClass().getInterfaces()) {
										if (interfaceImpl.equals(clazz)) {
											field.set(object, singleton);
											found = true;
											break;
										}
									}

									if (found) {
										break;
									}
								}
							}

							if (!found && autowireAnnotaiton.required()) {
								throw new NoSuchBeanException(object.getClass()
										.getSimpleName(), field.getName(),
										clazz.getSimpleName());
							}
						} else if(autowireAnnotaiton.required()) {
							throw new NoSuchBeanException(object.getClass()
									.getSimpleName(), field.getName(),
									clazz.getSimpleName());
						}
					}
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchBeanException e) {
			throw e;
		}

		isOpen = false;
	}

	private static <T> String getBeanKey(Class<T> clazz) {
		return clazz.getPackage().getName() + clazz.getSimpleName();
	}
}
