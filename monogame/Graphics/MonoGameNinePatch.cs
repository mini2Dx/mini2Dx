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
    public class MonoGameNinePatch : global::Java.Lang.Object, Org.Mini2Dx.Core.Graphics.NinePatch
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
            _middleWidth = _textureRegion.getRegionWidth_0EE0D08D() - right - left;
            _rightWidth = right;
            _middleHeight = _textureRegion.getRegionHeight_0EE0D08D() - top - bottom;
            _bottomHeight = bottom;
            _ninePatchRegion = new MonoGameTextureRegion(_textureRegion);
            _textureRegionX = _textureRegion.getRegionX_0EE0D08D();
            _textureRegionY = _textureRegion.getRegionY_0EE0D08D();
            _rightX = _textureRegion.getRegionWidth_0EE0D08D() - right;
            _bottomY = _textureRegion.getRegionHeight_0EE0D08D() - bottom;
        }

        private static void draw(Org.Mini2Dx.Core._Graphics g, TextureRegion ninePatchRegion, float dstX, float dstY, float dstWidth, float dstHeight)
        {
            g.drawTextureRegion_1F25C0DB(ninePatchRegion, dstX, dstY, dstWidth, dstHeight);
        }
        
        public void render_13ECCFE3(Org.Mini2Dx.Core._Graphics g, float x, float y, float width, float height)
        {
            
            var newTint = _setColor.copy_F0D7D9CF().multiply_F18CABCA(g.getTint_F0D7D9CF());
            var oldTint = g.getTint_F0D7D9CF();
            var middleWidth = (int) (width - _leftWidth - _rightWidth);
            var middleHeight = (int) (height - _topHeight - _bottomHeight);
            g.setTint_24D51C91(newTint);
            
            _ninePatchRegion.setRegionX_3518BA33(_textureRegionX);
            _ninePatchRegion.setRegionY_3518BA33(_textureRegionY);
            _ninePatchRegion.setRegionWidth_3518BA33((int) _leftWidth);
            _ninePatchRegion.setRegionHeight_3518BA33((int) _topHeight);
            draw(g, _ninePatchRegion, x, y, _leftWidth, _topHeight);
            _ninePatchRegion.setRegionX_3518BA33(_textureRegionX + (int) _leftWidth);
            _ninePatchRegion.setRegionWidth_3518BA33((int) _middleWidth);
            draw(g, _ninePatchRegion, x + _leftWidth, y, middleWidth, _topHeight);
            _ninePatchRegion.setRegionX_3518BA33(_textureRegionX + (int) _rightX);
            _ninePatchRegion.setRegionWidth_3518BA33((int) _rightWidth);
            draw(g, _ninePatchRegion, x + width - _rightWidth, y, _rightWidth, _topHeight);
            _ninePatchRegion.setRegionY_3518BA33(_textureRegionY + (int) _topHeight);
            _ninePatchRegion.setRegionHeight_3518BA33((int) _middleHeight);
            draw(g, _ninePatchRegion, x + width - _rightWidth, y + _topHeight, _rightWidth, middleHeight);
            _ninePatchRegion.setRegionX_3518BA33(_textureRegionX + (int) _leftWidth);
            _ninePatchRegion.setRegionWidth_3518BA33((int) _middleWidth);
            draw(g, _ninePatchRegion, x + _leftWidth, y + _topHeight, middleWidth, middleHeight);
            _ninePatchRegion.setRegionX_3518BA33(_textureRegionX);
            _ninePatchRegion.setRegionWidth_3518BA33((int) _leftWidth);
            draw(g, _ninePatchRegion, x, y + _topHeight, _leftWidth, middleHeight);
            _ninePatchRegion.setRegionY_3518BA33(_textureRegionY + (int) _bottomY);
            _ninePatchRegion.setRegionHeight_3518BA33((int) _bottomHeight);
            draw(g, _ninePatchRegion, x, y + height - _bottomHeight, _leftWidth, _bottomHeight);
            _ninePatchRegion.setRegionX_3518BA33(_textureRegionX + (int) _leftWidth);
            _ninePatchRegion.setRegionWidth_3518BA33((int) _middleWidth);
            draw(g, _ninePatchRegion, x + _leftWidth, y + height - _bottomHeight, middleWidth, _bottomHeight);
            _ninePatchRegion.setRegionX_3518BA33(_textureRegionX + (int) _rightX);
            _ninePatchRegion.setRegionWidth_3518BA33((int) _rightWidth);
            draw(g, _ninePatchRegion, x + width - _rightWidth, y + height - _bottomHeight, _rightWidth, _bottomHeight);
            g.setTint_24D51C91(oldTint);
        }

        public Color getColor_F0D7D9CF()
        {
            return _setColor;
        }

        public void setColor_24D51C91(Color c)
        {
            _setColor = c;
        }

        public float getLeftWidth_FFE0B8F0()
        {
            return _leftWidth;
        }

        public void setLeftWidth_97413DCA(float leftWidth)
        {
            _leftWidth = leftWidth;
        }

        public float getRightWidth_FFE0B8F0()
        {
            return _rightWidth;
        }

        public void setRightWidth_97413DCA(float rightWidth)
        {
            _rightWidth = rightWidth;
        }

        public float getTopHeight_FFE0B8F0()
        {
            return _topHeight;
        }

        public void setTopHeight_97413DCA(float topHeight)
        {
            _topHeight = topHeight;
        }

        public float getBottomHeight_FFE0B8F0()
        {
            return _bottomHeight;
        }

        public void setBottomHeight_97413DCA(float bottomHeight)
        {
            _bottomHeight = bottomHeight;
        }

        public float getMiddleWidth_FFE0B8F0()
        {
            return _middleWidth;
        }

        public void setMiddleWidth_97413DCA(float middleWidth)
        {
            _middleWidth = middleWidth;
        }

        public float getMiddleHeight_FFE0B8F0()
        {
            return _middleHeight;
        }

        public void setMiddleHeight_97413DCA(float middleHeight)
        {
            _middleHeight = middleHeight;
        }

        public float getTotalWidth_FFE0B8F0()
        {
            return getLeftWidth_FFE0B8F0() + getMiddleWidth_FFE0B8F0() + getRightWidth_FFE0B8F0();
        }

        public float getTotalHeight_FFE0B8F0()
        {
            return getTopHeight_FFE0B8F0() + getMiddleHeight_FFE0B8F0() + getBottomHeight_FFE0B8F0();
        }

        public void setPadding_C2EDAFC0(float left, float right, float top, float bottom)
        {
            setPaddingLeft_97413DCA(left);
            setPaddingRight_97413DCA(right);
            setPaddingTop_97413DCA(top);
            setPaddingBottom_97413DCA(bottom);
        }

        public float getPaddingLeft_FFE0B8F0()
        {
            return _padLeft == -1 ? getLeftWidth_FFE0B8F0() : _padLeft;
        }

        public void setPaddingLeft_97413DCA(float left)
        {
            _padLeft = left;
        }

        public float getPaddingRight_FFE0B8F0()
        {
            return _padRight == -1 ? getRightWidth_FFE0B8F0() : _padRight;
        }

        public void setPaddingRight_97413DCA(float right)
        {
            _padRight = right;
        }

        public float getPaddingTop_FFE0B8F0()
        {
            return _padTop == -1 ? getTopHeight_FFE0B8F0() : _padTop;
        }

        public void setPaddingTop_97413DCA(float top)
        {
            _padTop = top;
        }

        public float getPaddingBottom_FFE0B8F0()
        {
            return _padBottom == -1 ? getBottomHeight_FFE0B8F0() : _padBottom;
        }

        public void setPaddingBottom_97413DCA(float bottom)
        {
            _padBottom = bottom;
        }

        public void scale_0948E7C0(float scaleX, float scaleY)
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

        public Texture getTexture_D75719FD()
        {
            return _textureRegion.getTexture_D75719FD();
        }
    }
}