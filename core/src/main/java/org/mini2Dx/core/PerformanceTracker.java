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

import org.mini2Dx.core.font.FontGlyphLayout;
import org.mini2Dx.core.util.Align;

import java.util.Locale;

public class PerformanceTracker {
    private static final String DURATION_PREFIX = "Avg update duration:: ";
    private static final String UPDATE_PREFIX = "Updates / second:: ";
    private static final String FRAMES_PREFIX = "Frames / second:: ";
    private static final String MEMORY_PREFIX = "Memory usage:: ";
    private static final String MS = "ms";

    private static final String[] messages = new String[4];
    private static long lastMessagesUpdate = 0L;

    private static FontGlyphLayout glyphLayout;

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
    public static void draw(Graphics g, float x, float y) {
        draw(g, x, y, -1, 0);
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
     * @param targetWidth
     *            The target width to render at, or, -1 to use auto-width
     * @param horizontalAlign
     *            The text alignment. Note: Use {@link Align} to retrieve the
     *            appropriate value. If target width is set to -1 this option is
     *            ignored and the text is left aligned
     */
    public static void draw(Graphics g, float x, float y, float targetWidth, int horizontalAlign) {
        updateMessages();
        float lineHeight = getLineHeight(g);

        for (int i = 0; i < messages.length; i++) {
            float computedY = y + (lineHeight * i) + (1f * i);
            if (targetWidth < 0f) {
                g.drawString(messages[i], x, computedY);
            } else {
                g.drawString(messages[i], x, computedY, targetWidth, horizontalAlign);
            }
        }
    }

    /**
     * Draws the current values to the top left of the screen
     *
     * @param g
     *            The {@link Graphics} context
     */
    public static void drawInTopLeft(Graphics g) {
        draw(g, 0, 0);
    }

    /**
     * Draws the current values to the top right of the screen
     *
     * @param g
     *            The {@link Graphics} context
     */
    public static void drawInTopRight(Graphics g) {
        updateMessages();
        float textWidth = getLineWidth(g);
        draw(g, g.getViewportWidth() - textWidth - 1f, 0f, textWidth, Align.RIGHT);
    }

    /**
     * Draws the current values to the bottom left of the screen
     *
     * @param g
     *            The {@link Graphics} context
     */
    public static void drawInBottomLeft(Graphics g) {
        updateMessages();
        float textHeight = (getLineHeight(g) * messages.length) + messages.length;
        draw(g, 0f, g.getViewportHeight() - textHeight - 1f, -1f, Align.LEFT);
    }

    /**
     * Draws the current values to the bottom right of the screen
     *
     * @param g
     *            The {@link Graphics} context
     */
    public static void drawInBottomRight(Graphics g) {
        updateMessages();
        float textWidth = getLineWidth(g);
        float textHeight = (getLineHeight(g) * messages.length) + messages.length;
        draw(g, g.getViewportWidth() - textWidth - 1f, g.getViewportHeight() - textHeight - 1f, textWidth, Align.RIGHT);
    }

    private static void updateMessages() {
        long currentTime = Mdx.platformUtils.currentTimeMillis();
        if(currentTime - lastMessagesUpdate < 1000L) {
            return;
        }

        lastMessagesUpdate = currentTime;
        messages[0] = DURATION_PREFIX + String.format(Locale.ENGLISH, "%.3f", Mdx.platformUtils.getAverageUpdateDuration() / 1000000)
                + MS;
        messages[1] = UPDATE_PREFIX + Mdx.platformUtils.getUpdatesPerSecond();
        messages[2] = FRAMES_PREFIX + Mdx.platformUtils.getFramesPerSecond();
        messages[3] = MEMORY_PREFIX + getHumanReadableByteValue(Mdx.platformUtils.getUsedMemory()) + "/"
                + getHumanReadableByteValue(Mdx.platformUtils.getTotalMemory());
    }


    private static float getLineWidth(Graphics g) {
        if(glyphLayout == null) {
            glyphLayout = g.getFont().newGlyphLayout();
        }

        float lineWidth = 0f;
        for (int i = 0; i < messages.length; i++) {
            glyphLayout.setText(messages[i]);
            if (glyphLayout.getWidth() > lineWidth) {
                lineWidth = glyphLayout.getWidth();
            }
        }
        return lineWidth;
    }

    private static float getLineHeight(Graphics g) {
        if(glyphLayout == null) {
            glyphLayout = g.getFont().newGlyphLayout();
        }

        float lineHeight = 0f;
        for (int i = 0; i < messages.length; i++) {
            glyphLayout.setText(messages[i]);
            if (glyphLayout.getHeight() > lineHeight) {
                lineHeight = glyphLayout.getHeight();
            }
        }
        return lineHeight;
    }

    private static String getHumanReadableByteValue(long bytes) {
        int unit = 1024;
        if (bytes < unit)
            return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        char pre = "KMGTPE".charAt(exp - 1);
        return String.format(Locale.ENGLISH, "%.1f %ciB", bytes / Math.pow(unit, exp), pre);
    }
}
