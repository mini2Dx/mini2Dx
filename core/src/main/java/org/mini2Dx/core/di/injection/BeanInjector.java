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
package org.mini2Dx.core.di.injection;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.di.annotation.Autowired;
import org.mini2Dx.core.di.annotation.PostInject;
import org.mini2Dx.core.di.bean.Bean;
import org.mini2Dx.core.di.bean.PrototypeBean;
import org.mini2Dx.core.di.bean.SingletonBean;
import org.mini2Dx.core.exception.NoSuchBeanException;
import org.mini2Dx.core.exception.PostInjectException;
import org.mini2Dx.core.exception.ReflectionException;
import org.mini2Dx.core.reflect.Annotation;
import org.mini2Dx.core.reflect.Field;
import org.mini2Dx.core.reflect.Method;
import org.mini2Dx.gdx.utils.OrderedMap;

import java.util.concurrent.ExecutorService;

/**
 * Injects beans into each other
 */
public class BeanInjector {
	private static final String LOGGING_TAG = BeanInjector.class.getSimpleName();

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
			} catch (ReflectionException e) {
				Mdx.log.error(LOGGING_TAG, e.getMessage(), e);
			}
		}

		for (String key : singletons.keys()) {
			Object object = singletons.get(key);
			try {
				invokePostInject(object);
			} catch (ReflectionException e) {
				Mdx.log.error(LOGGING_TAG, e.getMessage(), e);
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

	private void invokePostInject(Object object) throws IllegalArgumentException {
		for (Method method : Mdx.reflect.getMethods(object.getClass())) {
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
			for (Field field : Mdx.reflect.getDeclaredFields(currentClass)) {
				final Annotation annotation = field.getDeclaredAnnotation(Autowired.class);
				if (annotation == null) {
					continue;
				}
				Autowired autowireAnnotaiton = annotation.getAnnotation(Autowired.class);
				Object value = field.get(object);

				if(autowireAnnotaiton == null) {
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
			for (Field field : Mdx.reflect.getDeclaredFields(currentClass)) {
				final Annotation annotation = field.getDeclaredAnnotation(Autowired.class);
				if (annotation == null) {
					continue;
				}

				final Autowired autowireAnnotaiton = annotation.getAnnotation(Autowired.class);
				final Object value = field.get(object);

				if(autowireAnnotaiton == null) {
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
