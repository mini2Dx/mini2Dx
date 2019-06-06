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
using org.mini2Dx.core.graphics;
using Color = Microsoft.Xna.Framework.Color;

namespace monogame.Graphics
{
    class MonoGameTexture : org.mini2Dx.core.graphics.Texture
    {
        public readonly Texture2D texture2D;

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
            var rawTextureData = new Color[getWidth() * getHeight()];
            texture2D.GetData(rawTextureData);
            
            for (var textureX = 0; textureX < getWidth(); textureX++)
            {
                for (var textureY = 0; textureY < getHeight(); textureY++)
                {
                    pixmap.drawPixel(x + textureX, y + textureY, new MonoGameColor(rawTextureData[textureX + textureY * getWidth()]));
                }
            }
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
    }
}
