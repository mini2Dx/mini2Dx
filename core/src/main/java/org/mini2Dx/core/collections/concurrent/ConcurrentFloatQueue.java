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
import org.mini2Dx.core.collections.FloatQueue;
import org.mini2Dx.core.lock.ReadWriteLock;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ConcurrentFloatQueue extends FloatQueue implements ConcurrentCollection {

    protected ReadWriteLock lock = Mdx.locks.newReadWriteLock();

    /**
     * Creates a new Queue which can hold 16 values without needing to resize backing array.
     */
    public ConcurrentFloatQueue() {
        super();
    }

    /**
     * Creates a new Queue which can hold the specified number of values without needing to resize backing array.
     *
     * @param initialSize
     */
    public ConcurrentFloatQueue(int initialSize) {
        super(initialSize);
    }

    /**
     * Append given object to the tail. (enqueue to tail) Unless backing array needs resizing, operates in O(1) time.
     *
     * @param object can be null
     */
    @Override
    public void addLast(float object) {
        lock.lockWrite();
        super.addLast(object);
        lock.unlockWrite();
    }

    /**
     * Prepend given object to the head. (enqueue to head) Unless backing array needs resizing, operates in O(1) time.
     *
     * @param object can be null
     * @see #addLast(float)
     */
    @Override
    public void addFirst(float object) {
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
    public float removeFirst() {
        lock.lockWrite();
        float f = super.removeFirst();
        lock.unlockWrite();
        return f;
    }

    /**
     * Remove the last item from the queue. (dequeue from tail) Always O(1).
     *
     * @return removed object
     * @throws NoSuchElementException when queue is empty
     * @see #removeFirst()
     */
    @Override
    public float removeLast() {
        lock.lockWrite();
        float f = super.removeLast();
        lock.unlockWrite();
        return f;
    }

    /**
     * Returns the index of first occurrence of value in the queue, or -1 if no such value exists.
     *
     * @param value
     * @return An index of first occurrence of value in queue or -1 if no such value exists
     */
    @Override
    public int indexOf(float value) {
        lock.lockRead();
        int i = super.indexOf(value);
        lock.unlockRead();
        return i;
    }

    /**
     * Removes the first instance of the specified value in the queue.
     *
     * @param value
     * @return true if value was found and removed, false otherwise
     */
    @Override
    public boolean removeValue(float value) {
        lock.lockWrite();
        boolean b = super.removeValue(value);
        lock.unlockWrite();
        return b;
    }

    /**
     * Removes and returns the item at the specified index.
     *
     * @param index
     */
    @Override
    public float removeIndex(int index) {
        lock.lockWrite();
        float f = super.removeIndex(index);
        lock.unlockWrite();
        return f;
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
     * @see #addFirst(float)
     * @see #removeFirst()
     */
    @Override
    public float first() {
        lock.lockRead();
        float f = super.first();
        lock.unlockRead();
        return f;
    }

    /**
     * Returns the last (tail) item in the queue (without removing it).
     *
     * @throws NoSuchElementException when queue is empty
     * @see #addLast(float)
     * @see #removeLast()
     */
    @Override
    public float last() {
        lock.lockRead();
        float f = super.last();
        lock.unlockRead();
        return f;
    }

    /**
     * Retrieves the value in queue without removing it. Indexing is from the front to back, zero based. Therefore get(0) is the
     * same as {@link #first()}.
     *
     * @param index
     * @throws IndexOutOfBoundsException when the index is negative or greater or equal than size
     */
    @Override
    public float get(int index) {
        lock.lockRead();
        float f = super.get(index);
        lock.unlockRead();
        return f;
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
    public Iterator<Float> iterator() {
        return new FloatQueueIterator(this);
    }

    @Override
    public int hashCode() {
        lock.lockRead();
        int hc = super.hashCode();
        lock.unlockRead();
        return hc;
    }
    @Override
    public boolean equals(Object object) {
        boolean isOtherConcurrent = object instanceof ConcurrentCollection;
        if (isOtherConcurrent){
            ((ConcurrentCollection) object).getLock().lockRead();
        }
        lock.lockRead();
        boolean e = super.equals(object);
        lock.unlockRead();
        if (isOtherConcurrent){
            ((ConcurrentCollection) object).getLock().unlockRead();
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

    @Override
    public String toString(String separator) {
        lock.lockRead();
        String s = super.toString(separator);
        lock.unlockRead();
        return s;
    }

    @Override
    public ReadWriteLock getLock() {
        return lock;
    }
}
