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
package org.mini2Dx.core.screen;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;

/**
 * A common interface to game screen implementations
 */
public interface GameScreen {
	/**
	 * Initialises the game screen
	 * @param gc The {@link GameContainer} of the game
	 */
	public void initialise(GameContainer gc);
	
	/**
	 * Updates the game screen
	 * @param gc The {@link GameContainer} of the game
	 * @param screenManager The {@link ScreenManager} of the game
	 * @param delta The time in seconds since the last update
	 */
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta);
	
	/**
	 * Interpolate between the previous and current state
	 * @param alpha The interpolation alpha value
	 */
	public void interpolate(GameContainer gc, float alpha);
	
	/**
	 * Renders the game screen
	 * @param gc The {@link GameContainer} of the game
	 * @param g The {@link Graphics} context available for rendering
	 */
	public void render(GameContainer gc, Graphics g);
	
	/**
	 * Called before the transition in
	 * @param transitionIn The {@link Transition} in to this screen
	 */
	public void preTransitionIn(Transition transitionIn);
	
	/**
	 * Called after the transition in
	 * @param transitionIn The {@link Transition} in to this screen
	 */
	public void postTransitionIn(Transition transitionIn);
	
	/**
	 * Called before the transition out
	 * @param transitionOut The {@link Transition} out from this screen
	 */
	public void preTransitionOut(Transition transitionOut);
	
	/**
	 * Called after the transition out
	 * @param transitionOut The {@link Transition} out from this screen
	 */
	public void postTransitionOut(Transition transitionOut);
	
	/**
	 * Returns the identifier of the screen
	 * @return A unique identifier
	 */
	public int getId();
}
