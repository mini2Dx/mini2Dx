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
package org.mini2Dx.tiled.tileset;

import java.util.HashMap;
import java.util.Map;

import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.tiled.Tile;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

/**
 * A {@link TilesetSource} referenced by image directly in a TMX file
 */
public class ImageTilesetSource extends TilesetSource {
	private final Tile[][] tiles;
	private final int width, height;
	private final int tileWidth, tileHeight;
	private final int spacing, margin;
	
	private String name, tilesetImagePath, transparentColorValue;
	private Map<String, String> properties;
	private int widthInTiles, heightInTiles;
	
	private Texture texture;

	public ImageTilesetSource(int width, int height, int tileWidth, int tileHeight, int spacing, int margin) {
		super();
		this.width = width;
		this.height = height;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.spacing = spacing;
		this.margin = margin;

		this.widthInTiles = -1;
		this.heightInTiles = -1;
		tiles = new Tile[getWidthInTiles()][getHeightInTiles()];
		for(int x = 0; x < getWidthInTiles(); x++) {
			for(int y = 0; y < getHeightInTiles(); y++) {
				tiles[x][y] = new Tile();
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

	@Override
	public void loadTexture(FileHandle tmxDirectory) {
		if(texture != null) {
			return;
		}
		
		Pixmap pixmap = new Pixmap(tmxDirectory.child(tilesetImagePath));
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
				tiles[x][y].setTileImage(tileImage);
			}
		}
	}

	@Override
	public boolean isTextureLoaded() {
		return texture != null;
	}

	@Override
	public int getWidthInTiles() {
		if (widthInTiles < 0) {
			int result = 0;
			for (int x = margin; x <= width - tileWidth; x += tileWidth + spacing) {
				result++;
			}
			widthInTiles = result;
		}
		return widthInTiles;
	}

	@Override
	public int getHeightInTiles() {
		if (heightInTiles < 0) {
			int result = 0;
			for (int y = margin; y <= height - tileHeight; y += tileHeight + spacing) {
				result++;
			}
			heightInTiles = result;
		}
		return heightInTiles;
	}
	
	@Override
	public Tile getTile(int tileId, int firstGid) {
		int tileX = getTileX(tileId, firstGid);
		int tileY = getTileY(tileId, firstGid);
		return tiles[tileX][tileY];
	}

	@Override
	public void drawTile(Graphics g, int tileId, int firstGid, int renderX, int renderY) {
		int tileX = getTileX(tileId, firstGid);
		int tileY = getTileY(tileId, firstGid);
		tiles[tileX][tileY].draw(g, renderX, renderY); 
	}

	@Override
	public void drawTileset(Graphics g, int renderX, int renderY) {
		for (int y = 0; y < getHeightInTiles(); y++) {
			for (int x = 0; x < getWidthInTiles(); x++) {
				tiles[x][y].draw(g, renderX + (x * getTileWidth()), renderY
						+ (y * getTileHeight())); 
			}
		}
	}
	
	@Override
	public boolean containsProperty(String propertyName) {
		if(properties == null)
			return false;
		return properties.containsKey(propertyName);
	}

	@Override
	public String getProperty(String propertyName) {
		if(properties == null)
			return null;
		return properties.get(propertyName);
	}
	
	@Override
	public void setProperty(String propertyName, String value) {
		if(properties == null)
			properties = new HashMap<String, String>();
		properties.put(propertyName, value);
	}
	
	@Override
	public Map<String, String> getProperties() {
		return properties;
	}
	
	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getTileWidth() {
		return tileWidth;
	}

	@Override
	public int getTileHeight() {
		return tileHeight;
	}
	
	@Override
	public int getSpacing() {
		return spacing;
	}

	@Override
	public int getMargin() {
		return margin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTilesetImagePath() {
		return tilesetImagePath;
	}

	public void setTilesetImagePath(String tilesetImagePath) {
		this.tilesetImagePath = tilesetImagePath;
	}

	public String getTransparentColorValue() {
		return transparentColorValue;
	}

	public void setTransparentColorValue(String transparentColorValue) {
		this.transparentColorValue = transparentColorValue;
	}

	@Override
	public void dispose() {
		for (int x = 0; x < getWidthInTiles(); x++) {
			for (int y = 0; y < getHeightInTiles(); y++) {
				if(tiles[x][y] != null) {
					tiles[x][y].dispose();
				}
			}
		}
		texture.dispose();
		texture = null;
	}
}
