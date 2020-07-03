package org.mini2Dx.core.collections.concurrent;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConcurrentReflectionPoolTest extends ConcurrentCollectionTest {
    @Override
    public void testGetLock() {
        assertNotNull(new ConcurrentReflectionPool<>(Object.class).getLock());
    }

    @Test
    public void testPool() {
        ConcurrentReflectionPool<Object> pool = new ConcurrentReflectionPool<>(Object.class);
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
                Object o = pool.obtain();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    pool.free(o);
                }
            }
        }, 100);
        assertEquals(100, pool.getFree());
        pool.clear();
        assertEquals(0, pool.getFree());
    }
}

