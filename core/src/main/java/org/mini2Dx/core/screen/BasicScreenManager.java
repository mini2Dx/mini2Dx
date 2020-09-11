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
import org.mini2Dx.core.screen.transition.NullTransition;
import org.mini2Dx.gdx.utils.IntMap;

import java.util.Iterator;

/**
 * A basic screen manager implementation based on Slick implementation by Kevin
 * Glass
 */
public class BasicScreenManager<T extends GameScreen> implements
		ScreenManager<T> {
	private final IntMap<T> gameScreens = new IntMap<T>();
	protected T currentScreen, nextScreen;
	protected Transition transitionIn, transitionOut;

	@Override
	public void update(GameContainer gc, float delta) {
		if (transitionOut != null) {
			transitionOut.update(gc, delta);
			if (transitionOut.isFinished()) {
				if (currentScreen != null) {
					currentScreen.postTransitionOut(transitionOut);
				}
				GameScreen oldScreen = currentScreen;
				currentScreen = nextScreen;
				nextScreen = null;
				transitionOut = null;

				if (transitionIn != null) {
					currentScreen.preTransitionIn(transitionIn);
				}
			} else {
				return;
			}
		}

		if (transitionIn != null) {
			transitionIn.update(gc, delta);
			if (transitionIn.isFinished()) {
				currentScreen.postTransitionIn(transitionIn);
				transitionIn = null;
			} else {
				return;
			}
		}

		currentScreen.update(gc, this, delta);
	}

	@Override
	public void updatePhysics(GameContainer gc, float delta) {
		if(currentScreen == null) {
			return;
		}
		currentScreen.updatePhysics(gc, delta);
	}

	@Override
	public void interpolate(GameContainer gc, float alpha) {
		if (currentScreen != null) {
			currentScreen.interpolate(gc, alpha);
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		if (transitionOut != null) {
			transitionOut.preRender(gc, g);
		} else if (transitionIn != null) {
			transitionIn.preRender(gc, g);
		}

		if (currentScreen != null) {
			currentScreen.render(gc, g);
		}

		if (transitionOut != null) {
			transitionOut.postRender(gc, g);
		} else if (transitionIn != null) {
			transitionIn.postRender(gc, g);
		}
	}

	@Override
	public void enterGameScreen(int id, Transition transitionOut,
			Transition transitionIn) {
		if (transitionOut == null) {
			transitionOut = new NullTransition();
		}
		if (transitionIn == null) {
			transitionIn = new NullTransition();
		}
		this.transitionIn = transitionIn;
		this.transitionOut = transitionOut;

		this.nextScreen = gameScreens.get(id);
		
		this.transitionIn.initialise(currentScreen, nextScreen);
		this.transitionOut.initialise(currentScreen, nextScreen);

		if (currentScreen != null) {
			currentScreen.preTransitionOut(transitionOut);
		}
	}

	@Override
	public void addGameScreen(T screen) {
		this.gameScreens.put(screen.getId(), screen);
	}

	@Override
	public T getGameScreen(int id) {
		return this.gameScreens.get(id);
	}

	@Override
	public boolean isTransitioning() {
		return transitionIn != null || transitionOut != null;
	}

	@Override
	public void onResize(int width, int height) {
		if(currentScreen == null) {
			return;
		}
		currentScreen.onResize(width, height);
	}
	
	@Override
	public void onPause() {
		if(currentScreen == null) {
			return;
		}
		currentScreen.onPause();
	}

	@Override
	public void onResume() {
		if(currentScreen == null) {
			return;
		}
		currentScreen.onResume();
	}

	@Override
	public Iterator<T> getGameScreens() {
		return gameScreens.values().iterator();
	}
}
