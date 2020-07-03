package org.mini2Dx.core.collections.concurrent;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

public class ConcurrentArrayTest extends ConcurrentCollectionTest {
    @Override
    public void testGetLock() {
        assertNotNull(new ConcurrentArray<>().getLock());
    }

    @Test
    public void testAddItems() {
        ConcurrentArray<Integer> a = new ConcurrentArray<>();
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
        ConcurrentArray<Integer> a = new ConcurrentArray<>();
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
}
