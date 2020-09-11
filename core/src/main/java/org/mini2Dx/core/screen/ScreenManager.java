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
import org.mini2Dx.core.game.GameResizeListener;

import java.util.Iterator;

/**
 * Updates and renders {@link GameScreen}s and {@link Transition}s between them
 */
public interface ScreenManager<T extends GameScreen> extends GameResizeListener {
	/**
	 * Updates the current {@link GameScreen} and any {@link Transition} that
	 * may be occurring
	 * 
	 * @param gc
	 *            The {@link GameContainer} that is updating this
	 *            {@link ScreenManager}
	 * @param delta
	 *            The time since the last update
	 */
	public void update(GameContainer gc, float delta);

	/**
	 * Updates the physics on the current game screen
	 *
	 * @param gc
	 *            The {@link GameContainer} that is updating this
	 *            {@link ScreenManager}
	 * @param delta
	 *            The time to advance physics by
	 */
	public void updatePhysics(GameContainer gc, float delta);
	
	/**
	 * Interpolate between the previous and current state
	 * 
	 * @param alpha
	 *            The interpolation alpha value
	 */
	public void interpolate(GameContainer gc, float alpha);
	
	/**
	 * Renders the current {@link GameScreen} and any {@link Transition} that
	 * may be occurring
	 * 
	 * @param gc
	 *            The {@link GameContainer} that is rendering this
	 *            {@link ScreenManager}
	 * @param g
	 *            The {@link Graphics} context available for rendering
	 */
	public void render(GameContainer gc, Graphics g);
	
	public void onPause();

	public void onResume();
	
	/**
	 * Begins a transition to a new {@link GameScreen}
	 * 
	 * @param id
	 *            The id of the {@link GameScreen} to transition to
	 * @param transitionOut
	 *            The outgoing {@link Transition}, e.g. fade out
	 * @param transitionIn
	 *            The incoming {@link Transition}, e.g. fade in
	 */
	public void enterGameScreen(int id, Transition transitionOut,
			Transition transitionIn);
	
	/**
	 * Adds a {@link GameScreen} to this manager
	 * 
	 * @param screen
	 *            The {@link GameScreen} to be added
	 */
	public void addGameScreen(T screen);
	
	/**
	 * Returns the {@link GameScreen} with the given id
	 * 
	 * @param id
	 *            The id to search for
	 * @return Null if there is no such {@link GameScreen} registered with this
	 *         manager
	 */
	public T getGameScreen(int id);
	
	/**
	 * Returns if the {@link ScreenManager} is moving between {@link GameScreen}s
	 * 
	 * @return False if there are no {@link Transition}s active
	 */
	public boolean isTransitioning();
	
	/**
	 * Returns an {@link Iterator} over all the game screens
	 * @return
	 */
	public Iterator<T> getGameScreens();
}
