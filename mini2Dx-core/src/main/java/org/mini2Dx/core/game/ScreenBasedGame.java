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
package org.mini2Dx.core.game;

import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;

import com.badlogic.gdx.Input;

/**
 * An implementation of {@link GameContainer} that allows for separation of a
 * game into multiple {@link GameScreen}s and provides support for
 * {@link Transition}s between such screens via a {@link ScreenManager}
 * 
 * @author Thomas Cashman
 */
public abstract class ScreenBasedGame extends GameContainer {
	private ScreenManager screenManager;

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
	public void render(Graphics g) {
		screenManager.render(this, g);
	}
	
	@Override
	public void handleInput(Input input) {
		screenManager.handleInput(input);
	}

	/**
	 * Add a {@link GameScreen} to this game
	 * @param screen The {@link GameScreen} to be added
	 */
	public void addScreen(GameScreen screen) {
		screenManager.addGameScreen(screen);
	}
	
	@Override
	protected void preinit() {
		super.preinit();
		screenManager = new ScreenManager();
	}
	
	@Override
	protected void postinit() {
		super.postinit();
		screenManager.enterGameScreen(getInitialScreenId(), null, null);
	}
}
