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
    public class MonoGameTextureAtlasRegion : MonoGameTextureRegion, org.mini2Dx.core.graphics.TextureAtlasRegion
    {
        public readonly string name;
        public readonly string texturePath = "";
        public readonly bool rotate;
        public readonly int x, y;
        public readonly int width, height;
        public readonly int originalWidth, originalHeight;
        public readonly int offsetX, offsetY;
        public readonly int index;
        
        public MonoGameTextureAtlasRegion(Texture texture, string name, int index, int x, int y, int width, int height, bool rotate, int originalWidth, int originalHeight, int offsetX, int offsetY) : base(texture, x, y, width, height)
        {
            this.name = name;
            this.index = index;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.rotate = rotate;
            this.originalWidth = originalWidth;
            this.originalHeight = originalHeight;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }

        public MonoGameTextureAtlasRegion(string texturePath, string name, int index, int x, int y, int width, int height, bool rotate, int originalWidth, int originalHeight, int offsetX, int offsetY)
        {
            this.texturePath = texturePath;
            this.name = name;
            this.index = index;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.rotate = rotate;
            this.originalWidth = originalWidth;
            this.originalHeight = originalHeight;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }

        public override void setTexture(Texture t)
        {
            base.setTexture(t);
            setRegion(x, y, width, height);
        }

        public float getRotatedPackedWidth()
        {
            if (rotate)
            {
                return getRegionHeight();
            }
            else
            {
                return getRegionWidth();
            }
        }

        public float getRotatedPackedHeight()
        {
            if (rotate)
            {
                return getRegionWidth();
            }
            else
            {
                return getRegionHeight();
            }
        }

        public string getTexturePath()
        {
            return texturePath;
        }

        public string getName()
        {
            return name;
        }

        public int getIndex()
        {
            return index;
        }

        public float getPackedWidth()
        {
            return width;
        }

        public float getPackedHeight()
        {
            return height;
        }

        public float getOriginalWidth()
        {
            return originalWidth;
        }

        public float getOriginalHeight()
        {
            return originalHeight;
        }

        public float getOffsetX()
        {
            return offsetX;
        }

        public float getOffsetY()
        {
            return offsetY;
        }
    }
}