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
import org.mini2Dx.gdx.utils.PooledLinkedList;

public class ConcurrentPooledLinkedList<T> extends PooledLinkedList<T> implements ConcurrentCollection {

    protected ReadWriteLock lock = Mdx.locks.newReadWriteLock();

    public ConcurrentPooledLinkedList(int maxPoolSize) {
        super(maxPoolSize);
    }

    /**
     * Adds the specified object to the end of the list regardless of iteration status
     *
     * @param object
     */
    @Override
    public void add(T object) {
        lock.lockWrite();
        super.add(object);
        lock.unlockWrite();
    }

    /**
     * Adds the specified object to the head of the list regardless of iteration status
     *
     * @param object
     */
    @Override
    public void addFirst(T object) {
        lock.lockWrite();
        super.addFirst(object);
        lock.unlockWrite();
    }

    /**
     * Returns the number of items in the list
     */
    @Override
    public int size() {
        lock.lockRead();
        int i = super.size();
        lock.unlockRead();
        return i;
    }

    /**
     * Starts iterating over the list's items from the head of the list
     */
    @Override
    public void iter() {
        lock.lockWrite();
        super.iter();
        lock.unlockWrite();
    }

    /**
     * Starts iterating over the list's items from the tail of the list
     */
    @Override
    public void iterReverse() {
        lock.lockWrite();
        super.iterReverse();
        lock.unlockWrite();
    }

    /**
     * Gets the next item in the list
     *
     * @return the next item in the list or null if there are no more items
     */
    @Override
    public T next() {
        lock.lockWrite();
        T t = super.next();
        lock.unlockWrite();
        return t;
    }

    /**
     * Gets the previous item in the list
     *
     * @return the previous item in the list or null if there are no more items
     */
    @Override
    public T previous() {
        lock.lockWrite();
        T t = super.previous();
        lock.unlockWrite();
        return t;
    }

    /**
     * Removes the current list item based on the iterator position.
     */
    @Override
    public void remove() {
        lock.lockWrite();
        super.remove();
        lock.unlockWrite();
    }

    /**
     * Removes the tail of the list regardless of iteration status
     */
    @Override
    public T removeLast() {
        lock.lockWrite();
        T t = super.removeLast();
        lock.unlockWrite();
        return t;
    }

    @Override
    public void clear() {
        lock.lockWrite();
        super.clear();
        lock.unlockWrite();
    }

    @Override
    public ReadWriteLock getLock() {
        return lock;
    }
}
