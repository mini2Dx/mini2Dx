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
package org.mini2Dx.core;

import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.font.GameFont;
import org.mini2Dx.core.font.MonospaceGameFont;
import org.mini2Dx.core.graphics.Texture;
import org.mini2Dx.core.graphics.TextureRegion;

public abstract class Fonts {
	/**
	 * Returns the default {@link GameFont} for the current {@link Platform}
	 * @return The default font
	 */
	public abstract GameFont defaultFont();

	/**
	 * Loads a font using the current {@link Platform}'s native format.
	 * Note that the file type support and rendering made be inconsistent across platforms when using this.
	 * It is mainly used to get up and running quickly before switching to a bitmap or monospace font.
	 * @param fileHandle The {@link FileHandle} for the file
	 * @return A new {@link GameFont} instance
	 */
	public abstract GameFont newPlatformFont(FileHandle fileHandle);

	/**
	 * Loads a font made using <a href="http://www.angelcode.com/products/bmfont/">BMFont</a>
	 * @param fntFileHandle A .fnt file
	 * @return A new {@link GameFont} instance
	 */
	public abstract GameFont newBitmapFont(FileHandle fntFileHandle);

	/**
	 * Creates a new {@link MonospaceGameFont} instance. {@link GameFont#load(AssetManager)},
	 * {@link GameFont#loadInternal()} or {@link GameFont#loadExternal()} must be called afterwards.
	 * @param fontParameters The {@link org.mini2Dx.core.font.MonospaceGameFont.FontParameters}
	 * @return A new {@link MonospaceGameFont} instance
	 */
	public GameFont newMonospaceFont(MonospaceGameFont.FontParameters fontParameters) {
		return new MonospaceGameFont(fontParameters);
	}
}
