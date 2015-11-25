/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.artemis.system;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.MdxWorld;
import com.artemis.WorldConfiguration;
import com.artemis.listener.WorldListener;
import com.artemis.system.test.DummyComponent;
import com.artemis.system.test.DummyWorldListener;
import com.badlogic.gdx.math.MathUtils;

import junit.framework.Assert;

/**
 * Unit tests for {@link InterpolatingEntitySystem}
 */
public class InterpolatingEntitySystemTest extends InterpolatingEntitySystem {
	private MdxWorld world;
	private InterpolatingEntitySystem system;
	
	private static Set<Integer> updatedIds, interpolatedIds;
	private static float expectedDelta, expectedAlpha;
	
	public InterpolatingEntitySystemTest() {
		super(Aspect.all(DummyComponent.class));
	}
	
	@Before
	public void setUp() {
		expectedDelta = 0f;
		expectedAlpha = 0f;
		updatedIds = new HashSet<Integer>();
		interpolatedIds = new HashSet<Integer>();
		
		system = new InterpolatingEntitySystemTest();
		
		WorldConfiguration configuration = new WorldConfiguration();
		configuration.setSystem(system);
		world = new MdxWorld(configuration);
	}
	
	@Test
	public void testUpdateMatchesEntities() {
		Entity entityWithComponent = world.createEntity();
		entityWithComponent.edit().add(new DummyComponent());
		
		Entity entityWithoutComponent = world.createEntity();
		
		world.process();
		
		Assert.assertEquals(true, updatedIds.contains(entityWithComponent.getId()));
		Assert.assertEquals(false, updatedIds.contains(entityWithoutComponent.getId()));
	}
	
	@Test
	public void testUpdatePassesCorrectDelta() {
		Entity entityWithComponent = world.createEntity();
		entityWithComponent.edit().add(new DummyComponent());
		
		expectedDelta = MathUtils.random();
		world.setDelta(expectedDelta);
		world.process();
	}
	
	@Test
	public void testInterpolateMatchesEntities() {
		Entity entityWithComponent = world.createEntity();
		entityWithComponent.edit().add(new DummyComponent());
		
		Entity entityWithoutComponent = world.createEntity();
		
		world.process();
		world.interpolate();
		
		Assert.assertEquals(true, interpolatedIds.contains(entityWithComponent.getId()));
		Assert.assertEquals(false, interpolatedIds.contains(entityWithoutComponent.getId()));
	}
	
	@Test
	public void testInterpolatePassesCorrectAlpha() {
		Entity entityWithComponent = world.createEntity();
		entityWithComponent.edit().add(new DummyComponent());
		
		expectedAlpha = MathUtils.random();
		world.setAlpha(expectedAlpha);
		world.process();
		world.interpolate();
	}
	
	@Test
	public void testInterpolateDoesNothingIfCalledBeforeFirstProcess() {
		Entity entityWithComponent = world.createEntity();
		entityWithComponent.edit().add(new DummyComponent());
		world.interpolate();
	}
	
	@Test
	public void testWorldListeners() {
		DummyWorldListener listener = new DummyWorldListener();
		world.addWorldListener(listener);
		
		Entity entity = world.createEntity();
		listener.assertEntityCreated(entity.getId());
		listener.assertEntityNotDeleted(entity.getId());
		
		world.delete(entity.getId());
		listener.assertEntityDeleted(entity.getId());
	}

	@Override
	protected void update(int entityId, float delta) {
		updatedIds.add(entityId);
		Assert.assertEquals(expectedDelta, delta);
	}

	@Override
	protected void interpolate(int entityId, float alpha) {
		interpolatedIds.add(entityId);
		Assert.assertEquals(expectedAlpha, alpha);
	}
}
