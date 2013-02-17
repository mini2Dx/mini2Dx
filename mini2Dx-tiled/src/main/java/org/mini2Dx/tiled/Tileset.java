/**
 * Copyright (c) 2013, mini2Dx Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.tiled;

import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * A tileset loaded with a {@link TiledMap}
 * 
 * @author Thomas Cashman
 */
public class Tileset {
	private TextureRegion[][] tiles;
	private String name, tilesetImagePath;
	private int width, height;
	private int tileWidth, tileHeight;
	private int spacing, margin;
	private int firstGid;
	private int lastGid = Integer.MAX_VALUE;
	private int widthInTiles = -1, heightInTiles = -1;

	/**
	 * Draws a tile to the {@link Graphics} context
	 * 
	 * @param g
	 *            The {@link Graphics} context available for rendering
	 * @param tileId
	 *            The tile id to render
	 * @param renderX
	 *            The X coordinate to render at
	 * @param renderY
	 *            The Y coordinate to render at
	 */
	public void drawTile(Graphics g, int tileId, int renderX, int renderY) {
		g.drawTextureRegion(tiles[getTileX(tileId)][getTileY(tileId)], renderX,
				renderY);
	}

	/**
	 * Draws the whole tileset to the {@link Graphics} context
	 * 
	 * @param g
	 *            The {@link Graphics} context available for rendering
	 * @param renderX
	 *            The X coordinate to render at
	 * @param renderY
	 *            The Y coordinate to render at
	 */
	public void drawTileset(Graphics g, int renderX, int renderY) {
		for (int y = 0; y < getHeightInTiles(); y++) {
			for (int x = 0; x < getWidthInTiles(); x++) {
				g.drawTextureRegion(tiles[x][y],
						renderX + (x * getTileWidth()), renderY
								+ (y * getTileHeight()));
			}
		}
	}

	/**
	 * Returns if the tileset image has been loaded
	 * 
	 * @return True if loaded
	 */
	public boolean isTextureLoaded() {
		return tiles != null;
	}

	/**
	 * Loads the tileset image
	 * 
	 * @param tmxDirectory
	 *            The directory containing the TMX files for the
	 *            {@link TiledMap} that has loaded this tileset
	 */
	public void loadTexture(FileHandle tmxDirectory) {
		Texture texture = new Texture(tmxDirectory.child(tilesetImagePath));
		lastGid = (tileWidth * tileHeight) + firstGid - 1;
		tiles = new TextureRegion[getWidthInTiles()][getHeightInTiles()];

		for (int x = 0; x < getWidthInTiles(); x++) {
			for (int y = 0; y < getHeightInTiles(); y++) {
				int tileX = margin + (x * spacing) + (x * tileWidth);
				int tileY = margin + (y * spacing) + (y * tileHeight);
				TextureRegion tile = new TextureRegion(texture, tileX, tileY,
						tileWidth, tileHeight);
				tile.flip(false, true);
				tiles[x][y] = tile;
			}
		}
	}

	/**
	 * Returns true if this tileset contains the tile with the given id
	 * @param tileId The tile id to search for
	 * @return False if the tileset does not contain the tile
	 */
	public boolean contains(int tileId) {
		return tileId >= firstGid && tileId <= lastGid;
	}

	/**
	 * Returns the x coordinate of a tile within this tileset
	 * @param tileId The tile id to get the x coordinate for
	 * @return
	 */
	public int getTileX(int tileId) {
		return (tileId - firstGid) % getWidthInTiles();
	}

	/**
	 * Returns the y coordinate of a tile within this tileset
	 * @param tileId The tile id to get the y coordinate for
	 * @return
	 */
	public int getTileY(int tileId) {
		return (tileId - firstGid) / getWidthInTiles();
	}

	/**
	 * Returns the width of this tileset in tiles
	 * @return
	 */
	public int getWidthInTiles() {
		if (widthInTiles < 0) {
			widthInTiles = (width - (margin * 2)) / tileWidth;

			int xSpacing = (widthInTiles - 1) * spacing;
			widthInTiles = (width - (margin * 2) - xSpacing) / tileWidth;
		}
		return widthInTiles;
	}

	/**
	 * Returns the height of this tileset in tiles
	 * @return
	 */
	public int getHeightInTiles() {
		if (heightInTiles < 0) {
			heightInTiles = (height - (margin * 2)) / tileHeight;

			int ySpacing = (heightInTiles - 1) * spacing;
			heightInTiles = (height - (margin * 2) - ySpacing) / tileHeight;
		}
		return heightInTiles;
	}

	/**
	 * Returns the name of this tileset
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this tileset
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the width of this tileset in pixels
	 * @return
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the width of this tileset in pixels
	 * @param width The width in pixels
	 */
	public void setWidth(int width) {
		this.width = width;
		widthInTiles = -1;
	}

	/**
	 * Returns the height of this tileset in pixels
	 * @return
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the height of this tileset in pixels
	 * @param height The height in pixels
	 */
	public void setHeight(int height) {
		this.height = height;
		heightInTiles = -1;
	}

	/**
	 * Returns the width of each tile in pixels
	 * @return
	 */
	public int getTileWidth() {
		return tileWidth;
	}
	
	/**
	 * Sets the width of each tile in pixels
	 * @param tileWidth The width in pixels
	 */
	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
		widthInTiles = -1;
	}

	/**
	 * Returns the height of each tile in pixels
	 * @return
	 */
	public int getTileHeight() {
		return tileHeight;
	}

	/**
	 * Sets the height of each tile in pixels
	 * @param tileHeight The height in pixels
	 */
	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
		heightInTiles = -1;
	}

	/**
	 * Returns the internal spacing between each tile
	 * @return The spacing in pixels
	 */
	public int getSpacing() {
		return spacing;
	}

	/**
	 * Sets the internal spacing between each tile
	 * @param spacing The spacing in pixels
	 */
	public void setSpacing(int spacing) {
		this.spacing = spacing;
		widthInTiles = -1;
		heightInTiles = -1;
	}

	/**
	 * Returns the margin at the borders of the tileset
	 * @return The margin in pixels
	 */
	public int getMargin() {
		return margin;
	}

	/**
	 * Sets the margin at the borders of the tileset
	 * @param margin The margin in pixels
	 */
	public void setMargin(int margin) {
		this.margin = margin;
		widthInTiles = -1;
		heightInTiles = -1;
	}

	/**
	 * Returns the first GID {@see <a>https://github.com/bjorn/tiled/wiki/TMX-Map-Format</a>}
	 * @return
	 */
	public int getFirstGid() {
		return firstGid;
	}

	/**
	 * Sets the first GID {@see <a>https://github.com/bjorn/tiled/wiki/TMX-Map-Format</a>}
	 * @param firstGid
	 */
	public void setFirstGid(int firstGid) {
		this.firstGid = firstGid;
	}

	/**
	 * Returns the relative path of the tileset image
	 * @return
	 */
	public String getTilesetImagePath() {
		return tilesetImagePath;
	}

	/**
	 * Sets the relative path of the tileset image
	 * @param tilesetImagePath
	 */
	public void setTilesetImagePath(String tilesetImagePath) {
		this.tilesetImagePath = tilesetImagePath;
	}
}
