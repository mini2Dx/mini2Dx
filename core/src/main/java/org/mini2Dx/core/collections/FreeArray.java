/*******************************************************************************
 * Copyright 2022 See AUTHORS file
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
import org.mini2Dx.gdx.utils.compat.ArrayReflection;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class FreeArray<T> implements Iterable<T> {
    public FreeElement<T>[] items;
    public int length = 0;
    public int totalItems = 0;
    public int nextFreeIndex = -1;

    private FreeArray.FreeArrayIterable iterable;

    public FreeArray () {
        this(16);
    }

    public FreeArray(int capacity) {
        items = (FreeElement<T>[]) ArrayReflection.newInstance(FreeElement.class, capacity);
    }

    public T get(int index) {
        return items[index].element;
    }

    public int add(T item) {
        FreeElement<T>[] items = this.items;
        if (length == items.length) {
            items = resize(Math.max(8, (int) (length * 1.75f)));
        }
        totalItems++;

        if(nextFreeIndex != -1) {
            final int result = nextFreeIndex;
            items[nextFreeIndex].element = item;
            nextFreeIndex = items[nextFreeIndex].nextFreeIndex;
            return result;
        }
        FreeElement<T> element = new FreeElement<>();
        element.element = item;
        items[length++] = element;
        return length - 1;
    }

    public T remove(int index) {
        T result = items[index].element;
        items[index].element = null;
        items[index].nextFreeIndex = nextFreeIndex;
        nextFreeIndex = index;
        totalItems--;
        return result;
    }

    public void clear() {
        Arrays.fill(items, 0, length, null);
        length = 0;
        totalItems = 0;
        nextFreeIndex = -1;
    }

    protected FreeElement<T>[] resize (int newSize) {
        FreeElement<T>[] items = this.items;
        FreeElement<T>[] newItems = (FreeElement<T>[]) ArrayReflection.newInstance(items.getClass().getComponentType(), newSize);
        System.arraycopy(items, 0, newItems, 0, Math.min(length, newItems.length));
        this.items = newItems;
        return newItems;
    }

    @Override
    public Iterator<T> iterator() {
        if (iterable == null) {
            iterable = new FreeArray.FreeArrayIterable(this);
        }
        return iterable.iterator();
    }

    static public class FreeArrayIterator<T> implements Iterator<T>, Iterable<T> {
        private final FreeArray<T> array;
        private final boolean allowRemove;
        int index;
        boolean valid = true;

        public FreeArrayIterator (FreeArray<T> array) {
            this(array, true);
        }

        public FreeArrayIterator (FreeArray<T> array, boolean allowRemove) {
            this.array = array;
            this.allowRemove = allowRemove;
        }

        public boolean hasNext () {
            if (!valid) {
                throw new MdxException("#iterator() cannot be used nested.");
            }
            return index < array.length;
        }

        public T next () {
            if (index >= array.length) throw new NoSuchElementException(String.valueOf(index));
            if (!valid) {
                throw new MdxException("#iterator() cannot be used nested.");
            }
            return array.items[index++].element;
        }

        public void remove () {
            if (!allowRemove) throw new MdxException("Remove not allowed.");
            index--;
            array.remove(index);
        }

        public void reset () {
            index = 0;
        }

        public FreeArray.FreeArrayIterator<T> iterator () {
            return this;
        }
    }

    static public class FreeArrayIterable<T> implements Iterable<T> {
        private final FreeArray<T> array;
        private final boolean allowRemove;
        private FreeArray.FreeArrayIterator iterator1, iterator2;

        public FreeArrayIterable (FreeArray<T> array) {
            this(array, true);
        }

        public FreeArrayIterable (FreeArray<T> array, boolean allowRemove) {
            this.array = array;
            this.allowRemove = allowRemove;
        }

        public FreeArray.FreeArrayIterator<T> iterator () {
            if (iterator1 == null) {
                iterator1 = new FreeArray.FreeArrayIterator(array, allowRemove);
                iterator2 = new FreeArray.FreeArrayIterator(array, allowRemove);
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
