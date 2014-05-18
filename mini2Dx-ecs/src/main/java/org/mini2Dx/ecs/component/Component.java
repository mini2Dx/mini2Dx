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
import java.util.SortedSet;
import java.util.TreeSet;

import org.mini2Dx.ecs.entity.Entity;

/**
 * A common interface for implementing {@link Component}s as part of the Entity-Component-System pattern
 * @author Thomas Cashman
 */
public class Component implements Comparable<Component> {
	private String name;
	private Entity entity;
	
	/**
	 * Default constructor
	 * @param name The name of this component
	 */
	public Component(String name) {
		this.name = name;
	}
	
	/**
	 * Called when this {@link Component} is added to an {@link Entity}
	 * 
	 * By default this does nothing, can be overridden by developer
	 */
	public void onAddToEntity() {
		
	}

	public <T extends Component> T getComponent(Class<T> clazz) {
		if(entity == null)
			return null;
		return (T) entity.getComponent(clazz);
	}
	
	public <T extends Component> T getComponent(String name, Class<T> clazz) {
		if(entity == null)
			return null;
		SortedSet<T> components = entity.getComponents(clazz);
		Iterator<T> iterator = components.iterator();
		while(iterator.hasNext()) {
			T component = iterator.next();
			if(component.getName().equals(name)) {
				return component;
			}
		}
		return null;
	}
	
	public <T extends Component> SortedSet<T> getComponents(Class<T> clazz) {
		if(entity == null)
			return new TreeSet<T>();
		return entity.getComponents(clazz);
	}
	
	private <T extends Component> T getComponentInEntityDescendants(Entity entity, Class<T> clazz) {
		List<Entity> childEntities = entity.getChildren();
		for(int i = 0; i < childEntities.size(); i++) {
			T result = (T) childEntities.get(i).getComponent(clazz);
			if(result != null) {
				return result;
			}
		}
		for(int i = 0; i < childEntities.size(); i++) {
			T result = getComponentInEntityDescendants(childEntities.get(i), clazz);
			if(result != null) {
				return result;
			}
		}
		return null;
	}
	
	public <T extends Component> T getComponentInDescendants(Class<T> clazz) {
		if(entity == null)
			return null;
		return getComponentInEntityDescendants(entity, clazz);
	}
	
	private <T extends Component> T getComponentInEntityDescendants(Entity entity, String name, Class<T> clazz) {
		List<Entity> childEntities = entity.getChildren();
		for(int i = 0; i < childEntities.size(); i++) {
			SortedSet<T> components = childEntities.get(i).getComponents(clazz);
			Iterator<T> iterator = components.iterator();
			while(iterator.hasNext()) {
				T component = iterator.next();
				if(component != null && component.getName().equals(name)) {
					return component;
				}
			}
		}
		for(int i = 0; i < childEntities.size(); i++) {
			T result = getComponentInEntityDescendants(childEntities.get(i), name, clazz);
			if(result != null) {
				return result;
			}
		}
		return null;
	}
	
	public <T extends Component> T getComponentInDescendants(String name, Class<T> clazz) {
		if(entity == null)
			return null;
		return getComponentInEntityDescendants(entity, name, clazz);
	}
	
	private <T extends Component> void getComponentsInEntityDescendants(SortedSet<T> result, Entity entity, Class<T> clazz) {
		List<Entity> childEntities = entity.getChildren();
		for(int i = 0; i < childEntities.size(); i++) {
			SortedSet<T> components = childEntities.get(i).getComponents(clazz);
			if(components != null && components.size() > 0) {
				result.addAll(components);
			}
		}
		for(int i = 0; i < childEntities.size(); i++) {
			getComponentsInEntityDescendants(result, childEntities.get(i), clazz);
		}
	}
	
	public <T extends Component> SortedSet<T> getComponentsInDescendants(Class<T> clazz) {
		SortedSet<T> result = new TreeSet<T>();
		if(entity != null) {
			getComponentsInEntityDescendants(result, entity, clazz);
		}
		return result;
	}
	
	public void destroy() {
		if(entity == null)
			return;
		entity.removeComponent(this);
	}
	
	public String getName() {
		return name;
	}
	
	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	@Override
	public int compareTo(Component o) {
		return name.compareTo(o.name);
	}
}
