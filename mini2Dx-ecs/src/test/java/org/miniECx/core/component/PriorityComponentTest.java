/**
 * Copyright 2014 Thomas Cashman
 */
package org.miniECx.core.component;

import java.util.Iterator;
import java.util.SortedSet;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.ecs.component.PriorityComponent;
import org.mini2Dx.ecs.entity.UUIDEntity;

/**
 *
 *
 * @author Thomas Cashman
 */
public class PriorityComponentTest {
	private PriorityComponent [] components;
	private UUIDEntity entity;
	
	@Before
	public void setUp() {
		entity = new UUIDEntity();
		
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
