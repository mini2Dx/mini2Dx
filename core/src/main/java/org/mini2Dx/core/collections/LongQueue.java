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

import org.mini2Dx.core.exception.MdxException;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LongQueue {

    /** Contains the values in the queue. Head and tail indices go in a circle around this array, wrapping at the end. */
    protected long[] values;

    /** Index of first element. Logically smaller than tail. Unless empty, it points to a valid element inside queue. */
    protected int head = 0;

    /** Index of last element. Logically bigger than head. Usually points to an empty position, but points to the head when full
     * (size == values.length). */
    protected int tail = 0;

    /** Number of elements in the queue. */
    public int size = 0;

    private LongQueueIterable iterable;

    /** Creates a new Queue which can hold 16 values without needing to resize backing array. */
    public LongQueue () {
        this(16);
    }

    /** Creates a new Queue which can hold the specified number of values without needing to resize backing array. */
    public LongQueue (int initialSize) {
        this.values = new long[initialSize];
    }

    /** Append given object to the tail. (enqueue to tail) Unless backing array needs resizing, operates in O(1) time.
     * @param object can be null */
    public void addLast (long object) {
        long[] values = this.values;

        if (size == values.length) {
            resize(values.length << 1);// * 2
            values = this.values;
        }

        values[tail++] = object;
        if (tail == values.length) {
            tail = 0;
        }
        size++;
    }

    /** Prepend given object to the head. (enqueue to head) Unless backing array needs resizing, operates in O(1) time.
     * @see #addLast(long)
     * @param object can be null */
    public void addFirst (long object) {
        long[] values = this.values;

        if (size == values.length) {
            resize(values.length << 1);// * 2
            values = this.values;
        }

        int head = this.head;
        head--;
        if (head == -1) {
            head = values.length - 1;
        }
        values[head] = object;

        this.head = head;
        this.size++;
    }

    /** Increases the size of the backing array to accommodate the specified number of additional items. Useful before adding many
     * items to avoid multiple backing array resizes. */
    public void ensureCapacity (int additional) {
        final int needed = size + additional;
        if (values.length < needed) {
            resize(needed);
        }
    }

    /** Resize backing array. newSize must be bigger than current size. */
    protected void resize (int newSize) {
        final long[] values = this.values;
        final int head = this.head;
        final int tail = this.tail;

        final long[] newArray = new long[newSize];
        if (head < tail) {
            // Continuous
            System.arraycopy(values, head, newArray, 0, tail - head);
        } else if (size > 0) {
            // Wrapped
            final int rest = values.length - head;
            System.arraycopy(values, head, newArray, 0, rest);
            System.arraycopy(values, 0, newArray, rest, tail);
        }
        this.values = newArray;
        this.head = 0;
        this.tail = size;
    }

    /** Remove the first item from the queue. (dequeue from head) Always O(1).
     * @return removed object
     * @throws NoSuchElementException when queue is empty */
    public long removeFirst () {
        if (size == 0) {
            // Underflow
            throw new NoSuchElementException("Queue is empty.");
        }

        final long[] values = this.values;

        final long result = values[head];
        head++;
        if (head == values.length) {
            head = 0;
        }
        size--;

        return result;
    }

    /** Remove the last item from the queue. (dequeue from tail) Always O(1).
     * @see #removeFirst()
     * @return removed object
     * @throws NoSuchElementException when queue is empty */
    public long removeLast () {
        if (size == 0) {
            throw new NoSuchElementException("Queue is empty.");
        }

        final long[] values = this.values;
        int tail = this.tail;
        tail--;
        if (tail == -1) {
            tail = values.length - 1;
        }
        final long result = values[tail];
        this.tail = tail;
        size--;

        return result;
    }

    /** Returns the index of first occurrence of value in the queue, or -1 if no such value exists.
     * @return An index of first occurrence of value in queue or -1 if no such value exists */
    public int indexOf (long value) {
        if (size == 0) return -1;
        long[] values = this.values;
        final int head = this.head, tail = this.tail;
        if (head < tail) {
            for (int i = head; i < tail; i++)
                if (values[i] == value) return i - head;
        } else {
            for (int i = head, n = values.length; i < n; i++)
                if (values[i] == value) return i - head;
            for (int i = 0; i < tail; i++)
                if (values[i] == value) return i + values.length - head;
        }
        return -1;
    }

    /** Removes the first instance of the specified value in the queue.
     * @return true if value was found and removed, false otherwise */
    public boolean removeValue (long value) {
        int index = indexOf(value);
        if (index == -1) return false;
        removeIndex(index);
        return true;
    }

    /** Removes and returns the item at the specified index. */
    public long removeIndex (int index) {
        if (index < 0) throw new IndexOutOfBoundsException("index can't be < 0: " + index);
        if (index >= size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);

        long[] values = this.values;
        int head = this.head, tail = this.tail;
        index += head;
        long value;
        if (head < tail) { // index is between head and tail.
            value = values[index];
            System.arraycopy(values, index + 1, values, index, tail - index);
            this.tail--;
        } else if (index >= values.length) { // index is between 0 and tail.
            index -= values.length;
            value = values[index];
            System.arraycopy(values, index + 1, values, index, tail - index);
            this.tail--;
        } else { // index is between head and values.length.
            value = values[index];
            System.arraycopy(values, head, values, head + 1, index - head);
            this.head++;
            if (this.head == values.length) {
                this.head = 0;
            }
        }
        size--;
        return value;
    }

    /** Returns true if the queue has one or more items. */
    public boolean notEmpty () {
        return size > 0;
    }

    /** Returns true if the queue is empty. */
    public boolean isEmpty () {
        return size == 0;
    }

    /** Returns the first (head) item in the queue (without removing it).
     * @see #addFirst(long)
     * @see #removeFirst()
     * @throws NoSuchElementException when queue is empty */
    public long first () {
        if (size == 0) {
            // Underflow
            throw new NoSuchElementException("Queue is empty.");
        }
        return values[head];
    }

    /** Returns the last (tail) item in the queue (without removing it).
     * @see #addLast(long)
     * @see #removeLast()
     * @throws NoSuchElementException when queue is empty */
    public long last () {
        if (size == 0) {
            // Underflow
            throw new NoSuchElementException("Queue is empty.");
        }
        final long[] values = this.values;
        int tail = this.tail;
        tail--;
        if (tail == -1) {
            tail = values.length - 1;
        }
        return values[tail];
    }

    /** Retrieves the value in queue without removing it. Indexing is from the front to back, zero based. Therefore get(0) is the
     * same as {@link #first()}.
     * @throws IndexOutOfBoundsException when the index is negative or greater or equal than size */
    public long get (int index) {
        if (index < 0) throw new IndexOutOfBoundsException("index can't be < 0: " + index);
        if (index >= size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);
        final long[] values = this.values;

        int i = head + index;
        if (i >= values.length) {
            i -= values.length;
        }
        return values[i];
    }

    /** Removes all values from this queue. Values in backing array are set to null to prevent memory leak, so this operates in
     * O(n). */
    public void clear () {
        if (size == 0) return;
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }

    /** Returns an iterator for the items in the queue. Remove is supported.
     */
    public Iterator<Long> iterator () {
        if (iterable == null) iterable = new LongQueueIterable(this);
        return iterable.iterator();
    }

    public String toString () {
        if (size == 0) {
            return "[]";
        }
        final long[] values = this.values;
        final int head = this.head;
        final int tail = this.tail;

        StringBuilder sb = new StringBuilder(64);
        sb.append('[');
        sb.append(values[head]);
        for (int i = (head + 1) % values.length; i != tail; i = (i + 1) % values.length) {
            sb.append(", ").append(values[i]);
        }
        sb.append(']');
        return sb.toString();
    }

    public String toString (String separator) {
        if (size == 0) return "";
        final long[] values = this.values;
        final int head = this.head;
        final int tail = this.tail;

        StringBuilder sb = new StringBuilder(64);
        sb.append(values[head]);
        for (int i = (head + 1) % values.length; i != tail; i = (i + 1) % values.length)
            sb.append(separator).append(values[i]);
        return sb.toString();
    }

    public int hashCode () {
        final int size = this.size;
        final long[] values = this.values;
        final int backingLength = values.length;
        int index = this.head;

        int hash = size + 1;
        for (int s = 0; s < size; s++) {
            final long value = values[index];

            hash *= 16;
            hash += value;

            index++;
            if (index == backingLength) index = 0;
        }

        return hash;
    }

    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof LongQueue)) return false;

        LongQueue q = (LongQueue)o;
        final int size = this.size;

        if (q.size != size) return false;

        final long[] myValues = this.values;
        final int myBackingLength = myValues.length;
        final long[] itsValues = q.values;
        final int itsBackingLength = itsValues.length;

        int myIndex = head;
        int itsIndex = q.head;
        for (int s = 0; s < size; s++) {
            long myValue = myValues[myIndex];
            long itsValue = itsValues[itsIndex];

            if (myValue != itsValue) return false;
            myIndex++;
            itsIndex++;
            if (myIndex == myBackingLength) myIndex = 0;
            if (itsIndex == itsBackingLength) itsIndex = 0;
        }
        return true;
    }

    static public class LongQueueIterator implements Iterator<Long>, Iterable<Long> {
        private final LongQueue queue;
        private final boolean allowRemove;
        int index;
        boolean valid = true;


        public LongQueueIterator(LongQueue queue) {
            this(queue, true);
        }

        public LongQueueIterator(LongQueue queue, boolean allowRemove) {
            this.queue = queue;
            this.allowRemove = allowRemove;
        }

        public boolean hasNext () {
            if (!valid) {
                throw new MdxException("#iterator() cannot be used nested.");
            }
            return index < queue.size;
        }

        public Long next () {
            if (index >= queue.size) throw new NoSuchElementException(String.valueOf(index));
            if (!valid) {
                throw new MdxException("#iterator() cannot be used nested.");
            }
            return queue.get(index++);
        }

        public void remove () {
            if (!allowRemove) throw new MdxException("Remove not allowed.");
            index--;
            queue.removeIndex(index);
        }

        public void reset () {
            index = 0;
        }

        public Iterator<Long> iterator () {
            return this;
        }
    }

    static public class LongQueueIterable implements Iterable<Long> {
        private final LongQueue queue;
        private final boolean allowRemove;
        private LongQueueIterator iterator1, iterator2;

        public LongQueueIterable(LongQueue queue) {
            this(queue, true);
        }

        public LongQueueIterable(LongQueue queue, boolean allowRemove) {
            this.queue = queue;
            this.allowRemove = allowRemove;
        }

        public Iterator<Long> iterator () {
            if (iterator1 == null) {
                iterator1 = new LongQueueIterator(queue, allowRemove);
                iterator2 = new LongQueueIterator(queue, allowRemove);
            }
            if (!iterator1.valid) {
                iterator1.index = 0;
                iterator1.valid = true;
                iterator2.valid = false;
                return iterator1;
            }
            iterator2.index = 0;
            iterator2.valid = true;
            iterator1.valid = false;
            return iterator2;
        }
    }
}
