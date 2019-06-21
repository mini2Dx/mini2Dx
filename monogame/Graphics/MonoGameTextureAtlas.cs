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
    class MonoGameTextureAtlasImage : IComparable<MonoGameTextureAtlasImage>
    {
        public string name;
        public bool rotate;
        public int x, y;
        public int width, height;
        public int originX, originY;
        public int offsetX, offsetY;
        public int index;
        public MonoGameTextureAtlasRegion textureRegion;
        public int CompareTo(MonoGameTextureAtlasImage other)
        {
            return index.CompareTo(other.index);
        }
    }
    
    public class MonoGameTextureAtlas : org.mini2Dx.core.graphics.TextureAtlas
    {
        Dictionary<string, Dictionary<int, MonoGameTextureAtlasImage>> _atlasImages = new Dictionary<string, Dictionary<int, MonoGameTextureAtlasImage>>(); 
        
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
                    var image = new MonoGameTextureAtlasImage();
                    image.name = lines[i];
                    image.rotate = bool.Parse(lines[i + 1].Split(':')[1]);

                    if (image.rotate)
                    {
                        throw new NotSupportedException("Rotated atlas aren't supported yet!");
                    }
                    
                    readTuple(lines[i + 2], out image.x, out image.y);
                    readTuple(lines[i + 3], out image.width, out image.height);
                    readTuple(lines[i + 4], out image.originX, out image.originY);
                    readTuple(lines[i + 5], out image.offsetX, out image.offsetY);
                    image.index = int.Parse(lines[i + 6].Split(':')[1]);

                    image.textureRegion = new MonoGameTextureAtlasRegion(packTexture, image.x, image.y, image.width, image.height, image.rotate);
                    
                    if (!_atlasImages.ContainsKey(image.name))
                    {
                        _atlasImages[image.name] = new Dictionary<int, MonoGameTextureAtlasImage>();
                    }
                    _atlasImages[image.name][image.index] = image;
                    
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
            
        }

        public Array getRegions()
        {
            var regions = new List<MonoGameTextureAtlasRegion>();
            foreach (var imageName in _atlasImages.Values)
            {
                foreach (var image in imageName.Values)
                {
                    regions.Add(image.textureRegion);
                }
            }
            
            return Array.with(regions.ToArray());
        }

        public TextureAtlasRegion findRegion(string str)
        {
            return findRegion(str, _atlasImages[str].Keys.GetEnumerator().Current);
        }

        public TextureAtlasRegion findRegion(string str, int i)
        {
            return _atlasImages[str][i].textureRegion;
        }

        public Array findRegions(string str)
        {
            var images = new MonoGameTextureAtlasImage[_atlasImages[str].Count];
            _atlasImages[str].Values.CopyTo(images, 0);
            System.Array.Sort(images);
            var regions = new MonoGameTextureAtlasRegion[images.Length];
            for (var i = 0; i < images.Length; i++)
            {
                regions[i] = images[i].textureRegion;
            }
            return Array.with(regions);
        }
    }
}