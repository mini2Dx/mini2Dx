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

import org.mini2Dx.gdx.math.RandomXS128;
import org.mini2Dx.gdx.utils.Array;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.PriorityQueue;

public class FastXYSorterTest {
    private static final int SEED = 203562035;
    private static final int WIDTH = 1920 / 8;
    private static final int HEIGHT = 1080 / 8;
    private static final int EXPECTED_ELEMENTS_PER_BUCKET = 8;
    private static final int TOTAL_ELEMENTS = 4096;

    @State(Scope.Thread)
    public static class TestState {
        public FastXYSorter<TestCollision> sorter = new FastXYSorter<>(WIDTH, HEIGHT, EXPECTED_ELEMENTS_PER_BUCKET);
        public PriorityQueue<TestCollision> queue = new PriorityQueue<>(TOTAL_ELEMENTS);

        public RandomXS128 random = new RandomXS128(SEED);
        public Array<TestCollision> elements = new Array<TestCollision>(TOTAL_ELEMENTS);
        public int result;

        @Setup(Level.Trial)
        public void setUp() throws IOException {
            if(elements.size != 0) {
                return;
            }
            for(int i = 0; i < TOTAL_ELEMENTS; i++) {
                TestCollision collision = new TestCollision();
                collision.x = random.nextInt(WIDTH);
                collision.y = random.nextInt(HEIGHT);
                elements.add(collision);
            }
        }
    }

    @Benchmark
    @BenchmarkMode(value= Mode.AverageTime)
    public void testFastXYSorter(FastXYSorterTest.TestState state) {
        for(int i = 0; i < TOTAL_ELEMENTS; i++) {
            final TestCollision collision = state.elements.get(i);
            state.sorter.add(collision.x, collision.y, collision);
        }

        state.sorter.begin();
        TestCollision result = state.sorter.poll();
        int total = 0;
        while(result != null) {
            total = result.x * result.y;
            result = state.sorter.poll();
        }
        state.result = total;
    }

    @Benchmark
    @BenchmarkMode(value=Mode.AverageTime)
    public void testPriorityQueue(FastXYSorterTest.TestState state) {
        for(int i = 0; i < TOTAL_ELEMENTS; i++) {
            final TestCollision collision = state.elements.get(i);
            state.queue.offer(collision);
        }

        TestCollision result = state.queue.poll();
        int total = 0;
        while(result != null) {
            total = result.x * result.y;
            result = state.queue.poll();
        }
        state.result = total;
    }

    public static class TestCollision implements Comparable<TestCollision> {
        public int x, y;

        @Override
        public int compareTo(TestCollision o) {
            int result = Integer.compare(y, o.y);
            if (result == 0) {
                return Integer.compare(x, o.x);
            }
            return result;
        }
    }
}
