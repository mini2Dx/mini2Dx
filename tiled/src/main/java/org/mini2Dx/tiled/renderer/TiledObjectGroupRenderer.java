/**
 * Copyright 2016 Thomas Cashman
 */
package org.mini2Dx.tiled.renderer;

import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.tiled.TiledObjectGroup;

/**
 * A common interface for {@link TiledObjectGroup} rendering implementations
 */
public interface TiledObjectGroupRenderer {
	/**
	 * Renders a {@link TiledObjectGroup}
	 * @param g The {@link Graphics} context
	 * @param objectGroup The {@link TiledObjectGroup} to render
	 * @param renderX The screen x coordinate to render at
	 * @param renderY The screen y coordinate to render at
	 * @param startTileX The tile x coordinate to start at
	 * @param startTileY The tile y coordinate to start at
	 * @param widthInTiles The width to render in tiles
	 * @param heightInTiles The height to render in tiles
	 */
	public void drawObjectGroup(Graphics g, TiledObjectGroup objectGroup, int renderX,
			int renderY, int startTileX, int startTileY, int widthInTiles, int heightInTiles);
	
	public void dispose();
}
