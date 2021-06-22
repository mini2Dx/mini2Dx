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
using Org.Mini2Dx.Core;
using Org.Mini2Dx.Core.Assets;
using Org.Mini2Dx.Core.Font;
using Org.Mini2Dx.Core.Geom;
using Org.Mini2Dx.Core.Graphics;
using Org.Mini2Dx.Core.Util;
using Color = Org.Mini2Dx.Core.Graphics.Color;
using Texture = Org.Mini2Dx.Core.Graphics.Texture;

namespace monogame.Graphics
{
    public class MonoGameSprite : MonoGameTextureRegion, Sprite
    {

        private float _x, _y, _width, _height, _originX, _originY, _rotation, _scaleX = 1, _scaleY = 1, _90degRotation;
        private Color _tint = new MonoGameColor(Microsoft.Xna.Framework.Color.White);

        public MonoGameSprite()
        {
            setOriginCenter_EFE09FC0();
        }

        public MonoGameSprite(Texture texture) : this(texture, texture.getWidth_0EE0D08D(), texture.getHeight_0EE0D08D()) { }

        public MonoGameSprite(Texture texture, int srcWidth, int srcHeight) : this(texture, 0, 0, srcWidth, srcHeight) { }

        public MonoGameSprite(Texture texture, int srcX, int srcY, int srcWidth, int srcHeight) : base(texture, srcX,
            srcY, srcWidth, srcHeight)
        {
            setBounds_C2EDAFC0(srcX, srcY, srcWidth, srcHeight);
            setOriginCenter_EFE09FC0();
        }

        public MonoGameSprite(TextureRegion region) : this(region, region.getRegionWidth_0EE0D08D(), region.getRegionHeight_0EE0D08D()) { }

        public MonoGameSprite(TextureRegion region, int width, int height) : this(region, 0, 0, width, height) { }

        public MonoGameSprite(TextureRegion region, int x, int y, int width, int height) : base(region, x, y, width, height)
        {
            setFlip_62FCD310(region.isFlipX_FBE0B2A4(), region.isFlipY_FBE0B2A4());
            setBounds_C2EDAFC0(x, y, width, height);
            setOriginCenter_EFE09FC0();
        }

        public MonoGameSprite(TextureAtlasRegion region) : this(region, region.getRegionWidth_0EE0D08D(), region.getRegionHeight_0EE0D08D()) { }

        public MonoGameSprite(TextureAtlasRegion region, int width, int height) : this(region, 0, 0, width, height) { }

        public MonoGameSprite(TextureAtlasRegion region, int x, int y, int width, int height) : base(region, x, y,
            width, height)
        {
            if (region.getRotatedPackedHeight_FFE0B8F0() != region.getRegionHeight_0EE0D08D())
            {
                rotate90_AA5A2C66(false);
            }
            setOriginCenter_EFE09FC0();
            setBounds_C2EDAFC0(x, y, width, height);
        }

        public void set_615359F5(Sprite s)
        {
            setRegion_EBB2FEFB(s);
            setBounds_C2EDAFC0(s.getX_FFE0B8F0(), s.getY_FFE0B8F0(), s.getWidth_FFE0B8F0(), s.getHeight_FFE0B8F0());
            setOrigin_0948E7C0(s.getOriginX_FFE0B8F0(), s.getOriginY_FFE0B8F0());
            _90degRotation = ((MonoGameSprite)s)._90degRotation;
            setRotation_97413DCA(s.getRotation_FFE0B8F0());
            setScale_0948E7C0(s.getScaleX_FFE0B8F0(), s.getScaleY_FFE0B8F0());
        }

        public void setBounds_C2EDAFC0(float x, float y, float width, float height)
        {
            setSize_0948E7C0(width, height);
        }

        public void setSize_0948E7C0(float width, float height)
        {
            _width = width;
            _height = height;
        }

        public void setPosition_0948E7C0(float x, float y)
        {
            _x = x;
            _y = y;
        }

        public void setOriginBasedPosition_0948E7C0(float x, float y)
        {
            setPosition_0948E7C0(x - _originX, y - _originY);
        }

        public void setCenterX_97413DCA(float x)
        {
            _x = x - _width / 2;
        }

        public void setCenterY_97413DCA(float y)
        {
            _y = y - _height / 2;
        }

        public void setCenter_0948E7C0(float x, float y)
        {
            setCenterX_97413DCA(x);
            setCenterY_97413DCA(y);
        }

        public void translateX_97413DCA(float xAmount)
        {
            _x += xAmount;
        }

        public void translateY_97413DCA(float yAmount)
        {
            _y += yAmount;
        }

        public void translate_0948E7C0(float xAmount, float yAmount)
        {
            translateX_97413DCA(xAmount);
            translateY_97413DCA(yAmount);
        }

        public void setOrigin_0948E7C0(float originX, float originY)
        {
            _originX = originX;
            _originY = originY;
        }

        public void setOriginCenter_EFE09FC0()
        {
            setOrigin_0948E7C0(getRegionWidth_0EE0D08D() / 2f, getRegionHeight_0EE0D08D() / 2f);
        }

        public float getRotation_FFE0B8F0()
        {
            return _rotation;
        }

        public float getTotalRotation()
        {
            return _rotation + _90degRotation;
        }

        public void setRotation_97413DCA(float degrees)
        {
            _rotation = degrees % 360;
        }

        public void rotate_97413DCA(float degrees)
        {
            setRotation_97413DCA(getRotation_FFE0B8F0() + degrees);
        }

        public void rotate90_AA5A2C66(bool clockwise)
        {
            if (clockwise)
            {
                _90degRotation += 90;
            }
            else
            {
                _90degRotation -= 90;
            }
        }

        public void setScale_97413DCA(float scaleXY)
        {
            setScale_0948E7C0(scaleXY, scaleXY);
        }

        public void setScale_0948E7C0(float scaleX, float scaleY)
        {
            _scaleX = scaleX;
            _scaleY = scaleY;
        }

        public void scale_97413DCA(float amount)
        {
            _scaleX += amount;
            _scaleY += amount;
        }

        public float[] getVertices_9E7A8229()
        {
            throw new NotImplementedException(); //should not be needed using MonoGame SpriteBatch
        }

        public Rectangle getBoundingRectangle_A029B76C()
        {
            var rect = new Rectangle();
            rect._init_(_x, _y, _width, _height);
            rect.setRotation_97413DCA(_90degRotation + _rotation);
            return rect;
        }

        public float getX_FFE0B8F0()
        {
            return _x;
        }

        public void setX_97413DCA(float x)
        {
            _x = x;
        }

        public float getY_FFE0B8F0()
        {
            return _y;
        }

        public void setY_97413DCA(float y)
        {
            _y = y;
        }

        public float getWidth_FFE0B8F0()
        {
            return _width;
        }

        public float getHeight_FFE0B8F0()
        {
            return _height;
        }

        public float getOriginX_FFE0B8F0()
        {
            return _originX;
        }

        public float getOriginY_FFE0B8F0()
        {
            return _originY;
        }

        public float getScaleX_FFE0B8F0()
        {
            return _scaleX;
        }

        public float getScaleY_FFE0B8F0()
        {
            return _scaleY;
        }

        public Color getTint_F0D7D9CF()
        {
            return _tint;
        }

        public void setTint_24D51C91(Color c)
        {
            var oldAlpha = getAlpha_FFE0B8F0();
            _tint = c;
            setAlpha_97413DCA(oldAlpha);
        }

        public float getAlpha_FFE0B8F0()
        {
            return _tint.getAAsFloat_FFE0B8F0();
        }

        public void setAlpha_97413DCA(float alpha)
        {
            _tint.multiply_DF74E9CF(1f, 1f, 1f, 0f);
            _tint.add_DF74E9CF(0, 0, 0, alpha);
        }
    }
}