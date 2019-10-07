/*******************************************************************************
 * Copyright 2019 See AUTHORS file
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.mini2Dx.core.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Default implementation of {@link ReadWriteLock} for JVM environments
 */
public class JvmReadWriteLock implements ReadWriteLock {
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	@Override
	public void lockRead() {
		lock.readLock().lock();
	}

	@Override
	public boolean tryLockRead() {
		return lock.readLock().tryLock();
	}

	@Override
	public void unlockRead() {
		lock.readLock().unlock();
	}

	@Override
	public void lockWrite() {
		lock.writeLock().lock();
	}

	@Override
	public boolean tryLockWrite() {
		return lock.writeLock().tryLock();
	}

	@Override
	public void unlockWrite() {
		lock.writeLock().unlock();
	}
}
