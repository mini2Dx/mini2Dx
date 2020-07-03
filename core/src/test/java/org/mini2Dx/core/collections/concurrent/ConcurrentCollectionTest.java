package org.mini2Dx.core.collections.concurrent;

import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.JvmLocks;
import org.mini2Dx.core.Mdx;

public abstract class ConcurrentCollectionTest {
    @Before
    public void setUp() {
        Mdx.locks = new JvmLocks();
    }

    public static Thread[] createAndStartThreads(Runnable runnable, int count){
        Thread[] threads = new Thread[count];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(runnable);
            threads[i].start();
        }
        return threads;
    }

    public static void joinAll(Thread[]... threadss){
        for (Thread[] threads : threadss) {
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void createStartAndJoinThreads(Runnable runnable, int count){
        joinAll(createAndStartThreads(runnable, count));
    }

    @Test
    public abstract void testGetLock();
}
