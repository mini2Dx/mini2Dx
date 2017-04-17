/**
 * Copyright (c) 2017 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.core.util;

import java.util.concurrent.TimeUnit;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.MathUtils;

/**
 * Tracks the following performance metrics during gameplay:<br>
 * <ul>
 * <li>Updates per second</li>
 * <li>Average, min + max update durations</li>
 * <li>Frames per second</li>
 * <li>Current memory usage</li>
 * </ul>
 */
public class PerformanceTracker {
	private static final GlyphLayout GLYPH_LAYOUT = new GlyphLayout();

	private final String[] messages = new String[4];

	private long updateSecondStart;
	private int updates;
	private int updatesPerSecond;

	private long updateStart;
	private RollingAverage averageUpdateDuration = new RollingAverage(GameContainer.TARGET_FPS);
	private long minUpdateDuration = Long.MAX_VALUE;
	private long maxUpdateDuration = 0L;

	private long frameSecondStart;
	private int frames;
	private int framesPerSecond;

	public PerformanceTracker() {
		updateSecondStart = System.nanoTime();
		frameSecondStart = System.nanoTime();
	}

	/**
	 * Internal usage only: marks the beginning of update operations
	 */
	public void markUpdateBegin() {
		long time = System.nanoTime();

		if (time - updateSecondStart >= 1000000000) {
			updatesPerSecond = updates;
			updates = 0;
			updateSecondStart = time;
		}
		updates++;

		updateStart = System.nanoTime();
	}

	/**
	 * Internal usage only: marks the end of update operations
	 */
	public void markUpdateEnd() {
		long time = System.nanoTime();
		long updateDuration = time - updateStart;

		averageUpdateDuration.mark(updateDuration);
		minUpdateDuration = Math.min(updateDuration, minUpdateDuration);
		maxUpdateDuration = Math.max(updateDuration, maxUpdateDuration);
	}

	/**
	 * Internal usage only: marks a new frame
	 */
	public void markFrame() {
		long time = System.nanoTime();

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
	 * @return
	 */
	public int getUpdatesPerSecond() {
		return updatesPerSecond;
	}

	/**
	 * Returns the number of frames per second
	 * 
	 * @return
	 */
	public int getFramesPerSecond() {
		return framesPerSecond;
	}

	/**
	 * Returns the total memory allocated to the JVM
	 * 
	 * @return The value in bytes
	 */
	public long getTotalMemory() {
		return Runtime.getRuntime().totalMemory();
	}

	/**
	 * Returns the remaining memory inside the JVM
	 * 
	 * @return The value in bytes
	 */
	public long getUsedMemory() {
		return getTotalMemory() - getAvailableMemory();
	}

	/**
	 * Returns the available memory inside the JVM
	 * 
	 * @return The value in bytes
	 */
	public long getAvailableMemory() {
		return Runtime.getRuntime().freeMemory();
	}

	/**
	 * Draws the current values to screen
	 * 
	 * @param g
	 *            The {@link Graphics} context
	 * @param x
	 *            The x coordinate to render at
	 * @param y
	 *            The y coordinate to render at
	 */
	public void draw(Graphics g, int x, int y) {
		messages[0] = "Update duration:: Min: " + getNanoSecondsInMillis(minUpdateDuration) + "ns, Max: "
				+ getNanoSecondsInMillis(maxUpdateDuration) + "ms, Avg: "
				+ ((int) averageUpdateDuration.getAverage()) + "ns";
		messages[1] = "Updates / second:: " + updatesPerSecond;
		messages[2] = "Frames / second:: " + framesPerSecond;
		messages[3] = "Memory usage:: " + getHumanReadableByteValue(getUsedMemory()) + "/"
				+ getHumanReadableByteValue(getTotalMemory());

		float lineHeight = getLineHeight(g);

		for (int i = 0; i < messages.length; i++) {
			g.drawString(messages[i], x, y + (lineHeight * i) + (1f * i));
		}
	}

	private float getLineHeight(Graphics g) {
		float lineHeight = 0f;
		for (int i = 0; i < messages.length; i++) {
			GLYPH_LAYOUT.setText(g.getFont(), messages[i]);
			if (GLYPH_LAYOUT.height > lineHeight) {
				lineHeight = GLYPH_LAYOUT.height;
			}
		}
		return lineHeight;
	}

	private String getHumanReadableByteValue(long bytes) {
		int unit = 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = "KMGTPE".charAt(exp - 1) + "i";
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

	private long getNanoSecondsInMillis(long duration) {
		return TimeUnit.NANOSECONDS.toMillis(duration);
	}
}
