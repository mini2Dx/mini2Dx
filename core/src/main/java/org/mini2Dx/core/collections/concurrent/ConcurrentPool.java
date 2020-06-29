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
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.Pool;

public abstract class ConcurrentPool<T> extends Pool<T> implements ConcurrentCollection {

    protected ReadWriteLock lock = Mdx.locks.newReadWriteLock();

    /**
     * Creates a pool with an initial capacity of 16 and no maximum.
     */
    public ConcurrentPool() {
        super();
    }

    /**
     * Creates a pool with the specified initial capacity and no maximum.
     *
     * @param initialCapacity
     */
    public ConcurrentPool(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * @param initialCapacity
     * @param max             The maximum number of free objects to store in this pool.
     */
    public ConcurrentPool(int initialCapacity, int max) {
        super(initialCapacity, max);
    }

    /**
     * Returns an object from this pool. The object may be new (from {@link #newObject()}) or reused (previously
     * {@link #free(Object) freed}).
     */
    @Override
    public T obtain() {
        lock.lockWrite();
        T t = super.obtain();
        lock.unlockWrite();
        return t;
    }

    /**
     * Puts the specified object in the pool, making it eligible to be returned by {@link #obtain()}. If the pool already contains
     * {@link #max} free objects, the specified object is reset but not added to the pool.
     * <p>
     * The pool does not check if an object is already freed, so the same object must not be freed multiple times.
     *
     * @param object
     */
    @Override
    public void free(T object) {
        lock.lockWrite();
        super.free(object);
        lock.unlockWrite();
    }

    /**
     * Called when an object is freed to clear the state of the object for possible later reuse. The default implementation calls
     * {@link Poolable#reset()} if the object is {@link Poolable}.
     *
     * @param object
     */
    @Override
    protected void reset(T object) {
        super.reset(object);
    }

    /**
     * Puts the specified objects in the pool. Null objects within the array are silently ignored.
     * <p>
     * The pool does not check if an object is already freed, so the same object must not be freed multiple times.
     *
     * @param objects
     * @see #free(Object)
     */
    @Override
    public void freeAll(Array<T> objects) {
        boolean isOtherConcurrent = objects instanceof ConcurrentCollection;
        if (isOtherConcurrent){
            ((ConcurrentCollection) objects).getLock().lockRead();
        }
        super.freeAll(objects);
        if (isOtherConcurrent){
            ((ConcurrentCollection) objects).getLock().unlockRead();
        }
    }

    /**
     * Removes all free objects from this pool.
     */
    @Override
    public void clear() {
        lock.lockWrite();
        super.clear();
        lock.unlockWrite();
    }

    /**
     * The number of objects available to be obtained.
     */
    @Override
    public int getFree() {
        lock.lockRead();
        int i = super.getFree();
        lock.unlockRead();
        return i;
    }

    @Override
    public ReadWriteLock getLock() {
        return lock;
    }
}
