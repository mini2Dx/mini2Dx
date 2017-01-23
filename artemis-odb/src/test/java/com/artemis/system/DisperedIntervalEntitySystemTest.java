/**
 * Copyright (c) 2017 See AUTHORS file
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

import org.junit.Before;
import org.junit.Test;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.MdxWorld;
import com.artemis.WorldConfiguration;
import com.artemis.system.test.DummyComponent;

import junit.framework.Assert;

/**
 * Unit tests for {@link DispersedIntervalEntitySystem}
 */
public class DisperedIntervalEntitySystemTest extends DispersedIntervalEntitySystem {
	private static final int ITERATIONS = 3;
	private static final float INTERVAL = 5.0f;
	private static final float DELTA = 0.16f;

	private int totalEntitiesUpdated; 
	private float delay = 0f, delayDelta = 0f;
	
	public DisperedIntervalEntitySystemTest() {
		super(Aspect.all(DummyComponent.class), INTERVAL);
	}
	
	@Before
	public void setUp() {
		totalEntitiesUpdated = 0;
		
		WorldConfiguration configuration = new WorldConfiguration();
		configuration.setSystem(this);
		world = new MdxWorld(configuration);
	}
	
	@Test
	public void testSystemWithFewEntities() {
		final int entities = 7;
		createEntities(entities);
		runSystem();
		Assert.assertEquals(ITERATIONS * entities, totalEntitiesUpdated);
	}
	
	@Test
	public void testSystemWithManyEntities() {
		final int entities = 570;
		createEntities(entities);
		runSystem();
		Assert.assertEquals(ITERATIONS * entities, totalEntitiesUpdated);
	}
	
	private void createEntities(final int totalEntities) {
		for(int i = 0; i < totalEntities; i++) {
			Entity entityWithComponent = world.createEntity();
			entityWithComponent.edit().add(new DummyComponent());
		}
	}
	
	private void runSystem() {
		for(float timer = 0f; timer < INTERVAL * ITERATIONS; timer += DELTA) {
			world.setDelta(DELTA);
			world.process();
		}
	}

	@Override
	protected void update(int entityId, float delta) {
		totalEntitiesUpdated++;
		Assert.assertEquals(true, delta >= INTERVAL);
	}

}
