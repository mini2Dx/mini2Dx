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
package org.mini2Dx.core.font;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.graphics.Texture;
import org.mini2Dx.core.graphics.TextureAtlas;
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.core.serialization.annotation.PostDeserialize;
import org.mini2Dx.core.util.Align;
import org.mini2Dx.gdx.utils.Array;
import org.mini2Dx.gdx.utils.CharArray;
import org.mini2Dx.gdx.utils.IntIntMap;

/**
 * A {@link GameFont} where every character is a fixed-width which simplifies rendering calculations
 */
public class MonospaceGameFont implements GameFont {
	private final MonospaceFontGlyphLayout sharedGlyphLayout;
	private final FontParameters fontParameters;
	private TextureRegion [][] characterTextures;
	private int charactersPerRow, charactersPerColumn;
	private Color color = Mdx.graphics.newColor(0, 0, 0, 0);

	public MonospaceGameFont(FontParameters fontParameters) {
		super();
		this.fontParameters = fontParameters;

		if(fontParameters.frameWidth <= 0) {
			throw new MdxException("fontParameters.frameWidth must be greater than 0");
		} else if(fontParameters.frameHeight <= 0) {
			throw new MdxException("fontParameters.frameHeight must be greater than 0");
		}

		if(fontParameters.characterWidth < 0) {
			fontParameters.characterWidth = fontParameters.frameWidth;
		}
		if(fontParameters.lineHeight < 0) {
			fontParameters.lineHeight = fontParameters.frameHeight;
		}

		sharedGlyphLayout = (MonospaceFontGlyphLayout) newGlyphLayout();
	}

	/**
	 * Loads the {@link GameFont} using {@link org.mini2Dx.core.Files#internal(String)}.
	 * The texture path must be set in the {@link FontParameters}.
	 * @return True on success
	 */
	@Override
	public boolean loadInternal() {
		return load(Mdx.files.internal(fontParameters.texturePath));
	}

	/**
	 * Loads the {@link GameFont} using {@link org.mini2Dx.core.Files#external(String)}.
	 * The texture path must be set in the {@link FontParameters}.
	 * @return True on success
	 */
	@Override
	public boolean loadExternal() {
		return load(Mdx.files.external(fontParameters.texturePath));
	}

	/**
	 * Loads the {@link GameFont} using the
	 * @param textureHandle
	 * @return True on success
	 */
	public boolean load(FileHandle textureHandle) {
		if(characterTextures != null) {
			return true;
		}
		load(Mdx.graphics.newTextureRegion(Mdx.graphics.newTexture(textureHandle)));
		return true;
	}

	@Override
	public boolean load(AssetManager assetManager) {
		if(characterTextures != null) {
			return true;
		}
		final TextureRegion textureRegion;

		if(fontParameters.textureAtlasPath != null) {
			if(!assetManager.isLoaded(fontParameters.textureAtlasPath)) {
				assetManager.load(fontParameters.textureAtlasPath, TextureAtlas.class);
				return false;
			}
			final TextureAtlas textureAtlas = assetManager.get(fontParameters.textureAtlasPath, TextureAtlas.class);
			if(textureAtlas == null) {
				throw new MdxException("No such texture atlas '" + fontParameters.textureAtlasPath + "'");
			}
			textureRegion = Mdx.graphics.newTextureRegion(textureAtlas.findRegion(fontParameters.texturePath));
		} else {
			if(!assetManager.isLoaded(fontParameters.texturePath)) {
				assetManager.load(fontParameters.texturePath, Texture.class);
				return false;
			}
			textureRegion = Mdx.graphics.newTextureRegion(assetManager.get(fontParameters.texturePath, Texture.class));
		}

		load(textureRegion);
		return true;
	}

	private void load(TextureRegion textureRegion) {
		charactersPerRow = textureRegion.getRegionWidth() / fontParameters.frameWidth;
		charactersPerColumn = textureRegion.getRegionHeight() / fontParameters.frameHeight;

		characterTextures = new TextureRegion[charactersPerRow][charactersPerColumn];
		for(int x = 0; x < charactersPerRow; x++) {
			for(int y = 0; y < charactersPerColumn; y++) {
				final int textureX = (x * fontParameters.frameWidth) + fontParameters.framePaddingLeft;
				final int textureY = (y * fontParameters.frameHeight) + fontParameters.framePaddingTop;
				final int textureWidth = fontParameters.frameWidth - fontParameters.framePaddingLeft - fontParameters.framePaddingRight;
				final int textureHeight = fontParameters.frameHeight - fontParameters.framePaddingTop - fontParameters.framePaddingBottom;

				characterTextures[x][y] = Mdx.graphics.newTextureRegion(textureRegion, textureX, textureY, textureWidth, textureHeight);
			}
		}
	}

	@Override
	public void draw(Graphics g, String str, float x, float y) {
		draw(g, str, x, y, -1f);
	}

	@Override
	public void draw(Graphics g, String str, float x, float y, float renderWidth) {
		draw(g, str, x, y, renderWidth, Align.LEFT, true);
	}

	@Override
	public void draw(Graphics g, String str, float x, float y, float renderWidth, int horizontalAlignment, boolean wrap) {
		draw(g, str, x, y, renderWidth, horizontalAlignment, wrap, null);
	}

	public void draw(Graphics g, String str, float x, float y, float renderWidth, FontRenderListener listener) {
		draw(g, str, x, y, renderWidth, Align.LEFT, true, listener);
	}

	public void draw(Graphics g, String str, float x, float y, float renderWidth, int horizontalAlignment, boolean wrap, FontRenderListener listener) {
		sharedGlyphLayout.setText(str, color, renderWidth, horizontalAlignment, wrap);
		draw(g, sharedGlyphLayout.getGlyphs(), x, y, listener);
	}

	public void draw(Graphics g, Array<MonospaceGlyph> glyphs, float x, float y, FontRenderListener listener) {
		final float charRenderWidth = fontParameters.characterWidth;
		final float charRenderHeight = fontParameters.lineHeight;

		final Color previousTint = g.getTint();
		for(int i = 0; i < glyphs.size; i++) {
			final MonospaceGlyph glyph = glyphs.get(i);
			if(glyph.textureRegion == null) {
				continue;
			}

			final float renderX = x + glyph.x;
			final float renderY = y + glyph.y;

			g.setTint(glyph.color);
			if(listener == null) {
				g.drawTextureRegion(glyph.textureRegion, renderX, renderY);
			} else {
				if(listener.preRenderChar(g, glyph.glyphChar, renderX, renderY, charRenderWidth, charRenderHeight)) {
					g.drawTextureRegion(glyph.textureRegion, renderX, renderY);
				}
				listener.postRenderChar(g, glyph.glyphChar, renderX, renderY, charRenderWidth, charRenderHeight);
			}
		}
		g.setTint(previousTint);
	}

	@Override
	public FontGlyphLayout newGlyphLayout() {
		return new MonospaceFontGlyphLayout(this);
	}

	@Override
	public FontGlyphLayout getSharedGlyphLayout() {
		return sharedGlyphLayout;
	}

	@Override
	public GameFontCache newCache() {
		return new MonospaceGameFontCache(this);
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void setColor(Color color) {
		if(color == null) {
			return;
		}
		this.color = color;
	}

	@Override
	public float getLineHeight() {
		return fontParameters.lineHeight;
	}

	@Override
	public float getCapHeight() {
		return fontParameters.lineHeight;
	}

	private int getXIndex(int index, int charactersPerRow) {
		return index % charactersPerRow;
	}

	private int getYIndex(int index, int charactersPerRow) {
		return index / charactersPerRow;
	}

	public TextureRegion getTextureRegion(char c) {
		if(characterTextures == null) {
			return null;
		}
		final int xIndex, yIndex;

		if(fontParameters.overrideCharacterIndices != null) {
			final int index = fontParameters.overrideCharacterIndices.get(c, -1);
			if(index < 0) {
				return null;
			}
			xIndex = getXIndex(index, charactersPerRow);
			yIndex = getYIndex(index, charactersPerRow);
		} else {
			xIndex = getXIndex(c, charactersPerRow);
			yIndex = getYIndex(c, charactersPerRow);
		}

		return characterTextures[xIndex][yIndex];
	}

	@Override
	public boolean useIntegerPositions() {
		return true;
	}

	@Override
	public void dispose() {
		if(sharedGlyphLayout != null) {
			sharedGlyphLayout.dispose();
		}
	}

	public FontParameters getFontParameters() {
		return fontParameters;
	}

	public static class FontParameters {
		@Field(optional = true)
		public String textureAtlasPath;
		@Field(optional = true)
		public String texturePath;
		@Field
		public int frameWidth;
		@Field
		public int frameHeight;
		@Field
		public int framePaddingLeft;
		@Field
		public int framePaddingRight;
		@Field
		public int framePaddingTop;
		@Field
		public int framePaddingBottom;
		@Field(optional = true)
		public int characterWidth = -1;
		@Field(optional = true)
		public int lineHeight = -1;
		@Field(optional = true)
		public int spacing = 1;
		@Field(optional = true)
		public IntIntMap overrideCharacterIndices;
		@Field(optional = true)
		public CharArray overrideCharacterIndicesList;

		@PostDeserialize
		public void postDeserialize() {
			if(overrideCharacterIndicesList != null) {
				overrideCharacterIndices = new IntIntMap();
				for(int i = 0; i < overrideCharacterIndicesList.size; i++) {
					overrideCharacterIndices.put(overrideCharacterIndicesList.get(i), i);
				}
				overrideCharacterIndicesList.clear();
			}
		}
	}

	public interface FontRenderListener {

		/**
		 * Called before rendering a character to screen
		 * @param g The {@link Graphics} context
		 * @param c The text character to be rendered
		 * @param charRenderX The x coordinate the character will render at
		 * @param charRenderY The y coordinate the character will render at
		 * @param charRenderWidth The frame width of the character
		 * @param charRenderHeight The frame height of the character
		 * @return True if the character should be rendered, otherwise false
		 */
		public boolean preRenderChar(Graphics g, char c, float charRenderX, float charRenderY, float charRenderWidth, float charRenderHeight);

		/**
		 * Called after rendering a character to screen. <br><br>Note: If {@link #preRenderChar(Graphics, char, float, float, float, float)} returns false, this method will still be called.
		 * @param g The {@link Graphics} context
		 * @param c The text character rendered
		 * @param charRenderX The x coordinate the character was rendered at
		 * @param charRenderY The y coordinate the character was rendered at
		 * @param charRenderWidth The frame width of the character
		 * @param charRenderHeight The frame height of the character
		 */
		public void postRenderChar(Graphics g, char c, float charRenderX, float charRenderY, float charRenderWidth, float charRenderHeight);
	}
}