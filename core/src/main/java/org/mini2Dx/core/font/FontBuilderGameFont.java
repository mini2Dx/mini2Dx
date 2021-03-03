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
import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.graphics.Color;
import org.mini2Dx.core.graphics.TextureRegion;
import org.mini2Dx.core.serialization.annotation.Field;
import org.mini2Dx.core.serialization.annotation.PostDeserialize;
import org.mini2Dx.gdx.utils.CharArray;
import org.mini2Dx.gdx.utils.IntIntMap;

/**
 * A {@link GameFont} made with <a href="https://github.com/andryblack/fontbuilder">FontBuilder</a>
 */
public class FontBuilderGameFont implements GameFont {
	private final FontBuilderGlyphLayout sharedGlyphLayout;
	private final FontParameters fontParameters;

	public FontBuilderGameFont(FontParameters fontParameters) {
		this.fontParameters = fontParameters;

		sharedGlyphLayout = (FontBuilderGlyphLayout) newGlyphLayout();
	}

	@Override
	public boolean loadInternal() {
		return false;
	}

	@Override
	public boolean loadExternal() {
		return false;
	}

	@Override
	public boolean load(AssetManager assetManager) {
		return false;
	}

	private void load(TextureRegion textureRegion) {

	}

	@Override
	public void draw(Graphics g, String str, float x, float y) {

	}

	@Override
	public void draw(Graphics g, String str, float x, float y, float targetWidth) {

	}

	@Override
	public void draw(Graphics g, String str, float x, float y, float targetWidth, int horizontalAlignment, boolean wrap) {

	}

	@Override
	public FontGlyphLayout newGlyphLayout() {
		return null;
	}

	@Override
	public FontGlyphLayout getSharedGlyphLayout() {
		return sharedGlyphLayout;
	}

	@Override
	public GameFontCache newCache() {
		return null;
	}

	@Override
	public Color getColor() {
		return null;
	}

	@Override
	public void setColor(Color color) {

	}

	@Override
	public float getLineHeight() {
		return 0;
	}

	@Override
	public float getCapHeight() {
		return 0;
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
	}
}
