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
package org.mini2Dx.ecs.system;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.ecs.entity.Entity;
import org.mini2Dx.ecs.entity.UUIDEntity;

/**
 * An abstract base implementation of {@link System} which stores {@link UUIDEntity}
 * instances based on their {@link UUID}
 * 
 * @author Thomas Cashman
 */
public abstract class UUIDSystem implements System<UUIDEntity> {
	protected Map<UUID, UUIDEntity> entities;
	protected boolean isDebugging;

	/**
	 * Default constructor
	 */
	public UUIDSystem() {
		entities = new ConcurrentHashMap<UUID, UUIDEntity>();
		isDebugging = false;
	}

	@Override
	public void addEntity(UUIDEntity entity) {
		entities.put(entity.getUUID(), entity);
	}

	@Override
	public void removeEntity(UUIDEntity entity) {
		entities.remove(entity.getUUID());
	}
	
	public Entity getEntity(UUID uuid) {
		return entities.get(uuid);
	}

	@Override
	public void update(GameContainer gc, float delta) {
		for (UUID uuid : entities.keySet()) {
			update(entities.get(uuid), gc, delta);
		}
	}

	/**
	 * Update an {@link UUIDEntity}
	 * 
	 * @param entity
	 *            The {@link UUIDEntity} to be updated
	 * @param gc
	 *            The {@link GameContainer} calling update
	 * @param delta
	 *            The time in seconds since the last update
	 */
	public abstract void update(UUIDEntity entity, GameContainer gc, float delta);

	@Override
	public void interpolate(GameContainer gc, float alpha) {
		for (UUID uuid : entities.keySet()) {
			interpolate(entities.get(uuid), gc, alpha);
		}
	}

	/**
	 * Interpolate an {@link UUIDEntity}
	 * 
	 * @param entity
	 *            The {@link UUIDEntity} to be interpolated
	 * @param gc
	 *            The {@link GameContainer} calling interpolate
	 * @param alpha
	 *            The alpha value to use during interpolation
	 */
	public abstract void interpolate(UUIDEntity entity, GameContainer gc,
			float alpha);

	@Override
	public void render(GameContainer gc, Graphics g) {
		for (UUID uuid : entities.keySet()) {
			render(entities.get(uuid), gc, g);
		}
	}

	/**
	 * Render an {@link UUIDEntity} and any debugging information if debugging is
	 * enabled
	 * 
	 * @param entity
	 *            The {@link UUIDEntity} to render
	 * @param gc
	 *            The {@link GameContainer} calling render
	 * @param g
	 *            The {@link Graphics} context
	 */
	public abstract void render(UUIDEntity entity, GameContainer gc, Graphics g);

	@Override
	public boolean isDebugging() {
		return isDebugging;
	}

	@Override
	public void setDebugging(boolean isDebugging) {
		this.isDebugging = isDebugging;
	}

}
