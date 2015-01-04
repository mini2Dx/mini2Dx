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
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.mini2Dx.ecs.component.Component;
import org.mini2Dx.ecs.component.ComponentStore;
import org.mini2Dx.ecs.component.ComponentTypeIdAllocator;
import org.mini2Dx.ecs.component.DefaultComponentStore;
import org.omg.CORBA.OMGVMCID;

/**
 * A common interface for Entity implementations within the
 * Entity-Component-System pattern
 */
public class Entity implements ComponentStore {
	private int id;
	private UUID uuid;
	private ComponentStore componentStore;
	private List<EntityListener> listeners;
	private List<Entity> children;
	
	/**
	 * Creates a new {@link Entity} with a generated ID and a {@link DefaultComponentStore}
	 */
	public Entity() {
		this.id = EntityIdAllocator.allocate();
		initialise();
	}
	
	/**
	 * Creates a new {@link Entity} with a specific ID and a {@link DefaultComponentStore}
	 * @param id The ID to allocate this {@link Entity}
	 */
	public Entity(int id) {
		this.id = id;
		initialise();
	}
	
	/**
	 * Creates a new {@link Entity} with a generated ID with a specific {@link ComponentStore} implementaton
	 * @param componentStore The {@link ComponentStore} implementation to use
	 */
	public Entity(ComponentStore componentStore) {
		this.id = EntityIdAllocator.allocate();
		initialise();
		this.componentStore = componentStore;
	}
	
	/**
	 * Creates a new {@link Entity} with a specific ID and {@link ComponentStore} implementation
	 * @param id The ID to allocate this {@link Entity}
	 * @param componentStore The {@link ComponentStore} implementation to use
	 */
	public Entity(int id, ComponentStore componentStore) {
		this.id = id;
		initialise();
		this.componentStore = componentStore;
	}
	
	private void initialise() {
		uuid = UUID.randomUUID();
		componentStore = new DefaultComponentStore();
		children = new CopyOnWriteArrayList<Entity>();
	}
	
	/**
	 * Destroys this {@link Entity} and deallocates its ID
	 */
	public void destroy() {
		EntityIdAllocator.deallocate(id);
	}
	
	public void addChild(Entity child) {
		children.add(child);
	}

	public void removeChild(Entity child) {
		children.remove(child);
	}

	public List<Entity> getChildren() {
		return children;
	}

	@Override
	public <T> T getComponent(Class<T> clazz) {
		return componentStore.getComponent(clazz);
	}
	
	@Override
	public <T extends Component> T getComponent(String name, Class<T> clazz) {
		return componentStore.getComponent(name, clazz);
	}
	
	@Override
	public <T extends Component> T getComponent(String name, int componentTypeId) {
		return componentStore.getComponent(name, componentTypeId);
	}

	/**
	 * Adds a {@link Component} to this {@link UUIDEntity} and notifies any
	 * attached {@link EntityListener}s of this addition
	 * 
	 * @param component
	 *            An instance of {@link Component}
	 */
	@Override
	public void addComponent(Component component) {
		componentStore.addComponent(component);

		component.setEntity(this);
		component.onAddToEntity();

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
	 *         to this {@link UUIDEntity}
	 */
	@Override
	public <T> SortedSet<T> getComponents(Class<T> clazz) {
		return componentStore.getComponents(clazz);
	}

	/**
	 * Removes the specified {@link Component} from this {@link UUIDEntity}
	 * 
	 * @param component
	 *            The {@link Component} to remove
	 */
	@Override
	public void removeComponent(Component component) {
		componentStore.removeComponent(component);

		if (listeners != null) {
			for (EntityListener listener : listeners) {
				listener.componentRemoved(this, component);
			}
		}
		component.setEntity(null);
	}

	/**
	 * Removes all {@link Component}s that implement a specific type
	 * 
	 * @param clazz
	 *            The {@link Class} to search for
	 */
	@Override
	public <T extends Component> SortedSet<T> removeAllComponentsOfType(
			Class<T> clazz) {
		SortedSet<T> componentsRemoved = componentStore.removeAllComponentsOfType(clazz);

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
	public <T> T getComponent(int componentTypeId) {
		return componentStore.getComponent(componentTypeId);
	}

	@Override
	public <T> SortedSet<T> getComponents(int componentTypeId) {
		return componentStore.getComponents(componentTypeId);
	}

	@Override
	public <T extends Component> SortedSet<T> removeAllComponentsOfType(
			int componentTypeId) {
		SortedSet<T> componentsRemoved = componentStore.removeAllComponentsOfType(componentTypeId);

		if (listeners != null) {
			for (T component : componentsRemoved) {
				for (EntityListener listener : listeners) {
					listener.componentRemoved(this, component);
				}
			}
		}
		return componentsRemoved;
	}

	public <T extends Component> T getComponentInDescendants(Class<T> clazz) {
		return getComponentInEntityDescendants(this, ComponentTypeIdAllocator.getId(clazz));
	}

	public <T extends Component> T getComponentInDescendants(int componentTypeId) {
		return getComponentInEntityDescendants(this, componentTypeId);
	}
	
	
	public <T extends Component> T getComponentInDescendants(String name, Class<T> clazz) {
		return getComponentInEntityDescendants(this, name, ComponentTypeIdAllocator.getId(clazz));
	}
	
	public <T extends Component> T getComponentInDescendants(String name, int componentTypeId) {
		return getComponentInEntityDescendants(this, name, componentTypeId);
	}
	
	public <T extends Component> SortedSet<T> getComponentsInDescendants(Class<T> clazz) {
		SortedSet<T> result = new TreeSet<T>();
		getComponentsInEntityDescendants(result, this, ComponentTypeIdAllocator.getId(clazz));
		return result;
	}
	
	public <T extends Component> SortedSet<T> getComponentsInDescendants(int componentTypeId) {
		SortedSet<T> result = new TreeSet<T>();
		getComponentsInEntityDescendants(result, this, componentTypeId);
		return result;
	}
	
	private <T extends Component> T getComponentInEntityDescendants(Entity entity, int componentTypeId) {
		List<Entity> childEntities = entity.getChildren();
		for(int i = 0; i < childEntities.size(); i++) {
			T result = (T) childEntities.get(i).getComponent(componentTypeId);
			if(result != null) {
				return result;
			}
		}
		for(int i = 0; i < childEntities.size(); i++) {
			T result = getComponentInEntityDescendants(childEntities.get(i), componentTypeId);
			if(result != null) {
				return result;
			}
		}
		return null;
	}
	
	private <T extends Component> T getComponentInEntityDescendants(Entity entity, String name, int componentTypeId) {
		List<Entity> childEntities = entity.getChildren();
		for(int i = 0; i < childEntities.size(); i++) {
			SortedSet<T> components = childEntities.get(i).getComponents(componentTypeId);
			Iterator<T> iterator = components.iterator();
			while(iterator.hasNext()) {
				T component = iterator.next();
				if(component != null && component.getName().equals(name)) {
					return component;
				}
			}
		}
		for(int i = 0; i < childEntities.size(); i++) {
			T result = getComponentInEntityDescendants(childEntities.get(i), name, componentTypeId);
			if(result != null) {
				return result;
			}
		}
		return null;
	}
	
	private <T extends Component> void getComponentsInEntityDescendants(SortedSet<T> result, Entity entity, int componentTypeId) {
		List<Entity> childEntities = entity.getChildren();
		for(int i = 0; i < childEntities.size(); i++) {
			SortedSet<T> components = childEntities.get(i).getComponents(componentTypeId);
			if(components != null && components.size() > 0) {
				result.addAll(components);
			}
		}
		for(int i = 0; i < childEntities.size(); i++) {
			getComponentsInEntityDescendants(result, childEntities.get(i), componentTypeId);
		}
	}

	/**
	 * Adds an {@link EntityListener} to this {@link UUIDEntity}
	 * 
	 * @param listener
	 *            The {@link EntityListener} to add
	 */
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
	public void removeEntityListener(EntityListener listener) {
		if (listeners != null) {
			listeners.remove(listener);
		}
	}

	public UUID getUUID() {
		return uuid;
	}

	public int getId() {
		return id;
	}
}
