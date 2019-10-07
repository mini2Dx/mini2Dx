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

import org.mini2Dx.core.lock.JvmReadWriteLock;
import org.mini2Dx.core.lock.JvmReentrantLock;
import org.mini2Dx.core.lock.ReadWriteLock;
import org.mini2Dx.core.lock.ReentrantLock;

/**
 * Default implementation of {@link Locks} for the JVM
 */
public class JvmLocks implements Locks {

	@Override
	public ReentrantLock newReentrantLock() {
		return new JvmReentrantLock();
	}

	@Override
	public ReadWriteLock newReadWriteLock() {
		return new JvmReadWriteLock();
	}
}
