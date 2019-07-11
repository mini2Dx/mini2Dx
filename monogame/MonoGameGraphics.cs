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
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using monogame.Font;
using monogame.Graphics;
using monogame.Util;
using org.mini2Dx.core;
using org.mini2Dx.core.font;
using org.mini2Dx.core.geom;
using org.mini2Dx.core.graphics;
using org.mini2Dx.gdx.math;
using Color = org.mini2Dx.core.graphics.Color;
using Rectangle = Microsoft.Xna.Framework.Rectangle;
using Texture = org.mini2Dx.core.graphics.Texture;
using TextureAddressMode = org.mini2Dx.core.graphics.TextureAddressMode;
using TextureFilter = org.mini2Dx.core.graphics.TextureFilter;
using Vector2 = Microsoft.Xna.Framework.Vector2;
using Vector3 = Microsoft.Xna.Framework.Vector3;

namespace monogame
{
    public class MonoGameGraphics : org.mini2Dx.core.Graphics
    {
        private static readonly BlendState _defaultBlending = BlendState.NonPremultiplied; 
        
        internal readonly SpriteBatch _spriteBatch;
        internal readonly GraphicsDevice _graphicsDevice;
        private Color _setColor = new MonoGameColor(255,255,255,255);
        private Microsoft.Xna.Framework.Color _backgroundColor = Microsoft.Xna.Framework.Color.Black;
        internal Microsoft.Xna.Framework.Color _tint = Microsoft.Xna.Framework.Color.White;
        internal int _gameWidth, _gameHeight;
        private Effect _currentShader;
        private org.mini2Dx.core.geom.Rectangle _clipRectangle;
        private RasterizerState _rasterizerState;
        private SamplerState _samplerState = new SamplerState();
        private TextureFilter _currentMinFilter = TextureFilter.PIXEL;
        private TextureFilter _currentMagFilter = TextureFilter.PIXEL;
        private TextureAddressMode _currentUMode = TextureAddressMode.CLAMP;
        private TextureAddressMode _currentVMode = TextureAddressMode.CLAMP;
        private bool _beginSpriteBatchCalled;
        private GameFont _font;
        private long _frameId;
        internal RenderTarget2D _currentRenderTarget;
        private readonly MonoGameShapeRenderer _shapeRenderer;
        private BlendState _currentBlending = _defaultBlending;
        private Mini2DxBlendFunction _srcFunction = Mini2DxBlendFunction.SRC_ALPHA, _dstFunction = Mini2DxBlendFunction.ONE_MINUS_SRC_ALPHA;
        private bool _isBlending = true;
        private Matrix _transformationMatrix;
        private bool _isTransformationMatrixDirty = true;

        private Vector2 _scale = Vector2.One;
        private Vector2 _translation = Vector2.Zero;
        private Vector2 _rotationCenter = Vector2.Zero;
        private float _rotation;

        private Matrix CurrentTransformationMatrix
        {
            get
            {
                if (_isTransformationMatrixDirty)
                {
                    _transformationMatrix = createTransformationMatrix();
                    _isTransformationMatrixDirty = false;
                }

                return _transformationMatrix;
            }
        }

        public MonoGameGraphics(GraphicsDevice graphicsDevice)
        {
            _spriteBatch = new SpriteBatch(graphicsDevice);
            _graphicsDevice = graphicsDevice;
            _clipRectangle = new org.mini2Dx.core.geom.Rectangle(0, 0, getWindowWidth(), getWindowHeight());
            _shapeRenderer = new MonoGameShapeRenderer(graphicsDevice, (MonoGameColor) _setColor, _spriteBatch);
            _rasterizerState = new RasterizerState {ScissorTestEnable = false};
            _graphicsDevice.ScissorRectangle = new Rectangle();
            _font = Mdx.fonts.defaultFont();
            updateFilter();
        }

        private void updateAddressMode()
        {
            _samplerState = new SamplerState
            {
                Filter = _samplerState.Filter,
                AddressU = MonoGameGraphicsHelpers.convertTextureAddressMode(_currentUMode),
                AddressV = MonoGameGraphicsHelpers.convertTextureAddressMode(_currentVMode)
            };

            if (_beginSpriteBatchCalled)
            {
                flush();
            }
        }

        private void updateFilter()
        {
            var newSamplerState = new SamplerState
            {
                AddressU = _samplerState.AddressU,
                AddressV = _samplerState.AddressV,
                Filter = MonoGameGraphicsHelpers.convertTextureFilter(_currentMinFilter, _currentMagFilter)
            };

            _samplerState = newSamplerState;

            if (_beginSpriteBatchCalled)
            {
                flush();
            }
        }

        private void updateBlending()
        {
            _currentBlending = _isBlending ? MonoGameGraphicsHelpers.convertBlending(_srcFunction, _dstFunction) : _defaultBlending;
        }

        internal void beginSpriteBatch()
        {
            _spriteBatch.Begin(
                SpriteSortMode.Deferred, 
                _currentBlending,
                transformMatrix: CurrentTransformationMatrix,
                effect: _currentShader,
                rasterizerState: _rasterizerState,
                samplerState: _samplerState);
            _beginSpriteBatchCalled = true;
        }

        private Matrix createTransformationMatrix()
        {
            var scaledRotationCenterX = _rotationCenter.X * _scale.X;
            var scaledRotationCenterY = _rotationCenter.Y * _scale.Y;
            var scaledTranslationX = _translation.X * _scale.X;
            var scaledTranslationY = _translation.Y * _scale.Y;
            return Matrix.CreateScale(_scale.X, _scale.Y, 1) * 
                   Matrix.CreateTranslation(-scaledRotationCenterX, -scaledRotationCenterY, 0) *
                   Matrix.CreateRotationZ(MonoGameMathsUtil.degreeToRadian(_rotation)) *
                   Matrix.CreateTranslation(scaledRotationCenterX, scaledRotationCenterY, 0) *
                   Matrix.CreateTranslation(-scaledTranslationX, -scaledTranslationY, 0);
        }

        internal void endSpriteBatch()
        {
            _spriteBatch.End();
            _beginSpriteBatchCalled = false;
        }
        
        public void preRender(int gameWidth, int gameHeight)
        {
            _graphicsDevice.Clear(_backgroundColor);
            _gameWidth = gameWidth;
            _gameHeight = gameHeight;
            _frameId++;
            beginSpriteBatch();
        }

        public void postRender()
        {
            endSpriteBatch();
            clearScaling();
            clearShader();
            setTranslation(0, 0);
            setRotation(0, 0, 0);
            _currentBlending = _defaultBlending;
        }

        public void clearContext()
        {
            _graphicsDevice.Clear(_backgroundColor);
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
            _font.draw(this, text, x, y);
        }

        public void drawString(string text, float x, float y, float targetWidth)
        {
            _font.draw(this, text, x, y, targetWidth);
        }

        public void drawString(string text, float x, float y, float targetWidth, int horizontalAlign)
        {
            _font.draw(this, text, x, y, targetWidth, horizontalAlign, true);
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
            if (texture.getUAddressMode() != _currentUMode || texture.getVAddressMode() != _currentVMode)
            {
                _currentUMode = texture.getUAddressMode();
                _currentVMode = texture.getVAddressMode();
                updateAddressMode();
            }
            _spriteBatch.Draw(((MonoGameTexture)texture).texture2D, new Vector2(x, y), null, _tint, 0,
                Vector2.Zero, new Vector2(width / texture.getWidth(), height / texture.getHeight()),
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
            if (textureRegion.getTexture().getUAddressMode() != _currentUMode || textureRegion.getTexture().getVAddressMode() != _currentVMode)
            {
                _currentUMode = textureRegion.getTexture().getUAddressMode();
                _currentVMode = textureRegion.getTexture().getVAddressMode();
                updateAddressMode();
            }
            var sourceRectangle = new Rectangle(textureRegion.getRegionX(), textureRegion.getRegionY(), textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
            if (textureRegion.isFlipX())
            {
                sourceRectangle.X -= sourceRectangle.Width;
            }

            if (textureRegion.isFlipY())
            {
                sourceRectangle.Y -= sourceRectangle.Height;
            }
            _spriteBatch.Draw(((MonoGameTexture) textureRegion.getTexture()).texture2D,
                new Vector2(x, y), sourceRectangle, _tint, rotation, Vector2.Zero, 
                new Vector2(width / textureRegion.getRegionWidth(), height / textureRegion.getRegionHeight()),
                (textureRegion.isFlipX() ? SpriteEffects.FlipHorizontally : SpriteEffects.None) |
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
            var sourceRectangle = new Rectangle(sprite.getRegionX(), sprite.getRegionY(), sprite.getRegionWidth(), sprite.getRegionHeight());
            if (sprite.isFlipX())
            {
                sourceRectangle.X -= sourceRectangle.Width * 2;
            }

            if (sprite.isFlipY())
            {
                sourceRectangle.Y -= sourceRectangle.Height * 2;
            }
            var origin = new Vector2(sprite.getOriginX(), sprite.getOriginY());
            _spriteBatch.Draw(((MonoGameTexture) sprite.getTexture()).texture2D, 
                new Vector2(x, y) + origin, sourceRectangle,
                ((MonoGameColor) sprite.getTint()).toMonoGameColor(),
                MonoGameMathsUtil.degreeToRadian(((MonoGameSprite) sprite).getTotalRotation()), origin, 
                new Vector2(sprite.getScaleX(), sprite.getScaleY()),
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
            ninePatch.render(this, x, y, width, height);
        }

        public void drawTilingDrawable(TilingDrawable tilingDrawable, float x, float y, float width, float height)
        {
            tilingDrawable.draw(this, x, y, width, height);
        }

        public void rotate(float degrees, float x, float y)
        {
            _rotationCenter.X = x;
            _rotationCenter.Y = y;
            _rotation = (_rotation + degrees) % 360;
            _isTransformationMatrixDirty = true;
            flush();
        }

        public void setRotation(float degrees, float x, float y)
        {
            rotate(degrees - _rotation, x, y);
        }

        public void scale(float scaleX, float scaleY)
        {
            _scale.X *= scaleX;
            _scale.Y *= scaleY;
            _isTransformationMatrixDirty = true;
            flush();
        }

        public void setScale(float scaleX, float scaleY)
        {
            _scale.X = scaleX;
            _scale.Y = scaleY;
            _isTransformationMatrixDirty = true;
            flush();
        }

        public void clearScaling()
        {
            setScale(1, 1);
        }

        public void translate(float translateX, float translateY)
        {
            _translation.X += translateX;
            _translation.Y += translateY;
            _isTransformationMatrixDirty = true;
            flush();
        }

        public void setTranslation(float translateX, float translateY)
        {
            _translation.X = translateX;
            _translation.Y = translateY;
            _isTransformationMatrixDirty = true;
            flush();
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
            _isBlending = true;
            updateBlending();
            flush();
        }

        public void disableBlending()
        {
            _isBlending = false;
            updateBlending();
            flush();
        }

        public void setBlendFunction(Mini2DxBlendFunction srcFunc, Mini2DxBlendFunction dstFunc)
        {
            _srcFunction = srcFunc;
            _dstFunction = dstFunc;
            updateBlending();
            flush();
        }

        public void clearBlendFunction()
        {
            _currentBlending = _defaultBlending;
            updateBlending();
            flush();
        }

        public void flush()
        {
            if (_beginSpriteBatchCalled)
            {
                endSpriteBatch();
                beginSpriteBatch();
            }
        }

        public int getLineHeight()
        {
            return _shapeRenderer.LineHeight;
        }

        public void setLineHeight(int lineHeight)
        {
            _shapeRenderer.LineHeight = lineHeight;
        }

        public Color getColor()
        {
            return _setColor;
        }

        public void setColor(Color color)
        {
            _setColor = color;
            _shapeRenderer.setColor((MonoGameColor) _setColor);
            _font.setColor(color);
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

        public TextureFilter getMinFilter()
        {
            return _currentMinFilter;
        }

        public void setMinFilter(TextureFilter tf)
        {
            if (tf != _currentMinFilter)
            {
                _currentMinFilter = tf;
                updateFilter();
            }
        }

        public TextureFilter getMagFilter()
        {
            return _currentMagFilter;
        }

        public void setMagFilter(TextureFilter tf)
        {
            if (tf != _currentMagFilter)
            {
                _currentMagFilter = tf;
                updateFilter();
            }
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
            return new Matrix4(Matrix.ToFloatArray(CurrentTransformationMatrix));
        }

        public void setClip(org.mini2Dx.core.geom.Rectangle clip)
        {
            _rasterizerState = new RasterizerState() { ScissorTestEnable = true };
            _clipRectangle = clip;
            var rect = _graphicsDevice.ScissorRectangle;
            rect.X = (int) (clip.getX() * _scale.X);
            rect.Y = (int) (clip.getY() * _scale.Y);
            rect.Width = (int) (clip.getWidth() * _scale.X);
            rect.Height = (int) (clip.getHeight() * _scale.Y);
            _graphicsDevice.ScissorRectangle = rect;
            if (_beginSpriteBatchCalled)
            {
                flush();
            }
        }

        public void setClip(float x, float y, float width, float height)
        {
            setClip(new org.mini2Dx.core.geom.Rectangle(x, y, width, height));
        }

        public org.mini2Dx.core.geom.Rectangle removeClip()
        {
            if (_rasterizerState.ScissorTestEnable)
            {
                _rasterizerState = new RasterizerState() { ScissorTestEnable = false };

                if (_beginSpriteBatchCalled)
                {
                    flush();
                }

                var oldClip = _clipRectangle;
                _clipRectangle = new org.mini2Dx.core.geom.Rectangle(0, 0, getViewportWidth(), getViewportHeight());
                return oldClip;
            }

            return _clipRectangle;
        }

        public org.mini2Dx.core.geom.Rectangle peekClip()
        {
            return _clipRectangle;
        }

        public void peekClip(org.mini2Dx.core.geom.Rectangle rectangle)
        {
            rectangle.setXY(_clipRectangle.getX(), _clipRectangle.getY());
            rectangle.setHeight(_clipRectangle.getHeight());
            rectangle.setWidth(_clipRectangle.getWidth());
        }

        public void drawFontCache(GameFontCache gameFontCache)
        {
            gameFontCache.draw(this);
        }

        public void setFont(GameFont font)
        {
            _font = font;
        }

        public Shader getShader()
        {
            return new MonoGameShader(_currentShader);
        }

        public GameFont getFont()
        {
            return _font;
        }

        public void setShader(Shader shader)
        {
            _currentShader = ((MonoGameShader) shader).shader;

            if (_beginSpriteBatchCalled)
            {
                flush();
            }
        }

        public void clearShader()
        {
            _currentShader = null;
        }

        public long getFrameId()
        {
            return _frameId;
        }
    }
}