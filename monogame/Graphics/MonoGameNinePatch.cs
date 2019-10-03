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
using Org.Mini2Dx.Core;
using Org.Mini2Dx.Core.Assets;
using Org.Mini2Dx.Core.Font;
using Org.Mini2Dx.Core.Graphics;
using Org.Mini2Dx.Core.Util;
using Color = Org.Mini2Dx.Core.Graphics.Color;
using Texture = Org.Mini2Dx.Core.Graphics.Texture;

namespace monogame.Graphics
{
    [SuppressMessage("ReSharper", "CompareOfFloatsByEqualityOperator")]
    public class MonoGameNinePatch : Org.Mini2Dx.Core.Graphics.NinePatch
    {
        private readonly TextureRegion _textureRegion;
        private readonly MonoGameTextureRegion _ninePatchRegion;
        private readonly int _textureRegionX;
        private readonly int _textureRegionY;
        private Color _setColor = new MonoGameColor(Microsoft.Xna.Framework.Color.White);
        private float _padLeft = -1, _padRight = -1, _padTop = -1, _padBottom = -1;
        private float _leftWidth, _rightWidth, _topHeight, _bottomHeight, _middleWidth, _middleHeight;
        private float _rightX, _bottomY;
 
        public MonoGameNinePatch(TextureRegion texture, int left, int right, int top, int bottom)
        {
            _textureRegion = texture;
            _topHeight = top;
            _leftWidth = left;
            _middleWidth = _textureRegion.getRegionWidth() - right - left;
            _rightWidth = right;
            _middleHeight = _textureRegion.getRegionHeight() - top - bottom;
            _bottomHeight = bottom;
            _ninePatchRegion = new MonoGameTextureRegion(_textureRegion);
            _textureRegionX = _textureRegion.getRegionX();
            _textureRegionY = _textureRegion.getRegionY();
            _rightX = _textureRegion.getRegionWidth() - right;
            _bottomY = _textureRegion.getRegionHeight() - bottom;
        }

        private static void draw(Org.Mini2Dx.Core._Graphics g, TextureRegion ninePatchRegion, float dstX, float dstY, float dstWidth, float dstHeight)
        {
            g.drawTextureRegion(ninePatchRegion, dstX, dstY, dstWidth, dstHeight);
        }
        
        public void render(Org.Mini2Dx.Core._Graphics g, float x, float y, float width, float height)
        {
            
            var newTint = _setColor.copy().multiply(g.getTint());
            var oldTint = g.getTint();
            var middleWidth = (int) (width - _leftWidth - _rightWidth);
            var middleHeight = (int) (height - _topHeight - _bottomHeight);
            g.setTint(newTint);
            
            _ninePatchRegion.setRegionX(_textureRegionX);
            _ninePatchRegion.setRegionY(_textureRegionY);
            _ninePatchRegion.setRegionWidth((int) _leftWidth);
            _ninePatchRegion.setRegionHeight((int) _topHeight);
            draw(g, _ninePatchRegion, x, y, _leftWidth, _topHeight);
            _ninePatchRegion.setRegionX(_textureRegionX + (int) _leftWidth);
            _ninePatchRegion.setRegionWidth((int) _middleWidth);
            draw(g, _ninePatchRegion, x + _leftWidth, y, middleWidth, _topHeight);
            _ninePatchRegion.setRegionX(_textureRegionX + (int) _rightX);
            _ninePatchRegion.setRegionWidth((int) _rightWidth);
            draw(g, _ninePatchRegion, x + width - _rightWidth, y, _rightWidth, _topHeight);
            _ninePatchRegion.setRegionY(_textureRegionY + (int) _topHeight);
            _ninePatchRegion.setRegionHeight((int) _middleHeight);
            draw(g, _ninePatchRegion, x + width - _rightWidth, y + _topHeight, _rightWidth, middleHeight);
            _ninePatchRegion.setRegionX(_textureRegionX + (int) _leftWidth);
            _ninePatchRegion.setRegionWidth((int) _middleWidth);
            draw(g, _ninePatchRegion, x + _leftWidth, y + _topHeight, middleWidth, middleHeight);
            _ninePatchRegion.setRegionX(_textureRegionX);
            _ninePatchRegion.setRegionWidth((int) _leftWidth);
            draw(g, _ninePatchRegion, x, y + _topHeight, _leftWidth, middleHeight);
            _ninePatchRegion.setRegionY(_textureRegionY + (int) _bottomY);
            _ninePatchRegion.setRegionHeight((int) _bottomHeight);
            draw(g, _ninePatchRegion, x, y + height - _bottomHeight, _leftWidth, _bottomHeight);
            _ninePatchRegion.setRegionX(_textureRegionX + (int) _leftWidth);
            _ninePatchRegion.setRegionWidth((int) _middleWidth);
            draw(g, _ninePatchRegion, x + _leftWidth, y + height - _bottomHeight, middleWidth, _bottomHeight);
            _ninePatchRegion.setRegionX(_textureRegionX + (int) _rightX);
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