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
import org.mini2Dx.gdx.utils.*;

import java.util.Comparator;
import java.util.Iterator;

public class ConcurrentDelayedRemovalArray<T> extends DelayedRemovalArray<T> implements ConcurrentCollection{

    protected ReadWriteLock lock = Mdx.locks.newReadWriteLock();

    public ConcurrentDelayedRemovalArray() {
        super();
    }

    /**
     * NOTE: read access to the other array is not thread-safe
     * @param array
     */
    public ConcurrentDelayedRemovalArray(Array<? extends T> array) {
        super(array);
    }

    public ConcurrentDelayedRemovalArray(boolean ordered, int capacity, Class arrayType) {
        super(ordered, capacity, arrayType);
    }

    public ConcurrentDelayedRemovalArray(boolean ordered, int capacity) {
        super(ordered, capacity);
    }

    public ConcurrentDelayedRemovalArray(boolean ordered, T[] array, int startIndex, int count) {
        super(ordered, array, startIndex, count);
    }

    public ConcurrentDelayedRemovalArray(Class arrayType) {
        super(arrayType);
    }

    public ConcurrentDelayedRemovalArray(int capacity) {
        super(capacity);
    }

    public ConcurrentDelayedRemovalArray(T[] array) {
        super(array);
    }

    @Override
    public void begin() {
        lock.lockWrite();
        super.begin();
        lock.unlockWrite();
    }

    @Override
    public void end() {
        lock.lockWrite();
        super.end();
        lock.unlockWrite();
    }

    @Override
    public boolean removeValue(T value, boolean identity) {
        lock.lockWrite();
        boolean b = super.removeValue(value, identity);
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
    public void removeRange(int start, int end) {
        lock.lockWrite();
        super.removeRange(start, end);
        lock.unlockWrite();
    }

    @Override
    public void clear() {
        lock.lockWrite();
        super.clear();
        lock.unlockWrite();
    }

    @Override
    public void set(int index, T value) {
        lock.lockWrite();
        super.set(index, value);
        lock.unlockWrite();
    }

    @Override
    public void insert(int index, T value) {
        lock.lockWrite();
        super.insert(index, value);
        lock.unlockWrite();
    }

    @Override
    public void swap(int first, int second) {
        lock.lockWrite();
        super.swap(first, second);
        lock.unlockWrite();
    }

    @Override
    public T pop() {
        lock.lockWrite();
        T t = super.pop();
        lock.unlockWrite();
        return t;
    }

    @Override
    public void sort() {
        lock.lockWrite();
        super.sort();
        lock.unlockWrite();
    }

    @Override
    public void sort(Comparator<? super T> comparator) {
        lock.lockWrite();
        super.sort(comparator);
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

    @Override
    public void truncate(int newSize) {
        lock.lockWrite();
        super.truncate(newSize);
        lock.unlockWrite();
    }

    @Override
    public T[] setSize(int newSize) {
        lock.lockWrite();
        T[] t = super.setSize(newSize);
        lock.unlockWrite();
        return t;
    }

    @Override
    public void add(T value) {
        lock.lockWrite();
        super.add(value);
        lock.unlockWrite();
    }

    @Override
    public void add(T value1, T value2) {
        lock.lockWrite();
        super.add(value1, value2);
        lock.unlockWrite();
    }

    @Override
    public void add(T value1, T value2, T value3) {
        lock.lockWrite();
        super.add(value1, value2, value3);
        lock.unlockWrite();
    }

    @Override
    public void add(T value1, T value2, T value3, T value4) {
        lock.lockWrite();
        super.add(value1, value2, value3, value4);
        lock.unlockWrite();
    }

    @Override
    public void addAll(Array<? extends T> array, int start, int count) {
        boolean isOtherConcurrent = array instanceof ConcurrentCollection;
        if (isOtherConcurrent){
            ((ConcurrentCollection)array).getLock().lockRead();
        }
        lock.lockWrite();
        super.addAll(array, start, count);
        lock.unlockWrite();
        if (isOtherConcurrent){
            ((ConcurrentCollection)array).getLock().unlockRead();
        }
    }

    @Override
    public void addAll(T[] array, int start, int count) {
        lock.lockWrite();
        super.addAll(array, start, count);
        lock.unlockWrite();
    }

    @Override
    public T get(int index) {
        lock.lockRead();
        T t = super.get(index);
        lock.unlockRead();
        return t;
    }

    /**
     * Returns true if this array contains the specified value.
     *
     * @param value    May be null.
     * @param identity If true, == comparison will be used. If false, .equals() comparison will be used.
     */
    @Override
    public boolean contains(T value, boolean identity) {
        lock.lockRead();
        boolean b = super.contains(value, identity);
        lock.unlockRead();
        return b;
    }

    /**
     * Returns true if this array contains all the specified values.
     *
     * @param values   May contains nulls.
     * @param identity If true, == comparison will be used. If false, .equals() comparison will be used.
     */
    @Override
    public boolean containsAll(Array<? extends T> values, boolean identity) {
        lock.lockRead();
        boolean b = super.containsAll(values, identity);
        lock.unlockRead();
        return b;
    }

    /**
     * Returns true if this array contains any the specified values.
     *
     * @param values   May contains nulls.
     * @param identity If true, == comparison will be used. If false, .equals() comparison will be used.
     */
    @Override
    public boolean containsAny(Array<? extends T> values, boolean identity) {
        lock.lockRead();
        boolean b = super.containsAny(values, identity);
        lock.unlockRead();
        return b;
    }

    /**
     * Returns the index of first occurrence of value in the array, or -1 if no such value exists.
     *
     * @param value    May be null.
     * @param identity If true, == comparison will be used. If false, .equals() comparison will be used.
     * @return An index of first occurrence of value in array or -1 if no such value exists
     */
    @Override
    public int indexOf(T value, boolean identity) {
        lock.lockRead();
        int i = super.indexOf(value, identity);
        lock.unlockRead();
        return i;
    }

    /**
     * Returns an index of last occurrence of value in array or -1 if no such value exists. Search is started from the end of an
     * array.
     *
     * @param value    May be null.
     * @param identity If true, == comparison will be used. If false, .equals() comparison will be used.
     * @return An index of last occurrence of value in array or -1 if no such value exists
     */
    @Override
    public int lastIndexOf(T value, boolean identity) {
        lock.lockRead();
        int i = super.lastIndexOf(value, identity);
        lock.unlockRead();
        return i;
    }

    /**
     * Removes from this array all of elements contained in the specified array.
     *
     * @param array
     * @param identity True to use ==, false to use .equals().
     * @return true if this array was modified.
     */
    @Override
    public boolean removeAll(Array<? extends T> array, boolean identity) {
        lock.lockWrite();
        boolean b = super.removeAll(array, identity);
        lock.unlockWrite();
        return b;
    }

    /**
     * Returns the last item.
     */
    @Override
    public T peek() {
        lock.lockRead();
        T t = super.peek();
        lock.unlockRead();
        return t;
    }

    /**
     * Returns the first item.
     */
    @Override
    public T first() {
        lock.lockRead();
        T t = super.first();
        lock.unlockRead();
        return t;
    }

    /**
     * Returns true if the array has one or more items.
     */
    @Override
    public boolean notEmpty() {
        lock.lockRead();
        boolean b = super.notEmpty();
        lock.unlockRead();
        return b;
    }

    /**
     * Returns true if the array is empty.
     */
    @Override
    public boolean isEmpty() {
        lock.lockRead();
        boolean b = super.isEmpty();
        lock.unlockRead();
        return b;
    }

    /**
     * Reduces the size of the backing array to the size of the actual items. This is useful to release memory when many items
     * have been removed, or if it is known that more items will not be added.
     *
     * @return {@link #items}
     */
    @Override
    public T[] shrink() {
        lock.lockWrite();
        T[] t = super.shrink();
        lock.unlockWrite();
        return t;
    }

    /**
     * Increases the size of the backing array to accommodate the specified number of additional items. Useful before adding many
     * items to avoid multiple backing array resizes.
     *
     * @param additionalCapacity
     * @return {@link #items}
     */
    @Override
    public T[] ensureCapacity(int additionalCapacity) {
        lock.lockWrite();
        T[] t = super.ensureCapacity(additionalCapacity);
        lock.unlockWrite();
        return t;
    }

    /**
     * Selects the nth-lowest element from the Array according to Comparator ranking. This might partially sort the Array. The
     * array must have a size greater than 0, or a {@link GdxRuntimeException} will be thrown.
     *
     * @param comparator used for comparison
     * @param kthLowest  rank of desired object according to comparison, n is based on ordinal numbers, not array indices. for min
     *                   value use 1, for max value use size of array, using 0 results in runtime exception.
     * @return the value of the Nth lowest ranked object.
     * @see Select
     */
    @Override
    public T selectRanked(Comparator<T> comparator, int kthLowest) {
        lock.lockRead();
        T t = super.selectRanked(comparator, kthLowest);
        lock.unlockRead();
        return t;
    }

    /**
     * @param comparator used for comparison
     * @param kthLowest  rank of desired object according to comparison, n is based on ordinal numbers, not array indices. for min
     *                   value use 1, for max value use size of array, using 0 results in runtime exception.
     * @return the index of the Nth lowest ranked object.
     * @see Array#selectRanked(Comparator, int)
     */
    @Override
    public int selectRankedIndex(Comparator<T> comparator, int kthLowest) {
        lock.lockRead();
        int i = super.selectRankedIndex(comparator, kthLowest);
        lock.unlockRead();
        return i;
    }

    /**
     * Returns an iterator for the items in the array. Remove is supported.
     */
    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator<>(this);
    }

    /**
     * Returns an iterable for the selected items in the array. Remove is supported, but not between hasNext() and next().
     * <p>
     * If {@link Collections#allocateIterators} is false, the same iterable instance is returned each time this method is called.
     * Use the {@link Predicate.PredicateIterable} constructor for nested or multithreaded iteration.
     *
     * @param predicate
     */
    @Override
    public Iterable<T> select(Predicate<T> predicate) {
        lock.lockRead();
        Iterable<T> iT = super.select(predicate);
        lock.unlockRead();
        return iT;
    }

    /**
     * Returns a random item from the array, or null if the array is empty.
     */
    @Override
    public T random() {
        lock.lockRead();
        T t = super.random();
        lock.unlockRead();
        return t;
    }

    /**
     * Returns the items as an array. Note the array is typed, so the {@link #ConcurrentDelayedRemovalArray(Class)} constructor must have been used.
     * Otherwise use {@link #toArray(Class)} to specify the array type.
     */
    @Override
    public T[] toArray() {
        lock.lockRead();
        T[] t = super.toArray();
        lock.unlockRead();
        return t;
    }

    @Override
    public <V> V[] toArray(Class<V> type) {
        lock.lockRead();
        V[] v = super.toArray(type);
        lock.unlockRead();
        return v;
    }

    @Override
    public int hashCode() {
        lock.lockRead();
        int i = super.hashCode();
        lock.unlockRead();
        return i;
    }

    /**
     * Returns false if either array is unordered.
     *
     * @param object
     */
    @Override
    public boolean equals(Object object) {
        boolean isOtherConcurrent = object instanceof ConcurrentDelayedRemovalArray;
        if (isOtherConcurrent){
            ((ConcurrentDelayedRemovalArray)object).lock.lockRead();
        }
        lock.lockRead();
        boolean b = super.equals(object);
        lock.unlockRead();
        if (isOtherConcurrent){
            ((ConcurrentDelayedRemovalArray)object).lock.unlockRead();
        }
        return b;
    }

    /**
     * Uses == for comparison of each item. Returns false if either array is unordered.
     *
     * @param object
     */
    @Override
    public boolean equalsIdentity(Object object) {
        boolean isOtherConcurrent = object instanceof ConcurrentDelayedRemovalArray;
        if (isOtherConcurrent){
            ((ConcurrentDelayedRemovalArray)object).lock.lockRead();
        }
        lock.lockRead();
        boolean b = super.equalsIdentity(object);
        lock.unlockRead();
        if (isOtherConcurrent){
            ((ConcurrentDelayedRemovalArray)object).lock.unlockRead();
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

    @Override
    public ReadWriteLock getLock() {
        return lock;
    }
}
