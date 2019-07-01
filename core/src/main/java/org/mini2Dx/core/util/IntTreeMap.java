/**
 * Copyright (c) 2018 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.util;

import org.mini2Dx.gdx.utils.IntArray;
import org.mini2Dx.gdx.utils.IntMap;

import java.util.NoSuchElementException;

/**
 * Extends {@link IntMap} to add {@link #ascendingKeys()} and {@link #descendingKeys()} methods
 * @param <V> The value type
 */
public class IntTreeMap<V> extends IntMap<V> {
	final IntArray sortedKeys;

	private SortedKeys ascKeys1, ascKeys2;
	private SortedKeys descKeys1, descKeys2;

	public IntTreeMap() {
		super();
		sortedKeys = new IntArray();
	}

	public IntTreeMap(int initialCapacity) {
		super(initialCapacity);
		sortedKeys = new IntArray(initialCapacity);
	}

	public IntTreeMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
		sortedKeys = new IntArray(initialCapacity);
	}

	public IntTreeMap(IntMap<? extends V> map) {
		super(map);
		sortedKeys = new IntArray(map.size);
	}

	@Override
	public V put(int key, V value) {
		final V result = super.put(key, value);
		if(result == null) {
			sortedKeys.add(key);
			sortedKeys.sort();
		}
		return result;
	}

	@Override
	public void putAll(IntMap<V> map) {
		super.putAll(map);

		Keys keys = map.keys();
		keys.reset();
		while(keys.hasNext) {
			sortedKeys.add(keys.next());
		}
		sortedKeys.sort();
	}

	@Override
	public V remove(int key) {
		final V result = super.remove(key);
		sortedKeys.removeValue(key);
		sortedKeys.sort();
		return result;
	}

	@Override
	public void clear() {
		sortedKeys.clear();
		super.clear();
	}

	@Override
	public void clear(int maximumCapacity) {
		sortedKeys.clear();
		super.clear(maximumCapacity);
	}

	public Keys ascendingKeys() {
		if (ascKeys1 == null) {
			ascKeys1 = new SortedKeys(this, true);
			ascKeys2 = new SortedKeys(this, true);
		}
		if (!ascKeys1.valid) {
			ascKeys1.reset();
			ascKeys1.valid = true;
			ascKeys2.valid = false;
			return ascKeys1;
		}
		ascKeys2.reset();
		ascKeys2.valid = true;
		ascKeys1.valid = false;
		return ascKeys2;
	}

	public Keys descendingKeys() {
		if (descKeys1 == null) {
			descKeys1 = new SortedKeys(this, false);
			descKeys2 = new SortedKeys(this, false);
		}
		if (!descKeys1.valid) {
			descKeys1.reset();
			descKeys1.valid = true;
			descKeys2.valid = false;
			return descKeys1;
		}
		descKeys2.reset();
		descKeys2.valid = true;
		descKeys1.valid = false;
		return descKeys2;
	}

	public static class SortedKeys extends Keys {
		private final IntTreeMap map;

		private boolean ascending;
		boolean valid = true;
		int index = 0;

		public SortedKeys(IntTreeMap map, boolean ascending) {
			super(map);
			this.map = map;
			this.ascending = ascending;
			reset();
		}

		private void initHasNext() {
			if(map == null) {
				return;
			}
			if(ascending) {
				hasNext = index < map.sortedKeys.size;
			} else {
				hasNext = index >= 0 && index < map.sortedKeys.size;
			}
		}

		@Override
		public int next() {
			if (!hasNext) throw new NoSuchElementException();
			final int result = map.sortedKeys.get(index);
			if(ascending) {
				index++;
			} else {
				index--;
			}
			initHasNext();
			return result;
		}

		@Override
		public void reset() {
			super.reset();
			if(map != null) {
				index = ascending ? 0 : map.sortedKeys.size - 1;
			}
			initHasNext();
		}
	}
}
