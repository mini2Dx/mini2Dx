/**
 * Copyright (c) 2017 See AUTHORS file
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * <p>
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.artemis;

import com.artemis.system.test.DummyComponent;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Unit tests for {@link DispersedIntervalEntitySystem}
 */
public class DisperedIntervalEntitySystemTest extends DispersedIntervalEntitySystem {
    private static final int ITERATIONS = 3;
    private static final float INTERVAL = 5.0f;
    private static final float DELTA = 0.16f;

    private int totalEntitiesUpdated;
    private float delay = 0f, delayDelta = 0f;

    private final Set<Integer> processedEntities = new HashSet<Integer>();

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

    @Test
    public void testEntityRemovedDuringQueue() {
        final int entities = 1024;

        final List<Integer> entityIds = createEntities(entities);
        final int deletedEntityId = entityIds.get((entityIds.size() / 2) + 1);

        runSystem(DELTA);
        world.delete(deletedEntityId);
        runSystem();

        Assert.assertEquals(false, processedEntities.contains(deletedEntityId));
    }

    private List<Integer> createEntities(final int totalEntities) {
        final List<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < totalEntities; i++) {
            Entity entityWithComponent = world.createEntity();
            entityWithComponent.edit().add(new DummyComponent());
            result.add(entityWithComponent.getId());
        }
        return result;
    }

    private void runSystemOnce() {
        runSystem(INTERVAL);
    }

    private void runSystem() {
        runSystem(INTERVAL * ITERATIONS);
    }

    private void runSystem(float duration) {
        for (float timer = 0f; timer < duration; timer += DELTA) {
            world.setDelta(DELTA);
            world.process();
        }
    }

    @Override
    protected void update(int entityId, float delta) {
        processedEntities.add(entityId);
        totalEntitiesUpdated++;
        Assert.assertEquals(true, delta >= INTERVAL);
        Assert.assertEquals(true, delta < INTERVAL * 2f);
    }

}
