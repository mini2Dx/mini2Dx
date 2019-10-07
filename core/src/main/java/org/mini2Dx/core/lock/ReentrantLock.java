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
 * Interface for lock implementations that allow for re-entry.
 * See {@link java.util.concurrent.locks.ReentrantLock} for more information on how re-entrant locks work.
 */
public interface ReentrantLock {
	/**
	 * Checks if the current thread owns this lock
	 * @return True if the current thread owns the lock
	 */
	public boolean isHeldByCurrentThread();

	/**
	 * Acquires the lock
	 */
	public void lock();

	/**
	 * Attempts to acquire the lock
	 * @return False if the lock could not be acquired
	 */
	public boolean tryLock();

	/**
	 * Releases the lock
	 */
	public void unlock();
}
