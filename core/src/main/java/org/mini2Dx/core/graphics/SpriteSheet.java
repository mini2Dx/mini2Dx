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
package org.mini2Dx.core.graphics;

import org.mini2Dx.core.Mdx;

/**
 * Splits a texture into multiple {@link Sprite} instances
 */
public class SpriteSheet {
	private final int totalColumns, totalRows, totalFrames;
	private final int frameWidth, frameHeight;
	private final Sprite [] frames;

	public SpriteSheet(Texture sheet, int frameWidth, int frameHeight) {
		this(Mdx.graphics.newTextureRegion(sheet), frameWidth, frameHeight);
	}

	public SpriteSheet(TextureRegion sheet, int frameWidth, int frameHeight) {
		super();
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;

		totalColumns = sheet.getRegionWidth() / frameWidth;
		totalRows = sheet.getRegionHeight() / frameHeight;
		totalFrames = totalColumns * totalRows;

		frames = new Sprite[totalFrames];

		for (int i = 0; i < totalFrames; i++) {
			final int x = getFrameX(i) * frameWidth;
			final int y = getFrameY(i) * frameHeight;
			if (sheet == null) {
				continue;
			}
			frames[i] = Mdx.graphics.newSprite(Mdx.graphics.newTextureRegion(sheet, x, y, frameWidth, frameHeight));
		}
	}

	public Sprite getSprite(int index) {
		return frames[index];
	}

	public Sprite getSprite(int x, int y) {
		final int index = (y * totalColumns) + x;
		return frames[index];
	}

	public int getFrameX(int index) {
		return index % totalColumns;
	}

	public int getFrameY(int index) {
		return index / totalColumns;
	}

	public int getTotalColumns() {
		return totalColumns;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public int getTotalFrames() {
		return totalFrames;
	}
}
