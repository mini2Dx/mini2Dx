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
import org.mini2Dx.gdx.utils.IntArray;
import org.mini2Dx.gdx.utils.IntSet;

public class ConcurrentIntSet extends IntSet implements ConcurrentCollection {

    protected ReadWriteLock lock = Mdx.locks.newReadWriteLock();

    /**
     * Creates a new set with an initial capacity of 51 and a load factor of 0.8.
     */
    public ConcurrentIntSet() {
        super();
    }

    /**
     * Creates a new set with a load factor of 0.8.
     *
     * @param initialCapacity If not a power of two, it is increased to the next nearest power of two.
     */
    public ConcurrentIntSet(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Creates a new set with the specified initial capacity and load factor. This set will hold initialCapacity items before
     * growing the backing table.
     *
     * @param initialCapacity If not a power of two, it is increased to the next nearest power of two.
     * @param loadFactor
     */
    public ConcurrentIntSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * Creates a new set identical to the specified set.
     *
     * NOTE: read access to the other set is not thread-safe
     *
     * @param set
     */
    public ConcurrentIntSet(IntSet set) {
        super(set);
    }

    /**
     * Returns true if the key was not already in the set.
     *
     * @param key
     */
    @Override
    public boolean add(int key) {
        lock.lockWrite();
        boolean b = super.add(key);
        lock.unlockWrite();
        return b;
    }

    @Override
    public void addAll(IntArray array, int offset, int length) {
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
    public void addAll(int[] array, int offset, int length) {
        lock.lockWrite();
        super.addAll(array, offset, length);
        lock.unlockWrite();
    }

    @Override
    public void addAll(IntSet set) {
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
    public boolean remove(int key) {
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
        lock.lockWrite();
        boolean b = super.notEmpty();
        lock.unlockWrite();
        return b;
    }

    /**
     * Returns true if the set is empty.
     */
    @Override
    public boolean isEmpty() {
        lock.lockWrite();
        boolean b = super.isEmpty();
        lock.unlockWrite();
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
     * Clears the set and reduces the size of the backing arrays to be the specified capacity if they are larger.
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
    public boolean contains(int key) {
        lock.lockRead();
        boolean b = super.contains(key);
        lock.unlockRead();
        return b;
    }

    @Override
    public int first() {
        lock.lockRead();
        int i = super.first();
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
     * Returns an iterator for the keys in the set. Remove is supported.
     */
    @Override
    public IntSetIterator iterator() {
        return new IntSetIterator(this);
    }

    @Override
    public ReadWriteLock getLock() {
        return lock;
    }
}
