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
    public class MonoGameGraphicsUtils : org.mini2Dx.core.GraphicsUtils
    {
        private readonly GraphicsDevice _graphicsDevice;

        public MonoGameGraphicsUtils(GraphicsDevice graphicsDevice)
        {
            _graphicsDevice = graphicsDevice;
        }

        public override Texture newTexture(byte[] barr)
        {
            var texture = Texture2D.FromStream(_graphicsDevice, new MemoryStream(barr));
            return new MonoGameTexture(texture);
        }

        public override Color newColor(float r, float g, float b, float a)
        {
            return new MonoGameColor(r,g,b,a);
        }

        public override Color newColor(int rgba8888)
        {
            return new MonoGameColor(MonoGameColor.toMonoGameColor((uint) rgba8888));
        }

        public override Color newColor(int r, int g, int b, int a)
        {
            return new MonoGameColor(r,g,b,a);
        }

        public override Color newColor(byte r, byte g, byte b, byte a)
        {
            return new MonoGameColor(r,g,b,a);
        }

        public override Color newColor(Color color)
        {
            return new MonoGameColor(color.rb(), color.gb(), color.bb(), color.ab());
        }

        public override NinePatch newNinePatch(Texture texture, int left, int right, int top, int bottom)
        {
            return newNinePatch(new MonoGameTextureRegion(texture), left, right, top, bottom);
        }

        public override NinePatch newNinePatch(TextureRegion region, int left, int right, int top, int bottom)
        {
            return new MonoGameNinePatch(region, left, right, top, bottom);
        }

        public override FrameBuffer newFrameBuffer(int width, int height)
        {
            return new MonoGameFrameBuffer(_graphicsDevice, width, height);
        }

        public override ParticleEffect newParticleEffect()
        {
            throw new NotImplementedException();
        }

        public override ParticleEffect newParticleEffect(FileHandle effectFile, FileHandle imagesDir)
        {
            throw new NotImplementedException();
        }

        public override ParticleEffect newParticleEffect(FileHandle effectFile, TextureAtlas atlas)
        {
            throw new NotImplementedException();
        }

        public override Pixmap newPixmap(int width, int height, PixmapFormat format)
        {
            return new MonoGamePixmap(width, height, format);
        }

        public override Pixmap newPixmap(FileHandle file)
        {
            Texture2D texture;

            texture = ((MonoGameTexture)newTexture(file)).texture2D;

            var rawTextureData = new UInt32[texture.Width * texture.Height];
            texture.GetData(rawTextureData);
            
            var pixmap = new MonoGamePixmap(texture.Width, texture.Height);

            for (int x = 0; x < texture.Width; x++)
            {
                for (int y = 0; y < texture.Height; y++)
                {
                    pixmap.drawPixel(x, y, MonoGameColor.argbToRgba(rawTextureData[x + y * texture.Width]));
                }
            }
            
            return pixmap;
        }

        public override Sprite newSprite(TextureRegion texture)
        {
            return new MonoGameSprite(texture);
        }

        public override Sprite newSprite(Texture texture)
        {
            return new MonoGameSprite(texture);
        }

        public override Sprite newSprite(Texture texture, int width, int height)
        {
            return new MonoGameSprite(texture, width, height);
        }

        public override Sprite newSprite(Texture texture, int x, int y, int width, int height)
        {
            return new MonoGameSprite(texture, x, y, width, height);
        }

        public override Sprite newSprite(TextureRegion texture, int width, int height)
        {
            return new MonoGameSprite(texture, width, height);
        }

        public override Sprite newSprite(TextureRegion texture, int x, int y, int width, int height)
        {
            return new MonoGameSprite(texture, x, y, width, height);
        }

        public override Sprite newSprite(Sprite s)
        {
            Sprite sprite = new MonoGameSprite();
            sprite.set(s);
            return sprite;
        }

        public override Texture newTexture(FileHandle fileHandle)
        {
            try
            {
                return newTexture(fileHandle.readBytes());
            }
            catch (Exception)
            {
                return new MonoGameTexture(((MonoGameFileHandle) fileHandle).loadFromContentManager<Texture2D>());
            }
        }

        public override Texture newTexture(FileHandle fileHandle, PixmapFormat format)
        {
            var pixmap = newPixmap(fileHandle);
            return newTexture(pixmap, pixmap.getFormat());
        }

        public override Texture newTexture(Pixmap pixmap)
        {
            return newTexture(pixmap, pixmap.getFormat());
        }

        public override Texture newTexture(Pixmap pixmap, PixmapFormat format)
        {
            var rawPixmap = ((MonoGamePixmap)pixmap).toRawPixelsARGB();

            var texture = new Texture2D(_graphicsDevice, pixmap.getWidth(), pixmap.getHeight(), false, SurfaceFormat.Color);
            texture.SetData(rawPixmap);

            return new MonoGameTexture(texture);
        }

        public override TextureAtlas newTextureAtlas(FileHandle packFile)
        {
            return newTextureAtlas(packFile, false);
        }

        public override TextureAtlas newTextureAtlas(FileHandle packFile, bool flip)
        {
            return newTextureAtlas(packFile, packFile.parent(), flip);
        }

        public override TextureAtlas newTextureAtlas(FileHandle packFile, FileHandle imagesDir)
        {
            return newTextureAtlas(packFile, imagesDir, false);
        }

        public override TextureAtlas newTextureAtlas(FileHandle packFile, FileHandle imagesDir, bool flip)
        {
            return new MonoGameTextureAtlas(packFile, imagesDir, flip);
        }

        public override TextureRegion newTextureRegion(Texture texture)
        {
            return new MonoGameTextureRegion(texture);
        }

        public override TextureRegion newTextureRegion(TextureRegion texture, int x, int y, int width, int height)
        {
            return new MonoGameTextureRegion(texture, x, y, width, height);
        }

        public override TextureRegion newTextureRegion(Texture texture, int width, int height)
        {
            return new MonoGameTextureRegion(texture, width, height);
        }

        public override TextureRegion newTextureRegion(Texture texture, int x, int y, int width, int height)
        {
            return new MonoGameTextureRegion(texture, x, y, width, height);
        }

        public override TextureRegion newTextureRegion(TextureRegion texture)
        {
            return new MonoGameTextureRegion(texture);
        }

        public override TextureRegion newTextureRegion(TextureRegion texture, int width, int height)
        {
            return new MonoGameTextureRegion(texture, width, height);
        }

        public override Shader newShader(String path)
        {
            return new MonoGameShader(path);
        }

        public override CustomCursor newCustomCursor(Pixmap p1, Pixmap p2, int i1, int i2)
        {
            return new MonoGameCustomCursor(p1, p2, i1, i2);
        }
    }
}
