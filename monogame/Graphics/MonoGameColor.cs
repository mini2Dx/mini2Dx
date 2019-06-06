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
using org.mini2Dx.core.graphics;

namespace monogame.Graphics
{
    public class MonoGameColor : org.mini2Dx.core.graphics.Color
    {
        private Microsoft.Xna.Framework.Color _color;

        public MonoGameColor(byte r, byte g, byte b, byte a)
        {
            _color = new Microsoft.Xna.Framework.Color(r, g, b, a);
        }

        public MonoGameColor(float r, float g, float b, float a)
        {
            _color = new Microsoft.Xna.Framework.Color(r, g, b, a);
        }

        public MonoGameColor(int r, int g, int b, int a) : this((byte) r, (byte) g, (byte) b, (byte) a) {}

        public MonoGameColor(UInt32 rgba8888)
        {
            _color = new Microsoft.Xna.Framework.Color(rgba8888);
        }

        public MonoGameColor(Microsoft.Xna.Framework.Color color)
        {
            _color = color;
        }
        
        public Color copy()
        {
            return new MonoGameColor(_color.PackedValue);
        }

        public Color set(Color c)
        {
            _color.R = c.getRAsByte();
            _color.G = c.getGAsByte();
            _color.B = c.getBAsByte();
            _color.A = c.getAAsByte();
            return this;
        }

        public Color set(float r, float g, float b, float a)
        {
            _color.R = floatToByte(r);
            _color.G = floatToByte(g);
            _color.B = floatToByte(b);
            _color.A = floatToByte(a);
            return this;
        }

        public Color set(byte r, byte g, byte b, byte a)
        {
            _color.R = r;
            _color.G = g;
            _color.B = b;
            _color.A = a;
            return this;
        }

        public Color add(Color c)
        {
            _color.R += c.getRAsByte();
            _color.G += c.getGAsByte();
            _color.B += c.getBAsByte();
            _color.A += c.getAAsByte();
            return this;
        }

        public Color add(float r, float g, float b, float a)
        {
            _color.R += floatToByte(r);
            _color.G += floatToByte(g);
            _color.B += floatToByte(b);
            _color.A += floatToByte(a);
            return this;
        }

        public Color add(byte r, byte g, byte b, byte a)
        {
            _color.R += r;
            _color.G += g;
            _color.B += b;
            _color.A += a;
            return this;
        }

        public Color multiply(Color c)
        {
            _color.R *= c.getRAsByte();
            _color.G *= c.getGAsByte();
            _color.B *= c.getBAsByte();
            _color.A *= c.getAAsByte();
            return this;
        }

        public Color multiply(float r, float g, float b, float a)
        {
            _color.R *= floatToByte(r);
            _color.G *= floatToByte(g);
            _color.B *= floatToByte(b);
            _color.A *= floatToByte(a);
            return this;
        }

        public Color multiply(byte r, byte g, byte b, byte a)
        {
            _color.R *= r;
            _color.G *= g;
            _color.B *= b;
            _color.A *= a;
            return this;
        }

        public Color multiply(float multiplier)
        {
            _color.R = (byte) (_color.R * multiplier);
            _color.G = (byte) (_color.G * multiplier);
            _color.B = (byte) (_color.B * multiplier);
            _color.A = (byte) (_color.A * multiplier);
            return this;
        }

        public Color subtract(Color c)
        {
            _color.R -= c.getRAsByte();
            _color.G -= c.getGAsByte();
            _color.B -= c.getBAsByte();
            _color.A -= c.getAAsByte();
            return this;
        }

        public Color subtract(float r, float g, float b, float a)
        {
            _color.R -= floatToByte(r);
            _color.G -= floatToByte(g);
            _color.B -= floatToByte(b);
            _color.A -= floatToByte(a);
            return this;
        }

        public Color subtract(byte r, byte g, byte b, byte a)
        {
            _color.R -= r;
            _color.G -= g;
            _color.B -= b;
            _color.A -= a;
            return this;
        }

        public Color lerp(Color color, float t)
        {
            _color.R = (byte) ((color.getRAsByte() - _color.R) * t + _color.R);
            _color.G = (byte) ((color.getGAsByte() - _color.G) * t + _color.G);
            _color.B = (byte) ((color.getBAsByte() - _color.B) * t + _color.B);
            _color.A = (byte) ((color.getAAsByte() - _color.A) * t + _color.A);
            return this;
        }

        public Color lerp(float r, float g, float b, float a, float t)
        {
            _color.R = (byte) ((floatToByte(r) - _color.R) * t + _color.R);
            _color.G = (byte) ((floatToByte(g) - _color.G) * t + _color.G);
            _color.B = (byte) ((floatToByte(b) - _color.B) * t + _color.B);
            _color.A = (byte) ((floatToByte(a) - _color.A) * t + _color.A);
            return this;
        }

        public Color lerp(byte r, byte g, byte b, byte a, float t)
        {
            _color.R = (byte) ((r - _color.R) * t + _color.R);
            _color.G = (byte) ((g - _color.G) * t + _color.G);
            _color.B = (byte) ((b - _color.B) * t + _color.B);
            _color.A = (byte) ((a - _color.A) * t + _color.A);
            return this;
        }

        public float getRAsFloat()
        {
            return byteToFloat(_color.R);
        }

        public float getGAsFloat()
        {
            return byteToFloat(_color.G);
        }

        public float getBAsFloat()
        {
            return byteToFloat(_color.B);
        }

        public float getAAsFloat()
        {
            return byteToFloat(_color.A);
        }

        public byte getRAsByte()
        {
            return _color.R;
        }

        public static byte getRAsByte(UInt32 rgba8888)
        {
            return (byte) (rgba8888 << 24);
        }

        public byte getGAsByte()
        {
            return _color.G;
        }

        public static byte getGAsByte(UInt32 rgba8888)
        {
            return (byte) (rgba8888 << 16 & 0xff);
        }

        public byte getBAsByte()
        {
            return _color.B;
        }

        public static byte getBAsByte(UInt32 rgba8888)
        {
            return (byte) (rgba8888 << 8 & 0xff);
        }

        public byte getAAsByte()
        {
            return _color.A;
        }

        public static byte getAAsByte(UInt32 rgba8888)
        {
            return (byte) (rgba8888 & 0xff);
        }

        public Microsoft.Xna.Framework.Color toMonoGameColor()
        {
            return new Microsoft.Xna.Framework.Color(_color.PackedValue);
        }

        private static byte floatToByte(float f)
        {
            return (byte) (f * 255);
        }

        private static float byteToFloat(byte b)
        {
            return b / 255f;
        }

        public UInt32 toRGB888()
        {
            return (UInt32) (getRAsByte() >> 16 | getGAsByte() >> 8 | getBAsByte());
        }

        public static UInt32 toRGB888(UInt32 rgba8888)
        {
            return rgba8888 << 8;
        }

        public UInt32 toRGBA8888()
        {
            return toRGB888() >> 8 | getAAsByte();
        }

        public static UInt16 toLuminanceAlpha(UInt32 rgba8888)
        {
            //luminance is defined as the average of the largest and the smallest color components
            var luminance = Math.Max(Math.Max(getRAsByte(rgba8888), getGAsByte(rgba8888)), getBAsByte(rgba8888));
            return (UInt16) (luminance >> 8 | getAAsByte(rgba8888));
        }

        public UInt16 toLuminanceAlpha()
        {
            return toLuminanceAlpha(toRGBA8888());
        }

        public static UInt16 toRGB565(UInt32 rgba8888)
        {
            return (UInt16) (((getRAsByte(rgba8888) & 0xf8) >> 8) | ((getGAsByte(rgba8888) & 0xfc) >> 3) | ((getBAsByte(rgba8888) & 0xf8) << 3));
        }

        public UInt16 toRGB565()
        {
            return toRGB565(toRGBA8888());
        }

        public static UInt16 toRGBA4444(UInt32 rgba8888)
        {
            return (UInt16)(((getRAsByte(rgba8888) & 0xf0) >> 8) | ((getGAsByte(rgba8888) & 0xf0) >> 4) | (getBAsByte(rgba8888) & 0xf0) | ((getAAsByte(rgba8888) & 0xf0) << 4));
        }

        public UInt16 toRGBA4444()
        {
            return toRGBA4444(toRGBA8888());
        }

        public static byte toIntensity(UInt32 rgba8888)
        {
            return (byte) (0.21 * getRAsByte(rgba8888) + 0.72 * getGAsByte(rgba8888) + 0.07 * getBAsByte(rgba8888));
        }

        public static UInt32 convertTo(PixmapFormat format, UInt32 colorRGBA8888)
        {
            if (format == PixmapFormat.RGBA8888)
            {
                return colorRGBA8888;
            }
            else if (format == PixmapFormat.RGB565)
            {
                return toRGB565(colorRGBA8888);
            }
            else if (format == PixmapFormat.RGB888)
            {
                return toRGB888(colorRGBA8888);
            }
            else if (format == PixmapFormat.RGBA4444)
            {
                return toRGBA4444(colorRGBA8888);
            }
            else if (format == PixmapFormat.ALPHA)
            {
                return getAAsByte(colorRGBA8888);
            }
            else if (format == PixmapFormat.INTENSITY)
            {
                return toIntensity(colorRGBA8888);
            }
            else if (format == PixmapFormat.LUMINANCE_ALPHA)
            {
                return toLuminanceAlpha(colorRGBA8888);
            }

            return 0;
        }

        public byte toIntensity()
        {
            return toIntensity(toRGBA8888());
        }
    }
}