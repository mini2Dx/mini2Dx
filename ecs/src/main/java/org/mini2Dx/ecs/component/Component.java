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

import java.util.SortedSet;
import java.util.TreeSet;

import org.mini2Dx.ecs.entity.Entity;

/**
 * A common interface for implementing {@link Component}s as part of the Entity-Component-System pattern
 * @author Thomas Cashman
 */
@SuppressWarnings("unchecked")
public class Component implements Comparable<Component> {
	private int componentTypeId;
	private String name;
	private Entity entity;
	
	/**
	 * Default constructor
	 * @param name The name of this component
	 */
	public Component(String name) {
		this.name = name;
		componentTypeId = ComponentTypeIdAllocator.getId(getClass());
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
	
	public <T extends Component> T getComponent(int componentTypeId) {
		if(entity == null)
			return null;
		return (T) entity.getComponent(componentTypeId);
	}
	
	public <T extends Component> T getComponent(String name, Class<T> clazz) {
		if(entity == null)
			return null;
		return entity.getComponent(name, clazz);
	}
	
	public <T extends Component> T getComponent(String name, int componentTypeId) {
		if(entity == null)
			return null;
		return entity.getComponent(name, componentTypeId);
	}
	
	public <T extends Component> SortedSet<T> getComponents(Class<T> clazz) {
		if(entity == null)
			return new TreeSet<T>();
		return entity.getComponents(clazz);
	}
	
	public <T extends Component> SortedSet<T> getComponents(int componentTypeId) {
		if(entity == null)
			return new TreeSet<T>();
		return entity.getComponents(componentTypeId);
	}
	
	public <T extends Component> T getComponentInDescendants(Class<T> clazz) {
		return entity.getComponentInDescendants(clazz);
	}

	public <T extends Component> T getComponentInDescendants(int componentTypeId) {
		return entity.getComponentInDescendants(componentTypeId);
	}
	
	
	public <T extends Component> T getComponentInDescendants(String name, Class<T> clazz) {
		return entity.getComponentInDescendants(name, clazz);
	}
	
	public <T extends Component> T getComponentInDescendants(String name, int componentTypeId) {
		return entity.getComponentInDescendants(name, componentTypeId);
	}
	
	public <T extends Component> SortedSet<T> getComponentsInDescendants(Class<T> clazz) {
		return entity.getComponentsInDescendants(clazz);
	}
	
	public <T extends Component> SortedSet<T> getComponentsInDescendants(int componentTypeId) {
		return entity.getComponentsInDescendants(componentTypeId);
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

	public int getComponentTypeId() {
		return componentTypeId;
	}
}
