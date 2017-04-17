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
package com.badlogic.gdx.backends.headless;

import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.game.ApplicationListener;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.headless.HeadlessGameWrapper;
import org.mini2Dx.headless.HeadlessMini2DxConfig;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationLogger;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.headless.mock.audio.MockAudio;
import com.badlogic.gdx.backends.headless.mock.graphics.Mini2DxMockGraphics;
import com.badlogic.gdx.backends.headless.mock.input.MockInput;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Clipboard;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Launches headless mini2Dx games. Based on <a href=
 * "https://github.com/libgdx/libgdx/blob/master/backends/gdx-backend-headless/src/com/badlogic/gdx/backends/headless/HeadlessApplication.java">
 * LibGDX's LwjglApplication class</a>
 * 
 * The executeGame loop follows mini2Dx sequence: update, interpolate, render and tries to stay close to targetFps 
 */
public class HeadlessMini2DxGame implements Application {
	protected final HeadlessMini2DxConfig config;
	protected final Mini2DxMockGraphics graphics;
	protected MockAudio audio;
	protected final HeadlessFiles files;
	protected final MockInput input;
	protected final HeadlessNet net;
	protected final ApplicationListener listener;
	protected Thread mainLoopThread;
	protected boolean running = true;
	protected final Array<Runnable> runnables = new Array<Runnable>();
	protected final Array<Runnable> executedRunnables = new Array<Runnable>();
	protected final Array<LifecycleListener> lifecycleListeners = new Array<LifecycleListener>();
	protected int logLevel = LOG_INFO;
	protected ApplicationLogger applicationLogger;
	protected String preferencesdir;

	public HeadlessMini2DxGame(GameContainer game, HeadlessMini2DxConfig config) {
		setApplicationLogger(new HeadlessApplicationLogger());
		
		this.config = config;
		listener = new HeadlessGameWrapper(game, config.gameIdentifier);

		this.files = new HeadlessFiles();
		this.net = new HeadlessNet();
		// the following elements are not applicable for headless applications
		// they are only implemented as mock objects
		this.graphics = new Mini2DxMockGraphics(config);
		this.audio = new MockAudio();
		this.input = new MockInput();
		initialiseLibGDX();
		if(config.runGame) {
			launchGame();
		} else {
			listener.create();
		}
	}

	private void initialiseLibGDX() {
		preferencesdir = config.preferencesDirectory;

		Gdx.app = this;
		Gdx.graphics = graphics;
		Gdx.audio = audio;
		Gdx.files = files;
		Gdx.input = input;
		Gdx.net = net;
	}

	private void launchGame() {
		mainLoopThread = new Thread("Headless Application") {
			@Override
			public void run() {
				try {
					HeadlessMini2DxGame.this.executeGame();
				} catch (Throwable t) {
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
		Array<LifecycleListener> lifecycleListeners = this.lifecycleListeners;

		listener.create();

		int frameRate = config.targetFPS;
		float maximumDelta = 1f / frameRate;
		long fpsDeltaNanos = (long) (maximumDelta*1000000000f);
		float accumulator = 0f;
		float targetTimestep = config.targetTimestep;

		while (running) {

			graphics.updateTime();
			Mdx.performanceTracker.markFrame();
			graphics.incrementFrameId();

			executeRunnables();
			// If one of the runnables set running to false, for example after
			// an exit().
			if (!running) {
				break;
			}
			
			float delta = graphics.getDeltaTime();
			if (delta > maximumDelta) {
				delta = maximumDelta;
			}

			accumulator += delta;

			while (accumulator >= targetTimestep) {
				Mdx.performanceTracker.markUpdateBegin();
				listener.update(targetTimestep);
				Mdx.performanceTracker.markUpdateEnd();
				accumulator -= targetTimestep;
			}
			listener.interpolate(accumulator / targetTimestep);

			listener.render();

			if (frameRate > 0) {
				graphics.sleepTillDeltaTime(fpsDeltaNanos);
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
	public Graphics getGraphics() {
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
		return ApplicationType.HeadlessDesktop;
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
			Preferences prefs = new HeadlessPreferences(name, this.preferencesdir);
			preferences.put(name, prefs);
			return prefs;
		}
	}

	@Override
	public Clipboard getClipboard() {
		// no clipboards for headless apps
		return null;
	}

	@Override
	public void postRunnable(Runnable runnable) {
		synchronized (runnables) {
			runnables.add(runnable);
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
