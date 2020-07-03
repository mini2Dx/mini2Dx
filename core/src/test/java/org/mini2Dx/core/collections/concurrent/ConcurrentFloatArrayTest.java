package org.mini2Dx.core.collections.concurrent;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConcurrentFloatArrayTest extends ConcurrentCollectionTest {
    @Override
    public void testGetLock() {
        assertNotNull(new ConcurrentFloatArray().getLock());
    }

    @Test
    public void testAddItems() {
        ConcurrentFloatArray a = new ConcurrentFloatArray();
        CountDownLatch latch = new CountDownLatch(100);
        createStartAndJoinThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < 100; i++) {
                    a.add(i);
                }
            }
        }, 100);
        assertEquals(a.size, 10*1000);
    }

    @Test
    public void testPopItems(){
        ConcurrentFloatArray a = new ConcurrentFloatArray();
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
        ConcurrentFloatArray a = new ConcurrentFloatArray();
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
            assertEquals(a.get(i), i, 0.1f);
        }
    }
}
