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
package org.mini2Dx.core;

import org.mini2Dx.core.di.BeanUtils;
import org.mini2Dx.core.di.ComponentScanner;
import org.mini2Dx.core.di.annotation.Autowired;
import org.mini2Dx.core.di.annotation.Prototype;
import org.mini2Dx.core.di.annotation.Singleton;
import org.mini2Dx.core.di.bean.Bean;
import org.mini2Dx.core.di.injection.BeanInjector;
import org.mini2Dx.gdx.utils.OrderedMap;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Provides cross-platform dependency injection
 */
public class DependencyInjection {
    private final OrderedMap<String, Object> presetSingletons = new OrderedMap<String, Object>();
    private final OrderedMap<String, Object> presetPrototypes = new OrderedMap<String, Object>();

    private final BeanUtils beanUtils;
    private final ComponentScanner componentScanner;

    private OrderedMap<String, Bean> beans;
    private ExecutorService prototypeService;

    public DependencyInjection(BeanUtils beanUtils, ComponentScanner componentScanner) {
        this.beanUtils = beanUtils;
        this.componentScanner = componentScanner;
    }

    /**
     * Manually set a singleton before calling {@link #scan(String...)}
     *
     * @param ref
     *            The singleton object
     * @param clazz
     *            The {@link Class} of the singleton
     * @param <T> The type of {@link Class}
     */
    public <T> void presetSingleton(T ref, Class<T> clazz) {
        presetSingletons.put(Bean.getClassKey(clazz), ref);
    }

    /**
     * Manually set a singleton before calling {@link #scan(String...)}
     *
     * @param clazz
     *            The {@link Class} of the singleton
     * @param <T> The type of {@link Class}
     * @throws Exception
     *             Thrown if the object could not be instantiated
     */
    public <T> void presetSingleton(Class<T> clazz) throws Exception {
        presetSingletons.put(Bean.getClassKey(clazz), clazz.newInstance());
    }

    /**
     * Manually set a prototype before calling {@link #scan(String...)}
     *
     * @param clazz
     *            The {@link Class} of the prototype
     * @param <T> The type of {@link Class}
     * @throws Exception
     *             Thrown if the object could not be instantiated
     */
    public <T> void presetPrototype(Class<T> clazz) throws Exception {
        presetPrototypes.put(Bean.getClassKey(clazz), clazz.newInstance());
    }

    /**
     * Scans a set of packages, creates all required {@link Singleton} and
     * {@link Prototype} instances and processes all {@link Autowired}
     * annotations
     *
     * @param packageNames
     *            The names of packages to scan
     * @throws Exception
     *             Thrown if a class could not be instantiated or if autowired
     *             could not be completed
     */
    public void scan(String... packageNames) throws Exception {
        componentScanner.scan(packageNames);

        OrderedMap<String, Object> singletons = new OrderedMap<String, Object>(presetSingletons);
        OrderedMap<String, Object> prototypes = new OrderedMap<String, Object>(presetPrototypes);

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
        if (beans == null)
            return null;
        if (!beans.containsKey(classKey))
            return null;
        return (T) beans.get(classKey).getInstance();
    }

    public BeanUtils beanUtils() {
        return beanUtils;
    }
}
