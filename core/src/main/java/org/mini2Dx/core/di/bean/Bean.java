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
package org.mini2Dx.core.di.bean;

/**
 * A base class to bean facades during dependency injection
 */
public abstract class Bean {
	public abstract Object getInstance();

	/**
	 * Returns the key to be used for a given {@link Class}
	 * @param clazz The {@link Class} to get the key for
     * @param <T> The type of {@link Class}
	 * @return The {@link Class} unique key
	 */
	public static <T> String getClassKey(Class<T> clazz) {
		return clazz.getName();
	}
}
