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
using Microsoft.Xna.Framework;
using Java.Lang;
using Org.Mini2Dx.Core;
using Org.Mini2Dx.Core.Assets;
using Org.Mini2Dx.Core.Font;
using Org.Mini2Dx.Core.Util;
using Color = Microsoft.Xna.Framework.Color;
using Texture = Org.Mini2Dx.Core.Graphics.Texture;
using Org.Mini2Dx.Core.Graphics;

namespace monogame.Graphics
{
    public class MonoGameTextureRegion : global::Java.Lang.Object, Org.Mini2Dx.Core.Graphics.TextureRegion
    {
        private Texture _texture;
        private float _u, _v, _u2, _v2;
        private int _regionWidth, _regionHeight;

        public MonoGameTextureRegion()
        {
            
        }
        
        public MonoGameTextureRegion(Texture texture)
        {
            _texture = texture ?? throw new Java.Lang.IllegalArgumentException();
            setRegion(0, 0, texture.getWidth(), texture.getHeight());
        }

        public MonoGameTextureRegion(Texture texture, int x, int y, int width, int height)
        {
            _texture = texture ?? throw new Java.Lang.IllegalArgumentException();
            setRegion(x, y, width, height);
        }

        public MonoGameTextureRegion(TextureRegion region)
        {
            setRegion(region);
        }

        public MonoGameTextureRegion(TextureRegion region, int x, int y, int width, int height)
        {
            setRegion(region, x, y, width, height);
        }

        public MonoGameTextureRegion(TextureRegion region, int width, int height) : this(region, 0, 0, width, height) {}
        public MonoGameTextureRegion(Texture texture, int width, int height) : this(texture, 0, 0, width, height) {}

        public void setRegion(Texture texture)
        {
            _texture = texture;
            setRegion(0, 0, texture.getWidth(), texture.getHeight());
        }

        public void setRegion(int x, int y, int width, int height)
        {
            var invTexWidth = 1f / _texture.getWidth();
            var invTexHeight = 1f / _texture.getHeight();
            setRegion(x * invTexWidth, y * invTexHeight, (x + width) * invTexWidth, (y + height) * invTexHeight);
            _regionWidth = System.Math.Abs(width);
            _regionHeight = System.Math.Abs(height);
        }

        public void setRegion(float u, float v, float u2, float v2)
        {
            int texWidth = _texture.getWidth(), texHeight = _texture.getHeight();
            _regionWidth = (int) System.Math.Round(System.Math.Abs(u2 - u) * texWidth);
            _regionHeight = (int) System.Math.Round(System.Math.Abs(v2 - v) * texHeight);

            if (_regionWidth == 1 && _regionHeight == 1)
            {
                var adjustX = 0.25f / texWidth;
                u += adjustX;
                u2 -= adjustX;
                var adjustY = 0.25f / texHeight;
                v += adjustY;
                v2 -= adjustY;
            }

            _u = u;
            _v = v;
            _u2 = u2;
            _v2 = v2;
        }

        public void setRegion(TextureRegion region)
        {
            _texture = region.getTexture();
            setRegion(region.getU(), region.getV(), region.getU2(), region.getV2());
        }

        public void setRegion(TextureRegion region, int x, int y, int width, int height)
        {
            _texture = region.getTexture();
            setRegion(region.getRegionX() + x, region.getRegionY() + y, width, height);
        }

        public Texture getTexture()
        {
            return _texture;
        }

        public virtual void setTexture(Texture texture)
        {
            _texture = texture;
        }

        public float getU()
        {
            return _u;
        }

        public void setU(float u)
        {
            _u = u;
            _regionWidth = (int) System.Math.Round(System.Math.Abs(_u2 - _u) * _texture.getWidth());
        }

        public float getV()
        {
            return _v;
        }

        public void setV(float v)
        {
            _v = v;
            _regionHeight = (int) System.Math.Round(System.Math.Abs(_v2 - _v) * _texture.getHeight());
        }

        public float getU2()
        {
            return _u2;
        }

        public void setU2(float u2)
        {
            _u2 = u2;
            _regionWidth = (int) System.Math.Round(System.Math.Abs(_u2 - _u) * _texture.getWidth());
        }

        public float getV2()
        {
            return _v2;
        }

        public void setV2(float v2)
        {
            _v2 = v2;
            _regionHeight = (int) System.Math.Round(System.Math.Abs(_v2 - _v) * _texture.getHeight());
        }

        public int getRegionX()
        {
            return (int) System.Math.Round(System.Math.Min(_u, _u2) * _texture.getWidth());
        }

        public void setRegionX(int x)
        {
            setU(x / (float) _texture.getWidth());
        }

        public int getRegionY()
        {
            var result = (int) System.Math.Round(System.Math.Min(_v, _v2) * _texture.getHeight());
            return result;
        }

        public void setRegionY(int y)
        {
            setV(y / (float) _texture.getHeight());
        }

        public int getRegionWidth()
        {
            return _regionWidth;
        }

        public void setRegionWidth(int width)
        {
            if (isFlipX()) {
                setU(_u2 + width / (float)_texture.getWidth());
            } else {
                setU2(_u + width / (float)_texture.getWidth());
            }
        }

        public int getRegionHeight()
        {
            return _regionHeight;
        }

        public void setRegionHeight(int height)
        {
            if (isFlipY()) {
                setV(_v2 + height / (float)_texture.getHeight());			
            } else {
                setV2(_v + height / (float)_texture.getHeight());
            }
        }

        public void flip(bool x, bool y)
        {
            if (x) {
                var temp = _u;
                _u = _u2;
                _u2 = temp;
            }
            if (y) {
                var temp = _v;
                _v = _v2;
                _v2 = temp;
            }
        }

        public void setFlip(bool x, bool y)
        {
            setFlipX(x);
            setFlipY(y);
        }

        public bool isFlipX()
        {
            return _u > _u2;
        }

        public void setFlipX(bool b)
        {
            if (isFlipX() != b)
                flip(true, false);
        }

        public bool isFlipY()
        {
            return _v > _v2;
        }

        public void setFlipY(bool b)
        {
            if (isFlipY() != b)
                flip(false, true);
        }

        public void scroll(float xAmount, float yAmount)
        {
            if (xAmount != 0) {
                var width = (_u2 - _u) * _texture.getWidth();
                _u = (_u + xAmount) % 1;
                _u2 = _u + width / _texture.getWidth();
            }
            if (yAmount != 0) {
                var height = (_v2 - _v) * _texture.getHeight();
                _v = (_v + yAmount) % 1;
                _v2 = _v + height / _texture.getHeight();
            }
        }

        public Pixmap toPixmap()
        {
            var pixmap = new MonoGamePixmap(_regionWidth, _regionHeight);

            var texture2d = ((MonoGameTexture) _texture).texture2D;
            var rawTextureRegionData = new Color[_regionWidth * _regionHeight];

            Rectangle boundingRect = new Rectangle(getRegionX(), getRegionY(), _regionWidth, _regionHeight);
            
            bool flipX = isFlipX();
            bool flipY = isFlipY();
            
            if (flipX)
            {
                boundingRect.X -= _regionWidth;
            }

            if (flipY)
            {
                boundingRect.Y -= _regionHeight;
            }
            
            texture2d.GetData(0, boundingRect, rawTextureRegionData, 0, _regionWidth * _regionHeight);
            for (var x = 0; x < _regionWidth; x++)
            {
                for (var y = 0; y < _regionHeight; y++)
                {
                    var actualX = x;
                    var actualY = y;
                    if (flipX)
                    {
                        actualX = _regionWidth - x;
                    }
                    
                    if (flipY)
                    {
                        actualY = _regionHeight - y;
                    }
                    pixmap.drawPixel(actualX, actualY, MonoGameColor.toRGBA8888(rawTextureRegionData[x + y * _regionWidth]));
                }
            }
            
            return pixmap;
        }

        public TextureRegion[][] split(int tileWidth, int tileHeight)
        {
            var x = getRegionX();
            var y = getRegionY();
            var width = _regionWidth;
            var height = _regionHeight;

            var rows = height / tileHeight;
            var cols = width / tileWidth;

            var startX = x;
            var tiles = new TextureRegion[rows][];
            
            for (var row = 0; row < rows; row++, y += tileHeight) {
                tiles[row] = new TextureRegion[cols];
                x = startX;
                for (var col = 0; col < cols; col++, x += tileWidth) {
                    tiles[row][col] = new MonoGameTextureRegion(_texture, x, y, tileWidth, tileHeight);
                }
            }
            return tiles;
        }
    }
}