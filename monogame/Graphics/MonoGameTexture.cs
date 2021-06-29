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

using Microsoft.Xna.Framework.Graphics;
using TextureAddressMode = Org.Mini2Dx.Core.Graphics.TextureAddressMode;
using Org.Mini2Dx.Core.Graphics;

namespace monogame.Graphics
{
    class MonoGameTexture : global::Java.Lang.Object, Org.Mini2Dx.Core.Graphics.Texture
    {
        internal Texture2D texture2D;
        private TextureAddressMode _uMode = TextureAddressMode.CLAMP_, _vMode = TextureAddressMode.CLAMP_;

        private Microsoft.Xna.Framework.Color[] rawTexture;

        public MonoGameTexture(Texture2D texture2D)
        {
            this.texture2D = texture2D;
        }

        public void dispose_EFE09FC0()
        {
            texture2D.Dispose();
            rawTexture = null;
        }

        public void draw_A700ECD1(Pixmap pixmap, int x, int y)
        {
            if(rawTexture == null || rawTexture.Length != (texture2D.Width * texture2D.Height))
            {
                rawTexture = new Microsoft.Xna.Framework.Color[texture2D.Width * texture2D.Height];
                texture2D.GetData(rawTexture);
            }

            bool changed = false;
            for (var pixmapX = 0; pixmapX < pixmap.getWidth_0EE0D08D(); pixmapX++)
            {
                for (var pixmapY = 0; pixmapY < pixmap.getHeight_0EE0D08D(); pixmapY++)
                {
                    Microsoft.Xna.Framework.Color existingValue = rawTexture[x + pixmapX + (y + pixmapY) * texture2D.Width];
                    Microsoft.Xna.Framework.Color newValue = ((MonoGamePixmap) pixmap)._getMonoGamePixel(pixmapX, pixmapY);
                    if (existingValue != newValue)
                    {
                        rawTexture[x + pixmapX + (y + pixmapY) * texture2D.Width] = newValue;
                        changed = true;
                    }
                }
            }
            if (changed)
            {
                texture2D.SetData(rawTexture);
            }
        }

        public int getHeight_0EE0D08D()
        {
            return texture2D.Height;
        }

        public int getWidth_0EE0D08D()
        {
            return texture2D.Width;
        }

        public bool isManaged_FBE0B2A4()
        {
            return true;
        }

        public TextureAddressMode getUAddressMode_F8C501D6()
        {
            return _uMode;
        }

        public void setUAddressMode_B0E621FC(TextureAddressMode mode)
        {
            _uMode = mode;
        }
        
        public TextureAddressMode getVAddressMode_F8C501D6()
        {
            return _vMode;
        }
        
        public void setVAddressMode_B0E621FC(TextureAddressMode mode)
        {
            _vMode = mode;
        }

        public void setAddressMode_59F6F5E0(TextureAddressMode uMode, TextureAddressMode vMode)
        {
            _uMode = uMode;
            _vMode = vMode;
        }
    }
}
