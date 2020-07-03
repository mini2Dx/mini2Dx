package org.mini2Dx.core.collections.concurrent;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConcurrentBooleanArrayTest extends ConcurrentCollectionTest {
    @Override
    public void testGetLock() {
        assertNotNull(new ConcurrentBooleanArray().getLock());
    }

    @Test
    public void testAddItems() {
        ConcurrentBooleanArray a = new ConcurrentBooleanArray();
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
                    a.add(i % 2 == 0);
                }
            }
        }, 10);
        assertEquals(a.size, 10*1000);
    }

    @Test
    public void testPopItems(){
        ConcurrentBooleanArray a = new ConcurrentBooleanArray();
        for (int i = 0; i < 1000; i++) {
            a.add(i % 2 == 0);
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
}
