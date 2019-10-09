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
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Org.Mini2Dx.Core;
using Org.Mini2Dx.Core.Assets;
using Org.Mini2Dx.Core.Font;
using Org.Mini2Dx.Core.Graphics;
using Org.Mini2Dx.Core.Util;
using Color = Org.Mini2Dx.Core.Graphics.Color;
using Texture = Org.Mini2Dx.Core.Graphics.Texture;

namespace monogame.Graphics
{
    
    public class MonoGameSpriteCache : global::Java.Lang.Object, Org.Mini2Dx.Core.Graphics.SpriteCache
    {
        private struct SpriteCacheDrawingOperation
        {
            public int srcX, srcY;
            public int srcWidth, srcHeight;
            
            public float dstX, dstY;
            public float scaleX, scaleY;
            public float originX, originY;
            public float rotation;
            public bool flipX, flipY;
            public MonoGameColor color;
        }

        private class Cache : IDisposable
        {
            public static GraphicsDevice graphicsDevice;
            public readonly MonoGameTexture texture = new MonoGameTexture(new RenderTarget2D(graphicsDevice,
                Mdx.graphicsContext_.getWindowWidth(), Mdx.graphicsContext_.getWindowHeight(), false, SurfaceFormat.Color,
                graphicsDevice.PresentationParameters.DepthStencilFormat, 0, RenderTargetUsage.PreserveContents));
            public readonly List<SpriteCacheDrawingOperation> operations = new List<SpriteCacheDrawingOperation>();

            public void clear()
            {
                operations.Clear();
                texture.texture2D.SetData(new uint[texture.getWidth() * texture.getHeight()]);
            }

            public void Dispose()
            {
                operations.Clear();
                texture.dispose();
            }
        }
        private readonly GraphicsDevice _graphicsDevice;
        private readonly SpriteBatch _spriteBatch;
        private MonoGameColor _currentColor = new MonoGameColor(255, 255, 255, 255);
        private readonly List<Cache> _caches = new List<Cache>();
        private Shader _customShader;
        private int _currentCache = -1;
        private int _posX, _posY, _maxHeight;
        private Vector2 _sharedPositionVector = Vector2.Zero;
        private Rectangle _sharedSourceRectangle = Rectangle.Empty;

        public MonoGameSpriteCache(GraphicsDevice graphicsDevice)
        {
            _graphicsDevice = graphicsDevice;
            Cache.graphicsDevice = graphicsDevice;
            _spriteBatch = new SpriteBatch(graphicsDevice);
        }

        private void beginSpriteBatch()
        {
            _graphicsDevice.SetRenderTarget((RenderTarget2D)_caches[_currentCache].texture.texture2D);
            _spriteBatch.Begin(SpriteSortMode.Deferred, BlendState.NonPremultiplied, effect: (_customShader == null ? null : ((MonoGameShader) _customShader).shader));
            _graphicsDevice.SetRenderTarget(((MonoGameGraphics)Mdx.graphicsContext_)._currentRenderTarget);
            ((MonoGameGraphics)Mdx.graphicsContext_).updateClip();
        }

        private void endSpriteBatch()
        {
            _graphicsDevice.SetRenderTarget((RenderTarget2D)_caches[_currentCache].texture.texture2D);
            _spriteBatch.End();
            _graphicsDevice.SetRenderTarget(((MonoGameGraphics)Mdx.graphicsContext_)._currentRenderTarget);
            ((MonoGameGraphics)Mdx.graphicsContext_).updateClip();
        }

        private void add(Texture texture, int srcX, int srcY, int srcWidth, int srcHeight, out int posX, out int posY)
        {
            if (_posX + srcWidth > _caches[_currentCache].texture.getWidth())
            {
                _posX = 0;
                _posY += _maxHeight;
                _maxHeight = 0;
            }

            _sharedSourceRectangle.X = srcX;
            _sharedSourceRectangle.Y = srcY;
            _sharedSourceRectangle.Width = srcWidth;
            _sharedSourceRectangle.Height = srcHeight;
            _sharedPositionVector.X = _posX;
            _sharedPositionVector.Y = _posY;
            posX = _posX;
            posY = _posY;
            
            _spriteBatch.Draw(((MonoGameTexture) texture).texture2D, new Vector2(_posX, _posY), 
                new Rectangle(srcX, srcY, srcWidth, srcHeight), Microsoft.Xna.Framework.Color.White, 0f,
                Vector2.Zero, Vector2.One, SpriteEffects.None, 0f);
            _posX += srcWidth;
            _maxHeight = Math.Max(srcHeight, _maxHeight);
        }

        private void add(TextureRegion region, out int posX, out int posY)
        {
            add(region.getTexture(), region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), out posX, out posY);
        }
        
        public int add(Sprite sprite)
        {
            var operation = new SpriteCacheDrawingOperation
            {
                srcWidth = sprite.getRegionWidth(),
                srcHeight = sprite.getRegionHeight(),
                dstX = sprite.getX(),
                dstY = sprite.getY(),
                scaleX = sprite.getScaleX(),
                scaleY = sprite.getScaleY(),
                originX = sprite.getOriginX(),
                originY = sprite.getOriginY(),
                rotation = ((MonoGameSprite) sprite).getTotalRotation(),
                flipX = sprite.isFlipX(),
                flipY = sprite.isFlipY(),
                color = _currentColor
            };
            add(sprite, out operation.srcX, out operation.srcY);
            _caches[_currentCache].operations.Add(operation);
            return _caches[_currentCache].operations.Count - 1;
        }

        public int add(Texture texture, float x, float y)
        {
            return add(texture, x, y, 0, 0, texture.getWidth(), texture.getWidth());
        }

        public int add(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight)
        {
            return add(texture, x, y, 0, 0, texture.getWidth(), texture.getWidth(), false, false);
        }

        public int add(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight, bool flipX,
            bool flipY)
        {
            return add(texture, x, y, 0, 0, texture.getWidth(), texture.getWidth(), 1, 1,false, false);
        }

        public int add(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight, float scaleX,
            float scaleY, bool flipX, bool flipY)
        {
            return add(texture, x, y, 0, 0, texture.getWidth(), texture.getWidth(), 1, 1, 0, 0, 0, false, false);
        }

        public int add(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight, float scaleX,
            float scaleY, float originX, float originY, float rotation, bool flipX, bool flipY)
        {
            var operation = new SpriteCacheDrawingOperation
            {
                srcWidth = srcWidth,
                srcHeight = srcHeight,
                dstX = x,
                dstY = y,
                scaleX = scaleX,
                scaleY = scaleY,
                originX = originX,
                originY = originY,
                rotation = rotation,
                flipX = flipX,
                flipY = flipY,
                color = _currentColor
            };
            add(texture, srcX, srcY, srcWidth, srcHeight, out operation.srcX, out operation.srcY);
            _caches[_currentCache].operations.Add(operation);
            return _caches[_currentCache].operations.Count - 1;
        }

        public int add(TextureRegion region, float x, float y)
        {
            return add(region, x, y, 1, 1);
        }

        public int add(TextureRegion region, float x, float y, float scaleX, float scaleY)
        {
            return add(region, x, y, 1, 1, 0, 0, 0);
        }

        public int add(TextureRegion region, float x, float y, float scaleX, float scaleY, float originX, float originY,
            float rotation)
        {
            return add(region, x, y, 1, 1, 0, 0, 0, false, false);
        }

        public int add(TextureRegion region, float x, float y, float scaleX, float scaleY, float originX, float originY,
            float rotation, bool flipX, bool flipY)
        {
            var operation = new SpriteCacheDrawingOperation
            {
                srcWidth = region.getRegionWidth(),
                srcHeight = region.getRegionHeight(),
                dstX = x,
                dstY = y,
                scaleX = scaleX,
                scaleY = scaleY,
                originX = originX,
                originY = originY,
                rotation = rotation,
                flipX = flipX,
                flipY = flipY,
                color = _currentColor
            };
            add(region, out operation.srcX, out operation.srcY);
            _caches[_currentCache].operations.Add(operation);
            return _caches[_currentCache].operations.Count - 1;
        }

        public void clear()
        {
            endSpriteBatch();
            _caches[_currentCache].clear();
            beginSpriteBatch();
        }

        public void dispose()
        {
            for (var i = 0; i < _caches.Count; i++)
            {
                _caches[i].Dispose();
            }
            _spriteBatch.Dispose();
        }

        public void draw(Org.Mini2Dx.Core._Graphics g, int cacheID)
        {
            draw(g, cacheID, 0, _caches[cacheID].operations.Count);
        }

        public void draw(Org.Mini2Dx.Core._Graphics g, int cacheID, int offset, int length)
        {
            for (var i = offset; i < _caches[cacheID].operations.Count && i < offset + length; i++)
            {
                draw(g, _caches[cacheID].texture,_caches[cacheID].operations[i]);
            }
        }

        private static void draw(Org.Mini2Dx.Core._Graphics g, MonoGameTexture texture, SpriteCacheDrawingOperation op)
        {
            ((MonoGameGraphics) g).drawTexture(texture, op.dstX, op.dstY, op.srcX, op.srcY, op.srcWidth, op.srcHeight, op.scaleX, op.scaleY, op.originX, op.originY, op.rotation, op.flipX, op.flipY, op.color);
        }

        public void beginCache()
        {
            _caches.Add(new Cache());
            _currentCache = _caches.Count - 1;
            beginSpriteBatch();
        }

        public void beginCache(int cacheID)
        {
            _currentCache = cacheID;
            _caches[cacheID].clear();
            beginSpriteBatch();
        }

        public int endCache()
        {
            endSpriteBatch();
            var cacheID = _currentCache;
            _currentCache = -1;
            return cacheID;
        }

        public Color getColor()
        {
            return _currentColor.copy();
        }

        public void setColor(Color tint)
        {
            _currentColor = (MonoGameColor) _currentColor.copy();
            _currentColor.setR(tint.rf());
            _currentColor.setG(tint.gf());
            _currentColor.setB(tint.bf());
            _currentColor.setA(tint.af());
        }

        public void setColor(float r, float g, float b, float a)
        {
            _currentColor = (MonoGameColor) _currentColor.copy();
            _currentColor.setR(r);
            _currentColor.setG(g);
            _currentColor.setB(b);
            _currentColor.setA(a);
        }

        public Shader getCustomShader()
        {
            return _customShader;
        }

        public void setCustomShader(Shader s)
        {
            _customShader = s;
        }

        public bool isDrawing()
        {
            return _currentCache != -1;
        }
    }
}