/*******************************************************************************
 * Copyright 2019 See AUTHORS file
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

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class FloatQueueTest {
    @Test
    public void addFirstAndLastTest() {
        FloatQueue queue = new FloatQueue();
        queue.addFirst((float) 1);
        queue.addLast((float) 2);
        queue.addFirst((float) 3);
        queue.addLast((float) 4);

        assertEquals(0, queue.indexOf((float) 3));
        assertEquals(1, queue.indexOf((float) 1));
        assertEquals(2, queue.indexOf((float) 2));
        assertEquals(3, queue.indexOf((float) 4));
    }

    @Test
    public void removeLastTest() {
        FloatQueue queue = new FloatQueue();
        queue.addLast((float) 1);
        queue.addLast((float) 2);
        queue.addLast((float) 3);
        queue.addLast((float) 4);

        assertEquals(4, queue.size);
        assertEquals(3, queue.indexOf((float) 4));
        assertEquals(4, queue.removeLast(), 0.01);

        assertEquals(3, queue.size);
        assertEquals(2, queue.indexOf((float) 3));
        assertEquals(3, queue.removeLast(), 0.01);

        assertEquals(2, queue.size);
        assertEquals(1, queue.indexOf((float) 2));
        assertEquals(2, queue.removeLast(), 0.01);

        assertEquals(1, queue.size);
        assertEquals(0, queue.indexOf((float) 1));
        assertEquals(1, queue.removeLast(), 0.01);

        assertEquals(0, queue.size);
    }

    @Test
    public void removeFirstTest() {
        FloatQueue queue = new FloatQueue();
        queue.addLast((float) 1);
        queue.addLast((float) 2);
        queue.addLast((float) 3);
        queue.addLast((float) 4);

        assertEquals(4, queue.size);
        assertEquals(0, queue.indexOf((float) 1), 0.01);
        assertEquals(1, queue.removeFirst(), 0.01);

        assertEquals(3, queue.size);
        assertEquals(0, queue.indexOf((float) 2), 0.01);
        assertEquals(2, queue.removeFirst(), 0.01);

        assertEquals(2, queue.size);
        assertEquals(0, queue.indexOf((float) 3), 0.01);
        assertEquals(3, queue.removeFirst(), 0.01);

        assertEquals(1, queue.size);
        assertEquals(0, queue.indexOf((float) 4), 0.01);
        assertEquals(4, queue.removeFirst(), 0.01);

        assertEquals(0, queue.size);
    }

    @Test
    public void resizableQueueTest () {
        final FloatQueue q = new FloatQueue(8);

        assertEquals("New queue is not empty!", 0, q.size);

        for (int i = 0; i < 100; i++) {

            for (int j = 0; j < i; j++) {
                try {
                    q.addLast((float) j);
                } catch (IllegalStateException e) {
                    fail("Failed to add element " + j + " (" + i + ")");
                }
                final float peeked = q.last();
                assertEquals("peekLast shows " + peeked + ", should be " + j + " (" + i + ")", peeked, j, 0.0);
                final int size = q.size;
                assertEquals("Size should be " + (j + 1) + " but is " + size + " (" + i + ")", size, j + 1);
            }

            if (i != 0) {
                final float peek = q.first();
                assertEquals("First thing is not zero but " + peek + " (" + i + ")", 0, peek, 0.0);
            }

            for (int j = 0; j < i; j++) {
                final float pop = q.removeFirst();
                assertEquals("Popped should be " + j + " but is " + pop + " (" + i + ")", pop, j, 0.0);

                final int size = q.size;
                assertEquals("Size should be " + (i - 1 - j) + " but is " + size + " (" + i + ")", size, i - 1 - j);
            }

            assertEquals("Not empty after cycle " + i, 0, q.size);
        }

        for (int i = 0; i < 56; i++) {
            q.addLast((float) 42);
        }
        q.clear();
        assertEquals("Clear did not clear properly", 0, q.size);
    }

    /** Same as resizableQueueTest, but in reverse */
    @Test
    public void resizableDequeTest () {
        final FloatQueue q = new FloatQueue(8);

        assertEquals("New deque is not empty!", 0, q.size);

        for (int i = 0; i < 100; i++) {

            for (int j = 0; j < i; j++) {
                try {
                    q.addFirst((float) j);
                } catch (IllegalStateException e) {
                    fail("Failed to add element " + j + " (" + i + ")");
                }
                final float peeked = q.first();
                assertEquals("peek shows " + peeked + ", should be " + j + " (" + i + ")", peeked, j, 0.0);
                final int size = q.size;
                assertEquals("Size should be " + (j + 1) + " but is " + size + " (" + i + ")", size, j + 1);
            }

            if (i != 0) {
                final float peek = q.last();
                assertEquals("Last thing is not zero but " + peek + " (" + i + ")", 0, peek, 0.0);
            }

            for (int j = 0; j < i; j++) {
                final float pop = q.removeLast();
                assertEquals("Popped should be " + j + " but is " + pop + " (" + i + ")", pop, j, 0.0);

                final int size = q.size;
                assertEquals("Size should be " + (i - 1 - j) + " but is " + size + " (" + i + ")", size, i - 1 - j);
            }

            assertEquals("Not empty after cycle " + i, 0, q.size);
        }

        for (int i = 0; i < 56; i++) {
            q.addFirst((float) 42);
        }
        q.clear();
        assertEquals("Clear did not clear properly", 0, q.size);
    }

    @Test
    public void getTest () {
        final FloatQueue q = new FloatQueue(7);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                q.addLast((float) j);
            }
            assertEquals("get(0) is not equal to peek (" + i + ")", q.get(0), q.first(), 0.01);
            assertEquals("get(size-1) is not equal to peekLast (" + i + ")", q.get(q.size - 1), q.last(), 0.01);
            for (int j = 0; j < 4; j++) {
                assertEquals(q.get(j), j, 0.0);
            }
            for (int j = 0; j < 4 - 1; j++) {
                q.removeFirst();
                assertEquals("get(0) is not equal to peek (" + i + ")", q.get(0), q.first(), 0.01);
            }
            q.removeFirst();
            assert q.size == 0; // Failing this means broken test
            try {
                q.get(0);
                fail("get() on empty queue did not throw");
            } catch (IndexOutOfBoundsException ignore) {
                // Expected
            }
        }
    }

    @Test
    public void removeTest () {
        final FloatQueue q = new FloatQueue();

        // Test head < tail.
        for (int j = 0; j <= 6; j++)
            q.addLast((float) j);
        assertValues(q, 0, 1, 2, 3, 4, 5, 6);
        q.removeIndex(0);
        assertValues(q, 1, 2, 3, 4, 5, 6);
        q.removeIndex(1);
        assertValues(q, 1, 3, 4, 5, 6);
        q.removeIndex(4);
        assertValues(q, 1, 3, 4, 5);
        q.removeIndex(2);
        assertValues(q, 1, 3, 5);

        // Test head >= tail and index >= head.
        q.clear();
        for (int j = 2; j >= 0; j--)
            q.addFirst((float) j);
        for (int j = 3; j <= 6; j++)
            q.addLast((float) j);
        assertValues(q, 0, 1, 2, 3, 4, 5, 6);
        q.removeIndex(1);
        assertValues(q, 0, 2, 3, 4, 5, 6);
        q.removeIndex(0);
        assertValues(q, 2, 3, 4, 5, 6);

        // Test head >= tail and index < tail.
        q.clear();
        for (int j = 2; j >= 0; j--)
            q.addFirst((float) j);
        for (int j = 3; j <= 6; j++)
            q.addLast((float) j);
        assertValues(q, 0, 1, 2, 3, 4, 5, 6);
        q.removeIndex(5);
        assertValues(q, 0, 1, 2, 3, 4, 6);
        q.removeIndex(5);
        assertValues(q, 0, 1, 2, 3, 4);
    }

    @Test
    public void indexOfTest () {
        final FloatQueue q = new FloatQueue();

        // Test head < tail.
        for (int j = 0; j <= 6; j++)
            q.addLast((float) j);
        for (int j = 0; j <= 6; j++)
            assertEquals(q.indexOf((float) j), j);

        // Test head >= tail.
        q.clear();
        for (int j = 2; j >= 0; j--)
            q.addFirst((float) j);
        for (int j = 3; j <= 6; j++)
            q.addLast((float) j);
        for (int j = 0; j <= 6; j++)
            assertEquals(q.indexOf((float) j), j);
    }

    @Test
    public void iteratorTest () {
        final FloatQueue q = new FloatQueue();

        // Test head < tail.
        for (int j = 0; j <= 6; j++)
            q.addLast((float) j);
        Iterator<Float> iter = q.iterator();
        for (int j = 0; j <= 6; j++)
            assertEquals(iter.next().intValue(), j);
        iter = q.iterator();
        iter.next();
        iter.remove();
        assertValues(q, 1, 2, 3, 4, 5, 6);
        iter.next();
        iter.remove();
        assertValues(q, 2, 3, 4, 5, 6);
        iter.next();
        iter.next();
        iter.remove();
        assertValues(q, 2, 4, 5, 6);
        iter.next();
        iter.next();
        iter.next();
        iter.remove();
        assertValues(q, 2, 4, 5);

        // Test head >= tail.
        q.clear();
        for (int j = 2; j >= 0; j--)
            q.addFirst((float) j);
        for (int j = 3; j <= 6; j++)
            q.addLast((float) j);
        iter = q.iterator();
        for (int j = 0; j <= 6; j++)
            assertEquals(iter.next().intValue(), j);
        iter = q.iterator();
        iter.next();
        iter.remove();
        assertValues(q, 1, 2, 3, 4, 5, 6);
        iter.next();
        iter.remove();
        assertValues(q, 2, 3, 4, 5, 6);
        iter.next();
        iter.next();
        iter.remove();
        assertValues(q, 2, 4, 5, 6);
        iter.next();
        iter.next();
        iter.next();
        iter.remove();
        assertValues(q, 2, 4, 5);
    }

    @Test
    public void iteratorRemoveEdgeCaseTest() {//See #4300
        FloatQueue queue = new FloatQueue();

        //Simulate normal usage
        for(int i = 0; i < 100; i++) {
            queue.addLast((float) i);
            if(i > 50)
                queue.removeFirst();
        }

        Iterator<Float> it = queue.iterator();
        while(it.hasNext()) {
            it.next();
            it.remove();
        }

        queue.addLast((float) 1337);

        float i = queue.first();
        assertEquals((float) 1337, i, 0.01);
    }

    @Test
    public void toStringTest () {
        FloatQueue q = new FloatQueue(1);
        assertEquals("[]", q.toString());
        q.addLast((float) 4);
        assertEquals("[4.0]", q.toString());
        q.addLast((float) 5);
        q.addLast((float) 6);
        q.addLast((float) 7);
        assertEquals("[4.0, 5.0, 6.0, 7.0]", q.toString());
    }

    @Test
    public void hashEqualsTest () {
        FloatQueue q1 = new FloatQueue();
        FloatQueue q2 = new FloatQueue();

        assertEqualsAndHash(q1, q2);
        q1.addFirst((float) 1);
        assertNotEquals(q1, q2);
        q2.addFirst((float) 1);
        assertEqualsAndHash(q1, q2);

        q1.clear();
        q1.addLast((float) 1);
        q1.addLast((float) 2);
        q2.addLast((float) 2);
        assertEqualsAndHash(q1, q2);

        for (int i = 0; i < 100; i++) {
            q1.addLast((float) i);
            q1.addLast((float) i);
            q1.removeFirst();

            assertNotEquals(q1, q2);

            q2.addLast((float) i);
            q2.addLast((float) i);
            q2.removeFirst();

            assertEqualsAndHash(q1, q2);
        }
    }

    private void assertEqualsAndHash (FloatQueue q1, FloatQueue q2) {
        assertEquals(q1, q2);
        assertEquals("Hash codes are not equal", q1.hashCode(), q2.hashCode());
    }

    private void assertValues (FloatQueue q, int... values) {
        for (int i = 0, n = values.length; i < n; i++) {
            assertEquals((float) values[i], q.get(i), 0.01);
        }
    }
}
