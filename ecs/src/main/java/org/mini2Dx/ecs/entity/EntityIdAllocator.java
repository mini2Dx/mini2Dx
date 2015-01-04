/**
 * Copyright (c) 2015, mini2Dx Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.ecs.entity;

import java.util.BitSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Manages allocation of {@link Entity} identifiers
 */
public class EntityIdAllocator {
	private static BitSet ALLOCATIONS = new BitSet();
	private static Lock LOCK = new ReentrantLock();

	public static void allocate(int id) throws Exception {
		boolean success = true;
		LOCK.lock();
		if(ALLOCATIONS.get(id)) {
			success = false;
		} else {
			ALLOCATIONS.set(id, true);
		}
		LOCK.unlock();
		if(!success) {
			throw new Exception("Entity ID '" + id + "' has already been allocated.");
		}
	}

	public static int allocate() {
		int result = -1;
		LOCK.lock();
		for(int i = 0; i < ALLOCATIONS.length(); i++) {
			if(!ALLOCATIONS.get(i)) {
				ALLOCATIONS.set(i, true);
				result = i;
				break;
			}
		}
		if(result < 0) {
			result = ALLOCATIONS.length();
			ALLOCATIONS.set(result, true);
		}
		LOCK.unlock();
		return result;
	}

	public static void deallocate(int id) {
		LOCK.lock();
		ALLOCATIONS.set(id, false);
		LOCK.unlock();
	}
}
