package org.mini2Dx.core.collections.concurrent;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConcurrentQueueTest extends ConcurrentCollectionTest {
    @Override
    public void testGetLock() {
        assertNotNull(new ConcurrentQueue<>().getLock());
    }

    @Test
    public void testAdd() {
        ConcurrentQueue<Integer> q = new ConcurrentQueue<>();
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
        ConcurrentQueue<Integer> q = new ConcurrentQueue<>();
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
                assertEquals(0, (int) q.removeFirst());
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
                assertEquals(1, (int) q.removeLast());
            }
        }, 100);
        joinAll(firstThreads, lastThreads);
        assertEquals(0, q.size);
    }
}
