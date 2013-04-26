package org.mini2Dx.context;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * 
 * @author Thomas Cashman
 */
public class PrototypePool<T> implements Runnable, BeanRetriever<T> {
	private ExecutorService executorService;
	private BlockingQueue<T> instanceQueue;
	private Class<T> clazz;

	public PrototypePool(int poolSize, Class<T> clazz, ExecutorService executorService)
			throws InstantiationException, IllegalAccessException {
		this.executorService = executorService;
		this.clazz = clazz;

		instanceQueue = new LinkedBlockingQueue<T>(poolSize);
		instanceQueue.offer(clazz.newInstance());
	}

	public T getBean() {
		try {
			return instanceQueue.take();
		} catch (InterruptedException e) {
			return getBean();
		}
	}

	@Override
	public void run() {
		try {
			instanceQueue.offer(clazz.newInstance(), 10, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			executorService.submit(this);
		}
	}
}
