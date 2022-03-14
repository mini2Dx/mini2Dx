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
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.graphics.Sprite;
import org.mini2Dx.tiled.tileset.TilesetSource;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A {@link TileRenderer} for animated tiles
 */
public class AnimatedTileRenderer implements TileRenderer {
	public static final int RENDERER_TYPE = 1;

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

	public static AnimatedTileRenderer fromInputStream(TilesetSource tilesetSource,
													   DataInputStream inputStream) throws IOException {
		final int totalFrames = inputStream.readInt();
		final AnimatedTileRenderer result = new AnimatedTileRenderer(tilesetSource,
				new TileFrame[totalFrames]);
		result.readData(inputStream);
		return result;
	}

	@Override
	public void writeData(DataOutputStream outputStream) throws IOException {
		outputStream.writeInt(frames.length);
		for(int i = 0; i < frames.length; i++) {
			frames[i].writeData(outputStream);
		}
	}

	@Override
	public void readData(DataInputStream inputStream) throws IOException {
		for(int i = 0; i < frames.length; i++) {
			frames[i] = TileFrame.fromInputStream(inputStream);
		}
	}

	@Override
	public void update(float delta) {
		// Prevent duplicate updates per frame
		long currentFrameId = Mdx.graphicsContext.getFrameId();
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
		return tilesetSource.getTileImage(frames[currentFrame].tileId);
	}

	@Override
	public int getRendererType() {
		return RENDERER_TYPE;
	}

	@Override
	public void dispose() {
	}
}
