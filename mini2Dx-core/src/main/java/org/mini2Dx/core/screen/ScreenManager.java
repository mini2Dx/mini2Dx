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
package org.mini2Dx.core.screen;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.transition.NullTransition;

/**
 * Updates and renders {@link GameScreen}s and {@link Transition}s between them
 * 
 * NOTE: Based on Slick implementation by Kevin Glass
 * 
 * @author Thomas Cashman
 */
public class ScreenManager<T extends GameScreen> {
	private Map<Integer, T> gameScreens;
	protected T currentScreen, nextScreen;
	protected Transition transitionIn, transitionOut;

	/**
	 * Constructor
	 */
	public ScreenManager() {
		gameScreens = new ConcurrentHashMap<Integer, T>();
	}

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
	public void update(GameContainer gc, float delta) {
		if (transitionOut != null) {
			transitionOut.update(gc, delta);
			if (transitionOut.isFinished()) {
				GameScreen oldScreen = currentScreen;
				currentScreen = nextScreen;
				nextScreen = null;
				transitionOut = null;

				if (transitionIn != null) {
					transitionIn.initialise(oldScreen, currentScreen);
				}
			} else {
				return;
			}
		}

		if (transitionIn != null) {
			transitionIn.update(gc, delta);
			if (transitionIn.isFinished()) {
				transitionIn = null;
			} else {
				return;
			}
		}

		currentScreen.update(gc, this, delta);
	}
	
	/**
	 * Interpolate between the previous and current state
	 * @param alpha The interpolation alpha value
	 */
	public void interpolate(GameContainer gc, float alpha) {
		if (currentScreen != null) {
			currentScreen.interpolate(gc, alpha);
		}
	}

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
	}

	/**
	 * Adds a {@link GameScreen} to this manager
	 * 
	 * @param screen
	 *            The {@link GameScreen} to be added
	 */
	public void addGameScreen(T screen) {
		this.gameScreens.put(screen.getId(), screen);
	}

	/**
	 * Returns if the {@link ScreenManager} is moving between {@link Screen}s
	 * 
	 * @return False if there are no {@link Transition}s active
	 */
	public boolean isTransitioning() {
		return transitionIn != null || transitionOut != null;
	}
}
