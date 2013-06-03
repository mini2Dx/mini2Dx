/*******************************************************
 * Copyright (c) 2013 Thomas Cashman
 * All rights reserved.
 *******************************************************/
package org.mini2Dx.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Thomas Cashman
 */
public class LockOnWriteArrayList<T> implements List<T> {
	private List<T> list;
	private ReentrantLock lock;
	
	public LockOnWriteArrayList() {
		list = new ArrayList<T>();
		lock = new ReentrantLock();
	}
	
	private void waitForUnlock() {
		while(lock.isLocked()) {
			try {
				Thread.sleep(1);
			} catch (Exception e) {}
		}
	}

	@Override
	public boolean add(T arg0) {
		lock.lock();
		boolean result = list.add(arg0);
		lock.unlock();
		return result;
	}

	@Override
	public void add(int arg0, T arg1) {
		lock.lock();
		list.add(arg0, arg1);
		lock.unlock();
	}

	@Override
	public boolean addAll(Collection<? extends T> arg0) {
		lock.lock();
		boolean result = list.addAll(arg0);
		lock.unlock();
		return result;
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends T> arg1) {
		lock.lock();
		boolean result = list.addAll(arg0, arg1);
		lock.unlock();
		return result;
	}

	@Override
	public void clear() {
		lock.lock();
		list.clear();
		lock.unlock();
	}

	@Override
	public boolean contains(Object arg0) {
		waitForUnlock();
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		waitForUnlock();
		return false;
	}

	@Override
	public T get(int arg0) {
		waitForUnlock();
		return null;
	}

	@Override
	public int indexOf(Object arg0) {
		waitForUnlock();
		return 0;
	}

	@Override
	public boolean isEmpty() {
		waitForUnlock();
		return false;
	}

	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int lastIndexOf(Object arg0) {
		waitForUnlock();
		return 0;
	}

	@Override
	public ListIterator<T> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<T> listIterator(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public T remove(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public T set(int arg0, T arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		waitForUnlock();
		return 0;
	}

	@Override
	public List<T> subList(int arg0, int arg1) {
		waitForUnlock();
		return null;
	}

	@Override
	public Object[] toArray() {
		waitForUnlock();
		return null;
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		waitForUnlock();
		return null;
	}

}
