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
package org.mini2Dx.core.di;

import org.mini2Dx.core.di.annotation.Prototype;
import org.mini2Dx.core.di.annotation.Singleton;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.gdx.utils.Array;

import java.io.IOException;

/**
 * A common interface to component scanning implementations.
 *
 * Scans packages for classes annotated with {@link Singleton} and
 * {@link Prototype}
 */
public interface ComponentScanner {
    /**
     * Scans multiple packages recursively for {@link Singleton} and {@link Prototype}
     * annotated classes
     *
     * @param packageNames  The package name to scan through, e.g. org.mini2Dx.component
     * @throws MdxException Thrown if an exception occurs during scanning
     */
    public void scan(String[] packageNames) throws MdxException, IOException;

    public Array<Class<?>> getSingletonClasses();

    public Array<Class<?>> getPrototypeClasses();
}
