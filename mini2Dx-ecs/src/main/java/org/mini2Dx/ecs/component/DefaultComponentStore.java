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
package org.mini2Dx.ecs.component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.mini2Dx.ecs.entity.EntityListener;

/**
 * Default implementation of {@link ComponentStore} that allows for concurrent
 * modification
 * 
 * @author Thomas Cashman
 */
public class DefaultComponentStore implements ComponentStore {
	private Map<Integer, SortedSet> components;

	public DefaultComponentStore() {
		components = new ConcurrentHashMap<Integer, SortedSet>();
	}

	@Override
	public <T> T getComponent(int componentTypeId) {
		checkConsistency(componentTypeId);
		SortedSet<T> components = this.components.get(componentTypeId);
		if (components.size() > 0) {
			return components.first();
		}
		return null;
	}

	@Override
	public <T> T getComponent(Class<T> clazz) {
		return getComponent(ComponentTypeIdAllocator.getId(clazz));
	}
	
	@Override
	public <T extends Component> T getComponent(String name, int componentTypeId) {
		SortedSet<T> components = getComponents(componentTypeId);
		Iterator<T> iterator = components.iterator();
		while(iterator.hasNext()) {
			T component = iterator.next();
			if(component.getName().equals(name)) {
				return component;
			}
		}
		return null;
	}

	@Override
	public <T extends Component> T getComponent(String name, Class<T> clazz) {
		SortedSet<T> components = getComponents(clazz);
		Iterator<T> iterator = components.iterator();
		while(iterator.hasNext()) {
			T component = iterator.next();
			if(component.getName().equals(name)) {
				return component;
			}
		}
		return null;
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
		int componentTypeId = ComponentTypeIdAllocator.getId(component
				.getClass());
		checkConsistency(componentTypeId);
		components.get(componentTypeId).add(component);

		addInterfaces(component);
		addSuperclasses(component);
	}

	private void addSuperclasses(Component component) {
		Class clazz = component.getClass().getSuperclass();

		while (clazz != null) {
			int key = ComponentTypeIdAllocator.getId(clazz);
			checkConsistency(key);
			components.get(key).add(component);
			clazz = clazz.getSuperclass();
		}
	}

	private void addInterfaces(Component component) {
		Class clazz = component.getClass();

		while (clazz != null) {
			for (Class interfaceClass : clazz.getInterfaces()) {
				int key = ComponentTypeIdAllocator.getId(interfaceClass);
				checkConsistency(key);
				components.get(key).add(component);
			}
			clazz = clazz.getSuperclass();
		}
	}

	@Override
	public <T> SortedSet<T> getComponents(int componentTypeId) {
		checkConsistency(componentTypeId);
		return components.get(componentTypeId);
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
		return getComponents(ComponentTypeIdAllocator.getId(clazz));
	}

	/**
	 * Removes the specified {@link Component} from this {@link UUIDEntity}
	 * 
	 * @param component
	 *            The {@link Component} to remove
	 */
	@Override
	public void removeComponent(Component component) {
		Class clazz = component.getClass();

		int componentTypeId = component.getComponentTypeId();
		checkConsistency(componentTypeId);
		components.get(componentTypeId).remove(component);

		removeInterfaces(component);
		removeSuperclasses(component);
		removeChildClasses(component);
	}

	private void removeSuperclasses(Component component) {
		Class clazz = component.getClass().getSuperclass();

		while (clazz != null) {
			int componentTypeId = ComponentTypeIdAllocator.getId(clazz);
			checkConsistency(componentTypeId);
			components.get(componentTypeId).remove(component);
			clazz = clazz.getSuperclass();
		}
	}

	private void removeInterfaces(Component component) {
		Class clazz = component.getClass();

		while (clazz != null) {
			for (@SuppressWarnings("rawtypes")
			Class interfaceClass : clazz.getInterfaces()) {
				int componentTypeId = ComponentTypeIdAllocator
						.getId(interfaceClass);
				checkConsistency(componentTypeId);
				components.get(componentTypeId).remove(component);
			}
			clazz = clazz.getSuperclass();
		}
	}

	private void removeChildClasses(Component component) {
		Class componentClass = component.getClass();
		for (String key : ComponentTypeIdAllocator.IDENTIFIERS.keySet()) {
			try {
				Class clazz = Class.forName(key);
				if (clazz.equals(componentClass)) {
					int componentTypeId = ComponentTypeIdAllocator.getId(clazz);
					components.get(componentTypeId).remove(component);
					removeChildClassInterfaces(clazz, component);
				}
			} catch (Exception e) {
			}
		}
	}
	
	private void removeChildClassInterfaces(Class clazz, Component component) {
		while (clazz != null) {
			for (@SuppressWarnings("rawtypes")
			Class interfaceClass : clazz.getInterfaces()) {
				int componentTypeId = ComponentTypeIdAllocator
						.getId(interfaceClass);
				checkConsistency(componentTypeId);
				components.get(componentTypeId).remove(component);
			}
			clazz = clazz.getSuperclass();
		}
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
		int componentTypeId = ComponentTypeIdAllocator.getId(clazz);
		return removeAllComponentsOfType(componentTypeId);
	}

	private <T> void checkConsistency(int componentTypeId) {
		if (!components.containsKey(componentTypeId)) {
			components.put(componentTypeId, new ConcurrentSkipListSet<T>());
		}
	}

	@Override
	public <T extends Component> SortedSet<T> removeAllComponentsOfType(
			int componentTypeId) {
		SortedSet<T> componentsRemoved = components.remove(componentTypeId);

		if (componentsRemoved == null)
			return null;

		for (T component : componentsRemoved) {
			removeInterfaces(component);
			removeSuperclasses(component);
			removeChildClasses(component);
			component.setEntity(null);
		}

		checkConsistency(componentTypeId);
		return componentsRemoved;
	}
}
