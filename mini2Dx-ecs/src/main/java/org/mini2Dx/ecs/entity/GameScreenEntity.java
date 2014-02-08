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
package org.mini2Dx.ecs.entity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.ecs.component.Component;
import org.mini2Dx.ecs.component.screen.ScreenComponent;
import org.mini2Dx.ecs.game.EntityComponentSystemGame;

/**
 * An implementation of {@link Entity} that also implements {@link GameScreen}
 * 
 * @author Thomas Cashman
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class GameScreenEntity implements Entity, GameScreen {
	private int id;
	private Map<String, List> components;
	private List<EntityListener> listeners;

	public GameScreenEntity(int id) {
		this.id = id;
		components = new ConcurrentHashMap<String, List>();
	}
	
	public void updateScreenComponents(GameContainer gc,
			ScreenManager<? extends GameScreen> screenManager, float delta) {
		List<ScreenComponent> components = getComponents(ScreenComponent.class);
		for(ScreenComponent component : components) {
			component.update(gc, screenManager, this, delta);
		}
	}
	
	public void interpolateScreenComponents(GameContainer gc, float alpha) {
		List<ScreenComponent> components = getComponents(ScreenComponent.class);
		for(ScreenComponent component : components) {
			component.interpolate(gc, this, alpha);
		}
	}
	
	public void renderScreenComponents(GameContainer gc, Graphics g) {
		List<ScreenComponent> components = getComponents(ScreenComponent.class);
		for(ScreenComponent component : components) {
			component.render(gc, this, g);
		}
	}

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
		
		addSuperclassInterfaces(component);

		if (listeners != null) {
			for (EntityListener listener : listeners) {
				listener.componentAdded(this, component);
			}
		}
	}
	
	private void addSuperclassInterfaces(Component component) {
		Class clazz = component.getClass().getSuperclass();
		
		while(clazz != null) {
			String key;
			for (Class interfaceClass : clazz.getInterfaces()) {
				key = getClassKey(interfaceClass);
				checkConsistency(key, interfaceClass);
				components.get(key).add(component);
			}
			clazz = clazz.getSuperclass();
		}
	}

	@Override
	public <T> List<T> getComponents(Class<T> clazz) {
		String key = getClassKey(clazz);
		checkConsistency(key, clazz);
		return components.get(key);
	}

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
		
		removeSuperclassInterfaces(component);

		if (listeners != null) {
			for (EntityListener listener : listeners) {
				listener.componentRemoved(this, component);
			}
		}
	}
	
	private void removeSuperclassInterfaces(Component component) {
		Class clazz = component.getClass().getSuperclass();
		
		while(clazz != null) {
			String key;
			for (Class interfaceClass : clazz.getInterfaces()) {
				key = getClassKey(interfaceClass);
				checkConsistency(key, interfaceClass);
				components.get(key).remove(component);
			}
			clazz = clazz.getSuperclass();
		}
	}

	@Override
	public <T extends Component> List<T> removeAllComponentsOfType(Class<T> clazz) {
		String key = getClassKey(clazz);
		List<T> componentsRemoved = components.remove(key);
		
		if(componentsRemoved == null)
			return null;
		
		for (T component : componentsRemoved) {
			removeSuperclassInterfaces(component);
		}
		
		checkConsistency(key, clazz);

		if (listeners != null) {
			for (T component : componentsRemoved) {
				for (EntityListener listener : listeners) {
					listener.componentRemoved(this, component);
				}
			}
		}
		return componentsRemoved;
	}

	@Override
	public void addEntityListener(EntityListener listener) {
		if (listeners == null) {
			listeners = new CopyOnWriteArrayList<EntityListener>();
		}
		listeners.add(listener);
	}

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
		return clazz.getName();
	}

	@Override
	public int getId() {
		return id;
	}
}
