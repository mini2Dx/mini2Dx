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
using java.lang;
using Microsoft.Xna.Framework.Graphics;
using org.mini2Dx.core.graphics;
using TextureAddressMode = org.mini2Dx.core.graphics.TextureAddressMode;

namespace monogame.Graphics
{
    class MonoGameTexture : org.mini2Dx.core.graphics.Texture
    {
        internal Texture2D texture2D;
        private TextureAddressMode _uMode = TextureAddressMode.CLAMP, _vMode = TextureAddressMode.CLAMP;

        public MonoGameTexture(Texture2D texture2D)
        {
            this.texture2D = texture2D;
        }

        public void dispose()
        {
            texture2D.Dispose();
        }

        public void draw(Pixmap pixmap, int x, int y)
        {
            var rawTexture = new UInt32[texture2D.Width * texture2D.Height];
            texture2D.GetData(rawTexture);
            for (var pixmapX = 0; pixmapX < pixmap.getWidth(); pixmapX++)
            {
                for (var pixmapY = 0; pixmapY < pixmap.getHeight(); pixmapY++)
                {
                    rawTexture[x + pixmapX + (y + pixmapY) * texture2D.Width] = (uint) pixmap.getPixel(pixmapX, pixmapY);
                }
            }
            texture2D.SetData(rawTexture);
        }

        public int getHeight()
        {
            return texture2D.Height;
        }

        public int getWidth()
        {
            return texture2D.Width;
        }

        public bool isManaged()
        {
            return true;
        }

        public TextureAddressMode getUAddressMode()
        {
            return _uMode;
        }

        public void setUAddressMode(TextureAddressMode mode)
        {
            _uMode = mode;
        }
        
        public TextureAddressMode getVAddressMode()
        {
            return _vMode;
        }
        
        public void setVAddressMode(TextureAddressMode mode)
        {
            _vMode = mode;
        }
    }
}
