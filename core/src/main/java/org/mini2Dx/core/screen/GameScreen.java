/*******************************************************************************
 * Copyright 2019 See AUTHORS file
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.mini2Dx.core.screen;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.game.GameContainer;

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
	 * Updates the game physics on the screen
	 * @param gc The {@link GameContainer} of the game
	 * @param delta The time in seconds to advance physics by
	 */
	public default void updatePhysics(GameContainer gc, float delta) {
	}

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
	 * Called when the game window's dimensions changes. 
	 * On mobile devices this is called when the screen is rotated.
	 * 
	 * @param width The new game window width
	 * @param height The new game window height
	 */
	public void onResize(int width, int height);
	
	/**
	 * Called when the game window is no longer active or visible.
	 * On
	 */
	public void onPause();

	/**
	 * Called when the game window becomes active or visible again
	 */
	public void onResume();
	
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
