package org.mini2Dx.core.collections.concurrent;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

public class ConcurrentBitsTest extends ConcurrentCollectionTest {
    @Override
    public void testGetLock() {
        assertNotNull(new ConcurrentBits().getLock());
    }

    @Test
    public void testFlipBit() {
        ConcurrentBits b = new ConcurrentBits(16);
        CountDownLatch latch = new CountDownLatch(20);
        Thread[] ascThreads = createAndStartThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < 16; i++) {
                    b.flip(i);
                }
            }
        }, 10);
        Thread[] descThreads = createAndStartThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 15; i >= 0; i--) {
                    b.flip(i);
                }
            }
        }, 10);
        joinAll(ascThreads);
        joinAll(descThreads);
        for (int i = 0; i < 16; i++) {
            assertFalse(b.get(i));
        }
    }

    @Test
    public void testOperands() {
        ConcurrentBits ones = new ConcurrentBits(16),
                zeros = new ConcurrentBits(16),
                result = new ConcurrentBits(16);
        for (int i = 0; i < 16; i++) {
            ones.set(i);
        }
        CountDownLatch latch = new CountDownLatch(200);
        Thread[] andZero = createAndStartThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < 100; i++) {
                    result.and(zeros);
                }
            }
        }, 50);
        Thread[] orOne = createAndStartThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < 100; i++) {
                    result.or(ones);
                }
            }
        }, 50);
        Thread[] xorZero = createAndStartThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < 100; i++) {
                    result.xor(zeros);
                }
            }
        }, 50);
        Thread[] xorOne = createAndStartThreads(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < 100; i++) {
                    result.xor(ones);
                }
            }
        }, 50);
        joinAll(andZero, orOne, xorOne, xorZero);
        boolean first = result.get(0);
        for (int i = 1; i < 16; i++) {
            assertEquals(first, result.get(i));
        }
    }
}
