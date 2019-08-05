/**
 * Copyright 2017 Thomas Cashman
 */
package org.mini2Dx.core.di.dummy;

import org.mini2Dx.core.di.annotation.Autowired;
import org.mini2Dx.core.di.annotation.Singleton;

/**
 *
 */
@Singleton
public class TestBeanA {
    @Autowired
    private TestPrototypeBean prototypeBean;

    public TestPrototypeBean getPrototypeBean() {
        return prototypeBean;
    }
}
