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
using Org.Mini2Dx.Core;
using Org.Mini2Dx.Core.Assets;
using Org.Mini2Dx.Core.Font;
using Org.Mini2Dx.Core.Util;
using Color = Org.Mini2Dx.Core.Graphics.Color;
using Texture = Org.Mini2Dx.Core.Graphics.Texture;

namespace monogame.Graphics
{
    public class MonoGameFrameBuffer : global::Java.Lang.Object, Org.Mini2Dx.Core.Graphics.FrameBuffer
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
        
        public void dispose_EFE09FC0()
        {
            _renderTarget.Dispose();
        }

        public void begin_EFE09FC0()
        {
            ((MonoGameGraphics)Mdx.graphicsContext_).endRendering();
            bind_EFE09FC0();
            Mdx.graphicsContext_.clearContext_EFE09FC0();
            ((MonoGameGraphics)Mdx.graphicsContext_)._gameWidth = _renderTarget.Width;
            ((MonoGameGraphics)Mdx.graphicsContext_)._gameHeight = _renderTarget.Height;
            ((MonoGameGraphics)Mdx.graphicsContext_).beginRendering();
        }

        public void end_EFE09FC0()
        {
            ((MonoGameGraphics)Mdx.graphicsContext_).endRendering();
            unbind_EFE09FC0();
            ((MonoGameGraphics)Mdx.graphicsContext_)._gameWidth = Mdx.graphicsContext_.getWindowWidth_0EE0D08D();
            ((MonoGameGraphics)Mdx.graphicsContext_)._gameHeight = Mdx.graphicsContext_.getWindowHeight_0EE0D08D();
            ((MonoGameGraphics)Mdx.graphicsContext_).beginRendering();
        }

        public void bind_EFE09FC0()
        {
            _graphicsDevice.SetRenderTarget(_renderTarget);
            ((MonoGameGraphics)Mdx.graphicsContext_)._currentRenderTarget = _renderTarget;
        }

        public void unbind_EFE09FC0()
        {
            _graphicsDevice.SetRenderTarget(null);
            ((MonoGameGraphics)Mdx.graphicsContext_)._currentRenderTarget = null;
        }

        public int getWidth_0EE0D08D()
        {
            return _renderTarget.Width;
        }

        public int getHeight_0EE0D08D()
        {
            return _renderTarget.Height;
        }

        public Texture getTexture_D75719FD()
        {
            texture.texture2D = _renderTarget;
            return texture;
        }
    }
}