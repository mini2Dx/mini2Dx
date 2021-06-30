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
using Org.Mini2Dx.Core;
using Org.Mini2Dx.Core.Files;
using Org.Mini2Dx.Core.Font;
using Org.Mini2Dx.Core.Geom;
using Org.Mini2Dx.Core.Graphics;
using Org.Mini2Dx.Gdx.Math;
using Color = Org.Mini2Dx.Core.Graphics.Color;
using Rectangle = Microsoft.Xna.Framework.Rectangle;
using Texture = Org.Mini2Dx.Core.Graphics.Texture;
using TextureAddressMode = Org.Mini2Dx.Core.Graphics.TextureAddressMode;
using TextureFilter = Org.Mini2Dx.Core.Graphics.TextureFilter;
using Vector2 = Microsoft.Xna.Framework.Vector2;

namespace monogame
{
    public class MonoGameGraphics : global::Java.Lang.Object, Org.Mini2Dx.Core._Graphics
    {
        private static readonly BlendState DefaultBlending = BlendState.NonPremultiplied;
        private static readonly RasterizerState RasterizerClipping = new RasterizerState{ScissorTestEnable = true};
        private static readonly RasterizerState RasterizerNoClipping = new RasterizerState{ScissorTestEnable = false};
        
        internal readonly SpriteBatch _spriteBatch;
        internal readonly GraphicsDevice _graphicsDevice;
        private Color _setColor = new MonoGameColor(255,255,255,255);
        private Microsoft.Xna.Framework.Color _backgroundColor = new Microsoft.Xna.Framework.Color(0, 0, 0);
        internal Microsoft.Xna.Framework.Color _tint = new Microsoft.Xna.Framework.Color(255, 255, 255);
        internal int _gameWidth, _gameHeight;
        private MonoGameShader _defaultShader;
        private MonoGameShader _currentShader;
        private Org.Mini2Dx.Core.Geom.Rectangle _clipRectangle;
        private RasterizerState _rasterizerState;
        private SamplerState _samplerState = new SamplerState();
        private TextureFilter _currentMinFilter = TextureFilter.PIXEL_;
        private TextureFilter _currentMagFilter = TextureFilter.PIXEL_;
        private TextureAddressMode _currentUMode = TextureAddressMode.CLAMP_;
        private TextureAddressMode _currentVMode = TextureAddressMode.CLAMP_;
        private bool _isRendering;
        private GameFont _font;
        private long _frameId;
        internal RenderTarget2D _currentRenderTarget;
        private readonly MonoGameShapeRenderer _shapeRenderer;
        private BlendState _currentBlending = DefaultBlending;
        private Mini2DxBlendFunction _srcFunction = Mini2DxBlendFunction.SRC_ALPHA_, _dstFunction = Mini2DxBlendFunction.ONE_MINUS_SRC_ALPHA_;
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
            _clipRectangle = new Org.Mini2Dx.Core.Geom.Rectangle();
            _clipRectangle._init_(0, 0, getWindowWidth_0EE0D08D(), getWindowHeight_0EE0D08D());
            _shapeRenderer = new MonoGameShapeRenderer(graphicsDevice, (MonoGameColor) _setColor, _spriteBatch);
            Effect defaultEffect = null;
            _defaultShader = new MonoGameShader(defaultEffect);
            _currentShader = _defaultShader;
            _rasterizerState = RasterizerNoClipping;
            _graphicsDevice.ScissorRectangle = new Rectangle();
            _font = Mdx.fonts_.defaultFont_0370ED29();
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
                    effect: _currentShader.shader,
                    rasterizerState: _rasterizerState,
                    samplerState: _samplerState);

                if(_currentShader != null && _currentShader.shader != null)
                {
                    //_currentShader.shader.CurrentTechnique.Passes[0].Apply();
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
        
        public void preRender_224D2728(int gameWidth, int gameHeight)
        {
            _gameWidth = gameWidth;
            _gameHeight = gameHeight;
            _frameId++;
            _graphicsDevice.Clear(_backgroundColor);
        }

        public void postRender_EFE09FC0()
        {
            endRendering();
            clearScaling_EFE09FC0();
            clearShader_EFE09FC0();
            setTranslation_0948E7C0(0, 0);
            setRotation_4556C5CA(0, 0, 0);
            removeClip_A029B76C();
            _currentBlending = DefaultBlending;
        }

        public void clearContext_EFE09FC0()
        {
            internalClearContext(_backgroundColor, true, true);
        }

        public void clearContext_24D51C91(Color c)
        {
            clearContext_C68DBD81(c, true, true);
        }

        public void clearContext_C68DBD81(Color c, bool depthBufferBit, bool colorBufferBit)
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

        public void drawLineSegment_C2EDAFC0(float x1, float y1, float x2, float y2)
        {
            beginRendering();
            _shapeRenderer.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        }

        public void drawRect_C2EDAFC0(float x, float y, float width, float height)
        {
            beginRendering();
            _shapeRenderer.drawRect((int) x, (int) y, (int) width, (int) height);
        }

        public void fillRect_C2EDAFC0(float x, float y, float width, float height)
        {
            beginRendering();
            _shapeRenderer.fillRect((int) x, (int) y, (int) width, (int) height);
        }

        public void drawCircle_E32E4233(float centerX, float centerY, int radius)
        {
            beginRendering();
            _shapeRenderer.drawCircle((int) centerX, (int) centerY, radius);
        }

        public void drawCircle_4556C5CA(float centerX, float centerY, float radius)
        {
            drawCircle_E32E4233(centerX, centerY, (int)radius);
        }

        public void fillCircle_E32E4233(float centerX, float centerY, int radius)
        {
            beginRendering();
            _shapeRenderer.fillCircle((int) centerX, (int) centerY, radius);
        }

        public void fillCircle_4556C5CA(float centerX, float centerY, float radius)
        {
            fillCircle_E32E4233(centerX, centerY, (int) radius);
        }

        public void drawTriangle_0416F7C0(float x1, float y1, float x2, float y2, float x3, float y3)
        {
            beginRendering();
            _shapeRenderer.drawTriangle((int) x1, (int) y1, (int) x2, (int) y2, (int) x3, (int) y3);
        }

        public void fillTriangle_0416F7C0(float x1, float y1, float x2, float y2, float x3, float y3)
        {
            beginRendering();
            _shapeRenderer.fillTriangle((int) x1, (int) y1, (int) x2, (int) y2, (int) x3, (int) y3);
        }

        public void drawPolygon_A7F282AF(float[] vertices)
        {
            for (int i = 0; i < vertices.Length - 2; i+=2)
            {
                drawLineSegment_C2EDAFC0(vertices[i], vertices[i+1], vertices[i + 2], vertices[i + 3]);
            }
            drawLineSegment_C2EDAFC0(vertices[0], vertices[1], vertices[vertices.Length - 2], vertices[vertices.Length - 1]);
        }

        public void fillPolygon_C74D914F(float[] vertices, short[] triangles)
        {
            for (int i = 0; i < triangles.Length - 2; i+=3)
            {
                fillTriangle_0416F7C0(vertices[triangles[i] * 2], vertices[triangles[i] * 2 + 1], vertices[triangles[i + 1] * 2],
                             vertices[triangles[i + 1] * 2 + 1], vertices[triangles[i + 2] * 2], vertices[triangles[i + 2] * 2 + 1]);
            }
        }

        public void drawString_BFC06056(Java.Lang.String text, float x, float y)
        {
            beginRendering();
            _font.draw_FB6AB899(this, text, x, y);
        }

        public void drawString_2C2C4844(Java.Lang.String text, float x, float y, float targetWidth)
        {
            beginRendering();
            _font.draw_5881F77F(this, text, x, y, targetWidth);
        }

        public void drawString_8338FD87(Java.Lang.String text, float x, float y, float targetWidth, int horizontalAlign)
        {
            beginRendering();
            _font.draw_F97A968A(this, text, x, y, targetWidth, horizontalAlign, true);
        }

        public void drawTexture_95EC6133(Texture texture, float x, float y)
        {
            drawTexture_59AA8AE1(texture, x, y, false);
        }

        public void drawTexture_59AA8AE1(Texture texture, float x, float y, bool flipY)
        {
            drawTexture_04079731(texture, x, y, texture.getWidth_0EE0D08D(), texture.getHeight_0EE0D08D(), flipY);
        }

        public void drawTexture_5E52BD63(Texture texture, float x, float y, float width, float height)
        {
            drawTexture_04079731(texture, x, y, width, height, false);
        }

        public void drawTexture_04079731(Texture texture, float x, float y, float width, float height, bool flipY)
        {
            beginRendering();
            if (texture.getUAddressMode_F8C501D6() != _currentUMode || texture.getVAddressMode_F8C501D6() != _currentVMode)
            {
                _currentUMode = texture.getUAddressMode_F8C501D6();
                _currentVMode = texture.getVAddressMode_F8C501D6();
                updateAddressMode();
            }

            _sharedPositionVector.X = x;
            _sharedPositionVector.Y = y;
            _sharedScaleVector.X = width / texture.getWidth_0EE0D08D();
            _sharedScaleVector.Y = height / texture.getHeight_0EE0D08D();
            _spriteBatch.Draw(((MonoGameTexture)texture).texture2D, _sharedPositionVector, null, _tint, 0,
                Vector2.Zero, _sharedScaleVector, flipY ? SpriteEffects.FlipVertically : SpriteEffects.None, 0f);
        }

        public void drawTextureRegion_C70A626B(TextureRegion textureRegion, float x, float y)
        {
            drawTextureRegion_1F25C0DB(textureRegion, x, y, textureRegion.getRegionWidth_0EE0D08D(), textureRegion.getRegionHeight_0EE0D08D());
        }

        public void drawTextureRegion_1F25C0DB(TextureRegion textureRegion, float x, float y, float width, float height)
        {
            drawTextureRegion_AF37B61D(textureRegion, x, y, width, height, 0);
        }

        public void drawTextureRegion_AF37B61D(TextureRegion textureRegion, float x, float y, float width, float height, float rotation)
        {
            beginRendering();
            
            if (textureRegion.getTexture_D75719FD().getUAddressMode_F8C501D6() != _currentUMode || textureRegion.getTexture_D75719FD().getVAddressMode_F8C501D6() != _currentVMode)
            {
                _currentUMode = textureRegion.getTexture_D75719FD().getUAddressMode_F8C501D6();
                _currentVMode = textureRegion.getTexture_D75719FD().getVAddressMode_F8C501D6();
                updateAddressMode();
            }
            
            _sharedSourceRectangle.X = textureRegion.getRegionX_0EE0D08D();
            _sharedSourceRectangle.Y = textureRegion.getRegionY_0EE0D08D();
            _sharedSourceRectangle.Width = textureRegion.getRegionWidth_0EE0D08D();
            _sharedSourceRectangle.Height = textureRegion.getRegionHeight_0EE0D08D();
            _sharedPositionVector.X = x;
            _sharedPositionVector.Y = y;
            _sharedScaleVector.X = width / textureRegion.getRegionWidth_0EE0D08D();
            _sharedScaleVector.Y = height / textureRegion.getRegionHeight_0EE0D08D();
            
            _spriteBatch.Draw(((MonoGameTexture) textureRegion.getTexture_D75719FD()).texture2D, _sharedPositionVector,
                _sharedSourceRectangle, _tint, rotation, Vector2.Zero, _sharedScaleVector,
                (textureRegion.isFlipX_FBE0B2A4() ? SpriteEffects.FlipHorizontally : SpriteEffects.None) |
                (textureRegion.isFlipY_FBE0B2A4() ? SpriteEffects.FlipVertically : SpriteEffects.None), 0f);
        }

        public void drawShape_3BD1E2A4(Shape shape)
        {
            drawPolygon_A7F282AF(shape.getPolygon_A507AF13().getVertices_9E7A8229());
        }

        public void fillShape_3BD1E2A4(Shape shape)
        {
            fillPolygon_C74D914F(shape.getPolygon_A507AF13().getVertices_9E7A8229(), shape.getPolygon_A507AF13().getTriangles_78095B83().toArray_917A6DB2());
        }

        private void _internalDrawSprite(Sprite sprite, float x, float y, Microsoft.Xna.Framework.Color tint)
        {
            beginRendering();   
            if (sprite.getTexture_D75719FD().getUAddressMode_F8C501D6() != _currentUMode || sprite.getTexture_D75719FD().getVAddressMode_F8C501D6() != _currentVMode)
            {
                _currentUMode = sprite.getTexture_D75719FD().getUAddressMode_F8C501D6();
                _currentVMode = sprite.getTexture_D75719FD().getVAddressMode_F8C501D6();
                updateAddressMode();
            }

            _sharedPositionVector.X = x + sprite.getOriginX_FFE0B8F0();
            _sharedPositionVector.Y = y + sprite.getOriginY_FFE0B8F0();
            _sharedSourceRectangle.X = sprite.getRegionX_0EE0D08D();
            _sharedSourceRectangle.Y = sprite.getRegionY_0EE0D08D();
            _sharedSourceRectangle.Width = sprite.getRegionWidth_0EE0D08D();
            _sharedSourceRectangle.Height = sprite.getRegionHeight_0EE0D08D();
            _sharedOriginVector.X = sprite.getOriginX_FFE0B8F0();
            _sharedOriginVector.Y = sprite.getOriginY_FFE0B8F0();
            _sharedScaleVector.X = sprite.getScaleX_FFE0B8F0();
            _sharedScaleVector.Y = sprite.getScaleY_FFE0B8F0();
            
            _spriteBatch.Draw(((MonoGameTexture) sprite.getTexture_D75719FD()).texture2D, _sharedPositionVector,
                _sharedSourceRectangle, tint,
                MonoGameMathsUtil.degreeToRadian(((MonoGameSprite) sprite).getTotalRotation()),
                _sharedOriginVector, _sharedScaleVector, (sprite.isFlipX_FBE0B2A4() ? SpriteEffects.FlipHorizontally : SpriteEffects.None) |
                                                         (sprite.isFlipY_FBE0B2A4() ? SpriteEffects.FlipVertically : SpriteEffects.None), 0f);
        }

        public void drawSprite_615359F5(Sprite sprite)
        {
            //if tint is white use sprite tint
            Microsoft.Xna.Framework.Color drawColor;
            if (_tint.Equals(Microsoft.Xna.Framework.Color.White))
            {
                drawColor = ((MonoGameSprite)sprite)._actualTint;
            } else
            {
                drawColor = _tint;
            }
            _internalDrawSprite(sprite, sprite.getX_FFE0B8F0(), sprite.getY_FFE0B8F0(), drawColor);
        }

        public void drawSprite_2D162465(Sprite sprite, float x, float y)
        {     
            _internalDrawSprite(sprite, x, y, _tint);
        }

        internal void drawTexture(MonoGameTexture texture, float x, float y, int srcX, int srcY, int srcWidth,
            int srcHeight, float scaleX, float scaleY, float originX, float originY, float rotation, bool flipX,
            bool flipY, MonoGameColor tint)
        {     
            beginRendering();   
            if (texture.getUAddressMode_F8C501D6() != _currentUMode || texture.getVAddressMode_F8C501D6() != _currentVMode)
            {
                _currentUMode = texture.getUAddressMode_F8C501D6();
                _currentVMode = texture.getVAddressMode_F8C501D6();
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

        public void drawSpriteCache_C5995FF6(SpriteCache spriteCache, int cacheId)
        {
            spriteCache.draw_3AB38A38(this, cacheId);
        }

        public void drawParticleEffect_29AC8B27(ParticleEffect effect)
        {
            throw new System.NotImplementedException();
        }

        public void drawNinePatch_EE654A84(NinePatch ninePatch, float x, float y, float width, float height)
        {
            beginRendering();
            ninePatch.render_13ECCFE3(this, x, y, width, height);
        }

        public void drawTilingDrawable_68C092E7(TilingDrawable tilingDrawable, float x, float y, float width, float height)
        {
            beginRendering();
            tilingDrawable.draw_13ECCFE3(this, x, y, width, height);
        }

        public void rotate_4556C5CA(float degrees, float x, float y)
        {
            _rotationCenter.X = x;
            _rotationCenter.Y = y;
            _rotation = (_rotation + degrees) % 360;
            _isTransformationMatrixDirty = true;
            endRendering();
        }

        public void setRotation_4556C5CA(float degrees, float x, float y)
        {
            rotate_4556C5CA(degrees - _rotation, x, y);
        }

        public void scale_0948E7C0(float scaleX, float scaleY)
        {
            _scale.X *= scaleX;
            _scale.Y *= scaleY;
            _isTransformationMatrixDirty = true;
            endRendering();
        }

        public void setScale_0948E7C0(float scaleX, float scaleY)
        {
            if (MathUtils.isEqual_1548FAA4(_scale.X, scaleX) && MathUtils.isEqual_1548FAA4(_scale.Y, scaleY))
            {
                return;
            }
            endRendering();

            _scale.X = scaleX;
            _scale.Y = scaleY;
            _isTransformationMatrixDirty = true;
        }

        public void clearScaling_EFE09FC0()
        {
            setScale_0948E7C0(1, 1);
        }

        public void translate_0948E7C0(float translateX, float translateY)
        {
            _translation.X += translateX;
            _translation.Y += translateY;
            _isTransformationMatrixDirty = true;
            endRendering();
        }

        public void setTranslation_0948E7C0(float translateX, float translateY)
        {
            if (MathUtils.isEqual_1548FAA4(_translation.X, translateX) && MathUtils.isEqual_1548FAA4(_translation.Y, translateY))
            {
                return;
            }
            _translation.X = translateX;
            _translation.Y = translateY;
            _isTransformationMatrixDirty = true;
            endRendering();
        }

        public void setTint_24D51C91(Color tint)
        {
            _tint.R = (byte)tint.getRAsByte_03E0BF3C();
            _tint.G = (byte)tint.getGAsByte_03E0BF3C();
            _tint.B = (byte)tint.getBAsByte_03E0BF3C();
            _tint.A = (byte)tint.getAAsByte_03E0BF3C();
        }


        public void removeTint_EFE09FC0()
        {
            _tint.R = 255;
            _tint.G = 255;
            _tint.B = 255;
            _tint.A = 255;
        }

        public void enableBlending_EFE09FC0()
        {
            _isBlending = true;
            updateBlending();
            endRendering();
        }

        public void disableBlending_EFE09FC0()
        {
            _isBlending = false;
            updateBlending();
            endRendering();
        }

        public void setBlendFunction_AAE1F480(Mini2DxBlendFunction srcFunc, Mini2DxBlendFunction dstFunc)
        {
            _srcFunction = srcFunc;
            _dstFunction = dstFunc;
            updateBlending();
            endRendering();
        }

        public void clearBlendFunction_EFE09FC0()
        {
            _currentBlending = DefaultBlending;
            _srcFunction = Mini2DxBlendFunction.SRC_ALPHA_;
            _dstFunction = Mini2DxBlendFunction.ONE_MINUS_SRC_ALPHA_;
            updateBlending();
            endRendering();
        }

        public void flush_EFE09FC0()
        {
            endRendering();
            beginRendering();
        }

        public int getLineHeight_0EE0D08D()
        {
            return _shapeRenderer.LineHeight;
        }

        public void setLineHeight_3518BA33(int lineHeight)
        {
            _shapeRenderer.LineHeight = lineHeight;
        }

        public Color getColor_F0D7D9CF()
        {
            return _setColor;
        }

        public void setColor_24D51C91(Color color)
        {
            _setColor = color;
            _shapeRenderer.setColor((MonoGameColor) _setColor);
            _font.setColor_24D51C91(color);
        }

        public Color getBackgroundColor_F0D7D9CF()
        {
            return new MonoGameColor(_backgroundColor);
        }

        public void setBackgroundColor_24D51C91(Color backgroundColor)
        {
            _backgroundColor = ((MonoGameColor) backgroundColor)._color;
        }

        public Color getTint_F0D7D9CF()
        {
            return new MonoGameColor(_tint);
        }

        public float getScaleX_FFE0B8F0()
        {
            return _scale.X;
        }

        public float getScaleY_FFE0B8F0()
        {
            return _scale.Y;
        }

        public float getTranslationX_FFE0B8F0()
        {
            return _translation.X;
        }

        public float getTranslationY_FFE0B8F0()
        {
            return _translation.Y;
        }

        public float getRotation_FFE0B8F0()
        {
            return _rotation;
        }

        public float getRotationX_FFE0B8F0()
        {
            return _rotationCenter.X;
        }

        public float getRotationY_FFE0B8F0()
        {
            return _rotationCenter.Y;
        }

        public bool isWindowReady_FBE0B2A4()
        {
            return _gameHeight != 0;
        }

        public int getWindowWidth_0EE0D08D()
        {
            return _graphicsDevice.PresentationParameters.BackBufferWidth;
        }

        public int getWindowHeight_0EE0D08D()
        {
            return _graphicsDevice.PresentationParameters.BackBufferHeight;
        }
        
        public int getWindowSafeX_0EE0D08D()
        {
            return _graphicsDevice.Viewport.TitleSafeArea.X;
        }

        public int getWindowSafeY_0EE0D08D()
        {
            return _graphicsDevice.Viewport.TitleSafeArea.Y;
        }

        public int getWindowSafeWidth_0EE0D08D()
        {
            return _graphicsDevice.Viewport.TitleSafeArea.Width;
        }

        public int getWindowSafeHeight_0EE0D08D()
        {
            return _graphicsDevice.Viewport.TitleSafeArea.Height;
        }

        public TextureFilter getMinFilter_8B0D1E35()
        {
            return _currentMinFilter;
        }

        public void setMinFilter_0523C13B(TextureFilter tf)
        {
            if (tf != _currentMinFilter)
            {
                _currentMinFilter = tf;
                updateFilter();
            }
        }

        public TextureFilter getMagFilter_8B0D1E35()
        {
            return _currentMagFilter;
        }

        public void setMagFilter_0523C13B(TextureFilter tf)
        {
            if (tf != _currentMagFilter)
            {
                _currentMagFilter = tf;
                updateFilter();
            }
        }

        public float getViewportWidth_FFE0B8F0()
        {
            return _gameWidth / _scale.X;
        }

        public float getViewportHeight_FFE0B8F0()
        {
            return _gameHeight / _scale.Y;
        }

        public Matrix4 getProjectionMatrix_84886056()
        {
            Matrix4 result = new Matrix4();
            result._init_(Matrix.ToFloatArray(CurrentTransformationMatrix));
            return result;
        }

        public void setClip_477DF50E(Org.Mini2Dx.Core.Geom.Rectangle clip)
        {

            if (clip.getX_FFE0B8F0() == 0 && clip.getY_FFE0B8F0() == 0 && clip.getWidth_FFE0B8F0() == getViewportWidth_FFE0B8F0() &&
                clip.getHeight_FFE0B8F0() == getViewportHeight_FFE0B8F0())
            {
                removeClip_A029B76C();
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
            rect.X = (int) ((_clipRectangle.getX_FFE0B8F0() - _translation.X) * _scale.X);
            rect.Y = (int) ((_clipRectangle.getY_FFE0B8F0() - _translation.Y) * _scale.Y);
            rect.Width = (int) (_clipRectangle.getWidth_FFE0B8F0() * _scale.X);
            rect.Height = (int) (_clipRectangle.getHeight_FFE0B8F0() * _scale.Y);
            _graphicsDevice.ScissorRectangle = rect;
        }

        public void setClip_C2EDAFC0(float x, float y, float width, float height)
        {
            var rect = new Org.Mini2Dx.Core.Geom.Rectangle();
            rect._init_(x, y, width, height);
            setClip_477DF50E(rect);
        }

        public Org.Mini2Dx.Core.Geom.Rectangle removeClip_A029B76C()
        {
            if (!_rasterizerState.ScissorTestEnable)
            {
                return null;
            }
            
            endRendering();
            
            _rasterizerState = RasterizerNoClipping;

            var oldClip = _clipRectangle.copy_46F53776();
            
            _clipRectangle.setX_97413DCA(0);
            _clipRectangle.setY_97413DCA(0);
            _clipRectangle.setWidth_0188706E(getViewportWidth_FFE0B8F0());
            _clipRectangle.setHeight_0188706E(getViewportHeight_FFE0B8F0());
            
            return (Org.Mini2Dx.Core.Geom.Rectangle) oldClip;
        }

        public Org.Mini2Dx.Core.Geom.Rectangle peekClip_A029B76C()
        {
            var rect = new Org.Mini2Dx.Core.Geom.Rectangle();
            rect._init_();
            peekClip_477DF50E(rect);
            return rect;
        }

        public void peekClip_477DF50E(Org.Mini2Dx.Core.Geom.Rectangle rectangle)
        {
            rectangle.setXY_0948E7C0(_clipRectangle.getX_FFE0B8F0(), _clipRectangle.getY_FFE0B8F0());
            rectangle.setHeight_0188706E(_clipRectangle.getHeight_FFE0B8F0());
            rectangle.setWidth_0188706E(_clipRectangle.getWidth_FFE0B8F0());
        }

        public void drawFontCache_CF61996B(GameFontCache gameFontCache)
        {
            gameFontCache.draw_2CFA5803(this);
        }

        public void setFont_6B60E80F(GameFont font)
        {
            _font = font;
            _font.setColor_24D51C91(_setColor);
        }

        public Shader getShader_364FDDC3()
        {
            return _currentShader;
        }

        public GameFont getFont_0370ED29()
        {
            return _font;
        }

        public void setShader_09F44B85(Shader shader)
        {
            if(shader == null)
            {
                clearShader_EFE09FC0();
                return;
            }
            endRendering();
            _currentShader = ((MonoGameShader) shader);
        }

        public void clearShader_EFE09FC0()
        {
            endRendering();
            _currentShader = _defaultShader;
        }

        public long getFrameId_0BE0CBD4()
        {
            return _frameId;
        }
    }
}