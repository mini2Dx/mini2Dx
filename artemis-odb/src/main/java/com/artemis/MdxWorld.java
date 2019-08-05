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

import com.artemis.listener.WorldListener;
import com.artemis.utils.Bag;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.gdx.utils.Array;

/**
 * Extends {@link World} to allow for interpolating and rendering of {@link System}s
 */
public class MdxWorld extends World {
    private final Bag<InterpolatingSystem> interpolatingSystemsBag;
    private final Bag<RenderingSystem> renderingSystemsBag;

    private final MdxInvocationStrategy mdxInvocationStrategy;

    private Array<WorldListener> worldListeners;
    public float alpha;

    /**
     * Creates a new world
     *
     * @param configuration The configuration to be applied
     */
    public MdxWorld(WorldConfiguration configuration) {
        super(configuration);
        interpolatingSystemsBag = new Bag<InterpolatingSystem>();
        renderingSystemsBag = new Bag<RenderingSystem>();
        mdxInvocationStrategy = new MdxInvocationStrategy();

        for (BaseSystem system : configuration.systems) {
            if (system instanceof InterpolatingSystem) {
                interpolatingSystemsBag.add((InterpolatingSystem) system);
            }
            if (system instanceof RenderingSystem) {
                renderingSystemsBag.add((RenderingSystem) system);
            }
        }

        setInvocationStrategy(mdxInvocationStrategy);
    }

    /**
     * Invokes interpolate on all {@link InterpolatingEntitySystem}s
     */
    public void interpolate() {
        mdxInvocationStrategy.interpolate(interpolatingSystemsBag);
    }

    /**
     * Invokes render on all {@link RenderingEntitySystem}s
     * @param g The {@link Graphics}s instance
     */
    public void render(Graphics g) {
        mdxInvocationStrategy.render(renderingSystemsBag, g);
    }

    @Override
    public int create() {
        int result = super.create();
        notifyWorldListenersOnCreate(result);
        return result;
    }

    @Override
    public Entity createEntity() {
        Entity result = super.createEntity();
        notifyWorldListenersOnCreate(result.id);
        return result;
    }

    @Override
    public void deleteEntity(Entity e) {
        notifyWorldListenersOnDeleted(e.id);
        super.deleteEntity(e);
    }

    @Override
    public void delete(int entityId) {
        notifyWorldListenersOnDeleted(entityId);
        super.delete(entityId);
    }

    private void notifyWorldListenersOnCreate(int entityId) {
        if (worldListeners == null) {
            return;
        }
        for (int i = worldListeners.size - 1; i >= 0; i--) {
            worldListeners.get(i).afterEntityCreated(this, entityId);
        }
    }

    private void notifyWorldListenersOnDeleted(int entityId) {
        if (worldListeners == null) {
            return;
        }
        for (int i = worldListeners.size - 1; i >= 0; i--) {
            worldListeners.get(i).beforeEntityDeleted(this, entityId);
        }
    }

    /**
     * Adds a {@link WorldListener} to be notified of {@link MdxWorld} events
     * @param listener The {@link WorldListener} to be added
     */
    public void addWorldListener(WorldListener listener) {
        if (worldListeners == null) {
            worldListeners = new Array<WorldListener>();
        }
        worldListeners.add(listener);
    }

    /**
     * Removes a {@link WorldListener} from {@link MdxWorld} notifications
     * @param listener The {@link WorldListener} to be removed
     */
    public void removeWorldListener(WorldListener listener) {
        if (worldListeners == null) {
            return;
        }
        worldListeners.removeValue(listener, false);
    }

    /**
     * Returns the interpolation alpha
     * @return
     */
    public float getAlpha() {
        return alpha;
    }

    /**
     * Sets the interpolation alpha
     * @param alpha
     */
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}
