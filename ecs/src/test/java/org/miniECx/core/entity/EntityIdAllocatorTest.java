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
package org.miniECx.core.entity;

import junit.framework.Assert;

import org.junit.Test;
import org.mini2Dx.ecs.entity.EntityIdAllocator;

/**
 * Unit tests for {@link EntityIdAllocator}
 */
public class EntityIdAllocatorTest {

	@Test(expected=Exception.class)
	public void testAllocateInt() throws Exception {
		try {
			EntityIdAllocator.allocate(1000);
		} catch (Exception e) {
			Assert.fail("Could not allocate ID 1000");
		}
		EntityIdAllocator.allocate(1000);
		Assert.fail("Should not be able to allocate an ID twice");
	}

	@Test
	public void testAllocate() {
		int startId = EntityIdAllocator.allocate();
		for(int i = startId + 1; i < startId + 100; i++) {
			Assert.assertEquals(i, EntityIdAllocator.allocate());
		}
	}

	@Test
	public void testDeallocate() {
		int id = 10000;
		try {
			EntityIdAllocator.allocate(id);
		} catch (Exception e) {
			Assert.fail("Could not allocate ID " + id);
		}
		EntityIdAllocator.deallocate(id);
		try {
			EntityIdAllocator.allocate(id);
		} catch (Exception e) {
			Assert.fail("Could not allocate ID " + id);
		}
	}

}
