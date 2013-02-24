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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Base class for mini2Dx game containers. All games using mini2Dx must extend
 * this.
 * 
 * @author Thomas Cashman
 */
public abstract class GameContainer implements Screen {
	private int width, height;
	private Graphics graphics;
	private SpriteBatch spriteBatch;
	private boolean isInitialised = false;
	
	/**
	 * Initialse the game
	 */
	public abstract void initialise();

	/**
	 * Update the game
	 * @param delta The time in seconds since the last update
	 */
	public abstract void update(float delta);

	/**
	 * Render the game
	 * @param g The {@link Graphics} context available for rendering
	 */
	public abstract void render(Graphics g);
	
	/**
	 * Handle input from the player
	 * @param input The {@link Input} provided by LibGDX
	 */
	public abstract void handleInput(Input input);

	@Override
	public void render(float delta) {
		handleInput(Gdx.input);
		update(delta);
		graphics.preRender(width, height);
		render(graphics);
		graphics.postRender();
	}

	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Internal pre-initialisation code
	 */
	protected void preinit() {
		this.spriteBatch = new SpriteBatch();
		this.graphics = new Graphics(spriteBatch);
	}
	
	/**
	 * Internal post-initialisation code
	 */
	protected void postinit() {}

	@Override
	public void show() {
		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();
		
		if(!isInitialised) {
			preinit();
			initialise();
			postinit();
			isInitialised = true;
		}
	}
	
	@Override
	public void dispose() {
	}

	@Override
	public void hide() {
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
