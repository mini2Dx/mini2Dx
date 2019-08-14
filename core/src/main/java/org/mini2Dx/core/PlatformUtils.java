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
package org.mini2Dx.core;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.util.RollingAverage;

public abstract class PlatformUtils {

    private long updateSecondStart;
    private int updates;
    private int updatesPerSecond;
    private final RollingAverage averageUpdateDuration = new RollingAverage(GameContainer.TARGET_FPS);

    private long updateStart;

    private long frameSecondStart;
    private int frames;
    private int framesPerSecond;

    /**
     * Exits the game
     * @param ignorePlatformRestrictions exit even if platform rules don't permit exiting programmatically. Should be
     *                                   used only for debug purposes and always set to false for release builds.
     */
    public abstract void exit(boolean ignorePlatformRestrictions);

    /**
     * Platform independent version of {@link System#nanoTime()}
     * @see System#nanoTime()
     */
    public abstract long nanoTime();

    /**
     * Platform independent version of {@link System#currentTimeMillis()}
     * @see System#currentTimeMillis()
     */
    public abstract long currentTimeMillis();

    /**
     * Gets the total memory that can be used by the game
     * @return Number of bytes that can be used by the game
     */
    public abstract long getTotalMemory();

    /**
     * Gets the total memory available for the game
     * @return Number of bytes available for the game
     */
    public abstract long getAvailableMemory();

    /**
     * Gets the total memory used by the game
     * @return Number of bytes used by the game
     */
    public abstract long getUsedMemory();

    /**
     * Indicates if the current thread is the game thread
     * @return if the current thread is the game thread
     */
    public abstract boolean isGameThread();

    /**
     * Internal usage only: marks the beginning of update operations
     */
    public void markUpdateBegin() {
        long time = nanoTime();

        if (time - updateSecondStart >= 1000000000) {
            updatesPerSecond = updates;
            updates = 0;
            updateSecondStart = time;
        }
        updates++;

        updateStart = nanoTime();
    }

    /**
     * Internal usage only: marks the end of update operations
     */
    public void markUpdateEnd() {
        long time = nanoTime();
        long updateDuration = time - updateStart;

        averageUpdateDuration.mark(updateDuration);
    }

    /**
     * Internal usage only: marks a new frame
     */
    public void markFrame() {
        long time = nanoTime();

        if (time - frameSecondStart >= 1000000000) {
            framesPerSecond = frames;
            frames = 0;
            frameSecondStart = time;
        }
        frames++;
    }

    /**
     * Returns the number of updates per second
     *
     * @return The number of updates per second
     */
    public int getUpdatesPerSecond() {
        return updatesPerSecond;
    }

    /**
     * Returns the number of frames per second
     *
     * @return the number of frames per second
     */
    public int getFramesPerSecond() {
        return framesPerSecond;
    }

    /**
     * Returns the average duration of update()
     *
     * @return The average duration in nanoseconds
     */
    public double getAverageUpdateDuration() {
        return averageUpdateDuration.getAverage();
    }
}
