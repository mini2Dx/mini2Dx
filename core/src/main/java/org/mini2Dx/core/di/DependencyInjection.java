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
package org.mini2Dx.core.di;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.mini2Dx.core.di.bean.Bean;
import org.mini2Dx.core.di.injection.BeanInjector;

/**
 * Provides dependency injection to mini2Dx games
 * 
 * @author Thomas Cashman
 */
@SuppressWarnings(value = { "unchecked", "rawtypes" })
public class DependencyInjection {
	private final ComponentScanner componentScanner;
	private Map<String, Bean> beans;
	private ExecutorService prototypeService;
	
	public DependencyInjection(ComponentScanner componentScanner) {
		this.componentScanner = componentScanner;
	}

	public void scan(String[] packageNames) throws Exception {
		componentScanner.scan(packageNames);

		Map<String, Object> singletons = new HashMap<String, Object>();
		Map<String, Object> prototypes = new HashMap<String, Object>();

		for (Class clazz : componentScanner.getSingletonClasses()) {
			String key = Bean.getClassKey(clazz);
			singletons.put(key, clazz.newInstance());
		}

		for (Class clazz : componentScanner.getPrototypeClasses()) {
			String key = Bean.getClassKey(clazz);
			prototypes.put(key, clazz.newInstance());
		}
		
		BeanInjector injector = new BeanInjector(singletons, prototypes);
		injector.inject();
		
		prototypeService = Executors.newFixedThreadPool(1);
		beans = injector.getInjectionResult(prototypeService);
	}
	
	public void shutdown() {
		prototypeService.shutdown();
	}

	public <T> T getBean(Class<T> clazz) {
		String classKey = Bean.getClassKey(clazz);
		if(beans == null)
			return null;
		if(!beans.containsKey(classKey))
			return null;
		return (T) beans.get(classKey).getInstance();
	}
}
