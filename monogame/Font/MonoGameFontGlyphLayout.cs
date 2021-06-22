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

using Java.Lang;
using Microsoft.Xna.Framework;
using Org.Mini2Dx.Core.Font;
using Color = Org.Mini2Dx.Core.Graphics.Color;
using Math = System.Math;

namespace monogame.Font
{
    public class MonoGameFontGlyphLayout : global::Java.Lang.Object, FontGlyphLayout
    {
        private readonly MonoGameGameFont _font;
        private static readonly Vector2 defaultValue = new Vector2(-1, -1);
        private Vector2 _textSize = defaultValue; 

        public MonoGameFontGlyphLayout(MonoGameGameFont font)
        {
            _font = font;
        }
        
        public void setText_C7B6A388(CharSequence cs)
        {
            _textSize.X = _font._spriteFont.MeasureString(cs.toString_E605312C()).X;
            _textSize.Y = _font.getCapHeight_FFE0B8F0();
            for (var i = 0; i < cs.length_0EE0D08D(); i++)
            {
                if (cs.charAt_4818D81C(i) == '\n' && i + 1 < cs.length_0EE0D08D())
                {
                    _textSize.Y += _font.getCapHeight_FFE0B8F0();
                }
            }
        }

        public void setText_8167018A(CharSequence cs, Color color, float targetWidth, int align, bool wrap)
        {
            setText_C7B6A388(_font.wrapText(cs.toString_E605312C(), targetWidth + 1));
            if (!wrap)
            {
                _textSize.Y = _font.getCapHeight_FFE0B8F0();
            }
        }

        public void reset_EFE09FC0()
        {
            _textSize = defaultValue;
        }

        public void dispose_EFE09FC0()
        {
            
        }

        public float getWidth_FFE0B8F0()
        {
            return _textSize.X;
        }

        public float getHeight_FFE0B8F0()
        {
            return _textSize.Y;
        }

        public GameFont getFont_0370ED29()
        {
            return _font;
        }
    }
}