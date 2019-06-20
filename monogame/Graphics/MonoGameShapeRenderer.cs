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

namespace monogame.Graphics
{
    public class MonoGameShapeRenderer
    {
        private GraphicsDevice _graphicsDevice;
        private uint _color;
        private Vector2 _rotationCenter, _translation, _scale;
        private Color _tint;
        private readonly SpriteBatch _spriteBatch;
        private static Rectangle rect = new Rectangle();

        public MonoGameShapeRenderer(GraphicsDevice graphicsDevice, uint colorARGB8888, SpriteBatch spriteBatch, Vector2 rotationCenter, Vector2 translation, Vector2 scale, Color tint)
        {
            _graphicsDevice = graphicsDevice;
            _color = colorARGB8888;
            _spriteBatch = spriteBatch;
            _rotationCenter = rotationCenter;
            _translation = translation;
            _scale = scale;
            _tint = tint;
        }

        public void setRotationCenter(Vector2 rotationCenter)
        {
            _rotationCenter = rotationCenter;
        }

        public void setTranslation(Vector2 translation)
        {
            _translation = translation;
        }

        public void setScale(Vector2 scale)
        {
            _scale = scale;
        }

        public void setTint(Color tint)
        {
            _tint = tint;
        }

        public void setColor(uint colorARGB8888)
        {
            _color = colorARGB8888;
        }
        
        private void draw(Texture2D texture, Vector2 position)
        {
            _spriteBatch.Draw(texture,
                (position + _translation - _rotationCenter) * _scale, null, _tint, 0f, 
                Vector2.Zero, _scale, SpriteEffects.None, 0f);
        }

        private Texture2D newTexture2D(int width, int height)
        {
            return new Texture2D(_graphicsDevice, width, height, false, SurfaceFormat.Color);
        }

        public void drawLine(int x, int y, int x2, int y2)
        {
            var minX = Math.Min(x, x2);
            var minY = Math.Min(y, y2);

            x -= minX;
            x2 -= minX;
            y -= minY;
            y2 -= minY;
            
            var deltaX = x2 - x;
            var deltaY = y2 - y;
            
            var deltaXSign = Math.Sign(deltaX);
            var deltaYSign = Math.Sign(deltaY);
            
            var dx2 = deltaXSign;
            var dy2 = 0;
            
            var width = Math.Abs(deltaX);
            var height = Math.Abs(deltaY);
            
            var texture = newTexture2D(width + 1, height + 1);
            var textureData = new uint[texture.Width  * texture.Height];

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
                textureData[x + y * texture.Width] = _color;
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
            
            texture.SetData(textureData);
            draw(texture, new Vector2(minX, minY));
        }

        public void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3)
        {
            drawLine(x1, y1, x2, y2);
            drawLine(x2, y2, x3, y3);
            drawLine(x3, y3, x1, y1);
        }

        public void drawRect(int x, int y, int width, int height)
        {
            var texture = newTexture2D(width, height);
            var textureData = new uint[Math.Max(width, height)];
            for (int i = 0; i < textureData.Length; i++)
            {
                textureData[i] = _color;
            }
            rect.Height = 1;
            rect.Width = width - 1;
            texture.SetData(0, rect, textureData, 0, width - 1);
            rect.Y = height - 1;
            texture.SetData(0, rect, textureData, 0, width - 1);
            rect.Height = height - 1;
            rect.Width = 1;
            rect.Y = 0;
            texture.SetData(0, rect, textureData, 0, height - 1);
            rect.X = width - 1;
            texture.SetData(0, rect, textureData, 0, height - 1);
            rect.X = 0;
            draw(texture, new Vector2(x, y));
        }

        public void fillRect(int x, int y, int width, int height)
        {
            var texture = newTexture2D(width, height);
            var textureData = new uint[width * height];
            
            for (int i = 0; i < textureData.Length; i++)
            {
                textureData[i] = _color;
            }
            
            texture.SetData(textureData);
            draw(texture, new Vector2(x, y));
        }
        
        private static void putPixel(uint[] pixels, int width, int height, int pixX, int pixY, uint color)
        {
            if (pixX >= 0 && pixX < width && pixY >= 0 && pixY < height)
            {
                pixels[pixX + width * pixY] = color;
            }
        }
        
        public void drawCircle(int centerX, int centerY, int radius)
            {
            var texture = newTexture2D(radius * 2 + 1, radius * 2 + 1);
            var textureData = new uint[texture.Width * texture.Height];

            var radiusSquared = radius * radius;
            putPixel(textureData, texture.Width, texture.Height, radius, radius + radius, _color);
            putPixel(textureData, texture.Width, texture.Height, radius, radius - radius, _color);
            putPixel(textureData, texture.Width, texture.Height, radius + radius, radius, _color);
            putPixel(textureData, texture.Width, texture.Height, radius - radius, radius, _color);
            var x = 1;
            var y = (int) Math.Sqrt(radiusSquared - 1);
            while (x < y) {
                putPixel(textureData, texture.Width, texture.Height, radius + x, radius + y, _color);
                putPixel(textureData, texture.Width, texture.Height, radius + x, radius - y, _color);
                putPixel(textureData, texture.Width, texture.Height, radius - x, radius + y, _color);
                putPixel(textureData, texture.Width, texture.Height, radius - x, radius - y, _color);
                putPixel(textureData, texture.Width, texture.Height, radius + y, radius + x, _color);
                putPixel(textureData, texture.Width, texture.Height, radius + y, radius - x, _color);
                putPixel(textureData, texture.Width, texture.Height, radius - y, radius + x, _color);
                putPixel(textureData, texture.Width, texture.Height, radius - y, radius - x, _color);
                x += 1;
                y = (int) (Math.Sqrt(radiusSquared - x*x));
            }
            if (x == y) {
                putPixel(textureData, texture.Width, texture.Height, radius + x, radius + y, _color);
                putPixel(textureData, texture.Width, texture.Height, radius + x, radius - y, _color);
                putPixel(textureData, texture.Width, texture.Height, radius - x, radius + y, _color);
                putPixel(textureData, texture.Width, texture.Height, radius - x, radius - y, _color);
            }

            texture.SetData(textureData);
            draw(texture, new Vector2(centerX - radius, centerY - radius));
        }

        public void fillCircle(int centerX, int centerY, int radius)
        {
            var texture = newTexture2D(radius * 2, radius * 2);
            var textureData = new uint[texture.Width * texture.Height];
            
            for (int circleX = 0; circleX < radius * 2; circleX++)
            {
             
                for (int circleY = 0; circleY < radius * 2; circleY++)
                {
                    if (Math.Pow(circleX - radius, 2) + Math.Pow(circleY - radius, 2) <= Math.Pow(radius, 2))
                    {
                        textureData[circleX + texture.Width * circleY] = _color;
                    }
                }
            }

            texture.SetData(textureData);
            draw(texture, new Vector2(centerX - radius, centerY - radius));
        }

        public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3)
        {
            var xMin = Math.Min(x1, Math.Min(x2, x3));
            var yMin = Math.Min(y1, Math.Min(y2, y3));
            var xMax = Math.Max(x1, Math.Max(x2, x3));
            var yMax = Math.Max(y1, Math.Max(y2, y3));
            var texture = newTexture2D(xMax - xMin, yMax - yMin);
            var textureData = new uint[texture.Width * texture.Height];
            
            x1 -= xMin;
            x2 -= xMin;
            x3 -= xMin;
            y1 -= yMin;
            y2 -= yMin;
            y3 -= yMin;
            
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
                    textureData[j + texture.Width * (y1 + i)] = _color;
                }
            }
            
            texture.SetData(textureData);
            draw(texture, new Vector2(xMin, yMin));
        }
    }
}