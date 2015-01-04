/**
 * Copyright (c) 2015, mini2Dx Project
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

import java.util.HashMap;
import java.util.Map;

import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * A tileset loaded with a {@link TiledMap}
 */
public class Tileset {
	private Tile[][] tiles;
	private String name, tilesetImagePath, transparentColorValue;
	private int width, height;
	private int tileWidth, tileHeight;
	private int spacing, margin;
	private int firstGid;
	private int lastGid = Integer.MAX_VALUE;
	private int widthInTiles, heightInTiles;
	private Map<String, String> properties;
	
	public Tileset(int width, int height, int tileWidth, int tileHeight, int spacing, int margin, int firstGid) {
		this.width = width;
		this.height = height;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.spacing = spacing;
		this.margin = margin;
		this.firstGid = firstGid;
		this.widthInTiles = -1;
		this.heightInTiles = -1;
		
		calculateLastGid();
		tiles = new Tile[getWidthInTiles()][getHeightInTiles()];
		for(int x = 0; x < getWidthInTiles(); x++) {
			for(int y = 0; y < getHeightInTiles(); y++) {
				tiles[x][y] = new Tile();
				tiles[x][y].setTileId(this.getTileId(x, y));
			}
		}
	}
	
	/**
	 * Returns if the tileset contains the specified property
	 * @param propertyName The property name to search for
	 * @return True if the tileset contains the property
	 */
	public boolean containsProperty(String propertyName) {
		if(properties == null)
			return false;
		return properties.containsKey(propertyName);
	}

	/**
	 * Returns the value of a specified property
	 * @param propertyName The property name to search for
	 * @return Null if there is no such property
	 */
	public String getProperty(String propertyName) {
		if(properties == null)
			return null;
		return properties.get(propertyName);
	}
	
	/**
	 * Sets the value of a specified property
	 * @param propertyName The property name to set the value for
	 * @param value The value of the property to set
	 */
	public void setProperty(String propertyName, String value) {
		if(properties == null)
			properties = new HashMap<String, String>();
		properties.put(propertyName, value);
	}

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
		int tileX = getTileX(tileId);
		int tileY = getTileY(tileId);
		tiles[tileX][tileY].draw(g, renderX, renderY); 
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
				tiles[x][y].draw(g, renderX + (x * getTileWidth()), renderY
						+ (y * getTileHeight())); 
			}
		}
	}
	
	/**
	 * Returns the {@link Tile} for a given tile id
	 * @param tileId The tile id to look up
	 * @return The {@link Tile}
	 */
	public Tile getTile(int tileId) {
		int tileX = getTileX(tileId);
		int tileY = getTileY(tileId);
		return tiles[tileX][tileY];
	}
	
	/**
	 * Returns the {@link Tile} for a given tile coordinate on the tileset
	 * @param x The x coordinate in tiles
	 * @param y The y coordinate in tiles
	 * @return The {@link Tile}
	 */
	public Tile getTile(int x, int y) {
		return tiles[x][y];
	}

	/**
	 * Returns if the tileset image has been loaded
	 * 
	 * @return True if loaded
	 */
	public boolean isTextureLoaded() {
		return tiles[0][0].getTileImage() != null;
	}

	/**
	 * Loads the tileset image
	 * 
	 * @param tmxDirectory
	 *            The directory containing the TMX files for the
	 *            {@link TiledMap} that has loaded this tileset
	 */
	public void loadTexture(FileHandle tmxDirectory) {
		Pixmap pixmap = new Pixmap(tmxDirectory.child(tilesetImagePath));
		Texture texture = null;
		if(transparentColorValue != null) {
			texture = modifyPixmapWithTransparentColor(pixmap);
		} else {
			texture = new Texture(pixmap);
			pixmap.dispose();
		}
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		for (int x = 0; x < getWidthInTiles(); x++) {
			for (int y = 0; y < getHeightInTiles(); y++) {
				int tileX = margin + (x * spacing) + (x * tileWidth);
				int tileY = margin + (y * spacing) + (y * tileHeight);
				TextureRegion tileImage = new TextureRegion(texture, tileX, tileY,
						tileWidth, tileHeight);
				tileImage.flip(false, true);
				tiles[x][y].setTileImage(tileImage);
			}
		}
	}
	
	private Texture modifyPixmapWithTransparentColor(Pixmap pixmap) {
		float r = Integer.parseInt(transparentColorValue.substring(0, 2), 16) / 255f;
		float g = Integer.parseInt(transparentColorValue.substring(2, 4), 16) / 255f;
		float b = Integer.parseInt(transparentColorValue.substring(4, 6), 16) / 155f;
		
		int transparentColor = Color.rgba8888(new Color(r, g, b, 1f));
		
		Pixmap updatedPixmap = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), Format.RGBA8888);
		
		for(int x = 0; x < pixmap.getWidth(); x++) {
			for(int y = 0; y < pixmap.getHeight(); y++) {
				int pixelColor = pixmap.getPixel(x, y);
				if(pixelColor != transparentColor) {
					updatedPixmap.drawPixel(x, y, pixelColor);
				}
			}
		}
		
		Texture result = new Texture(updatedPixmap);
		updatedPixmap.dispose();
		pixmap.dispose();
		return result;
	}
	
	private void calculateLastGid() {
		lastGid = (getWidthInTiles() * getHeightInTiles()) + firstGid - 1;
	}

	/**
	 * Returns true if this tileset contains the tile with the given id
	 * 
	 * @param tileId
	 *            The tile id to search for
	 * @return False if the tileset does not contain the tile
	 */
	public boolean contains(int tileId) {
		return tileId >= firstGid && tileId <= lastGid;
	}
	
	/**
	 * Returns the tile ID for a given tile within this tileset
	 * @param x The x coordinate of the tile on the tileset
	 * @param y The y coordinate of the tile on the tileset
	 * @return The tile ID
	 */
	public int getTileId(int x, int y) {
		return firstGid + (y * getWidthInTiles()) + x;
	}

	/**
	 * Returns the x coordinate of a tile within this tileset
	 * 
	 * @param tileId
	 *            The tile id to get the x coordinate for
	 * @return
	 */
	public int getTileX(int tileId) {
		return (tileId - firstGid) % getWidthInTiles();
	}

	/**
	 * Returns the y coordinate of a tile within this tileset
	 * 
	 * @param tileId
	 *            The tile id to get the y coordinate for
	 * @return
	 */
	public int getTileY(int tileId) {
		return (tileId - firstGid) / getWidthInTiles();
	}

	/**
	 * Returns the width of this tileset in tiles
	 * 
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
	 * 
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
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this tileset
	 * 
	 * @param name
	 *            The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the width of this tileset in pixels
	 * 
	 * @return
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of this tileset in pixels
	 * 
	 * @return
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns the width of each tile in pixels
	 * 
	 * @return
	 */
	public int getTileWidth() {
		return tileWidth;
	}

	/**
	 * Returns the height of each tile in pixels
	 * 
	 * @return
	 */
	public int getTileHeight() {
		return tileHeight;
	}

	/**
	 * Returns the internal spacing between each tile
	 * 
	 * @return The spacing in pixels
	 */
	public int getSpacing() {
		return spacing;
	}

	/**
	 * Returns the margin at the borders of the tileset
	 * 
	 * @return The margin in pixels
	 */
	public int getMargin() {
		return margin;
	}
	
	/**
	 * Returns the first GID. See:
	 * <a href="https://github.com/bjorn/tiled/wiki/TMX-Map-Format">TMX Map Format</a>
	 * 
	 * @return
	 */
	public int getFirstGid() {
		return firstGid;
	}

	/**
	 * Returns the relative path of the tileset image
	 * 
	 * @return
	 */
	public String getTilesetImagePath() {
		return tilesetImagePath;
	}

	/**
	 * Sets the relative path of the tileset image
	 * 
	 * @param tilesetImagePath
	 */
	public void setTilesetImagePath(String tilesetImagePath) {
		this.tilesetImagePath = tilesetImagePath;
	}

	public String getTransparentColorValue() {
		return transparentColorValue;
	}

	public void setTransparentColorValue(String transparentColorValue) {
		this.transparentColorValue = transparentColorValue;
	}
}
