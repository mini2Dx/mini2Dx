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
package org.mini2Dx.tiled.renderer;

import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.tiled.Tile;
import org.mini2Dx.tiled.tileset.TilesetSource;

/**
 * A {@link TileRenderer} for single tile images
 */
public class StaticTileRenderer implements TileRenderer {
	private final Tile tile;
	private final TilesetSource tilesetSource;

	public StaticTileRenderer(TilesetSource tilesetSource, Tile tile) {
		super();
		this.tilesetSource = tilesetSource;
		this.tile = tile;
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void draw(Graphics g, int renderX, int renderY) {
		g.drawTextureRegion(getCurrentTileImage(), renderX, renderY);
	}

	@Override
	public TextureRegion getCurrentTileImage() {
		return tilesetSource.getTileImage(tile.getTileId(0));
	}

	@Override
	public void dispose() {
	}
}
