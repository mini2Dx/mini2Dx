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

/**
 * A pair of locks; one for read-only operations and one for write operations.
 * The read lock can be held by multiple threads as long as the write lock is held by zero threads.
 */
public interface ReadWriteLock {

	/**
	 * Acquires the read lock
	 */
	public void lockRead();

	/**
	 * Attempts to acquire the read lock
	 * @return False if the lock was not acquired
	 */
	public boolean tryLockRead();

	/**
	 * Releases the read lock
	 */
	public void unlockRead();

	/**
	 * Acquires the write lock
	 */
	public void lockWrite();

	/**
	 * Attempts to acquire the write lock
	 * @return False if the lock was not acquired
	 */
	public boolean tryLockWrite();

	/**
	 * Releases the write lock
	 */
	public void unlockWrite();
}
