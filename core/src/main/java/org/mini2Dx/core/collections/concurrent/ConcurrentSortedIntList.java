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
import org.mini2Dx.gdx.utils.SortedIntList;

import java.util.Iterator;

public class ConcurrentSortedIntList<E> extends SortedIntList<E> implements ConcurrentCollection {

    protected ReadWriteLock lock = Mdx.locks.newReadWriteLock();

    /**
     * Creates an ascending list
     */
    public ConcurrentSortedIntList() {
        super();
    }

    /**
     * Inserts an element into the list at the given index
     *
     * @param index Index of the element
     * @param value Element to insert
     * @return Element replaced by newly inserted element, null if nothing was replaced
     */
    @Override
    public E insert(int index, E value) {
        lock.lockWrite();
        E e = super.insert(index, value);
        lock.unlockWrite();
        return e;
    }

    /**
     * Retrieves an element at a given index
     *
     * @param index Index of the element to retrieve
     * @return Matching element, null otherwise
     */
    @Override
    public E get(int index) {
        lock.lockRead();
        E e = super.get(index);
        lock.unlockRead();
        return e;
    }

    /**
     * Clears list
     */
    @Override
    public void clear() {
        lock.lockWrite();
        super.clear();
        lock.unlockWrite();
    }

    /**
     * @return size of list equal to elements contained in it
     */
    @Override
    public int size() {
        lock.lockRead();
        int i = super.size();
        lock.unlockRead();
        return i;
    }

    /**
     * Returns true if the list has one or more items.
     */
    @Override
    public boolean notEmpty() {
        lock.lockRead();
        boolean b = super.notEmpty();
        lock.unlockRead();
        return b;
    }

    /**
     * Returns true if the list is empty.
     */
    @Override
    public boolean isEmpty() {
        lock.lockRead();
        boolean b = super.isEmpty();
        lock.unlockRead();
        return b;
    }

    /**
     * Returns an iterator to traverse the list.
     * Only one iterator can be active per list at any given time. Not thread-safe
     *
     * @return Iterator to traverse list
     */
    @Override
    public Iterator<Node<E>> iterator() {
        return super.iterator();
    }

    @Override
    public ReadWriteLock getLock() {
        return lock;
    }
}
