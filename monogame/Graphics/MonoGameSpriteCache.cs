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
                Mdx.graphicsContext_.getWindowWidth_0EE0D08D(), Mdx.graphicsContext_.getWindowHeight_0EE0D08D(), false, SurfaceFormat.Color,
                graphicsDevice.PresentationParameters.DepthStencilFormat, 0, RenderTargetUsage.PreserveContents));
            public readonly List<SpriteCacheDrawingOperation> operations = new List<SpriteCacheDrawingOperation>();

            public void clear()
            {
                operations.Clear();
                texture.texture2D.SetData(new uint[texture.getWidth_0EE0D08D() * texture.getHeight_0EE0D08D()]);
            }

            public void Dispose()
            {
                operations.Clear();
                texture.dispose_EFE09FC0();
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
            _spriteBatch.Begin(SpriteSortMode.Deferred, BlendState.NonPremultiplied, effect: (_customShader == null ? null : ((MonoGameShader)_customShader).shader));
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
            if (_posX + srcWidth > _caches[_currentCache].texture.getWidth_0EE0D08D())
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

            _spriteBatch.Draw(((MonoGameTexture)texture).texture2D, new Vector2(_posX, _posY),
                new Rectangle(srcX, srcY, srcWidth, srcHeight), Microsoft.Xna.Framework.Color.White, 0f,
                Vector2.Zero, Vector2.One, SpriteEffects.None, 0f);
            _posX += srcWidth;
            _maxHeight = Math.Max(srcHeight, _maxHeight);
        }

        private void add(TextureRegion region, out int posX, out int posY)
        {
            add(region.getTexture_D75719FD(), region.getRegionX_0EE0D08D(), region.getRegionY_0EE0D08D(), region.getRegionWidth_0EE0D08D(), region.getRegionHeight_0EE0D08D(), out posX, out posY);
        }

        public int add_52534258(Sprite sprite)
        {
            var operation = new SpriteCacheDrawingOperation
            {
                srcWidth = sprite.getRegionWidth_0EE0D08D(),
                srcHeight = sprite.getRegionHeight_0EE0D08D(),
                dstX = sprite.getX_FFE0B8F0(),
                dstY = sprite.getY_FFE0B8F0(),
                scaleX = sprite.getScaleX_FFE0B8F0(),
                scaleY = sprite.getScaleY_FFE0B8F0(),
                originX = sprite.getOriginX_FFE0B8F0(),
                originY = sprite.getOriginY_FFE0B8F0(),
                rotation = ((MonoGameSprite)sprite).getTotalRotation(),
                flipX = sprite.isFlipX_FBE0B2A4(),
                flipY = sprite.isFlipY_FBE0B2A4(),
                color = _currentColor
            };
            add(sprite, out operation.srcX, out operation.srcY);
            _caches[_currentCache].operations.Add(operation);
            return _caches[_currentCache].operations.Count - 1;
        }

        public int add_B2EC8EDA(Texture texture, float x, float y)
        {
            return add_DD5B35CA(texture, x, y, 0, 0, texture.getWidth_0EE0D08D(), texture.getHeight_0EE0D08D());
        }

        public int add_DD5B35CA(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight)
        {
            return add_11B17DBA(texture, x, y, 0, 0, texture.getWidth_0EE0D08D(), texture.getHeight_0EE0D08D(), false, false);
        }

        public int add_11B17DBA(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight, bool flipX,
            bool flipY)
        {
            return add_86EC8950(texture, x, y, 0, 0, texture.getWidth_0EE0D08D(), texture.getHeight_0EE0D08D(), 1, 1, flipX, flipY);
        }

        public int add_86EC8950(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight, float scaleX,
            float scaleY, bool flipX, bool flipY)
        {
            return add_F9EB320A(texture, x, y, 0, 0, texture.getWidth_0EE0D08D(), texture.getHeight_0EE0D08D(), 1, 1, 0, 0, 0, flipX, flipY);
        }

        public int add_F9EB320A(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight, float scaleX,
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

        public int add_AA22D178(Texture arg0, float arg1, float arg2, int arg3, int arg4, int arg5, int arg6, float arg7, float arg8, float arg9, float arg10, float arg11, bool arg12, bool arg13)
        {
            throw new NotImplementedException();
        }

        public int add_F9EB320A(Texture arg0, float arg1, float arg2, int arg3, int arg4, int arg5, int arg6, float arg7, float arg8, bool arg9, bool arg10)
        {
            throw new NotImplementedException();
        }

        public int add_D40A76E2(TextureRegion region, float x, float y)
        {
            return add_0C25A2F2(region, x, y, 1, 1);
        }

        public int add_0C25A2F2(TextureRegion region, float x, float y, float scaleX, float scaleY)
        {
            return add_B3F74400(region, x, y, 1, 1, 0, 0, 0);
        }

        public int add_B3F74400(TextureRegion region, float x, float y, float scaleX, float scaleY, float originX, float originY,
            float rotation)
        {
            return add_86EC8950(region, x, y, 1, 1, 0, 0, 0, false, false);
        }

        public int add_86EC8950(TextureRegion region, float x, float y, float scaleX, float scaleY, float originX, float originY,
            float rotation, bool flipX, bool flipY)
        {
            var operation = new SpriteCacheDrawingOperation
            {
                srcWidth = region.getRegionWidth_0EE0D08D(),
                srcHeight = region.getRegionHeight_0EE0D08D(),
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

        public void clear_EFE09FC0()
        {
            endSpriteBatch();
            _caches[_currentCache].clear();
            beginSpriteBatch();
        }

        public void dispose_EFE09FC0()
        {
            for (var i = 0; i < _caches.Count; i++)
            {
                _caches[i].Dispose();
            }
            _spriteBatch.Dispose();
        }

        public void draw_3AB38A38(Org.Mini2Dx.Core._Graphics g, int cacheID)
        {
            draw_B16CC9E0(g, cacheID, 0, _caches[cacheID].operations.Count);
        }

        public void draw_B16CC9E0(Org.Mini2Dx.Core._Graphics g, int cacheID, int offset, int length)
        {
            for (var i = offset; i < _caches[cacheID].operations.Count && i < offset + length; i++)
            {
                draw(g, _caches[cacheID].texture, _caches[cacheID].operations[i]);
            }
        }

        private static void draw(Org.Mini2Dx.Core._Graphics g, MonoGameTexture texture, SpriteCacheDrawingOperation op)
        {
            ((MonoGameGraphics)g).drawTexture(texture, op.dstX, op.dstY, op.srcX, op.srcY, op.srcWidth, op.srcHeight, op.scaleX, op.scaleY, op.originX, op.originY, op.rotation, op.flipX, op.flipY, op.color);
        }

        public void beginCache_EFE09FC0()
        {
            _caches.Add(new Cache());
            _currentCache = _caches.Count - 1;
            beginSpriteBatch();
        }

        public void beginCache_3518BA33(int cacheID)
        {
            _currentCache = cacheID;
            _caches[cacheID].clear();
            beginSpriteBatch();
        }

        public int endCache_0EE0D08D()
        {
            endSpriteBatch();
            var cacheID = _currentCache;
            _currentCache = -1;
            return cacheID;
        }

        public Color getColor_F0D7D9CF()
        {
            return _currentColor.copy_F0D7D9CF();
        }

        public void setColor_24D51C91(Color tint)
        {
            _currentColor = (MonoGameColor)_currentColor.copy_F0D7D9CF();
            _currentColor.setR_97413DCA(tint.rf_FFE0B8F0());
            _currentColor.setG_97413DCA(tint.gf_FFE0B8F0());
            _currentColor.setB_97413DCA(tint.bf_FFE0B8F0());
            _currentColor.setA_97413DCA(tint.af_FFE0B8F0());
        }

        public void setColor_C2EDAFC0(float r, float g, float b, float a)
        {
            _currentColor = (MonoGameColor)_currentColor.copy_F0D7D9CF();
            _currentColor.setR_97413DCA(r);
            _currentColor.setG_97413DCA(g);
            _currentColor.setB_97413DCA(b);
            _currentColor.setA_97413DCA(a);
        }

        public Shader getCustomShader_364FDDC3()
        {
            return _customShader;
        }

        public void setCustomShader_09F44B85(Shader s)
        {
            _customShader = s;
        }

        public bool isDrawing_FBE0B2A4()
        {
            return _currentCache != -1;
        }
    }
}