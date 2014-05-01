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
import java.util.SortedSet;

import org.mini2Dx.ecs.component.Component;

/**
 * A common interface for Entity implementations within the
 * Entity-Component-System pattern
 * 
 * @author Thomas Cashman
 */
public interface Entity {
	/**
	 * Adds a {@link Entity} as a child of this {@link Entity}
	 * @param child The {@link Entity} to be added as a child
	 */
	public void addChild(Entity child);
	
	/**
	 * Removes a {@link Entity} as a child of this {@link Entity}
	 * @param child The {@link Entity} to be removed
	 */
	public void removeChild(Entity child);
	
	/**
	 * Returns the list of child {@link Entity} objects attached to this {@link Entity}
	 * @return An empty {@link List} if there are no children of this {@link Entity}
	 */
	public List<Entity> getChildren();
	
	/**
	 * Adds a {@link Component} to this {@link Entity} and notifies any attached
	 * {@link EntityListener}s of this addition
	 * 
	 * @param component
	 *            An instance of {@link Component}
	 */
	public void addComponent(Component component);

	/**
	 * Returns the first {@link Component} of this {@link Entity} that
	 * implements the specified class
	 * 
	 * @param clazz The {@link Class} to search for
	 * @return Null if no such {@link Component} exists
	 */
	public <T> T getComponent(Class<T> clazz);

	/**
	 * Returns all {@link Component}s that implement the specified the class or
	 * interface
	 * 
	 * @param clazz
	 *            The {@link Class} to search for
	 * @return An empty {@link List} if no such {@link Component}s are attached
	 *         to this {@link Entity}
	 */
	public <T> SortedSet<T> getComponents(Class<T> clazz);

	/**
	 * Removes the specified {@link Component} from this {@link Entity}
	 * 
	 * @param component
	 *            The {@link Component} to remove
	 */
	public void removeComponent(Component component);

	/**
	 * Removes all {@link Component}s that implement a specific type
	 * 
	 * @param clazz
	 *            The {@link Class} to search for
	 */
	public <T extends Component> SortedSet<T> removeAllComponentsOfType(
			Class<T> clazz);

	/**
	 * Adds an {@link EntityListener} to this {@link Entity}
	 * 
	 * @param listener
	 *            The {@link EntityListener} to add
	 */
	public void addEntityListener(EntityListener listener);

	/**
	 * Removes an {@link EntityListener} from this {@link Entity}
	 * 
	 * @param listener
	 *            The {@link EntityListener} to be removed
	 */
	public void removeEntityListener(EntityListener listener);
}
