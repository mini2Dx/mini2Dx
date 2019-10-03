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
using Microsoft.Xna.Framework.Graphics;
using Org.Mini2Dx.Core;
using Org.Mini2Dx.Core.Assets;
using Org.Mini2Dx.Core.Font;
using Org.Mini2Dx.Core.Util;
using Org.Mini2Dx.Core.Graphics;
using Color = Org.Mini2Dx.Core.Graphics.Color;
using TextureAddressMode = Org.Mini2Dx.Core.Graphics.TextureAddressMode;
using TextureFilter = Org.Mini2Dx.Core.Graphics.TextureFilter;

namespace monogame.Util
{
    internal class MonoGameGraphicsHelpers
    {
        public static Microsoft.Xna.Framework.Graphics.TextureAddressMode convertTextureAddressMode(TextureAddressMode mode)
        {
            if (mode == TextureAddressMode.CLAMP)
            {
                return Microsoft.Xna.Framework.Graphics.TextureAddressMode.Clamp;
            }
            else if (mode == TextureAddressMode.MIRROR)
            {
                return Microsoft.Xna.Framework.Graphics.TextureAddressMode.Mirror;
            }
            else if (mode == TextureAddressMode.WRAP)
            {
                return Microsoft.Xna.Framework.Graphics.TextureAddressMode.Wrap;
            }
            else
            {
                throw new NotSupportedException();
            }
        }

        public static Microsoft.Xna.Framework.Graphics.TextureFilter convertTextureFilter(TextureFilter minFilter, TextureFilter magFilter)
        {
            Microsoft.Xna.Framework.Graphics.TextureFilter newFilter;
            if (minFilter == TextureFilter.LINEAR || minFilter == TextureFilter.LINEAR_MIP_POINT)
            {
                if (magFilter == TextureFilter.LINEAR || magFilter == TextureFilter.LINEAR_MIP_POINT)
                {
                    if (minFilter == TextureFilter.LINEAR || magFilter == TextureFilter.LINEAR)
                    {
                        newFilter = Microsoft.Xna.Framework.Graphics.TextureFilter.Linear;
                    }
                    else
                    {
                        newFilter = Microsoft.Xna.Framework.Graphics.TextureFilter.LinearMipPoint;
                    }
                }
                else
                {
                    if (minFilter == TextureFilter.LINEAR || magFilter == TextureFilter.PIXEL)
                    {
                        newFilter = Microsoft.Xna.Framework.Graphics.TextureFilter.MinLinearMagPointMipLinear;
                    }
                    else
                    {
                        newFilter = Microsoft.Xna.Framework.Graphics.TextureFilter.MinLinearMagPointMipPoint;
                    }
                }
            }
            else
            {
                if (magFilter == TextureFilter.LINEAR || magFilter == TextureFilter.LINEAR_MIP_POINT)
                {
                    if (minFilter == TextureFilter.PIXEL || magFilter == TextureFilter.LINEAR)
                    {
                        newFilter = Microsoft.Xna.Framework.Graphics.TextureFilter.MinPointMagLinearMipLinear;
                    }
                    else
                    {
                        newFilter = Microsoft.Xna.Framework.Graphics.TextureFilter.MinPointMagLinearMipPoint;
                    }
                }
                else
                {
                    if (minFilter == TextureFilter.PIXEL || magFilter == TextureFilter.PIXEL)
                    {
                        newFilter = Microsoft.Xna.Framework.Graphics.TextureFilter.PointMipLinear;
                    }
                    else
                    {
                        newFilter = Microsoft.Xna.Framework.Graphics.TextureFilter.Point;
                    }
                }
            }

            return newFilter;
        }

        public static BlendState convertBlending(Mini2DxBlendFunction srcFunction, Mini2DxBlendFunction dstFunction)
        {
            var convertedBlendState = new BlendState();
            
            if (srcFunction == Mini2DxBlendFunction.ZERO)
            {
                convertedBlendState.AlphaSourceBlend = Blend.Zero;
                convertedBlendState.ColorSourceBlend = Blend.Zero;
            }
            else if (srcFunction == Mini2DxBlendFunction.ONE)
            {
                convertedBlendState.AlphaSourceBlend = Blend.One;
                convertedBlendState.ColorSourceBlend = Blend.One;
            }
            else if (srcFunction == Mini2DxBlendFunction.SRC_COLOR)
            {
                convertedBlendState.AlphaSourceBlend = Blend.SourceColor;
                convertedBlendState.ColorSourceBlend = Blend.SourceColor;
            }
            else if (srcFunction == Mini2DxBlendFunction.ONE_MINUS_SRC_COLOR)
            {
                convertedBlendState.AlphaSourceBlend = Blend.InverseSourceColor;
                convertedBlendState.ColorSourceBlend = Blend.InverseSourceColor;
            }
            else if (srcFunction == Mini2DxBlendFunction.DST_COLOR)
            {
                convertedBlendState.AlphaSourceBlend = Blend.DestinationColor;
                convertedBlendState.ColorSourceBlend = Blend.DestinationColor;
            }
            else if (srcFunction == Mini2DxBlendFunction.ONE_MINUS_DST_COLOR)
            {
                convertedBlendState.AlphaSourceBlend = Blend.InverseDestinationColor;
                convertedBlendState.ColorSourceBlend = Blend.InverseDestinationColor;
            }
            else if (srcFunction == Mini2DxBlendFunction.SRC_ALPHA)
            {
                convertedBlendState.AlphaSourceBlend = Blend.SourceAlpha;
                convertedBlendState.ColorSourceBlend = Blend.SourceAlpha;
            }
            else if (srcFunction == Mini2DxBlendFunction.ONE_MINUS_SRC_ALPHA)
            {
                convertedBlendState.AlphaSourceBlend = Blend.InverseSourceAlpha;
                convertedBlendState.ColorSourceBlend = Blend.InverseSourceAlpha;
            }
            else if (srcFunction == Mini2DxBlendFunction.DST_ALPHA)
            {
                convertedBlendState.AlphaSourceBlend = Blend.DestinationAlpha;
                convertedBlendState.ColorSourceBlend = Blend.DestinationAlpha;
            }
            else if (srcFunction == Mini2DxBlendFunction.ONE_MINUS_DST_ALPHA)
            {
                convertedBlendState.AlphaSourceBlend = Blend.InverseDestinationAlpha;
                convertedBlendState.ColorSourceBlend = Blend.InverseDestinationAlpha;
            }
            else if (srcFunction == Mini2DxBlendFunction.SRC_ALPHA_SATURATE)
            {
                convertedBlendState.AlphaSourceBlend = Blend.SourceAlphaSaturation;
                convertedBlendState.ColorSourceBlend = Blend.SourceAlphaSaturation;
            }

            if (dstFunction == Mini2DxBlendFunction.ZERO)
            {
                convertedBlendState.AlphaDestinationBlend = Blend.Zero;
                convertedBlendState.ColorDestinationBlend = Blend.Zero;
            }
            else if (dstFunction == Mini2DxBlendFunction.ONE)
            {
                convertedBlendState.AlphaDestinationBlend = Blend.One;
                convertedBlendState.ColorDestinationBlend = Blend.One;
            }
            else if (dstFunction == Mini2DxBlendFunction.SRC_COLOR)
            {
                convertedBlendState.AlphaDestinationBlend = Blend.SourceColor;
                convertedBlendState.ColorDestinationBlend = Blend.SourceColor;
            }
            else if (dstFunction == Mini2DxBlendFunction.ONE_MINUS_SRC_COLOR)
            {
                convertedBlendState.AlphaDestinationBlend = Blend.InverseSourceColor;
                convertedBlendState.ColorDestinationBlend = Blend.InverseSourceColor;
            }
            else if (dstFunction == Mini2DxBlendFunction.DST_COLOR)
            {
                convertedBlendState.AlphaDestinationBlend = Blend.DestinationColor;
                convertedBlendState.ColorDestinationBlend = Blend.DestinationColor;
            }
            else if (dstFunction == Mini2DxBlendFunction.ONE_MINUS_DST_COLOR)
            {
                convertedBlendState.AlphaDestinationBlend = Blend.InverseDestinationColor;
                convertedBlendState.ColorDestinationBlend = Blend.InverseDestinationColor;
            }
            else if (dstFunction == Mini2DxBlendFunction.SRC_ALPHA)
            {
                convertedBlendState.AlphaDestinationBlend = Blend.SourceAlpha;
                convertedBlendState.ColorDestinationBlend = Blend.SourceAlpha;
            }
            else if (dstFunction == Mini2DxBlendFunction.ONE_MINUS_SRC_ALPHA)
            {
                convertedBlendState.AlphaDestinationBlend = Blend.InverseSourceAlpha;
                convertedBlendState.ColorDestinationBlend = Blend.InverseSourceAlpha;
            }
            else if (dstFunction == Mini2DxBlendFunction.DST_ALPHA)
            {
                convertedBlendState.AlphaDestinationBlend = Blend.DestinationAlpha;
                convertedBlendState.ColorDestinationBlend = Blend.DestinationAlpha;
            }
            else if (dstFunction == Mini2DxBlendFunction.ONE_MINUS_DST_ALPHA)
            {
                convertedBlendState.AlphaDestinationBlend = Blend.InverseDestinationAlpha;
                convertedBlendState.ColorDestinationBlend = Blend.InverseDestinationAlpha;
            }
            else if (dstFunction == Mini2DxBlendFunction.SRC_ALPHA_SATURATE)
            {
                convertedBlendState.AlphaDestinationBlend = Blend.SourceAlphaSaturation;
                convertedBlendState.ColorDestinationBlend = Blend.SourceAlphaSaturation;
            }

            return convertedBlendState;
        }
    }
}