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
import org.mini2Dx.core.collections.LruObjectMap;
import org.mini2Dx.core.lock.ReadWriteLock;
import org.mini2Dx.gdx.utils.ObjectMap;

public class ConcurrentLruObjectMap<K, V> extends LruObjectMap<K, V> implements ConcurrentCollection {

    protected ReadWriteLock lock = Mdx.locks.newReadWriteLock();

    public ConcurrentLruObjectMap() {
        super();
    }

    public ConcurrentLruObjectMap(int initialCapacity) {
        super(initialCapacity);
    }

    public ConcurrentLruObjectMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * NOTE: read access to the other map is not thread-safe
     * @param map
     */
    public ConcurrentLruObjectMap(ObjectMap<? extends K, ? extends V> map) {
        super(map);
    }

    public ConcurrentLruObjectMap(int initialCapacity, int maxCapacity) {
        super(initialCapacity, maxCapacity);
    }

    public ConcurrentLruObjectMap(int initialCapacity, int maxCapacity, float loadFactor) {
        super(initialCapacity, maxCapacity, loadFactor);
    }

    /**
     * NOTE: read access to the other map is not thread-safe
     * @param map
     * @param maxCapacity
     */
    public ConcurrentLruObjectMap(ObjectMap<? extends K, ? extends V> map, int maxCapacity) {
        super(map, maxCapacity);
    }

    @Override
    public int getMaxCapacity() {
        lock.lockRead();
        int i = super.getMaxCapacity();
        lock.unlockRead();
        return i;
    }

    /**
     * Returns the old value associated with the specified key, or null.
     *
     * @param key
     * @param value
     */
    @Override
    public V put(K key, V value) {
        lock.lockWrite();
        V v = super.put(key, value);
        lock.unlockWrite();
        return v;
    }

    @Override
    public void putAll(ObjectMap<? extends K, ? extends V> map) {
        boolean isOtherConcurrent = map instanceof ConcurrentCollection;
        if (isOtherConcurrent){
            ((ConcurrentCollection) map).getLock().lockRead();
        }
        lock.lockWrite();
        super.putAll(map);
        lock.unlockWrite();
        if (isOtherConcurrent){
            ((ConcurrentCollection) map).getLock().unlockRead();
        }
    }

    /**
     * Returns the value (which may be null) for the specified key, or null if the key is not in the map.
     *
     * @param key
     */
    @Override
    public V get(K key) {
        lock.lockRead();
        V v = super.get(key);
        lock.unlockRead();
        return v;
    }

    /**
     * Returns the value (which may be null) for the specified key, or the default value if the key is not in the map.
     *
     * @param key
     * @param defaultValue
     */
    @Override
    public V get(K key, V defaultValue) {
        lock.lockRead();
        V v = super.get(key, defaultValue);
        lock.unlockRead();
        return v;
    }

    /**
     * Returns the value associated with the key, or null.
     *
     * @param key
     */
    @Override
    public V remove(K key) {
        lock.lockWrite();
        V v = super.remove(key);
        lock.unlockWrite();
        return v;
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
     * Reduces the size of the backing arrays to be the specified capacity or less. If the capacity is already less, nothing is
     * done. If the map contains more items than the specified capacity, the next highest power of two capacity is used instead.
     *
     * @param maximumCapacity
     */
    @Override
    public void shrink(int maximumCapacity) {
        lock.lockWrite();
        super.shrink(maximumCapacity);
        lock.unlockWrite();
    }

    /**
     * Clears the map and reduces the size of the backing arrays to be the specified capacity, if they are larger. The reduction
     * is done by allocating new arrays, though for large arrays this can be faster than clearing the existing array.
     *
     * @param maximumCapacity
     */
    @Override
    public void clear(int maximumCapacity) {
        lock.lockWrite();
        super.clear(maximumCapacity);
        lock.unlockWrite();
    }

    /**
     * Clears the map, leaving the backing arrays at the current capacity. When the capacity is high and the population is low,
     * iteration can be unnecessarily slow. {@link #clear(int)} can be used to reduce the capacity.
     */
    @Override
    public void clear() {
        lock.lockWrite();
        super.clear();
        lock.unlockWrite();
    }

    /**
     * Returns true if the specified value is in the map. Note this traverses the entire map and compares every value, which may
     * be an expensive operation.
     *
     * @param value
     * @param identity If true, uses == to compare the specified value with values in the map. If false, uses
     *                 {@link #equals(Object)}.
     */
    @Override
    public boolean containsValue(Object value, boolean identity) {
        lock.lockRead();
        boolean b = super.containsValue(value, identity);
        lock.unlockRead();
        return b;
    }

    @Override
    public boolean containsKey(K key) {
        lock.lockRead();
        boolean b = super.containsKey(key);
        lock.unlockRead();
        return b;
    }

    /**
     * Returns the key for the specified value, or null if it is not in the map. Note this traverses the entire map and compares
     * every value, which may be an expensive operation.
     *
     * @param value
     * @param identity If true, uses == to compare the specified value with values in the map. If false, uses
     *                 {@link #equals(Object)}.
     */
    @Override
    public K findKey(Object value, boolean identity) {
        lock.lockRead();
        K k =  super.findKey(value, identity);
        lock.unlockRead();
        return k;
    }

    /**
     * Increases the size of the backing array to accommodate the specified number of additional items. Useful before adding many
     * items to avoid multiple backing array resizes.
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
    public int hashCode() {
        lock.lockRead();
        int i = super.hashCode();
        lock.unlockRead();
        return i;
    }

    @Override
    public boolean equals(Object obj) {
        boolean isOtherConcurrent = obj instanceof ConcurrentCollection;
        if (isOtherConcurrent){
            ((ConcurrentCollection) obj).getLock().lockRead();
        }
        lock.lockRead();
        boolean b = super.equals(obj);
        lock.unlockRead();
        if (isOtherConcurrent){
            ((ConcurrentCollection) obj).getLock().unlockRead();
        }
        return b;
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
        boolean b = super.equalsIdentity(obj);
        lock.unlockRead();
        if (isOtherConcurrent){
            ((ConcurrentCollection) obj).getLock().unlockRead();
        }
        return b;
    }

    @Override
    public String toString(String separator) {
        lock.lockRead();
        String s = super.toString(separator);
        lock.unlockRead();
        return s;
    }

    @Override
    public String toString() {
        lock.lockRead();
        String s = super.toString();
        lock.unlockRead();
        return s;
    }

    /**
     * Returns an iterator for the keys in the map. Remove is supported.
     */
    @Override
    public Keys<K> keys() {
        return new Keys<>(this);
    }

    /**
     * Returns an iterator for the entries in the map. Remove is supported.
     */
    @Override
    public Entries<K, V> entries() {
        return new Entries<>(this);
    }

    /**
     * Returns an iterator for the values in the map. Remove is supported.
     */
    @Override
    public Values<V> values() {
        return new Values<>(this);
    }

    @Override
    public ReadWriteLock getLock() {
        return lock;
    }
}
