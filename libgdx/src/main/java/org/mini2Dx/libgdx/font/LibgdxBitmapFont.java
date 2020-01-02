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
package org.mini2Dx.libgdx.font;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Align;
import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.font.FontGlyphLayout;
import org.mini2Dx.core.font.GameFont;
import org.mini2Dx.core.font.GameFontCache;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.libgdx.LibgdxGraphics;
import org.mini2Dx.libgdx.files.LibgdxFileHandle;
import org.mini2Dx.libgdx.graphics.GdxTextureRegion;
import org.mini2Dx.libgdx.graphics.LibgdxColor;
import org.mini2Dx.libgdx.graphics.LibgdxTextureRegion;

public class LibgdxBitmapFont implements GameFont {
	public final com.badlogic.gdx.graphics.g2d.BitmapFont bitmapFont;
	private final BitmapFontGlyphLayout sharedGlyphLayout;

	private final Color tmpColor;

	public LibgdxBitmapFont() {
		bitmapFont = new com.badlogic.gdx.graphics.g2d.BitmapFont(true);
		sharedGlyphLayout = (BitmapFontGlyphLayout) newGlyphLayout();
		tmpColor = new LibgdxColor(bitmapFont.getColor());
	}

	public LibgdxBitmapFont(FileHandle fileHandle) {
		bitmapFont = new com.badlogic.gdx.graphics.g2d.BitmapFont(((LibgdxFileHandle) fileHandle).fileHandle, true);
		sharedGlyphLayout = (BitmapFontGlyphLayout) newGlyphLayout();
		tmpColor = new LibgdxColor(bitmapFont.getColor());
	}

	public LibgdxBitmapFont (BitmapFont.BitmapFontData data, TextureRegion region, boolean integer) {
		this(data, region != null ? Array.with(region) : null, integer);
	}

	public LibgdxBitmapFont (BitmapFont.BitmapFontData data, Array<TextureRegion> pageRegions, boolean integer) {
		bitmapFont = new com.badlogic.gdx.graphics.g2d.BitmapFont(data, convert(pageRegions), integer);
		sharedGlyphLayout = (BitmapFontGlyphLayout) newGlyphLayout();
		tmpColor = new LibgdxColor(bitmapFont.getColor());
	}

	public LibgdxBitmapFont(com.badlogic.gdx.graphics.g2d.BitmapFont bitmapFont) {
		this.bitmapFont = bitmapFont;
		sharedGlyphLayout = (BitmapFontGlyphLayout) newGlyphLayout();
		tmpColor = new LibgdxColor(bitmapFont.getColor());
	}

	@Override
	public boolean loadInternal() {
		return true;
	}

	@Override
	public boolean loadExternal() {
		return true;
	}

	@Override
	public boolean load(AssetManager assetManager) {
		return true;
	}

	@Override
	public void draw(Graphics g, String str, float x, float y) {
		bitmapFont.draw(((LibgdxGraphics) g).spriteBatch, str, x, y);
	}

	@Override
	public void draw(Graphics g, String str, float x, float y, float targetWidth) {
		draw(g, str, x, y, targetWidth, Align.left, true);
	}

	@Override
	public void draw(Graphics g, String str, float x, float y, float targetWidth, int horizontalAlignment, boolean wrap) {
		bitmapFont.draw(((LibgdxGraphics) g).spriteBatch, str, x, y, targetWidth, horizontalAlignment, wrap);
	}

	@Override
	public FontGlyphLayout newGlyphLayout() {
		return new BitmapFontGlyphLayout(this);
	}

	@Override
	public FontGlyphLayout getSharedGlyphLayout() {
		return sharedGlyphLayout;
	}

	@Override
	public GameFontCache newCache() {
		return new LibgdxBitmapFontCache(this, useIntegerPositions());
	}

	@Override
	public Color getColor() {
		return tmpColor;
	}

	@Override
	public void setColor(Color color) {
		bitmapFont.setColor(color.rf(), color.gf(), color.bf(), color.af());
		tmpColor.set(color);
	}

	@Override
	public float getLineHeight() {
		return bitmapFont.getLineHeight();
	}

	@Override
	public float getCapHeight() {
		return bitmapFont.getCapHeight();
	}

	@Override
	public boolean useIntegerPositions() {
		return bitmapFont.usesIntegerPositions();
	}

	@Override
	public void dispose() {
		bitmapFont.dispose();
	}

	private static com.badlogic.gdx.utils.Array<com.badlogic.gdx.graphics.g2d.TextureRegion> convert(Array<TextureRegion> pageRegions) {
		final com.badlogic.gdx.utils.Array<com.badlogic.gdx.graphics.g2d.TextureRegion>	 result = new  com.badlogic.gdx.utils.Array<com.badlogic.gdx.graphics.g2d.TextureRegion>();
		for(int i = 0; i < pageRegions.size; i++) {
			final GdxTextureRegion gdxTextureRegion = (GdxTextureRegion) pageRegions.get(i);
			result.add(gdxTextureRegion.asGdxTextureRegion());
		}
		return result;
	}
}
