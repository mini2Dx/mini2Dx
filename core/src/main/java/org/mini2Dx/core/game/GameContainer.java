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
package org.mini2Dx.core.game;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.TimestepMode;
import org.mini2Dx.core.util.InterpolationTracker;
import org.mini2Dx.gdx.utils.Array;

/**
 * Base class for mini2Dx game containers. All games using mini2Dx must extend
 * this.
 */
public abstract class GameContainer {
    public static int TARGET_FPS = 60;

    protected int width, height;
    protected Graphics graphics;
    private boolean isInitialised = false;
    private Array<GameResizeListener> gameResizeListeners;

    /**
     * Initialise the game
     */
    public abstract void initialise();

    /**
     * Called by mini2Dx
     * @param delta The time in seconds since the last update
     */
    public void preUpdate(float delta) {
        Mdx.executor.update(delta);
    }

    /**
     * Called by mini2Dx
     * @param delta The time in seconds since the last update
     */
    public void preUpdatePhysics(float delta) {
        InterpolationTracker.preUpdate();
    }

    /**
     * Update the game
     * @param delta The time in seconds since the last update
     */
    public abstract void update(float delta);

    /**
     * Update the game physics
     * @param delta The time in seconds to advance the physics by
     */
    public void updatePhysics(float delta) {}

    /**
     * Interpolate the game physics
     * @param alpha The alpha value to use during interpolation
     */
    public void interpolate(float alpha) {
        InterpolationTracker.interpolate(alpha);
    }

    /**
     * Render the game
     * @param g The {@link Graphics} context available for rendering
     */
    public abstract void render(Graphics g);

    public abstract void onPause();

    public abstract void onResume();

    public void render() {
        graphics.preRender(width, height);
        render(graphics);
        graphics.postRender();
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        for(GameResizeListener listener : gameResizeListeners) {
            listener.onResize(width, height);
        }
    }

    /**
     * Internal pre-initialisation code
     */
    protected void preinit(Graphics g) {
        this.gameResizeListeners = new Array<GameResizeListener>(1);
        this.graphics = g;
    }

    /**
     * Internal post-initialisation code
     */
    protected void postinit() {}

    public void start(Graphics g) {
        this.width = g.getWindowWidth();
        this.height = g.getWindowHeight();

        if(!isInitialised) {
            preinit(g);
            initialise();
            postinit();
            isInitialised = true;
        }
    }

    public void dispose() {

    }

    public void addResizeListener(GameResizeListener listener) {
        gameResizeListeners.add(listener);
    }

    public void removeResizeListener(GameResizeListener listener) {
        gameResizeListeners.removeValue(listener, true);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

