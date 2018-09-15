/**
 * Copyright (c) 2018 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice, this
 *   list of conditions and the following disclaimer in the documentation and/or
 *   other materials provided with the distribution.
 *
 * * Neither the name of mini2Dx nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.di.injection;

import org.mini2Dx.core.di.annotation.Autowired;
import org.mini2Dx.core.di.annotation.PostInject;
import org.mini2Dx.core.di.bean.Bean;
import org.mini2Dx.core.di.bean.PrototypeBean;
import org.mini2Dx.core.di.bean.SingletonBean;
import org.mini2Dx.core.exception.NoSuchBeanException;
import org.mini2Dx.core.exception.PostInjectException;
import org.mini2Dx.gdx.utils.OrderedMap;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;

/**
 * Injects beans into each other
 */
public class BeanInjector {
	private OrderedMap<String, Object> singletons;
	private OrderedMap<String, Object> prototypes;
	private OrderedMap<String, NoSuchBeanException> exceptions;

	public BeanInjector(OrderedMap<String, Object> singletons, OrderedMap<String, Object> prototypes) {
		this.singletons = singletons;
		this.prototypes = prototypes;
		this.exceptions = new OrderedMap<String, NoSuchBeanException>();
	}

	public void inject() throws NoSuchBeanException, IllegalArgumentException, IllegalAccessException {
		injectSingletons();
		injectPrototypes();
		checkInjectionSuccessful();

		for (String key : prototypes.keys()) {
			Object object = prototypes.get(key);
			try {
				invokePostInject(object);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		for (String key : singletons.keys()) {
			Object object = singletons.get(key);
			try {
				invokePostInject(object);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		if (exceptions.size > 0) {
			for (String key : exceptions.keys()) {
				throw exceptions.get(key);
			}
		}
	}

	public OrderedMap<String, Bean> getInjectionResult(ExecutorService prototypeExecutorService) {
		OrderedMap<String, Bean> result = new OrderedMap<String, Bean>();

		for (String key : singletons.keys()) {
			Object object = singletons.get(key);
			result.put(key, new SingletonBean(object));
		}

		for (String key : prototypes.keys()) {
			Object object = prototypes.get(key);
			PrototypeBean prototypeBean = new PrototypeBean(object, prototypeExecutorService);
			prototypeExecutorService.submit(prototypeBean);
			result.put(key, prototypeBean);
		}

		return result;
	}

	private void invokePostInject(Object object)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		for (Method method : object.getClass().getMethods()) {
			if (!method.isAnnotationPresent(PostInject.class)) {
				continue;
			}
			if (method.getParameterTypes().length > 0) {
				throw new PostInjectException();
			}
			method.invoke(object);
		}
	}

	private void injectPrototypes() throws NoSuchBeanException, IllegalArgumentException, IllegalAccessException {
		OrderedMap<String, Object> prototypeInjectionMap = new PrototypeInjectionMap(prototypes);

		for (String key : prototypes.keys()) {
			Object object = prototypes.get(key);
			inject(object, key, prototypeInjectionMap);
		}

		for (String key : singletons.keys()) {
			Object object = singletons.get(key);
			inject(object, key, prototypeInjectionMap);
		}
	}

	private void injectSingletons() throws NoSuchBeanException, IllegalArgumentException, IllegalAccessException {
		for (String key : singletons.keys()) {
			Object object = singletons.get(key);
			inject(object, key, singletons);
		}

		for (String key : prototypes.keys()) {
			Object object = prototypes.get(key);
			inject(object, key, singletons);
		}
	}
	
	private void checkInjectionSuccessful() throws NoSuchBeanException, IllegalArgumentException, IllegalAccessException {
		for (String key : singletons.keys()) {
			Object object = singletons.get(key);
			checkInjectionSuccessful(object, key);
		}

		for (String key : prototypes.keys()) {
			Object object = prototypes.get(key);
			checkInjectionSuccessful(object, key);
		}
	}

	private void checkInjectionSuccessful(Object object, String objectKey)
			throws NoSuchBeanException, IllegalArgumentException, IllegalAccessException {
		Class<?> currentClass = object.getClass();
		while (!currentClass.equals(Object.class)) {
			for (Field field : currentClass.getDeclaredFields()) {
				field.setAccessible(true);

				Autowired autowireAnnotaiton = field.getAnnotation(Autowired.class);
				Object value = field.get(object);

				if (autowireAnnotaiton == null) {
					continue;
				}
				if (value != null) {
					continue;
				}
				if (!autowireAnnotaiton.required()) {
					continue;
				}
				Class<?> clazz = field.getType();
				String clazzKey = Bean.getClassKey(clazz);
				exceptions.put(clazzKey, new NoSuchBeanException(object.getClass().getSimpleName(), field.getName(),
						clazz.getSimpleName()));
			}
			currentClass = currentClass.getSuperclass();
		}
	}

	private void inject(Object object, String objectKey, OrderedMap<String, Object> beans)
			throws NoSuchBeanException, IllegalArgumentException, IllegalAccessException {
		Class<?> currentClass = object.getClass();
		while (!currentClass.equals(Object.class)) {
			for (Field field : currentClass.getDeclaredFields()) {
				field.setAccessible(true);
				Autowired autowireAnnotaiton = field.getAnnotation(Autowired.class);

				Object value = field.get(object);

				if (autowireAnnotaiton == null) {
					continue;
				}
				if (value != null) {
					continue;
				}

				Class<?> clazz = field.getType();

				/* Injecting a class */
				String clazzKey = Bean.getClassKey(clazz);
				if (beans.containsKey(clazzKey)) {
					Object dependency = beans.get(clazzKey);
					field.set(object, dependency);
				} else if (clazz.isInterface()) {
					boolean found = false;
					/*
					 * Injecting a dependency implementation for an interface
					 */
					for (String beanKey : beans.keys()) {
						if (beanKey.compareTo(objectKey) != 0) {
							Object beanToInject = beans.get(beanKey);

							for (Class<?> interfaceImpl : beanToInject.getClass().getInterfaces()) {
								if (interfaceImpl.equals(clazz)) {
									field.set(object, beanToInject);
									found = true;
									break;
								}
							}

							if (found) {
								break;
							}
						}
					}
				}
			}
			currentClass = currentClass.getSuperclass();
		}
	}
}
