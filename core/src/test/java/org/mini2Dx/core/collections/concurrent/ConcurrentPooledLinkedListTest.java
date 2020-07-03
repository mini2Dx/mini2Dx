package org.mini2Dx.core.collections.concurrent;

import org.junit.Test;
import org.mini2Dx.gdx.utils.PooledLinkedList;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConcurrentPooledLinkedListTest extends ConcurrentCollectionTest {
    @Override
    public void testGetLock() {
        assertNotNull(new ConcurrentPooledLinkedList<>(1).getLock());
    }

    @Test
    public void testAdd() {
        ConcurrentPooledLinkedList<Integer> l = new ConcurrentPooledLinkedList<>(2000);
        l.iter();
        CountDownLatch latch = new CountDownLatch(200);
        Thread[] addThread = createAndStartThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < 10; i++) {
                    l.add(i);
                }
            }
        }, 100);
        Thread[] addFirstThread = createAndStartThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < 10; i++) {
                    l.addFirst(i);
                }
            }
        }, 100);
        joinAll(addThread, addFirstThread);
        assertEquals(2000, l.size());
    }

    @Test
    public void testRemove() {
        ConcurrentPooledLinkedList<Integer> l = new ConcurrentPooledLinkedList<>(10000);
        for (int i = 0; i < 10000; i++) {
            l.addFirst(i);
        }
        l.iter();
        CountDownLatch latch = new CountDownLatch(1000);
        Thread[] removeThread = createAndStartThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < 10; i++) {
                    l.lock.lockWrite();
                    l.next();
                    l.remove();
                    l.lock.unlockWrite();
                }
            }
        }, 500);
        Thread[] removeLastThread = createAndStartThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < 10; i++) {
                    l.removeLast();
                }
            }
        }, 500);
        joinAll(removeThread, removeLastThread);
        assertEquals(0, l.size());
    }
}
