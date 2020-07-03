package org.mini2Dx.core.collections.concurrent;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConcurrentLongArrayTest extends ConcurrentCollectionTest {
    @Override
    public void testGetLock() {
        assertNotNull(new ConcurrentLongArray().getLock());
    }

    @Test
    public void testAddItems() {
        ConcurrentLongArray a = new ConcurrentLongArray();
        CountDownLatch latch = new CountDownLatch(10);
        createStartAndJoinThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < 1000; i++) {
                    a.add(i);
                }
            }
        }, 10);
        assertEquals(a.size, 10*1000);
    }

    @Test
    public void testPopItems(){
        ConcurrentLongArray a = new ConcurrentLongArray();
        CountDownLatch latch = new CountDownLatch(1000);
        for (int i = 0; i < 1000; i++) {
            a.add(i);
        }
        createStartAndJoinThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                a.pop();
            }
        }, 1000);
        assertEquals(0, a.size);
    }

    @Test
    public void testAddSubtract(){
        ConcurrentLongArray a = new ConcurrentLongArray();
        for (int i = 0; i < 1000; i++) {
            a.add((char) i);
        }
        CountDownLatch latch = new CountDownLatch(20);
        Thread[] addThreads = createAndStartThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < 1000; i++) {
                    a.incr(i, 1);
                }
            }
        }, 10);
        Thread[] subThreads = createAndStartThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < 1000; i++) {
                    a.incr(i, -1);
                }
            }
        }, 10);
        joinAll(addThreads, subThreads);
        for (int i = 0; i < 1000; i++) {
            assertEquals(a.get(i), i);
        }
    }
}
