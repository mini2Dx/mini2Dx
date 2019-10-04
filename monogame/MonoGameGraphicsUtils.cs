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
using Org.Mini2Dx.Core.Files;
using Org.Mini2Dx.Core.Graphics;
using Color = Org.Mini2Dx.Core.Graphics.Color;
using Texture = Org.Mini2Dx.Core.Graphics.Texture;

namespace monogame
{
    public class MonoGameGraphicsUtils : Org.Mini2Dx.Core.GraphicsUtils
    {
        private readonly GraphicsDevice _graphicsDevice;

        public MonoGameGraphicsUtils(GraphicsDevice graphicsDevice)
        {
            _graphicsDevice = graphicsDevice;
        }

        public override Texture newTexture(sbyte[] barr)
        {
            byte[] data = new byte[barr.Length];
            for(int i = 0; i < barr.Length; i++)
            {
                data[i] = (byte) barr[i];
            }

            var texture = Texture2D.FromStream(_graphicsDevice, new MemoryStream(data));
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

        public override Color newColor(sbyte r, sbyte g, sbyte b, sbyte a)
        {
            return new MonoGameColor(r,g,b,a);
        }

        public override Color newColor(Color color)
        {
            return new MonoGameColor(color.rb(), color.gb(), color.bb(), color.ab());
        }

        public override Color newReadOnlyColor(float r, float g, float b, float a)
        {
            return new MonoGameReadOnlyColor(r,g,b,a);
        }

        public override Color newReadOnlyColor(int rgba8888)
        {
            return new MonoGameReadOnlyColor(MonoGameColor.toMonoGameColor((uint) rgba8888));
        }

        public override Color newReadOnlyColor(int r, int g, int b, int a)
        {
            return new MonoGameReadOnlyColor(r,g,b,a);
        }

        public override Color newReadOnlyColor(sbyte r, sbyte g, sbyte b, sbyte a)
        {
            return new MonoGameReadOnlyColor(r,g,b,a);
        }

        public override Color newReadOnlyColor(Color color)
        {
            return new MonoGameReadOnlyColor(color.rb(), color.gb(), color.bb(), color.ab());
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
            if(texture is Sprite)
            {
                Sprite sprite = new MonoGameSprite();
                sprite.set((Sprite)texture);
                return sprite;
            }
            return new MonoGameSprite(texture);
        }

        public override TextureAtlasRegion newTextureAtlasRegion(Texture texture, Java.Lang.String name, int index, int x, int y, int width, int height,
            bool rotate, int originalWidth, int originalHeight, int offsetX, int offsetY)
        {
            return new MonoGameTextureAtlasRegion(texture, name, index, x, y, width, height, rotate, originalWidth, originalHeight, offsetX, offsetY);
        }

        public override TextureAtlasRegion newTextureAtlasRegion(Java.Lang.String texturePath, Java.Lang.String name, int index, int x, int y, int width, int height,
            bool rotate, int originalWidth, int originalHeight, int offsetX, int offsetY)
        {
            return new MonoGameTextureAtlasRegion(texturePath, name, index, x, y, width, height, rotate, originalWidth, originalHeight, offsetX, offsetY);
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

        public override Shader newShader(Java.Lang.String path)
        {
            return new MonoGameShader(path);
        }

        public override CustomCursor newCustomCursor(Pixmap p1, Pixmap p2, int i1, int i2)
        {
            return new MonoGameCustomCursor(p1, p2, i1, i2);
        }

        public override SpriteCache newSpriteCache()
        {
            return new MonoGameSpriteCache(_graphicsDevice);
        }
    }
}
