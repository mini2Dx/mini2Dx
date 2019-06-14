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
    class MonoGamePixmap : org.mini2Dx.core.graphics.Pixmap
    {
        private UInt32[,] _pixmap;
        private MonoGameColor _setColor;
        private PixmapBlending _blending;
        private PixmapFilter _filter;
        private readonly PixmapFormat _format;
        private readonly int _width, _height;

        public MonoGamePixmap(int width, int height) : this(width, height, PixmapFormat.RGBA8888){}

        public MonoGamePixmap(int width, int height, PixmapFormat format)
        {
            _width = width;
            _height = height;
            _pixmap = new UInt32[width, height];
            _setColor = new MonoGameColor(0,0, 0, 255);
            _blending = PixmapBlending.NONE;
            _filter = PixmapFilter.NEAREST_NEIGHBOUR;
            _format = format;
        }
        
        public void dispose()
        {
            
        }

        public void drawCircle(int x, int y, int radius)
        {
            if (radius <= 0)
            {
                return;
            }
            var color = _setColor.toRGBA8888();
            for (float angle = 0; angle < 2 * Math.PI; angle += 0.001f)
            {
                drawPixel((int) (radius * Math.Sin(angle) + x), (int) (radius * Math.Cos(angle) + y), color);
            }
        }

        //Implementation of Bresenham's line algorithm: https://stackoverflow.com/a/11683720
        public void drawLine(int x, int y, int x2, int y2) 
        {
            var deltaX = x2 - x;
            var deltaY = y2 - y;
            
            var deltaXSign = Math.Sign(deltaX);
            var deltaYSign = Math.Sign(deltaY);
            
            var dx2 = deltaXSign;
            var dy2 = 0;
            
            var width = Math.Abs(deltaX);
            var height = Math.Abs(deltaY);
            if (width <= height)
            {
                width = Math.Abs(deltaY);
                height = Math.Abs(deltaX);
                dy2 = Math.Sign(deltaY);
                dx2 = 0;
            }
            var numerator = width >> 1 ;
            var color = _setColor.toRGBA8888();
            for (var i=0; i<=width; i++)
            {
                drawPixel(x, y, color);
                numerator += height;
                if (numerator >= width)
                {
                    numerator -= width;
                    x += deltaXSign;
                    y += deltaYSign;
                } 
                else
                {
                    x += dx2;
                    y += dy2;
                }
            }
        }

        public void drawPixel(int x, int y)
        {
            drawPixel(x, y, _setColor.toRGBA8888());
        }

        public void drawPixel(int x, int y, Color color)
        {
            drawPixel(x, y, ((MonoGameColor) color).toRGBA8888());
        }

        public void drawPixel(int x, int y, UInt32 color, bool blend = true)
        {
            var colorAlpha = MonoGameColor.getAAsByte(color);
            if (x < getWidth() && y < getHeight() && x >= 0 && y >= 0 && colorAlpha > 0)
            {
                if (colorAlpha == 255 || !blend)
                {
                    _pixmap[x, y] = color;
                }
                else
                {
                    var newR = (byte)(MonoGameColor.getRAsByte(color) * colorAlpha + MonoGameColor.getAAsByte((uint) getPixel(x, y)) * (255 - colorAlpha));
                    var newG = (byte)(MonoGameColor.getRAsByte(color) * colorAlpha + MonoGameColor.getAAsByte((uint) getPixel(x, y)) * (255 - colorAlpha));
                    var newB = (byte)(MonoGameColor.getRAsByte(color) * colorAlpha + MonoGameColor.getAAsByte((uint) getPixel(x, y)) * (255 - colorAlpha));
                    _pixmap[x, y] = MonoGameColor.toRGBA8888(newR, newG, newB, colorAlpha);
                }
            }
        }

        private void drawPixel(int pixNum, UInt32 color)
        {
            drawPixel(pixNum % getWidth(), pixNum / getWidth(), color);
        }

        public void drawPixmap(Pixmap pixmap, int x, int y)
        {
            drawPixmap(pixmap, x, y, 0, 0, pixmap.getWidth(), pixmap.getHeight());
        }

        public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight)
        {
            for (var pixmapX = 0; pixmapX < srcWidth; pixmapX++)
            {
                for (var pixmapY = 0; pixmapY < srcWidth; pixmapY++)
                {
                    drawPixel(x + pixmapX, y + pixmapY, (uint) pixmap.getPixel(srcX + pixmapX, srcY + pixmapY));
                }
            }
        }
 
        private static float lerp(float s, float e, float t) {
            return s + (e - s) * t;
        }
 
        private static float blerp(float c00, float c10, float c01, float c11, float tx, float ty) {
            return lerp(lerp(c00, c10, tx), lerp(c01, c11, tx), ty);
        }

        public void drawPixmap(Pixmap pixmap, int srcX, int srcY, int srcWidth, int srcHeight, int dstX, int dstY, int dstWidth, int dstHeight)
        {
            if (srcWidth == dstWidth && srcHeight == dstHeight)
            {
                for (var x = 0; x < srcWidth; x++)
                {
                    for (var y = 0; y < srcHeight; y++)
                    {
                        drawPixel(dstX + x, dstY + y, (uint) pixmap.getPixel(srcX + x, srcY + y));
                    }
                }
            }
            else if (_filter == PixmapFilter.NEAREST_NEIGHBOUR)
            {
                var xRatio = (float)srcWidth / dstWidth;
                var yRatio = (float)srcHeight / dstHeight;
                for (var x = dstX; x < dstX + dstWidth; x++)
                {
                    for (var y = dstY; y < dstY + dstWidth; y++)
                    {
                        var srcPixX = (int) Math.Round((x - dstX + srcX) * xRatio, MidpointRounding.AwayFromZero);
                        var srcPixY = (int) Math.Round((y - dstY + srcY) * yRatio, MidpointRounding.AwayFromZero);
                        drawPixel(x, y, (uint) pixmap.getPixel(srcPixX, srcPixY));
                    }
                }
            }
            else
            {
                for (int x = 0; x < dstWidth; ++x)
                {
                    for (int y = 0; y < dstWidth; ++y)
                    {
                        
                        float gx = ((float)x) / dstWidth * (srcWidth - 1);
                        float gy = ((float)y) / dstWidth * (srcWidth - 1);
                        int gxi = (int)gx;
                        int gyi = (int)gy;
                        
                        var c00 = (uint) pixmap.getPixel(gxi, gyi);
                        var c10 = (uint) pixmap.getPixel(gxi + 1, gyi);
                        var c01 = (uint) pixmap.getPixel(gxi, gyi + 1);
                        var c11 = (uint) pixmap.getPixel(gxi + 1, gyi + 1);
 
                        UInt32 red = (byte)blerp(MonoGameColor.getRAsByte(c00), MonoGameColor.getRAsByte(c10), MonoGameColor.getRAsByte(c01), MonoGameColor.getRAsByte(c11), gx - gxi, gy - gyi);
                        UInt32 green = (byte)blerp(MonoGameColor.getGAsByte(c00), MonoGameColor.getGAsByte(c10), MonoGameColor.getGAsByte(c01), MonoGameColor.getGAsByte(c11), gx - gxi, gy - gyi);
                        UInt32 blue = (byte)blerp(MonoGameColor.getBAsByte(c00), MonoGameColor.getBAsByte(c10), MonoGameColor.getBAsByte(c01), MonoGameColor.getBAsByte(c11), gx - gxi, gy - gyi);
                        UInt32 alpha = (byte)blerp(MonoGameColor.getAAsByte(c00), MonoGameColor.getAAsByte(c10), MonoGameColor.getAAsByte(c01), MonoGameColor.getAAsByte(c11), gx - gxi, gy - gyi);
                        var color = ((red << 24) & 0xff000000) |
                                      ((green << 16) & 0xff0000) |
                                      ((blue << 8) & 0xff00) |
                                      alpha;
                        drawPixel(dstX + x, dstY + y, color);
                    }
                }
            }
        }

        public void drawRectangle(int x, int y, int width, int height)
        {
            drawLine(x, y, x + width, y);
            drawLine(x, y, x, y + height);
            drawLine(x + width, y, x + width, y + height);
            drawLine(x, y + height, x + width, y + height);
        }

        public void fill()
        {
            var color = _setColor.toRGBA8888();
            for (int x = 0; x < getWidth(); x++)
            {
                for (int y = 0; y < getHeight(); y++)
                {
                    drawPixel(x, y, color, false);
                }
            }
        }

        public void fillCircle(int x, int y, int radius)
        {
            var color = _setColor.toRGBA8888();
            for (int circleX = x - radius; circleX < x + radius; circleX++)
            {
                
                for (int circleY = y - radius; circleY < y + radius; circleY++)
                {
                    if (Math.Pow(circleX - x, 2) + Math.Pow(circleY - y, 2) <= Math.Pow(radius, 2))
                    {
                        drawPixel(circleX, circleY, color);
                    }
                }
            }
        }

        public void fillRectangle(int x, int y, int width, int height)
        {
            for (var i = 0; i < height; i++)
            {
                drawLine(x, y + i,  x+ width, y + i);
            }
        }

        public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3)
        {
            //sort the vertices by y
            if (y1 > y2)
            {
                //swap x1, x2
                x1 ^= x2;
                x2 ^= x1;
                x1 ^= x2;
                
                //swap y1, y2
                y1 ^= y2;
                y2 ^= y1;
                y1 ^= y2;
            }
            if (y1 > y3)
            {
                //swap x1, x3
                x1 ^= x3;
                x3 ^= x1;
                x1 ^= x3;
                
                //swap y1, y3
                y1 ^= y3;
                y3 ^= y1;
                y1 ^= y3;
            }
            if (y2 > y3)
            {
                //swap x2, x3
                x2 ^= x3;
                x3 ^= x2;
                x2 ^= x3;
                
                //swap y2, y3
                y2 ^= y3;
                y3 ^= y2;
                y2 ^= y3;
            }

            var triangleHeight = y3 - y1;
            var color = _setColor.toRGBA8888();
            for (var i = 0; i < triangleHeight; i++)
            {
                var secondHalf = i > y2 - y1 || y2 == y1;
                var segmentHeight = secondHalf ? y3 - y2 : y2 - y1;
                var alpha = (float) i / triangleHeight;
                var beta = (float) (i - (secondHalf ? y2 - y1 : 0)) / segmentHeight;
                var aX = x1 + (x3 - x1) * alpha;
                var bX = secondHalf ? x2 + (x3 - x2) * beta : x1 + (x2 - x1) * beta;
                if (aX > bX)
                {
                    aX += bX;
                    bX = aX - bX;
                    aX -= bX;
                }

                for (var j = (int) aX; j <= bX; j++)
                {
                    drawPixel(j, y1 + i, color);
                }
            }
        }

        public PixmapBlending getBlending()
        {
            return _blending;
        }

        public PixmapFilter getFilter()
        {
            return _filter;
        }

        public PixmapFormat getFormat()
        {
            return _format;
        }

        public int getHeight()
        {
            return _height;
        }

        public int getPixel(int x, int y)
        {
            if (x >= getWidth() || y >= getHeight() || x < 0 || y < 0)
            {
                return 0;
            }
            return (int) _pixmap[x, y];
        }

        private UInt32 getPixel(int pixNum)
        {
            return (uint) getPixel(pixNum % getWidth(), pixNum / getWidth());
        }

        public UInt32[] toRawPixelsARGB()
        {
            if (_format != PixmapFormat.RGBA8888)
            {
                throw new NotSupportedException();
            }
            
            var rawPixels = new UInt32[getWidth() * getHeight()];
            for (var x = 0; x < getWidth(); x++)
            {
                for (var y = 0; y < getHeight(); y++)
                {
                    rawPixels[y * getWidth() + x] = MonoGameColor.rgbaToArgb(_pixmap[x, y]);
                }
            }

            return rawPixels;
        }

        public byte[] getPixels()
        {
            var numOfPixels = getWidth() * getHeight();
            var bytesPerPixel = getBytesPerPixel(_format);

            var bytes = new byte[numOfPixels * bytesPerPixel];

            for (int currentPixel = 0; currentPixel < numOfPixels; currentPixel++)
            {
                var convertedPixel = MonoGameColor.convertTo(_format, getPixel(currentPixel));
                switch (bytesPerPixel)
                {
                    case 4:
                        bytes[(bytesPerPixel * currentPixel) + bytesPerPixel - 4] = (byte) (convertedPixel >> 24 & 0xff);
                        goto case 3; //because c# doesn't support switch fallthrough
                    case 3:
                        bytes[(bytesPerPixel * currentPixel) + bytesPerPixel - 3] = (byte) (convertedPixel >> 16 & 0xff);
                        goto case 2; //because c# doesn't support switch fallthrough
                    case 2:
                        bytes[(bytesPerPixel * currentPixel) + bytesPerPixel - 2] = (byte) (convertedPixel >> 8 & 0xff);
                        goto case 1; //because c# doesn't support switch fallthrough
                    case 1:
                        bytes[(bytesPerPixel * currentPixel) + bytesPerPixel - 1] = (byte) (convertedPixel & 0xff);
                        break;
                    default:
                        return null;
                }
            }
            
            return bytes;
        }

        public static int getBytesPerPixel(PixmapFormat format)
        {
            var bytesPerPixel = 2;
            if (format == PixmapFormat.INTENSITY)
            {
                bytesPerPixel = 1;
            }
            else if (format == PixmapFormat.RGB888)
            {
                bytesPerPixel = 3;
            }
            else if (format == PixmapFormat.RGBA8888)
            {
                bytesPerPixel = 4;
            }

            return bytesPerPixel;
        }

        public int getWidth()
        {
            return _width;
        }

        public void setBlending(PixmapBlending blending)
        {
            _blending = blending;
        }

        public void setColor(Color color)
        {
            _setColor = (MonoGameColor) color;
        }

        public void setFilter(PixmapFilter filter)
        {
            _filter = filter;
        }

        public void clear()
        {
            _pixmap = new UInt32[_width, _height];
        }
    }
}
