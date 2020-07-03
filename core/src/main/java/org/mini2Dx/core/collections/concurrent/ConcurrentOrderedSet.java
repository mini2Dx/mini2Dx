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
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.ObjectSet;
import org.mini2Dx.gdx.utils.OrderedSet;

public class ConcurrentOrderedSet<T> extends OrderedSet<T> implements ConcurrentCollection {

    protected ReadWriteLock lock = Mdx.locks.newReadWriteLock();

    /**
     * Creates a new set with an initial capacity of 51 and a load factor of 0.8.
     */
    public ConcurrentOrderedSet() {
        super();
    }

    /**
     * Creates a new set with a load factor of 0.8.
     *
     * @param initialCapacity If not a power of two, it is increased to the next nearest power of two.
     */
    public ConcurrentOrderedSet(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Creates a new set with the specified initial capacity and load factor. This set will hold initialCapacity items before
     * growing the backing table.
     *
     * @param initialCapacity If not a power of two, it is increased to the next nearest power of two.
     * @param loadFactor
     */
    public ConcurrentOrderedSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * Creates a new set identical to the specified set.
     *
     * NOTE: read access to the other set is not thread-safe
     *
     * @param set
     */
    public ConcurrentOrderedSet(OrderedSet<? extends T> set) {
        super(set);
    }

    /**
     * Returns true if the key was not already in the set. If this set already contains the key, the call leaves the set unchanged
     * and returns false.
     *
     * @param key
     */
    @Override
    public boolean add(T key) {
        lock.lockWrite();
        boolean b = super.add(key);
        lock.unlockWrite();
        return b;
    }

    @Override
    public void addAll(Array<? extends T> array, int offset, int length) {
        boolean isOtherConcurrent = array instanceof ConcurrentCollection;
        if (isOtherConcurrent){
            ((ConcurrentCollection)array).getLock().lockRead();
        }
        lock.lockWrite();
        super.addAll(array, offset, length);
        lock.unlockWrite();
        if (isOtherConcurrent){
            ((ConcurrentCollection)array).getLock().unlockRead();
        }
    }

    @Override
    public void addAll(T[] array, int offset, int length) {
        lock.lockWrite();
        super.addAll(array, offset, length);
        lock.unlockWrite();
    }

    @Override
    public void addAll(ObjectSet<T> set) {
        boolean isOtherConcurrent = set instanceof ConcurrentCollection;
        if (isOtherConcurrent){
            ((ConcurrentCollection)set).getLock().lockRead();
        }
        lock.lockWrite();
        super.addAll(set);
        lock.unlockWrite();
        if (isOtherConcurrent){
            ((ConcurrentCollection)set).getLock().unlockRead();
        }
    }

    /**
     * Returns true if the key was removed.
     *
     * @param key
     */
    @Override
    public boolean remove(T key) {
        lock.lockWrite();
        boolean b = super.remove(key);
        lock.unlockWrite();
        return b;
    }

    /**
     * Returns true if the set has one or more items.
     */
    @Override
    public boolean notEmpty() {
        lock.lockRead();
        boolean b = super.notEmpty();
        lock.unlockRead();
        return b;
    }

    /**
     * Returns true if the set is empty.
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
     * done. If the set contains more items than the specified capacity, the next highest power of two capacity is used instead.
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
     * Clears the set and reduces the size of the backing arrays to be the specified capacity, if they are larger. The reduction
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
     * Clears the set, leaving the backing arrays at the current capacity. When the capacity is high and the population is low,
     * iteration can be unnecessarily slow. {@link #clear(int)} can be used to reduce the capacity.
     */
    @Override
    public void clear() {
        lock.lockWrite();
        super.clear();
        lock.unlockWrite();
    }

    @Override
    public boolean contains(T key) {
        lock.lockRead();
        boolean b = super.contains(key);
        lock.unlockRead();
        return b;
    }

    /**
     * @param key
     * @return May be null.
     */
    @Override
    public T get(T key) {
        lock.lockRead();
        T t = super.get(key);
        lock.unlockRead();
        return t;
    }

    @Override
    public T first() {
        lock.lockRead();
        T t = super.first();
        lock.unlockRead();
        return t;
    }

    @Override
    public boolean add(T key, int index) {
        lock.lockWrite();
        boolean b = super.add(key, index);
        lock.unlockWrite();
        return b;
    }

    @Override
    public T removeIndex(int index) {
        lock.lockWrite();
        T t = super.removeIndex(index);
        lock.unlockWrite();
        return t;
    }

    @Override
    public Array<T> orderedItems() {
        lock.lockRead();
        Array<T> aT = super.orderedItems();
        lock.unlockRead();
        return aT;
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

    @Override
    public String toString(String separator) {
        lock.lockRead();
        String s = super.toString(separator);
        lock.unlockRead();
        return s;
    }

    /**
     * Returns an iterator for the keys in the set. Remove is supported.
     */
    @Override
    public OrderedSetIterator<T> iterator() {
        return new OrderedSetIterator<T>(this);
    }

    @Override
    public ReadWriteLock getLock() {
        return lock;
    }
}
