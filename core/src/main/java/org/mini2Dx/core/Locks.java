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
package org.mini2Dx.core;

import org.mini2Dx.core.lock.ReadWriteLock;
import org.mini2Dx.core.lock.ReentrantLock;

/**
 * Interface to platform-specific implementations of thread lock mechanisms
 */
public interface Locks {

	/**
	 * Returns the platform's implementation of {@link ReentrantLock}
	 * @return A new {@link ReentrantLock} instance
	 */
	public ReentrantLock newReentrantLock();

	/**
	 * Returns the platform's implementation of {@link ReadWriteLock}
	 * @return A new {@link ReadWriteLock} instance
	 */
	public ReadWriteLock newReadWriteLock();
}
