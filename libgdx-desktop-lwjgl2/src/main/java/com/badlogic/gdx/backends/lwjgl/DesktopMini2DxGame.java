/*******************************************************************************
 * Copyright 2011 See LIBGDX_AUTHORS file.
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
package com.badlogic.gdx.backends.lwjgl;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl.audio.Mini2DxOpenALAudio;
import com.badlogic.gdx.utils.*;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.libgdx.desktop.Lwjgl2GameWrapper;
import org.mini2Dx.libgdx.desktop.Lwjgl2Mini2DxConfig;
import org.mini2Dx.libgdx.game.ApplicationListener;
import org.mini2Dx.libgdx.game.GameWrapper;

import java.awt.*;

/**
 * Launches desktop-based mini2Dx games. Based on <a href=
 * "https://github.com/libgdx/libgdx/blob/master/backends/gdx-backend-lwjgl/src/com/badlogic/gdx/backends/lwjgl/LwjglApplication.java">
 * LibGDX's LwjglApplication class</a>
 */
public class DesktopMini2DxGame implements Application {
	protected final Lwjgl2Mini2DxConfig config;
	protected final LwjglGraphics graphics;
	protected Mini2DxOpenALAudio audio;
	protected final LwjglFiles files;
	protected final LwjglInput input;
	protected final LwjglNet net;
	protected final GameWrapper listener;
	protected Thread mainLoopThread;
	protected boolean running = true;
	protected final Array<Runnable> runnables = new Array<Runnable>(Runnable.class);
	protected final Array<Runnable> executedRunnables = new Array<Runnable>(Runnable.class);
	protected final SnapshotArray<LifecycleListener> lifecycleListeners = new SnapshotArray<LifecycleListener>(LifecycleListener.class);
	protected int logLevel = LOG_INFO;
	protected ApplicationLogger applicationLogger;
	protected String preferencesdir;

	private int lastFrameDropWarning = -1;

	public DesktopMini2DxGame(GameContainer game, Lwjgl2Mini2DxConfig config) {
		this(game, config, new LwjglGraphics(config));
	}

	public DesktopMini2DxGame(GameContainer game, Lwjgl2Mini2DxConfig config, Canvas canvas) {
		this(game, config, new LwjglGraphics(canvas, config));
	}

	public DesktopMini2DxGame(GameContainer game, Lwjgl2Mini2DxConfig config, LwjglGraphics graphics) {
		LwjglNativesLoader.load();
		setApplicationLogger(new LwjglApplicationLogger());
		
		this.config = config;
		listener = new Lwjgl2GameWrapper(game, config.gameIdentifier);

		if (config.title == null) {
			config.title = game.getClass().getSimpleName();
		}

		this.graphics = graphics;
		this.files = new LwjglFiles();
		this.input = new LwjglInput();
		this.net = new LwjglNet(config);

		initialiseLibGDX();
		launchGame();
	}

	protected void initialiseLibGDX() {
		if (!LwjglApplicationConfiguration.disableAudio) {
			try {
				audio = new Mini2DxOpenALAudio(config.audioDeviceSimultaneousSources, config.audioDeviceBufferCount,
						config.audioDeviceBufferSize);
			} catch (Throwable t) {
				log("LwjglApplication", "Couldn't initialize audio, disabling audio", t);
				LwjglApplicationConfiguration.disableAudio = true;
			}
		}
		preferencesdir = config.preferencesDirectory;

		Gdx.app = this;
		Gdx.graphics = graphics;
		Gdx.audio = audio;
		Gdx.files = files;
		Gdx.input = input;
		Gdx.net = net;
	}

	private void launchGame() {
		mainLoopThread = new Thread("LWJGL Application") {
			@Override
			public void run() {
				graphics.setVSync(graphics.config.vSyncEnabled);
				try {
					DesktopMini2DxGame.this.executeGame();
				} catch (Throwable t) {
					if (audio != null)
						audio.dispose();
					Gdx.input.setCursorCatched(false);
					if (t instanceof RuntimeException)
						throw (RuntimeException) t;
					else
						throw new GdxRuntimeException(t);
				}
			}
		};
		mainLoopThread.start();
	}

	void executeGame() {
		SnapshotArray<LifecycleListener> lifecycleListeners = this.lifecycleListeners;

		try {
			graphics.setupDisplay();
		} catch (LWJGLException e) {
			throw new GdxRuntimeException(e);
		}

		listener.create();
		graphics.resize = true;

		int lastWidth = graphics.getWidth();
		int lastHeight = graphics.getHeight();

		graphics.lastTime = System.nanoTime();

		float maximumDeltaSeconds = config.maximumTimestepSeconds();
		float accumulator = 0f;
		float targetTimestepSeconds = config.targetTimestepSeconds();

		boolean wasPaused = false;
		while (running) {
			Display.processMessages();
			if (Display.isCloseRequested()) {
				exit();
			}

			boolean isMinimized = config.pauseWhenMinimized && !Display.isVisible();
			boolean isBackground = !Display.isActive();
			boolean paused = isMinimized || (isBackground && config.pauseWhenBackground);
			if (!wasPaused && paused) { // just been minimized
				wasPaused = true;
				synchronized (lifecycleListeners) {
					LifecycleListener[] listeners = lifecycleListeners.begin();
					for (int i = 0, n = lifecycleListeners.size; i < n; ++i)
						listeners[i].pause();
					lifecycleListeners.end();
				}
				listener.pause();
			}
			if (wasPaused && !paused) { // just been restore from being minimized
				wasPaused = false;
				synchronized (lifecycleListeners) {
					LifecycleListener[] listeners = lifecycleListeners.begin();
					for (int i = 0, n = lifecycleListeners.size; i < n; ++i)
						listeners[i].resume();
					lifecycleListeners.end();
				}
				listener.resume();
			}

			boolean shouldRender = false;

			if (graphics.canvas != null) {
				int width = graphics.canvas.getWidth();
				int height = graphics.canvas.getHeight();
				if (lastWidth != width || lastHeight != height) {
					lastWidth = width;
					lastHeight = height;
					Gdx.gl.glViewport(0, 0, lastWidth, lastHeight);
					listener.resize(lastWidth, lastHeight);
					shouldRender = true;
				}
			} else {
				graphics.config.x = Display.getX();
				graphics.config.y = Display.getY();
				if (graphics.resize || Display.wasResized()
						|| (int)(Display.getWidth() * Display.getPixelScaleFactor()) != graphics.config.width
						|| (int)(Display.getHeight() * Display.getPixelScaleFactor()) != graphics.config.height) {
					graphics.resize = false;
					graphics.config.width = (int)(Display.getWidth() * Display.getPixelScaleFactor());
					graphics.config.height = (int)(Display.getHeight() * Display.getPixelScaleFactor());
					Gdx.gl.glViewport(0, 0, graphics.config.width, graphics.config.height);
					if (listener != null) listener.resize(graphics.config.width, graphics.config.height);
					shouldRender = true;
				}
			}

			if (executeRunnables()) {
				shouldRender = true;
			}

			// If one of the runnables set running to false, for example after
			// an exit().
			if (!running) {
				break;
			}

			if (graphics.shouldRender()) shouldRender = true;
			if (audio != null) {
				audio.update();
			}

			if (isMinimized)
				shouldRender = false;
			else if (isBackground && graphics.config.backgroundFPS == -1) //
				shouldRender = false;

			int frameRate = isBackground ? graphics.config.backgroundFPS : graphics.config.foregroundFPS;
			if (shouldRender) {
				graphics.updateTime();
				Mdx.platformUtils.markFrame();
				graphics.frameId++;

				input.update();
				input.processEvents();

				final float delta = graphics.getDeltaTime();

				switch(Mdx.timestepMode) {
				case DEFAULT:
					Mdx.platformUtils.markUpdateBegin();
					listener.preUpdate(delta);
					listener.preUpdatePhysics(targetTimestepSeconds);
					listener.updatePhysics(targetTimestepSeconds);
					listener.update(delta);
					Mdx.platformUtils.markUpdateEnd();

					listener.interpolate(1f);
					break;
				case PHYSICS:
					float physicsDelta = graphics.getDeltaTime();
					if (physicsDelta > maximumDeltaSeconds) {
						physicsDelta = maximumDeltaSeconds;
					}

					accumulator += physicsDelta;

					Mdx.platformUtils.markUpdateBegin();
					listener.preUpdate(delta);
					while (accumulator >= targetTimestepSeconds) {
						listener.preUpdatePhysics(targetTimestepSeconds);
						listener.updatePhysics(targetTimestepSeconds);

						accumulator -= targetTimestepSeconds;
					}
					listener.update(delta);
					Mdx.platformUtils.markUpdateEnd();

					listener.interpolate(accumulator / targetTimestepSeconds);
					break;
				}

				Mdx.platformUtils.markRenderBegin();
				listener.render();
				Mdx.platformUtils.markRenderEnd();
				Display.update(false);

				if(config.errorOnFrameDrop) {
					if(Mdx.platformUtils.getUpdatesPerSecond() < config.targetFPS) {
						if(lastFrameDropWarning != Mdx.platformUtils.getUpdatesPerSecond()) {
							lastFrameDropWarning = Mdx.platformUtils.getUpdatesPerSecond();
							Mdx.log.error("mini2Dx", "WARN: " + (config.targetFPS - Mdx.platformUtils.getUpdatesPerSecond()) + " frames dropped.");
						}
					} else {
						lastFrameDropWarning = -1;
					}
				}
			} else {
				// Sleeps to avoid wasting CPU in an empty loop.
				if (frameRate == -1) {
					frameRate = 10;
				}
				if (frameRate == 0) {
					frameRate = graphics.config.backgroundFPS;
				}
				if (frameRate == 0) {
					frameRate = 30;
				}
			}
			if (frameRate > 0) {
				Display.sync(frameRate);
			}
		}

		synchronized (lifecycleListeners) {
			for (LifecycleListener listener : lifecycleListeners) {
				listener.pause();
				listener.dispose();
			}
		}
		listener.pause();
		listener.dispose();
		Display.destroy();
		if (audio != null) {
			audio.dispose();
		}
		if (graphics.config.forceExit) {
			System.exit(-1);
		}
	}

	public boolean executeRunnables() {
		synchronized (runnables) {
			for (int i = runnables.size - 1; i >= 0; i--) {
				executedRunnables.add(runnables.get(i));
			}
			runnables.clear();
		}
		if (executedRunnables.size == 0) {
			return false;
		}
		do {
			executedRunnables.pop().run();
		} while (executedRunnables.size > 0);
		return true;
	}

	@Override
	public ApplicationListener getApplicationListener() {
		return listener;
	}

	@Override
	public Audio getAudio() {
		return audio;
	}

	@Override
	public Files getFiles() {
		return files;
	}

	@Override
	public LwjglGraphics getGraphics() {
		return graphics;
	}

	@Override
	public Input getInput() {
		return input;
	}

	@Override
	public Net getNet() {
		return net;
	}

	@Override
	public ApplicationType getType() {
		return ApplicationType.Desktop;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	public void stop() {
		running = false;
		try {
			mainLoopThread.join();
		} catch (Exception ex) {
		}
	}

	@Override
	public long getJavaHeap() {
		return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	}

	@Override
	public long getNativeHeap() {
		return getJavaHeap();
	}

	ObjectMap<String, Preferences> preferences = new ObjectMap<String, Preferences>();

	@Override
	public Preferences getPreferences(String name) {
		if (preferences.containsKey(name)) {
			return preferences.get(name);
		} else {
			Preferences prefs = new LwjglPreferences(name, this.preferencesdir);
			preferences.put(name, prefs);
			return prefs;
		}
	}

	@Override
	public Clipboard getClipboard() {
		return new LwjglClipboard();
	}

	@Override
	public void postRunnable(Runnable runnable) {
		synchronized (runnables) {
			runnables.add(runnable);
			Gdx.graphics.requestRendering();
		}
	}

	@Override
	public void debug(String tag, String message) {
		if (logLevel >= LOG_DEBUG) {
			System.out.println(tag + ": " + message);
		}
	}

	@Override
	public void debug(String tag, String message, Throwable exception) {
		if (logLevel >= LOG_DEBUG) {
			System.out.println(tag + ": " + message);
			exception.printStackTrace(System.out);
		}
	}

	@Override
	public void log(String tag, String message) {
		if (logLevel >= LOG_INFO) {
			System.out.println(tag + ": " + message);
		}
	}

	@Override
	public void log(String tag, String message, Throwable exception) {
		if (logLevel >= LOG_INFO) {
			System.out.println(tag + ": " + message);
			exception.printStackTrace(System.out);
		}
	}

	@Override
	public void error(String tag, String message) {
		if (logLevel >= LOG_ERROR) {
			System.err.println(tag + ": " + message);
		}
	}

	@Override
	public void error(String tag, String message, Throwable exception) {
		if (logLevel >= LOG_ERROR) {
			System.err.println(tag + ": " + message);
			exception.printStackTrace(System.err);
		}
	}

	@Override
	public void setLogLevel(int logLevel) {
		this.logLevel = logLevel;
	}

	@Override
	public int getLogLevel() {
		return logLevel;
	}
	
	@Override
	public void setApplicationLogger (ApplicationLogger applicationLogger) {
		this.applicationLogger = applicationLogger;
	}

	@Override
	public ApplicationLogger getApplicationLogger () {
		return applicationLogger;
	}

	@Override
	public void exit() {
		postRunnable(new Runnable() {
			@Override
			public void run() {
				running = false;
			}
		});
	}

	@Override
	public void addLifecycleListener(LifecycleListener listener) {
		synchronized (lifecycleListeners) {
			lifecycleListeners.add(listener);
		}
	}

	@Override
	public void removeLifecycleListener(LifecycleListener listener) {
		synchronized (lifecycleListeners) {
			lifecycleListeners.removeValue(listener, true);
		}
	}
}
