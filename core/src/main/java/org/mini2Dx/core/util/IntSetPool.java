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

import org.mini2Dx.gdx.utils.IntSet;
import org.mini2Dx.gdx.utils.Queue;

public class IntSetPool {
    private static final int INITIAL_POOL_SIZE = 10;

    private static final Queue<IntSet> POOL = new Queue<>();

    static {
        for(int i = 0; i < INITIAL_POOL_SIZE; i++) {
            POOL.addLast(new IntSet(16));
        }
    }

    public static void warmup(int capacity) {
        synchronized(POOL) {
            while(POOL.size < capacity) {
                POOL.addLast(new IntSet(16));
            }
        }
    }

    public static IntSet allocate() {
        synchronized (POOL) {
            if(POOL.size == 0) {
                return new IntSet();
            }
            return POOL.removeFirst();
        }
    }

    public static void release(IntSet intSet) {
        intSet.clear();
        synchronized (POOL) {
            POOL.addLast(intSet);
        }
    }
}
