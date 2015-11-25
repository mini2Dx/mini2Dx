/**
 * Copyright 2015 Thomas Cashman
 */
package com.artemis.system.test;

import java.util.HashSet;
import java.util.Set;

import com.artemis.MdxWorld;
import com.artemis.listener.WorldListener;

import junit.framework.Assert;

/**
 *
 */
public class DummyWorldListener implements WorldListener {
	private Set<Integer> entitiesCreated = new HashSet<Integer>();
	private Set<Integer> entitiesDeleted = new HashSet<Integer>();

	@Override
	public void afterEntityCreated(MdxWorld world, int entityId) {
		entitiesCreated.add(entityId);
	}

	@Override
	public void beforeEntityDeleted(MdxWorld world, int entityId) {
		entitiesDeleted.add(entityId);
	}

	public void assertEntityCreated(int entityId) {
		Assert.assertEquals(true, entitiesCreated.contains(entityId));
	}
	
	public void assertEntityNotCreated(int entityId) {
		Assert.assertEquals(false, entitiesCreated.contains(entityId));
	}
	
	public void assertEntityDeleted(int entityId) {
		Assert.assertEquals(true, entitiesDeleted.contains(entityId));
	}
	
	public void assertEntityNotDeleted(int entityId) {
		Assert.assertEquals(false, entitiesDeleted.contains(entityId));
	}
}
