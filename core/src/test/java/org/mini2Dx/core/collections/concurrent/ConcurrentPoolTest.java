package org.mini2Dx.core.collections.concurrent;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConcurrentPoolTest extends ConcurrentCollectionTest {
    @Override
    public void testGetLock() {
        assertNotNull(new ConcurrentPoolImpl().getLock());
    }

    @Test
    public void testPool() {
        ConcurrentPoolImpl pool = new ConcurrentPoolImpl();
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
                int i = pool.obtain();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    pool.free(i);
                }
            }
        }, 100);
        assertEquals(100, pool.getFree());
        pool.clear();
        assertEquals(0, pool.getFree());
        assertEquals(pool.i.get(), 100);
    }
}

class ConcurrentPoolImpl extends ConcurrentPool<Integer>{
    final AtomicInteger i = new AtomicInteger(0);
    @Override
    protected Integer newObject() {
        return i.getAndIncrement();
    }
}