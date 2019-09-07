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

namespace monogame
{
    public class MonoGameGraphics : org.mini2Dx.core.Graphics
    {
        private static readonly BlendState DefaultBlending = BlendState.NonPremultiplied;
        private static readonly RasterizerState RasterizerClipping = new RasterizerState{ScissorTestEnable = true};
        private static readonly RasterizerState RasterizerNoClipping = new RasterizerState{ScissorTestEnable = false};
        
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
        private bool _isRendering;
        private GameFont _font;
        private long _frameId;
        internal RenderTarget2D _currentRenderTarget;
        private readonly MonoGameShapeRenderer _shapeRenderer;
        private BlendState _currentBlending = DefaultBlending;
        private Mini2DxBlendFunction _srcFunction = Mini2DxBlendFunction.SRC_ALPHA, _dstFunction = Mini2DxBlendFunction.ONE_MINUS_SRC_ALPHA;
        private bool _isBlending = true;
        private Matrix _transformationMatrix;
        private bool _isTransformationMatrixDirty = true;
        private Vector2 _sharedPositionVector = Vector2.Zero;
        private Vector2 _sharedScaleVector = Vector2.Zero;
        private Vector2 _sharedOriginVector = Vector2.Zero;
        private Rectangle _sharedSourceRectangle = Rectangle.Empty;

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
            _rasterizerState = RasterizerNoClipping;
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
            
            endRendering();
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

            endRendering();
        }

        private void updateBlending()
        {
            _currentBlending = _isBlending ? MonoGameGraphicsHelpers.convertBlending(_srcFunction, _dstFunction) : DefaultBlending;
        }

        internal void beginRendering()
        {
            if (!_isRendering)
            {
                if (_rasterizerState.ScissorTestEnable)
                {
                    updateClip();
                }
                _spriteBatch.Begin(
                    SpriteSortMode.Deferred,
                    _currentBlending,
                    transformMatrix: CurrentTransformationMatrix,
                    effect: _currentShader,
                    rasterizerState: _rasterizerState,
                    samplerState: _samplerState);

                if(_currentShader != null)
                {
                    _currentShader.CurrentTechnique.Passes[0].Apply();
                }

                _isRendering = true;
            }
        }

        internal void endRendering()
        {
            if (_isRendering)
            {
                _isRendering = false;
                _spriteBatch.End();
            }
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
                   Matrix.CreateTranslation(scaledRotationCenterX - scaledTranslationX, scaledRotationCenterY - scaledTranslationY, 0);
        }
        
        public void preRender(int gameWidth, int gameHeight)
        {
            _gameWidth = gameWidth;
            _gameHeight = gameHeight;
            _frameId++;
            _graphicsDevice.Clear(_backgroundColor);
        }

        public void postRender()
        {
            endRendering();
            clearScaling();
            clearShader();
            setTranslation(0, 0);
            setRotation(0, 0, 0);
            removeClip();
            _currentBlending = DefaultBlending;
        }

        public void clearContext()
        {
            internalClearContext(_backgroundColor, true, true);
        }

        public void clearContext(Color c)
        {
            clearContext(c, true, true);
        }

        public void clearContext(Color c, bool depthBufferBit, bool colorBufferBit)
        {
            internalClearContext((c as MonoGameColor)._color, depthBufferBit, colorBufferBit);
        }

        private void internalClearContext(Microsoft.Xna.Framework.Color c, bool depthBufferBit, bool colorBufferBit)
        {
            if (depthBufferBit && colorBufferBit)
            {
                _graphicsDevice.Clear(ClearOptions.Target | ClearOptions.DepthBuffer, c, _graphicsDevice.Viewport.MaxDepth, 0);
            }
            else if (depthBufferBit)
            {
                _graphicsDevice.Clear(ClearOptions.DepthBuffer, c, _graphicsDevice.Viewport.MaxDepth, 0);
            }
            else if (colorBufferBit)
            {
                _graphicsDevice.Clear(ClearOptions.Target, c, _graphicsDevice.Viewport.MaxDepth, 0);
            }
            else
            {
                _graphicsDevice.Clear(c);
            }
        }

        public void drawLineSegment(float x1, float y1, float x2, float y2)
        {
            beginRendering();
            _shapeRenderer.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        }

        public void drawRect(float x, float y, float width, float height)
        {
            beginRendering();
            _shapeRenderer.drawRect((int) x, (int) y, (int) width, (int) height);
        }

        public void fillRect(float x, float y, float width, float height)
        {
            beginRendering();
            _shapeRenderer.fillRect((int) x, (int) y, (int) width, (int) height);
        }

        public void drawCircle(float centerX, float centerY, int radius)
        {
            beginRendering();
            _shapeRenderer.drawCircle((int) centerX, (int) centerY, radius);
        }

        public void drawCircle(float centerX, float centerY, float radius)
        {
            drawCircle(centerX, centerY, (int)radius);
        }

        public void fillCircle(float centerX, float centerY, int radius)
        {
            beginRendering();
            _shapeRenderer.fillCircle((int) centerX, (int) centerY, radius);
        }

        public void fillCircle(float centerX, float centerY, float radius)
        {
            fillCircle(centerX, centerY, (int) radius);
        }

        public void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3)
        {
            beginRendering();
            _shapeRenderer.drawTriangle((int) x1, (int) y1, (int) x2, (int) y2, (int) x3, (int) y3);
        }

        public void fillTriangle(float x1, float y1, float x2, float y2, float x3, float y3)
        {
            beginRendering();
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
            beginRendering();
            _font.draw(this, text, x, y);
        }

        public void drawString(string text, float x, float y, float targetWidth)
        {
            beginRendering();
            _font.draw(this, text, x, y, targetWidth);
        }

        public void drawString(string text, float x, float y, float targetWidth, int horizontalAlign)
        {
            beginRendering();
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
            beginRendering();
            if (texture.getUAddressMode() != _currentUMode || texture.getVAddressMode() != _currentVMode)
            {
                _currentUMode = texture.getUAddressMode();
                _currentVMode = texture.getVAddressMode();
                updateAddressMode();
            }

            _sharedPositionVector.X = x;
            _sharedPositionVector.Y = y;
            _sharedScaleVector.X = width / texture.getWidth();
            _sharedScaleVector.Y = height / texture.getHeight();
            _spriteBatch.Draw(((MonoGameTexture)texture).texture2D, _sharedPositionVector, null, _tint, 0,
                Vector2.Zero, _sharedScaleVector, flipY ? SpriteEffects.FlipVertically : SpriteEffects.None, 0f);
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
            beginRendering();
            
            if (textureRegion.getTexture().getUAddressMode() != _currentUMode || textureRegion.getTexture().getVAddressMode() != _currentVMode)
            {
                _currentUMode = textureRegion.getTexture().getUAddressMode();
                _currentVMode = textureRegion.getTexture().getVAddressMode();
                updateAddressMode();
            }
            
            _sharedSourceRectangle.X = textureRegion.getRegionX();
            _sharedSourceRectangle.Y = textureRegion.getRegionY();
            _sharedSourceRectangle.Width = textureRegion.getRegionWidth();
            _sharedSourceRectangle.Height = textureRegion.getRegionHeight();
            _sharedPositionVector.X = x;
            _sharedPositionVector.Y = y;
            _sharedScaleVector.X = width / textureRegion.getRegionWidth();
            _sharedScaleVector.Y = height / textureRegion.getRegionHeight();
            
            _spriteBatch.Draw(((MonoGameTexture) textureRegion.getTexture()).texture2D, _sharedPositionVector,
                _sharedSourceRectangle, _tint, rotation, Vector2.Zero, _sharedScaleVector,
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
            beginRendering();   
            if (sprite.getTexture().getUAddressMode() != _currentUMode || sprite.getTexture().getVAddressMode() != _currentVMode)
            {
                _currentUMode = sprite.getTexture().getUAddressMode();
                _currentVMode = sprite.getTexture().getVAddressMode();
                updateAddressMode();
            }

            _sharedPositionVector.X = x + sprite.getOriginX();
            _sharedPositionVector.Y = y + sprite.getOriginY();
            _sharedSourceRectangle.X = sprite.getRegionX();
            _sharedSourceRectangle.Y = sprite.getRegionY();
            _sharedSourceRectangle.Width = sprite.getRegionWidth();
            _sharedSourceRectangle.Height = sprite.getRegionHeight();
            _sharedOriginVector.X = sprite.getOriginX();
            _sharedOriginVector.Y = sprite.getOriginY();
            _sharedScaleVector.X = sprite.getScaleX();
            _sharedScaleVector.Y = sprite.getScaleY();
            
            _spriteBatch.Draw(((MonoGameTexture) sprite.getTexture()).texture2D, _sharedPositionVector,
                _sharedSourceRectangle, ((MonoGameColor) sprite.getTint())._color,
                MonoGameMathsUtil.degreeToRadian(((MonoGameSprite) sprite).getTotalRotation()),
                _sharedOriginVector, _sharedScaleVector, (sprite.isFlipX() ? SpriteEffects.FlipHorizontally : SpriteEffects.None) |
                (sprite.isFlipY() ? SpriteEffects.FlipVertically : SpriteEffects.None), 0f);
        }

        internal void drawTexture(MonoGameTexture texture, float x, float y, int srcX, int srcY, int srcWidth,
            int srcHeight, float scaleX, float scaleY, float originX, float originY, float rotation, bool flipX,
            bool flipY, MonoGameColor tint)
        {     
            beginRendering();   
            if (texture.getUAddressMode() != _currentUMode || texture.getVAddressMode() != _currentVMode)
            {
                _currentUMode = texture.getUAddressMode();
                _currentVMode = texture.getVAddressMode();
                updateAddressMode();
            }
            

            _sharedPositionVector.X = x - originX;
            _sharedPositionVector.Y = y - originY;
            _sharedSourceRectangle.X = srcX;
            _sharedSourceRectangle.Y = srcY;
            _sharedSourceRectangle.Width = srcWidth;
            _sharedSourceRectangle.Height = srcHeight;
            _sharedOriginVector.X = originX;
            _sharedOriginVector.Y = originY;
            _sharedScaleVector.X = scaleX;
            _sharedScaleVector.Y = scaleY;
            
            _spriteBatch.Draw(texture.texture2D, _sharedPositionVector, _sharedSourceRectangle, tint._color,
                MonoGameMathsUtil.degreeToRadian(rotation), _sharedOriginVector, _sharedScaleVector,
                (flipX ? SpriteEffects.FlipHorizontally : SpriteEffects.None) |
                (flipY ? SpriteEffects.FlipVertically : SpriteEffects.None), 0f);
        }

        public void drawSpriteCache(SpriteCache spriteCache, int cacheId)
        {
            spriteCache.draw(this, cacheId);
        }

        public void drawParticleEffect(ParticleEffect effect)
        {
            throw new System.NotImplementedException();
        }

        public void drawNinePatch(NinePatch ninePatch, float x, float y, float width, float height)
        {
            beginRendering();
            ninePatch.render(this, x, y, width, height);
        }

        public void drawTilingDrawable(TilingDrawable tilingDrawable, float x, float y, float width, float height)
        {
            beginRendering();
            tilingDrawable.draw(this, x, y, width, height);
        }

        public void rotate(float degrees, float x, float y)
        {
            _rotationCenter.X = x;
            _rotationCenter.Y = y;
            _rotation = (_rotation + degrees) % 360;
            _isTransformationMatrixDirty = true;
            endRendering();
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
            endRendering();
        }

        public void setScale(float scaleX, float scaleY)
        {
            _scale.X = scaleX;
            _scale.Y = scaleY;
            _isTransformationMatrixDirty = true;
            endRendering();
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
            endRendering();
        }

        public void setTranslation(float translateX, float translateY)
        {
            _translation.X = translateX;
            _translation.Y = translateY;
            _isTransformationMatrixDirty = true;
            endRendering();
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
            endRendering();
        }

        public void disableBlending()
        {
            _isBlending = false;
            updateBlending();
            endRendering();
        }

        public void setBlendFunction(Mini2DxBlendFunction srcFunc, Mini2DxBlendFunction dstFunc)
        {
            _srcFunction = srcFunc;
            _dstFunction = dstFunc;
            updateBlending();
            endRendering();
        }

        public void clearBlendFunction()
        {
            _currentBlending = DefaultBlending;
            updateBlending();
            endRendering();
        }

        public void flush()
        {
            endRendering();
            beginRendering();
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
            _backgroundColor = ((MonoGameColor) backgroundColor)._color;
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

            if (clip.getX() == 0 && clip.getY() == 0 && clip.getWidth() == getViewportWidth() &&
                clip.getHeight() == getViewportHeight())
            {
                removeClip();
                return;
            }

            var wasClipping = _rasterizerState.ScissorTestEnable;
            
            if (!_rasterizerState.ScissorTestEnable)
            {
                _rasterizerState = RasterizerClipping;
            }

            _clipRectangle = clip;
            updateClip();
            if (!wasClipping)
            {
                endRendering();
            }
        }

        internal void updateClip()
        {
            var rect = _graphicsDevice.ScissorRectangle;
            rect.X = (int) (_clipRectangle.getX() * _scale.X - _translation.X);
            rect.Y = (int) (_clipRectangle.getY() * _scale.Y - _translation.Y);
            rect.Width = (int) (_clipRectangle.getWidth() * _scale.X);
            rect.Height = (int) (_clipRectangle.getHeight() * _scale.Y);
            _graphicsDevice.ScissorRectangle = rect;
        }

        public void setClip(float x, float y, float width, float height)
        {
            setClip(new org.mini2Dx.core.geom.Rectangle(x, y, width, height));
        }

        public org.mini2Dx.core.geom.Rectangle removeClip()
        {
            if (!_rasterizerState.ScissorTestEnable)
            {
                return null;
            }
            
            endRendering();
            
            _rasterizerState = RasterizerNoClipping;

            var oldClip = _clipRectangle.copy();
            
            _clipRectangle.setX(0);
            _clipRectangle.setY(0);
            _clipRectangle.setWidth(getViewportWidth());
            _clipRectangle.setHeight(getViewportHeight());
            
            return (org.mini2Dx.core.geom.Rectangle) oldClip;
        }

        public org.mini2Dx.core.geom.Rectangle peekClip()
        {
            var rect = new org.mini2Dx.core.geom.Rectangle();
            peekClip(rect);
            return rect;
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
            endRendering();
            _currentShader = ((MonoGameShader) shader).shader;
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