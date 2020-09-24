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
package org.mini2Dx.core.collections;

import org.mini2Dx.gdx.utils.ObjectIntMap;
import org.mini2Dx.gdx.utils.ObjectMap;

/**
 * Extends {@link ObjectMap} to implement least-recently used capabilities.
 * Once the specified maximum capacity is reached,
 * the key that has been used the least is removed from the map.
 *
 * @param <K> The key type
 * @param <V> The value type
 */
public class LruObjectMap<K, V> extends ObjectMap<K, V> {
	public static final int DEFAULT_MAX_CAPACITY = 128;

	private final int maxCapacity;
	private final ObjectIntMap<K> accessCounter = new ObjectIntMap<K>();

	public LruObjectMap() {
		super();
		maxCapacity = DEFAULT_MAX_CAPACITY;
	}

	public LruObjectMap(int initialCapacity) {
		this(initialCapacity, DEFAULT_MAX_CAPACITY);
	}

	public LruObjectMap(int initialCapacity, float loadFactor) {
		this(initialCapacity, DEFAULT_MAX_CAPACITY, loadFactor);
	}

	public LruObjectMap(ObjectMap<? extends K, ? extends V> map) {
		this(map, DEFAULT_MAX_CAPACITY);
	}

	public LruObjectMap(int initialCapacity, int maxCapacity) {
		super(initialCapacity);
		this.maxCapacity = maxCapacity;
	}

	public LruObjectMap(int initialCapacity, int maxCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
		this.maxCapacity = maxCapacity;
	}

	public LruObjectMap(ObjectMap<? extends K, ? extends V> map, int maxCapacity) {
		super(map);
		this.maxCapacity = maxCapacity;
	}

	@Override
	public V put(K key, V value) {
		while(super.size >= maxCapacity) {
			purge();
		}

		final V result = super.put(key, value);
		accessCounter.getAndIncrement(key, -1, 1);
		return result;
	}

	@Override
	public void putAll(ObjectMap<? extends K, ? extends V> map) {
		while(super.size >= maxCapacity) {
			purge();
		}

		super.putAll(map);
	}

	@Override
	public <T extends K> V get(T key) {
		final V result = super.get(key);
		accessCounter.getAndIncrement(key, 0, 1);
		return result;
	}

	@Override
	public V get(K key, V defaultValue) {
		final V result = super.get(key, defaultValue);
		accessCounter.getAndIncrement(key, 0, 1);
		return result;
	}

	@Override
	public V remove(K key) {
		final V result = super.remove(key);
		accessCounter.remove(key, -1);
		return result;
	}

	@Override
	public void clear() {
		super.clear();
		accessCounter.clear();
	}

	private void purge() {
		K smallest = null;
		int smallestCount = Integer.MAX_VALUE;

		for(K key : accessCounter.keys()) {
			final int count = accessCounter.get(key, 0);
			if(count < smallestCount) {
				smallest = key;
				smallestCount = count;
			}
		}

		if(smallest == null) {
			return;
		}
		remove(smallest);
	}

	/**
	 * Returns the maximum number of keys that can be stored in the {@link LruObjectMap}
	 * @return Defaults to 128
	 */
	public int getMaxCapacity() {
		return maxCapacity;
	}
}
