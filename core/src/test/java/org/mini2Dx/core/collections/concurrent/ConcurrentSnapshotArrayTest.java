package org.mini2Dx.core.collections.concurrent;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConcurrentSnapshotArrayTest extends ConcurrentCollectionTest {
    @Override
    public void testGetLock() {
        assertNotNull(new ConcurrentSnapshotArray<>().getLock());
    }

    @Test
    public void testAddItems() {
        ConcurrentSnapshotArray<Integer> a = new ConcurrentSnapshotArray<>();
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
        assertEquals(a.size, 100*100);
    }

    @Test
    public void testPopItems(){
        ConcurrentSnapshotArray<Integer> a = new ConcurrentSnapshotArray<>();
        for (int i = 0; i < 1000; i++) {
            a.add(i);
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
                a.pop();
            }
        }, 1000);
        assertEquals(0, a.size);
    }

    @Test
    public void testSnapshotPopItems(){
        ConcurrentSnapshotArray<Integer> a = new ConcurrentSnapshotArray<>(1000);
        for (int i = 0; i < 1000; i++) {
            a.add(i);
        }
        Object[] backing = a.begin();
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
                a.removeIndex(0);
            }
        }, 1000);
        assertEquals(1000, backing.length);
        a.end();
        assertEquals(0, a.size);
    }
}
