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
import org.mini2Dx.gdx.utils.FlushablePool;

public abstract class ConcurrentFlushablePool<T> extends FlushablePool<T> implements ConcurrentCollection {

    protected ReadWriteLock lock = Mdx.locks.newReadWriteLock();

    public ConcurrentFlushablePool() {
        super();
    }

    public ConcurrentFlushablePool(int initialCapacity) {
        super(initialCapacity);
    }

    public ConcurrentFlushablePool(int initialCapacity, int max) {
        super(initialCapacity, max);
    }

    @Override
    public T obtain() {
        lock.lockWrite();
        T t = super.obtain();
        lock.unlockWrite();
        return t;
    }

    /**
     * Frees all obtained instances.
     */
    @Override
    public void flush() {
        lock.lockWrite();
        super.flush();
        lock.unlockWrite();
    }

    @Override
    public void free(T object) {
        lock.lockWrite();
        super.free(object);
        lock.unlockWrite();
    }

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
