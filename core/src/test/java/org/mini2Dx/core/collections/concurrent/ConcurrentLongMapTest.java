package org.mini2Dx.core.collections.concurrent;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class ConcurrentLongMapTest extends ConcurrentCollectionTest {
    @Override
    public void testGetLock() {
        assertNotNull(new ConcurrentLongMap<>().getLock());
    }

    @Test
    public void testPutItems() {
        ConcurrentLongMap<Integer> m = new ConcurrentLongMap<>();
        Random r = new Random();
        AtomicInteger count = new AtomicInteger(0);
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
                for (int i = 0; i < 1000; i++) {
                    int key = r.nextInt();
                    int value = r.nextInt();
                    m.getLock().lockWrite();
                    if (!m.containsKey(key)){
                        m.put(key, value);
                        count.incrementAndGet();
                    }
                    m.getLock().unlockWrite();
                }
            }
        }, 100);
        assertEquals(count.get(), m.size);
    }

    @Test
    public void testRemoveItems() {
        ConcurrentLongMap<Integer> m = new ConcurrentLongMap<>();
        for (int i = 0; i < 10000; i++) {
            m.put(i, 2*i);
        }
        CountDownLatch latch = new CountDownLatch(1000);
        Thread[] kThreads = createAndStartThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < 10; i++) {
                    m.getLock().lockWrite();
                    long key = m.keys().next();
                    assertTrue(m.containsKey(key));
                    m.remove(key);
                    assertFalse(m.containsKey(key));
                    m.getLock().unlockWrite();
                }
            }
        }, 900);
        Thread[] vThreads = createAndStartThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < 10; i++) {
                    m.getLock().lockWrite();
                    int value = m.values().next();
                    assertTrue(m.containsValue(value, false));
                    m.remove(m.findKey(value, false, 0));
                    assertFalse(m.containsValue(value, false));
                    m.getLock().unlockWrite();
                }
            }
        }, 100);
        joinAll(kThreads, vThreads);
        assertEquals(0, m.size);
    }
}
