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

using System;
using System.Collections.Generic;
using Java.Lang;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using monogame.Graphics;
using Org.Mini2Dx.Core;
using Org.Mini2Dx.Core.Assets;
using Org.Mini2Dx.Core.Font;
using Org.Mini2Dx.Core.Util;
using Color = Org.Mini2Dx.Core.Graphics.Color;

namespace monogame.Font
{
    public class MonoGameGameFontCache : global::Java.Lang.Object, GameFontCache
    {
        private static MonoGameFontCacheTexture GLOBAL_CACHE = null;

        public readonly MonoGameGameFont _gameFont;
        public readonly List<MonoGameGameFontCacheDrawingOperation> _previousDrawingOperations = new List<MonoGameGameFontCacheDrawingOperation>();

        private Vector2 _position = Vector2.Zero;
        public Microsoft.Xna.Framework.Color _setColor = Microsoft.Xna.Framework.Color.Black;
        public int cacheId;

        internal MonoGameGameFontCache(GameFont font)
        {
            _gameFont = (MonoGameGameFont) font;

            if(GLOBAL_CACHE == null)
            {
                GLOBAL_CACHE = new MonoGameFontCacheTexture();
            }

            cacheId = GLOBAL_CACHE.allocateId();
            GLOBAL_CACHE.begin();
        }

        public void clear()
        {
            GLOBAL_CACHE.clear(cacheId);
            _previousDrawingOperations.Clear();
        }

        private void updateCache()
        {
            GLOBAL_CACHE.setText(this);
        }

        public void addText(CharSequence str, float x, float y, float targetWidth, int horizontalAlign, bool wrap)
        {
            Vector2 v2 = _gameFont._spriteFont.MeasureString(str.toString());
            addText(str, x, y, targetWidth, v2.Y, horizontalAlign, wrap);
        }

        public void addText(CharSequence str, float x, float y, float targetWidth, float targetHeight, int horizontalAlign, bool wrap)
        {
            Vector2 v2 = _gameFont._spriteFont.MeasureString(str.toString());
            var operation = new MonoGameGameFontCacheDrawingOperation()
            {
                text = str.toString(),
                x = x,
                y = y,
                targetWidth = targetWidth,
                targetHeight = targetHeight,
                horizontalAlign = horizontalAlign,
                wrap = wrap,
                color = _setColor,
                alpha = 1
            };
            _previousDrawingOperations.Add(operation);
            updateCache();
        }

        public void addText(CharSequence str, float x, float y)
        {
            Vector2 v2 = _gameFont._spriteFont.MeasureString(str.toString());
            addText(str, x, y, v2.X, v2.Y, Align.LEFT_, true);
        }

        public void draw(Org.Mini2Dx.Core._Graphics g)
        {
            GLOBAL_CACHE.draw(g, this, _position);
        }

        public void setText(CharSequence str, float x, float y)
        {
            if(_previousDrawingOperations.Count == 1)
            {
                MonoGameGameFontCacheDrawingOperation previousOperation = _previousDrawingOperations[0];
                if (previousOperation.text.Equals((string)str.toString()) &&
                    previousOperation.x == x && previousOperation.y == y &&
                    previousOperation.color.Equals(_setColor))
                {
                    return;
                }
            }
            clear();
            addText(str, x, y);
        }

        public void setText(CharSequence str, float x, float y, float targetWidth, int horizontalAlign, bool wrap)
        {
            if (_previousDrawingOperations.Count == 1)
            {
                MonoGameGameFontCacheDrawingOperation previousOperation = _previousDrawingOperations[0];
                if (previousOperation.text.Equals((string)str.toString()) &&
                    previousOperation.x == x && previousOperation.y == y &&
                    previousOperation.color.Equals(_setColor) &&
                    previousOperation.targetWidth.Equals(targetWidth) &&
                    previousOperation.horizontalAlign == horizontalAlign &&
                    previousOperation.wrap == wrap)
                {
                    return;
                }
            }
            clear();
            addText(str, x, y, targetWidth, horizontalAlign, wrap);
        }

        public void translate(float x, float y)
        {
            _position.X += x;
            _position.Y += y;
        }

        public void setPosition(float x, float y)
        {
            _position.X = x;
            _position.Y = y;
        }

        public void setAllColors(Color color)
        {
            GLOBAL_CACHE.clear(cacheId);
            for(int i = 0; i < _previousDrawingOperations.Count; i++)
            {
                MonoGameGameFontCacheDrawingOperation op = _previousDrawingOperations[i];
                op.color = ((MonoGameColor)color)._color;
            }
            updateCache();
        }

        public void setAllAlphas(float alpha)
        {
            GLOBAL_CACHE.clear(cacheId);
            for (int i = 0; i < _previousDrawingOperations.Count; i++)
            {
                MonoGameGameFontCacheDrawingOperation op = _previousDrawingOperations[i];
                op.alpha = alpha;
            }
            updateCache();
        }

        public void setColor(Color color)
        {
            _setColor = ((MonoGameColor)color)._color;
        }

        public Color getColor()
        {
            return new MonoGameColor(_setColor);
        }

        public GameFont getFont()
        {
            return _gameFont;
        }
    }

    public struct MonoGameGameFontCacheDrawingOperation
    {
        public string text;
        public float x, y, targetWidth, targetHeight;
        public int horizontalAlign;
        public bool wrap;
        public Microsoft.Xna.Framework.Color color;
        public float alpha;
    }
}
