﻿/*******************************************************************************
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
using System.IO;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using monogame.Files;
using monogame.Graphics;
using org.mini2Dx.core.files;
using org.mini2Dx.core.graphics;
using Color = org.mini2Dx.core.graphics.Color;
using Texture = org.mini2Dx.core.graphics.Texture;

namespace monogame
{
    class MonoGameGraphicsUtils : org.mini2Dx.core.GraphicsUtils
    {
        private readonly Game _gameInstance;

        public MonoGameGraphicsUtils()
        {
            
        }

        public MonoGameGraphicsUtils(Game game)
        {
            _gameInstance = game;
        }
        
        public Color newColor(float r, float g, float b, float a)
        {
            return new MonoGameColor(r,g,b,a);
        }

        public Color newColor(int rgba8888)
        {
            return new MonoGameColor((uint) rgba8888);
        }

        public Color newColor(int r, int g, int b, int a)
        {
            return new MonoGameColor(r,g,b,a);
        }

        public Color newColor(byte r, byte g, byte b, byte a)
        {
            return new MonoGameColor(r,g,b,a);
        }

        public NinePatch newNinePatch(Texture texture, int left, int right, int top, int bottom)
        {
            throw new NotImplementedException();
        }

        public NinePatch newNinePatch(TextureRegion region, int left, int right, int top, int bottom)
        {
            throw new NotImplementedException();
        }

        public FrameBuffer newFrameBuffer(int width, int height)
        {
            throw new NotImplementedException();
        }

        public ParticleEffect newParticleEffect()
        {
            throw new NotImplementedException();
        }

        public ParticleEffect newParticleEffect(FileHandle effectFile, FileHandle imagesDir)
        {
            throw new NotImplementedException();
        }

        public ParticleEffect newParticleEffect(FileHandle effectFile, TextureAtlas atlas)
        {
            throw new NotImplementedException();
        }

        public Pixmap newPixmap(int width, int height, PixmapFormat format)
        {
            return new MonoGamePixmap(width, height, format);
        }

        public Pixmap newPixmap(FileHandle file)
        {
            var texture = newTexture(file);
            var pixmap = new MonoGamePixmap(texture.getWidth(), texture.getHeight());
            texture.draw(pixmap, 0, 0);
            return pixmap;
        }

        public Sprite newSprite(TextureRegion texture)
        {
            return new MonoGameSprite(texture);
        }

        public Sprite newSprite(Texture texture)
        {
            return new MonoGameSprite(texture);
        }

        public Sprite newSprite(Texture texture, int width, int height)
        {
            return new MonoGameSprite(texture, width, height);
        }

        public Sprite newSprite(Texture texture, int x, int y, int width, int height)
        {
            return new MonoGameSprite(texture, x, y, width, height);
        }

        public Sprite newSprite(TextureRegion texture, int width, int height)
        {
            return new MonoGameSprite(texture, width, height);
        }

        public Sprite newSprite(TextureRegion texture, int x, int y, int width, int height)
        {
            return new MonoGameSprite(texture, x, y, width, height);
        }

        public Sprite newSprite(Sprite s)
        {
            Sprite sprite = new MonoGameSprite();
            sprite.set(s);
            return sprite;
        }

        public Texture newTexture(FileHandle fileHandle)
        {
            Texture2D texture;

            using (var fileStream = new FileStream(((MonoGameFileHandle) fileHandle).fullPath(), FileMode.Open))
            {
                texture = Texture2D.FromStream(_gameInstance.GraphicsDevice, fileStream);
            }
            return new MonoGameTexture(texture);
        }

        public Texture newTexture(FileHandle fileHandle, PixmapFormat format)
        {
            var pixmap = newPixmap(fileHandle);
            return newTexture(pixmap, pixmap.getFormat());
        }

        public Texture newTexture(Pixmap pixmap)
        {
            return newTexture(pixmap, pixmap.getFormat());
        }

        public Texture newTexture(Pixmap pixmap, PixmapFormat format)
        {
            var rawPixmap = pixmap.getPixels();

            SurfaceFormat destFormat;

            if (format == PixmapFormat.ALPHA)
            {
                destFormat = SurfaceFormat.Alpha8;
            }
            else if (format == PixmapFormat.RGBA8888)
            {
                destFormat = SurfaceFormat.ColorSRgb;
            }
            else
            {
                throw new ArgumentException("format not supported");
            }
            var texture = new Texture2D(_gameInstance.GraphicsDevice, pixmap.getWidth(), pixmap.getHeight(), false, destFormat);
            texture.SetData(rawPixmap);

            return new MonoGameTexture(texture);
        }

        public TextureAtlas newTextureAtlas(FileHandle packFile)
        {
            throw new NotImplementedException();
        }

        public TextureAtlas newTextureAtlas(FileHandle packFile, bool flip)
        {
            throw new NotImplementedException();
        }

        public TextureAtlas newTextureAtlas(FileHandle packFile, FileHandle imagesDir)
        {
            throw new NotImplementedException();
        }

        public TextureAtlas newTextureAtlas(FileHandle packFile, FileHandle imagesDir, bool flip)
        {
            throw new NotImplementedException();
        }

        public TextureRegion newTextureRegion(Texture texture)
        {
            return new MonoGameTextureRegion(texture);
        }

        public TextureRegion newTextureRegion(TextureRegion texture, int x, int y, int width, int height)
        {
            return new MonoGameTextureRegion(texture, x, y, width, height);
        }

        public TextureRegion newTextureRegion(Texture texture, int width, int height)
        {
            return new MonoGameTextureRegion(texture, width, height);
        }

        public TextureRegion newTextureRegion(Texture texture, int x, int y, int width, int height)
        {
            return new MonoGameTextureRegion(texture, x, y, width, height);
        }

        public TextureRegion newTextureRegion(TextureRegion texture)
        {
            return new MonoGameTextureRegion(texture);
        }

        public TextureRegion newTextureRegion(TextureRegion texture, int width, int height)
        {
            return new MonoGameTextureRegion(texture, width, height);
        }
    }
}
