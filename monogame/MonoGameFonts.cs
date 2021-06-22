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
using Org.Mini2Dx.Core;
using Org.Mini2Dx.Core.Files;
using Org.Mini2Dx.Core.Font;

namespace monogame
{
    public class MonoGameFonts : Org.Mini2Dx.Core.Fonts
    {
        public override GameFont defaultFont_0370ED29()
        {
            return newPlatformFont_4C54323B((MonoGameFileHandle)Mdx.files_.internal_1F3F44D2("defaultFont.spritefont"));
        }

        public override GameFont newPlatformFont_4C54323B(FileHandle fh)
        {
            var font = new MonoGameGameFont((MonoGameFileHandle) fh);
            font.loadInternal_FBE0B2A4();
            return font;
        }

        public override GameFont newBitmapFont_4C54323B(FileHandle fh)
        {
            return MonoGameGameFont.loadBitmapFont((MonoGameFileHandle) fh);
        }

        public override GameFont newTrueTypeFont_4C54323B(FileHandle fh)
        {
            return new MonoGameGameFont((MonoGameFileHandle) fh, 12);
        }
    }
}
