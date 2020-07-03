package org.mini2Dx.core.collections.concurrent;

import org.junit.Test;
import org.mini2Dx.core.collections.LruIntMap;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class ConcurrentLruIntMapTest extends ConcurrentCollectionTest {
    @Override
    public void testGetLock() {
        assertNotNull(new ConcurrentLruIntMap<>().getLock());
    }

    @Test
    public void testPutItems() {
        ConcurrentLruIntMap<Integer> m = new ConcurrentLruIntMap<>();
        Random r = new Random();
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
                    }
                    m.getLock().unlockWrite();
                }
            }
        }, 100);
        assertEquals(LruIntMap.DEFAULT_MAX_CAPACITY, m.size);
    }

    @Test
    public void testRemoveItems() {
        ConcurrentLruIntMap<Integer> m = new ConcurrentLruIntMap<>();
        for (int i = 0; i < LruIntMap.DEFAULT_MAX_CAPACITY; i++) {
            m.put(i, 2*i);
        }
        CountDownLatch latch = new CountDownLatch(128);
        Thread[] kThreads = createAndStartThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                m.getLock().lockWrite();
                int key = m.keys().next();
                assertTrue(m.containsKey(key));
                m.remove(key);
                assertFalse(m.containsKey(key));
                m.getLock().unlockWrite();
            }
        }, 100);
        Thread[] vThreads = createAndStartThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                m.getLock().lockWrite();
                int value = m.values().next();
                assertTrue(m.containsValue(value, false));
                m.remove(m.findKey(value, false, 0));
                assertFalse(m.containsValue(value, false));
                m.getLock().unlockWrite();
            }
        }, 28);
        joinAll(kThreads, vThreads);
        assertEquals(0, m.size);
    }
}
