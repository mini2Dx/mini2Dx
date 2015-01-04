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

import java.util.Iterator;
import java.util.SortedSet;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.ecs.component.Component;
import org.mini2Dx.ecs.entity.Entity;
import org.miniECx.core.test.TestComponent1;
import org.miniECx.core.test.TestComponent2;
import org.miniECx.core.test.TestComponent3;

/**
 * Unit tests for {@link Entity}
 */
public class EntityTest {
	private Entity entity;
	private TestComponent1 testComponent1;
	private TestComponent2 testComponent2;
	private TestComponent3 testComponent3;
	
	@Before
	public void setup() {
		entity = new Entity();
		testComponent1 = new TestComponent1();
		testComponent2 = new TestComponent2("test");
		testComponent3 = new TestComponent3("test-test");
	}

	@Test
	public void testAddGetComponent() {
		Assert.assertNull(testComponent1.getEntity());
		Assert.assertNull(testComponent2.getEntity());
		
		entity.addComponent(testComponent1);
		Assert.assertEquals(entity, testComponent1.getEntity());
		
		entity.addComponent(testComponent2);
		Assert.assertEquals(entity, testComponent2.getEntity());
		
		SortedSet<TestComponent1> results1 = entity.getComponents(TestComponent1.class);
		Assert.assertEquals(1, results1.size());
		Assert.assertEquals(testComponent1, results1.first());
		
		SortedSet<TestComponent2> results2 = entity.getComponents(TestComponent2.class);
		Assert.assertEquals(1, results2.size());
		Assert.assertEquals(testComponent2, results2.first());
		
		SortedSet<Runnable> results3 = entity.getComponents(Runnable.class);
		Assert.assertEquals(1, results3.size());
		Assert.assertEquals(testComponent1, results3.first());
		
		entity.addComponent(testComponent3);
		results2 = entity.getComponents(TestComponent2.class);
		Assert.assertEquals(2, results2.size());
		Assert.assertEquals(testComponent2, results2.first());
	}
	
	@Test
	public void testRemoveComponentWithGetByName() {
		entity.addComponent(testComponent1);
		entity.addComponent(testComponent2);
		entity.addComponent(testComponent3);
		
		Assert.assertEquals(testComponent1, entity.getComponent(testComponent1.getComponentTypeId()));
		Assert.assertEquals(testComponent2, entity.getComponent(testComponent2.getComponentTypeId()));
		Assert.assertEquals(testComponent3, entity.getComponent(testComponent3.getComponentTypeId()));
		
		Assert.assertEquals(testComponent1, entity.getComponent(testComponent1.getName(), testComponent1.getComponentTypeId()));
		Assert.assertEquals(testComponent2, entity.getComponent(testComponent2.getName(), testComponent2.getComponentTypeId()));
		Assert.assertEquals(testComponent3, entity.getComponent(testComponent3.getName(), testComponent2.getComponentTypeId()));
	
		entity.removeComponent(testComponent2);
		Assert.assertEquals(testComponent3, entity.getComponent(testComponent3.getName(), testComponent2.getComponentTypeId()));
		Assert.assertEquals(testComponent3, entity.getComponent(testComponent3.getName(), testComponent3.getComponentTypeId()));
	
		Assert.assertNull(entity.getComponent(testComponent2.getName(), testComponent2.getComponentTypeId()));
	
		entity.addComponent(testComponent2);
		entity.addComponent(testComponent3);
		
		entity.removeComponent(testComponent3);
		Assert.assertEquals(testComponent2, entity.getComponent(testComponent2.getName(), testComponent2.getComponentTypeId()));
	}
	
	@Test
	public void testRemoveAllComponentsOfType() {
		Assert.assertNull(testComponent1.getEntity());
		Assert.assertNull(testComponent2.getEntity());
		
		entity.addComponent(testComponent1);
		entity.addComponent(testComponent2);
		entity.addComponent(new TestComponent2("test2"));
		
		SortedSet<TestComponent2> componentsRemoved = entity.removeAllComponentsOfType(TestComponent2.class);
		Assert.assertEquals(2, componentsRemoved.size());
		
		Iterator<TestComponent2> iterator = componentsRemoved.iterator();
		while(iterator.hasNext()) {
			TestComponent2 component = iterator.next();
			Assert.assertNull(component.getEntity());
		}
	}
	
	@Test
	public void testRemoveAllComponentsOfTypeWithSubclass() {
		Assert.assertNull(testComponent1.getEntity());
		Assert.assertNull(testComponent2.getEntity());
		
		entity.addComponent(testComponent1);
		entity.addComponent(testComponent2);
		entity.addComponent(new TestComponent2("test2"));
		
		SortedSet<Component> componentsRemoved = entity.removeAllComponentsOfType(Component.class);
		Assert.assertEquals(3, componentsRemoved.size());
		
		Assert.assertEquals(0, entity.getComponents(TestComponent1.class).size());
		Assert.assertEquals(0, entity.getComponents(TestComponent2.class).size());
	}
}
