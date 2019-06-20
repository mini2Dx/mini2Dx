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

using org.mini2Dx.core.graphics;

namespace monogame.Graphics
{
    public class MonoGameTilingDrawable : org.mini2Dx.core.graphics.TilingDrawable
    {
        private readonly TextureRegion _textureRegion;

        public MonoGameTilingDrawable(TextureRegion region)
        {
            _textureRegion = region;
        }

        public MonoGameTilingDrawable(Texture texture) : this(new MonoGameTextureRegion(texture)){}

        public void draw(org.mini2Dx.core.Graphics g, float x, float y, float width, float height)
        {
            var xCount = (int)(width / _textureRegion.getRegionWidth());
            var yCount = (int) (height / _textureRegion.getRegionHeight());
            var xRemainder = width - xCount * _textureRegion.getRegionWidth();
            var yRemainder = height - yCount * _textureRegion.getRegionHeight();

            TextureRegion xRemainderRegion = null;
            TextureRegion yRemainderRegion = null;
            TextureRegion xyRemainderRegion = null;
            
            if (xRemainder != 0)
            {
                xRemainderRegion = new MonoGameTextureRegion(_textureRegion, (int) xRemainder, _textureRegion.getRegionHeight());
            }

            if (yRemainder != 0)
            {
                yRemainderRegion = new MonoGameTextureRegion(_textureRegion, _textureRegion.getRegionWidth(), (int)yRemainder);
            }

            if (xRemainder != 0 && yRemainder != 0)
            {
                xyRemainderRegion = new MonoGameTextureRegion(_textureRegion, (int) xRemainder, (int) yRemainder);
            }
            
            for (int i = 0; i < xCount; i++)
            {
                for (int j = 0; j < yCount; j++)
                {
                    g.drawTextureRegion(_textureRegion, x + _textureRegion.getRegionWidth() * i, y + j * _textureRegion.getRegionHeight());
                }
            }

            if (xRemainderRegion != null)
            {
                for (int i = 0; i < yCount; i++)
                {
                    g.drawTextureRegion(xRemainderRegion, x + xCount * _textureRegion.getRegionWidth(), y + i * _textureRegion.getRegionHeight());
                }
            }
            
            if (yRemainderRegion != null)
            {
                for (int i = 0; i < xCount; i++)
                {
                    g.drawTextureRegion(yRemainderRegion, x + i * _textureRegion.getRegionWidth(), y + yCount * _textureRegion.getRegionHeight());
                }
            }

            if (xyRemainderRegion != null)
            {
                g.drawTextureRegion(xyRemainderRegion, x + xCount * _textureRegion.getRegionWidth(), y + yCount * _textureRegion.getRegionHeight());
            }
        }
    }
}