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
package org.mini2Dx.ecs.component.screen;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.ecs.component.Component;
import org.mini2Dx.ecs.entity.GameScreenEntity;

/**
 * A common interface for screen component implementations
 * @author Thomas Cashman
 */
public interface ScreenComponent extends Component {
	/**
	 * Update the screen component
	 * @param game The {@link GameContainer} calling the update
	 * @param delta The time since the last update in seconds
	 */
	public void update(GameContainer gc,
			ScreenManager<? extends GameScreen> screenManager, GameScreenEntity owner, float delta);
	
	/**
	 * Interpolate the screen component
	 * @param game The {@link GameContainer} calling the interpolation
	 * @param alpha The alpha value to use during interpolation
	 */
	public void interpolate(GameContainer gc, GameScreenEntity owner, float alpha);
	
	/**
	 * Render the screen component
	 * @param game The {@link GameContainer} calling the render
	 * @param g The {@link Graphics} context
	 */
	public void render(GameContainer gc, GameScreenEntity owner, Graphics g);
}
