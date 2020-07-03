package org.mini2Dx.core.collections.concurrent;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class ConcurrentSortedIntListTest extends ConcurrentCollectionTest{
    @Override
    public void testGetLock() {
        assertNotNull(new ConcurrentSortedIntList<>().getLock());
    }

    @Test
    public void testAdd() {
        ConcurrentSortedIntList<Integer> l = new ConcurrentSortedIntList<>();
        AtomicInteger counter = new AtomicInteger(0);
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
                for (int i = 0; i < 10; i++) {
                    l.insert(counter.getAndIncrement(), i);
                }
            }
        }, 100);
        assertEquals(1000, l.size());
    }
}
