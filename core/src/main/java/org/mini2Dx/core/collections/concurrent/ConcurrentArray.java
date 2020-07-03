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

public class ConcurrentArray<T> extends Array<T> implements ConcurrentCollection {

    protected ReadWriteLock lock = Mdx.locks.newReadWriteLock();

    /**
     * Creates an ordered array with a capacity of 16.
     */
    public ConcurrentArray() {
    }

    /**
     * Creates an ordered array with the specified capacity.
     *
     * @param capacity
     */
    public ConcurrentArray(int capacity) {
        super(capacity);
    }

    /**
     * @param ordered  If false, methods that remove elements may change the order of other elements in the array, which avoids a
     *                 memory copy.
     * @param capacity Any elements added beyond this will cause the backing array to be grown.
     */
    public ConcurrentArray(boolean ordered, int capacity) {
        super(ordered, capacity);
    }

    /**
     * Creates a new array with {@link #items} of the specified type.
     *
     * @param ordered   If false, methods that remove elements may change the order of other elements in the array, which avoids a
     *                  memory copy.
     * @param capacity  Any elements added beyond this will cause the backing array to be grown.
     * @param arrayType
     */
    public ConcurrentArray(boolean ordered, int capacity, Class arrayType) {
        super(ordered, capacity, arrayType);
    }

    /**
     * Creates an ordered array with {@link #items} of the specified type and a capacity of 16.
     *
     * @param arrayType
     */
    public ConcurrentArray(Class arrayType) {
        super(arrayType);
    }

    /**
     * Creates a new array containing the elements in the specified array. The new array will have the same type of backing array
     * and will be ordered if the specified array is ordered. The capacity is set to the number of elements, so any subsequent
     * elements added will cause the backing array to be grown.
     *
     * NOTE: read access to the other Array is not thread-safe
     *
     * @param array
     */
    public ConcurrentArray(Array<? extends T> array) {
        super(array);
    }

    /**
     * Creates a new ordered array containing the elements in the specified array. The new array will have the same type of
     * backing array. The capacity is set to the number of elements, so any subsequent elements added will cause the backing array
     * to be grown.
     *
     * @param array
     */
    public ConcurrentArray(T[] array) {
        super(array);
    }

    /**
     * Creates a new array containing the elements in the specified array. The new array will have the same type of backing array.
     * The capacity is set to the number of elements, so any subsequent elements added will cause the backing array to be grown.
     *
     * @param ordered If false, methods that remove elements may change the order of other elements in the array, which avoids a
     *                memory copy.
     * @param array
     * @param start
     * @param count
     */
    public ConcurrentArray(boolean ordered, T[] array, int start, int count) {
        super(ordered, array, start, count);
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
        T elem = super.get(index);
        lock.unlockRead();
        return elem;
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

    /**
     * Returns true if this array contains the specified value.
     *
     * @param value    May be null.
     * @param identity If true, == comparison will be used. If false, .equals() comparison will be used.
     */
    @Override
    public boolean contains(T value, boolean identity) {
        lock.lockRead();
        boolean cont = super.contains(value, identity);
        lock.unlockRead();
        return cont;
    }

    /**
     * Returns true if this array contains all the specified values.
     *
     * @param values   May contains nulls.
     * @param identity If true, == comparison will be used. If false, .equals() comparison will be used.
     */
    @Override
    public boolean containsAll(Array<? extends T> values, boolean identity) {
        boolean isOtherConcurrent = values instanceof ConcurrentCollection;
        if (isOtherConcurrent){
            ((ConcurrentCollection)values).getLock().lockRead();
        }
        lock.lockRead();
        boolean b = super.containsAll(values, identity);
        lock.unlockRead();
        if (isOtherConcurrent){
            ((ConcurrentCollection)values).getLock().unlockRead();
        }
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
        boolean isOtherConcurrent = values instanceof ConcurrentCollection;
        if (isOtherConcurrent){
            ((ConcurrentCollection)values).getLock().lockRead();
        }
        lock.lockRead();
        boolean b = super.containsAny(values, identity);
        lock.unlockRead();
        if (isOtherConcurrent){
            ((ConcurrentCollection)values).getLock().unlockRead();
        }
        return b;
    }

    /**
     * Removes the first instance of the specified value in the array.
     *
     * @param value    May be null.
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
     * Removes from this array all of elements contained in the specified array.
     *
     * @param array
     * @param identity True to use ==, false to use .equals().
     * @return true if this array was modified.
     */
    @Override
    public boolean removeAll(Array<? extends T> array, boolean identity) {
        boolean isOtherConcurrent = array instanceof ConcurrentCollection;
        if (isOtherConcurrent){
            ((ConcurrentCollection)array).getLock().lockRead();
        }
        lock.lockWrite();
        boolean b = super.removeAll(array, identity);
        lock.unlockWrite();
        if (isOtherConcurrent){
            ((ConcurrentCollection)array).getLock().unlockRead();
        }
        return b;
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
        T e = super.selectRanked(comparator, kthLowest);
        lock.unlockRead();
        return e;
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
     * Returns the index of first occurrence of value in the array, or -1 if no such value exists.
     *
     * @param value    May be null.
     * @param identity If true, == comparison will be used. If false, .equals() comparison will be used.
     * @return An index of first occurrence of value in array or -1 if no such value exists
     */
    @Override
    public int indexOf(T value, boolean identity) {
        lock.lockRead();
        int idx = super.indexOf(value, identity);
        lock.unlockRead();
        return idx;
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
        int idx = super.lastIndexOf(value, identity);
        lock.unlockRead();
        return idx;
    }

    /**
     * Removes and returns the item at the specified index.
     *
     * @param index
     */
    @Override
    public T removeIndex(int index) {
        lock.lockWrite();
        T elem = super.removeIndex(index);
        lock.unlockWrite();
        return elem;
    }

    /**
     * Removes the items between the specified indices, inclusive.
     *
     * @param start
     * @param end
     */
    @Override
    public void removeRange(int start, int end) {
        lock.lockWrite();
        super.removeRange(start, end);
        lock.unlockWrite();
    }

    /**
     * Removes and returns the last item.
     */
    @Override
    public T pop() {
        lock.lockWrite();
        T elem = super.pop();
        lock.unlockWrite();
        return elem;
    }

    /**
     * Returns the last item.
     */
    @Override
    public T peek() {
        lock.lockRead();
        T elem = super.peek();
        lock.unlockRead();
        return elem;
    }

    /**
     * Returns the first item.
     */
    @Override
    public T first() {
        lock.lockRead();
        T elem = super.first();
        lock.unlockRead();
        return elem;
    }

    /**
     * Returns true if the array has one or more items.
     */
    @Override
    public boolean notEmpty() {
        lock.lockRead();
        boolean notE = super.notEmpty();
        lock.unlockRead();
        return notE;
    }

    /**
     * Returns true if the array is empty.
     */
    @Override
    public boolean isEmpty() {
        lock.lockRead();
        boolean isE = super.isEmpty();
        lock.unlockRead();
        return isE;
    }

    @Override
    public void clear() {
        lock.lockWrite();
        super.clear();
        lock.unlockWrite();
    }

    /**
     * Returns an iterator for the items in the array. Remove is supported.
     */
    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator<>(this);
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
        T[] elems = super.shrink();
        lock.unlockWrite();
        return elems;
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
        T[] elems = super.ensureCapacity(additionalCapacity);
        lock.unlockWrite();
        return elems;
    }

    /**
     * Sets the array size, leaving any values beyond the current size null.
     *
     * @param newSize
     * @return {@link #items}
     */
    @Override
    public T[] setSize(int newSize) {
        lock.lockWrite();
        T[] elems = super.setSize(newSize);
        lock.unlockWrite();
        return elems;
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

    /**
     * Reduces the size of the array to the specified size. If the array is already smaller than the specified size, no action is
     * taken.
     *
     * @param newSize
     */
    @Override
    public void truncate(int newSize) {
        lock.lockWrite();
        super.truncate(newSize);
        lock.unlockWrite();
    }

    /**
     * Returns a random item from the array, or null if the array is empty.
     */
    @Override
    public T random() {
        lock.lockRead();
        T elem = super.random();
        lock.unlockRead();
        return elem;
    }

    /**
     * Returns the items as an array. Note the array is typed, so the {@link #ConcurrentArray(Class)} constructor must have been used.
     * Otherwise use {@link #toArray(Class)} to specify the array type.
     */
    @Override
    public T[] toArray() {
        lock.lockRead();
        T[] elems = super.toArray();
        lock.unlockRead();
        return elems;
    }

    @Override
    public <V> V[] toArray(Class<V> type) {
        lock.lockRead();
        V[] elems = super.toArray(type);
        lock.unlockRead();
        return elems;
    }

    @Override
    public int hashCode() {
        lock.lockRead();
        int hc = super.hashCode();
        lock.unlockRead();
        return hc;
    }

    /**
     * Returns false if either array is unordered.
     *
     * @param object
     */
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

    /**
     * Uses == for comparison of each item. Returns false if either array is unordered.
     *
     * @param object
     */
    @Override
    public boolean equalsIdentity(Object object) {
        boolean isOtherConcurrent = object instanceof ConcurrentCollection;
        if (isOtherConcurrent){
            ((ConcurrentCollection) object).getLock().lockRead();
        }
        lock.lockRead();
        boolean e = super.equalsIdentity(object);
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
