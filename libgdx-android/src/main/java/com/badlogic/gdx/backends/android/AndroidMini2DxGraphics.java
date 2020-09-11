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
package com.badlogic.gdx.backends.android;

import org.mini2Dx.android.AndroidMini2DxConfig;
import org.mini2Dx.core.Mdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.backends.android.surfaceview.ResolutionStrategy;
import com.badlogic.gdx.utils.Array;

/**
 * Overrides {@link AndroidGraphics} to add mini2Dx's timestep logic
 */
public class AndroidMini2DxGraphics extends AndroidGraphics {
	private static final String LOG_TAG = "AndroidMini2DxGraphics";
	
	private final AndroidMini2DxGame game;
	private final float maximumDelta;
	private final float targetTimestep;
	private float accumulator = 0f;
	private int lastFrameDropWarning = -1;

	public AndroidMini2DxGraphics(AndroidMini2DxGame application, AndroidMini2DxConfig config,
			ResolutionStrategy resolutionStrategy) {
		this(application, config, resolutionStrategy, true);
	}

	public AndroidMini2DxGraphics(AndroidMini2DxGame application, AndroidMini2DxConfig config,
			ResolutionStrategy resolutionStrategy, boolean focusableView) {
		super(application, config, resolutionStrategy, focusableView);
		maximumDelta = config.maximumTimestepSeconds();
		targetTimestep = config.targetTimestepSeconds();
		game = application;
	}

	@Override
	public void onDrawFrame(javax.microedition.khronos.opengles.GL10 gl) {
		final AndroidMini2DxConfig config = (AndroidMini2DxConfig) super.config;

		long time = System.nanoTime();
		deltaTime = (time - lastFrameTime) / 1000000000.0f;
		lastFrameTime = time;
		Mdx.platformUtils.markFrame();

		// After pause deltaTime can have somewhat huge value that destabilizes
		// the mean, so let's cut it off
		if (!resume) {
			mean.addValue(deltaTime);
		} else {
			deltaTime = 0;
		}

		boolean lrunning = false;
		boolean lpause = false;
		boolean ldestroy = false;
		boolean lresume = false;

		synchronized (synch) {
			lrunning = running;
			lpause = pause;
			ldestroy = destroy;
			lresume = resume;

			if (resume) {
				resume = false;
			}

			if (pause) {
				pause = false;
				synch.notifyAll();
			}

			if (destroy) {
				destroy = false;
				synch.notifyAll();
			}
		}

		if (lresume) {
			Array<LifecycleListener> listeners = app.getLifecycleListeners();
			synchronized (listeners) {
				for (LifecycleListener listener : listeners) {
					listener.resume();
				}
			}
			app.getApplicationListener().resume();
			Gdx.app.log(LOG_TAG, "resumed");
		}

		if (lrunning) {
			synchronized (app.getRunnables()) {
				app.getExecutedRunnables().clear();
				app.getExecutedRunnables().addAll(app.getRunnables());
				app.getRunnables().clear();
			}

			for (int i = 0; i < app.getExecutedRunnables().size; i++) {
				try {
					app.getExecutedRunnables().get(i).run();
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			final float delta = deltaTime;

			app.getInput().processEvents();

			switch(Mdx.timestepMode) {
				case DEFAULT:
					Mdx.platformUtils.markUpdateBegin();
					game.getApplicationListener().preUpdate(delta);
					game.getApplicationListener().preUpdatePhysics(targetTimestep);
					game.getApplicationListener().updatePhysics(targetTimestep);
					game.getApplicationListener().update(delta);
					Mdx.platformUtils.markUpdateEnd();

					game.getApplicationListener().interpolate(1f);
					break;
				case PHYSICS:
					float physicsDelta = deltaTime;
					if (physicsDelta > maximumDelta) {
						physicsDelta = maximumDelta;
					}

					accumulator += physicsDelta;

					Mdx.platformUtils.markUpdateBegin();
					game.getApplicationListener().preUpdate(delta);
					while (accumulator >= targetTimestep) {
						game.getApplicationListener().preUpdatePhysics(targetTimestep);
						game.getApplicationListener().updatePhysics(targetTimestep);
						accumulator -= targetTimestep;
					}
					game.getApplicationListener().update(delta);
					Mdx.platformUtils.markUpdateEnd();

					game.getApplicationListener().interpolate(accumulator / targetTimestep);
					break;
			}
			
			frameId++;
			Mdx.platformUtils.markRenderBegin();
			app.getApplicationListener().render();
			Mdx.platformUtils.markRenderEnd();

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
		}

		if (lpause) {
			Array<LifecycleListener> listeners = app.getLifecycleListeners();
			synchronized (listeners) {
				for (LifecycleListener listener : listeners) {
					listener.pause();
				}
			}
			app.getApplicationListener().pause();
			Gdx.app.log(LOG_TAG, "paused");
		}

		if (ldestroy) {
			Array<LifecycleListener> listeners = app.getLifecycleListeners();
			synchronized (listeners) {
				for (LifecycleListener listener : listeners) {
					listener.dispose();
				}
			}
			app.getApplicationListener().dispose();
			Gdx.app.log(LOG_TAG, "destroyed");
		}

		if (time - frameStart > 1000000000) {
			fps = frames;
			frames = 0;
			frameStart = time;
		}
		frames++;
	}
}
