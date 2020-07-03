package org.mini2Dx.core.collections.concurrent;

import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.JvmLocks;
import org.mini2Dx.core.Mdx;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.mini2Dx.core.collections.concurrent.ConcurrentCollectionTest.createAndStartThreads;
import static org.mini2Dx.core.collections.concurrent.ConcurrentCollectionTest.joinAll;

public class ConcurrentPoolsTest {
    @Before
    public void setUp() {
        Mdx.locks = new JvmLocks();
    }

    @Test
    public void testPool() {
        CountDownLatch latch = new CountDownLatch(150);
        Thread[] stringThreads = createAndStartThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                String o = ConcurrentPools.obtain(String.class);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    ConcurrentPools.free(o);
                }
            }
        }, 100);
        Thread[] objectThreads = createAndStartThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Object o = ConcurrentPools.obtain(Object.class);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    ConcurrentPools.free(o);
                }
            }
        }, 50);

        joinAll(stringThreads, objectThreads);

        assertEquals(100, ConcurrentPools.get(String.class).getFree());
        ConcurrentPools.get(String.class).clear();
        assertEquals(0, ConcurrentPools.get(String.class).getFree());

        assertEquals(50, ConcurrentPools.get(Object.class).getFree());
        ConcurrentPools.get(Object.class).clear();
        assertEquals(0, ConcurrentPools.get(Object.class).getFree());
    }
}
