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

import org.mini2Dx.core.di.bean.PrototypeBean;
import org.mini2Dx.gdx.utils.OrderedMap;

/**
 * An implementation of {@link OrderedMap} that produces prototypes when get() is called
 */
public class PrototypeInjectionMap<K, V> extends OrderedMap<K, V> {
	
	public PrototypeInjectionMap(OrderedMap<K, V> prototypes) {
		super(prototypes);
	}

	@Override
	public V get(K key, V defaultValue) {
		V prototype = super.get(key, defaultValue);

		try {
			return (V) PrototypeBean.duplicate(prototype);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultValue;
	}
}
