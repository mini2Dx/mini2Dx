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

import org.mini2Dx.core.di.BeanUtils;
import org.mini2Dx.core.di.ComponentScanner;
import org.mini2Dx.core.di.annotation.Autowired;
import org.mini2Dx.core.di.annotation.Prototype;
import org.mini2Dx.core.di.annotation.Singleton;
import org.mini2Dx.core.di.bean.Bean;
import org.mini2Dx.core.di.injection.BeanInjector;
import org.mini2Dx.gdx.utils.OrderedMap;

/**
 * Provides cross-platform dependency injection
 */
public class DependencyInjection {
    private final OrderedMap<String, Object> presetSingletons = new OrderedMap<String, Object>();
    private final OrderedMap<String, Object> presetPrototypes = new OrderedMap<String, Object>();

    private final BeanUtils beanUtils = new BeanUtils();
    private final ComponentScanner componentScanner;

    private OrderedMap<String, Bean> beans;

    public DependencyInjection(ComponentScanner componentScanner) {
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

        beans = injector.getInjectionResult();
    }

    public <T> T getBean(Class<T> clazz) {
        String classKey = Bean.getClassKey(clazz);
        if (beans == null)
            return null;
        if (!beans.containsKey(classKey))
            return null;
        return (T) beans.get(classKey).getInstance();
    }

    public ComponentScanner getComponentScanner() {
        return componentScanner;
    }

    public BeanUtils beanUtils() {
        return beanUtils;
    }
}
