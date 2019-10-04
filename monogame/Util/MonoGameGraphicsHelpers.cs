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
            if (mode == TextureAddressMode.CLAMP_)
            {
                return Microsoft.Xna.Framework.Graphics.TextureAddressMode.Clamp;
            }
            else if (mode == TextureAddressMode.MIRROR_)
            {
                return Microsoft.Xna.Framework.Graphics.TextureAddressMode.Mirror;
            }
            else if (mode == TextureAddressMode.WRAP_)
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
            if (minFilter == TextureFilter.LINEAR_ || minFilter == TextureFilter.LINEAR_MIP_POINT_)
            {
                if (magFilter == TextureFilter.LINEAR_ || magFilter == TextureFilter.LINEAR_MIP_POINT_)
                {
                    if (minFilter == TextureFilter.LINEAR_ || magFilter == TextureFilter.LINEAR_)
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
                    if (minFilter == TextureFilter.LINEAR_ || magFilter == TextureFilter.PIXEL_)
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
                if (magFilter == TextureFilter.LINEAR_ || magFilter == TextureFilter.LINEAR_MIP_POINT_)
                {
                    if (minFilter == TextureFilter.PIXEL_ || magFilter == TextureFilter.LINEAR_)
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
                    if (minFilter == TextureFilter.PIXEL_ || magFilter == TextureFilter.PIXEL_)
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
            
            if (srcFunction == Mini2DxBlendFunction.ZERO_)
            {
                convertedBlendState.AlphaSourceBlend = Blend.Zero;
                convertedBlendState.ColorSourceBlend = Blend.Zero;
            }
            else if (srcFunction == Mini2DxBlendFunction.ONE_)
            {
                convertedBlendState.AlphaSourceBlend = Blend.One;
                convertedBlendState.ColorSourceBlend = Blend.One;
            }
            else if (srcFunction == Mini2DxBlendFunction.SRC_COLOR_)
            {
                convertedBlendState.AlphaSourceBlend = Blend.SourceColor;
                convertedBlendState.ColorSourceBlend = Blend.SourceColor;
            }
            else if (srcFunction == Mini2DxBlendFunction.ONE_MINUS_SRC_COLOR_)
            {
                convertedBlendState.AlphaSourceBlend = Blend.InverseSourceColor;
                convertedBlendState.ColorSourceBlend = Blend.InverseSourceColor;
            }
            else if (srcFunction == Mini2DxBlendFunction.DST_COLOR_)
            {
                convertedBlendState.AlphaSourceBlend = Blend.DestinationColor;
                convertedBlendState.ColorSourceBlend = Blend.DestinationColor;
            }
            else if (srcFunction == Mini2DxBlendFunction.ONE_MINUS_DST_COLOR_)
            {
                convertedBlendState.AlphaSourceBlend = Blend.InverseDestinationColor;
                convertedBlendState.ColorSourceBlend = Blend.InverseDestinationColor;
            }
            else if (srcFunction == Mini2DxBlendFunction.SRC_ALPHA_)
            {
                convertedBlendState.AlphaSourceBlend = Blend.SourceAlpha;
                convertedBlendState.ColorSourceBlend = Blend.SourceAlpha;
            }
            else if (srcFunction == Mini2DxBlendFunction.ONE_MINUS_SRC_ALPHA_)
            {
                convertedBlendState.AlphaSourceBlend = Blend.InverseSourceAlpha;
                convertedBlendState.ColorSourceBlend = Blend.InverseSourceAlpha;
            }
            else if (srcFunction == Mini2DxBlendFunction.DST_ALPHA_)
            {
                convertedBlendState.AlphaSourceBlend = Blend.DestinationAlpha;
                convertedBlendState.ColorSourceBlend = Blend.DestinationAlpha;
            }
            else if (srcFunction == Mini2DxBlendFunction.ONE_MINUS_DST_ALPHA_)
            {
                convertedBlendState.AlphaSourceBlend = Blend.InverseDestinationAlpha;
                convertedBlendState.ColorSourceBlend = Blend.InverseDestinationAlpha;
            }
            else if (srcFunction == Mini2DxBlendFunction.SRC_ALPHA_SATURATE_)
            {
                convertedBlendState.AlphaSourceBlend = Blend.SourceAlphaSaturation;
                convertedBlendState.ColorSourceBlend = Blend.SourceAlphaSaturation;
            }

            if (dstFunction == Mini2DxBlendFunction.ZERO_)
            {
                convertedBlendState.AlphaDestinationBlend = Blend.Zero;
                convertedBlendState.ColorDestinationBlend = Blend.Zero;
            }
            else if (dstFunction == Mini2DxBlendFunction.ONE_)
            {
                convertedBlendState.AlphaDestinationBlend = Blend.One;
                convertedBlendState.ColorDestinationBlend = Blend.One;
            }
            else if (dstFunction == Mini2DxBlendFunction.SRC_COLOR_)
            {
                convertedBlendState.AlphaDestinationBlend = Blend.SourceColor;
                convertedBlendState.ColorDestinationBlend = Blend.SourceColor;
            }
            else if (dstFunction == Mini2DxBlendFunction.ONE_MINUS_SRC_COLOR_)
            {
                convertedBlendState.AlphaDestinationBlend = Blend.InverseSourceColor;
                convertedBlendState.ColorDestinationBlend = Blend.InverseSourceColor;
            }
            else if (dstFunction == Mini2DxBlendFunction.DST_COLOR_)
            {
                convertedBlendState.AlphaDestinationBlend = Blend.DestinationColor;
                convertedBlendState.ColorDestinationBlend = Blend.DestinationColor;
            }
            else if (dstFunction == Mini2DxBlendFunction.ONE_MINUS_DST_COLOR_)
            {
                convertedBlendState.AlphaDestinationBlend = Blend.InverseDestinationColor;
                convertedBlendState.ColorDestinationBlend = Blend.InverseDestinationColor;
            }
            else if (dstFunction == Mini2DxBlendFunction.SRC_ALPHA_)
            {
                convertedBlendState.AlphaDestinationBlend = Blend.SourceAlpha;
                convertedBlendState.ColorDestinationBlend = Blend.SourceAlpha;
            }
            else if (dstFunction == Mini2DxBlendFunction.ONE_MINUS_SRC_ALPHA_)
            {
                convertedBlendState.AlphaDestinationBlend = Blend.InverseSourceAlpha;
                convertedBlendState.ColorDestinationBlend = Blend.InverseSourceAlpha;
            }
            else if (dstFunction == Mini2DxBlendFunction.DST_ALPHA_)
            {
                convertedBlendState.AlphaDestinationBlend = Blend.DestinationAlpha;
                convertedBlendState.ColorDestinationBlend = Blend.DestinationAlpha;
            }
            else if (dstFunction == Mini2DxBlendFunction.ONE_MINUS_DST_ALPHA_)
            {
                convertedBlendState.AlphaDestinationBlend = Blend.InverseDestinationAlpha;
                convertedBlendState.ColorDestinationBlend = Blend.InverseDestinationAlpha;
            }
            else if (dstFunction == Mini2DxBlendFunction.SRC_ALPHA_SATURATE_)
            {
                convertedBlendState.AlphaDestinationBlend = Blend.SourceAlphaSaturation;
                convertedBlendState.ColorDestinationBlend = Blend.SourceAlphaSaturation;
            }

            return convertedBlendState;
        }
    }
}