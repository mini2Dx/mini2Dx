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
using java.lang;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using monogame.Graphics;
using org.mini2Dx.core;
using org.mini2Dx.core.font;
using org.mini2Dx.core.util;
using Color = org.mini2Dx.core.graphics.Color;

namespace monogame.Font
{
    class MonoGameGameFontCache : org.mini2Dx.core.font.GameFontCache
    {
        private struct MonoGameGameFontCacheDrawingOperation
        {
            public string text;
            public float x, y, targetWidth;
            public int horizontalAlign;
            public bool wrap;
            public Microsoft.Xna.Framework.Color color;
            public float alpha;
        }

        private readonly MonoGameGameFont _gameFont;
        private GraphicsDevice _graphicsDevice;
        private MonoGameTexture _gameFontCache;
        private SpriteBatch _spriteBatch;
        private Vector2 _position = Vector2.Zero;
        private Microsoft.Xna.Framework.Color _setColor = Microsoft.Xna.Framework.Color.Black;
        private LinkedList<MonoGameGameFontCacheDrawingOperation> _previousDrawingOperations = new LinkedList<MonoGameGameFontCacheDrawingOperation>();

        internal MonoGameGameFontCache(GameFont font)
        {
            _gameFont = (MonoGameGameFont) font;
            _graphicsDevice = ((MonoGameGraphics) Mdx.graphicsContext)._graphicsDevice;
            _spriteBatch = new SpriteBatch(_graphicsDevice);
            _gameFontCache = new MonoGameTexture(new RenderTarget2D(_graphicsDevice, Mdx.graphicsContext.getWindowWidth(), Mdx.graphicsContext.getWindowHeight(), false, SurfaceFormat.Color, _graphicsDevice.PresentationParameters.DepthStencilFormat, 0, RenderTargetUsage.PreserveContents));
            beginSpriteBatch();
        }

        private void beginSpriteBatch()
        {
            _graphicsDevice.SetRenderTarget((RenderTarget2D) _gameFontCache.texture2D);
            _spriteBatch.Begin();
            _graphicsDevice.SetRenderTarget(((MonoGameGraphics)Mdx.graphicsContext)._currentRenderTarget);
        }

        private void endSpriteBatch()
        {
            _graphicsDevice.SetRenderTarget((RenderTarget2D)_gameFontCache.texture2D);
            _spriteBatch.End();
            _graphicsDevice.SetRenderTarget(((MonoGameGraphics)Mdx.graphicsContext)._currentRenderTarget);
        }

        public void clear()
        {
            endSpriteBatch();
            clearRenderTarget();
            _previousDrawingOperations.Clear();
            beginSpriteBatch();
        }

        private void clearRenderTarget()
        {
            uint[] textureData = new uint[_gameFontCache.getWidth() * _gameFontCache.getHeight()];
            _gameFontCache.texture2D.SetData(textureData);
        }

        private void addText(MonoGameGameFontCacheDrawingOperation operation)
        {
            _gameFont.draw(_spriteBatch, operation.text, operation.targetWidth, operation.horizontalAlign, operation.wrap, new Vector2(operation.x, operation.y), operation.color * operation.alpha);
        }

        public void addText(CharSequence str, float x, float y, float targetWidth, int horizontalAlign, bool wrap)
        {
            var operation = new MonoGameGameFontCacheDrawingOperation()
            {
                text = str.toString(),
                x = x,
                y = y,
                targetWidth = targetWidth,
                horizontalAlign = horizontalAlign,
                wrap = wrap,
                color = _setColor,
                alpha = 1
            };
            _previousDrawingOperations.AddLast(operation);
            addText(operation);
        }

        public void addText(CharSequence str, float x, float y)
        {
            addText(str, x, y, _gameFont._spriteFont.MeasureString(str.toString()).X, Align.LEFT, true);
        }

        public void draw(org.mini2Dx.core.Graphics g)
        {
            endSpriteBatch();
            g.drawTexture(_gameFontCache, _position.X, _position.Y);
            beginSpriteBatch();
        }

        public void setText(CharSequence str, float x, float y)
        {
            clear();
            addText(str, x, y);
        }

        public void setText(CharSequence str, float x, float y, float targetWidth, int horizontalAlign, bool wrap)
        {
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
            endSpriteBatch();
            clearRenderTarget();
            beginSpriteBatch();
            var node = _previousDrawingOperations.First;
            while (node != null)
            {
                var operation = node.Value;
                operation.color = ((MonoGameColor)color).toMonoGameColor();
                addText(operation);
                node.Value = operation;
                node = node.Next;
            }
        }

        public void setAllAlphas(float alpha)
        {
            endSpriteBatch();
            clearRenderTarget();
            beginSpriteBatch();
            var node = _previousDrawingOperations.First;
            while (node != null)
            {
                var operation = node.Value;
                operation.alpha = alpha;
                addText(operation);
                node.Value = operation;
                node = node.Next;
            }
        }

        public void setColor(Color color)
        {
            _setColor = ((MonoGameColor)color).toMonoGameColor();
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
}
