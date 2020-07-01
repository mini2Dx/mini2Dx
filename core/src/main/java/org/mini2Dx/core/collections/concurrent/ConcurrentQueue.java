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
import org.mini2Dx.gdx.utils.Queue;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ConcurrentQueue<T> extends Queue<T> implements ConcurrentCollection {

    protected ReadWriteLock lock = Mdx.locks.newReadWriteLock();

    /**
     * Creates a new Queue which can hold 16 values without needing to resize backing array.
     */
    public ConcurrentQueue() {
        super();
    }

    /**
     * Creates a new Queue which can hold the specified number of values without needing to resize backing array.
     *
     * @param initialSize
     */
    public ConcurrentQueue(int initialSize) {
        super(initialSize);
    }

    /**
     * Creates a new Queue which can hold the specified number of values without needing to resize backing array. This creates
     * backing array of the specified type via reflection, which is necessary only when accessing the backing array directly.
     *
     * @param initialSize
     * @param type
     */
    public ConcurrentQueue(int initialSize, Class<T> type) {
        super(initialSize, type);
    }

    /**
     * Append given object to the tail. (enqueue to tail) Unless backing array needs resizing, operates in O(1) time.
     *
     * @param object can be null
     */
    @Override
    public void addLast(T object) {
        lock.lockWrite();
        super.addLast(object);
        lock.unlockWrite();
    }

    /**
     * Prepend given object to the head. (enqueue to head) Unless backing array needs resizing, operates in O(1) time.
     *
     * @param object can be null
     * @see #addLast(Object)
     */
    @Override
    public void addFirst(T object) {
        lock.lockWrite();
        super.addFirst(object);
        lock.unlockWrite();
    }

    /**
     * Increases the size of the backing array to accommodate the specified number of additional items. Useful before adding many
     * items to avoid multiple backing array resizes.
     *
     * @param additional
     */
    @Override
    public void ensureCapacity(int additional) {
        lock.lockWrite();
        super.ensureCapacity(additional);
        lock.unlockWrite();
    }

    /**
     * Remove the first item from the queue. (dequeue from head) Always O(1).
     *
     * @return removed object
     * @throws NoSuchElementException when queue is empty
     */
    @Override
    public T removeFirst() {
        lock.lockWrite();
        T t = super.removeFirst();
        lock.unlockWrite();
        return t;
    }

    /**
     * Remove the last item from the queue. (dequeue from tail) Always O(1).
     *
     * @return removed object
     * @throws NoSuchElementException when queue is empty
     * @see #removeFirst()
     */
    @Override
    public T removeLast() {
        lock.lockWrite();
        T t = super.removeLast();
        lock.unlockWrite();
        return t;
    }

    /**
     * Returns the index of first occurrence of value in the queue, or -1 if no such value exists.
     *
     * @param value
     * @param identity If true, == comparison will be used. If false, .equals() comparison will be used.
     * @return An index of first occurrence of value in queue or -1 if no such value exists
     */
    @Override
    public int indexOf(T value, boolean identity) {
        lock.lockRead();
        int i = super.indexOf(value, identity);
        lock.unlockRead();
        return i;
    }

    /**
     * Removes the first instance of the specified value in the queue.
     *
     * @param value
     * @param identity If true, == comparison will be used. If false, .equals() comparison will be used.
     * @return true if value was found and removed, false otherwise
     */
    @Override
    public boolean removeValue(T value, boolean identity) {
        lock.lockWrite();
        boolean b = super.removeValue(value, identity);
        lock.unlockWrite();
        return b;
    }

    /**
     * Removes and returns the item at the specified index.
     *
     * @param index
     */
    @Override
    public T removeIndex(int index) {
        lock.lockWrite();
        T t = super.removeIndex(index);
        lock.unlockWrite();
        return t;
    }

    /**
     * Returns true if the queue has one or more items.
     */
    @Override
    public boolean notEmpty() {
        lock.lockRead();
        boolean b = super.notEmpty();
        lock.unlockRead();
        return b;
    }

    /**
     * Returns true if the queue is empty.
     */
    @Override
    public boolean isEmpty() {
        lock.lockRead();
        boolean b = super.isEmpty();
        lock.unlockRead();
        return b;
    }

    /**
     * Returns the first (head) item in the queue (without removing it).
     *
     * @throws NoSuchElementException when queue is empty
     * @see #addFirst(Object)
     * @see #removeFirst()
     */
    @Override
    public T first() {
        lock.lockRead();
        T t = super.first();
        lock.unlockRead();
        return t;
    }

    /**
     * Returns the last (tail) item in the queue (without removing it).
     *
     * @throws NoSuchElementException when queue is empty
     * @see #addLast(Object)
     * @see #removeLast()
     */
    @Override
    public T last() {
        lock.lockRead();
        T t = super.last();
        lock.unlockRead();
        return t;
    }

    /**
     * Retrieves the value in queue without removing it. Indexing is from the front to back, zero based. Therefore get(0) is the
     * same as {@link #first()}.
     *
     * @param index
     * @throws IndexOutOfBoundsException when the index is negative or greater or equal than size
     */
    @Override
    public T get(int index) {
        lock.lockRead();
        T t = super.get(index);
        lock.unlockRead();
        return t;
    }

    /**
     * Removes all values from this queue. Values in backing array are set to null to prevent memory leak, so this operates in
     * O(n).
     */
    @Override
    public void clear() {
        lock.lockWrite();
        super.clear();
        lock.unlockWrite();
    }

    /**
     * Returns an iterator for the items in the queue. Remove is supported.
     */
    @Override
    public Iterator<T> iterator() {
        return new QueueIterator<>(this);
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
     * Uses == for comparison of each item.
     *
     * @param o
     */
    @Override
    public boolean equalsIdentity(Object o) {
        return super.equalsIdentity(o);
    }

    @Override
    public ReadWriteLock getLock() {
        return lock;
    }
}
