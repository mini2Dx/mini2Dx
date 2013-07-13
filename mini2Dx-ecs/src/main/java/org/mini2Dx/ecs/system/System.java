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
package org.mini2Dx.ecs.system;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.ecs.entity.Entity;
import org.mini2Dx.ecs.entity.UUIDEntity;

/**
 * A common interface for implementing {@link System}s as part of the Entity-Component-System pattern
 * @author Thomas Cashman
 */
public interface System<T extends Entity> {
	/**
	 * Adds an {@link Entity} to the {@link System}
	 * @param entity The {@link Entity} to be added
	 */
	public void addEntity(T entity);
	
	/**
	 * Removes an {@link Entity} from the {@link System}
	 * @param entity The {@link UUIDEntity} to be removed
	 */
	public void removeEntity(T entity);
	
	/**
	 * Update the {@link System}
	 * @param gc The {@link GameContainer} calling update
	 * @param delta The time in seconds since the last update
	 */
	public void update(GameContainer gc, float delta);
	
	/**
	 * Interpolate the {@link System}
	 * @param gc The {@link GameContainer} calling interpolate
	 * @param alpha The alpha value to use during interpolation
	 */
	public void interpolate(GameContainer gc, float alpha);
	
	/**
	 * Render the {@link System}
	 * @param gc The {@link GameContainer} calling render
	 * @param g The {@link Graphics} instance
	 */
	public void render(GameContainer gc, Graphics g);
	
	/**
	 * Returns if this {@link System} is debugging
	 * @return True if debugging
	 */
	public boolean isDebugging();
	
	/**
	 * Sets whether or not this {@link System} is in debug mode
	 * @param debugging True if debugging
	 */
	public void setDebugging(boolean debugging);
}
