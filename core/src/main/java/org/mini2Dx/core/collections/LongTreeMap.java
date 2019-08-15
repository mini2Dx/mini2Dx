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

import org.mini2Dx.gdx.utils.LongArray;
import org.mini2Dx.gdx.utils.LongMap;

import java.util.NoSuchElementException;

public class LongTreeMap<V> extends LongMap<V> {
    final LongArray sortedKeys;

    private LongTreeMap.SortedKeys ascKeys1, ascKeys2;
    private LongTreeMap.SortedKeys descKeys1, descKeys2;

    public LongTreeMap() {
        super();
        sortedKeys = new LongArray();
    }

    public LongTreeMap(int initialCapacity) {
        super(initialCapacity);
        sortedKeys = new LongArray(initialCapacity);
    }

    public LongTreeMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        sortedKeys = new LongArray(initialCapacity);
    }

    public LongTreeMap(LongMap<? extends V> map) {
        super(map);
        sortedKeys = new LongArray(map.size);
    }

    @Override
    public V put(long key, V value) {
        final V result = super.put(key, value);
        if(result == null) {
            sortedKeys.add(key);
            sortedKeys.sort();
        }
        return result;
    }

    @Override
    public void putAll(LongMap<V> map) {
        super.putAll(map);

        LongMap.Keys keys = map.keys();
        keys.reset();
        while(keys.hasNext) {
            sortedKeys.add(keys.next());
        }
        sortedKeys.sort();
    }

    @Override
    public V remove(long key) {
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

    public LongTreeMap.Keys ascendingKeys() {
        if (ascKeys1 == null) {
            ascKeys1 = new LongTreeMap.SortedKeys(this, true);
            ascKeys2 = new LongTreeMap.SortedKeys(this, true);
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

    public LongTreeMap.Keys descendingKeys() {
        if (descKeys1 == null) {
            descKeys1 = new LongTreeMap.SortedKeys(this, false);
            descKeys2 = new LongTreeMap.SortedKeys(this, false);
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

    public static class SortedKeys extends LongMap.Keys {
        private final LongTreeMap map;

        private boolean ascending;
        boolean valid = true;
        int index = 0;

        public SortedKeys(LongTreeMap map, boolean ascending) {
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
        public long next() {
            if (!hasNext) throw new NoSuchElementException();
            final long result = map.sortedKeys.get(index);
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
