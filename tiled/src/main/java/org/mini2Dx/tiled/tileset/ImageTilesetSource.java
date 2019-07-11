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
package org.mini2Dx.tiled.tileset;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetDescriptor;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.graphics.*;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.IntMap;
import org.mini2Dx.gdx.utils.ObjectMap;
import org.mini2Dx.tiled.Tile;

/**
 * A {@link TilesetSource} referenced by image directly in a TMX file
 */
public class ImageTilesetSource extends TilesetSource {
	private final Tile[][] tiles;
	private final IntMap<Sprite> tileImages = new IntMap<Sprite>();
	private final int width, height;
	private final int tileWidth, tileHeight;
	private final int spacing, margin;
	
	private String name, tilesetImagePath, transparentColorValue;
	private ObjectMap<String, String> properties;
	private int widthInTiles, heightInTiles;

	private Texture backingTexture;
	private TextureRegion textureRegion;

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
				tiles[x][y].setTileId(getTileId(x, y, 0));
			}
		}
	}
	
	private Texture modifyPixmapWithTransparentColor(Pixmap pixmap) {
		float r = Integer.parseInt(transparentColorValue.substring(0, 2), 16) / 255f;
		float g = Integer.parseInt(transparentColorValue.substring(2, 4), 16) / 255f;
		float b = Integer.parseInt(transparentColorValue.substring(4, 6), 16) / 155f;
		
		final Color transparentColor = Mdx.graphics.newColor(r, g, b, 1f);
		
		Pixmap updatedPixmap = Mdx.graphics.newPixmap(pixmap.getWidth(), pixmap.getHeight(), PixmapFormat.RGBA8888);

		final IntMap<Color> colorCache = new IntMap<Color>();

		for(int x = 0; x < pixmap.getWidth(); x++) {
			for(int y = 0; y < pixmap.getHeight(); y++) {
				final int pixelRGBA8888 = pixmap.getPixel(x, y);
				final Color pixelColor;
				if(colorCache.containsKey(pixelRGBA8888)) {
					pixelColor = colorCache.get(pixelRGBA8888);
				} else {
					pixelColor = Mdx.graphics.newColor(pixelRGBA8888);
					colorCache.put(pixelRGBA8888, pixelColor);
				}
				if(!transparentColor.equals(pixelColor)) {
					updatedPixmap.drawPixel(x, y, pixelColor);
				}
			}
		}
		colorCache.clear();
		
		final Texture result = Mdx.graphics.newTexture(updatedPixmap);
		updatedPixmap.dispose();
		pixmap.dispose();
		return result;
	}
	
	@Override
	public Array<AssetDescriptor> getDependencies(FileHandle tmxPath) {
		Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>();
		dependencies.add(new AssetDescriptor(tilesetImagePath, Pixmap.class));
		return dependencies;
	}

	@Override
	public void loadTexture(FileHandle tmxPath) {
		if(textureRegion != null) {
			return;
		}
		switch(tmxPath.type()) {
		case INTERNAL:
			loadTileImages(Mdx.graphics.newPixmap(Mdx.files.internal(tilesetImagePath)));
			break;
		case EXTERNAL:
			loadTileImages(Mdx.graphics.newPixmap(Mdx.files.external(tilesetImagePath)));
			break;
		case LOCAL:
			loadTileImages(Mdx.graphics.newPixmap(Mdx.files.local(tilesetImagePath)));
			break;
		}
	}
	
	@Override
	public void loadTexture(AssetManager assetManager, FileHandle tmxPath) {
		if(textureRegion != null) {
			return;
		}
		loadTileImages(assetManager.get(tilesetImagePath, Pixmap.class));
	}

	@Override
	public void loadTexture(TextureAtlas textureAtlas) {
		if(textureRegion != null) {
			return;
		}
		final TextureAtlasRegion atlasRegion = textureAtlas.findRegion(tilesetImagePath);
		if(atlasRegion == null && tilesetImagePath.lastIndexOf('.') > -1) {
			loadTileImages(Mdx.graphics.newTextureRegion(textureAtlas.findRegion(
					tilesetImagePath.substring(0, tilesetImagePath.lastIndexOf('.')))));
		} else {
			loadTileImages(Mdx.graphics.newTextureRegion(atlasRegion));
		}
	}

	private void loadTileImages(TextureRegion textureRegion) {
		if(transparentColorValue != null) {
			backingTexture = modifyPixmapWithTransparentColor(textureRegion.toPixmap());
			this.textureRegion = Mdx.graphics.newTextureRegion(backingTexture);
		} else {
			this.textureRegion = textureRegion;
		}
		cutTiles();
	}

	private void loadTileImages(Pixmap pixmap) {
		if(transparentColorValue != null) {
			backingTexture = modifyPixmapWithTransparentColor(pixmap);
			textureRegion = Mdx.graphics.newTextureRegion(backingTexture);
		} else {
			backingTexture = Mdx.graphics.newTexture(pixmap);
			textureRegion = Mdx.graphics.newTextureRegion(backingTexture);
			pixmap.dispose();
		}
		cutTiles();
	}

	private void cutTiles() {
		for (int x = 0; x < getWidthInTiles(); x++) {
			for (int y = 0; y < getHeightInTiles(); y++) {
				int tileX = margin + (x * spacing) + (x * tileWidth);
				int tileY = margin + (y * spacing) + (y * tileHeight);
				Sprite tileImage = Mdx.graphics.newSprite(textureRegion, tileX, tileY,
						tileWidth, tileHeight);
				tileImages.put(tiles[x][y].getTileId(0), tileImage);
			}
		}
	}

	@Override
	public boolean isTextureLoaded() {
		return textureRegion != null;
	}
	
	@Override
	public Sprite getTileImage(int tileId) {
		return tileImages.get(tileId);
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
	public Tile getTileByPosition(int x, int y) {
		return tiles[x][y];
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
			properties = new ObjectMap<String, String>();
		properties.put(propertyName, value);
	}
	
	@Override
	public ObjectMap<String, String> getProperties() {
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

	@Override
	public String getInternalUuid() {
		return tilesetImagePath;
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
		textureRegion = null;

		if(backingTexture == null) {
			return;
		}
		backingTexture.dispose();
		backingTexture = null;
	}
}
