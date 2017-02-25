/**
 * Copyright (c) 2015 See AUTHORS file
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

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.transition.NullTransition;

/**
 * A basic screen manager implementation based on Slick implementation by Kevin
 * Glass
 */
public class BasicScreenManager<T extends GameScreen> implements
		ScreenManager<T> {
	private final Map<Integer, T> gameScreens = new ConcurrentHashMap<Integer, T>();
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
