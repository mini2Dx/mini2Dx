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
import org.mini2Dx.gdx.utils.IntFloatMap;
import org.mini2Dx.lockprovider.ReadWriteLock;

public class ConcurrentIntFloatMap extends IntFloatMap implements ConcurrentCollection {

    protected ReadWriteLock lock = Mdx.locks.newReadWriteLock();

    /**
     * Creates a new map with an initial capacity of 51 and a load factor of 0.8.
     */
    public ConcurrentIntFloatMap() {
        super();
    }

    /**
     * Creates a new map with a load factor of 0.8.
     *
     * @param initialCapacity If not a power of two, it is increased to the next nearest power of two.
     */
    public ConcurrentIntFloatMap(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Creates a new map with the specified initial capacity and load factor. This map will hold initialCapacity items before
     * growing the backing table.
     *
     * @param initialCapacity If not a power of two, it is increased to the next nearest power of two.
     * @param loadFactor
     */
    public ConcurrentIntFloatMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * Creates a new map identical to the specified map.
     *
     * NOTE: read access to the other map is not thread-safe
     *
     * @param map
     */
    public ConcurrentIntFloatMap(IntFloatMap map) {
        super(map);
    }

    /**
     * Returns the size in a thread-safe manner
     * @return 0 if empty
     */
    public int size() {
        lock.lockRead();
        final int result = super.size;
        lock.unlockRead();
        return result;
    }

    @Override
    public void put(int key, float value) {
        lock.lockWrite();
        super.put(key, value);
        lock.unlockWrite();
    }

    /**
     * Puts a key/value if the key is not already present
     * @param key The key to put if absent
     * @param value The value to put if absent
     * @return True if the value was put
     */
    public boolean putIfAbsent(int key, float value) {
        boolean result = false;
        lock.lockWrite();
        if(!super.containsKey(key)) {
            super.put(key, value);
            result = true;
        }
        lock.unlockWrite();
        return result;
    }

    /**
     * Puts a key/value if the key is already present
     * @param key The key to put if present
     * @param value The value to put if present
     * @return True if the value was put
     */
    public boolean putIfPresent(int key, float value) {
        boolean result = false;
        lock.lockWrite();
        if(super.containsKey(key)) {
            super.put(key, value);
            result = true;
        }
        lock.unlockWrite();
        return result;
    }

    @Override
    public float put(int key, float value, float defaultValue) {
        lock.lockWrite();
        final float result = super.put(key, value, defaultValue);
        lock.unlockWrite();
        return result;
    }

    @Override
    public void putAll(IntFloatMap map) {
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
    public float get(int key, float defaultValue) {
        lock.lockRead();
        float f = super.get(key, defaultValue);
        lock.unlockRead();
        return f;
    }

    /**
     * Returns the key's current value and increments the stored value. If the key is not in the map, defaultValue + increment is
     * put into the map.
     *
     * @param key
     * @param defaultValue
     * @param increment
     */
    @Override
    public float getAndIncrement(int key, float defaultValue, float increment) {
        lock.lockWrite();
        float f = super.getAndIncrement(key, defaultValue, increment);
        lock.unlockWrite();
        return f;
    }

    @Override
    public float remove(int key, float defaultValue) {
        lock.lockWrite();
        float f = super.remove(key, defaultValue);
        lock.unlockWrite();
        return f;
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

    /**
     * Returns true if the specified value is in the map. Note this traverses the entire map and compares every value, which may
     * be an expensive operation.
     *
     * @param value
     */
    @Override
    public boolean containsValue(float value) {
        lock.lockRead();
        boolean b = super.containsValue(value);
        lock.unlockRead();
        return b;
    }

    /**
     * Returns true if the specified value is in the map. Note this traverses the entire map and compares every value, which may
     * be an expensive operation.
     *
     * @param value
     * @param epsilon
     */
    @Override
    public boolean containsValue(float value, float epsilon) {
        lock.lockRead();
        boolean b = super.containsValue(value, epsilon);
        lock.unlockRead();
        return b;
    }

    @Override
    public boolean containsKey(int key) {
        lock.lockRead();
        boolean b = super.containsKey(key);
        lock.unlockRead();
        return b;
    }

    /**
     * Returns the key for the specified value, or null if it is not in the map. Note this traverses the entire map and compares
     * every value, which may be an expensive operation.
     *
     * @param value
     * @param notFound
     */
    @Override
    public int findKey(float value, int notFound) {
        lock.lockRead();
        int i = super.findKey(value, notFound);
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
     */
    @Override
    public Entries entries() {
        return new Entries(this);
    }

    /**
     * Returns an iterator for the values in the map. Remove is supported.
     */
    @Override
    public Values values() {
        return new Values(this);
    }

    /**
     * Returns an iterator for the keys in the map. Remove is supported.
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
