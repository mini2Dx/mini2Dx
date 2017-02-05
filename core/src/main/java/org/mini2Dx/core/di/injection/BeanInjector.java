/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.di.injection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.mini2Dx.core.di.annotation.Autowired;
import org.mini2Dx.core.di.annotation.PostInject;
import org.mini2Dx.core.di.bean.Bean;
import org.mini2Dx.core.di.bean.PrototypeBean;
import org.mini2Dx.core.di.bean.SingletonBean;
import org.mini2Dx.core.di.exception.NoSuchBeanException;
import org.mini2Dx.core.di.exception.PostInjectException;

/**
 * Injects beans into each other
 */
public class BeanInjector {
	private Map<String, Object> singletons;
	private Map<String, Object> prototypes;
	private Map<String, NoSuchBeanException> exceptions;

	public BeanInjector(Map<String, Object> singletons,
			Map<String, Object> prototypes) {
		this.singletons = singletons;
		this.prototypes = prototypes;
		this.exceptions = new HashMap<String, NoSuchBeanException>();
	}

	public void inject() throws NoSuchBeanException, IllegalArgumentException,
			IllegalAccessException {
		injectSingletons();
		injectPrototypes();
		
		for (String key : prototypes.keySet()) {
			Object object = prototypes.get(key);
			try {
				invokePostInject(object);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		for (String key : singletons.keySet()) {
			Object object = singletons.get(key);
			try {
				invokePostInject(object);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		if (exceptions.size() > 0) {
			for (String key : exceptions.keySet()) {
				throw exceptions.get(key);
			}
		}
	}

	public Map<String, Bean> getInjectionResult(
			ExecutorService prototypeExecutorService) {
		Map<String, Bean> result = new HashMap<String, Bean>();

		for (String key : singletons.keySet()) {
			Object object = singletons.get(key);
			result.put(key, new SingletonBean(object));
			
		}

		for (String key : prototypes.keySet()) {
			Object object = prototypes.get(key);
			PrototypeBean prototypeBean = new PrototypeBean(object,
					prototypeExecutorService);
			prototypeExecutorService.submit(prototypeBean);
			result.put(key, prototypeBean);
		}

		return result;
	}
	
	private void invokePostInject(Object object) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		for(Method method : object.getClass().getMethods()) {
			if(!method.isAnnotationPresent(PostInject.class)) {
				continue;
			}
			if(method.getParameterTypes().length > 0) {
				throw new PostInjectException();
			}
			method.invoke(object);
		}
	}

	private void injectPrototypes() throws NoSuchBeanException,
			IllegalArgumentException, IllegalAccessException {
		Map<String, Object> prototypeInjectionMap = new PrototypeInjectionMap(
				prototypes);

		for (String key : prototypes.keySet()) {
			Object object = prototypes.get(key);
			inject(object, key, prototypeInjectionMap);
		}

		for (String key : singletons.keySet()) {
			Object object = singletons.get(key);
			inject(object, key, prototypeInjectionMap);
		}
	}

	private void injectSingletons() throws NoSuchBeanException,
			IllegalArgumentException, IllegalAccessException {
		for (String key : singletons.keySet()) {
			Object object = singletons.get(key);
			inject(object, key, singletons);
		}

		for (String key : prototypes.keySet()) {
			Object object = prototypes.get(key);
			inject(object, key, singletons);
		}
	}

	private void inject(Object object, String objectKey,
			Map<String, Object> beans) throws NoSuchBeanException,
			IllegalArgumentException, IllegalAccessException {
		Class<?> currentClass = object.getClass();
		while(!currentClass.equals(Object.class)) {
			for (Field field : currentClass.getDeclaredFields()) {
				field.setAccessible(true);
				Autowired autowireAnnotaiton = field.getAnnotation(Autowired.class);
				
				Object value = field.get(object);
				
				if (autowireAnnotaiton != null && value == null) {
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
						for (String beanKey : beans.keySet()) {
							if (beanKey.compareTo(objectKey) != 0) {
								Object beanToInject = beans.get(beanKey);

								for (Class<?> interfaceImpl : beanToInject
										.getClass().getInterfaces()) {
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

						if (!found && autowireAnnotaiton.required()) {
							exceptions.put(clazzKey, new NoSuchBeanException(object
									.getClass().getSimpleName(), field.getName(),
									clazz.getSimpleName()));
						} else if (found) {
							exceptions.remove(clazzKey);
						}
					} else if (autowireAnnotaiton.required()) {
						exceptions.put(
								clazzKey,
								new NoSuchBeanException(object.getClass()
										.getSimpleName(), field.getName(), clazz
										.getSimpleName()));
					}
				}
			}
			currentClass = currentClass.getSuperclass();
		}
	}
}
