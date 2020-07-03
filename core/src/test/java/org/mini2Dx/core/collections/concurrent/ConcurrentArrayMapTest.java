package org.mini2Dx.core.collections.concurrent;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class ConcurrentArrayMapTest extends ConcurrentCollectionTest {
    @Override
    public void testGetLock() {
        assertNotNull(new ConcurrentArrayMap<>().getLock());
    }

    @Test
    public void testPutItems() {
        ConcurrentArrayMap<Integer, String> m = new ConcurrentArrayMap<>();
        Random r = new Random();
        AtomicInteger count = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(100);
        Thread[] threads = createAndStartThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < 100; i++) {
                    Integer key = r.nextInt();
                    String value = Integer.toString(r.nextInt());
                    m.getLock().lockWrite();
                    if (!m.containsKey(key)){
                        m.put(key, value);
                        count.incrementAndGet();
                    }
                    m.getLock().unlockWrite();
                }
            }
        }, 100);
        joinAll(threads);
        assertEquals(count.get(), m.size);
        assertFalse(m.isEmpty());
        assertTrue(m.notEmpty());
    }

    @Test
    public void testRemoveItems() {
        ConcurrentArrayMap<Integer, String> m = new ConcurrentArrayMap<>();
        Random r = new Random();
        for (int i = 0; i < 1000; i++) {
            m.put(i, Integer.toString(r.nextInt()));
        }
        CountDownLatch latch = new CountDownLatch(1000);
        Thread[] threads = createAndStartThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                assertFalse(m.isEmpty());
                assertTrue(m.notEmpty());
                m.getLock().lockWrite();
                int key = m.firstKey();
                assertTrue(m.containsKey(key));
                m.removeKey(key);
                assertFalse(m.containsKey(key));
                m.getLock().unlockWrite();
            }
        }, 1000);
        joinAll(threads);
        assertFalse(m.notEmpty());
        assertTrue(m.isEmpty());
        assertEquals(0, m.size);
    }
}
