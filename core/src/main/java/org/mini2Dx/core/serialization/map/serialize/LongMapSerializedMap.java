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
package org.mini2Dx.core.serialization.map.serialize;

import org.mini2Dx.core.collections.LongArrayIterable;
import org.mini2Dx.gdx.utils.LongArray;
import org.mini2Dx.gdx.utils.LongMap;

/**
 * Utility class used during JSON/XML serialization
 */
public class LongMapSerializedMap extends SerializedMap<LongMap> {
	private static final LongArray KEYS_TMP = new LongArray(1);

	public LongMapSerializedMap(LongMap map) {
		super(map);
		KEYS_TMP.clear();
		KEYS_TMP.addAll(map.keys().toArray());
	}

	@Override
	public Object get(Object key) {
		return map.get((int) key);
	}

	@Override
	public int getSize() {
		return map.size;
	}

	@Override
	public Iterable keys() {
		return new LongArrayIterable(KEYS_TMP) ;
	}
}
