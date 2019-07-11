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
    internal class MonoGameShapeRenderer
    {
        private static Vector2 _sharedPositionVector = Vector2.Zero;
        private static Vector2 _sharedScaleVector = Vector2.One;
        private static Texture2D _sharedTexture;
        
        private readonly SpriteBatch _spriteBatch;
        private readonly GraphicsDevice _graphicsDevice;

        private Color _color;
        internal int LineHeight;

        public MonoGameShapeRenderer(GraphicsDevice graphicsDevice, MonoGameColor color, SpriteBatch spriteBatch)
        {
            _graphicsDevice = graphicsDevice;
            setColor(color);
            _spriteBatch = spriteBatch;
            if (_sharedTexture == null)
            {
                _sharedTexture = newTexture2D(1, 1);
                _sharedTexture.SetData(new[]{0xffffffff});
            }

        }

        public void setColor(MonoGameColor color)
        {
            _color = color.toMonoGameColor();
        }
        
        private void draw(Texture2D texture, Vector2 position, Color color, Vector2 scale = default(Vector2))
        {
            if (scale == default(Vector2))
            {
                scale = Vector2.One;
            }
            _spriteBatch.Draw(texture,
                position, null, color, 0f, 
                Vector2.Zero, scale, SpriteEffects.None, 0f);
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
                _sharedPositionVector.X = minX + x;
                _sharedPositionVector.Y = minY + y;
                draw(_sharedTexture, _sharedPositionVector, color: _color);
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

        public void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3)
        {
            drawLine(x1, y1, x2, y2);
            drawLine(x2, y2, x3, y3);
            drawLine(x3, y3, x1, y1);
        }

        public void drawRect(int x, int y, int width, int height)
        {
            _sharedPositionVector.X = x;
            _sharedPositionVector.Y = y;
            _sharedScaleVector.X = width;
            _sharedScaleVector.Y = 1;
            draw(_sharedTexture, _sharedPositionVector, _color, _sharedScaleVector);
            _sharedPositionVector.Y += height;
            draw(_sharedTexture, _sharedPositionVector, _color, _sharedScaleVector);
            _sharedPositionVector.X = x;
            _sharedPositionVector.Y = y;
            _sharedScaleVector.X = 1;
            _sharedScaleVector.Y = height;
            draw(_sharedTexture, _sharedPositionVector, _color, _sharedScaleVector);
            _sharedPositionVector.X += width;
            draw(_sharedTexture, _sharedPositionVector, _color, _sharedScaleVector);
        }

        public void fillRect(int x, int y, int width, int height)
        {
            _sharedPositionVector.X = x;
            _sharedPositionVector.Y = y;
            _sharedScaleVector.X = width;
            _sharedScaleVector.Y = height;
            draw(_sharedTexture, _sharedPositionVector, _color, _sharedScaleVector);
        }
        
        private void putPixel(int pixX, int pixY, Color color)
        {
            _sharedPositionVector.X += pixX;
            _sharedPositionVector.Y += pixY;
            draw(_sharedTexture, _sharedPositionVector, color: color);
            _sharedPositionVector.X -= pixX;
            _sharedPositionVector.Y -= pixY;
        }
        
        public void drawCircle(int centerX, int centerY, int radius)
        {

            _sharedPositionVector.X = centerX;
            _sharedPositionVector.Y = centerY;
            var radiusSquared = radius * radius;
            putPixel(0, radius, _color);
            putPixel(0, -radius, _color);
            putPixel(radius, 0, _color);
            putPixel(-radius, 0, _color);
            var x = 1;
            var y = (int) Math.Sqrt(radiusSquared - 1);
            while (x < y) {
                putPixel(x, y, _color);
                putPixel(x, -y, _color);
                putPixel(-x, y, _color);
                putPixel(-x, -y, _color);
                putPixel(y, x, _color);
                putPixel(y, -x, _color);
                putPixel(-y, x, _color);
                putPixel(-y, -x, _color);
                x += 1;
                y = (int) (Math.Sqrt(radiusSquared - x*x));
            }
            if (x == y) {
                putPixel(x, y, _color);
                putPixel(x, -y, _color);
                putPixel(-x, y, _color);
                putPixel(-x, -y, _color);
            }
        }

        public void fillCircle(int centerX, int centerY, int radius)
        {
            _sharedPositionVector.X = centerX - radius;
            _sharedPositionVector.Y = centerY - radius;
            for (int circleX = 0; circleX < radius * 2; circleX++)
            {
                for (int circleY = 0; circleY < radius * 2; circleY++)
                {
                    if (Math.Pow(circleX - radius, 2) + Math.Pow(circleY - radius, 2) <= Math.Pow(radius, 2))
                    {
                        draw(_sharedTexture, _sharedPositionVector, _color);
                    }
                    _sharedPositionVector.Y = _sharedPositionVector.Y + 1;
                }

                _sharedPositionVector.Y = centerY - radius;
                _sharedPositionVector.X = _sharedPositionVector.X + 1;
            }
        }

        public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3)
        {
            var xMin = Math.Min(x1, Math.Min(x2, x3));
            var yMin = Math.Min(y1, Math.Min(y2, y3));
            var xMax = Math.Max(x1, Math.Max(x2, x3));
            var yMax = Math.Max(y1, Math.Max(y2, y3));
            if (xMax - xMin == 0 || yMax - yMin == 0)
            {
                return;
            }

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
                    _sharedPositionVector.X = xMin + j;
                    _sharedPositionVector.Y = yMin + y1 + i;
                    draw(_sharedTexture, _sharedPositionVector, _color);
                }
            }
        }
    }
}