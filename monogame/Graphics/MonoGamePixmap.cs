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
        private readonly UInt32[,] _pixmap;
        private MonoGameColor _setColor;
        private PixmapBlending _blending;
        private PixmapFilter _filter;
        private readonly PixmapFormat _format;

        public MonoGamePixmap(int width, int height) : this(width, height, PixmapFormat.RGBA8888){}

        public MonoGamePixmap(int width, int height, PixmapFormat format)
        {
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
            for (float angle = 0; angle < 2 * Math.PI; angle += 0.05f)
            {
                drawPixel((int) (radius * Math.Sin(angle) + x), (int) (radius * Math.Cos(angle) + y));
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
            for (var i=0; i<=width; i++)
            {
                drawPixel(x, y);
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
            _pixmap[x, y] = _setColor.toRGBA8888();
        }

        public void drawPixel(int x, int y, Color color)
        {
            _pixmap[x, y] = ((MonoGameColor) color).toRGBA8888();
        }

        private void drawPixel(int x, int y, UInt32 color)
        {
            _pixmap[x, y] = color;
        }

        private void drawPixel(int pixNum, UInt32 color)
        {
            _pixmap[pixNum % getWidth(), pixNum / getWidth()] = color;
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

        public void drawPixmap(Pixmap pixmap, int srcX, int srcY, int srcWidth, int srcHeight, int dstX, int dstY, int dstWidth, int dstHeight)
        {

            if (_filter == PixmapFilter.NEAREST_NEIGHBOUR)
            {
                var xRatio = (float)srcWidth / dstWidth;
                var yRatio = (float)srcHeight / dstHeight;
                for (var x = dstX; x < dstX + dstWidth; x++)
                {
                    for (var y = dstY; y < dstY + dstWidth; y++)
                    {
                        var srcPixX = (int) ((x - dstX + srcX) * xRatio - 0.5f);
                        var srcPixY = (int) ((y - dstY + srcY) * yRatio - 0.5f);
                        drawPixel(x, y, (uint) pixmap.getPixel(srcPixX, srcPixY));
                    }
                }
            }
            else
            {
                var xRatio = (float)(srcWidth - 1) / dstWidth;
                var yRatio = (float)(srcHeight - 1) / dstHeight;
                var offset = 0;
                for (var i = 0; i < dstHeight; i++)
                {
                    for (var j = 0; j < dstWidth; j++)
                    {
                        var xDiff = xRatio * (j - dstX + srcX);
                        var yDiff = yRatio * (i - dstY + srcY);
                        var x = (int) xDiff;
                        var y = (int) yDiff;
                        xDiff -= x;
                        yDiff -= y;
                        UInt32 a, b, c, d;
                        a = (uint) pixmap.getPixel(x, y);
                        b = (uint) pixmap.getPixel(x + 1, y);
                        c = (uint) pixmap.getPixel(x, y + 1);
                        d = (uint) pixmap.getPixel(x + 1, y + 1);

                        var alpha = (uint) ((a&0xff)*(1-xDiff)*(1-yDiff) + (b&0xff)*(xDiff)*(1-yDiff) +
                                             (c&0xff)*(yDiff)*(1-xDiff)   + (d&0xff)*(xDiff*yDiff));

                        var blue = (uint) (((a>>8)&0xff)*(1-xDiff)*(1-yDiff) + ((b>>8)&0xff)*(xDiff)*(1-yDiff) +
                                            ((c>>8)&0xff)*(yDiff)*(1-xDiff)   + ((d>>8)&0xff)*(xDiff*yDiff));

                        var green = (uint) (((a>>16)&0xff)*(1-xDiff)*(1-yDiff) + ((b>>16)&0xff)*(xDiff)*(1-yDiff) +
                                             ((c>>16)&0xff)*(yDiff)*(1-xDiff)   + ((d>>16)&0xff)*(xDiff*yDiff));

                        var red = (uint) (((a>>24)&0xff)*(1-xDiff)*(1-yDiff) + ((b>>24)&0xff)*(xDiff)*(1-yDiff) +
                                           ((c>>24)&0xff)*(yDiff)*(1-xDiff)   + ((d>>24)&0xff)*(xDiff*yDiff));
                        
                        drawPixel(offset++, ((red<<24)&0xff000000) |
                                            ((green<<16)&0xff0000) |
                                            ((blue<<8)&0xff00) |
                                            alpha);
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
            for (int x = 0; x < getWidth(); x++)
            {
                for (int y = 0; y < getHeight(); y++)
                {
                    drawPixel(x, y);
                }
            }
        }

        public void fillCircle(int x, int y, int radius)
        {
            for (int circleX = x - radius; circleX < x + radius; circleX++)
            {
                
                for (int circleY = y - radius; circleY < y + radius; circleY++)
                {
                    if (Math.Pow(circleX - x, 2) + Math.Pow(circleY - y, 2) <= Math.Pow(radius, 2))
                    {
                        drawPixel(circleX, circleY);
                    }
                }
            }
        }

        public void fillRectangle(int x, int y, int width, int height)
        {
            for (var i = 0; i < height; i++)
            {
                drawLine(x, y + i, width, y + i);
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
                    drawPixel(j, y1 + i);
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
            return _pixmap.GetLength(1);
        }

        public int getPixel(int x, int y)
        {
            return (int) _pixmap[x, y];
        }

        private UInt32 getPixel(int pixNum)
        {
            return _pixmap[pixNum % getWidth(), pixNum / getWidth()];
        }

        public byte[] getPixels()
        {
            var numOfPixels = getWidth() * getHeight();
            var bytesPerPixel = 2;
            if (_format == PixmapFormat.INTENSITY)
            {
                bytesPerPixel = 1;
            }
            else if (_format == PixmapFormat.RGB888)
            {
                bytesPerPixel = 3;
            }
            else if (_format == PixmapFormat.RGBA8888)
            {
                bytesPerPixel = 4;
            }

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

        public int getWidth()
        {
            return _pixmap.GetLength(0);
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
    }
}
