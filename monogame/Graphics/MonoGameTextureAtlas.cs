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
using System.Collections.Generic;
using monogame.Files;
using org.mini2Dx.core;
using org.mini2Dx.core.files;
using org.mini2Dx.core.graphics;
using Array = org.mini2Dx.gdx.utils.Array;

namespace monogame.Graphics
{
    public class MonoGameTextureAtlas : org.mini2Dx.core.graphics.TextureAtlas
    {
        Array _atlasImages = new Array(); 
        
        public MonoGameTextureAtlas(FileHandle atlasFile, FileHandle imagesDir, bool flip)
        {
            var lines = ((MonoGameFileHandle) atlasFile).readAllLines();
            var i = 1;
            MonoGameTexture packTexture;
            while (i < lines.Length)
            {
                packTexture = (MonoGameTexture) Mdx.graphics.newTexture(imagesDir.child(lines[i]));
                i += 5; //skip size, format, filter and repeat;
                while (i < lines.Length && lines[i] != string.Empty)
                {
                    int index, x, y, width, height, originalWidth, originalHeight, offsetX, offsetY;
                    string name = lines[i];
                    bool rotate = bool.Parse(lines[i + 1].Split(':')[1]);

                    readTuple(lines[i + 2], out x, out y);
                    readTuple(lines[i + 3], out width, out height);
                    readTuple(lines[i + 4], out originalWidth, out originalHeight);
                    readTuple(lines[i + 5], out offsetX, out offsetY);
                    index = int.Parse(lines[i + 6].Split(':')[1]);

                    var image = new MonoGameTextureAtlasRegion(packTexture, name, index, x, y, width, height, rotate);
                    image.originalWidth = originalWidth;
                    image.originalHeight = originalHeight;
                    image.offsetX = offsetX;
                    image.offsetY = offsetY;

                    _atlasImages.add(image);
                    
                    i += 7;
                }

                i++;
            }
        }

        private void readTuple(string s, out int int1, out int int2)
        {
            var splitString = s.Split(':')[1].Split(',');
            int1 = int.Parse(splitString[0]);
            int2 = int.Parse(splitString[1]);
        }
        
        public void dispose()
        {
            _atlasImages.clear();
        }

        public Array getRegions()
        {
            return _atlasImages;
        }

        public TextureAtlasRegion findRegion(string str)
        {
            for(int i = 0; i < _atlasImages.size; i++)
            {
                TextureAtlasRegion textureAtlasRegion = _atlasImages.get(i) as TextureAtlasRegion;
                if (textureAtlasRegion.getName().Equals(str))
                {
                    return textureAtlasRegion;
                }
            }
            return null;
        }

        public TextureAtlasRegion findRegion(string str, int index)
        {
            for (int i = 0; i < _atlasImages.size; i++)
            {
                TextureAtlasRegion textureAtlasRegion = _atlasImages.get(i) as TextureAtlasRegion;
                if (!textureAtlasRegion.getName().Equals(str))
                {
                    continue;
                }
                if (textureAtlasRegion.getIndex() != index)
                {
                    continue;
                }
                return textureAtlasRegion;
            }
            return null;
        }

        public Array findRegions(string str)
        {
            Array result = new Array();
            for (int i = 0; i < _atlasImages.size; i++)
            {
                TextureAtlasRegion textureAtlasRegion = _atlasImages.get(i) as TextureAtlasRegion;
                if (textureAtlasRegion.getName().Equals(str))
                {
                    result.add(textureAtlasRegion);
                }
            }
            return result;
        }
    }
}