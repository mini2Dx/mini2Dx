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
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using monogame.Graphics;
using Org.Mini2Dx.Core;
using Org.Mini2Dx.Gdx.Math;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;
using Rectangle = Microsoft.Xna.Framework.Rectangle;
using Vector2 = Microsoft.Xna.Framework.Vector2;

namespace monogame.Font
{
    public class MonoGameFontCacheTexture
    {
        private const int TEXTURE_WIDTH = 2048;
        private const int TEXTURE_HEIGHT = 2048;
        private const int REGION_WIDTH = 32;
        private const int REGION_HEIGHT = 16;
        private const int REGION_ARRAY_WIDTH = TEXTURE_WIDTH / REGION_WIDTH;
        private const int REGION_ARRAY_HEIGHT = TEXTURE_HEIGHT / REGION_HEIGHT;

        private int _totalIds = 0;
        private Dictionary<int, MonoGameTextureRegion> _caches = new Dictionary<int, MonoGameTextureRegion>();
        private List<MonoGameTextureRegion> _gc = new List<MonoGameTextureRegion>();
        private short[,] _regions = new short[REGION_ARRAY_WIDTH, REGION_ARRAY_HEIGHT];
        private MonoGameTexture _texture;
        private GraphicsDevice _graphicsDevice;
        private SpriteBatch _spriteBatch;

        public MonoGameFontCacheTexture()
        {
            _graphicsDevice = ((MonoGameGraphics)Mdx.graphicsContext_)._graphicsDevice;
            _spriteBatch = new SpriteBatch(_graphicsDevice);
            _texture = new MonoGameTexture(new RenderTarget2D(_graphicsDevice, TEXTURE_WIDTH, TEXTURE_HEIGHT, false, SurfaceFormat.Color, _graphicsDevice.PresentationParameters.DepthStencilFormat, 0, RenderTargetUsage.PreserveContents));

            for(int x = 0; x < REGION_ARRAY_WIDTH; x++)
            {
                for(int y = 0; y < REGION_ARRAY_HEIGHT; y++)
                {
                    _regions[x, y] = -1;
                }
            }
        }

        public int allocateId()
        {
            int result = _totalIds++;
            _caches.Add(result, null);
            return result;
        }

        public void begin()
        {
            _graphicsDevice.SetRenderTarget((RenderTarget2D)_texture.texture2D);
            _spriteBatch.Begin();
            _graphicsDevice.SetRenderTarget(((MonoGameGraphics)Mdx.graphicsContext_)._currentRenderTarget);
        }

        public void end()
        {
            _graphicsDevice.SetRenderTarget((RenderTarget2D)_texture.texture2D);
            _spriteBatch.End();
            _graphicsDevice.SetRenderTarget(((MonoGameGraphics)Mdx.graphicsContext_)._currentRenderTarget);
        }

        public void clear()
        {
            begin();
            _graphicsDevice.SetRenderTarget((RenderTarget2D)_texture.texture2D);
            _spriteBatch.GraphicsDevice.Clear(Microsoft.Xna.Framework.Color.Transparent);
            _graphicsDevice.SetRenderTarget(((MonoGameGraphics)Mdx.graphicsContext_)._currentRenderTarget);
            end();

            _gc.Clear();
            _caches.Clear();
            for (int x = 0; x < REGION_ARRAY_WIDTH; x++)
            {
                for (int y = 0; y < REGION_ARRAY_HEIGHT; y++)
                {
                    _regions[x, y] = -1;
                }
            }
        }

        public void setText(MonoGameGameFontCache fontCache)
        {
            if(_caches.ContainsKey(fontCache.cacheId) && _caches[fontCache.cacheId] != null)
            {
                //TODO: Implement some way to clear old texture regions
                _gc.Add(_caches[fontCache.cacheId]);
                _caches.Remove(fontCache.cacheId);
            }

            Rectangle rect = determineRequiredArea(fontCache);
            if(!allocateTextureRegionXY(fontCache.cacheId, ref rect))
            {
                clear();
                setText(fontCache);
                return;
            }

            begin();
            for (int i = 0; i < fontCache._previousDrawingOperations.Count; i++)
            {
                MonoGameGameFontCacheDrawingOperation operation = fontCache._previousDrawingOperations[i];
                fontCache._gameFont.draw(_spriteBatch, operation.text, operation.targetWidth, operation.horizontalAlign, operation.wrap, new Vector2(rect.X + operation.x, rect.Y + operation.y), operation.color * operation.alpha);
            }
            end();

            _caches[fontCache.cacheId] = new MonoGameTextureRegion(_texture, rect.X, rect.Y, rect.Width, rect.Height);
        }

        public void draw(Org.Mini2Dx.Core._Graphics g, MonoGameGameFontCache fontCache, Vector2 position)
        {
            int cacheId = fontCache.cacheId;
            if(fontCache._previousDrawingOperations.Count == 0)
            {
                return;
            }
            if (!_caches.ContainsKey(cacheId) || _caches[cacheId] == null)
            {
                setText(fontCache);
            }

            MonoGameTextureRegion textureRegion = _caches[cacheId];
            g.drawTextureRegion(textureRegion, position.X, position.Y);
        }

        public void clear(int cacheId)
        {
            if (!_caches.ContainsKey(cacheId))
            {
                return;
            }
            _caches.Remove(cacheId);
        }

        private Rectangle determineRequiredArea(MonoGameGameFontCache cache)
        {
            int maxX = 0;
            int maxY = 0;

            for(int i = 0; i < cache._previousDrawingOperations.Count; i++)
            {
                int currentMaxX = MathUtils.round(cache._previousDrawingOperations[i].x +
                    cache._previousDrawingOperations[i].targetWidth);
                int currentMaxY = MathUtils.round(cache._previousDrawingOperations[i].y +
                    cache._previousDrawingOperations[i].targetHeight);

                maxX = Math.Max(maxX, currentMaxX);
                maxY = Math.Max(maxY, currentMaxY);
            }

            return new Rectangle(0, 0, maxX, maxY);
        }

        private bool allocateTextureRegionXY(int cacheId, ref Rectangle area)
        {
            int requiredSlotsX = (area.Width + (REGION_WIDTH - (area.Width % REGION_WIDTH))) / REGION_WIDTH;
            int requiredSlotsY = (area.Height + (REGION_HEIGHT - (area.Height % REGION_HEIGHT))) / REGION_HEIGHT;

            for (int y = 0; y < REGION_ARRAY_HEIGHT; y++)
            {
                for (int x = 0; x < REGION_ARRAY_WIDTH; x++)
                {
                    if (_regions[x, y] >= 0)
                    {
                        continue;
                    }
                    if (isFree(x, y, requiredSlotsX, requiredSlotsY))
                    {
                        area.X = x * REGION_WIDTH;
                        area.Y = y * REGION_HEIGHT;

                        for (int x1 = x; x1 < x + requiredSlotsX; x1++)
                        {
                            for (int y1 = y; y1 < y + requiredSlotsY; y1++)
                            {
                                _regions[x1, y1] = (short)cacheId;
                            }
                        }
                        return true;
                    }
                }
            }
            return false;
        }

        private bool isFree(int x, int y, int width, int height)
        {
            if(x + width >= REGION_ARRAY_WIDTH)
            {
                return false;
            }
            if(y + height >= REGION_ARRAY_HEIGHT)
            {
                return false;
            }
            for(int x1 = x; x1 < x + width; x1++)
            {
                for(int y1 = y; y1 < y + height && y1 < REGION_ARRAY_HEIGHT; y1++)
                {
                    if(_regions[x1, y1] >= 0)
                    {
                        return false;
                    }
                }
            }
            return true;
        }
    }
}
