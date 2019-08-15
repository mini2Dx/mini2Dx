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

import org.mini2Dx.gdx.utils.ByteArray;

import java.util.NoSuchElementException;

public class ByteTreeMap<V> extends ByteMap<V> {
    final ByteArray sortedKeys;

    private ByteTreeMap.SortedKeys ascKeys1, ascKeys2;
    private ByteTreeMap.SortedKeys descKeys1, descKeys2;

    public ByteTreeMap() {
        super();
        sortedKeys = new ByteArray();
    }

    public ByteTreeMap(int initialCapacity) {
        super(initialCapacity);
        sortedKeys = new ByteArray(initialCapacity);
    }

    public ByteTreeMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        sortedKeys = new ByteArray(initialCapacity);
    }

    public ByteTreeMap(ByteMap<? extends V> map) {
        super(map);
        sortedKeys = new ByteArray(map.size);
    }

    @Override
    public V put(byte key, V value) {
        final V result = super.put(key, value);
        if(result == null) {
            sortedKeys.add(key);
            sortedKeys.sort();
        }
        return result;
    }

    @Override
    public void putAll(ByteMap<V> map) {
        super.putAll(map);

        Keys keys = map.keys();
        keys.reset();
        while(keys.hasNext) {
            sortedKeys.add(keys.next());
        }
        sortedKeys.sort();
    }

    @Override
    public V remove(byte key) {
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
            ascKeys1 = new ByteTreeMap.SortedKeys(this, true);
            ascKeys2 = new ByteTreeMap.SortedKeys(this, true);
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
            descKeys1 = new ByteTreeMap.SortedKeys(this, false);
            descKeys2 = new ByteTreeMap.SortedKeys(this, false);
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
        private final ByteTreeMap map;

        private boolean ascending;
        boolean valid = true;
        int index = 0;

        public SortedKeys(ByteTreeMap map, boolean ascending) {
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
        public byte next() {
            if (!hasNext) throw new NoSuchElementException();
            final byte result = map.sortedKeys.get(index);
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
