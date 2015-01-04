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
package org.miniECx.core.component;

import java.util.Iterator;
import java.util.SortedSet;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.ecs.component.PriorityComponent;
import org.mini2Dx.ecs.entity.Entity;

/**
 * Unit tests for {@link PriorityComponent}
 */
public class PriorityComponentTest {
	private PriorityComponent [] components;
	private Entity entity;
	
	@Before
	public void setUp() {
		entity = new Entity();
		
		components = new PriorityComponent[100];
		for(int i = 0; i < components.length; i++) {
			components[i] = new PriorityComponent(i - 50);
			entity.addComponent(components[i]);
		}
	}

	@Test
	public void testCompareTo() {
		SortedSet<PriorityComponent> result = entity.getComponents(PriorityComponent.class);
		Assert.assertEquals(components.length, result.size());
		
		int i = 0;
		Iterator<PriorityComponent> iterator = result.iterator();
		while(iterator.hasNext()) {
			PriorityComponent component = iterator.next();
			Assert.assertEquals(components[components.length - 1 - i].getPriority(), component.getPriority());
			Assert.assertEquals(components[components.length - 1 - i], component);
			i++;
		}
	}

}
