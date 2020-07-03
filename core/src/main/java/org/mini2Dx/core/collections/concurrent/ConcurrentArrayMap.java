/*******************************************************************************
 * Copyright 2020 See AUTHORS file
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
package org.mini2Dx.core.collections.concurrent;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.lock.ReadWriteLock;
import org.mini2Dx.gdx.utils.ArrayMap;
import org.mini2Dx.gdx.utils.Collections;

public class ConcurrentArrayMap<K, V> extends ArrayMap<K, V> implements ConcurrentCollection {

    protected ReadWriteLock lock = Mdx.locks.newReadWriteLock();

    /**
     * Creates an ordered map with a capacity of 16.
     */
    public ConcurrentArrayMap() {
    }

    /**
     * Creates an ordered map with the specified capacity.
     *
     * @param capacity
     */
    public ConcurrentArrayMap(int capacity) {
        super(capacity);
    }

    /**
     * @param ordered  If false, methods that remove elements may change the order of other elements in the arrays, which avoids a
     *                 memory copy.
     * @param capacity Any elements added beyond this will cause the backing arrays to be grown.
     */
    public ConcurrentArrayMap(boolean ordered, int capacity) {
        super(ordered, capacity);
    }

    /**
     * Creates a new map with {@link #keys} and {@link #values} of the specified type.
     *
     * @param ordered        If false, methods that remove elements may change the order of other elements in the arrays, which avoids a
     *                       memory copy.
     * @param capacity       Any elements added beyond this will cause the backing arrays to be grown.
     * @param keyArrayType
     * @param valueArrayType
     */
    public ConcurrentArrayMap(boolean ordered, int capacity, Class keyArrayType, Class valueArrayType) {
        super(ordered, capacity, keyArrayType, valueArrayType);
    }

    /**
     * Creates an ordered map with {@link #keys} and {@link #values} of the specified type and a capacity of 16.
     *
     * @param keyArrayType
     * @param valueArrayType
     */
    public ConcurrentArrayMap(Class keyArrayType, Class valueArrayType) {
        super(keyArrayType, valueArrayType);
    }

    /**
     * Creates a new map containing the elements in the specified map. The new map will have the same type of backing arrays and
     * will be ordered if the specified map is ordered. The capacity is set to the number of elements, so any subsequent elements
     * added will cause the backing arrays to be grown.
     *
     * NOTE: read access to the other ArrayMap is not thread-safe
     *
     * @param array
     */
    public ConcurrentArrayMap(ArrayMap array) {
        super(array);
    }

    @Override
    public int put(K key, V value) {
        lock.lockWrite();
        int i = super.put(key, value);
        lock.unlockWrite();
        return i;
    }

    @Override
    public int put(K key, V value, int index) {
        lock.lockWrite();
        int i = super.put(key, value, index);
        lock.unlockWrite();
        return i;
    }

    @Override
    public void putAll(ArrayMap<? extends K, ? extends V> map, int offset, int length) {
        boolean isOtherConcurrent = map instanceof ConcurrentCollection;
        if (isOtherConcurrent){
            ((ConcurrentCollection)map).getLock().lockRead();
        }
        lock.lockWrite();
        super.putAll(map, offset, length);
        lock.unlockWrite();
        if (isOtherConcurrent){
            ((ConcurrentCollection)map).getLock().unlockRead();
        }
    }

    /**
     * Returns the value (which may be null) for the specified key, or null if the key is not in the map. Note this does a
     * .equals() comparison of each key in reverse order until the specified key is found.
     *
     * @param key
     */
    @Override
    public V get(K key) {
        lock.lockRead();
        V value = super.get(key);
        lock.unlockRead();
        return value;
    }

    /**
     * Returns the value (which may be null) for the specified key, or the default value if the key is not in the map. Note this
     * does a .equals() comparison of each key in reverse order until the specified key is found.
     *
     * @param key
     * @param defaultValue
     */
    @Override
    public V get(K key, V defaultValue) {
        lock.lockRead();
        V value = super.get(key, defaultValue);
        lock.unlockRead();
        return value;
    }

    /**
     * Returns the key for the specified value. Note this does a comparison of each value in reverse order until the specified
     * value is found.
     *
     * @param value
     * @param identity If true, == comparison will be used. If false, .equals() comparison will be used.
     */
    @Override
    public K getKey(V value, boolean identity) {
        lock.lockRead();
        K key = super.getKey(value, identity);
        lock.unlockRead();
        return key;
    }

    @Override
    public K getKeyAt(int index) {
        lock.lockRead();
        K key = super.getKeyAt(index);
        lock.unlockRead();
        return key;
    }

    @Override
    public V getValueAt(int index) {
        lock.lockRead();
        V value = super.getValueAt(index);
        lock.unlockRead();
        return value;
    }

    @Override
    public K firstKey() {
        lock.lockRead();
        K key = super.firstKey();
        lock.unlockRead();
        return key;
    }

    @Override
    public V firstValue() {
        lock.lockRead();
        V value = super.firstValue();
        lock.unlockRead();
        return value;
    }

    @Override
    public void setKey(int index, K key) {
        lock.lockWrite();
        super.setKey(index, key);
        lock.unlockWrite();
    }

    @Override
    public void setValue(int index, V value) {
        lock.lockWrite();
        super.setValue(index, value);
        lock.unlockWrite();
    }

    @Override
    public void insert(int index, K key, V value) {
        lock.lockWrite();
        super.insert(index, key, value);
        lock.unlockWrite();
    }

    @Override
    public boolean containsKey(K key) {
        lock.lockRead();
        boolean b = super.containsKey(key);
        lock.unlockRead();
        return b;
    }

    /**
     * @param value
     * @param identity If true, == comparison will be used. If false, .equals() comparison will be used.
     */
    @Override
    public boolean containsValue(V value, boolean identity) {
        lock.lockRead();
        boolean b = super.containsValue(value, identity);
        lock.unlockRead();
        return b;
    }

    @Override
    public int indexOfKey(K key) {
        lock.lockRead();
        int i = super.indexOfKey(key);
        lock.unlockRead();
        return i;
    }

    @Override
    public int indexOfValue(V value, boolean identity) {
        lock.lockRead();
        int i = super.indexOfValue(value, identity);
        lock.unlockRead();
        return i;
    }

    /**
     * Removes and returns the key/values pair at the specified index.
     *
     * @param index
     */
    @Override
    public void removeIndex(int index) {
        lock.lockWrite();
        super.removeIndex(index);
        lock.unlockWrite();
    }

    /**
     * Returns true if the map has one or more items.
     */
    @Override
    public boolean notEmpty() {
        lock.lockRead();
        boolean b = super.notEmpty();
        lock.unlockRead();
        return b;
    }

    /**
     * Returns true if the map is empty.
     */
    @Override
    public boolean isEmpty() {
        lock.lockRead();
        boolean b = super.isEmpty();
        lock.unlockRead();
        return b;
    }

    /**
     * Returns the last key.
     */
    @Override
    public K peekKey() {
        lock.lockRead();
        K key = super.peekKey();
        lock.unlockRead();
        return key;
    }

    /**
     * Returns the last value.
     */
    @Override
    public V peekValue() {
        lock.lockRead();
        V value = super.peekValue();
        lock.unlockRead();
        return value;
    }

    /**
     * Clears the map and reduces the size of the backing arrays to be the specified capacity if they are larger.
     *
     * @param maximumCapacity
     */
    @Override
    public void clear(int maximumCapacity) {
        if (keys.length <= maximumCapacity){
            clear();
            return;
        }
        lock.lockWrite();
        super.clear(maximumCapacity);
        lock.unlockWrite();
    }

    @Override
    public void clear() {
        lock.lockWrite();
        super.clear();
        lock.unlockWrite();
    }

    /**
     * Reduces the size of the backing arrays to the size of the actual number of entries. This is useful to release memory when
     * many items have been removed, or if it is known that more entries will not be added.
     */
    @Override
    public void shrink() {
        lock.lockWrite();
        super.shrink();
        lock.unlockWrite();
    }

    /**
     * Increases the size of the backing arrays to accommodate the specified number of additional entries. Useful before adding
     * many entries to avoid multiple backing array resizes.
     *
     * @param additionalCapacity
     */
    @Override
    public void ensureCapacity(int additionalCapacity) {
        lock.lockWrite();
        super.ensureCapacity(additionalCapacity);
        lock.unlockWrite();
    }

    @Override
    public void reverse() {
        lock.lockWrite();
        super.reverse();
        lock.unlockWrite();
    }

    @Override
    public void shuffle() {
        lock.lockWrite();
        super.shuffle();
        lock.unlockWrite();
    }

    /**
     * Reduces the size of the arrays to the specified size. If the arrays are already smaller than the specified size, no action
     * is taken.
     *
     * @param newSize
     */
    @Override
    public void truncate(int newSize) {
        lock.lockWrite();
        super.truncate(newSize);
        lock.unlockWrite();
    }

    @Override
    public V removeKey(K key) {
        lock.lockWrite();
        V v = super.removeKey(key);
        lock.unlockWrite();
        return v;
    }

    @Override
    public boolean removeValue(V value, boolean identity) {
        lock.lockWrite();
        boolean b = super.removeValue(value, identity);
        lock.unlockWrite();
        return b;
    }
    @Override
    public int hashCode() {
        lock.lockRead();
        int hc = super.hashCode();
        lock.unlockRead();
        return hc;
    }

    @Override
    public boolean equals(Object obj) {
        boolean isOtherConcurrent = obj instanceof ConcurrentCollection;
        if (isOtherConcurrent){
            ((ConcurrentCollection) obj).getLock().lockRead();
        }
        lock.lockRead();
        boolean e = super.equals(obj);
        lock.unlockRead();
        if (isOtherConcurrent){
            ((ConcurrentCollection) obj).getLock().unlockRead();
        }
        return e;
    }

    /**
     * Uses == for comparison of each value.
     *
     * @param obj
     */
    @Override
    public boolean equalsIdentity(Object obj) {
        boolean isOtherConcurrent = obj instanceof ConcurrentCollection;
        if (isOtherConcurrent){
            ((ConcurrentCollection) obj).getLock().lockRead();
        }
        lock.lockRead();
        boolean e = super.equalsIdentity(obj);
        lock.unlockRead();
        if (isOtherConcurrent){
            ((ConcurrentCollection) obj).getLock().unlockRead();
        }
        return e;
    }

    @Override
    public String toString() {
        lock.lockRead();
        String s = super.toString();
        lock.unlockRead();
        return s;
    }

    /**
     * Returns an iterator for the entries in the map. Remove is supported.
     * <p>
     * If {@link Collections#allocateIterators} is false, the same iterator instance is returned each time this method is called. Use the
     * {@link Entries} constructor for nested or multithreaded iteration.
     *
     * @see Collections#allocateIterators
     */
    @Override
    public Entries<K, V> entries() {
        lock.lockRead();
        Entries<K, V> e = super.entries();
        lock.unlockRead();
        return e;
    }

    /**
     * Returns an iterator for the values in the map. Remove is supported.
     * <p>
     * If {@link Collections#allocateIterators} is false, the same iterator instance is returned each time this method is called. Use the
     * {@link Entries} constructor for nested or multithreaded iteration.
     *
     * @see Collections#allocateIterators
     */
    @Override
    public Values<V> values() {
        lock.lockRead();
        Values<V> v = super.values();
        lock.unlockRead();
        return v;
    }

    /**
     * Returns an iterator for the keys in the map. Remove is supported.
     * <p>
     * If {@link Collections#allocateIterators} is false, the same iterator instance is returned each time this method is called. Use the
     * {@link Entries} constructor for nested or multithreaded iteration.
     *
     * @see Collections#allocateIterators
     */
    @Override
    public Keys<K> keys() {
        lock.lockRead();
        Keys<K> k = super.keys();
        lock.unlockRead();
        return k;
    }

    @Override
    public ReadWriteLock getLock() {
        return lock;
    }
}
