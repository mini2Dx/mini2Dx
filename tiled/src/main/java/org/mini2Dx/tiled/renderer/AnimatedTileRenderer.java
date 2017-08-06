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
import org.mini2Dx.core.graphics.Sprite;
import org.mini2Dx.tiled.tileset.TilesetSource;

import com.badlogic.gdx.Gdx;

/**
 * A {@link TileRenderer} for animated tiles
 */
public class AnimatedTileRenderer implements TileRenderer {
	private final TileFrame[] frames;
	private final TilesetSource tilesetSource;

	private int currentFrame;
	private float timer;
	private long lastFrameId;

	public AnimatedTileRenderer(TilesetSource tilesetSource, TileFrame[] frames) {
		super();
		this.tilesetSource = tilesetSource;
		this.frames = frames;
	}

	@Override
	public void update(float delta) {
		// Prevent duplicate updates per frame
		long currentFrameId = Gdx.graphics.getFrameId();
		if (currentFrameId <= lastFrameId) {
			return;
		}
		timer += delta;
		while (timer >= frames[currentFrame].duration) {
			timer -= frames[currentFrame].duration;
			currentFrame = currentFrame == frames.length - 1 ? 0 : currentFrame + 1;
		}
		lastFrameId = currentFrameId;
	}

	@Override
	public void draw(Graphics g, int renderX, int renderY) {
		g.drawSprite(getCurrentTileImage(), renderX, renderY);
	}

	@Override
	public void draw(Graphics g, int renderX, int renderY, boolean flipH, boolean flipV, boolean flipD) {
		Sprite tileImage = getCurrentTileImage();

		boolean previousFlipX = tileImage.isFlipX();
		boolean previousFlipY = tileImage.isFlipY();

		if (flipD) {
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
			tileImage.setFlip(flipH, !flipV);
		}

		g.drawSprite(tileImage, renderX, renderY);
		tileImage.setRotation(0f);
		tileImage.setFlip(previousFlipX, previousFlipY);
	}

	@Override
	public Sprite getCurrentTileImage() {
		return tilesetSource.getTileImage(frames[currentFrame].tileId);
	}

	@Override
	public void dispose() {
	}
}
