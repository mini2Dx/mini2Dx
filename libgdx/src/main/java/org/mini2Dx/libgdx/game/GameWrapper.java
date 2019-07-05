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
package org.mini2Dx.libgdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.Platform;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.reflect.jvm.JvmReflection;
import org.mini2Dx.libgdx.*;
import org.mini2Dx.natives.OsInformation;

/**
 * An abstract implementation of {@link Game} for launching mini2Dx games
 */
public abstract class GameWrapper implements ApplicationListener {
	private final GameContainer gameContainer;
	private final String gameIdentifier;

	/**
	 * Constructor
	 * @param gc The {@link GameContainer} which implements the developer's game
	 */
	public GameWrapper(GameContainer gc, String gameIdentifier) {
		this.gameContainer = gc;
		this.gameIdentifier = gameIdentifier;
	}

	public abstract void initialise(String gameIdentifier);

	protected Graphics createGraphicsContext() {
		SpriteBatch spriteBatch = new SpriteBatch();
		PolygonSpriteBatch polygonSpriteBatch = new PolygonSpriteBatch();
		ShapeRenderer shapeRenderer = new ShapeRenderer();

		return new LibgdxGraphics(this, spriteBatch, polygonSpriteBatch, shapeRenderer);
	}

	@Override
	public void create() {
		initialise(gameIdentifier);

		Mdx.audio = new LibgdxAudio();
		Mdx.files = new LibgdxFiles();
		Mdx.graphics = new LibgdxGraphicsUtils();
		Mdx.graphicsContext = createGraphicsContext();
		Mdx.input = new LibgdxInput();
		Mdx.log = new LibgdxLogger();
		Mdx.platform = getPlatform();
		Mdx.reflect = new JvmReflection();

		gameContainer.start(Mdx.graphicsContext);
	}

	@Override
	public void resize(int width, int height) {
		if(gameContainer == null) {
			return;
		}
		gameContainer.resize(width, height);
	}

	@Override
	public void update(float delta) {
		if(gameContainer == null) {
			return;
		}
		gameContainer.preUpdate(delta);
		gameContainer.update(delta);
	}

	@Override
	public void interpolate(float alpha) {
		if(gameContainer == null) {
			return;
		}
		gameContainer.interpolate(alpha);
	}

	@Override
	public void render() {
		if(gameContainer == null) {
			return;
		}
		gameContainer.render();
	}

	@Override
	public void pause() {
		if(gameContainer == null) {
			return;
		}
		gameContainer.onPause();
	}

	@Override
	public void resume() {
		if(gameContainer == null) {
			return;
		}
		gameContainer.onResume();
	}

	@Override
	public void dispose() {
		if(gameContainer == null) {
			return;
		}
		gameContainer.dispose();
		Mdx.executor.dispose();
	}

	/**
	 * Returns if the game window is initialised natively
	 * @return False at startup, true once the window/game is visible to the user
	 */
	public abstract boolean isGameWindowReady();

	public Platform getPlatform() {
		switch(OsInformation.getOs()) {
		case WINDOWS:
			return Platform.WINDOWS;
		case MAC:
			return Platform.MAC;
		case ANDROID:
			return Platform.ANDROID;
		case IOS:
			return Platform.IOS;
		default:
		case UNKNOWN:
		case UNIX:
			return Platform.LINUX;
		}
	}
}