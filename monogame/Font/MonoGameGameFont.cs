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

using System.Collections.Generic;
using System.Text;
using Java.Lang;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using monogame.Files;
using monogame.Graphics;
using Org.Mini2Dx.Core;
using Org.Mini2Dx.Core.Assets;
using Org.Mini2Dx.Core.Font;
using Org.Mini2Dx.Core.Util;
using SpriteFontPlus;
using Color = Org.Mini2Dx.Core.Graphics.Color;

namespace monogame.Font
{
    public class MonoGameGameFont : global::Java.Lang.Object, GameFont
    {
        internal SpriteFont _spriteFont;
        private FontGlyphLayout _sharedFontGlyphLayout;
        private float _capHeight;
        private Microsoft.Xna.Framework.Color _color = Microsoft.Xna.Framework.Color.Black;
        private readonly MonoGameFileHandle _fileHandle;

        private MonoGameGameFont()
        {
            
        }

        public MonoGameGameFont(MonoGameFileHandle fileHandle)
        {
            _fileHandle = fileHandle;
        }


        public static MonoGameGameFont loadBitmapFont(MonoGameFileHandle fntFileHandle)
        { 
            var font = new MonoGameGameFont();
            font._spriteFont = BMFontLoader.LoadXml(fntFileHandle.readString_E605312C(), textureFileName =>
            {
                return new TextureWithOffset(((MonoGameTexture)Mdx.graphics_.newTexture_69120FDF(fntFileHandle.parent_D17169DC().child_1F3F44D2(textureFileName))).texture2D);
            });
            font._sharedFontGlyphLayout = font.newGlyphLayout_2AEFE927();
            font._capHeight = font._spriteFont.GetGlyphs()['A'].BoundsInTexture.Height;
            return font;
        }

        public MonoGameGameFont(MonoGameFileHandle ttfFileHandle, int size)
        {
            size++;
            _spriteFont = TtfFontBaker.Bake(ttfFileHandle.readBytesAsByteArray(),
                size,
                1024,
                1024,
                new[]
                {
                    CharacterRange.BasicLatin,
                    CharacterRange.Latin1Supplement,
                    CharacterRange.LatinExtendedA,
                    CharacterRange.Cyrillic
                }
            ).CreateSpriteFont(((MonoGameGraphics)Mdx.graphicsContext_)._graphicsDevice);
            _sharedFontGlyphLayout = newGlyphLayout_2AEFE927();
            _capHeight = _spriteFont.GetGlyphs()['A'].BoundsInTexture.Height;
        }

        public bool load_DFE6C4BD(AssetManager am)
        {
            return false;
        }

        public bool loadInternal_FBE0B2A4()
        {
            _spriteFont = _fileHandle.loadFromContentManager<SpriteFont>();
            _sharedFontGlyphLayout = newGlyphLayout_2AEFE927();
            _capHeight = _spriteFont.GetGlyphs()['A'].BoundsInTexture.Height;
            return true;
        }

        public bool loadExternal_FBE0B2A4()
        {
            return false;
        }

        public void draw_FB6AB899(_Graphics g, String str, float x, float y)
        {
            _sharedFontGlyphLayout.setText_C7B6A388(str);
            draw_5881F77F(g, str, x, y, _sharedFontGlyphLayout.getWidth_FFE0B8F0());
            _sharedFontGlyphLayout.reset_EFE09FC0();
        }

        public void draw_5881F77F(_Graphics g, String str, float x, float y, float targetWidth)
        {
            draw_F97A968A(g, str, x, y, targetWidth, Align.LEFT_, true);
        }

        internal void draw(SpriteBatch spriteBatch, String str, float targetWidth, int horizontalAlignment, bool wrap, Vector2 position, Microsoft.Xna.Framework.Color renderColor)
        {
            var wrapStr = (string)wrapText(str, targetWidth + 1);
            var strings = wrapStr.Split('\n');
            var origin = Vector2.Zero;
            if ((horizontalAlignment & Align.RIGHT_) != 0)
            {
                origin.X = _spriteFont.MeasureString(strings[0]).X;
                position.X += targetWidth;
            }
            else if ((horizontalAlignment & Align.CENTER_) != 0)
            {
                origin.X = (int)(_spriteFont.MeasureString(strings[0]).X / 2);
                position.X += (int)(targetWidth / 2);
            }
            spriteBatch.DrawString(_spriteFont,
                strings[0],
                 position,
                renderColor,
                0,
                origin,
                Vector2.One, 
                SpriteEffects.None,
                0f);

            if (wrap)
            {
                for (var i = 1; i < strings.Length; i++)
                {
                    if ((horizontalAlignment & Align.RIGHT_) != 0)
                    {
                        origin.X = _spriteFont.MeasureString(strings[i]).X;
                    }
                    else if ((horizontalAlignment & Align.CENTER_) != 0)
                    {
                        origin.X = (int)(_spriteFont.MeasureString(strings[i]).X / 2);
                    }
                    position.Y += getLineHeight_FFE0B8F0();
                    spriteBatch.DrawString(_spriteFont,
                        strings[i],
                        position,
                        renderColor,
                        0,
                        origin,
                        Vector2.One, 
                        SpriteEffects.None,
                        0f);
                }
            }
        }

        public void draw_F97A968A(_Graphics g, String str, float x, float y, float targetWidth, int horizontalAlignment, bool wrap)
        {
            var graphics = (MonoGameGraphics)g;
            draw(graphics._spriteBatch, str, targetWidth, horizontalAlignment, wrap,
                new Vector2(x, y), 
                new Microsoft.Xna.Framework.Color {
                    R = (byte)(graphics._tint.R / 255.0f * _color.R),
                    G = (byte)(graphics._tint.G / 255.0f * _color.G),
                    B = (byte)(graphics._tint.B / 255.0f * _color.B),
                    A = (byte)(graphics._tint.A / 255.0f * _color.A)
                });
        }

        public FontGlyphLayout newGlyphLayout_2AEFE927()
        {
            return new MonoGameFontGlyphLayout(this);
        }

        public GameFontCache newCache_8BFDA5A5()
        {
            return new MonoGameGameFontCache(this);
        }

        public FontGlyphLayout getSharedGlyphLayout_2AEFE927()
        {
            return _sharedFontGlyphLayout;
        }

        public Color getColor_F0D7D9CF()
        {
            return new MonoGameColor(_color);
        }

        public void setColor_24D51C91(Color c)
        {
            _color = ((MonoGameColor) c)._color;
        }

        public float getLineHeight_FFE0B8F0()
        {
            return _spriteFont.LineSpacing;
        }

        public float getCapHeight_FFE0B8F0()
        {
            return _capHeight;
        }

        public bool useIntegerPositions_FBE0B2A4()
        {
            return false;
        }

        public void dispose_EFE09FC0()
        {
            _sharedFontGlyphLayout.dispose_EFE09FC0();
        }

        internal String wrapText(string text, float maxLineWidth) {
            if(_spriteFont.MeasureString(text).X < maxLineWidth) {
                return text;
            }

            var words = text.Split(' ');
            var wrappedText = new System.Text.StringBuilder();
            var lineWidth = 0f;
            var spaceWidth = _spriteFont.MeasureString(" ").X;
            
            for(var i = 0; i < words.Length; ++i) {
                Vector2 v = _spriteFont.MeasureString(words[i]);
                if(lineWidth + v.X < maxLineWidth) {
                    lineWidth += v.X + spaceWidth;
                } else {
                    wrappedText.Append("\n");
                    lineWidth = v.X + spaceWidth;
                }
                wrappedText.Append(words[i]);
                wrappedText.Append(" ");
            }

            return wrappedText.ToString();
        }
    }
}