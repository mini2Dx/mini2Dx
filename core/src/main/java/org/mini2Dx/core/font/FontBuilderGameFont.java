/*******************************************************************************
 * Copyright 2021 See AUTHORS file
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
import org.mini2Dx.core.collections.CharMap;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.files.FileType;
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

import java.io.IOException;

/**
 * A {@link GameFont} made with <a href="https://github.com/andryblack/fontbuilder">FontBuilder</a>
 */
public class FontBuilderGameFont implements GameFont {
	private static final String LOGGING_TAG = FontBuilderGameFont.class.getSimpleName();
	private static final FontBuilderXmlReader XML_READER = new FontBuilderXmlReader();

	private final FontBuilderGlyphLayout sharedGlyphLayout;
	final FontParameters fontParameters;

	final FontProperties fontProperties = new FontProperties();
	final CharMap<FontBuilderChar> charMap = new CharMap<>();

	private Color color = Mdx.graphics.newColor(0f, 0f, 0f, 1f);

	public FontBuilderGameFont(FontParameters fontParameters) {
		this.fontParameters = fontParameters;

		sharedGlyphLayout = (FontBuilderGlyphLayout) newGlyphLayout();
	}

	@Override
	public boolean loadInternal() {
		if(fontParameters.xmlFileHandleType != null && !fontParameters.xmlFileHandleType.equals(FileType.INTERNAL)) {
			throw new MdxException("Attempting to use loadInternal() but font is set to file handle type " + fontParameters.xmlFileHandleType);
		}
		return load(Mdx.files.internal(fontParameters.xmlPath), Mdx.graphics.newTextureRegion(
				Mdx.graphics.newTexture(Mdx.files.internal(fontParameters.texturePath))));
	}

	@Override
	public boolean loadExternal() {
		if(fontParameters.xmlFileHandleType != null && !fontParameters.xmlFileHandleType.equals(FileType.EXTERNAL)) {
			throw new MdxException("Attempting to use loadExternal() but font is set to file handle type " + fontParameters.xmlFileHandleType);
		}
		return load(Mdx.files.external(fontParameters.xmlPath), Mdx.graphics.newTextureRegion(
				Mdx.graphics.newTexture(Mdx.files.external(fontParameters.texturePath))));
	}

	@Override
	public boolean load(AssetManager assetManager) {
		if(fontParameters.xmlFileHandleType == null) {
			throw new MdxException("Attempting to use load(AssetManager) but xmlFileHandleType is not set");
		}
		final FileHandle xmlFileHandle;
		final TextureRegion textureRegion;

		switch (fontParameters.xmlFileHandleType) {
		case EXTERNAL:
			xmlFileHandle = Mdx.files.external(fontParameters.xmlPath);
			break;
		case LOCAL:
			xmlFileHandle = Mdx.files.local(fontParameters.xmlPath);
			break;
		default:
		case INTERNAL:
			xmlFileHandle = Mdx.files.internal(fontParameters.xmlPath);
			break;
		}

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
		return load(xmlFileHandle, textureRegion);
	}

	private boolean load(FileHandle xmlFileHandle, TextureRegion textureRegion) {
		if(charMap.size > 0) {
			return true;
		}
		try {
			XML_READER.read(xmlFileHandle, fontProperties, charMap);
		} catch (Exception e) {
			Mdx.log.error(LOGGING_TAG, e.getMessage(), e);
			return false;
		}

		for(FontBuilderChar fontChar : charMap.values()) {
			fontChar.textureRegion = Mdx.graphics.newTextureRegion(textureRegion,
					fontChar.rectX, fontChar.rectY, fontChar.rectWidth, fontChar.rectHeight);
		}
		return true;
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

	public void draw(Graphics g, String str, float x, float y, float renderWidth, MonospaceGameFont.FontRenderListener listener) {
		draw(g, str, x, y, renderWidth, Align.LEFT, true, listener);
	}

	public void draw(Graphics g, String str, float x, float y, float renderWidth, int horizontalAlignment, boolean wrap, MonospaceGameFont.FontRenderListener listener) {
		sharedGlyphLayout.setText(str, color, renderWidth, horizontalAlignment, wrap);
		draw(g, sharedGlyphLayout.getGlyphs(), x, y, listener);
	}

	public void draw(Graphics g, Array<FontBuilderGlyph> glyphs, float x, float y, MonospaceGameFont.FontRenderListener listener) {
		final Color previousTint = g.getTint();
		for(int i = 0; i < glyphs.size; i++) {
			final FontBuilderGlyph glyph = glyphs.get(i);
			if(glyph.glyphChar == null) {
				continue;
			}
			if(glyph.glyphChar.textureRegion == null) {
				continue;
			}

			final float renderX = x + glyph.x + glyph.glyphChar.offsetX;
			final float renderY = y + glyph.y + glyph.glyphChar.offsetY;

			g.setTint(glyph.color);
			if(listener == null) {
				g.drawTextureRegion(glyph.glyphChar.textureRegion, renderX, renderY);
			} else {
				if(listener.preRenderChar(g, glyph.glyphChar.code, renderX, renderY, glyph.glyphChar.rectWidth, glyph.glyphChar.rectHeight)) {
					g.drawTextureRegion(glyph.glyphChar.textureRegion, renderX, renderY);
				}
				listener.postRenderChar(g, glyph.glyphChar.code, renderX, renderY, glyph.glyphChar.rectWidth, glyph.glyphChar.rectHeight);
			}
		}
		g.setTint(previousTint);
	}

	@Override
	public FontGlyphLayout newGlyphLayout() {
		return new FontBuilderGlyphLayout(this);
	}

	@Override
	public FontGlyphLayout getSharedGlyphLayout() {
		return sharedGlyphLayout;
	}

	@Override
	public GameFontCache newCache() {
		return new FontBuilderGameFontCache(this);
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
		return fontProperties.height;
	}

	@Override
	public float getCapHeight() {
		return fontProperties.height;
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

	public static class FontParameters {
		@Field(optional = true)
		public String textureAtlasPath;
		@Field(optional = true)
		public String texturePath;
		@Field
		public String xmlPath;
		@Field(optional = true)
		public FileType xmlFileHandleType;
	}

	public static class FontBuilderChar {
		public char code;
		public int width;
		public int offsetX, offsetY;
		public int rectX, rectY, rectWidth, rectHeight;

		public TextureRegion textureRegion;
	}

	public static class FontProperties {
		public int size;
		public String family;
		public int height;
		public String style;
	}
}
