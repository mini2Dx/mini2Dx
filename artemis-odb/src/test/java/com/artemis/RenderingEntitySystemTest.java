/**
 * Copyright (c) 2015 See AUTHORS file
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
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;
import org.mini2Dx.core.Graphics;

import java.util.HashSet;
import java.util.Set;

/**
 * Unit tests for {@link RenderingEntitySystem}
 */
public class RenderingEntitySystemTest extends RenderingEntitySystem {
    private MdxWorld world;
    private RenderingEntitySystem system;

    private Mockery mockery;
    private static Graphics graphics;

    private static Set<Integer> renderedIds;

    public RenderingEntitySystemTest() {
        super(Aspect.all(DummyComponent.class));
    }

    @Before
    public void setUp() {
        renderedIds = new HashSet<Integer>();
        system = new RenderingEntitySystemTest();

        mockery = new Mockery();
        graphics = mockery.mock(Graphics.class);

        WorldConfiguration configuration = new WorldConfiguration();
        configuration.setSystem(system);
        world = new MdxWorld(configuration);
    }

    @Test
    public void testRenderMatchesEntities() {
        Entity entityWithComponent = world.createEntity();
        entityWithComponent.edit().add(new DummyComponent());

        Entity entityWithoutComponent = world.createEntity();

        world.process();
        world.render(graphics);

        Assert.assertEquals(true, renderedIds.contains(entityWithComponent.getId()));
        Assert.assertEquals(false, renderedIds.contains(entityWithoutComponent.getId()));
    }

    @Test
    public void testRenderPassesGraphicsInstance() {
        Entity entityWithComponent = world.createEntity();
        entityWithComponent.edit().add(new DummyComponent());

        world.process();
        world.render(graphics);
    }

    @Test
    public void testRenderDoesNothingIfCalledBeforeFirstProcess() {
        Entity entityWithComponent = world.createEntity();
        entityWithComponent.edit().add(new DummyComponent());
        world.render(graphics);
    }

    @Override
    protected void render(int entityId, Graphics g) {
        renderedIds.add(entityId);
        Assert.assertEquals(graphics, g);
    }

}
