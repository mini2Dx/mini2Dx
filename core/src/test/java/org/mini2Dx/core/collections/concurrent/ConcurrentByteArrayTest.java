package org.mini2Dx.core.collections.concurrent;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConcurrentByteArrayTest extends ConcurrentCollectionTest {
    @Override
    public void testGetLock() {
        assertNotNull(new ConcurrentByteArray().getLock());
    }

    @Test
    public void testAddItems() {
        ConcurrentByteArray a = new ConcurrentByteArray();
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
                    a.add((byte)(i % 256 - 128));
                }
            }
        }, 100);
        assertEquals(a.size, 10*1000);
    }

    @Test
    public void testPopItems(){
        ConcurrentByteArray a = new ConcurrentByteArray();
        for (int i = 0; i < 1000; i++) {
            a.add((byte)(i % 256 - 128));
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
        ConcurrentByteArray a = new ConcurrentByteArray();
        for (int i = 0; i < 1000; i++) {
            a.add((byte)(i % 256 - 128));
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
                    a.incr(i, (byte) 1);
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
                    a.incr(i, (byte) -1);
                }
            }
        }, 10);
        joinAll(addThreads, subThreads);
        for (int i = 0; i < 1000; i++) {
            assertEquals(a.get(i), (byte)(i % 256 - 128));
        }
    }
}
