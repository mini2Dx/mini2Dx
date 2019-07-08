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
package org.mini2Dx.libgdx;

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import org.mini2Dx.core.Fonts;
import org.mini2Dx.core.files.FileHandle;
import org.mini2Dx.core.font.GameFont;
import org.mini2Dx.libgdx.files.LibgdxFileHandle;
import org.mini2Dx.libgdx.font.LibgdxBitmapFont;

public class LibgdxFonts extends Fonts {
	private static GameFont DEFAULT_FONT;

	@Override
	public GameFont defaultFont() {
		if(DEFAULT_FONT == null) {
			DEFAULT_FONT = new LibgdxBitmapFont();
		}
		return DEFAULT_FONT;
	}

	@Override
	public GameFont newPlatformFont(FileHandle fileHandle) {
		if(fileHandle.path().endsWith(".ttf")) {
			final LibgdxFileHandle gdxFileHandle = (LibgdxFileHandle) fileHandle;
			FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
			fontParameter.size = 12;
			fontParameter.flip = true;
			fontParameter.kerning = true;
			FreeTypeFontGenerator freeTypeFontGenerator = new FreeTypeFontGenerator(gdxFileHandle.fileHandle);
			return new LibgdxBitmapFont(freeTypeFontGenerator.generateFont(fontParameter));
		}
		return new LibgdxBitmapFont(fileHandle);
	}

	@Override
	public GameFont newTrueTypeFont(FileHandle fileHandle) {
		return newPlatformFont(fileHandle);
	}

	@Override
	public GameFont newBitmapFont(FileHandle fntFileHandle) {
		return new LibgdxBitmapFont(fntFileHandle);
	}
}
