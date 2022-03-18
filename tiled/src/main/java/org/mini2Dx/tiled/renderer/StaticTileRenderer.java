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
package org.mini2Dx.tiled.renderer;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.graphics.Sprite;
import org.mini2Dx.tiled.Tile;
import org.mini2Dx.tiled.tileset.TilesetSource;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * A {@link TileRenderer} for single tile images
 */
public class StaticTileRenderer implements TileRenderer {
	public static final int RENDERER_TYPE = 0;

	private final Tile tile;
	private final TilesetSource tilesetSource;

	public StaticTileRenderer(TilesetSource tilesetSource, Tile tile) {
		super();
		this.tilesetSource = tilesetSource;
		this.tile = tile;
	}

	public static StaticTileRenderer fromInputStream(TilesetSource tilesetSource, Tile tile) {
		return new StaticTileRenderer(tilesetSource, tile);
	}

	@Override
	public void writeData(DataOutputStream outputStream) throws IOException {
	}

	@Override
	public void readData(DataInputStream inputStream) throws IOException {
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void draw(Graphics g, int renderX, int renderY, float alpha) {
		Sprite tileImage = getCurrentTileImage();
		tileImage.setAlpha(alpha);
		tileImage.setPosition(renderX, renderY);
		g.drawSprite(tileImage);
	}
	
	@Override
	public void draw(Graphics g, int renderX, int renderY, float alpha, boolean flipH, boolean flipV, boolean flipD) {
		StaticTileRenderer.drawTileImage(g, getCurrentTileImage(), renderX, renderY, alpha, flipH, flipV, flipD);
	}

	@Override
	public Sprite getCurrentTileImage() {
		return tilesetSource.getTileImage(tile.getTileId(0));
	}

	@Override
	public void dispose() {
	}

	public static void drawTileImage(Graphics g, Sprite tileImage, int renderX, int renderY,
	                                 float alpha, boolean flipH, boolean flipV, boolean flipD) {
		boolean previousFlipX = tileImage.isFlipX();
		boolean previousFlipY = tileImage.isFlipY();

		if(flipD) {
			if (flipH && flipV) {
				tileImage.setRotation(90f);
				tileImage.setFlip(true, previousFlipY);
			} else if (flipH) {
				tileImage.setRotation(90f);
			} else if (flipV) {
				tileImage.setRotation(270f);
			} else {
				tileImage.setRotation(90f);
				tileImage.setFlip(previousFlipX, true);
			}
		} else {
			tileImage.setFlip(flipH, flipV);
		}
		tileImage.setAlpha(alpha);
		tileImage.setPosition(renderX, renderY);

		g.drawSprite(tileImage);
		tileImage.setRotation(0f);
		tileImage.setFlip(previousFlipX, previousFlipY);
	}

	@Override
	public int getRendererType() {
		return RENDERER_TYPE;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash();
	}
}
