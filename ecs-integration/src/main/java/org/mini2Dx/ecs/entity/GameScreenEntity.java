/**
 * Copyright (c) 2015, mini2Dx Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.ecs.entity;

import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.ecs.component.ComponentTypeIdAllocator;
import org.mini2Dx.ecs.component.screen.RenderableComponent;
import org.mini2Dx.ecs.component.screen.UpdatableComponent;

/**
 * An implementation of {@link Entity} that also implements {@link GameScreen}
 * 
 * This {@link Entity} will update/interpolate/render in a depth-first order,
 * i.e. it will find the deepest descendant in the tree and execute
 * update/interpolate/render on the {@link UpdatableComponent}s/
 * {@link RenderableComponent}s of that first before traversing up the tree
 * until it reaches this entity
 */
public abstract class GameScreenEntity extends Entity implements GameScreen {
	private int updatableComponentTypeId;
	private int renderableComponentTypeId;
	
	public GameScreenEntity(int id) {
		super(id);
		updatableComponentTypeId = ComponentTypeIdAllocator.getId(UpdatableComponent.class);
		renderableComponentTypeId = ComponentTypeIdAllocator.getId(RenderableComponent.class);
	}

	@Override
	public void update(GameContainer gc,
			ScreenManager<? extends GameScreen> screenManager, float delta) {
		update(this, gc, screenManager, delta);
	}

	private void update(Entity entity, GameContainer gc,
			ScreenManager<? extends GameScreen> screenManager, float delta) {
		List<Entity> entities = entity.getChildren();
		for (int i = 0; i < entities.size(); i++) {
			update(entities.get(i), gc, screenManager, delta);
		}

		SortedSet<UpdatableComponent> components = entity
				.getComponents(updatableComponentTypeId);
		Iterator<UpdatableComponent> iterator = components.iterator();
		while (iterator.hasNext()) {
			UpdatableComponent component = iterator.next();
			component.update(gc, delta);
		}
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
		interpolate(this, gc, alpha);
	}

	private void interpolate(Entity entity, GameContainer gc, float alpha) {
		List<Entity> entities = entity.getChildren();
		for (int i = 0; i < entities.size(); i++) {
			interpolate(entities.get(i), gc, alpha);
		}

		SortedSet<UpdatableComponent> components = entity
				.getComponents(updatableComponentTypeId);
		Iterator<UpdatableComponent> iterator = components.iterator();
		while (iterator.hasNext()) {
			UpdatableComponent component = iterator.next();
			component.interpolate(gc, alpha);
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		render(this, gc, g);
	}

	private void render(Entity entity, GameContainer gc, Graphics g) {
		List<Entity> entities = entity.getChildren();
		for (int i = 0; i < entities.size(); i++) {
			render(entities.get(i), gc, g);
		}

		SortedSet<RenderableComponent> components = entity
				.getComponents(renderableComponentTypeId);
		Iterator<RenderableComponent> iterator = components.iterator();
		while (iterator.hasNext()) {
			RenderableComponent component = iterator.next();
			component.render(gc, g);
		}
	}
}
