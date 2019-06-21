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
        private bool _rotated;
        
        public MonoGameTextureAtlasRegion(Texture texture, int x, int y, int width, int height, bool rotated) : base(texture, x, y, width, height)
        {
            _rotated = _rotated;
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
    }
}