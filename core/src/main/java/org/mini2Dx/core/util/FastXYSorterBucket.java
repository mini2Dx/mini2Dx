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
package org.mini2Dx.core.util;

import org.mini2Dx.gdx.utils.Array;

public class FastXYSorterBucket<T extends Comparable<T>> {
    private final Array<T> queue;
    private boolean sortRequired = false;

    public FastXYSorterBucket(int expectedElements) {
        queue = new Array<>(expectedElements);
    }

    public void add(T obj) {
        queue.add(obj);
        sortRequired = true;
    }

    public boolean isEmpty() {
        return queue.size == 0;
    }

    public T poll(FastXYSorter instance) {
        switch (queue.size) {
        case 0:
            return null;
        case 1:
            instance.moveCursor = true;
            return queue.removeIndex(0);
        default:
            if(sortRequired) {
                queue.sort();
            }
            return queue.removeIndex(0);
        }
    }
}
