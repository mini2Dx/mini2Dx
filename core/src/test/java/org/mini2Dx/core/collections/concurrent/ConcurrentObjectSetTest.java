package org.mini2Dx.core.collections.concurrent;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

public class ConcurrentObjectSetTest extends ConcurrentCollectionTest {
    @Override
    public void testGetLock() {
        assertNotNull(new ConcurrentObjectSet<>().getLock());
    }

    @Test
    public void testAdd() {
        ConcurrentObjectSet<Integer> set = new ConcurrentObjectSet<>();
        Random r = new Random();
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
                while (!set.add(r.nextInt())){

                }
            }
        }, 1000);
        assertEquals(1000, set.size);
    }

    @Test
    public void testRemove(){
        ConcurrentObjectSet<Integer> set = new ConcurrentObjectSet<>();
        for (int i = 0; i < 10000; i++) {
            set.add(i);
        }
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
                for (int i = 0; i < 10; i++) {
                    set.lock.lockWrite();
                    int key = set.first();
                    assertTrue(set.contains(key));
                    set.remove(key);
                    assertFalse(set.contains(key));
                    set.lock.unlockWrite();
                }
            }
        }, 1000);
        assertEquals(0, set.size);
    }
}
