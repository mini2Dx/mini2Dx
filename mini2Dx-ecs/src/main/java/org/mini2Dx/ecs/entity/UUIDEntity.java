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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.mini2Dx.ecs.component.Component;

/**
 * Implements an entity as part of the Entity-Component-System pattern
 * 
 * @author Thomas Cashman
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UUIDEntity implements Entity {
	private UUID uuid;
	private Map<String, List> components;
	private List<EntityListener> listeners;

	/**
	 * Constructs an {@link UUIDEntity} with a random {@link UUID}
	 */
	public UUIDEntity() {
		this(UUID.randomUUID());
	}

	/**
	 * Constructs an {@link UUIDEntity} with the specified {@link UUID}
	 * 
	 * @param uuid
	 *            The {@link UUID} to associate with this {@link UUIDEntity}
	 */
	public UUIDEntity(UUID uuid) {
		components = new ConcurrentHashMap<String, List>();
		this.uuid = uuid;
	}

	/**
	 * Adds a {@link Component} to this {@link UUIDEntity} and notifies any attached
	 * {@link EntityListener}s of this addition
	 * 
	 * @param component An instance of {@link Component}
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
	 * Returns all {@link Component}s that implement the specified the class or interface
	 * @param clazz The {@link Class} to search for
	 * @return An empty {@link List} if no such {@link Component}s are attached to this {@link UUIDEntity}
	 */
	@Override
	public <T> List<T> getComponents(Class<T> clazz) {
		String key = getClassKey(clazz);
		checkConsistency(key, clazz);
		return components.get(key);
	}

	/**
	 * Removes the specified {@link Component} from this {@link UUIDEntity}
	 * @param component The {@link Component} to remove
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
	 * Adds an {@link EntityListener} to this {@link UUIDEntity}
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
		return clazz.getName();
	}

	/**
	 * Returns the {@link UUID} for this {@link UUIDEntity}
	 * 
	 * @return
	 */
	public UUID getUUID() {
		return uuid;
	}
}
