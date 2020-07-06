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
using Org.Mini2Dx.Core.Graphics;

namespace monogame.Graphics
{
    public class MonoGameColor : global::Java.Lang.Object, Color
    {
        internal Microsoft.Xna.Framework.Color _color;

        public MonoGameColor(sbyte r, sbyte g, sbyte b, sbyte a)
        {
            _color = new Microsoft.Xna.Framework.Color(r, g, b, a);
        }

        public MonoGameColor(float r, float g, float b, float a)
        {
            _color = new Microsoft.Xna.Framework.Color(r, g, b, a);
        }

        public MonoGameColor(int r, int g, int b, int a)
        {
            _color = new Microsoft.Xna.Framework.Color(r, g, b, a);
        }

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
            return new MonoGameColor(new Microsoft.Xna.Framework.Color(_color.PackedValue));
        }

        public virtual Color set(Color c)
        {
            _color.R = (byte) c.getRAsByte();
            _color.G = (byte) c.getGAsByte();
            _color.B = (byte) c.getBAsByte();
            _color.A = (byte) c.getAAsByte();
            return this;
        }

        public virtual Color set(float r, float g, float b, float a)
        {
            _color.R = floatToByte(r);
            _color.G = floatToByte(g);
            _color.B = floatToByte(b);
            _color.A = floatToByte(a);
            return this;
        }

        public virtual Color set(sbyte r, sbyte g, sbyte b, sbyte a)
        {
            _color.R = (byte) r;
            _color.G = (byte) g;
            _color.B = (byte) b;
            _color.A = (byte) a;
            return this;
        }

        public virtual Color add(Color c)
        {
            _color.R += (byte) c.getRAsByte();
            _color.G += (byte) c.getGAsByte();
            _color.B += (byte) c.getBAsByte();
            _color.A += (byte) c.getAAsByte();
            return this;
        }

        public virtual Color add(float r, float g, float b, float a)
        {
            _color.R += floatToByte(r);
            _color.G += floatToByte(g);
            _color.B += floatToByte(b);
            _color.A += floatToByte(a);
            return this;
        }

        public virtual Color add(sbyte r, sbyte g, sbyte b, sbyte a)
        {
            _color.R += (byte) r;
            _color.G += (byte) g;
            _color.B += (byte) b;
            _color.A += (byte) a;
            return this;
        }

        public virtual Color multiply(Color c)
        {
            _color.R = (byte) (_color.R * c.getRAsFloat());
            _color.G = (byte) (_color.G * c.getGAsFloat());
            _color.B = (byte) (_color.B * c.getBAsFloat());
            _color.A = (byte) (_color.A * c.getAAsFloat());
            return this;
        }

        public virtual Color multiply(float r, float g, float b, float a)
        {
            _color.R = (byte) (_color.R * r);
            _color.G = (byte) (_color.G * g);
            _color.B = (byte) (_color.B * b);
            _color.A = (byte) (_color.A * a);
            return this;
        }

        public virtual Color multiply(sbyte r, sbyte g, sbyte b, sbyte a)
        {
            _color.R = (byte) (_color.R * sbyteToFloat(r));
            _color.G = (byte) (_color.G * sbyteToFloat(g));
            _color.B = (byte) (_color.B * sbyteToFloat(b));
            _color.A = (byte) (_color.A * sbyteToFloat(a) );
            return this;
        }

        public virtual Color multiply(float multiplier)
        {
            _color.R = (byte) (_color.R * multiplier);
            _color.G = (byte) (_color.G * multiplier);
            _color.B = (byte) (_color.B * multiplier);
            _color.A = (byte) (_color.A * multiplier);
            return this;
        }

        public virtual Color subtract(Color c)
        {
            _color.R -= (byte) c.getRAsByte();
            _color.G -= (byte) c.getGAsByte();
            _color.B -= (byte) c.getBAsByte();
            _color.A -= (byte) c.getAAsByte();
            return this;
        }

        public virtual Color subtract(float r, float g, float b, float a)
        {
            _color.R -= floatToByte(r);
            _color.G -= floatToByte(g);
            _color.B -= floatToByte(b);
            _color.A -= floatToByte(a);
            return this;
        }

        public virtual Color subtract(sbyte r, sbyte g, sbyte b, sbyte a)
        {
            _color.R -= (byte) r;
            _color.G -= (byte) g;
            _color.B -= (byte) b;
            _color.A -= (byte) a;
            return this;
        }

        public virtual Color lerp(Color color, float t)
        {
            _color.R = (byte) ((color.getRAsByte() - _color.R) * t + _color.R);
            _color.G = (byte) ((color.getGAsByte() - _color.G) * t + _color.G);
            _color.B = (byte) ((color.getBAsByte() - _color.B) * t + _color.B);
            _color.A = (byte) ((color.getAAsByte() - _color.A) * t + _color.A);
            return this;
        }

        public virtual Color lerp(float r, float g, float b, float a, float t)
        {
            _color.R = (byte) ((floatTosbyte(r) - _color.R) * t + _color.R);
            _color.G = (byte) ((floatTosbyte(g) - _color.G) * t + _color.G);
            _color.B = (byte) ((floatTosbyte(b) - _color.B) * t + _color.B);
            _color.A = (byte) ((floatTosbyte(a) - _color.A) * t + _color.A);
            return this;
        }

        public virtual Color lerp(sbyte r, sbyte g, sbyte b, sbyte a, float t)
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

        public sbyte getRAsByte()
        {
            return (sbyte) _color.R;
        }

        public static sbyte getRAsByte(UInt32 rgba8888)
        {
            return (sbyte) (rgba8888 >> 24);
        }

        public sbyte getGAsByte()
        {
            return (sbyte) _color.G;
        }

        public static sbyte getGAsByte(UInt32 rgba8888)
        {
            return (sbyte) (rgba8888 >> 16 & 0xff);
        }

        public sbyte getBAsByte()
        {
            return (sbyte) _color.B;
        }

        public static sbyte getBAsByte(UInt32 rgba8888)
        {
            return (sbyte) (rgba8888 >> 8 & 0xff);
        }

        public sbyte getAAsByte()
        {
            return (sbyte) _color.A;
        }

        public static sbyte getAAsByte(UInt32 rgba8888)
        {
            return (sbyte) (rgba8888 & 0xff);
        }

        public static Microsoft.Xna.Framework.Color toMonoGameColor(UInt32 rgba8888)
        {
            return new Microsoft.Xna.Framework.Color(getRAsByte(rgba8888), getGAsByte(rgba8888), getBAsByte(rgba8888), getAAsByte(rgba8888));
        }

        private static byte floatToByte(float f)
        {
            return (byte)(f * 255);
        }

        private static sbyte floatTosbyte(float f)
        {
            return (sbyte) (byte) (f * 255);
        }

        private static float sbyteToFloat(sbyte b)
        {
            return ((byte) b) / 255f;
        }

        private static float byteToFloat(byte b)
        {
            return b / 255f;
        }

        public UInt32 toRGB888()
        {
            byte r = (byte)getRAsByte();
            byte g = (byte)getGAsByte();
            byte b = (byte)getBAsByte();
            return (UInt32) (r << 16 | g << 8 | b);
        }

        public static UInt32 toRGB888(UInt32 rgba8888)
        {
            return rgba8888 >> 8;
        }

        public UInt32 toRGBA8888()
        {
            return toRGB888() << 8 | (byte) getAAsByte();
        }

        public static UInt32 toRGBA8888(sbyte r, sbyte g, sbyte b, sbyte a)
        {
            byte r1 = (byte)r;
            byte g1 = (byte)g;
            byte b1 = (byte)b;
            byte a1 = (byte)a;
            return (uint) (r1 << 24 | g1 << 16 | b1 << 8 | a1);
        }

        public static UInt32 toARGB8888(sbyte r, sbyte g, sbyte b, sbyte a)
        {
            byte r1 = (byte)r;
            byte g1 = (byte)g;
            byte b1 = (byte)b;
            byte a1 = (byte)a;
            return (uint) (a1 << 24 | r1 << 16 | g1 << 8 | b1);
        }

        public static UInt32 toRGBA8888(Microsoft.Xna.Framework.Color color)
        {
            return (uint) (color.R << 24 | color.G << 16 | color.B << 8 | color.A);
        }

        public static UInt16 toLuminanceAlpha(UInt32 rgba8888)
        {
            //luminance is defined as the average of the largest and the smallest color components
            var luminance = Math.Max(Math.Max(getRAsByte(rgba8888), getGAsByte(rgba8888)), getBAsByte(rgba8888));
            byte a = (byte)getAAsByte(rgba8888);
            return (UInt16) (luminance << 8 | a);
        }

        public static UInt32 rgbaToArgb(UInt32 rgba8888)
        {
            var alpha = rgba8888 & 0xff;
            return (rgba8888 >> 8) | (alpha << 24);
        }

        public static UInt32 argbToRgba(UInt32 argb8888)
        {
            var alpha = (argb8888 & 0xff000000) >> 24;
            return (argb8888 << 8) | alpha;
        }

        public UInt16 toLuminanceAlpha()
        {
            return toLuminanceAlpha(toRGBA8888());
        }

        public static UInt16 toRGB565(UInt32 rgba8888)
        {
            return (UInt16) (((getRAsByte(rgba8888) & 0xf8) << 8) | ((getGAsByte(rgba8888) & 0xfc) << 3) | ((getBAsByte(rgba8888) & 0xf8) >> 3));
        }

        public UInt16 toRGB565()
        {
            return toRGB565(toRGBA8888());
        }

        public static UInt16 toRGBA4444(UInt32 rgba8888)
        {
            return (UInt16)(((getRAsByte(rgba8888) & 0xf0) << 8) | ((getGAsByte(rgba8888) & 0xf0) << 4) | (getBAsByte(rgba8888) & 0xf0) | ((getAAsByte(rgba8888) & 0xf0) >> 4));
        }

        public UInt16 toRGBA4444()
        {
            return toRGBA4444(toRGBA8888());
        }

        public static sbyte toIntensity(UInt32 rgba8888)
        {
            return (sbyte) (0.21 * getRAsByte(rgba8888) + 0.72 * getGAsByte(rgba8888) + 0.07 * getBAsByte(rgba8888));
        }

        public static UInt32 convertTo(PixmapFormat format, UInt32 colorRGBA8888)
        {
            if (format == PixmapFormat.RGBA8888_)
            {
                return colorRGBA8888;
            }
            else if (format == PixmapFormat.RGB565_)
            {
                return toRGB565(colorRGBA8888);
            }
            else if (format == PixmapFormat.RGB888_)
            {
                return toRGB888(colorRGBA8888);
            }
            else if (format == PixmapFormat.RGBA4444_)
            {
                return toRGBA4444(colorRGBA8888);
            }
            else if (format == PixmapFormat.ALPHA_)
            {
                return (byte) getAAsByte(colorRGBA8888);
            }
            else if (format == PixmapFormat.INTENSITY_)
            {
                return (byte) toIntensity(colorRGBA8888);
            }
            else if (format == PixmapFormat.LUMINANCE_ALPHA_)
            {
                return toLuminanceAlpha(colorRGBA8888);
            }

            return 0;
        }

        public sbyte toIntensity()
        {
            return toIntensity(toRGBA8888());
        }

        public static uint toArgb(Color color)
        {
            return toARGB8888(color.getRAsByte(), color.getGAsByte(), color.getBAsByte(), color.getAAsByte());
        }

        public bool equals(Color c)
        {
            if(c is MonoGameColor)
            {
                return _color.Equals((c as MonoGameColor)._color);
            }
            if(getRAsByte() != c.getRAsByte())
            {
                return false;
            }
            if (getGAsByte() != c.getGAsByte())
            {
                return false;
            }
            if (getBAsByte() != c.getBAsByte())
            {
                return false;
            }
            if (getAAsByte() != c.getAAsByte())
            {
                return false;
            }
            return true;
        }

        public virtual void setA(float f)
        {
            _color.A = (byte) (Math.Min(1, Math.Max(0, f)) * 255);
        }

        public virtual void setR(float f)
        {
            _color.R = (byte)(Math.Min(1, Math.Max(0, f)) * 255);
        }

        public virtual void setG(float f)
        {
            _color.G = (byte)(Math.Min(1, Math.Max(0, f)) * 255);
        }

        public virtual void setB(float f)
        {
            _color.B = (byte)(Math.Min(1, Math.Max(0, f)) * 255);
        }

        public virtual void setR(sbyte b)
        {
            _color.R = (byte)b;
        }

        public virtual void setG(sbyte b)
        {
            _color.G = (byte)b;
        }

        public virtual void setB(sbyte b)
        {
            _color.B = (byte)b;
        }

        public virtual void setA(sbyte b)
        {
            _color.A = (byte)b;
        }

        public float rf()
        {
            return getRAsFloat();
        }

        public float gf()
        {
            return getGAsFloat();
        }

        public float bf()
        {
            return getBAsFloat();
        }

        public float af()
        {
            return getAAsFloat();
        }

        public sbyte rb()
        {
            return getRAsByte();
        }

        public sbyte gb()
        {
            return getGAsByte();
        }

        public sbyte bb()
        {
            return getBAsByte();
        }

        public sbyte ab()
        {
            return getAAsByte();
        }

        public int argb8888()
        {
            return ((_color.A << 24) | (_color.R << 16) | (_color.G << 8) | _color.B);
        }

        public int rgba8888()
        {
            return ((_color.R << 24) | (_color.G << 16) | (_color.B << 8) | _color.A);
        }

        public int rgba4444()
        {
            return ((int)(getRAsFloat() * 15) << 12) | ((int)(getGAsFloat() * 15) << 8) | ((int)(getBAsFloat() * 15) << 4) | (int)(getAAsFloat() * 15);
        }

        public int rgb888()
        {
            return ((_color.R << 16) | (_color.G << 8) | _color.B);
        }

        public int rgb565()
        {
            return ((int)(getRAsFloat() * 31) << 11) | ((int)(getGAsFloat() * 63) << 5) | (int)(getBAsFloat() * 31);
        }

        public int bgr565()
        {
            return ((int)(getBAsFloat() * 31) << 11) | ((int)(getGAsFloat() * 63) << 5) | (int)(getRAsFloat() * 31);
        }

        public int bgra4444()
        {
            return ((int)(getBAsFloat() * 15) << 12) | ((int)(getGAsFloat() * 15) << 8) | ((int)(getRAsFloat() * 15) << 4) | (int)(getAAsFloat() * 15);
        }
    }
}