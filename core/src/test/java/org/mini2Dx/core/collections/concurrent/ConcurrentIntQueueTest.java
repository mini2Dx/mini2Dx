package org.mini2Dx.core.collections.concurrent;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConcurrentIntQueueTest extends ConcurrentCollectionTest {
    @Override
    public void testGetLock() {
        assertNotNull(new ConcurrentIntQueue().getLock());
    }

    @Test(timeout = 10000L)
    public void testAdd() {
        ConcurrentIntQueue q = new ConcurrentIntQueue();
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

    @Test(timeout = 10000L)
    public void testRemove() {
        ConcurrentIntQueue q = new ConcurrentIntQueue();
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
                assertEquals(0, q.removeFirst());
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
                assertEquals(1, q.removeLast());
            }
        }, 100);
        joinAll(firstThreads, lastThreads);
        assertEquals(0, q.size);
    }
}
