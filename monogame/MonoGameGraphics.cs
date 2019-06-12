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

using Microsoft.Xna.Framework.Graphics;
using monogame.Graphics;
using monogame.Util;
using org.mini2Dx.core;
using org.mini2Dx.core.font;
using org.mini2Dx.core.geom;
using org.mini2Dx.core.graphics;
using org.mini2Dx.gdx.math;
using Texture = org.mini2Dx.core.graphics.Texture;
using Vector2 = Microsoft.Xna.Framework.Vector2;

namespace monogame
{
    public class MonoGameGraphics : org.mini2Dx.core.Graphics
    {
        private readonly SpriteBatch _spriteBatch;
        private Pixmap _pixmap;
        private readonly GraphicsDevice _graphicsDevice;
        private readonly GraphicsUtils _graphicsUtils;
        private Color _setColor = new MonoGameColor(255,255,255,255);
        private Color _backgroundColor = new MonoGameColor(0,0,0,255);
        private Microsoft.Xna.Framework.Color _tint = Microsoft.Xna.Framework.Color.White;
        private static readonly BlendState _blendState = new BlendState
        {
            AlphaSourceBlend = Blend.One,
            AlphaDestinationBlend = Blend.Zero,
            ColorSourceBlend = Blend.One,
            ColorDestinationBlend = Blend.InverseSourceAlpha
        };
        public MonoGameGraphics(GraphicsDevice graphicsDevice)
        {
            _spriteBatch = new SpriteBatch(graphicsDevice);
            _graphicsDevice = graphicsDevice;
            _graphicsUtils = new MonoGameGraphicsUtils(graphicsDevice);
            _pixmap = _graphicsUtils.newPixmap(1, 1, PixmapFormat.RGBA8888);
        }

        private static Texture2D pixmapToTexture2D(Pixmap pixmap, GraphicsDevice graphicsDevice)
        {
            var texture = new Texture2D(graphicsDevice, pixmap.getWidth(), pixmap.getHeight(), false, SurfaceFormat.Color);
            var pixMapPixels = ((MonoGamePixmap) pixmap).toRawPixelsARGB();
            texture.SetData(pixMapPixels, 0, pixMapPixels.Length);
            return texture;
        }
        
        public void preRender(int gameWidth, int gameHeight)
        {
            _spriteBatch.Begin(SpriteSortMode.FrontToBack, _blendState);
            if (_pixmap.getWidth() != gameWidth || _pixmap.getHeight() != gameHeight)
            {
                _pixmap = _graphicsUtils.newPixmap(gameWidth, gameHeight, PixmapFormat.RGBA8888);
            }
            _pixmap.setColor(_backgroundColor);
            _pixmap.fill();
            _pixmap.setColor(_setColor);
        }

        public void postRender()
        {
            _spriteBatch.Draw(pixmapToTexture2D(_pixmap, _graphicsDevice), Vector2.Zero, _tint);
            _spriteBatch.End();
        }

        public void drawLineSegment(float x1, float y1, float x2, float y2)
        {
            _pixmap.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        }

        public void drawRect(float x, float y, float width, float height)
        {
            _pixmap.drawRectangle((int) x, (int) y, (int) width, (int) height);
        }

        public void fillRect(float x, float y, float width, float height)
        {
            _pixmap.fillRectangle((int) x, (int) y, (int) width, (int) height);
        }

        public void drawCircle(float centerX, float centerY, int radius)
        {
            _pixmap.drawCircle((int) centerX, (int) centerY, radius);
        }

        public void drawCircle(float centerX, float centerY, float radius)
        {
            _pixmap.drawCircle((int) centerX, (int) centerY, (int) radius);
        }

        public void fillCircle(float centerX, float centerY, int radius)
        {
            _pixmap.fillCircle((int) centerX, (int) centerY, radius);
        }

        public void fillCircle(float centerX, float centerY, float radius)
        {
            _pixmap.fillCircle((int) centerX, (int) centerY, (int) radius);
        }

        public void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3)
        {
            drawLineSegment(x1, y1, x2, y2);
            drawLineSegment(x2, y2, x3, y3);
            drawLineSegment(x3, y3, x1, y1);
        }

        public void fillTriangle(float x1, float y1, float x2, float y2, float x3, float y3)
        {
            _pixmap.fillTriangle((int) x1, (int) y1, (int) x2, (int) y2, (int) x3, (int) y3);
        }

        public void drawPolygon(float[] vertices)
        {
            throw new System.NotImplementedException();
        }

        public void fillPolygon(float[] vertices, short[] triangles)
        {
            throw new System.NotImplementedException();
        }

        public void drawString(string text, float x, float y)
        {
            throw new System.NotImplementedException();
        }

        public void drawString(string text, float x, float y, float targetWidth)
        {
            throw new System.NotImplementedException();
        }

        public void drawString(string text, float x, float y, float targetWidth, int horizontalAlign)
        {
            throw new System.NotImplementedException();
        }

        public void drawTexture(Texture texture, float x, float y)
        {
            drawTexture(texture, x, y, false);
        }

        public void drawTexture(Texture texture, float x, float y, bool flipY)
        {
            drawTexture(texture, x, y, texture.getWidth(), texture.getHeight(), flipY);
        }

        public void drawTexture(Texture texture, float x, float y, float width, float height)
        {
            drawTexture(texture, x, y, width, height, false);
        }

        public void drawTexture(Texture texture, float x, float y, float width, float height, bool flipY)
        {
            _spriteBatch.Draw(((MonoGameTexture)texture).texture2D, new Vector2(x, y), null, _tint, 0,
                Vector2.Zero, new Vector2(width / texture.getWidth(), height / texture.getHeight()),
                flipY ? SpriteEffects.FlipVertically : SpriteEffects.None, 1f);
        }

        public void drawTextureRegion(TextureRegion textureRegion, float x, float y)
        {
            drawTextureRegion(textureRegion, x, y, textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        }

        public void drawTextureRegion(TextureRegion textureRegion, float x, float y, float width, float height)
        {
            drawTextureRegion(textureRegion, x, y, width, height, 0);
        }

        public void drawTextureRegion(TextureRegion textureRegion, float x, float y, float width, float height, float rotation)
        {
            _spriteBatch.Draw(((MonoGameTextureRegion) textureRegion).toTexture2D(_graphicsDevice), new Vector2(x, y), null, _tint,
                rotation, Vector2.Zero,
                new Vector2(width / textureRegion.getRegionWidth(), height / textureRegion.getRegionHeight()),
                (textureRegion.isFlipX() ? SpriteEffects.FlipHorizontally : SpriteEffects.None) |
                (textureRegion.isFlipY() ? SpriteEffects.FlipVertically : SpriteEffects.None), 1f);
        }

        public void drawShape(Shape shape)
        {
            throw new System.NotImplementedException();
        }

        public void fillShape(Shape shape)
        {
            throw new System.NotImplementedException();
        }

        public void drawSprite(Sprite sprite)
        {
            drawSprite(sprite, sprite.getX(), sprite.getY());
        }

        public void drawSprite(Sprite sprite, float x, float y)
        {
            var origin = new Vector2(sprite.getOriginX(), sprite.getOriginY());
            _spriteBatch.Draw(((MonoGameSprite) sprite).toTexture2D(_graphicsDevice), new Vector2(x, y) + origin, null, ((MonoGameColor)sprite.getTint()).toMonoGameColor(),
                MonoGameMathsUtil.radianToDegree(((MonoGameSprite) sprite).getTotalRotation()), origin, 
                new Vector2(sprite.getScaleX(), sprite.getScaleY()),
                (sprite.isFlipX() ? SpriteEffects.FlipHorizontally : SpriteEffects.None) |
                (sprite.isFlipY() ? SpriteEffects.FlipVertically : SpriteEffects.None), 1f);
        }

        public void drawSpriteCache(SpriteCache spriteCache, int cacheId)
        {
            throw new System.NotImplementedException();
        }

        public void drawParticleEffect(ParticleEffect effect)
        {
            throw new System.NotImplementedException();
        }

        public void drawNinePatch(NinePatch ninePatch, float x, float y, float width, float height)
        {
            throw new System.NotImplementedException();
        }

        public void drawTilingDrawable(TilingDrawable tilingDrawable, float x, float y, float width, float height)
        {
            throw new System.NotImplementedException();
        }

        public void rotate(float degrees, float x, float y)
        {
            throw new System.NotImplementedException();
        }

        public void setRotation(float degrees, float x, float y)
        {
            throw new System.NotImplementedException();
        }

        public void scale(float scaleX, float scaleY)
        {
            throw new System.NotImplementedException();
        }

        public void setScale(float scaleX, float scaleY)
        {
            throw new System.NotImplementedException();
        }

        public void clearScaling()
        {
            throw new System.NotImplementedException();
        }

        public void translate(float translateX, float translateY)
        {
            throw new System.NotImplementedException();
        }

        public void setTranslation(float translateX, float translateY)
        {
            throw new System.NotImplementedException();
        }

        public void setClip(float x, float y, float width, float height)
        {
            throw new System.NotImplementedException();
        }

        public void setTint(Color tint)
        {
            _tint.R = tint.getRAsByte();
            _tint.G = tint.getGAsByte();
            _tint.B = tint.getBAsByte();
            _tint.A = tint.getAAsByte();
        }


        public void removeTint()
        {
            _tint.R = 255;
            _tint.G = 255;
            _tint.B = 255;
            _tint.A = 255;
        }

        public void enableBlending()
        {
            throw new System.NotImplementedException();
        }

        public void disableBlending()
        {
            throw new System.NotImplementedException();
        }

        public void setBlendFunction(int srcFunc, int dstFunc)
        {
            throw new System.NotImplementedException();
        }

        public void clearBlendFunction()
        {
            throw new System.NotImplementedException();
        }

        public void flush()
        {
            postRender();
            preRender(_pixmap.getWidth(), _pixmap.getHeight());
        }

        public int getLineHeight()
        {
            throw new System.NotImplementedException();
        }

        public void setLineHeight(int lineHeight)
        {
            throw new System.NotImplementedException();
        }

        public Color getColor()
        {
            return _setColor;
        }

        public void setColor(Color color)
        {
            _pixmap.setColor(color);
            _setColor = color;
        }

        public Color getBackgroundColor()
        {
            return _backgroundColor;
        }

        public void setBackgroundColor(Color backgroundColor)
        {
            _backgroundColor = backgroundColor;
        }

        public Color getTint()
        {
            return new MonoGameColor(_tint);
        }

        public float getScaleX()
        {
            throw new System.NotImplementedException();
        }

        public float getScaleY()
        {
            throw new System.NotImplementedException();
        }

        public float getTranslationX()
        {
            throw new System.NotImplementedException();
        }

        public float getTranslationY()
        {
            throw new System.NotImplementedException();
        }

        public float getRotation()
        {
            throw new System.NotImplementedException();
        }

        public float getRotationX()
        {
            throw new System.NotImplementedException();
        }

        public float getRotationY()
        {
            throw new System.NotImplementedException();
        }

        public bool isWindowReady()
        {
            return getWindowHeight() != 0;
        }

        public int getWindowWidth()
        {
            return _pixmap.getWidth();
        }

        public int getWindowHeight()
        {
            return _pixmap.getHeight();
        }
        
        public int getWindowSafeX()
        {
            throw new System.NotImplementedException();
        }

        public int getWindowSafeY()
        {
            throw new System.NotImplementedException();
        }

        public int getWindowSafeWidth()
        {
            throw new System.NotImplementedException();
        }

        public int getWindowSafeHeight()
        {
            throw new System.NotImplementedException();
        }

        public float getViewportWidth()
        {
            throw new System.NotImplementedException();
        }

        public float getViewportHeight()
        {
            throw new System.NotImplementedException();
        }

        public Matrix4 getProjectionMatrix()
        {
            throw new System.NotImplementedException();
        }

        public void setClip(org.mini2Dx.core.geom.Rectangle clip)
        {
            throw new System.NotImplementedException();
        }

        public org.mini2Dx.core.geom.Rectangle removeClip()
        {
            throw new System.NotImplementedException();
        }

        public org.mini2Dx.core.geom.Rectangle peekClip()
        {
            throw new System.NotImplementedException();
        }

        public void peekClip(org.mini2Dx.core.geom.Rectangle rectangle)
        {
            throw new System.NotImplementedException();
        }

        public void drawFontCache(GameFontCache gameFontCache)
        {
            throw new System.NotImplementedException();
        }

        public void setFont(GameFont font)
        {
            throw new System.NotImplementedException();
        }

        public Shader getShader()
        {
            throw new System.NotImplementedException();
        }

        public GameFont getFont()
        {
            throw new System.NotImplementedException();
        }

        public void setShader(Shader shader)
        {
            throw new System.NotImplementedException();
        }

        public void clearShader()
        {
            throw new System.NotImplementedException();
        }
    }
}