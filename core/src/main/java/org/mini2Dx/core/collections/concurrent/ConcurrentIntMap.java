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
import org.mini2Dx.gdx.utils.Collections;
import org.mini2Dx.gdx.utils.IntMap;

public class ConcurrentIntMap<T> extends IntMap<T> implements ConcurrentCollection {

    protected ReadWriteLock lock = Mdx.locks.newReadWriteLock();

    /**
     * Creates a new map with an initial capacity of 51 and a load factor of 0.8.
     */
    public ConcurrentIntMap() {
        super();
    }

    /**
     * Creates a new map with a load factor of 0.8.
     *
     * @param initialCapacity If not a power of two, it is increased to the next nearest power of two.
     */
    public ConcurrentIntMap(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Creates a new map with the specified initial capacity and load factor. This map will hold initialCapacity items before
     * growing the backing table.
     *
     * @param initialCapacity If not a power of two, it is increased to the next nearest power of two.
     * @param loadFactor
     */
    public ConcurrentIntMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * Creates a new map identical to the specified map.
     *
     * NOTE: read access to the other map is not thread-safe
     *
     * @param map
     */
    public ConcurrentIntMap(IntMap<? extends T> map) {
        super(map);
    }

    @Override
    public T put(int key, T value) {
        lock.lockWrite();
        T t = super.put(key, value);
        lock.unlockWrite();
        return t;
    }

    @Override
    public void putAll(IntMap<? extends  T> map) {
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
     * @param key
     * @param defaultValue Returned if the key was not associated with a value.
     */
    @Override
    public T get(int key, T defaultValue) {
        lock.lockRead();
        T t = super.get(key, defaultValue);
        lock.unlockRead();
        return t;
    }

    @Override
    public T get(int key) {
        lock.lockRead();
        T t = super.get(key);
        lock.unlockRead();
        return t;
    }

    @Override
    public T remove(int key) {
        lock.lockWrite();
        T t = super.remove(key);
        lock.unlockWrite();
        return t;
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
     * Clears the map and reduces the size of the backing arrays to be the specified capacity if they are larger.
     *
     * @param maximumCapacity
     */
    @Override
    public void clear(int maximumCapacity) {
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

    @Override
    public boolean containsKey(int key) {
        lock.lockRead();
        boolean b = super.containsKey(key);
        lock.unlockRead();
        return b;
    }

    /**
     * Returns the key for the specified value, or <tt>notFound</tt> if it is not in the map. Note this traverses the entire map
     * and compares every value, which may be an expensive operation.
     *
     * @param value
     * @param identity If true, uses == to compare the specified value with values in the map. If false, uses
     *                 {@link #equals(Object)}.
     * @param notFound
     */
    @Override
    public int findKey(Object value, boolean identity, int notFound) {
        lock.lockRead();
        int i = super.findKey(value, identity, notFound);
        lock.unlockRead();
        return i;
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
     */
    @Override
    public Entries<T> entries() {
        return new Entries<T>(this);
    }

    /**
     * Returns an iterator for the values in the map. Remove is supported.
     * <p>
     * If {@link Collections#allocateIterators} is false, the same iterator instance is returned each time this method is called. Use the
     * {@link Entries} constructor for nested or multithreaded iteration.
     */
    @Override
    public Values<T> values() {
        return new Values<T>(this);
    }

    /**
     * Returns an iterator for the keys in the map. Remove is supported.
     * <p>
     * If {@link Collections#allocateIterators} is false, the same iterator instance is returned each time this method is called. Use the
     * {@link Entries} constructor for nested or multithreaded iteration.
     */
    @Override
    public Keys keys() {
        return new Keys(this);
    }

    @Override
    public ReadWriteLock getLock() {
        return lock;
    }
}
