package org.mini2Dx.core.collections.concurrent;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

public class ConcurrentCharQueueTest extends ConcurrentCollectionTest {
    @Override
    public void testGetLock() {
        assertNotNull(new ConcurrentCharQueue().getLock());
    }

    @Test
    public void testAdd() {
        ConcurrentCharQueue q = new ConcurrentCharQueue();
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
                q.addFirst('f');
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
                q.addLast('l');
            }
        }, 100);
        joinAll(firstThreads, lastThreads);
        assertEquals(200, q.size);
    }

    @Test
    public void testRemove() {
        ConcurrentCharQueue q = new ConcurrentCharQueue();
        for (int i = 0; i < 100; i++) {
            q.addFirst('f');
            q.addLast('l');
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
                assertEquals('f', q.removeFirst());
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
                assertEquals('l', q.removeLast());
            }
        }, 100);
        joinAll(firstThreads, lastThreads);
        assertEquals(0, q.size);
    }
}
