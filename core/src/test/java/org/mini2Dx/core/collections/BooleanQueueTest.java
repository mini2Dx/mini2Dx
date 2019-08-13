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
public class BooleanQueueTest {
    @Test
    public void addFirstAndLastTest() {
        BooleanQueue queue = new BooleanQueue();
        queue.addFirst(true);
        queue.addLast(false);
        queue.addFirst(true);
        queue.addLast(false);

        assertEquals(0, queue.indexOf(true));
        assertEquals(2, queue.indexOf(false));
    }

    @Test
    public void removeLastTest() {
        BooleanQueue queue = new BooleanQueue();
        queue.addLast(true);
        queue.addLast(false);
        queue.addLast(true);
        queue.addLast(false);

        assertEquals(4, queue.size);
        assertFalse(queue.removeLast());

        assertEquals(3, queue.size);
        assertTrue(queue.removeLast());

        assertEquals(2, queue.size);
        assertFalse(queue.removeLast());

        assertEquals(1, queue.size);
        assertTrue(queue.removeLast());

        assertEquals(0, queue.size);
    }

    @Test
    public void removeFirstTest() {
        BooleanQueue queue = new BooleanQueue();
        queue.addLast(false);
        queue.addLast(true);
        queue.addLast(false);
        queue.addLast(true);

        assertEquals(4, queue.size);
        assertFalse(queue.removeFirst());

        assertEquals(3, queue.size);
        assertTrue(queue.removeFirst());

        assertEquals(2, queue.size);
        assertFalse(queue.removeFirst());

        assertEquals(1, queue.size);
        assertTrue(queue.removeFirst());

        assertEquals(0, queue.size);
    }

    @Test
    public void resizableQueueTest () {
        final BooleanQueue q = new BooleanQueue(8);

        assertEquals("New queue is not empty!", 0, q.size);

        for (int i = 0; i < 100; i++) {

            for (int j = 0; j < i; j++) {
                try {
                    q.addLast(j % 2 == 0);
                } catch (IllegalStateException e) {
                    fail("Failed to add element " + j + " (" + i + ")");
                }
                final boolean peeked = q.last();
                assertEquals("peekLast shows " + peeked + ", should be " + j + " (" + i + ")", peeked, (j % 2 == 0));
                final int size = q.size;
                assertEquals("Size should be " + (j + 1) + " but is " + size + " (" + i + ")", size, j + 1);
            }

            if (i != 0) {
                final boolean peek = q.first();
                assertTrue("First thing is not true but " + peek + " (" + i + ")", peek);
            }

            for (int j = 0; j < i; j++) {
                final boolean pop = q.removeFirst();
                assertEquals("Popped should be " + j + " but is " + pop + " (" + i + ")", pop, (j % 2 == 0));

                final int size = q.size;
                assertEquals("Size should be " + (i - 1 - j) + " but is " + size + " (" + i + ")", size, i - 1 - j);
            }

            assertEquals("Not empty after cycle " + i, 0, q.size);
        }

        for (int i = 0; i < 56; i++) {
            q.addLast(true);
        }
        q.clear();
        assertEquals("Clear did not clear properly", 0, q.size);
    }

    @Test
    public void resizableDequeTest () {
        final BooleanQueue q = new BooleanQueue(8);

        assertEquals("New deque is not empty!", 0, q.size);

        for (int i = 0; i < 100; i++) {

            for (int j = 0; j < i; j++) {
                try {
                    q.addFirst(j % 2 == 0);
                } catch (IllegalStateException e) {
                    fail("Failed to add element " + j + " (" + i + ")");
                }
                final boolean peeked = q.first();
                assertEquals("peek shows " + peeked + ", should be " + j + " (" + i + ")", peeked, (j % 2 == 0));
                final int size = q.size;
                assertEquals("Size should be " + (j + 1) + " but is " + size + " (" + i + ")", size, j + 1);
            }

            if (i != 0) {
                final boolean peek = q.last();
                assertTrue("Last thing is not zero but " + peek + " (" + i + ")", peek);
            }

            for (int j = 0; j < i; j++) {
                final boolean pop = q.removeLast();
                assertEquals("Popped should be " + j + " but is " + pop + " (" + i + ")", pop, (j % 2 == 0));

                final int size = q.size;
                assertEquals("Size should be " + (i - 1 - j) + " but is " + size + " (" + i + ")", size, i - 1 - j);
            }

            assertEquals("Not empty after cycle " + i, 0, q.size);
        }

        for (int i = 0; i < 56; i++) {
            q.addFirst(true);
        }
        q.clear();
        assertEquals("Clear did not clear properly", 0, q.size);
    }

    @Test
    public void getTest () {
        final BooleanQueue q = new BooleanQueue(7);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                q.addLast(j % 2 == 0);
            }
            assertEquals("get(0) is not equal to peek (" + i + ")", q.get(0), q.first());
            assertEquals("get(size-1) is not equal to peekLast (" + i + ")", q.get(q.size - 1), q.last());
            for (int j = 0; j < 4; j++) {
                assertEquals(q.get(j), (j % 2 == 0));
            }
            for (int j = 0; j < 4 - 1; j++) {
                q.removeFirst();
                assertEquals("get(0) is not equal to peek (" + i + ")", q.get(0), q.first());
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
        final BooleanQueue q = new BooleanQueue();

        // Test head < tail.
        for (int j = 0; j <= 6; j++)
            q.addLast(j % 2 == 0);
        assertValues(q, true, false, true, false, true, false, true);
        q.removeIndex(0);
        assertValues(q, false, true, false, true, false, true);
        q.removeIndex(1);
        assertValues(q, false, false, true, false, true);
        q.removeIndex(4);
        assertValues(q, false, false, true, false);
        q.removeIndex(2);
        assertValues(q, false, false, false);

        // Test head >= tail and index >= head.
        q.clear();
        for (int j = 2; j >= 0; j--)
            q.addFirst(j % 2 == 0);
        for (int j = 3; j <= 6; j++)
            q.addLast(j % 2 == 0);
        assertValues(q, true, false, true, false, true, false, true);
        q.removeIndex(1);
        assertValues(q, true, true, false, true, false, true);
        q.removeIndex(0);
        assertValues(q, true, false, true, false, true);

        // Test head >= tail and index < tail.
        q.clear();
        for (int j = 2; j >= 0; j--)
            q.addFirst(j % 2 == 0);
        for (int j = 3; j <= 6; j++)
            q.addLast(j % 2 == 0);
        assertValues(q, true, false, true, false, true, false, true);
        q.removeIndex(5);
        assertValues(q, true, false, true, false, true, true);
        q.removeIndex(5);
        assertValues(q, true, false, true, false, true);
    }

    @Test
    public void indexOfTest () {
        final BooleanQueue q = new BooleanQueue();

        // Test head < tail.
        for (int j = 0; j <= 6; j++)
            q.addLast(j % 2 == 0);
        for (int j = 0; j <= 6; j++)
            assertEquals(q.indexOf(j % 2 == 0), j % 2);

        // Test head >= tail.
        q.clear();
        for (int j = 2; j >= 0; j--)
            q.addFirst(j % 2 == 0);
        for (int j = 3; j <= 6; j++)
            q.addLast(j % 2 == 0);
        for (int j = 0; j <= 6; j++)
            assertEquals(q.indexOf(j % 2 == 0), j % 2);
    }

    @Test
    public void iteratorTest () {
        final BooleanQueue q = new BooleanQueue();

        // Test head < tail.
        for (int j = 0; j <= 6; j++)
            q.addLast(j % 2 == 0);
        Iterator<Boolean> iter = q.iterator();
        for (int j = 0; j <= 6; j++)
            assertEquals(iter.next(), j % 2 == 0);
        iter = q.iterator();
        iter.next();
        iter.remove();
        assertValues(q, false, true, false, true, false, true);
        iter.next();
        iter.remove();
        assertValues(q, true, false, true, false, true);
        iter.next();
        iter.next();
        iter.remove();
        assertValues(q, true, true, false, true);
        iter.next();
        iter.next();
        iter.next();
        iter.remove();
        assertValues(q, true, true, false);

        // Test head >= tail.
        q.clear();
        for (int j = 2; j >= 0; j--)
            q.addFirst(j % 2 == 0);
        for (int j = 3; j <= 6; j++)
            q.addLast(j % 2 == 0);
        iter = q.iterator();
        for (int j = 0; j <= 6; j++)
            assertEquals(iter.next(), j % 2 == 0);
        iter = q.iterator();
        iter.next();
        iter.remove();
        assertValues(q, false, true, false, true, false, true);
        iter.next();
        iter.remove();
        assertValues(q, true, false, true, false, true);
        iter.next();
        iter.next();
        iter.remove();
        assertValues(q, true, true, false, true);
        iter.next();
        iter.next();
        iter.next();
        iter.remove();
        assertValues(q, true, true, false);
    }

    @Test
    public void iteratorRemoveEdgeCaseTest() {//See libgdx/libgdx#4300
        BooleanQueue queue = new BooleanQueue();

        //Simulate normal usage
        for(int i = 0; i < 100; i++) {
            queue.addLast(false);
            if(i > 50)
                queue.removeFirst();
        }

        Iterator<Boolean> it = queue.iterator();
        while(it.hasNext()) {
            it.next();
            it.remove();
        }

        queue.addLast(true);

        boolean i = queue.first();
        assertTrue(i);
    }

    @Test
    public void toStringTest () {
        BooleanQueue q = new BooleanQueue(1);
        assertEquals("[]", q.toString());
        q.addLast(true);
        assertEquals("[true]", q.toString());
        q.addLast(false);
        q.addLast(false);
        q.addLast(true);
        assertEquals("[true, false, false, true]", q.toString());
    }

    @Test
    public void hashEqualsTest () {
        BooleanQueue q1 = new BooleanQueue();
        BooleanQueue q2 = new BooleanQueue();

        assertEqualsAndHash(q1, q2);
        q1.addFirst(true);
        assertNotEquals(q1, q2);
        q2.addFirst(true);
        assertEqualsAndHash(q1, q2);

        q1.clear();
        q1.addLast(true);
        q1.addLast(false);
        q2.addLast(false);
        assertEqualsAndHash(q1, q2);

        for (int i = 0; i < 100; i++) {
            q1.addLast(i % 2 == 0);
            q1.addLast(i % 2 == 0);
            q1.removeFirst();

            assertNotEquals(q1, q2);

            q2.addLast(i % 2 == 0);
            q2.addLast(i % 2 == 0);
            q2.removeFirst();

            assertEqualsAndHash(q1, q2);
        }
    }

    private void assertEqualsAndHash (BooleanQueue q1, BooleanQueue q2) {
        assertEquals(q1, q2);
        assertEquals("Hash codes are not equal", q1.hashCode(), q2.hashCode());
    }

    private void assertValues (BooleanQueue q, boolean... values) {
        for (int i = 0, n = values.length; i < n; i++) {
            assertEquals(values[i], q.get(i));
        }
    }
}
