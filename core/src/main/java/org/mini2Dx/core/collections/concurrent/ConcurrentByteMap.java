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
import org.mini2Dx.core.collections.ByteMap;
import org.mini2Dx.core.lock.ReadWriteLock;

public class ConcurrentByteMap<T> extends ByteMap<T> implements ConcurrentCollection {

    protected ReadWriteLock lock = Mdx.locks.newReadWriteLock();

    /**
     * Creates a new map with an initial capacity of 51 and a load factor of 0.8.
     */
    public ConcurrentByteMap() {
        super();
    }

    /**
     * Creates a new map with a load factor of 0.8.
     *
     * @param initialCapacity If not a power of two, it is increased to the next nearest power of two.
     */
    public ConcurrentByteMap(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Creates a new map with the specified initial capacity and load factor. This map will hold initialCapacity items before
     * growing the backing table.
     *
     * @param initialCapacity If not a power of two, it is increased to the next nearest power of two.
     * @param loadFactor
     */
    public ConcurrentByteMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * Creates a new map identical to the specified map.
     *
     * NOTE: read access to the other map is not thread-safe
     *
     * @param map
     */
    public ConcurrentByteMap(ByteMap<? extends T> map) {
        super(map);
    }

    @Override
    public T put(byte key, T value) {
        lock.lockWrite();
        T t = super.put(key, value);
        lock.unlockWrite();
        return t;
    }

    @Override
    public void putAll(ByteMap<T> map) {
        boolean isOtherConcurrent = map instanceof ConcurrentCollection;
        if (isOtherConcurrent){
            ((ConcurrentCollection)map).getLock().lockRead();
        }
        lock.lockWrite();
        super.putAll(map);
        lock.unlockWrite();
        if (isOtherConcurrent){
            ((ConcurrentCollection)map).getLock().unlockRead();
        }
    }

    @Override
    public T get(byte key, T defaultValue) {
        lock.lockRead();
        T t = super.get(key, defaultValue);
        lock.unlockRead();
        return t;
    }

    @Override
    public T remove(byte key) {
        lock.lockWrite();
        T t = super.remove(key);
        lock.unlockWrite();
        return t;
    }

    @Override
    public void shrink(int maximumCapacity) {
        lock.lockWrite();
        super.shrink(maximumCapacity);
        lock.unlockWrite();
    }

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
    public boolean containsValue(Object value, boolean identity) {
        lock.lockRead();
        boolean b = super.containsValue(value, identity);
        lock.unlockRead();
        return b;
    }

    @Override
    public boolean containsKey(byte key) {
        lock.lockRead();
        boolean b = super.containsKey(key);
        lock.unlockRead();
        return b;
    }

    @Override
    public byte findKey(Object value, boolean identity, byte notFound) {
        lock.lockRead();
        byte b = super.findKey(value, identity, notFound);
        lock.unlockRead();
        return b;
    }

    @Override
    public void ensureCapacity(int additionalCapacity) {
        lock.lockWrite();
        super.ensureCapacity(additionalCapacity);
        lock.unlockWrite();
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
    public Entries<T> entries() {
        return new Entries<>(this);
    }

    @Override
    public Values<T> values() {
        return new Values<>(this);
    }

    @Override
    public Keys keys() {
        return new Keys(this);
    }

    @Override
    public ReadWriteLock getLock() {
        return lock;
    }
}
