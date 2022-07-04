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
import org.mini2Dx.core.util.RollingMax;
import org.mini2Dx.core.util.ZlibStream;

public abstract class PlatformUtils {

    private long updateSecondStart;
    private long updateStartNanos;
    private int updates;
    private int updatesPerSecond;
    private final RollingAverage averageUpdateDuration = new RollingAverage(GameContainer.TARGET_FPS);
    private final RollingMax maxUpdateDuration = new RollingMax(GameContainer.TARGET_FPS);

    private long interpolateStartNanos;
    private final RollingAverage averageInterpolateDuration = new RollingAverage(GameContainer.TARGET_FPS);
    private final RollingMax maxInterpolateDuration = new RollingMax(GameContainer.TARGET_FPS);

    private long renderStartNanos;
    private final RollingAverage averageRenderDuration = new RollingAverage(GameContainer.TARGET_FPS);
    private final RollingMax maxRenderDuration = new RollingMax(GameContainer.TARGET_FPS);

    private long frameStartNanos;
    private final RollingAverage averageFrameInterval = new RollingAverage(GameContainer.TARGET_FPS);
    private final RollingAverage averageFrameDuration = new RollingAverage(GameContainer.TARGET_FPS);
    private final RollingMax maxFrameDuration = new RollingMax(GameContainer.TARGET_FPS);

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
     * Enables performance mode on the device. Depending on the device, this may be disabled by the OS at any time.
     * This is best used to temporarily boost performance during asset loading. On devices where this is not supported, this does nothing.
     */
    public abstract void enablePerformanceMode();

    /**
     * Cancels performance mode on the device. If performance mode was already cancelled by the OS, this does nothing.
     * On devices where this is not supported, this does nothing.
     */
    public abstract void cancelPerformanceMode();

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

        updateStartNanos = nanoTime();
    }

    /**
     * Internal usage only: marks the beginning of interpolate operations
     */
    public void markInterpolateBegin() {
        interpolateStartNanos = nanoTime();
    }

    /**
     * Internal usage only: marks the end of interpolate operations
     */
    public void markInterpolateEnd() {
        long time = nanoTime();
        long interpolateDuration = time - interpolateStartNanos;

        averageInterpolateDuration.mark(interpolateDuration);
        maxInterpolateDuration.mark(interpolateDuration);
    }

    /**
     * Internal usage only: marks the beginning of rendering operations
     */
    public void markRenderBegin() {
        renderStartNanos = nanoTime();
    }

    /**
     * Internal usage only: marks the end of rendering operations
     */
    public void markRenderEnd() {
        long time = nanoTime();
        long renderDuration = time - renderStartNanos;

        averageRenderDuration.mark(renderDuration);
        maxRenderDuration.mark(renderDuration);
    }

    /**
     * Internal usage only: marks the end of update operations
     */
    public void markUpdateEnd() {
        long time = nanoTime();
        long updateDuration = time - updateStartNanos;

        averageUpdateDuration.mark(updateDuration);
        maxUpdateDuration.mark(updateDuration);
    }

    /**
     * Internal usage only: marks a new frame
     */
    public void markFrameBegin() {
        long time = nanoTime();

        if(frameStartNanos != 0) {
            averageFrameInterval.mark(time - frameStartNanos);
        }
        frameStartNanos = time;

        if (time - frameSecondStart >= 1000000000) {
            framesPerSecond = frames;
            frames = 0;
            frameSecondStart = time;
        }
        frames++;
    }

    public void markFrameEnd() {
        long time = nanoTime();
        long frameDuration = time - frameStartNanos;

        averageFrameDuration.mark(frameDuration);
        maxFrameDuration.mark(frameDuration);
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
     * Returns the number of frames that have occurred so far this second
     * @return
     */
    public int getUpdatesThisSecond() {
        return updates;
    }

    /**
     * Returns the average duration of update()
     *
     * @return The average duration in nanoseconds
     */
    public double getAverageUpdateDuration() {
        return averageUpdateDuration.getAverage();
    }

    /**
     * Returns the maximum update() duration in the last second
     * @return The duration in nanoseconds
     */
    public double getMaxUpdateDuration() {
        return maxUpdateDuration.getMax();
    }

    /**
     * Returns the average duration of interpolate()
     *
     * @return The average duration in nanoseconds
     */
    public double getAverageInterpolateDuration() {
        return averageInterpolateDuration.getAverage();
    }

    /**
     * Returns the maximum interpolate() duration in the last second
     * @return The duration in nanoseconds
     */
    public double getMaxInterpolateDuration() {
        return maxInterpolateDuration.getMax();
    }

    /**
     * Returns the average duration of render({@link Graphics})
     *
     * @return The average duration in nanoseconds
     */
    public double getAverageRenderDuration() {
        return averageRenderDuration.getAverage();
    }

    /**
     * Returns the maximum render() duration in the last second
     * @return The duration in nanoseconds
     */
    public double getMaxRenderDuration() {
        return maxRenderDuration.getMax();
    }

    /**
     * Returns the average interval of frames (the delta)
     *
     * @return The average interval in nanoseconds
     */
    public double getAverageFrameInterval() {
        return averageFrameInterval.getAverage();
    }

    /**
     * Returns the average duration of frames (update/interpolate/render/flush)
     *
     * @return The average duration in nanoseconds
     */
    public double getAverageFrameDuration() {
        return averageFrameDuration.getAverage();
    }

    /**
     * Returns the max duration of frames (update/interpolate/render/flush) in the last second
     *
     * @return The duration in nanoseconds
     */
    public double getMaxFrameDuration() {
        return maxFrameDuration.getMax();
    }

    /**
     * Returns a {@link ZlibStream} to decompress data
     * @param compressedData The compressed data as a byte array
     * @return A new {@link ZlibStream} instance
     */
    public abstract ZlibStream decompress(byte [] compressedData);

    /**
     * Converts a timestamp to the specified date format
     * @param millis The timestamp (in millis) to convert
     * @param format The date format (e.g. yyyy-MM-dd HH:mm:ss)
     * @return The timestamp in the date format
     */
    public abstract String timestampToDateFormat(long millis, String format);
}
