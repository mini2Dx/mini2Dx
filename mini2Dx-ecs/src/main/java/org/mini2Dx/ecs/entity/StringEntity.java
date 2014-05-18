/**
 * Copyright 2014 Thomas Cashman
 */
package org.mini2Dx.ecs.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

import org.mini2Dx.ecs.component.Component;

/**
 *
 *
 * @author Thomas Cashman
 */
public class StringEntity implements Entity<String> {
	private String id;
	private Map<String, SortedSet> components;
	private List<EntityListener> listeners;
	private List<Entity<String>> children;

	/**
	 * Constructs an {@link UUIDEntity} with a random {@link UUID}
	 */
	public StringEntity() {
		this(UUID.randomUUID().toString());
	}

	/**
	 * Constructs an {@link UUIDEntity} with the specified {@link UUID}
	 * 
	 * @param uuid
	 *            The {@link UUID} to associate with this {@link UUIDEntity}
	 */
	public StringEntity(String id) {
		components = new ConcurrentHashMap<String, SortedSet>();
		children = new ArrayList<Entity<String>>();
		this.id = id;
	}

	@Override
	public void addChild(Entity<String> child) {
		children.add(child);
	}

	@Override
	public void removeChild(Entity<String> child) {
		children.remove(child);
	}

	@Override
	public List<Entity<String>> getChildren() {
		return children;
	}

	@Override
	public <T> T getComponent(Class<T> clazz) {
		String key = getClassKey(clazz);
		checkConsistency(key, clazz);
		SortedSet<T> components = this.components.get(key);
		if (components.size() > 0) {
			return components.first();
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
		Class clazz = component.getClass();

		String key = getClassKey(clazz);
		checkConsistency(key, clazz);
		components.get(key).add(component);

		addInterfaces(component);
		addSuperclasses(component);

		if (listeners != null) {
			for (EntityListener listener : listeners) {
				listener.componentAdded(this, component);
			}
		}
		component.setEntity(this);
		component.onAddToEntity();
	}

	private void addSuperclasses(Component component) {
		Class clazz = component.getClass().getSuperclass();

		while (clazz != null) {
			String key = getClassKey(clazz);
			checkConsistency(key, clazz);
			components.get(key).add(component);
			clazz = clazz.getSuperclass();
		}
	}

	private void addInterfaces(Component component) {
		Class clazz = component.getClass();

		while (clazz != null) {
			String key;
			for (Class interfaceClass : clazz.getInterfaces()) {
				key = getClassKey(interfaceClass);
				checkConsistency(key, interfaceClass);
				components.get(key).add(component);
			}
			clazz = clazz.getSuperclass();
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
		String key = getClassKey(clazz);
		checkConsistency(key, clazz);
		return components.get(key);
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

		String key = getClassKey(clazz);
		checkConsistency(key, clazz);
		components.get(key).remove(component);

		removeInterfaces(component);
		removeSuperclasses(component);
		removeChildClasses(component);

		if (listeners != null) {
			for (EntityListener listener : listeners) {
				listener.componentRemoved(this, component);
			}
		}
		component.setEntity(null);
	}
	
	private void removeSuperclasses(Component component) {
		Class clazz = component.getClass().getSuperclass();

		while (clazz != null) {
			String key = getClassKey(clazz);
			checkConsistency(key, clazz);
			components.get(key).remove(component);
			clazz = clazz.getSuperclass();
		}
	}

	private void removeInterfaces(Component component) {
		Class clazz = component.getClass();

		while (clazz != null) {
			String key;
			for (Class interfaceClass : clazz.getInterfaces()) {
				key = getClassKey(interfaceClass);
				checkConsistency(key, interfaceClass);
				components.get(key).remove(component);
			}
			clazz = clazz.getSuperclass();
		}
	}

	private void removeChildClasses(Component component) {
		Class componentClass = component.getClass();
		for(String key : components.keySet()) {
			try {
				Class rootClass = Class.forName(key);
				Class clazz = rootClass;
				while(clazz != null) {
					if(clazz.equals(componentClass)) {
						removeChildClasses(rootClass, component);
						break;
					}
					clazz = clazz.getSuperclass();
				}
			} catch(Exception e) {}
		}
	}
	
	private void removeChildClasses(Class clazz, Component component) {
		while(clazz != null) {
			String key = getClassKey(clazz);
			checkConsistency(key, clazz);
			components.get(key).remove(component);
			
			for (Class interfaceClass : clazz.getInterfaces()) {
				key = getClassKey(interfaceClass);
				checkConsistency(key, interfaceClass);
				components.get(key).remove(component);
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
		String key = getClassKey(clazz);
		SortedSet<T> componentsRemoved = components.remove(key);

		if (componentsRemoved == null)
			return null;

		for (T component : componentsRemoved) {
			removeInterfaces(component);
			removeSuperclasses(component);
			removeChildClasses(component);
			component.setEntity(null);
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
			components.put(key, new ConcurrentSkipListSet<T>());
		}
	}

	private String getClassKey(Class<?> clazz) {
		return clazz.getName();
	}

	@Override
	public String getEntityId() {
		return id;
	}
}
