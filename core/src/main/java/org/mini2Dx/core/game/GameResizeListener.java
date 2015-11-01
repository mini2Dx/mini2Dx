/**
 * Copyright 2015 Thomas Cashman
 */
package org.mini2Dx.core.game;

/**
 * Common interface for listening to {@link GameContainer} resizes
 */
public interface GameResizeListener {

	public void onResize(int width, int height);
}
