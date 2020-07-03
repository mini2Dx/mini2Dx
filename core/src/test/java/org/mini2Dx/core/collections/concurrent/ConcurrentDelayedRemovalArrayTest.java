package org.mini2Dx.core.collections.concurrent;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConcurrentDelayedRemovalArrayTest extends ConcurrentCollectionTest {
    @Override
    public void testGetLock() {
        assertNotNull(new ConcurrentDelayedRemovalArray<>().getLock());
    }

    @Test
    public void testAddItems() {
        ConcurrentDelayedRemovalArray<Integer> a = new ConcurrentDelayedRemovalArray<>();
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
        ConcurrentDelayedRemovalArray<Integer> a = new ConcurrentDelayedRemovalArray<>();
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
    public void testDelayedRemoval(){
        ConcurrentDelayedRemovalArray<Integer> a = new ConcurrentDelayedRemovalArray<>();
        for (int i = 0; i < 1000; i++) {
            a.add(i);
        }
        a.begin();
        AtomicInteger idx = new AtomicInteger(0);
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
                a.removeIndex(idx.getAndIncrement());
            }
        }, 1000);
        assertEquals(1000, a.size);
        a.end();
        assertEquals(0, a.size);
    }
}
