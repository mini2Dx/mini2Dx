package org.mini2Dx.core.collections.concurrent;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConcurrentFloatQueueTest extends ConcurrentCollectionTest {
    @Override
    public void testGetLock() {
        assertNotNull(new ConcurrentFloatQueue().getLock());
    }

    @Test
    public void testAdd() {
        ConcurrentFloatQueue q = new ConcurrentFloatQueue();
        CountDownLatch latch = new CountDownLatch(200);
        Thread[] firstThreads = createAndStartThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                q.addFirst(0);
            }
        }, 100);
        Thread[] lastThreads = createAndStartThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                q.addLast(1);
            }
        }, 100);
        joinAll(firstThreads, lastThreads);
        assertEquals(200, q.size);
    }

    @Test
    public void testRemove() {
        ConcurrentFloatQueue q = new ConcurrentFloatQueue();
        for (int i = 0; i < 100; i++) {
            q.addFirst(0);
            q.addLast(1);
        }
        CountDownLatch latch = new CountDownLatch(200);
        Thread[] firstThreads = createAndStartThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                assertEquals(0, q.removeFirst(), 0.1f);
            }
        }, 100);
        Thread[] lastThreads = createAndStartThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                assertEquals(1, q.removeLast(), 0.1f);
            }
        }, 100);
        joinAll(firstThreads, lastThreads);
        assertEquals(0, q.size);
    }
}
