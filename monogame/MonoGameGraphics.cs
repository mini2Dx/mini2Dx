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

using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using monogame.Graphics;
using monogame.Util;
using org.mini2Dx.core.font;
using org.mini2Dx.core.geom;
using org.mini2Dx.core.graphics;
using org.mini2Dx.gdx.math;
using Color = org.mini2Dx.core.graphics.Color;
using Texture = org.mini2Dx.core.graphics.Texture;
using Vector2 = Microsoft.Xna.Framework.Vector2;
using Vector3 = Microsoft.Xna.Framework.Vector3;

namespace monogame
{
    public class MonoGameGraphics : org.mini2Dx.core.Graphics
    {
        private readonly SpriteBatch _spriteBatch;
        private readonly GraphicsDevice _graphicsDevice;
        private Color _setColor = new MonoGameColor(255,255,255,255);
        internal Microsoft.Xna.Framework.Color _backgroundColor = Microsoft.Xna.Framework.Color.Black;
        private Microsoft.Xna.Framework.Color _tint = Microsoft.Xna.Framework.Color.White;
        private float _rotation;
        internal int _gameWidth, _gameHeight;

        private MonoGameShapeRenderer _shapeRenderer;
        
        private static readonly BlendState _blendState = new BlendState
        {
            AlphaSourceBlend = Blend.One,
            AlphaDestinationBlend = Blend.Zero,
            ColorSourceBlend = Blend.One,
            ColorDestinationBlend = Blend.InverseSourceAlpha
        };

        private Vector2 _translation, _scale, _rotationCenter;
        private bool _isFlushing;

        public MonoGameGraphics(GraphicsDevice graphicsDevice)
        {
            _spriteBatch = new SpriteBatch(graphicsDevice);
            _graphicsDevice = graphicsDevice;
            _translation = Vector2.Zero;
            _scale = Vector2.One;
            _rotationCenter = Vector2.Zero;
            _shapeRenderer = new MonoGameShapeRenderer(graphicsDevice, MonoGameColor.toArgb(_setColor), _spriteBatch, _rotationCenter, _translation, _scale, _tint);
        }

        internal void beginSpriteBatch()
        {
            _spriteBatch.Begin(SpriteSortMode.FrontToBack, _blendState, transformMatrix: Matrix.CreateRotationZ(MonoGameMathsUtil.degreeToRadian(_rotation)) * Matrix.CreateTranslation(new Vector3((_rotationCenter + _translation) * _scale, 0)));
        }

        internal void endSpriteBatch()
        {
            _spriteBatch.End();
        }

        internal void clearGraphicsDevice(Microsoft.Xna.Framework.Color color)
        {
            _graphicsDevice.Clear(color);
        }
        
        
        public void preRender(int gameWidth, int gameHeight)
        {
            if (!_isFlushing)
            {
                clearGraphicsDevice(_backgroundColor);
            }

            _gameWidth = gameWidth;
            _gameHeight = gameHeight;
            beginSpriteBatch();
        }

        public void postRender()
        {
            _spriteBatch.End();
        }

        public void drawLineSegment(float x1, float y1, float x2, float y2)
        {
            _shapeRenderer.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        }

        public void drawRect(float x, float y, float width, float height)
        {
            _shapeRenderer.drawRect((int) x, (int) y, (int) width, (int) height);
        }

        public void fillRect(float x, float y, float width, float height)
        {
            _shapeRenderer.fillRect((int) x, (int) y, (int) width, (int) height);
        }

        public void drawCircle(float centerX, float centerY, int radius)
        {
            _shapeRenderer.drawCircle((int) centerX, (int) centerY, radius);
        }

        public void drawCircle(float centerX, float centerY, float radius)
        {
            drawCircle(centerX, centerY, (int)radius);
        }

        public void fillCircle(float centerX, float centerY, int radius)
        {
            _shapeRenderer.fillCircle((int) centerX, (int) centerY, radius);
        }

        public void fillCircle(float centerX, float centerY, float radius)
        {
            fillCircle(centerX, centerY, (int) radius);
        }

        public void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3)
        {
            _shapeRenderer.drawTriangle((int) x1, (int) y1, (int) x2, (int) y2, (int) x3, (int) y3);
        }

        public void fillTriangle(float x1, float y1, float x2, float y2, float x3, float y3)
        {
            _shapeRenderer.fillTriangle((int) x1, (int) y1, (int) x2, (int) y2, (int) x3, (int) y3);
        }

        public void drawPolygon(float[] vertices)
        {
            for (int i = 0; i < vertices.Length - 2; i+=2)
            {
                drawLineSegment(vertices[i], vertices[i+1], vertices[i + 2], vertices[i + 3]);
            }
            drawLineSegment(vertices[0], vertices[1], vertices[vertices.Length - 2], vertices[vertices.Length - 1]);
        }

        public void fillPolygon(float[] vertices, short[] triangles)
        {
            for (int i = 0; i < triangles.Length - 2; i+=3)
            {
                fillTriangle(vertices[triangles[i] * 2], vertices[triangles[i] * 2 + 1], vertices[triangles[i + 1] * 2],
                             vertices[triangles[i + 1] * 2 + 1], vertices[triangles[i + 2] * 2], vertices[triangles[i + 2] * 2 + 1]);
            }
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
            _spriteBatch.Draw(((MonoGameTexture)texture).texture2D, (new Vector2(x, y) + _translation - _rotationCenter) * _scale, null, _tint, 0,
                Vector2.Zero, new Vector2(width / texture.getWidth(), height / texture.getHeight()) * _scale,
                flipY ? SpriteEffects.FlipVertically : SpriteEffects.None, 0f);
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
            _spriteBatch.Draw(((MonoGameTextureRegion) textureRegion).toTexture2D(_graphicsDevice),
                (new Vector2(x, y) + _translation - _rotationCenter) * _scale, null, _tint, rotation, Vector2.Zero,
                new Vector2(width / textureRegion.getRegionWidth(), height / textureRegion.getRegionHeight()) *
                _scale, (textureRegion.isFlipX() ? SpriteEffects.FlipHorizontally : SpriteEffects.None) |
                (textureRegion.isFlipY() ? SpriteEffects.FlipVertically : SpriteEffects.None), 0f);
        }

        public void drawShape(Shape shape)
        {
            drawPolygon(shape.getPolygon().getVertices());
        }

        public void fillShape(Shape shape)
        {
            fillPolygon(shape.getPolygon().getVertices(), shape.getPolygon().getTriangles().toArray());
        }

        public void drawSprite(Sprite sprite)
        {
            drawSprite(sprite, sprite.getX(), sprite.getY());
        }

        public void drawSprite(Sprite sprite, float x, float y)
        {
            var origin = new Vector2(sprite.getOriginX(), sprite.getOriginY());
            _spriteBatch.Draw(((MonoGameSprite) sprite).toTexture2D(_graphicsDevice), 
                (new Vector2(x, y) + _translation + origin - _rotationCenter) * _scale, null,
                ((MonoGameColor) sprite.getTint()).toMonoGameColor(),
                MonoGameMathsUtil.degreeToRadian(((MonoGameSprite) sprite).getTotalRotation()), origin, 
                (new Vector2(sprite.getScaleX(), sprite.getScaleY())) * _scale,
                (sprite.isFlipX() ? SpriteEffects.FlipHorizontally : SpriteEffects.None) |
                (sprite.isFlipY() ? SpriteEffects.FlipVertically : SpriteEffects.None), 0f);
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
            _rotation = (_rotation + degrees) % 360;
            _rotationCenter.X = x;
            _rotationCenter.Y = y;
            _shapeRenderer.setRotationCenter(_rotationCenter);
        }

        public void setRotation(float degrees, float x, float y)
        {
            _rotation = degrees;
            rotate(0, x, y);
        }

        public void scale(float scaleX, float scaleY)
        {
            _scale.X += scaleX;
            _scale.Y += scaleY;
            _shapeRenderer.setScale(_scale);
        }

        public void setScale(float scaleX, float scaleY)
        {
            _scale.X = scaleX;
            _scale.Y = scaleY;
            _shapeRenderer.setScale(_scale);
        }

        public void clearScaling()
        {
            setScale(1, 1);
            _shapeRenderer.setScale(_scale);
        }

        public void translate(float translateX, float translateY)
        {
            _translation.X += translateX;
            _translation.Y += translateY;
            _shapeRenderer.setTranslation(_translation);
        }

        public void setTranslation(float translateX, float translateY)
        {
            _translation.X = translateX;
            _translation.Y = translateY;
            _shapeRenderer.setTranslation(_translation);
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
            _shapeRenderer.setTint(_tint);
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
            _isFlushing = true;
            postRender();
            preRender(_gameWidth, _gameHeight);
            _isFlushing = false;
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
            _setColor = color;
            _shapeRenderer.setColor(MonoGameColor.toArgb(_setColor));
        }

        public Color getBackgroundColor()
        {
            return new MonoGameColor(_backgroundColor);
        }

        public void setBackgroundColor(Color backgroundColor)
        {
            _backgroundColor = ((MonoGameColor) backgroundColor).toMonoGameColor();
        }

        public Color getTint()
        {
            return new MonoGameColor(_tint);
        }

        public float getScaleX()
        {
            return _scale.X;
        }

        public float getScaleY()
        {
            return _scale.Y;
        }

        public float getTranslationX()
        {
            return _translation.X;
        }

        public float getTranslationY()
        {
            return _translation.Y;
        }

        public float getRotation()
        {
            return _rotation;
        }

        public float getRotationX()
        {
            return _rotationCenter.X;
        }

        public float getRotationY()
        {
            return _rotationCenter.Y;
        }

        public bool isWindowReady()
        {
            return _gameHeight != 0;
        }

        public int getWindowWidth()
        {
            return _graphicsDevice.PresentationParameters.BackBufferWidth;
        }

        public int getWindowHeight()
        {
            return _graphicsDevice.PresentationParameters.BackBufferHeight;
        }
        
        public int getWindowSafeX()
        {
            return _graphicsDevice.Viewport.TitleSafeArea.X;
        }

        public int getWindowSafeY()
        {
            return _graphicsDevice.Viewport.TitleSafeArea.Y;
        }

        public int getWindowSafeWidth()
        {
            return _graphicsDevice.Viewport.TitleSafeArea.Width;
        }

        public int getWindowSafeHeight()
        {
            return _graphicsDevice.Viewport.TitleSafeArea.Height;
        }

        public float getViewportWidth()
        {
            return _gameWidth;
        }

        public float getViewportHeight()
        {
            return _gameHeight;
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