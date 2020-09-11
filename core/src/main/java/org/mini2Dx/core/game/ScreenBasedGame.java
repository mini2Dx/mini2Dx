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
package org.mini2Dx.core.game;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.screen.BasicScreenManager;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;

/**
 * An implementation of {@link GameContainer} that allows for separation of a
 * game into multiple {@link GameScreen}s and provides support for
 * {@link Transition}s between such screens via a {@link ScreenManager}
 */
public abstract class ScreenBasedGame extends GameContainer {
	private ScreenManager<GameScreen> screenManager;

	/**
	 * Returns the identifier of the {@link GameScreen} that should be shown
	 * when the game starts
	 * 
	 * @return The {@link GameScreen} identifier via {@link GameScreen}.getId()
	 */
	public abstract int getInitialScreenId();

	@Override
	public void update(float delta) {
		screenManager.update(this, delta);
	}

	@Override
	public void updatePhysics(float delta) {
		screenManager.updatePhysics(this, delta);
	}

	@Override
	public void interpolate(float alpha) {
		super.interpolate(alpha);
		screenManager.interpolate(this, alpha);
	}

	@Override
	public void render(Graphics g) {
		screenManager.render(this, g);
	}
	
	@Override
	public void onPause() {
		screenManager.onPause();
	}

	@Override
	public void onResume() {
		screenManager.onResume();
	}

	/**
	 * Add a {@link GameScreen} to this game
	 * @param screen The {@link GameScreen} to be added
	 */
	public void addScreen(GameScreen screen) {
		screen.initialise(this);
		screenManager.addGameScreen(screen);
	}
	
	/**
	 * Begins a transition to a new {@link GameScreen}
	 * @param id The id of the {@link GameScreen} to transition to
	 * @param transitionOut The outgoing {@link Transition}, e.g. fade out
	 * @param transitionIn The incoming {@link Transition}, e.g. fade in
	 */
	public void enterGameScreen(int id, Transition transitionOut,
			Transition transitionIn) {
		screenManager.enterGameScreen(id, transitionOut, transitionIn);
	}
	
	@Override
	protected void preinit(Graphics g) {
		super.preinit(g);
		screenManager = new BasicScreenManager<GameScreen>();
		addResizeListener(screenManager);
	}
	
	@Override
	protected void postinit() {
		super.postinit();
		screenManager.enterGameScreen(getInitialScreenId(), null, null);
	}

	/**
	 * Returns the {@link ScreenManager} used by this game
	 * @return
	 */
	public ScreenManager<GameScreen> getScreenManager() {
		return screenManager;
	}
}
