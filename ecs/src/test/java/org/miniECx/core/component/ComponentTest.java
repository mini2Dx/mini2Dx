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
package org.miniECx.core.component;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.ecs.component.Component;
import org.mini2Dx.ecs.entity.Entity;
import org.miniECx.core.test.TestComponent1;
import org.miniECx.core.test.TestComponent2;

/**
 * Unit tests for {@link Component}
 *
 * @author Thomas Cashman
 */
public class ComponentTest {
	private TestComponent1 component;
	private TestComponent2 component2;
	private Entity entity, childEntity;
	
	@Before
	public void setUp() {
		component = new TestComponent1();
		component2 = new TestComponent2("test");
		
		entity = new Entity();
		entity.addComponent(component);
		
		childEntity = new Entity();
		entity.addChild(childEntity);
	}

	@Test
	public void testGetComponent() {
		Assert.assertNull(component.getComponent(TestComponent2.class));
		entity.addComponent(component2);
		Assert.assertEquals(component2, component.getComponent(TestComponent2.class));
		Assert.assertNull(component.getComponentInDescendants(TestComponent2.class));
	}

	@Test
	public void testGetComponentWithName() {
		Assert.assertNull(component.getComponent(TestComponent2.class));
		entity.addComponent(component2);
		
		TestComponent2 component3 = new TestComponent2("test2");
		entity.addComponent(component3);
		
		Assert.assertEquals(component2, component.getComponent("test", TestComponent2.class));
		Assert.assertEquals(component3, component.getComponent("test2", TestComponent2.class));
		Assert.assertNull(component.getComponent("test3", TestComponent2.class));
	}

	@Test
	public void testGetComponents() {
		Assert.assertEquals(0, component.getComponents(TestComponent2.class).size());
		entity.addComponent(component2);
		Assert.assertEquals(1, component.getComponents(TestComponent2.class).size());
		Assert.assertEquals(component2, component.getComponents(TestComponent2.class).first());
	}

	@Test
	public void testGetComponentInDescendants() {
		Assert.assertNull(component.getComponentInDescendants(TestComponent2.class));
		childEntity.addComponent(component2);
		Assert.assertNull(component.getComponent(TestComponent2.class));
		Assert.assertEquals(component2, component.getComponentInDescendants(TestComponent2.class));
	}

	@Test
	public void testGetComponentInDescendantsWithName() {
		Assert.assertNull(component.getComponentInDescendants(TestComponent2.class));
		childEntity.addComponent(component2);
		
		TestComponent2 component3 = new TestComponent2("test2");
		childEntity.addComponent(component3);
		
		Assert.assertNull(component.getComponent("test", TestComponent2.class));
		Assert.assertNull(component.getComponent("test2", TestComponent2.class));
		
		Assert.assertEquals(component2, component.getComponentInDescendants("test", TestComponent2.class));
		Assert.assertEquals(component3, component.getComponentInDescendants("test2", TestComponent2.class));
		Assert.assertNull(component.getComponentInDescendants("test3", TestComponent2.class));
	}

	@Test
	public void testGetComponentsInDescendants() {
		Assert.assertEquals(0, component.getComponentsInDescendants(TestComponent2.class).size());
		childEntity.addComponent(component2);
		Assert.assertEquals(1, component.getComponentsInDescendants(TestComponent2.class).size());
		Assert.assertEquals(component2, component.getComponentsInDescendants(TestComponent2.class).first());
	}

	@Test
	public void testDestroy() {
		Assert.assertEquals(entity, component.getEntity());
		component.destroy();
		Assert.assertNull(component.getEntity());
	}

	@Test
	public void testGetName() {
		Assert.assertEquals(true, component.getName() != null);
		Assert.assertEquals(true, component.getName().length() > 0);
	}
}
