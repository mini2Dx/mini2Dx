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
            if(texture == null)
            {
                throw new Java.Lang.IllegalArgumentException();
            }
            _texture = texture;
            setRegion_9C90BED0(0, 0, texture.getWidth_0EE0D08D(), texture.getHeight_0EE0D08D());
        }

        public MonoGameTextureRegion(Texture texture, int x, int y, int width, int height)
        {
            if (texture == null)
            {
                throw new Java.Lang.IllegalArgumentException();
            }
            _texture = texture;
            setRegion_9C90BED0(x, y, width, height);
        }

        public MonoGameTextureRegion(TextureRegion region)
        {
            setRegion_EBB2FEFB(region);
        }

        public MonoGameTextureRegion(TextureRegion region, int x, int y, int width, int height)
        {
            setRegion_9F8BE18B(region, x, y, width, height);
        }

        public MonoGameTextureRegion(TextureRegion region, int width, int height) : this(region, 0, 0, width, height) {}
        public MonoGameTextureRegion(Texture texture, int width, int height) : this(texture, 0, 0, width, height) {}

        public void setRegion_1B161583(Texture texture)
        {
            _texture = texture;
            setRegion_9C90BED0(0, 0, texture.getWidth_0EE0D08D(), texture.getHeight_0EE0D08D());
        }

        public void setRegion_9C90BED0(int x, int y, int width, int height)
        {
            var invTexWidth = 1f / _texture.getWidth_0EE0D08D();
            var invTexHeight = 1f / _texture.getHeight_0EE0D08D();
            setRegion_C2EDAFC0(x * invTexWidth, y * invTexHeight, (x + width) * invTexWidth, (y + height) * invTexHeight);
            _regionWidth = System.Math.Abs(width);
            _regionHeight = System.Math.Abs(height);
        }

        public void setRegion_C2EDAFC0(float u, float v, float u2, float v2)
        {
            int texWidth = _texture.getWidth_0EE0D08D(), texHeight = _texture.getHeight_0EE0D08D();
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

        public void setRegion_EBB2FEFB(TextureRegion region)
        {
            _texture = region.getTexture_D75719FD();
            setRegion_C2EDAFC0(region.getU_FFE0B8F0(), region.getV_FFE0B8F0(), region.getU2_FFE0B8F0(), region.getV2_FFE0B8F0());
        }

        public void setRegion_9F8BE18B(TextureRegion region, int x, int y, int width, int height)
        {
            _texture = region.getTexture_D75719FD();
            setRegion_9C90BED0(region.getRegionX_0EE0D08D() + x, region.getRegionY_0EE0D08D() + y, width, height);
        }

        public Texture getTexture_D75719FD()
        {
            return _texture;
        }

        public virtual void setTexture_1B161583(Texture texture)
        {
            _texture = texture;
        }

        public float getU_FFE0B8F0()
        {
            return _u;
        }

        public void setU_97413DCA(float u)
        {
            _u = u;
            _regionWidth = (int) System.Math.Round(System.Math.Abs(_u2 - _u) * _texture.getWidth_0EE0D08D());
        }

        public float getV_FFE0B8F0()
        {
            return _v;
        }

        public void setV_97413DCA(float v)
        {
            _v = v;
            _regionHeight = (int) System.Math.Round(System.Math.Abs(_v2 - _v) * _texture.getHeight_0EE0D08D());
        }

        public float getU2_FFE0B8F0()
        {
            return _u2;
        }

        public void setU2_97413DCA(float u2)
        {
            _u2 = u2;
            _regionWidth = (int) System.Math.Round(System.Math.Abs(_u2 - _u) * _texture.getWidth_0EE0D08D());
        }

        public float getV2_FFE0B8F0()
        {
            return _v2;
        }

        public void setV2_97413DCA(float v2)
        {
            _v2 = v2;
            _regionHeight = (int) System.Math.Round(System.Math.Abs(_v2 - _v) * _texture.getHeight_0EE0D08D());
        }

        public int getRegionX_0EE0D08D()
        {
            return (int) System.Math.Round(System.Math.Min(_u, _u2) * _texture.getWidth_0EE0D08D());
        }

        public void setRegionX_3518BA33(int x)
        {
            setU_97413DCA(x / (float) _texture.getWidth_0EE0D08D());
        }

        public int getRegionY_0EE0D08D()
        {
            var result = (int) System.Math.Round(System.Math.Min(_v, _v2) * _texture.getHeight_0EE0D08D());
            return result;
        }

        public void setRegionY_3518BA33(int y)
        {
            setV_97413DCA(y / (float) _texture.getHeight_0EE0D08D());
        }

        public int getRegionWidth_0EE0D08D()
        {
            return _regionWidth;
        }

        public void setRegionWidth_3518BA33(int width)
        {
            if (isFlipX_FBE0B2A4()) {
                setU_97413DCA(_u2 + width / (float)_texture.getWidth_0EE0D08D());
            } else {
                setU2_97413DCA(_u + width / (float)_texture.getWidth_0EE0D08D());
            }
        }

        public int getRegionHeight_0EE0D08D()
        {
            return _regionHeight;
        }

        public void setRegionHeight_3518BA33(int height)
        {
            if (isFlipY_FBE0B2A4()) {
                setV_97413DCA(_v2 + height / (float)_texture.getHeight_0EE0D08D());			
            } else {
                setV2_97413DCA(_v + height / (float)_texture.getHeight_0EE0D08D());
            }
        }

        public void flip_62FCD310(bool x, bool y)
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

        public void setFlip_62FCD310(bool x, bool y)
        {
            setFlipX_AA5A2C66(x);
            setFlipY_AA5A2C66(y);
        }

        public bool isFlipX_FBE0B2A4()
        {
            return _u > _u2;
        }

        public void setFlipX_AA5A2C66(bool b)
        {
            if (isFlipX_FBE0B2A4() != b)
                flip_62FCD310(true, false);
        }

        public bool isFlipY_FBE0B2A4()
        {
            return _v > _v2;
        }

        public void setFlipY_AA5A2C66(bool b)
        {
            if (isFlipY_FBE0B2A4() != b)
                flip_62FCD310(false, true);
        }

        public void scroll_0948E7C0(float xAmount, float yAmount)
        {
            if (xAmount != 0) {
                var width = (_u2 - _u) * _texture.getWidth_0EE0D08D();
                _u = (_u + xAmount) % 1;
                _u2 = _u + width / _texture.getWidth_0EE0D08D();
            }
            if (yAmount != 0) {
                var height = (_v2 - _v) * _texture.getHeight_0EE0D08D();
                _v = (_v + yAmount) % 1;
                _v2 = _v + height / _texture.getHeight_0EE0D08D();
            }
        }

        public Pixmap toPixmap_3CDB13DF()
        {
            var pixmap = new MonoGamePixmap(_regionWidth, _regionHeight);

            var texture2d = ((MonoGameTexture) _texture).texture2D;
            var rawTextureRegionData = new Color[_regionWidth * _regionHeight];

            Rectangle boundingRect = new Rectangle(getRegionX_0EE0D08D(), getRegionY_0EE0D08D(), _regionWidth, _regionHeight);
            
            bool flipX = isFlipX_FBE0B2A4();
            bool flipY = isFlipY_FBE0B2A4();
            
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
                    pixmap.drawPixel(actualX, actualY, rawTextureRegionData[x + y * _regionWidth]);
                }
            }
            
            return pixmap;
        }

        public TextureRegion[][] split_B458A465(int tileWidth, int tileHeight)
        {
            var x = getRegionX_0EE0D08D();
            var y = getRegionY_0EE0D08D();
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