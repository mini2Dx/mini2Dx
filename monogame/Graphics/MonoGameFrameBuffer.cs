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
using org.mini2Dx.core;
using Texture = org.mini2Dx.core.graphics.Texture;

namespace monogame.Graphics
{
    public class MonoGameFrameBuffer : org.mini2Dx.core.graphics.FrameBuffer
    {
        private GraphicsDevice _graphicsDevice;
        private RenderTarget2D _renderTarget;
        private MonoGameTexture texture;

        public MonoGameFrameBuffer(GraphicsDevice graphicsDevice, int width, int height)
        {
            _graphicsDevice = graphicsDevice;
            _renderTarget = new RenderTarget2D(graphicsDevice, width, height, false, SurfaceFormat.Color, DepthFormat.Depth24, 0, RenderTargetUsage.PreserveContents);
            texture = new MonoGameTexture(null);
        }
        
        public void dispose()
        {
            _renderTarget.Dispose();
        }

        public void begin()
        {
            ((MonoGameGraphics)Mdx.graphicsContext).endSpriteBatch();
            bind();
            ((MonoGameGraphics)Mdx.graphicsContext).clearGraphicsDevice(((MonoGameGraphics)Mdx.graphicsContext)._backgroundColor);
            ((MonoGameGraphics)Mdx.graphicsContext)._gameWidth = _renderTarget.Width;
            ((MonoGameGraphics)Mdx.graphicsContext)._gameHeight = _renderTarget.Height;
            ((MonoGameGraphics)Mdx.graphicsContext).beginSpriteBatch();
        }

        public void end()
        {
            ((MonoGameGraphics)Mdx.graphicsContext).endSpriteBatch();
            unbind();
            ((MonoGameGraphics)Mdx.graphicsContext)._gameWidth = Mdx.graphicsContext.getWindowWidth();
            ((MonoGameGraphics)Mdx.graphicsContext)._gameHeight = Mdx.graphicsContext.getWindowHeight();
            ((MonoGameGraphics)Mdx.graphicsContext).beginSpriteBatch();
        }

        public void bind()
        {
            _graphicsDevice.SetRenderTarget(_renderTarget);
            ((MonoGameGraphics)Mdx.graphicsContext)._currentRenderTarget = _renderTarget;
        }

        public void unbind()
        {
            _graphicsDevice.SetRenderTarget(null);
            ((MonoGameGraphics)Mdx.graphicsContext)._currentRenderTarget = null;
        }

        public int getWidth()
        {
            return _renderTarget.Width;
        }

        public int getHeight()
        {
            return _renderTarget.Height;
        }

        public Texture getTexture()
        {
            texture.texture2D = _renderTarget;
            return texture;
        }
    }
}