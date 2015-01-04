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
package org.mini2Dx.ecs.component;

import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import org.mini2Dx.ecs.entity.Entity;

/**
 * An implementation of {@link Component} that can be sorted by a priority
 * value.
 * 
 * The main benefit of this is when using {@link Component#getComponents(Class)}
 * that the results will be returned sorted by their priority.
 */
public class PriorityComponent extends Component {
	private int priority;
	
	public PriorityComponent(String name, int priority) {
		super(name);
		this.priority = priority;
	}

	public PriorityComponent(int priority) {
		this(String.valueOf(priority), priority);
	}

	public <T extends PriorityComponent> T getComponent(int priority,
			Class<T> clazz) {
		if (getEntity() == null)
			return null;
		SortedSet<T> components = getEntity().getComponents(clazz);
		Iterator<T> iterator = components.iterator();
		while (iterator.hasNext()) {
			T component = iterator.next();
			if (component.getPriority() == priority) {
				return component;
			}
		}
		return null;
	}

	private <T extends PriorityComponent> T getComponentInEntityDescendants(
			Entity entity, int priority, Class<T> clazz) {
		List<Entity> childEntities = entity.getChildren();
		for (int i = 0; i < childEntities.size(); i++) {
			SortedSet<T> components = childEntities.get(i).getComponents(clazz);
			Iterator<T> iterator = components.iterator();
			while (iterator.hasNext()) {
				T component = iterator.next();
				if (component != null && component.getPriority() == priority) {
					return component;
				}
			}
		}
		for (int i = 0; i < childEntities.size(); i++) {
			T result = getComponentInEntityDescendants(childEntities.get(i),
					priority, clazz);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	public <T extends PriorityComponent> T getComponentInDescendants(
			int priority, Class<T> clazz) {
		if (getEntity() == null)
			return null;
		return getComponentInEntityDescendants(getEntity(), priority, clazz);
	}

	public int getPriority() {
		return priority;
	}
	
	public int compareTo(PriorityComponent o) {
		int result = Integer.valueOf(o.getPriority()).compareTo(Integer.valueOf(getPriority()));
		if(result == 0)  {
			return o.getName().compareTo(getName());
		}
		return result;
	}
	
	@Override
	public int compareTo(Component o) {
		if(o instanceof PriorityComponent) {
			return compareTo((PriorityComponent) o);
		}
		return o.getName().compareTo(getName());
	}
}
