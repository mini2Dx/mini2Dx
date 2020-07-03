package org.mini2Dx.core.collections.concurrent;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConcurrentShortArrayTest extends ConcurrentCollectionTest {
    @Override
    public void testGetLock() {
        assertNotNull(new ConcurrentShortArray().getLock());
    }

    @Test
    public void testAddItems() {
        ConcurrentShortArray a = new ConcurrentShortArray();
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
        ConcurrentShortArray a = new ConcurrentShortArray();
        for (int i = 0; i < 1000; i++) {
            a.add(i);
        }
        CountDownLatch latch = new CountDownLatch(1000);
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
        ConcurrentShortArray a = new ConcurrentShortArray();
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
                    a.incr(i, (short) 1);
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
                    a.incr(i, (short) -1);
                }
            }
        }, 10);
        joinAll(addThreads, subThreads);
        for (int i = 0; i < 1000; i++) {
            assertEquals(a.get(i), i);
        }
    }
}
