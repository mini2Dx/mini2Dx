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
package org.mini2Dx.libgdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.LibgdxSpriteBatchWrapper;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.mini2Dx.core.*;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.reflect.jvm.JvmReflection;
import org.mini2Dx.core.serialization.JsonSerializer;
import org.mini2Dx.core.serialization.XmlSerializer;
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
		LibgdxSpriteBatchWrapper spriteBatch = new LibgdxSpriteBatchWrapper();
		PolygonSpriteBatch polygonSpriteBatch = new PolygonSpriteBatch();
		ShapeRenderer shapeRenderer = new ShapeRenderer();

		return new LibgdxGraphics(this, spriteBatch, polygonSpriteBatch, shapeRenderer);
	}

	@Override
	public void create() {
		Mdx.platform = getPlatform();
		Mdx.runtime = ApiRuntime.LIBGDX;
		Mdx.gameIdentifier = gameIdentifier;
		Mdx.locks = new JvmLocks();
		Mdx.xml = new XmlSerializer();
		Mdx.json = new JsonSerializer();
		initialise(gameIdentifier);

		Mdx.audio = new LibgdxAudio();
		Mdx.executor = new LibgdxTaskExecutor(Math.max(2, Runtime.getRuntime().availableProcessors()));
		Mdx.files = new LibgdxFiles();
		Mdx.fonts = new LibgdxFonts();
		Mdx.graphics = createGraphicsUtils();
		Mdx.graphicsContext = createGraphicsContext();
		Mdx.input = new LibgdxInput();
		Mdx.log = new LibgdxLogger();
		Mdx.reflect = new JvmReflection();

		gameContainer.start(Mdx.graphicsContext);
	}

	protected GraphicsUtils createGraphicsUtils() {
		return new LibgdxGraphicsUtils();
	}

	@Override
	public void resize(int width, int height) {
		if(gameContainer == null) {
			return;
		}
		gameContainer.resize(width, height);
	}

	@Override
	public void preUpdate(float delta) {
		if(gameContainer == null) {
			return;
		}
		gameContainer.preUpdate(delta);
	}

	@Override
	public void preUpdatePhysics(float delta) {
		if(gameContainer == null) {
			return;
		}
		gameContainer.preUpdatePhysics(delta);
	}

	@Override
	public void update(float delta) {
		if(gameContainer == null) {
			return;
		}
		gameContainer.update(delta);
	}

	@Override
	public void updatePhysics(float delta) {
		if(gameContainer == null) {
			return;
		}
		gameContainer.updatePhysics(delta);
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

	public static Platform getPlatform() {
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