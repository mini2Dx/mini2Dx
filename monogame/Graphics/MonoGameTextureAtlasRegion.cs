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

using Java.Lang;
using Org.Mini2Dx.Core;
using Org.Mini2Dx.Core.Assets;
using Org.Mini2Dx.Core.Font;
using Org.Mini2Dx.Core.Util;
using Org.Mini2Dx.Core.Graphics;
using Color = Org.Mini2Dx.Core.Graphics.Color;

namespace monogame.Graphics
{
    public class MonoGameTextureAtlasRegion : MonoGameTextureRegion, TextureAtlasRegion
    {
        public readonly Java.Lang.String name;
        public readonly Java.Lang.String texturePath = "";
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

        public override void setTexture_1B161583(Texture t)
        {
            base.setTexture_1B161583(t);
            setRegion_9C90BED0(x, y, width, height);
        }

        public float getRotatedPackedWidth_FFE0B8F0()
        {
            if (rotate)
            {
                return getRegionHeight_0EE0D08D();
            }
            else
            {
                return getRegionWidth_0EE0D08D();
            }
        }

        public float getRotatedPackedHeight_FFE0B8F0()
        {
            if (rotate)
            {
                return getRegionWidth_0EE0D08D();
            }
            else
            {
                return getRegionHeight_0EE0D08D();
            }
        }

        public Java.Lang.String getTexturePath_E605312C()
        {
            return texturePath;
        }

        public Java.Lang.String getName_E605312C()
        {
            return name;
        }

        public int getIndex_0EE0D08D()
        {
            return index;
        }

        public float getPackedWidth_FFE0B8F0()
        {
            return width;
        }

        public float getPackedHeight_FFE0B8F0()
        {
            return height;
        }

        public float getOriginalWidth_FFE0B8F0()
        {
            return originalWidth;
        }

        public float getOriginalHeight_FFE0B8F0()
        {
            return originalHeight;
        }

        public float getOffsetX_FFE0B8F0()
        {
            return offsetX;
        }

        public float getOffsetY_FFE0B8F0()
        {
            return offsetY;
        }
    }
}