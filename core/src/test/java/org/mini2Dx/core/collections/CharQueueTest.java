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

public class CharQueueTest {
    @Test
    public void addFirstAndLastTest() {
        CharQueue queue = new CharQueue();
        queue.addFirst((char) 1);
        queue.addLast((char) 2);
        queue.addFirst((char) 3);
        queue.addLast((char) 4);

        assertEquals(0, queue.indexOf((char) 3));
        assertEquals(1, queue.indexOf((char) 1));
        assertEquals(2, queue.indexOf((char) 2));
        assertEquals(3, queue.indexOf((char) 4));
    }

    @Test
    public void removeLastTest() {
        CharQueue queue = new CharQueue();
        queue.addLast((char) 1);
        queue.addLast((char) 2);
        queue.addLast((char) 3);
        queue.addLast((char) 4);

        assertEquals(4, queue.size);
        assertEquals(3, queue.indexOf((char) 4));
        assertEquals((char) 4, queue.removeLast());

        assertEquals(3, queue.size);
        assertEquals(2, queue.indexOf((char) 3));
        assertEquals((char) 3, queue.removeLast());

        assertEquals(2, queue.size);
        assertEquals(1, queue.indexOf((char) 2));
        assertEquals((char) 2, queue.removeLast());

        assertEquals(1, queue.size);
        assertEquals(0, queue.indexOf((char) 1));
        assertEquals((char) 1, queue.removeLast());

        assertEquals(0, queue.size);
    }

    @Test
    public void removeFirstTest() {
        CharQueue queue = new CharQueue();
        queue.addLast((char) 1);
        queue.addLast((char) 2);
        queue.addLast((char) 3);
        queue.addLast((char) 4);

        assertEquals(4, queue.size);
        assertEquals(0, queue.indexOf((char) 1));
        assertEquals((char) 1, queue.removeFirst());

        assertEquals(3, queue.size);
        assertEquals(0, queue.indexOf((char) 2));
        assertEquals((char) 2, queue.removeFirst());

        assertEquals(2, queue.size);
        assertEquals(0, queue.indexOf((char) 3));
        assertEquals((char) 3, queue.removeFirst());

        assertEquals(1, queue.size);
        assertEquals(0, queue.indexOf((char) 4));
        assertEquals((char) 4, queue.removeFirst());

        assertEquals(0, queue.size);
    }

    @Test
    public void resizableQueueTest () {
        final CharQueue q = new CharQueue(8);

        assertEquals("New queue is not empty!", 0, q.size);

        for (int i = 0; i < 100; i++) {

            for (int j = 0; j < i; j++) {
                try {
                    q.addLast((char) j);
                } catch (IllegalStateException e) {
                    fail("Failed to add element " + j + " (" + i + ")");
                }
                final char peeked = q.last();
                assertEquals("peekLast shows " + peeked + ", should be " + j + " (" + i + ")", peeked, j);
                final int size = q.size;
                assertEquals("Size should be " + (j + 1) + " but is " + size + " (" + i + ")", size, j + 1);
            }

            if (i != 0) {
                final char peek = q.first();
                assertEquals("First thing is not zero but " + peek + " (" + i + ")", 0, peek);
            }

            for (int j = 0; j < i; j++) {
                final char pop = q.removeFirst();
                assertEquals("Popped should be " + j + " but is " + pop + " (" + i + ")", pop, j);

                final int size = q.size;
                assertEquals("Size should be " + (i - 1 - j) + " but is " + size + " (" + i + ")", size, i - 1 - j);
            }

            assertEquals("Not empty after cycle " + i, 0, q.size);
        }

        for (int i = 0; i < 56; i++) {
            q.addLast((char) 42);
        }
        q.clear();
        assertEquals("Clear did not clear properly", 0, q.size);
    }

    /** Same as resizableQueueTest, but in reverse */
    @Test
    public void resizableDequeTest () {
        final CharQueue q = new CharQueue(8);

        assertEquals("New deque is not empty!", 0, q.size);

        for (int i = 0; i < 100; i++) {

            for (int j = 0; j < i; j++) {
                try {
                    q.addFirst((char) j);
                } catch (IllegalStateException e) {
                    fail("Failed to add element " + j + " (" + i + ")");
                }
                final char peeked = q.first();
                assertEquals("peek shows " + peeked + ", should be " + j + " (" + i + ")", peeked, j);
                final int size = q.size;
                assertEquals("Size should be " + (j + 1) + " but is " + size + " (" + i + ")", size, j + 1);
            }

            if (i != 0) {
                final char peek = q.last();
                assertEquals("Last thing is not zero but " + peek + " (" + i + ")", 0, peek);
            }

            for (int j = 0; j < i; j++) {
                final char pop = q.removeLast();
                assertEquals("Popped should be " + j + " but is " + pop + " (" + i + ")", pop, j);

                final int size = q.size;
                assertEquals("Size should be " + (i - 1 - j) + " but is " + size + " (" + i + ")", size, i - 1 - j);
            }

            assertEquals("Not empty after cycle " + i, 0, q.size);
        }

        for (int i = 0; i < 56; i++) {
            q.addFirst((char) 42);
        }
        q.clear();
        assertEquals("Clear did not clear properly", 0, q.size);
    }

    @Test
    public void getTest () {
        final CharQueue q = new CharQueue(7);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                q.addLast((char) j);
            }
            assertEquals("get(0) is not equal to peek (" + i + ")", q.get(0), q.first());
            assertEquals("get(size-1) is not equal to peekLast (" + i + ")", q.get(q.size - 1), q.last());
            for (int j = 0; j < 4; j++) {
                assertEquals(q.get(j), j);
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
        final CharQueue q = new CharQueue();

        // Test head < tail.
        for (int j = 0; j <= 6; j++)
            q.addLast((char) j);
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
            q.addFirst((char) j);
        for (int j = 3; j <= 6; j++)
            q.addLast((char) j);
        assertValues(q, 0, 1, 2, 3, 4, 5, 6);
        q.removeIndex(1);
        assertValues(q, 0, 2, 3, 4, 5, 6);
        q.removeIndex(0);
        assertValues(q, 2, 3, 4, 5, 6);

        // Test head >= tail and index < tail.
        q.clear();
        for (int j = 2; j >= 0; j--)
            q.addFirst((char) j);
        for (int j = 3; j <= 6; j++)
            q.addLast((char) j);
        assertValues(q, 0, 1, 2, 3, 4, 5, 6);
        q.removeIndex(5);
        assertValues(q, 0, 1, 2, 3, 4, 6);
        q.removeIndex(5);
        assertValues(q, 0, 1, 2, 3, 4);
    }

    @Test
    public void indexOfTest () {
        final CharQueue q = new CharQueue();

        // Test head < tail.
        for (int j = 0; j <= 6; j++)
            q.addLast((char) j);
        for (int j = 0; j <= 6; j++)
            assertEquals(q.indexOf((char) j), j);

        // Test head >= tail.
        q.clear();
        for (int j = 2; j >= 0; j--)
            q.addFirst((char) j);
        for (int j = 3; j <= 6; j++)
            q.addLast((char) j);
        for (int j = 0; j <= 6; j++)
            assertEquals(q.indexOf((char) j), j);
    }

    @Test
    public void iteratorTest () {
        final CharQueue q = new CharQueue();

        // Test head < tail.
        for (int j = 0; j <= 6; j++)
            q.addLast((char) j);
        Iterator<Character> iter = q.iterator();
        for (int j = 0; j <= 6; j++)
            assertEquals(iter.next().charValue(), j);
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
            q.addFirst((char) j);
        for (int j = 3; j <= 6; j++)
            q.addLast((char) j);
        iter = q.iterator();
        for (int j = 0; j <= 6; j++)
            assertEquals(iter.next().charValue(), j);
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
        CharQueue queue = new CharQueue();

        //Simulate normal usage
        for(int i = 0; i < 100; i++) {
            queue.addLast((char) i);
            if(i > 50)
                queue.removeFirst();
        }

        Iterator<Character> it = queue.iterator();
        while(it.hasNext()) {
            it.next();
            it.remove();
        }

        queue.addLast((char) 127);

        char i = queue.first();
        assertEquals(127, (int)i);
    }

    @Test
    public void toStringTest () {
        CharQueue q = new CharQueue(1);
        assertEquals("[]", q.toString());
        q.addLast((char) 4);
        assertEquals(q.toString(), "[" + '\4' + "]");
        q.addLast((char) 5);
        q.addLast((char) 6);
        q.addLast((char) 7);
        assertEquals(q.toString(), "[" + '\4' + ", " + '\5' + ", " + '\6' + ", " + '\7' + "]");
    }

    @Test
    public void hashEqualsTest () {
        CharQueue q1 = new CharQueue();
        CharQueue q2 = new CharQueue();

        assertEqualsAndHash(q1, q2);
        q1.addFirst((char) 1);
        assertNotEquals(q1, q2);
        q2.addFirst((char) 1);
        assertEqualsAndHash(q1, q2);

        q1.clear();
        q1.addLast((char) 1);
        q1.addLast((char) 2);
        q2.addLast((char) 2);
        assertEqualsAndHash(q1, q2);

        for (int i = 0; i < 100; i++) {
            q1.addLast((char) i);
            q1.addLast((char) i);
            q1.removeFirst();

            assertNotEquals(q1, q2);

            q2.addLast((char) i);
            q2.addLast((char) i);
            q2.removeFirst();

            assertEqualsAndHash(q1, q2);
        }
    }

    private void assertEqualsAndHash (CharQueue q1, CharQueue q2) {
        assertEquals(q1, q2);
        assertEquals("Hash codes are not equal", q1.hashCode(), q2.hashCode());
    }

    private void assertValues (CharQueue q, int... values) {
        for (int i = 0, n = values.length; i < n; i++) {
            assertEquals((char) values[i], q.get(i));
        }
    }
}
