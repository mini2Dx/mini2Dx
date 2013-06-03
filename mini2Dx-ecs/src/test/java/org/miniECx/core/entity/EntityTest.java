/**
 * Copyright (c) 2013, mini2Dx Project
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

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.miniECx.core.test.TestComponent1;
import org.miniECx.core.test.TestComponent2;

/**
 * Unit tests for {@link Entity}
 * @author Thomas Cashman
 */
public class EntityTest {
	private Entity entity;
	private TestComponent1 testComponent1;
	private TestComponent2 testComponent2;
	
	@Before
	public void setup() {
		entity = new Entity();
		testComponent1 = new TestComponent1();
		testComponent2 = new TestComponent2();
	}

	@Test
	public void testAddGetComponent() {
		entity.addComponent(testComponent1);
		entity.addComponent(testComponent2);
		
		List<TestComponent1> results1 = entity.getComponents(TestComponent1.class);
		Assert.assertEquals(1, results1.size());
		Assert.assertEquals(testComponent1, results1.get(0));
		
		List<TestComponent2> results2 = entity.getComponents(TestComponent2.class);
		Assert.assertEquals(1, results2.size());
		Assert.assertEquals(testComponent2, results2.get(0));
		
		List<Runnable> results3 = entity.getComponents(Runnable.class);
		Assert.assertEquals(1, results3.size());
		Assert.assertEquals(testComponent1, results3.get(0));
	}
}
