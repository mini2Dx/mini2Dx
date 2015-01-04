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
package org.mini2Dx.ecs.game;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.ecs.system.GameSystem;

/**
 * An implementation of {@link GameContainer} based on the
 * entity-component-system pattern
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class EntityComponentSystemGame extends GameContainer {
	private List<GameSystem> systems;

	@Override
	protected void preinit() {
		super.preinit();
		systems = new CopyOnWriteArrayList<GameSystem>();
	}
	
	@Override
	protected void postinit() {
		for (GameSystem system : systems) {
			system.initialise(this);
		}
	}

	@Override
	public void update(float delta) {
		for (GameSystem system : systems) {
			system.update(this, delta);
		}
	}

	@Override
	public void interpolate(float alpha) {
		for (GameSystem system : systems) {
			system.interpolate(this, alpha);
		}
	}

	@Override
	public void render(Graphics g) {
		for (GameSystem system : systems) {
			system.render(this, g);
		}
	}

	/**
	 * Adds a {@link GameSystem} to be handled by this
	 * {@link EntityComponentSystemGame}
	 * 
	 * @param system
	 *            An implementation of {@link GameSystem} to be added
	 */
	public void addSystem(GameSystem system) {
		systems.add(system);
	}

	/**
	 * Removes a {@link GameSystem} from this {@link EntityComponentSystemGame}
	 * 
	 * @param system
	 *            An implementation of {@link GameSystem} to be removed
	 */
	public void removeSystem(GameSystem system) {
		systems.remove(system);
	}
}
