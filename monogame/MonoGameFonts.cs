/*******************************************************************************
 * Copyright 2019 Viridian Software Limited
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

using monogame.Files;
using monogame.Font;
using org.mini2Dx.core;
using org.mini2Dx.core.files;
using org.mini2Dx.core.font;

namespace monogame
{
    public class MonoGameFonts : org.mini2Dx.core.Fonts
    {
        public override GameFont defaultFont()
        {
            return newPlatformFont((MonoGameFileHandle)Mdx.files.@internal("defaultFont.spritefont"));
        }

        public override GameFont newPlatformFont(FileHandle fh)
        {
            var font = new MonoGameGameFont((MonoGameFileHandle) fh);
            font.loadInternal();
            return font;
        }

        public override GameFont newBitmapFont(FileHandle fh)
        {
            return MonoGameGameFont.loadBitmapFont((MonoGameFileHandle) fh);
        }

        public override GameFont newTrueTypeFont(FileHandle fh)
        {
            return new MonoGameGameFont((MonoGameFileHandle) fh, 12);
        }
    }
}
