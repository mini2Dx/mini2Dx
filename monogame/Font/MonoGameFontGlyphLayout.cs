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

using java.lang;
using Microsoft.Xna.Framework;
using org.mini2Dx.core.font;
using Color = org.mini2Dx.core.graphics.Color;
using Math = System.Math;

namespace monogame.Font
{
    public class MonoGameFontGlyphLayout : org.mini2Dx.core.font.FontGlyphLayout
    {
        private readonly MonoGameGameFont _font;
        private static readonly Vector2 defaultValue = new Vector2(-1, -1);
        private Vector2 _textSize = defaultValue; 

        public MonoGameFontGlyphLayout(MonoGameGameFont font)
        {
            _font = font;
        }
        
        public void setText(CharSequence cs)
        {
            _textSize.X = _font._spriteFont.MeasureString(cs.toString()).X;
            _textSize.Y = _font.getCapHeight();
            for (var i = 0; i < cs.length(); i++)
            {
                if (cs.charAt(i) == '\n' && i + 1 < cs.length())
                {
                    _textSize.Y += _font.getCapHeight();
                }
            }
        }

        public void setText(CharSequence cs, Color color, float targetWidth, int align, bool wrap)
        {
            setText(_font.wrapText(cs.toString(), targetWidth + 1));
            if (!wrap)
            {
                _textSize.Y = _font.getCapHeight();
            }
        }

        public void reset()
        {
            _textSize = defaultValue;
        }

        public void dispose()
        {
            
        }

        public float getWidth()
        {
            return _textSize.X;
        }

        public float getHeight()
        {
            return _textSize.Y;
        }

        public GameFont getFont()
        {
            return _font;
        }
    }
}