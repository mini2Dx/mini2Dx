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
using org.mini2Dx.core.geom;
using org.mini2Dx.core.graphics;
using Texture = org.mini2Dx.core.graphics.Texture;

namespace monogame.Graphics
{
    public class MonoGameSprite : MonoGameTextureRegion, Sprite
    {

        private float _x, _y, _width, _height, _originX, _originY, _rotation, _scaleX = 1, _scaleY = 1, _90degRotation;
        private Color _tint = new MonoGameColor(Microsoft.Xna.Framework.Color.White);
        
        public MonoGameSprite()
        {
            setOriginCenter();
        }

        public MonoGameSprite(Texture texture) : this(texture, texture.getWidth(), texture.getHeight()){}

        public MonoGameSprite(Texture texture, int srcWidth, int srcHeight): this(texture, 0, 0, srcWidth, srcHeight){}

        public MonoGameSprite(Texture texture, int srcX, int srcY, int srcWidth, int srcHeight) : base(texture, srcX,
            srcY, srcWidth, srcHeight)
        {
            setBounds(srcX, srcY, srcWidth, srcHeight);
            setOriginCenter();
        }

        public MonoGameSprite(TextureRegion region) : this(region, region.getRegionWidth(), region.getRegionHeight()){}
        
        public MonoGameSprite(TextureRegion region, int width, int height) : this(region, 0, 0, width, height){}

        public MonoGameSprite(TextureRegion region, int x, int y, int width, int height) : base(region, x, y, width, height)
        {
            setFlip(region.isFlipX(), region.isFlipY());
            setOriginCenter();
        }
        
        public MonoGameSprite(TextureAtlasRegion region) : this(region, region.getRegionWidth(), region.getRegionHeight()) {}

        public MonoGameSprite(TextureAtlasRegion region, int width, int height) : this(region, 0, 0, width, height) {}
        
        public MonoGameSprite(TextureAtlasRegion region, int x, int y, int width, int height) : base(region, x, y,
            width, height)
        {
            if (region.getRotatedPackedHeight() != region.getRegionHeight())
            {
                rotate90(false);
            }
            setOriginCenter();
        }
        
        public void set(Sprite s)
        {
            setRegion(s);
            setBounds(s.getX(), s.getY(), s.getWidth(), s.getHeight());
            setOrigin(s.getOriginX(), s.getOriginY());
            _90degRotation = ((MonoGameSprite) s)._90degRotation;
            setRotation(s.getRotation());
            setScale(s.getScaleX(), s.getScaleY());
        }

        public void setBounds(float x, float y, float width, float height)
        {
            setSize(width, height);
        }

        public void setSize(float width, float height)
        {
            _width = width;
            _height = height;
        }

        public void setPosition(float x, float y)
        {
            _x = x;
            _y = y;
        }

        public void setOriginBasedPosition(float x, float y)
        {
            setPosition(x - _originX, y - _originY);
        }

        public void setCenterX(float x)
        {
            _x = x - _width / 2;
        }

        public void setCenterY(float y)
        {
            _y = y - _height / 2;
        }

        public void setCenter(float x, float y)
        {
            setCenterX(x);
            setCenterY(y);
        }

        public void translateX(float xAmount)
        {
            _x += xAmount;
        }

        public void translateY(float yAmount)
        {
            _y += yAmount;
        }

        public void translate(float xAmount, float yAmount)
        {
            translateX(xAmount);
            translateY(yAmount);
        }

        public void setOrigin(float originX, float originY)
        {
            _originX = originX;
            _originY = originY;
        }

        public void setOriginCenter()
        {
            setOrigin(getRegionWidth() / 2f, getRegionHeight() / 2f);
        }

        public float getRotation()
        {
            return _rotation;
        }

        public float getTotalRotation()
        {
            return _rotation + _90degRotation;
        }

        public void setRotation(float degrees)
        {
            _rotation = degrees % 360;
        }

        public void rotate(float degrees)
        {
            setRotation(getRotation() + degrees);
        }

        public void rotate90(bool clockwise)
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

        public void setScale(float scaleXY)
        {
            setScale(scaleXY, scaleXY);
        }

        public void setScale(float scaleX, float scaleY)
        {
            _scaleX = scaleX;
            _scaleY = scaleY;
        }

        public void scale(float amount)
        {
            _scaleX += amount;
            _scaleY += amount;
        }

        public float[] getVertices()
        {
            throw new NotImplementedException(); //should not be needed using MonoGame SpriteBatch
        }

        public Rectangle getBoundingRectangle()
        {
            var rect = new Rectangle(_x, _y, _width, _height);
            rect.setRotation(_90degRotation + _rotation);
            return rect;
        }

        public float getX()
        {
            return _x;
        }

        public void setX(float x)
        {
            _x = x;
        }

        public float getY()
        {
            return _y;
        }

        public void setY(float y)
        {
            _y = y;
        }

        public float getWidth()
        {
            return _width;
        }

        public float getHeight()
        {
            return _height;
        }

        public float getOriginX()
        {
            return _originX;
        }

        public float getOriginY()
        {
            return _originY;
        }

        public float getScaleX()
        {
            return _scaleX;
        }

        public float getScaleY()
        {
            return _scaleY;
        }

        public Color getTint()
        {
            return _tint;
        }

        public void setTint(Color c)
        {
            var oldAlpha = getAlpha();
            _tint = c;
            setAlpha(oldAlpha);
        }

        public float getAlpha()
        {
            return _tint.getAAsFloat();
        }

        public void setAlpha(float alpha)
        {
            _tint.multiply(1f, 1f, 1f, 0f);
            _tint.add(0, 0, 0, alpha);
        }
    }
}