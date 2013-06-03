/*******************************************************
 * Copyright (c) 2013 Thomas Cashman
 * All rights reserved.
 *******************************************************/
package org.miniECx.core.system;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.mini2Dx.core.graphics.Graphics;
import org.miniECx.core.entity.Entity;

/**
 *
 * @author Thomas Cashman
 */
public abstract class AbstractSystem implements System {
	protected Map<UUID, Entity> entities;
	protected boolean isDebugging;
	
	public AbstractSystem() {
		entities = new ConcurrentHashMap<UUID, Entity>();
		isDebugging = false;
	}

	@Override
	public void addEntity(Entity entity) {
		entities.put(entity.getUUID(), entity);
	}

	@Override
	public void removeEntity(Entity entity) {
		entities.remove(entity.getUUID());
	}

	@Override
	public void update(float delta) {
		for (UUID uuid : entities.keySet()) {
			update(entities.get(uuid), delta);
		}
	}

	@Override
	public abstract void update(Entity entity, float delta);

	@Override
	public void interpolate(float alpha) {
		for (UUID uuid : entities.keySet()) {
			interpolate(entities.get(uuid), alpha);
		}
	}

	@Override
	public abstract void interpolate(Entity entity, float alpha);

	@Override
	public void render(Graphics g) {
		for (UUID uuid : entities.keySet()) {
			render(entities.get(uuid), g);
		}
	}

	@Override
	public abstract void render(Entity entity, Graphics g);

	@Override
	public boolean isDebugging() {
		return isDebugging;
	}

	@Override
	public void setDebugging(boolean isDebugging) {
		this.isDebugging = isDebugging;
	}

}
