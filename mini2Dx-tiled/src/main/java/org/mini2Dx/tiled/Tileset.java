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
	 * @param g
	 *            The {@link Graphics} context available for rendering
	 * @param renderX
	 *            The X coordinate to render at
	 * @param renderY
	 *            The Y coordinate to render at
	 */
	public void drawTileset(Graphics g, int renderX, int renderY) {
		for(int y = 0; y < getHeightInTiles(); y++) {
			for(int x = 0; x < getWidthInTiles(); x++) {
				g.drawTextureRegion(tiles[x][y], renderX + (x * getTileWidth()), renderY + (y * getTileHeight()));
			}
		}
	}

	public boolean isTextureLoaded() {
		return tiles != null;
	}

	public void loadTexture(FileHandle tmxDirectory) {
		Texture texture = new Texture(tmxDirectory.child(tilesetImagePath));
		lastGid = (tileWidth * tileHeight) + firstGid - 1;
		tiles = new TextureRegion[getWidthInTiles()][getHeightInTiles()];

		for (int x = 0; x < getWidthInTiles(); x++) {
			for (int y = 0; y < getHeightInTiles(); y++) {
				int tileX = margin + (x * spacing) + (x * tileWidth);
				int tileY = margin + (y * spacing) + (y * tileHeight);
				TextureRegion tile = new TextureRegion(texture, tileX, tileY, tileWidth,
						tileHeight);
				tile.flip(false, true);
				tiles[x][y] = tile;
			}
		}
	}

	public boolean contains(int tileId) {
		return tileId >= firstGid && tileId <= lastGid;
	}

	public int getTileX(int tileId) {
		return (tileId - firstGid) % getWidthInTiles();
	}

	public int getTileY(int tileId) {
		return (tileId - firstGid) / getWidthInTiles();
	}

	public int getWidthInTiles() {
		if(widthInTiles < 0) {
			widthInTiles = (width - (margin * 2)) / tileWidth;
			
			int xSpacing = (widthInTiles - 1) * spacing;
			widthInTiles = (width - (margin * 2) - xSpacing) / tileWidth;
		}
		return widthInTiles;
	}

	public int getHeightInTiles() {
		if(heightInTiles < 0) {
			heightInTiles = (height - (margin * 2)) / tileHeight;
			
			int ySpacing = (heightInTiles - 1) * spacing;
			heightInTiles = (height - (margin * 2) - ySpacing) / tileHeight;
		}
		return heightInTiles;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		widthInTiles = -1;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
		heightInTiles = -1;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
		widthInTiles = -1;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
		heightInTiles = -1;
	}

	public int getSpacing() {
		return spacing;
	}

	public void setSpacing(int spacing) {
		this.spacing = spacing;
		widthInTiles = -1;
		heightInTiles = -1;
	}

	public int getMargin() {
		return margin;
	}

	public void setMargin(int margin) {
		this.margin = margin;
		widthInTiles = -1;
		heightInTiles = -1;
	}

	public int getFirstGid() {
		return firstGid;
	}

	public void setFirstGid(int firstGid) {
		this.firstGid = firstGid;
	}

	public String getTilesetImagePath() {
		return tilesetImagePath;
	}

	public void setTilesetImagePath(String tilesetImagePath) {
		this.tilesetImagePath = tilesetImagePath;
	}
}
