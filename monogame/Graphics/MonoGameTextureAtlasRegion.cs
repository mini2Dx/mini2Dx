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
    public class MonoGameTextureAtlasRegion : MonoGameTextureRegion, org.mini2Dx.core.graphics.TextureAtlasRegion, System.IComparable<MonoGameTextureAtlasRegion>
    {
        public string name;
        public bool rotate;
        public int x, y;
        public int width, height;
        public int originalWidth, originalHeight;
        public int offsetX, offsetY;
        public int index;
        private bool _rotated;
        
        public MonoGameTextureAtlasRegion(Texture texture, int x, int y, int width, int height, bool rotated) : base(texture, x, y, width, height)
        {
            _rotated = rotated;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        public float getRotatedPackedWidth()
        {
            if (_rotated)
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
            if (_rotated)
            {
                return getRegionWidth();
            }
            else
            {
                return getRegionHeight();
            }
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

        public int CompareTo(MonoGameTextureAtlasRegion other)
        {
            int nameCompare = name.CompareTo(other.name);
            if(nameCompare == 0)
            {
                return index.CompareTo(other.index);
            }
            return nameCompare;
        }
    }
}