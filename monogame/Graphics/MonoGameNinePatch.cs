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

using System.Diagnostics.CodeAnalysis;
using org.mini2Dx.core.graphics;
using Texture = org.mini2Dx.core.graphics.Texture;

namespace monogame.Graphics
{
    [SuppressMessage("ReSharper", "CompareOfFloatsByEqualityOperator")]
    public class MonoGameNinePatch : org.mini2Dx.core.graphics.NinePatch
    {
        private readonly TextureRegion _textureRegion;
        private Color _setColor = new MonoGameColor(Microsoft.Xna.Framework.Color.White);
        private float _leftWidth, _rightWidth, _topHeight, _bottomHeight, _middleWidth, _middleHeight;
        private float _padLeft = -1, _padRight = -1, _padTop = -1, _padBottom = -1;
        private MonoGameTextureRegion _ninePatchRegion;

        public MonoGameNinePatch(TextureRegion texture, int left, int right, int top, int bottom)
        {
            _textureRegion = texture;
            _topHeight = top;
            _leftWidth = left;
            _middleWidth = _textureRegion.getRegionWidth() - right;
            _rightWidth = right;
            _middleHeight = _textureRegion.getRegionHeight() - top - bottom;
            _bottomHeight = bottom;
        }

        private static void draw(org.mini2Dx.core.Graphics g, TextureRegion ninePatchRegion, float dstX, float dstY, float dstWidth, float dstHeight)
        {
            g.drawTextureRegion(ninePatchRegion, dstX, dstY, dstWidth, dstHeight);
        }
        
        public void render(org.mini2Dx.core.Graphics g, float x, float y, float width, float height)
        {
            
            var newTint = _setColor.copy().multiply(g.getTint());
            var oldTint = g.getTint();
            g.setTint(newTint);
            
            _ninePatchRegion = new MonoGameTextureRegion(_textureRegion);
            
            _ninePatchRegion.setRegionX(0);
            _ninePatchRegion.setRegionY(0);
            _ninePatchRegion.setRegionWidth((int) _leftWidth);
            _ninePatchRegion.setRegionHeight((int) _topHeight);
            draw(g, _ninePatchRegion, x, y, _leftWidth, _topHeight);
            _ninePatchRegion.setRegionX((int) _leftWidth);
            _ninePatchRegion.setRegionWidth((int) (_textureRegion.getRegionWidth() - _leftWidth - _rightWidth));
            draw(g, _ninePatchRegion, x + _leftWidth, y, width - _leftWidth - _rightWidth, _topHeight);
            _ninePatchRegion.setRegionX((int) (_textureRegion.getRegionWidth() - _rightWidth));
            _ninePatchRegion.setRegionWidth((int) _rightWidth);
            draw(g, _ninePatchRegion, x + width - _rightWidth, y, _rightWidth, _topHeight);
            _ninePatchRegion.setRegionY((int) _topHeight);
            _ninePatchRegion.setRegionHeight((int) (_textureRegion.getRegionHeight() - _topHeight - _bottomHeight));
            draw(g, _ninePatchRegion, x + width - _rightWidth, y + _topHeight, _rightWidth, height - _topHeight - _bottomHeight);
            _ninePatchRegion.setRegionX((int) _leftWidth);
            _ninePatchRegion.setRegionWidth((int) (_textureRegion.getRegionWidth() - _leftWidth - _rightWidth));
            draw(g, _ninePatchRegion, x + _leftWidth, y + _topHeight, width - _leftWidth - _rightWidth, height - _topHeight - _bottomHeight);
            _ninePatchRegion.setRegionX(0);
            _ninePatchRegion.setRegionWidth((int) _leftWidth);
            draw(g, _ninePatchRegion, x, y + _topHeight, _leftWidth, height - _topHeight - _bottomHeight);
            _ninePatchRegion.setRegionY((int) (_textureRegion.getRegionHeight() - _bottomHeight));
            _ninePatchRegion.setRegionHeight((int) _bottomHeight);
            draw(g, _ninePatchRegion, x, y + height - _bottomHeight, _leftWidth, _bottomHeight);
            _ninePatchRegion.setRegionX((int) _leftWidth);
            _ninePatchRegion.setRegionWidth((int) (_textureRegion.getRegionWidth() - _leftWidth - _rightWidth));
            draw(g, _ninePatchRegion, x + _leftWidth, y + height - _bottomHeight, width - _leftWidth - _rightWidth, _bottomHeight);
            _ninePatchRegion.setRegionX((int) (_textureRegion.getRegionWidth() - _rightWidth));
            _ninePatchRegion.setRegionWidth((int) _rightWidth);
            draw(g, _ninePatchRegion, x + width - _rightWidth, y + height - _bottomHeight, _rightWidth, _bottomHeight);
            g.setTint(oldTint);
        }

        public Color getColor()
        {
            return _setColor;
        }

        public void setColor(Color c)
        {
            _setColor = c;
        }

        public float getLeftWidth()
        {
            return _leftWidth;
        }

        public void setLeftWidth(float leftWidth)
        {
            _leftWidth = leftWidth;
        }

        public float getRightWidth()
        {
            return _rightWidth;
        }

        public void setRightWidth(float rightWidth)
        {
            _rightWidth = rightWidth;
        }

        public float getTopHeight()
        {
            return _topHeight;
        }

        public void setTopHeight(float topHeight)
        {
            _topHeight = topHeight;
        }

        public float getBottomHeight()
        {
            return _bottomHeight;
        }

        public void setBottomHeight(float bottomHeight)
        {
            _bottomHeight = bottomHeight;
        }

        public float getMiddleWidth()
        {
            return _middleWidth;
        }

        public void setMiddleWidth(float middleWidth)
        {
            _middleWidth = middleWidth;
        }

        public float getMiddleHeight()
        {
            return _middleHeight;
        }

        public void setMiddleHeight(float middleHeight)
        {
            _middleHeight = middleHeight;
        }

        public float getTotalWidth()
        {
            return getLeftWidth() + getMiddleWidth() + getRightWidth();
        }

        public float getTotalHeight()
        {
            return getTopHeight() + getMiddleHeight() + getBottomHeight();
        }

        public void setPadding(float left, float right, float top, float bottom)
        {
            setPaddingLeft(left);
            setPaddingRight(right);
            setPaddingTop(top);
            setPaddingBottom(bottom);
        }

        public float getPaddingLeft()
        {
            return _padLeft == -1 ? getLeftWidth() : _padLeft;
        }

        public void setPaddingLeft(float left)
        {
            _padLeft = left;
        }

        public float getPaddingRight()
        {
            return _padRight == -1 ? getRightWidth() : _padRight;
        }

        public void setPaddingRight(float right)
        {
            _padRight = right;
        }

        public float getPaddingTop()
        {
            return _padTop == -1 ? getTopHeight() : _padTop;
        }

        public void setPaddingTop(float top)
        {
            _padTop = top;
        }

        public float getPaddingBottom()
        {
            return _padBottom == -1 ? getBottomHeight() : _padBottom;
        }

        public void setPaddingBottom(float bottom)
        {
            _padBottom = bottom;
        }

        public void scale(float scaleX, float scaleY)
        {
            _leftWidth *= scaleX;
            _rightWidth *= scaleX;
            _topHeight *= scaleY;
            _bottomHeight *= scaleY;
            _middleWidth *= scaleX;
            _middleHeight *= scaleY;
            if (_padLeft != -1)
            {
                _padLeft *= scaleX;
            }

            if (_padRight != -1)
            {
                _padRight *= scaleX;
            }

            if (_padTop != -1)
            {
                _padTop *= scaleY;
            }

            if (_padBottom != -1)
            {
                _padBottom *= scaleY;
            }
        }

        public Texture getTexture()
        {
            return _textureRegion.getTexture();
        }
    }
}