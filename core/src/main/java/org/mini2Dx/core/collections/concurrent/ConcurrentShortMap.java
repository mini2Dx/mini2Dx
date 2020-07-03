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
import org.mini2Dx.core.collections.ShortMap;
import org.mini2Dx.core.lock.ReadWriteLock;

public class ConcurrentShortMap<T> extends ShortMap<T> implements ConcurrentCollection {

    protected ReadWriteLock lock = Mdx.locks.newReadWriteLock();

    public ConcurrentShortMap() {
        super();
    }

    public ConcurrentShortMap(int initialCapacity) {
        super(initialCapacity);
    }

    public ConcurrentShortMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * NOTE: read access to the other map is not thread-safe
     * @param map
     */
    public ConcurrentShortMap(ShortMap<? extends T> map) {
        super(map);
    }

    @Override
    public T put(short key, T value) {
        lock.lockWrite();
        T t = super.put(key, value);
        lock.unlockWrite();
        return t;
    }

    @Override
    public void putAll(ShortMap<T> map) {
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
    public T get(short key, T defaultValue) {
        lock.lockRead();
        T t = super.get(key, defaultValue);
        lock.unlockRead();
        return t;
    }

    @Override
    public T remove(short key) {
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
    public boolean containsKey(short key) {
        lock.lockRead();
        boolean b = super.containsKey(key);
        lock.unlockRead();
        return b;
    }

    @Override
    public short findKey(Object value, boolean identity, short notFound) {
        lock.lockRead();
        short s = super.findKey(value, identity, notFound);
        lock.unlockRead();
        return s;
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
