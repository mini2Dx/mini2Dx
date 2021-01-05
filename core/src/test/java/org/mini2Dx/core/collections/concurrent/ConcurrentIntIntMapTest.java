package org.mini2Dx.core.collections.concurrent;

import org.junit.Ignore;
import org.junit.Test;
import org.mini2Dx.gdx.utils.IntFloatMap;
import org.mini2Dx.gdx.utils.IntIntMap;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class ConcurrentIntIntMapTest extends ConcurrentCollectionTest {
    @Override
    public void testGetLock() {
        assertNotNull(new ConcurrentIntIntMap().getLock());
    }

    @Test(timeout = 10000L)
    public void testPutItems() {
        ConcurrentIntIntMap m = new ConcurrentIntIntMap();
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

    //Re-add after this issue is resolved -> https://github.com/libgdx/libgdx/issues/6347
    @Ignore
    @Test(timeout = 10000L)
    public void testRemoveItems() {
        ConcurrentIntIntMap m = new ConcurrentIntIntMap();
        for (int i = 0; i < 10000; i++) {
            m.put(i, 2*i);
        }
        CountDownLatch latch = new CountDownLatch(1000);
        Thread[] kThreads = createAndStartThreads(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.countDown();
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    for (int i = 0; i < 10; i++) {
                        m.getLock().lockWrite();
                        final IntIntMap.Keys iterator = m.keys();
                        if(iterator.hasNext) {
                            int key = iterator.next();
                            assertTrue(m.containsKey(key));
                            m.remove(key, 0);
                            assertFalse(m.containsKey(key));
                        }
                        m.getLock().unlockWrite();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 900);
        Thread[] vThreads = createAndStartThreads(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        latch.countDown();
                        try {
                            latch.await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        m.getLock().lockWrite();
                        final IntIntMap.Values iterator = m.values();
                        if(iterator.hasNext()) {
                            int value = iterator.next();
                            assertTrue(m.containsValue(value));
                            m.remove(m.findKey(value, 0), 0);
                            assertFalse(m.containsValue(value));
                        }
                        m.getLock().unlockWrite();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 100);
        joinAll(kThreads, vThreads);
        assertEquals(0, m.size);
    }
}
