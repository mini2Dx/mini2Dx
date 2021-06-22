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

        public MonoGameGraphicsUtils(GraphicsDevice graphicsDevice) : base()
        {
            base._init_();
            _graphicsDevice = graphicsDevice;
        }

        public override Texture newTexture_4CDE97CA(sbyte[] barr)
        {
            byte[] data = new byte[barr.Length];
            for(int i = 0; i < barr.Length; i++)
            {
                data[i] = (byte) barr[i];
            }

            var texture = Texture2D.FromStream(_graphicsDevice, new MemoryStream(data));
            return new MonoGameTexture(texture);
        }

        public override Color newColor_DF74E9CF(float r, float g, float b, float a)
        {
            return new MonoGameColor(r,g,b,a);
        }

        public override Color newColor_290C98AC(int rgba8888)
        {
            return new MonoGameColor(MonoGameColor.toMonoGameColor((uint) rgba8888));
        }

        public override Color newColor_AD27635F(int r, int g, int b, int a)
        {
            return new MonoGameColor(r,g,b,a);
        }

        public override Color newColor_2CE9FB0F(sbyte r, sbyte g, sbyte b, sbyte a)
        {
            return new MonoGameColor(r,g,b,a);
        }

        public override Color newColor_F18CABCA(Color color)
        {
            return new MonoGameColor(color.rb_03E0BF3C(), color.gb_03E0BF3C(), color.bb_03E0BF3C(), color.ab_03E0BF3C());
        }

        public override Color newReadOnlyColor_DF74E9CF(float r, float g, float b, float a)
        {
            return new MonoGameReadOnlyColor(r,g,b,a);
        }

        public override Color newReadOnlyColor_290C98AC(int rgba8888)
        {
            return new MonoGameReadOnlyColor(MonoGameColor.toMonoGameColor((uint) rgba8888));
        }

        public override Color newReadOnlyColor_AD27635F(int r, int g, int b, int a)
        {
            return new MonoGameReadOnlyColor(r,g,b,a);
        }

        public override Color newReadOnlyColor_2CE9FB0F(sbyte r, sbyte g, sbyte b, sbyte a)
        {
            return new MonoGameReadOnlyColor(r,g,b,a);
        }

        public override Color newReadOnlyColor_F18CABCA(Color color)
        {
            return new MonoGameReadOnlyColor(color.rb_03E0BF3C(), color.gb_03E0BF3C(), color.bb_03E0BF3C(), color.ab_03E0BF3C());
        }

        public override NinePatch newNinePatch_D8829279(Texture texture, int left, int right, int top, int bottom)
        {
            return newNinePatch_E8B73EE1(new MonoGameTextureRegion(texture), left, right, top, bottom);
        }

        public override NinePatch newNinePatch_E8B73EE1(TextureRegion region, int left, int right, int top, int bottom)
        {
            return new MonoGameNinePatch(region, left, right, top, bottom);
        }

        public override FrameBuffer newFrameBuffer_9F3C0E57(int width, int height)
        {
            return new MonoGameFrameBuffer(_graphicsDevice, width, height);
        }

        public override ParticleEffect newParticleEffect_5F264111()
        {
            throw new NotImplementedException();
        }

        public override ParticleEffect newParticleEffect_D29FBBD1(FileHandle effectFile, FileHandle imagesDir)
        {
            throw new NotImplementedException();
        }

        public override ParticleEffect newParticleEffect_21903989(FileHandle effectFile, TextureAtlas atlas)
        {
            throw new NotImplementedException();
        }

        public override Pixmap newPixmap_990CB4F7(int width, int height, PixmapFormat format)
        {
            return new MonoGamePixmap(width, height, format);
        }

        public override Pixmap newPixmap_7C2D11FD(FileHandle file)
        {
            Texture2D texture;

            texture = ((MonoGameTexture)newTexture_69120FDF(file)).texture2D;

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

        public override Sprite newSprite_CB432FF0(TextureRegion texture)
        {
            if(texture is Sprite)
            {
                Sprite sprite = new MonoGameSprite();
                sprite.set_615359F5((Sprite)texture);
                return sprite;
            }
            return new MonoGameSprite(texture);
        }

        public override TextureAtlasRegion newTextureAtlasRegion_95DF68AC(Texture texture, Java.Lang.String name, int index, int x, int y, int width, int height,
            bool rotate, int originalWidth, int originalHeight, int offsetX, int offsetY)
        {
            return new MonoGameTextureAtlasRegion(texture, name, index, x, y, width, height, rotate, originalWidth, originalHeight, offsetX, offsetY);
        }

        public override TextureAtlasRegion newTextureAtlasRegion_5044756D(Java.Lang.String texturePath, Java.Lang.String name, int index, int x, int y, int width, int height,
            bool rotate, int originalWidth, int originalHeight, int offsetX, int offsetY)
        {
            return new MonoGameTextureAtlasRegion(texturePath, name, index, x, y, width, height, rotate, originalWidth, originalHeight, offsetX, offsetY);
        }

        public override Sprite newSprite_768542D8(Texture texture)
        {
            return new MonoGameSprite(texture);
        }

        public override Sprite newSprite_F735C8D0(Texture texture, int width, int height)
        {
            return new MonoGameSprite(texture, width, height);
        }

        public override Sprite newSprite_93C59988(Texture texture, int x, int y, int width, int height)
        {
            return new MonoGameSprite(texture, x, y, width, height);
        }

        public override Sprite newSprite_AD1B3FA8(TextureRegion texture, int width, int height)
        {
            return new MonoGameSprite(texture, width, height);
        }

        public override Sprite newSprite_67C53C60(TextureRegion texture, int x, int y, int width, int height)
        {
            return new MonoGameSprite(texture, x, y, width, height);
        }

        public override Texture newTexture_69120FDF(FileHandle fileHandle)
        {
            try
            {
                return newTexture_4CDE97CA(fileHandle.readBytes_A27A8875());
            }
            catch (Exception)
            {
                return new MonoGameTexture(((MonoGameFileHandle) fileHandle).loadFromContentManager<Texture2D>());
            }
        }

        public override Texture newTexture_D5999D8F(FileHandle fileHandle, PixmapFormat format)
        {
            var pixmap = newPixmap_7C2D11FD(fileHandle);
            return newTexture_5B771B24(pixmap, pixmap.getFormat_4C177B62());
        }

        public override Texture newTexture_5A25B7D4(Pixmap pixmap)
        {
            return newTexture_5B771B24(pixmap, pixmap.getFormat_4C177B62());
        }

        public override Texture newTexture_5B771B24(Pixmap pixmap, PixmapFormat format)
        {
            var rawPixmap = ((MonoGamePixmap)pixmap).toRawPixelsARGB();

            var texture = new Texture2D(_graphicsDevice, pixmap.getWidth_0EE0D08D(), pixmap.getHeight_0EE0D08D(), false, SurfaceFormat.Color);
            texture.SetData(rawPixmap);

            return new MonoGameTexture(texture);
        }

        public override TextureRegion newTextureRegion_F6DA8362(Texture texture)
        {
            return new MonoGameTextureRegion(texture);
        }

        public override TextureRegion newTextureRegion_8A18329A(TextureRegion texture, int x, int y, int width, int height)
        {
            return new MonoGameTextureRegion(texture, x, y, width, height);
        }

        public override TextureRegion newTextureRegion_B9E5B3CA(Texture texture, int width, int height)
        {
            return new MonoGameTextureRegion(texture, width, height);
        }

        public override TextureRegion newTextureRegion_3F7528D2(Texture texture, int x, int y, int width, int height)
        {
            return new MonoGameTextureRegion(texture, x, y, width, height);
        }

        public override TextureRegion newTextureRegion_25152E6A(TextureRegion texture)
        {
            return new MonoGameTextureRegion(texture);
        }

        public override TextureRegion newTextureRegion_BCDF5E72(TextureRegion texture, int width, int height)
        {
            return new MonoGameTextureRegion(texture, width, height);
        }

        public override Shader newShader_FAA50909(Java.Lang.String path)
        {
            return new MonoGameShader(path);
        }

        public override CustomCursor newCustomCursor_0F969A1B(Pixmap p1, Pixmap p2, int i1, int i2)
        {
            return new MonoGameCustomCursor(p1, p2, i1, i2);
        }

        public override SpriteCache newSpriteCache_B8D502BF()
        {
            return new MonoGameSpriteCache(_graphicsDevice);
        }
    }
}
