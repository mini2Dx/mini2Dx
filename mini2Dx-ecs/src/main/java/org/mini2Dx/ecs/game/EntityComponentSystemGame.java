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
package org.mini2Dx.ecs.game;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.ecs.component.Component;
import org.mini2Dx.ecs.component.screen.ScreenComponent;
import org.mini2Dx.ecs.entity.Entity;
import org.mini2Dx.ecs.entity.EntityListener;
import org.mini2Dx.ecs.entity.UUIDEntity;
import org.mini2Dx.ecs.system.System;

/**
 * An implementation of {@link GameContainer} based on the
 * entity-component-system pattern
 * 
 * @author Thomas Cashman
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class EntityComponentSystemGame extends GameContainer implements
		Entity {
	private Map<String, List> components;
	private List<EntityListener> listeners;
	private List<System> systems;

	@Override
	protected void preinit() {
		super.preinit();
		components = new ConcurrentHashMap<String, List>();
		systems = new CopyOnWriteArrayList<System>();
	}

	@Override
	public void update(float delta) {
		for (System system : systems) {
			system.update(this, delta);
		}

		List<ScreenComponent> screenComponents = getComponents(ScreenComponent.class);
		for (ScreenComponent component : screenComponents) {
			component.update(this, delta);
		}
	}

	@Override
	public void interpolate(float alpha) {
		for (System system : systems) {
			system.interpolate(this, alpha);
		}

		List<ScreenComponent> screenComponents = getComponents(ScreenComponent.class);
		for (ScreenComponent component : screenComponents) {
			component.interpolate(this, alpha);
		}
	}

	@Override
	public void render(Graphics g) {
		for (System system : systems) {
			system.render(this, g);
		}

		List<ScreenComponent> screenComponents = getComponents(ScreenComponent.class);
		for (ScreenComponent component : screenComponents) {
			component.render(this, g);
		}
	}

	/**
	 * Adds a {@link Component} to this {@link EntityComponentSystemGame} and
	 * notifies any attached {@link EntityListener}s of this addition
	 * 
	 * @param component
	 *            An instance of {@link Component}
	 */
	@Override
	public void addComponent(Component component) {
		Class clazz = component.getClass();

		String key = getClassKey(clazz);
		checkConsistency(key, clazz);
		components.get(key).add(component);

		for (Class interfaceClass : clazz.getInterfaces()) {
			key = getClassKey(interfaceClass);
			checkConsistency(key, interfaceClass);
			components.get(key).add(component);
		}

		if (listeners != null) {
			for (EntityListener listener : listeners) {
				listener.componentAdded(this, component);
			}
		}
	}

	/**
	 * Returns all {@link Component}s that implement the specified the class or
	 * interface
	 * 
	 * @param clazz
	 *            The {@link Class} to search for
	 * @return An empty {@link List} if no such {@link Component}s are attached
	 *         to this {@link EntityComponentSystemGame}
	 */
	@Override
	public <T> List<T> getComponents(Class<T> clazz) {
		String key = getClassKey(clazz);
		checkConsistency(key, clazz);
		return components.get(key);
	}

	/**
	 * Removes the specified {@link Component} from this
	 * {@link EntityComponentSystemGame}
	 * 
	 * @param component
	 *            The {@link Component} to remove
	 */
	@Override
	public void removeComponent(Component component) {
		Class clazz = component.getClass();

		String key = getClassKey(clazz);
		checkConsistency(key, clazz);
		components.get(key).remove(component);

		for (Class interfaceClass : clazz.getInterfaces()) {
			key = getClassKey(interfaceClass);
			checkConsistency(key, interfaceClass);
			components.get(key).remove(component);
		}

		if (listeners != null) {
			for (EntityListener listener : listeners) {
				listener.componentRemoved(this, component);
			}
		}
	}

	/**
	 * Removes all {@link Component}s that implement a specific type
	 * 
	 * @param clazz
	 *            The {@link Class} to search for
	 */
	@Override
	public <T extends Component> void removeAllComponentsOfType(Class<T> clazz) {
		String key = getClassKey(clazz);
		List<T> componentsRemoved = components.remove(key);
		checkConsistency(key, clazz);

		if (listeners != null) {
			for (T component : componentsRemoved) {
				for (EntityListener listener : listeners) {
					listener.componentRemoved(this, component);
				}
			}
		}
	}

	/**
	 * Adds an {@link EntityListener} to this {@link EntityComponentSystemGame}
	 * 
	 * @param listener
	 *            The {@link EntityListener} to add
	 */
	@Override
	public void addEntityListener(EntityListener listener) {
		if (listeners == null) {
			listeners = new CopyOnWriteArrayList<EntityListener>();
		}
		listeners.add(listener);
	}

	/**
	 * Removes an {@link EntityListener} from this {@link UUIDEntity}
	 * 
	 * @param listener
	 *            The {@link EntityListener} to be removed
	 */
	@Override
	public void removeEntityListener(EntityListener listener) {
		if (listeners != null) {
			listeners.remove(listener);
		}
	}

	private <T> void checkConsistency(String key, Class<T> clazz) {
		if (!components.containsKey(key)) {
			components.put(key, new CopyOnWriteArrayList<T>());
		}
	}

	private String getClassKey(Class<?> clazz) {
		return clazz.getPackage() + "." + clazz.getSimpleName();
	}

	/**
	 * Adds a {@link System} to be handled by this
	 * {@link EntityComponentSystemGame}
	 * 
	 * @param system
	 *            An implementation of {@link System} to be added
	 */
	public void addSystem(System system) {
		systems.add(system);
	}

	/**
	 * Removes a {@link System} from this {@link EntityComponentSystemGame}
	 * 
	 * @param system
	 *            An implementation of {@link System} to be removed
	 */
	public void removeSystem(System system) {
		systems.remove(system);
	}
}
