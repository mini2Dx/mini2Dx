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

import com.artemis.utils.Bag;
import com.artemis.utils.IntBag;
import org.mini2Dx.gdx.math.MathUtils;

import java.util.Arrays;

import static com.artemis.utils.reflect.ReflectionUtil.implementsObserver;

/**
 * An {@link EntitySystem} that will wait for an interval then queue all
 * entities to be updated over the duration of the next interval.
 */
public abstract class DispersedIntervalEntitySystem extends BaseEntitySystem
        implements EntitySubscription.SubscriptionListener {

    static final int FLAG_INSERTED = 1;
    static final int FLAG_REMOVED = 1 << 1;

    protected final IntBag processingQueue = new IntBag();

    private float interval;
    private float timer;
    private float updateDelta;
    protected int entitiesPerUpdate;

    private boolean shouldSyncEntities;
    private WildBag<Entity> entities = new WildBag(Entity.class);

    private int methodFlags;

    /**
     * Creates an entity system that uses the specified aspect as a matcher
     * against entities.
     *
     * @param aspect
     *			to match against entities
     */
    public DispersedIntervalEntitySystem(Aspect.Builder aspect, float interval) {
        super(aspect);
        this.interval = interval;
        this.timer = interval;
    }

    /**
     * Updates an entity
     *
     * @param entityId
     *            The entity id
     * @param delta
     *            The delta since the last update
     */
    protected abstract void update(int entityId, float delta);

    @Override
    protected void processSystem() {
        updateDelta += getWorld().getDelta();
        for (int i = 0; i < entitiesPerUpdate && processingQueue.size() > 0; i++) {
            int nextEntityId = processingQueue.remove(0);
            update(nextEntityId, updateDelta);
        }

        timer += getWorld().getDelta();
        if (timer >= interval) {
            updateDelta = timer;
            timer = timer % interval;

            int totalEntities = getEntityIds().size();

            entitiesPerUpdate = MathUtils.round((totalEntities + processingQueue.size()) / interval);
            entitiesPerUpdate = Math.max(1, entitiesPerUpdate);

            for (int i = 0; i < totalEntities; i++) {
                processingQueue.add(getEntityIds().get(i));
            }
        }
    }

    @Override
    protected void setWorld(World world) {
        super.setWorld(world);
        if (implementsObserver(this, "inserted"))
            methodFlags |= FLAG_INSERTED;
        if (implementsObserver(this, "removed"))
            methodFlags |= FLAG_REMOVED;
    }

    @Override
    public final void inserted(IntBag entities) {
        shouldSyncEntities = true;
        if ((methodFlags & FLAG_INSERTED) > 0)
            super.inserted(entities);
    }

    @Override
    protected final void inserted(int entityId) {
    }

    @Override
    public final void removed(IntBag entities) {
        shouldSyncEntities = true;
        if ((methodFlags & FLAG_REMOVED) > 0)
            super.removed(entities);
    }

    @Override
    protected final void removed(int entityId) {
        processingQueue.removeValue(entityId);
    }

    public void inserted(Entity e) {
        throw new RuntimeException("everything changes");
    }

    public void removed(Entity e) {
        throw new RuntimeException("everything breaks");
    }

    /**
     * Gets the entities processed by this {@link DispersedIntervalEntitySystem}. Warning: do not delete entities from
     * this bag.
     *
     * @return {@link DispersedIntervalEntitySystem}'s entity bag, as matched by {@link Aspect}.
     */
    public Bag<Entity> getEntities() {
        if (shouldSyncEntities) {
            int oldSize = entities.size();
            entities.setSize(0);
            IntBag entityIds = subscription.getEntities();
            int[] ids = entityIds.getData();
            for (int i = 0; i < entityIds.size(); i++) {
                entities.add(world.getEntity(ids[i]));
            }

            if (oldSize > entities.size()) {
                Arrays.fill(entities.getData(), entities.size(), oldSize, null);
            }

            shouldSyncEntities = false;
        }

        return entities;
    }

    /**
     * Returns the interval of the system
     * @return
     */
    public float getInterval() {
        return interval;
    }

    /**
     * Sets the interval of the system
     * @param interval
     */
    public void setInterval(float interval) {
        this.interval = interval;
    }
}
