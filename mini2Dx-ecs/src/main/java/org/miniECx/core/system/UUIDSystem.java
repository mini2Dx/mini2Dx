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
package org.miniECx.core.system;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.miniECx.core.entity.Entity;

/**
 * An abstract base implementation of {@link System} which stores {@link Entity}
 * instances based on their {@link UUID}
 * 
 * @author Thomas Cashman
 */
public abstract class UUIDSystem implements System {
	protected Map<UUID, Entity> entities;
	protected boolean isDebugging;

	/**
	 * Default constructor
	 */
	public UUIDSystem() {
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
	public void update(GameContainer gc, float delta) {
		for (UUID uuid : entities.keySet()) {
			update(entities.get(uuid), gc, delta);
		}
	}

	/**
	 * Update an {@link Entity}
	 * 
	 * @param entity
	 *            The {@link Entity} to be updated
	 * @param gc
	 *            The {@link GameContainer} calling update
	 * @param delta
	 *            The time in seconds since the last update
	 */
	public abstract void update(Entity entity, GameContainer gc, float delta);

	@Override
	public void interpolate(GameContainer gc, float alpha) {
		for (UUID uuid : entities.keySet()) {
			interpolate(entities.get(uuid), gc, alpha);
		}
	}

	/**
	 * Interpolate an {@link Entity}
	 * 
	 * @param entity
	 *            The {@link Entity} to be interpolated
	 * @param gc
	 *            The {@link GameContainer} calling interpolate
	 * @param alpha
	 *            The alpha value to use during interpolation
	 */
	public abstract void interpolate(Entity entity, GameContainer gc,
			float alpha);

	@Override
	public void render(GameContainer gc, Graphics g) {
		for (UUID uuid : entities.keySet()) {
			render(entities.get(uuid), gc, g);
		}
	}

	/**
	 * Render an {@link Entity} and any debugging information if debugging is
	 * enabled
	 * 
	 * @param entity
	 *            The {@link Entity} to render
	 * @param gc
	 *            The {@link GameContainer} calling render
	 * @param g
	 *            The {@link Graphics} context
	 */
	public abstract void render(Entity entity, GameContainer gc, Graphics g);

	@Override
	public boolean isDebugging() {
		return isDebugging;
	}

	@Override
	public void setDebugging(boolean isDebugging) {
		this.isDebugging = isDebugging;
	}

}
